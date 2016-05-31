package com.tinytrustframework.epos.dao.impl;

import java.util.List;

import com.tinytrustframework.epos.dao.BaseDao;
import com.tinytrustframework.epos.dao.MenuDao;
import com.tinytrustframework.epos.entity.Menu;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author owen
 * @version [版本号, 2015-7-26]
 */
@Repository
public class MenuDaoImpl extends BaseDao implements MenuDao {

    public List<Menu> queryMenuList() {
        String hql = "from Menu";
        return this.hibernateTemplate.find(hql);
    }

    public List<Menu> queryByMenuLevel(String roleUserCode, int menuLevel) {
        String hql = "select new Menu(m.menuCode,m.menuName,m.menuUrl) from Menu m,Authority a where m.menuCode = a.menuCode and a.roleUserCode = :roleUserCode and m.menuLevel = :menuLevel order by m.menuOrder asc";

        List<Menu> menuList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"roleUserCode", "menuLevel"},
                        new Object[]{roleUserCode, menuLevel});
        return menuList;
    }

    public List<Menu> querySubmenu(String roleUserCode, int topMenuCode) {
        String hql = "select new Menu(m.menuCode,m.menuName,m.menuUrl) from Menu m,Authority a where  m.menuCode = a.menuCode  and a.roleUserCode = :roleUserCode and m.topMenuCode = :topMenuCode order by m.menuOrder asc";
        List<Menu> menuList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"roleUserCode", "topMenuCode"},
                        new Object[]{roleUserCode, topMenuCode});
        return menuList;
    }

}
