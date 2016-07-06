package com.tinytrust.epos.task;

/**
 * @author owen
 * @date 2016-07-06 06:13:29
 */
public interface RechargeTask {

    /**
     * T0类型订单加款充值
     */
    void rechargeForT0();


    /**
     * T1类型订单加款充值
     */
    void rechargeForT1();
}
