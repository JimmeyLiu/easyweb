package org.easyweb.groovy.groovyobject;

import groovy.lang.GroovyObject;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 上午12:10
 *
 *
 * groovy对象的缓存对象封装
 */
public class CacheDO {
    /**
     * 缓存的groovy对象
     */
    private GroovyObject obj;
    /**
     * 对应groovy文件的最后修改时间
     */
    private long lastModified;
    /**
     * groovy对象是否已经做了依赖的注入
     */
    private boolean autowired;

    public GroovyObject getObj() {
        return obj;
    }

    public void setObj(GroovyObject obj) {
        this.obj = obj;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isAutowired() {
        return autowired;
    }

    public void setAutowired(boolean autowired) {
        this.autowired = autowired;
    }
}
