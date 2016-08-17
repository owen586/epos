package com.tinytrust.epos.task.impl;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.business.weidian.VdianConstant;
import com.tinytrust.epos.common.utils.business.weidian.VdianUtil;
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
import com.tinytrust.epos.task.WeidianOrderTask;
import com.weidian.open.sdk.exception.OpenException;
import com.weidian.open.sdk.response.order.VdianOrderGetResponse;
import com.weidian.open.sdk.response.order.VdianOrderListGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author owen
 * @date 2016-07-07 07:18:08
 */
@Slf4j
@Service(value = "vdianOrderTaskService")
public class WeidianOrderTaskImpl implements WeidianOrderTask {


    @Resource
    private SystemService systemService;

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @Resource
    private FeeRateService feeRateService;


    /**
     * 获取微店令牌
     */
    private String getToken() {

        String token = null;
        SystemConfig systemConfig = systemService.querySystemConfig("7_sys_weidian_token");//微店(手动)订单费率开关
        if (null != systemConfig) {
            token = systemConfig.getSysValue();
        }
        return token;
    }


    /**
     * 令牌生成
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void tokenGenerate() {

        try {

            log.info("微店令牌生成开始!");

            String token = VdianUtil.tokenGenerate();
            SystemConfig systemConfig = systemService.querySystemConfig("7_sys_weidian_token");
            systemConfig.setSysValue(token);
            systemService.updateSystemConfig(systemConfig);// 更新令牌

            log.info("微店令牌生成结束! 令牌: {}", token);

        } catch (OpenException e) {
            log.error("令牌生成异常!");
        }
    }

    /**
     * 设置对应值value的BigDecimal值,保存4位小数
     *
     * @param value 数字字符串
     */
    public BigDecimal getBigDecimal(String value) {
        MathContext mc = new MathContext(value.length());// 设置有效数字
        return new BigDecimal(value, mc).setScale(4, RoundingMode.HALF_UP);
    }


