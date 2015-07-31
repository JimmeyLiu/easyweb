package org.easyweb.groovy.annotation;

import org.easyweb.app.App;
import groovy.lang.GroovyObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationParserFactory {

    private static Map<AnnotationParser.ParsePhase, List<AnnotationParser>> annotationParsers = new ConcurrentHashMap<AnnotationParser.ParsePhase, List<AnnotationParser>>();

    public static void register(AnnotationParser parser, AnnotationParser.ParsePhase[] phases) {
        for (AnnotationParser.ParsePhase parsePhase : phases) {
            List<AnnotationParser> list = annotationParsers.get(parsePhase);
            if (list == null) {
                list = new ArrayList<AnnotationParser>();
                annotationParsers.put(parsePhase, list);
            }
            list.add(parser);
        }
    }

    @SuppressWarnings("all")
    public static void parse(AnnotationParser.ParsePhase parsePhase, App app, Annotation annotation, File file, Object target, GroovyObject groovyObject) {
        for (AnnotationParser parser : annotationParsers.get(parsePhase)) {
            if (!parser.match(annotation)) {
                continue;
            }
            parser.parse(app, annotation, file, target, groovyObject);
        }
    }

}
