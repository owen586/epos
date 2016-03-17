package com.tinytrustframework.epos.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 银盛HTTP请求响应类
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
