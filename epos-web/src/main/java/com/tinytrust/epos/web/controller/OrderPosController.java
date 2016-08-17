package com.tinytrust.epos.web.controller;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.lang.DateUtil;
import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.entity.PosOrder;
import com.tinytrust.epos.entity.User;
import com.tinytrust.epos.service.OrderService;
import com.tinytrust.epos.web.controller.rsp.CommonRsp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * POS订单
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Slf4j
@Controller
@RequestMapping(value = "/order/pos")
public class OrderPosController extends BaseController {

    @Resource
    private OrderService orderService;

    /**
     * 转发至订单列表页面
     */
    @RequestMapping(value = "/list/index")
    public String listPosOrderIndex() {
        return "order/pos_order_list";
    }

    /**
     * 订单列表查询
     *
     * @param order   订单查询表单
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp listPosOrder(PosOrder order, HttpServletRequest request) {
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
        if (status != -999) {
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

        // 订单来源
        params.put("orderSrc", new ArrayList<Integer>() {
            {
                add(Constant.ORDER_SOURCE_POS);//pos
//                add(Constant.ORDER_SOURCE_WEIDIAN);
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


}
