package com.tinytrustframework.epos.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class BaseDao extends HibernateDaoSupport {
    @Resource
    public void setSessionFactory_(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
