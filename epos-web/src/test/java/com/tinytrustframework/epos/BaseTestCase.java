package com.tinytrust.epos;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;


/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author owen
 * @version 2.0, 2016-01-11 10:45
 */
@Slf4j
public class BaseTestCase {

    @Before
    public void before() {
        log.info("单元测试开始!  before");
    }

    @After
    public void after() {
        log.info("单元测试结束!  after");
    }
}
