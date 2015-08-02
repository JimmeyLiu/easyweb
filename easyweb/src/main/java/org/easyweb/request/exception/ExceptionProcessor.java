package org.easyweb.request.exception;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by jimmey on 15-8-2.
 */
public class ExceptionProcessor {

    static Map<ExceptionType, ExceptionHandler> handlerMap = new HashMap<ExceptionType, ExceptionHandler>();

    public static void process(ExceptionType type, Exception e, HttpServletResponse response) {
        ExceptionHandler handler = handlerMap.get(type);
        handler.handle(e, response);
    }

    public static void loadHandlers() {
        ServiceLoader<ExceptionHandler> loader = ServiceLoader.load(ExceptionHandler.class);
        Iterator<ExceptionHandler> it = loader.iterator();
        while (it.hasNext()) {
            ExceptionHandler t = it.next();
            if (!handlerMap.containsKey(t.type) ||
                    !t.getClass().getName().startsWith("org.easyweb.exception.impl")) {
                handlerMap.put(t.type, t);
            }
        }
    }
}
