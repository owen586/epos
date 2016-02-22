/*
 * 文 件 名:  JSONUtil.java
 * 版    权:  Absolute Software Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  Owen 
 * 修改时间:  2010-11-16
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.common.utils.lang;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP 工具類
 *
 * @author Owen
 * @version [版本号, 2010-11-16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HttpClientUtil {
    /**
     * 注释内容
     */
    private final static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 默认字符集
     */
    private final static String DEFAULT_CHARSET = "UTF-8";

    /**
     * <默认构造函数>
     */
    private HttpClientUtil() {
    }

    /**
     * <执行HTTP POST请求,以JSON MAP格式返回响应数据>
     *
     * @param url       api地址
     * @param reqParams 请求参数
     * @param charset   字符集
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> getResponseAsJsonMap(String url, Map<String, String> reqParams, String charset) {
        String response = post(url, reqParams, charset);
        if (null != response) {
            return JsonUtil.json2Map(response);
        }
        return null;
    }


    /**
     * <执行HTTP POST请求,以字符串格式返回响应数据>
     *
     * @param url
     * @param reqParams
     * @param charset
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getResponseAsString(String url, Map<String, String> reqParams, String charset) {
        return post(url, reqParams, charset);
    }

    /**
     * <执行HTTP POST请求>
     *
     * @param url       请求地址
     * @param reqParams 请求参数
     * @param charset   字符集
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String post(String url, Map<String, String> reqParams, String charset) {
        try {
            HttpClient client = null;
            HttpMethod post = null;

            BufferedReader bufReader = null;
            InputStreamReader reader = null;
            InputStream input = null;
            try {
                //创建HttpClient对象
                client = new HttpClient();
                //设置连接超时
                client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);//1m
                //设置读取数据超时
                client.getHttpConnectionManager().getParams().setSoTimeout(120000);//2m

                post = new PostMethod(url);

                boolean charsetFlag = false;
                if (StringUtils.isEmpty(charset)) {
                    post.getParams().setContentCharset(DEFAULT_CHARSET);
                    charsetFlag = true;
                } else {
                    post.getParams().setContentCharset(charset);
                }

                //使用系统提供的默认的恢复策略
                post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

                NameValuePair[] pair = new NameValuePair[reqParams.size()];
                Iterator<Entry<String, String>> iter = reqParams.entrySet().iterator();
                Entry<String, String> entry = null;
                int i = 0;
                while (iter.hasNext()) {
                    entry = iter.next();
                    pair[i] = new NameValuePair(entry.getKey(), entry.getValue());
                    i++;
                }
                post.setQueryString(pair);//设置请求参数
                client.executeMethod(post);//执行请求

                //String response = post.getResponseBodyAsString();
                input = post.getResponseBodyAsStream();
                //InputStreamReader 是字节流通向字符流的桥梁：它使用指定的 charset 读取字节并将其解码为字符
                if (charsetFlag) {
                    reader = new InputStreamReader(input, DEFAULT_CHARSET);
                } else {
                    reader = new InputStreamReader(input, charset);
                }

                //从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取
                bufReader = new BufferedReader(reader, 1024 * 2);
                StringBuffer response = new StringBuffer();
                String line = null;
                while (null != (line = bufReader.readLine())) {
                    response.append(line);
                }
                String result = response.toString().replace("\r\n", "");
                return result;
            } finally {
                //关闭输入流
                if (null != bufReader) {
                    bufReader.close();
                }

                if (null != reader) {
                    reader.close();
                }

                if (null != input) {
                    input.close();
                }

                //释放连接
                if (null != post) {
                    post.releaseConnection();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送HTTP请求失败! 异常信息: {}", e.getMessage());
        }
        return null;
    }
}
