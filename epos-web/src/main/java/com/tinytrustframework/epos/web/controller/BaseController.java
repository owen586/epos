package com.tinytrustframework.epos.web.controller;

import com.tinytrustframework.epos.web.controller.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 控制处理类基类
 *
 * @author Owen
 * @version [版本号, 2010-11-5]
 */
public class BaseController {

    // logger
    protected Logger log = LoggerFactory.getLogger(BaseController.class);

    // 操作结果成功
    protected final String RESULT_SUCCESS = "success";

    // 操作结果失败
    protected final String RESULT_FAIL = "fail";
}
