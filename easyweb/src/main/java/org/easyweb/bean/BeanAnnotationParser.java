package org.easyweb.bean;

import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.util.EasywebLogger;

import java.io.File;
import java.lang.annotation.Annotation;

public class BeanAnnotationParser extends AnnotationParser<Bean> {

    public BeanAnnotationParser() {
        super();
    }

    @Override
    public boolean match(Annotation annotation) {
        return annotation instanceof Bean;
    }

    @Override
    public void parse(App app, Bean bean, File file, Object target, GroovyObject groovyObject) {
        String name = bean.name();
        if (StringUtils.isBlank(name)) {
            name = bean.value();
        }
        if (StringUtils.isBlank(name)) {
            name = groovyObject.getClass().getSimpleName();
            String s = name.substring(0, 1);
            name = s.toLowerCase() + name.substring(1, name.length());
        }
        EasywebLogger.info("[Bean] %s register %s", app.getName(), name);
        BeanFactory.register(app, name, groovyObject);
    }

}
