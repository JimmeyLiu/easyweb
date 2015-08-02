package org.easyweb.app;

import org.easyweb.app.listener.AppChangeAdapter;
import org.easyweb.util.EasywebLogger;

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
        EasywebLogger.warn("[AppContainer] [%s] stop", app.getName());
        app.setStatus(AppStatus.STOP);
        deployedApps.putIfAbsent(app.getName(), app);
    }

    @Override
    public void success(App app) {
        EasywebLogger.warn("[AppContainer] [%s] start success", app.getName());
        app.setStatus(AppStatus.OK);
        deployedApps.putIfAbsent(app.getName(), app);
    }

    @Override
    public void failed(App app) {
        EasywebLogger.warn("[AppContainer] [%s] start error", app.getName());
        app.setStatus(AppStatus.ERROR);
        deployedApps.putIfAbsent(app.getName(), app);
    }

}
