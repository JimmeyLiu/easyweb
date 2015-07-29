package org.easyweb.groovy.annotation;

import org.easyweb.app.App;
import groovy.lang.GroovyObject;

import java.io.File;
import java.lang.annotation.Annotation;

/**
 * Annotation解析
 *
 * @author jimmey
 */
public abstract class AnnotationParser {

    private ParsePhase[] parsePhases;

    public AnnotationParser() {
        this(new ParsePhase[]{ParsePhase.Init});
    }

    public AnnotationParser(ParsePhase[] parsePhases) {
        this.parsePhases = parsePhases;
        AnnotationParserFactory.register(this);
    }

    public ParsePhase[] getParsePhases() {
        return parsePhases;
    }

    public void setParsePhases(ParsePhase[] parsePhases) {
        this.parsePhases = parsePhases;
    }

    /**
     * 是否是本parser支持的annotation
     *
     * @param annotation
     * @return
     */
    public abstract boolean isParse(Annotation annotation);

    /**
     * 如果是，则做对应的处理
     *
     * @param annotation
     * @param file         文件地址
     * @param target       注解对应的对象，Class或Method
     * @param groovyObject groovy对象
     */
    public abstract void parse(App app, Annotation annotation, File file, Object target, GroovyObject groovyObject);

    public enum ParsePhase {
        Init,//groovyObject初始化阶段
        Ioc//groovy对象注入阶段
    }

}
