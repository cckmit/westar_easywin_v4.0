package com.westar.core.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.annotation.Filed;
import com.westar.base.enums.LoanReportWayEnum;
import com.westar.base.enums.LoanWayEnum;
import com.westar.base.model.BusAttrMapFormColTemp;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.EventPm;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FeeBusMod;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.FeeLoanReport;
import com.westar.base.model.FormColMapBusAttr;
import com.westar.base.model.IssuePm;
import com.westar.base.model.Leave;
import com.westar.base.model.ModifyPm;
import com.westar.base.model.OverTime;
import com.westar.base.model.ReleasePm;
import com.westar.base.model.SpFlowInputData;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.SpFlowOptData;
import com.westar.base.model.SpFlowUpfile;
import com.westar.base.model.UserInfo;
import com.westar.base.util.BeanRefUtil;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.MathExtend;
import com.westar.core.dao.BusRelateSpFlowDao;

@Service
public class BusRelateSpFlowService {
	
	private static Logger logger = LoggerFactory.getLogger(BusRelateSpFlowService.class);

	@Autowired
	BusRelateSpFlowDao busRelateSpFlowDao;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	AdminCfgService adminCfgService;
	
	@Autowired
	FormColTempService formColTempService;
	
	@Autowired
	FinancialService financialService;
	
