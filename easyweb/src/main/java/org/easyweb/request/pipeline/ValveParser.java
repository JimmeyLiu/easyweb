package org.easyweb.request.pipeline;

import groovy.lang.GroovyObject;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;

import java.io.File;
import java.lang.annotation.Annotation;

/**
 * Created by jimmey on 15-7-31.
 */
public class ValveParser extends AnnotationParser<Valve> {

    @Override
    public boolean match(Annotation annotation) {
        return annotation instanceof Valve;
    }

    @Override
    public void parse(App app, Valve valve, File file, Object target, GroovyObject groovyObject) {
        Pipeline.addValve(app, new ValveObject(valve, groovyObject));
    }
}
