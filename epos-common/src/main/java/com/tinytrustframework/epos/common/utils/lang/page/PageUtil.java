package com.tinytrustframework.epos.common.utils.lang.page;

import org.hibernate.Query;
import org.hibernate.Session;

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

    /**
     * 默认构造函数
     */
    private PageUtil() {
    }

    /**
     * 分页查询
     *
     * @param session        Hibernate Session
     * @param hql            HQL
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询条件
     */
    public static Map<String, Object> hqlQuery(Session session, String hql, Map<String, Object> businessParams, Page pageParams) {

        Map<String, Object> resultMap = new HashMap();

        Query bizQuery = session.createQuery(hql);//业务查询
        if (hql.indexOf("order") != -1) {
            hql = hql.substring(0, hql.lastIndexOf("order"));
        }
        Query countQuery = session.createQuery(" select count(*) " + hql.substring(hql.indexOf("from")));//总记录数查询

        // 绑定参数
        if (null != businessParams && !businessParams.isEmpty()) {
            Iterator<Entry<String, Object>> entrys = businessParams.entrySet().iterator();
            Entry<String, Object> entry = null;
            while (entrys.hasNext()) {
                entry = entrys.next();
                bizQuery.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }

        //查询总记录数
        Long count = (Long) countQuery.uniqueResult();
        resultMap.put("count", count);

        //查询业务列表
        bizQuery.setFirstResult(pageParams.getPageStart() * pageParams.getPageSize());//设置检索的第一行
        bizQuery.setMaxResults(pageParams.getPageSize());//设置检索记录数最大行数
        List<?> list = bizQuery.list();
        resultMap.put("list", list);


        return resultMap;
    }


}
