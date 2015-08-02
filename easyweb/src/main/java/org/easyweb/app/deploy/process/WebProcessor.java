package org.easyweb.app.deploy.process;

import org.easyweb.app.App;
import org.easyweb.app.deploy.DeployException;
import org.easyweb.app.deploy.DeployPhase;
import org.easyweb.app.deploy.Deployer;
import org.easyweb.app.monitor.ScanResult;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import org.easyweb.util.EasywebLogger;

import javax.annotation.Resource;
import java.io.File;
import java.util.Set;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午2:55
 */
@Deployer(DeployPhase.INIT_WEB)
public class WebProcessor extends FileProcessor {

    GroovyObjectLoader groovyObjectLoader;

    public WebProcessor() {
        this.groovyObjectLoader = GroovyObjectLoader.getInstance();
    }

    @Override
    public void process(ScanResult result) throws DeployException {
        App app = result.getApp();
        Set<String> groovyCode = result.getWebGroovyFiles();
        /**
         * 先实例化groovy对象
         */
        for (String file : groovyCode) {
            try {
                groovyObjectLoader.instanceObject(true, app, new File(file));
            } catch (Throwable e) {
                EasywebLogger.error("[WebProcessor] [%s] Instance Exception " + file, e);
            }
        }

        /**
         * 处理对象注入
         */
        for (String file : groovyCode) {
            try {
                groovyObjectLoader.autowiredObject(true, app, new File(file));
            } catch (Throwable e) {
                EasywebLogger.error("[WebProcessor] [%s] Inject Exception " + file, e);
            }
        }

    }
}
