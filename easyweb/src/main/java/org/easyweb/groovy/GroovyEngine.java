package org.easyweb.groovy;

import org.easyweb.context.ThreadContext;
import org.easyweb.app.App;
import org.easyweb.context.ThreadContext;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;
import groovy.lang.GroovyObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component("ewGroovyEngine")
public class GroovyEngine {

    @Resource
    private GroovyObjectLoader groovyObjectLoader;


    public Object execute(File file, String method) throws Exception {
        return execute(file, method, null);
    }

    public Object execute(File file, String method, Object[] params) throws Exception {
        App app = ThreadContext.getContext().getApp();
        GroovyObject groovyObject = groovyObjectLoader.getObject(true, app, file);
        return groovyObject.invokeMethod(method, params);
    }

}
