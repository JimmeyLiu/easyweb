package org.easyweb.velocity;

import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import org.easyweb.app.App;
import org.easyweb.groovy.annotation.AnnotationParser;
import groovy.lang.GroovyObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Annotation;

@Component
public class VmToolParser extends AnnotationParser {

	@Override
	public boolean isParse(Annotation annotation) {
		return annotation instanceof VmTool;
	}

	@Override
	public void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
		VmTool vmTool = (VmTool) annotation;
		if (StringUtils.isNotBlank(vmTool.value())) {
			VmToolFactory.putAppTool(app.getAppKey(), vmTool.value(), groovyObject);
		}
	}

}