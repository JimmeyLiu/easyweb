package org.easyweb.request.error;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface ErrorPage {

    /**
     * 异常类型
     *
     * @return
     */
    ErrorType error();

    String layout() default "";

    /**
     * 异常类名
     *
     * @return
     */
    String exception() default "";

    /**
     * 如果找不到，直接用这个
     * @return
     */
    boolean container() default false;

}
