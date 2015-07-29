package org.easyweb.bean;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    String value() default "";

    String name() default "";

    boolean singleton() default true;

}
