package org.easyweb.app.deploy;

import org.easyweb.app.App;
import org.easyweb.app.listener.AppChangeHolder;
import org.easyweb.app.monitor.ScanResult;
import org.easyweb.util.EasywebLogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 应用部署器
 *
 * @author jimmey
 */
public class AppDeployer {

    public final static Map<DeployPhase, List<DeployListener>> listeners = new HashMap<DeployPhase, List<DeployListener>>();

    public static void addListener(DeployListener deployListener) {
        Deployer deployer = deployListener.getClass().getAnnotation(Deployer.class);
        DeployPhase phase = DeployPhase.COMPILE_GROOVY;
        if (deployer != null) {
            phase = deployer.value();
        }
        List<DeployListener> list = listeners.get(phase);
        if (list == null) {
            list = new LinkedList<DeployListener>();
            listeners.put(phase, list);
        }
        list.add(deployListener);
        EasywebLogger.error("register deployer phase: %s deployer: %s", phase.name(), deployListener.getClass().getName());
    }

    public void deploy(App app, ScanResult result) throws DeployException {
        //biz groovy变化才先停止app服务
        boolean restart = result.isRestart();
        if (restart) {
            AppChangeHolder.stop(app);
        }

        //调用初始化处理
        for (DeployPhase phase : DeployPhase.getAll()) {
            List<DeployListener> list = listeners.get(phase);
            EasywebLogger.error("Phase: %s List: %s", phase.name(), list);
            if (list == null) {
                continue;
            }

            for (DeployListener processor : list) {
                EasywebLogger.error(phase.name() + " " + processor.getClass().getName());
                processor.process(result);
            }
        }

        if (restart) {
            AppChangeHolder.success(app);
        }
    }

}
