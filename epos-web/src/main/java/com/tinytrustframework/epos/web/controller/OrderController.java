package com.tinytrustframework.epos.web.controller;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.common.utils.lang.DateUtil;
import com.tinytrustframework.epos.common.utils.lang.DigestUtil;
import com.tinytrustframework.epos.common.utils.props.PropUtils;
import com.tinytrustframework.epos.entity.*;
import com.tinytrustframework.epos.web.controller.req.YSNotifyReq;
import com.tinytrustframework.epos.web.controller.rsp.CommonRsp;
import com.tinytrustframework.epos.web.controller.rsp.YSNotifyRsp;
import com.tinytrustframework.epos.service.OrderService;
import com.tinytrustframework.epos.service.PriceService;
import com.tinytrustframework.epos.service.SystemService;
import com.tinytrustframework.epos.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单控制处理类
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @Resource
    private PriceService priceService;

    @Resource
    private SystemService systemService;

    /**
     * 转发至订单列表页面
     */
    @RequestMapping(value = "/online/list/index")
    public String onlineOrderListIndex() {
        return "order/online_order_list";
    }

    /**
     * 订单列表查询
     *
     * @param order   订单查询表单
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/online/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp onlineOrderList(PosOrder order, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        String orderCode = order.getOrderCode();// 订单编号
        if (StringUtils.isNotEmpty(orderCode)) {
            params.put("orderCode", "%" + orderCode + "%");
        }

        int orderType = order.getOrderType();// 订单类型
        if (orderType != -1) {
            params.put("orderType", orderType);
        }

        int tranferType = order.getTranferType();
        if (tranferType != -1) {
            params.put("tranferType", tranferType);
        }

        int status = order.getStatus();// 订单状态
        if (status != -1) {
            params.put("status", status);
        }

        String terminalCode = order.getTerminalCode();// 终端编号
        if (StringUtils.isNotEmpty(terminalCode)) {
            params.put("terminalCode", terminalCode);
        }

        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        int roleCode = user.getRoleCode();// 角色编号
        if (roleCode == Constant.ROLE_TYPE_ADMIN) {
            String userCode = order.getUserCode();// 用户编号
            if (StringUtils.isNotEmpty(userCode)) {
                params.put("userCode", userCode);
            }
        } else {
            // 一、二级经销商只能查自己的订单信息
            String loginUserCode = user.getUserCode();
            params.put("userCode", loginUserCode);
        }

        String outterUserCode = order.getOutterUserCode();// 外部用户编号
        if (StringUtils.isNotEmpty(outterUserCode)) {
            params.put("outterUserCode", outterUserCode);
        }

        String userName = order.getUserName();// 用户名称
        if (StringUtils.isNotEmpty(userName)) {
            params.put("userName", userName);
        }

        String cellphone = order.getCellphone();// 手机号
        if (StringUtils.isNotEmpty(cellphone)) {
            params.put("cellphone", cellphone);
        }

        String startTime = request.getParameter("startTime");// 下单起始时间
        if (!StringUtils.isEmpty(startTime)) {
            params.put("startTime", DateUtil.parse(startTime, Constant.DATE_FORMAT_19));
        }

        String endTime = request.getParameter("endTime");// 下单结束时间
        if (!StringUtils.isEmpty(endTime)) {
            params.put("endTime", DateUtil.parse(endTime, Constant.DATE_FORMAT_19));
        }
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        try {
            Map<String, Object> dataMap = orderService.queryOrderList(params, pageNo, pageSize);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询订单列表信息成功").dataMap(dataMap).build();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonRsp.builder().result(this.RESULT_FAIL).message("查询订单列表信息失败").build();
        }
    }

    /**
     * 订单回调通知
     *
     * @param yinShengOrder 银盛POS订单
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public YSNotifyRsp notify(@RequestBody YSNotifyReq yinShengOrder) {
        String ysOrderid = yinShengOrder.getYsOrderid();// 银盛订单号
        String orderid = yinShengOrder.getOrderid();// 商户订单号
        String status = yinShengOrder.getStatus();// 状态 1、消费成功 ；0、消费失败
        String money = yinShengOrder.getMoney();// 实际消费金额，单位:分
        String time = yinShengOrder.getTime();// 消费日期yyyyMMddHHmmss
        String remark = yinShengOrder.getRemark();// 备注
        String checkValue = yinShengOrder.getCheckValue();// 校验值

        log.info("银盛订单通知! ysOrderid:{},orderid:{},status:{},money:{},time:{},remark:{},checkValue:{}", ysOrderid, orderid, status, money, time
                , remark, checkValue);

        // 必要字段校验
        if (StringUtils.isEmpty(ysOrderid) || StringUtils.isEmpty(orderid) || StringUtils.isEmpty(status)
                || StringUtils.isEmpty(money) || StringUtils.isEmpty(time) || StringUtils.isEmpty(remark)
                || StringUtils.isEmpty(checkValue)) {
            log.error("参数不完整!  ysOrderid:{},orderid:{},status:{},money:{},time:{},remark:{},checkValue:{}", ysOrderid, orderid, status, money, time
                    , remark, checkValue);
            return YSNotifyRsp.builder().status("参数不完整").build();
        }

        // 签名校验
        String sign =
                DigestUtil.md5(ysOrderid + orderid + status + money + time + remark
                        + PropUtils.getPropertyValue("ys.order.notify.key"));
        if (!sign.equals(checkValue)) {
            log.error("签名错误! checkValue:{},sign:{},source:{}", checkValue, sign, ysOrderid + orderid + status + money
                    + time + remark + PropUtils.getPropertyValue("ys.order.notify.key"));
            return YSNotifyRsp.builder().status("签名错误").build();
        }

        // 解析备注remark
        // V1 "卡号|发卡行|终端号|银盛系统交易参考号" @Deprecated
        // V2 "业务标识|业务代码|商户日期|卡号|发卡行|终端号|银盛系统交易参考号"
        // 13|00490004|20150824114751|6228480011006182619|农业银行|A1100101|242464013166
        // 0 1 2 3 4 5 6
        String[] remarks = remark.split("\\|");
        String payBankCode = remarks[3];// 支付卡号
        // String payBankName = remarks[4];//发卡行
        String terminalCode = remarks[5];// 终端号
        String ysSysTradeCode = remarks[6];// 银盛系统交易参考号
        try {
            // 订单重复性检查
            PosOrder order = orderService.getOrderDetail(Constant.ORDER_SOURCE_POS, ysOrderid);
            if (null != order) {
                log.error("POS订单已存在");
                return YSNotifyRsp.builder().status("POS订单已存在").build();
            }

            // 终端检查
            Terminal terminal = userService.getTerminalDetailByTerminalCode(terminalCode);
            if (null == terminal) {
                log.error("终端不存在");
                return YSNotifyRsp.builder().status("终端不存在").build();
            }

            String userCode = terminal.getUserCode();// 用户编号
            User user = userService.getUserDetail(userCode);
            int tranferType = user.getTranferType();// 用户到账类型
            String topUserCode = user.getTopUserCode();// 上级用户编号
            // 根据用户编号查询用户密价信息
            PriceUser priceUser = priceService.getPriceUserDetail(userCode);
            int feeRate = -1, topUserFeeRateReturn = -1;// 当前用户到账费率‰,上级用户返点费率‰
            if (null != priceUser) {
                feeRate = priceUser.getFeeRate();
                topUserFeeRateReturn = priceUser.getTopUserFeeRateReturn();
            } else {
                // 根据角色查询用户设价信息
                int roleCode = user.getRoleCode();// 角色编号
                PriceRole priceRole = priceService.getPriceRoleDetail(roleCode);
                feeRate = priceRole.getFeeRate();
                topUserFeeRateReturn = priceRole.getTopUserFeeRateReturn();
            }

            BigDecimal feeRateBigDecimal =
                    this.getBigDecimal(String.valueOf(feeRate))
                            .divide(new BigDecimal(10000))
                            .setScale(3, RoundingMode.HALF_UP);//费率变更为‱
            BigDecimal orderMoneyBigDecimal =
                    this.getBigDecimal(money).divide(new BigDecimal(100)).setScale(3, RoundingMode.HALF_UP);// 订单加款金额，由分转换为元
            BigDecimal settlementMoneyBigDecimal =
                    orderMoneyBigDecimal.multiply(feeRateBigDecimal).setScale(3, RoundingMode.HALF_UP);// 到账结算金额

            PosOrder mainOrder = new PosOrder();
            mainOrder.setOrderSrc(Constant.ORDER_SOURCE_POS);// 订单来源
            mainOrder.setOrderCode(ysOrderid);// 银盛订单编号
            mainOrder.setOrderType(Constant.ORDER_TYPE_COMMON);// 普通订单
            mainOrder.setUserCode(userCode);// 用户编号
            mainOrder.setUserName(user.getUserName());// 用户名称
            mainOrder.setCellphone(user.getCellphone());// 手机号
            mainOrder.setOrderMoney(orderMoneyBigDecimal.doubleValue());// 订单金额
            mainOrder.setFeeRate(feeRate);// 到账费率
            mainOrder.setTradeMoney(settlementMoneyBigDecimal.doubleValue());// 结算金额
            mainOrder.setOutterUserCode(user.getOutterUserCode());// 外部商家编号
            Date addDate = new Date(System.currentTimeMillis());
            mainOrder.setAddDate(addDate);// 订单入库时间

            Date shouldDealDate = null;
            if (tranferType == Constant.USER_TRANFER_TYPE_T0) {
                shouldDealDate =
                        DateUtils.addMinutes(addDate,
                                Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t0")));

            } else if (tranferType == Constant.USER_TRANFER_TYPE_T1)// T+1
            {
                shouldDealDate =
                        DateUtils.addMinutes(DateUtils.addHours(addDate, 24),
                                Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t1")));
            }
            mainOrder.setShouldDealDate(shouldDealDate);

            mainOrder.setStatus(Constant.ORDER_STATE_WAITING);
            mainOrder.setTerminalCode(terminalCode);
            mainOrder.setBatchCode(ysSysTradeCode);// 银盛系统交易参考号
            mainOrder.setMemo("待处理");
            mainOrder.setPayBankCode(payBankCode);// 支付卡号
            orderService.saveOrUpdateOrder(mainOrder);

            // 非直属下级需要跟上级设置返点
            if (!topUserCode.equals(PropUtils.getPropertyValue("top.user.code.default"))) {
                // 上级返点费率
                BigDecimal topUserFeeRateReturnBigDecimal =
                        this.getBigDecimal(String.valueOf(topUserFeeRateReturn))
                                .divide(new BigDecimal(10000))
                                .setScale(3, RoundingMode.HALF_UP);
                BigDecimal settlementMoneyReturnBigDecimal =
                        orderMoneyBigDecimal.multiply(topUserFeeRateReturnBigDecimal).setScale(3, RoundingMode.HALF_UP);// 到账结算金额

                //返点金额大于0才生成返点订单
                if (settlementMoneyReturnBigDecimal.compareTo(new BigDecimal(0)) > 0) {
                    // 根据上级用户编号查询用户信息
                    User topUser = userService.getUserDetail(topUserCode);
                    PosOrder subOrder = new PosOrder();
                    String subOrderCode = ysOrderid + "_1";
                    subOrder.setOrderSrc(Constant.ORDER_SOURCE_POS);// 订单来源
                    subOrder.setOrderCode(subOrderCode);// 银盛订单编号
                    subOrder.setOrderType(Constant.ORDER_TYPE_RETURN);// 返点订单
                    subOrder.setUserCode(topUser.getUserCode());// 上级用户编号
                    subOrder.setUserName(topUser.getUserName());// 用户名称
                    subOrder.setCellphone(topUser.getCellphone());// 手机号
                    subOrder.setOrderMoney(orderMoneyBigDecimal.doubleValue());// 订单金额
                    subOrder.setFeeRate(topUserFeeRateReturn);// 上级返点费率
                    subOrder.setTradeMoney(settlementMoneyReturnBigDecimal.doubleValue());// 结算金额
                    subOrder.setOutterUserCode(topUser.getOutterUserCode());// 外部商家编号
                    subOrder.setAddDate(addDate);// 订单入库时间
                    subOrder.setShouldDealDate(shouldDealDate);// 订单应处理时间
                    subOrder.setStatus(Constant.ORDER_STATE_WAITING);
                    subOrder.setTerminalCode(terminalCode);
                    subOrder.setBatchCode(ysSysTradeCode);// 银盛系统交易参考号
                    subOrder.setMemo("待处理");
                    subOrder.setPayBankCode(payBankCode);// 支付卡号

                    orderService.saveOrUpdateOrder(subOrder);// 保存上级返点订单
                }
            }
            return YSNotifyRsp.builder().status("ok").build();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("银盛订单通知接口异常!  异常信息:{},ysOrderid:{},orderid:{},status:{},money:{},time:{},remark:{},checkValue:{}", e.getMessage(), ysOrderid, orderid, status, money, time
                    , remark, checkValue);
            return YSNotifyRsp.builder().status("系统异常").build();
        }
    }

    /**
     * 设置对应值value的BigDecimal值,保存3位小数
     *
     * @param value 数字字符串
     */
    private BigDecimal getBigDecimal(String value) {
        MathContext mc = new MathContext(value.length());// 设置有效数字
        return new BigDecimal(value, mc).setScale(3, RoundingMode.HALF_UP);
    }

    /**
     * 转发至线下订单列表
     */
    @RequestMapping(value = "/offline/list/index")
    public String offlineOrderListIndex() {
        return "order/offline_order_list";
    }

    /**
     * 线下订单列表查询
     *
     * @param order   线下订单查询表单
     * @param request HttpServletRequest
     */
    //TODO
    @RequestMapping(value = "/offline/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp offlineOrderList(PosOrder order, HttpServletRequest request) {
        return null;
    }

    /**
     * 转发至新增线下加款订单页面
     */
    @RequestMapping(value = "/offline/save/fwd", method = RequestMethod.GET)
    public String saveOrderFwd() {
        return "order/offline_order_add";
    }

    /**
     * 新增线下加款订单
     *
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/offline/save", method = RequestMethod.POST)
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
        order.setOrderSrc(Constant.ORDER_SOURCE_OFFLINE);
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
