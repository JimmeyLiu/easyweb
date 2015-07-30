package org.easyweb.groovy;

import groovy.lang.GroovyObject;
import org.easyweb.app.App;
import org.easyweb.context.ThreadContext;
import org.easyweb.groovy.groovyobject.GroovyObjectLoader;

import java.io.File;

public class GroovyEngine {

    private GroovyObjectLoader groovyObjectLoader;

    public GroovyEngine() {
        this.groovyObjectLoader = GroovyObjectLoader.getInstance();
    }

    public Object execute(File file, String method) throws Exception {
        return execute(file, method, null);
    }

    public Object execute(File file, String method, Object[] params) throws Exception {
        App app = ThreadContext.getContext().getApp();
        GroovyObject groovyObject = groovyObjectLoader.getObject(true, app, file);
        return groovyObject.invokeMethod(method, params);
    }

}
