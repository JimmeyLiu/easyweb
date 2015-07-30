package org.easyweb.request.error;

import org.easyweb.Configuration;
import org.easyweb.bean.BeanFactory;
import org.easyweb.context.ThreadContext;
import org.easyweb.request.PageMethod;
import org.easyweb.request.render.CodeRender;
import org.easyweb.request.render.param.ParamBuilder;
import org.easyweb.velocity.VelocityEngine;
import org.easyweb.velocity.VmToolFactory;
import org.easyweb.app.App;
import org.easyweb.app.AppContainer;
import org.easyweb.util.EasywebLogger;
import org.easyweb.request.AppUriMapping;
import org.easyweb.request.render.LayoutRender;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午7:52
 * <p/>
 * 错误信息返回
 */
public class ErrorResponse {
    //    private static String code;
    private static List<String> messages = new LinkedList<String>();

    static {
//        try {
//            InputStream in = ErrorResponse.class.getClassLoader().getResourceAsStream("com/taobao/easyweb/core/request/error/error.vm");
//            code = IOUtils.toString(in);
//        } catch (Exception e) {
//            code = "code not found, error message is " + e.getMessage();
//        }
        messages.add("旺旺联系：<a target=\"_blank\" href=\"http://www.taobao.com/webww/ww.php?ver=3&touid=%E5%8D%95%E9%80%9A&siteid=cntaobao&status=1&charset=utf-8\"><img border=\"0\" src=\"http://amos.alicdn.com/realonline.aw?v=2&uid=%E5%8D%95%E9%80%9A&site=cntaobao&s=1&charset=utf-8\" alt=\"点击这里给我发消息\" /></a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/issues/new\">报告问题</a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/issues/new\">查看常见问题</a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/wiki/%E9%97%AE%E9%A2%98%E6%8E%92%E6%9F%A5\">查看Easyweb文档</a>");
        messages.add("感谢使用Easyweb，欢迎提出您的问题和建议！！");
    }

    private static Map<String, Map<String, PageMethod>> appErrorPages = new HashMap<String, Map<String, PageMethod>>();

    private static Map<String, PageMethod> containerErrorPages = new HashMap<String, PageMethod>();

    private static Map<String, App> containerApps = new HashMap<String, App>();

    public static void regist(App app, ErrorPage errorPage, PageMethod pageMethod) {
        String key = errorPage.error().name() + errorPage.exception();
        Map<String, PageMethod> appPages = getAppErrorPage(app);
        if (appPages.containsKey(key)) {
//            throw new RuntimeException("错误页面 " + key + "重复定义了");
            EasywebLogger.error("错误页面 " + key + "重复定义了");
        }
        if (errorPage.container()) {
            containerApps.put(key, app);
            containerErrorPages.put(key, pageMethod);
        }
        appPages.put(key, pageMethod);
    }

    public static Map<String, PageMethod> getAppErrorPage(App app) {
        Map<String, PageMethod> result = appErrorPages.get(app.getAppName());
        if (result == null) {
            result = new HashMap<String, PageMethod>();
            appErrorPages.put(app.getAppName(), result);
        }
        return result;
    }

