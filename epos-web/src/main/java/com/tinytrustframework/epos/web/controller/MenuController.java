package com.tinytrustframework.epos.web.controller;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.MenuService;
import com.tinytrustframework.epos.web.controller.response.CommonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制类
 *
 * @author owen
 * @version [版本号, 2015-7-29]
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
     * 查询子菜单
     *
     * @param menuCode 一级菜单编号
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/query_submenu/{menuCode}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse querySubmenu(@PathVariable int menuCode, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        int roleCode = user.getRoleCode();//角色编号
        List<Menu> menuList = menuService.querySubmenu(String.valueOf(roleCode), menuCode);
        if (null == menuList || menuList.isEmpty()) {
            return CommonResponse.builder().result(this.RESULT_FAIL).message("查询子菜单失败").build();
        } else {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("menuList", menuList);
            return CommonResponse.builder().result(this.RESULT_SUCCESS).message("查询子菜单成功").dataMap(dataMap).build();
        }
    }
}
