package com.tinytrust.epos.model;

import lombok.*;

/**
 * @author owen
 * @date 2016-08-04 04:17:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FeeRateEntity {

    // 当前用户费率
    private Integer feeRate;

    // 当前用户上级费率
    private Integer topUserFeeRateReturn;


}
