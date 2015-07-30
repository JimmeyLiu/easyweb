package org.easyweb.request;

import org.apache.commons.lang.StringUtils;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;
import org.easyweb.profiler.Profiler;
import org.easyweb.request.assets.AssetsProcessor;
import org.easyweb.request.pipeline.Pipeline;
import org.easyweb.request.render.CodeRender;
import org.easyweb.request.uri.UriTemplate;
import org.easyweb.util.EasywebLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

public class RequestProcessor {

    private static String defaultCharset = Configuration.getRequestCharset();
    private static RequestProcessor instance;

    public static RequestProcessor getInstance() {
        if (instance == null) {
            instance = new RequestProcessor();
        }
        return instance;
    }

    private RequestProcessor() {
    }

    public void process(HttpServletRequest request, HttpServletResponse response) {
        process(request, response, false);
    }

    public void process(HttpServletRequest request, HttpServletResponse response, boolean contextInit) {
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
    public void process(HttpServletRequest request, HttpServletResponse response, boolean contextInit, Map<String, Object> inputParams) {
        try {
            Profiler.start("process HTTP request");
            if (!contextInit) {
                ThreadContext.init(request, response);
            }
            initMDC();
            Context context = ThreadContext.getContext();
            App app = context.getApp();
            if (app == null) {
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                return;
            }

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
                    processPage(app, request, response, inputParams);
                } finally {
                    Profiler.release();
                }
            }

            if (!response.isCommitted()) {
                //正常情况下response是已经commit了的
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            EasywebLogger.error(String.format("Handler %s Error", request.getRequestURI()), e);
        } finally {
            //直接这里clean ThreadLocal的缓存
            ThreadContext.clean();
            Profiler.release();
            String requestString = dumpRequest(request);
            long duration = Profiler.getDuration();
            int threshold = Configuration.getProfilerThreshold();
            if (duration > threshold) {
                EasywebLogger.warn(MessageFormat.format("Response of {0} returned in {1,number}ms\n{2}\n", requestString, duration, getDetail()));
            } else {
                EasywebLogger.debug(MessageFormat.format("Response of {0} returned in {1,number}ms\n{2}\n",
                        requestString, duration, getDetail()));
            }

            Profiler.reset();
        }

    }

    private void processPage(App app, HttpServletRequest request, HttpServletResponse response, Map<String, Object> inputParams) throws Exception {
        String uri = request.getRequestURI();
        Context context = ThreadContext.getContext();
        if (context.getForwardTo() != null) {
            uri = context.getForwardTo();
        }
        UriTemplate uriTemplate = AppUriMapping.getUriTemplate(app, uri, request.getMethod());
        if (uriTemplate == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            PageMethod pageMethod = uriTemplate.getPageMethod();
            if (StringUtils.isNotBlank(pageMethod.getLayout())) {
                ThreadContext.getContext().setLayout(pageMethod.getLayout());
            }
            String content = CodeRender.getInstance().renderPage(uriTemplate, inputParams);
            response(response, content);
        }
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
