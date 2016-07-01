package com.tinytrustframework.epos.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 控制处理类基类
 *
 * @author Owen
 * @version [版本号, 2010-11-5]
 */
public class BaseController {

    // 操作结果成功
    protected final String RESULT_SUCCESS = "success";
    // 操作结果失败
    protected final String RESULT_FAIL = "fail";
    // logger
    protected Logger log = LoggerFactory.getLogger(BaseController.class);

    /**
     * 设置对应值value的BigDecimal值,保存3位小数
     *
     * @param value 数字字符串
     */
    public BigDecimal getBigDecimal(String value) {
        MathContext mc = new MathContext(value.length());// 设置有效数字
        return new BigDecimal(value, mc).setScale(3, RoundingMode.HALF_UP);
    }
}
