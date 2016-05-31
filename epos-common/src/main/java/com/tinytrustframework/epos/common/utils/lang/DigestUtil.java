package com.tinytrustframework.epos.common.utils.lang;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 签名工具类
 *
 * @author Owen
 * @version [版本号, 2012-5-25]
 */
public class DigestUtil {

    private DigestUtil() {
    }

    /**
     * 生成MD5摘要
     * Calculates the MD5 digest and returns the value as a 32 character hex string
     *
     * @param plainText 明文
     *                  String 密文
     */
    public static String md5(String plainText) {
        String cipherText = DigestUtils.md5Hex(plainText);
        return cipherText;
    }

    /**
     * 生成SHA摘要
     * Calculates the SHA digest and returns the value as a hex string
     * 安全哈希算法（Secure Hash Algorithm）主要适用于数字签名标准（Digital Signature Standard DSS）里面定义的数字签名算法（Digital Signature Algorithm DSA）
     *
     * @param plainText 明文
     *                  密文
     */
    public static String sha(String plainText) {
        String cipherText = DigestUtils.shaHex(plainText);
        return cipherText;
    }


    public static void main(String[] args) {
        String name = "owen";
        System.out.println(DigestUtil.md5(name));
        System.out.println(DigestUtil.sha(name));

        System.out.println(DigestUtil.md5("13913317376" + "1" + "@TinyTrust2015ForLoginAndRegister####"));
    }
}
