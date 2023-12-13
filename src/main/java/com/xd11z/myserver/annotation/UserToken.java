package com.xd11z.myserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个自定义注解，用在方法上，表示要token验证
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserToken {
    //是否启用，默认启用
    boolean required() default true;
    //角色信息，all表示任意角色，不验证
    String role() default "all";
}
