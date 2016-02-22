package com.tinytrustframework.epos.common.utils.lang;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 *
 * @author owen
 * @version [版本号, 2015-7-14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class IpUtil {

    /**
     * <获得请求IP>
     *
     *
     * @param request HttpServletRequest
     * @return IP
     * @see [类、类#方法、类#成员]
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级反向代理
        if (null != ip && !"".equals(ip.trim())) {
            StringTokenizer st = new StringTokenizer(ip, ",");
            if (st.countTokens() > 1) {
                return st.nextToken();
            }
        }

        return ip;
    }
}
