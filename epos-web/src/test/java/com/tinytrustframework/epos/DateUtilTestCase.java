package com.tinytrust.epos;

import com.tinytrust.epos.common.utils.lang.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author owen
 * @version 2.0, 2016-01-11 10:34
 */
public class DateUtilTestCase extends BaseTestCase {

    @Test
    public void testParse() {
        // null
        String date1 = null;
        String format = "yyyy-MM-dd HH:mm:ss";
        Assert.assertEquals(null, DateUtil.parse(date1, format));
        logger.trace("Hi,My name is {}", "gekunjin");
        logger.debug("Hi,My name is {}", "gekunjin");
        logger.info("Hi,My name is {}", "gekunjin");
        logger.warn("Hi,My name is {}", "gekunjin");

        logger.error("Hi,My name is {}", "gekunjin");   // exception
        String date2 = "";
        Assert.assertNull(null, DateUtil.parse(date2, format));

        // correct
        String date3 = "2016-01-11 10:59:10";
        Assert.assertNotSame(new Date(System.currentTimeMillis()), DateUtil.parse(date3, format));
    }


}
