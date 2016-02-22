package com.tinytrustframework.epos.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.entity.Role;
import com.tinytrustframework.epos.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tinytrustframework.epos.web.controller.response.CommonResponse;
import com.tinytrustframework.epos.common.utils.lang.DigestUtil;
import com.tinytrustframework.epos.common.utils.lang.I18nUtil;
import com.tinytrustframework.epos.entity.Terminal;
import com.tinytrustframework.epos.service.UserService;

/**
 * 用户控制类
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    /**
     * UserService
     */
    @Resource
    private UserService userService;

    /**
     * <用户注册>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse register(User user) {
        CommonResponse commonRes = new CommonResponse();
        if (null == user) {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("注册信息为空");
        } else {
            //初始化默认值
            String password = user.getPassword();
            String cellphone = user.getCellphone();
            String passwordEncode =
                    DigestUtil.md5(cellphone + password + I18nUtil.getProperty("login.and.register.key"));
            user.setPassword(passwordEncode);//保存密码MD5摘要
            user.setRoleCode(Constant.ROLE_TYPE_JUNIOR_DISTRIBUTOR);//默认角色:二级经销商
            user.setStatus(Constant.USER_STATE_WAITING_VALIDATE);//默用户认状态:待审核
            if (StringUtils.isEmpty(user.getTopUserCode()))//默认上级(邀请码):402884ea4ed2b7fa014ed2ba61240002
            {
                user.setTopUserCode(I18nUtil.getProperty("top.user.code.default"));
            } else {
                //根据用户填写的上级编号(邀请码)查询指定用户是否存在，如果不存在则使用默认上级(邀请码 )
                User topUser = userService.getUserDetail(user.getTopUserCode());
                if (null == topUser) {
                    user.setTopUserCode(I18nUtil.getProperty("top.user.code.default"));
                }
            }
            user.setTranferType(Constant.USER_TRANFER_TYPE_T1);//到账时间:T+1
            userService.saveOrUpdateUser(user);

            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("TinyTrust:\r\n\n注册成功，请耐心等待审核!\r\n\n点击确定按钮后，2秒内系统自动转至系统首页!");
        }
        return commonRes;
    }

    /**
     * <转发至角色列表页面>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/role/index")
    public String roleIndex() {
        return "user/role_list";
    }

    /**
     * <角色列表查询>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/role/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse roleList(Role role, HttpServletRequest request) {
        CommonResponse commonRes = new CommonResponse();
        Map<String, Object> params = new HashMap<String, Object>();
        String roleName = role.getRoleName();//角色名称
        if (StringUtils.isNotEmpty(roleName)) {
            params.put("roleName", roleName);
        }
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        try {
            Map<String, Object> dataMap = userService.queryRoleList(params, pageNo, pageSize);
            commonRes.setDataMap(dataMap);
            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("查询角色列表信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("查询角色列表信息失败");
        }
        return commonRes;
    }

    /**
     * <角色列表查询>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/role/all", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse roleList() {
        CommonResponse commonRes = new CommonResponse();
        List<Role> roleList = userService.queryRoleList();
        if (null == roleList || roleList.isEmpty()) {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("查询角色列表信息失败");
        } else {
            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("查询角色列表信息成功");
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("roleList", roleList);
            commonRes.setDataMap(dataMap);
        }
        return commonRes;
    }

    /**
     * <转换至用户列表页面>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/index")
    public String userIndex() {
        return "user/user_list";
    }

    /**
     * <用户列表查询>
     *
     * @param userForm
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse userList(User userForm, HttpServletRequest request) {
        CommonResponse commonRes = new CommonResponse();

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

        Map<String, Object> dataMap = userService.queryUserList(params, pageNo, pageSize);
        commonRes.setDataMap(dataMap);
        commonRes.setResult(this.RESULT_SUCCESS);
        commonRes.setMessage("查询用户列表信息成功");
        return commonRes;

    }

    /**
     * <用户列表查询>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/list/all", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse userList() {
        CommonResponse commonRes = new CommonResponse();

        List<User> userList = userService.queryUserList(Constant.USER_STATE_OK);
        if (null != userList && !userList.isEmpty()) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("userList", userList);
            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("查询用户列表成功");
            commonRes.setDataMap(dataMap);
        } else {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("查询用户列表失败");
        }
        return commonRes;
    }

    /**
     * <转发至用户编辑页面>
     *
     * @param userCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/user_edit_index")
    public ModelAndView userEditIndex(@RequestParam(required = false)
                                      String userCode) {
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
     * <用户编辑>
     * <可以重置用户密码、为用户配置外部商家编号、调整用户角色、上级以及到账类型>
     *
     * @param userForm
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/user_edit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse userEdit(User userForm, HttpServletRequest request) {
        CommonResponse commonRes = new CommonResponse();
        if (null == userForm) {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("用户信息不完整");
        } else {
            String userCode = userForm.getUserCode();
            if (StringUtils.isEmpty(userCode))//新增
            {
                String cellphone = userForm.getCellphone();
                String password = userForm.getPassword();
                String passwordEncode =
                        DigestUtil.md5(cellphone + password + I18nUtil.getProperty("login.and.register.key"));
                userForm.setPassword(passwordEncode);//保存密码MD5摘要
                //上级编号
                if (StringUtils.isEmpty(userForm.getTopUserCode()))//默认上级(邀请码):402884ea4ed2b7fa014ed2ba61240002
                {
                    userForm.setTopUserCode(I18nUtil.getProperty("top.user.code.default"));
                } else {
                    //根据用户填写的上级编号(邀请码)查询指定用户是否存在，如果不存在则使用默认上级(邀请码 )
                    User topUser = userService.getUserDetail(userForm.getTopUserCode());
                    if (null == topUser) {
                        userForm.setTopUserCode(I18nUtil.getProperty("top.user.code.default"));
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
                            DigestUtil.md5(user.getCellphone() + password + I18nUtil.getProperty("login.and.register.key"));
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
                    user.setTopUserCode(I18nUtil.getProperty("top.user.code.default"));
                } else {
                    //根据用户填写的上级编号(邀请码)查询指定用户是否存在，如果不存在则使用默认上级(邀请码 )
                    User topUser = userService.getUserDetail(userForm.getTopUserCode());
                    if (null == topUser) {
                        user.setTopUserCode(I18nUtil.getProperty("top.user.code.default"));
                    } else {
                        user.setTopUserCode(topUserCode);
                    }
                }
                userService.saveOrUpdateUser(user);
                
                /*//FIXME 如果用户编辑自身敏感信息，比如权限，这里需要同步更新会话中用户信息
                User userInSession = (User)request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
                String userCodeInSession = userInSession.getUserCode();//用户编号
                if (userCode.equals(userCodeInSession))//编辑
                {
                    //实时查询用户信息,主要是更新用户角色信息
                    User userSynchronized = userService.getUserDetail(userCodeInSession);
                    request.getSession().setAttribute(Constant.CURRENT_USER_IN_SESSION, userSynchronized);//保存会话
                }*/
            }
            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("用户编辑成功");
        }
        return commonRes;
    }

    /**
     * <用户状态编辑>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/state_edit/{state}/{userCode}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse userStateEdit(@PathVariable
                                        int state, @PathVariable
                                        String userCode) {
        CommonResponse commonRes = new CommonResponse();
        User user = userService.getUserDetail(userCode);
        if (null == user) {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("用户不存在");
        } else {
            user.setStatus(state);
            userService.saveOrUpdateUser(user);
            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("状态更新成功");
        }
        return commonRes;
    }

    /**
     * <转发至终端列表页面>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/terminal/index")
    public String terminalIndex() {
        return "user/terminal_list";
    }

    /**
     * <终端列表查询>
     *
     * @param request HttpServletRequest
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/terminal/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse terminalList(HttpServletRequest request) {
        CommonResponse commonRes = new CommonResponse();
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

        Map<String, Object> dataMap = userService.queryTerminalList(params, pageNo, pageSize);
        commonRes.setDataMap(dataMap);
        commonRes.setResult(this.RESULT_SUCCESS);
        commonRes.setMessage("查询终端列表信息成功");
        return commonRes;
    }

    /**
     * <转发至pos终端编辑页面>
     *
     * @param userCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/terminal/edit/index")
    public ModelAndView terminalEditIndex(@RequestParam(required = false)
                                          String userCode) {
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
     * <编辑pos终端>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/terminal/edit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse terminalEdit(Terminal terminalForm) {
        CommonResponse commonRes = new CommonResponse();
        if (null == terminalForm) {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("终端信息不完整");
        } else {
            userService.saveOrUpdateTerminal(terminalForm);

            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("终端信息编辑成功");
        }
        return commonRes;
    }

    /**
     * <删除pos终端信息>
     *
     * @return
     * @userCode 用户编码
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/terminal/delete/{userCode}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse terminalDelete(@PathVariable
                                         String userCode) {
        CommonResponse commonRes = new CommonResponse();
        if (userService.deleteTerminal(userCode)) {
            commonRes.setResult(this.RESULT_SUCCESS);
            commonRes.setMessage("终端信息删除成功");
        } else {
            commonRes.setResult(this.RESULT_FAIL);
            commonRes.setMessage("终端信息删除失败");
        }
        return commonRes;
    }

}
