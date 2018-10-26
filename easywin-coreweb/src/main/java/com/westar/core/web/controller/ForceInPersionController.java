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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.ForceInPersion;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.ForceInPersionService;

/**
 * 
 * 描述:监督人员的控制类
 * @author zzq
 * @date 2018年8月25日 上午11:50:12
 */
@Controller
@RequestMapping("/forceIn")
public class ForceInPersionController extends BaseController{

	@Autowired
	ForceInPersionService forceInPersionService;
	
	/**
	 * 跳转督察人员维护页面
	 * @return
	 */
	@RequestMapping(value="/editForceInPersionPage")
	public ModelAndView editForceInPersionPage(String busType){
		ModelAndView view = new ModelAndView("/adminCfg/adminCfg_center");
		view.addObject("toPage","../forceIn/editForceInPersion.jsp");
		UserInfo userInfo = this.getSessionUser();
		//没有管理权限
		if(userInfo.getAdmin().equals("0")){
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		view.addObject("busType", busType);
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 修改单个模块参与人
	 * @param busType
	 * @return
	 */
	@RequestMapping(value="/editSingleForceInPersionPage")
	public ModelAndView editSingleForceInPersionPage(String busType){
		ModelAndView view = new ModelAndView("/forceIn/editSingleForceInPersion");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		view.addObject("busType", busType);
		return view;
	}
	/**
	 *检查当前人是否是监督人员
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authCheckForceIn")
	public Map<String,Object> authCheckForceIn(String busType){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		boolean flag = forceInPersionService.isForceInPersion(sessionUser, busType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("flag", flag);
		return map;
	}
	
	/**
	 *取得模块的监督人员
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListForceInPerson")
	public Map<String,Object> ajaxListForceInPerson(String busType){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//取得模块的监督人员
		List<ForceInPersion> listFordeInPerson = forceInPersionService.
				listForceInPerson(sessionUser.getComId(),busType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("listFordeInPerson", listFordeInPerson);
		return map;
	}
	/**
	 * 督察人员维护
	 * @param forceInUserStr
	 * @param busType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateForceInPersion")
	public Map<String,Object> updateForceInPersion(String forceInUserStr,String busType){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}else if("0".equals(sessionUser.getAdmin())){
			map.put(ConstantInterface.TYPE_STATUS, "f1");
			map.put(ConstantInterface.TYPE_INFO, "抱歉，你没有管理权限");
			return map;
		}
		List<ForceInPersion> forceInList = JSONArray.parseArray(forceInUserStr, ForceInPersion.class);
		if(null!=forceInList && !forceInList.isEmpty()){
			for (ForceInPersion forceIn : forceInList) {
				forceIn.setComId(sessionUser.getComId());
				forceIn.setType(busType);
				
			}
		}
		
		forceInPersionService.updateForceInPersion(sessionUser,forceInList,busType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		//取得模块的监督人员
		List<ForceInPersion> listFordeInPerson = forceInPersionService.
				listForceInPerson(sessionUser.getComId(),busType);
		map.put("listFordeInPerson", listFordeInPerson);
		return map;
	}
	/**
	 * 删除强制参与人
	 * @param forceIn
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delForceInPersion")
	public Map<String,Object> delForceInPersion(ForceInPersion forceIn){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}else if(Integer.parseInt(sessionUser.getAdmin()) >0){
			forceInPersionService.delForceInPersion(sessionUser,forceIn);
		}else{
			map.put(ConstantInterface.TYPE_STATUS, "f1");
			map.put(ConstantInterface.TYPE_INFO,"你不是管理员，不能进行维护");
			return map;
			
		}
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
}