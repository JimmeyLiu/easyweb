package org.easyweb.container;

import org.easyweb.Easyweb;
import org.easyweb.container.handler.EasywebHandler;
import org.easyweb.request.RequestProcessor;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;

/**
 * Created by jimmey on 15-7-20.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("easyweb.deployPath", "/Users/jimmey/workspace/platform/easyweb/container");
        System.setProperty("easyweb.env", "dev");
        System.setProperty("org.mortbay.util.URI.charset", "utf-8");

        Easyweb.initialize();

//        new ClassPathXmlApplicationContext("easyweb.xml");

        Server server = new Server();
        server.addHandler(new EasywebHandler(RequestProcessor.getInstance()));
        SocketConnector connector = new SocketConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        server.start();
    }

}
