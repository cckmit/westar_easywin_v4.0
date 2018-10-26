package com.westar.base.pojo;

import java.util.List;

import com.westar.base.model.Task;

//存放项目阶段组合信息
public class ItemStagedInfo {

	// 主键
	private String key;

	// 创建时间
	private String recordcreatetime;

	// 名称
	private String name;

	// 创建人姓名
	private String userName;

	// 排序
	private String orderBy;

	// 父级主键
	private String parentId;

	// 节点类型
	private String type;

	// 节点ID
	private Integer realId;

	// 业务主键
	private Integer moduleId;

	// 真正的父节点ID
	private Integer realPid;

	// 子文件夹数
	private Integer children;

	// 文件的uuid
	private String uuid;

	// 文件的uuid
	private String fileExt;
	// 是否为叶子节点
	private Integer isLeaf;
	// 项目阶段下所属任务集合
	private List<Task> listTask;
	// 任务执行用时（单位：时）
	private long usedTimes;
	// 任务执行超时（单位：时）
	private long overTimes;
	/* 项目负责人 */
	private Integer owner;
	/* 项目状态(1，进行中；3，挂起中；4，完成。) */
	private Integer state;
	/* 负责人姓名 */
	private String ownerName;
	// 项目名次
	private String itemName;
	/* 项目主键 */
	private Integer itemId;
	/**
	 * 创建人
	 */
	private Integer creator;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRecordcreatetime() {
		return recordcreatetime;
	}

	public void setRecordcreatetime(String recordcreatetime) {
		this.recordcreatetime = recordcreatetime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getRealId() {
		return realId;
	}

	public void setRealId(Integer realId) {
		this.realId = realId;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getRealPid() {
		return realPid;
	}

	public void setRealPid(Integer realPid) {
		this.realPid = realPid;
	}

	public Integer getChildren() {
		return children;
	}

	public void setChildren(Integer children) {
		this.children = children;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * @return the listTask
	 */
	public List<Task> getListTask() {
		return listTask;
	}

	/**
	 * @param listTask
	 *            the listTask to set
	 */
	public void setListTask(List<Task> listTask) {
		this.listTask = listTask;
	}

	/**
	 * @return the usedTimes
	 */
	public long getUsedTimes() {
		return usedTimes;
	}

	/**
	 * @param usedTimes
	 *            the usedTimes to set
	 */
	public void setUsedTimes(long usedTimes) {
		this.usedTimes = usedTimes;
	}

	/**
	 * @return the overTimes
	 */
	public long getOverTimes() {
		return overTimes;
	}

	/**
	 * @param overTimes
	 *            the overTimes to set
	 */
	public void setOverTimes(long overTimes) {
		this.overTimes = overTimes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemStagedInfo [key=" + key + ", recordcreatetime=" + recordcreatetime + ", name=" + name
				+ ", userName=" + userName + ", orderBy=" + orderBy + ", parentId=" + parentId + ", type=" + type
				+ ", realId=" + realId + ", moduleId=" + moduleId + ", realPid=" + realPid + ", children=" + children
				+ ", uuid=" + uuid + ", fileExt=" + fileExt + ", isLeaf=" + isLeaf + ", creator=" + creator + "]";
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
}
