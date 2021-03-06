package org.easyweb.app.deploy.process;

import org.easyweb.app.App;
import org.easyweb.app.deploy.DeployException;
import org.easyweb.app.deploy.DeployPhase;
import org.easyweb.app.deploy.Deployer;
import org.easyweb.app.monitor.ScanResult;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.util.Set;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午2:55
 */
@Deployer(DeployPhase.INIT_BIZ)
public class BizInitProcessor extends FileProcessor {

    GroovyObjectLoader groovyObjectLoader;

    public BizInitProcessor() {
        groovyObjectLoader = GroovyObjectLoader.getInstance();
    }

    @Override
    public void process(ScanResult result) throws DeployException {
        if (!result.isRestart()) {
            return;
        }
        Set<String> groovyCode = result.getBizGroovyFiles();
        App app = result.getApp();
        try {
            /**
             * 先实例化groovy对象
             */
            for (String file : groovyCode) {
                groovyObjectLoader.instanceObject(false, app, new File(file));
            }
            /**
             * 处理对象注入
             */
            for (String file : groovyCode) {
                groovyObjectLoader.autowiredObject(false, app, new File(file));
            }
        } catch (Exception e) {
            EasywebLogger.error("[BizInitProcessor] [%s] Exception %s", app.getName(), e.getMessage());
            throw new DeployException("Init Error", e);
        }
    }
}
