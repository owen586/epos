package com.tinytrustframework.epos.service.impl;

import com.tinytrustframework.epos.common.utils.lang.page.Page;
import com.tinytrustframework.epos.dao.UserDao;
import com.tinytrustframework.epos.entity.Role;
import com.tinytrustframework.epos.entity.Terminal;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    /**
     * 新增或编辑用户信息
     *
     * @param user 用户信息
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveOrUpdateUser(User user) {
        userDao.saveOrUpdateUser(user);
    }

    /**
     * 查询角色信息列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryRoleList(Map<String, Object> businessParams, Page pageParams) {
        return userDao.queryRoleList(businessParams, pageParams);
    }

    /**
     * 查询角色列表信息
     */
    public List<Role> queryRoleList() {
        return userDao.queryRoleList();
    }

    /**
     * 查询用户信息列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryUserList(Map<String, Object> businessParams, Page pageParams) {
        return userDao.queryUserList(businessParams, pageParams);
    }

    /**
     * 查询用户详情信息
     *
     * @param userCode 用户编号
     */
    public User getUserDetail(String userCode) {
        return userDao.getUserDetail(userCode);
    }

    /**
     * 根据用户状态查询用户信息列表
     */
    public List<User> queryUserList(int status) {
        return userDao.queryUserList(status);
    }


    /**
     * 查询终端列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryTerminalList(Map<String, Object> businessParams, Page pageParams) {
        return userDao.queryTerminalList(businessParams, pageParams);
    }

    /**
     * 根据用户编号查询终端信息
     *
     * @param userCode 用户编号
     */
    public Terminal getTerminalDetail(String userCode) {
        return userDao.getTerminalDetail(userCode);
    }

    /**
     * 编辑终端信息
     *
     * @param terminal 终端
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveOrUpdateTerminal(Terminal terminal) {
        userDao.saveOrUpdateTerminal(terminal);
    }

    /**
     * 删除终端信息
     *
     * @param userCode 用户编号
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean deleteTerminal(String userCode) {
        return userDao.deleteTerminal(userCode);
    }

    /**
     * 根据终端编号查询终端信息
     *
     * @param terminalCode 终端编号
     */
    public Terminal getTerminalDetailByTerminalCode(String terminalCode) {
        return userDao.getTerminalDetailByTerminalCode(terminalCode);
    }
}
