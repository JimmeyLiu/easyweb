package org.easyweb.request.pipeline;

import org.easyweb.app.App;
import org.easyweb.app.change.AppChangeAdapter;
import org.easyweb.context.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 上午11:53
 */
public class Pipeline extends AppChangeAdapter {

    private static Map<String, List<Valve>> appValves = new ConcurrentHashMap<String, List<Valve>>();

    public static void initPipeline(App app, List<Valve> valves) {
        appValves.put(app.getAppKey(), valves);
    }

    public static void invoke(Context context) throws Exception {
        List<Valve> valves = appValves.get(context.getApp().getAppKey());
        if (valves == null || valves.isEmpty()) {
            return;
        }
        for (Valve valve : valves) {
            if (context.isBreakPipeline()) {
                return;
            }
            valve.invoke(context);
        }
    }

    @Override
    public void stop(App app) {
        appValves.remove(app.getAppKey());
    }

}
