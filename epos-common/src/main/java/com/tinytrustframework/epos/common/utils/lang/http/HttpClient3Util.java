package com.tinytrustframework.epos.common.utils.lang.http;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.common.utils.lang.JacksonUtil;
import lombok.Cleanup;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HttpClient 3 工具类
 *
 * @author Owen
 * @version [版本号, 2010-11-16]
 */
public class HttpClient3Util {

    // Logger
    private final static Logger log = LoggerFactory.getLogger(HttpClient3Util.class);

    // 默认构造函数
    private HttpClient3Util() {
    }

    /**
     * post
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    public static Map<String, Object> responseMap(String url, Map<String, String> params) throws IOException {
        String response = post(url, params, Constant.CHARSET_UTF8);
        if (StringUtils.isNotEmpty(response)) {
            return JacksonUtil.json2Map(response);
        }
        return null;
    }


    /**
     * post
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    public static String responseString(String url, Map<String, String> params) {
        return post(url, params, Constant.CHARSET_UTF8);
    }

    /**
     * post
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param charset 字符集
     */
    private static String post(String url, Map<String, String> params, String charset) {

        String response = null;

        try {

            // init
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);//1m
            client.getHttpConnectionManager().getParams().setSoTimeout(120000);//2m

            HttpMethod post = new PostMethod(url);
            post.getParams().setContentCharset(charset);
            post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());// 使用系统提供的默认的恢复策略

            // set params pair
            NameValuePair[] pair = new NameValuePair[params.size()];
            Iterator<Entry<String, String>> iter = params.entrySet().iterator();
            Entry<String, String> entry = null;
            int i = 0;
            while (iter.hasNext()) {
                entry = iter.next();
                pair[i] = new NameValuePair(entry.getKey(), entry.getValue());
                i++;
            }
            post.setQueryString(pair);

            // execute
            int httpStatus = client.executeMethod(post);
            if (httpStatus == HttpStatus.SC_OK) {
                @Cleanup InputStream input = post.getResponseBodyAsStream();
                @Cleanup InputStreamReader reader = new InputStreamReader(input, charset);
                @Cleanup BufferedReader bufReader = new BufferedReader(reader, 1024 * 5);

                StringBuffer responseBuffer = new StringBuffer();
                String line = null;
                while (null != (line = bufReader.readLine())) {
                    responseBuffer.append(line);
                }

                response = responseBuffer.toString();
            }

        } catch (Exception e) {
            log.error("HttpClient3 execute post request error ! exception:{}", e.getMessage());
        }

        return response;
    }
}
