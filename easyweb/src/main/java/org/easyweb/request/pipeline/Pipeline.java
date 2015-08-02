package org.easyweb.request.pipeline;

import org.easyweb.app.App;
import org.easyweb.app.listener.AppChangeAdapter;
import org.easyweb.context.ThreadContext;
import org.easyweb.util.EasywebLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 上午11:53
 */
public class Pipeline extends AppChangeAdapter {

    private static Map<String, List<ValveObject>> appValves = new ConcurrentHashMap<String, List<ValveObject>>();

    public static void addValve(final App app, ValveObject valveObject) {
        List<ValveObject> list = appValves.get(app.getName());
        if (list == null) {
            list = new ArrayList<ValveObject>();
            appValves.put(app.getName(), list);
        }
        list.add(valveObject);
        EasywebLogger.warn("[Pipeline] [%s] addValve %s", app.getName(), valveObject.groovyObject);
        Collections.sort(list, new Comparator<ValveObject>() {
            @Override
            public int compare(ValveObject o1, ValveObject o2) {
                int i = o1.order() - o2.order();
                if (i == 0) {
                    throw new RuntimeException(String.format("[Pipeline] [%s] valve order same %s %s", app.getName(), o1.groovyObject, o2.groovyObject));
                }
                return i;
            }
        });
    }

    public static void invoke() {
        App app = ThreadContext.getContext().getApp();
        if (app == null) {
            return;
        }
        List<ValveObject> list = appValves.get(app.getName());
        if (list == null) {
            return;
        }
        for (ValveObject valve : list) {
            valve.invoke();
        }
    }

    @Override
    public void stop(App app) {
        appValves.remove(app.getName());
    }

}
