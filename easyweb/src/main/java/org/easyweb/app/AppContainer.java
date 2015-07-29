package org.easyweb.app;

import org.easyweb.app.change.AppChangeAdapter;

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
        app.setStatus(0);
        deployedApps.putIfAbsent(app.getAppKey(), app);
    }

    @Override
    public void success(App appInfo) {
        appInfo.setStatus(1);
        deployedApps.putIfAbsent(appInfo.getAppKey(), appInfo);
    }

    @Override
    public void failed(App app) {
        app.setStatus(2);
        deployedApps.putIfAbsent(app.getAppKey(), app);
    }

    @Override
    public String report(App app) {
        return null;
    }
}
