package com.westar.core.web.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Customer;
import com.westar.base.model.CustomerStage;
import com.westar.base.model.CustomerType;
import com.westar.base.model.FeedBackType;
import com.westar.base.model.ModAdmin;
import com.westar.base.model.ModuleChangeApply;
import com.westar.base.model.ModuleChangeExamine;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.CrmService;
import com.westar.core.service.ModAdminService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.ModuleChangeExamineService;
import com.westar.core.service.TodayWorksService;

@Controller
@RequestMapping("/moduleChangeExamine")
public class ModuleChangeExamineController extends BaseController{

	@Autowired
	ModuleChangeExamineService moduleChangeExamineService;
	
	@Autowired
	CrmService crmService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ModOptConfService modOptConfService;
	
	@Autowired
	ModAdminService modAdminService;
	
	
	/**
	 * 跳转模块操作配置页面
	 * @return
	 */
	@RequestMapping("/modConfigPage")
	public ModelAndView modConfigPage(String moduleType){
		ModelAndView view = new ModelAndView("/crm/customerList");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		//模块的操作权限
		List<ModuleOperateConfig> listModConf = modOptConfService.listModOptConfig(userInfo.getComId(), moduleType);
		view.addObject("listModConf",listModConf);
		view.addObject("userInfo",userInfo);
		view.addObject("moduleType",moduleType);
		if(ConstantInterface.TYPE_CRM.equals(moduleType)) {
			//头文件的显示
			view.addObject("homeFlag",ConstantInterface.TYPE_CRM);
		}
		return view;
	}
	
	/**
	 * 跳转属性变更审批配置页面
	 * @return
	 */
	@RequestMapping("/modChangeExamPage")
	public ModelAndView modChangeExamPage(String moduleType){
		ModelAndView view = new ModelAndView("/modChangeExam/modChangeExam");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		view.addObject("userInfo",userInfo);
		view.addObject("moduleType",moduleType);
		return view;
	}
	
	
	/**
	 * 异步方式变更属性变更配置
	 * @param moduleChangeExamine
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateChangeConfig")
	public String updateChangeConfig(ModuleChangeExamine moduleChangeExamine) {
		UserInfo userInfo = this.getSessionUser();
		boolean succ =  moduleChangeExamineService.updateExamConfig(moduleChangeExamine, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	/**
	 * 属性变更审批配置
	 * @param moduleChangeExamine
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/updateExamConfig")
	public ModelAndView updateExamConfig(ModuleChangeExamine moduleChangeExamine){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		boolean succ = moduleChangeExamineService.updateExamConfig(moduleChangeExamine, userInfo);
		if(succ){
			this.setNotification(Notification.SUCCESS, "配置成功!");
		}else{
			this.setNotification(Notification.ERROR, "配置失败!");
		}
		return view;
	}
	
	
	/**
	 * 根据业务类型等条件查询属性变更审批配置
	 * @param moduleChangeExamine
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetModChangeExam")
	public Map<String, Object> ajaxGetModChangeExam(ModuleChangeExamine moduleChangeExamine) {
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<ModuleChangeExamine> lists =  moduleChangeExamineService.listModChangeExam(moduleChangeExamine,userInfo);
		map.put("lists", lists);
		map.put("status", "y");
		List<ModAdmin> listExamUser = modAdminService.listModAdmin(userInfo.getComId(),ConstantInterface.TYPE_CHANGE_EXAM);
		if(!CommonUtil.isNull(listExamUser)) {
			int count = 0;
			for (int i = 0; i < listExamUser.size(); i++) {
				if(listExamUser.get(i).getUserId().equals(userInfo.getId())) {
					count++;
					break;
				}
			}
			if(count < 1) {
				List<ModAdmin> listNeedExamUser = modAdminService.listModAdmin(userInfo.getComId(),ConstantInterface.TYPE_NEED_EXAM_USER);
				if(!CommonUtil.isNull(listNeedExamUser)) {
					int countNeed = 0;
					for (int i = 0; i < listNeedExamUser.size(); i++) {
						if(listNeedExamUser.get(i).getUserId().equals(userInfo.getId())) {
							countNeed++;
							break;
						}
					} 
					if(countNeed > 0) {
						map.put("needExam", "y");
					}
				}else {
					map.put("needExam", "y");
				}
			}
		}
		
		return map;
	}
	
	/**
	 * 变更申请页面
	 * @param moduleChangeApply
	 * @return
	 */
	@RequestMapping("/modChangeApplyPage")
	public ModelAndView modChangeApplyPage(ModuleChangeApply moduleChangeApply){
		ModelAndView view = new ModelAndView("/modChangeExam/modChangeApply");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		view.addObject("moduleChangeApply",moduleChangeApply);
		if(ConstantInterface.TYPE_CRM.equals(moduleChangeApply.getModuleType())) {
			if("customerTypeId".equals(moduleChangeApply.getField())) {
				List<CustomerType> listCustomerType = crmService.listCustomerType(userInfo.getComId());
				view.addObject("listCustomerType",listCustomerType);
			}else if("owner".equals(moduleChangeApply.getField())) {
				List<FeedBackType> listFeedBackType = crmService.listFeedBackType(userInfo.getComId());
				view.addObject("listFeedBackType",listFeedBackType);
			}else if("stage".equals(moduleChangeApply.getField())) {
				List<CustomerStage> listCustomerStages = crmService.listCustomerStage(userInfo.getComId());
				view.addObject("listCustomerStages",listCustomerStages);
			}
			
		}
		//取得属性变更审批人员
		List<ModAdmin> listExamUser = modAdminService.listModAdmin(userInfo.getComId(),ConstantInterface.TYPE_CHANGE_EXAM);
		view.addObject("listExamUser",listExamUser);
		return view;
	}
	
