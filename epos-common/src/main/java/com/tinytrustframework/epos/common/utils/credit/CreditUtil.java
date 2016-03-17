package com.tinytrustframework.epos.common.utils.credit;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.common.utils.props.PropUtils;
import com.tinytrustframework.epos.common.utils.lang.DigestUtil;
import com.tinytrustframework.epos.common.utils.lang.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟币加款工具类
 *
 * @author owen
 * @version [版本号, 2015-9-14]
 */
public class CreditUtil {

    /**
     * 注释内容
     */
    private final static Logger log = LoggerFactory.getLogger(CreditUtil.class);

    /**
     * 默认构造函数
     */
    private CreditUtil() {
    }

    /**
     * 去第三方加款充值
     *
     * @param spid    商户订单编号
     * @param account 下级账号
     * @param money   加款金额
     * @return 加款结果
     */
    public static Map<String, Object> recharge(String spid, String account, String money) {
        Map<String, String> rechargeParams = new HashMap<String, String>();
        String userid = PropUtils.getPropertyValue("third.recharge.usercode");//第三方充值账号
        String userpws = PropUtils.getPropertyValue("third.recharge.password");//第三方充值密码
        String md5key = PropUtils.getPropertyValue("third.recharge.md5.key");//第三方约定密匙

        rechargeParams.put("userid", userid);//商户编号（虚拟币发行者）
        rechargeParams.put("userpws", userpws);//商户登录密码的Md5值 md5(登录密码)
        rechargeParams.put("spbillid", spid);//商户订单号
        rechargeParams.put("tergetid", account);//要加点的下级编号
        rechargeParams.put("actcredit", money);//加点金额
        rechargeParams.put("format", "json");
        StringBuffer orignBuf = new StringBuffer();
        orignBuf.append(userid).append(userpws).append(spid).append(md5key);
        rechargeParams.put("md5_str", DigestUtil.md5(orignBuf.toString()).toUpperCase());
        String rechargeUrl = PropUtils.getPropertyValue("third.recharge.url");
        log.info("请求第三方充值加款接口, 参数: {}" + rechargeParams.toString());
        return HttpClientUtil.getResponseAsJsonMap(rechargeUrl, rechargeParams, Constant.CHARSET_UTF8);
    }
}
