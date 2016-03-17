package com.tinytrustframework.epos.common.utils.lang;

import com.tinytrustframework.epos.common.statics.Constant;
import org.apache.commons.lang3.StringUtils;
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
 */
public class DateUtil {
    // Log
    private final static Logger log = LoggerFactory.getLogger(DateUtil.class);

    // 默认构造函数
    private DateUtil() {
    }

    /**
     * 将字符串按照默认的日期格式转换成日期
     *
     * @param str 字符串
     * @return 日期
     */
    public static Date parse(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_19);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            log.error("字符串转换成日期出错! 字符串: {}", str);
        }
        return date;
    }

    /**
     * 将字符串按照指定的日期格式转换成日期
     *
     * @param str    字符串
     * @param format 日期格式
     * @return 日期
     */
    public static Date parse(String str, String format) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            log.error("字符串转换成日期出错! 字符串: {},日期格式:{}", str, format);
        }
        return date;
    }


    /**
     * 将日期对象按指定的日期格式转换成字符串
     *
     * @param date   日期
     * @param format 日期格式
     * @return 字符串
     */
    public static String format(Date date, String format) {
        if (null == date) {
            return null;
        }

        if (StringUtils.isBlank(format)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将日期对象按默认的日期格式转换成字符串
     *
     * @param date 日期
     * @return 字符串
     */
    public static String format(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_19);
        return sdf.format(date);
    }


}
