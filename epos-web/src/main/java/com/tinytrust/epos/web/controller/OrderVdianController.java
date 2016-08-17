package com.tinytrust.epos.web.controller;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.lang.DateUtil;
import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.common.utils.lang.props.PropUtils;
import com.tinytrust.epos.entity.PosOrder;
import com.tinytrust.epos.entity.SystemConfig;
import com.tinytrust.epos.entity.User;
import com.tinytrust.epos.model.FeeRateEntity;
import com.tinytrust.epos.service.FeeRateService;
import com.tinytrust.epos.service.OrderService;
import com.tinytrust.epos.service.SystemService;
import com.tinytrust.epos.service.UserService;
import com.tinytrust.epos.web.controller.rsp.CommonRsp;
import lombok.extern.slf4j.Slf4j;
import nl.captcha.Captcha;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微店订单,手动订单
 *
 * @author owen
 * @date 2016-07-01 01:15:58
 */
@Slf4j
@Controller
@RequestMapping(value = "/order/weidian")
public class OrderVdianController extends BaseController {

    @Resource
    private SystemService systemService;

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @Resource
    private FeeRateService feeRateService;

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
        Map<String, Object> params = new HashMap<String, Object>();

        // 订单编号
        String orderCode = order.getOrderCode();
        if (StringUtils.isNotEmpty(orderCode)) {
            params.put("orderCode", "%" + orderCode + "%");
        }

        // 订单状态
        int status = order.getStatus();
        if (status != -999) {
            params.put("status", status);
        }

        // 订单数据权限过滤
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        String currentUserId = user.getIdCode();
        int roleCode = user.getRoleCode();//角色编号
        if (roleCode == Constant.ROLE_TYPE_ADMIN) {
            currentUserId = order.getUserCode();
            if (StringUtils.isNotEmpty(currentUserId)) {
                params.put("userCode", currentUserId);
            }
        } else {
            // 一、二级经销商只能查自己的订单信息
            params.put("userCode", currentUserId);
        }

        String outterUserCode = order.getOutterUserCode();// 外部用户编号
        if (StringUtils.isNotEmpty(outterUserCode)) {
            params.put("outterUserCode", outterUserCode);
        }

        String userName = order.getUserName();// 用户名称
        if (StringUtils.isNotEmpty(userName)) {
            params.put("userName", userName);
        }


        String terminalCode = order.getTerminalCode();// 终端编号
        if (StringUtils.isNotEmpty(terminalCode)) {
            params.put("terminalCode", terminalCode);
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

        // 订单来源
        params.put("orderSrc", new ArrayList<Integer>() {
            {
                add(Constant.ORDER_SOURCE_WEIDIAN);//微店
                add(Constant.ORDER_SOURCE_HAND);//手动

            }
        });

        try {
            // 分页信息
            int pageNo = Integer.parseInt(request.getParameter("pageNo"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));

            Map<String, Object> dataMap = orderService.queryOrderList(params, Page.builder().pageStart(pageNo).pageSize(pageSize).build());
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询订单列表信息成功").dataMap(dataMap).build();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonRsp.builder().result(this.RESULT_FAIL).message("查询订单列表信息失败").build();
        }
    }

    /**
     * 转发至新增微店加款订单页面
     */
    @RequestMapping(value = "/save/fwd", method = RequestMethod.GET)
    public String saveOrderFwd() {
        return "order/weidian_order_edit";
    }

