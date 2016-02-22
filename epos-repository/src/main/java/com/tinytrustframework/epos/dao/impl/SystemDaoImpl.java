package com.tinytrustframework.epos.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.tinytrustframework.epos.entity.Authority;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.entity.SystemConfig;
import com.tinytrustframework.epos.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tinytrustframework.epos.dao.SystemDao;

/**
 * <一句话功能简述>
 *
 *
 * @author Owen
 * @version [版本号, 2013-5-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public class SystemDaoImpl extends BaseDao implements SystemDao {

    public User login(String cellphone, String password) {
        List<User> userList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.system.login",
                        new String[]{"cellphone", "password"},
                        new Object[]{cellphone, password});
        if (userList.isEmpty()) {
            return null;
        }

        return userList.get(0);
    }

    
    public boolean cellphoneUniqueCheck(String cellphone) {
        List<User> userList =
                (List<User>) this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.system.cellphone_unique_check",
                        new String[]{"cellphone"},
                        new Object[]{cellphone});

        if (null != userList && !userList.isEmpty()) {
            return false;
        }
        return true;
    }


    public List<Menu> queryAuthorityRoleList(String roleCode) {
        @SuppressWarnings("unchecked")
        List<Menu> menuList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.authority.query_menu_by_rolecode",
                        new String[]{"roleUserCode"},
                        new Object[]{roleCode});
        return menuList;
    }


    public void deleteAuthorityRole(final String roleCode) {
        this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql = "delete from Authority s where s.roleUserCode = :roleUserCode";
                session.createQuery(hql).setString("roleUserCode", roleCode).executeUpdate();
                return null;
            }
        });
    }


    public void saveAuthorityRole(Authority security) {
        this.getHibernateTemplate().save(security);
    }


    public boolean isAuthority(String roleUserCode, String menuPath) {
        @SuppressWarnings("unchecked")
        List<Menu> menuList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.authority.is_authority",
                        new String[]{"roleUserCode", "menuUrl"},
                        new Object[]{roleUserCode, menuPath});
        if (null != menuList && !menuList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <查询系统配置项列表>
     *
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public List<SystemConfig> querySystemConfigList() {
        String hql = "from SystemConfig";
        return this.getHibernateTemplate().find(hql);
    }

    /**
     * <查询指定的系统配置项>
     *
     *
     * @param systemCode 系统配置项编码
     * @return
     * @see [类、类#方法、类#成员]
     */
    public SystemConfig querySystemConfig(String systemCode) {
        return this.getHibernateTemplate().get(SystemConfig.class, systemCode);
    }

    /**
     * <更新系统配置项>
     *
     *
     * @param systemConfig 系统配置项
     * @see [类、类#方法、类#成员]
     */
    public void updateSystemConfig(SystemConfig systemConfig) {
        this.getHibernateTemplate().update(systemConfig);
    }

}
