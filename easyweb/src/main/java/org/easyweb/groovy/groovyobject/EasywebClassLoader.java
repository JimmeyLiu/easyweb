package org.easyweb.groovy.groovyobject;

import groovy.lang.GroovyClassLoader;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 上午11:31
 * <p/>
 * 继承GroovyClassLoader，主要增加web变量来标示GroovyObject对象的classloader类型，用来处理web页面的变化
 */
public class EasywebClassLoader extends GroovyClassLoader {

    private boolean web = false;

    public EasywebClassLoader(boolean web, ClassLoader parent) {
        super(parent);
        this.web = web;
    }

    public boolean isWeb() {
        return web;
    }
}
