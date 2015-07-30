package org.easyweb.app;

import org.easyweb.app.listener.AppChangeAdapter;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class AppContainer extends AppChangeAdapter {

    private static ConcurrentHashMap<String, App> deployedApps = new ConcurrentHashMap<String, App>();

    public static App getApp(String appName) {
        return deployedApps.get(appName);
    }


    public static Collection<App> getApps() {
        return deployedApps.values();
    }

    @Override
    public void stop(App app) {
        app.setStatus(AppStatus.ERROR);
        deployedApps.putIfAbsent(app.getAppName(), app);
    }

    @Override
    public void success(App app) {
        app.setStatus(AppStatus.OK);
        deployedApps.putIfAbsent(app.getAppName(), app);
    }

    @Override
    public void failed(App app) {
        app.setStatus(AppStatus.ERROR);
        deployedApps.putIfAbsent(app.getAppName(), app);
    }

    @Override
    public String report(App app) {
        return null;
    }
}
