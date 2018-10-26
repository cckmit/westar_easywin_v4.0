package com.westar.base.util;

/**
 * 常量类接口
 * 业务常量定义规则 ：业务类型标识_*
 */
public interface ConstantInterface {
	
	//平台登录标注
	final static String TYPE_LOGIN="001";
	//平台注销标注
	final static String TYPE_EXIT="002";
	//平台任务标注
	final static String TYPE_TASK="003";
	//平台任务讨论标注
	final static String TYPE_TASKTALK="00301";
	//平台任务删除标注
	final static String TYPE_TASKDEL="00302";
	//平台任务委托标注
	final static String TYPE_TASKDONE="00303";
	//平台任务办结标注
	final static String TYPE_TASKFINISH="00304";
	//平台下属逾期任务标注
	final static String TYPE_SUBTASK="00305";
	//平台任务报延申请
	final static String TYPE_DELAYAPPLY="00306";
	//平台发起投票标注
	final static String TYPE_VOTE="004";
	//平台投票讨论标注
	final static String TYPE_VOTETALK="00401";
	//平台投票标注
	final static String TYPE_VOTECHOOSE="00402";
	//平台投票删除标注
	final static String TYPE_VOTEDEL="00403";
	//平台项目标注
	final static String TYPE_ITEM="005";
	//平台项目讨论标注
	final static String TYPE_ITEMTALK="00501";
	//平台项目删除标注
	final static String TYPE_ITEMDEL="00502";
	//平台项目阶段标注
	final static String TYPE_ITEMSTAGE="00503";
	//平台周报标注
	final static String TYPE_WEEK="006";
	//平台反馈留言标注
	final static String TYPE_WEEKTALK="00601";
	//平台部门操作标注
	final static String TYPE_DEP="007";
	//平台用户管理标注
	final static String TYPE_USER="008";
	//平台用户管理基本信息标注
	final static String TYPE_USERINFO="00801";
	//平台用户管理头像信息标注
	final static String TYPE_USERHEAD="00802";
	//平台用户管理联系信息标注
	final static String TYPE_USERTEL="00803";
	//平台用户管理邀请用户标注
	final static String TYPE_USERINV="00804";
	//平台用户管理创建分组标注
	final static String TYPE_USERGRP="00805";
	//平台用户管理删除分组标注
	final static String TYPE_USERGRPDEL="00806";
	//平台企业标注
	final static String TYPE_ORG="009";
	//平台菜单标注
	final static String TYPE_MENU="010";
	//平台问标注
	final static String TYPE_QUES="011";
	//平台答标注
	final static String TYPE_ANS="01101";
	//平台问答讨论标注
	final static String TYPE_QASTALK="01102";
	//平台答删除标注
	final static String TYPE_ANSDEL="01103";
	//平台答删除标注
	final static String TYPE_QUESDEL="01104";
	//平台客户中心标注
	final static String TYPE_CRM="012";
	//平台客户中心讨论标注
	final static String TYPE_CRMTALK="01201";
	//平台客户中心讨论删除标注
	final static String TYPE_CRMTALKDEL="01202";
	//平台客户中心删除标注
	final static String TYPE_CRMDEL="01203";
	//平台文档中心标注
	final static String TYPE_FILE="013";
	//平台评论删除
	final static String TYPE_TALKDEL="014";

	//平台任务讨论标注
	final static String TYPE_SHARETALK="101";
	//平台任务删除标注
	final static String TYPE_SHARETALKDEL="102";
	
	//平台申请键入
	final static String TYPE_APPLY="015";
	//平台日程
	final static String TYPE_SCHEDULE="016";
	//平台会议
	final static String TYPE_MEETING="017";
	//平台周期会议
	final static String TYPE_MEETINGREG="01701";
	//平台会议
	final static String TYPE_MEETROOM="018";
	//平台通知消息
	final static String TYPE_NOTICE="019";
	//待办事项
	final static String TYPE_WORK="020";
	//关注
	final static String TYPE_ATTEN="021";
	//流程审批业务
	final static String TYPE_FLOW_SP="022";
	//流程审批完结消息告知业务
	final static String TYPE_SP_END ="02201";
	final static String TYPE_WORK_FLOW ="02202";
	//审批留言
	final static String TYPE_FLOW_SPTALK ="02203";
	
