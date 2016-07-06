package com.tinytrust.epos.service.impl;

import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.dao.PriceDao;
import com.tinytrust.epos.entity.PriceRole;
import com.tinytrust.epos.entity.PriceUser;
import com.tinytrust.epos.service.PriceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-8-3]
 */
@Service
public class PriceServiceImpl implements PriceService {

    @Resource
    private PriceDao priceDao;

    /**
     * 查询角色价格列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryPriceRoleList(Map<String, Object> businessParams, Page pageParams) {
        return priceDao.queryPriceRoleList(businessParams, pageParams);
    }

    /**
     * 查询角色价格信息
     *
     * @param roleCode 角色编号
     */
    public PriceRole getPriceRoleDetail(int roleCode) {
        return priceDao.getPriceRoleDetail(roleCode);
    }

    /**
     * 新增或编辑角色价格信息
     *
     * @param priceRole 角色价格信息
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveOrUpdatePriceRole(PriceRole priceRole) {
        priceDao.saveOrUpdatePriceRole(priceRole);
    }


    /**
     * 查询用户价格信息
     *
     * @param userCode 角色编号
     */
    public PriceUser getPriceUserDetail(String userCode) {
        return priceDao.getPriceUserDetail(userCode);
    }

    /**
     * 查询用户价格列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryPriceUserList(Map<String, Object> businessParams, Page pageParams) {
        return priceDao.queryPriceUserList(businessParams, pageParams);
    }

    /**
     * 新增或编辑用户价格信息
     *
     * @param priceUser 用户价格信息
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveOrUpdatePriceUser(PriceUser priceUser) {
        priceDao.saveOrUpdatePriceUser(priceUser);
    }
}
