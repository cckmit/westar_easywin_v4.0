package com.westar.core.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.westar.base.enums.TripFeeEnum;
import com.westar.core.service.ForceInPersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Consume;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.FeeLoanReport;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.ConsumeService;
import com.westar.core.service.FinancialService;
import com.westar.core.service.WorkFlowService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 财务相关控制类
 * @author luojian
 *
 */

@Controller
@RequestMapping("/financial")
public class FinancialController extends BaseController {
	
	@Autowired
	FinancialService financialService;
	
	@Autowired
	ConsumeService consumeService;

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	ForceInPersionService forceInService;

	/**
	 * 获取个人申请记录列表
	 * @param request
	 * @param activityMenu
	 * @param feeBudget
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loanApply/listLoanApply")
	public ModelAndView listLoanApply(HttpServletRequest request,String activityMenu,FeeBudget feeBudget){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		UserInfo curUser = this.getSessionUser();

		view.addObject("userInfo",curUser);
		view.addObject("loanApply",feeBudget);//筛选参数
		return view;
	}

	/**
	 * 个人报销
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply")
	public ModelAndView personalApply(HttpServletRequest request){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.MENU_FEE_APPLY);
		return view;
	}
	
	/**
	 * 获取个人报销信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loanApply/feeLoanOffForPersonal")
	public Map<String,Object> feeLoanOffForPersonal(){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		FeeLoanOff feeLoanOff = financialService.queryFeeLoanOffForPersonal(userInfo);
		map.put("feeLoanOff", feeLoanOff);
		map.put("status", "y");
		return map;
	}
	/**
	 * 个人借款统计显示
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loanApply/feeLoanForPersonal")
	public Map<String,Object> feeLoanForPersonal(){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		FeeLoan feeLoan = financialService.queryFeeLoanForPersonal(userInfo);
		map.put("feeLoan", feeLoan);
		map.put("status", "y");
		return map;
	}
	/**
	 * 统计个人的消费记录数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loanApply/consumeForPersonal")
	public Map<String,Object> consumeForPersonal(){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<Consume> listConsume = financialService.listConsumeForPersonalByStatus(userInfo);
		map.put("listConsume", listConsume);
		map.put("status", "y");
		return map;
	}
	/**
	 * 个人出差统计
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loanApply/businessTripForPersonal")
	public Map<String,Object> businessTripForPersonal(){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<FeeBudget> listBusinessTrip= financialService.queryBusinessTripForPersonal(userInfo);
		map.put("listBusinessTrip", listBusinessTrip);
		map.put("status", "y");
		return map;
	}
	/**
	 * 获取权限下的申请记录列表
	 * @param request
	 * @param feeBudget
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loanApply/listLoanApplyOfAuth")
	public ModelAndView listLoanApplyOfAuth(HttpServletRequest request,FeeBudget feeBudget){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		view.addObject("loanApply",feeBudget);//筛选参数
		//验证当前登录人是否是督察人员
		view.addObject("isForceInPersion",forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_FINANCIAL));
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.MENU_FEE_APPLY);
		return view;
	}

	/**
	 * 个人报销申请列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply/listMyLoanOffPage")
	public ModelAndView listMyLoanOffPage(HttpServletRequest request,FeeLoanOff feeLoanOff){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/personalApply/listMyLoanOff");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo",sessionUser);

		PageBean<FeeLoanOff> pageBean = financialService.listPersonalLoanOffOfAuthV2(sessionUser,feeLoanOff);
		view.addObject("pageBean", pageBean);
		return view;
	}
	/**
	 * 个人借款申请列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply/listMyLoanPage")
	public ModelAndView listMyLoanPage(HttpServletRequest request,FeeLoan feeLoan){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/personalApply/listMyLoan");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo",sessionUser);
		
		PageBean<FeeLoan> pageBean = financialService.listPersonalLoanOfAuthV2(sessionUser,feeLoan);
		view.addObject("pageBean", pageBean);
		return view;
	}
	/**
	 * 个人报销详情页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply/detailsLoanOffPage")
	public ModelAndView detailsLoanOffPage(HttpServletRequest request,FeeLoanOff feeLoanOff){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/personalApply/detailsLoanOff");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		view.addObject("loanOffAccount",feeLoanOff);
		return view;
	}
	/**
	 * 个人借款详情页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply/detailsLoanPage")
	public ModelAndView detailsLoanPage(HttpServletRequest request,FeeLoan feeLoan){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/personalApply/detailsLoan");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		view.addObject("loan",feeLoan);
		return view;
	}
	/**
	 * 个人报销详情内容
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply/detailsLoanOffViewPage")
	public ModelAndView detailsLoanOffViewPage(HttpServletRequest request,FeeLoanOff feeLoanOffParam){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
 		ModelAndView view = new ModelAndView("/financial/personalApply/detailsLoanOff_view");
		UserInfo curUser = this.getSessionUser();

		//取出备用
		Integer instanceId = feeLoanOffParam.getInstanceId();
		Integer repInstanceId = feeLoanOffParam.getLoanRepInsId();

		//查询报销信息
		feeLoanOffParam.setLoanRepInsId(null);
		FeeLoanOff feeLoanOff = financialService.getLoanOffAccountForDetails(feeLoanOffParam,curUser);
		view.addObject("loanOffAccount",feeLoanOff);

		//如果是一般报销则查询其借款申请
		if(null != feeLoanOff && null != feeLoanOff.getIsBusinessTrip() && feeLoanOff.getIsBusinessTrip() == 0 && null != feeLoanOff.getFeeBudgetId()){
			FeeBudget feeBudget = financialService.getFeeBudGetById(feeLoanOff.getFeeBudgetId(),curUser);
			view.addObject("loanApply",feeBudget);
		}

		//出差报销绑定的消费记录
		if(null != instanceId && instanceId > 0){
			List<Consume> consumes = financialService.listConsumeFromSpFlowData(curUser,instanceId);
			view.addObject("consumes",consumes);
		}

        //查询出差汇报
        feeLoanOffParam.setLoanRepInsId(repInstanceId);
        feeLoanOffParam.setInstanceId(null);
        FeeLoanOff feeLoan = financialService.getLoanOffAccountForDetails(feeLoanOffParam,curUser);
        view.addObject("loanReport",feeLoan);

		feeLoanOffParam.setLoanRepInsId(repInstanceId);

		Boolean isLoan = null != feeLoanOff && null != feeLoanOff.getIsBusinessTrip() && feeLoanOff.getIsBusinessTrip() == TripFeeEnum.YES.getValue();
		Boolean hasReport = null != feeLoan && null != feeLoan.getLoanWay() && feeLoan.getLoanWay().equals(TripFeeEnum.YES.getValue().toString());
		//是否为出差
		if(isLoan || hasReport){
			//查询出差的基本信息
			FeeBudget feeBudget = financialService.getFeeBudGetById(isLoan ? feeLoanOff.getFeeBudgetId() : feeLoan.getFeeBudgetId(),curUser);
			view.addObject("businessApply",feeBudget);
		}

		return view;
	}
	/**
	 * 个人借款详情内容
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply/detailsLoanViewPage")
	public ModelAndView detailsLoanViewPage(HttpServletRequest request,FeeLoan feeLoanParam){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/personalApply/detailsLoan_view");
		UserInfo sessionUser = this.getSessionUser();
		FeeLoan feeLoan = financialService.getLoanForDetails(feeLoanParam,sessionUser);
		if((null != feeLoanParam.getInstanceId() && feeLoanParam.getInstanceId() > 0)){
			List<Consume> listConsume = workFlowService.listLayoutDetailForConsume(sessionUser,feeLoanParam.getInstanceId());
			view.addObject("consumes",listConsume);
		}
		view.addObject("userInfo",sessionUser);
		view.addObject("loan",feeLoan);
		return view;
	}
	/**
	 * 个人代销消费记录列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/personalApply/listMyConsumePage")
	public ModelAndView listMyConsumePage(HttpServletRequest request,Consume consume){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/personalApply/listMyConsume");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		view.addObject("consume",consume);//筛选参数
		List<Consume> listConsumes = consumeService.listPagedConsume(consume,userInfo,null);
		view.addObject("listConsumes", listConsumes);
		return view;
	}
	/**
	 * 获取权限下的申请记录列表
	 * @param loanApply
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/loanApply/ajaxListLoanApplyOfAuth")
	public Map<String,Object> ajaxListLoanApplyOfAuth(FeeBudget loanApply,Integer pageNum, Integer pageSize){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		PageBean<FeeBudget> pageBean = financialService.listLoanApplyOfAuth(userInfo,loanApply);
		map.put("pageBean", pageBean);
		map.put("nowDate", DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		map.put("status", "y");
		return map;
	}
	/**
	 * 选择可以用于选择的额度申请
	 * @return
	 */
	@RequestMapping("/loanApply/loanApplyForStartSelect")
	public ModelAndView loanApplyForStart(Integer isBusinessTrip){
		ModelAndView view = new ModelAndView("/financial/loanApply/loanApplyForStartSelect");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		view.addObject("isBusinessTrip", isBusinessTrip);

		return view;
	}
	/**
	 * 异步获取需要加载数据的额度申请
	 * @param loanApply 额度申请查询条件
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loanApply/ajaxLoanApplyForStartSelect")
	public Map<String,Object> ajaxLoanApplyForStartSelect(FeeBudget loanApply,Integer pageNum, Integer pageSize){
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		PageBean<FeeBudget> pageBean = financialService.listPagedLoanApplyForStartSelect(userInfo,loanApply);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	/**
	 * 选择可以用于选择的额度申请
	 * @return
	 */
	@RequestMapping("/loanApply/loanApplyForOffSelect")
	public ModelAndView loanApplyForOffSelect(Integer isBusinessTrip){
		ModelAndView view = new ModelAndView("/financial/loanApply/loanApplyForOffSelect");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		view.addObject("isBusinessTrip", isBusinessTrip);
		return view;
	}
	/**
	 * 选择用于报销的额度申请信息
	 * @param loanApply
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loanApply/ajaxLoanApplyForOffSelect")
	public Map<String,Object> ajaxLoanApplyForOffSelect(FeeBudget loanApply,Integer pageNum, Integer pageSize){
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		PageBean<FeeBudget> pageBean = financialService.listPagedLoanApplyForOffSelect(userInfo,loanApply);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	/**
	 * 获取申请申请的借款记录
	 * @param request
	 * @param tripId 申请记录主键
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loanApply/listLoanOfTrip")
	public ModelAndView listLoanOfTrip(HttpServletRequest request,Integer tripId) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/loanApply/listLoanOfTrip");
		UserInfo curUser = this.getSessionUser();
		List<FeeLoan> listLoanOfTrip = financialService.listLoanOfTrip(curUser,tripId);
		view.addObject("listLoanOfTrip", listLoanOfTrip);
		return view;
	}
	/**
	 * 借款方式选择
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loan/loanWayChoosePage")
	public ModelAndView loanWayChoosePage() throws Exception{
		//清除缓存中所有的操作
		ModelAndView view = new ModelAndView("/financial/loan/loanWayChoose");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		return view;
	}

	/**
	 * 选择可以用于选择的额度申请
	 * @return
	 */
	@RequestMapping("/loan/loanForOffSelect")
	public ModelAndView loanForOffSelect(Integer isBusinessTrip){
		ModelAndView view = new ModelAndView("/financial/loan/loanForOffSelect");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		view.addObject("isBusinessTrip", isBusinessTrip);
		return view;
	}
	/**
	 * 选择用于报销的额度申请信息
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loan/ajaxLoanForOffSelect")
	public Map<String,Object> ajaxLoanForOffSelect(FeeLoan loan,Integer pageNum, Integer pageSize){
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		PageBean<FeeLoan> pageBean = financialService.listPagedLoanForOffSelect(userInfo,loan);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}

	/**
	 * 获取权限下借款记录
	 * @param request
	 * @param loan
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loan/listLoanOfAuth")
	public ModelAndView listLoanOfAuth(HttpServletRequest request,FeeLoan loan) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo", curUser);
		view.addObject("loan",loan);//筛选参数
		//验证当前登录人是否是督察人员
		view.addObject("isForceInPersion",forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_FINANCIAL));
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.MENU_FEE_APPLY);
		return view;
	}
	/**
	 * 个人借款记录列表
	 * @param loan
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/loan/ajaxListLoanOfAuth")
	public Map<String, Object> ajaxListLoanOfAuth(FeeLoan loan,Integer pageNum, Integer pageSize) throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		PageBean<FeeLoan> pageBean = financialService.listLoanOfAuthV2(userInfo,loan);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	/**
	 * 查询借款信息
	 * @param loanId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loan/queryLoanInfo")
	public Map<String,Object> queryLoanInfo(Integer loanId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		map = financialService.queryLoanInfo(userInfo,loanId);
		return map;
	}
	/**
	 * 查询借款信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryLoanBaseInfo")
	public Map<String,Object> queryLoanBaseInfo(String busType,Integer busId,Integer spInsId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		map = financialService.queryLoanBaseInfo(userInfo,busType,busId,spInsId);
		return map;
	}

	/**
	 * 借款方式选择
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loanOff/loanOffWayChoosePage")
	public ModelAndView loanOffWayChoosePage() throws Exception{
		//清除缓存中所有的操作
		ModelAndView view = new ModelAndView("/financial/loanOff/loanOffWayChoose");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		return view;
	}
	/**
	 * 获取个人报销记录
	 * @param request
	 * @param loanOff
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loanOff/listLoanOff")
	public ModelAndView listLoanOff(HttpServletRequest request,FeeLoanOff loanOff) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo", curUser);
		view.addObject("loanOff",loanOff);//筛选参数
		return view;
	}

	/**
	 * 异步方式取得报销数据
	 * @param loanOff 报销查询的条件
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loanOff/ajaxListLoanOffOfAuth")
	public Map<String, Object> ajaxListLoanOffOfAuth(FeeLoanOff loanOff,Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();

		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		PageBean<FeeLoanOff> pageBean = financialService.listLoanOffOfAuthV2(userInfo,loanOff,pageNum);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}

	/**
	 * 获取权限下报销记录
	 * @param request
	 * @param loanOff
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loanOff/listLoanOffOfAuth")
	public ModelAndView listLoanOffOfAuth(HttpServletRequest request,FeeLoanOff loanOff) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		view.addObject("loanOff",loanOff);//筛选参数
		//验证当前登录人是否是督察人员
		view.addObject("isForceInPersion",forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_FINANCIAL));
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.MENU_FEE_APPLY);
		return view;
	}
	/**
	 * 验证操作人是否把所有借款都销账了
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkLoanOffAll")
	public Map<String,Object> checkLoanOffAll(Integer feeBudgetId,String busType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		map = financialService.checkLoanOffAll(userInfo,feeBudgetId,busType);
		return map;
	}
	/**
	 * 验证是否能够发起报销
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkLoanRep4Off")
	public Map<String,Object> checkLoanRep4Off( FeeLoanOff loanOffAccount){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		map = financialService.checkLoanRep4Off(userInfo,loanOffAccount);
		return map;
	}

	/**
	 * 核查当前预报销记录信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkFeeBudget4LoanOff")
	public Map<String,Object> checkFeeBudget4LoanOff(Integer feeBudgetId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}

		map = financialService.checkFeeBudget4LoanOff(userInfo,feeBudgetId);
		return map;
	}

	/**
	 * 差旅一览
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/financialStatistics")
	public ModelAndView financialStatistics(HttpServletRequest request,FeeBudget loanApply) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		return view;
	}
	/**
	 * 展示借款信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showListLoanOffAll")
	public ModelAndView showListLoanOff() throws Exception{
		//清除缓存中所有的操作
		ModelAndView view = new ModelAndView("/financial/loanOff/showListLoanOffAll");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		return view;
	}
	/**
	 * 展示借款信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showListLoanAll")
	public ModelAndView showListLoanAll() throws Exception{
		//清除缓存中所有的操作
		ModelAndView view = new ModelAndView("/financial/loan/showListLoanAll");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		return view;
	}



	/**
	 * 财务办公页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewFinancialOffices")
	public ModelAndView viewFinancialOffices(HttpServletRequest request) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/financial/financialCenter");
		view.addObject("userInfo", userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.MENU_FEE_APPLY);
		return view;
	}
	
	/**
	 * 查询财务办公各模块总数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryOfficesCounts")
	public Map<String,Object> queryOfficesCounts() {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer countLoan = financialService.countLoans(userInfo);
		Integer countLoanOff = financialService.countLoanOffs(userInfo);
		map.put("status", "y");
		map.put("countLoanOff", countLoanOff);
		map.put("countLoan", countLoan);
		return map;
	}
	
	/**
	 * 借款审核页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listLoansPage")
	public ModelAndView listLoans(HttpServletRequest request,FeeLoan loan) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/financial/financialOffice/listLoans");
		view.addObject("userInfo", userInfo);
		List<FeeLoan> listLoans =  financialService.listPagedLoans(userInfo,loan);
		view.addObject("listLoans", listLoans);
		view.addObject("loan", loan);
		return view;
	}

	/**
	 * 报销审核页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listLoanOffsPage")
	public ModelAndView listLoanOffs(HttpServletRequest request,FeeLoanOff loanOff) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/financial/financialOffice/listLoanOffs");
		view.addObject("userInfo", userInfo);
		List<FeeLoanOff> listLoanOffs =  financialService.listPagedLoanOffs(userInfo,loanOff);
		view.addObject("listLoanOffs", listLoanOffs);
		view.addObject("loanOff", loanOff);
		return view;
	}

	/**
	 * 查询个人费用各模块总数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/personalApply/queryCounts")
	public Map<String,Object> queryCounts() {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null == userInfo){
			map.put("status", "f");
			map.put("info",CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//待销消费记录总数
		Integer countUrConsume = consumeService.countUrConsume(userInfo);
		//报销申请总数
		Integer  countLoanOff = financialService.countPersonalLoanOffOfAuthV2(userInfo);
		//借款申请总数
		Integer countLoan = financialService.countPersonalLoanOfAuthV2(userInfo);
		map.put("status", "y");
		map.put("countUrConsume", countUrConsume);
		map.put("countLoanOff", countLoanOff);
		map.put("countLoan", countLoan);
		return map;
	}

	/**************************************************************/
	
