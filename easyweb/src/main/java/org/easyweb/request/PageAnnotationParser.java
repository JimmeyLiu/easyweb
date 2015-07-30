package org.easyweb.request;

import groovy.lang.GroovyObject;
import org.easyweb.annocation.Page;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class PageAnnotationParser extends AnnotationParser {

    @Override
    public boolean isParse(Annotation annotation) {
        return annotation instanceof Page;
    }

    @Override
    public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        if (!(target instanceof Method)) {
            return;
        }
        Method javaMethod = (Method) target;
        Page page = javaMethod.getAnnotation(Page.class);
        if (page == null) {// 只支持没有参数的方法
            return;
        }

        EasywebLogger.warn("Add Page [%s]%s", page.method(), page.url());
        AppUriMapping.put(app, page, file, javaMethod);
    }

}
