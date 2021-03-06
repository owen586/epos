package com.tinytrust.epos.dao.impl;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.common.utils.lang.page.PageUtil;
import com.tinytrust.epos.dao.BaseDao;
import com.tinytrust.epos.dao.OrderDao;
import com.tinytrust.epos.entity.PosOrder;
import com.tinytrust.epos.entity.SystemConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao {

    /**
     * 查询订单详情
     *
     * @param orderSrc  订单来源
     * @param orderCode 订单编号
     */
    public PosOrder getOrderDetail(int orderSrc, String orderCode) {
        String hql = "from PosOrder o where o.orderSrc = :orderSrc and o.orderCode = :orderCode";
        List<PosOrder> orderList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"orderSrc", "orderCode"},
                        new Object[]{orderSrc, orderCode});
        if (null != orderList && !orderList.isEmpty()) {
            return orderList.get(0);
        }
        return null;
    }

    /**
     * 新增或编辑订单信息
     *
     * @param order 订单信息
     */
    public void saveOrUpdateOrder(PosOrder order) {
        this.hibernateTemplate.saveOrUpdate(order);
    }

    /**
     * 查询订单信息列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryOrderList(final Map<String, Object> businessParams, final Page pageParams) {
        return this.hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql =
                        "select new PosOrder(o.orderCode,o.orderType,u.tranferType,o.terminalCode,o.payBankCode,o.userCode,"
                                + "o.outterUserCode,o.tradeMoney,o.addDate,o.shouldDealDate,o.indeedDealDate,"
                                + "o.status,o.memo,u.userName,u.cellphone,o.orderMoney,o.feeRate) from PosOrder o,User u where o.userCode = u.userCode ";

                // 订单来源
                if (businessParams.containsKey("orderSrc")) {
                    hql += " and o.orderSrc in :orderSrc";
                }

                //订单编号
                if (businessParams.containsKey("orderCode")) {
                    hql += " and o.orderCode like :orderCode";
                }

                //订单类型
                if (businessParams.containsKey("orderType")) {
                    hql += " and o.orderType = :orderType";
                }

                //到账类型
                if (businessParams.containsKey("tranferType")) {
                    hql += " and u.tranferType = :tranferType";
                }

                //订单状态
                if (businessParams.containsKey("status")) {
                    hql += " and o.status = :status";
                }

                //终端编号
                if (businessParams.containsKey("terminalCode")) {
                    hql += " and o.terminalCode = :terminalCode";
                }

                //用户编号
                if (businessParams.containsKey("userCode")) {
                    hql += " and u.userCode = :userCode";
                }

                //外部用户编号
                if (businessParams.containsKey("outterUserCode")) {
                    hql += " and u.outterUserCode = :outterUserCode";
                }

                //手机号
                if (businessParams.containsKey("cellphone")) {
                    hql += " and u.cellphone = :cellphone";
                }

                //用户名称
                if (businessParams.containsKey("userName")) {
                    hql += " and u.userName like :userName";
                }

                //订单入库时间
                if (businessParams.containsKey("startTime") && businessParams.containsKey("endTime")) {
                    hql += " and o.addDate between :startTime and :endTime";
                } else if (businessParams.containsKey("startTime") && !businessParams.containsKey("endTime")) {
                    hql += " and o.addDate >= :startTime";
                } else if (!businessParams.containsKey("startTime") && businessParams.containsKey("endTime")) {
                    hql += " and o.addDate <= :endTime";
                }

                hql += " order by o.addDate desc";
                return PageUtil.hqlQuery(session, hql, businessParams, pageParams);
            }
        });
    }

    /**
     * 根据到账类型查询待重置订单列表
     *
     * @param tranferType 到账类型
     */
    public List<PosOrder> queryOrderForRecharge(int tranferType) {
        String orderHql = null;
        //T+0到账类型
        if (Constant.USER_TRANFER_TYPE_T0 == tranferType) {
            String systemConfigHqlT0 = "FROM SystemConfig c WHERE c.sysCode = '5_sys_order_query_minutes_limit'";
            List<SystemConfig> systemConfig = this.hibernateTemplate.find(systemConfigHqlT0);
            int intervalMinute = Integer.parseInt(systemConfig.get(0).getSysValue());
            orderHql =
                    "SELECT new PosOrder(o.orderSrc,o.orderCode,u.userName,o.orderMoney,o.feeRate,o.tradeMoney,o.outterUserCode)"
                            + " FROM PosOrder o,User u" + " WHERE o.userCode = u.userCode"
                            + " AND o.tradeMoney > 0" //结算金额大于0
                            + " AND u.status = 2" //客户状态正常
                            + " AND o.status = 1 AND u.tranferType =" + Constant.USER_TRANFER_TYPE_T0// status:1 待处理  ,tranferType:1 T+1
                            + " AND ABS(UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(o.shouldDealDate)) >=" + intervalMinute + " * 60";
            return this.hibernateTemplate.find(orderHql);
        } else if (Constant.USER_TRANFER_TYPE_T1 == tranferType) //T+1到账类型
        {
            return this.hibernateTemplate.execute(new HibernateCallback<List<PosOrder>>() {

                public List<PosOrder> doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    String systemConfigHqlT1 = "FROM SystemConfig c WHERE c.sysCode = '4_sys_order_query_days_limit'";
                    List<SystemConfig> systemConfig = session.createQuery(systemConfigHqlT1).list();
                    int intervalDay = Integer.parseInt(systemConfig.get(0).getSysValue());
                    String orderSql =
                            "SELECT o.order_code,u.user_name,o.order_money,o.fee_rate,o.trade_money,o.outter_user_code,o.order_src"
                                    + " FROM t_pos_order o,t_pos_user u" + " WHERE o.user_code = u.user_code"
                                    + " AND o.status = 1 AND u.tranfer_type ="
                                    + Constant.USER_TRANFER_TYPE_T1 // status:1 待处理  ,tranferType:2 T+1
                                    + " AND o.trade_money > 0" //结算金额大于0
                                    + " AND u.status = 2" //客户状态正常
                                    + " AND o.should_deal_date <= TIMESTAMP(DATE(NOW()))" //今天之前
                                    + " AND o.should_deal_date >= DATE_SUB(TIMESTAMP(DATE(NOW()))," + " INTERVAL " + intervalDay
                                    + " DAY)";

                    List<Object[]> orderList = session.createSQLQuery(orderSql).list();
                    List<PosOrder> posOrderList = null;
                    if (null != orderList && !orderList.isEmpty()) {
                        posOrderList = new ArrayList<PosOrder>(orderList.size());
                        for (Object[] obj : orderList) {
                            posOrderList.add(new PosOrder((Integer)obj[6],String.valueOf(obj[0]), String.valueOf(obj[1]),
                                    new BigDecimal(String.valueOf(obj[2])).doubleValue(), (Integer) obj[3], new BigDecimal(
                                    String.valueOf(obj[4])).doubleValue(), String.valueOf(obj[5])));
                        }
                    }
                    return posOrderList;
                }
            });
        }

        return null;

    }
}
