/*
 * 文 件 名:  DateUtil.java
 * 版    权:  Absolute Software Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  Owen 
 * 修改时间:  2010-11-5
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.common.utils.lang;

import com.tinytrustframework.epos.common.statics.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具類
 *
 * @author Owen
 * @version [版本号, 2010-11-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DateUtil {
    /**
     * 注释内容
     */
    private final static Logger log = LoggerFactory.getLogger(DateUtil.class);

    /**
     * <默认构造函数>
     */
    private DateUtil() {
    }

    /**
     * <字符串转换成日期>
     * <如果转换格式为空，则利用默认格式进行转换操作>
     *
     * @param str    字符串
     * @param format 日期格式
     * @return 日期
     * @see [类、类#方法、类#成员]
     */
    public static Date parse(String str, String format) {
        if (null == str || "".equals(str)) {
            return null;
        }
        //如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format)) {
            format = Constant.DATE_FORMAT_19;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            log.error("Parse string to date error!String : {}", str);
        }

        return null;
    }

    /**
     * <字符串转时间戳>
     *
     * @param str
     * @param format
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Timestamp parse2Timestamp(String str, String format) {
        Date date = parse(str, format);
        return new Timestamp(date.getTime());
    }

    /**
     * <日期转字符串>
     *
     * @param date   日期
     * @param format 日期格式
     * @return 字符串
     * @see [类、类#方法、类#成员]
     */
    public static String format(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * <时间戳转换为字符串>
     *
     * @param timestamp
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String format(Timestamp timestamp, String format) {
        if (null == timestamp) {
            return null;
        }
        return format(timestamp, format);
    }

}
