package org.easyweb.app.deploy.process;

import org.easyweb.Configuration;
import org.easyweb.app.deploy.DeployException;
import org.easyweb.app.deploy.Deployer;
import org.easyweb.groovy.groovyobject.AppClassLoaderFactory;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import org.easyweb.app.App;
import org.easyweb.util.EasywebLogger;
import org.easyweb.app.deploy.DeployPhase;
import org.easyweb.app.scanner.ScanResult;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.FileSystemCompiler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 下午2:55
 */
@Deployer(DeployPhase.COMPILE_GROOVY)
@Component
public class BizCompileProcessor extends FileProcessor implements InitializingBean {

    @Resource
    private GroovyObjectLoader groovyObjectLoader;

    private String classpath;

    @Override
    public void afterPropertiesSet() throws Exception {
        URL url = BizCompileProcessor.class.getClassLoader().getResource("WEB-INF/lib");
        if (url != null) {
            File file = new File(url.getFile());
            StringBuilder sb = new StringBuilder();
            int start = 0;
            for (File jar : file.listFiles()) {
                if (start++ != 0) {
                    sb.append(File.pathSeparator);
                }
                sb.append(jar.getAbsolutePath());
            }
            this.classpath = sb.toString();
        }
    }

    @Override
    public void process(ScanResult result) throws DeployException {
        App app = result.getApp();
        List<String> groovyCode = result.getBizGroovyFiles();

        String appClassPath = Configuration.getClasspath(app);
        if (!Configuration.isDev()) {
            appClassPath += "_" + System.currentTimeMillis();
        }

        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setTargetDirectory(appClassPath);
        if (classpath != null) {
            configuration.setClasspath(classpath);
        }

        FileSystemCompiler compiler = new FileSystemCompiler(configuration);
        try {
            compiler.compile(groovyCode.toArray(new String[groovyCode.size()]));
            GroovyClassLoader classLoader = AppClassLoaderFactory.getAppBizClassLoader(app);
            classLoader.addClasspath(appClassPath);
        } catch (Exception e) {
            EasywebLogger.error(e);
            throw new DeployException("Compile Error", e);
        }
    }
}
