package com.tinytrust.epos.web.controller.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class YSNotifyRsp implements Serializable {

    private static final long serialVersionUID = -698359291423491769L;

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
