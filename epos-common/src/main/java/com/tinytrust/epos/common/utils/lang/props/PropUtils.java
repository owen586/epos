package com.tinytrust.epos.common.utils.lang.props;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Properties文件读取工具类
 *
 * @author Owen
 * @version [版本号, 2010-11-11]
 */
@Slf4j
public class PropUtils {

    // 默认构造函数
    private PropUtils() {
    }

    /**
     * 根据属性文件KEY获取属性文件值
     *
     * @param key 属性文件键
     *            属性文件值
     */
    public static String getPropertyValue(String key) {
        String value = null;
        try {
            Properties property = PropertiesLoaderUtils.loadAllProperties("config.properties");
            value = property.getProperty(key);
        } catch (IOException e) {
            log.error("获取属性值失败! key:{}", key);
        }
        return value;
    }
}
