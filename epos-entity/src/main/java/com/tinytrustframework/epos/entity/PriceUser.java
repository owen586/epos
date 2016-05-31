package com.tinytrustframework.epos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户（密价）价格类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_price_user")
public class PriceUser implements java.io.Serializable {
    private static final long serialVersionUID = 3322482884652009248L;

    // 用户编号
    @Id
    @Column(name = "user_code", unique = true, nullable = false, length = 64)
    private String userCode;

    // 用户名称
    @Transient
    private String userName;

    // 手机号码
    @Transient
    private String cellphone;

    // 费率
    @Column(name = "fee_rate", nullable = false, precision = 4, scale = 3)
    private int feeRate;

    // 上级返点率
    @Column(name = "top_user_fee_rate_return", precision = 4, scale = 3)
    private int topUserFeeRateReturn;


    /**
     * 构造函数
     *
     * @param userCode             用户编号
     * @param feeRate              费率
     * @param topUserFeeRateReturn 上级返点费率
     * @param userName             用户名称
     * @param cellphone            手机号码
     */
    public PriceUser(String userCode, int feeRate, int topUserFeeRateReturn, String userName, String cellphone) {
        super();
        this.userCode = userCode;
        this.feeRate = feeRate;
        this.topUserFeeRateReturn = topUserFeeRateReturn;
        this.userName = userName;
        this.cellphone = cellphone;
    }

}
