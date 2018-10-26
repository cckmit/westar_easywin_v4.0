package com.westar.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.Customer;
import com.westar.base.model.Item;
import com.westar.base.model.Meeting;
import com.westar.base.model.MsgShare;
import com.westar.base.model.Organic;
import com.westar.base.model.OrganicCfg;
import com.westar.base.model.OrganicSpaceCfg;
import com.westar.base.model.Question;
import com.westar.base.model.Schedule;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.Task;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.UserOrganic;
import com.westar.base.model.Vote;
import com.westar.base.model.WeekReport;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.OrganicDao;

@Service
public class OrganicService {
	private static final Log loger = LogFactory.getLog(OrganicService.class);
	@Autowired
	OrganicDao organicDao;
	@Autowired
	TaskService taskService;
	@Autowired
	ItemService itemService;
	@Autowired
	CrmService crmService;
	@Autowired
	WeekReportService weekReportService;
	@Autowired
	FileCenterService fileCenterService;
	@Autowired
	UploadService uploadService;
	@Autowired
	QasService qasService;
	@Autowired
	VoteService voteService;
	@Autowired
	MsgShareService msgShareService;
	@Autowired
	MeetingService meetingService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	WorkFlowService workFlowService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	OrderService orderService;
	@Autowired
	SystemLogService systemLogService;
	/**
	 * 查询企业信息
	 * @param orgNum
	 * @return
	 */
	public Organic getOrgInfo(Integer orgNum) {
		Organic organic = organicDao.getOrgInfo(orgNum);
		return organic;
	}

	/**
	 *  企业信息修改
	 * @param organic
	 */
	public void updateOrganic(Organic organic) {
		organicDao.update(organic);
		
	}
	/**
	 * 当前账号已加入的企业
	 * @param email
	 * @param passwordMD5 
	 * @return
	 */
	public List<Organic> listUserOrg(String email, String passwordMD5) {
		List<Organic> list = organicDao.listUserOrg(email,passwordMD5);
		return list;
	}
	/**
	 * 企业添加LOGO
	 * @param upfiles
	 * @return
	 */
	public Integer addFile(Upfiles upfiles) {
		return organicDao.add(upfiles);
	}
	
	/**
	 * 新注册企业用户邮箱验证
	 * @param email
	 * @return
	 */
	public Organic orgAccountCheck(String email){
		return organicDao.orgAccountCheck(email);
	}

