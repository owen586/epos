package com.tinytrust.epos.web.controller;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.entity.Menu;
import com.tinytrust.epos.entity.User;
import com.tinytrust.epos.service.MenuService;
import com.tinytrust.epos.web.controller.rsp.CommonRsp;
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

    @Resource
    private MenuService menuService;

    /**
     * 查询子菜单
     *
     * @param menuCode 一级菜单编号
     */
    @RequestMapping(value = "/query_submenu/{menuCode}")
    @ResponseBody
    public CommonRsp querySubmenu(@PathVariable int menuCode, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        int roleCode = user.getRoleCode();//角色编号
        List<Menu> menuList = menuService.querySubmenu(String.valueOf(roleCode), menuCode);
        if (null == menuList || menuList.isEmpty()) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("查询子菜单失败").build();
        } else {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("menuList", menuList);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询子菜单成功").dataMap(dataMap).build();
        }
    }
}
