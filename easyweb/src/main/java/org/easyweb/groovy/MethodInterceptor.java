package org.easyweb.groovy;

import groovy.lang.GroovyObject;
import groovy.lang.Interceptor;
import groovy.lang.MetaMethod;
import groovy.lang.Script;
import org.codehaus.groovy.reflection.CachedMethod;
import org.easyweb.app.App;
import org.easyweb.context.ThreadContext;
import org.easyweb.groovy.annotation.MethodAnnotationInvokeFactory;

import java.lang.reflect.Method;

public class MethodInterceptor implements Interceptor {


    public Object beforeInvoke(Object object, String methodName, Object[] arguments) {
        App app = ThreadContext.getApp();
        //app级别设置每个方法是否做控制
        if (app == null || !app.isGroovyMethodInterceptor()) {
            return null;
        }
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

    public Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
        return result;
    }

    public boolean doInvoke() {
        return true;
    }

}