    /**
     * 订单下载
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void orderListQuery() {
        String token = this.getToken();
        if (StringUtils.isEmpty(token)) {
            log.error("查询微店订单列表失败,微店令牌为空!");
            return;
        }

        log.info("微店订单下载开始!");

        try {
            // 获取订单列表
            VdianOrderListGetResponse.VdianOrderListGetResult orderListGetResult = VdianUtil.orderList(token);
            if (orderListGetResult == null) {
                log.error("搜索微店订单超时! 令牌: {}", token);
                return;
            }

            //微店(手动)订单费率开关
            SystemConfig systemConfig = systemService.querySystemConfig("6_sys_order_hand_feerate_open");
            if (null == systemConfig) {
                log.error("新增订单失败,线下订单费率开关未设置!");
                return;
            }


            int orderNum = orderListGetResult.getOrderNum();
            VdianOrderListGetResponse.ListOrder[] orders = orderListGetResult.getOrders();
            if (orderNum > 0) {

                VdianOrderListGetResponse.ListOrder order = null;

                for (int i = 0; i < orderNum; i++) {

                    order = orders[i];
                    String orderId = order.getOrderId();

                    // 查询订单详情
                    VdianOrderGetResponse.VdianOrderGetResult orderDetail = VdianUtil.orderDetail(token, orderId);

                    if (null != orderDetail) {
                        String total = orderDetail.getTotal();// 订单总价,包含运费
                        String status = orderDetail.getStatus();// 订单状态
                        String statusText = orderDetail.getStatus2();//订单状态说明
                        String note = orderDetail.getNote();// 卖家备注
                        String addTime = orderDetail.getAddTime();// 下单时间

                        log.info("微店订单详情: 订单编号: {}, 买家备注: {},订单金额: {}, 订单状态: {},订单状态描述: {},买家下单时间: {}",
                                orderId, note, total, status, statusText, addTime);

                        if (status.equals(VdianConstant.ORDER_STATUS_PAY)) {

                            PosOrder posOrder = orderService.getOrderDetail(Constant.ORDER_SOURCE_WEIDIAN, orderId);
                            if (null != posOrder) {
                                log.info("订单已经存在,自动忽略! 订单编号:{}", orderId);
                                continue;
                            }

                            // 保存订单
                            Date currentDate = new Date(System.currentTimeMillis());
                            posOrder = PosOrder.builder()
                                    .orderSrc(Constant.ORDER_SOURCE_WEIDIAN)
                                    .orderType(Constant.ORDER_TYPE_COMMON)
                                    .orderCode(PropUtils.getPropertyValue("order.prefix") + orderId)
                                    .orderMoney(Double.valueOf(total))
                                    .status(Constant.ORDER_STATE_PRECHECK)
                                    .memo("待处理")
                                    .addDate(currentDate)
                                    .build();

                            // 买家填写留言
                            if (StringUtils.isNotEmpty(note)) {

                                // 买家填写的是欧飞账号,保存在用户的手机号里面
                                User targetUser = systemService.getUserByCellphone(note);
                                if (null != targetUser) {

                                    String userCode = targetUser.getUserCode();// 系统用户编号
                                    posOrder.setUserCode(userCode);
                                    posOrder.setOutterUserCode(targetUser.getOutterUserCode());//外部订单编号

                                    // 设置费率,结算金额
                                    FeeRateEntity feeRateEntity = feeRateService.listFeeRate(userCode);
                                    Integer feeRate = feeRateEntity.getFeeRate(), topUserFeeRateReturn = feeRateEntity.getTopUserFeeRateReturn();// 当前用户到账费率,上级用户返点费率
                                    posOrder.setFeeRate(feeRate);
                                    BigDecimal feeRateBigDecimal =
                                            this.getBigDecimal(String.valueOf(feeRate)).divide(new BigDecimal(10000)).setScale(4, RoundingMode.HALF_UP);
                                    BigDecimal orderMoneyBigDecimal = this.getBigDecimal(total).setScale(4, RoundingMode.HALF_UP);// 订单加款金额(单位为元)
                                    BigDecimal settlementMoneyBigDecimal = orderMoneyBigDecimal.multiply(feeRateBigDecimal).setScale(4, RoundingMode.HALF_UP);
                                    posOrder.setTradeMoney(settlementMoneyBigDecimal.doubleValue());// 到账结算金额

                                    // 设置应该到账时间
                                    Date addDate = new Date(System.currentTimeMillis());
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
                                    posOrder.setShouldDealDate(shouldDealDate);

                                    orderService.saveOrUpdateOrder(posOrder);


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
                                            String subOrderCode = PropUtils.getPropertyValue("order.prefix") + orderId + "_1";
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
                                            subOrder.setMemo("待处理");

                                            orderService.saveOrUpdateOrder(subOrder);// 保存上级返点订单
                                        }
                                    }
                                } else {

                                    // 用户不存在
                                    posOrder.setUserCode(note);
                                    posOrder.setAddDate(currentDate);// 订单入库时间
                                    posOrder.setTradeMoney(Double.valueOf(total));
                                    posOrder.setStatus(Constant.ORDER_STATE_FIAL);// 直接设置订单重置失败
                                    posOrder.setMemo("找不到匹配的外部账户");

                                    orderService.saveOrUpdateOrder(posOrder);
                                }

                            } else {

                                // 买家未留言,直接设置订单加款失败
                                posOrder.setUserCode(PropUtils.getPropertyValue("top.user.code.default"));// 默认上级
                                posOrder.setAddDate(currentDate);// 订单入库时间
                                posOrder.setTradeMoney(Double.valueOf(total));
                                posOrder.setStatus(Constant.ORDER_STATE_FIAL);// 直接设置订单重置失败
                                posOrder.setMemo("买家未留言");

                                orderService.saveOrUpdateOrder(posOrder);

                            }


                            // 发货
                            VdianUtil.orderDeliver(token, orderId);
                            log.info("微店订单发货! 订单编号: {}", orderId);
                        }
                    }

                }
            }

        } catch (OpenException e) {
            e.printStackTrace();
            log.error("查询微店订单列表失败! 令牌: {}", token);
        }

        log.info("微店订单下载结束!");
    }


    /**
     * 订单详情查询
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void orderDetailQuery() {


        String token = this.getToken();
        if (StringUtils.isEmpty(token)) {
            log.error("查询微店订单详情失败,微店令牌为空!");
            return;
        }

        log.info("开始查询微店订单详情!");

        // 分页参数
        Page page = Page.builder().pageStart(Constant.PAGE_DEFAULT_START).pageSize(Constant.PAGE_DEFAULT_LIMIT).build();

        // 业务参数
        Map<String, Object> businessParams = new HashMap();
        businessParams.put("orderSrc", new ArrayList<Integer>() {
            {
                add(Constant.ORDER_SOURCE_WEIDIAN);//微店
                add(Constant.ORDER_SOURCE_HAND);//手动

            }
        });// 订单来源
        businessParams.put("status", Constant.ORDER_STATE_PRECHECK);//订单状态(待确认)
        Map<String, Object> dataMap = orderService.queryOrderList(businessParams, page);
        List<PosOrder> orderList = (List<PosOrder>) dataMap.get("list");


        if (CollectionUtils.isNotEmpty(orderList)) {
            VdianOrderGetResponse.VdianOrderGetResult result = null;
            String weidianOrderNo = null;// 终端编号(微店订单编号)
            String weidianStatus = null;// 微店订单状态
            for (PosOrder order : orderList) {

                try {
                    weidianOrderNo = order.getOrderCode();

                    String vdOrderId = weidianOrderNo.replace(PropUtils.getPropertyValue("order.prefix"), "");
                    System.out.println("------------------ " + vdOrderId);
                    order = orderService.getOrderDetail(Constant.ORDER_SOURCE_WEIDIAN, weidianOrderNo);

                    result = VdianUtil.orderDetail(token, vdOrderId);
                    if (null != result) {
                        weidianStatus = result.getStatus();// 订单状态
                        if (VdianConstant.ORDER_STATUS_FINISH.equals(weidianStatus)) {
                            //微店订单已经确认收货
                            log.info("微店订单详情查询成功,订单已经确认收货! 微店订单编号: {}", weidianOrderNo);
                            order.setStatus(Constant.ORDER_STATE_WAITING);
                            orderService.saveOrUpdateOrder(order);
                        }

                    }

                } catch (OpenException e) {
                    log.error("查询微店订单详情失败! 微店订单编号: {} ,令牌: {}", weidianOrderNo, token);
                }
            }
        } else {
            log.info("暂无微店订单需要查询详情!");
        }


    }


}
