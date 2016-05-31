package com.tinytrustframework.epos.service;

import java.util.List;
import java.util.Map;

import com.tinytrustframework.epos.entity.Role;
import com.tinytrustframework.epos.entity.Terminal;
import com.tinytrustframework.epos.entity.User;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
public interface UserService {
    /**
     * 新增或编辑用户信息
     *
     * @param user 用户信息
     */
    void saveOrUpdateUser(User user);

    /**
     * 查询角色信息列表
     *
     * @param params   查询条件
     * @param pageNo   分页参数，当前页
     * @param pageSize 分页参数，每页记录数
     */
    Map<String, Object> queryRoleList(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * 查询角色列表信息
     */
    List<Role> queryRoleList();

    /**
     * 查询用户信息列表
     *
     * @param params   查询条件
     * @param pageNo   分页参数，当前页
     * @param pageSize 分页参数，每页记录数
     */
    Map<String, Object> queryUserList(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * 根据用户状态查询用户信息列表
     */
    List<User> queryUserList(int status);

    /**
     * 查询用户详情信息
     *
     * @param userCode 用户编号
     */
    User getUserDetail(String userCode);

    /**
     * 查询终端列表
     *
     * @param params   查询条件
     * @param pageNo   分页参数，当前页
     * @param pageSize 分页参数，每页记录数
     */
    Map<String, Object> queryTerminalList(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * 根据用户编号查询终端信息
     *
     * @param userCode 用户编号
     */
    Terminal getTerminalDetail(String userCode);


    /**
     * 根据终端编号查询终端信息
     *
     * @param terminalCode 终端编号
     */
    Terminal getTerminalDetailByTerminalCode(String terminalCode);

    /**
     * 编辑终端信息
     *
     * @param terminal 终端
     */
    void saveOrUpdateTerminal(Terminal terminal);

    /**
     * 删除终端信息
     *
     * @param userCode 用户编号
     */
    boolean deleteTerminal(String userCode);
}
