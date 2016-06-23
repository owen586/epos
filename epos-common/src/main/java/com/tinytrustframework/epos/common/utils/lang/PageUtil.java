package com.tinytrustframework.epos.common.utils.lang;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 分页工具类
 *
 * @author Owen
 * @version [版本号, 2010-11-5]
 */
public class PageUtil {

    // 注释内容
    private final static Logger log = LoggerFactory.getLogger(PageUtil.class);

    /**
     * <默认构造函数>
     */
    private PageUtil() {
    }

    /**
     * <分页查询>
     *
     * @param session
     * @param start
     * @param limit
     * @param hql
     * @param parameters map
     */
    public static Map<String, Object> findByHQLQueryWithMap(Session session, int start, int limit, String hql,
                                                            Map<String, Object> parameters) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Query bizQuery = session.createQuery(hql);//业务Query
        if (hql.indexOf("order") != -1) {
            hql = hql.substring(0, hql.lastIndexOf("order"));
        }
        Query countQuery = session.createQuery(" select count(*) " + hql.substring(hql.indexOf("from")));//执行总记录数统计Query
//        String countSql = "select count(1) " + sql.substring(sql.indexOf("from"));
        if (null != parameters && !parameters.isEmpty())//绑定参数
        {
            Iterator<Entry<String, Object>> entrys = parameters.entrySet().iterator();
            Entry<String, Object> entry = null;
            while (entrys.hasNext()) {
                entry = entrys.next();
                bizQuery.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }

        //总记录数
        Long count = (Long) countQuery.uniqueResult();
        resultMap.put("count", count);

        //数据列表
        bizQuery.setFirstResult(start * limit);//设置检索的第一行
        bizQuery.setMaxResults(limit);//设置检索记录数最大行数
        List<?> list = bizQuery.list();
        resultMap.put("list", list);
        return resultMap;
    }

    /**
     * <分页查询>
     *
     * @param session
     * @param start
     * @param limit
     * @param hql
     * @param parameters list
     */
    public static List<?> findByHQLQueryWithList(Session session, int start, int limit, String hql,
                                                 Map<String, Object> parameters) {
        Query bizQuery = session.createQuery(hql);//业务Query
        if (null != parameters && !parameters.isEmpty())//绑定参数
        {
            Iterator<Entry<String, Object>> entrys = parameters.entrySet().iterator();
            Entry<String, Object> entry = null;
            while (entrys.hasNext()) {
                entry = entrys.next();
                bizQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        //数据列表
        bizQuery.setFirstResult(start * limit);//设置检索的第一行
        bizQuery.setMaxResults(limit);//设置检索记录数最大行数
        List<?> resultList = bizQuery.list();
        return resultList;
    }


}
