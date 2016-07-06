package com.tinytrust.epos.service.impl;

import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.dao.OrderDao;
import com.tinytrust.epos.entity.PosOrder;
import com.tinytrust.epos.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    /**
     * 查询订单详情
     *
     * @param orderSrc  订单来源
     * @param orderCode 订单编号
     */
    public PosOrder getOrderDetail(int orderSrc, String orderCode) {
        return orderDao.getOrderDetail(orderSrc, orderCode);
    }

    /**
     * 新增或编辑订单信息
     *
     * @param order 订单信息
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveOrUpdateOrder(PosOrder order) {
        orderDao.saveOrUpdateOrder(order);
    }

    /**
     * 查询订单信息列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryOrderList(Map<String, Object> businessParams, Page pageParams) {
        return orderDao.queryOrderList(businessParams, pageParams);
    }


}
