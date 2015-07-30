package org.easyweb.app.listener;

import org.easyweb.app.App;

import java.util.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 下午9:35
 */
public class AppChangeHolder {

    private static Map<Priority, List<AppChangeListener>> listeners;

    static {
        listeners = new HashMap<Priority, List<AppChangeListener>>();
        ServiceLoader<AppChangeListener> loader = ServiceLoader.load(AppChangeListener.class);
        Iterator<AppChangeListener> it = loader.iterator();
        while (it.hasNext()) {
            AppChangeListener listener = it.next();
            listener.init();
            Priority priority = Priority.DEFAULT;
            if (listener.getClass().isAnnotationPresent(ListenerPriority.class)) {
                priority = listener.getClass().getAnnotation(ListenerPriority.class).value();
            }
            List<AppChangeListener> l = listeners.get(priority);
            if (l == null) {
                l = new ArrayList<AppChangeListener>();
                listeners.put(priority, l);
            }
            l.add(listener);
        }
    }

    public static void stop(App app) {
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                listener.stop(app);
            }
        }
    }

    public static void success(App app) {
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                listener.success(app);
            }
        }
    }

    public static void failed(App app) {
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                listener.failed(app);
            }
        }
    }

    public static String report(App app) {
        StringBuilder sb = new StringBuilder();
        for (Priority priority : Priority.getAll()) {
            List<AppChangeListener> list = listeners.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }
            for (AppChangeListener listener : list) {
                sb.append("==============" + listener.getClass().getName() + "  report  ==============\n");
                sb.append(listener.report(app)).append("\n\n\n\n");
            }
        }
        return sb.toString();
    }

}
