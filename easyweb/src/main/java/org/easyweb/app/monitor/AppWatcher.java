package org.easyweb.app.monitor;

import org.apache.commons.lang.StringUtils;
import org.easyweb.Configuration;
import org.easyweb.app.App;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.monitor.impl.FileAlterationMonitor;
import org.easyweb.util.EasywebLogger;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by jimmey on 15-7-30.
 */
public class AppWatcher {

    FileAlterationMonitor monitor;
    AppDeployer appDeployer;

    public AppWatcher() {
        monitor = new FileAlterationMonitor(Configuration.isDevMod() ? 1000 : 10000);
        this.appDeployer = new AppDeployer();
    }

    public void addObserver(AppObserver observer) {
        monitor.addObserver(observer);
    }

    private void initScan() {
        File root = new File(Configuration.getDeployPath());
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
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
            addObserver(new AppObserver(app, appDeployer));
        }
    }

    private static App parse(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            App app = new App();
            app.setName(properties.getProperty(App.APP_NAME));
            String webPathStr = properties.getProperty(App.APP_WEB_PATH, "");
            List<String> webPaths = new ArrayList<String>();
            for (String v : webPathStr.split(",")) {
                if (StringUtils.isNotBlank(v)) {
                    webPaths.add(v);
                }
            }
            app.setWebPaths(webPaths);
            app.setRootPath(file);
            app.setDomain(properties.getProperty(App.APP_DOMAIN));
            app.setVelocityNoEscape(properties.getProperty(App.VELOCITY_NO_ESCAPE));
            return app;
        } catch (Exception e) {
            EasywebLogger.error("Parse " + file.getAbsolutePath() + " Error", e);
        }
        return null;
    }

    Thread first = new Thread(new Runnable() {
        @Override
        public void run() {
            initScan();
        }
    });

    public void start() {
        try {
            monitor.start();
            first.start();
        } catch (Exception e) {
            throw new RuntimeException("FileAlterationMonitor Start Error", e);
        }
    }
}
