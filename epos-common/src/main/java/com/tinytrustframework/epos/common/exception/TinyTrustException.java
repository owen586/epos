/*
 * 文 件 名:  TinyTrustException.java
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2015-8-26
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.common.exception;

/**
 * 异常类
 *
 * @author owen
 * @version [版本号, 2015-8-26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TinyTrustException extends Exception {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5349074211458559093L;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * <默认构造函数>
     */
    public TinyTrustException() {
        super();
    }

    /**
     * <默认构造函数>
     */
    public TinyTrustException(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
