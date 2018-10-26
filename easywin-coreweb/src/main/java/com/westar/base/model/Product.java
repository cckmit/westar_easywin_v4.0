package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 产品表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Product {
	/** 
	* id主键
	*/
	@Identity
	private Integer id;
	/** 
	* 记录创建时间
	*/
	@DefaultFiled
	private String recordCreateTime;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 产品名称
	*/
	@Filed
	private String name;
	/** 
	* 产品类型
	*/
	@Filed
	private Integer type;
	/** 
	* 产品说明
	*/
	@Filed
	private String description;
	/** 
	* 创建者
	*/
	@Filed
	private Integer creator;
	/** 
	* 负责人
	*/
	@Filed
	private Integer principal;
	/** 
	* 产品经理
	*/
	@Filed
	private Integer manager;
	/** 
	* 版本号，可小数
	*/
	@Filed
	private String version;
	/** 
	* 状态；0：正常/进行中；1：完成；2：暂停；3：废弃；
	*/
	@Filed
	private Integer state;

	/****************以上主要为系统表字段********************/
	/** 
	* 负责人姓名
	*/
	private String principalName;
	/** 
	* 产品经历姓名
	*/
	private String managerName;
	/** 
	* 创建者姓名
	*/
	private String creatorName;
	/** 
	* 筛选起始时间
	*/
	private String startDate;
	/** 
	* 筛选结束时间
	*/
	private String endDate;
	/** 
	* 排序方式
	*/
	private String orderBy;
	/** 
	* 创建者主键集合，用于筛选
	*/
	private List<UserInfo> creators;
	/** 
	* 产品经理主键集合，用于筛选
	*/
	private List<UserInfo> managers;
	/** 
	* 负责人主键集合，用于筛选
	*/
	private List<UserInfo> principals;
	/** 
	* 是否查询自己的，1：是；0：不是；
	*/
	private Integer ownerType;
	/** 
	* 是否为关注，0：否；1：是；
	*/
	private Integer attention;
	/** 
	* 是否已经关注，0：否；1：是；
	*/
	private Integer attentionState;
	/** 
	* 产品文档
	*/
	private List<Upfiles> listUpfiles;
	/** 
	* 父版本
	*/
	private String parentVer;
	/** 
	* 子版本
	*/
	private String childVer;
	/** 
	* 留言数
	*/
	private Integer msgNum;
	/** 
	* 文档数
	*/
	private Integer docNum;

	/****************以上为自己添加字段********************/
	/** 
	* id主键
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** 
	* id主键
	* @return
	*/
	public Integer getId() {
		return id;
	}

	/** 
	* 记录创建时间
	* @param recordCreateTime
	*/
	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	/** 
	* 记录创建时间
	* @return
	*/
	public String getRecordCreateTime() {
		return recordCreateTime;
	}

	/** 
	* 企业编号
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 产品名称
	* @param name
	*/
	public void setName(String name) {
		this.name = name;
	}

	/** 
	* 产品名称
	* @return
	*/
	public String getName() {
		return name;
	}

	/** 
	* 产品类型
	* @param type
	*/
	public void setType(Integer type) {
		this.type = type;
	}

	/** 
	* 产品类型
	* @return
	*/
	public Integer getType() {
		return type;
	}

	/** 
	* 产品说明
	* @param description
	*/
	public void setDescription(String description) {
		this.description = description;
	}

	/** 
	* 产品说明
	* @return
	*/
	public String getDescription() {
		return description;
	}

	/** 
	* 创建者
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建者
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 负责人
	* @param principal
	*/
	public void setPrincipal(Integer principal) {
		this.principal = principal;
	}

	/** 
	* 负责人
	* @return
	*/
	public Integer getPrincipal() {
		return principal;
	}

	/** 
	* 产品经理
	* @param manager
	*/
	public void setManager(Integer manager) {
		this.manager = manager;
	}

	/** 
	* 产品经理
	* @return
	*/
	public Integer getManager() {
		return manager;
	}

	/** 
	* 版本号，可小数
	* @param version
	*/
	public void setVersion(String version) {
		this.version = version;
	}

	/** 
	* 版本号，可小数
	* @return
	*/
	public String getVersion() {
		return version;
	}

	/** 
	* 状态；0：正常/进行中；1：完成；2：暂停；3：废弃；
	* @param state
	*/
	public void setState(Integer state) {
		this.state = state;
	}

	/** 
	* 状态；0：正常/进行中；1：完成；2：暂停；3：废弃；
	* @return
	*/
	public Integer getState() {
		return state;
	}

	/** 
	* 负责人姓名
	* @return
	*/
	public String getPrincipalName() {
		return principalName;
	}

	/** 
	* 负责人姓名
	* @param principalName
	*/
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	/** 
	* 产品经历姓名
	* @return
	*/
	public String getManagerName() {
		return managerName;
	}

	/** 
	* 产品经历姓名
	* @param managerName
	*/
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	/** 
	* 创建者姓名
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建者姓名
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 筛选起始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 筛选起始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 筛选结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 筛选结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 排序方式
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 排序方式
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/** 
	* 创建者主键集合，用于筛选
	* @return
	*/
	public List<UserInfo> getCreators() {
		return creators;
	}

	/** 
	* 创建者主键集合，用于筛选
	* @param creators
	*/
	public void setCreators(List<UserInfo> creators) {
		this.creators = creators;
	}

	/** 
	* 产品经理主键集合，用于筛选
	* @return
	*/
	public List<UserInfo> getManagers() {
		return managers;
	}

	/** 
	* 产品经理主键集合，用于筛选
	* @param managers
	*/
	public void setManagers(List<UserInfo> managers) {
		this.managers = managers;
	}

	/** 
	* 负责人主键集合，用于筛选
	* @return
	*/
	public List<UserInfo> getPrincipals() {
		return principals;
	}

	/** 
	* 负责人主键集合，用于筛选
	* @param principals
	*/
	public void setPrincipals(List<UserInfo> principals) {
		this.principals = principals;
	}

	/** 
	* 是否查询自己的，1：是；0：不是；
	* @return
	*/
	public Integer getOwnerType() {
		return ownerType;
	}

	/** 
	* 是否查询自己的，1：是；0：不是；
	* @param ownerType
	*/
	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}

	/** 
	* 是否为关注，0：否；1：是；
	* @return
	*/
	public Integer getAttention() {
		return attention;
	}

	/** 
	* 是否为关注，0：否；1：是；
	* @param attention
	*/
	public void setAttention(Integer attention) {
		this.attention = attention;
	}

	/** 
	* 是否已经关注，0：否；1：是；
	* @return
	*/
	public Integer getAttentionState() {
		return attentionState;
	}

	/** 
	* 是否已经关注，0：否；1：是；
	* @param attentionState
	*/
	public void setAttentionState(Integer attentionState) {
		this.attentionState = attentionState;
	}

	/** 
	* 产品文档
	* @return
	*/
	public List<Upfiles> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 产品文档
	* @param listUpfiles
	*/
	public void setListUpfiles(List<Upfiles> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}

	/** 
	* 父版本
	* @return
	*/
	public String getParentVer() {
		return parentVer;
	}

	/** 
	* 父版本
	* @param parentVer
	*/
	public void setParentVer(String parentVer) {
		this.parentVer = parentVer;
	}

	/** 
	* 子版本
	* @return
	*/
	public String getChildVer() {
		return childVer;
	}

	/** 
	* 子版本
	* @param childVer
	*/
	public void setChildVer(String childVer) {
		this.childVer = childVer;
	}

	/** 
	* 留言数
	* @return
	*/
	public Integer getMsgNum() {
		return msgNum;
	}

	/** 
	* 留言数
	* @param msgNum
	*/
	public void setMsgNum(Integer msgNum) {
		this.msgNum = msgNum;
	}

	/** 
	* 文档数
	* @return
	*/
	public Integer getDocNum() {
		return docNum;
	}

	/** 
	* 文档数
	* @param docNum
	*/
	public void setDocNum(Integer docNum) {
		this.docNum = docNum;
	}
}
