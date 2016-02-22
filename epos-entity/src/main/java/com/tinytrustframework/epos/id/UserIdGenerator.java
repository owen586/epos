/*
 * 文 件 名:  UserCodeKey.java
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2015-7-26
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.web.id;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

/**
 * <自定义用户编号生成器>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UserIdGenerator extends TableGenerator {
    /**
     * 指定前缀
     */
    private String prefix = "U";

    /**
     * {@inheritDoc}
     */
    public void configure(Type type, Properties params, Dialect d) {
        super.configure(new IntegerType(), params, d);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Serializable generate(SessionImplementor session, Object obj)
            throws HibernateException {
        return prefix + super.generate(session, obj);
    }

}
