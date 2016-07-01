package com.tinytrustframework.epos.web.controller;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.common.utils.props.PropUtils;
import com.tinytrustframework.epos.entity.PosOrder;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.OrderService;
import com.tinytrustframework.epos.service.SystemService;
import com.tinytrustframework.epos.web.controller.rsp.CommonRsp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 微店订单,手动订单
 *
 * @author owen
 * @date 2016-07-01 01:15:58
 */
@RequestMapping(value = "/order/weidian")
public class OrderVdianController extends BaseController {

    @Resource
    private SystemService systemService;

    @Resource
    private OrderService orderService;

    /**
     * 转发至查询微店订单列表页面
     */
    @RequestMapping(value = "/list/index")
    public String listVdianOrderIndex() {
        return "order/weidian_order_list";
    }

    /**
     * 查询微店订单列表
     *
     * @param order   线下订单查询表单
     * @param request HttpServletRequest
     */
    // TODO: 16/7/1
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp listVdianOrder(PosOrder order, HttpServletRequest request) {
        return null;
    }

    /**
     * 转发至新增微店加款订单页面
     */
    @RequestMapping(value = "/save/fwd", method = RequestMethod.GET)
    public String saveOrderFwd() {
        return "order/offline_order_add";
    }

    /**
     * 新增微店加款订单(手动)
     *
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp saveOrder(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        if (null == user) {
            log.error("新增订单失败, 用户信息为空!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,用户信息为空!").build();
        }

        int roleCode = user.getRoleCode();
        if (roleCode != Constant.ROLE_TYPE_ADMIN) {
            log.error("新增订单失败,无新增订单权限!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,无新增订单权限!").build();
        }

        String outterUserCode = request.getParameter("outterUserCode");//外部用户编号
        String terminalCode = request.getParameter("terminalCode");//终端（支付通）编号
        String orderMoney = request.getParameter("orderMoney");//加款金额
        String tranferType = request.getParameter("tranferType");//到账类型

        if (StringUtils.isEmpty(outterUserCode) || StringUtils.isEmpty(terminalCode) || StringUtils.isEmpty(orderMoney)
                || StringUtils.isEmpty(tranferType)) {
            log.error("新增订单失败,加款信息不完整!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,加款信息不完整!").build();
        }

        PosOrder order = new PosOrder();
        order.setOrderSrc(Constant.ORDER_SOURCE_HAND);
        order.setOrderCode("H" + System.currentTimeMillis());
        order.setOrderType(Constant.ORDER_TYPE_COMMON);
        order.setTerminalCode(terminalCode);
        order.setOutterUserCode(outterUserCode.toUpperCase());
        order.setOrderMoney(Double.valueOf(orderMoney));
        //查询线下订单费率 （‱）
        SystemConfig systemConfig = systemService.querySystemConfig("6_sys_manual_operation_rate");
        if (null == systemConfig) {
            log.error("新增订单失败,线下加款订单费率为设置!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,线下加款订单费率为设置!").build();
        }
        int offlineRate = Integer.parseInt(systemConfig.getSysValue());
        order.setFeeRate(offlineRate);
        BigDecimal feeRateBigDecimal =
                this.getBigDecimal(String.valueOf(offlineRate))
                        .divide(new BigDecimal(10000))
                        .setScale(3, RoundingMode.HALF_UP);

        BigDecimal orderMoneyBigDecimal = this.getBigDecimal(orderMoney).setScale(3, RoundingMode.HALF_UP);// 订单加款金额，由分转换为元
        BigDecimal settlementMoneyBigDecimal =
                orderMoneyBigDecimal.multiply(feeRateBigDecimal).setScale(3, RoundingMode.HALF_UP);
        order.setTradeMoney(settlementMoneyBigDecimal.doubleValue());// 到账结算金额

        Date addDate = new Date(System.currentTimeMillis());
        order.setAddDate(addDate);// 订单入库时间
        Date shouldDealDate = null;
        if (Integer.parseInt(tranferType) == Constant.USER_TRANFER_TYPE_T0) {
            shouldDealDate =
                    DateUtils.addMinutes(addDate, Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t0")));

        } else if (Integer.parseInt(tranferType) == Constant.USER_TRANFER_TYPE_T1)// T+1
        {
            shouldDealDate =
                    DateUtils.addMinutes(DateUtils.addHours(addDate, 24),
                            Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t1")));
        }
        order.setShouldDealDate(shouldDealDate);
        order.setStatus(Constant.ORDER_STATE_WAITING);
        order.setMemo("待处理");

        orderService.saveOrUpdateOrder(order);
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("新增线下加款订单成功").build();
    }
}