	/**
	 * 删除企业所有信息
	 * @param organic
	 */
	public void delOrgAllInfo(Integer comId) {
		//TODO　解散团队需要删除团队所有数据
		//删除回收箱数据
		organicDao.delBatchByField("recycleBin", new String[]{"comId"}, new Object[]{comId});
		//删除常用意见数据
		organicDao.delBatchByField("usagIdea", new String[]{"comId"}, new Object[]{comId});
		//删除个人直属上司数据
		organicDao.delBatchByField("immediateSuper", new String[]{"comId"}, new Object[]{comId});
		//删除积分历史数据
		organicDao.delBatchByField("jifenHistory", new String[]{"comId"}, new Object[]{comId});
		//删除业务更新表
		organicDao.delBatchByField("busUpdate", new String[]{"comId"}, new Object[]{comId});
		
		//删除文档部门查看范围数据
		organicDao.delBatchByField("fileViewScopeDep", new String[]{"comId"}, new Object[]{comId});
		//删除文档查看范围数据
		organicDao.delBatchByField("fileViewScope", new String[]{"comId"}, new Object[]{comId});
		//删除文档夹数据
		organicDao.delBatchByField("fileClassify", new String[]{"comId"}, new Object[]{comId});
		//删除文档数据
		organicDao.delBatchByField("fileDetail", new String[]{"comId"}, new Object[]{comId});
		
		Set<String> doneTableSet = new HashSet<String>();
		
		//提问模块删除数据表
		this.delOrgAllInfoByModBase("question",comId,doneTableSet);
		//周报模块删除数据表
		this.delOrgAllInfoByModBase("weekReport",comId,doneTableSet);
		//周报模块删除数据表
		this.delOrgAllInfoByModBase("weekReportMod",comId,doneTableSet);
		//投票模块删除数据表
		this.delOrgAllInfoByModBase("vote",comId,doneTableSet);
		
		//删除加入的激活记录数据
		organicDao.delBatchByField("joinRecord", new String[]{"comId"}, new Object[]{comId});
		
		//删除业务模块配置数据
		organicDao.delBatchByField("moduleOperateConfig", new String[]{"comId"}, new Object[]{comId});
		//删除强制参与人列表数据
		organicDao.delBatchByField("forceInPersion", new String[]{"comId"}, new Object[]{comId});
		
		//客户模块删除数据表
		this.delOrgAllInfoByModBase("customer",comId,doneTableSet);
		//删除客户类型数据
		organicDao.delBatchByField("customerType", new String[]{"comId"}, new Object[]{comId});
		//删除客户阶段数据
		organicDao.delBatchByField("customerStage", new String[]{"comId"}, new Object[]{comId});
		//删除区域表数据
		organicDao.delBatchByField("area", new String[]{"comId"}, new Object[]{comId});
		
		//项目模块删除数据表
		this.delOrgAllInfoByModBase("item",comId,doneTableSet);
		
		//任务模块删除数据表
		this.delOrgAllInfoByModBase("task",comId,doneTableSet);
		
		//分享模块删除数据表
		this.delOrgAllInfoByModBase("msgShare",comId,doneTableSet);
		
		//删除聊天人员
		organicDao.delBatchByField("chatUser", new String[]{"comId"}, new Object[]{comId});
		//删除聊天内容
		organicDao.delBatchByField("chatMsg", new String[]{"comId"}, new Object[]{comId});
		//删除聊天室
		organicDao.delBatchByField("chatRoom", new String[]{"comId"}, new Object[]{comId});
		
		//删除聊天人员
		organicDao.delBatchByField("chatsNoRead", new String[]{"comId"}, new Object[]{comId});
		//删除聊天内容
		organicDao.delBatchByField("chatsMsg", new String[]{"comId"}, new Object[]{comId});
		//删除聊天分组人员
		organicDao.delBatchByField("chatsGrpUser", new String[]{"comId"}, new Object[]{comId});
		//删除聊天分组
		organicDao.delBatchByField("chatsGrp", new String[]{"comId"}, new Object[]{comId});
		//删除聊天室
		organicDao.delBatchByField("chatsRoom", new String[]{"comId"}, new Object[]{comId});
		
		//日程模块删除数据表
		this.delOrgAllInfoByModBase("schedule",comId,doneTableSet);
		
		//会议模块删除数据表
		this.delOrgAllInfoByModBase("meeting",comId,doneTableSet);
		
		//删除会议室申请
		organicDao.delBatchByField("roomApply", new String[]{"comId"}, new Object[]{comId});
		//删除会议室
		organicDao.delBatchByField("meetingRoom", new String[]{"comId"}, new Object[]{comId});
		
		//删除用品申领详情
		organicDao.delBatchByField("bgypApplyDetail", new String[]{"comId"}, new Object[]{comId});
		//删除用品申领
		organicDao.delBatchByField("bgypApply", new String[]{"comId"}, new Object[]{comId});
		//删除采购清单附件
		organicDao.delBatchByField("bgypPurFile", new String[]{"comId"}, new Object[]{comId});
		//删除用品采购清单
		organicDao.delBatchByField("bgypPurDetail", new String[]{"comId"}, new Object[]{comId});
		//删除用品采购清单总
		organicDao.delBatchByField("bgypPurOrder", new String[]{"comId"}, new Object[]{comId});
		//删除办公用品条目
		organicDao.delBatchByField("bgypItem", new String[]{"comId"}, new Object[]{comId});
		//删除办公用品分类表
		organicDao.delBatchByField("bgypFl", new String[]{"comId"}, new Object[]{comId});
		
		//删除人事奖惩附件
		organicDao.delBatchByField("rsdaIncFile", new String[]{"comId"}, new Object[]{comId});
		//删除人事档案奖惩
		organicDao.delBatchByField("rsdaIncentive", new String[]{"comId"}, new Object[]{comId});
		//删除工作经历附件
		organicDao.delBatchByField("jobHisFile", new String[]{"comId"}, new Object[]{comId});
		//删除人事档案其他
		organicDao.delBatchByField("rsdaOther", new String[]{"comId"}, new Object[]{comId});
		//删除人事档案附件
		organicDao.delBatchByField("rsdaBaseFile", new String[]{"comId"}, new Object[]{comId});
		
		
		
		
		//删除模块管理员
		organicDao.delBatchByField("modAdmin", new String[]{"comId"}, new Object[]{comId});
		
		//删除菜单使用频率
		organicDao.delBatchByField("menuRate", new String[]{"comId"}, new Object[]{comId});
		//删除个人菜单设置
		organicDao.delBatchByField("menuSet", new String[]{"comId"}, new Object[]{comId});
		//删除个人首页栏目设置
		organicDao.delBatchByField("menuHomeSet", new String[]{"comId"}, new Object[]{comId});
		
		
		//删除系统日志表数据
		organicDao.delBatchByField("systemLog", new String[]{"comId"}, new Object[]{comId});
		
		//删除组成员数据
		organicDao.delBatchByField("usedUser", new String[]{"comId"}, new Object[]{comId});
		//删除上次操作的分组数据
		organicDao.delBatchByField("usedGroup", new String[]{"comId"}, new Object[]{comId});
		//删除组成员数据
		organicDao.delBatchByField("groupPersion", new String[]{"comId"}, new Object[]{comId});
		//删除个人分组表数据
		organicDao.delBatchByField("selfGroup", new String[]{"comId"}, new Object[]{comId});
		//删除数据更新记录列表数据
		organicDao.delBatchByField("todayWorks", new String[]{"comId"}, new Object[]{comId});
		//删除系统菜单数据
		organicDao.delBatchByField("menu", new String[]{"comId"}, new Object[]{comId});
		
		
		//删除附件是否从数据库下载的信息数据
		organicDao.delBatchByField("webeditorFileDataState", new String[]{"comId"}, new Object[]{comId});
		//删除在线编辑器的附件数据
		organicDao.delBatchByField("webeditorFileData", new String[]{"comId"}, new Object[]{comId});
		//删除附件是否从数据库下载的信息数据
		organicDao.delBatchByField("fileDataState", new String[]{"comId"}, new Object[]{comId});
		//删除附件文件信息数据
		organicDao.delBatchByField("fileData", new String[]{"comId"}, new Object[]{comId});
		//删除附件文件内容数据
		organicDao.delBatchByField("filecontent", new String[]{"comId"}, new Object[]{comId});
		//删除附件评论数据
		organicDao.delBatchByField("fileTalk", new String[]{"comId"}, new Object[]{comId});
		
		
		//删除查看记录息表数据
		organicDao.delBatchByField("viewRecord", new String[]{"comId"}, new Object[]{comId});
		//删除关注表数据
		organicDao.delBatchByField("attention", new String[]{"comId"}, new Object[]{comId});
		//删除模块最新动态表数据
		organicDao.delBatchByField("newsInfo", new String[]{"comId"}, new Object[]{comId});
		
		//定时提醒模块删除数据表
		this.delOrgAllInfoByModBase("clock",comId,doneTableSet);
		
		
		
		//删除审批附件
		organicDao.delByField("spFlowUpfile", new String[]{"comId"}, new Object[]{comId});
		//删除流程表单多数据
		organicDao.delByField("spFlowOptData", new String[]{"comId"}, new Object[]{comId});
		//删除流程表单数据
		organicDao.delByField("spFlowInputData", new String[]{"comId"}, new Object[]{comId});
		//删除流程实例步骤间关系表
		organicDao.delByField("spFlowHiStepRelation", new String[]{"comId"}, new Object[]{comId});
		//删除流程实例步骤
		organicDao.delByField("spFlowHiStep", new String[]{"comId"}, new Object[]{comId});
		//删除流程历史审批人
		organicDao.delByField("spFlowHiExecutor", new String[]{"comId"}, new Object[]{comId});
		//删除流程实例对象步骤授权
		organicDao.delByField("spFlowRunStepFormControl", new String[]{"comId"}, new Object[]{comId});
		//删除流程变量主键
		organicDao.delByField("spFlowRunVariableKey", new String[]{"comId"}, new Object[]{comId});
		//删除流程当前审批人
		organicDao.delByField("spFlowCurExecutor", new String[]{"comId"}, new Object[]{comId});
		
		//删除 业务更新 -
		organicDao.delByField("busUpdate", new String[]{"comId"}, new Object[]{comId});
		//删除 表单数据与业务数据映射关系
		organicDao.delByField("busAttrMapFormColTemp", new String[]{"comId"}, new Object[]{comId});
		//删除 表单数据与业务数据映射关系
		organicDao.delByField("busAttrMapFormCol", new String[]{"comId"}, new Object[]{comId});
		//删除 数据映射关系权限控制表（以部门为单位）
		organicDao.delByField("busMapFlowAuthDep", new String[]{"comId"}, new Object[]{comId});
		//删除 模块操作与审批流程之间关联
		organicDao.delByField("busMapFlow", new String[]{"comId"}, new Object[]{comId});
		
		//费用模块删除数据表
		this.delOrgAllInfoByModBase("feebudget",comId,doneTableSet);
		
		//删除请假申请记录表
		organicDao.delByField("leave", new String[]{"comId"}, new Object[]{comId});
		//删除加班申请记录表
		organicDao.delByField("overTime", new String[]{"comId"}, new Object[]{comId});
				
		//删除流程实例化对象表
		organicDao.delByField("spFlowInstance", new String[]{"comId"}, new Object[]{comId});
		
		//删除流程使用次数记录表
		organicDao.delByField("spFlowUsedTimes", new String[]{"comId"}, new Object[]{comId});
		//删除步骤条件描述
		organicDao.delByField("spStepConditions", new String[]{"comId"}, new Object[]{comId});
		//删除步骤权限控制
		organicDao.delByField("spStepFormControl", new String[]{"comId"}, new Object[]{comId});
		//删除流程步骤间关系表
		organicDao.delByField("spFlowStepRelation", new String[]{"comId"}, new Object[]{comId});
		//删除流程步骤审批人
		organicDao.delByField("spFlowStepExecutor", new String[]{"comId"}, new Object[]{comId});
		//删除流程步骤
		organicDao.delByField("spFlowStep", new String[]{"comId"}, new Object[]{comId});
		//删除通过人员控制流程范发起权限
		organicDao.delByField("spFlowScopeByUser", new String[]{"comId"}, new Object[]{comId});
		//删除通过部门控制流程范发起权限
		organicDao.delByField("spFlowScopeByDep", new String[]{"comId"}, new Object[]{comId});
		//删除流程定义表
		organicDao.delByField("spFlowModel", new String[]{"comId"}, new Object[]{comId});
		//删除流程分类表
		organicDao.delByField("spFlowType", new String[]{"comId"}, new Object[]{comId});
		//删除表单组件当前标识
		organicDao.delByField("formComponMaxField", new String[]{"comId"}, new Object[]{comId});
		
		//费用模块删除数据表
		this.delOrgAllInfoByModBase("formMod",comId,doneTableSet);
		
		//删除表单模板归类
		organicDao.delByField("formModSort", new String[]{"comId"}, new Object[]{comId});
		
		
		//删除工作时段设定
		organicDao.delByField("attenceTime", new String[]{"comId"}, new Object[]{comId});
		//删除工作日设定
		organicDao.delByField("attenceWeek", new String[]{"comId"}, new Object[]{comId});
		//删除工作日类型
		organicDao.delByField("attenceType", new String[]{"comId"}, new Object[]{comId});
		//删除考勤规则设定
		organicDao.delByField("attenceRule", new String[]{"comId"}, new Object[]{comId});
		
		//删除GPS位置
		organicDao.delByField("pointGPS", new String[]{"comId"}, new Object[]{comId});
		
		//删除GPS位置
		organicDao.delByField("userConf", new String[]{"comId"}, new Object[]{comId});
		//删除手机短信
		organicDao.delByField("phoneMsg", new String[]{"comId"}, new Object[]{comId});
		
		//删除节假日日期
		organicDao.delByField("festModDate", new String[]{"comId"}, new Object[]{comId});
		//删除定时提醒
		organicDao.delByField("festMod", new String[]{"comId"}, new Object[]{comId});
		
		//删除考勤人员
		organicDao.delByField("attenceUser", new String[]{"comId"}, new Object[]{comId});
		//删除考勤记录
		organicDao.delByField("attenceRecord", new String[]{"comId"}, new Object[]{comId});
		//删除考勤记录上传记录
		organicDao.delByField("attenceRecordUpload", new String[]{"comId"}, new Object[]{comId});
		//删除考勤机连接配置
		organicDao.delByField("attenceConnSet", new String[]{"comId"}, new Object[]{comId});
		
		//删除团队升级类型订单配置表
		organicDao.delByField("orgUpgrade", new String[]{"comId"}, new Object[]{comId});
		//删团队使用空间配置表
		organicDao.delByField("organicSpaceCfg", new String[]{"comId"}, new Object[]{comId});
		//删除交易订单信息
		organicDao.delByField("orders", new String[]{"comId"}, new Object[]{comId});
		//删除模块管理员
		organicDao.delByField("modAdmin", new String[]{"comId"}, new Object[]{comId});
		
		//删除用品申领详情
		organicDao.delByField("bgypApplyDetail", new String[]{"comId"}, new Object[]{comId});
		//删除用品采购清单附件
		organicDao.delByField("bgypPurFile", new String[]{"comId"}, new Object[]{comId});
		//删除用品用品申领
		organicDao.delByField("bgypApply", new String[]{"comId"}, new Object[]{comId});
		//删除用品采购清单"
		organicDao.delByField("bgypPurDetail", new String[]{"comId"}, new Object[]{comId});
		//删除用品采购清单总
		organicDao.delByField("bgypPurOrder", new String[]{"comId"}, new Object[]{comId});
		//删除办公用品条目
		organicDao.delByField("bgypItem", new String[]{"comId"}, new Object[]{comId});
		//删除办公用品分类表
		organicDao.delByField("bgypFl", new String[]{"comId"}, new Object[]{comId});
		
		//删除办人事档案附件
		organicDao.delByField("rsdaBaseFile", new String[]{"comId"}, new Object[]{comId});
		//删除办复职附件
		organicDao.delByField("rsdaResumeFile", new String[]{"comId"}, new Object[]{comId});
		//删除复职管理
		organicDao.delByField("rsdaResume", new String[]{"comId"}, new Object[]{comId});
		//删除离职附件
		organicDao.delByField("rsdaLeaveFile", new String[]{"comId"}, new Object[]{comId});
		//删除离职管理
		organicDao.delByField("rsdaLeave", new String[]{"comId"}, new Object[]{comId});
		//删除人事调动附件
		organicDao.delByField("rsdaTranceFile", new String[]{"comId"}, new Object[]{comId});
		//删除人事调动
		organicDao.delByField("rsdaTrance", new String[]{"comId"}, new Object[]{comId});
		//删除人事奖惩附件
		organicDao.delByField("rsdaIncFile", new String[]{"comId"}, new Object[]{comId});
		//删除人事奖惩
		organicDao.delByField("rsdaIncentive", new String[]{"comId"}, new Object[]{comId});
		//删除工作经历附件
		organicDao.delByField("jobHisFile", new String[]{"comId"}, new Object[]{comId});
		//删除工作经历
		//organicDao.delByField("jobHistory", new String[]{"comId"}, new Object[]{comId});
		//删除人事档案其他
		organicDao.delByField("rsdaOther", new String[]{"comId"}, new Object[]{comId});
		//删除人事档案基本信息
		//organicDao.delByField("rsdaBase", new String[]{"comId"}, new Object[]{comId});
		
		//费用模块删除数据表
		this.delOrgAllInfoByModBase("car",comId,doneTableSet);
		//车辆基本信息 
		organicDao.delByField("carType", new String[]{"comId"}, new Object[]{comId});
		
		//删除固定资产附件表 
		organicDao.delByField("gdzcUpfile", new String[]{"comId"}, new Object[]{comId});
		//删除 固定资产维护记录表
		organicDao.delByField("gdzcMaintainRecord", new String[]{"comId"}, new Object[]{comId});
		//删除 固定资产维护记录表
		organicDao.delByField("gdzcReduceRecord", new String[]{"comId"}, new Object[]{comId});
		//删除 固定资产维护记录表
		organicDao.delByField("gdzcType", new String[]{"comId"}, new Object[]{comId});
		//删除模块引导
		organicDao.delByField("intro", new String[]{"comId"}, new Object[]{comId});
		
		//删除公告 附件 
		organicDao.delByField("announUpfile", new String[]{"comId"}, new Object[]{comId});
		//删除公告 附件 
		organicDao.delByField("talkUpfile", new String[]{"comId"}, new Object[]{comId});
		//删除公告 附件 
		organicDao.delByField("talk", new String[]{"comId"}, new Object[]{comId});
		//删除日志统一 -
		organicDao.delByField("logs", new String[]{"comId"}, new Object[]{comId});
		//删除公告日志
		organicDao.delByField("announLog", new String[]{"comId"}, new Object[]{comId});
		//删除公告公告个人查看权限
		organicDao.delByField("announScopeByUser", new String[]{"comId"}, new Object[]{comId});
		//删除公告公告个人查看权限
		organicDao.delByField("announScopeByDep", new String[]{"comId"}, new Object[]{comId});
		//删除公告
		organicDao.delByField("announcement", new String[]{"comId"}, new Object[]{comId});
		
		//删除制度个人查看权限
		organicDao.delByField("instituScopeByUser", new String[]{"comId"}, new Object[]{comId});
		//删除通过部门控制对制度查看权限
		organicDao.delByField("instituScopeByDep", new String[]{"comId"}, new Object[]{comId});
		//删除制度管理
		organicDao.delByField("institution", new String[]{"comId"}, new Object[]{comId});
		//删除制度类型
		organicDao.delByField("instituType", new String[]{"comId"}, new Object[]{comId});
		
		//删除积分评定
		organicDao.delByField("jfScore", new String[]{"comId"}, new Object[]{comId});
		//删除积分下属范围
		organicDao.delByField("jfSubUserScope", new String[]{"comId"}, new Object[]{comId});
		//删除积分评定模块
		organicDao.delByField("jfMod", new String[]{"comId"}, new Object[]{comId});
		//删除积分标准范围
		organicDao.delByField("jfzbDepScope", new String[]{"comId"}, new Object[]{comId});
		//删除积分标准范围
		organicDao.delByField("jfzb", new String[]{"comId"}, new Object[]{comId});
		//删除积分评定指标 
		organicDao.delByField("jfzbType", new String[]{"comId"}, new Object[]{comId});
		
	
		//删除附件信息数据
		organicDao.delBatchByField("upfiles", new String[]{"comId"}, new Object[]{comId});
		//删除部门信息数据
		organicDao.delBatchByField("department", new String[]{"comId"}, new Object[]{comId});
		//删除企业信息表数据
		organicDao.delBatchByField("organic", new String[]{"orgNum"}, new Object[]{comId});
		//删除企业企业配置数据
		organicDao.delBatchByField("organicCfg", new String[]{"comId"}, new Object[]{comId});
		//删除企业人员表数据
		organicDao.delBatchByField("userOrganic", new String[]{"comId"}, new Object[]{comId});
	}
	
