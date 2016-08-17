package com.tinytrust.epos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(PosOrderId.class)
@Table(name = "t_pos_order")
public class PosOrder implements java.io.Serializable {

    private static final long serialVersionUID = -2544597554952499192L;

    // 订单来源 (1:pos订单 2:微店订单 3:手动订单)
    @Id
    @Column(name = "order_src")
    private Integer orderSrc;

    // 订单编号
    @Id
    @Column(name = "order_code", length = 32)
    private String orderCode;

    // 订单类型（1:正常订单  2:返点订单)
    @Column(name = "order_type")
    private int orderType;

    // 终端编号
    @Column(name = "terminal_code", length = 32)
    private String terminalCode;

    // 批次号
    @Column(name = "batch_code", length = 64)
    private String batchCode;

    // 付款银行卡卡号
    @Column(name = "pay_bank_code", length = 32)
    private String payBankCode;

    // 用户编号
    @Column(name = "user_code", length = 32)
    private String userCode;

    // 外部用户编号
    @Column(name = "outter_user_code", length = 32)
    private String outterUserCode;

    // 订单金额
    @Column(name = "order_money", precision = 10, scale = 3)
    private Double orderMoney;

    // 费率
    @Column(name = "fee_rate")
    private int feeRate;

    // 结算金额
    @Column(name = "trade_money", precision = 10, scale = 3)
    private Double tradeMoney;

    // 订单入库时间
    @Column(name = "add_date", length = 0)
    private Date addDate;

    // 订单SHOULD处理时间
    @Column(name = "should_deal_date", length = 0)
    private Date shouldDealDate;

    // 订单实际处理时间
    @Column(name = "indeed_deal_date", length = 0)
    private Date indeedDealDate;

    // 订单状态
    // -1: 待核实 1： 待处理  2：处理中  3：处理失败  4：处理成功
    @Column(name = "status")
    private Integer status;

    // 订单备忘
    @Column(name = "memo", length = 64)
    private String memo;

    // 用户名
    @Transient
    private String userName;

    //  手机号
    @Transient
    private String cellphone;

    //  到账类型 1：T+0 2：T+1
    @Transient
    private int tranferType;

    /**
     * 默认构造函数
     */
    public PosOrder(String orderCode, Integer orderType, Integer tranferType, String terminalCode, String payBankCode,
                    String userCode, String outterUserCode, Double tradeMoney, Date addDate, Date shouldDealDate,
                    Date indeedDealDate, Integer status, String memo, String userName, String cellphone, Double orderMoney,
                    int feeRate) {
        super();
        this.orderCode = orderCode;
        this.orderType = orderType;
        this.tranferType = tranferType;
        this.terminalCode = terminalCode;
        this.payBankCode = payBankCode;
        this.userCode = userCode;
        this.outterUserCode = outterUserCode;
        this.tradeMoney = tradeMoney;
        this.addDate = addDate;
        this.shouldDealDate = shouldDealDate;
        this.indeedDealDate = indeedDealDate;
        this.status = status;
        this.memo = memo;
        this.userName = userName;
        this.cellphone = cellphone;
        this.orderMoney = orderMoney;
        this.feeRate = feeRate;
    }

    /**
     * 默认构造函数
     */
    public PosOrder(Integer orderSrc, String orderCode, String userName, double orderMoney, int feeRate, double tradeMoney,
                    String outterUserCode) {
        this.orderSrc = orderSrc;
        this.orderCode = orderCode;
        this.userName = userName;
        this.orderMoney = orderMoney;
        this.feeRate = feeRate;
        this.tradeMoney = tradeMoney;
        this.outterUserCode = outterUserCode;
    }

}
