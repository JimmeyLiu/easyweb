package org.easyweb.app.change;

import java.lang.annotation.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 上午9:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListenerPriority {

    Priority value();

}
