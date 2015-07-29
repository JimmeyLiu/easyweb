package org.easyweb.request.debug;

import org.apache.commons.io.IOUtils;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.app.AppContainer;
import org.easyweb.bean.BeanFactory;
import org.easyweb.context.ThreadContext;
import org.easyweb.request.error.AppInfo;
import org.easyweb.velocity.VelocityEngine;
import org.easyweb.velocity.VmToolFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 上午11:21
 */
@Component
public class DebugHelper {

    private static String code;
    private static List<String> messages = new LinkedList<String>();

    static {
        try {
            InputStream in = DebugHelper.class.getClassLoader().getResourceAsStream("org/easyweb/request/debug/debug.vm");
            code = IOUtils.toString(in);
        } catch (Exception e) {
            code = "code not found, error message is " + e.getMessage();
        }
        messages.add("旺旺联系：<a target=\"_blank\" href=\"http://www.taobao.com/webww/ww.php?ver=3&touid=%E5%8D%95%E9%80%9A&siteid=cntaobao&status=1&charset=utf-8\"><img border=\"0\" src=\"http://amos.alicdn.com/realonline.aw?v=2&uid=%E5%8D%95%E9%80%9A&site=cntaobao&s=1&charset=utf-8\" alt=\"点击这里给我发消息\" /></a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/issues/new\">报告问题</a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/issues/new\">查看常见问题</a>");
        messages.add("<a target=\"_blank\"  href=\"https://github.com/jimgithub/easyweb/wiki/%E9%97%AE%E9%A2%98%E6%8E%92%E6%9F%A5\">查看Easyweb文档</a>");
        messages.add("感谢使用Easyweb，欢迎提出您的问题和建议！！");
    }

    @Resource
    private VelocityEngine velocityEngine;

    public String pageNotFount(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
//        Map<String, PageMethod> pages = RequestMapping.getAppPages();
        String msg = "页面 【" + request.getRequestURI() + "】 没有配置";
//        if (pages != null) {
//            for (String url : pages.keySet()) {
//                if (url.endsWith(request.getRequestURI())) {
//                    PageMethod pageMethod = pages.get(url);
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("存在页面【" + url + "】<br>");
//                    sb.append("请确定在【").append(pageMethod.getFile().getAbsolutePath());
//                    sb.append("】文件中的【").append(pageMethod.getMethod()).append("】方法的@Page注解配置正确<br>");
//                    sb.append("<b style='color:red'>是否method配置不正确</b><br>");
//                    sb.append("期待的地址为【").append(url).append("】但实际为【").append(RequestMapping.getUrl(request)).append("】");
//                    msg = sb.toString();
//                }
//            }
//        }
        debugInfo.addErrorInfo(new Info("错误类型", "页面不存在"));
        debugInfo.addErrorInfo(new Info("原因说明", msg));
        return render(debugInfo);
    }

    public String pipelineException(HttpServletRequest request, Throwable e) {
        DebugInfo debugInfo = getCommonInfo(request);
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        debugInfo.addErrorInfo(new Info("错误类型", "pipeline执行一次"));
        debugInfo.addErrorInfo(new Info("错误信息", "请求 " + request.getRequestURL() + " 处理异常"));
        debugInfo.addErrorInfo(new Info("详细堆栈", writer.toString(), 1));
        return render(debugInfo);
    }

//    public String serverError(HttpServletRequest request, Throwable e) {
//        DebugInfo debugInfo = getCommonInfo(request);
//        StringWriter writer = new StringWriter();
//        PageMethod method = RequestMapping.getPageMthod(request);
//        e.printStackTrace(new PrintWriter(writer));
//        debugInfo.addErrorInfo(new Info("错误类型", "服务端错误"));
//        debugInfo.addErrorInfo(new Info("错误信息", "请求 " + request.getRequestURL() + " 处理异常"));
//        debugInfo.addErrorInfo(new Info("groovy文件", method.getFile().getAbsolutePath()));
//        debugInfo.addErrorInfo(new Info("执行的方法", method.getMethod().getName()));
//        debugInfo.addErrorInfo(new Info("详细堆栈", writer.toString(), 1));
//        return render(debugInfo);
//    }

    public String appStarting(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("错误类型", "应用正在启动中"));
        return render(debugInfo);
    }

    public String deployError(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("错误类型", "应用启动失败"));
        return render(debugInfo);
    }

    public String appNotExist(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("错误类型", "应用不存在"));
        return render(debugInfo);
    }

    public String appRemove(HttpServletRequest request) {
        DebugInfo debugInfo = getCommonInfo(request);
        debugInfo.addErrorInfo(new Info("错误类型", "应用被下线（删除）"));
        return render(debugInfo);
    }

    private DebugInfo getCommonInfo(HttpServletRequest request) {
        DebugInfo debugInfo = new DebugInfo(request);
        debugInfo.setAppInfos(deployInfo());
        return debugInfo;
    }

    private List<AppInfo> deployInfo() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        Collection<App> apps = AppContainer.getApps();
        for (App app : apps) {
            AppInfo info = new AppInfo(app);
            info.setBeans(BeanFactory.getBeans(app));
            info.setVmTools(VmToolFactory.getAppTools(app.getAppKey()));
//            info.setPages(RequestMapping.getAppPages(app.getAppKey()));//fixme
            list.add(info);
        }
        return list;
    }

    private String render(DebugInfo info) {
        if (Configuration.isOnline() && ThreadContext.getContext().getRequest().getParameter("debug") == null) {
            //log
            return "<!-- error -->";
        }
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("debugInfo", info);
        context.put("messages", messages);
        return velocityEngine.renderTemplate("org/easyweb/request/debug/debug.vm", code, context);
    }

}
