package com.tinytrustframework.epos.web.controller.req;

import lombok.*;

import java.io.Serializable;

/**
 * 银盛订单类
 *
 * @author owen
 * @version 2.0, 2016-01-09 14:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YSNotifyReq implements Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7232298636014733212L;

    /**
     * 银盛订单号
     */
    private String ysOrderid;

    /**
     * 商户订单号
     */
    private String orderid;

    /**
     * 状态
     */
    private String status;

    /**
     * status 状态 1、消费成功 ；0、消费失败
     */
    private String money;

    /**
     * 实际消费金额，单位:分
     */
    private String time;

    /**
     * 备注
     */
    private String remark;

    /**
     * 校验值
     */
    private String checkValue;
}
