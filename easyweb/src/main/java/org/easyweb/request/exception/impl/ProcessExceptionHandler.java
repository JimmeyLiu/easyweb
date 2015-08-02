package org.easyweb.request.exception.impl;

import org.easyweb.Configuration;
import org.easyweb.request.exception.ExceptionHandler;
import org.easyweb.request.exception.ExceptionType;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by jimmey on 15-8-2.
 * 请求处理报错，通常是groovy执行错误或业务抛出的错误
 */
public class ProcessExceptionHandler extends ExceptionHandler {

    public ProcessExceptionHandler() {
        super(ExceptionType.PROCESS_EXCEPTION);
    }

    @Override
    public void handle(Exception e, HttpServletResponse response) {
        if (!Configuration.isDevMod()) {
            response.setStatus(500);
            return;
        }
        StringWriter writer = new StringWriter();
        writer.write("<h1>Page Process Exception</h1>");
        writer.write("<pre>");
        e.printStackTrace(new PrintWriter(writer));
        writer.write("</pre>");
        response(response, writer.toString());
    }
}
