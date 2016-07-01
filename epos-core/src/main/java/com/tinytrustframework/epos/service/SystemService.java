package com.tinytrustframework.epos.service;

import java.util.List;

import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;

/**
 * @author Owen
 * @version [版本号, 2013-5-19]
 */
public interface SystemService {

    /**
     * 用户登录
     *
     * @param cellphone 手机号
     * @param password  密码
     */
    User login(String cellphone, String password);

    /**
     * 手机号重复性校验
     *
     * @param cellphone 手机号
     */
    boolean cellphoneUniqueCheck(String cellphone);

    /**
     * 根据角色编号查询权限菜单信息
     *
     * @param roleCode 角色编号
     */
    List<Menu> queryAuthorityRoleList(String roleCode);

    /**
     * 根据角色（用户）编号删除配置信息
     *
     * @param roleCode 角色（用户）编号
     */
    void deleteAuthorityRole(String roleCode);

    /**
     * 新增角色权限
     *
     * @param security 角色权限
     */
    void saveAuthorityRole(Authority security);

    /**
     * 是否有操作权限
     *
     * @param roleUserCode 角色权限编号
     * @param menuPath     菜单路径
     */
    boolean isAuthority(String roleUserCode, String menuPath);

    /**
     * 查询系统配置项列表
     */
    List<SystemConfig> querySystemConfigList();

    /**
     * 查询指定的系统配置项
     *
     * @param systemCode 系统配置项编码
     */
    SystemConfig querySystemConfig(String systemCode);

    /**
     * 更新系统配置项
     *
     * @param systemConfig 系统配置项
     */
    void updateSystemConfig(SystemConfig systemConfig);
}
