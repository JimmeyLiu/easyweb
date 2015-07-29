package org.easyweb.groovy.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodAnnotationInvokeFactory {

    public static Map<String, MethodAnnotationInvoke> map = new HashMap<String, MethodAnnotationInvoke>();

    public static void register(String name, MethodAnnotationInvoke methodAnnotation) {
        map.put(name, methodAnnotation);
    }

    public static void remove(String name) {
        map.remove(name);
    }

    public static void invoke(Method method, Object[] arguments) {
        for (MethodAnnotationInvoke annotation : map.values()) {
            annotation.invoke(method, arguments);
        }
    }

}
