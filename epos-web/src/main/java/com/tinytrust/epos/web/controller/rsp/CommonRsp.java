package com.tinytrust.epos.web.controller.rsp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * HTTP请求常用响应类
 *
 * @author Owen
 * @version [版本号, 2013-5-23]
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRsp implements Serializable {

    private static final long serialVersionUID = -6756450794052871118L;

    /**
     * 响应结果
     * success|fail
     */
    private String result;

    /**
     * 信息备忘
     */
    private String message;

    /**
     * 数据对象Map
     */
    private Map<String, Object> dataMap;
}
