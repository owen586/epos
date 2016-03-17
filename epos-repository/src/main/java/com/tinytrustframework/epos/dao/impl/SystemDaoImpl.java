package com.tinytrustframework.epos.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.tinytrustframework.epos.dao.BaseDao;
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
 * @author Owen
 * @version [版本号, 2013-5-19]
*/
@Repository
public class SystemDaoImpl extends BaseDao implements SystemDao {

    public User login(String cellphone, String password) {
        String hql = "from User u where u.cellphone =:cellphone and u.password =:password";
        List<User> userList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"cellphone", "password"},
                        new Object[]{cellphone, password});
        if (userList.isEmpty()) {
            return null;
        }
        return userList.get(0);
    }


    public boolean cellphoneUniqueCheck(String cellphone) {
        String hql = "from User u where u.cellphone = :cellphone";
        List<User> userList =
                (List<User>) this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"cellphone"},
                        new Object[]{cellphone});

        if (null != userList && !userList.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<Menu> queryAuthorityRoleList(String roleCode) {
        String hql = "select new Menu(m.menuCode,m.menuName,m.menuUrl) from Menu m,Authority a where m.menuCode = a.menuCode and a.roleUserCode = :roleUserCode";
        List<Menu> menuList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"roleUserCode"},
                        new Object[]{roleCode});
        return menuList;
    }

    public void deleteAuthorityRole(final String roleCode) {
        this.hibernateTemplate.execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql = "delete from Authority s where s.roleUserCode = :roleUserCode";
                session.createQuery(hql).setString("roleUserCode", roleCode).executeUpdate();
                return null;
            }
        });
    }

    public void saveAuthorityRole(Authority security) {
        this.hibernateTemplate.save(security);
    }

    public boolean isAuthority(String roleUserCode, String menuPath) {
        String hql = "select new Menu(m.menuCode,m.menuName,m.menuUrl) from Menu m,Authority a where m.menuCode = a.menuCode and a.roleUserCode = :roleUserCode and a.menuUrl = :menuUrl";
        List<Menu> menuList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"roleUserCode", "menuUrl"},
                        new Object[]{roleUserCode, menuPath});
        if (null != menuList && !menuList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public List<SystemConfig> querySystemConfigList() {
        String hql = "from SystemConfig";
        return this.hibernateTemplate.find(hql);
    }

    public SystemConfig querySystemConfig(String systemCode) {
        return this.hibernateTemplate.get(SystemConfig.class, systemCode);
    }

    public void updateSystemConfig(SystemConfig systemConfig) {
        this.hibernateTemplate.update(systemConfig);
    }

}
