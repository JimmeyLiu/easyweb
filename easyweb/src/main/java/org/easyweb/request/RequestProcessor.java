package org.easyweb.request;

import org.apache.commons.lang.StringUtils;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.profiler.Profiler;
import org.easyweb.request.assets.AssetsProcessor;
import org.easyweb.request.error.ErrorResponse;
import org.easyweb.request.error.ErrorType;
import org.easyweb.request.pipeline.Pipeline;
import org.easyweb.request.render.CodeRender;
import org.easyweb.request.render.LayoutRender;
import org.easyweb.request.render.PageRender;
import org.easyweb.request.uri.UriTemplate;
import org.easyweb.util.EasywebLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

@Component("ewRequestProcessor")
public class RequestProcessor {

    private static String defaultCharset = Configuration.getHttpCharset();
    @Resource(name = "ewCodeRender")
    private CodeRender codeRender;
    @Resource
    private PageRender pageRender;
    @Resource
    private LayoutRender layoutRender;

    public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        process(request, response, false);
    }


    public void process(HttpServletRequest request, HttpServletResponse response, boolean contextInit) throws Exception {
        process(request, response, contextInit, null);
    }

    /**
     * 对一个请求做处理
     *
     * @param request
     * @param response
     * @param contextInit 是否已经初始化
     * @param inputParams 外部输出的参数，可以为null
     * @return
     * @throws IOException
     */
    public void process(HttpServletRequest request, HttpServletResponse response, boolean contextInit, Map<String, Object> inputParams) throws Exception {
        Throwable failed = null;
        try {
            Profiler.start("process HTTP request");
            if (!contextInit) {
                Profiler.enter("Init ThreadContext");
                ThreadContext.init(request, response);
                Profiler.release();
            }
            initMDC();
            Context context = ThreadContext.getContext();
            App app = context.getApp();

            ErrorType errorType = null;
            Throwable throwable = null;
            if (app == null) {
                errorType = ErrorType.APP_NOT_EXIST;
            } else if (app.getStatus() != 1) {
                errorType = ErrorType.APP_STATUS_ERROR;
            }

            if (errorType != null) {
                ErrorResponse.response(errorType, throwable);
                return;
            }

            //assets文件访问
            if (AssetsProcessor.process(request, response, app)) {
                return;
            }

            /**
             * 先执行pipeline的逻辑代码
             */
            boolean pipeline = true;
            try {
                Profiler.enter("start pipeline");
                Pipeline.invoke(context);
                pipeline = !context.isBreakPipeline();
            } catch (Exception e) {
                throwable = e;
                errorType = ErrorType.PIPELINE_ERROR;
                pipeline = false;
            } finally {
                Profiler.release();
            }

            /**
             * 如果pipeline进行了redirect,则直接redirect,后面逻辑不再执行
             */
            String redirect = context.getRedirectTo();
            if (StringUtils.isNotBlank(redirect)) {
                response.sendRedirect(redirect);
                return;
            }

            /**
             * 如果pipeline break了，那么后面的页面渲染也不执行，二是执行
             */
            if (pipeline) {
                try {
                    Profiler.enter("process page");
                    errorType = processPage(app, request, response, inputParams);
                } catch (SecurityException e) {//security exception throw out
                    throw e;
                } catch (Throwable e) {
                    throwable = e;
                    EasywebLogger.error("page render error", e);
                    errorType = ErrorType.RENDER_EXCEPTION;
                } finally {
                    Profiler.release();
                }
            }

            if (errorType != null) {
                ErrorResponse.response(errorType, throwable);
                return;
            }
            if (!response.isCommitted()) {
                //正常情况下response是已经commit了的
            }
        } catch (Exception e) {
            failed = e;
            EasywebLogger.error("request error", e);
            throw e;
        } finally {
            //直接这里clean ThreadLocal的缓存
            ThreadContext.clean();
            Profiler.release();
            String requestString = dumpRequest(request);
            long duration = Profiler.getDuration();
            int threshold = Configuration.getProfilerThreshold();
            if (failed != null) {
                EasywebLogger.error(MessageFormat.format("Response of {0} failed in {1,number}ms: {2}\n{3}\n",
                        requestString, duration, failed.getLocalizedMessage(), getDetail()));
            } else if (duration > threshold) {
                EasywebLogger.warn(MessageFormat.format("Response of {0} returned in {1,number}ms\n{2}\n", requestString, duration, getDetail()));
            } else {
                EasywebLogger.info(MessageFormat.format("Response of {0} returned in {1,number}ms\n{2}\n",
                        requestString, duration, getDetail()));
            }

            Profiler.reset();
        }

    }

    private ErrorType processPage(App app, HttpServletRequest request, HttpServletResponse response, Map<String, Object> inputParams) throws Throwable {
        String uri = request.getRequestURI();
        Context context = ThreadContext.getContext();
        if (context.getForwardTo() != null) {
            uri = context.getForwardTo();
        }
        UriTemplate uriTemplate = AppUriMapping.getUriTemplate(app, uri, request.getMethod());
        if (uriTemplate == null) {
            return ErrorType.PAGE_NOT_FOUND;
        } else {
            PageMethod pageMethod = uriTemplate.getPageMethod();
            if (StringUtils.isNotBlank(pageMethod.getLayout())) {
                ThreadContext.getContext().setLayout(pageMethod.getLayout());
            }
            String content = pageRender.render(uriTemplate, inputParams);
            content = layoutRender.render(content);
            response(response, content);
        }
        return null;
    }

    private void response(HttpServletResponse response, String content) throws Exception {
        Context context = ThreadContext.getContext();
        String redirect = context.getRedirectTo();
        if (StringUtils.isNotBlank(redirect)) {
            response.sendRedirect(redirect);
        } else if (!context.isDownload()) {
            if (response.getContentType() == null) {
                response.setContentType("text/html;charset=" + defaultCharset);
            }
            byte[] bytes = content.getBytes(defaultCharset);
            write(bytes, response);
        }
    }

    private void write(byte[] bytes, HttpServletResponse response) throws IOException {
        response.setContentLength(bytes.length);
        response.setHeader("Content-Language", "zh-CN");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private void initMDC() {
        Context context = ThreadContext.getContext();
        EasywebLogger.initMDC("requestURI", context.getRequest().getRequestURL().toString());
        EasywebLogger.initMDC("app", context.getAppName());
    }

    private String dumpRequest(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer(request.getMethod());

        buffer.append(" ").append(request.getRequestURI());

        String queryString = StringUtils.trimToNull(request.getQueryString());

        if (queryString != null) {
            buffer.append("?").append(queryString);
        }

        return buffer.toString();
    }

    private String getDetail() {
        return Profiler.dump("Detail: ", "        ");
    }

}
