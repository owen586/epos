package com.tinytrust.epos.common.utils.business.weidian;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.lang.DateUtil;
import com.weidian.open.sdk.AbstractWeidianClient;
import com.weidian.open.sdk.DefaultWeidianClient;
import com.weidian.open.sdk.exception.OpenException;
import com.weidian.open.sdk.oauth.OAuth;
import com.weidian.open.sdk.request.order.VdianOrderDeliverRequest;
import com.weidian.open.sdk.request.order.VdianOrderGetRequest;
import com.weidian.open.sdk.request.order.VdianOrderListGetRequest;
import com.weidian.open.sdk.response.oauth.OAuthResponse;
import com.weidian.open.sdk.response.order.VdianOrderGetResponse;
import com.weidian.open.sdk.response.order.VdianOrderListGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author owen
 * @date 2016-06-28 28:13:17
 */
@Slf4j
public class VdianUtil {

    private VdianUtil() {
    }

    /**
     * 生成token
     */
    public static String tokenGenerate() throws OpenException {
        OAuth oAuth = OAuth.getInstance();
        OAuthResponse response = oAuth.getPersonalToken();
        String token = response.getResult().getAccessToken();
        return token;
    }


    /**
     * 查询订单详情
     *
     * @param token   令牌
     * @param tradeNo 订单编号
     * @return
     */
    public static VdianOrderGetResponse.VdianOrderGetResult orderDetail(String token, String tradeNo) throws OpenException {

        // 查询订单详情
        AbstractWeidianClient client = DefaultWeidianClient.getInstance();
        VdianOrderGetRequest orderGetRequest = new VdianOrderGetRequest(token, tradeNo);
        VdianOrderGetResponse response = client.executePost(orderGetRequest);

        VdianOrderGetResponse.VdianOrderGetResult result = null;
        if (null != response) {
            int statusCode = response.getStatus().getStatusCode();
            if (statusCode == VdianConstant.STATUS_OK) {
                result = response.getResult();
            } else {
                log.error("查询订单详情异常! statusCode:{},tradeNo:{}", statusCode, tradeNo);
            }
        }
//
//            String tradeNo = result.getTradeNo();//订单交易号
//            String totoal = result.getTotal();// 订单总价,包含运费
//            String price = result.getPrice();// 商品总价格,不包含运费
//            String status = result.getStatus();// 订单状态
//            String note = result.getNote();// 卖家备注

        return result;
    }

    /**
     * 订单发货
     *
     * @param token   令牌
     * @param orderId 订单编号
     */
    public static void orderDeliver(String token, String orderId) throws OpenException {

        // 发货
        VdianOrderDeliverRequest deliverRequest = new VdianOrderDeliverRequest(token);
        deliverRequest.setOrderId(orderId);
        deliverRequest.setExpressType(VdianConstant.ORDER_DELIVER_TYPE);

        AbstractWeidianClient client = DefaultWeidianClient.getInstance();
        client.executePost(deliverRequest);
    }

    /**
     * 查询制定时间段内等待发货的订单
     *
     * @param token 令牌
     */
    public static VdianOrderListGetResponse.VdianOrderListGetResult orderList(String token) throws OpenException {

        AbstractWeidianClient client = DefaultWeidianClient.getInstance();

        // 查询订单列表(一周内等待发货的订单)
        VdianOrderListGetRequest orderListRequest = new VdianOrderListGetRequest(token);
        orderListRequest.setOrderType(VdianConstant.ORDER_STATUS_PEND);
        orderListRequest.setPageNum(VdianConstant.PAGE_NUM);

        Date currentDate = new Date();
        // TODO: 8/1/16
        String aweek = DateUtil.format(DateUtils.addDays(currentDate, -180), Constant.DATE_FORMAT_19);
        orderListRequest.setAddStart(aweek);
        String currentDateString = DateUtil.format(currentDate, Constant.DATE_FORMAT_19);
        orderListRequest.setAddEnd(currentDateString);

        VdianOrderListGetResponse orderListResponse = client.executePost(orderListRequest);
        VdianOrderListGetResponse.VdianOrderListGetResult orderListResult = orderListResponse.getResult();

        return orderListResult;
    }


    public static void main(String[] args) {

//
        try {
            // 令牌
            String token = "76ee8a664cc24f2df15f71c0929fdf290004881660";//VdianUtil.76ee8a664cc24f2df15f71c0929fdf290004881660tokenGenerate();
            System.out.println("--------------TOKEN------------- " + token);

            VdianOrderListGetResponse.VdianOrderListGetResult result =  VdianUtil.orderList(token);
            System.out.println(result.getOrderNum());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


