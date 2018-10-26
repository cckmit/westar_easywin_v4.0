package com.westar.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.enums.LoanReportWayEnum;
import com.westar.base.enums.LoanWayEnum;
import com.westar.base.enums.SysInitEnum;
import com.westar.base.enums.TripFeeEnum;
import com.westar.base.model.BusAttrMapFormColTemp;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.BusRemindUser;
import com.westar.base.model.Consume;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FeeBusMod;
import com.westar.base.model.FeeDirectBalance;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.FeeLoanReport;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.StatisticFeeCrmVo;
import com.westar.base.pojo.StatisticFeeItemVo;
import com.westar.base.util.BeanRefUtil;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.MathExtend;
import com.westar.core.dao.FinancialDao;
import com.westar.core.web.PaginationContext;

@Service
public class FinancialService {

	@Autowired
	FinancialDao financialDao;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	AdminCfgService adminCfgService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	FormColTempService formColTempService;
	


	/**
	 * 报销催办
	 * @param userInfo 当前操作元员
	 * @param busId 业务主键
	 * @return
	 */
	public Map<String, Object> queryRemindConf(UserInfo userInfo,
											   Integer busId,String busType) {
		Map<String, Object> map = new HashMap<String, Object>();
		SpFlowInstance flowInstance = (SpFlowInstance) financialDao.objectQuery(SpFlowInstance.class, busId);
		map.put("busModName", flowInstance.getFlowName());
		if(flowInstance.getFlowState().equals(ConstantInterface.SP_STATE_FINISH)){
			map.put("status", "f");
			map.put("info","事项审批已经结束！");
		}else{
			map.put("status", "y");
			String defMsg = "请尽快办理“"+flowInstance.getFlowName()+"”事项审批！";
			map.put("defMsg", defMsg);

			//事项的执行人员信息
			List<BusRemindUser> listReminderUser = workFlowService.listSpFlowRemindExecutor(userInfo,busId,busType);
			map.put("listRemindUser", listReminderUser);
		}
		return map;
	}

	/**
	 * 获取个人某时间出差记录
	 * @return
	 */
	public List<FeeBudget> listLoanApplyByDate(UserInfo creator,String date){
		return financialDao.listLoanApplyByDate(creator, date);
	}
	
	/**
	 * 分页查询有效额度申请信息
	 * @param curUser 当前操作人员
	 * @param loanApply 额度申请查询条件
	 * @return
	 */
	public PageBean<FeeBudget> listPagedLoanApplyForStartSelect(UserInfo curUser,
			FeeBudget loanApply) {
		
		List<FeeBudget> loanApplies = null;
		if(TripFeeEnum.YES.getValue() == loanApply.getIsBusinessTrip()){
			loanApplies = financialDao.listPagedLoanTripApplyForStartSelect(curUser,loanApply);
		}else{
			loanApplies = financialDao.listPagedLoanDailyApplyForStartSelect(curUser,loanApply);
		}
		PageBean<FeeBudget> pageBean = new PageBean<FeeBudget>();
		pageBean.setRecordList(loanApplies);
		
		//已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		pageBean.setTotalCount(total);
		return pageBean;
	}
	/**
	 * 分页查询有效额度申请信息
	 * @param userInfo 当前操作人员
	 * @param loanApplyParam 额度申请查询条件
	 * @return
	 */
	public PageBean<FeeBudget> listPagedLoanApplyForOffSelect(UserInfo userInfo,
			FeeBudget loanApplyParam) {
		
		List<FeeBudget> loanApplies = financialDao.listPagedFeeBudgetForOffSelect(userInfo,loanApplyParam);
		
		if(!CommonUtil.isNull(loanApplies)) {
			for (FeeBudget feeBudget : loanApplies) {
				//查询处于汇报成功未报销的数据
				FeeLoanOff feeLoanOffUnOff = financialDao.queryFeeLoanOffUnOff(feeBudget.getId());
				//查询汇报成功并且报销失败的汇报记录
				List<FeeLoanReport> reports = financialDao.queryFeeLoanReport(feeBudget.getId());
				if(!CommonUtil.isNull(reports)) {
					feeBudget.setCanLoanOffRepId(reports.get(0).getId());
				}
				if(!CommonUtil.isNull(feeLoanOffUnOff)) {
					feeBudget.setCanLoanOffRepId(feeLoanOffUnOff.getLoanReportId());
				}
			}
		}
		
		PageBean<FeeBudget> pageBean = new PageBean<FeeBudget>();
		pageBean.setRecordList(loanApplies);
		
		//已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		pageBean.setTotalCount(total);
		return pageBean;
	}
	
	/**
	 * 分页查询有效借款申请信息
	 * @param curUser 当前操作人员
	 * @param loan 额度申请查询条件
	 * @return
	 */
	public PageBean<FeeLoan> listPagedLoanForOffSelect(UserInfo curUser,
			FeeLoan loan) {
		
		List<FeeLoan> loanApplies = financialDao.listPagedLoanForOffSelect(curUser,loan);
		PageBean<FeeLoan> pageBean = new PageBean<FeeLoan>();
		pageBean.setRecordList(loanApplies);
		
		//已有数据的总数
		Integer total = PaginationContext.getTotalCount();
		pageBean.setTotalCount(total);
		return pageBean;
	}
	/**
	 * 获取权限下的出差记录列表
	 * @param curUser
	 * @param loanApply
	 * @return
	 */
	public PageBean<FeeBudget> listLoanApplyOfAuth(UserInfo curUser,
			FeeBudget loanApply) {
		
		//验证当前登录人是否是督察人员
	    boolean isForceInPersion = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_FINANCIAL);
	    //分页查询额度申请信息
		List<FeeBudget> listLoanApply = financialDao.listPagedLoanApplyOfAuth(curUser,loanApply,isForceInPersion);
		
