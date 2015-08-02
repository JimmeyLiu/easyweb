package org.easyweb.request.exception.impl;

import com.alibaba.fastjson.JSON;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.context.ThreadContext;
import org.easyweb.request.AppUriMapping;
import org.easyweb.request.exception.ExceptionHandler;
import org.easyweb.request.exception.ExceptionType;
import org.easyweb.request.uri.UriTemplate;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jimmey on 15-8-2.
 */
public class PageNotFoundHandler extends ExceptionHandler {
    public PageNotFoundHandler() {
        super(ExceptionType.PAGE_NOT_FOUND);
    }

    @Override
    public void handle(Exception e, HttpServletResponse response) {
        if (!Configuration.isDevMod()) {
            response.setStatus(404);
            return;
        }
        App app = ThreadContext.getApp();
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Page Not Found</h1><p>page not found in the App</p><h2>The App's Pages</h2><ul>");
        for (UriTemplate template : AppUriMapping.getAppUris(app).values()) {
            sb.append("<li>").append(template.getPageMethod().getHttpMethod());
            sb.append(" ").append(template.getUriTemplate());
            sb.append("</li>");
        }
        sb.append("</ul><h2>The App Info</h2>");
        sb.append(printAppInfo(app));
        response(response, sb.toString());
    }
}
