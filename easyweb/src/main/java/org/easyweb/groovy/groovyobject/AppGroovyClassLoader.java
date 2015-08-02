package org.easyweb.groovy.groovyobject;

import groovy.lang.GroovyClassLoader;

/**
 * User: jimmey/shantong
 * DateTime: 13-3-31 下午7:40
 */
public class AppGroovyClassLoader {

    private EasywebClassLoader bizClassLoader;
    /**
     * web代码的classloader是biz的子loader，这样biz代码在初始化完成后就可以给web使用了
     */
    private EasywebClassLoader webClassLoader;

    public AppGroovyClassLoader() {
        this.bizClassLoader = new EasywebClassLoader(false, AppGroovyClassLoader.class.getClassLoader());
        this.webClassLoader = new EasywebClassLoader(true, this.bizClassLoader);
    }

    public GroovyClassLoader getBizClassLoader() {
        return this.bizClassLoader;
    }

    public GroovyClassLoader getWebClassLoader() {
        return this.webClassLoader;
    }
}