	/**
	 * 按模块删除数据
	 * @param tableName
	 * @param comId
	 * @param doneTableSet 
	 */
	private void delOrgAllInfoByModBase(String tableName, Integer comId, Set<String> doneTableSet) {
		if(doneTableSet.contains(tableName.toLowerCase())){
			return;
		}
		doneTableSet.add(tableName.toLowerCase());
		
		List<JSONObject> list = organicDao.listTableInfo(tableName.toUpperCase());
		if(null != list && !list.isEmpty()){
			for (JSONObject jsonObject : list) {
				String subTableName = jsonObject.getString("TABLENAME");
				this.delOrgAllInfoByModBase(subTableName,comId,doneTableSet);
			}
		}
		//删除企业人员表数据
		organicDao.delBatchByField(tableName, new String[]{"comId"}, new Object[]{comId});	
		
	}

	/**
	 * 团队索引重建
	 * @param userInfo
	 * @throws Exception 
	 */
	public void indexRebuild(UserInfo userInfo) throws Exception{
		loger.info("任务中心索引***************************开始**********************");
		//任务中心索引
		List<Task> listTaskOfAll = taskService.listTaskOfAll(userInfo);
		if(null!=listTaskOfAll && !listTaskOfAll.isEmpty()){
			for(Task vo:listTaskOfAll){
				taskService.updateTaskIndex(vo.getId(), userInfo,"add");
			}
		}
		loger.info("项目中心索引***************************开始**********************");
		//项目中心索引
		//验证当前登录人是否是督察人员
		List<Item> listItemOfAll = itemService.listItemOfAll(userInfo);
		if(null!=listItemOfAll && !listItemOfAll.isEmpty()){
			for(Item vo:listItemOfAll){
				itemService.updateItemIndex(vo.getId(), userInfo, "add");
			}
		}
		loger.info("客户中心索引***************************开始**********************");
		//客户中心索引
		//是否是督察人员验证
	    List<Customer> listCustomerOfAll = crmService.listCrmOfAll(userInfo);
	    if(null!=listCustomerOfAll && !listCustomerOfAll.isEmpty()){
			for(Customer vo:listCustomerOfAll){
				crmService.updateCustomerIndex(vo.getId(), userInfo,"add");
			}
		}
		loger.info("周报中心索引***************************开始**********************");
		//周报中心索引
	    List<WeekReport> listWeekReportOfAll = weekReportService.listWeekReportOfAll(userInfo);
	    if(null!=listWeekReportOfAll && !listWeekReportOfAll.isEmpty()){
			for(WeekReport vo:listWeekReportOfAll){
				weekReportService.updateWeekReportIndex(vo.getId(), userInfo, "add");
			}
		}
		loger.info("文档中心索引***************************开始**********************");
		//文档中心索引
	    List<Upfiles> listUpfilesOfAll = fileCenterService.listUpfilesOfAll(userInfo);
	    if(null!=listUpfilesOfAll && !listUpfilesOfAll.isEmpty()){
			for(Upfiles vo:listUpfilesOfAll){
				uploadService.updateUpfileIndex(vo.getId(), userInfo,"add",vo.getBusId(),vo.getBusType());
			}
		}
		loger.info("问答中心索引***************************开始**********************");
		//问答中心索引
	    List<Question> listQasOfAll = qasService.listQasOfAll(userInfo);
	    if(null!=listQasOfAll && !listQasOfAll.isEmpty()){
			for(Question vo:listQasOfAll){
				qasService.updateQasIndex(vo.getId(), userInfo,"add");
			}
		}
		loger.info("投票中心索引***************************开始**********************");
		//投票中心索引
	    List<Vote> listVoteOfAll = voteService.listVoteOfAll(userInfo);
	    if(null!=listVoteOfAll && !listVoteOfAll.isEmpty()){
			for(Vote vo:listVoteOfAll){
				voteService.updateVoteIndex(vo.getId(), userInfo, "add");
			}
		}
		loger.info("分享信息索引***************************开始**********************");
		//分享信息索引
	    List<MsgShare> listMsgShareOfAll = msgShareService.listMsgShareOfAll(userInfo);
	    if(null!=listMsgShareOfAll && !listMsgShareOfAll.isEmpty()){
			for(MsgShare vo:listMsgShareOfAll){
				msgShareService.updateMsgIndex(vo.getId(), userInfo,"add");
			}
		}
		loger.info("会议中心索引***************************开始**********************");
		//会议中心索引
	    List<Meeting> listMeetingOfAll = meetingService.listMeetingOfAll(userInfo);
	    if(null!=listMeetingOfAll && !listMeetingOfAll.isEmpty()){
			for(Meeting vo:listMeetingOfAll){
				meetingService.updateMeetIndex(vo.getId(), userInfo, "add");
			}
		}
		loger.info("日程中心索引***************************开始**********************");
		//日程中心索引
	    List<Schedule> listScheduleOfAll = scheduleService.listScheduleOfAll(userInfo);
		if(null!=listScheduleOfAll && !listScheduleOfAll.isEmpty()){
			for(Schedule vo:listScheduleOfAll){
				scheduleService.updateScheIndex(vo.getId(), userInfo, "add");
			}
		}
		loger.info("审批中心索引***************************开始**********************");
		//获取所有有效的审批流程
		List<SpFlowInstance> listSpFlowInstanceOfOrg = workFlowService.listSpFlowInstanceOfOrg(userInfo);
	    if(null!=listSpFlowInstanceOfOrg && !listSpFlowInstanceOfOrg.isEmpty()){
			for(SpFlowInstance vo:listSpFlowInstanceOfOrg){
				workFlowService.updateSpFlowInstanceIndex(vo.getId(), userInfo,"add");
			}
		}
		loger.info("***************************团队索引重建结束**********************");
	}

