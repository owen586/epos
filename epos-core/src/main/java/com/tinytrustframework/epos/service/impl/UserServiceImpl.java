package com.tinytrustframework.epos.service.impl;

import com.tinytrustframework.epos.dao.UserDao;
import com.tinytrustframework.epos.entity.Role;
import com.tinytrustframework.epos.entity.Terminal;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <一句话功能简述>
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * UserDao
     */
    @Resource
    private UserDao userDao;

    public void saveOrUpdateUser(User user) {
        userDao.saveOrUpdateUser(user);
    }


    public Map<String, Object> queryRoleList(Map<String, Object> params, int pageNo, int pageSize) {
        return userDao.queryRoleList(params, pageNo, pageSize);
    }


    public List<Role> queryRoleList() {
        return userDao.queryRoleList();
    }


    public Map<String, Object> queryUserList(Map<String, Object> params, int pageNo, int pageSize) {
        return userDao.queryUserList(params, pageNo, pageSize);
    }


    public User getUserDetail(String userCode) {
        return userDao.getUserDetail(userCode);
    }


    public Map<String, Object> queryTerminalList(Map<String, Object> params, int pageNo, int pageSize) {
        return userDao.queryTerminalList(params, pageNo, pageSize);
    }


    public Terminal getTerminalDetail(String userCode) {
        return userDao.getTerminalDetail(userCode);
    }


    public void saveOrUpdateTerminal(Terminal terminal) {
        userDao.saveOrUpdateTerminal(terminal);
    }


    public boolean deleteTerminal(String userCode) {
        return userDao.deleteTerminal(userCode);
    }


    public List<User> queryUserList(int status) {
        return userDao.queryUserList(status);
    }


    public Terminal getTerminalDetailByTerminalCode(String terminalCode) {
        return userDao.getTerminalDetailByTerminalCode(terminalCode);
    }
}
