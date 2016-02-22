/*
 * 文 件 名:  SpringUtil.java
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2015-8-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.common.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring工具类
 *
 * @author owen
 * @version [版本号, 2015-8-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SpringContextHelper implements ApplicationContextAware {

    /**
     * ApplicationContext
     */
    private static ApplicationContext context;


    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        SpringContextHelper.context = context;
    }

    /**
     * <获取业务bean>
     *
     *
     * @param beanName bean名称
     * @return 业务bean
     * @see [类、类#方法、类#成员]
     */
    public static Object getObject(String beanName) {
        return context.getBean(beanName);
    }
}
