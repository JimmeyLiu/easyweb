package org.easyweb.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    /**
     * 用于标识param的名称。
     * <p>
     * 此参数用于简化的形式：<code>@Param("paramName")</code>。
     * </p>
     */
    String value() default "";

    /**
     * 用于标识param的名称。
     * <p>
     * 此参数用于有多个参数的形式：<code>@Param(name="paramName", defaultValue="123")</code>。
     * </p>
     */
    String name() default "";

    /**
     * 指定参数的默认值。
     */
    String defaultValue() default "";

}
