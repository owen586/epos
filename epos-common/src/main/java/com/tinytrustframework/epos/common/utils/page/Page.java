package com.tinytrustframework.epos.common.utils.page;

import lombok.Builder;
import lombok.Data;

/**
 * @author owen
 * @date 2016-07-01 01:14:53
 */
@Data
@Builder
public class Page {

    // 分页参数,当前页
    private int pageStart;

    // 分页参数,每页记录数
    private int pageSize;
}
