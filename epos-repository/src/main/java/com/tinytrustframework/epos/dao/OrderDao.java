package com.tinytrustframework.epos.dao;

import java.util.List;
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
public interface OrderDao {
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
     * <查询订单提交第三方充值>
     *
     *
     * @param tranferType 加款单到账类型
     * @return 待充值订单
     * @see [类、类#方法、类#成员]
     */
    List<PosOrder> queryOrderForRecharge(int tranferType);
}
