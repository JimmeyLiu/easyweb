package org.easyweb.app.deploy;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-24 下午10:13
 */
public enum DeployPhase {
    PARSE_CONFIG(1),//环境初始化
    COMPILE_GROOVY(2),//停止服务（如果已有app在运行）
    INIT_BIZ(3),
    INIT_WEB(4),
    INIT_GROOVYOBJECT(5),//实例化groovy对象
    AFTER_INIT(6);//groovy对象实例化之后
    int phase;

    private DeployPhase(int phase) {
        this.phase = phase;
    }

    public static DeployPhase[] getAll() {
        return new DeployPhase[]{PARSE_CONFIG, COMPILE_GROOVY, INIT_BIZ,INIT_WEB,INIT_GROOVYOBJECT, AFTER_INIT};
    }
}
