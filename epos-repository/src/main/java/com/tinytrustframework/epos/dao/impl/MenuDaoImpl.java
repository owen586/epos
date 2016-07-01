package com.tinytrustframework.epos.dao.impl;

import com.tinytrustframework.epos.dao.BaseDao;
import com.tinytrustframework.epos.dao.MenuDao;
import com.tinytrustframework.epos.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author owen
 * @version [版本号, 2015-7-26]
 */
@Repository
public class MenuDaoImpl extends BaseDao implements MenuDao {

    /**
     * 查询菜单列表
     */
    public List<Menu> queryMenuList() {
        String hql = "from Menu";
        return this.hibernateTemplate.find(hql);
    }

    /**
     * 根据菜单类型查找菜单列表
     *
     * @param roleUserCode 角色（用户）编号
     * @param menuLevel    菜单类型  1:一级菜单  2:二级菜单
     */
    public List<Menu> queryByMenuLevel(String roleUserCode, int menuLevel) {
        String hql = "select new Menu(m.menuCode,m.menuName,m.menuUrl) from Menu m,Authority a where m.menuCode = a.menuCode and a.roleUserCode = :roleUserCode and m.menuLevel = :menuLevel order by m.menuOrder asc";

        List<Menu> menuList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"roleUserCode", "menuLevel"},
                        new Object[]{roleUserCode, menuLevel});
        return menuList;
    }

    /**
     * 根据父菜单编号查询子菜单列表
     *
     * @param roleUserCode 角色（用户）编号
     * @param topMenuCode  父菜单编号
     */
    public List<Menu> querySubmenu(String roleUserCode, int topMenuCode) {
        String hql = "select new Menu(m.menuCode,m.menuName,m.menuUrl) from Menu m,Authority a where  m.menuCode = a.menuCode  and a.roleUserCode = :roleUserCode and m.topMenuCode = :topMenuCode order by m.menuOrder asc";
        List<Menu> menuList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"roleUserCode", "topMenuCode"},
                        new Object[]{roleUserCode, topMenuCode});
        return menuList;
    }

}
