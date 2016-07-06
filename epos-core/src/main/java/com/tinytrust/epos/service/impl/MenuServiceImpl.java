package com.tinytrust.epos.service.impl;

import com.tinytrust.epos.dao.MenuDao;
import com.tinytrust.epos.entity.Menu;
import com.tinytrust.epos.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDao menuDao;

    /**
     * 查询菜单列表
     */
    public List<Menu> queryMenuList() {
        return menuDao.queryMenuList();
    }

    /**
     * 根据菜单类型查找菜单列表
     *
     * @param roleUserCode 角色（用户）编号
     * @param menuLevel    菜单类型  1:一级菜单  2:二级菜单
     */
    public List<Menu> queryByMenuLevel(String roleUserCode, int menuLevel) {
        return menuDao.queryByMenuLevel(roleUserCode, menuLevel);
    }

    /**
     * 根据父菜单编号查询子菜单列表
     *
     * @param roleUserCode 角色（用户）编号
     * @param topMenuCode  父菜单编号
     */
    public List<Menu> querySubmenu(String roleUserCode, int topMenuCode) {
        return menuDao.querySubmenu(roleUserCode, topMenuCode);
    }

}