		//填充借款信息和销账信息
		if(null!=listLoanApply && !listLoanApply.isEmpty()){
			//额度申请主键，额度申请
			Map<Integer, FeeBudget> map = new HashMap<Integer, FeeBudget>();
			//获取审核通过的出差申请的借款记录
			for (FeeBudget feeBudget : listLoanApply) {
				//系统初始化的默认额度申请完成了
				if(feeBudget.getInitStatus().equals(SysInitEnum.YES.getValue())){
					feeBudget.setStatus(ConstantInterface.COMMON_FINISH);
				}
				//额度申请主键
				Integer feeBudgetId = feeBudget.getId();
				if(null!=map.get(feeBudgetId)){
					continue;
				}else{
					map.put(feeBudgetId, feeBudget);
				}
				
				UserInfo user = new UserInfo();
				user.setComId(curUser.getComId());
				user.setId(feeBudget.getCreator());
				//额度借款信息
				List<FeeLoan> listLoan = financialDao.listLoanForQuota(user,feeBudget.getId());
				feeBudget.setListFeeLoan(listLoan);
				
				if(ConstantInterface.COMMON_FINISH.equals(feeBudget.getStatus())){//只有审核批准的出差申请才有借款的权限
					
					FeeLoanOff loanOffAccount = new FeeLoanOff();
					loanOffAccount.setFeeBudgetId(feeBudget.getId());
					loanOffAccount.setLoanWay(LoanWayEnum.QUOTA.getValue());
					//查询报销成功的或是正在审批的
					List<FeeLoanOff> loanOffAccountNs = financialDao.listLoanOffN(curUser,loanOffAccount);
					feeBudget.setListLoanOff(loanOffAccountNs);
//					//工作成果信息
//					List<FeeLoanReport> listLoanReport = financialDao.listLoanReport(user,feeBudget.getId());
//					feeBudget.setListLoanReport(listLoanReport);
//					FeeLoanOff loanOffDoing =  financialDao.queryLoanOffOfReportRunning(curUser.getComId(),feeBudget.getCreator(),feeBudget.getId());;
//					feeBudget.setLoanOffDoing(loanOffDoing);
				}
			}
		}
		PageBean<FeeBudget> pageBean = new PageBean<FeeBudget>();
		pageBean.setRecordList(listLoanApply);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		
		return pageBean;
	}
	/**
	 * 列出权限下借款记录
	 * @param curUser 当前操作人
	 * @param loan
	 * @return
	 */
	public List<FeeLoan> listLoanOfAuth(UserInfo curUser, FeeLoan loan) {
		boolean isForceInPersion = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_FINANCIAL);
		List<FeeLoan> listLoan = financialDao.listPagedLoanOfAuth(curUser,loan,isForceInPersion);
		return listLoan;
	}
	/**
	 * 取得借款信息
	 * @param curUser 当前操作员
	 * @param loan 借款信息
	 * @return
	 */
	public PageBean<FeeLoan> listLoanOfAuthV2(UserInfo curUser, FeeLoan loan) {
		//是否为差旅监督人员
		boolean isForceInPersion = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_FINANCIAL);
		//查询借款信息
		List<FeeLoan> listLoan = financialDao.listPagedLoanOfAuth(curUser,loan,isForceInPersion);
		if(!CommonUtil.isNull(listLoan)){
			Map<Integer,Integer> feeBudgetId_StateMap = new HashMap<>();
			for (FeeLoan feeLoan : listLoan) {
				Integer feeBudgetId = feeLoan.getFeeBudgetId();
				Integer reLoanState = feeBudgetId_StateMap.get(feeBudgetId);
				if(null!=reLoanState){
					feeLoan.setReLoanState(0);
					continue;
				}
				
				//非创建人不可借款
				Integer creator = feeLoan.getCreator();
				if(!creator.equals(curUser.getId())){
					feeBudgetId_StateMap.put(feeBudgetId,0);
					feeLoan.setReLoanState(0);
					continue;
				}
				//额度用完不能借款
				String allowedQuota = feeLoan.getAllowedQuota();
				if(MathExtend.compareTo(allowedQuota,"0")<=0){
					feeBudgetId_StateMap.put(feeBudgetId,0);
					feeLoan.setReLoanState(0);
					continue;
				}
				//按时间借款的状态
				List<FeeLoan> feeLoans = financialDao.listRecentFeeLoan(feeLoan.getFeeBudgetId(),creator);
				Integer isBusinessTrip = feeLoan.getIsBusinessTrip();
				if(TripFeeEnum.NO.getValue().equals(isBusinessTrip)){
					if(!CommonUtil.isNull(feeLoans)) {
						FeeLoan feeLoanRecent =  feeLoans.get(0);
						Integer status = feeLoanRecent.getStatus();
						if(status.equals(-1)){//第一个就是报销失败的
							feeBudgetId_StateMap.put(feeBudgetId,1);
							feeLoan.setReLoanState(1);
						}else{
							SpFlowInstance flowInstance = this.getBaseSpFlowInstanceGetById(feeLoanRecent.getInstanceId());
							if(flowInstance != null && flowInstance.getFlowState()==0) {
								feeBudgetId_StateMap.put(feeBudgetId,1);
								feeLoan.setReLoanState(1);
							}else {
								feeBudgetId_StateMap.put(feeBudgetId,0);
								feeLoan.setReLoanState(0);
							}
						}
					}else {
						feeBudgetId_StateMap.put(feeBudgetId,0);
						feeLoan.setReLoanState(0);
					}
					
				}else{
					
					String loanApplyEndDateStr = feeLoan.getLoanApplyEndDate();
					String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
					
					Long loanApplyEndDate = DateTimeUtil.parseDate(loanApplyEndDateStr, DateTimeUtil.yyyy_MM_dd).getTime();
					Long nowDate = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd).getTime();
					
					if(loanApplyEndDate>nowDate){
						feeBudgetId_StateMap.put(feeBudgetId,1);
						feeLoan.setReLoanState(1);
					}else{
						feeBudgetId_StateMap.put(feeBudgetId,0);
						feeLoan.setReLoanState(0);
					}
					
					FeeLoan feeLoanRecent =  feeLoans.get(0);
					Integer status = feeLoanRecent.getStatus();
					if(status.equals(-1)){//第一个就是报销失败的
						feeBudgetId_StateMap.put(feeBudgetId,1);
						feeLoan.setReLoanState(1);
					}else{
						SpFlowInstance flowInstance = this.getBaseSpFlowInstanceGetById(feeLoanRecent.getInstanceId());
						if(flowInstance != null && flowInstance.getFlowState()==0) {
							feeBudgetId_StateMap.put(feeBudgetId,1);
							feeLoan.setReLoanState(1);
						}else {
							feeBudgetId_StateMap.put(feeBudgetId,0);
							feeLoan.setReLoanState(0);
						}
					}
				}
				
				
			}
		}
		
		
		PageBean<FeeLoan> pageBean = new PageBean<FeeLoan>();
		pageBean.setRecordList(listLoan);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}

	/**
	 * 取得借款信息
	 * @param curUser 当前操作员
	 * @param loan 借款信息
	 * @return
	 */
	public PageBean<FeeLoan> listPersonalLoanOfAuthV2(UserInfo curUser, FeeLoan loan) {
		List<FeeLoan> listLoan = financialDao.listPagedPersonalLoanOfAuth(curUser,loan);
		PageBean<FeeLoan> pageBean = new PageBean<FeeLoan>();
		pageBean.setRecordList(listLoan);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	
	/**
	 * 分页获取权限下报销记录
	 * @param curUser 当前操作人
	 * @param loanOff 报销筛选参数
	 * @return
	 */
	public PageBean<FeeLoanOff> listLoanOffOfAuthV2(UserInfo curUser,
			FeeLoanOff loanOff,Integer pageNum) {
		//验证当前登录人是否是督察人员
	    Boolean isForceInPersion = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_FINANCIAL);
	    List<FeeLoanOff> loanOffAccounts = financialDao.listLoanOffOfAuthV2(curUser,loanOff,isForceInPersion);
	    
	    if(!CommonUtil.isNull(loanOffAccounts)){
	    	Map<String,Integer> map = new  HashMap<String,Integer>();
	    	for (FeeLoanOff feeLoanOff : loanOffAccounts) {
	    		//依据主键
				Integer feeBudgetId = feeLoanOff.getFeeBudgetId(); 
				//汇报主键
				Integer loanReportId = feeLoanOff.getLoanReportId();
				//创建人
				Integer creator = feeLoanOff.getCreator();
				//避免重复查询同一依据和汇报的数据主键
				String key = feeBudgetId+"_"+(null == loanReportId?0:loanReportId);
				Integer reLoanState = map.get(key);
				if(null!=reLoanState){//已有数据，则跳过
					feeLoanOff.setReLoanState(0);
					map.put(key,0);
					continue;
				}
				//非当前操作人员，不能继续操作
				if(!curUser.getId().equals(creator)){
					feeLoanOff.setReLoanState(0);
					map.put(key,0);
					continue;
				}
				Integer isBusinessTrip = feeLoanOff.getIsBusinessTrip();
				List<FeeLoanOff> feeLoanOffs = financialDao.listAllLoanOff(feeBudgetId,loanReportId,creator,isBusinessTrip);
				//未开始报销的，无再次操作权限
				if(CommonUtil.isNull(feeLoanOffs)){
					map.put(key,0);
					feeLoanOff.setReLoanState(0);
					continue;
				}
				//一般借款只能成功报销一次，一次成功的汇报只有一次成功的报销
				FeeLoanOff feeLoanOffRecent =  feeLoanOffs.get(0);
				Integer status = feeLoanOffRecent.getStatus();
				if(status.equals(-1) && !LoanWayEnum.DIRECT.getValue().equals(feeLoanOffRecent.getLoanWay())){//第一个就是报销失败的
					map.put(key,1);
					feeLoanOff.setReLoanState(1);
				}else{
					SpFlowInstance flowInstance = this.getBaseSpFlowInstanceGetById(feeLoanOffRecent.getInstanceId());
					if(flowInstance != null && flowInstance.getFlowState()==0) {
						map.put(key,1);
						feeLoanOff.setReLoanState(1);
					}else {
						map.put(key,0);
						feeLoanOff.setReLoanState(0);
					}
					
				}
			}
	    }

        PageBean<FeeLoanOff> pageBean = new PageBean<FeeLoanOff>();
	    pageBean.setRecordList(loanOffAccounts);
	    pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}


	/**
	 * 分页获取权限下报销记录
	 * @param curUser 当前操作人
	 * @param loanOff 报销筛选参数
	 * @return
	 */
	public PageBean<FeeLoanOff> listPersonalLoanOffOfAuthV2(UserInfo curUser,FeeLoanOff loanOff) {
		List<FeeLoanOff> loanOffAccounts = financialDao.listPersonalLoanOffOfAuthV2(curUser,loanOff);
		PageBean<FeeLoanOff> pageBean = new PageBean<>();
		pageBean.setRecordList(loanOffAccounts);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}

	/**
	 * 报销详情内容
	 * @param feeLoanOffParam
	 * @param userInfo
	 * @return
	 */
	public FeeLoanOff getLoanOffAccountForDetails(FeeLoanOff feeLoanOffParam,UserInfo userInfo){
		FeeLoanOff feeLoanOff = financialDao.getLoanOffAccountForDetails(feeLoanOffParam,userInfo);
		if(null != feeLoanOffParam.getId() && feeLoanOffParam.getId() > 0){//如果有报销的id则查询报销
			Map<String,String> mapForBean = workFlowService.mapForBean(userInfo, feeLoanOff.getInstanceId());
			BeanRefUtil.setFieldValue(feeLoanOff,mapForBean);
		}
		return feeLoanOff;
	}

	/**
	 * 获取出差信息
	 * @param id
	 * @return
	 */
	public FeeBudget getFeeBudGetById(Integer id,UserInfo userInfo){
		return financialDao.getLoanApplyForDetails(id,userInfo);
	}

    /**
     * 获取FeeBudGet
     * @param feeBudgetId
     * @return
     */
	public FeeBudget getBaseFeeBudGetById(Integer feeBudgetId,UserInfo userInfo){
		FeeBudget feeBudget = financialDao.getBaseFeeBudGetById(feeBudgetId);
		if(null!=feeBudget && feeBudget.getStatus()!=4){
			Map<String,String> mapForBean = workFlowService.mapForBean(userInfo, feeBudget.getInstanceId());
			BeanRefUtil.setFieldValue(feeBudget,mapForBean);
		}
	    return feeBudget;
    }
	/**
	 * 取得预算产生的所有数据信息
	 * @param feeBudgetId
	 * @param userInfo
	 * @return
	 */
	public FeeBudget getFeeBugdetDetail(Integer feeBudgetId,UserInfo userInfo){
		FeeBudget feeBudget = this.getBaseFeeBudGetById(feeBudgetId, userInfo);
		
		UserInfo user = new UserInfo();
		user.setComId(userInfo.getComId());
		user.setId(feeBudget.getCreator());
		
		
		if(ConstantInterface.COMMON_FINISH.equals(feeBudget.getStatus())){//只有审核批准的出差申请才有借款的权限
			
			//额度借款信息
			List<FeeLoan> listLoan = financialDao.listLoanForQuota(user,feeBudgetId);
			feeBudget.setListFeeLoan(listLoan);
			
			FeeLoanOff loanOffAccount = new FeeLoanOff();
			loanOffAccount.setFeeBudgetId(feeBudget.getId());
			loanOffAccount.setLoanWay(LoanWayEnum.QUOTA.getValue());
			
			//查询报销记录信息
			List<FeeLoanOff> listLoanOff = financialDao.listFeeLoanOffForFeebudgetDetail(userInfo,loanOffAccount);
			feeBudget.setListLoanOff(listLoanOff);
			
		}
		
		return feeBudget;
	}

    public FeeLoan getFeeLoanForDetails(){
        return null;
    }

    /**
     * 获取SpFlowInstance
     * @param id
     * @return
     */
    public SpFlowInstance getBaseSpFlowInstanceGetById(Integer id){
        return (SpFlowInstance) financialDao.objectQuery(SpFlowInstance.class,id);
    }

	/**
	 * 借款详情内容
	 * @param loan
	 * @param userInfo
	 * @return
	 */
	public FeeLoan getLoanForDetails(FeeLoan loanParam,UserInfo userInfo){
		FeeLoan feeLoan = financialDao.getLoanForDetails(loanParam,userInfo);
		if((null != loanParam.getInstanceId() && loanParam.getInstanceId() > 0)){
			Map<String,String> mapForBean = workFlowService.mapForBean(userInfo, loanParam.getInstanceId());
			BeanRefUtil.setFieldValue(feeLoan,mapForBean);
		}
		return feeLoan;
	}
	/**
	 * 验证操作人是否把所有借款都销账了
	 * @param cusUser 当前操作人员
	 * @param feeBudgetId 预算主键
	 * @param busType 业务类型
	 * @return
	 */
	public Map<String, Object> checkLoanOffAll(UserInfo cusUser, Integer feeBudgetId,String busType) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		
		//查询本次出差申请
		FeeBudget loanApply = financialDao.queryLoanApply4Check(feeBudgetId);
		
		Integer isBusinessTrip = loanApply.getIsBusinessTrip();
		if(isBusinessTrip.equals(ConstantInterface.COMMON_YES)){//是差旅
			
			/************时间比对开始**************/
			String endTimeStr = loanApply.getEndTime();
			if(null != endTimeStr){
				String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
				Long endTime = DateTimeUtil.parseDate(endTimeStr, DateTimeUtil.yyyy_MM_dd).getTime();
				Long nowTime = DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.yyyy_MM_dd).getTime();
				if(endTime>=nowTime){//截止时间没有过，还可以借款
					map.put("status","y");
				}else{
					map.put("status","f");
					map.put("info","出差计划截止时间已过,需补填出差计划！");
				}
			}
			/************时间比对结束**************/
			
			//本次总借款
			String loanFeeTotal = loanApply.getLoanFeeTotal();
			loanFeeTotal = StringUtils.isEmpty(loanFeeTotal)?"0":loanFeeTotal;
			
			//申请额度
			String allowedQuota = loanApply.getAllowedQuota();
			allowedQuota = StringUtils.isEmpty(allowedQuota)?"0":allowedQuota;
			
			/************借款额度比对开始**************/
			//没有过截止时间
			if(map.get("status").equals("y")){
				//如果申请额度 >借款额度
				if(MathExtend.compareTo(allowedQuota,loanFeeTotal)>0){//还可以借款
					map.put("status","y");
				}else{
					map.put("status","f");
					map.put("info","本次出差审批额度已用完,不能借款");
				}
			}
			/************借款额度比对开始**************/
			
			/************销账比对开始**************/
			//额度没有用完
			if(map.get("status").equals("y")){
				//查人员总借款和总销账信息
				FeeBudget allApply = financialDao.queryTotalApplyFee(cusUser.getComId(),loanApply.getCreator(),loanApply.getIsBusinessTrip());
				Integer totalBorrow = allApply.getTotalBorrow();
				totalBorrow = totalBorrow == null?0:totalBorrow;
				
				Integer totalOff = allApply.getTotalOff();
				totalOff = totalOff == null?0:totalOff;
				
				Integer totalNoOff = totalBorrow-totalOff;
				if(totalNoOff>=4000){
					map.put("status","f");
					map.put("info","超过4000元未销账,不能借款");
				}else{
					map.put("status","y");
				}
				
			}
			/************销账比对开始**************/
			
		}else{//不是差旅
			map.put("status","y");
		}
