/*
 * 文 件 名:  OrderService.java
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2015-7-28
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.service;

import java.util.Map;

import com.tinytrustframework.epos.entity.PosOrder;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface OrderService {
    /**
     * <查询订单详情>
     *
     *
     * @param orderSrc  订单来源
     * @param orderCode 订单编号
     * @return
     * @see [类、类#方法、类#成员]
     */
    PosOrder getOrderDetail(int orderSrc, String orderCode);

    /**
     * <新增或编辑订单信息>
     *
     *
     * @param order 订单信息
     * @see [类、类#方法、类#成员]
     */
    void saveOrUpdateOrder(PosOrder order);

    /**
     * <查询角色信息列表>
     *
     *
     * @param params   查询条件
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    Map<String, Object> queryOrderList(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * <T0类型订单加款充值>
     *
     *
     * @see [类、类#方法、类#成员]
     */
    void rechargeForT0();


    /**
     * <T1类型订单加款充值>
     *
     *
     * @see [类、类#方法、类#成员]
     */
    void rechargeForT1();


}
