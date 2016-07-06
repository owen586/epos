package com.tinytrust.epos.dao;

import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.entity.PriceRole;
import com.tinytrust.epos.entity.PriceUser;

import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-8-3]
 */
public interface PriceDao {

    /**
     * 查询角色价格列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    Map<String, Object> queryPriceRoleList(Map<String, Object> businessParams, Page pageParams);

    /**
     * 查询角色价格信息
     *
     * @param roleCode 角色编号
     */
    PriceRole getPriceRoleDetail(int roleCode);

    /**
     * 新增或编辑角色价格信息
     *
     * @param priceRole
     */
    void saveOrUpdatePriceRole(PriceRole priceRole);


    /**
     * 查询用户价格列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    Map<String, Object> queryPriceUserList(Map<String, Object> businessParams, Page pageParams);

    /**
     * 查询用户价格信息
     *
     * @param userCode 角色编号
     */
    PriceUser getPriceUserDetail(String userCode);

    /**
     * 新增或编辑用户价格信息
     *
     * @param priceUser 用户价格信息
     */
    void saveOrUpdatePriceUser(PriceUser priceUser);
}
