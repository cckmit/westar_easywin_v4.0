package com.westar.base.model;

import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.FlowRecord;
import com.westar.base.pojo.RelateModeVo;

/** 
 * 任务看板
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Task {
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
	* 任务父节点ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 任务名称
	*/
	@Filed
	private String taskName;
	/** 
	* 任务详情
	*/
	@Filed
	private String taskRemark;
	/** 
	* 完成时限
	*/
	@Filed
	private String dealTimeLimit;
	/** 
	* 任务负责人
	*/
	@Filed
	private Integer owner;
	/** 
	* 任务创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 任务进度
	*/
	@Filed
	private Integer taskProgress;
	/** 
	* 任务状态(1，进行中；3，挂起中；4，完成。)
	*/
	@Filed
	private Integer state;
	/** 
	* 删除标识 0未删除，1预删除
	*/
	@Filed
	private Integer delState;
	/** 
	* 重要级别 见字典grade
	*/
	@Filed
	private String grade;
	/** 
	* 关联模块主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 关联模块类型
	*/
	@Filed
	private String busType;
	/** 
	* 任务类型见字典表：taskType 默认1常规任务
	*/
	@Filed
	private String taskType;
	/** 
	* 完成时间
	*/
	@Filed
	private String finishTime;
	/** 
	* 新版本的任务标记
	*/
	@Filed
	private String version;
	/** 
	* 任务办理时限小时数
	*/
	@Filed
	private String expectTime;

	/****************以上主要为系统表字段********************/
	/** 
	* 模块权限
	*/
	private List<ModuleOperateConfig> optCfgs;
	private String ownerName;
	/** 
	* 任务执行人集合
	*/
	private List<TaskExecutor> listTaskExecutor;
	/** 
	* 参与人集合
	*/
	private List<TaskSharer> listTaskSharer;
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
	private List<Task> listSonTask;
	/** 
	* 母任务名称
	*/
	private String pTaskName;
	/** 
	* 节点标识
	*/
	private String stepTag;
	/** 
	* 是否子任务一起标记
	*/
	private String childAlsoRemark;
	/** 
	* 任务参与人JSON字符串
	*/
	private String taskSharerJson;
	/** 
	* 讨论统计
	*/
	private Integer talkSum;
	/** 
	* 子项目数
	*/
	private Integer sonTaskNum;
	/** 
	* 关联模块名称
	*/
	private String busName;
	/** 
	* 关联模块状态
	*/
	private String busState;
	/** 
	* 关联的模块删除状态
	*/
	private Integer busDelState;
	private String executorName;
	/** 
	* 父任务
	*/
	private Task ptask;
	/** 
	* 任务推动人主键
	*/
	private Integer pushUserId;
	/** 
	* 是否为任务执行人
	*/
	private Integer isExecute;
	/** 
	* 当前人员执行状态
	*/
	private Integer executeState;
	/** 
	* 当前办理人员的工作进度
	*/
	private String executeProgress;
	/** 
	* 执行人附件名称
	*/
	private String executorFileName;
	/** 
	* 执行人附件UUID
	*/
	private String executorUuid;
	/** 
	* 执行人0女1男
	*/
	private String executorGender;
	/** 
	* 任务协同说明
	*/
	private String cooperateExplain;
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
	private Integer stagedItemId;
	/** 
	* 项目阶段名称
	*/
	private String stagedItemName;
	/** 
	* 记录起始索引位置
	*/
	private int offset;
	/** 
	* 第一个新建任务页面的附件
	*/
	private List<Upfiles> listUpfilesOfFirstStep;
	/** 
	* 是否查看过0没有
	*/
	private String isRead;
	/** 
	* 经办人
	*/
	private Integer operator;
	/** 
	* 经办人名称
	*/
	private String operatorName;
	/** 
	* 经办开始时间
	*/
	private String operatStartDate;
	/** 
	* 经办结束时间
	*/
	private String operatEndDate;
	/** 
	* 经办人（不含当前执行人）
	*/
	private String containExecutor;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 关注状态0未关注1已关注
	*/
	private String attentionState;
	/** 
	* 紧急度名称
	*/
	private String gradeName;
	/** 
	* 执行人类型0自己1下属
	*/
	private String execuType;
	/** 
	* 任务更新说明
	*/
	private String content;
	/** 
	* 普通任务总和
	*/
	private Integer ptNum;
	/** 
	* 重要任务总和
	*/
	private Integer zyNum;
	/** 
	* 紧急任务总和
	*/
	private Integer jjNum;
	/** 
	* 重要且紧急任务总和
	*/
	private Integer zyqjjNum;
	/** 
	* 完成任务总和
	*/
	private Integer wjNum;
	/** 
	* 超期任务总和
	*/
	private Integer cqNum;
	/** 
	* 个人所有的待办任务
	*/
	private Integer toDoNum;
	/** 
	* 下属执行总和
	*/
	private Integer subToDoNum;
	/** 
	* 逾期任务
	*/
	private Integer overdueNum;
	/** 
	* 关注任务
	*/
	private Integer attenNum;
	/** 
	* 经办任务
	*/
	private Integer operateNum;
	/** 
	* 负责的任务
	*/
	private Integer chargeNum;
	/** 
	* 任务总和
	*/
	private Integer allTaskNum;
	/** 
	* 办理时限（不是任务总的办理时限）
	*/
	private String handTimeLimit;
	/** 
	* 上传附件名称数组
	*/
	private String[] filesName;
	/** 
	* 任务留言集合
	*/
	private List<TaskTalk> listTaskTalk;
	/** 
	* 任务文档集合
	*/
	private List<TaskUpfile> listTaskUpfile;
	/** 
	* 任务协作日志
	*/
	private List<FlowRecord> listFlowRecord;
	/** 
	* 任务浏览记录集合
	*/
	private List<ViewRecord> listViewRecord;
	/** 
	* 任务日志集合
	*/
	private List<TaskLog> listTaskLog;
	/** 
	* 任务发布类型标识
	*/
	private String addType;
	/** 
	* 系统字典表对象
	*/
	private DataDic dataDic;
	/** 
	* 是否逾期标识
	*/
	private Boolean overdue;
	/** 
	* 逾期等级1已逾期2三天内到期3未到期
	*/
	private String overDueLevel;
	/** 
	* 列表排序规则
	*/
	private String orderBy;
	/** 
	* 统计类别
	*/
	private String countType;
	/** 
	* 统计类别名称
	*/
	private String countTypeName;
	/** 
	* 统计数量
	*/
	private int counts;
	/** 
	* 任务执行人
	*/
	private Integer executor;
	/** 
	* 执行人
	*/
	private List<UserInfo> listExecutor;
	/** 
	* 负责人
	*/
	private List<UserInfo> listOwner;
	private List<UserInfo> listOperator;
	/** 
	* 执行部门筛选
	*/
	private List<Department> listExecuteDep;
	/** 
	* 任务执行用时（单位：毫秒）
	*/
	private Long usedTimes;
	/** 
	* 任务执行超时（单位：毫秒）
	*/
	private Long overTimes;
	/** 
	* 结束时间
	*/
	private String endTime;
	private Integer fromUser;
	private String fromUserName;
	/** 
	* 推送人附件名称
	*/
	private String fromUserFileName;
	/** 
	* 推送人附件UUID
	*/
	private String fromUserUuid;
	/** 
	* 推送人0女1男
	*/
	private String fromUserGender;
	private String handTime;
	/** 
	* 推送人集合
	*/
	private List<UserInfo> listFromUser;
	/** 
	* 关联模块类型
	*/
	private String relateModType;
	/** 
	* 关联模块集合
	*/
	private List<RelateModeVo> listRelateModes;
	/** 
	* 统计任务留言数
	*/
	private Integer msgNum;
	/** 
	* 统计任务文档数
	*/
	private Integer docNum;
	/** 
	* 新增任务数
	*/
	private Integer newTaskNum;
	/** 
	* 完成任务数
	*/
	private Integer finishTaskNum;
	/** 
	* 任务实际执行时间
	*/
	private String executeTime;
	/** 
	* 剩余时间
	*/
	private String remainingTime;
	/** 
	* 任务实际开始时间
	*/
	private String curStartTime;

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
	* 任务父节点ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 任务父节点ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 任务名称
	* @param taskName
	*/
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/** 
	* 任务名称
	* @return
	*/
	public String getTaskName() {
		return taskName;
	}

	/** 
	* 任务详情
	* @param taskRemark
	*/
	public void setTaskRemark(String taskRemark) {
		this.taskRemark = taskRemark;
	}

	/** 
	* 任务详情
	* @return
	*/
	public String getTaskRemark() {
		return taskRemark;
	}

	/** 
	* 完成时限
	* @param dealTimeLimit
	*/
	public void setDealTimeLimit(String dealTimeLimit) {
		this.dealTimeLimit = dealTimeLimit;
	}

	/** 
	* 完成时限
	* @return
	*/
	public String getDealTimeLimit() {
		return dealTimeLimit;
	}

	/** 
	* 任务负责人
	* @param owner
	*/
	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	/** 
	* 任务负责人
	* @return
	*/
	public Integer getOwner() {
		return owner;
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
	public List<TaskSharer> getListTaskSharer() {
		return listTaskSharer;
	}

	/** 
	* 参与人集合
	* @param listTaskSharer
	*/
	public void setListTaskSharer(List<TaskSharer> listTaskSharer) {
		this.listTaskSharer = listTaskSharer;
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
	* 任务创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 任务创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 任务进度
	* @param taskProgress
	*/
	public void setTaskProgress(Integer taskProgress) {
		this.taskProgress = taskProgress;
	}

	/** 
	* 任务进度
	* @return
	*/
	public Integer getTaskProgress() {
		return taskProgress;
	}

	/** 
	* 任务状态(1，进行中；3，挂起中；4，完成。)
	* @param state
	*/
	public void setState(Integer state) {
		this.state = state;
	}

	/** 
	* 任务状态(1，进行中；3，挂起中；4，完成。)
	* @return
	*/
	public Integer getState() {
		return state;
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
	public List<Task> getListSonTask() {
		return listSonTask;
	}

	/** 
	* 子任务集合
	* @param listSonTask
	*/
	public void setListSonTask(List<Task> listSonTask) {
		this.listSonTask = listSonTask;
	}

	public String getpTaskName() {
		return pTaskName;
	}

	public void setpTaskName(String pTaskName) {
		this.pTaskName = pTaskName;
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
	public String getTaskSharerJson() {
		return taskSharerJson;
	}

	/** 
	* 任务参与人JSON字符串
	* @param taskSharerJson
	*/
	public void setTaskSharerJson(String taskSharerJson) {
		this.taskSharerJson = taskSharerJson;
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
	*/
	void publicStringgetTaskprogressDescribe() {
	}

	/** 
	*/
	void publicvoidsetTaskprogressDescribe() {
	}

	/** 
	* 子项目数
	* @return
	*/
	public Integer getSonTaskNum() {
		return sonTaskNum;
	}

	/** 
	* 子项目数
	* @param sonTaskNum
	*/
	public void setSonTaskNum(Integer sonTaskNum) {
		this.sonTaskNum = sonTaskNum;
	}

	/** 
	* 关联模块名称
	* @return
	*/
	public String getBusName() {
		return busName;
	}

	/** 
	* 关联模块名称
	* @param busName
	*/
	public void setBusName(String busName) {
		this.busName = busName;
	}

	/** 
	* 任务执行人
	* @param executor
	*/
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	/** 
	* 任务执行人
	* @return
	*/
	public Integer getExecutor() {
		return executor;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	/** 
	* 执行人附件名称
	* @return
	*/
	public String getExecutorFileName() {
		return executorFileName;
	}

	/** 
	* 执行人附件名称
	* @param executorFileName
	*/
	public void setExecutorFileName(String executorFileName) {
		this.executorFileName = executorFileName;
	}

	/** 
	* 执行人附件UUID
	* @return
	*/
	public String getExecutorUuid() {
		return executorUuid;
	}

	/** 
	* 执行人附件UUID
	* @param executorUuid
	*/
	public void setExecutorUuid(String executorUuid) {
		this.executorUuid = executorUuid;
	}

	/** 
	* 执行人0女1男
	* @return
	*/
	public String getExecutorGender() {
		return executorGender;
	}

	/** 
	* 执行人0女1男
	* @param executorGender
	*/
	public void setExecutorGender(String executorGender) {
		this.executorGender = executorGender;
	}

	/** 
	* 任务协同说明
	* @return
	*/
	public String getCooperateExplain() {
		return cooperateExplain;
	}

	/** 
	* 任务协同说明
	* @param cooperateExplain
	*/
	public void setCooperateExplain(String cooperateExplain) {
		this.cooperateExplain = cooperateExplain;
	}

	/** 
	* 经办人
	* @return
	*/
	public Integer getOperator() {
		return operator;
	}

	/** 
	* 经办人
	* @param operator
	*/
	public void setOperator(Integer operator) {
		this.operator = operator;
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
	public Integer getStagedItemId() {
		return stagedItemId;
	}

	/** 
	* 项目阶段主键
	* @param stagedItemId
	*/
	public void setStagedItemId(Integer stagedItemId) {
		this.stagedItemId = stagedItemId;
	}

	/** 
	* 记录起始索引位置
	* @return
	*/
	public int getOffset() {
		return offset;
	}

	/** 
	* 记录起始索引位置
	* @param offset
	*/
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/** 
	* 项目阶段名称
	* @return
	*/
	public String getStagedItemName() {
		return stagedItemName;
	}

	/** 
	* 项目阶段名称
	* @param stagedItemName
	*/
	public void setStagedItemName(String stagedItemName) {
		this.stagedItemName = stagedItemName;
	}

	/** 
	* 第一个新建任务页面的附件
	* @return
	*/
	public List<Upfiles> getListUpfilesOfFirstStep() {
		return listUpfilesOfFirstStep;
	}

	/** 
	* 第一个新建任务页面的附件
	* @param listUpfilesOfFirstStep
	*/
	public void setListUpfilesOfFirstStep(List<Upfiles> listUpfilesOfFirstStep) {
		this.listUpfilesOfFirstStep = listUpfilesOfFirstStep;
	}

	/** 
	* 是否查看过0没有
	* @return
	*/
	public String getIsRead() {
		return isRead;
	}

	/** 
	* 是否查看过0没有
	* @param isRead
	*/
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	/** 
	* 经办人名称
	* @return
	*/
	public String getOperatorName() {
		return operatorName;
	}

	/** 
	* 经办人名称
	* @param operatorName
	*/
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/** 
	* 关联模块状态
	* @return
	*/
	public String getBusState() {
		return busState;
	}

	/** 
	* 关联模块状态
	* @param busState
	*/
	public void setBusState(String busState) {
		this.busState = busState;
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
	* 关联的模块删除状态
	* @return
	*/
	public Integer getBusDelState() {
		return busDelState;
	}

	/** 
	* 关联的模块删除状态
	* @param busDelState
	*/
	public void setBusDelState(Integer busDelState) {
		this.busDelState = busDelState;
	}

	/** 
	* 重要级别 见字典grade
	* @param grade
	*/
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/** 
	* 重要级别 见字典grade
	* @return
	*/
	public String getGrade() {
		return grade;
	}

	/** 
	* 紧急度名称
	* @return
	*/
	public String getGradeName() {
		return gradeName;
	}

	/** 
	* 执行人类型0自己1下属
	* @return
	*/
	public String getExecuType() {
		return execuType;
	}

	/** 
	* 紧急度名称
	* @param gradeName
	*/
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	/** 
	* 执行人类型0自己1下属
	* @param execuType
	*/
	public void setExecuType(String execuType) {
		this.execuType = execuType;
	}

	/** 
	* 任务更新说明
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 任务更新说明
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 普通任务总和
	* @return
	*/
	public Integer getPtNum() {
		return ptNum;
	}

	/** 
	* 普通任务总和
	* @param ptNum
	*/
	public void setPtNum(Integer ptNum) {
		this.ptNum = ptNum;
	}

	/** 
	* 重要任务总和
	* @return
	*/
	public Integer getZyNum() {
		return zyNum;
	}

	/** 
	* 重要任务总和
	* @param zyNum
	*/
	public void setZyNum(Integer zyNum) {
		this.zyNum = zyNum;
	}

	/** 
	* 紧急任务总和
	* @return
	*/
	public Integer getJjNum() {
		return jjNum;
	}

	/** 
	* 紧急任务总和
	* @param jjNum
	*/
	public void setJjNum(Integer jjNum) {
		this.jjNum = jjNum;
	}

	/** 
	* 重要且紧急任务总和
	* @return
	*/
	public Integer getZyqjjNum() {
		return zyqjjNum;
	}

	/** 
	* 重要且紧急任务总和
	* @param zyqjjNum
	*/
	public void setZyqjjNum(Integer zyqjjNum) {
		this.zyqjjNum = zyqjjNum;
	}

	/** 
	* 完成任务总和
	* @return
	*/
	public Integer getWjNum() {
		return wjNum;
	}

	/** 
	* 完成任务总和
	* @param wjNum
	*/
	public void setWjNum(Integer wjNum) {
		this.wjNum = wjNum;
	}

	/** 
	* 超期任务总和
	* @return
	*/
	public Integer getCqNum() {
		return cqNum;
	}

	/** 
	* 超期任务总和
	* @param cqNum
	*/
	public void setCqNum(Integer cqNum) {
		this.cqNum = cqNum;
	}

	/** 
	* 关联模块主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 关联模块主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 关联模块类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 关联模块类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 办理时限（不是任务总的办理时限）
	* @return
	*/
	public String getHandTimeLimit() {
		return handTimeLimit;
	}

	/** 
	* 办理时限（不是任务总的办理时限）
	* @param handTimeLimit
	*/
	public void setHandTimeLimit(String handTimeLimit) {
		this.handTimeLimit = handTimeLimit;
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
	* 任务留言集合
	* @return
	*/
	public List<TaskTalk> getListTaskTalk() {
		return listTaskTalk;
	}

	/** 
	* 任务留言集合
	* @param listTaskTalk
	*/
	public void setListTaskTalk(List<TaskTalk> listTaskTalk) {
		this.listTaskTalk = listTaskTalk;
	}

	/** 
	* 任务文档集合
	* @return
	*/
	public List<TaskUpfile> getListTaskUpfile() {
		return listTaskUpfile;
	}

	/** 
	* 任务文档集合
	* @param listTaskUpfile
	*/
	public void setListTaskUpfile(List<TaskUpfile> listTaskUpfile) {
		this.listTaskUpfile = listTaskUpfile;
	}

	/** 
	* 任务协作日志
	* @return
	*/
	public List<FlowRecord> getListFlowRecord() {
		return listFlowRecord;
	}

	/** 
	* 任务协作日志
	* @param listFlowRecord
	*/
	public void setListFlowRecord(List<FlowRecord> listFlowRecord) {
		this.listFlowRecord = listFlowRecord;
	}

	/** 
	* 任务浏览记录集合
	* @return
	*/
	public List<ViewRecord> getListViewRecord() {
		return listViewRecord;
	}

	/** 
	* 任务浏览记录集合
	* @param listViewRecord
	*/
	public void setListViewRecord(List<ViewRecord> listViewRecord) {
		this.listViewRecord = listViewRecord;
	}

	/** 
	* 任务日志集合
	* @return
	*/
	public List<TaskLog> getListTaskLog() {
		return listTaskLog;
	}

	/** 
	* 任务日志集合
	* @param listTaskLog
	*/
	public void setListTaskLog(List<TaskLog> listTaskLog) {
		this.listTaskLog = listTaskLog;
	}

	/** 
	* 任务发布类型标识
	* @return
	*/
	public String getAddType() {
		return addType;
	}

	/** 
	* 任务发布类型标识
	* @param addType
	*/
	public void setAddType(String addType) {
		this.addType = addType;
	}

	/** 
	* 系统字典表对象
	* @return
	*/
	public DataDic getDataDic() {
		return dataDic;
	}

	/** 
	* 系统字典表对象
	* @param dataDic
	*/
	public void setDataDic(DataDic dataDic) {
		this.dataDic = dataDic;
	}

	/** 
	* 个人所有的待办任务
	* @return
	*/
	public Integer getToDoNum() {
		return toDoNum;
	}

	/** 
	* 个人所有的待办任务
	* @param toDoNum
	*/
	public void setToDoNum(Integer toDoNum) {
		this.toDoNum = toDoNum;
	}

	/** 
	* 下属执行总和
	* @return
	*/
	public Integer getSubToDoNum() {
		return subToDoNum;
	}

	/** 
	* 下属执行总和
	* @param subToDoNum
	*/
	public void setSubToDoNum(Integer subToDoNum) {
		this.subToDoNum = subToDoNum;
	}

	/** 
	* 逾期任务
	* @return
	*/
	public Integer getOverdueNum() {
		return overdueNum;
	}

	/** 
	* 逾期任务
	* @param overdueNum
	*/
	public void setOverdueNum(Integer overdueNum) {
		this.overdueNum = overdueNum;
	}

	/** 
	* 关注任务
	* @return
	*/
	public Integer getAttenNum() {
		return attenNum;
	}

	/** 
	* 关注任务
	* @param attenNum
	*/
	public void setAttenNum(Integer attenNum) {
		this.attenNum = attenNum;
	}

	/** 
	* 经办任务
	* @return
	*/
	public Integer getOperateNum() {
		return operateNum;
	}

	/** 
	* 经办任务
	* @param operateNum
	*/
	public void setOperateNum(Integer operateNum) {
		this.operateNum = operateNum;
	}

	/** 
	* 负责的任务
	* @return
	*/
	public Integer getChargeNum() {
		return chargeNum;
	}

	/** 
	* 负责的任务
	* @param chargeNum
	*/
	public void setChargeNum(Integer chargeNum) {
		this.chargeNum = chargeNum;
	}

	/** 
	* 任务总和
	* @return
	*/
	public Integer getAllTaskNum() {
		return allTaskNum;
	}

	/** 
	* 任务总和
	* @param allTaskNum
	*/
	public void setAllTaskNum(Integer allTaskNum) {
		this.allTaskNum = allTaskNum;
	}

	/** 
	* 是否逾期标识
	* @return
	*/
	public Boolean getOverdue() {
		return overdue;
	}

	/** 
	* 是否逾期标识
	* @param overdue
	*/
	public void setOverdue(Boolean overdue) {
		this.overdue = overdue;
	}

	/** 
	* 列表排序规则
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 列表排序规则
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/** 
	* 统计类别
	* @return
	*/
	public String getCountType() {
		return countType;
	}

	/** 
	* 统计类别
	* @param countType
	*/
	public void setCountType(String countType) {
		this.countType = countType;
	}

	/** 
	* 统计类别名称
	* @return
	*/
	public String getCountTypeName() {
		return countTypeName;
	}

	/** 
	* 统计类别名称
	* @param countTypeName
	*/
	public void setCountTypeName(String countTypeName) {
		this.countTypeName = countTypeName;
	}

	/** 
	* 统计数量
	* @return
	*/
	public int getCounts() {
		return counts;
	}

	/** 
	* 统计数量
	* @param counts
	*/
	public void setCounts(int counts) {
		this.counts = counts;
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
	* 父任务
	* @return
	*/
	public Task getPtask() {
		return ptask;
	}

	/** 
	* 父任务
	* @param ptask
	*/
	public void setPtask(Task ptask) {
		this.ptask = ptask;
	}

	/** 
	* 执行人
	* @return
	*/
	public List<UserInfo> getListExecutor() {
		return listExecutor;
	}

	/** 
	* 执行人
	* @param listExecutor
	*/
	public void setListExecutor(List<UserInfo> listExecutor) {
		this.listExecutor = listExecutor;
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

	public List<UserInfo> getListOperator() {
		return listOperator;
	}

	public void setListOperator(List<UserInfo> listOperator) {
		this.listOperator = listOperator;
	}

	/** 
	* 执行部门筛选
	* @return
	*/
	public List<Department> getListExecuteDep() {
		return listExecuteDep;
	}

	/** 
	* 执行部门筛选
	* @param listExecuteDep
	*/
	public void setListExecuteDep(List<Department> listExecuteDep) {
		this.listExecuteDep = listExecuteDep;
	}

	/** 
	* 任务执行用时（单位：毫秒）
	* @return
	*/
	public Long getUsedTimes() {
		return usedTimes;
	}

	/** 
	* 任务执行用时（单位：毫秒）
	* @param usedTimes
	*/
	public void setUsedTimes(Long usedTimes) {
		this.usedTimes = usedTimes;
	}

	/** 
	* 任务执行超时（单位：毫秒）
	* @return
	*/
	public Long getOverTimes() {
		return overTimes;
	}

	/** 
	* 任务执行超时（单位：毫秒）
	* @param overTimes
	*/
	public void setOverTimes(Long overTimes) {
		this.overTimes = overTimes;
	}

	/** 
	* 结束时间
	* @return
	*/
	public String getEndTime() {
		return endTime;
	}

	/** 
	* 结束时间
	* @param endTime
	*/
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	/** 
	* 推送人附件名称
	* @return
	*/
	public String getFromUserFileName() {
		return fromUserFileName;
	}

	/** 
	* 推送人附件名称
	* @param fromUserFileName
	*/
	public void setFromUserFileName(String fromUserFileName) {
		this.fromUserFileName = fromUserFileName;
	}

	/** 
	* 推送人附件UUID
	* @return
	*/
	public String getFromUserUuid() {
		return fromUserUuid;
	}

	/** 
	* 推送人附件UUID
	* @param fromUserUuid
	*/
	public void setFromUserUuid(String fromUserUuid) {
		this.fromUserUuid = fromUserUuid;
	}

	/** 
	* 推送人0女1男
	* @return
	*/
	public String getFromUserGender() {
		return fromUserGender;
	}

	/** 
	* 推送人0女1男
	* @param fromUserGender
	*/
	public void setFromUserGender(String fromUserGender) {
		this.fromUserGender = fromUserGender;
	}

	public String getHandTime() {
		return handTime;
	}

	public void setHandTime(String handTime) {
		this.handTime = handTime;
	}

	/** 
	* 推送人集合
	* @return
	*/
	public List<UserInfo> getListFromUser() {
		return listFromUser;
	}

	/** 
	* 推送人集合
	* @param listFromUser
	*/
	public void setListFromUser(List<UserInfo> listFromUser) {
		this.listFromUser = listFromUser;
	}

	public Integer getFromUser() {
		return fromUser;
	}

	public void setFromUser(Integer fromUser) {
		this.fromUser = fromUser;
	}

	/** 
	* 关联模块类型
	* @return
	*/
	public String getRelateModType() {
		return relateModType;
	}

	/** 
	* 关联模块类型
	* @param relateModType
	*/
	public void setRelateModType(String relateModType) {
		this.relateModType = relateModType;
	}

	/** 
	* 关联模块集合
	* @return
	*/
	public List<RelateModeVo> getListRelateModes() {
		return listRelateModes;
	}

	/** 
	* 关联模块集合
	* @param listRelateModes
	*/
	public void setListRelateModes(List<RelateModeVo> listRelateModes) {
		this.listRelateModes = listRelateModes;
	}

	/** 
	* 任务执行人集合
	* @return
	*/
	public List<TaskExecutor> getListTaskExecutor() {
		return listTaskExecutor;
	}

	/** 
	* 任务执行人集合
	* @param listTaskExecutor
	*/
	public void setListTaskExecutor(List<TaskExecutor> listTaskExecutor) {
		this.listTaskExecutor = listTaskExecutor;
	}

	/** 
	* 任务类型见字典表：taskType 默认1常规任务
	* @param taskType
	*/
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	/** 
	* 任务类型见字典表：taskType 默认1常规任务
	* @return
	*/
	public String getTaskType() {
		return taskType;
	}

	/** 
	* 当前人员执行状态
	* @return
	*/
	public Integer getExecuteState() {
		return executeState;
	}

	/** 
	* 统计任务留言数
	* @return
	*/
	public Integer getMsgNum() {
		return msgNum;
	}

	/** 
	* 统计任务留言数
	* @param msgNum
	*/
	public void setMsgNum(Integer msgNum) {
		this.msgNum = msgNum;
	}

	/** 
	* 统计任务文档数
	* @return
	*/
	public Integer getDocNum() {
		return docNum;
	}

	/** 
	* 统计任务文档数
	* @param docNum
	*/
	public void setDocNum(Integer docNum) {
		this.docNum = docNum;
	}

	/** 
	* 当前人员执行状态
	* @param executeState
	*/
	public void setExecuteState(Integer executeState) {
		this.executeState = executeState;
	}

	/** 
	* 当前办理人员的工作进度
	* @return
	*/
	public String getExecuteProgress() {
		return executeProgress;
	}

	/** 
	* 当前办理人员的工作进度
	* @param executeProgress
	*/
	public void setExecuteProgress(String executeProgress) {
		this.executeProgress = executeProgress;
	}

	/** 
	* 是否为任务执行人
	* @return
	*/
	public Integer getIsExecute() {
		return isExecute;
	}

	/** 
	* 是否为任务执行人
	* @param isExecute
	*/
	public void setIsExecute(Integer isExecute) {
		this.isExecute = isExecute;
	}

	/** 
	* 任务推动人主键
	* @return
	*/
	public Integer getPushUserId() {
		return pushUserId;
	}

	/** 
	* 任务推动人主键
	* @param pushUserId
	*/
	public void setPushUserId(Integer pushUserId) {
		this.pushUserId = pushUserId;
	}

	/** 
	* 经办人（不含当前执行人）
	* @return
	*/
	public String getContainExecutor() {
		return containExecutor;
	}

	/** 
	* 经办人（不含当前执行人）
	* @param containExecutor
	*/
	public void setContainExecutor(String containExecutor) {
		this.containExecutor = containExecutor;
	}

	@Override
	public String toString() {
		return "Task{" + "id=" + id + ", recordCreateTime='" + recordCreateTime + '\'' + ", comId=" + comId
				+ ", parentId=" + parentId + ", taskName='" + taskName + '\'' + ", taskRemark='" + taskRemark + '\''
				+ ", dealTimeLimit='" + dealTimeLimit + '\'' + ", owner=" + owner + ", creator=" + creator
				+ ", taskProgress=" + taskProgress + ", state=" + state + ", delState=" + delState + ", grade='" + grade
				+ '\'' + ", busId=" + busId + ", busType='" + busType + '\'' + ", taskType='" + taskType + '\''
				+ ", optCfgs=" + optCfgs + ", ownerName='" + ownerName + '\'' + ", listTaskExecutor=" + listTaskExecutor
				+ ", listTaskSharer=" + listTaskSharer + ", listUpfiles=" + listUpfiles + ", filename='" + filename
				+ '\'' + ", uuid='" + uuid + '\'' + ", gender='" + gender + '\'' + ", listSonTask=" + listSonTask
				+ ", pTaskName='" + pTaskName + '\'' + ", childAlsoRemark='" + childAlsoRemark + '\''
				+ ", taskSharerJson='" + taskSharerJson + '\'' + ", talkSum=" + talkSum + ", taskprogressDescribe='"
				+ '\'' + ", sonTaskNum=" + sonTaskNum + ", busName='" + busName + '\'' + ", busState='" + busState
				+ '\'' + ", busDelState=" + busDelState + ", executorName='" + executorName + '\'' + ", ptask=" + ptask
				+ ", pushUserId=" + pushUserId + ", isExecute=" + isExecute + ", executeState=" + executeState
				+ ", executeProgress='" + executeProgress + '\'' + ", executorFileName='" + executorFileName + '\''
				+ ", executorUuid='" + executorUuid + '\'' + ", executorGender='" + executorGender + '\''
				+ ", cooperateExplain='" + cooperateExplain + '\'' + ", succ=" + succ + ", promptMsg='" + promptMsg
				+ '\'' + ", shareMsg='" + shareMsg + '\'' + ", stagedItemId=" + stagedItemId + ", stagedItemName='"
				+ stagedItemName + '\'' + ", offset=" + offset + ", listUpfilesOfFirstStep=" + listUpfilesOfFirstStep
				+ ", isRead='" + isRead + '\'' + ", operator=" + operator + ", operatorName='" + operatorName + '\''
				+ ", containExecutor='" + containExecutor + '\'' + ", startDate='" + startDate + '\'' + ", endDate='"
				+ endDate + '\'' + ", attentionState='" + attentionState + '\'' + ", gradeName='" + gradeName + '\''
				+ ", execuType='" + execuType + '\'' + ", content='" + content + '\'' + ", ptNum=" + ptNum + ", zyNum="
				+ zyNum + ", jjNum=" + jjNum + ", zyqjjNum=" + zyqjjNum + ", wjNum=" + wjNum + ", cqNum=" + cqNum
				+ ", toDoNum=" + toDoNum + ", subToDoNum=" + subToDoNum + ", overdueNum=" + overdueNum + ", attenNum="
				+ attenNum + ", operateNum=" + operateNum + ", chargeNum=" + chargeNum + ", allTaskNum=" + allTaskNum
				+ ", handTimeLimit='" + handTimeLimit + '\'' + ", filesName=" + Arrays.toString(filesName)
				+ ", listTaskTalk=" + listTaskTalk + ", listTaskUpfile=" + listTaskUpfile + ", listFlowRecord="
				+ listFlowRecord + ", listViewRecord=" + listViewRecord + ", listTaskLog=" + listTaskLog + ", addType='"
				+ addType + '\'' + ", dataDic=" + dataDic + ", overdue=" + overdue + ", orderBy='" + orderBy + '\''
				+ ", countType='" + countType + '\'' + ", countTypeName='" + countTypeName + '\'' + ", counts=" + counts
				+ ", executor=" + executor + ", listExecutor=" + listExecutor + ", listOwner=" + listOwner
				+ ", listOperator=" + listOperator + ", listExecuteDep=" + listExecuteDep + ", usedTimes=" + usedTimes
				+ ", overTimes=" + overTimes + ", endTime='" + endTime + '\'' + ", fromUser=" + fromUser
				+ ", fromUserName='" + fromUserName + '\'' + ", fromUserFileName='" + fromUserFileName + '\''
				+ ", fromUserUuid='" + fromUserUuid + '\'' + ", fromUserGender='" + fromUserGender + '\''
				+ ", handTime='" + handTime + '\'' + ", listFromUser=" + listFromUser + ", relateModType='"
				+ relateModType + '\'' + ", listRelateModes=" + listRelateModes + ", msgNum=" + msgNum + ", docNum="
				+ docNum + '}';
	}

	/** 
	* 经办开始时间
	* @return
	*/
	public String getOperatStartDate() {
		return operatStartDate;
	}

	/** 
	* 经办开始时间
	* @param operatStartDate
	*/
	public void setOperatStartDate(String operatStartDate) {
		this.operatStartDate = operatStartDate;
	}

	/** 
	* 经办结束时间
	* @return
	*/
	public String getOperatEndDate() {
		return operatEndDate;
	}

	/** 
	* 经办结束时间
	* @param operatEndDate
	*/
	public void setOperatEndDate(String operatEndDate) {
		this.operatEndDate = operatEndDate;
	}

	/** 
	* 逾期等级1已逾期2三天内到期3未到期
	* @return
	*/
	public String getOverDueLevel() {
		return overDueLevel;
	}

	/** 
	* 逾期等级1已逾期2三天内到期3未到期
	* @param overDueLevel
	*/
	public void setOverDueLevel(String overDueLevel) {
		this.overDueLevel = overDueLevel;
	}

	/** 
	* 完成时间
	* @return
	*/
	public String getFinishTime() {
		return finishTime;
	}

	/** 
	* 完成时间
	* @param finishTime
	*/
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	/** 
	* 新增任务数
	* @return
	*/
	public Integer getNewTaskNum() {
		return newTaskNum;
	}

	/** 
	* 新增任务数
	* @param newTaskNum
	*/
	public void setNewTaskNum(Integer newTaskNum) {
		this.newTaskNum = newTaskNum;
	}

	/** 
	* 完成任务数
	* @return
	*/
	public Integer getFinishTaskNum() {
		return finishTaskNum;
	}

	/** 
	* 完成任务数
	* @param finishTaskNum
	*/
	public void setFinishTaskNum(Integer finishTaskNum) {
		this.finishTaskNum = finishTaskNum;
	}

	/** 
	* 新版本的任务标记
	* @param version
	*/
	public void setVersion(String version) {
		this.version = version;
	}

	/** 
	* 新版本的任务标记
	* @return
	*/
	public String getVersion() {
		return version;
	}

	/** 
	* 任务办理时限小时数
	* @param expectTime
	*/
	public void setExpectTime(String expectTime) {
		this.expectTime = expectTime;
	}

	/** 
	* 任务办理时限小时数
	* @return
	*/
	public String getExpectTime() {
		return expectTime;
	}

	/** 
	* 节点标识
	* @return
	*/
	public String getStepTag() {
		return stepTag;
	}

	/** 
	* 节点标识
	* @param stepTag
	*/
	public void setStepTag(String stepTag) {
		this.stepTag = stepTag;
	}

	/** 
	* 任务实际执行时间
	* @return
	*/
	public String getExecuteTime() {
		return executeTime;
	}

	/** 
	* 任务实际执行时间
	* @param executeTime
	*/
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	/** 
	* 剩余时间
	* @return
	*/
	public String getRemainingTime() {
		return remainingTime;
	}

	/** 
	* 剩余时间
	* @param remainingTime
	*/
	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}

	/** 
	* 任务实际开始时间
	* @return
	*/
	public String getCurStartTime() {
		return curStartTime;
	}

	/** 
	* 任务实际开始时间
	* @param curStartTime
	*/
	public void setCurStartTime(String curStartTime) {
		this.curStartTime = curStartTime;
	}
}
