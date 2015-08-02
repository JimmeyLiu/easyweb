package org.easyweb.request;

import groovy.lang.GroovyObject;
import org.easyweb.annocation.Page;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class PageAnnotationParser extends AnnotationParser<Page> {

    @Override
    public boolean match(Annotation annotation) {
        return annotation instanceof Page;
    }

    @Override
    public void parse(App app, Page page, File file, Object target, GroovyObject groovyObject) {
        if (!(target instanceof Method)) {
            return;
        }
        Method javaMethod = (Method) target;
        EasywebLogger.warn("[PageAnnotationParser] [%s] Add %s %s", app.getName(), page.method(), page.url());
        AppUriMapping.put(app, page, file, javaMethod);
    }

}
