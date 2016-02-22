package com.tinytrustframework.epos.dao;

import java.util.Map;

import com.tinytrustframework.epos.entity.PriceUser;
import com.tinytrustframework.epos.entity.PriceRole;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-8-3]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface PriceDao {
    /**
     * <查询角色价格列表>
     *
     *
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    Map<String, Object> queryPriceRoleList(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * <查询角色价格信息>
     *
     *
     * @param roleCode 角色编号
     * @return
     * @see [类、类#方法、类#成员]
     */
    PriceRole getPriceRoleDetail(int roleCode);

    /**
     * <新增或编辑角色价格信息>
     *
     *
     * @param priceRole
     * @see [类、类#方法、类#成员]
     */
    void saveOrUpdatePriceRole(PriceRole priceRole);


    /**
     * <查询用户价格列表>
     *
     *
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    Map<String, Object> queryPriceUserList(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * <查询用户价格信息>
     *
     *
     * @param roleCode 角色编号
     * @return
     * @see [类、类#方法、类#成员]
     */
    PriceUser getPriceUserDetail(String userCode);

    /**
     * <新增或编辑用户价格信息>
     *
     *
     * @param priceUser
     * @see [类、类#方法、类#成员]
     */
    void saveOrUpdatePriceUser(PriceUser priceUser);
}
