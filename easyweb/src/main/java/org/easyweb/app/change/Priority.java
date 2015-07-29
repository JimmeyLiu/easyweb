package org.easyweb.app.change;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 上午9:55
 */
public enum Priority {
    LOW, //低优先级，最后执行。如classloader的卸载
    DEFAULT, //默认优先级，在高优先级之后
    HIGH;//高优先级，首先执行

    public static Priority[] getAll() {
        return new Priority[]{HIGH, DEFAULT, LOW};
    }
}
