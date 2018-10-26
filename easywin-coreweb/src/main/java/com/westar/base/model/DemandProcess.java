package com.westar.base.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 需求信息
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DemandProcess {
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
	* 需求提出人
	*/
	@Filed
	private Integer creator;
	/** 
	* 星级等级
	*/
	@Filed
	private String startLevel;
	/** 
	* 需求描述
	*/
	@Filed
	private String describe;
	/** 
	* 验收标准
	*/
	@Filed
	private String standard;
	/** 
	* 需求类型
	*/
	@Filed
	private String type;
	/** 
	* 项期待完成时间
	*/
	@Filed
	private String expectFinishDate;
	/** 
	* 项目关联
	*/
	@Filed
	private Integer itemId;
	/** 
	* 产品关联
	*/
	@Filed
	private Integer productId;
	/** 
	* 处理状态 0待处理 1审核 -1拒绝 2处理中 3成果确认 4完结
	*/
	@Filed
	private String state;
	/** 
	* 处理人员
	*/
	@Filed
	private Integer handleUser;
	/** 
	* 拒绝接收的理由
	*/
	@Filed
	private String rejectReason;
	/** 
	* 需求编号
	*/
	@Filed
	private String serialNum;
	/** 
	* 修改模块
	*/
	@Filed
	private Integer itemModId;

	/****************以上主要为系统表字段********************/
	/** 
	* 需求附件说明
	*/
	public List<DemandFile> listDemandFile;
	/** 
	* 查询条件的发布人
	*/
	public List<UserInfo> listCreator;
	/** 
	* 查询条件的项目
	*/
	public List<Item> listItem;
	/** 
	* 查询条件的产品
	*/
	public List<Product> listProduct;
	/** 
	* 阶段信息
	*/
	private List<DemandHandleHis> listDemandHandleHis;
	/** 
	* 项目名称
	*/
	public String itemName;
	/** 
	* 类型名称
	*/
	private String typeName;
	/** 
	* 产品名称
	*/
	public String productName;
	/** 
	* 产品模块名称
	*/
	public String itemModName;
	/** 
	* 发布人员名称
	*/
	public String creatorName;
	/** 
	* 项目负责人主键
	*/
	public Integer itemOwnerId;
	/** 
	* 项目负责人名称
	*/
	public String itemOwnerName;
	/** 
	* 状态名称
	*/
	public String stateName;
	/** 
	* 查询的开始时间
	*/
	private String startDate;
	/** 
	* 查询的结束时间
	*/
	private String endDate;
	/** 
	* 处理结果
	*/
	private String content;
	/** 
	* 阶段负责人
	*/
	private String handleUserName;
	/** 
	* 阶段状态
	*/
	private String stageState;
	/** 
	* 阶段开始时间
	*/
	private String stageStartDateTime;
	/** 
	* 阶段耗费时间
	*/
	private Long stageCostTime;

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
	* 需求提出人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 需求提出人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 星级等级
	* @param startLevel
	*/
	public void setStartLevel(String startLevel) {
		this.startLevel = startLevel;
	}

	/** 
	* 星级等级
	* @return
	*/
	public String getStartLevel() {
		return startLevel;
	}

	/** 
	* 需求描述
	* @param describe
	*/
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/** 
	* 需求描述
	* @return
	*/
	public String getDescribe() {
		return describe;
	}

	/** 
	* 验收标准
	* @param standard
	*/
	public void setStandard(String standard) {
		this.standard = standard;
	}

	/** 
	* 验收标准
	* @return
	*/
	public String getStandard() {
		return standard;
	}

	/** 
	* 需求类型
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 需求类型
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 项期待完成时间
	* @param expectFinishDate
	*/
	public void setExpectFinishDate(String expectFinishDate) {
		this.expectFinishDate = expectFinishDate;
	}

	/** 
	* 项期待完成时间
	* @return
	*/
	public String getExpectFinishDate() {
		return expectFinishDate;
	}

	/** 
	* 项目关联
	* @param itemId
	*/
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/** 
	* 项目关联
	* @return
	*/
	public Integer getItemId() {
		return itemId;
	}

	/** 
	* 产品关联
	* @param productId
	*/
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/** 
	* 产品关联
	* @return
	*/
	public Integer getProductId() {
		return productId;
	}

	/** 
	* 处理状态 0待处理 1审核 -1拒绝 2处理中 3成果确认 4完结
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 处理状态 0待处理 1审核 -1拒绝 2处理中 3成果确认 4完结
	* @return
	*/
	public String getState() {
		return state;
	}

	/** 
	* 处理人员
	* @param handleUser
	*/
	public void setHandleUser(Integer handleUser) {
		this.handleUser = handleUser;
	}

	/** 
	* 处理人员
	* @return
	*/
	public Integer getHandleUser() {
		return handleUser;
	}

	/** 
	* 拒绝接收的理由
	* @param rejectReason
	*/
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	/** 
	* 拒绝接收的理由
	* @return
	*/
	public String getRejectReason() {
		return rejectReason;
	}

	/** 
	* 需求附件说明
	* @return
	*/
	public List<DemandFile> getListDemandFile() {
		return listDemandFile;
	}

	/** 
	* 需求附件说明
	* @param listDemandFile
	*/
	public void setListDemandFile(List<DemandFile> listDemandFile) {
		this.listDemandFile = listDemandFile;
	}

	/** 
	* 需求编号
	* @param serialNum
	*/
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	/** 
	* 需求编号
	* @return
	*/
	public String getSerialNum() {
		return serialNum;
	}

	/** 
	* 项目名称
	* @return
	*/
	public String getItemName() {
		return itemName;
	}

	/** 
	* 项目名称
	* @param itemName
	*/
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/** 
	* 产品名称
	* @return
	*/
	public String getProductName() {
		return productName;
	}

	/** 
	* 产品名称
	* @param productName
	*/
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/** 
	* 产品模块名称
	* @return
	*/
	public String getItemModName() {
		return itemModName;
	}

	/** 
	* 产品模块名称
	* @param itemModName
	*/
	public void setItemModName(String itemModName) {
		this.itemModName = itemModName;
	}

	/** 
	* 发布人员名称
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 发布人员名称
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 项目负责人主键
	* @return
	*/
	public Integer getItemOwnerId() {
		return itemOwnerId;
	}

	/** 
	* 项目负责人主键
	* @param itemOwnerId
	*/
	public void setItemOwnerId(Integer itemOwnerId) {
		this.itemOwnerId = itemOwnerId;
	}

	/** 
	* 项目负责人名称
	* @return
	*/
	public String getItemOwnerName() {
		return itemOwnerName;
	}

	/** 
	* 项目负责人名称
	* @param itemOwnerName
	*/
	public void setItemOwnerName(String itemOwnerName) {
		this.itemOwnerName = itemOwnerName;
	}

	/** 
	* 状态名称
	* @return
	*/
	public String getStateName() {
		return stateName;
	}

	/** 
	* 状态名称
	* @param stateName
	*/
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/** 
	* 查询条件的发布人
	* @return
	*/
	public List<UserInfo> getListCreator() {
		return listCreator;
	}

	/** 
	* 查询条件的发布人
	* @param listCreator
	*/
	public void setListCreator(List<UserInfo> listCreator) {
		this.listCreator = listCreator;
	}

	/** 
	* 查询条件的项目
	* @return
	*/
	public List<Item> getListItem() {
		return listItem;
	}

	/** 
	* 查询条件的项目
	* @param listItem
	*/
	public void setListItem(List<Item> listItem) {
		this.listItem = listItem;
	}

	/** 
	* 查询条件的产品
	* @return
	*/
	public List<Product> getListProduct() {
		return listProduct;
	}

	/** 
	* 查询条件的产品
	* @param listProduct
	*/
	public void setListProduct(List<Product> listProduct) {
		this.listProduct = listProduct;
	}

	/** 
	* 查询的开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 类型名称
	* @return
	*/
	public String getTypeName() {
		return typeName;
	}

	/** 
	* 类型名称
	* @param typeName
	*/
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** 
	* 处理结果
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 处理结果
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 阶段信息
	* @return
	*/
	public List<DemandHandleHis> getListDemandHandleHis() {
		return listDemandHandleHis;
	}

	/** 
	* 阶段信息
	* @param listDemandHandleHis
	*/
	public void setListDemandHandleHis(List<DemandHandleHis> listDemandHandleHis) {
		this.listDemandHandleHis = listDemandHandleHis;
	}

	/** 
	* 修改模块
	* @param itemModId
	*/
	public void setItemModId(Integer itemModId) {
		this.itemModId = itemModId;
	}

	/** 
	* 修改模块
	* @return
	*/
	public Integer getItemModId() {
		return itemModId;
	}

	/** 
	* 阶段负责人
	* @return
	*/
	public String getHandleUserName() {
		return handleUserName;
	}

	/** 
	* 阶段负责人
	* @param handleUserName
	*/
	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}

	/** 
	* 阶段开始时间
	* @return
	*/
	public String getStageStartDateTime() {
		return stageStartDateTime;
	}

	/** 
	* 阶段开始时间
	* @param stageStartDateTime
	*/
	public void setStageStartDateTime(String stageStartDateTime) {
		this.stageStartDateTime = stageStartDateTime;
	}

	/** 
	* 阶段状态
	* @return
	*/
	public String getStageState() {
		return stageState;
	}

	/** 
	* 阶段状态
	* @param stageState
	*/
	public void setStageState(String stageState) {
		this.stageState = stageState;
	}

	/** 
	* 阶段耗费时间
	* @return
	*/
	public Long getStageCostTime() {
		return stageCostTime;
	}

	/** 
	* 阶段耗费时间
	* @param stageCostTime
	*/
	public void setStageCostTime(Long stageCostTime) {
		this.stageCostTime = stageCostTime;
	}
}
