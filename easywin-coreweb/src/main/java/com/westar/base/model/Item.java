package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 项目配置信息列表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Item {
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
	* 项目父节点ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 客户主键
	*/
	@Filed
	private Integer partnerId;
	/** 
	* 任务名称
	*/
	@Filed
	private String itemName;
	/** 
	* 任务详情
	*/
	@Filed
	private String itemRemark;
	/** 
	* 项目负责人
	*/
	@Filed
	private Integer owner;
	/** 
	* 项目创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 项目进度
	*/
	@Filed
	private Integer itemProgress;
	/** 
	* 项目状态(1，进行中；3，挂起中；4，完成。)
	*/
	@Filed
	private Integer state;
	/** 
	* 更新时间
	*/
	@Filed
	private String modifyDate;
	/** 
	* 删除标识 0未删除，1预删除
	*/
	@Filed
	private Integer delState;
	/** 
	* 公开私有 0私有  1公开 
	*/
	@Filed
	private Integer pubState;
	/** 
	* 所属产品
	*/
	@Filed
	private Integer productId;
	/** 
	* 研发负责人
	*/
	@Filed
	private Integer developLeader;
	/** 
	* 服务期限
	*/
	@Filed
	private String serviceDate;
	/** 
	* 金额（万）
	*/
	@Filed
	private String amount;

	/****************以上主要为系统表字段********************/
	private String ownerName;
	/** 
	* 参与人集合
	*/
	private List<ItemSharer> listItemSharer;
	/** 
	* 任务附件集合
	*/
	private List<Upfiles> listUpfiles;
	/** 
	* 附件名称
	*/
	private String filename;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 子任务集合
	*/
	private List<Item> listSonItem;
	/** 
	* 母任务名称
	*/
	private String pItemName;
	/** 
	* 是否子任务一起标记
	*/
	private String childAlsoRemark;
	/** 
	* 任务参与人JSON字符串
	*/
	private String itemSharerJson;
	/** 
	* 讨论统计
	*/
	private Integer talkSum;
	/** 
	* 任务进度描述
	*/
	private String itemProgressDescribe;
	/** 
	* 合作人名称
	*/
	private String partnerName;
	/** 
	* 子项目数
	*/
	private Integer sonItemNum;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 是否分享信息
	*/
	private String shareMsg;
	/** 
	* 项目阶段主键
	*/
	private int stagedItemId;
	/** 
	* 浏览状态0未读1已读
	*/
	private int readState;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 项目成员组
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
	* 更新内容
	*/
	private String modifyContent;
	/** 
	* 更新人主键
	*/
	private Integer modifier;
	/** 
	* 更新人名字
	*/
	private String modifierName;
	/** 
	* 更新人性别
	*/
	private String modifierGender;
	/** 
	* 更新人UUID
	*/
	private String modifierUuid;
	/** 
	* 更新人附件名
	*/
	private String modifierFileName;
	/** 
	* 上传附件名称数组
	*/
	private String[] filesName;
	/** 
	* 项目留言集合
	*/
	private List<ItemTalk> listItemTalk;
	/** 
	* 项目文档集合
	*/
	private List<ItemUpfile> listItemUpfile;
	/** 
	* 移交记录
	*/
	private List<FlowRecord> listFlowRecord;
	/** 
	* 浏览记录
	*/
	private List<ViewRecord> listViewRecord;
	/** 
	* 项目日志
	*/
	private List<ItemLog> listItemLog;
	/** 
	* 项目日志
	*/
	private List<Task> listItemTask;
	/** 
	* 添加方式类型
	*/
	private String addType;
	/** 
	* 所有项目数
	*/
	private Integer allItemNum;
	/** 
	* 我的项目项目数
	*/
	private Integer myItemNum;
	/** 
	* 下属项目数
	*/
	private Integer subItemNum;
	/** 
	* 关注目数
	*/
	private Integer attenItemNum;
	/** 
	* 移交项目数
	*/
	private Integer handsItemNum;
	/** 
	* 本月新增项目数
	*/
	private Integer addByMonthNum;
	/** 
	* 异步查询数据需要排除的
	*/
	private String itemIds;
	/** 
	* 模块权限
	*/
	private List<ModuleOperateConfig> optCfgs;
	/** 
	* 负责人
	*/
	private List<UserInfo> listOwner;
	/** 
	* 项目各阶段信息集合
	*/
	private List<ItemStagedInfo> listStagedItemInfo;
	/** 
	* 留言总数（显示数量用）
	*/
	private Integer talkNum;
	/** 
	* 附件总数（显示数量用）
	*/
	private Integer fileNum;
	/** 
	* 产品名称
	*/
	private String productName;
	/** 
	* 研发负责人姓名
	*/
	private String developLeaderName;
	/** 
	* 项目进度list
	*/
	private List<ItemProgress> listItemProgress;
	/** 
	* 销售材料总数（显示数量用）
	*/
	private Integer saleFileNum;
	/** 
	* 产品经理名称
	*/
	private String productManagerName;
	/** 
	* 维护记录总数
	*/
	private Integer maintenanceRecordNum;
	/** 
	* 查询条件的产品
	*/
	public List<Product> listProduct;

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
	* 项目父节点ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 项目父节点ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 客户主键
	* @param partnerId
	*/
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	/** 
	* 客户主键
	* @return
	*/
	public Integer getPartnerId() {
		return partnerId;
	}

	/** 
	* 任务名称
	* @param itemName
	*/
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/** 
	* 任务名称
	* @return
	*/
	public String getItemName() {
		return itemName;
	}

	/** 
	* 任务详情
	* @param itemRemark
	*/
	public void setItemRemark(String itemRemark) {
		this.itemRemark = itemRemark;
	}

	/** 
	* 任务详情
	* @return
	*/
	public String getItemRemark() {
		return itemRemark;
	}

	/** 
	* 项目负责人
	* @param owner
	*/
	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	/** 
	* 项目负责人
	* @return
	*/
	public Integer getOwner() {
		return owner;
	}

	/** 
	* 项目创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 项目创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 项目进度
	* @param itemProgress
	*/
	public void setItemProgress(Integer itemProgress) {
		this.itemProgress = itemProgress;
	}

	/** 
	* 项目进度
	* @return
	*/
	public Integer getItemProgress() {
		return itemProgress;
	}

	/** 
	* 项目状态(1，进行中；3，挂起中；4，完成。)
	* @param state
	*/
	public void setState(Integer state) {
		this.state = state;
	}

	/** 
	* 项目状态(1，进行中；3，挂起中；4，完成。)
	* @return
	*/
	public Integer getState() {
		return state;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/** 
	* 参与人集合
	* @return
	*/
	public List<ItemSharer> getListItemSharer() {
		return listItemSharer;
	}

	/** 
	* 参与人集合
	* @param listItemSharer
	*/
	public void setListItemSharer(List<ItemSharer> listItemSharer) {
		this.listItemSharer = listItemSharer;
	}

	/** 
	* 任务附件集合
	* @return
	*/
	public List<Upfiles> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 任务附件集合
	* @param listUpfiles
	*/
	public void setListUpfiles(List<Upfiles> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFilename() {
		return filename;
	}

	/** 
	* 附件名称
	* @param filename
	*/
	public void setFilename(String filename) {
		this.filename = filename;
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
	* 子任务集合
	* @return
	*/
	public List<Item> getListSonItem() {
		return listSonItem;
	}

	/** 
	* 子任务集合
	* @param listSonItem
	*/
	public void setListSonItem(List<Item> listSonItem) {
		this.listSonItem = listSonItem;
	}

	public String getpItemName() {
		return pItemName;
	}

	public void setpItemName(String pItemName) {
		this.pItemName = pItemName;
	}

	/** 
	* 是否子任务一起标记
	* @return
	*/
	public String getChildAlsoRemark() {
		return childAlsoRemark;
	}

	/** 
	* 是否子任务一起标记
	* @param childAlsoRemark
	*/
	public void setChildAlsoRemark(String childAlsoRemark) {
		this.childAlsoRemark = childAlsoRemark;
	}

	/** 
	* 任务参与人JSON字符串
	* @return
	*/
	public String getItemSharerJson() {
		return itemSharerJson;
	}

	/** 
	* 任务参与人JSON字符串
	* @param itemSharerJson
	*/
	public void setItemSharerJson(String itemSharerJson) {
		this.itemSharerJson = itemSharerJson;
	}

	/** 
	* 讨论统计
	* @return
	*/
	public Integer getTalkSum() {
		return talkSum;
	}

	/** 
	* 讨论统计
	* @param talkSum
	*/
	public void setTalkSum(Integer talkSum) {
		this.talkSum = talkSum;
	}

	/** 
	* 合作人名称
	* @return
	*/
	public String getPartnerName() {
		return partnerName;
	}

	/** 
	* 合作人名称
	* @param partnerName
	*/
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	/** 
	* 任务进度描述
	* @return
	*/
	public String getItemProgressDescribe() {
		if (null == itemProgress) {
			this.itemProgress = -1;
		}
		itemProgressDescribe = DataDicContext.getInstance().getCurrentPathZvalue("progress", itemProgress.toString());
		return itemProgressDescribe;
	}

	/** 
	* 任务进度描述
	* @param itemProgressDescribe
	*/
	public void setItemProgressDescribe(String itemProgressDescribe) {
		this.itemProgressDescribe = itemProgressDescribe;
	}

	/** 
	* 子项目数
	* @return
	*/
	public Integer getSonItemNum() {
		return sonItemNum;
	}

	/** 
	* 子项目数
	* @param sonItemNum
	*/
	public void setSonItemNum(Integer sonItemNum) {
		this.sonItemNum = sonItemNum;
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
	* 项目阶段主键
	* @return
	*/
	public int getStagedItemId() {
		return stagedItemId;
	}

	/** 
	* 项目阶段主键
	* @param stagedItemId
	*/
	public void setStagedItemId(int stagedItemId) {
		this.stagedItemId = stagedItemId;
	}

	/** 
	* 浏览状态0未读1已读
	* @return
	*/
	public int getReadState() {
		return readState;
	}

	/** 
	* 浏览状态0未读1已读
	* @param readState
	*/
	public void setReadState(int readState) {
		this.readState = readState;
	}

	/** 
	* 更新时间
	* @param modifyDate
	*/
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	/** 
	* 更新时间
	* @return
	*/
	public String getModifyDate() {
		return modifyDate;
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
	* 项目成员组
	* @return
	*/
	public List<SelfGroup> getListShareGroup() {
		return listShareGroup;
	}

	/** 
	* 项目成员组
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
	* 更新人主键
	* @return
	*/
	public Integer getModifier() {
		return modifier;
	}

	/** 
	* 更新人主键
	* @param modifier
	*/
	public void setModifier(Integer modifier) {
		this.modifier = modifier;
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
	* 更新人UUID
	* @return
	*/
	public String getModifierUuid() {
		return modifierUuid;
	}

	/** 
	* 更新人UUID
	* @param modifierUuid
	*/
	public void setModifierUuid(String modifierUuid) {
		this.modifierUuid = modifierUuid;
	}

	/** 
	* 更新人附件名
	* @return
	*/
	public String getModifierFileName() {
		return modifierFileName;
	}

	/** 
	* 更新人附件名
	* @param modifierFileName
	*/
	public void setModifierFileName(String modifierFileName) {
		this.modifierFileName = modifierFileName;
	}

	/** 
	* 更新内容
	* @return
	*/
	public String getModifyContent() {
		return modifyContent;
	}

	/** 
	* 更新内容
	* @param modifyContent
	*/
	public void setModifyContent(String modifyContent) {
		this.modifyContent = modifyContent;
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
	* 项目留言集合
	* @return
	*/
	public List<ItemTalk> getListItemTalk() {
		return listItemTalk;
	}

	/** 
	* 项目留言集合
	* @param listItemTalk
	*/
	public void setListItemTalk(List<ItemTalk> listItemTalk) {
		this.listItemTalk = listItemTalk;
	}

	/** 
	* 项目文档集合
	* @return
	*/
	public List<ItemUpfile> getListItemUpfile() {
		return listItemUpfile;
	}

	/** 
	* 项目文档集合
	* @param listItemUpfile
	*/
	public void setListItemUpfile(List<ItemUpfile> listItemUpfile) {
		this.listItemUpfile = listItemUpfile;
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
	* 项目日志
	* @return
	*/
	public List<ItemLog> getListItemLog() {
		return listItemLog;
	}

	/** 
	* 项目日志
	* @param listItemLog
	*/
	public void setListItemLog(List<ItemLog> listItemLog) {
		this.listItemLog = listItemLog;
	}

	/** 
	* 项目日志
	* @return
	*/
	public List<Task> getListItemTask() {
		return listItemTask;
	}

	/** 
	* 项目日志
	* @param listItemTask
	*/
	public void setListItemTask(List<Task> listItemTask) {
		this.listItemTask = listItemTask;
	}

	/** 
	* 添加方式类型
	* @return
	*/
	public String getAddType() {
		return addType;
	}

	/** 
	* 添加方式类型
	* @param addType
	*/
	public void setAddType(String addType) {
		this.addType = addType;
	}

	/** 
	* 所有项目数
	* @return
	*/
	public Integer getAllItemNum() {
		return allItemNum;
	}

	/** 
	* 所有项目数
	* @param allItemNum
	*/
	public void setAllItemNum(Integer allItemNum) {
		this.allItemNum = allItemNum;
	}

	/** 
	* 我的项目项目数
	* @return
	*/
	public Integer getMyItemNum() {
		return myItemNum;
	}

	/** 
	* 我的项目项目数
	* @param myItemNum
	*/
	public void setMyItemNum(Integer myItemNum) {
		this.myItemNum = myItemNum;
	}

	/** 
	* 下属项目数
	* @return
	*/
	public Integer getSubItemNum() {
		return subItemNum;
	}

	/** 
	* 下属项目数
	* @param subItemNum
	*/
	public void setSubItemNum(Integer subItemNum) {
		this.subItemNum = subItemNum;
	}

	/** 
	* 关注目数
	* @return
	*/
	public Integer getAttenItemNum() {
		return attenItemNum;
	}

	/** 
	* 关注目数
	* @param attenItemNum
	*/
	public void setAttenItemNum(Integer attenItemNum) {
		this.attenItemNum = attenItemNum;
	}

	/** 
	* 移交项目数
	* @return
	*/
	public Integer getHandsItemNum() {
		return handsItemNum;
	}

	/** 
	* 移交项目数
	* @param handsItemNum
	*/
	public void setHandsItemNum(Integer handsItemNum) {
		this.handsItemNum = handsItemNum;
	}

	/** 
	* 本月新增项目数
	* @return
	*/
	public Integer getAddByMonthNum() {
		return addByMonthNum;
	}

	/** 
	* 本月新增项目数
	* @param addByMonthNum
	*/
	public void setAddByMonthNum(Integer addByMonthNum) {
		this.addByMonthNum = addByMonthNum;
	}

	/** 
	* 异步查询数据需要排除的
	* @return
	*/
	public String getItemIds() {
		return itemIds;
	}

	/** 
	* 异步查询数据需要排除的
	* @param itemIds
	*/
	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	/** 
	* 模块权限
	* @return
	*/
	public List<ModuleOperateConfig> getOptCfgs() {
		return optCfgs;
	}

	/** 
	* 模块权限
	* @param optCfgs
	*/
	public void setOptCfgs(List<ModuleOperateConfig> optCfgs) {
		this.optCfgs = optCfgs;
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
	* 项目各阶段信息集合
	* @return
	*/
	public List<ItemStagedInfo> getListStagedItemInfo() {
		return listStagedItemInfo;
	}

	/** 
	* 项目各阶段信息集合
	* @param listStagedItemInfo
	*/
	public void setListStagedItemInfo(List<ItemStagedInfo> listStagedItemInfo) {
		this.listStagedItemInfo = listStagedItemInfo;
	}

	/** 
	* 公开私有 0私有  1公开 
	* @param pubState
	*/
	public void setPubState(Integer pubState) {
		this.pubState = pubState;
	}

	/** 
	* 公开私有 0私有  1公开 
	* @return
	*/
	public Integer getPubState() {
		return pubState;
	}

	/** 
	* 留言总数（显示数量用）
	* @return
	*/
	public Integer getTalkNum() {
		return talkNum;
	}

	/** 
	* 留言总数（显示数量用）
	* @param talkNum
	*/
	public void setTalkNum(Integer talkNum) {
		this.talkNum = talkNum;
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

	/** 
	* 所属产品
	* @param productId
	*/
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/** 
	* 所属产品
	* @return
	*/
	public Integer getProductId() {
		return productId;
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
	* 研发负责人
	* @param developLeader
	*/
	public void setDevelopLeader(Integer developLeader) {
		this.developLeader = developLeader;
	}

	/** 
	* 研发负责人
	* @return
	*/
	public Integer getDevelopLeader() {
		return developLeader;
	}

	/** 
	* 服务期限
	* @param serviceDate
	*/
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	/** 
	* 服务期限
	* @return
	*/
	public String getServiceDate() {
		return serviceDate;
	}

	/** 
	* 金额（万）
	* @param amount
	*/
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/** 
	* 金额（万）
	* @return
	*/
	public String getAmount() {
		return amount;
	}

	/** 
	* 研发负责人姓名
	* @return
	*/
	public String getDevelopLeaderName() {
		return developLeaderName;
	}

	/** 
	* 研发负责人姓名
	* @param developLeaderName
	*/
	public void setDevelopLeaderName(String developLeaderName) {
		this.developLeaderName = developLeaderName;
	}

	/** 
	* 项目进度list
	* @return
	*/
	public List<ItemProgress> getListItemProgress() {
		return listItemProgress;
	}

	/** 
	* 项目进度list
	* @param listItemProgress
	*/
	public void setListItemProgress(List<ItemProgress> listItemProgress) {
		this.listItemProgress = listItemProgress;
	}

	/** 
	* 销售材料总数（显示数量用）
	* @return
	*/
	public Integer getSaleFileNum() {
		return saleFileNum;
	}

	/** 
	* 销售材料总数（显示数量用）
	* @param saleFileNum
	*/
	public void setSaleFileNum(Integer saleFileNum) {
		this.saleFileNum = saleFileNum;
	}

	/** 
	* 产品经理名称
	* @return
	*/
	public String getProductManagerName() {
		return productManagerName;
	}

	/** 
	* 产品经理名称
	* @param productManagerName
	*/
	public void setProductManagerName(String productManagerName) {
		this.productManagerName = productManagerName;
	}

	/** 
	* 维护记录总数
	* @return
	*/
	public Integer getMaintenanceRecordNum() {
		return maintenanceRecordNum;
	}

	/** 
	* 维护记录总数
	* @param maintenanceRecordNum
	*/
	public void setMaintenanceRecordNum(Integer maintenanceRecordNum) {
		this.maintenanceRecordNum = maintenanceRecordNum;
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
}
