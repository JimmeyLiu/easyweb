package org.easyweb.app.monitor;

import org.apache.commons.lang.StringUtils;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.listener.AppChangeHolder;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by jimmey on 15-7-31.
 */
public class AppMonitor implements Runnable {

    Set<String> appsRoot;
    int intervalMS;
    ScheduledExecutorService service;
    Map<File, AppAlterationObserver> observers;
    AppDeployer deployer;
    private static AppMonitor instance;
    private boolean loop = true;

    public static AppMonitor getInstance() {
        if (instance == null) instance = new AppMonitor();
        return instance;
    }

    private AppMonitor() {
        this.appsRoot = new HashSet<String>();
        this.observers = new HashMap<File, AppAlterationObserver>();
        this.intervalMS = Configuration.isDevMod() ? 1000 : 10000;
        this.service = Executors.newSingleThreadScheduledExecutor();
        this.deployer = new AppDeployer();
    }

    public void start() {
        if (service.isShutdown()) {
            this.service = Executors.newSingleThreadScheduledExecutor();
        }
        loop = true;
        service.submit(this);
    }

    public void stop() {
        loop = false;
    }

    @Override
    public void run() {
        try {
            scanApps();
            for (AppAlterationObserver observer : observers.values()) {
                observer.checkAndNotify();
            }
        } catch (Throwable e) {
            EasywebLogger.error("AppFileMonitor Error", e);
        } finally {
            if (loop) {
                service.schedule(this, intervalMS, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * 应用新增、删除处理
     */
    private void scanApps() {
        for (File file : new ArrayList<File>(observers.keySet())) {
            if (!file.exists()) {
                AppAlterationObserver observer = observers.remove(file);
                AppChangeHolder.stop(observer.getApp());
            }
        }
        File root = new File(Configuration.getDeployPath());
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() || observers.containsKey(file)) {
                continue;
            }
            File appFile = new File(file, "app.properties");
            if (!appFile.exists()) {
                continue;
            }
            App app = parse(appFile);
            if (app == null) {
                continue;
            }
            AppAlterationListener listener = new AppAlterationListener(app, deployer);
            AppAlterationObserver observer = new AppAlterationObserver(app, file, listener);
            observers.put(file, observer);
        }
    }

    private static App parse(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            App app = new App(properties.getProperty(App.APP_NAME));
            String webPathStr = properties.getProperty(App.APP_WEB_PATH, "");
            List<String> webPaths = new ArrayList<String>();
            for (String v : webPathStr.split(",")) {
                if (StringUtils.isNotBlank(v)) {
                    webPaths.add(v);
                }
            }
            app.setWebPaths(webPaths);
            app.setRootPath(file);
            for (Object key : properties.keySet()) {
                app.putConfig((String) key, properties.getProperty((String) key));
            }
            return app;
        } catch (Exception e) {
            EasywebLogger.error("Parse " + file.getAbsolutePath() + " Error", e);
        }
        return null;
    }
}
