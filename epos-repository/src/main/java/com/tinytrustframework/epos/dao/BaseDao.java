package com.tinytrustframework.epos.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

/**
 * <一句话功能简述>
 *
 * @author owen
 * @version [版本号, 2015-7-14]
*/
@Component
public class BaseDao {

    // 注入HibernateTemplate
    @Resource
    protected HibernateTemplate hibernateTemplate;
}
