package com.westar.core.web.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.SystemLog;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WorkTrace;
import com.westar.base.pojo.Notification;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.UserInfoService;

@Controller
@RequestMapping("/systemLog")
public class SystemLogController extends BaseController{
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 分页查询系统日志列表信息
	 * @param systemLog
	 * @return
	 */
	@RequestMapping(value="listPagedOrgSysLog")
	public ModelAndView listPagedOrgSysLog(SystemLog systemLog){
		//当前操作人员
		UserInfo userInfo = this.getSessionUser();
		//设置企业号
		systemLog.setComId(userInfo.getComId());
		//
		List<SystemLog> list = systemLogService.listPagedOrgSysLog(systemLog);
		ModelAndView mav = new ModelAndView("/organic/organicCenter", "list", list);
		mav.addObject("userInfo",userInfo);
		mav.addObject("systemLog",systemLog);
		
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		mav.addObject("listOwners",listOwners);
		
		return mav;
	}
	/**
	 * 分页查询个人系统日志列表信息
	 * @param systemLog
	 * @return
	 */
	@RequestMapping(value="listPagedSelfSysLog")
	public ModelAndView listPagedSelfSysLog(SystemLog systemLog){
		//当前操作人员
		UserInfo userInfo = this.getSessionUser();
		//设置企业号
		systemLog.setComId(userInfo.getComId());
		//设置操作人员主键
		systemLog.setUserId(userInfo.getId());
		List<SystemLog> list = systemLogService.listPagedSelfSysLog(systemLog);
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter", "list", list);
		
		mav.addObject("userInfo",userInfo);
		mav.addObject("systemLog",systemLog);
		return mav;
	}
	
	/**
	 * 批量删除系统日志
	 * @param ids id数组
	 * @param redirectPage 重定向路径
	 * @return
	 */

	@RequestMapping(value="delSystemLogs",method=RequestMethod.POST)
	public ModelAndView delSystemLogs(Integer[] ids, String redirectPage){
		systemLogService.delSystemLogByIds(ids);
		this.setNotification(Notification.SUCCESS, "删除成功!");
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 删除系统日志
	 * @param id 
	 * @param redirectPage 重定向路径
	 * @return
	 */
	@RequestMapping(value="delSystemLog")
	public ModelAndView delSystemLog(Integer id, String redirectPage){
		systemLogService.delSystemLogById(id);
		this.setNotification(Notification.SUCCESS, "删除成功!");
		return new ModelAndView("redirect:" + redirectPage);
	}
	
	/**
	 * 导出工作轨迹(信息分享，客户，项目，任务，问答，投票)
	 * @param weekreport
	 * @return
	 */
	@RequestMapping(value="/exportWorkTrace")
	public ModelAndView exportWorkTrace(SystemLog systemLog,String[] modTypes) {
		ModelAndView view = new ModelAndView("/systemLog/exportWorkTrace");
		UserInfo userInfo = this.getSessionUser();
		//查询的开始时间
		String startDate = systemLog.getStartDate();
		//查询的截止时间
		String endDate = systemLog.getEndDate();
		//保留数据
		String preEndDate = systemLog.getEndDate();
		if(null!=endDate && !"".equals(endDate)){
			if(null==startDate || "".equals(startDate)){
				startDate = DateTimeUtil.addDate(endDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -7);
			}
		}else{
			//默认为今天
			preEndDate =  DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			endDate = preEndDate;
			if(null==startDate || "".equals(startDate)){
				startDate = DateTimeUtil.addDate(endDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -7);
			}
		}
		systemLog.setStartDate(startDate);
		systemLog.setEndDate(endDate);

		//模块数组化
		List<String> modList = null;
		if(null!=modTypes && modTypes.length>0){
			Arrays.sort(modTypes);
			modList = Arrays.asList(modTypes);
		}
		//查询工作轨迹
		List<WorkTrace> listMsgShare = systemLogService.listWorkTrace(userInfo,systemLog,modList);
		//设置查询的时间
		systemLog.setEndDate(preEndDate);
		
		view.addObject("list", listMsgShare);
		view.addObject("systemLog", systemLog);
		view.addObject("modList", modList);
		return view;
	}	
}
