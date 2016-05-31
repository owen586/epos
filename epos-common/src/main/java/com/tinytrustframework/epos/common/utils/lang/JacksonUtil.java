package com.tinytrustframework.epos.common.utils.lang;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JSON工具类
 *
 * @author owen
 * @version [版本号, 2012-11-17]
 */
public class JacksonUtil {

    // 注释内容
    private static Logger log = LoggerFactory.getLogger(JacksonUtil.class);

    /**
     * Object 转换成 json字符串对象
     *
     * @param obj 目标对象
     * @return json字符串对象
     */
    public static String obj2json(Object obj)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * json字符串转換成Map對象
     *
     * @param json json字符串
     * @return Map對象
     */
    public static Map<String, Object> json2Map(String json) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> map = objectMapper.readValue(json, typeRef);

        return map;

    }
}
