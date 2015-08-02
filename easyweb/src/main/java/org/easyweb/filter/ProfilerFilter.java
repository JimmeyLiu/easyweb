package org.easyweb.filter;

import org.apache.commons.lang.StringUtils;
import org.easyweb.profiler.Profiler;
import org.easyweb.profiler.Profiler;
import org.easyweb.util.EasywebLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午11:50
 */
public class ProfilerFilter implements Filter {

    private int threshold;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String thresholdString = filterConfig.getInitParameter("threshold");

        if (thresholdString != null) {
            try {
                threshold = Integer.parseInt(thresholdString);
            } catch (NumberFormatException e) {
                threshold = 0;
            }

            if (threshold <= 0) {
                throw new ServletException(MessageFormat.format("Invalid init parameter for filter: threshold = {0}",
                        new Object[]{thresholdString}));
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
            IOException, ServletException {
        // 开始处理request, 并计时.
        String requestString = dumpRequest((HttpServletRequest) request);

        Profiler.start("process HTTP request");

        Throwable failed = null;

        try {
            chain.doFilter(request, response);
        } catch (Throwable e) {
            failed = e;
        } finally {
            Profiler.release();

            long duration = Profiler.getDuration();

            if (failed != null) {
//                if (logger.isErrorEnabled()) {
                EasywebLogger.debug(MessageFormat.format("[Profiler] Response of {0} failed in {1,number}ms: {2}\n{3}\n", requestString, duration, failed.getLocalizedMessage(), getDetail()));
//                }
            } else if (duration > threshold) {
//                if (logger.isWarnEnabled()) {
                EasywebLogger.warn(MessageFormat.format("[Profiler] Response of {0} returned in {1,number}ms\n{2}\n",
                        requestString, duration, getDetail()));
//                }
            } else {
                EasywebLogger.debug(MessageFormat.format("[Profiler] Response of {0} returned in {1,number}ms\n{2}\n",
                        requestString, duration, getDetail()));
            }

            Profiler.reset();
        }

        if (failed != null) {
            if (failed instanceof Error) {
                throw (Error) failed;
            } else if (failed instanceof RuntimeException) {
                throw (RuntimeException) failed;
            } else if (failed instanceof IOException) {
                throw (IOException) failed;
            } else if (failed instanceof ServletException) {
                throw (ServletException) failed;
            }
        }
    }

    @Override
    public void destroy() {
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
