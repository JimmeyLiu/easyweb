package org.easyweb.request.pipeline;

import java.lang.annotation.*;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 上午11:53
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Valve {

    int order();

    String method();
}
