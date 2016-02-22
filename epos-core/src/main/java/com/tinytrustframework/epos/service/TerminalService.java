package com.tinytrustframework.epos.service;

import java.util.Map;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface TerminalService {
    /**
     * <查询终端信息列表>
     *
     *
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    Map<String, Object> queryTerminalList(Map<String, Object> params, int pageNo, int pageSize);
}
