package com.tinytrustframework.epos.service.impl;

import java.util.List;

import com.tinytrustframework.epos.common.utils.props.PropUtils;
import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.SystemService;
import org.springframework.stereotype.Service;

import com.tinytrustframework.epos.common.utils.lang.DigestUtil;
import com.tinytrustframework.epos.dao.SystemDao;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author owen
 * @version [版本号, 2013-5-19]
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Resource
    private SystemDao systemDao;

    public User login(String cellphone, String password) {
        String md5Password = DigestUtil.md5(cellphone + password + PropUtils.getPropertyValue("login.and.register.key"));
        return systemDao.login(cellphone, md5Password);
    }

    public boolean cellphoneUniqueCheck(String cellphone) {
        return systemDao.cellphoneUniqueCheck(cellphone);
    }

    public List<Menu> queryAuthorityRoleList(String roleCode) {
        return systemDao.queryAuthorityRoleList(roleCode);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void deleteAuthorityRole(String roleCode) {
        systemDao.deleteAuthorityRole(roleCode);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveAuthorityRole(Authority security) {
        systemDao.saveAuthorityRole(security);
    }

    public boolean isAuthority(String roleUserCode, String menuPath) {
        return systemDao.isAuthority(roleUserCode, menuPath);
    }

    public SystemConfig querySystemConfig(String systemCode) {
        return systemDao.querySystemConfig(systemCode);
    }

    public List<SystemConfig> querySystemConfigList() {
        return systemDao.querySystemConfigList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateSystemConfig(SystemConfig systemConfig) {
        systemDao.updateSystemConfig(systemConfig);
    }

}