	// 流程审批业务
	final static String MSG_FLOW_SP_TODO = "02203";// 审批办理
	final static String MSG_FLOW_SP_NOTICE = "02204";// 审批抄送
	final static String MSG_FLOW_SP_HUIQIAN = "02205";// 审批会签
	//在线学习
	final static String TYPE_LEARN ="023";
	//综合办公
	final static String TYPE_ZHBG ="024";
	//车辆管理
	final static String TYPE_CLGL ="025";
	//车辆强险
	final static String TYPE_CLGL_QX ="02501";
	//车辆商业险
	final static String TYPE_CLGL_SYX ="02502";
	//车辆使用记录
	final static String TYPE_CLGL_SYJL ="02503";
	//车辆维修记录
	final static String TYPE_CLGL_WXJL ="02504";
	//固定资产管理
	final static String TYPE_GDZC ="026";
	//固定资产所属类型
	final static String TYPE_GDZC_SS ="02601";
	//固定资产增加
	final static String TYPE_GDZC_ADD ="02602";
	//固定资产减少类型
	final static String TYPE_GDZC_REDUCE ="02603";
	//固定资产维修类型
	final static String TYPE_GDZC_MAINTAIN ="02604";
	
	//办公用品
	final static String TYPE_BGYP ="027";
	//办公用品采购
	final static String TYPE_BGYP_BUY="02701";
	//办公用品采购审核
	final static String TYPE_BGYP_BUY_CHECK="027010";
	//办公用品采购审核通知
	final static String TYPE_BGYP_BUY_NOTICE="027011";
	//办公用品申领审核
	final static String TYPE_BGYP_APPLY_CHECK="027020";
	//办公用品申领通知
	final static String TYPE_BGYP_APPLY_NOTICE="027021";
	
	//人事档案
	final static String TYPE_RSDA ="028";
	
	//我要出差
	final static String MENU_FEE_APPLY ="029";
	//我要借款
	final static String MENU_LOAN ="030";
	//我要报销
	final static String MENU_LOANOFF ="031";
	
	//出差业务
	final static String TYPE_FEE_APPLY_TRIP ="029";
	//一般业务
	final static String TYPE_FEE_APPLY_DAYLY ="035";
	
	//出差借款业务
	final static String TYPE_LOAN_TRIP ="030";
	//一般借款业务
	final static String TYPE_LOAN_DAYLY ="031";
	//出差报销业务
	final static String TYPE_LOANOFF_TRIP ="03101";
	//一般报销业务
	final static String TYPE_LOANOFF_DAYLY ="03102";
	//差旅一览
	final static String TYPE_FINANCIAL_STATISTICS ="032";
	//差旅差旅管理模块标识
	final static String TYPE_FINANCIAL="033";
	
	//出差报告
	final static String TYPE_REPORT_TRIP ="03401";
	//一般借款说明
	final static String TYPE_REPORT_DAYLY ="03402";
	
