package org.easyweb.filter;

import org.easyweb.Easyweb;
import org.easyweb.request.RequestProcessor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jimmey on 15-7-31.
 * 采用Filter方式启动Easyweb
 */
public class EasywebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Easyweb.initialize();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestProcessor.getInstance().process((HttpServletRequest) request, (HttpServletResponse) response);
    }

    @Override
    public void destroy() {
        Easyweb.destroy();
    }
}
