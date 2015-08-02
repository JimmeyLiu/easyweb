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
import java.util.concurrent.*;

/**
 * Created by jimmey on 15-7-31.
 * 监听文件变化自动部署
 * 1. 开发环境线程自动循环
 * 2. 正式环境需要外部发布部署命令再执行，不做自动循环
 */
public class AppMonitor implements Runnable {

    Set<String> appsRoot;
    final int intervalMS = 5000;
    ScheduledExecutorService service;
    Map<File, AppAlterationObserver> observers;
    AppDeployer deployer;
    private static AppMonitor instance;
    private boolean loop = false;

    public static AppMonitor getInstance() {
        if (instance == null) instance = new AppMonitor();
        return instance;
    }

    private AppMonitor() {
        this.appsRoot = new HashSet<String>();
        this.observers = new HashMap<File, AppAlterationObserver>();
        this.service = Executors.newSingleThreadScheduledExecutor();
        this.deployer = new AppDeployer();
    }

    public void start() {
        if (service.isShutdown()) {
            this.service = Executors.newSingleThreadScheduledExecutor();
        }
        loop = Configuration.isDevMod();
        service.submit(this);
    }

    public void stop() {
        loop = false;
    }

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            scanApps();
            for (AppAlterationObserver observer : observers.values()) {
                observer.checkAndNotify();
            }
            EasywebLogger.warn("[AppMonitor] Scan Apps used %s ms", System.currentTimeMillis() - start);
        } catch (Throwable e) {
            EasywebLogger.error("[AppMonitor] Error", e);
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

        for (String appsRoot : Configuration.getDeployPaths()) {
            File root = new File(appsRoot);
            if (root.isFile()) {
                continue;
            }
            if (parseApp(root)) {
                continue;
            }
            Configuration.setAppsRoot(appsRoot);
            File[] files = root.listFiles();
            if (files == null) {
                return;
            }

            for (File file : files) {
                parseApp(file);
            }
        }
    }

    private boolean parseApp(File file) {
        if (observers.containsKey(file)) {
            return true;
        }

        if (file.isFile()) {
            return false;
        }
        File appFile = new File(file, "app.properties");
        if (!appFile.exists()) {
            return false;
        }
        App app = parse(appFile);
        if (app == null) {
            return false;
        }
        AppAlterationListener listener = new AppAlterationListener(app, deployer);
        AppAlterationObserver observer = new AppAlterationObserver(app, file, listener);
        observers.put(file, observer);
        return true;
    }

    private static App parse(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            String name = properties.getProperty(App.APP_NAME);
            if ("easyweb".equalsIgnoreCase(name)) {
                EasywebLogger.error("[ParseAppProperties] app name should not be easyweb");
                return null;
            }
            App app = new App(name);
            String webPathStr = properties.getProperty(App.APP_WEB_PATH, "");
            List<String> webPaths = new ArrayList<String>();
            for (String v : webPathStr.split(",")) {
                if (StringUtils.isNotBlank(v)) {
                    webPaths.add(v);
                }
            }
            app.setWebPaths(webPaths);
            app.setRootPath(file);
            app.setGroovyMethodInterceptor(Boolean.valueOf(properties.getProperty(App.GROOVY_METHOD_INTERCEPTOR, "false")));
            app.setProfilerThreshold(Integer.valueOf(properties.getProperty(App.PROFILER_THRESHOLD, "2000")));
            for (Object key : properties.keySet()) {
                app.putConfig((String) key, properties.getProperty((String) key));
            }
            return app;
        } catch (Exception e) {
            EasywebLogger.error("[ParseAppProperties] " + file.getAbsolutePath() + " Error", e);
        }
        return null;
    }
}
