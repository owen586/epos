package com.tinytrust.epos.common.utils.lang.http;

import com.tinytrust.epos.common.utils.lang.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;


/**
 * @author owen
 * @date 2016-06-24 24:11:10
 */
public class HttpClient4Util {


    private static final Logger log = LoggerFactory.getLogger(HttpClient4Util.class);

    private static int SocketTimeout = 3000;//3秒
    private static int ConnectTimeout = 3000;//3秒

    /**
     * post
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    public static Map<String, Object> doPostWithMapResponse(String url, Map<String, String> params) throws IOException {
        String response = post(url, params);
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
    public static String doPostWithStringResponse(String url, Map<String, String> params) {
        return post(url, params);
    }


    /**
     * post
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    public static Map<String, Object> doGetWithMapResponse(String url, Map<String, String> params) throws IOException {
        String response = post(url, params);
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
    public static String doGetWithStringResponse(String url, Map<String, String> params) {
        return post(url, params);
    }


    /**
     * get
     *
     * @param url    请求的url
     * @param params 请求的参数，在浏览器？后面的数据，没有可以传null
     */
    public static String get(String url, Map<String, String> params) {
        String responseBody = "";
        CloseableHttpResponse response = null;

        try {
            try {
                // 获得HttpClient对象,支持https
                CloseableHttpClient httpClient = getHttpClient();
                HttpGet httpGet = new HttpGet(url);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(SocketTimeout)
                        .setConnectTimeout(ConnectTimeout).build();//设置请求和传输超时时间
                httpGet.setConfig(requestConfig);

                //添加参数
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                if (params != null && params.keySet().size() > 0) {
                    Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = iterator.next();
                        nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                }
                String requestParam = EntityUtils.toString(new UrlEncodedFormEntity(nvps), Consts.UTF_8);
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + requestParam));


                System.out.println("Executing request " + httpGet.getRequestLine());

                // 请求数据
                response = httpClient.execute(httpGet);
                System.out.println(response.getStatusLine());
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                } else {
                    System.out.println("http return status error:" + status);
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

            } finally {
                response.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseBody;
    }


    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     * @throws IOException
     */

    private static String post(String url, Map<String, String> params) {
        String responseBody = "";
        CloseableHttpResponse response = null;

        try {
            try {
                //支持https
                CloseableHttpClient httpClient = getHttpClient();
                HttpPost httpPost = new HttpPost(url);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(SocketTimeout)
                        .setConnectTimeout(ConnectTimeout).build();//设置请求和传输超时时间
                httpPost.setConfig(requestConfig);

                //添加参数
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                if (params != null && params.keySet().size() > 0) {
                    Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                        nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
                    }
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

                //请求数据
                response = httpClient.execute(httpPost);
                System.out.println(response.getStatusLine());
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity);
                } else {
                    System.out.println("http return status error:" + response.getStatusLine().getStatusCode());
                }
            } finally {
                response.close();
            }

        } catch (Exception e) {
            log.error("HttpClient4 execute post request error ! exception:{}", e.getMessage());
        }
        return responseBody;
    }

    /**
     * 获取HttpClient
     */
    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);

        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();

        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
//		connManager.setDefaultConnectionConfig(connConfig);
//		connManager.setDefaultSocketConfig(socketConfig);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }


    public static void main(String[] args) throws Exception {

//        String tokenUrl = PropUtils.getPropertyValue("weidian.token.url");
//        final String appkey = PropUtils.getPropertyValue("weidian.appkey");
//        final String secret = PropUtils.getPropertyValue("weidian.secret");
//
//        Map<String, Object> tokenMapResponse = doGetWithMapResponse(tokenUrl, new HashMap<String, String>() {
//            {
//                put("grant_type", "client_credential");
//                put("appkey", appkey);
//                put("secret", secret);
//            }
//        });
//
//        System.out.println(tokenMapResponse);


//


    }

}

