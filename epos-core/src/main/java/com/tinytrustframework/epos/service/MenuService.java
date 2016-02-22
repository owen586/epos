package com.tinytrustframework.epos.service;

import java.util.List;

import com.tinytrustframework.epos.entity.Menu;

public interface MenuService {
    /**
     * <查询菜单列表>
     *
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    List<Menu> queryMenuList();

    /**
     * <根据菜单类型查找菜单列表>
     *
     *
     * @param roleUserCode 角色（用户）编号
     * @param menuLevel    菜单类型  1:一级菜单  2:二级菜单
     * @return
     * @see [类、类#方法、类#成员]
     */
    List<Menu> queryByMenuLevel(String roleUserCode, int menuLevel);

    /**
     * <根据父菜单编号查询子菜单列表>
     *
     *
     * @param roleUserCode 角色（用户）编号
     * @param topMenuCode  父菜单编号
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<Menu> querySubmenu(String roleUserCode, int topMenuCode);
}
