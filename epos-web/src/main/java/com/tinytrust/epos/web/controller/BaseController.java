package com.tinytrust.epos.web.controller;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 控制处理类基类
 *
 * @author Owen
 * @version [版本号, 2010-11-5]
 */
@Slf4j
public class BaseController {

    // 操作结果成功
    protected final String RESULT_SUCCESS = "success";
    // 操作结果失败
    protected final String RESULT_FAIL = "fail";

    public static void main(String[] args) {
        System.out.println(new BigDecimal(0).compareTo(new BigDecimal(0.000000)));
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
}
