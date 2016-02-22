/*
 * 文 件 名:  DigestUtil.java
 * 版    权:  Absolute Software
 * 描    述:  <描述>
 * 修 改 人:  Owen
 * 修改时间:  2012-5-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.common.utils.lang;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 签名工具类
 *
 * @author Owen
 * @version [版本号, 2012-5-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DigestUtil {

    private DigestUtil() {
    }

    /**
     * <一句话功能简述>
     *
     * @param plainText 明文
     * @return String 密文
     * @see [类、类#方法、类#成员]
     */
    public static String md5(String plainText) {
        String cipherText = null;
        cipherText = DigestUtils.md5Hex(plainText);
        return cipherText;
    }
}
