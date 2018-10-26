package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.ModAdmin;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.ModAdminService;

/**
 * 指定模块的管理人员
 * @author zzq
 *
 */
@Controller
@RequestMapping("/modAdmin")
public class ModAdminController extends BaseController{

	@Autowired
	ModAdminService modAdminService;
	
	/**
	 * 跳转管理人员维护界面
	 * @return
	 */
	@RequestMapping(value="/editModAdminPage")
	public ModelAndView editForceInPersionPage(String busType){
		ModelAndView view = new ModelAndView("/adminCfg/adminCfg_center");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限
			view = new ModelAndView("/index");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		view.addObject("toPage","../zhbg/modAdmin/editModAdmin.jsp");
		
		view.addObject("busType", busType);
		
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
	}
	/**
	 * 跳转管理人员维护界面
	 * @return
	 */
	@RequestMapping(value="/editModAdminSinglePage")
	public ModelAndView editModAdminSinglePage(String busType){
		ModelAndView view = new ModelAndView("/zhbg/modAdmin/editModAdminSingle");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		view.addObject("busType", busType);
		
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
	}
	/**
	 * 取得指定模块的管理人员
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxListModAdmin")
	public Map<String,Object> ajaxListForceInPerson(String busType){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//取得指定模块的监督人员
		List<ModAdmin> listModAdmin = modAdminService.listModAdmin(sessionUser.getComId(),busType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("listModAdmin", listModAdmin);
		return map;
	}
	
	/**
	 * 修改模块的管理人员
	 * @param modAdminStr 模块管理人员json字符串
	 * @param busType 模块类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateModAdmin")
	public Map<String,Object> updateModAdmin(String modAdminStr,String busType){
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
		modAdminService.updateModAdmin(sessionUser,modAdminStr,busType);
		
		//取得指定模块的监督人员
		List<ModAdmin> listModAdmin = modAdminService.listModAdmin(sessionUser.getComId(),busType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("listModAdmin", listModAdmin);
		return map;
	}
	/**
	 * 删除模块管理人员
	 * @param modAdmin 模块管理人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delModAdmin")
	public Map<String,Object> delModAdmin(ModAdmin modAdmin){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}else if(Integer.parseInt(sessionUser.getAdmin()) >0){
			modAdminService.delModAdmin(sessionUser,modAdmin);
		}else{
			map.put(ConstantInterface.TYPE_STATUS, "f1");
			map.put(ConstantInterface.TYPE_INFO,"你不是管理员，不能进行维护");
			return map;
			
		}
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 验证是否为模块的管理人员
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authCheckModAdmin")
	public Map<String,Object> authCheckModAdmin(String[] busType){
		Map<String,Object> map = new  HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}else{
			if(null!=busType &&busType.length==1){
				//取得指定模块的监督人员
				boolean modAdminFlag = modAdminService.authCheck(sessionUser,busType[0]);
				map.put("modAdminFlag", modAdminFlag);
			}else{
				Map<String,Boolean> modAdminFlag = modAdminService.authCheck(sessionUser,busType);
				map.put("modAdminFlag", modAdminFlag);
			}
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		}
		return map;
	}
	
}