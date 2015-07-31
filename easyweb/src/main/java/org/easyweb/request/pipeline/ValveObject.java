package org.easyweb.request.pipeline;

import groovy.lang.GroovyObject;

/**
 * Created by jimmey on 15-7-31.
 */
public class ValveObject {

    GroovyObject groovyObject;
    Valve valve;

    public ValveObject(Valve valve, GroovyObject groovyObject) {
        this.valve = valve;
        this.groovyObject = groovyObject;
    }

    public int order() {
        return valve.order();
    }

    public void invoke() {
        groovyObject.invokeMethod(valve.method(), null);
    }
}