    /**
     * 新增微店加款订单(手动)
     *
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp saveOrder(HttpServletRequest request) {

        String userCode = request.getParameter("userCode");//用户编号
        String terminalCode = request.getParameter("terminalCode");//终端（支付通）编号
        String orderMoney = request.getParameter("orderMoney");//加款金额
        String captcha = request.getParameter("captcha");//验证码

        if (StringUtils.isAnyEmpty(userCode, terminalCode, orderMoney, captcha)) {
            log.error("新增线下订单失败,加款信息不完整!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,加款信息不完整!").build();
        }

        HttpSession session = request.getSession();
        Captcha captchaInSession = (Captcha) session.getAttribute(Captcha.NAME);
        if (!captchaInSession.
                isCorrect(captcha.toLowerCase().trim())) {
            log.error("新增订单失败,验证码错误!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,验证码错误!").build();
        }

        User currentUser = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        if (null == currentUser) {
            log.error("新增订单失败, 用户信息为空!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,用户信息为空!").build();
        }

        int roleCode = currentUser.getRoleCode();
        if (roleCode != Constant.ROLE_TYPE_ADMIN) {
            log.error("新增订单失败,无新增订单权限!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,无新增订单权限!").build();
        }

        // 查询目标对象欧飞编号
        User targetUser = userService.getUserDetail(userCode);//目标
        String outterUserCode = StringUtils.isEmpty(targetUser.getOutterUserCode()) ? targetUser.getUserName() : targetUser.getOutterUserCode();


        // 生成订单
        PosOrder order = new PosOrder();
        order.setOrderSrc(Constant.ORDER_SOURCE_HAND);
        String orderId = PropUtils.getPropertyValue("order.prefix") + System.currentTimeMillis() + RandomUtils.nextLong(10000, 99999);//UUID.randomUUID().toString();
        order.setOrderCode(orderId);
        order.setOrderType(Constant.ORDER_TYPE_COMMON);
        order.setTerminalCode(terminalCode);//外部订单号
        order.setOutterUserCode(outterUserCode.toUpperCase());
        order.setOrderMoney(Double.valueOf(orderMoney));
        order.setUserCode(userCode);

        //微店(手动)订单费率开关
        SystemConfig systemConfig = systemService.querySystemConfig("6_sys_order_hand_feerate_open");
        if (null == systemConfig) {
            log.error("新增订单失败,线下订单费率开关未设置!");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("新增订单失败,线下订单费率开关未设置!").build();
        }

        // 设置费率,结算金额
        FeeRateEntity feeRateEntity = feeRateService.listFeeRate(userCode);
        Integer feeRate = feeRateEntity.getFeeRate(), topUserFeeRateReturn = feeRateEntity.getTopUserFeeRateReturn();// 当前用户到账费率,上级用户返点费率
        order.setFeeRate(feeRate);// 费率
        BigDecimal feeRateBigDecimal =
                this.getBigDecimal(String.valueOf(feeRate)).divide(new BigDecimal(10000)).setScale(4, RoundingMode.HALF_UP);
        BigDecimal orderMoneyBigDecimal = this.getBigDecimal(orderMoney).setScale(4, RoundingMode.HALF_UP);// 订单加款金额(单位为元)
        BigDecimal settlementMoneyBigDecimal = orderMoneyBigDecimal.multiply(feeRateBigDecimal).setScale(4, RoundingMode.HALF_UP);
        order.setTradeMoney(settlementMoneyBigDecimal.doubleValue());// 结算金额

        // 设置应该到账时间
        Date addDate = new Date(System.currentTimeMillis());
        order.setAddDate(addDate);// 订单入库时间
        Date shouldDealDate = null;
        int tranferType = targetUser.getTranferType();//到账类型
        if (tranferType == Constant.USER_TRANFER_TYPE_T0) {
            shouldDealDate =
                    DateUtils.addMinutes(addDate, Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t0")));

        } else if (tranferType == Constant.USER_TRANFER_TYPE_T1)// T+1
        {
            shouldDealDate =
                    DateUtils.addMinutes(DateUtils.addHours(addDate, 24),
                            Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t1")));
        }
        order.setShouldDealDate(shouldDealDate);
        order.setStatus(Constant.ORDER_STATE_WAITING);// 待处理
        order.setMemo("待处理");
        orderService.saveOrUpdateOrder(order);

        // 非直属下级且上级返点费率非空--->需要向上级返点
        String topUserCode = targetUser.getTopUserCode();//上级编号
        if (!topUserCode.equals(PropUtils.getPropertyValue("top.user.code.default")) && null != topUserFeeRateReturn) {

            // 上级返点费率
            BigDecimal topUserFeeRateReturnBigDecimal =
                    this.getBigDecimal(String.valueOf(topUserFeeRateReturn)).divide(new BigDecimal(10000)).setScale(4, RoundingMode.HALF_UP);
            BigDecimal settlementMoneyReturnBigDecimal =
                    orderMoneyBigDecimal.multiply(topUserFeeRateReturnBigDecimal).setScale(4, RoundingMode.HALF_UP);// 到账结算金额

            //返点金额大于0才生成返点订单
            if (settlementMoneyReturnBigDecimal.compareTo(new BigDecimal(0)) > 0) {
                // 根据上级用户编号查询用户信息
                User topUser = userService.getUserDetail(topUserCode);
                PosOrder subOrder = new PosOrder();
                String subOrderCode = orderId + "_1";
                subOrder.setOrderSrc(Constant.ORDER_SOURCE_HAND);// 订单来源
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
                subOrder.setMemo("待处理");

                orderService.saveOrUpdateOrder(subOrder);// 保存上级返点订单
            }
        }
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("新增线下加款订单成功").build();
    }
}
