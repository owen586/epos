package com.tinytrustframework.epos.web.controller;

import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.service.MenuService;
import com.tinytrustframework.epos.service.SystemService;
import com.tinytrustframework.epos.web.controller.rsp.CommonRsp;
import com.tinytrustframework.epos.web.controller.rsp.TreeNodeRsp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限设置
 *
 * @author owen
 * @version [版本号, 2015-8-20]
 */
@Controller
@RequestMapping(value = "/authority")
public class AuthorityController extends BaseController {

    // 父节点编号
    private static final String TOP_PARENT_NODE_CODE = "0";

    @Resource
    private SystemService systemService;

    @Resource
    private MenuService menuService;

    /**
     * 转发至角色权限设置列表页面
     */
    @RequestMapping(value = "/role/list/index")
    public String securityRoleIndex() {
        return "system/security_role_index";
    }

    /**
     * 转发至角色权限设置页面
     */
    @RequestMapping(value = "/role/tree/index/{roleCode}")
    public String securityRoleConfigIndex(@PathVariable int roleCode, Model model) {
        model.addAttribute("roleCode", roleCode);
        return "system/security_role_config";
    }

    /**
     * 角色权限设置
     */
    @RequestMapping(value = "/role/config", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp securityRoleConfig(HttpServletRequest request) {
        String roleUserCode = request.getParameter("roleUserCode");//角色用户编号
        String menuCodes = request.getParameter("checkedMenuCodes");//菜单编号,多个编号以“|”分隔
        if (StringUtils.isBlank(roleUserCode) || StringUtils.isBlank(menuCodes)) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("角色权限设置失败,参数不完整").build();
        }

        try {
            //删除角色（用户）编号对应的角色权限信息
            systemService.deleteAuthorityRole(roleUserCode);

            //新增
            String[] checkedMenuCodes = menuCodes.split("\\|");
            String menuCode = null, menuUrl = null;
            for (String aMenu : checkedMenuCodes) {
                String[] menus = aMenu.split("#");
                menuCode = menus[0];//菜单编号
                menuUrl = menus[1];//菜单地址
                if (TOP_PARENT_NODE_CODE.equals(menuCode)) {
                    continue;
                }
                Authority authority = new Authority();
                authority.setMenuCode(Integer.parseInt(menuCode));
                authority.setRoleUserCode(roleUserCode);
                authority.setMenuUrl(menuUrl);

                systemService.saveAuthorityRole(authority);
            }
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("角色权限设置成功").build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("角色权限设置失败");
            return CommonRsp.builder().result(this.RESULT_FAIL).message("角色权限设置失败").build();
        }
    }

    /**
     * 根据角色编号查询权限菜单树
     */
    @RequestMapping(value = "/role/{roleCode}")
    @ResponseBody
    public CommonRsp querySecurityRoleList(@PathVariable String roleCode, HttpServletRequest request) {
        //查询菜单树列表
        List<Menu> menuTreeList = menuService.queryMenuList();
        //查询配置的菜单权限列表
        List<Menu> menuAuthorityList = systemService.queryAuthorityRoleList(roleCode);
        boolean parentNodeChecked = false;
        if (null != menuAuthorityList && !menuAuthorityList.isEmpty()) {
            for (Menu authorityMenu : menuAuthorityList) {
                for (Menu aMenu : menuTreeList) {
                    if (authorityMenu.getMenuCode() == aMenu.getMenuCode()) {
                        aMenu.setChecked(true);
                    }
                }
            }
            parentNodeChecked = true;
        }

        List<TreeNodeRsp> treeNodeList = this.dataConversion(menuTreeList, parentNodeChecked);
        if (null != treeNodeList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("treeNodeList", treeNodeList);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询权限菜单成功").dataMap(dataMap).build();
        } else {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("查询权限菜单失败").build();
        }
    }

    /**
     * 数据转换
     */
    private List<TreeNodeRsp> dataConversion(List<Menu> menuList, boolean parentNodeChecked) {
        if (null == menuList || menuList.isEmpty()) {
            return null;
        }

        List<TreeNodeRsp> treeNodeList = new ArrayList<TreeNodeRsp>(menuList.size());
        treeNodeList.add(new TreeNodeRsp(0, "角色权限管理 ", 0, true, "javascript:void(0);", parentNodeChecked));
        for (Menu aMenu : menuList) {
            treeNodeList.add(new TreeNodeRsp(aMenu.getMenuCode(), aMenu.getMenuName(), aMenu.getTopMenuCode(), true,
                    aMenu.getMenuUrl(), aMenu.isChecked()));
        }

        return treeNodeList;
    }
}
