package com.westar.core.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.westar.base.model.SpFlowModel;
import com.westar.core.service.WorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.BusAttrMapFormCol;
import com.westar.base.model.BusMapFlow;
import com.westar.base.model.FormCompon;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.KeyValue;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.core.service.AdminCfgService;
import com.westar.core.web.FreshManager;

/**
 * 管理员配置控制类
 * @author luojian
 *
 */

@Controller
@RequestMapping("/adminCfg")
public class AdminCfgController extends BaseController {

	@Autowired
	AdminCfgService adminCfgService;

    @Autowired
	WorkFlowService workFlowService;

    /**
	 * 公文操作与审批流程关联
	 * @param busMapFlow 配置描述
	 * @return
	 */
	@RequestMapping(value="/initBusMapFlow")
	public ModelAndView initBusMapFlow(BusMapFlow busMapFlow){
		ModelAndView view = new ModelAndView("/adminCfg/conf/busMapFlowCfg");
		if(null!=busMapFlow.getFlowId() && busMapFlow.getFlowId()>0){
			UserInfo userInfo = this.getSessionUser();
			Integer actionId = adminCfgService.initBusMapFlow(userInfo,busMapFlow);
			view = new ModelAndView("redirect:/adminCfg/busMapFlowCfg?sid="+this.getSid()+"&actionId="+actionId+"&busType="+busMapFlow.getBusType());
			this.setNotification(Notification.SUCCESS, "配置成功!");
		}else{
			this.setNotification(Notification.ERROR, "配置失败，配置参数有误!");
		}
		return view;
	}
	
	/**
	 * 审批关联流程配置
	 * @param actionId
	 * @param busType 关联动作类型
	 * @return
	 */
	@RequestMapping("/busMapFlowCfg")
	public ModelAndView busMapFlowCfg(String busType,Integer actionId){
		ModelAndView view = new ModelAndView("/adminCfg/conf/busMapFlowCfg");
		UserInfo userInfo = this.getSessionUser();
		//头文件的显示
		view.addObject("userInfo", userInfo);
		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlow(userInfo.getComId(),busType,actionId);
		view.addObject("busMapFlow",busMapFlow);
		if(CommonUtil.isNull(busMapFlow)){
			//未配置过，直接打开配置页面
			return view;
		}
		//获取模块与审批表单映射关系
		List<KeyValue> listKeyMap = adminCfgService.listKeyMap(userInfo,busType,busMapFlow.getId());
		view.addObject("attrsMap",listKeyMap);
		List<FormCompon> listFlowFormCompons = adminCfgService.listFlowFormCompons(userInfo,busType,busMapFlow.getId());
		view.addObject("listFlowFormCompons",listFlowFormCompons);
		//取得费用管理中属性映射关系集合
		List<KeyValue> listFormColMapBusAttr = adminCfgService.listFormColMapBusAttr(userInfo,busType,busMapFlow.getId());
		//获取模块与审批表单映射关系
		view.addObject("listFormColMapBusAttr",listFormColMapBusAttr);
		return view;
	}

	/**
	 * 审批流程数据映射
	 * @param flowId
	 * @return
	 */
	@RequestMapping("/spMapFlowCfg")
	public ModelAndView spMapFlowCfg(Integer flowId){
		ModelAndView view = new ModelAndView("/adminCfg/conf/spMapFlowCfg");
		UserInfo userInfo = this.getSessionUser();
		//头文件的显示
		view.addObject("userInfo", userInfo);

        SpFlowModel spFlowModel = workFlowService.getSpFlowModelById(flowId);

		BusMapFlow busMapFlow = adminCfgService.queryBusMapFlowByFlowId(userInfo.getComId(),flowId);
		view.addObject("busMapFlow",busMapFlow);
		if(CommonUtil.isNull(busMapFlow)){
			//未配置过，直接打开配置页面
			return view;
		}
		//获取模块与审批表单映射关系
		List<KeyValue> listKeyMap = adminCfgService.listKeyMap(userInfo,busMapFlow.getBusType(),busMapFlow.getId());
		view.addObject("attrsMap",listKeyMap);
		List<FormCompon> listFlowFormCompons = adminCfgService.listFlowFormCompons(userInfo,busMapFlow.getBusType(),busMapFlow.getId());
		view.addObject("listFlowFormCompons",listFlowFormCompons);
		//取得费用管理中属性映射关系集合
		List<KeyValue> listFormColMapBusAttr = adminCfgService.listFormColMapBusAttr(userInfo,busMapFlow.getBusType(),busMapFlow.getId());
		//获取模块与审批表单映射关系
		view.addObject("listFormColMapBusAttr",listFormColMapBusAttr);
		return view;
	}


