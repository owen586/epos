package com.tinytrustframework.epos.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统配置类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_system_config")
public class SystemConfig implements java.io.Serializable {

    private static final long serialVersionUID = -902876107097720164L;

    // 系统配置编号
    @Id
    @Column(name = "sys_code", unique = true, nullable = false, length = 32)
    private String sysCode;

    // 系统配置名称
    @Column(name = "sys_name", nullable = false, length = 32)
    private String sysName;

    // 系统配置描述
    @Column(name = "sys_desc", nullable = false, length = 64)
    private String sysDesc;

    // 系统配置值
    @Column(name = "sys_value", nullable = false, length = 32)
    private String sysValue;

    // 系统配置项类型
    @Column(name = "sys_show_type")
    private Integer sysShowType;
}