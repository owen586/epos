package com.tinytrustframework.epos.common.captcha;

import java.awt.*;

/**
 * @author owen
 * @date 2016-05-26 26:13:14
 */
public class TinyTrustFont {
    public static void main(String[] args) {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = e.getAvailableFontFamilyNames();
        for(String font : fontName){
            System.out.println(font);
        }
    }
}
