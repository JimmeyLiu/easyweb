package org.easyweb.groovy;

import groovy.lang.GroovyObject;
import groovy.lang.Interceptor;
import groovy.lang.MetaMethod;
import groovy.lang.Script;
import org.codehaus.groovy.reflection.CachedMethod;
import org.easyweb.Configuration;
import org.easyweb.groovy.annotation.MethodAnnotationInvokeFactory;

import java.lang.reflect.Method;

public class MethodInterceptor implements Interceptor {


    public Object beforeInvoke(Object object, String methodName, Object[] arguments) {
        if (!Configuration.isMethodInterceptor()) {
            return null;
        }
        //先不用，比较影响性能
        if (object instanceof Script) {
            Script script = (Script) object;

            MetaMethod metaMethod = script.getMetaClass().getMetaMethod(methodName, arguments);
            if (metaMethod == null || !(metaMethod instanceof CachedMethod)) {
                return null;
            }

            Method javaMethod = ((CachedMethod) metaMethod).getCachedMethod();
            MethodAnnotationInvokeFactory.invoke(javaMethod, arguments);
        } else if (object instanceof GroovyObject) {
            GroovyObject obj = (GroovyObject) object;
            MetaMethod metaMethod = obj.getMetaClass().getMetaMethod(methodName, arguments);
            if (metaMethod == null || !(metaMethod instanceof CachedMethod)) {
                return null;
            }
            Method javaMethod = ((CachedMethod) metaMethod).getCachedMethod();
            MethodAnnotationInvokeFactory.invoke(javaMethod, arguments);
        }
        return null;
    }

    private Class<?>[] getParamTypes(Object[] arguments) {
        if (arguments == null) {
            return null;
        }
        Class<?>[] classes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            classes[i] = arguments[i] == null ? null : arguments[i].getClass();
        }
        return classes;
    }

    public Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
//		if (result instanceof Map<?, ?> && arguments.length == 1 && arguments[0] instanceof Map<?, ?>) {
//			Map<String, Object> context = (Map<String, Object>) arguments[0];
//			context.putAll((Map<String, Object>) result);
//		}
        return result;
    }

    public boolean doInvoke() {
        return true;
    }

}
