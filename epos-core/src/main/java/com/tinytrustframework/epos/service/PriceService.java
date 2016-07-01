package com.tinytrustframework.epos.service;

import java.util.Map;

import com.tinytrustframework.epos.common.utils.page.Page;
import com.tinytrustframework.epos.entity.PriceUser;
import com.tinytrustframework.epos.entity.PriceRole;

/**
 * @author owen
 * @version [版本号, 2015-8-3]
 */
public interface PriceService {

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
     * @param priceRole 角色价格信息
     */
    void saveOrUpdatePriceRole(PriceRole priceRole);

    /**
     * 查询用户价格信息
     *
     * @param userCode 角色编号
     */
    PriceUser getPriceUserDetail(String userCode);


    /**
     * 查询用户价格列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    Map<String, Object> queryPriceUserList(Map<String, Object> businessParams, Page pageParams);



    /**
     * 新增或编辑用户价格信息
     *
     * @param priceUser 用户价格信息
     */
    void saveOrUpdatePriceUser(PriceUser priceUser);
}
