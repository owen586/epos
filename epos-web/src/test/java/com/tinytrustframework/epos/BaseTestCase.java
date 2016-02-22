package com.tinytrustframework.epos;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author owen
 * @version 2.0, 2016-01-11 10:45
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BaseTestCase {

    // logger
    protected Logger logger = LoggerFactory.getLogger(BaseTestCase.class);

    @Before
    public void before() {
        logger.info("单元测试开始!  before");
    }

    @After
    public void after() {
        logger.info("单元测试结束!  after");
    }
}
