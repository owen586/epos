package com.tinytrustframework.epos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinytrustframework.epos.dao.MenuDao;
import com.tinytrustframework.epos.entity.Menu;
import com.tinytrustframework.epos.service.MenuService;

import javax.annotation.Resource;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuDao menuDao;

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
