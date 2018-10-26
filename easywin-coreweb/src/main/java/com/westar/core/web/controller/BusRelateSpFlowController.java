package com.westar.core.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.UserInfo;
import com.westar.core.service.BusRelateSpFlowService;

@Controller
@RequestMapping("/busRelateSpFlow")
public class BusRelateSpFlowController extends BaseController{

	@Autowired
	BusRelateSpFlowService busRelateSpFlowService;
	
	/**
	 * 发起出差申请
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @return
	 */
	@RequestMapping("/loanApply/addLoanApply")
	public ModelAndView addFeeBudget(Integer busMapFlowId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addFeeBudget(busMapFlowId,userInfo,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/**
	 * 针对申请申请借款
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @param feeBudgetId 申请记录主键
	 * @return
	 */
	@RequestMapping("/loan/addLoan")
	public ModelAndView addLoan(Integer busMapFlowId,Integer feeBudgetId,String busType,Integer isBusinessTrip){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addLoan(busMapFlowId,userInfo,feeBudgetId,busType,isBusinessTrip);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	
	
	
	/**
	 * 针对申请申请借款
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @param feeBudgetId 申请记录主键
	 * @return
	 */
	@RequestMapping("/loanReport/addLoanReport")
	public ModelAndView addLoanReport(Integer busMapFlowId,Integer feeBudgetId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addLoanReport(busMapFlowId,userInfo,feeBudgetId,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	
	/**
	 * 发起报销申请
	 * @param busMapFlowId
	 * @param loanWay
	 * @param feeBudgetId
	 * @param loanReportWay
	 * @param loanReportId
	 * @param busType
	 * @return
	 */
	@RequestMapping("/loanOff/addLoanOff")
	public ModelAndView addLoanOff(Integer busMapFlowId,String loanWay,Integer feeBudgetId,
			String loanReportWay,Integer loanReportId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addLoanOff(busMapFlowId,userInfo,busType,loanWay,feeBudgetId,loanReportWay,loanReportId);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		view.addObject("spFlowInstance",spFlowInstance);
		
		return view;
	}
	
	/****************************费用管理结束*******************************************/
	
	/****************************考勤开始*******************************************/
	/**
	 * 发起请假申请
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @return
	 */
	@RequestMapping("/attence/addLeaveApply")
	public ModelAndView addLeaveApply(Integer busMapFlowId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addLeaveApply(busMapFlowId,userInfo,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/**
	 * 发起加班申请
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @return
	 */
	@RequestMapping("/attence/addOverTimeApply")
	public ModelAndView addOverTimeApply(Integer busMapFlowId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addOverTimeApply(busMapFlowId,userInfo,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/****************************考勤结束*******************************************/

	/****************************需求映射开始*******************************************/
	/**
	 * 发起需求处理流程
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @return
	 */
	@RequestMapping("/demand/addDemanfHandleApply")
	public ModelAndView addDemanfHandleApply(Integer busMapFlowId,String busType,Integer demandId){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addDemanfHandleApply(busMapFlowId,userInfo,busType,demandId);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/****************************需求映射结束*******************************************/
	
	
	/****************************运维过程开始*******************************************/
	/**
	 * 新增事项反馈
	 * @param busMapFlowId 模块操作与审批流程之间关联主键
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping("/eventPm/addEventPm")
	public ModelAndView addEventPm(Integer busMapFlowId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addEventPm(busMapFlowId,userInfo,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/**
	 * 添加问题过程管理
	 * @param busMapFlowId
	 * @param busType
	 * @return
	 */
	@RequestMapping("/issuePm/addIssuePm")
	public ModelAndView addIssuePm(Integer busMapFlowId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addIssuePm(busMapFlowId,userInfo,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/**
	 * 添加变更过程管理
	 * @param busMapFlowId 映射关系
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping("/modifyPm/addModifyPm")
	public ModelAndView addModifyPm(Integer busMapFlowId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addModifyPm(busMapFlowId,userInfo,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/**
	 * 添加变更过程管理
	 * @param busMapFlowId 映射关系
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping("/releasePm/addReleasePm")
	public ModelAndView addReleasePm(Integer busMapFlowId,String busType){
		ModelAndView view = new ModelAndView("/sp/form/addFormData");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		SpFlowInstance spFlowInstance = busRelateSpFlowService.addReleasePm(busMapFlowId,userInfo,busType);
		
		Integer formLayoutState = spFlowInstance.getFormLayoutState();
		if(null!=formLayoutState && formLayoutState==1){
			view.setViewName("/sp/form/addFormDataDev");
		}
		
		view.addObject("spFlowInstance",spFlowInstance);
		return view;
	}
	/****************************运维过程结束*******************************************/
}