//		//验证方法要重新，这直接返回TRUE
//		Loan loan = financialDao.queryLoanIsNotWriteOff(cusUser);
//		if(CommonUtil.isNull(loan)){//借款全部销账了；可以继续借款
//			map.put("status","yes");
//		}else{
//			map.put("status","no");
//			map.put("msg","您的“<span class=\"red\">"+loan.getFlowName()+"</span>”未销账;<br/><span class=\"red\">借款原则：先销账，再借款。</span>");
//			map.put("instanceId",loan.getInstanceId());
//		}
		if(map.get("status").equals("y")){//可以借款，返回数据信息
			List<BusMapFlow> listBusMapFlows = adminCfgService.listBusMapFlowByAuth(cusUser,busType);
			map.put("listBusMapFlows",listBusMapFlows);
		}
		return map;
	}
	/**
	 * 验证是否能够报销费用
	 * 1、报销成功的不能报销
	 * 2、正在报销的不能报销
	 * @param sessionUser 当前操作人员
	 * @param feeLoanOff 报销基本条件
	 * @return
	 */
	public Map<String, Object> checkLoanRep4Off(UserInfo sessionUser, FeeLoanOff feeLoanOff) {
		Map<String,Object> map = new HashMap<String, Object>();
		//查询不能报销的
		List<FeeLoanOff> loanOffAccountNs = financialDao.listLoanOffN(sessionUser,feeLoanOff);
		FeeLoanOff loanOffAccountN = loanOffAccountNs.get(0);
		if(loanOffAccountN.getLoanRepStatus().equals(ConstantInterface.COMMON_FINISH)){//汇报成功了
			//报销状态
			Integer status = loanOffAccountN.getStatus();
			//报销流程审批状态
			Integer flowState = loanOffAccountN.getFlowState();
			if(null == status || status.equals(ConstantInterface.COMMON_NO)){//没有报销过,或是报销失败过的
				map.put("status","y");
			}else if(status.equals(ConstantInterface.COMMON_FINISH)){//报销成功了，不能报销
				map.put("status","f");
				map.put("info","报销已成功,不能重复报销");
			}else if(status.equals(ConstantInterface.COMMON_YES) //正在报销,流程刚开始
					&& flowState.equals(ConstantInterface.COMMON_DEF) ){
				map.put("status","y");
			}else if(status.equals(ConstantInterface.COMMON_DEF) //流程为草稿状态
					&& flowState != null && flowState==2 ){
				map.put("status","y");
				map.put("flowState",flowState);
				map.put("instanceId",loanOffAccountN.getInstanceId());
			}else if(status.equals(ConstantInterface.COMMON_YES)){//正在报销,流程已开始
				map.put("status","f");
				map.put("info","正在报销,不能重复报销");
			}else {
				map.put("status","y");
			}
		}else{
			map.put("status","f");
			map.put("info","工作汇报未成功,不能报销");
		}
		if(map.get("status").equals("y")){
			String busType = null;
			if(loanOffAccountN.getIsBusinessTrip().equals(ConstantInterface.COMMON_YES)){
				busType = ConstantInterface.TYPE_LOANOFF_TRIP;
				List<BusMapFlow> listBusMapFlows = adminCfgService.listBusMapFlowByAuth(sessionUser,busType);
				map.put("listBusMapFlows",listBusMapFlows);
				
			}else if(loanOffAccountN.getIsBusinessTrip().equals(ConstantInterface.COMMON_DEF)){
				busType = ConstantInterface.TYPE_LOANOFF_DAYLY;
				List<BusMapFlow> listBusMapFlows = adminCfgService.listBusMapFlowByAuth(sessionUser,busType);
				map.put("listBusMapFlows",listBusMapFlows);
			}
			
			map.put("busType",busType);
		}
		return map;
	}
	/**
	 * 验证是否能够报销费用
	 * 1、报销成功的不能报销
	 * 2、正在报销的不能报销
	 * @param sessionUser 当前操作人员
	 * @param feeLoanOff 报销基本条件
	 * @return
	 */
	public Map<String, Object> checkFeeBudget4LoanOff(UserInfo sessionUser,Integer feeBudgetId) {
		Map<String,Object> map = new HashMap<String, Object>();
		//哪些不能继续报销？
		//1、已经销账的 & 2、报销进行中的
		FeeBudget feeBudget = (FeeBudget)financialDao.objectQuery(FeeBudget.class,feeBudgetId); 
		if(CommonUtil.isNull(feeBudget)) {
			map.put("status","f");
			map.put("info","报销以及不存在！");
		}else {
			map.put("feeBudget",feeBudget);
			//累计借款金额
			Integer loanFeeTotal = CommonUtil.isNull(feeBudget.getLoanFeeTotal())?0:Integer.parseInt(feeBudget.getLoanFeeTotal());
			//查询处于报销中的数据
			FeeLoanOff feeLoanOffOnDoing = financialDao.queryFeeLoanOffOnDoing(feeBudgetId);
			//查询汇报成功并且报销失败的汇报记录
			List<FeeLoanReport> reports = financialDao.queryFeeLoanReport(feeBudgetId);
			if(loanFeeTotal>0) {//有借款；可多次报销
				if(ConstantInterface.COMMON_YES.equals(feeBudget.getLoanOffState())) {//已经销账，不能继续报销
					map.put("status","f");
					map.put("info","已经销账，不能继续报销！");
				}else {
					if(!CommonUtil.isNull(feeLoanOffOnDoing)) {
						map.put("status","f");
						map.put("feeLoanOffOnDoing",feeLoanOffOnDoing);
						map.put("info","报销进行中，不能继续报销！");
					}else {//处于未销账且无报销进行中的；可以报销
						map.put("status","y");
						if(!CommonUtil.isNull(reports)) {
							map.put("reports",reports);
						}
					}
				}
				
			}else {//无借款，一次报销急销账
				List<FeeLoanOff> feeLoanOffDone = financialDao.queryFeeLoanOffDone(feeBudgetId);//查询是否已经报销完结过的
				if(CommonUtil.isNull(feeLoanOffDone) && CommonUtil.isNull(feeLoanOffOnDoing)) {//未报销过；没有完结以及报销中的
					map.put("status","y");
					if(!CommonUtil.isNull(reports)) {
						map.put("reports",reports);
					}
				}else {
					map.put("status","f");
					map.put("feeLoanOffOnDoing",feeLoanOffOnDoing);
					map.put("info","已经销账，不能继续报销！");
				}
			}
			
		}
		return map;
	}

	/**
	 * 获取出差申请的借款记录
	 * @param curUser 当前操作人
	 * @param applyId 出差记录主键
	 * @return
	 */
	public List<FeeLoan> listLoanOfTrip(UserInfo curUser, Integer applyId) {
		List<FeeLoan> listLoanOfTrip = financialDao.listLoanForQuota(curUser,applyId);
		return listLoanOfTrip;
	}

	/**
	 * 查询借款信息
	 * @param userInfo
	 * @param loanId
	 * @return
	 */
	public Map<String, Object> queryLoanInfo(UserInfo userInfo, Integer loanId) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		FeeLoan feeLoan = (FeeLoan) financialDao.objectQuery(FeeLoan.class, loanId);
		if(null != feeLoan.getFeeBudgetId() && feeLoan.getFeeBudgetId()>0){
			//查询本次出差申请
			FeeBudget loanApply = financialDao.queryLoanApply4Check(feeLoan.getFeeBudgetId());
			//查人员总借款和总销账信息
			FeeBudget allApply = financialDao.queryTotalApplyFee(userInfo.getComId(),loanApply.getCreator(),loanApply.getIsBusinessTrip());
			
			Integer totalBorrow = allApply.getTotalBorrow();
			totalBorrow = totalBorrow==null?0:totalBorrow;
			loanApply.setTotalBorrow(totalBorrow);
			
			Integer totalOff = allApply.getTotalOff();
			totalOff = totalOff==null?0:totalOff;
			loanApply.setTotalOff(totalOff);
			
			map.put("loanApply", loanApply);
		}else{
			map.put("loanApply", null);
		}
		
		//根据流程实例主键获取业务、审批表关联关系
		List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp = formColTempService.
				listBusAttrMapFormColTemp(userInfo.getComId(),feeLoan.getInstanceId());
		
		//核实额度
		String borrowingBalanceFieldId = null;
		//剩余额度
		String allowedQuotaFieldId = null;
		//本次借款
		String loanMoneyFieldId = null;
		
		if(null!=listBusAttrMapFormColTemp && !listBusAttrMapFormColTemp.isEmpty()){
			for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
				if(null == loanMoneyFieldId 
					&& busAttrMapFormColTemp.getBusAttr().equals("loanMoney")){
					loanMoneyFieldId = busAttrMapFormColTemp.getFormCol();
					continue;
				}
				if(null == borrowingBalanceFieldId
						&& busAttrMapFormColTemp.getBusAttr().equals("borrowingBalance")){
					borrowingBalanceFieldId = busAttrMapFormColTemp.getFormCol();
					continue;
				}
				if(null == allowedQuotaFieldId
					&& busAttrMapFormColTemp.getBusAttr().equals("allowedQuota")){
					allowedQuotaFieldId = busAttrMapFormColTemp.getFormCol();
					continue;
				}
				if(null != borrowingBalanceFieldId && null != allowedQuotaFieldId){
					break;
				}
			}
		}
		//本次借款的名称
		map.put("loanFieldId", loanMoneyFieldId);
		//剩余额度
		map.put("leftQuatoFieldId", allowedQuotaFieldId);
		
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 查询借款信息
	 * @param userInfo 当前操作员
	 * @param busType 业务类型
	 * @param busId 业务主键
	 * @param spInsId 流程主键
	 */
	public Map<String,Object> queryLoanBaseInfo(UserInfo userInfo, String busType, Integer busId, Integer spInsId){
		Map<String,Object> map = new HashMap<String,Object>();
		//关联额度申请主键
		Integer feeBudgetId = 0;
		if(ConstantInterface.TYPE_FEE_APPLY_TRIP.equals(busType)
				|| ConstantInterface.TYPE_FEE_APPLY_DAYLY.equals(busType)){//额度申请
			feeBudgetId= busId;
			
		}else if(ConstantInterface.TYPE_LOAN_TRIP.equals(busType)
				|| ConstantInterface.TYPE_LOAN_DAYLY.equals(busType)){//借款
			Integer loanId= busId;
			FeeLoan feeLoan = (FeeLoan) financialDao.objectQuery(FeeLoan.class, loanId);
			feeBudgetId = feeLoan.getFeeBudgetId();
			
			//根据流程实例主键获取业务、审批表关联关系
			List<BusAttrMapFormColTemp> listBusAttrMapFormColTemp = formColTempService.
					listBusAttrMapFormColTemp(userInfo.getComId(),feeLoan.getInstanceId());
			//核实额度
			String borrowingBalanceFieldId = null;
			//剩余额度
			String allowedQuotaFieldId = null;
			//本次借款
			String loanMoneyFieldId = null;
			
			if(null!=listBusAttrMapFormColTemp && !listBusAttrMapFormColTemp.isEmpty()){
				for (BusAttrMapFormColTemp busAttrMapFormColTemp : listBusAttrMapFormColTemp) {
					if(null == loanMoneyFieldId 
						&& busAttrMapFormColTemp.getBusAttr().equals("loanMoney")){
						loanMoneyFieldId = busAttrMapFormColTemp.getFormCol();
						continue;
					}
					if(null == borrowingBalanceFieldId
							&& busAttrMapFormColTemp.getBusAttr().equals("borrowingBalance")){
						borrowingBalanceFieldId = busAttrMapFormColTemp.getFormCol();
						continue;
					}
					if(null == allowedQuotaFieldId
						&& busAttrMapFormColTemp.getBusAttr().equals("allowedQuota")){
						allowedQuotaFieldId = busAttrMapFormColTemp.getFormCol();
						continue;
					}
					if(null != borrowingBalanceFieldId && null != allowedQuotaFieldId){
						break;
					}
				}
			}
			//本次借款的名称
			map.put("loanFieldId", loanMoneyFieldId);
			//剩余额度
			map.put("leftQuatoFieldId", allowedQuotaFieldId);
			
			map.put("status", "y");
			
		}else if(ConstantInterface.TYPE_REPORT_TRIP.equals(busType)
				|| ConstantInterface.TYPE_REPORT_DAYLY.equals(busType)){//汇报
			Integer loanRepId= busId;
			FeeLoanReport loanReport = (FeeLoanReport) financialDao.objectQuery(FeeLoanReport.class, loanRepId);
			feeBudgetId = loanReport.getFeeBudgetId();
		}else if(ConstantInterface.TYPE_LOANOFF_TRIP.equals(busType)
				|| ConstantInterface.TYPE_LOANOFF_DAYLY.equals(busType)){//报销
			Integer loanOffId= busId;
			FeeLoanOff loanOffAccount = (FeeLoanOff) financialDao.objectQuery(FeeLoanOff.class, loanOffId);
			feeBudgetId = loanOffAccount.getFeeBudgetId();
		}
		
		FeeBudget feeBudget = financialDao.queryLoanApply4Check(feeBudgetId);
		if(ConstantInterface.COMMON_FINISH.equals(feeBudget.getStatus()) 
				&& LoanWayEnum.QUOTA.getValue().equals(feeBudget.getLoanWay())){
			//查询额度申请表的关联信息
			//只有办结后才有借款信息
			UserInfo user = new UserInfo();
			user.setComId(userInfo.getComId());
			user.setId(feeBudget.getCreator());
			
			
			List<FeeLoan> listLoan = financialDao.listLoanForQuota(user,feeBudget.getId());
			feeBudget.setListFeeLoan(listLoan);
			FeeLoanOff loanOffAccount = new FeeLoanOff();
			loanOffAccount.setFeeBudgetId(feeBudgetId);
			loanOffAccount.setLoanWay(LoanWayEnum.QUOTA.getValue());
			//查询不能报销的
			List<FeeLoanOff> loanOffAccountNs = financialDao.listLoanOffN(userInfo,loanOffAccount);
			if(null == loanOffAccountNs || loanOffAccountNs.isEmpty()){
				Integer isBusinessTrip= feeBudget.getIsBusinessTrip();
				if(null!=isBusinessTrip && isBusinessTrip.equals(0)){
					loanOffAccountNs = financialDao.listLoanOffNDaily(userInfo,loanOffAccount);
				}
			}
			feeBudget.setListLoanOff(loanOffAccountNs);
			//工作成果信息
			List<FeeLoanReport> listLoanReport = financialDao.listLoanReport(user,feeBudgetId);
			feeBudget.setListLoanReport(listLoanReport);
			
			//查人员总借款和总销账信息
			FeeBudget allFeeBudget = financialDao.queryTotalApplyFee(userInfo.getComId(),feeBudget.getCreator(),feeBudget.getIsBusinessTrip());
			
			Integer totalBorrow = allFeeBudget.getTotalBorrow();
			totalBorrow = totalBorrow==null?0:totalBorrow;
			feeBudget.setTotalBorrow(totalBorrow);
			
			Integer totalOff = allFeeBudget.getTotalOff();
			totalOff = totalOff==null?0:totalOff;
			feeBudget.setTotalOff(totalOff);
			
			map.put("loanApply", feeBudget);
			map.put("status", "y");
		}else{
			FeeBudget loanApply = new FeeBudget();
			loanApply.setIsBusinessTrip(-1);
			map.put("loanApply", loanApply);
		}
		return map;
	}
	/*************************客户类型出差费用统计**************************************************/
	
	/**
	 * 用于业务出差费用统计
	 * @param userInfo
	 * @param statisticFeeCrmVo
	 * @return
	 */
	public PageBean<StatisticFeeCrmVo> listPagedStatisticFeeCrm(
			UserInfo userInfo, StatisticFeeCrmVo statisticFeeCrmVo) {
		List<StatisticFeeCrmVo> recordList = financialDao.listPagedStatisticFeeCrm(userInfo,statisticFeeCrmVo);
		PageBean<StatisticFeeCrmVo> pageBean = new PageBean<StatisticFeeCrmVo>();
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 用于业务出差费用统计
	 * @param userInfo
	 * @param statisticFeeItemVo
	 * @return
	 */
	public PageBean<StatisticFeeItemVo> listPagedStatisticFeeItem(
			UserInfo userInfo, StatisticFeeItemVo statisticFeeItemVo) {
		List<StatisticFeeItemVo> recordList = financialDao.listPagedStatisticFeeItem(userInfo,statisticFeeItemVo);
		PageBean<StatisticFeeItemVo> pageBean = new PageBean<StatisticFeeItemVo>();
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 分页查询财务办公借款记录
	 * @param curUser
	 * @param loan
	 * @return
	 */
	public List<FeeLoan> listPagedLoans(UserInfo curUser, FeeLoan loan) {
		return  financialDao.listPagedLoans(curUser,loan);
	}
	
	/**
	 * 分页查询财务办公报销信息
	 * @param userInfo
	 * @param loanOff
	 * @return
	 */
	public List<FeeLoanOff> listPagedLoanOffs(UserInfo userInfo, FeeLoanOff loanOff) {
		return  financialDao.listPagedLoanOffs(userInfo,loanOff);
	}
	
	/**
	 * 查询财务办公借款审核总数
	 * @param userInfo
	 * @return
	 */
	public Integer countLoans(UserInfo userInfo) {
		return financialDao.countLoans(userInfo);
	}
	/**
	 * 查询财务办公报销待办总数
	 * @param userInfo
	 * @return
	 */
	public Integer countLoanOffs(UserInfo userInfo) {
		return financialDao.countLoanOffs(userInfo);
	}
	
	/**
	 * 查询报销信息
	 * @param loanReportId 汇报主键
	 * @return
	 */
	public FeeLoanOff queryLoanOffFlowByReportId(Integer loanReportId) {
		return financialDao.queryLoanOffFlowByReportId(loanReportId);
	}
	
	/**
	 * 查询记账数据用于显示
	 * @param userInfo 当前操作人员
	 * @param instnceId 审批流程主键
	 * @return
	 */
	public List<Consume> listConsumeFromSpFlowData(UserInfo userInfo,Integer instnceId){
		List<Consume> consumes = workFlowService.listLayoutDetailForConsume(userInfo,instnceId);
		return consumes;
	}

	/**
	 * 查询个人报销信息
	 * @param userInfo
	 * @return
	 */
	public FeeLoanOff queryFeeLoanOffForPersonal(UserInfo userInfo) {
		//成功报销
		FeeLoanOff feeLoanOffDone = financialDao.queryFeeLoanOffForPersonalForDone(userInfo);
		if(CommonUtil.isNull(feeLoanOffDone)) {
			feeLoanOffDone = new FeeLoanOff();
		}
		//已完成的报销次数
		feeLoanOffDone.setLoanOffDoneTimes(CommonUtil.isNull(feeLoanOffDone.getLoanOffDoneTimes())?0:feeLoanOffDone.getLoanOffDoneTimes());
		//已完成的报销总额
		feeLoanOffDone.setLoanDoneItemTotal(CommonUtil.isNull(feeLoanOffDone.getLoanDoneItemTotal())?0:feeLoanOffDone.getLoanDoneItemTotal());
		//报销申请中
		FeeLoanOff feeLoanOffDoing = financialDao.queryFeeLoanOffForPersonalForDoing(userInfo);
		if(CommonUtil.isNull(feeLoanOffDoing)) {
			feeLoanOffDoing = new FeeLoanOff();
		}
		//报销中的次数
		feeLoanOffDone.setLoanOffDoingTimes(CommonUtil.isNull(feeLoanOffDoing.getLoanOffDoingTimes())?0:feeLoanOffDoing.getLoanOffDoingTimes());
		//报销中的金额
		feeLoanOffDone.setLoanDoingItemTotal(CommonUtil.isNull(feeLoanOffDoing.getLoanDoingItemTotal())?0:feeLoanOffDoing.getLoanDoingItemTotal());
		return feeLoanOffDone ;
	}
	/**
	 * 查询个人借款信息
	 * @param userInfo
	 * @return
	 */
	public FeeLoan queryFeeLoanForPersonal(UserInfo userInfo) {
		//成功借款
		FeeLoan feeLoanDone = financialDao.queryFeeLoanForPersonalForDone(userInfo);
		if(CommonUtil.isNull(feeLoanDone)) {
			feeLoanDone = new FeeLoan();
		}
		feeLoanDone.setFeeLoanDoneTimes(CommonUtil.isNull(feeLoanDone.getFeeLoanDoneTimes())?0:feeLoanDone.getFeeLoanDoneTimes());
		feeLoanDone.setBorrowingBalanceDoneTotal(CommonUtil.isNull(feeLoanDone.getBorrowingBalanceDoneTotal())?0:feeLoanDone.getBorrowingBalanceDoneTotal());
		//借款申请中
		FeeLoan feeLoanDoing = financialDao.queryFeeLoanForPersonalForDoing(userInfo);
		if(CommonUtil.isNull(feeLoanDoing)) {
			feeLoanDoing = new FeeLoan();
		}
		feeLoanDone.setFeeLoanDoingTimes(CommonUtil.isNull(feeLoanDoing.getFeeLoanDoingTimes())?0:feeLoanDoing.getFeeLoanDoingTimes());
		feeLoanDone.setLoanmoneyDoingTotal(CommonUtil.isNull(feeLoanDoing.getLoanmoneyDoingTotal())?0:feeLoanDoing.getLoanmoneyDoingTotal());
		return feeLoanDone;
	}
	/**
	 * 查询个人消费信息
	 * @param userInfo
	 * @return
	 */
	public List<Consume> listConsumeForPersonalByStatus(UserInfo userInfo) {
		return financialDao.listConsumeForPersonalByStatus(userInfo);
	}
	
	/**
	 * 个人出差统计
	 * @param userInfo
	 * @return
	 */
	public List<FeeBudget> queryBusinessTripForPersonal(UserInfo userInfo) {
		return financialDao.queryBusinessTripForPersonal(userInfo);
	}

	/**
	 * 查询借款用于
	 * 查询借款用于确认
	 * @param userInfo
	 * @param loanId
	 * @return
	 */
	public FeeLoan getLoanForBalance(UserInfo userInfo, Integer loanId) {
		return financialDao.getLoanForBalance(userInfo,loanId);
	}
	
	/**
	 * 查询报销用于确认
	 * @param userInfo
	 * @param loanOffId
	 * @return
	 */
	public FeeLoanOff getLoanOffForBalance(UserInfo userInfo, Integer loanOffId) {
		return financialDao.getLoanOffForBalance(userInfo,loanOffId);
	}

	/**
	 * 查询所有的用于初始化的数据
	 * @param comId
	 * @return
	 */
	public List<FeeBudget> listPagedFeeBudgetForInit(Integer comId) {
		return financialDao.listPagedFeeBudgetForInit(comId);
	}
	/**
	 * 查询所有的用于初始化的数据
	 * @param comId
	 * @return
	 */
	public List<FeeLoan> listPagedFeeLoanForInit(Integer comId) {
		return financialDao.listPagedFeeLoanForInit(comId);
	}
	/**
	 * 查询所有的用于初始化的数据
	 * @param comId
	 * @return
	 */
	public List<FeeLoanOff> listPagedFeeLoanOffForInit(Integer comId) {
		return financialDao.listPagedFeeLoanOffForInit(comId);
	}

	/**
	 * 开始初始化数据信息
	 * @param feeBudgets
	 * @param map 
	 */
	@SuppressWarnings("unchecked")
	public void initFeeBudgetData(List<FeeBudget> feeBudgets, Map<String, Integer> map) {
		if(null!=feeBudgets && !feeBudgets.isEmpty()){
			for (FeeBudget feeBudget : feeBudgets) {
				Integer budgetNum = map.get(ConstantInterface.MENU_FEE_APPLY);
				Integer loanNum = map.get(ConstantInterface.MENU_LOAN);
				Integer loanOffNum = map.get(ConstantInterface.MENU_LOANOFF);
				
				budgetNum = budgetNum + 1;
				map.put(ConstantInterface.MENU_FEE_APPLY,budgetNum);
				//预算审核状态
				Integer feeBudgetStatus = feeBudget.getStatus();
				if(feeBudgetStatus != ConstantInterface.COMMON_FINISH){//未办结的数据，默认已经销账
					feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
					financialDao.update(feeBudget);
					continue;
				}
				//以下是已经审核通过的数据
				
				//总借款
				String loanFeeTotal = "0";
				//总销账
				String loanOffTotal = "0";
				//总报销
				String loanItemTotal = "0";
				
				//查询有依据的借款记录
				List<FeeLoan> feeLoans = (List<FeeLoan>) financialDao.listFeeLoanForInit(feeBudget,FeeLoan.class);
				if(null!=feeLoans && !feeLoans.isEmpty()){
					
					loanNum = loanNum + feeLoans.size();
					map.put(ConstantInterface.MENU_LOAN,loanNum);
					
					for (FeeLoan feeLoan : feeLoans) {
						//借款状态
						Integer feeLoanStatus = feeLoan.getStatus();
						if(feeLoanStatus != ConstantInterface.COMMON_FINISH){//借款中
							continue;
						}
						//本次借款
						String borrowingBalance = feeLoan.getBorrowingBalance();
						borrowingBalance = StringUtils.isEmpty(borrowingBalance)?"0":borrowingBalance;
						//总借款
						loanFeeTotal =MathExtend.add(loanFeeTotal,borrowingBalance );
					}
				}
				
				List<FeeLoanOff> feeLoanOffs = (List<FeeLoanOff>) financialDao.listFeeLoanForInit(feeBudget,FeeLoanOff.class);
				if(null!=feeLoanOffs && !feeLoanOffs.isEmpty()){
					
					loanOffNum = loanOffNum + feeLoanOffs.size();
					map.put(ConstantInterface.MENU_LOANOFF,loanOffNum);
					
					for (FeeLoanOff feeLoanOff : feeLoanOffs) {
						//借款状态
						Integer feeLoanStatus = feeLoanOff.getStatus();
						if(feeLoanStatus != ConstantInterface.COMMON_FINISH){//报销中
							continue;
						}
						//本次销账
						String loanOffBalance = feeLoanOff.getLoanOffBalance();
						loanOffBalance = StringUtils.isEmpty(loanOffBalance)?"0":loanOffBalance;
						//总销账
						loanOffTotal =MathExtend.add(loanOffTotal,loanOffBalance);
						
						//本次销账
						String loanOffItemFee = feeLoanOff.getLoanOffItemFee();
						loanOffItemFee = StringUtils.isEmpty(loanOffItemFee)?"0":loanOffItemFee;
						//总销账
						loanItemTotal =MathExtend.add(loanItemTotal,loanOffItemFee);
					}
					
				}
				//总借款
				feeBudget.setLoanFeeTotal(loanFeeTotal);
				//总销账
				feeBudget.setLoanOffTotal(loanOffTotal);
				//总报销
				feeBudget.setLoanItemTotal(loanItemTotal);
				
				//销账状态
				if(loanFeeTotal.equals(loanOffTotal)){//销账成功
					feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
				}else{//未销账
					feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
				}
				financialDao.update(feeBudget);
			}
		}
		
	}

	/**
	 * 初始化借款数据信息
	 * @param feeLoans
	 * @param map 
	 */
	public void initFeeLoanData(List<FeeLoan> feeLoans, Map<String, Integer> map) {
		if(null!=feeLoans && !feeLoans.isEmpty()){
			
			Integer loanNum = map.get(ConstantInterface.MENU_LOAN);
			loanNum = loanNum + feeLoans.size();
			map.put(ConstantInterface.MENU_LOAN,loanNum);
			
			for (FeeLoan feeLoan : feeLoans) {
				FeeBudget feeBudget = this.constrFeeBudgetForDirect(feeLoan.getCreator(), feeLoan.getComId());
				//设置是否为出差
				feeBudget.setIsBusinessTrip(feeLoan.getIsBusinessTrip());
				Integer isBusinessTrip = feeLoan.getIsBusinessTrip();
				
				if(isBusinessTrip == 1){
					feeBudget.setLoanWay(LoanWayEnum.RELATEITEM.getValue());
				}else{
					//借款方式
					feeBudget.setLoanWay(LoanWayEnum.DIRECT.getValue());
				}
				
				//总借款
				String loanFeeTotal = "0";
				//总销账
				String loanOffTotal = "0";
				//总报销
				String loanItemTotal = "0";
				//借款状态
				Integer feeLoanStatus = feeLoan.getStatus();
				if(feeLoanStatus != ConstantInterface.COMMON_FINISH){
					//销账状态
					if(loanFeeTotal.equals(loanOffTotal)){//销账成功
						feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
					}else{//未销账
						feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
					}
					Integer feeBudgetId = financialDao.add(feeBudget);
					feeBudget.setId(feeBudgetId);
					feeBudget.setRecordCreateTime(feeLoan.getRecordCreateTime());
					
					feeLoan.setFeeBudgetId(feeBudgetId);
					financialDao.update(feeLoan);
					
					financialDao.update("update feeBudget a set recordCreateTime=:recordCreateTime where id=:id",feeBudget);
					continue;
				}
				
				//本次借款
				String borrowingBalance = feeLoan.getBorrowingBalance();
				borrowingBalance = StringUtils.isEmpty(borrowingBalance)?"0":borrowingBalance;
				//总借款
				loanFeeTotal =MathExtend.add(loanFeeTotal,borrowingBalance );
				
				//总借款
				feeBudget.setLoanFeeTotal(loanFeeTotal);
				//总销账
				feeBudget.setLoanOffTotal(loanOffTotal);
				//总报销
				feeBudget.setLoanItemTotal(loanItemTotal);
				
				//销账状态
				if(loanFeeTotal.equals(loanOffTotal)){//销账成功
					feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
				}else{//未销账
					feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
				}
				Integer feeBudgetId = financialDao.add(feeBudget);
				
				feeLoan.setFeeBudgetId(feeBudgetId);
				financialDao.update(feeLoan);
				
				feeBudget.setId(feeBudgetId);
				feeBudget.setRecordCreateTime(feeLoan.getRecordCreateTime());
				financialDao.update("update feeBudget a set recordCreateTime=:recordCreateTime where id=:id",feeBudget);
				
			}
		}
		
	}
	
	/**
	 * 添加预算基本信息
	 * @param userId 当前操作人员
	 * @return
	 */
	private FeeBudget constrFeeBudgetForDirect(Integer userId,Integer comId) {
		//实例化
		FeeBudget feeBudget = new FeeBudget();
		//设置团队号
		feeBudget.setComId(comId);
		//流程实例化主键
		feeBudget.setInstanceId(0);
		//申请人
		feeBudget.setCreator(userId);
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
	 * 添加报销数据信息
	 * @param feeLoanOffs
	 * @param map 
	 */
	public void initFeeLoanOffData(List<FeeLoanOff> feeLoanOffs, Map<String, Integer> map) {
		if(null!=feeLoanOffs && !feeLoanOffs.isEmpty()){
			
			Integer loanOffNum = map.get(ConstantInterface.MENU_LOANOFF);
			loanOffNum = loanOffNum + feeLoanOffs.size();
			map.put(ConstantInterface.MENU_LOANOFF,loanOffNum);
			
			for (FeeLoanOff feeLoanOff : feeLoanOffs) {
				FeeBudget feeBudget = this.constrFeeBudgetForDirect(feeLoanOff.getCreator(), feeLoanOff.getComId());
				Integer isBusinessTrip = feeLoanOff.getIsBusinessTrip();
				//设置是否为出差
				feeBudget.setIsBusinessTrip(feeLoanOff.getIsBusinessTrip());
				String loanReportWay = feeLoanOff.getLoanReportWay();
				if(isBusinessTrip == 0 && LoanReportWayEnum.DIRECT.getValue().equals(loanReportWay)){
					//借款方式
					feeBudget.setLoanWay(LoanWayEnum.DIRECTOFF.getValue());
				}else if(isBusinessTrip == 1 && LoanReportWayEnum.DIRECT.getValue().equals(loanReportWay)){
					feeBudget.setLoanWay(LoanWayEnum.ITEMOFF.getValue());
				}else{
					//借款方式
					feeBudget.setLoanWay(loanReportWay);
				}
				//总借款
				String loanFeeTotal = "0";
				//总销账
				String loanOffTotal = "0";
				//总报销
				String loanItemTotal = "0";
				//借款状态
				Integer feeLoanOffStatus = feeLoanOff.getStatus();
				if(feeLoanOffStatus != ConstantInterface.COMMON_FINISH){
					//销账状态
					if(loanFeeTotal.equals(loanOffTotal)){//销账成功
						feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
					}else{//未销账
						feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
					}
					Integer feeBudgetId = financialDao.add(feeBudget);
					feeBudget.setId(feeBudgetId);
					feeBudget.setRecordCreateTime(feeLoanOff.getRecordCreateTime());
					
					feeLoanOff.setFeeBudgetId(feeBudgetId);
					financialDao.update(feeLoanOff);
					
					financialDao.update("update feeBudget a set recordCreateTime=:recordCreateTime where id=:id",feeBudget);
					continue;
				}
				//总借款
				loanFeeTotal = "0";
				
				//本次销账
				String loanOffBalance = feeLoanOff.getLoanOffBalance();
				loanOffBalance = StringUtils.isEmpty(loanOffBalance)?"0":loanOffBalance;
				//总销账
				loanOffTotal =MathExtend.add(loanOffTotal,loanOffBalance);
				
				//本次销账
				String loanOffItemFee = feeLoanOff.getLoanOffItemFee();
				loanOffItemFee = StringUtils.isEmpty(loanOffItemFee)?"0":loanOffItemFee;
				//总销账
				loanItemTotal =MathExtend.add(loanItemTotal,loanOffItemFee);
				
				//总借款
				feeBudget.setLoanFeeTotal(loanFeeTotal);
				//总销账
				feeBudget.setLoanOffTotal(loanOffTotal);
				//总报销
				feeBudget.setLoanItemTotal(loanItemTotal);
				
				//销账状态
				if(loanFeeTotal.equals(loanOffTotal)){//销账成功
					feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
				}else{//未销账
					feeBudget.setLoanOffState(ConstantInterface.LOANOFF_NO);
				}
				Integer feeBudgetId = financialDao.add(feeBudget);
				
				feeLoanOff.setFeeBudgetId(feeBudgetId);
				financialDao.update(feeLoanOff);
				
				feeBudget.setId(feeBudgetId);
				feeBudget.setRecordCreateTime(feeLoanOff.getRecordCreateTime());
				financialDao.update("update feeBudget a set recordCreateTime=:recordCreateTime where id=:id",feeBudget);
				
			}
		}
		
	}

	/**
	 * 统计个人报销
	 * @param curUser
	 * @return
	 */
	public Integer countPersonalLoanOffOfAuthV2(UserInfo curUser) {
		return financialDao.countPersonalLoanOffOfAuthV2(curUser);
	}

	/**
	 * 统计个人报销借款
	 * @param curUser
	 * @param loan
	 * @param isForceInPersion
	 * @return
	 */
	public Integer countPersonalLoanOfAuthV2(UserInfo curUser) {
		return financialDao.countPersonalLoanOfAuthV2(curUser);
	}
	
	/**
	 * 根据insId查询借款或者报销id
	 * @param busId
	 * @return
	 */
	public FeeLoan queryLoanByInsId(Integer insId) {
		return financialDao.queryLoanByInsId(insId);
	}
	
	/**
	 * 根据id查询借款详情
	 * @param id
	 * @return
	 */
	public FeeLoan queryLoanById(Integer id) {
		return (FeeLoan) financialDao.objectQuery(FeeLoan.class, id);
	}
	
	/**
	 * 根据id查询报销详情
	 * @param id
	 * @return
	 */
	public FeeLoanOff queryLoanOffById(Integer id) {
		return (FeeLoanOff) financialDao.objectQuery(FeeLoanOff.class, id);
	}
	
	/**
	 * 根据汇报主键获取依据详情
	 * @param loanReportId
	 * @param comId
	 * @return
	 */
	public FeeBudget queryFeeBudgetByLoanReport(Integer loanReportId,Integer comId) {
		FeeBudget feeBudget = financialDao.queryFeeBudgetByLoanReport(loanReportId,comId);
		if(!CommonUtil.isNull(feeBudget)) {
			//累计借款金额
			Integer loanFeeTotal = CommonUtil.isNull(feeBudget.getLoanFeeTotal())?0:Integer.parseInt(feeBudget.getLoanFeeTotal());
			//累计销账金额
			Integer loanOffTotal = CommonUtil.isNull(feeBudget.getLoanOffTotal())?0:Integer.parseInt(feeBudget.getLoanOffTotal());
			//未销账金额
			Integer unLoanOffTotal = loanFeeTotal-loanOffTotal;
			feeBudget.setUnLoanOffTotal(unLoanOffTotal.compareTo(0)>0?unLoanOffTotal:0);
			List<FeeBusMod> listFeeBusMod = financialDao.listFeeBusMod(feeBudget.getId(),comId);
			feeBudget.setListRelateMods(listFeeBusMod);
		}
		return feeBudget;
	}
	/**
	 * 根据出差报销主键获取依据详情
	 * @param loanReportId
	 * @param comId
	 * @return
	 */
	public FeeBudget queryFeeBudgetByFeeLoanOff(Integer feeLoanOffId,Integer comId) {
		FeeBudget feeBudget = financialDao.queryFeeBudgetByFeeLoanOff(feeLoanOffId,comId);
		if(!CommonUtil.isNull(feeBudget)) {
			//累计借款金额
			Integer loanFeeTotal = CommonUtil.isNull(feeBudget.getLoanFeeTotal())?0:Integer.parseInt(feeBudget.getLoanFeeTotal());
			//累计销账金额
			Integer loanOffTotal = CommonUtil.isNull(feeBudget.getLoanOffTotal())?0:Integer.parseInt(feeBudget.getLoanOffTotal());
			//未销账金额
			Integer unLoanOffTotal = loanFeeTotal-loanOffTotal;
			feeBudget.setUnLoanOffTotal(unLoanOffTotal.compareTo(0)>0?unLoanOffTotal:0);
			List<FeeBusMod> listFeeBusMod = financialDao.listFeeBusMod(feeBudget.getId(),comId);
			feeBudget.setListRelateMods(listFeeBusMod);
		}
		return feeBudget;
	}
	
	/**
	 * 根据id查询出差报告详情
	 * @param loanReportId
	 * @return
	 */
	public FeeLoanReport queryFeeLoanReportById(Integer loanReportId) {
		return (FeeLoanReport) financialDao.objectQuery(FeeLoanReport.class, loanReportId);
	}
	
	/**
	 * 根据loanReportId获取关联报销详情
	 * @param loanReportId
	 * @param comId
	 * @return
	 */
	public FeeLoanOff queryLoanOffByRepId(Integer loanReportId, Integer comId) {
		return financialDao.queryLoanOffByRepId(loanReportId,comId);
	}
	
	/**
	 * 根据feeBudgetId获取关联报销详情
	 * @param feeBudgetId
	 * @param comId
	 * @return
	 */
	public FeeLoanOff queryLoanOffByFeebId(Integer feeBudgetId, Integer comId) {
		return financialDao.queryLoanOffByFeebId(feeBudgetId,comId);
	}
	
	/**
	 * 直接销账操作
	 * @author hcj 
	 * @param id
	 * @param userInfo
	 * @param content 
	 * @return 
	 * @date 2018年9月5日 下午4:12:02
	 */
	public boolean updateDirectBalance(Integer id, UserInfo userInfo, String content) {
		FeeBudget feeBudget = new FeeBudget();
		feeBudget.setId(id);
		feeBudget.setLoanOffState(ConstantInterface.LOANOFF_YES);
		financialDao.update(feeBudget);
		//添加记录
		FeeDirectBalance feeDirectBalance = new FeeDirectBalance();
		feeDirectBalance.setComId(userInfo.getComId());
		feeDirectBalance.setCreator(userInfo.getId());
		feeDirectBalance.setFeeBudgetId(id);
		feeDirectBalance.setContent(content);
		financialDao.add(feeDirectBalance);
		return true;
	}
	
	
}
