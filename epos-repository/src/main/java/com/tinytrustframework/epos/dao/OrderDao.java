package com.tinytrustframework.epos.dao;

import com.tinytrustframework.epos.common.utils.lang.page.Page;
import com.tinytrustframework.epos.entity.PosOrder;

import java.util.List;
import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
public interface OrderDao {
    /**
     * 查询订单详情
     *
     * @param orderSrc  订单来源
     * @param orderCode 订单编号
     */
    PosOrder getOrderDetail(int orderSrc, String orderCode);

    /**
     * 新增或编辑订单信息
     *
     * @param order 订单信息
     */
    void saveOrUpdateOrder(PosOrder order);

    /**
     * 查询订单信息列表
     *
     * @param businessParam 业务查询条件
     * @param pageParams    分页查询参数
     */
    Map<String, Object> queryOrderList(Map<String, Object> businessParam, Page pageParams);

    /**
     * 查询订单提交第三方充值
     *
     * @param tranferType 加款单到账类型
     */
    List<PosOrder> queryOrderForRecharge(int tranferType);
}
