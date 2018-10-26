package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.FeeLoan;
import com.westar.base.model.FeeLoanOff;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.BalanceService;
import com.westar.core.service.BusRelateSpFlowService;
import com.westar.core.service.FinancialService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.WorkFlowService;
import com.westar.core.web.FreshManager;

@Controller
@RequestMapping("/balance")
public class BalanceController extends BaseController{

	@Autowired
	BalanceService balanceService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	BusRelateSpFlowService busRelateSpFlowService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	FinancialService financialService;
	
	/**
	 * 完成结算、借款
	 * @param instanceId流程id
	 * @param busId借款或者报销id
	 * @param type区分借款还是报销
	 * @param payType支付方式
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addBanlance")
	public  Map<String, Object> addBanlance(Integer instanceId,Integer busId,String type,String payType,String content,String usePhone) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		Boolean flag =  true;
		//添加数据住区数据信息
		balanceService.addBanlance(userInfo, instanceId, busId, type,payType,content,usePhone);
		if (flag) {
			map.put("status", "y");
			map.put("msg", "操作成功");
		} else {
			map.put("status", "n");
			map.put("msg", "操作失败");
		}
		return map;
	}
	
	/**
	 * 批量结算
	 * @param ids通知的借款或者报销id
	 * @param redirectPage
	 * @param type区分借款或者报销
	 * @return
	 */
	@RequestMapping("/addBalances")
	public ModelAndView addBlances(Integer[] ids,String redirectPage,String type){
		UserInfo userInfo = this.getSessionUser();
		try {
			balanceService.addBalances(ids,userInfo,type);
			this.setNotification(Notification.SUCCESS, "操作成功");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "操作失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	
	/**
	 *  跳转完成借款页面
	 * @param request
	 * @param type区分结算还是借款
	 * @param instanceId流程id
	 * @param busId借款或者报销id
	 * @return
	 */
	@RequestMapping("/banlancePage")
	public ModelAndView banlancePage(HttpServletRequest request,String type,Integer instanceId,Integer busId){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/financial/financialOffice/banlancePage");
		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm);
		view.addObject("userInfo", userInfo);
		view.addObject("nowTime", nowTime);
		SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(instanceId, userInfo);
		if(ConstantInterface.MENU_LOAN.equals(type)){
			//查询借款信息
			FeeLoan loan = busRelateSpFlowService.getLoanForBalance(userInfo,instanceId,busId);
			view.addObject("loan", loan);
			String content = "您的\" " + spFlowInstance.getFlowName() + " \"借款申请已完成借款；如有疑问请联系："
					+userInfo.getUserName()+"；联系电话："+(CommonUtil.isNull(userInfo.getMovePhone())?"":userInfo.getMovePhone());
			view.addObject("content", content);
		}else{
			//查询报销信息
			FeeLoanOff loanOff = busRelateSpFlowService.getLoanOffForBalance(userInfo,instanceId,busId);
			view.addObject("loanOff", loanOff);
			String content = "您的\" " + spFlowInstance.getFlowName() + " \"报销申请已完成结算；如有疑问请联系："
					+userInfo.getUserName()+"；联系电话："+(CommonUtil.isNull(userInfo.getMovePhone())?"":userInfo.getMovePhone());
			view.addObject("content", content);
		}
		
		return view;
	}
	/**
	 *  跳转消息通知页面
	 * @param request
	 * @param type区分借款还是报销
	 * @param applyName报销或者借款名
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/noticePage")
	public ModelAndView noticePage(HttpServletRequest request,String type,String applyName) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/financial/financialOffice/noticePage");
		if("0".equals(type)) {
			String content = "您的\" " + applyName + " \"借款申请已审核通过；请到财务室进行领款。联系人："
					+userInfo.getUserName()+"；联系电话："+(CommonUtil.isNull(userInfo.getMovePhone())?"":userInfo.getMovePhone());
			view.addObject("content", content);
		}else {
			String content = "您的\" " + applyName + " \"报销申请已审核通过；请到财务室进行结算。联系人："
					+userInfo.getUserName()+"；联系电话："+(CommonUtil.isNull(userInfo.getMovePhone())?"":userInfo.getMovePhone());
			view.addObject("content", content);
		}
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 添加消息提醒
	 * @param type区分报销还是借款
	 * @param busId报销或者借款id
	 * @param instanceId流程id
	 * @param proposer提醒的对象id
	 * @param content消息内容
	 * @param usePhone是否发送短信
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addNotice")
	public  Map<String, Object> addNotice(String type,Integer busId,Integer instanceId,Integer proposer ,String content,String usePhone) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		Boolean flag = balanceService.addNotice(userInfo, type, busId, instanceId, proposer, content, usePhone);
		if (flag) {
			map.put("status", "y");
			map.put("msg", "发送成功");
		} else {
			map.put("status", "n");
			map.put("msg", "发送失败");
		}
		return map;
	}
	
	/**
	 * 批量通知
	 * @param ids通知的借款或者报销id
	 * @param redirectPage
	 * @param type区分借款或者报销
	 * @return
	 */
	@RequestMapping("/addMoreNotices")
	public ModelAndView addMoreNotices(Integer[] ids,String redirectPage,String type){
		UserInfo userInfo = this.getSessionUser();
		try {
			balanceService.addMoreNotices(ids,userInfo,type);
			this.setNotification(Notification.SUCCESS, "操作成功");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "操作失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	/**
	 * 查看领款通知
	 * @param redirectPage
	 * @param id消息提醒id
	 * @param busType区分模块
	 * @param busId流程id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewNotices")
	public ModelAndView viewNotices(String redirectPage,Integer id,String busType,Integer busId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		
		TodayWorks todayWorks = todayWorksService.getMsgTodoById(id, busType, busId, userInfo);
		todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_NOTIFICATIONS, 0);
		
		ModelAndView mav = new ModelAndView("/financial/financialOffice/viewNotice");
		mav.addObject("todayWorks", todayWorks);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 查看完成结算通知
	 * @param redirectPage
	 * @param id消息提醒id
	 * @param busType区分模块
	 * @param busId流程id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewBalancedNotices")
	public ModelAndView viewBalancedNotices(String redirectPage,Integer id,String busType,Integer busId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		
		TodayWorks todayWorks = todayWorksService.getMsgTodoById(id, busType, busId, userInfo);
		todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_BALANCED, 0);
		
		ModelAndView mav = new ModelAndView("/financial/financialOffice/viewBalancedNotice");
		SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(busId, userInfo);
		FeeLoan loan = financialService.queryLoanByInsId(busId);//查询借款或者结算id
		if(!CommonUtil.isNull(loan)) {
			if(ConstantInterface.TYPE_FEE_APPLY_DAYLY.equals(spFlowInstance.getBusType()) 
					|| ConstantInterface.TYPE_LOAN_TRIP.equals(spFlowInstance.getBusType())
					|| ConstantInterface.TYPE_LOAN_DAYLY.equals(spFlowInstance.getBusType())) {
				mav.addObject("busType", ConstantInterface.MENU_LOAN);
				//查询借款信息
				FeeLoan feeloan = financialService.queryLoanById(loan.getId());
				mav.addObject("loan", feeloan);
				UserInfo user = userInfoService.getUserInfo(feeloan.getComId(), feeloan.getBalanceUserId());
				mav.addObject("balanceUserName", user.getUserName());
			}else {
				mav.addObject("busType", ConstantInterface.MENU_LOANOFF);
				//查询报销信息
				FeeLoanOff loanOff = financialService.queryLoanOffById(loan.getId());
				mav.addObject("loan", loanOff);
				UserInfo user = userInfoService.getUserInfo(loanOff.getComId(), loanOff.getBalanceUserId());
				mav.addObject("balanceUserName", user.getUserName());
			}
		}
		
		mav.addObject("todayWorks", todayWorks);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 查看财务结算通知
	 * @param redirectPage
	 * @param id消息提醒id
	 * @param busType区分模块
	 * @param busId流程id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewBalanceNotices")
	public ModelAndView viewBalanceNotices(String redirectPage,Integer id,String busType,Integer busId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		TodayWorks todayWorks = todayWorksService.getMsgTodoById(id, busType, busId, userInfo);
		todayWorksService.updateTodoWorkRead(busId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_FINALCIAL_BALANCE, 0);
		ModelAndView mav = new ModelAndView("/financial/financialOffice/viewBalanceNotice");
		SpFlowInstance spFlowInstance = workFlowService.getSpFlowInstanceById(busId, userInfo);
		FeeLoan loan = financialService.queryLoanByInsId(busId);//查询借款或者结算id
		if(!CommonUtil.isNull(loan)) {
			mav.addObject("busId", loan.getId());
			if(ConstantInterface.TYPE_FEE_APPLY_DAYLY.equals(spFlowInstance.getBusType()) 
					|| ConstantInterface.TYPE_LOAN_TRIP.equals(spFlowInstance.getBusType())
					|| ConstantInterface.TYPE_LOAN_DAYLY.equals(spFlowInstance.getBusType())) {
				mav.addObject("busType", ConstantInterface.MENU_LOAN);
				//查询借款信息
				FeeLoan feeloan = busRelateSpFlowService.getLoanForBalance(userInfo,busId,loan.getId());
				mav.addObject("loan", feeloan);
			}else {
				mav.addObject("busType", ConstantInterface.MENU_LOANOFF);
				//查询报销信息
				FeeLoanOff loanOff = busRelateSpFlowService.getLoanOffForBalance(userInfo,busId,loan.getId());
				mav.addObject("loanOff", loanOff);
			}
		}
		mav.addObject("creatorName", spFlowInstance.getCreatorName());
		mav.addObject("applyTime", spFlowInstance.getRecordCreateTime());
		mav.addObject("instanceId", busId);
		mav.addObject("todayWorks", todayWorks);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
}