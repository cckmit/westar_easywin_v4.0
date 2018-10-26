package com.westar.core.web.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.core.service.ModOptConfService;

@Controller
@RequestMapping("/modOptConf")
public class ModOptConfController extends BaseController{

	@Autowired
	ModOptConfService modOptConfService;
	
	/**
	 * 跳转客户中心操作配置页面
	 * @return
	 */
	@RequestMapping("/modOperateConfigPage")
	public ModelAndView modOperateConfigPage(String moduleType){
		ModelAndView view = new ModelAndView("/modOptConf/modOperateConfig");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限
			view = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return view;
		}
		view.addObject("userInfo",userInfo);
		
		//模块的操作权限
		List<ModuleOperateConfig> list = modOptConfService.listModOptConfig(userInfo.getComId(), moduleType);
		view.addObject("list",list);
		view.addObject("moduleType",moduleType);
		return view;
	}
	
	/**
	 * 客户中心操作配置
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/modOperateConfig")
	public ModelAndView modOperateConfig(ModuleOperateConfig config) throws Exception{
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		boolean succ = modOptConfService.updateModOperateConfig(config, userInfo);
		if(succ){
			this.setNotification(Notification.SUCCESS, "配置成功!");
		}else{
			this.setNotification(Notification.ERROR, "配置失败!");
		}
		return view;
	}
	
	/**
	 * 异步方式变更操作配置
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/updateOperateConfig")
	public String updateOperateConfig(ModuleOperateConfig config) throws Exception {
		UserInfo userInfo = this.getSessionUser();
		boolean succ =  modOptConfService.updateModOperateConfig(config, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
}