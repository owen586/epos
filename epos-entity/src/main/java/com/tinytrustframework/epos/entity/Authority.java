/*
 * 文 件 名:  Security.java
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2015-8-21
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * 权限类
 *
 * @author owen
 * @version [版本号, 2015-8-21]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 22067175343893247L;

    // 权限编号
    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "authority_code", unique = true, nullable = false)
    private Integer authorityCode;

    // 角色（用户）编号
    @Column(name = "role_user_code", length = 64)
    private String roleUserCode;

    // 菜单编号
    @Column(name = "menu_code", length = 64)
    private Integer menuCode;

    // 菜单路径
    @Column(name = "menu_url", length = 64)
    private String menuUrl;
}
