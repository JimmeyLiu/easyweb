package org.easyweb.container.handler;

import org.easyweb.request.RequestProcessor;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EasywebHandler extends AbstractHandler {

    RequestProcessor processor;

    public EasywebHandler(RequestProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
        processor.process(request, response);
    }
}
