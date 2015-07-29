package org.easyweb.groovy.metamethod;

import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import groovy.lang.GroovyObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Annotation;

@Component("ewMetaMethodParser")
public class MetaMethodParser extends AnnotationParser {

    @Override
    public boolean isParse(Annotation annotation) {
        return annotation instanceof MyMetaMethod;
    }

    @Override
    public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        if (groovyObject instanceof IMetaMethod) {
            MetaMethodFactory.regist(app.getAppKey(), (IMetaMethod) groovyObject);
        }
    }

}
