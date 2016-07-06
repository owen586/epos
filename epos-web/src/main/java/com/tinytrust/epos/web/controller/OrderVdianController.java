package com.tinytrust.epos.web.controller;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.lang.DateUtil;
import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.common.utils.lang.props.PropUtils;
import com.tinytrust.epos.entity.PosOrder;
import com.tinytrust.epos.entity.SystemConfig;
import com.tinytrust.epos.entity.User;
import com.tinytrust.epos.service.OrderService;
import com.tinytrust.epos.service.SystemService;
import com.tinytrust.epos.web.controller.rsp.CommonRsp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@Controller
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
        Map<String, Object> params = new HashMap<String, Object>();

        // 订单编号
        String orderCode = order.getOrderCode();
        if (StringUtils.isNotEmpty(orderCode)) {
            params.put("orderCode", "%" + orderCode + "%");
        }

        // 订单状态
        int status = order.getStatus();
        if (status != -1) {
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
        // 分页信息
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));


        try {
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
