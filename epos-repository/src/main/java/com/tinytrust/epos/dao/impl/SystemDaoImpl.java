package com.tinytrust.epos.dao.impl;

import com.tinytrust.epos.dao.BaseDao;
import com.tinytrust.epos.dao.SystemDao;
import com.tinytrust.epos.entity.Authority;
import com.tinytrust.epos.entity.Menu;
import com.tinytrust.epos.entity.SystemConfig;
import com.tinytrust.epos.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Owen
 * @version [版本号, 2013-5-19]
 */
@Repository
public class SystemDaoImpl extends BaseDao implements SystemDao {

    /**
     * 用户登录
     *
     * @param cellphone 手机号
     * @param password  密码
     */
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

    /**
     * 手机号重复性校验
     *
     * @param cellphone 手机号
     */
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



    /**
     * 查询用户信息
     *
     * @param cellphone  根据手机号
     */
    public User getUserByCellphone(String cellphone) {
        String hql = "from User u where u.cellphone = :cellphone";
        List<User> userList =
                (List<User>) this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"cellphone"},
                        new Object[]{cellphone});

        if (null != userList && !userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }


    /**
     * 根据角色编号查询权限菜单信息
     *
     * @param roleCode 角色编号
     */
    public List<Menu> queryAuthorityRoleList(String roleCode) {
        String hql = "select new Menu(m.menuCode,m.menuName,m.menuUrl) from Menu m,Authority a where m.menuCode = a.menuCode and a.roleUserCode = :roleUserCode";
        List<Menu> menuList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"roleUserCode"},
                        new Object[]{roleCode});
        return menuList;
    }

    /**
     * 根据角色（用户）编号删除配置信息
     *
     * @param roleCode 角色（用户）编号
     */
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

    /**
     * 新增角色权限
     *
     * @param security 角色权限
     */
    public void saveAuthorityRole(Authority security) {
        this.hibernateTemplate.save(security);
    }

    /**
     * 是否有操作权限
     *
     * @param roleUserCode 角色权限编号
     * @param menuPath     菜单路径
     */
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

    /**
     * 查询系统配置项列表
     */
    public List<SystemConfig> querySystemConfigList() {
        String hql = "from SystemConfig";
        return this.hibernateTemplate.find(hql);
    }

    /**
     * 查询指定的系统配置项
     *
     * @param systemCode 系统配置项编码
     */
    public SystemConfig querySystemConfig(String systemCode) {
        return this.hibernateTemplate.get(SystemConfig.class, systemCode);
    }

    /**
     * 更新系统配置项
     *
     * @param systemConfig 系统配置项
     */
    public void updateSystemConfig(SystemConfig systemConfig) {
        this.hibernateTemplate.update(systemConfig);
    }

}
