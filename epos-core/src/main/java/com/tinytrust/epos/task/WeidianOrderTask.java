package com.tinytrust.epos.task;

/**
 * @author owen
 * @date 2016-07-07 07:17:45
 */
public interface WeidianOrderTask {

    /**
     * 令牌生成
     */
    void tokenGenerate();


    /**
     * 订单下载
     */
    void orderListQuery();


    /**
     * 订单详情查询
     */
    void orderDetailQuery();


}
