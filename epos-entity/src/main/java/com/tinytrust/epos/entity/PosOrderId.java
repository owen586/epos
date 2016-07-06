package com.tinytrust.epos.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单类主鍵类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosOrderId implements java.io.Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1277611078241216888L;

    // 订单来源
    private Integer orderSrc;

    // 订单编号
    private String orderCode;


}