    public static void response(ErrorType errorType, Throwable throwable) throws Exception {
        App currentApp = ThreadContext.getContext().getApp();
        /**
         * 如果请求是json，则直接返回
         */
        if (responeJson(currentApp, errorType, throwable)) {
            return;
        }
        String content;
        String key = errorType.name();
        String subKey = (throwable != null) ? key + throwable.getClass().getName() : key;

        PageMethod pageMethod = null;
        if (currentApp != null) {
            pageMethod = getAppErrorPage(currentApp).get(subKey);
            if (pageMethod == null && !subKey.equals(key)) {
                pageMethod = getAppErrorPage(currentApp).get(key);
            }
        }

        App errorApp = null;
        if (pageMethod == null) {
            if (subKey.equals(key)) {
                pageMethod = containerErrorPages.get(key);
                errorApp = containerApps.get(key);
            } else {
                pageMethod = containerErrorPages.get(subKey);
                errorApp = containerApps.get(subKey);
            }
        }

        if (pageMethod != null) {
            try {
                StringWriter writer = new StringWriter();
                CodeRender codeRender = (CodeRender) BeanFactory.getSpringBean("ewCodeRender");
                LayoutRender layoutRender = (LayoutRender) BeanFactory.getSpringBean("layoutRender");

                ErrorInfo errorInfo = getErrorInfo(errorType, throwable, null);
                ThreadContext.getContext().putContext("errorInfo", errorInfo);
                ThreadContext.getContext().setApp(errorApp == null ? currentApp : errorApp);
                codeRender.render(pageMethod.getFile(), pageMethod.getMethod().getName(), ParamBuilder.build(pageMethod.getMethod(), null, null), writer);
                content = writer.toString();
                if (StringUtils.isNotBlank(pageMethod.getLayout())) {
                    ThreadContext.getContext().setLayout(pageMethod.getLayout());
                    content = layoutRender.render(content);
                }

            } catch (Exception e) {
                //默认页面执行出错，执行错误页面
                content = defaultError(errorType, throwable, e);
            } finally {
                ThreadContext.getContext().setApp(currentApp);
            }
        } else {//没有定义，执行默认错误页面
            content = defaultError(errorType, throwable, null);
        }
        response(content);
    }

    /**
     * json请求，则直接返回失败
     *
     * @param currentApp
     * @param errorType
     * @param throwable
     * @return
     */
    private static boolean responeJson(App currentApp, ErrorType errorType, Throwable throwable) {


        return false;
    }

    private static String defaultError(ErrorType errorType, Throwable throwable, Exception errorPageException) {
        ErrorInfo errorInfo = getErrorInfo(errorType, throwable, errorPageException);
        VelocityEngine velocityEngine = (VelocityEngine) BeanFactory.getSpringBean("ewVelocityEngine");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("errorInfo", errorInfo);
        if (!Configuration.isOnline()) {
            context.put("messages", messages);
        }
        if (Configuration.isOnline()) {
            return velocityEngine.renderTemplate("org/jim/easyweb/request/error/error1.vm", PageInfo.code1, context);
        } else {
            return velocityEngine.renderTemplate("org/jim/easyweb/request/error/error.vm", PageInfo.code, context);
        }
    }

    private static ErrorInfo getErrorInfo(ErrorType errorType, Throwable throwable, Exception errorPageException) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorType(errorType.name());
        errorInfo.setThrowable(throwable);
        errorInfo.setException(stack(throwable));
        errorInfo.setErrorPageException(stack(errorPageException));
        errorInfo.setRequest(ThreadContext.getContext().getRequest().getRequestURI());
        if ("true".equals(ThreadContext.getContext().getRequest().getParameter("debug"))) {
            setDeployInfos(errorInfo);
        }

        return errorInfo;
    }

    private static void setDeployInfos(ErrorInfo errorInfo) {
        Collection<App> apps = AppContainer.getApps();
        for (App app : apps) {
            AppInfo info = new AppInfo(app);
            info.setBeans(BeanFactory.getBeans(app));
            info.setVmTools(VmToolFactory.getAppTools(app.getAppName()));
            info.setAppUris(AppUriMapping.getAppUris(app));
            errorInfo.addAppInfo(info);
        }
    }

    private static String stack(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private static void response(String content) throws Exception {
        HttpServletResponse response = ThreadContext.getContext().getResponse();
        if (response.getContentType() == null) {
            response.setContentType("text/html;charset=" + Configuration.getHttpCharset());
        }
        byte[] bytes = content.getBytes(Configuration.getHttpCharset());
        response.setContentLength(bytes.length);
        response.setHeader("Content-Language", "zh-CN");
        write(bytes, response);
    }

    protected static void write(byte[] bytes, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
