package com.tinytrustframework.epos.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.tinytrustframework.epos.dao.PriceDao;
import com.tinytrustframework.epos.entity.PriceUser;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tinytrustframework.epos.common.utils.lang.PageUtil;
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
@Repository
public class PriceDaoImpl extends BaseDao implements PriceDao {
    /**
     * {@inheritDoc}
     */

    public Map<String, Object> queryPriceRoleList(final Map<String, Object> params, final int pageNo, final int pageSize) {

        return this.getHibernateTemplate().execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql =
                        "select new PriceRole(r.roleCode,r.roleName,t.feeRate,t.topUserFeeRateReturn) "
                                + "from Role r,PriceRole t where r.roleCode = t.roleCode ";

                if (params.containsKey("roleCode"))//角色编号
                {
                    hql += " and r.roleCode = :roleCode ";
                }

                hql += " order by r.roleCode asc";
                return PageUtil.findByHQLQueryWithMap(session, pageNo, pageSize, hql, params);
            }
        });

    }


    public PriceRole getPriceRoleDetail(int roleCode) {
        List<PriceRole> priceRoleList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.price.get_price_role_detail",
                        "roleCode",
                        roleCode);
        if (null != priceRoleList && !priceRoleList.isEmpty()) {
            return priceRoleList.get(0);
        }
        return null;
    }


    public void saveOrUpdatePriceRole(PriceRole priceRole) {
        this.getHibernateTemplate().saveOrUpdate(priceRole);
    }


    public void saveOrUpdatePriceUser(PriceUser priceUser) {
        this.getHibernateTemplate().saveOrUpdate(priceUser);
    }


    public PriceUser getPriceUserDetail(String userCode) {
        @SuppressWarnings("unchecked")
        List<PriceUser> priceUserList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.price.get_price_user_detail",
                        "userCode",
                        userCode);
        if (null != priceUserList && !priceUserList.isEmpty()) {
            return priceUserList.get(0);
        }
        return null;
    }


    public Map<String, Object> queryPriceUserList(final Map<String, Object> params, final int pageNo, final int pageSize) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql =
                        "select new PriceUser(u.userCode,p.feeRate,p.topUserFeeRateReturn,u.userName,u.cellphone) "
                                + "from User u,PriceUser p where u.userCode = p.userCode ";

                if (params.containsKey("userCode"))//用户编号
                {
                    hql += " and u.userCode = :userCode ";
                }

                if (params.containsKey("userName"))//用户名称
                {
                    hql += " and u.userName like :userName ";
                }

                if (params.containsKey("cellphone"))//手机号
                {
                    hql += " and u.cellphone = :cellphone ";
                }

                hql += " order by u.userCode desc";
                return PageUtil.findByHQLQueryWithMap(session, pageNo, pageSize, hql, params);
            }
        });
    }
}
