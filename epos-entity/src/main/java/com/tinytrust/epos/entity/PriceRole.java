package com.tinytrust.epos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 角色价格类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_price_role")
public class PriceRole implements java.io.Serializable {

    private static final long serialVersionUID = 3767255656333156175L;

    // 角色编号
    @Id
    @Column(name = "role_code", unique = true, nullable = false)
    private Integer roleCode;

    // 角色名称
    @Transient
    private String roleName;

    // 费率
    @Column(name = "fee_rate", nullable = false, precision = 5, scale = 4)
    private Integer feeRate;

    // 上级返点率
    @Column(name = "top_user_fee_rate_return", nullable = true, precision = 5, scale = 4)
    private Integer topUserFeeRateReturn;
}
