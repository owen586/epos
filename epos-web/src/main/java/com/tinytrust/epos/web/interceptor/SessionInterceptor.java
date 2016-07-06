package com.tinytrust.epos.web.interceptor;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Writer;

/**
 * Session安全拦截器，当前用户操作是否session正常
 *
 * @author owen
 * @version [版本号, 2011-8-13]
 */
public class SessionInterceptor implements HandlerInterceptor {
    /**
     * 注释内容
     */
    private final static Logger log = LoggerFactory.getLogger(SessionInterceptor.class);


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession();
        String requestUrl = request.getContextPath() + request.getServletPath();//请求路径
        User user = (User) session.getAttribute(Constant.CURRENT_USER_IN_SESSION);
        if (null == user) {
            Writer out = response.getWriter();
            //ajax请求
            if (null != request.getHeader("x-requested-with") && request.getHeader("x-requested-with")
                    .equalsIgnoreCase("XMLHttpRequest")) {
                log.debug("会话拦截器拦截成功：用户会话信息缺失或已失效! AJAX请求路径: {}", requestUrl);
                response.setHeader("sessionstatus", "timeout");//在响应头中设置session状态
                out.flush();
                return false;

            } else {
                //一般请求
                log.debug("会话拦截器拦截成功：用户会话信息缺失或已失效! 一般请求路径: {}", requestUrl);
                StringBuffer message = new StringBuffer()
                        .append("<script>")
                        .append("parent.layer.msg(\"登录超时,请重新登录\",{shade:0.5,time:800});")
                        .append("setTimeout(function(){\n" +
                                "                    parent.window.location.replace(\"/index.html\");\n" +
                                "                },1000);")
                        .append("</script>");
                out.write(message.toString());
                out.flush();
                return false;
            }
        }
        return true;
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
