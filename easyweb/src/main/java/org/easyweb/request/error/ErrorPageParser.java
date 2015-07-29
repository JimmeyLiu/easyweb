package org.easyweb.request.error;

import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.request.PageMethod;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.request.PageMethod;
import groovy.lang.GroovyObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ErrorPageParser extends AnnotationParser {

    @Override
    public boolean isParse(Annotation annotation) {
        return annotation instanceof ErrorPage;
    }

    @Override
    public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        if (!(target instanceof Method)) {
            return;
        }
        Method javaMethod = (Method) target;
        ErrorPage page = javaMethod.getAnnotation(ErrorPage.class);
        if (page == null) {// 只支持没有参数的方法
            return;
        }
        PageMethod pageMethod = new PageMethod();
        pageMethod.setFile(file);
        pageMethod.setMethod(javaMethod);
        pageMethod.setLayout(page.layout());
        ErrorResponse.regist(app, page, pageMethod);
    }

}
