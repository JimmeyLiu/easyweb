package org.easyweb.groovy.groovyobject;

import groovy.lang.Binding;
import org.easyweb.bean.BeanFactory;

public class BeanBinding extends Binding {

    @Override
    public Object getVariable(String name) {
        Object obj = getBean(name);
        if (obj != null) {
            return obj;
        }
        return super.getVariable(name);
    }

    @Override
    public Object getProperty(String property) {
        Object obj = getBean(property);
        if (obj != null) {
            return obj;
        }
        return super.getProperty(property);
    }

    private Object getBean(String name) {
        return BeanFactory.getBean(name);
    }

}
