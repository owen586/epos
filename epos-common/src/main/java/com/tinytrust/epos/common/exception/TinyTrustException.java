package com.tinytrust.epos.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常类
 *
 * @author owen
 * @version [版本号, 2015-8-26]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TinyTrustException extends Exception {

    // 注释内容
    private static final long serialVersionUID = 5349074211458559093L;

    // 错误编码
    private String errorCode;

    // 错误信息
    private String errorMessage;
}
