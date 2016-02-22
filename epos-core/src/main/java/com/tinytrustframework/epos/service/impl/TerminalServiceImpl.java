package com.tinytrustframework.epos.service.impl;

import java.util.Map;

import com.tinytrustframework.epos.dao.TerminalDao;
import com.tinytrustframework.epos.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class TerminalServiceImpl implements TerminalService {
    /**
     * 注释内容
     */
    @Resource
    private TerminalDao terminalDao;

    /**
     * {@inheritDoc}
     */

    public Map<String, Object> queryTerminalList(Map<String, Object> params, int pageNo, int pageSize) {
        return terminalDao.queryTerminalList(params, pageNo, pageSize);
    }

}
