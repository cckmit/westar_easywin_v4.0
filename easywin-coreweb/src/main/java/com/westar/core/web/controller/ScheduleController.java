package com.westar.core.web.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.ScheLog;
import com.westar.base.model.ScheTalk;
import com.westar.base.model.ScheUser;
import com.westar.base.model.Schedule;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.StringUtil;
import com.westar.core.service.ScheduleService;
import com.westar.core.service.TaskService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;

@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController{

	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	/**
	 * 跳转日历页面
	 * @return
	 */
	@RequestMapping("/listSchedule")
	public ModelAndView listSchedule(Schedule schedule){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/schedule/listSchedule");
		view.addObject("userInfo", userInfo);
		//取得常用人员列表
		List<UserInfo> listOwners = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		String usedUser = "";
		if(null!=listOwners && listOwners.size()>0){
			usedUser +="<hr style='margin: 8px 0px' />"; 
			for (UserInfo owner : listOwners) {
				usedUser +=" <li><a href='javascript:void(0);'  onclick=\\\"userOneForUserIdCallBack("+owner.getId()+",\\\'userId\\\',\\\'"+owner.getUserName()+"\\\',\\\'userName\\\')\\\">"+owner.getUserName()+"</a></li>"; 
			}
		}
		view.addObject("usedUser",usedUser);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_SCHEDULE);
		return view;
		
	}
	/**
	 * 查询页面展示的日程
	 * @param startDate 页面开始时间
	 * @param endDate 页面结束时间
	 * @param busType 业务类型
	 * @param userId 被查看人员主键
	 * @param subType 是否指定有下级
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listEvents")
	public Map<String,Object> listEvents(String startDate,String endDate,String busType,
			Integer userId,String subType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登录");
			return map;
		}
		 //选择的是下属的日程
	    if(userInfo.getCountSub()<=0 && null!=subType && "1".equals(subType)){
	    	subType = null;
	    }
	    //数组转集合
	    List<String> busTypeList = null;
	    if(!StringUtil.isBlank(busType)){
	    	String[] busTypes = busType.split(",");
			if(null!=busTypes && busTypes.length>0){
				busTypeList = Arrays.asList(busTypes);
			}
	    }
	    
	    //查询日程事件
	    List<Schedule> list = scheduleService.listEvents(userInfo.getComId(),userId,
	    		startDate,endDate,busTypeList,subType,userInfo);
	    map.put("status", "y");
	    map.put("list",list);
		return map;
		
	}
	/**
	 * 添加日程界面
	 * @param schedule 日程起止时间
	 * @param formatEl 0日期 1 日期加时间
	 * @return
	 */
	@RequestMapping("/addSchedulePage")
	public ModelAndView addSchedulePage(Schedule schedule,Integer formatEl){
		ModelAndView mav = new ModelAndView("/schedule/addSchedule");
		mav.addObject("schedule", schedule);
		mav.addObject("formatEl", formatEl);
		
		//设置默认截止时间
		if(formatEl==0){
			mav.addObject("repEndDate", schedule.getScheEndDate()+" 18:00");
		}else{
			mav.addObject("repEndDate", schedule.getScheEndDate());
		}
		String scheStartDate = schedule.getScheStartDate();
		scheStartDate = scheStartDate.substring(0,10);
		Calendar c = Calendar.getInstance();
		c.setTime(DateTimeUtil.parseDate(scheStartDate, DateTimeUtil.yyyy_MM_dd));
		mav.addObject("week", DateTimeUtil.getDay(c));
		return mav;
	}
	/**
	 * 添加日程
	 * @param schedule
	 * @param scheUserIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addSchedule")
	public Map<String,Object> addSchedule(Schedule schedule,Integer[] scheUserIds,String viewStart,String viewEnd ){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登录");
			return map;
		}
		//设置企业号
		schedule.setComId(userInfo.getComId());
		//设置创建人员
		schedule.setUserId(userInfo.getId());
		//添加日程后返回主键
		Integer id = scheduleService.addSchedule(userInfo,schedule,scheUserIds);
		map.put("status", "y");
		//设置日程主键
		schedule.setId(id);
		//设置创建人姓名
		schedule.setUserName(userInfo.getUserName());
		List<Schedule> scheduleList =new ArrayList<Schedule>();
		if(schedule.getIsRepeat().equals(1)//需要重复
				&& StringUtils.isNotEmpty(viewStart)//有开始时间
				&& StringUtils.isNotEmpty(viewEnd)){//有结束时间
			scheduleList = CommonUtil.formatSchedule(schedule,viewStart,viewEnd);
		}else{
			scheduleList.add(schedule);
		}
		map.put("scheduleList", scheduleList);
		map.put("id", id);
		return map;
	}
	/**
	 * 修改日程界面
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateSchedulePage")
	public ModelAndView updateSchedulePage(Integer id){
		ModelAndView mav = new ModelAndView();
		UserInfo userInfo = this.getSessionUser();
		//通过取得日程详情
		Schedule schedule = scheduleService.getScheduleById(id);
		if(schedule.getUserId().equals(userInfo.getId())){//是同一个人
			String content = schedule.getContent();
			if(StringUtils.isNotEmpty(content)){
				//去除html标签
				schedule.setContent(schedule.getContent().replaceAll("</?[^>]+>", ""));
			}
			mav.setViewName("/schedule/updateSchedule");
		}else{//不是同一个人
			mav.setViewName("/schedule/viewSchedule");
		}
		mav.addObject("schedule", schedule);
		
		//设置默认截止时间
		if(schedule.getScheEndDate().indexOf(":")>0){
			mav.addObject("repEndDate", schedule.getScheEndDate());
		}else{
			mav.addObject("repEndDate", schedule.getScheEndDate()+" 18:00");
		}
		//日程的参与人
		List<ScheUser> listUser = scheduleService.listScheUser(id,userInfo.getComId());
		mav.addObject("listUser", listUser);
		
		String scheStartDate = schedule.getScheStartDate();
		scheStartDate = scheStartDate.substring(0,10);
		Calendar c = Calendar.getInstance();
		c.setTime(DateTimeUtil.parseDate(scheStartDate, DateTimeUtil.yyyy_MM_dd));
		mav.addObject("week", DateTimeUtil.getDay(c));
		
		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_SCHEDULE,id);
		//添加查看记录
		viewRecordService.addViewRecord(userInfo,viewRecord);
		//查看分享信息，删除提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_SCHEDULE,0);
		return mav;
	}
	
	/**
	 * 修改日程
	 * @param schedule
	 * @param scheUserIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSchedule")
	public Map<String,Object> updateSchedule(Schedule schedule,Integer[] scheUserIds,String viewStart,String viewEnd){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登录");
			return map;
		}
		//修改日程
		schedule = scheduleService.updateSchedule(userInfo,schedule,scheUserIds);
		schedule.setUserName(userInfo.getUserName());
		map.put("status", "y");
		map.put("id", schedule.getId());
		
		List<Schedule> scheduleList =new ArrayList<Schedule>();
		if(schedule.getIsRepeat().equals(1)){//需要重复
			scheduleList = CommonUtil.formatSchedule(schedule,viewStart,viewEnd);
		}else{
			scheduleList.add(schedule);
		}
		map.put("scheduleList", scheduleList);
		//设置创建人姓名
		return map;
	}
	/**
	 * 修改日程
	 * @param id,
	 * @param day
	 * @param minu
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateScheduleByDrag")
	public Map<String,Object> updateScheduleByDrag(Integer id,Integer day,Integer minu){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登录");
			return map;
		}
		//修改日程
		scheduleService.updateScheduleByDrag(userInfo,id,day,minu);
		map.put("status", "y");
		//设置创建人姓名
		return map;
	}
	/**
	 * 修改日程
	 * @param id
	 * @param day
	 * @param minu
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateScheduleByResize")
	public Map<String,Object> updateScheduleByResize(Integer id,Integer day,Integer minu){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登录");
			return map;
		}
		//修改日程
		scheduleService.updateScheduleByResize(userInfo,id,day,minu);
		
		map.put("status", "y");
		//设置创建人姓名
		return map;
	}
	/**
	 * 删除日程
	 * @param id 日程主键
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/deleteSchedule")
	public Map<String,Object> deleteSchedule(Integer id) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登录");
			return map;
		}
		//删除日程
		scheduleService.deleteSchedule(id,userInfo);
		map.put("status", "y");
		return map;
		
	}
	/**
	 * 查询日程留言
	 * @param scheduleId
	 * @return
	 */
	@RequestMapping("/listPagedScheTalks")
	public ModelAndView listPagedScheTalks(Integer scheduleId) {
		UserInfo userInfo = this.getSessionUser();
		//附件评论
		List<ScheTalk> scheTalks = scheduleService.listPagedScheTalks(userInfo.getComId(),scheduleId);
		//页面跳转
		ModelAndView mav = new ModelAndView("/schedule/listScheTalks");
		mav.addObject("scheTalks", scheTalks);
		mav.addObject("scheduleId", scheduleId);
		mav.addObject("sessionUser", userInfo);
		return mav;
	}
	/**
	 * 日程讨论回复
	 * @param scheTalk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/addScheTalk")
	public Map<String, Object> addScheTalk(ScheTalk scheTalk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		scheTalk.setComId(sessionUser.getComId());
		scheTalk.setUserId(sessionUser.getId());
		Integer id = scheduleService.addScheTalk(scheTalk,sessionUser);
		scheduleService.addScheLog(sessionUser.getComId(),scheTalk.getScheduleId(), sessionUser.getId(), sessionUser.getUserName(), "添加日程留言");
		map.put("status", "y");
		map.put("id", id);
		//用于返回页面拼接代码
		ScheTalk scheTalk4Page = scheduleService.getScheTalk(id, sessionUser.getComId());
		//用于返回页面拼接代码
		map.put("scheTalk", scheTalk4Page);
		map.put("sessionUser", this.getSessionUser());
		return map;
	}
	/**
	 * 删除讨论回复
	 * @param id 留言主键
	 * @param delChildNode 是否删除子节点
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delScheTalk")
	public Map<String, Object> delScheTalk(Integer id,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆！");
		}else{
			//要删除的回复所有子节点和自己
			scheduleService.delScheTalk(id,delChildNode,sessionUser);
			map.put("status", "y");
		}
		return map;
	}
	/**
	 * 查看日程日志
	 * @param scheduleId
	 * @return
	 */
	@RequestMapping("/listScheLogs")
	public ModelAndView taskLogPage(Integer scheduleId){
		ModelAndView view = new ModelAndView("/schedule/listScheLogs");
		UserInfo userInfo = this.getSessionUser();
		
		List<ScheLog> listScheLogs = scheduleService.listPagedScheLog(scheduleId, userInfo.getComId());
		view.addObject("listScheLogs",listScheLogs);
		return view;
	}
	
	
	/**
	 * 日程转任务
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateToTask")
	public Map<String,Object>  updateToTask(Integer id) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		if(userInfo == null) {
			map.put("status", "f");
			map.put("info", "服务已断开，请重新登陆！");
		}else {
			Boolean state =  scheduleService.updateToTask(id,userInfo);
			if(state) {
				map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			}else {
				map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			}
		}
		return map;
	}

	/**
	 * 任务转日程
	 * @param taskId 任务主键
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @author LiuXiaoLin
	 * @date 2018/6/20 0020 13:38
	 */
	@ResponseBody
	@RequestMapping(value="/taskConversionSchedule",method=RequestMethod.POST)
	public Map<String,Object> taskConversionSchedule(Integer taskId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();

		Integer scheduleId = scheduleService.taskConversionSchedule(taskId,sessionUser);

		if(null == scheduleId || scheduleId <= 0){
			map.put("status", "f");
		}else{
			map.put("scheduleId",scheduleId);
			map.put("status", "y");
		}

		return map;
	}
}