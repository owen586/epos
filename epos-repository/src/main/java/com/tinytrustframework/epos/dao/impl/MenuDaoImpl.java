package com.tinytrustframework.epos.dao.impl;

import java.util.List;

import com.tinytrustframework.epos.dao.MenuDao;
import com.tinytrustframework.epos.entity.Menu;
import org.springframework.stereotype.Repository;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public class MenuDaoImpl extends BaseDao implements MenuDao {
    

    public List<Menu> queryMenuList() {
        return this.getHibernateTemplate().findByNamedQuery("hql.menu.query_menu_list");
    }

    /**
     * {@inheritDoc}
     */
    public List<Menu> queryByMenuLevel(String roleUserCode, int menuLevel) {
        List<Menu> menuList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.menu.query_by_menu_level",
                        new String[]{"roleUserCode", "menuLevel"},
                        new Object[]{roleUserCode, menuLevel});
        return menuList;
    }

    /**
     * {@inheritDoc}
     */
    public List<Menu> querySubmenu(String roleUserCode, int topMenuCode) {
        List<Menu> menuList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.menu.query_submenu",
                        new String[]{"roleUserCode", "topMenuCode"},
                        new Object[]{roleUserCode, topMenuCode});
        return menuList;
    }

}
