package org.easyweb.container;

import org.easyweb.Easyweb;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jimmey on 15-7-20.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("easyweb.env", "dev");
        System.setProperty("org.mortbay.util.URI.charset", "utf-8");
        Map<String, Object> beans = new HashMap<String, Object>();
        Easyweb.initialize(beans);

        Server server = new Server();
        server.addHandler(new EasywebHandler());
        SocketConnector connector = new SocketConnector();
        connector.setPort(Integer.getInteger("port", 8080));
        server.setConnectors(new Connector[]{connector});
        server.start();
    }

}