	//请假业务
	final static String TYPE_LEAVE = "036";
	//加班业务
	final static String TYPE_OVERTIME = "037";
	//考勤
	final static String TYPE_ATTENCE="038";
	//公告
	final static String TYPE_ANNOUNCEMENT="039";
	//公告
	final static String TYPE_ANNOUNCETALK="03901";
	//制度管理
	final static String TYPE_INSTITUTION="040";
	//制度管理
	final static String TYPE_INSTITUTTALK="04001";
	//积分系统
	final static String TYPE_JFMOD="041";
	//IT运维管理
	final static String TYPE_ITOM="042";
	//IT运维管理-事件管理过程
	final static String TYPE_ITOM_EVENTPM="04201";
	//IT运维管理-问题管理过程
	final static String TYPE_ITOM_ISSUEPM="04202";
	//IT运维管理-变更管理过程
	final static String TYPE_ITOM_MODIFYPM="04203";
	//IT运维管理-发布管理过程
	final static String TYPE_ITOM_RELEASEPM="04204";
	//IT运维管理统计
	final static String TYPE_ITOM_TJ="043";
	//IT运维管理-事件管理过程
	final static String TYPE_ITOM_EVENTPM_TJ="04301";
	//IT运维管理-问题管理过程
	final static String TYPE_ITOM_BUGPM_TJ="04302";
	//IT运维管理-变更管理过程
	final static String TYPE_ITOM_MODIFYPM_TJ="04303";
	//IT运维管理-发布管理过程
	final static String TYPE_ITOM_RELEASEPM_TJ="04304";
	//平台审批会议
	final static String TYPE_MEETING_SP="046";
	//会议纪要
	final static String TYPE_MEET_SUMMARY = "047";
	//分享标注
	final static String TYPE_DAILY = "050";
	//分享留言标注
	final static String TYPE_DAILYTALK = "051";
	// 平台记账
	final static String TYPE_CONSUME = "060";
	
	//需求管理
	final static String TYPE_DEMAND_PROCESS = "070";
	
	// 平台事项催办
	final static String TYPE_REMINDER = "099";
	// 平台事项领款通知
	final static String TYPE_NOTIFICATIONS = "0103";
	// 平台财务办公
	final static String TYPE_FINALCIAL_OFFICE = "066";
	
	// 平台事项财务结算
	final static String TYPE_FINALCIAL_BALANCE = "06601";
	
	// 平台事项完成结算
	final static String TYPE_BALANCED = "06602";

	//角色模块
	final static String TYPE_ROLE = "070";
	
	//属性变更审批模块
	final static String TYPE_CHANGE_EXAM = "067";
	
	//属性变更审批指定需要审批人员
	final static String TYPE_NEED_EXAM_USER = "06701";
	
	//外部联系人模块
	final static String TYPE_OUTLINKMAN = "068";
	
	/**
	 * 运营分分析
	 */
	final static String TYPE_OPERATION = "11111";
	
	/**
	 * 邮箱
	 */
	final static String TYPE_MAIL = "069";

	//产品
	final static String TYPE_PRODUCT = "080";
		
	/***************************返回状态常量*************************************************/
	//返回状态
	final static String TYPE_STATUS="status";
	//返回Y
	final static String TYPE_STATUS_Y="y";
	//返回n
	final static String TYPE_STATUS_N="n";
	//返回t
	final static String TYPE_STATUS_T="t";
	//返回f
	final static String TYPE_STATUS_F="f";
	//返回信息
	final static String TYPE_INFO="info";
	/***************************闹钟常量*************************************************/
	
	//分享 100 或 1 但不是001
	final static String TYPE_SHARE="100";
	//闹铃
	final static String TYPE_CLOCK="101";
	
	/***************************积分常量*************************************************/
	
	//平台系统任务积分标志
	final static String JIFEN_SYS="sysJifen";
	//平台日常任务积分标志
	final static String JIFEN_DAY="dayJifen";
	//平台永久任务积分标志
	final static String JIFEN_PERMANENT="permJifen";
	//平台自动以任务积分标志
	final static String JIFEN_ZDY="zdyJifen";
	
	/***************************手机短信分类标识*************************************************/
	//注册验证码
	final static String MSG_REGISTER_VCODE="10000";
	//权限验证码
	final static String MSG_AUTHORITY_VCODE="10001";
	//待办工作
	final static String MSG_JOB_TO_DO="10002";
	//注册申请审核
	final static String MSG_REGEIST_APPLY_OK="10003";
	//注册申请审核
	final static String MSG_REGEIST_APPLY_NO="10004";
	
	//邀请需要初始密码
	final static String MSG_REGEIST_INVITE_PY="10005";
	//邀请不需要初始密码
	final static String MSG_REGEIST_INVITE_PN="10006";
	
	/***************************系统平台手机短信标识*************************************************/
	//系统平台手机短信标识
	final static String SYS_INFO="10005";
	
