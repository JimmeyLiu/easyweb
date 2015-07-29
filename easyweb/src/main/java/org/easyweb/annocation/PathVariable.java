package org.easyweb.annocation;

import java.lang.annotation.*;

/**
 * User: jimmey/shantong
 * Date: 13-7-4
 * Time: 下午3:31
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

    String value();

}
