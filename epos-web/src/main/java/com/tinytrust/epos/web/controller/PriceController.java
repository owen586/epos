package com.tinytrust.epos.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.web.controller.rsp.CommonRsp;
import com.tinytrust.epos.entity.PriceUser;
import com.tinytrust.epos.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tinytrust.epos.entity.PriceRole;

/**
 * 角色价格控制处理类
 *
 * @author owen
 * @version [版本号, 2015-8-3]
 */
@Slf4j
@Controller
@RequestMapping(value = "/price")
public class PriceController extends BaseController {

    @Resource
    private PriceService priceService;

    /**
     * 转发至角色价格列表页面
     */
    @RequestMapping(value = "/role/list/index")
    public String priceRoleListIndex() {
        return "price/price_role_list";
    }

    /**
     * 查询角色价格列表
     *
     * @param rateCommon 角色价格表单
     * @param request    HttpServletRequest
     */
    @RequestMapping(value = "/role/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp priceRoleList(PriceRole rateCommon, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        int roleCode = rateCommon.getRoleCode();//角色编号
        if (roleCode != -1) {
            params.put("roleCode", roleCode);
        }
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Map<String, Object> dataMap = priceService.queryPriceRoleList(params, Page.builder().pageStart(pageNo).pageSize(pageSize).build());

        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询价格角色列表信息成功").dataMap(dataMap).build();
    }

    /**
     * 转发至角色价格编辑页面
     *
     * @param roleCode 角色编号
     */
    @RequestMapping(value = "/role/edit/index")
    public ModelAndView priceRoleEditIndex(@RequestParam(required = false) String roleCode) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("price/price_role_edit");
        if (StringUtils.isNotEmpty(roleCode)) {
            PriceRole priceRole = priceService.getPriceRoleDetail(Integer.parseInt(roleCode));
            modelAndView.addObject("priceRole", priceRole);
        }
        return modelAndView;
    }

    /**
     * 转发至角色价格编辑页面
     *
     * @param priceRole 角色价格对象
     */
    @RequestMapping(value = "/role/edit", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp priceRoleEdit(PriceRole priceRole) {
        priceService.saveOrUpdatePriceRole(priceRole);
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("编辑角色价格成功").build();
    }

    /**
     * 转发至用戶价格列表页面
     */
    @RequestMapping(value = "/user/list/index")
    public String priceUserListIndex() {
        return "price/price_user_list";
    }

    /**
     * 查询用户价格列表
     *
     * @param priceUser 用户价格对象（密价對象）
     * @param request   HttpServletRequest
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp priceUserList(PriceUser priceUser, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        String cellphone = priceUser.getCellphone();//手机号
        if (StringUtils.isNotEmpty(cellphone)) {
            params.put("cellphone", cellphone);
        }

        String userName = priceUser.getUserName();//用户姓名
        if (StringUtils.isNotEmpty(userName)) {
            params.put("userName", "%" + userName + "%");
        }
        String userCode = priceUser.getUserCode();//用户编号
        if (StringUtils.isNotEmpty(userCode)) {
            params.put("userCode", userCode);
        }

        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        Map<String, Object> dataMap = priceService.queryPriceUserList(params, Page.builder().pageStart(pageNo).pageSize(pageSize).build());
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询用户价格列表信息成功").dataMap(dataMap).build();
    }

    /**
     * 转发至用户价格编辑页面
     *
     * @param userCode 用户编号
     */
    @RequestMapping(value = "/user/edit/index")
    public ModelAndView priceUserEditIndex(@RequestParam(required = false) String userCode) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("price/price_user_edit");
        if (StringUtils.isNotEmpty(userCode)) {
            PriceUser priceUser = priceService.getPriceUserDetail(userCode);
            modelAndView.addObject("priceUser", priceUser);
        }
        return modelAndView;
    }

    /**
     * 用户价格编辑
     *
     * @param priceUser 角色
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp priceRoleEdit(PriceUser priceUser) {
        priceService.saveOrUpdatePriceUser(priceUser);
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("编辑用户价格成功").build();
    }
}
