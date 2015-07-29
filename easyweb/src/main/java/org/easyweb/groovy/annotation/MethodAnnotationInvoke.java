package org.easyweb.groovy.annotation;

import java.lang.reflect.Method;

public abstract class MethodAnnotationInvoke {

    public MethodAnnotationInvoke() {
        MethodAnnotationInvokeFactory.register(this.getClass().getName(), this);
    }

    public abstract Object invoke(Method method, Object[] arguments);

}
