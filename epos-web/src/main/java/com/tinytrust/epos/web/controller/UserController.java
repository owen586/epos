package com.tinytrust.epos.web.controller;

import com.tinytrust.epos.common.statics.Constant;
import com.tinytrust.epos.common.utils.lang.DigestUtil;
import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.common.utils.lang.props.PropUtils;
import com.tinytrust.epos.entity.Role;
import com.tinytrust.epos.entity.Terminal;
import com.tinytrust.epos.entity.User;
import com.tinytrust.epos.service.UserService;
import com.tinytrust.epos.web.controller.rsp.CommonRsp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制类
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    /**
     * UserService
     */
    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp register(User user) {
        if (null == user) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("注册信息为空").build();
        } else {
            //初始化默认值
            String password = user.getPassword();
            String cellphone = user.getCellphone();
            String passwordEncode =
                    DigestUtil.md5(cellphone + password + PropUtils.getPropertyValue("login.and.register.key"));
            user.setPassword(passwordEncode);//保存密码MD5摘要
            user.setRoleCode(Constant.ROLE_TYPE_JUNIOR_DISTRIBUTOR);//默认角色:二级经销商
            user.setStatus(Constant.USER_STATE_WAITING_VALIDATE);//默用户认状态:待审核
            if (StringUtils.isEmpty(user.getTopUserCode()))//默认上级(邀请码):402884ea4ed2b7fa014ed2ba61240002
            {
                user.setTopUserCode(PropUtils.getPropertyValue("top.user.code.default"));
            } else {
                //根据用户填写的上级编号(邀请码)查询指定用户是否存在，如果不存在则使用默认上级(邀请码 )
                User topUser = userService.getUserDetail(user.getTopUserCode());
                if (null == topUser) {
                    user.setTopUserCode(PropUtils.getPropertyValue("top.user.code.default"));
                }
            }
            user.setTranferType(Constant.USER_TRANFER_TYPE_T1);//到账时间:T+1
            userService.saveOrUpdateUser(user);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("\"TinyTrust:\\r\\n\\n注册成功，请耐心等待审核!\\r\\n\\n点击确定按钮后，2秒内系统自动转至系统首页!\"").build();
        }
    }

    /**
     * 转发至角色列表页面
     */
    @RequestMapping(value = "/role/index")
    public String roleIndex() {
        return "user/role_list";
    }

    /**
     * 角色列表查询
     **/
    @RequestMapping(value = "/role/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp roleList(Role role, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        String roleName = role.getRoleName();//角色名称
        if (StringUtils.isNotEmpty(roleName)) {
            params.put("roleName", roleName);
        }
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        try {
            Map<String, Object> dataMap = userService.queryRoleList(params, Page.builder().pageStart(pageNo).pageSize(pageSize).build());
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询角色列表信息成功").dataMap(dataMap).build();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonRsp.builder().result(this.RESULT_FAIL).message("查询角色列表信息失败").build();
        }
    }

    /**
     * 角色列表查询
     */
    @RequestMapping(value = "/role/all", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp roleList() {
        List<Role> roleList = userService.queryRoleList();
        if (null == roleList || roleList.isEmpty()) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("查询角色列表信息失败").build();
        } else {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("roleList", roleList);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询角色列表信息成功").dataMap(dataMap).build();
        }
    }

    /**
     * 转换至用户列表页面
     */
    @RequestMapping(value = "/index")
    public String userIndex() {
        return "user/user_list";
    }

    /**
     * 用户列表查询
     *
     * @param userForm 用户查询表单
     * @param request  HttpServletRequest
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp userList(User userForm, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userCode = userForm.getUserCode();//用户编号
        if (StringUtils.isNotEmpty(userCode)) {
            params.put("userCode", userCode);
        }

        String cellphone = userForm.getCellphone();//手机号
        if (StringUtils.isNotEmpty(cellphone)) {
            params.put("cellphone", cellphone);
        }

        String userName = userForm.getUserName();//用户名称
        if (StringUtils.isNotEmpty(userName)) {
            params.put("userName", "%" + userName + "%");
        }

        String topUserCode = userForm.getTopUserCode();//上级用户编号 
        if (StringUtils.isNotEmpty(topUserCode)) {
            params.put("topUserCode", topUserCode);
        }

        String outterUserCode = userForm.getOutterUserCode();//外部商家编号 
        if (StringUtils.isNotEmpty(outterUserCode)) {
            params.put("outterUserCode", outterUserCode);
        }

        int tranferType = userForm.getTranferType();//到账类型
        if (tranferType != -1) {
            params.put("tranferType", tranferType);
        }

        int status = userForm.getStatus();//用户状态
        if (status != -1) {
            params.put("status", status);
        }

        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        int loginUserRoleCode = user.getRoleCode();//登陆用户角色编号
        if (loginUserRoleCode == Constant.ROLE_TYPE_ADMIN) {
            int roleCode = userForm.getRoleCode();//角色编号
            if (roleCode != -1) {
                params.put("roleCode", roleCode);
            }
        } else {
            //非系统管理员只能查询下级商户信息
            String loginUserCode = user.getUserCode();//登陆用户编号
            params.put("topUserCode", loginUserCode);
        }

        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        Map<String, Object> dataMap = userService.queryUserList(params, Page.builder().pageStart(pageNo).pageSize(pageSize).build());
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询用户列表信息成功").dataMap(dataMap).build();

    }

    /**
     * 用户列表查询
     */
    @RequestMapping(value = "/list/all", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp userList() {
        List<User> userList = userService.queryUserList(Constant.USER_STATE_OK);
        if (null != userList && !userList.isEmpty()) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("userList", userList);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询用户列表成功").dataMap(dataMap).build();
        } else {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("查询用户列表失败").build();
        }
    }

    /**
     * 转发至用户编辑页面
     *
     * @param userCode 用户编号
     */
    @RequestMapping(value = "/user_edit_index")
    public ModelAndView userEditIndex(@RequestParam(required = false) String userCode) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/user_edit");

        List<Role> roleList = userService.queryRoleList();
        modelAndView.addObject("roleList", roleList);
        if (StringUtils.isNotEmpty(userCode)) {
            User user = userService.getUserDetail(userCode);
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    /**
     * 用户编辑
     * 可以重置用户密码、为用户配置外部商家编号、调整用户角色、上级以及到账类型
     *
     * @param userForm 用户表单信息
     */
    @RequestMapping(value = "/user_edit", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp userEdit(User userForm, HttpServletRequest request) {
        if (null == userForm) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("用户信息不完整").build();
        } else {
            String userCode = userForm.getUserCode();
            if (StringUtils.isEmpty(userCode))//新增
            {
                String cellphone = userForm.getCellphone();
                String password = userForm.getPassword();
                String passwordEncode =
                        DigestUtil.md5(cellphone + password + PropUtils.getPropertyValue("login.and.register.key"));
                userForm.setPassword(passwordEncode);//保存密码MD5摘要
                //上级编号
                if (StringUtils.isEmpty(userForm.getTopUserCode()))//默认上级(邀请码):402884ea4ed2b7fa014ed2ba61240002
                {
                    userForm.setTopUserCode(PropUtils.getPropertyValue("top.user.code.default"));
                } else {
                    //根据用户填写的上级编号(邀请码)查询指定用户是否存在，如果不存在则使用默认上级(邀请码 )
                    User topUser = userService.getUserDetail(userForm.getTopUserCode());
                    if (null == topUser) {
                        userForm.setTopUserCode(PropUtils.getPropertyValue("top.user.code.default"));
                    }
                }
                userForm.setTranferType(Constant.USER_TRANFER_TYPE_T1);//到账时间:T+1
                userService.saveOrUpdateUser(userForm);
            } else {
                //编辑
                User user = userService.getUserDetail(userCode);

                user.setUserName(userForm.getUserName());
                String password = userForm.getPassword();
                if (StringUtils.isNotEmpty(password))//如果重置了密码则修改
                {
                    String passwordEncode =
                            DigestUtil.md5(user.getCellphone() + password + PropUtils.getPropertyValue("login.and.register.key"));
                    user.setPassword(passwordEncode);//保存密码MD5摘要
                }
                user.setRoleCode(userForm.getRoleCode());
                user.setTranferType(userForm.getTranferType());
                user.setStatus(userForm.getStatus());
                String outterUserCode = userForm.getOutterUserCode();//外部商家编号
                if (StringUtils.isNotEmpty(outterUserCode)) {
                    user.setOutterUserCode(outterUserCode);
                }
                String topUserCode = userForm.getTopUserCode();//上级编号
                if (StringUtils.isEmpty(topUserCode))//默认上级(邀请码):402884ea4ed2b7fa014ed2ba61240002
                {
                    user.setTopUserCode(PropUtils.getPropertyValue("top.user.code.default"));
                } else {
                    //根据用户填写的上级编号(邀请码)查询指定用户是否存在，如果不存在则使用默认上级(邀请码 )
                    User topUser = userService.getUserDetail(userForm.getTopUserCode());
                    if (null == topUser) {
                        user.setTopUserCode(PropUtils.getPropertyValue("top.user.code.default"));
                    } else {
                        user.setTopUserCode(topUserCode);
                    }
                }
                userService.saveOrUpdateUser(user);
            }
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("用户编辑成功").build();
        }
    }

    /**
     * 用户状态编辑
     */
    @RequestMapping(value = "/state_edit/{state}/{userCode}", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp userStateEdit(@PathVariable int state, @PathVariable String userCode) {
        User user = userService.getUserDetail(userCode);
        if (null == user) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("用户不存在").build();
        } else {
            user.setStatus(state);
            userService.saveOrUpdateUser(user);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("状态更新成功").build();
        }
    }

    /**
     * 转发至终端列表页面
     */
    @RequestMapping(value = "/terminal/index")
    public String terminalIndex() {
        return "user/terminal_list";
    }

    /**
     * 终端列表查询
     *
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/terminal/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp terminalList(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();

        String terminalCode = request.getParameter("terminalCode");//终端编号
        if (StringUtils.isNotEmpty(terminalCode)) {
            params.put("terminalCode", terminalCode);
        }

        String cellphone = request.getParameter("cellphone");//手机号
        if (StringUtils.isNotEmpty(cellphone)) {
            params.put("cellphone", cellphone);
        }

        String usrCode = request.getParameter("userCode");//用户编号
        if (StringUtils.isNotEmpty(usrCode)) {
            params.put("userCode", usrCode);
        }

        String userName = request.getParameter("userName");//用户名称
        if (StringUtils.isNotEmpty(userName)) {
            params.put("userName", "%" + userName + "%");
        }

        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        Map<String, Object> dataMap = userService.queryTerminalList(params, Page.builder().pageStart(pageNo).pageSize(pageSize).build());
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询终端列表信息成功").dataMap(dataMap).build();
    }

    /**
     * 转发至pos终端编辑页面
     */
    @RequestMapping(value = "/terminal/edit/index")
    public ModelAndView terminalEditIndex(@RequestParam(required = false) String userCode) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/terminal_edit");
        if (StringUtils.isNotEmpty(userCode)) {
            Terminal terminal = userService.getTerminalDetail(userCode);
            modelAndView.addObject("terminal", terminal);
        }
        modelAndView.addObject("userCode", userCode);
        return modelAndView;
    }

    /**
     * 编辑pos终端
     */
    @RequestMapping(value = "/terminal/edit", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp terminalEdit(Terminal terminalForm) {
        if (null == terminalForm) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("终端信息不完整").build();
        } else {
            userService.saveOrUpdateTerminal(terminalForm);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("终端信息编辑成功").build();
        }
    }

    /**
     * 删除pos终端信息
     *
     * @userCode 用户编码
     */
    @RequestMapping(value = "/terminal/delete/{userCode}", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp terminalDelete(@PathVariable String userCode) {
        if (userService.deleteTerminal(userCode)) {
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("终端信息删除成功").build();
        } else {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("终端信息删除失败").build();
        }
    }

}
