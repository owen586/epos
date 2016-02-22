/*
 * 文 件 名:  I18nUtil.java
 * 版    权:  Absolute Software Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  Owen 
 * 修改时间:  2010-11-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.common.utils.lang;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 国际化資源文件工具类
 *
 *
 * @author Owen
 * @version [版本号, 2010-11-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class I18nUtil {

    private I18nUtil() {
    }

    /**
     * <一句话功能简述>
     *
     *
     * @param key
     * @see [类、类#方法、类#成员]
     */
    public static String getProperty(String key) {
        String value = null;
        try {
            Properties property = PropertiesLoaderUtils.loadAllProperties("config.properties");
            value = property.getProperty(key);

        } catch (IOException e) {
            //TODO 
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println(I18nUtil.getProperty("tranfer.type.delay.minute.t0"));
        System.out.println(I18nUtil.getProperty("tranfer.type.delay.minute.t1"));
    }
}
