package com.westar.core.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.SysUpgradeLog;
import com.westar.base.model.UserInfo;
import com.westar.base.util.StringUtil;
import com.westar.core.service.SysUpgradeLogService;
/**
 * 系统更新日志
 * @author ermu
 *
 */
@Controller
@RequestMapping("/sysUpLog")
public class SysUpgradeLogController extends BaseController  {
	
	@Autowired
	SysUpgradeLogService sysUpgradeLogService;
	
	/**
	 *跳转系统升级日志显示界面
	 */
	@RequestMapping("/toListSysUpLog")
	public ModelAndView toListSysUpLog(){
		ModelAndView view = new ModelAndView("/organic/organicCenter");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		return view;
	} 
	/**
	 * 系统升级日志按类型查询
	 * @param type 平台类型
	 * @return
	 */
	@RequestMapping("/listSysUpLog")
	public ModelAndView listSysUpLog(Integer type){
		ModelAndView view = new ModelAndView("/sysUpgradeLog/listUpLogForType");
		List<SysUpgradeLog> listUpLog = sysUpgradeLogService.listSysUpLog(null,type);
		//保存到数据库的更新 内容转换成HTML显示
		for(SysUpgradeLog s :listUpLog){
			s.setContent(StringUtil.toHtml(s.getContent()));
		}
		view.addObject("listUpLog",listUpLog);
		
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		return view;
	} 
	
	/**
	 * 系统升级日志按类型滚动查询
	 * @param type 平台类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listNextSysUpLog")
	public Map<String, Object> listNextSysUpLog(Integer maxId, Integer type){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<SysUpgradeLog> listUpLog = sysUpgradeLogService.listSysUpLog(maxId,type);
		//保存到数据库的更新 内容转换成HTML显示
		for(SysUpgradeLog s :listUpLog){
			s.setContent(StringUtil.toHtml(s.getContent()));
		}
		map.put("status", "y");
		map.put("listUpLog",listUpLog);
		
		return map;
	} 
}
