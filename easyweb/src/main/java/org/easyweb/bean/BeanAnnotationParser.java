package org.easyweb.bean;

import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Annotation;

@Component("ewBeanAnnotationParser")
public class BeanAnnotationParser extends AnnotationParser {

    public BeanAnnotationParser() {
        super();
    }

    @Override
    public boolean isParse(Annotation annotation) {
        return annotation instanceof Bean;
    }

    @Override
    public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        Bean bean = (Bean) annotation;
        String name = bean.name();
        if (StringUtils.isBlank(name)) {
            name = bean.value();
        }
        if (StringUtils.isBlank(name)) {
            name = groovyObject.getClass().getSimpleName();
            String s = name.substring(0, 1);
            name = s.toLowerCase() + name.substring(1, name.length());
        }
        BeanFactory.regist(app, name, groovyObject);
    }

}
