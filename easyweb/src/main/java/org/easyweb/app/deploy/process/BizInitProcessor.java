package org.easyweb.app.deploy.process;

import org.easyweb.app.App;
import org.easyweb.util.EasywebLogger;
import org.easyweb.app.deploy.DeployException;
import org.easyweb.app.deploy.DeployPhase;
import org.easyweb.app.deploy.Deployer;
import org.easyweb.app.scanner.ScanResult;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午2:55
 */
@Deployer(DeployPhase.INIT_BIZ)
@Component
public class BizInitProcessor extends FileProcessor {

    @Resource
    GroovyObjectLoader groovyObjectLoader;

    @Override
    public void process(ScanResult result) throws DeployException {
        App app = result.getApp();
        List<String> groovyCode = result.getBizGroovyFiles();

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
            EasywebLogger.error("biz init error", e);
            throw new DeployException("Init Error", e);
        }
    }
}