	@Autowired
	AttenceService  attenceService;
	/**
	 * 映射数据信息
	 * @param instance 流程实例化信息
	 * @param userInfo 当前操作人员
	 * @param listBusAttrMapFormColTemp
	 * @param formDataMap
	 */
	public void initBusEngineData(SpFlowInstance instance, UserInfo userInfo,
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp,
			Map<String, SpFlowInputData> formDataMap,Map<String, String> mapForBean) {
		if (ConstantInterface.TYPE_FEE_APPLY_TRIP.equals(instance.getBusType())// 费用额度申请
				|| ConstantInterface.TYPE_FEE_APPLY_DAYLY.equals(instance.getBusType())) {// 费用额度申请
			Object obj = this.constrObjBaseInfo(instance,FeeBudget.class);
			if(null != obj){
				FeeBudget loanApply = (FeeBudget) obj;
				//出差审批回调引擎
				this.initFeeBudget(loanApply,userInfo,listBusAttrMapFormColTemp,formDataMap);
				
			}
		} else if (ConstantInterface.TYPE_LOAN_TRIP.equals(instance.getBusType())
				|| ConstantInterface.TYPE_LOAN_DAYLY.equals(instance.getBusType())) {// 借款（出差借款、一般借款）申请流程
			
			Object obj = this.constrObjBaseInfo(instance,FeeLoan.class);
			if(null != obj){
				FeeLoan feeLoan = (FeeLoan) obj;
				
				//设置借款中的金额
				if(null!= listBusAttrMapFormColTemp && !listBusAttrMapFormColTemp.isEmpty()){
					FeeLoan modFeeLoan = new  FeeLoan();
					Map<String,String> map = new HashMap<String, String>(); 
					for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
						SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
						if(null==inputData){continue;}
						String value = (String) workFlowService.getFormComponentValue(
								inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),feeLoan.getInstanceId());
						map.put(busAttrMapFormColTemp.getBusAttr(),value);  
					}
					BeanRefUtil.setFieldValue(modFeeLoan,map);
					if(null!=modFeeLoan.getLoanMoney()){
						feeLoan.setLoanMoney(modFeeLoan.getLoanMoney());
					}
				}
				
				busRelateSpFlowDao.update(feeLoan);
			}
			
		} else if (ConstantInterface.TYPE_REPORT_TRIP.equals(instance.getBusType())// 工作汇报申请
				|| ConstantInterface.TYPE_REPORT_DAYLY.equals(instance.getBusType())) {// 工作汇报申请
			Object obj = this.constrObjBaseInfo(instance,FeeLoanReport.class);
			if(null != obj){
				FeeLoanReport feeLoanReport = (FeeLoanReport) obj;
				// 借款说明回调引擎
				this.initLoanReport(feeLoanReport, userInfo,listBusAttrMapFormColTemp,formDataMap);
			}
		} else if (ConstantInterface.TYPE_LOANOFF_DAYLY.equals(instance.getBusType())// 费用报销申请
				|| ConstantInterface.TYPE_LOANOFF_TRIP.equals(instance.getBusType())) {// 费用报销申请
			Object obj = this.constrObjBaseInfo(instance,FeeLoanOff.class);
			if(null != obj){
				FeeLoanOff feeLoanOff = (FeeLoanOff) obj;
				
				//设置报销中的金额
				if(null!= listBusAttrMapFormColTemp && !listBusAttrMapFormColTemp.isEmpty()){
					FeeLoanOff modFeeLoanOff = new  FeeLoanOff();
					Map<String,String> map = new HashMap<String, String>(); 
					for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
						SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
						if(null==inputData){continue;}
						String value = (String) workFlowService.getFormComponentValue(
								inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),feeLoanOff.getInstanceId());
						map.put(busAttrMapFormColTemp.getBusAttr(),value);  
					}
					BeanRefUtil.setFieldValue(modFeeLoanOff,map);
					if(null!=modFeeLoanOff.getLoanOffPreFee()){
						feeLoanOff.setLoanOffPreFee(modFeeLoanOff.getLoanOffPreFee());
					}
				}
				
				
				busRelateSpFlowDao.update(feeLoanOff);
				//查询本次出差总的借款
				if(null!=feeLoanOff.getStatus() 
						&& ConstantInterface.SP_STATE_FINISH.equals(feeLoanOff.getStatus())	){
					//关联的模块数据信息
					this.updateLoanOffRelateMod(feeLoanOff, userInfo,formDataMap);
				}
			}
		} else if (ConstantInterface.TYPE_LEAVE.equals(instance.getBusType())) {// 请假
			Object obj = this.constrObjBaseInfo(instance,Leave.class);
			if(null != obj){
				Leave leave = (Leave) obj;
				leave.setComId(userInfo.getComId());
				leave.setCreator(instance.getCreator());
				// 请假
				this.initLeaveApply(leave, userInfo,listBusAttrMapFormColTemp,formDataMap);
				
			}
		} else if (ConstantInterface.TYPE_OVERTIME.equals(instance.getBusType())) {// 加班
			Object obj = this.constrObjBaseInfo(instance,OverTime.class);
			if(null != obj){
				OverTime overTime = (OverTime) obj;
				overTime.setComId(userInfo.getComId());
				overTime.setCreator(instance.getCreator());
				// 加班
				this.initOverTime(overTime, userInfo,listBusAttrMapFormColTemp,formDataMap);
				
			}
		} else if (ConstantInterface.TYPE_ITOM_EVENTPM.equals(instance.getBusType())) {// IT运维管理-事件管理过程
			Object obj = this.constrObjBaseInfo(instance,EventPm.class);
			if(null != obj){
				EventPm eventPm = (EventPm) obj;
				// IT运维管理-事件管理过程
				this.initEventPm(eventPm, userInfo,listBusAttrMapFormColTemp,mapForBean);
			}
		} else if(ConstantInterface.TYPE_ITOM_ISSUEPM.equals(instance.getBusType())){// IT运维管理-问题管理过程
			Object obj = this.constrObjBaseInfo(instance,IssuePm.class);
			if(null != obj){
				IssuePm issuePm = (IssuePm) obj;
				// IT运维管理-问题管理过程
				this.initIssuePm(issuePm, userInfo,listBusAttrMapFormColTemp,mapForBean);
			}
		} else if(ConstantInterface.TYPE_ITOM_MODIFYPM.equals(instance.getBusType())){// IT运维管理-变更管理过程
			Object obj = this.constrObjBaseInfo(instance,ModifyPm.class);
			if(null != obj){
				ModifyPm modifyPm = (ModifyPm) obj;
				// IT运维管理-变更管理过程
				this.initModifyPm(modifyPm, userInfo,listBusAttrMapFormColTemp,mapForBean);
			}
		} else if(ConstantInterface.TYPE_ITOM_RELEASEPM.equals(instance.getBusType())){// IT运维管理-发布管理过程
			Object obj = this.constrObjBaseInfo(instance,ReleasePm.class);
			if(null != obj){
				ReleasePm releasePm = (ReleasePm) obj;
				// IT运维管理-发布管理过程
				this.initReleasePm(releasePm, userInfo,listBusAttrMapFormColTemp,mapForBean);
			}
		}
		
	}
	
	/**
	 * 发起出差预算流程
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @param userInfo 当前操作人员
	 * @param busType 业务类型
	 * @return
	 */
	public SpFlowInstance addFeeBudget(Integer busMapFlowId, UserInfo userInfo, String busType) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		//添加预算初始数据
		Integer feeBudgetId = this.addFeeBudgetForQuota(userInfo, busType, spFlowInstance.getId());
		
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),feeBudgetId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(feeBudgetId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance; 
	}

	/**
	 * 添加预算基本信息
	 * @param userInfo 当前操作人员
	 * @param busType 业务类型 
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	private Integer addFeeBudgetForQuota(UserInfo userInfo, String busType,
			Integer instanceId) {
		//实例化
		FeeBudget feeBudget = new FeeBudget();
		//设置团队号
		feeBudget.setComId(userInfo.getComId());
		//流程实例化主键
		feeBudget.setInstanceId(instanceId);
		//申请人
		feeBudget.setCreator(userInfo.getId());
		//申请人
		feeBudget.setStatus(ConstantInterface.COMMON_DEF);
		if(busType.equals(ConstantInterface.TYPE_FEE_APPLY_TRIP)){
			feeBudget.setIsBusinessTrip(ConstantInterface.COMMON_YES);
		}else{
			feeBudget.setIsBusinessTrip(ConstantInterface.COMMON_DEF);
		}
		//借款方式：额度借款
		feeBudget.setLoanWay(LoanWayEnum.QUOTA.getValue());
		
		//总借款
		feeBudget.setLoanFeeTotal("0");
		//总销账
		feeBudget.setLoanOffTotal("0");
		//总报销
		feeBudget.setLoanItemTotal("0");
		//销账状态，默认已销账
		feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
		//不是初始化数据
		feeBudget.setInitStatus(0);
		
		
		Integer feeBudgetId = busRelateSpFlowDao.add(feeBudget);
		return feeBudgetId;
	}
	/**
	 * 添加预算基本信息
	 * @param userInfo 当前操作人员
	 * @param busType 业务类型 
	 * @param instanceId 流程实例化主键
	 * @return
	 */
	private FeeBudget constrFeeBudgetForDirect(UserInfo userInfo) {
		//实例化
		FeeBudget feeBudget = new FeeBudget();
		//设置团队号
		feeBudget.setComId(userInfo.getComId());
		//流程实例化主键
		feeBudget.setInstanceId(0);
		//申请人
		feeBudget.setCreator(userInfo.getId());
		//申请状态
		feeBudget.setStatus(ConstantInterface.COMMON_FINISH);
		
		//借款方式：直接借款
		feeBudget.setLoanWay(LoanWayEnum.DIRECT.getValue());
		
		//总借款
		feeBudget.setLoanFeeTotal("0");
		//总销账
		feeBudget.setLoanOffTotal("0");
		//总报销
		feeBudget.setLoanItemTotal("0");
		//销账状态，默认已销账
		feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
		//不是初始化数据
		feeBudget.setInitStatus(0);
		
		return feeBudget;
	}
	
	
	/**
	 * 发起借款申请
	 * @param busMapFlowId 流程配置主键
	 * @param userInfo 当前操作人员
	 * @param feeBudgetId 申请主键
	 * @param busType 业务类型
	 * @param isBusinessTrip 是否为出差借款
	 * @return
	 */
	public SpFlowInstance addLoan(Integer busMapFlowId, UserInfo userInfo,
			Integer feeBudgetId,String busType,Integer isBusinessTrip) {
		//若是已有草稿则需要从草稿中获取数据
		FeeLoan loanParam =  busRelateSpFlowDao.queryLoanDraftFlowByApplyId(feeBudgetId);
		if(null!=loanParam && loanParam.getFlowState().equals(2)){
			Integer instanceId = loanParam.getInstanceId();
			SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(instanceId,userInfo);
			//审批关联附件
			List<SpFlowUpfile> spFlowUpfiles = workFlowService.listSpFiles(spFlowInstance.getId(), userInfo);
			if(null!=spFlowUpfiles && !spFlowUpfiles.isEmpty()){
				spFlowInstance.setSpFlowUpfiles(spFlowUpfiles);
			}
			return spFlowInstance;
		}
		
		
		
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		
		//添加借款业务数据
		FeeLoan feeLoan = new FeeLoan();
		feeLoan.setComId(userInfo.getComId());
		feeLoan.setCreator(userInfo.getId());
		feeLoan.setInstanceId(spFlowInstance.getId());
		feeLoan.setStatus(ConstantInterface.COMMON_DEF);
		feeBudgetId = null== feeBudgetId?0:feeBudgetId;
		feeLoan.setFeeBudgetId(feeBudgetId);//借款申请记录主键
		feeLoan.setLoanMoney("0");//默认借款0元
		
		if(null!=feeBudgetId && feeBudgetId>0){
			FeeBudget feeBudget = (FeeBudget) busRelateSpFlowDao.objectQuery(FeeBudget.class, feeBudgetId);
			//设置
			feeLoan.setIsBusinessTrip(feeBudget.getIsBusinessTrip());
			
			//已用额度
			String loanFeeTotal = feeBudget.getLoanFeeTotal();
			loanFeeTotal = StringUtils.isEmpty(loanFeeTotal)?"0":loanFeeTotal;
			
			//总额度
			String allowedQuota = feeBudget.getAllowedQuota();
			allowedQuota = StringUtils.isEmpty(allowedQuota)?"0":allowedQuota;
			
			//剩余额度
			String allowedLeftQuota = MathExtend.subtract(allowedQuota, allowedQuota);
			feeLoan.setAllowedQuota(allowedLeftQuota);
		}else{
			//设置
			feeLoan.setIsBusinessTrip(isBusinessTrip);
			//设置额度为不限制
			feeLoan.setAllowedQuota("-1");
			
			//添加loanapply 
			FeeBudget feeBudget = this.constrFeeBudgetForDirect(userInfo);
			if(isBusinessTrip == 1){
				feeBudget.setLoanWay(LoanWayEnum.RELATEITEM.getValue());	
			}else{
				feeBudget.setLoanWay(LoanWayEnum.DIRECT.getValue());	
			}
			feeBudget.setAllowedQuota("-1");
			feeBudget.setIsBusinessTrip(isBusinessTrip);
			feeBudgetId = busRelateSpFlowDao.add(feeBudget);
			
			feeLoan.setFeeBudgetId(feeBudgetId);//借款申请记录主键
		}
		//添加借款
		Integer loanId = busRelateSpFlowDao.add(feeLoan);
		
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),loanId,ConstantInterface.TYPE_LOAN_TRIP);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(loanId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance; 
	}
	
	/**
	 * 发起借款申请
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @param userInfo
	 * @param feeBudgetId 申请记录主键
	 * @return
	 */
	public SpFlowInstance addLoanReport(Integer busMapFlowId, UserInfo userInfo,Integer feeBudgetId,String busType) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		//添加借款业务数据
		FeeLoanReport loanReport = new FeeLoanReport();
		loanReport.setComId(userInfo.getComId());
		loanReport.setCreator(userInfo.getId());
		loanReport.setInstanceId(spFlowInstance.getId());
		loanReport.setStatus(ConstantInterface.COMMON_DEF);
		loanReport.setFeeBudgetId(feeBudgetId);//借款申请记录主键
		if(busType.equals(ConstantInterface.TYPE_REPORT_TRIP)){
			loanReport.setBusTripState(ConstantInterface.COMMON_YES);
		}else{
			loanReport.setBusTripState(ConstantInterface.COMMON_DEF);
		}
		Integer loanReportId = busRelateSpFlowDao.add(loanReport);
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),loanReportId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(loanReportId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		//查询是否存在数据映射关系
		initBusDataToForm(busMapFlowId, userInfo, busType, loanReportId,spFlowInstance.getId());
		return spFlowInstance; 
	}

	/**
	 * 业务数据映射到审批页面上
	 * @param busMapFlowId
	 * @param userInfo
	 * @param busType
	 * @param busId
	 * @param instanceId 
	 */
	private void initBusDataToForm(Integer busMapFlowId, UserInfo userInfo, String busType, Integer busId, Integer instanceId) {
		List<FormColMapBusAttr> listFormColMapBusAttr = adminCfgService.listFormColMapBusAttr(userInfo.getComId(),busType,busMapFlowId);
		if(!CommonUtil.isNull(listFormColMapBusAttr)) {//如果存在数据映射关系
			//获取依据信息
			FeeBudget feeBudget = null;
			if(busType.equals(ConstantInterface.TYPE_REPORT_TRIP)){//依据出差总结查询依据详情
				feeBudget = financialService.queryFeeBudgetByLoanReport(busId,userInfo.getComId());
			}else if(busType.equals(ConstantInterface.TYPE_LOANOFF_TRIP)) {//依据出差报销查询依据详情
				feeBudget = financialService.queryFeeBudgetByFeeLoanOff(busId,userInfo.getComId());
			}
			Map<String, String> valueMap = BeanRefUtil.getFieldValueMap(feeBudget);
			for (FormColMapBusAttr formColMapBusAttr : listFormColMapBusAttr) {
				
				//需要处理的字段
				String busAttr = formColMapBusAttr.getBusAttr();
				//表单控件标识
				String formCol = formColMapBusAttr.getFormCol();
				//组件类型
				String componentkey = formColMapBusAttr.getComponentkey();
				 
				//存值主信息
				SpFlowInputData flowInputData = new SpFlowInputData();
				flowInputData.setComId(userInfo.getComId());
				flowInputData.setInstanceId(instanceId);
				flowInputData.setFieldId(Integer.parseInt(formCol));
				flowInputData.setComponentKey(componentkey);
				
				 if(componentkey.equalsIgnoreCase("RelateCrm")
						 || componentkey.equalsIgnoreCase("RelateItem")){
					 Integer formFlowDataId = busRelateSpFlowDao.add(flowInputData);
					 
					 //取得关联的模块数据信息
					 List<FeeBusMod> busMods = feeBudget.getListRelateMods();
					 
					 if(!CommonUtil.isNull(listFormColMapBusAttr)){
						 for (FeeBusMod feeBusMod : busMods) {
							 
							 if(componentkey.equalsIgnoreCase("RelateCrm") 
									 && ConstantInterface.TYPE_ITEM.equals(feeBusMod.getBusType())){
								 break;
							 }
							 if(componentkey.equalsIgnoreCase("RelateItem") 
									 && ConstantInterface.TYPE_CRM.equals(feeBusMod.getBusType())){
								 break;
							 }
							 
							 SpFlowOptData spFlowOptData = new SpFlowOptData();
							 spFlowOptData.setComId(userInfo.getComId());
							 spFlowOptData.setInstanceId(instanceId);
							 spFlowOptData.setFormFlowDataId(formFlowDataId);
							 spFlowOptData.setOptionId(feeBusMod.getBusId());
							 spFlowOptData.setValType("0");
							 spFlowOptData.setContent(feeBusMod.getTripBusName());
							 
							 busRelateSpFlowDao.add(spFlowOptData);
							
						}
					 }
					 
				 }else if(componentkey.equalsIgnoreCase("Text")
						 || componentkey.equalsIgnoreCase("DateComponent")
						 || componentkey.equalsIgnoreCase("NumberComponent")
						 ||componentkey.equalsIgnoreCase("Monitor")){//1对1的数据项存储
					 flowInputData.setContent(valueMap.get(busAttr));
					 busRelateSpFlowDao.add(flowInputData);
				 }else if(componentkey.equalsIgnoreCase("TextArea")){//1对多的数据项存储
					 
					 Integer formFlowDataId = busRelateSpFlowDao.add(flowInputData);
					 SpFlowOptData spFlowOptData = new SpFlowOptData();
					 spFlowOptData.setComId(userInfo.getComId());
					 spFlowOptData.setInstanceId(instanceId);
					 spFlowOptData.setFormFlowDataId(formFlowDataId);
					 spFlowOptData.setOptionId(0);
					 spFlowOptData.setValType("1");
//					 spFlowOptData.setContent(valueMap.get(busAttr));//存短数据类型
					 spFlowOptData.setContentMore(valueMap.get(busAttr));//存长数据类型
					 
					 busRelateSpFlowDao.add(spFlowOptData);
				 }
			}
		}
	}
	
	/**
	 * 构建数据信息
	 * @param instance
	 * @param clz
	 * @param listBusAttrMapFormColTemp
	 * @return
	 */
	public Object constrObjBaseInfo(SpFlowInstance instance,Class<?> clz){
		Object obj = null;
		try {
			obj = clz.newInstance();
			
			Method m;
			obj = clz.newInstance();
			
			String getMethodName = "setId"; 
			m = clz.getMethod(getMethodName, Integer.class);
			m.invoke(obj, instance.getBusId());
			
			getMethodName = "setInstanceId"; 
			m = clz.getMethod(getMethodName, Integer.class);
			m.invoke(obj, instance.getId());
			
			Integer status = null;
			
			Integer flowState = instance.getFlowState();
			Integer spState = instance.getSpState();
			if(null!=spState && ConstantInterface.SP_STATE_PASS.equals(spState) 
					&& (null!=flowState && ConstantInterface.SP_STATE_FINISH.equals(flowState))){
				status = ConstantInterface.COMMON_FINISH;
			}else if(null!=spState && ConstantInterface.SP_STATE_REFUSE.equals(spState) 
					&& (null!=flowState && ConstantInterface.SP_STATE_FINISH.equals(flowState))){
				status = ConstantInterface.COMMON_NO;
			}
			if(null!=status){
				getMethodName = "setStatus"; 
				m = clz.getMethod(getMethodName, Integer.class);
				m.invoke(obj, status);
			}
			
			
		} catch (Exception e) {
		}
		return obj;
	}
 	
	/**
	 * 出差申请业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 * @param listBusAttrMapFormColTemp
	 * @param formDataMap
	 */
	public void initFeeBudget(FeeBudget loanApply,UserInfo userInfo,
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
			Map<String, SpFlowInputData> formDataMap) {
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			Map<String,String> map = new HashMap<String, String>(); 
			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
				SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
				if(null==inputData){continue;}
				String componentKey = inputData.getComponentKey();
				
				if ("RelateItem".equalsIgnoreCase(componentKey)) {
					//删除以前的数据
					busRelateSpFlowDao.delByField("feeBusMod", new String[]{"comId","feeBudgetId","loanWay"},
							new Object[]{userInfo.getComId(),loanApply.getId(),LoanWayEnum.QUOTA.getValue()});
					
					@SuppressWarnings("unchecked")
					List<SpFlowOptData> spFlowOptDatas = (List<SpFlowOptData>) workFlowService.getFormComponentValue(componentKey,inputData.getId(),userInfo.getComId(),loanApply.getInstanceId());
					if(null != spFlowOptDatas && !spFlowOptDatas.isEmpty()){
						for (SpFlowOptData spFlowOptData : spFlowOptDatas) {
							FeeBusMod loanApplyBusMod = new FeeBusMod();
							loanApplyBusMod.setComId(userInfo.getComId());
							
							loanApplyBusMod.setFeeBudgetId(loanApply.getId());
							//设置为额度借款
							loanApplyBusMod.setLoanWay(LoanWayEnum.QUOTA.getValue());
							loanApplyBusMod.setBusId(spFlowOptData.getOptionId());
							loanApplyBusMod.setBusType(ConstantInterface.TYPE_ITEM);
							loanApplyBusMod.setTripBusName(spFlowOptData.getContent());
							busRelateSpFlowDao.add(loanApplyBusMod);
						}
					}
				}else if("RelateCrm".equalsIgnoreCase(componentKey.toLowerCase())){
					//删除以前的数据
					busRelateSpFlowDao.delByField("feeBusMod", new String[]{"comId","feeBudgetId","loanWay"},
							new Object[]{userInfo.getComId(),loanApply.getId(),LoanWayEnum.QUOTA.getValue()});
					@SuppressWarnings("unchecked")
					List<SpFlowOptData> spFlowOptDatas = (List<SpFlowOptData>) workFlowService.getFormComponentValue(componentKey,inputData.getId(),userInfo.getComId(),loanApply.getInstanceId());
					if(null != spFlowOptDatas && !spFlowOptDatas.isEmpty()){
						for (SpFlowOptData spFlowOptData : spFlowOptDatas) {
							FeeBusMod loanApplyBusMod = new FeeBusMod();
							loanApplyBusMod.setComId(userInfo.getComId());
							loanApplyBusMod.setFeeBudgetId(loanApply.getId());
							//设置为额度借款
							loanApplyBusMod.setLoanWay(LoanWayEnum.QUOTA.getValue());
							loanApplyBusMod.setBusId(spFlowOptData.getOptionId());
							loanApplyBusMod.setBusType(ConstantInterface.TYPE_CRM);
							loanApplyBusMod.setTripBusName(spFlowOptData.getContent());
							busRelateSpFlowDao.add(loanApplyBusMod);
						}
					}
				}else{
					String value = (String) workFlowService.getFormComponentValue(componentKey,inputData.getId(),userInfo.getComId(),loanApply.getInstanceId());
					map.put(busAttrMapFormColTemp.getBusAttr(),value);  
				}
			}
			BeanRefUtil.setFieldValue(loanApply,map);
			busRelateSpFlowDao.update(loanApply);
		}
		
	}
	
	/**
	 * 发起报销申请
	 * @param busMapFlowId
	 * @param userInfo
	 * @param busType
	 * @param loanWay
	 * @param feeBudgetId
	 * @param loanReportWay
	 * @param loanReportId
	 * @return
	 */
	public SpFlowInstance addLoanOff(Integer busMapFlowId, UserInfo userInfo,String busType,
			String loanWay,Integer feeBudgetId,String loanReportWay,Integer loanReportId) {
		if(null!=loanReportId && loanReportId>0){//1、出差总结和报销统称为报销；此处为当报销进度刚到出差总结，且为草稿状态时
			FeeLoanOff loanOffParam =  financialService.queryLoanOffFlowByReportId(loanReportId);
			if(null!=loanOffParam && loanOffParam.getFlowState().equals(2)){
				Integer instanceId = loanOffParam.getInstanceId();
				SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(instanceId,userInfo);
				//审批关联附件
				List<SpFlowUpfile> spFlowUpfiles = workFlowService.listSpFiles(spFlowInstance.getId(), userInfo);
				if(null!=spFlowUpfiles && !spFlowUpfiles.isEmpty()){
					spFlowInstance.setSpFlowUpfiles(spFlowUpfiles);
				}
				return spFlowInstance;
			}
		}else if(ConstantInterface.TYPE_LOANOFF_DAYLY.equals(busType)) {
			FeeLoanOff loanOffParam =  financialService.queryLoanOffByFeebId(feeBudgetId, userInfo.getComId());
			if(null!=loanOffParam && loanOffParam.getFlowState().equals(2)){
				Integer instanceId = loanOffParam.getInstanceId();
				SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(instanceId,userInfo);
				//审批关联附件
				List<SpFlowUpfile> spFlowUpfiles = workFlowService.listSpFiles(spFlowInstance.getId(), userInfo);
				if(null!=spFlowUpfiles && !spFlowUpfiles.isEmpty()){
					spFlowInstance.setSpFlowUpfiles(spFlowUpfiles);
				}
				return spFlowInstance;
			}
		}
		
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		//添加出差业务数据
		FeeLoanOff loanOff = new FeeLoanOff();
		loanOff.setComId(userInfo.getComId());
		loanOff.setCreator(userInfo.getId());
		loanOff.setInstanceId(spFlowInstance.getId());
		loanOff.setStatus(ConstantInterface.COMMON_DEF);
		loanOff.setLoanWay(loanWay);
		//是否为差旅报销
		Integer isBusinessTrip = ConstantInterface.TYPE_LOANOFF_TRIP.equals(busType)?1:0;
		loanOff.setIsBusinessTrip(isBusinessTrip);
		if(null == feeBudgetId  || feeBudgetId == 0){
			
			if(LoanReportWayEnum.RELATEITEM.getValue().equals(loanReportWay)){//张总报销的
				FeeBudget feeBudget = this.constrFeeBudgetForDirect(userInfo);
				feeBudget.setLoanWay(LoanWayEnum.ITEMOFF.getValue());
				feeBudget.setIsBusinessTrip(1);
				feeBudgetId =  busRelateSpFlowDao.add(feeBudget);
			}else if(LoanReportWayEnum.DIRECT.getValue().equals(loanReportWay)){
				FeeBudget feeBudget = this.constrFeeBudgetForDirect(userInfo);
				feeBudget.setLoanWay(LoanWayEnum.DIRECTOFF.getValue());
				feeBudget.setIsBusinessTrip(0);
				feeBudgetId =  busRelateSpFlowDao.add(feeBudget);
			}
		}
		loanOff.setFeeBudgetId(feeBudgetId);//费用申请主键
		loanOff.setLoanReportId(loanReportId);//报销说明记录主键
		loanOff.setLoanReportWay(loanReportWay);
		
		//报销金额
		loanOff.setLoanOffItemFee("0");
		//销账金额
		loanOff.setLoanOffBalance("0");
		
		Integer loanOffId = busRelateSpFlowDao.add(loanOff);
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),loanOffId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(loanOffId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		//查询是否存在数据映射关系
		initBusDataToForm(busMapFlowId, userInfo, busType, loanOffId,spFlowInstance.getId());
		return spFlowInstance; 
	}
