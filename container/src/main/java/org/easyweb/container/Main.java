package org.easyweb.container;

import org.easyweb.container.handler.EasywebHandler;
import org.easyweb.request.RequestProcessor;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jimmey on 15-7-20.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("easyweb.xml");
        RequestProcessor processor = (RequestProcessor) ctx.getBean("ewRequestProcessor");
        System.setProperty("org.mortbay.util.URI.charset", "utf-8");
        Server server = new Server();
        server.addHandler(new EasywebHandler(processor));
        SocketConnector connector = new SocketConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        server.start();
    }

}
