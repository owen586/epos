/*
 * 文 件 名:  YSNoticeResponse.java
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2015-7-28
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.web.controller.response;

/**
 * 银盛HTTP请求响应类
 *
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class YSNotifyResponse extends Response {
    /**
     * 注释内容
     */
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
