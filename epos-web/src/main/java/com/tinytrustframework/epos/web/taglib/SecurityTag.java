package com.tinytrustframework.epos.web.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.SystemService;
import com.tinytrustframework.epos.common.utils.spring.SpringContextHelper;

/**
 * 自定义权限标签
 *
 * @author owen
 * @version [版本号, 2015-8-25]
*/
public class SecurityTag extends TagSupport {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2670710714292636650L;

    /**
     * 菜单路径
     */
    private String menuPath;


    public int doStartTag()
            throws JspException {
        super.doStartTag();
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        int roleCode = user.getRoleCode();//角色编号

        //指定menuPath
        SystemService systemService = (SystemService) SpringContextHelper.getObject("systemServiceImpl");
        boolean isAuthority = systemService.isAuthority(String.valueOf(roleCode), menuPath);
        if (isAuthority) {
            return Tag.EVAL_BODY_INCLUDE;
        } else {
            return Tag.SKIP_BODY;
        }
    }


    public int doEndTag()
            throws JspException {
        return super.doEndTag();
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

}
