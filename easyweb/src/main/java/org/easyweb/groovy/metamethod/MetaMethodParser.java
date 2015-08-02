package org.easyweb.groovy.metamethod;

import groovy.lang.GroovyObject;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;

import java.io.File;
import java.lang.annotation.Annotation;

public class MetaMethodParser extends AnnotationParser<MyMetaMethod> {
    @Override
    public boolean match(Annotation annotation) {
        return annotation instanceof MyMetaMethod;
    }

    @Override
    public void parse(App app, MyMetaMethod annotation, File file, Object target, GroovyObject groovyObject) {
        if (groovyObject instanceof IMetaMethod) {
            MetaMethodFactory.register(app.getName(), (IMetaMethod) groovyObject);
        }
    }

}
