package com.tinytrustframework.epos.dao;

import java.util.List;

import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.entity.User;

/**
 * <一句话功能简述>
 *
 *
 * @author Owen
 * @version [版本号, 2013-5-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface SystemDao {

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
     * @return
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


//    /** <新增TEST>
//     *
//     * @param test
//     * @see [类、类#方法、类#成员]
//     */
//    void saveOrUpdate(Test test);
}
