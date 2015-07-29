package org.easyweb.bean;

import java.lang.annotation.*;

/**
 * User: jimmey/shantong
 * Date: 13-7-21
 * Time: 上午7:58
 * 用来声明Bean不能被easyweb groovy注入
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoGroovy {
}
