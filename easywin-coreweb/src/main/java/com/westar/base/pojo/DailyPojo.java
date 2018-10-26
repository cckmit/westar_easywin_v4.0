package com.westar.base.pojo;

import com.westar.base.model.Department;
import com.westar.base.model.UserInfo;

import java.util.List;


/**
 * 用于访问页面时的参数传递
 * @author Administrator
 * @date 2018/7/4 0004 14:55
 * @ClassName: DailyPojo
 * @Description:  用于访问页面时的参数传递
 */
public class DailyPojo {

    //汇报人的企业号
    private Integer comId;
    //汇报人的姓名
    private String userName;
    //汇报人的主键
    private Integer dailierId;
    //部门的主键
    private Integer depId;
    //部门的名称
    private String depName;
    //查询时间起
    private String startDate;
    //查询时间止
    private String endDate;
    //查询分享名称
    private String dailyName;
    //汇报人类型 0自己 1下属 
    private String dailierType;
    //到底是写还是查看 0 写 1 查看
    private Integer viewType;
    //本日已汇报0 本日未汇报 1
    private String dailyDoneState;
    //汇报人
    private List<UserInfo> listOwner;
    //本部门和下级部门
    private Integer[] listTreeDeps;
    //查询的日数
    private Integer dailyNum ;
    //查询的年
    private Integer dailyYear;
    //查询的月
    private Integer dailyMonth;
    //发布状态 0未发布 1 已发布 2延迟发布
    private Integer submitState;
    //显示样式0或者null都是分享模块,1为分享模块
    private Integer mouldViewType;

    //分享类型
    private String iType;

    private List<Department> listDep;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDailierId() {
        return dailierId;
    }

    public void setDailierId(Integer dailierId) {
        this.dailierId = dailierId;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDailyName() {
        return dailyName;
    }

    public void setDailyName(String dailyName) {
        this.dailyName = dailyName;
    }

    public String getDailierType() {
        return dailierType;
    }

    public void setDailierType(String dailierType) {
        this.dailierType = dailierType;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public String getDailyDoneState() {
        return dailyDoneState;
    }

    public void setDailyDoneState(String dailyDoneState) {
        this.dailyDoneState = dailyDoneState;
    }

    public List<UserInfo> getListOwner() {
        return listOwner;
    }

    public void setListOwner(List<UserInfo> listOwner) {
        this.listOwner = listOwner;
    }

    public Integer[] getListTreeDeps() {
        return listTreeDeps;
    }

    public void setListTreeDeps(Integer[] listTreeDeps) {
        this.listTreeDeps = listTreeDeps;
    }

    public Integer getDailyNum() {
        return dailyNum;
    }

    public void setDailyNum(Integer dailyNum) {
        this.dailyNum = dailyNum;
    }

    public Integer getDailyYear() {
        return dailyYear;
    }

    public void setDailyYear(Integer dailyYear) {
        this.dailyYear = dailyYear;
    }

    public Integer getSubmitState() {
        return submitState;
    }

    public void setSubmitState(Integer submitState) {
        this.submitState = submitState;
    }

    public List<Department> getListDep() {
        return listDep;
    }

    public void setListDep(List<Department> listDep) {
        this.listDep = listDep;
    }

    public Integer getDailyMonth() {
        return dailyMonth;
    }

    public void setDailyMonth(Integer dailyMonth) {
        this.dailyMonth = dailyMonth;
    }


    public Integer getMouldViewType() {
        return mouldViewType;
    }

    public void setMouldViewType(Integer mouldViewType) {
        this.mouldViewType = mouldViewType;
    }

    public String getiType() {
        return iType;
    }

    public void setiType(String iType) {
        this.iType = iType;
    }
}
