package com.tinytrustframework.epos.dao;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author owen
 * @version [版本号, 2015-7-14]
 */
@Component
public class BaseDao {

    // 注入HibernateTemplate
    @Resource
    protected HibernateTemplate hibernateTemplate;

}
