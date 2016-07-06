package com.tinytrust.epos.id;

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
 * @author owen
 * @version [版本号, 2015-7-26]
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