	/**
	 * 验证当前操作人员是否是超级管理员
	 * @param curUser
	 * @return
	 */
	public boolean isAdministrator(UserInfo curUser) {
		UserInfo administrator = organicDao.isAdministrator(curUser);
		if(null==administrator){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 执行验证操作后，删除验证码
	 * @param account 帐号关键字
	 */
	public void delPassYzm(String account) {
		organicDao.delByField("passYzm", new String[]{"account"}, new Object[]{account.toLowerCase()});//根据关键字删除验证码
	}

	/**
	 * 取得查询企业名称
	 * @param account 账号
	 * @param searchName 需查询的名称
	 * @return
	 */
	public List<Organic> listSearchOrg(String account, String searchName) {
		return organicDao.listSearchOrg(account,searchName);
	}
	/**
	 * 验证企业是否存在
	 * 
	 * @param orgNum
	 * @return
	 */
	public Organic getOrganicByNum(Integer orgNum) {
		return organicDao.getOrganicByNum(orgNum);
	}

	/**
	 * 查询该账号的所加入的所有团队
	 * @param account
	 * @return
	 */
	public List<Organic> listUserAllOrg(String account) {
		return organicDao.listUserAllOrg(account);
	}

	/**
	 * 取得团队配置信息
	 * @param comId 团队号
	 * @param cfgType 
	 * @return
	 */
	public OrganicCfg getOrganicCfg(Integer comId, String cfgType) {
		return organicDao.getOrganicCfg(comId,cfgType);
	}

	/**
	 * 修改团队配置
	 * @param sessionUser 当前操作员
	 * @param organicCfg 配置信息
	 */
	public void updateOrgCfg(UserInfo sessionUser, OrganicCfg organicCfg) {
		organicDao.updateOrgCfg(sessionUser,organicCfg);
		
	}

	/**
	 * 取得团队的配置信息
	 * @param comId
	 * @return
	 */
	public List<OrganicCfg> listOrgCfg(Integer comId) {
		return organicDao.listOrgCfg(comId);
	}
	/**
	 * 分页查询所有企业信息
	 * @param organic
	 * @return
	 */
	public List<Organic> listPagedOrganic(Organic organic){
		return organicDao.listPagedOranic(organic);
		
	}
	public List<Organic> listAllOrganic(){
		return organicDao.listAllOrganic();
		
	}

	/**
	 * 检测、更新团队服务信息
	 */
	public void updateOrganicService() {
		List<Organic> listOrg=organicDao.listOrgOutOfService();//获取使用人数超过限制的团队
		if(null!=listOrg && !listOrg.isEmpty()){
			for (Organic organic : listOrg) {
				loger.info("超出服务期团队："+organic.getOrgName());
				organicDao.updateOrgMemberToUseless(organic.getOrgNum());//根据加入团队时间降序禁用到平台默认允许使用人数
			}
		}
	}
	
	/**
	 * 根据加入团队时间降序禁用到平台默认允许使用人数
	 * @param comId 团队主键
	 */
	public void updateOrgMemberToUseless(Integer comId){
		Organic org = organicDao.orgOutOfService(comId);
		if(null!=org){
			organicDao.updateOrgMemberToUseless(comId);//根据加入团队时间降序禁用到平台默认允许使用人数
		}
	}

	/**
	 * 核实现团队可激活人数信息
	 * @param comId 团队主键
	 * @param enabled 激活状态标识符
	 * @param inService 是否在服务中标识符
	 * @return
	 */
	public Organic checkOrgUsersInService(Integer comId,Integer enabled,Integer inService) {
		Organic organic = organicDao.getOrgInfo(comId);
		organic.setEnabled(enabled.toString());
		organic.setInService(inService);
		organic.setCanDo(false);//默认不能激活
		if(null!=organic){
			if(organic.getMembers()<organic.getUsersUpperLimit()){
				organic.setCanDo(true);
			}else{
				if(enabled==ConstantInterface.ENABLED_YES 
						&& inService==ConstantInterface.USER_INSERVICE_NO){//激活用户的服务状态
					UserInfo upperLimitUser= userInfoService.queryOrgUsersUpperLimitUser(comId);
					if(null!=upperLimitUser){
						organic.setCanDo(true);
						organic.setMsg("激活此账号，则需要禁用用户“"+upperLimitUser.getUserName()+"”；是否继续？");
					}
				}else{
					organic.setMsg("团队可使用人数已达到上限；请升级团队使用人数上限。");
				}
			}
		}
		return organic;
	}

	/**
	 * 获取团队购买服务信息
	 * @param comId
	 * @return
	 */
	public OrganicSpaceCfg getOrganicSpaceCfgInfo(Integer comId) {
		OrganicSpaceCfg organicSpaceCfg = organicDao.getOrganicSpaceCfgInfo(comId);
		Integer orgBalanceMoney = orderService.countOrgBalanceMoney(comId);//团队结余
		organicSpaceCfg.setOrgBalanceMoney(orgBalanceMoney);
		return organicSpaceCfg;
	}
	
	/**
	 * 统计团队下人员启用状态为启用的人员数
	 * @param comId 团队主键
	 * @return
	 */
	public Integer countOrgEnabledUsersNum(Integer comId){
		Organic org = organicDao.countOrgEnabledUsersNum(comId);
		if(null!=org){
			return org.getMembers();
		}
		return 0;
	}
	
	/**
	 * 激活团队成员InService为1
	 * @param orgNum 团队主键
	 * @param usersNum 可以使用人数范围
	 */
	public void updateOrgMemberToInService(Integer orgNum,Integer usersNum){
		organicDao.updateOrgMemberToInService(orgNum, usersNum);
	}

	/**
	 * 管理权限移交
	 * @param userInfo 当前操作人员
	 * @param transferUserId 被移交的人员
	 * @param transferDes 移交描述
	 */
	public void updateOrgAdmin(UserInfo userInfo, Integer transferUserId, String transferDes) {
		//原来的权限去掉，默认为普通人员
		UserOrganic currentUser = userInfoService.getUserOrganic(userInfo.getId(),userInfo.getComId());
		currentUser.setAdmin("0");
		userInfoService.updateUserOrganic(currentUser);
		//将移交对象的权限设置为系统管理员
		UserOrganic transferUser = userInfoService.getUserOrganic(transferUserId,userInfo.getComId());
		transferUser.setAdmin("1");
		userInfoService.updateUserOrganic(transferUser);

		//系统日志
		UserInfo userT = userInfoService.getUserInfo(userInfo.getComId(),transferUserId);
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "[" + userInfo.getUserName() + "]将团队移交给[" + userT.getUserName() + "]，移交说明：[" + transferDes + "]",
				ConstantInterface.TYPE_ORG, userInfo.getComId(),userInfo.getOptIP());
		
	}
}
