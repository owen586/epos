package com.tinytrust.epos.service.impl;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.entity.PriceRole;
import com.tinytrust.epos.entity.PriceUser;
import com.tinytrust.epos.entity.SystemConfig;
import com.tinytrust.epos.entity.User;
import com.tinytrust.epos.model.FeeRateEntity;
import com.tinytrust.epos.service.FeeRateService;
import com.tinytrust.epos.service.PriceService;
import com.tinytrust.epos.service.SystemService;
import com.tinytrust.epos.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author owen
 * @date 2016-08-04 04:17:49
 */
@Service
public class FeeRateServiceImpl implements FeeRateService {

    @Resource
    private PriceService priceService;

    @Resource
    private SystemService systemService;

    @Resource
    private UserService userService;

    /**
     * 查询用户费率信息
     *
     * @param userCode 用戶编号
     */
    public FeeRateEntity listFeeRate(String userCode) {

        // 查询用户信息
        User targetUser = userService.getUserDetail(userCode);

        SystemConfig systemConfig = systemService.querySystemConfig("6_sys_order_hand_feerate_open");

        // 设置费率
        Integer feeRate = null, topUserFeeRateReturn = null;// 当前用户到账费率,上级用户返点费率
        if (Integer.parseInt(systemConfig.getSysValue()) == Constant.WEIDIAN_ORDER_FEERATE_OPEN) {

            // 用户密价查询
            PriceUser priceUser = priceService.getPriceUserDetail(userCode);
            if (null != priceUser) {
                feeRate = priceUser.getFeeRate();
                topUserFeeRateReturn = priceUser.getTopUserFeeRateReturn();
            } else {
                // 用户角色价格查询

                PriceRole priceRole = priceService.getPriceRoleDetail(targetUser.getRoleCode());
                feeRate = priceRole.getFeeRate();
                topUserFeeRateReturn = priceRole.getTopUserFeeRateReturn();
            }
        } else {
            feeRate = 10000;//为设置费率,足额到账
        }

        return FeeRateEntity.builder()
                .feeRate(feeRate)
                .topUserFeeRateReturn(topUserFeeRateReturn)
                .build();
    }

}
