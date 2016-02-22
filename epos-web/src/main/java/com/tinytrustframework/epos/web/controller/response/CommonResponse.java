/*
 * 文 件 名:  ActionResponse.java
 * 版    权:  www.astcard.com. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2013-5-23
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.web.controller.response;

import java.util.Map;

/**
 * HTTP请求常用响应类
 *
 *
 * @author Owen
 * @version [版本号, 2013-5-23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonResponse extends Response {

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

}
