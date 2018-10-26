package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.ExcelExportColumn;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.FlowRecord;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 客户信息表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Customer {
	/** 
	* id主键
	*/
	@Identity
	private Integer id;
	/** 
	* 记录创建时间
	*/
	@DefaultFiled
	@ExcelExportColumn(name = "创建时间", order = 6)
	private String recordCreateTime;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 客户名称
	*/
	@ExcelExportColumn(name = "客户名称", order = 3)
	@Filed
	private String customerName;
	/** 
	* 区域主键
	*/
	@Filed
	private Integer areaId;
	/** 
	* 客户类型主键
	*/
	@Filed
	private Integer customerTypeId;
	/** 
	* 联系电话
	*/
	@Filed
	private String linePhone;
	/** 
	* 传真
	*/
	@Filed
	private String fax;
	/** 
	* 客户地址
	*/
	@Filed
	private String address;
	/** 
	* 邮编
	*/
	@Filed
	private Integer postCode;
	/** 
	* 客户备注
	*/
	@Filed
	private String customerRemark;
	/** 
	* 客户负责人
	*/
	@Filed
	private Integer owner;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 删除标识 0未删除，1预删除
	*/
	@Filed
	private Integer delState;
	/** 
	* 客户阶段
	*/
	@Filed
	private Integer stage;
	/** 
	* 资金预算
	*/
	@ExcelExportColumn(name = "资金预算", order = 2)
	@Filed
	private Float budget;
	/** 
	* 公开或私有标记 0私有  1公开 
	*/
	@Filed
	private Integer pubState;

	/****************以上主要为系统表字段********************/
	/** 
	* 客户参与人集合
	*/
	private List<CustomerSharer> listCustomerSharer;
	/** 
	* 附件集合
	*/
	private List<CustomerUpfile> listUpfiles;
	@ExcelExportColumn(name = "负责人", order = 5)
	private String ownerName;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 是否分享信息
	*/
	private String shareMsg;
	/** 
	* 私有组ID和组类型组合字符串
	*/
	private String groupIdAndType;
	/** 
	* 私有组ID和组类型组合字符串
	*/
	private String areaIdAndType;
	/** 
	* 客户联系人集合
	*/
	private List<LinkMan> listLinkMan;
	/** 
	* 区域名称
	*/
	private String areaName;
	/** 
	* 类型名称
	*/
	@ExcelExportColumn(name = "客户类型", order = 3)
	private String typeName;
	/** 
	* 客户分享人json字符串
	*/
	private String sharerJson;
	/** 
	* 联系人个数
	*/
	private Integer linkManSum;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 构建前端显示html
	*/
	private String linkManStr;
	/** 
	* 是否已经查看0没有，1已查看
	*/
	private Integer isRead;
	/** 
	* 最新更新时间
	*/
	private String modifyTime;
	/** 
	* 最新更新内容
	*/
	private String modifyContent;
	/** 
	* 更新人主键
	*/
	private String modifier;
	/** 
	* 更新人性别
	*/
	private String modifierGender;
	/** 
	* 更新人名字
	*/
	private String modifierName;
	/** 
	* 更新人名字
	*/
	private String modifierUuid;
	/** 
	* 更新人名字
	*/
	private String modifierFileName;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 客户成员组
	*/
	private List<SelfGroup> listShareGroup;
	/** 
	* 关注状态0未关注1已关注
	*/
	private String attentionState;
	/** 
	* 负责人类型0自己1下属
	*/
	private String ownerType;
	/** 
	* 上传附件名称数组
	*/
	private String[] filesName;
	/** 
	* 客户维护记录
	*/
	private List<FeedBackInfo> listFeedBackInfo;
	/** 
	* 移交记录
	*/
	private List<FlowRecord> listFlowRecord;
	/** 
	* 浏览记录
	*/
	private List<ViewRecord> listViewRecord;
	/** 
	* 客户日志
	*/
	private List<CustomerLog> listCustomerLog;
	/** 
	* 客户日志任务
	*/
	private List<Task> listCrmTask;
	/** 
	* 区域对象
	*/
	private Area crmArea;
	/** 
	* 客户类型对象
	*/
	private CustomerType crmType;
	/** 
	* 客户添加类型
	*/
	private String addType;
	/** 
	* 所有客户总数
	*/
	private Integer allCrmNum;
	/** 
	* 我的客户总数
	*/
	private Integer myCrmNum;
	/** 
	* 下属客户总数
	*/
	private Integer subCrmNum;
	/** 
	* 关注客户总数
	*/
	private Integer attenCrmNum;
	/** 
	* 移交客户总数
	*/
	private Integer handsCrmNum;
	/** 
	* 本月新增客户总数
	*/
	private Integer addByMonthNum;
	/** 
	* 异步查询数据需要排除的
	*/
	private String crmIds;
	/** 
	* 留言总数
	*/
	private Integer sumOfMsg;
	/** 
	* 客户的闹铃集合
	*/
	private List<Clock> listClocks;
	/** 
	* 关联项目
	*/
	private List<Item> itemList;
	/** 
	* 模块操作权限集合
	*/
	private List<ModuleOperateConfig> optCfgs;
	/** 
	* 客户关联任务数
	*/
	private Integer crmTaskSum;
	/** 
	* 客户关联项目数
	*/
	private Integer crmItemSum;
	/** 
	* 更新频率开始时间
	*/
	private String frequenStartDate;
	/** 
	* 更新频率结束时间
	*/
	private String frequenEndDate;
	/** 
	* 客户增量统计的选择年份
	*/
	private String selectYear;
	/** 
	* 负责人
	*/
	private List<UserInfo> listOwner;
	@ExcelExportColumn(name = "所属阶段", order = 4)
	private String stageName;
	/** 
	* 排序
	*/
	private String orderBy;
	private Integer typeOrder;
	/** 
	* 最小预算
	*/
	private Float minBudget;
	/** 
	* 最大预算
	*/
	private Float maxBudget;
	/** 
	* 客户类型集合
	*/
	private List<CustomerType> listCrmType;
	/** 
	* 项目总数（显示数量用）
	*/
	private Integer itemNum;
	/** 
	* 任务总数（显示数量用）
	*/
	private Integer taskNum;
	/** 
	* 附件总数（显示数量用）
	*/
	private Integer fileNum;

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
	* 客户名称
	* @param customerName
	*/
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/** 
	* 客户名称
	* @return
	*/
	public String getCustomerName() {
		return customerName;
	}

	/** 
	* 区域主键
	* @param areaId
	*/
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	/** 
	* 区域主键
	* @return
	*/
	public Integer getAreaId() {
		return areaId;
	}

	/** 
	* 客户类型主键
	* @param customerTypeId
	*/
	public void setCustomerTypeId(Integer customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	/** 
	* 客户类型主键
	* @return
	*/
	public Integer getCustomerTypeId() {
		return customerTypeId;
	}

	/** 
	* 联系电话
	* @param linePhone
	*/
	public void setLinePhone(String linePhone) {
		this.linePhone = linePhone;
	}

	/** 
	* 联系电话
	* @return
	*/
	public String getLinePhone() {
		return linePhone;
	}

	/** 
	* 传真
	* @param fax
	*/
	public void setFax(String fax) {
		this.fax = fax;
	}

	/** 
	* 传真
	* @return
	*/
	public String getFax() {
		return fax;
	}

	/** 
	* 客户地址
	* @param address
	*/
	public void setAddress(String address) {
		this.address = address;
	}

	/** 
	* 客户地址
	* @return
	*/
	public String getAddress() {
		return address;
	}

	/** 
	* 邮编
	* @param postCode
	*/
	public void setPostCode(Integer postCode) {
		this.postCode = postCode;
	}

	/** 
	* 邮编
	* @return
	*/
	public Integer getPostCode() {
		return postCode;
	}

	/** 
	* 客户备注
	* @param customerRemark
	*/
	public void setCustomerRemark(String customerRemark) {
		this.customerRemark = customerRemark;
	}

	/** 
	* 客户备注
	* @return
	*/
	public String getCustomerRemark() {
		return customerRemark;
	}

	/** 
	* 客户负责人
	* @param owner
	*/
	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	/** 
	* 客户负责人
	* @return
	*/
	public Integer getOwner() {
		return owner;
	}

	/** 
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 客户参与人集合
	* @return
	*/
	public List<CustomerSharer> getListCustomerSharer() {
		return listCustomerSharer;
	}

	/** 
	* 客户参与人集合
	* @param listCustomerSharer
	*/
	public void setListCustomerSharer(List<CustomerSharer> listCustomerSharer) {
		this.listCustomerSharer = listCustomerSharer;
	}

	/** 
	* 附件集合
	* @return
	*/
	public List<CustomerUpfile> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 附件集合
	* @param listUpfiles
	*/
	public void setListUpfiles(List<CustomerUpfile> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 是否分享信息
	* @return
	*/
	public String getShareMsg() {
		return shareMsg;
	}

	/** 
	* 是否分享信息
	* @param shareMsg
	*/
	public void setShareMsg(String shareMsg) {
		this.shareMsg = shareMsg;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @return
	*/
	public String getGroupIdAndType() {
		return groupIdAndType;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @param groupIdAndType
	*/
	public void setGroupIdAndType(String groupIdAndType) {
		this.groupIdAndType = groupIdAndType;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @return
	*/
	public String getAreaIdAndType() {
		return areaIdAndType;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @param areaIdAndType
	*/
	public void setAreaIdAndType(String areaIdAndType) {
		this.areaIdAndType = areaIdAndType;
	}

	/** 
	* 客户联系人集合
	* @return
	*/
	public List<LinkMan> getListLinkMan() {
		return listLinkMan;
	}

	/** 
	* 客户联系人集合
	* @param listLinkMan
	*/
	public void setListLinkMan(List<LinkMan> listLinkMan) {
		this.listLinkMan = listLinkMan;
	}

	/** 
	* 区域名称
	* @return
	*/
	public String getAreaName() {
		return areaName;
	}

	/** 
	* 区域名称
	* @param areaName
	*/
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	* 客户分享人json字符串
	* @return
	*/
	public String getSharerJson() {
		return sharerJson;
	}

	/** 
	* 客户分享人json字符串
	* @param sharerJson
	*/
	public void setSharerJson(String sharerJson) {
		this.sharerJson = sharerJson;
	}

	/** 
	* 联系人个数
	* @return
	*/
	public Integer getLinkManSum() {
		return linkManSum;
	}

	/** 
	* 联系人个数
	* @param linkManSum
	*/
	public void setLinkManSum(Integer linkManSum) {
		this.linkManSum = linkManSum;
	}

	public boolean isSucc() {
		return succ;
	}

	/** 
	* boolean标识
	* @param succ
	*/
	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	/** 
	* 提示信息
	* @return
	*/
	public String getPromptMsg() {
		return promptMsg;
	}

	/** 
	* 提示信息
	* @param promptMsg
	*/
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	/** 
	* 构建前端显示html
	* @return
	*/
	public String getLinkManStr() {
		return linkManStr;
	}

	/** 
	* 构建前端显示html
	* @param linkManStr
	*/
	public void setLinkManStr(String linkManStr) {
		this.linkManStr = linkManStr;
	}

	/** 
	* 是否已经查看0没有，1已查看
	* @return
	*/
	public Integer getIsRead() {
		return isRead;
	}

	/** 
	* 是否已经查看0没有，1已查看
	* @param isRead
	*/
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	/** 
	* 最新更新时间
	* @return
	*/
	public String getModifyTime() {
		return modifyTime;
	}

	/** 
	* 最新更新时间
	* @param modifyTime
	*/
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/** 
	* 最新更新内容
	* @return
	*/
	public String getModifyContent() {
		return modifyContent;
	}

	/** 
	* 最新更新内容
	* @param modifyContent
	*/
	public void setModifyContent(String modifyContent) {
		this.modifyContent = modifyContent;
	}

	/** 
	* 更新人名字
	* @return
	*/
	public String getModifierName() {
		return modifierName;
	}

	/** 
	* 更新人名字
	* @param modifierName
	*/
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	/** 
	* 查询的时间起
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的时间起
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的时间止
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的时间止
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 删除标识 0未删除，1预删除
	* @param delState
	*/
	public void setDelState(Integer delState) {
		this.delState = delState;
	}

	/** 
	* 删除标识 0未删除，1预删除
	* @return
	*/
	public Integer getDelState() {
		return delState;
	}

	/** 
	* 客户成员组
	* @return
	*/
	public List<SelfGroup> getListShareGroup() {
		return listShareGroup;
	}

	/** 
	* 客户成员组
	* @param listShareGroup
	*/
	public void setListShareGroup(List<SelfGroup> listShareGroup) {
		this.listShareGroup = listShareGroup;
	}

	/** 
	* 关注状态0未关注1已关注
	* @return
	*/
	public String getAttentionState() {
		return attentionState;
	}

	/** 
	* 关注状态0未关注1已关注
	* @param attentionState
	*/
	public void setAttentionState(String attentionState) {
		this.attentionState = attentionState;
	}

	/** 
	* 负责人类型0自己1下属
	* @return
	*/
	public String getOwnerType() {
		return ownerType;
	}

	/** 
	* 负责人类型0自己1下属
	* @param ownerType
	*/
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	/** 
	* 更新人名字
	* @return
	*/
	public String getModifierUuid() {
		return modifierUuid;
	}

	/** 
	* 更新人名字
	* @param modifierUuid
	*/
	public void setModifierUuid(String modifierUuid) {
		this.modifierUuid = modifierUuid;
	}

	/** 
	* 更新人名字
	* @return
	*/
	public String getModifierFileName() {
		return modifierFileName;
	}

	/** 
	* 更新人名字
	* @param modifierFileName
	*/
	public void setModifierFileName(String modifierFileName) {
		this.modifierFileName = modifierFileName;
	}

	/** 
	* 更新人主键
	* @return
	*/
	public String getModifier() {
		return modifier;
	}

	/** 
	* 更新人主键
	* @param modifier
	*/
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/** 
	* 更新人性别
	* @return
	*/
	public String getModifierGender() {
		return modifierGender;
	}

	/** 
	* 更新人性别
	* @param modifierGender
	*/
	public void setModifierGender(String modifierGender) {
		this.modifierGender = modifierGender;
	}

	/** 
	* 上传附件名称数组
	* @return
	*/
	public String[] getFilesName() {
		return filesName;
	}

	/** 
	* 上传附件名称数组
	* @param filesName
	*/
	public void setFilesName(String[] filesName) {
		this.filesName = filesName;
	}

	/** 
	* 客户维护记录
	* @return
	*/
	public List<FeedBackInfo> getListFeedBackInfo() {
		return listFeedBackInfo;
	}

	/** 
	* 客户维护记录
	* @param listFeedBackInfo
	*/
	public void setListFeedBackInfo(List<FeedBackInfo> listFeedBackInfo) {
		this.listFeedBackInfo = listFeedBackInfo;
	}

	/** 
	* 移交记录
	* @return
	*/
	public List<FlowRecord> getListFlowRecord() {
		return listFlowRecord;
	}

	/** 
	* 移交记录
	* @param listFlowRecord
	*/
	public void setListFlowRecord(List<FlowRecord> listFlowRecord) {
		this.listFlowRecord = listFlowRecord;
	}

	/** 
	* 浏览记录
	* @return
	*/
	public List<ViewRecord> getListViewRecord() {
		return listViewRecord;
	}

	/** 
	* 浏览记录
	* @param listViewRecord
	*/
	public void setListViewRecord(List<ViewRecord> listViewRecord) {
		this.listViewRecord = listViewRecord;
	}

	/** 
	* 客户日志
	* @return
	*/
	public List<CustomerLog> getListCustomerLog() {
		return listCustomerLog;
	}

	/** 
	* 客户日志
	* @param listCustomerLog
	*/
	public void setListCustomerLog(List<CustomerLog> listCustomerLog) {
		this.listCustomerLog = listCustomerLog;
	}

	/** 
	* 客户日志任务
	* @return
	*/
	public List<Task> getListCrmTask() {
		return listCrmTask;
	}

	/** 
	* 客户日志任务
	* @param listCrmTask
	*/
	public void setListCrmTask(List<Task> listCrmTask) {
		this.listCrmTask = listCrmTask;
	}

	/** 
	* 区域对象
	* @return
	*/
	public Area getCrmArea() {
		return crmArea;
	}

	/** 
	* 区域对象
	* @param crmArea
	*/
	public void setCrmArea(Area crmArea) {
		this.crmArea = crmArea;
	}

	/** 
	* 客户类型对象
	* @return
	*/
	public CustomerType getCrmType() {
		return crmType;
	}

	/** 
	* 客户类型对象
	* @param crmType
	*/
	public void setCrmType(CustomerType crmType) {
		this.crmType = crmType;
	}

	/** 
	* 客户添加类型
	* @return
	*/
	public String getAddType() {
		return addType;
	}

	/** 
	* 客户添加类型
	* @param addType
	*/
	public void setAddType(String addType) {
		this.addType = addType;
	}

	/** 
	* 所有客户总数
	* @return
	*/
	public Integer getAllCrmNum() {
		return allCrmNum;
	}

	/** 
	* 所有客户总数
	* @param allCrmNum
	*/
	public void setAllCrmNum(Integer allCrmNum) {
		this.allCrmNum = allCrmNum;
	}

	/** 
	* 我的客户总数
	* @return
	*/
	public Integer getMyCrmNum() {
		return myCrmNum;
	}

	/** 
	* 我的客户总数
	* @param myCrmNum
	*/
	public void setMyCrmNum(Integer myCrmNum) {
		this.myCrmNum = myCrmNum;
	}

	/** 
	* 下属客户总数
	* @return
	*/
	public Integer getSubCrmNum() {
		return subCrmNum;
	}

	/** 
	* 下属客户总数
	* @param subCrmNum
	*/
	public void setSubCrmNum(Integer subCrmNum) {
		this.subCrmNum = subCrmNum;
	}

	/** 
	* 关注客户总数
	* @return
	*/
	public Integer getAttenCrmNum() {
		return attenCrmNum;
	}

	/** 
	* 关注客户总数
	* @param attenCrmNum
	*/
	public void setAttenCrmNum(Integer attenCrmNum) {
		this.attenCrmNum = attenCrmNum;
	}

	/** 
	* 移交客户总数
	* @return
	*/
	public Integer getHandsCrmNum() {
		return handsCrmNum;
	}

	/** 
	* 移交客户总数
	* @param handsCrmNum
	*/
	public void setHandsCrmNum(Integer handsCrmNum) {
		this.handsCrmNum = handsCrmNum;
	}

	/** 
	* 本月新增客户总数
	* @return
	*/
	public Integer getAddByMonthNum() {
		return addByMonthNum;
	}

	/** 
	* 本月新增客户总数
	* @param addByMonthNum
	*/
	public void setAddByMonthNum(Integer addByMonthNum) {
		this.addByMonthNum = addByMonthNum;
	}

	/** 
	* 异步查询数据需要排除的
	* @return
	*/
	public String getCrmIds() {
		return crmIds;
	}

	/** 
	* 异步查询数据需要排除的
	* @param crmIds
	*/
	public void setCrmIds(String crmIds) {
		this.crmIds = crmIds;
	}

	/** 
	* 留言总数
	* @return
	*/
	public Integer getSumOfMsg() {
		return sumOfMsg;
	}

	/** 
	* 留言总数
	* @param sumOfMsg
	*/
	public void setSumOfMsg(Integer sumOfMsg) {
		this.sumOfMsg = sumOfMsg;
	}

	/** 
	* 客户的闹铃集合
	* @return
	*/
	public List<Clock> getListClocks() {
		return listClocks;
	}

	/** 
	* 客户的闹铃集合
	* @param listClocks
	*/
	public void setListClocks(List<Clock> listClocks) {
		this.listClocks = listClocks;
	}

	/** 
	* 关联项目
	* @return
	*/
	public List<Item> getItemList() {
		return itemList;
	}

	/** 
	* 关联项目
	* @param itemList
	*/
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	/** 
	* 模块操作权限集合
	* @return
	*/
	public List<ModuleOperateConfig> getOptCfgs() {
		return optCfgs;
	}

	/** 
	* 模块操作权限集合
	* @param optCfgs
	*/
	public void setOptCfgs(List<ModuleOperateConfig> optCfgs) {
		this.optCfgs = optCfgs;
	}

	/** 
	* 客户关联任务数
	* @return
	*/
	public Integer getCrmTaskSum() {
		return crmTaskSum;
	}

	/** 
	* 客户关联任务数
	* @param crmTaskSum
	*/
	public void setCrmTaskSum(Integer crmTaskSum) {
		this.crmTaskSum = crmTaskSum;
	}

	/** 
	* 客户关联项目数
	* @return
	*/
	public Integer getCrmItemSum() {
		return crmItemSum;
	}

	/** 
	* 客户关联项目数
	* @param crmItemSum
	*/
	public void setCrmItemSum(Integer crmItemSum) {
		this.crmItemSum = crmItemSum;
	}

	/** 
	* 更新频率开始时间
	* @return
	*/
	public String getFrequenStartDate() {
		return frequenStartDate;
	}

	/** 
	* 更新频率开始时间
	* @param frequenStartDate
	*/
	public void setFrequenStartDate(String frequenStartDate) {
		this.frequenStartDate = frequenStartDate;
	}

	/** 
	* 更新频率结束时间
	* @return
	*/
	public String getFrequenEndDate() {
		return frequenEndDate;
	}

	/** 
	* 更新频率结束时间
	* @param frequenEndDate
	*/
	public void setFrequenEndDate(String frequenEndDate) {
		this.frequenEndDate = frequenEndDate;
	}

	/** 
	* 客户增量统计的选择年份
	* @return
	*/
	public String getSelectYear() {
		return selectYear;
	}

	/** 
	* 客户增量统计的选择年份
	* @param selectYear
	*/
	public void setSelectYear(String selectYear) {
		this.selectYear = selectYear;
	}

	/** 
	* 负责人
	* @return
	*/
	public List<UserInfo> getListOwner() {
		return listOwner;
	}

	/** 
	* 负责人
	* @param listOwner
	*/
	public void setListOwner(List<UserInfo> listOwner) {
		this.listOwner = listOwner;
	}

	/** 
	* 客户阶段
	* @param stage
	*/
	public void setStage(Integer stage) {
		this.stage = stage;
	}

	/** 
	* 客户阶段
	* @return
	*/
	public Integer getStage() {
		return stage;
	}

	/** 
	* 资金预算
	* @param budget
	*/
	public void setBudget(Float budget) {
		this.budget = budget;
	}

	/** 
	* 资金预算
	* @return
	*/
	public Float getBudget() {
		return budget;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	/** 
	* 排序
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 排序
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(Integer typeOrder) {
		this.typeOrder = typeOrder;
	}

	/** 
	* 最小预算
	* @return
	*/
	public Float getMinBudget() {
		return minBudget;
	}

	/** 
	* 最小预算
	* @param minBudget
	*/
	public void setMinBudget(Float minBudget) {
		this.minBudget = minBudget;
	}

	/** 
	* 最大预算
	* @return
	*/
	public Float getMaxBudget() {
		return maxBudget;
	}

	/** 
	* 最大预算
	* @param maxBudget
	*/
	public void setMaxBudget(Float maxBudget) {
		this.maxBudget = maxBudget;
	}

	/** 
	* 客户类型集合
	* @return
	*/
	public List<CustomerType> getListCrmType() {
		return listCrmType;
	}

	/** 
	* 客户类型集合
	* @param listCrmType
	*/
	public void setListCrmType(List<CustomerType> listCrmType) {
		this.listCrmType = listCrmType;
	}

	/** 
	* 公开或私有标记 0私有  1公开 
	* @param pubState
	*/
	public void setPubState(Integer pubState) {
		this.pubState = pubState;
	}

	/** 
	* 公开或私有标记 0私有  1公开 
	* @return
	*/
	public Integer getPubState() {
		return pubState;
	}

	/** 
	* 项目总数（显示数量用）
	* @return
	*/
	public Integer getItemNum() {
		return itemNum;
	}

	/** 
	* 项目总数（显示数量用）
	* @param itemNum
	*/
	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}

	/** 
	* 任务总数（显示数量用）
	* @return
	*/
	public Integer getTaskNum() {
		return taskNum;
	}

	/** 
	* 任务总数（显示数量用）
	* @param taskNum
	*/
	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	/** 
	* 附件总数（显示数量用）
	* @return
	*/
	public Integer getFileNum() {
		return fileNum;
	}

	/** 
	* 附件总数（显示数量用）
	* @param fileNum
	*/
	public void setFileNum(Integer fileNum) {
		this.fileNum = fileNum;
	}
}
