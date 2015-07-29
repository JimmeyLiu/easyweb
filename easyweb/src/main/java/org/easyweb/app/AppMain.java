package org.easyweb.app;

import org.easyweb.Configuration;
import org.easyweb.util.EasywebLogger;
import org.easyweb.app.change.AppChangeHolder;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.scanner.AppScanner;
import org.easyweb.app.scanner.ScanResult;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午3:06
 * <p/>
 * 开发环境使用这个类
 */

@Component
public class AppMain implements ApplicationListener {
    static Map<String, Long> modified = new ConcurrentHashMap<String, Long>();
    private static Map<String, DeployStatus> deployStatusMap = new ConcurrentHashMap<String, DeployStatus>();
    @Resource
    GroovyObjectLoader groovyObjectLoader;
    FileFilter folder = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.getName().startsWith(".") && !pathname.getName().equals("target");
        }
    };
    Thread scanThread = new Thread(new Runnable() {
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }

            while (true) {
                try {
                    scan(System.currentTimeMillis());
                    Thread.sleep(10000);
                } catch (Throwable e) {
                    EasywebLogger.error("deploy error", e);
                }
            }
        }
    });
    @Resource
    private AppDeployer appDeployer;
    private String deployPath = Configuration.getDeployPath();
    private Map<String, App> appConfigs = new HashMap<String, App>();

    private static boolean isModified(File file) {
        String name = file.getAbsolutePath();
        long last = file.lastModified();
        Long l = modified.get(name);
        if (l == null || last != l.longValue()) {
            modified.put(name, last);
            return true;
        }
        return false;
    }

    public void scan(long startTime) throws Exception {
        File file = new File(deployPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<String> ignoreList = new ArrayList<String>();
        File ignore = new File(file.getAbsolutePath() + "/ignore.ini");
        if (ignore.exists()) {
            try {
                ignoreList = IOUtils.readLines(new FileInputStream(ignore));
            } catch (Exception e) {
            }
        }

        File[] apps = file.listFiles(folder);
        for (File app : apps) {
            if (ignoreList.contains(app.getName())) {
                EasywebLogger.error("ignored " + app.getName());
                continue;
            }
            scanApp(app, startTime);
        }
    }

    private void scanApp(File appFile, long startTime) {
        // 解析app的根目录
        File config = new File(appFile.getPath() + "/app.properties");
        if (!config.exists()) {
            return;
        }
        App app = AppParser.parse(config);
        if (app == null || app.getName() == null) {
            return;
        }
        DeployStatus deployStatus = deployStatusMap.get(app.getAppKey());

        if (deployStatus != null) {//没有部署记录，需要扫描
            if (deployStatus.getStatus() == 2 || deployStatus.getStatus() == 0) {//部署中，直接返回
                return;
            }
        } else {
            deployStatus = new DeployStatus();
            deployStatusMap.put(app.getAppKey(), deployStatus);
        }

        ScanResult scanResult = AppScanner.getInstance().scan(app);

        if (deployStatus.getStatus() == 1) {//部署成功状态的
            //如果biz没有变化，则处理web变化后直接返回
            if (scanResult.getModifiedBizGroovy().isEmpty()) {
                processWebChange(scanResult);
                return;
            }
        } else if (deployStatus.getStatus() == 2) {//部署中的
            //部署中状态的，直接返回，避免重复部署
            return;
        } else if (deployStatus.getStatus() == 3) {//部署失败的
            //部署失败的app，同时biz又没有变化，则直接返回
            if (scanResult.getModifiedBizGroovy().isEmpty()) {
                return;
            }
        }

        try {
            deployStatus.setStatus(2);
            appDeployer.deploy(app, scanResult);
            deployStatus.setStatus(1);
            AppChangeHolder.success(app);
        } catch (Throwable e) {
            EasywebLogger.error("deploy app error: " + app.getAppKey(), e);
            AppChangeHolder.failed(app);
            deployStatus.setStatus(3);
            return;
        }

    }

    private void processWebChange(ScanResult result) {
        App app = result.getApp();
        List<String> groovyCode = result.getModifiedWebGroovy();
        if (groovyCode.isEmpty()) {
            return;
        }
        /**
         * 先实例化groovy对象
         */
        for (String file : groovyCode) {
            try {
                groovyObjectLoader.instanceObject(true, app, new File(file));
            } catch (Throwable e) {
                EasywebLogger.error("init web error,file=" + file, e);
            }
        }

        /**
         * 处理对象注入
         */
        for (String file : groovyCode) {
            try {
                groovyObjectLoader.autowiredObject(true, app, new File(file));
            } catch (Throwable e) {
                EasywebLogger.error("autowired error", e);
            }
        }

    }

    private volatile boolean inited = false;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (Configuration.isDev() && !inited) {
            inited = true;
            scanThread.setName("Easyweb Thread");
            scanThread.start();
        }
    }

    class DeployStatus {
        // 0:初始状态；1：部署成功；2：部署中；3：部署失败
        int status = 0;
        long lastDeployTime = 0;

        int getStatus() {
            return status;
        }

        void setStatus(int status) {
            this.status = status;
        }

        long getLastDeployTime() {
            return lastDeployTime;
        }

        void setLastDeployTime(long lastDeployTime) {
            this.lastDeployTime = lastDeployTime;
        }
    }
}
