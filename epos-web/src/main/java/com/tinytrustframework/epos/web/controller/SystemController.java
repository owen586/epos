package com.tinytrustframework.epos.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tinytrustframework.epos.web.controller.rsp.CommonRsp;
import com.tinytrustframework.epos.common.statics.Constant;
import com.tinytrustframework.epos.common.utils.props.PropUtils;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.MenuService;
import com.tinytrustframework.epos.service.SystemService;
import nl.captcha.Captcha;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tinytrustframework.epos.common.utils.lang.DigestUtil;
import com.tinytrustframework.epos.service.UserService;

/**
 * 系统控制处理类
 *
 * @author Owen
 * @version [版本号, 2013-5-19]
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController extends BaseController {

    @Resource
    private SystemService systemService;

    @Resource
    private MenuService menuService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/index")
    public String index() {
        return "/login";
    }

    /**
     * 用户登录
     *
     * @param user    用户表单对象
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(User user, HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        String captcha = request.getParameter("captcha");//验证码
        if (null == user || StringUtils.isEmpty(captcha)) {
            modelAndView.addObject("login_message_error", "登陆信息为空");
            modelAndView.setViewName("login");
            return modelAndView;
        }
        HttpSession session = request.getSession();
        Captcha captchaInSession = (Captcha) session.getAttribute(Captcha.NAME);
        if (!captchaInSession.
                isCorrect(captcha.toLowerCase().trim())) {
            modelAndView.addObject("login_message_error", "验证码错误");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        User userLoginResult = systemService.login(user.getCellphone(), user.getPassword());
        if (null == userLoginResult) {
            modelAndView.addObject("login_message_error", "无效的用户名或密码");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        //用户状态
        int status = userLoginResult.getStatus();
        if (status == Constant.USER_STATE_STOP)//停用
        {
            modelAndView.addObject("login_message_error", "用户账号停用");
            modelAndView.setViewName("login");
            return modelAndView;
        } else if (status == Constant.USER_STATE_WAITING_VALIDATE) {
            modelAndView.addObject("login_message_error", "账户审核中，请稍后登陆");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        session.setAttribute(Constant.CURRENT_USER_IN_SESSION, userLoginResult);//保存会话
        modelAndView.setViewName("redirect:/system/console");
        return modelAndView;
    }

    /**
     * 转换至控制台
     */
    @RequestMapping(value = "/console")
    public ModelAndView console(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        String userCode = user.getUserCode();//用户编号
        //实时查询用户信息,主要是更新用户角色信息
        User userSynchronized = userService.getUserDetail(userCode);
        request.getSession().setAttribute(Constant.CURRENT_USER_IN_SESSION, userSynchronized);//保存会话

        int roleCode = userSynchronized.getRoleCode();//角色编号
        List<Menu> menuList = menuService.queryByMenuLevel(String.valueOf(roleCode), Constant.MENU_LEVEL_FIRST);

        if (null == menuList || menuList.isEmpty()) {
            modelAndView.addObject("login_message_error", "菜单初始化失败");
            modelAndView.setViewName("login");
        } else {
            modelAndView.addObject("menuList", menuList);
            modelAndView.setViewName("console");
        }
        return modelAndView;
    }

    /**
     * 转发至注册页面
     */
    @RequestMapping(value = "/register")
    public String registerIndex() {
        return "register";
    }

    /**
     * 转发至修改密码页面
     */
    @RequestMapping(value = "/modify_password_index")
    public String modifyPasswordIndex() {
        return "system/modify_password";
    }

    /**
     * 修改密码
     *
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/modify_password", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp modifyPassword(HttpServletRequest request) {
        String oldPwd = request.getParameter("oldPwd");//原始密码
        String newPwd = request.getParameter("newPwd");//新密码

        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER_IN_SESSION);
        String cellphone = user.getCellphone();
        String oldPwdMd5 = DigestUtil.md5(cellphone + oldPwd + PropUtils.getPropertyValue("login.and.register.key"));
        if (!oldPwdMd5.equals(user.getPassword())) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("旧密码错误").build();
        } else {
            String newPwdMd5 = DigestUtil.md5(cellphone + newPwd + PropUtils.getPropertyValue("login.and.register.key"));
            user.setPassword(newPwdMd5);
            userService.saveOrUpdateUser(user);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("密码修改成功").build();
        }
    }

    /**
     * 安全退出系统
     */
    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(Constant.CURRENT_USER_IN_SESSION);
            String userCode = user.getUserCode();
            String userName = user.getUserName();
            log.info("用户安全退出! userCode: {},userName:{}", userCode, userName);
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机号唯一性校验
     */
    @RequestMapping(value = "/cellphone_unique_check", method = RequestMethod.POST)
    @ResponseBody
    public boolean cellphoneUniqueCheck(@RequestParam(required = true) String cellphone) {
        boolean isUnique = systemService.cellphoneUniqueCheck(cellphone);
        return isUnique;
    }

    /**
     * 验证码校验
     */
    @RequestMapping(value = "/captcha_check", method = RequestMethod.POST)
    @ResponseBody
    public boolean captchaCheck(HttpServletRequest request) {
        boolean isCorrect = false;//默认验证码校验失败
        HttpSession session = request.getSession();
        String captcha = request.getParameter("captcha").toLowerCase().trim();//验证码
        Captcha captchaInSession = (Captcha) session.getAttribute(Captcha.NAME);
        if (captchaInSession.isCorrect(captcha)) {
            isCorrect = true;
        } else {
            isCorrect = false;
        }
        return isCorrect;
    }

    /**
     * 转发至统计面板
     * //TODO 统计面板
     */
    @RequestMapping(value = "/workpanel")
    public String workpanel() {
        return "system/workpanel";
    }

    /**
     * 转发至系统配置项列表页面
     */
    @RequestMapping(value = "/config/index")
    public String systemConfigIndex() {
        return "system/system_config_index";
    }

    /**
     * 查询系统配置项列表
     */
    @RequestMapping(value = "/config/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp systemConfigList() {
        List<SystemConfig> systemConfigList = systemService.querySystemConfigList();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("systemConfigList", systemConfigList);
        return CommonRsp.builder().result(this.RESULT_SUCCESS).message("查询系统配置项成功").dataMap(dataMap).build();
    }

    /**
     * 编辑系统配置项
     */
    @RequestMapping(value = "/config/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonRsp systemConfigEdit(HttpServletRequest request) {
        String systemCode = request.getParameter("systemCode");
        SystemConfig systemConfig = systemService.querySystemConfig(systemCode);
        if (null == systemConfig) {
            return CommonRsp.builder().result(this.RESULT_FAIL).message("系统配置项不存在").build();
        } else {
            String systemValue = request.getParameter("systemValue");
            systemConfig.setSysValue(systemValue);
            systemService.updateSystemConfig(systemConfig);
            return CommonRsp.builder().result(this.RESULT_SUCCESS).message("系统配置项更新成功").build();
        }
    }
}
