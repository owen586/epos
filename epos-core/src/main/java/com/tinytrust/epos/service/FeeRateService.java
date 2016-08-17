package com.tinytrust.epos.service;

import com.tinytrust.epos.model.FeeRateEntity;

/**
 * @author owen
 * @date 2016-08-04 04:17:38
 */
public interface FeeRateService {

    /**
     * 查询用户费率信息
     *
     * @param userCode 用戶编号
     */
    FeeRateEntity listFeeRate(String userCode);

}
