/*
 * 文 件 名:  SystemService.java
 * 版    权:  www.astcard.com. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2013-5-19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.service;

import java.util.List;

import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;

/**
 * <一句话功能简述>
 *
 *
 * @author Owen
 * @version [版本号, 2013-5-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface SystemService {
    /**
     * <用户登录>
     *
     *
     * @param cellphone 手机号
     * @param password  密码
     * @return
     * @see [类、类#方法、类#成员]
     */
    User login(String cellphone, String password);

    /**
     * <手机号重复性校验>
     *
     *
     * @param cellphone 手机号
     * @return false:手机号已经存在 true:手机账号不存在
     * @see [类、类#方法、类#成员]
     */
    boolean cellphoneUniqueCheck(String cellphone);

    /**
     * <根据角色编号查询权限菜单信息>
     *
     *
     * @param roleCode 角色编号
     * @return
     * @see [类、类#方法、类#成员]
     */
    List<Menu> queryAuthorityRoleList(String roleCode);

    /**
     * <根据角色（用户）编号删除配置信息>
     *
     *
     * @param roleCode 角色（用户）编号
     * @see [类、类#方法、类#成员]
     */
    void deleteAuthorityRole(String roleCode);

    /**
     * <新增角色权限>
     *
     *
     * @param security 角色权限
     * @see [类、类#方法、类#成员]
     */
    void saveAuthorityRole(Authority security);

    /**
     * <是否有操作权限>
     *
     *
     * @param roleUserCode 角色权限编号
     * @param menuPath     菜单路径
     * @return
     * @see [类、类#方法、类#成员]
     */
    boolean isAuthority(String roleUserCode, String menuPath);


    /**
     * <查询系统配置项列表>
     *
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    List<SystemConfig> querySystemConfigList();

    /**
     * <查询指定的系统配置项>
     *
     *
     * @param systemCode 系统配置项编码
     * @return
     * @see [类、类#方法、类#成员]
     */
    SystemConfig querySystemConfig(String systemCode);

    /**
     * <更新系统配置项>
     *
     *
     * @param systemConfig 系统配置项
     * @see [类、类#方法、类#成员]
     */
    void updateSystemConfig(SystemConfig systemConfig);


//    //TEST
//    
//    /** <新增TEST>
//     *
//     * @param test
//     * @see [类、类#方法、类#成员]
//     */
//    void saveOrUpdate(Test test);
}
