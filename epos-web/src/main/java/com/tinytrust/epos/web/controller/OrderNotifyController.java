package com.tinytrust.epos.web.controller;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.lang.DigestUtil;
import com.tinytrust.epos.common.utils.lang.props.PropUtils;
import com.tinytrust.epos.entity.*;
import com.tinytrust.epos.service.OrderService;
import com.tinytrust.epos.service.PriceService;
import com.tinytrust.epos.service.UserService;
import com.tinytrust.epos.web.controller.req.YSNotifyReq;
import com.tinytrust.epos.web.controller.rsp.YSNotifyRsp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 银盛订单回调控制类
 *
 * @author owen
 * @date 2016-07-01 01:16:05
 */
@Slf4j
@Controller
@RequestMapping(value = "/order")
public class OrderNotifyController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @Resource
    private PriceService priceService;


    /**
     * 订单回调通知
     *
     * @param ysOrder 银盛POS订单
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public YSNotifyRsp notify(@RequestBody YSNotifyReq ysOrder) {
        String ysOrderid = ysOrder.getYsOrderid();// 银盛订单号
        String orderid = ysOrder.getOrderid();// 商户订单号
        String status = ysOrder.getStatus();// 状态 1、消费成功 ；0、消费失败
        String money = ysOrder.getMoney();// 实际消费金额，单位:分
        String time = ysOrder.getTime();// 消费日期yyyyMMddHHmmss
        String remark = ysOrder.getRemark();// 备注
        String checkValue = ysOrder.getCheckValue();// 校验值

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


            // 用户密价查询
            PriceUser priceUser = priceService.getPriceUserDetail(userCode);
            Integer feeRate = null, topUserFeeRateReturn = null;// 当前用户到账费率,上级用户返点费率
            if (null != priceUser) {
                feeRate = priceUser.getFeeRate();
                topUserFeeRateReturn = priceUser.getTopUserFeeRateReturn();
            } else {

                // 用户角色价格查询
                int roleCode = user.getRoleCode();// 角色编号
                PriceRole priceRole = priceService.getPriceRoleDetail(roleCode);
                feeRate = priceRole.getFeeRate();
                topUserFeeRateReturn = priceRole.getTopUserFeeRateReturn();
            }


            BigDecimal feeRateBigDecimal =
                    this.getBigDecimal(String.valueOf(feeRate)).divide(new BigDecimal(10000)).setScale(4, RoundingMode.HALF_UP);//费率

            BigDecimal orderMoneyBigDecimal =
                    this.getBigDecimal(money).divide(new BigDecimal(100)).setScale(4, RoundingMode.HALF_UP);// 订单加款金额，由分转换为元

            BigDecimal settlementMoneyBigDecimal =
                    orderMoneyBigDecimal.multiply(feeRateBigDecimal).setScale(4, RoundingMode.HALF_UP);// 到账结算金额

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
            //到账时间设置
            if (tranferType == Constant.USER_TRANFER_TYPE_T0) {
                // T+0
                shouldDealDate =
                        DateUtils.addMinutes(addDate,
                                Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t0")));

            } else if (tranferType == Constant.USER_TRANFER_TYPE_T1) {
                // T+1
                shouldDealDate =
                        DateUtils.addMinutes(DateUtils.addHours(addDate, 24), Integer.parseInt(PropUtils.getPropertyValue("tranfer.type.delay.minute.t1")));
            }
            mainOrder.setShouldDealDate(shouldDealDate);

            mainOrder.setStatus(Constant.ORDER_STATE_WAITING);
            mainOrder.setTerminalCode(terminalCode);
            mainOrder.setBatchCode(ysSysTradeCode);// 银盛系统交易参考号
            mainOrder.setMemo("待处理");
            mainOrder.setPayBankCode(payBankCode);// 支付卡号
            orderService.saveOrUpdateOrder(mainOrder);

            // 非直属下级且上级返点费率非空--->需要向上级返点
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
}
