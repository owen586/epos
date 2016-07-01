package com.tinytrustframework.epos.common.utils.lang.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring工具类
 *
 * @author owen
 * @version [版本号, 2015-8-25]
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
     * @param beanName bean名称
     *                 业务bean
     */
    public static Object getObject(String beanName) {
        return context.getBean(beanName);
    }
}