	/***************************短信发送方式表示*************************************************/
	//通过手机短信
	final static String GET_BY_PHONE="phone";
	//通过邮件
	final static String GET_BY_EMAIL="email";
	
	/***************************系统平台组织编号默认*************************************************/
	//系统平台组织编号默认
	final static Integer SYS_COMID=-1;
	
	/***************************项目阶段关联模块类型*************************************************/
	
	//审批关联
	final static String STAGED_FLOW_SP="sp_flow";
	//任务关联
	final static String STAGED_TASK="task";
	//附件关联
	final static String STAGED_FILE="file";

	/***************************加入系统方式*************************************************/
	public final static Integer JOIN_APPLY = 1;// 申请加入
	public final static Integer JOIN_INVITE = 2;// 邀请加入
	/***************************流程审批业务常量*************************************************/
	
	//声明与流程步骤相关常量
	public final static String EXECUTOR_BY_DIRECT="directLeader";//直属上级
	public final static String EXECUTOR_BY_SELF="creatorSelf";//创建人自己
	public final static String EXECUTOR_BY_APPOINT="appointMan";//指定执行人

	//流程状态常量申明; 流程状态 0开始 1提交 2保存 4结束
	public final static Integer SP_STATE_UNSTART=0;
	public final static Integer SP_STATE_EXE=1;
	public final static Integer SP_STATE_SAVE=2;
	public final static Integer SP_STATE_FINISH=4;
	//流程审批状态常量
	public final static Integer SP_STATE_REFUSE=0;//驳回
	public final static Integer SP_STATE_PASS=1;//通过
	
	//审批随机码获取标识
	public final static String SPSTEP_CHECK_NO="0";//不需要
	public final static String SPSTEP_CHECK_YES="1";//需要
	
	// 申明发起人以及发起人所在部门控件key值常量
	public final static Integer FILEID_CREATOR = 10000;// 发起人员key
	public final static Integer FILEID_DEP = 11000;// 发起人所在部门key
	
	//表单初始化组件标识
	public final static Integer FORM_INIT_FIELID = 100;// 表单初始化组件标识
	
	//订单类型常量
	public final static String ORDER_ORDERTYPE_ORGUPGRADE = "001";//团队升级
	//折扣类型常量
	public final static String ORDER_DISCOUNTTYPE_YEAR = "years";//按年数折扣
	//收费类型常量
	public final static String ORDER_CHARGINGTYPE_USERSCOPE = "userScope";//按使用人数收费
	//支付方式常量
	public final static Integer ORDER_PAIDWAY_OFFLINE = 0;//线下
	public final static Integer ORDER_PAIDWAY_WECHAT = 1;//支付宝
	public final static Integer ORDER_PAIDWAY_ALIPAY = 2;//微信
	//订单支付状态常量
	public final static Integer ORDER_STATUS_UNPAID = 0;//未支付
	public final static Integer ORDER_STATUS_PAID = 1;//已经支付
	public final static Integer ORDER_STATUS_FAIL = 2;//支付失败
	//订单是否需要发表常量
	public final static Integer ORDER_BILL_NEEDLESS = 0;//不需要
	public final static Integer ORDER_BILL_NEED = 1;//需要
	//人员是否是在团队允许服务范围内常量
	public final static Integer USER_INSERVICE_YES =1;//在服务内
	public final static Integer USER_INSERVICE_NO =0;//在服务外
	//系统平台默认能使用的免费人数常量
	public final static Integer ORG_DEFAULT_MEMBERS = 10;//默认能使用的免费人数
	//定义平台状态常量
	public final static Integer ENABLED_YES = 1;//启用状态
	public final static Integer ENABLED_NO = 0;//禁用状态
	//定义平台状态常量
	public final static Integer COMMON_DEF = 0;//默认
	public final static Integer COMMON_YES = 1;//肯定
	public final static Integer COMMON_NO = -1;//否定
	public final static Integer COMMON_FINISH = 4;//完结状态
	//返回状态标识常量
	public final static String RETURN_CODE_SUCCESS="SUCCESS";//成功
	public final static String RETURN_CODE_FAIL="FAIL";//失败
	//结果状态标识常量
	public final static String RESULT_CODE_SUCCESS="SUCCESS";//成功
	public final static String RESULT_CODE_FAIL="FAIL";//失败
	//交易状态
	public final static String ORDER_TRADE_STATE_SUCCESS="SUCCESS";//支付成功
	public final static String ORDER_TRADE_STATE_REFUND="REFUND";//转入退款
	public final static String ORDER_TRADE_STATE_NOTPAY="NOTPAY";//未支付
	public final static String ORDER_TRADE_STATE_CLOSED="CLOSED";//已关闭
	public final static String ORDER_TRADE_STATE_REVOKED="REVOKED";//已撤销（刷卡支付）
	public final static String ORDER_TRADE_STATE_USERPAYING="USERPAYING";//用户支付中 
	public final static String ORDER_TRADE_STATE_PAYERROR="PAYERROR";//支付失败(其他原因，如银行返回失败)
	
	
	/***************************闹铃状态常量*************************************************/
	public final static String CLOCK_EXESTATE_NO="0";//闹铃执行状态 未执行
	public final static String CLOCK_EXESTATE_TODO="1";//闹铃执行状态 待执行
	public final static String CLOCK_EXESTATE_DONE="2";//闹铃执行状态 已执行
	/***************************闹铃状态常量*************************************************/
	
