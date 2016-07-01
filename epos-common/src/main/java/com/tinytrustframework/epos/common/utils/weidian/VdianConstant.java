package com.tinytrustframework.epos.common.utils.weidian;

/**
 * @author owen
 * @date 2016-06-28 28:14:41
 */
public class VdianConstant {

    // 状态码
    public static final int STATUS_OK = 0;


    // 订单状态
    public static final String ORDER_STATUS_UNPAY = "unpay";//unpay（未付款）
    public static final String ORDER_STATUS_PAY = "pay";//pay（待发货）
    public static final String ORDER_STATUS_PEND = "pend";//pend (待处理订单，已就是待发货订单)
    public static final String ORDER_STATUS_UNSHIP_REFUNDING = "unship_refunding";//unship_refunding（未发货，申请退款）
    public static final String ORDER_STATUS_SHIPED_REFUNDING = "shiped_refunding";//shiped_refunding（已发货，申请退款）
    public static final String ORDER_STATUS_ACCEPT = "accept";//accept（已确认收货）
    public static final String ORDER_STATUS_ACCEPT_REFUNDING = "accept_refunding";//accept_refunding（已确认收货，申请退款）
    public static final String ORDER_STATUS_FINISH = "finish";//finish（订单完成）
    public static final String ORDER_STATUS_CLOSE = "close";//close（订单关闭）


    // 物流
    public static final String ORDER_DELIVER_TYPE = "999";//无需物流发货

    // 分页信息
    public static final int PAGE_NUM = 1;
    public static final int PAGE_SIZE = 50;

}
