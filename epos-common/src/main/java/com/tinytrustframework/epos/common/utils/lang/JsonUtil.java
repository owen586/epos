package com.tinytrustframework.epos.common.utils.lang;

import java.util.Map;

import com.tinytrustframework.epos.common.statics.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON工具类
 *
 * @author owen
 * @version [版本号, 2012-11-17]
*/
public class JsonUtil {

    /**
     * 注释内容
     */
    private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * <Object转换成JSON字符串>
     *
     *
     * @param obj
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String obj2json(Object obj) {
        SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty};
        return JSON.toJSONStringWithDateFormat(obj, Constant.DATE_FORMAT_19, features);
    }

    /**
     * <json字符串转Map>
     *
     *
     * @param json
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> json2Map(String json) {
        try {
            return JSON.parseObject(json);
        } catch (Exception e) {
            log.error("json转换失败! Json String: " + json + ", Error:" + e.getMessage());
            return null;
        }

    }
}
