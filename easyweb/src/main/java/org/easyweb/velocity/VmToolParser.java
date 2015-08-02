package org.easyweb.velocity;

import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;

import java.io.File;
import java.lang.annotation.Annotation;

public class VmToolParser extends AnnotationParser<VmTool> {

    @Override
    public boolean match(Annotation annotation) {
        return annotation instanceof VmTool;
    }

    @Override
    public void parse(App app, VmTool vmTool, File file, Object target, GroovyObject groovyObject) {
        if (StringUtils.isNotBlank(vmTool.value())) {
            VmToolFactory.putAppTool(app.getName(), vmTool.value(), groovyObject);
        }
    }

}