//	/**
//	 * 借款申请业务回调方法
//	 * @param instance 流程实例对象数据
//	 * @param userInfo 当前操作人
//	 * @param formDataMap2 
//	 */
//	public void initLoan(Loan Loan,UserInfo userInfo,
//			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, Map<String, SpFlowInputData> formDataMap2) {
//		//获取当前审批流程对应表单的控件对象MAP
//		Map<String,SpFlowInputData> formDataMap = workFlowService.querySpFlowInputDataMap(
//				Loan.getInstanceId(),userInfo.getComId());
//		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
//			
//			Map<String,String> map = new HashMap<String, String>(); 
//			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
//				SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
//				if(null==inputData){continue;}
//				String value = (String) workFlowService.getFormComponentValue(
//						inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),Loan.getInstanceId());
//				map.put(busAttrMapFormColTemp.getBusAttr(),value);  
//			}
//			BeanRefUtil.setFieldValue(Loan,map);
//			busRelateSpFlowDao.update(Loan);
//			
//		}
//		
//	}
	
	/**
	 * 通过借款申请修改使用额度
	 * @param loanId
	 * @param userInfo
	 */
	public void updateApplyByLoanId(FeeLoan loanParam, UserInfo userInfo) {
		FeeLoan feeLoan = (FeeLoan) busRelateSpFlowDao.objectQuery(FeeLoan.class, loanParam.getId());
		
		FeeBudget feeBudget = (FeeBudget) busRelateSpFlowDao.objectQuery(FeeBudget.class, feeLoan.getFeeBudgetId());
		
		if(LoanWayEnum.DIRECT.getValue().equals(feeBudget.getLoanWay())){//直接借款，开始挂账
			//设置借款额度
			feeBudget.setAllowedQuota(loanParam.getBorrowingBalance());
			//总借款
			feeBudget.setLoanFeeTotal(loanParam.getBorrowingBalance());
			feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
			busRelateSpFlowDao.update(feeBudget);
			return;
		}
		
		//以前总借款
		String loanFeeTotal = feeBudget.getLoanFeeTotal();
		loanFeeTotal = StringUtils.isEmpty(loanFeeTotal)?"0":loanFeeTotal;
		
		//本次借款金额
		String borrowingBalance = loanParam.getBorrowingBalance();
		borrowingBalance = StringUtils.isEmpty(borrowingBalance)?"0":borrowingBalance;
		//原有借款与本次借款总和
		loanFeeTotal = MathExtend.add(loanFeeTotal, borrowingBalance);
		//总借款
		feeBudget.setLoanFeeTotal(loanFeeTotal);
		
		//总销账
		String loanOffTotal = feeBudget.getLoanOffTotal();
		loanOffTotal = StringUtils.isEmpty(loanOffTotal)?"0":loanOffTotal;
		
		//销账状态
		if(loanFeeTotal.equals(loanOffTotal)){//销账成功
			feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
		}else{//未销账
			feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
		}
		busRelateSpFlowDao.update(feeBudget);
	}
	
	
	

	/**
	 * 借款申请业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 * @param listBusAttrMapFormColTemp
	 * @param formDataMap2 
	 */
	public void initLoanReport(FeeLoanReport loanReport,UserInfo userInfo, 
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
			Map<String, SpFlowInputData> formDataMap) {
		//获取当前审批流程对应表单的控件对象MAP
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			
			Map<String,String> map = new HashMap<String, String>(); 
			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
				SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
				if(null==inputData){continue;}
				String value = (String) workFlowService.getFormComponentValue(
						inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),loanReport.getInstanceId());
				map.put(busAttrMapFormColTemp.getBusAttr(),value);  
			}
			BeanRefUtil.setFieldValue(loanReport,map);
			busRelateSpFlowDao.update(loanReport);
		}else{
			busRelateSpFlowDao.update(loanReport);
		}
	}
	
	
	
