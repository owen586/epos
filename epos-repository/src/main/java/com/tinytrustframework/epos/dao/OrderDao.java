package com.tinytrustframework.epos.dao;

import java.util.List;
import java.util.Map;

import com.tinytrustframework.epos.entity.PosOrder;

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
     * @param params   查询条件
     * @param pageNo   分页参数，当前页
     * @param pageSize 分页参数，每页记录数
     */
    Map<String, Object> queryOrderList(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * 查询订单提交第三方充值
     *
     * @param tranferType 加款单到账类型
     */
    List<PosOrder> queryOrderForRecharge(int tranferType);
}
