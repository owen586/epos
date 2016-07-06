package com.tinytrust.epos.entity;

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
 * <一句话功能简述>
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_role")
public class Role implements java.io.Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3019768157811043116L;

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "role_code", unique = true, nullable = false)
    private Integer roleCode;

    @Column(name = "role_name", length = 64)
    private String roleName;
}
