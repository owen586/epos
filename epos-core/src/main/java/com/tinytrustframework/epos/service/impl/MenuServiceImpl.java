package com.tinytrustframework.epos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinytrustframework.epos.dao.MenuDao;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.service.MenuService;

import javax.annotation.Resource;

@Service
public class MenuServiceImpl implements MenuService {
    /**
     * 注释内容
     */
    @Resource
    private MenuDao menuDao;

    /**
     * <查询菜单列表>
     *
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<Menu> queryMenuList() {
        return menuDao.queryMenuList();
    }


    public List<Menu> queryByMenuLevel(String roleUserCode, int menuLevel) {
        return menuDao.queryByMenuLevel(roleUserCode, menuLevel);
    }


    public List<Menu> querySubmenu(String roleUserCode, int topMenuCode) {
        return menuDao.querySubmenu(roleUserCode, topMenuCode);
    }

}
