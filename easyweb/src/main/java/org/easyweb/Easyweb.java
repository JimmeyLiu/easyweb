package org.easyweb;

import org.easyweb.app.deploy.DeployListener;
import org.easyweb.app.monitor.AppWatcher;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.groovy.groovyobject.BeanBinding;
import org.easyweb.velocity.VelocityTool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by jimmey on 15-7-30.
 */
public class Easyweb {

    public static void initialize() {
        initialize(new HashMap<String, Object>());
    }

    public static void initialize(Map<String, Object> binding) {
        //从外部注入bean
        BeanBinding.putOuterBindings(binding);
        load(AnnotationParser.class);
        load(DeployListener.class);
        load(VelocityTool.class);

        new AppWatcher().start();
    }

    private static <T> void load(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        Iterator<T> it = loader.iterator();
        while (it.hasNext()) it.next();
    }

}
