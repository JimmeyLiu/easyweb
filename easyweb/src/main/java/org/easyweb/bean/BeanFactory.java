package org.easyweb.bean;

import org.easyweb.app.App;
import org.easyweb.app.listener.AppChangeAdapter;
import org.easyweb.context.ThreadContext;
import org.easyweb.groovy.groovyobject.EasywebClassLoader;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory extends AppChangeAdapter {

    private static final String NATIVE = "NATIVE";
    private static Map<String, Map<String, Object>> beans = new HashMap<String, Map<String, Object>>();

    public static void registerNativeBeans(Map<String, Object> beans) {
        getAppBeans(NATIVE).putAll(beans);
    }

    public static void register(App app, String name, Object bean) {
        /**
         * 不是easyweb classloader加载的，不加载进bean中来
         */
        ClassLoader classLoader = bean.getClass().getClassLoader();
        if (classLoader instanceof EasywebClassLoader && ((EasywebClassLoader) classLoader).isWeb()) {
            return;
        }
        Map<String, Object> appBeans = getAppBeans(app.getAppName());
        appBeans.put(name, bean);
    }

    public static Object getBean(String name) {
        return getAppBeans(ThreadContext.getContext().getApp().getAppName()).get(name);
    }

    public static Object getAppBean(String appKey, String beanName) {
        Map<String, Object> appBeans = getAppBeans(appKey);
        return appBeans.get(beanName);
    }

    private static Map<String, Object> getAppBeans(String appKey) {
        Map<String, Object> appBeans = beans.get(appKey);
        if (appBeans == null) {
            appBeans = new HashMap<String, Object>();
            beans.put(appKey, appBeans);
        }
        if (!NATIVE.equals(appKey)) {
            appBeans.putAll(getAppBeans(NATIVE));
        }
        return appBeans;
    }

    public static Map<String, Object> getBeans(App app) {
        String key = app == null ? NATIVE : app.getAppName();
        return getAppBeans(key);
    }

    @Override
    public void stop(App app) {
        beans.remove(app.getAppName());
    }

    @Override
    public void failed(App app) {
        beans.remove(app.getAppName());
    }

}