	/**
	 * 批量变更申请页面
	 * @param moduleChangeApply
	 * @return
	 */
	@RequestMapping("/moreChangeApplyPage")
	public ModelAndView moreChangeApplyPage(ModuleChangeApply moduleChangeApply,Integer[] ids){
		ModelAndView view = new ModelAndView("/modChangeExam/moreChangeApply");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		view.addObject("moduleChangeApply",moduleChangeApply);
		if(ConstantInterface.TYPE_CRM.equals(moduleChangeApply.getModuleType())) {
			if (null != ids) {
				List<Customer> listCustomer = new ArrayList<Customer>();
				for (Integer id : ids) {
					Customer customer = crmService.getCrmById(id);
					listCustomer.add(customer);
				}
				view.addObject("listCustomer", listCustomer);
			}
			if("owner".equals(moduleChangeApply.getField())) {
				List<FeedBackType> listFeedBackType = crmService.listFeedBackType(userInfo.getComId());
				view.addObject("listFeedBackType",listFeedBackType);
			}
			
		}
		//取得属性变更审批人员
		List<ModAdmin> listExamUser = modAdminService.listModAdmin(userInfo.getComId(),ConstantInterface.TYPE_CHANGE_EXAM);
		view.addObject("listExamUser",listExamUser);
		return view;
	}
	
	/**
	 * 添加变更申请
	 * @param moduleChangeApply
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addApply")
	public Map<String, Object> addApply(ModuleChangeApply moduleChangeApply,Integer feedBackTypeId){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		moduleChangeExamineService.addApply(moduleChangeApply,feedBackTypeId,userInfo);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 批量添加变更申请
	 * @param moduleChangeApply
	 * @param feedBackTypeId
	 * @param busIds变更模块Id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addMoreApply")
	public Map<String, Object> addMoreApply(ModuleChangeApply moduleChangeApply,Integer feedBackTypeId,Integer[] busIds){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		moduleChangeExamineService.addMoreApply(moduleChangeApply,feedBackTypeId,userInfo,busIds);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 查看属性变更申请通知
	 * @param redirectPage
	 * @param id
	 * @param busType
	 * @param busId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewModChangeApply")
	public ModelAndView viewModChangeApply(String redirectPage,Integer id,String busType,Integer busId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		todayWorksService.updateTodayWorksReadStateTo1(busType, busId, userInfo.getId());
		ModelAndView mav = new ModelAndView("/modChangeExam/viewModChangeApply");
		mav.addObject("userInfo", userInfo);
		ModuleChangeApply moduleChangeApply = moduleChangeExamineService.queryModChangeApplyById(busId);
		mav.addObject("moduleChangeApply", moduleChangeApply);
		TodayWorks todayWorks = todayWorksService.getMsgTodoById(id, busType, busId, userInfo);
		mav.addObject("todayWorks", todayWorks);
		return mav;
	}
	
	/**
	 * 审批属性变更申请
	 * @param moduleChangeExamine
	 * @return
	 */
	@RequestMapping("/updateApply")
	public ModelAndView updateApply(ModuleChangeApply moduleChangeApply){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(), userInfo.getId(), moduleChangeApply.getId(), ConstantInterface.TYPE_CHANGE_EXAM, 0);
		if(CommonUtil.isNull(todayWorks)) {
			this.setNotification(Notification.ERROR, "操作无效，该记录他人已审批!");
		}else {
			boolean succ = moduleChangeExamineService.updateApply(moduleChangeApply, userInfo);
			if(succ){
				this.setNotification(Notification.SUCCESS, "操作成功!");
			}else{
				this.setNotification(Notification.ERROR, "操作失败!");
			}
		}
		
		return view;
	}
	
}