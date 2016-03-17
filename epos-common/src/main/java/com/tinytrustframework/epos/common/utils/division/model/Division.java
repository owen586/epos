package com.tinytrustframework.epos.common.utils.division.model;

import lombok.Builder;
import lombok.Data;

/**
 * 一句话功能简述
 *
 * @author owen
 * @version 2016-03-16 16:27
 */
@Data
@Builder
public class Division {
    // 区域编号
    private String areaId;

    // 区域名称
    private String areaName;

    // 区域名称拼音
    private String areaPinyin;

    // 上级区域编号
    private String parentAreaId;

}
