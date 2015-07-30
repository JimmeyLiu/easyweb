package org.easyweb.bean;

import org.easyweb.app.App;
import org.easyweb.app.listener.AppChangeAdapter;
import org.easyweb.context.ThreadContext;
import org.easyweb.groovy.groovyobject.EasywebClassLoader;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory extends AppChangeAdapter {


    private static Map<String, Map<String, Object>> beans = new HashMap<String, Map<String, Object>>();

    public static void register(App app, String name, Object bean) {
        /**
         * 不是easyweb classloader加载的，不加载进bean中来
         */
        ClassLoader classLoader = bean.getClass().getClassLoader();
        if (!(classLoader instanceof EasywebClassLoader)) {
            return;
        }

        EasywebClassLoader easywebClassLoader = (EasywebClassLoader) classLoader;
        if (easywebClassLoader.isWeb()) {//是web的classloader，也不注册bean
            return;
        }

        Map<String, Object> appBeans = getAppBeans(app.getAppName());
        appBeans.put(name, bean);
    }

    public static Object getBean(String name) {
//        Object obj = getAppBeans(ThreadContext.getContext().getApp().getAppName()).get(name);
//        if (obj.getClass().isAnnotationPresent(Bean.class)) {
//            Bean bean = obj.getClass().getAnnotation(Bean.class);
//            if (!bean.singleton()) {
//
//            }
//        }
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
        if (!"local".equals(appKey)) {
            appBeans.putAll(getAppBeans("local"));
        }

        return appBeans;
    }

    public static Map<String, Object> getBeans(App app) {
        String key = app == null ? "local" : app.getAppName();
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