	/***************************关注状态常量*************************************************/
	public final static String ATTENTION_STATE_NO="0";//不关注
	public final static String ATTENTION_STATE_YES="1";//关注
	/***************************关注状态常量*************************************************/
	
	/***************************模块操作常量*************************************************/
	public final static String MOD_OPT_STATE_NO="0";//不关注
	public final static String MOD_OPT_STATE_YES="1";//关注
	/***************************模块操作常量*************************************************/
	
	/***************************周报发布状态常量*************************************************/
	public final static String WEEK_STATE_DRAFT="0";//草稿
	public final static String WEEK_STATE_DONE="1";//已发布
	/***************************周报发布状态常量*************************************************/
	
	/***************************投票匿名类型状态常量***********************************************/
	public final static String VOTE_TYPE_REALNAME="1";//实名
	public final static String VOTE_TYPE_NONAME="0";//匿名
	/***************************投票匿名类型状态常量**********************************************/
	
	/***************************模块预删除标识 常量***********************************************/
	public final static Integer MOD_PREDEL_SATATE_NO = 0;//模块预删除标识 正常
	public final static Integer MOD_PREDEL_SATATE_YES = 1;//模块预删除标识  已预删除
	/***************************模块预删除标识 量**********************************************/
	
	/***************************用户启用标识常量***********************************************/
	public final static String SYS_ENABLED_NO = "0";//不可用
	public final static String SYS_ENABLED_YES = "1";//可用
	/***************************用户启用标识常量**********************************************/
	
	/***************************用户审核标识常量***********************************************/
	public final static String USER_CHECK_REJECT = "0";//拒绝
	public final static String USER_CHECK_AGREE = "1";//同意
	/***************************用户启用标识常量**********************************************/
	
	/***************************用户审核标识常量***********************************************/
	public final static String USER_ROLE_NORMAL = "0";//普通成员
	public final static String USER_ROLE_MANAGER_SUP = "1";//超级管理员
	public final static String USER_ROLE_MANAGER_NORMAL = "2";//普通管理员
	/***************************用户启用标识常量**********************************************/
	
	/***************************采购清单审核标识常量***********************************************/
	public final static String PURORDER_STATE_NORMAL = "0";//录入
	public final static String PURORDER_STATE_CHECK = "1";//送审
	public final static String PURORDER_STATE_PASS = "2";//通过
	public final static String PURORDER_STATE_BACK = "3";//未通过
	/***************************采购清单审核标识常量**********************************************/
	
	
	/***************************文件是否存在标识常量***********************************************/
	public final static String FILE_STATE_MISS = "0";//文件不存在
	public final static String FILE_STATE_EXIST = "1";//文件存在
	/***************************文件是否存在标识常量**********************************************/
	
