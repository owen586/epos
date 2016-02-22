package com.tinytrustframework.epos.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.tinytrustframework.epos.web.controller.response.CommonResponse;
import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单控制类
 *
 *
 * @author owen
 * @version [版本号, 2015-7-29]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {

    /**
     * MenuService
     */
    @Resource
    private MenuService menuService;

    /**
     * <一句话功能简述>
     *
     *
     * @param menuCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/query_submenu/{menuCode}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse querySubmenu(@PathVariable
                                       int menuCode, HttpServletRequest request) {
        CommonResponse commonRes = new CommonResponse();
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        int roleCode = user.getRoleCode();//角色编号
        List<Menu> menuList = menuService.querySubmenu(String.valueOf(roleCode), menuCode);
        if (null == menuList || menuList.isEmpty()) {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("查询子菜单失败");
        } else {
            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("查询子菜单成功");
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("menuList", menuList);
            commonRes.setDataMap(dataMap);
        }
        return commonRes;
    }
}
