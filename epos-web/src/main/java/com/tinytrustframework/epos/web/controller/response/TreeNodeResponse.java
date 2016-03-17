package com.tinytrustframework.epos.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 树形结构响应类
 *
 * @author owen
 * @version [版本号, 2015-8-20]
 */
@Data
@Builder
public class TreeNodeResponse extends Response {
    /**
     * 当前节点编号
     */
    private int id;

    /**
     * 当前节点名称
     */
    private String name;

    /**
     * 父节点编号
     */
    private int pId;

    /**
     * 是否打开
     */
    private boolean open;


    /**
     * 菜单路径
     */
    private String url;


    /**
     * 是否选中
     */
    private boolean checked;

    /**
     * 节点图标
     */
    private String icon;

    /**
     * <默认构造函数>
     */
    public TreeNodeResponse() {
        super();
    }


    /**
     * <默认构造函数>
     */
    public TreeNodeResponse(int id, String name, int pId, boolean open, String url, boolean checked) {
        super();
        this.id = id;
        this.name = name;
        this.pId = pId;
        this.open = open;
        this.url = url;
        this.checked = checked;
    }


    /**
     * <默认构造函数>
     */
    public TreeNodeResponse(int id, String name, int pId, boolean open, String url, boolean checked, String icon) {
        super();
        this.id = id;
        this.name = name;
        this.pId = pId;
        this.open = open;
        this.url = url;
        this.checked = checked;
        this.icon = icon;
    }


    public String getIcon() {
        return icon;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

//    public String getIcon()
//    {
//        return icon;
//    }
//    
//    public void setIcon(String icon)
//    {
//        this.icon = icon;
//    }

}