	/**************************审批查看费用报销详情开始************************************/
	
	/**
	 * 查看费用预算信息
	 * @param feeBudgetId 预算主键
	 * @param instanceId 预算审批流程主键
	 * @return
	 */
	@RequestMapping("/finalForSpDetail/feeBugdetDetail")
	public ModelAndView feeBugdetDetail(Integer feeBudgetId,Integer instanceId){
		//清除缓存中所有的操作
 		ModelAndView mav = new ModelAndView("/financial/personalApply/detailsFeebudget_view");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		
		FeeBudget feeBudget = financialService.getFeeBugdetDetail(feeBudgetId,sessionUser);
		mav.addObject("feeBudget",feeBudget);
		
		return mav;
	}
	/**
	 * 查看借款的详情
	 * @param loanId
	 * @param instanceId
	 * @return
	 */
	@RequestMapping("/finalForSpDetail/feeLoanDetail")
	public ModelAndView feeLoanDetail(Integer loanId,Integer instanceId){
		ModelAndView view = new ModelAndView("/financial/personalApply/detailsLoan_view");
		UserInfo sessionUser = this.getSessionUser();

		FeeLoan feeLoanParam = new FeeLoan();
		feeLoanParam.setInstanceId(instanceId);
        FeeLoan feeLoan = financialService.getLoanForDetails(feeLoanParam,sessionUser);
        if((null != feeLoanParam.getInstanceId() && feeLoanParam.getInstanceId() > 0)){
            List<Consume> listConsume = workFlowService.listLayoutDetailForConsume(sessionUser,feeLoanParam.getInstanceId());
            view.addObject("consumes",listConsume);
        }

        FeeBudget feeBudget = financialService.getFeeBugdetDetail(feeLoan.getFeeBudgetId(),sessionUser);
        view.addObject("feeBudget",feeBudget);
        view.addObject("userInfo",sessionUser);
        view.addObject("loan",feeLoan);

		return view;
	}
	/**
	 * 查看汇报的详情
	 * @param loanReportId
	 * @param instanceId
	 * @return
	 */
	@RequestMapping("/finalForSpDetail/feeLoanRepDetail")
	public ModelAndView feeLoanRepDetail(Integer loanReportId,Integer instanceId){
		ModelAndView view = new ModelAndView("/financial/personalApply/detailsLoanOff_view");
		UserInfo curUser = this.getSessionUser();

		FeeLoanOff feeLoanOffParam = new FeeLoanOff();
		//查询报销信息
		FeeLoanOff loanOff = financialService.queryLoanOffByRepId(loanReportId, curUser.getComId());
		if(!CommonUtil.isNull(loanOff)) {
			feeLoanOffParam.setInstanceId(loanOff.getInstanceId());
		}else {
			feeLoanOffParam.setInstanceId(0);
		}
		
		FeeLoanOff feeLoanOff = financialService.getLoanOffAccountForDetails(feeLoanOffParam,curUser);
		view.addObject("loanOffAccount",feeLoanOff);

        //如果是一般报销则查询其借款申请
        if(null != feeLoanOff && null != feeLoanOff.getIsBusinessTrip() && feeLoanOff.getIsBusinessTrip() == 0 && null != feeLoanOff.getFeeBudgetId()){
            FeeBudget feeBudget = financialService.getFeeBudGetById(feeLoanOff.getFeeBudgetId(),curUser);
            view.addObject("loanApply",feeBudget);
        }

		//出差报销绑定的消费记录
		if(null != loanOff && loanOff.getInstanceId() > 0){
			List<Consume> consumes = financialService.listConsumeFromSpFlowData(curUser,loanOff.getInstanceId());
			view.addObject("consumes",consumes);
		}
		
		FeeLoanReport feeLoanReport = financialService.queryFeeLoanReportById(loanReportId);
        //查询出差汇报
        feeLoanOffParam.setLoanRepInsId(feeLoanReport.getInstanceId());
        feeLoanOffParam.setInstanceId(null);
        FeeLoanOff feeLoan = financialService.getLoanOffAccountForDetails(feeLoanOffParam,curUser);
        view.addObject("loanReport",feeLoan);

		Boolean isLoan = null != feeLoanOff && null != feeLoanOff.getIsBusinessTrip() && feeLoanOff.getIsBusinessTrip() == TripFeeEnum.YES.getValue();
		Boolean hasReport = null != feeLoan && null != feeLoan.getLoanWay() && feeLoan.getLoanWay().equals(TripFeeEnum.YES.getValue().toString());
		//是否为出差
		if(isLoan || hasReport){
            //查询出差的基本信息
            FeeBudget feeBudgets = financialService.getFeeBugdetDetail(isLoan ? feeLoanOff.getFeeBudgetId() : feeLoan.getFeeBudgetId(),curUser);
            view.addObject("businessApply",feeBudgets);
            view.addObject("feeBudget",feeBudgets);
		}

		return view;
	}
	/**
	 * 查看报销详情
	 * @param loanOffId
	 * @param instanceId
	 * @return
	 */
	@RequestMapping("/finalForSpDetail/feeLoanOffDetail")
	public ModelAndView feeLoanOffDetail(Integer loanOffId,Integer instanceId){
		ModelAndView view = new ModelAndView("/financial/personalApply/detailsLoanOff_view");
		UserInfo curUser = this.getSessionUser();

		FeeLoanOff feeLoanOffParam = new FeeLoanOff();
		//查询报销信息
		feeLoanOffParam.setInstanceId(instanceId);
		FeeLoanOff feeLoanOff = financialService.getLoanOffAccountForDetails(feeLoanOffParam,curUser);
		view.addObject("loanOffAccount",feeLoanOff);

        //如果是一般报销则查询其借款申请
        if(null != feeLoanOff && null != feeLoanOff.getIsBusinessTrip() && feeLoanOff.getIsBusinessTrip() == 0 && null != feeLoanOff.getFeeBudgetId()){
            FeeBudget feeBudget = financialService.getFeeBudGetById(feeLoanOff.getFeeBudgetId(),curUser);
            view.addObject("loanApply",feeBudget);
        }

		//出差报销绑定的消费记录
		if(null != instanceId && instanceId > 0){
			List<Consume> consumes = financialService.listConsumeFromSpFlowData(curUser,instanceId);
			view.addObject("consumes",consumes);
		}
		
		FeeLoanOff loanOff = financialService.queryLoanOffById(loanOffId);
		FeeLoanReport feeLoanReport = financialService.queryFeeLoanReportById(loanOff.getLoanReportId());
        //查询出差汇报
		if(!CommonUtil.isNull(feeLoanReport)) {
			feeLoanOffParam.setLoanRepInsId(feeLoanReport.getInstanceId());
		}else {
			feeLoanOffParam.setLoanRepInsId(0);
		}
        
        feeLoanOffParam.setInstanceId(null);
        FeeLoanOff feeLoan = financialService.getLoanOffAccountForDetails(feeLoanOffParam,curUser);
        view.addObject("loanReport",feeLoan);

		Boolean isLoan = null != feeLoanOff && null != feeLoanOff.getIsBusinessTrip() && feeLoanOff.getIsBusinessTrip() == TripFeeEnum.YES.getValue();
		Boolean hasReport = null != feeLoan && null != feeLoan.getLoanWay() && feeLoan.getLoanWay().equals(TripFeeEnum.YES.getValue().toString());
		//是否为出差
		if(isLoan || hasReport){
			//查询出差的基本信息
			FeeBudget feeBudget = financialService.getFeeBudGetById(isLoan ? feeLoanOff.getFeeBudgetId() : feeLoan.getFeeBudgetId(),curUser);
			view.addObject("businessApply",feeBudget);
		}
		return view;
	}
	
	
	
	
	
	/**************************审批查看费用报销详情结束************************************/
	/**************************************************************/
	
	/**
	 * 直接销账页面
	 * @author hcj 
	 * @param request
	 * @return
	 * @throws Exception 
	 * @date 2018年9月11日 下午3:45:23
	 */
	@RequestMapping("/directBalancePage")
	public ModelAndView directBalancePage(HttpServletRequest request) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/financial/personalApply/directBalancePage");
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 直接销账
	 * @author hcj 
	 * @param id
	 * @return 
	 * @date 2018年9月5日 下午4:09:59
	 */
	@ResponseBody
	@RequestMapping(value = "/directBalance")
	public String directBalance(Integer id,String content) {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = financialService.updateDirectBalance(id, userInfo,content);
		if (succ) {
			return "操作成功";
		} else {
			return "操作失败";
		}
	}
}
