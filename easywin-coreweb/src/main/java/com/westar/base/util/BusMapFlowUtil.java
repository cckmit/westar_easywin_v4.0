package com.westar.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westar.base.pojo.KeyValue;


/**
 * 
 * 描述:表单字段映射工具类
 * @author zzq
 * @date 2018年7月18日 下午2:46:31
 */
public class BusMapFlowUtil {
	
	private static Map<String,List<KeyValue>> modKeyValueMap = new HashMap<String,List<KeyValue>>();
	//费用管理中依据表属性关系集合
	private static List<KeyValue> feeBudgetMap = new ArrayList<KeyValue>();
	/**
	 * 取得模块所需要的数据
	 * @param busType
	 * @return
	 */
	public static List<KeyValue> listAttr(String busType) {
		List<KeyValue> listAttr = modKeyValueMap.get(busType);
		if(null != listAttr ){
			return listAttr;
		}
		if(ConstantInterface.TYPE_FEE_APPLY_TRIP.equals(busType)){//出差映射属性字段
			listAttr = BusMapFlowUtil.getBusFeeApplyForTrip();
		}else if(ConstantInterface.TYPE_FEE_APPLY_DAYLY.equals(busType)){//一般借款
			listAttr = BusMapFlowUtil.getBusFeeApplyForDaily();
		}else if(ConstantInterface.TYPE_LOAN_TRIP.equals(busType) 
				|| ConstantInterface.TYPE_LOAN_DAYLY.equals(busType)){//借款（出差借款以及一般借款）映射属性字段
			listAttr = BusMapFlowUtil.getBusFeeLoan();
		}else if(ConstantInterface.TYPE_REPORT_TRIP.equals(busType) //出差报告不需要映射
				|| ConstantInterface.TYPE_REPORT_DAYLY.equals(busType)){//一般报告不需要映射
			listAttr = BusMapFlowUtil.getBusFeeReport();
		}else if(ConstantInterface.TYPE_LOANOFF_TRIP.equals(busType)//报销映射属性字段
				|| ConstantInterface.TYPE_LOANOFF_DAYLY.equals(busType)){//报销映射属性字段
			listAttr = BusMapFlowUtil.getBusFeeLoanOff();
		}else if(ConstantInterface.TYPE_LEAVE.equals(busType)){//请假
			listAttr = BusMapFlowUtil.getBusLeave();
		}else if(ConstantInterface.TYPE_OVERTIME.equals(busType)){//加班
			listAttr = BusMapFlowUtil.getBusOverTime();
		}else if(ConstantInterface.TYPE_ITOM_EVENTPM.equals(busType)){//IT运维管理-事件管理过程
			listAttr = BusMapFlowUtil.getBusEventPm();
		}else if(ConstantInterface.TYPE_ITOM_ISSUEPM.equals(busType)){//IT运维管理-问题管理过程
			listAttr = BusMapFlowUtil.getBusIssuePm();
		}else if(ConstantInterface.TYPE_ITOM_MODIFYPM.equals(busType)){//IT运维管理-变更管理过程
			listAttr = BusMapFlowUtil.getBusModifyPm();
		}else if(ConstantInterface.TYPE_ITOM_RELEASEPM.equals(busType)){//IT运维管理-发布管理过程
			listAttr = BusMapFlowUtil.getBusReleasePm();
		}
		modKeyValueMap.put(busType,listAttr);
		return listAttr;
	}

