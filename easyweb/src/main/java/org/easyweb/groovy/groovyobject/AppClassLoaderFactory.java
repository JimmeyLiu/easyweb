package org.easyweb.groovy.groovyobject;

import org.easyweb.app.change.ListenerPriority;
import org.easyweb.app.App;
import org.easyweb.app.change.AppChangeAdapter;
import org.easyweb.app.change.ListenerPriority;
import org.easyweb.app.change.Priority;
import groovy.lang.GroovyClassLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 上午12:03
 */
@ListenerPriority(Priority.LOW)
public class AppClassLoaderFactory extends AppChangeAdapter {

    private static Map<String, AppGroovyClassLoader> appGroovyClassLoaderMap = new ConcurrentHashMap<String, AppGroovyClassLoader>();

    public static AppGroovyClassLoader getAppClassLoader(App app) {
        AppGroovyClassLoader classLoader = appGroovyClassLoaderMap.get(app.getAppKey());
        if (classLoader == null) {
            classLoader = new AppGroovyClassLoader(app);
            appGroovyClassLoaderMap.put(app.getAppKey(), classLoader);
        }
        return classLoader;
    }

    /**
     * 更换app的classloader，用于开发和日常模式
     *
     * @param app
     */
    public static void reset(App app) {
        appGroovyClassLoaderMap.remove(app.getAppKey());
        appGroovyClassLoaderMap.put(app.getAppKey(), new AppGroovyClassLoader(app));
    }

    public static GroovyClassLoader getAppBizClassLoader(App app) {
        return getAppClassLoader(app).getBizClassLoader();
    }

    public static GroovyClassLoader getAppWebClassLoader(App app) {
        return getAppClassLoader(app).getWebClassLoader();
    }

    /**
     * 应用停止也更换classloader
     *
     * @param app
     */
    @Override
    public void stop(App app) {
        reset(app);
    }

}
