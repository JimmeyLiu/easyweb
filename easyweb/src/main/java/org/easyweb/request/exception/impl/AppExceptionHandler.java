package org.easyweb.request.exception.impl;

import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.app.AppContainer;
import org.easyweb.request.exception.ExceptionHandler;
import org.easyweb.request.exception.ExceptionType;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jimmey on 15-8-2.
 */
public class AppExceptionHandler extends ExceptionHandler {
    public AppExceptionHandler() {
        super(ExceptionType.APP_STATUS_ERROR);
    }

    @Override
    public void handle(Exception e, HttpServletResponse response) {
        if (!Configuration.isDevMod()) {
            response.setStatus(404);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>App Error</h1><p>The app your required is not exits or status Error, Please check your easyweb.log!!</p>");
        sb.append("<h2>All Apps</h2>");
        for (App app : AppContainer.getApps()) {
            sb.append("<h3>").append(app.getName()).append("</h3>");
            sb.append(printAppInfo(app));
        }
        response(response, sb.toString());
    }
}
