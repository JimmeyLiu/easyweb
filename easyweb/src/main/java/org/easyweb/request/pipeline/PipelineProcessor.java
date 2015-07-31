package org.easyweb.request.pipeline;

import org.easyweb.app.App;
import org.easyweb.app.deploy.DeployException;
import org.easyweb.app.deploy.DeployPhase;
import org.easyweb.app.deploy.Deployer;
import org.easyweb.app.deploy.process.FileProcessor;
import org.easyweb.app.monitor.ScanResult;
import org.easyweb.bean.BeanFactory;
import org.easyweb.util.EasywebLogger;

import java.util.LinkedList;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 下午12:01
 */
@Deployer(DeployPhase.AFTER_INIT)
public class PipelineProcessor extends FileProcessor {
    @Override
    public void process(ScanResult result) throws DeployException {
        App app = result.getApp();
        String pipelines = app.getConfig("pipeline.valves");
        if (pipelines == null) {
            return;
        }
        List<Valve> valves = new LinkedList<Valve>();
        String[] v = pipelines.split(",");
        for (String name : v) {
            Object obj = BeanFactory.getAppBean(app.getAppName(), name);
            if (obj == null || !(obj instanceof Valve)) {
                EasywebLogger.error("Processor &s Pipeline init error", app.getName());
                throw new DeployException("pipeline " + name + " error");
            }
            valves.add((Valve) obj);
        }
        Pipeline.initPipeline(result.getApp(), valves);
    }
}
