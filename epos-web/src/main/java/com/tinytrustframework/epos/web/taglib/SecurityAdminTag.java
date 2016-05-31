package com.tinytrustframework.epos.web.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.entity.User;

/**
 * Admin专用自定义标签
 *
 * @author owen
 * @version [版本号, 2015-8-25]
 */
public class SecurityAdminTag extends TagSupport {

    private static final long serialVersionUID = 2670710714292636650L;

    public int doStartTag()
            throws JspException {
        super.doStartTag();
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        int roleCode = user.getRoleCode();//角色编号
        if (roleCode == Constant.ROLE_TYPE_ADMIN) {
            return Tag.EVAL_BODY_INCLUDE;
        } else {
            return Tag.SKIP_BODY;
        }
    }


    public int doEndTag()
            throws JspException {
        return super.doEndTag();
    }

}
