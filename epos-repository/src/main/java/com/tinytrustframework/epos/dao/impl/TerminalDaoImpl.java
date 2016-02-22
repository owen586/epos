package com.tinytrustframework.epos.dao.impl;

import java.sql.SQLException;
import java.util.Map;

import com.tinytrustframework.epos.dao.TerminalDao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tinytrustframework.epos.common.utils.lang.PageUtil;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public class TerminalDaoImpl extends BaseDao implements TerminalDao {

    /**
     * {@inheritDoc}
     */

    public Map<String, Object> queryTerminalList(final Map<String, Object> params, final int pageNo, final int pageSize) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql = "from Terminal t where 1=1 ";

                if (params.containsKey("terminalCode"))//终端编号
                {
                    hql += " and t.terminalCode = :terminalCode";
                }
                return PageUtil.findByHQLQueryWithMap(session, pageNo, pageSize, hql, params);
            }
        });

    }

}
