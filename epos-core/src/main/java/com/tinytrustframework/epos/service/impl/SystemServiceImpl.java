/*
 * 文 件 名:  SystemServiceImpl.java
 * 版    权:  www.astcard.com. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2013-5-19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.service.impl;

import java.util.List;

import com.tinytrustframework.epos.common.utils.lang.I18nUtil;
import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.entity.User;
import com.tinytrustframework.epos.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinytrustframework.epos.common.utils.lang.DigestUtil;
import com.tinytrustframework.epos.dao.SystemDao;

import javax.annotation.Resource;

/**
 * <一句话功能简述>
 *
 *
 * @author Owen
 * @version [版本号, 2013-5-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class SystemServiceImpl implements SystemService {
    /**
     * SystemDao
     */
    @Resource
    private SystemDao systemDao;


    public User login(String cellphone, String password) {
        String md5Password = DigestUtil.md5(cellphone + password + I18nUtil.getProperty("login.and.register.key"));
        return systemDao.login(cellphone, md5Password);
    }


    public boolean cellphoneUniqueCheck(String cellphone) {
        return systemDao.cellphoneUniqueCheck(cellphone);
    }


    public List<Menu> queryAuthorityRoleList(String roleCode) {
        return systemDao.queryAuthorityRoleList(roleCode);
    }


    public void deleteAuthorityRole(String roleCode) {
        systemDao.deleteAuthorityRole(roleCode);
    }


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


    public void updateSystemConfig(SystemConfig systemConfig) {
        systemDao.updateSystemConfig(systemConfig);
    }

//
//    public void saveOrUpdate(Test test)
//    {
//        systemDao.saveOrUpdate(test);
//    }
}