//	/**
//	 * 借款申请业务回调方法
//	 * @param instance 流程实例对象数据
//	 * @param userInfo 当前操作人
//	 * @param formDataMap2 
//	 * @param listBusAttrMapFormColTemp2 
//	 */
//	public void initLoanOff(LoanOff LoanOff ,UserInfo userInfo, 
//			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
//			Map<String, SpFlowInputData> formDataMap) {
//		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
//			Map<String,String> map = new HashMap<String, String>(); 
//			
//			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
//				SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
//				if(null==inputData){continue;}
//				String value = (String) workFlowService.getFormComponentValue(
//						inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),LoanOff.getInstanceId());
//				map.put(busAttrMapFormColTemp.getBusAttr(),value);  
//			}
//			BeanRefUtil.setFieldValue(LoanOff,map);
//			
//			busRelateSpFlowDao.update(LoanOff);
//			
//			//查询本次出差总的借款
//			if(null!=LoanOff.getStatus() 
//					&& ConstantInterface.SP_STATE_FINISH.equals(LoanOff.getStatus())	){
//				//开始销账
//				this.updateApplyByLoanOffId(LoanOff, userInfo,formDataMap);
//			}
//		}
//		
//	}

	/**
	 * 通过报销信息修改借款信息
	 * @param loanOff
	 * @param userInfo
	 * @param formDataMap 
	 * @param instance 
	 */
	public void updateLoanOffRelateMod(FeeLoanOff feeLoanOff,
			UserInfo userInfo, Map<String, SpFlowInputData> formDataMap) {
		//销账
		FeeLoanOff loanOffAccount = (FeeLoanOff) busRelateSpFlowDao.objectQuery(FeeLoanOff.class, feeLoanOff.getId());
		FeeBudget feeBudget = (FeeBudget) busRelateSpFlowDao.objectQuery(FeeBudget.class, loanOffAccount.getFeeBudgetId());
		//借款方式
		String loanWay = feeBudget.getLoanWay();
		Integer isBusinessTrip = feeBudget.getIsBusinessTrip();
		if(null == isBusinessTrip ||  isBusinessTrip==0){
			return;
		}
		
		if(LoanWayEnum.ITEMOFF.getValue().equals(loanWay)){//直接借款的
			//删除以前的数据
			busRelateSpFlowDao.delByField("feeBusMod", new String[]{"comId","feeBudgetId","loanWay"},
					new Object[]{userInfo.getComId(),loanOffAccount.getFeeBudgetId(),LoanWayEnum.ITEMOFF.getValue()});
			
			for (Map.Entry<String, SpFlowInputData> entry : formDataMap.entrySet()) {  
				 SpFlowInputData inputData = entry.getValue();  
				 String componentKey = inputData.getComponentKey();
				 @SuppressWarnings("unchecked")
				 Object obj = workFlowService.getFormComponentValue(componentKey,inputData.getId(),userInfo.getComId(),feeLoanOff.getInstanceId());
				 if( obj instanceof List){
						 
					 List<SpFlowOptData> spFlowOptDatas = (List<SpFlowOptData>)obj ;
					 if(null != spFlowOptDatas && !spFlowOptDatas.isEmpty()){
						 for (SpFlowOptData spFlowOptData : spFlowOptDatas) {
							 FeeBusMod loanApplyBusMod = new FeeBusMod();
							 loanApplyBusMod.setComId(userInfo.getComId());
							 
							 loanApplyBusMod.setFeeBudgetId(feeBudget.getId());
							 //设置为额度借款
							 loanApplyBusMod.setLoanWay(LoanWayEnum.ITEMOFF.getValue());
							 loanApplyBusMod.setBusId(spFlowOptData.getOptionId());
							 if ("RelateItem".equalsIgnoreCase(componentKey)) {
								 loanApplyBusMod.setBusType(ConstantInterface.TYPE_ITEM);
							 }else if("RelateCrm".equalsIgnoreCase(componentKey)){
								 loanApplyBusMod.setBusType(ConstantInterface.TYPE_CRM);
							 }else if("RelateMod".equalsIgnoreCase(componentKey)){
								 loanApplyBusMod.setBusType(spFlowOptData.getDataBustype());
							 }else{
								 continue;
							 }
							 loanApplyBusMod.setTripBusName(spFlowOptData.getContent());
							 busRelateSpFlowDao.add(loanApplyBusMod);
						 }
					 }
				 }
			}
			return;
		}
	}
	/**
	 * 通过报销信息修改借款信息
	 * @param loanOff
	 * @param userInfo
	 * @param formDataMap 
	 * @param instance 
	 */
	public void updateApplyByLoanOffId(FeeLoanOff feeLoanOff,UserInfo userInfo) {
		//销账
		FeeLoanOff loanOffAccount = (FeeLoanOff) busRelateSpFlowDao.objectQuery(FeeLoanOff.class, feeLoanOff.getId());
		
		FeeBudget feeBudget = (FeeBudget) busRelateSpFlowDao.objectQuery(FeeBudget.class, loanOffAccount.getFeeBudgetId());
		
		//总借款
		String loanFeeTotal = feeBudget.getLoanFeeTotal();
		loanFeeTotal = StringUtils.isEmpty(loanFeeTotal)?"0":loanFeeTotal;
		
		//总销账
		String loanOffTotal = feeBudget.getLoanOffTotal();
		loanOffTotal = StringUtils.isEmpty(loanOffTotal)?"0":loanOffTotal;
		
		//销账金额
		String loanOffBalance = feeLoanOff.getLoanOffBalance();
		loanOffBalance = StringUtils.isEmpty(loanOffBalance)?"0":loanOffBalance;
		loanOffTotal = MathExtend.add(loanOffTotal, loanOffBalance);
		feeBudget.setLoanOffTotal(loanOffTotal);
		
		//报销金额
		String loanOffItemFee = feeLoanOff.getLoanOffItemFee();
		loanOffItemFee = StringUtils.isEmpty(loanOffItemFee)?"0":loanOffItemFee;
		//总报销
		String loanItemTotal = feeBudget.getLoanItemTotal();
		loanItemTotal =MathExtend.add(loanItemTotal, loanOffItemFee);
		feeBudget.setLoanItemTotal(loanItemTotal);
		
		
		//销账状态
		if(loanFeeTotal.equals(loanOffTotal)){//销账成功
			feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
		}else if("0".equals(loanFeeTotal)){
			feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
		}else{//未销账
			feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
		}
		busRelateSpFlowDao.update(feeBudget);
	}
	
	
	/**
	 * 关联业务状态激活
	 * @param busType 业务类型标识符
	 * @param busId 业务主键
	 * @param clz 业务表的class
	 */
	public void initBusModStatus(String busType,Integer busId,Class<?> clz) {
		
		Object obj = null;
		Method m;
		try {
			obj = clz.newInstance();
			
			String getMethodName = "setId"; 
			m = clz.getMethod(getMethodName, Integer.class);
			m.invoke(obj, busId);
			
			getMethodName = "setStatus"; 
			m = clz.getMethod(getMethodName, Integer.class);
			m.invoke(obj, ConstantInterface.COMMON_YES);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		busRelateSpFlowDao.update("update "+clz.getSimpleName()+" a set a.status=:status where a.id=:id",obj);// 业务激活
	}
	
	/*******************************费用管理结束**********************************/
	
	/*******************************考勤开始**********************************/
	/**
	 * 发起请假申请
	 * @param busMapFlowId
	 * @param userInfo
	 * @param busType
	 * @return
	 */
	public SpFlowInstance addLeaveApply(Integer busMapFlowId, UserInfo userInfo, String busType) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		Leave leave = new Leave();
		leave.setComId(userInfo.getComId());
		leave.setCreator(userInfo.getId());
		leave.setInstanceId(spFlowInstance.getId());
		leave.setStatus(ConstantInterface.COMMON_DEF);
		
		Integer leaveId = busRelateSpFlowDao.add(leave);
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),leaveId,ConstantInterface.TYPE_LEAVE);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(leaveId);
		spFlowInstance.setBusType(ConstantInterface.TYPE_LEAVE);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance;
	}
	/**
	 * 发起加班申请
	 * @param busMapFlowId
	 * @param userInfo
	 * @param busType
	 * @return
	 */
	public SpFlowInstance addOverTimeApply(Integer busMapFlowId, UserInfo userInfo, String busType) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		OverTime overTime = new OverTime();
		overTime.setComId(userInfo.getComId());
		overTime.setCreator(userInfo.getId());
		overTime.setInstanceId(spFlowInstance.getId());
		overTime.setStatus(ConstantInterface.COMMON_DEF);
		
		Integer overTimeId = busRelateSpFlowDao.add(overTime);
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),overTimeId,ConstantInterface.TYPE_OVERTIME);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(overTimeId);
		spFlowInstance.setBusType(ConstantInterface.TYPE_OVERTIME);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance;
	}
	/**
	 * 发起需求处理流程
	 * @param busMapFlowId
	 * @param userInfo
	 * @param busType
	 * @param demandId 
	 * @return
	 */
	public SpFlowInstance addDemanfHandleApply(Integer busMapFlowId, UserInfo userInfo, String busType, Integer demandId) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),demandId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(demandId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance;
	}
	/**
	 * 请假申请业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 * @param formDataMap2 
	 * @param listBusAttrMapFormColTemp2 
	 */
	public void initLeaveApply(Leave leave,UserInfo userInfo, 
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
			Map<String, SpFlowInputData> formDataMap) {
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			Map<String,String> map = new HashMap<String, String>(); 
			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
				SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
				if(null==inputData){continue;}
				String value = (String) workFlowService.getFormComponentValue(inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),leave.getInstanceId());
				map.put(busAttrMapFormColTemp.getBusAttr(),value);  
			}
			BeanRefUtil.setFieldValue(leave,map);
			busRelateSpFlowDao.update(leave);
			
			if(null!=leave.getStatus() && leave.getStatus() == 4){
				attenceService.addAttenceLeaveDetail(leave);
			}
		}
		
	}
	/**
	 * 加班申请业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 * @param listBusAttrMapFormColTemp2 
	 * @param formDataMap2 
	 */
	public void initOverTime(OverTime overTime, UserInfo userInfo, 
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
			Map<String, SpFlowInputData> formDataMap) {
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			Map<String,String> map = new HashMap<String, String>(); 
			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
				SpFlowInputData inputData = formDataMap.get(busAttrMapFormColTemp.getFormCol());
				if(null==inputData){continue;}
				String value = (String) workFlowService.getFormComponentValue(
						inputData.getComponentKey(),inputData.getId(),userInfo.getComId(),overTime.getInstanceId());
				map.put(busAttrMapFormColTemp.getBusAttr(),value);  
			}
			BeanRefUtil.setFieldValue(overTime,map);
			busRelateSpFlowDao.update(overTime);
			
			if(null!=overTime.getStatus() && overTime.getStatus() == 4){
				attenceService.addAttenceOverTimeDetail(overTime);
			}
		}
		
	}
	/*****************************考勤开始************************************/
	
	/*****************************运维过程管理开始************************************/
	
	/**
	 * 新增事项反馈
	 * @param busMapFlowId 映射关系主键
	 * @param userInfo 当前操作员
	 * @param busType 业务类型
	 * @return
	 */
	public SpFlowInstance addEventPm(Integer busMapFlowId, UserInfo userInfo, String busType) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		//添加出差业务数据
		EventPm eventPm = new EventPm();
		eventPm.setComId(userInfo.getComId());
		eventPm.setCreator(userInfo.getId());
		eventPm.setInstanceId(spFlowInstance.getId());
		eventPm.setStatus(ConstantInterface.COMMON_DEF);
		
		//实际开始时间
		String eventStartTimes = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		eventPm.setEventStartTimes(eventStartTimes);
		
		Integer eventPmId = busRelateSpFlowDao.add(eventPm);
		
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),eventPmId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(eventPmId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance; 
	}
	/**
	 * 添加问题过程管理
	 * @param busMapFlowId 映射关系主键
	 * @param userInfo 当前操作员
	 * @param busType 业务类型
	 * @return
	 */
	public SpFlowInstance addIssuePm(Integer busMapFlowId, UserInfo userInfo, String busType) {
		
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		//添加出差业务数据
		IssuePm issuePm = new IssuePm();
		issuePm.setComId(userInfo.getComId());
		issuePm.setCreator(userInfo.getId());
		issuePm.setInstanceId(spFlowInstance.getId());
		issuePm.setStatus(ConstantInterface.COMMON_DEF);
		
		//记录时间
		String issueTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		issuePm.setIssueTime(issueTime);
		
		Integer issuePmId = busRelateSpFlowDao.add(issuePm);
		
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),issuePmId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(issuePmId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance; 
	}
	/**
	 * 添加问题过程管理
	 * @param busMapFlowId 映射关系主键
	 * @param userInfo 当前操作员
	 * @param busType 业务类型
	 * @return
	 */
	public SpFlowInstance addModifyPm(Integer busMapFlowId, UserInfo userInfo, String busType) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		//添加出差业务数据
		ModifyPm modifyPm = new ModifyPm();
		modifyPm.setComId(userInfo.getComId());
		modifyPm.setCreator(userInfo.getId());
		modifyPm.setInstanceId(spFlowInstance.getId());
		modifyPm.setStatus(ConstantInterface.COMMON_DEF);
		
		Integer issuePmId = busRelateSpFlowDao.add(modifyPm);
		
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),issuePmId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(issuePmId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance; 
	}
	/**
	 * 添加问题过程管理
	 * @param busMapFlowId 映射关系主键
	 * @param userInfo 当前操作员
	 * @param busType 业务类型
	 * @return
	 */
	public SpFlowInstance addReleasePm(Integer busMapFlowId, UserInfo userInfo, String busType) {
		//获取业务模块审批配置信息
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,busMapFlowId);
		//启动审批流程
		SpFlowInstance spFlowInstance = workFlowService.initSpFlow(busMapFlow.getFlowId(),userInfo);
		
		//添加出差业务数据
		ReleasePm releasePm = new ReleasePm();
		releasePm.setComId(userInfo.getComId());
		releasePm.setCreator(userInfo.getId());
		releasePm.setInstanceId(spFlowInstance.getId());
		releasePm.setStatus(ConstantInterface.COMMON_DEF);
		
		Integer releasePmId = busRelateSpFlowDao.add(releasePm);
		
		//创建临时流程实例与模块关联关系业务数据映射关系临时表
		formColTempService.addBusAttrMapFormColTemp(busMapFlowId,spFlowInstance.getId(),userInfo.getComId(),releasePmId,busType);
		//初始化审批关联
		spFlowInstance.setStagedItemId(0);
		spFlowInstance.setBusId(releasePmId);
		spFlowInstance.setBusType(busType);
		busRelateSpFlowDao.update(spFlowInstance);
		return spFlowInstance; 
	}

	/**
	 * IT运维管理-事件管理过程业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 */
	public void initEventPm(EventPm eventPm, UserInfo userInfo,
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp,
			Map<String,String> map) {
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			BeanRefUtil.setFieldValue(eventPm,map);
			if(null!=eventPm.getStatus()
					&& ConstantInterface.SP_STATE_FINISH.equals(eventPm.getStatus() )){
				String eventEndTimes = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				eventPm.setEventEndTimes(eventEndTimes);
			}
			busRelateSpFlowDao.update(eventPm);
		}
		
	}
	/**
	 * IT运维管理-问题管理过程业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 * @param listBusAttrMapFormColTemp2 
	 * @param formDataMap2 
	 */
	public void initIssuePm(IssuePm issuePm, UserInfo userInfo, 
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
			Map<String,String> map) {
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			
			BeanRefUtil.setFieldValue(issuePm,map);
			if(null!=issuePm.getStatus() 
					&& ConstantInterface.SP_STATE_FINISH.equals(issuePm.getStatus())){
				String eventEndTimes = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				issuePm.setIssueEndTimes(eventEndTimes);
			}
			busRelateSpFlowDao.update(issuePm);
		}
	}
	/**
	 * IT运维管理-变更管理过程业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 * @param formDataMap2 
	 * @param listBusAttrMapFormColTemp2 
	 */
	public void initModifyPm(ModifyPm modifyPm, UserInfo userInfo, 
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
			Map<String,String> map) {
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			BeanRefUtil.setFieldValue(modifyPm,map);
			if(null!=modifyPm.getStatus() 
					 && ConstantInterface.SP_STATE_FINISH.equals(modifyPm.getStatus())){
				String eventEndTimes = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				modifyPm.setModifyCloseDate(eventEndTimes);
			}
			busRelateSpFlowDao.update(modifyPm);
		}
		
	}
	/**
	 * IT运维管理-发布管理过程业务回调方法
	 * @param instance 流程实例对象数据
	 * @param userInfo 当前操作人
	 * @param listBusAttrMapFormColTemp2 
	 * @param formDataMap2 
	 */
	public void initReleasePm(ReleasePm releasePm, UserInfo userInfo, 
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp, 
			Map<String,String> map) {
		if(!CommonUtil.isNull(listBusAttrMapFormColTemp)){
			
			BeanRefUtil.setFieldValue(releasePm,map);
			
			if(null!=releasePm.getStatus() 
					&& ConstantInterface.SP_STATE_FINISH.equals(releasePm.getStatus())){
				String eventEndTimes = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				releasePm.setReleaseCloseDate(eventEndTimes);
			}
			busRelateSpFlowDao.update(releasePm);
		}
		
	}
	/*****************************运维过程管理开始************************************/

	/**
	 * 删除无效业务数据
	 * @param userInfo
	 */
	public void delBusDataForNewSp(UserInfo userInfo) {
		
		busRelateSpFlowDao.delUnStartFeeBudgetForLoan(userInfo);
		busRelateSpFlowDao.delUnStartFeeBudgetForLoanOff(userInfo);
		// 删除无效的借款业务记录
		busRelateSpFlowDao.delRelateTableWithInsId(FeeBudget.class,userInfo);
		// 删除无效的借款业务记录
		busRelateSpFlowDao.delRelateTableWithInsId(FeeLoan.class,userInfo);
		// 删除无效的借款业务记录
		busRelateSpFlowDao.delRelateTableWithInsId(FeeLoanReport.class,userInfo);
		// 删除无效的报销业务记录
		busRelateSpFlowDao.delRelateTableWithInsId(FeeLoanOff.class,userInfo);
		
		// 删除无效的加班记录
		busRelateSpFlowDao.delRelateTableWithInsId(OverTime.class,userInfo);
		// 删除无效的请假记录
		busRelateSpFlowDao.delRelateTableWithInsId(Leave.class,userInfo);
		
		
		busRelateSpFlowDao.delUnuseEventPm(userInfo);//删除无效的事件管理
		busRelateSpFlowDao.delUnuseIssuePm(userInfo);//删除无效的过程管理
		busRelateSpFlowDao.delUnuseModifyPm(userInfo);//删除无效的问题管理
		busRelateSpFlowDao.delUnuseReleasePm(userInfo);//删除无效的发布管理
		
	}

	/**
	 * 通过抓取数据的方式取得需要映射的数据
	 * @param userInfo 当前操作人员
	 * @param instanceId 流程实例化主键
	 * @param loanId  借款主键
	 * @return
	 */
	public FeeLoan getLoanForBalance(UserInfo userInfo, Integer instanceId, Integer loanId) {
		//
		FeeLoan loan = (FeeLoan) financialService.getLoanForBalance(userInfo,loanId);
		Map<String,String> mapForBean = workFlowService.mapForBean(userInfo, instanceId);
		if(null!=mapForBean && !mapForBean.isEmpty()){
			BeanRefUtil.setFieldValue(loan,mapForBean);
		}
		
		return loan;
	}
	
	/**
	 * 通过抓取数据的方式取得需要映射的数据
	 * @param userInfo 当前操作人员
	 * @param instanceId 流程实例化主键
	 * @param loanOffId  报销主键
	 * @return
	 */
	public FeeLoanOff getLoanOffForBalance(UserInfo userInfo, Integer instanceId, Integer loanOffId) {
		//
		FeeLoanOff loanOff = (FeeLoanOff) financialService.getLoanOffForBalance(userInfo,loanOffId);
		Map<String,String> mapForBean = workFlowService.mapForBean(userInfo, instanceId);
		if(null!=mapForBean && !mapForBean.isEmpty()){
			BeanRefUtil.setFieldValue(loanOff,mapForBean);
		}
		
		return loanOff;
	}
}
