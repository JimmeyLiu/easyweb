package org.easyweb.groovy.groovyobject;

import org.easyweb.bean.BeanFactory;
import org.easyweb.bean.BeanFactory;
import groovy.lang.Binding;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("ewBinding")
public class BeanBinding extends Binding {

    public static Map<String, Object> outerBindings = new HashMap<String, Object>();

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
        Object obj = BeanFactory.getBean(name);
        if (obj == null) {
            obj = BeanFactory.getSpringBean(name);
        }
        if (obj == null) {
            obj = outerBindings.get(name);
        }
        return obj;
    }

    public static void putOuterBindings(Map<String, Object> map) {
        outerBindings.putAll(map);
    }

}
