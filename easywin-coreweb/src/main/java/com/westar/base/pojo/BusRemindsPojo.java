package com.westar.base.pojo;

import com.westar.base.model.BusRemindUser;

import java.util.List;

/**
 * 用于批量催办
 */
public class BusRemindsPojo {
    /**
     * 该表单需要被催办的人员集合
     */
    private List<BusRemindUser> busRemindUserList;
    /**
     * 表单名称
     */
    private String flowName;
    /**
     * 表单模块
     * @return
     */
    private String busModName;
    /**
     * 表单主键
     * @return
     */
    private Integer id;

    /**
     * 该表单需要被催办的人员集合
     */
    public List<BusRemindUser> getBusRemindUserList() {
        return busRemindUserList;
    }

    public void setBusRemindUserList(List<BusRemindUser> busRemindUserList) {
        this.busRemindUserList = busRemindUserList;
    }

    /**
     * 表单名称
     */
    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    /**
     * 表单模块
     * @return
     */
    public String getBusModName() {
        return busModName;
    }

    public void setBusModName(String busModName) {
        this.busModName = busModName;
    }

    public Integer getId() {
        return id;
    }

    /**
     * 表单主键
     * @return
     */
    public void setId(Integer id) {
        this.id = id;
    }
}