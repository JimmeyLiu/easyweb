package org.easyweb.groovy.annotation;

import groovy.lang.GroovyObject;
import org.easyweb.app.App;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

/**
 * Annotation解析
 *
 * @author jimmey
 */
public abstract class AnnotationParser<T> {

    public AnnotationParser() {
        this(new ParsePhase[]{ParsePhase.Init});
    }

    public AnnotationParser(ParsePhase[] parsePhases) {
        AnnotationParserFactory.register(this, parsePhases);
    }

    /**
     * 如果是，则做对应的处理
     *
     * @param annotation
     * @param file         文件地址
     * @param target       注解对应的对象，Class或Method
     * @param groovyObject groovy对象
     */
    public abstract void parse(App app, T annotation, File file, Object target, GroovyObject groovyObject);

    public abstract boolean match(Annotation annotation);

    public enum ParsePhase {
        Init,//groovyObject初始化阶段
        Ioc//groovy对象注入阶段
    }

}
