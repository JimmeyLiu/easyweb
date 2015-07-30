package org.easyweb.request.pipeline;

import org.easyweb.app.deploy.Deployer;
import org.easyweb.bean.BeanFactory;
import org.easyweb.app.App;
import org.easyweb.util.EasywebLogger;
import org.easyweb.app.deploy.DeployException;
import org.easyweb.app.deploy.DeployPhase;
import org.easyweb.app.deploy.process.FileProcessor;
import org.easyweb.app.monitor.ScanResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 下午12:01
 */
@Component
@Deployer(DeployPhase.AFTER_INIT)
public class PipelineProcessor extends FileProcessor {
    @Override
    public void process(ScanResult result) throws DeployException {
        List<String> list = result.getSuffixFiles(".properties");
        if (list.isEmpty()) {
            return;
        }
        App app = result.getApp();
        for (String file : list) {
            if (!file.endsWith("pipeline.properties")) {
                continue;
            }
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(file));
                String pipelines = properties.getProperty("pipeline.valves");
                if (StringUtils.isBlank(pipelines)) {
                    continue;
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
            } catch (IOException e) {
            }
        }
    }
}
