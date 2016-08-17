package com.tinytrust.epos.dao.impl;

import com.tinytrust.epos.common.utils.lang.page.Page;
import com.tinytrust.epos.common.utils.lang.page.PageUtil;
import com.tinytrust.epos.dao.BaseDao;
import com.tinytrust.epos.dao.PriceDao;
import com.tinytrust.epos.entity.PriceRole;
import com.tinytrust.epos.entity.PriceUser;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-8-3]
 */
@Repository
public class PriceDaoImpl extends BaseDao implements PriceDao {


    /**
     * 查询角色价格列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryPriceRoleList(final Map<String, Object> businessParams, final Page pageParams) {

        return this.hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql =
                        "select new PriceRole(r.roleCode,r.roleName,t.feeRate,COALESCE(t.topUserFeeRateReturn,0)) "
                                + "from Role r,PriceRole t where r.roleCode = t.roleCode ";
                if (businessParams.containsKey("roleCode"))//角色编号
                {
                    hql += " and r.roleCode = :roleCode ";
                }

                hql += " order by r.roleCode asc";
                return PageUtil.hqlQuery(session, hql, businessParams, pageParams);
            }
        });
    }

    /**
     * 查询角色价格信息
     *
     * @param roleCode 角色编号
     */
    public PriceRole getPriceRoleDetail(int roleCode) {
        String hql = "from PriceRole p where p.roleCode = :roleCode";
        List<PriceRole> priceRoleList =
                this.hibernateTemplate.findByNamedParam(hql,
                        "roleCode",
                        roleCode);
        if (null != priceRoleList && !priceRoleList.isEmpty()) {
            return priceRoleList.get(0);
        }
        return null;
    }

    /**
     * 新增或编辑角色价格信息
     *
     * @param priceRole 角色价格信息
     */
    public void saveOrUpdatePriceRole(PriceRole priceRole) {
        this.hibernateTemplate.saveOrUpdate(priceRole);
    }

    /**
     * 新增或编辑用户价格信息
     *
     * @param priceUser 用户价格信息
     */
    public void saveOrUpdatePriceUser(PriceUser priceUser) {
        this.hibernateTemplate.saveOrUpdate(priceUser);
    }

    /**
     * 查询用户价格信息
     *
     * @param userCode 角色编号
     */
    public PriceUser getPriceUserDetail(String userCode) {
        String hql = "from PriceUser p where p.userCode = :userCode";
        List<PriceUser> priceUserList =
                this.hibernateTemplate.findByNamedParam(hql,
                        "userCode",
                        userCode);
        if (null != priceUserList && !priceUserList.isEmpty()) {
            return priceUserList.get(0);
        }
        return null;
    }

    /**
     * 查询用户价格列表
     *
     * @param businessParams 查询条件
     * @param pageParams     分页参数，当前页
     */
    public Map<String, Object> queryPriceUserList(final Map<String, Object> businessParams, final Page pageParams) {
        return this.hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql =
                        "select new PriceUser(u.userCode,p.feeRate,p.topUserFeeRateReturn,u.userName,u.cellphone) "
                                + "from User u,PriceUser p where u.userCode = p.userCode ";

                if (businessParams.containsKey("userCode"))//用户编号
                {
                    hql += " and u.userCode = :userCode ";
                }

                if (businessParams.containsKey("userName"))//用户名称
                {
                    hql += " and u.userName like :userName ";
                }

                if (businessParams.containsKey("cellphone"))//手机号
                {
                    hql += " and u.cellphone = :cellphone ";
                }

                hql += " order by u.userCode desc";
                return PageUtil.hqlQuery(session, hql, businessParams, pageParams);
            }
        });
    }
}
