package com.tinytrust.epos.web.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 一句话功能简述
 *
 * @author owen
 * @version 2016-03-11 13:31
 */
public class HelloReflect {

    public static void main(String[] args) throws ClassNotFoundException, SecurityException {
        //AnnotatedElement
        //Class,Method, Constructor, Field, Method,Package

        Class helloClazz = Class.forName("com.tinytrust.epos.web.annotation.Hello");
        System.out.println(helloClazz.isAnnotationPresent(HelloAnno.class));//判断类是否存在指定注解

//        Method[] helloMethods = helloClazz.getMethods();//获得所有方法，包括从父类中继承的
        Method[] helloMethods = helloClazz.getDeclaredMethods();//获得类中直接申明的所有方法
        if (helloMethods.length > 0) {
            Annotation[] annotations = null;
            for (Method aMethod : helloMethods) {
                System.out.println(aMethod.getName() + " " + aMethod.isAnnotationPresent(HelloAnno.class));

                if (aMethod.isAnnotationPresent(HelloAnno.class)) {
                    annotations = aMethod.getDeclaredAnnotations();
                    for (Annotation anno : annotations) {
                        if (anno instanceof HelloAnno) {
                            HelloAnno helloAnno = (HelloAnno) anno;
                            System.out.println(helloAnno.age() + " " + helloAnno.author());
                        }
                    }
                }
            }
        }


    }
}