	/**
	 * 初始化表单数据与业务数据映射关系
	 * @param busMapFlow 表单数据与业务数据映射关系
	 * @return
	 */
	@RequestMapping("/initBusAttrMapFormCol")
	public ModelAndView initBusAttrMapFormCol(BusMapFlow busMapFlow){
		ModelAndView view =new ModelAndView("redirect:/adminCfg/busMapFlowCfg?sid="+this.getSid()+"&actionId="+busMapFlow.getId()+"&busType="+busMapFlow.getBusType());
		UserInfo userInfo = this.getSessionUser();
		adminCfgService.initbusAttrMapFormCol(busMapFlow,userInfo,busMapFlow.getBusType());
		this.setNotification(Notification.SUCCESS, "更新成功!");
		return view;
	}
	
	/**
	 * 异步更新表单数据与业务数据映射关系
	 * @param busAttrMapFormCol 表单数据与业务数据映射关系
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/initBusAttrMapFormColByAjax")
	public Map<String,Object> initBusAttrMapFormColByAjax(BusAttrMapFormCol busAttrMapFormCol){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		map.put("status", "f");
		if(null == userInfo){
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		adminCfgService.initbusAttrMapFormCol(busAttrMapFormCol,userInfo);
		map.put("status", "y");
		map.put("info","更新成功!");
		return map;
	}
	
	/**
	 * 删除公文操作与审批流程关联
	 * @param busMapFlow 配置信息
	 * @return
	 */
	@RequestMapping("/delBusMapFlow")
	public ModelAndView delBusMapFlow(BusMapFlow busMapFlow){
		ModelAndView view = new ModelAndView("redirect:/adminCfg/busMapFlowCfg?sid="+this.getSid()+"&busType="+busMapFlow.getBusType());
		if(null!=busMapFlow.getBusType() 
				&& !"".equals(busMapFlow.getBusType().trim()) 
				&& null!=busMapFlow.getId() 
				&& busMapFlow.getId()>0){
			UserInfo userInfo = this.getSessionUser();
			adminCfgService.delBusMapFlow(userInfo,busMapFlow.getBusType(),busMapFlow.getId());
		}
		this.setNotification(Notification.SUCCESS, "清除关联成功!");
		return view;
	}
	
	/**
	 * 批量删除配置
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/delBusMapFlowByBatch")
	public ModelAndView delBusMapFlowByBatch(Integer[] ids,String redirectPage) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView();
		try {
			adminCfgService.delBusMapFlowByBatch(userInfo,ids);
			this.setNotification(Notification.SUCCESS, "删除成功!");
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "删除失败!");
		}
		mav.setViewName("redirect:"+redirectPage);
		return mav;
	}
	
	/**
	 * 清除范围限制
	 * @param busMapFlow 映射关系对象
	 * @return
	 */
	@RequestMapping("/delBusMapFlowAuthDep")
	public ModelAndView delBusMapFlowAuthDep(BusMapFlow busMapFlow){
		ModelAndView view = new ModelAndView("redirect:/adminCfg/busMapFlowCfg?sid="+this.getSid()+"&actionId="+busMapFlow.getId()+"&busType="+busMapFlow.getBusType());
		UserInfo userInfo = this.getSessionUser();
		adminCfgService.delBusMapFlowAuthDep(busMapFlow,userInfo);
		this.setNotification(Notification.SUCCESS, "更新成功!");
		return view;
	}
	
	/**
	 * 跳转通用配置列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/conf/listBusMapFlow")
	public ModelAndView listBusMapFlow(HttpServletRequest request,BusMapFlow busMapFlow,String busType) throws Exception{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/adminCfg/adminCfg_center");
		UserInfo curUser = this.getSessionUser();
		view.addObject("userInfo",curUser);
		view.addObject("toPage","conf/listBusMapFlow.jsp");
		List<BusMapFlow> listBusMapFlow= adminCfgService.listBusMapFlow(curUser.getComId(),busType,busMapFlow);
		view.addObject("listBusMapFlow",listBusMapFlow);
		view.addObject("busType",busType);
		view.addObject("busMapFlow", busMapFlow);
		return view;
	}
	
	/**
	 * 取得自己权限需要映射的对象
	 * @param busType 业务类型
	 * @param feeBudgetId 业务依据主键（暂指费用报销、借款依据主键）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listBusMapFlowByAuth")
	public Map<String,Object> listBusMapFlowByAuth(String busType,Integer feeBudgetId){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}else{
			List<BusMapFlow> listBusMapFlows = null;
			if(CommonUtil.isNull(feeBudgetId)) {
				listBusMapFlows = adminCfgService.listBusMapFlowByAuth(sessionUser,busType);
			}else {
				listBusMapFlows = adminCfgService.listBusMapFlowByAuth(sessionUser,busType,feeBudgetId);
			}
			map.put("status", "y");
			map.put("listBusMapFlows",listBusMapFlows);
		}
		return map;
	}
	/**
	 * 列表数据信息
	 * @param busType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listBusMapSelect")
	public ModelAndView listBusMapSelect(String busType) throws Exception{
		ModelAndView view = new ModelAndView("/adminCfg/listBusMapSelect");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	
}
