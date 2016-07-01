package com.tinytrustframework.epos.service.impl;

import com.tinytrustframework.epos.common.utils.lang.DigestUtil;
import com.tinytrustframework.epos.common.utils.lang.props.PropUtils;
import com.tinytrustframework.epos.dao.SystemDao;
import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.SystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author owen
 * @version [版本号, 2013-5-19]
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Resource
    private SystemDao systemDao;

    /**
     * 用户登录
     *
     * @param cellphone 手机号
     * @param password  密码
     */
    public User login(String cellphone, String password) {
        String md5Password = DigestUtil.md5(cellphone + password + PropUtils.getPropertyValue("login.and.register.key"));
        return systemDao.login(cellphone, md5Password);
    }

    /**
     * 手机号重复性校验
     *
     * @param cellphone 手机号
     */
    public boolean cellphoneUniqueCheck(String cellphone) {
        return systemDao.cellphoneUniqueCheck(cellphone);
    }

    /**
     * 根据角色编号查询权限菜单信息
     *
     * @param roleCode 角色编号
     */
    public List<Menu> queryAuthorityRoleList(String roleCode) {
        return systemDao.queryAuthorityRoleList(roleCode);
    }

    /**
     * 根据角色（用户）编号删除配置信息
     *
     * @param roleCode 角色（用户）编号
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void deleteAuthorityRole(String roleCode) {
        systemDao.deleteAuthorityRole(roleCode);
    }

    /**
     * 新增角色权限
     *
     * @param security 角色权限
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveAuthorityRole(Authority security) {
        systemDao.saveAuthorityRole(security);
    }

    /**
     * 是否有操作权限
     *
     * @param roleUserCode 角色权限编号
     * @param menuPath     菜单路径
     */
    public boolean isAuthority(String roleUserCode, String menuPath) {
        return systemDao.isAuthority(roleUserCode, menuPath);
    }

    /**
     * 查询指定的系统配置项
     *
     * @param systemCode 系统配置项编码
     */
    public SystemConfig querySystemConfig(String systemCode) {
        return systemDao.querySystemConfig(systemCode);
    }


    /**
     * 查询系统配置项列表
     */
    public List<SystemConfig> querySystemConfigList() {
        return systemDao.querySystemConfigList();
    }

    /**
     * 更新系统配置项
     *
     * @param systemConfig 系统配置项
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateSystemConfig(SystemConfig systemConfig) {
        systemDao.updateSystemConfig(systemConfig);
    }

}
