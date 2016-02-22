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
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BaseController {

    /**
     * logger
     */
    protected Logger log = LoggerFactory.getLogger(BaseController.class);

    /**
     * 注释内容
     */
    protected String message;

    /**
     * 操作结果成功
     */
    protected final String RESULT_SUCCESS = "success";

    /**
     * 操作结果失败
     */
    protected final String RESULT_FAIL = "fail";

    /**
     * <创建成功ActionResponse>
     *
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createSuccessResponse() {
        return createResponse(RESULT_SUCCESS, null, "操作成功");
    }

    /**
     * <创建成功ActionResponse>
     *
     * @param message 信息备忘
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createSuccessResponse(String message) {
        return createResponse(RESULT_SUCCESS, null, message);
    }

    /**
     * <创建成功ActionResponse>
     *
     * @param dataMap 数据对象Map
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createSuccessResponse(Map<String, Object> dataMap) {
        return createResponse(RESULT_SUCCESS, dataMap, null);
    }

    /**
     * <创建成功ActionResponse>
     *
     * @param dataMap 数据对象Map
     * @param message 信息备忘
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createSuccessResponse(Map<String, Object> dataMap, String message) {
        return createResponse(RESULT_SUCCESS, dataMap, message);
    }

    /**
     * <创建失败ActionResponse>
     *
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createFailResponse() {
        return createResponse(RESULT_FAIL, null, "操作成功");
    }

    /**
     * <创建失败ActionResponse>
     *
     * @param message 信息备忘
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createFailResponse(String message) {
        return createResponse(RESULT_FAIL, null, message);
    }

    /**
     * <创建失败ActionResponse>
     *
     * @param dataMap 数据对象Map
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createFailResponse(Map<String, Object> dataMap) {
        return createResponse(RESULT_FAIL, dataMap, null);
    }

    /**
     * <创建失败ActionResponse>
     *
     * @param dataMap 数据对象Map
     * @param message 信息备忘
     * @return ActionResponse
     * @see [类、类#方法、类#成员]
     */
    public CommonResponse createFailResponse(Map<String, Object> dataMap, String message) {
        return createResponse(RESULT_FAIL, dataMap, message);
    }

    /**
     * 创建ActionResponse
     *
     * @param result  响应结果 success|fail
     * @param dataMap 数据对象Map
     * @param message 信息备忘
     * @return
     * @see [类、类#方法、类#成员]
     */
    private CommonResponse createResponse(String result, Map<String, Object> dataMap, String message) {
        CommonResponse actionResponse = new CommonResponse();
        actionResponse.setResult(result);
        actionResponse.setMessage(message);
        actionResponse.setDataMap(dataMap);
        return actionResponse;
    }
}
