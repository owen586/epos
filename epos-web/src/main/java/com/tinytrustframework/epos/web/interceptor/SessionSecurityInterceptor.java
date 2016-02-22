package com.tinytrustframework.epos.web.interceptor;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Session安全拦截器，当前用户操作是否session正常
 *
 * @author owen
 * @version [版本号, 2011-8-13]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SessionSecurityInterceptor implements HandlerInterceptor {
    /**
     * 注释内容
     */
    private final static Logger log = LoggerFactory.getLogger(SessionSecurityInterceptor.class);

    /**
     * 排除在外的路径
     */
    private List<String> excludePathList;

    public void setExcludePathList(List<String> excludePathList) {
        this.excludePathList = excludePathList;
    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        String currentUrl = request.getContextPath() + request.getServletPath();//当前请求路径
        if (excludePathList.contains(currentUrl) || currentUrl.contains("/resources") || currentUrl.contains("/favicon"))//排除允许的路径和静态资源路径
        {
            return true;
        } else {
            User user = (User) session.getAttribute(Constant.CURRENT_USER_IN_SESSION);
            if (null == user) {
                log.error("安全拦截器拦截成功：用户会话信息缺失或已失效! 请求路径: {}", currentUrl);
                request.getRequestDispatcher("/index.html").forward(request, response);
                return false;
            }
            return true;
        }
    }


    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }


    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }

}