	/***************************模块统计常量***********************************************/
	public final static String STATIS_TASK_GRADE = "1";//任务紧急度统计
	public final static String STATIS_TASK_OVERDUE = "2";//任务逾期统计
	public final static String STATIS_TASK_EXECTOR = "3";//执行分配统计
	public final static String STATIS_TASK_RELATE_ITEM = "4";//任务关联项目统计
	public final static String STATIS_TASK_RELATE_CRM = "5";//任务关联客户统计
	public final static String STATIS_TASK_USEDTIME = "6";//任务执行用时统计
	public final static String STATIS_TASK_STATE_CHOOSE = "0";//任务认领中
	public final static String STATIS_TASK_STATE_DOING = "1";//任务执行中
	public final static String STATIS_TASK_STATE_PAUSE = "3";//任务暂停中
	public final static String STATIS_TASK_STATE_DONE = "4";//任务已完成
	
	public final static String STATIS_CRM_TYPE = "1";//客户类型统计
	public final static String STATIS_CRM_MODIFY = "2";//客户更新统计
	public final static String STATIS_CRM_AREA = "3";//客户分布统计
	public final static String STATIS_CRM_OWNER = "4";//客户负责人统计
	public final static String STATIS_CRM_FREQ_ADD = "5";//客户增量统计
	public final static String STATIS_CRM_STAGE = "6";//客户阶段统计
	public final static String STATIS_CRM_BUDGET = "7";//客户资金预算统计
	
	/***************************模块统计常量*******************************************/
	
	/***************************文件是否存在标识常量***********************************************/
	public final static String FINISHED_YES = "1";//是办结
	public final static String FINISHED_NO = "0";//未办结
	/***************************文件是否存在标识常量**********************************************/
	
	/***************************业务更新记录**********************************************/
	public final static String BUSUPDATETYPE_ADD = "1";//是创建
	public final static String BUSUPDATETYPE_UPDATE = "2";//是修改
	public final static String BUSUPDATETYPE_FEEDBACK = "3";//是维护
	public final static String BUSUPDATETYPE_HANDOVER = "4";//是移交
	/***************************引导模块常量*************************************************/
	public final static String INTRO_HEAD = "001";//头部
	public final static String INTRO_INDEX = "002";//主页模块
	
	/***************************个人配置常量开始*************************************************/
	public final static String UCFG_SYS_SWITCH = "sysConfig";//头部
	public final static String UCFG_MENU_NUM = "menuNum";//头部
	/***************************个人配置常量结束*************************************************/
	/***************************个人配置常量开始*************************************************/
	public final static Integer LOANOFF_YES = 1;//头部
	public final static Integer LOANOFF_NO = 0;//头部
	/***************************个人配置常量结束*************************************************/
	

	public final static String TYPE_ADD = "add";// 添加
	public final static String TYPE_UPDATE = "update";// 修改
	/***************************待办工作类型、未读标识常量*************************************************/
	final static Integer TYPE_TODAYWORKS_BUSSPEC_0=0;//业务类别 0普通消息，点击查看后即消失
	final static Integer TYPE_TODAYWORKS_BUSSPEC_1=1;//业务类别 1需办理，需事项办理完后，才能消失
	final static Integer TYPE_TODAYWORKS_READSTATE_0=0;//是否已读； 0未读 
	final static Integer TYPE_TODAYWORKS_READSTATE_1=1;//是否已读；  1已读
	
	/***************************任务类型常量*************************************************/
	public final static String TASK_TYPE_CHOOSE = "1";//认领制
	public final static String TASK_TYPE_SURE = "2";//协同制
	
	/*************************** 是否发送短信标识常量 ***********************************************/
	public final static String MSG_SEND_YES = "1";// 是
	public final static String MSG_SEND__NO = "0";// 否
	/*************************** 是否发送短信标识常量 **********************************************/
	
	public final static String STATUS_Y = "y";// 状态y类型
	public final static String STATUS_N = "n";// 状态n类型
}
