package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.UserInfo;

/**
 * 
 * 描述:平台任务统计
 * @author zzq
 * @date 2018年5月22日 下午1:42:52
 */
public class StatisticCrmVo {
	//客户主键
	private Integer crmId;
	//客户名称
	private String crmName;
	//客户类别主键
	private Integer crmTypeId; 
	//客户类别名称
	private String crmTypeName; 
	//客户负责人主键
	private Integer owner;
	//客户负责人名称
	private String ownerName;
	//客户最近一次负责人更新时间
	private String lastUpdateDate;
	//维护周期
	private Integer modifyPeriod;
	//至今毫秒级未更新数目
	private Long lastTimes;
	//查询的年份
	private String year;
	//逾期等级
	private Integer overTimeLevel;
	//负责人选择
	private List<UserInfo> listOwner;
	//查询类别的客户数
	private Integer crmTypeNum;
	public Integer getCrmId() {
		return crmId;
	}
	public void setCrmId(Integer crmId) {
		this.crmId = crmId;
	}
	public String getCrmName() {
		return crmName;
	}
	public void setCrmName(String crmName) {
		this.crmName = crmName;
	}
	public Integer getCrmTypeId() {
		return crmTypeId;
	}
	public void setCrmTypeId(Integer crmTypeId) {
		this.crmTypeId = crmTypeId;
	}
	public String getCrmTypeName() {
		return crmTypeName;
	}
	public void setCrmTypeName(String crmTypeName) {
		this.crmTypeName = crmTypeName;
	}
	public Integer getOwner() {
		return owner;
	}
	public void setOwner(Integer owner) {
		this.owner = owner;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Integer getModifyPeriod() {
		return modifyPeriod;
	}
	public void setModifyPeriod(Integer modifyPeriod) {
		this.modifyPeriod = modifyPeriod;
	}
	public Long getLastTimes() {
		return lastTimes;
	}
	public void setLastTimes(Long lastTimes) {
		this.lastTimes = lastTimes;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getOverTimeLevel() {
		return overTimeLevel;
	}
	public void setOverTimeLevel(Integer overTimeLevel) {
		this.overTimeLevel = overTimeLevel;
	}
	public List<UserInfo> getListOwner() {
		return listOwner;
	}
	public void setListOwner(List<UserInfo> listOwner) {
		this.listOwner = listOwner;
	}
	public Integer getCrmTypeNum() {
		return crmTypeNum;
	}
	public void setCrmTypeNum(Integer crmTypeNum) {
		this.crmTypeNum = crmTypeNum;
	}
}
