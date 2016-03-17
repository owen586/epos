package com.tinytrustframework.epos.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinytrustframework.epos.dao.PriceDao;
import com.tinytrustframework.epos.entity.PriceRole;
import com.tinytrustframework.epos.entity.PriceUser;
import com.tinytrustframework.epos.service.PriceService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author owen
 * @version [版本号, 2015-8-3]
*/
@Service
public class PriceServiceImpl implements PriceService {
    @Resource
    private PriceDao priceDao;

    public Map<String, Object> queryPriceRoleList(Map<String, Object> params, int pageNo, int pageSize) {
        return priceDao.queryPriceRoleList(params, pageNo, pageSize);
    }

    public PriceRole getPriceRoleDetail(int roleCode) {
        return priceDao.getPriceRoleDetail(roleCode);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveOrUpdatePriceRole(PriceRole priceRole) {
        priceDao.saveOrUpdatePriceRole(priceRole);
    }

    public PriceUser getPriceUserDetail(String userCode) {
        return priceDao.getPriceUserDetail(userCode);
    }

    public Map<String, Object> queryPriceUserList(Map<String, Object> params, int pageNo, int pageSize) {
        return priceDao.queryPriceUserList(params, pageNo, pageSize);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void saveOrUpdatePriceUser(PriceUser priceUser) {
        priceDao.saveOrUpdatePriceUser(priceUser);
    }
}
