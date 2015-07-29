package org.easyweb.app.deploy;


public class Phase {

    /**
     * 需要在类初始化之前配置的，如数据源初始化
     */
    public static final int PARSE_CONFIG = 1;
    /**
     * 编译groovy
     */
    public static final int COMPILE_GROOVY = 2;
    /**
     * 实例化groovy对象
     */
    public static final int INIT_GROOVYOBJECT = 3;

    public static final int AFTER_INIT = 4;

}
