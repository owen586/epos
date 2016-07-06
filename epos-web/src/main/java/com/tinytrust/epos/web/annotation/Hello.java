package com.tinytrust.epos.web.annotation;

/**
 * @author owen
 * @version 2016-03-11 13:31
 */
public class Hello {

    @HelloAnno(author = "absolute", age = 30)
    public void sayHello(String userName) {
        System.out.println("Hello," + userName);
    }

    public void like(String userName) {
        System.out.println("I like " + userName);
    }
}
