package com.tinytrustframework.epos.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * 菜单类
 *
 * @author owen
 * @version [版本号, 2015-7-26]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = -9092168016857982030L;

    // 菜单编号
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "menu_code", unique = true, nullable = false)
    private int menuCode;

    // 菜单类型
    // 1:菜单  2:功能 3:链接
    @Column(name = "menu_type")
    private int menuType;

    // 菜单级别
    //  1:一级菜单  2:二级菜单
    @Column(name = "menu_level")
    private int menuLevel;

    // 菜单名称
    @Column(name = "menu_name", nullable = false, length = 32)
    private String menuName;

    // 菜单URL
    @Column(name = "menu_url", nullable = false, length = 128)
    private String menuUrl;

    // 菜单顺序
    @Column(name = "menu_order")
    private int menuOrder;

    // 父菜单编号
    @Column(name = "top_menu_code", nullable = false)
    private int topMenuCode;

    // 判断是否选中
    @Transient
    private boolean checked = false;

    /**
     * <默认构造函数>
     */
    public Menu(int menuCode, String menuName, String menuUrl) {
        super();
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
    }

    /**
     * <默认构造函数>
     */
    public Menu(int menuCode, int menuType, int menuLevel, String menuName, String menuUrl, int menuOrder,
                int topMenuCode) {
        super();
        this.menuCode = menuCode;
        this.menuType = menuType;
        this.menuLevel = menuLevel;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.menuOrder = menuOrder;
        this.topMenuCode = topMenuCode;
    }
}
