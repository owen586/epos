package com.tinytrustframework.epos.service.impl;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.common.utils.credit.CreditUtil;
import com.tinytrustframework.epos.dao.OrderDao;
import com.tinytrustframework.epos.dao.SystemDao;
import com.tinytrustframework.epos.entity.PosOrder;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <一句话功能简述>
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    /**
     * 注释内容
     */
    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 充值成功
     */
    private final String RECHARGE_SUCCESS = "ok";

    /**
     * 充值失败
     */
    private final String RECHARGE_FAIL = "fail";

    /**
     * OrderDao
     */
    private
    @Resource
    OrderDao orderDao;

    /**
     * SystemDao
     */
    private
    @Resource
    SystemDao systemDao;


    public PosOrder getOrderDetail(int orderSrc, String orderCode) {
        return orderDao.getOrderDetail(orderSrc, orderCode);
    }


    public void saveOrUpdateOrder(PosOrder order) {
        orderDao.saveOrUpdateOrder(order);
    }


    public Map<String, Object> queryOrderList(Map<String, Object> params, int pageNo, int pageSize) {
        return orderDao.queryOrderList(params, pageNo, pageSize);
    }

    /**
     * <查询订单提交第三方充值>
     *
     * @return 待充值订单
     * @see [类、类#方法、类#成员]
     */
    public List<PosOrder> queryOrderListForRecharge(int tranferType) {
        return orderDao.queryOrderForRecharge(tranferType);
    }

    /**
     * {@inheritDoc}
     */
    public void rechargeForT0() {
        List<PosOrder> orderListT0 = this.queryOrderListForRecharge(Constant.USER_TRANFER_TYPE_T0);
        this.recharger(Constant.USER_TRANFER_TYPE_T0, orderListT0);
    }

    /**
     * {@inheritDoc}
     */
    public void rechargeForT1() {
        List<PosOrder> orderListT1 = this.queryOrderListForRecharge(Constant.USER_TRANFER_TYPE_T1);
        this.recharger(Constant.USER_TRANFER_TYPE_T1, orderListT1);
    }

    /**
     * <加款充值>
     * <对于T+1到账类型的用户需要判断充值开关状态及是否在允许充值时间段>
     *
     * @param tranferType 到账类型
     * @param orderList
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private void recharger(int tranferType, List<PosOrder> orderList) {
        if (null == orderList || orderList.isEmpty()) {
            log.info((tranferType == Constant.USER_TRANFER_TYPE_T0 ? "T+0" : "T+1") + "暂无待充值加款订单!");
            return;
        }

        //T1充值用户需要判断当前是否为充值时间段
        if (tranferType == Constant.USER_TRANFER_TYPE_T1) {
            try {
                SystemConfig rechargeOpenSystemConfig = systemDao.querySystemConfig("1_sys_recharge_open");
                int rechargeOpenValue = Integer.parseInt(rechargeOpenSystemConfig.getSysValue());
                if (rechargeOpenValue == Constant.RECHARGE_OPEN_OFF)//充值开关关闭
                {
                    log.info("充值开关 关闭,T+1到账类型订单系统只接单不充值!");
                    return;
                }

                SystemConfig startTimeSysCfg = systemDao.querySystemConfig("2_sys_recharge_starttime");
                SystemConfig endTimeSysCfg = systemDao.querySystemConfig("3_sys_recharge_endtime");
                String startTime = startTimeSysCfg.getSysValue();
                String endTime = endTimeSysCfg.getSysValue();
                Date now = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
                String currentDay = dateFmt.format(now);//当天
                dateFmt.applyPattern(Constant.DATE_FORMAT_19);
                Date startTimeDate = dateFmt.parse(currentDay + " " + startTime);
                Date endTimeDate = dateFmt.parse(currentDay + " " + endTime);

                if (now.before(startTimeDate) || now.after(endTimeDate)) {
                    log.info("非充值时间段,T+1到账类型订单暂停充值!");
                    return;
                }

            } catch (ParseException pe) {
                log.error("时间格式化错误!");
                return;
            }
        }

        PosOrder rechargePosOrder = null;
        for (PosOrder order : orderList) {
            String orderCode = order.getOrderCode();//订单编号
            String userName = order.getUserName();//客户名称
            double orderMoney = order.getOrderMoney();//订单金额
            int feeRate = order.getFeeRate();//费率,千分之
            double tradeMoney = order.getTradeMoney();//实际加款金额
            String outterUserCode = order.getOutterUserCode();//第三方用户编号

            rechargePosOrder = orderDao.getOrderDetail(Constant.ORDER_SOURCE_POS, orderCode);

            log.info("加款充值开始! 订单编号:{},客户名称:{},外部商户编号:{},订单金额:{},费率：{}‱,结算金额:{}", new Object[]{
                    orderCode, userName, outterUserCode, orderMoney, feeRate, tradeMoney});


            if (StringUtils.isEmpty(outterUserCode)) {
                log.error("外部商户编号为空! 订单编号: {}", orderCode);
                continue;
            }

            rechargePosOrder.setStatus(Constant.ORDER_STATE_DEALING);
            rechargePosOrder.setMemo("充值中");
            orderDao.saveOrUpdateOrder(rechargePosOrder);

            Map<String, Object> rechargeResponse =
                    CreditUtil.recharge(orderCode, outterUserCode, String.valueOf(tradeMoney));
            if (null == rechargeResponse) {
                log.info("加款充值网络异常,默认充值成功! 订单编号: {}", orderCode);

                rechargePosOrder.setStatus(Constant.ORDER_STATE_SUCCESS);
                rechargePosOrder.setMemo("充值成功(WLYC)");
                rechargePosOrder.setIndeedDealDate(new Date(System.currentTimeMillis()));//订单实际处理时间
                orderDao.saveOrUpdateOrder(rechargePosOrder);
            } else {
                String result = rechargeResponse.get("result").toString();
                if (RECHARGE_SUCCESS.equals(result)) {
                    rechargePosOrder.setStatus(Constant.ORDER_STATE_SUCCESS);
                    rechargePosOrder.setIndeedDealDate(new Date(System.currentTimeMillis()));//订单实际处理时间
                    Map<String, Object> creditData = (Map<String, Object>) rechargeResponse.get("data");
                    String billId = creditData.get("billid").toString();
                    rechargePosOrder.setMemo("加款成功 " + billId);
                    orderDao.saveOrUpdateOrder(rechargePosOrder);

                    log.info("加款充值成功! 订单编号: {}", orderCode);
                } else if (RECHARGE_FAIL.equals(result)) {
                    rechargePosOrder.setStatus(Constant.ORDER_STATE_FIAL);
                    rechargePosOrder.setIndeedDealDate(new Date(System.currentTimeMillis()));//订单实际处理时间
                    rechargePosOrder.setMemo("加款失败");
                    orderDao.saveOrUpdateOrder(rechargePosOrder);
                    String message = rechargeResponse.get("msg").toString();

                    log.info("加款充值失败! 订单编号: {},错误信息：{}", orderCode, message);
                }
            }
        }
    }
}