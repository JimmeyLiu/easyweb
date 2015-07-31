package org.easyweb.container;

import org.easyweb.Easyweb;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EasywebHandler extends AbstractHandler {


    public EasywebHandler() {
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
        try {
            Easyweb.process(request, response);
        } catch (Exception e) {

        }
    }
}
