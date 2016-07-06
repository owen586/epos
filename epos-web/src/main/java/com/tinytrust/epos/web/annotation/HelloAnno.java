package com.tinytrust.epos.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author owen
 * @version 2016-03-11 13:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HelloAnno {

    //成员变量以无形参的方法提供
    // 作者
    String author() default "owen";

    // 年龄
    int age() default 30;
}
