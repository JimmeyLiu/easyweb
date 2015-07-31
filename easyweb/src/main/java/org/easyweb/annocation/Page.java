package org.easyweb.annocation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface Page {
    /**
     * @return
     * @Page("/your/path")
     */
    String value() default "";

    /**
     * with default
     *
     * @return
     * @Page(url="/your/path",layout="layout.vm")
     */
    String url();

    String layout() default "";

    /**
     * @return
     * @Page(url="/your/path",layout="layout.vm",method=Method.POST)
     */
    Method method() default Method.GET;

}