	/**
	 * 取得项目或业务出差预算所需字段
	 * @return
	 */
	private static List<KeyValue> getBusFeeApplyForTrip(){
		List<KeyValue> busTripAttrForMap = new ArrayList<KeyValue>();
		
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("startTime");
		keyValue.setValue("出差开始时间");
		keyValue.setIsRequired("1");//必填
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("endTime");
		keyValue.setValue("出差结束时间");
		keyValue.setIsRequired("1");//必填
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("busId");
		keyValue.setValue("项目/客户");
		keyValue.setIsRequired("1");//必填
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("tripPlace");
		keyValue.setValue("出差地点");
		keyValue.setIsRequired("1");//必填
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("allowedQuota");
		keyValue.setValue("借款限额");
		keyValue.setIsRequired("1");//必填
		busTripAttrForMap.add(keyValue);
		return busTripAttrForMap;
		
	}
	/**
	 * 取得一般借款所需要的字段信息
	 * @return
	 */
	private static List<KeyValue> getBusFeeApplyForDaily(){
		List<KeyValue>  daylyLoanApplyAttrForMap = new ArrayList<KeyValue>();
		
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("startTime");
		keyValue.setValue("开始时间");
		keyValue.setIsRequired("1");//必填
		daylyLoanApplyAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("endTime");
		keyValue.setValue("结束时间");
		keyValue.setIsRequired("1");//必填
		daylyLoanApplyAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("allowedQuota");
		keyValue.setValue("借款限额");
		keyValue.setIsRequired("1");//必填
		daylyLoanApplyAttrForMap.add(keyValue);
		return daylyLoanApplyAttrForMap;
	}
	/**
	 * 取得借款需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusFeeLoan(){
		List<KeyValue>  busTripAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("loanMoney");
		keyValue.setValue("借款中金额");
		keyValue.setIsRequired("1");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("borrowingBalance");
		keyValue.setValue("核实金额");
		keyValue.setIsRequired("1");
		busTripAttrForMap.add(keyValue);
		return busTripAttrForMap;
	}
	/**
	 * 取得汇报需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusFeeReport(){
		List<KeyValue>  busTripAttrForMap = new ArrayList<KeyValue>();
//		KeyValue keyValue = new KeyValue();
//		keyValue.setKey("startTime");
//		keyValue.setValue("汇报开始时间");
//		busTripAttrForMap.add(keyValue);
//		keyValue = new KeyValue();
//		keyValue.setKey("endTime");
//		keyValue.setValue("汇报结束时间");
//		busTripAttrForMap.add(keyValue);
		return busTripAttrForMap;
	}
	/**
	 * 取得销账需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusFeeLoanOff() {
		List<KeyValue>  busTripAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("loanOffBalance");
		keyValue.setValue("销账金额");
		keyValue.setIsRequired("1");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("loanOffItemFee");
		keyValue.setIsRequired("1");
		keyValue.setValue("报销金额");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("loanOffPreFee");
		keyValue.setIsRequired("1");
		keyValue.setValue("预报销金额");
		busTripAttrForMap.add(keyValue);
		return busTripAttrForMap;
	}
	/**
	 * 取得请假需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusLeave() {
		List<KeyValue>  busTripAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("busType");
		keyValue.setValue("请假类型");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("startTime");
		keyValue.setValue("请假开始时间");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("endTime");
		keyValue.setValue("请假结束时间");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("leaveTime");
		keyValue.setValue("请假时长");
		busTripAttrForMap.add(keyValue);
		return busTripAttrForMap;
	}
	/**
	 * 取得加班需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusOverTime() {
		List<KeyValue>  busTripAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue = new KeyValue();
		keyValue.setKey("xzhsStartTime");
		keyValue.setValue("行政核实开始时间");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("xzhsEndTime");
		keyValue.setValue("行政核实结束时间");
		busTripAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("xzhsOverTime");
		keyValue.setValue("行政核实加班时长");
		busTripAttrForMap.add(keyValue);
		return busTripAttrForMap;
	}
	/**
	 * 取得事件管理需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusEventPm() {
		List<KeyValue>  eventPmAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("eventId");
		keyValue.setValue("事件ID");
		keyValue.setIsRequired("1");//必填
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("crmName");
		keyValue.setValue("客户名称");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantName");
		keyValue.setValue("请求人姓名");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantCom");
		keyValue.setValue("请求人所在公司");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantDepName");
		keyValue.setValue("请求人所在部门");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantEmail");
		keyValue.setValue("请求人email");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantLinePhone");
		keyValue.setValue("请求人办公电话");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantMovePhone");
		keyValue.setValue("请求人移动电话");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventAddress");
		keyValue.setValue("事件发生的地点");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventTime");
		keyValue.setValue("事件发生时间");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventAttr");
		keyValue.setValue("事件性质");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventSource");
		keyValue.setValue("事件来源");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventInfluenceDegree");
		keyValue.setValue("服务（事件）影响度");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventPriorityDegree");
		keyValue.setValue("服务（事件）优先级");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("busModName");
		keyValue.setValue("业务名称");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventType");
		keyValue.setValue("事件分类");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventTitle");
		keyValue.setValue("（事件）标题");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventRemark");
		keyValue.setValue("（事件）描述");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventTerminator");
		keyValue.setValue("事件解决人");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventStatus");
		keyValue.setValue("事件状态");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventSolution");
		keyValue.setValue("事件解决方案");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventEndCode");
		keyValue.setValue("事件结束代码");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventRepetitionMark");
		keyValue.setValue("重复事件标记");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventBugRemark");
		keyValue.setValue("记录出现故障的配置项代码");
		eventPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("eventLimitDate");
		keyValue.setValue("事件完成期限");
		eventPmAttrForMap.add(keyValue);
		return eventPmAttrForMap;
	}
	
	/**
	 * 取得问题管理需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusIssuePm() {
		List<KeyValue>  issuePmAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("issueId");
		keyValue.setValue("问题ID");
		keyValue.setIsRequired("1");//必填
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantName");
		keyValue.setValue("请求人姓名");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantCom");
		keyValue.setValue("请求人所在公司");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantDepName");
		keyValue.setValue("请求人所在部门");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantEmail");
		keyValue.setValue("请求人email");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantLinePhone");
		keyValue.setValue("请求人办公电话");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantMovePhone");
		keyValue.setValue("请求人移动电话");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueSource");
		keyValue.setValue("问题来源");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issuePriorityDegree");
		keyValue.setValue("服务（问题）优先级");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("busModName");
		keyValue.setValue("业务名称");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueType");
		keyValue.setValue("问题分类");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueTitle");
		keyValue.setValue("（问题）标题");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueRemark");
		keyValue.setValue("（问题）描述");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueReplace");
		keyValue.setValue("变通方法");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueRepetitionMark");
		keyValue.setValue("重复问题标记");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueStatus");
		keyValue.setValue("问题状态");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueLogs");
		keyValue.setValue("问题日志");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueStartTimes");
		keyValue.setValue("记录事件状态到分析中的时间");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueEndTimes");
		keyValue.setValue("记录事件已有解决方案的时间");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueSolution");
		keyValue.setValue("解决方案");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueEndCode");
		keyValue.setValue("问题结束代码");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueStillReason");
		keyValue.setValue("问题无法解决原因");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueBugRemark");
		keyValue.setValue("记录问题的配置项代码");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueRelateEvent");
		keyValue.setValue("关联的事件单号");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueRelateModify");
		keyValue.setValue("关联的变更单号");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueRelateStock");
		keyValue.setValue("记录问题对应的知识单号");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("issueCloseDate");
		keyValue.setValue("关闭的时间");
		issuePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("crmName");
		keyValue.setValue("问题所影响的客户");
		issuePmAttrForMap.add(keyValue);
		return issuePmAttrForMap;
	}
	/**
	 * 取得变更管理需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusModifyPm() {
		List<KeyValue>  modifyPmAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("modifyId");
		keyValue.setValue("变更ID");
		keyValue.setIsRequired("1");//必填
		modifyPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantName");
		keyValue.setValue("请求人姓名");
		modifyPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantCom");
		keyValue.setValue("请求人所在公司");
		modifyPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantDepName");
		keyValue.setValue("请求人所在部门");
		modifyPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantEmail");
		keyValue.setValue("请求人email");
		modifyPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantLinePhone");
		keyValue.setValue("请求人办公电话");
		modifyPmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantMovePhone");
		keyValue.setValue("请求人移动电话");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyTitle");
		keyValue.setValue("（变更）标题");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifySource");
		keyValue.setValue("变更来源");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyRelateEvent");
		keyValue.setValue("关联的事件单号");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyRelateIssue");
		keyValue.setValue("关联的问题单号");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifySpace");
		keyValue.setValue("变更类型");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyRiskSpace");
		keyValue.setValue("风险等级");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("busModName");
		keyValue.setValue("业务名称");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyType");
		keyValue.setValue("变更分类");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyRemark");
		keyValue.setValue("（变更）描述");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyInSys");
		keyValue.setValue("变更影响应用系统");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyInDep");
		keyValue.setValue("变更影响客户部门");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyPauseState");
		keyValue.setValue("变更是否中断业务");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyTestState");
		keyValue.setValue("变更是否需要测试");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("notifyCrmName");
		keyValue.setValue("需要通知的客户名称");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyStatus");
		keyValue.setValue("变更状态");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyCharge");
		keyValue.setValue("变更主管");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyChargeTime");
		keyValue.setValue("变更主管接受变更时间");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyPlanStartTimes");
		keyValue.setValue("变更计划开始时间");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyPlanEndTimes");
		keyValue.setValue("变更计划完成时间");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyStartTimes");
		keyValue.setValue("变更实际开始时间");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyEndTimes");
		keyValue.setValue("变更实际完成时间");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyEffectRemark");
		keyValue.setValue("变更实施记录");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyTestRemark");
		keyValue.setValue("变更测试记录");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyViewRemark");
		keyValue.setValue("变更观察记录");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyPauseTime");
		keyValue.setValue("中断时长");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyReviewRemark");
		keyValue.setValue("回顾意见");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyReviewCode");
		keyValue.setValue("回顾代码");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyEndCode");
		keyValue.setValue("变更结束代码");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyCloseUser");
		keyValue.setValue("变更关闭人");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyCloseDate");
		keyValue.setValue("关闭的时间");
		modifyPmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("modifyRelateModify");
		keyValue.setValue("关联的发布单号");
		modifyPmAttrForMap.add(keyValue);
		return modifyPmAttrForMap;
	}
	/**
	 * 取得发布管理需要映射的字段
	 * @return
	 */
	private static List<KeyValue> getBusReleasePm() {
		List<KeyValue> releasePmAttrForMap = new ArrayList<KeyValue>();
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("releaseId");
		keyValue.setValue("发布ID");
		keyValue.setIsRequired("1");//必填
		releasePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantName");
		keyValue.setValue("请求人姓名");
		releasePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantCom");
		keyValue.setValue("请求人所在公司");
		releasePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantDepName");
		keyValue.setValue("请求人所在部门");
		releasePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantEmail");
		keyValue.setValue("请求人email");
		releasePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantLinePhone");
		keyValue.setValue("请求人办公电话");
		releasePmAttrForMap.add(keyValue);
		keyValue = new KeyValue();
		keyValue.setKey("applicantMovePhone");
		keyValue.setValue("请求人移动电话");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseTitle");
		keyValue.setValue("（发布）标题");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseSource");
		keyValue.setValue("发布来源");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseRelateEvent");
		keyValue.setValue("关联的事件单号");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseRelateIssue");
		keyValue.setValue("关联的问题单号");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseSpace");
		keyValue.setValue("发布类型");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseRiskSpace");
		keyValue.setValue("风险等级");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("busModName");
		keyValue.setValue("业务名称");
		keyValue.setIsRequired("1");//必填
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseType");
		keyValue.setValue("发布分类");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseRemark");
		keyValue.setValue("（发布）描述");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseInSys");
		keyValue.setValue("发布影响应用系统");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseInDep");
		keyValue.setValue("发布影响客户部门");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("notifyDepName");
		keyValue.setValue("需要通知的部门名称");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releasePauseState");
		keyValue.setValue("发布是否中断业务");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseSysTestState");
		keyValue.setValue("发布是否需要系统测试");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseCrmTestState");
		keyValue.setValue("发布是否需要客户测试");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseStatus");
		keyValue.setValue("发布状态");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseCharge");
		keyValue.setValue("发布主管");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseChargeTime");
		keyValue.setValue("发布主管接受发布时间");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releasePlanStartTimes");
		keyValue.setValue("发布计划开始时间");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releasePlanEndTimes");
		keyValue.setValue("发布计划完成时间");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseStartTimes");
		keyValue.setValue("发布实际开始时间");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseEndTimes");
		keyValue.setValue("发布实际完成时间");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseEffectRemark");
		keyValue.setValue("发布实施记录");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseSysTestRemark");
		keyValue.setValue("发布系统测试结果");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseCrmTestRemark");
		keyValue.setValue("发布客户测试结果");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseViewRemark");
		keyValue.setValue("发布观察记录");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releasePauseTime");
		keyValue.setValue("中断时长");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseCloseCode");
		keyValue.setValue("发布工作单关闭代码");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseState");
		keyValue.setValue("发布工作单状态");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseEndCode");
		keyValue.setValue("发布结束代码");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseCloseUser");
		keyValue.setValue("发布关闭人");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("releaseCloseDate");
		keyValue.setValue("关闭的时间");
		releasePmAttrForMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("otherRemark");
		keyValue.setValue("备注");
		releasePmAttrForMap.add(keyValue);
		return releasePmAttrForMap;
	}
	
	/**
	 * 取得费用管理中属性映射关系集合
	 * @return
	 */
	public static List<KeyValue> listAttrOfFeeBudget() {
		if(!CommonUtil.isNull(feeBudgetMap)){
			return feeBudgetMap;
		}

		
		KeyValue keyValue = new KeyValue();
		keyValue.setKey("listRelateMods");
		keyValue.setValue("关联数据映射");
		feeBudgetMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("allowedQuota");
		keyValue.setValue("借款限额");
		feeBudgetMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("loanFeeTotal");
		keyValue.setValue("累计借款");
		feeBudgetMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("loanOffTotal");
		keyValue.setValue("累计销账");
		feeBudgetMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("loanItemTotal");
		keyValue.setValue("累计报销");
		feeBudgetMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("unLoanOffTotal");
		keyValue.setValue("未销账金额");
		feeBudgetMap.add(keyValue);
		
		keyValue = new KeyValue();
		keyValue.setKey("tripPlace");
		keyValue.setValue("出差地点");
		feeBudgetMap.add(keyValue);
		
		return feeBudgetMap;
	}
}
