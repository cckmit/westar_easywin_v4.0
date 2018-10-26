package com.westar.base.pojo;

/**
 * 用于发布分享时将字符串转为该对象，以便于操作
 */
public class ZTreePojo {
    //主键
    private Integer id;
    //用于树形结构
    private Integer pId;
    //结点名称
    private String name;
    //类型0所有，1自定义，2自己
    private Integer ztype;
    //是否选中
    private String checked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getZtype() {
        return ztype;
    }

    public void setZtype(Integer ztype) {
        this.ztype = ztype;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}