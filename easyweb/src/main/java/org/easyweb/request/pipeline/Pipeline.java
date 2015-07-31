package org.easyweb.request.pipeline;

import org.easyweb.app.App;
import org.easyweb.app.listener.AppChangeAdapter;
import org.easyweb.context.Context;
import org.easyweb.context.ThreadContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 上午11:53
 */
public class Pipeline extends AppChangeAdapter {

    private static Map<String, List<ValveObject>> appValves = new ConcurrentHashMap<String, List<ValveObject>>();

    public static void addValve(App app, ValveObject valveObject) {
        List<ValveObject> list = appValves.get(app.getAppName());
        if (list == null) {
            list = new ArrayList<ValveObject>();
            appValves.put(app.getAppName(), list);
        }
        list.add(valveObject);
        Collections.sort(list, new Comparator<ValveObject>() {
            @Override
            public int compare(ValveObject o1, ValveObject o2) {
                return o1.order() > o2.order() ? 1 : -1;
            }
        });
    }

    public static void invoke() {
        App app = ThreadContext.getContext().getApp();
        if (app == null) {
            return;
        }
        List<ValveObject> list = appValves.get(app.getAppName());
        if (list == null) {
            return;
        }
        for (ValveObject valve : list) {
            valve.invoke();
        }
    }

    @Override
    public void stop(App app) {
        appValves.remove(app.getAppName());
    }

}
