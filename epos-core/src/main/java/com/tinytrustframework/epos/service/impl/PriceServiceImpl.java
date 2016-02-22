package com.tinytrustframework.epos.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinytrustframework.epos.dao.PriceDao;
import com.tinytrustframework.epos.entity.PriceRole;
import com.tinytrustframework.epos.entity.PriceUser;
import com.tinytrustframework.epos.service.PriceService;

import javax.annotation.Resource;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-8-3]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class PriceServiceImpl implements PriceService {
    /**
     * 注释内容
     */
    @Resource
    private PriceDao priceDao;

    public Map<String, Object> queryPriceRoleList(Map<String, Object> params, int pageNo, int pageSize) {
        return priceDao.queryPriceRoleList(params, pageNo, pageSize);
    }


    public PriceRole getPriceRoleDetail(int roleCode) {
        return priceDao.getPriceRoleDetail(roleCode);
    }


    public void saveOrUpdatePriceRole(PriceRole priceRole) {
        priceDao.saveOrUpdatePriceRole(priceRole);
    }


    public PriceUser getPriceUserDetail(String userCode) {
        return priceDao.getPriceUserDetail(userCode);
    }


    public Map<String, Object> queryPriceUserList(Map<String, Object> params, int pageNo, int pageSize) {
        return priceDao.queryPriceUserList(params, pageNo, pageSize);
    }


    public void saveOrUpdatePriceUser(PriceUser priceUser) {
        priceDao.saveOrUpdatePriceUser(priceUser);
    }
}
