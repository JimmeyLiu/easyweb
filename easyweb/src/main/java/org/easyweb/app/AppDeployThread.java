package org.easyweb.app;

import org.easyweb.Configuration;
import org.easyweb.app.change.AppChangeHolder;
import org.easyweb.app.deploy.AppDeployer;
import org.easyweb.app.deployfile.*;
import org.easyweb.app.scanner.AppScanner;
import org.easyweb.app.scanner.ScanResult;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * Date: 13-6-1
 * Time: 上午9:20
 */
public class AppDeployThread implements Runnable {

    public static Map<String, DeployStatus> deployLock = new ConcurrentHashMap<String, DeployStatus>();

    private AppDeployer appDeployer;

    private GroovyObjectLoader groovyObjectLoader;

    private QueueItem item;

    public AppDeployThread(AppDeployer appDeployer, GroovyObjectLoader groovyObjectLoader, QueueItem item) {
        this.appDeployer = appDeployer;
        this.groovyObjectLoader = groovyObjectLoader;
        this.item = item;
    }

    @Override
    public void run() {
        String appKey = item.getAppName();
        if (deployLock.containsKey(appKey)) {
            return;
        }
        DeployStatus status = DeployStatus.START;
        long start = System.currentTimeMillis();
        try {
            deployLock.put(appKey, status);

            File appFile = new File(Configuration.getDeployPath() + item.getFile());
            if (!appFile.exists()) {
                status = DeployStatus.FAILED;
                EasywebLogger.error("deploy file not exits " + item.getFile());
                return;
            }

            File config = new File(appFile.getPath() + "/app.properties");
            if (!config.exists()) {
                return;
            }

            App app = AppParser.parse(config);
            if (app == null || app.getName() == null) {
                return;
            }

            /**
             * 如果值独立的assets应用，则直接返回成功
             */
            if (DeployType.ASSETS.equals(item.getDeployType())) {
                AppChangeHolder.success(app);
                status = DeployStatus.SUCCESS;
                return;
            }

            /**
             * 如果是停止应用，则直接调用stop
             */
            if (DeployType.STOP.equals(item.getDeployType())) {
                AppChangeHolder.stop(app);
                status = DeployStatus.SUCCESS;
                return;
            }

            ScanResult scanResult = AppScanner.getInstance().scan(app);

            /**
             * 应用没有biz代码（ide中遇到）导致下面直接return，应用没有注册到AppContainer中
             */
            if (!scanResult.getBizGroovyFiles().isEmpty()) {//biz不为空，走下面的流程
                /**
                 * biz没有变化或者只是页面修改发布
                 */
                if (scanResult.getModifiedBizGroovy().isEmpty() || DeployType.TEMPLATE.equals(item.getDeployType())) {
                    if (!scanResult.getModifiedWebGroovy().isEmpty()) {
                        processWebChange(scanResult);
                        status = DeployStatus.SUCCESS;
                    }
                    return;
                }
            }


            try {
                appDeployer.deploy(app, scanResult);
                AppChangeHolder.success(app);
                status = DeployStatus.SUCCESS;
            } catch (Throwable e) {
                EasywebLogger.error("deploy app error: " + app.getAppKey(), e);
                status = DeployStatus.FAILED;
                AppChangeHolder.failed(app);
            }
        } catch (Throwable e) {
            status = DeployStatus.FAILED;
            EasywebLogger.error("deploy error:" + appKey, e);
        } finally {
            deployLock.remove(appKey);
            DeployQueueFile.remove(item);
            if (status != DeployStatus.START) {//没有部署动作就返回了，则不记录info、history信息
                item.setStatus(status);
                DeployInfoFile.update(item);
                History history = new History(item, start);
                DeployHistoryFile.add(history);
            }
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
                EasywebLogger.error("groovy Autowired error", e);
            }
        }

    }
}
