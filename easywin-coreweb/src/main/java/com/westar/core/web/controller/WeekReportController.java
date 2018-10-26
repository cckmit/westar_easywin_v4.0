package com.westar.core.web.controller;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.Organic;
import com.westar.base.model.SubTimeSet;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.model.WeekRepLog;
import com.westar.base.model.WeekRepModContent;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.model.WeekRepUpfiles;
import com.westar.base.model.WeekReport;
import com.westar.base.model.WeekReportMod;
import com.westar.base.model.WeekReportQ;
import com.westar.base.model.WeekReportVal;
import com.westar.base.model.WeekViewer;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.WeekReportDao;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.ModOptConfService;
import com.westar.core.service.OrganicService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.service.WeekReportService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;
/**
 * 周报中心
 * @author zzq
 *
 */
@Controller
@RequestMapping("/weekReport")
public class WeekReportController extends BaseController{

	@Autowired
	WeekReportService weekReportService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ModOptConfService modOptConfService;
	
	@Autowired
	OrganicService organicService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	@Autowired
	ViewRecordService viewRecordService;

	@Autowired
	WeekReportDao weekReportDao;


	/**
	 * 周报一览表
	 * @param weekReport
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/listPagedWeekRep")
	public ModelAndView listPagedWeekRep(HttpServletRequest request,WeekReportPojo weekReport) throws ParseException{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		
		UserInfo userInfo = this.getSessionUser();
		weekReport.setComId(userInfo.getComId());
		ModelAndView view = new ModelAndView("/weekReport/weekRepCenter");
		
		Integer  power = 0;
	    //汇报人类别
	    String ownerType = weekReport.getWeekerType();
	    //若是没有下属，则没有负责人类别一说
	    if(userInfo.getCountSub()<=0 && null!=ownerType && "1".equals(ownerType)){
	    	weekReport.setWeekerType(null);
	    }
	    //有下属
	    if(userInfo.getCountSub()>0){
	    	power++;
	    }
	  //验证当前登录人是否是督察人员
	  	boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
		if(isForceIn){
			power++;
		}
		view.addObject("userInfo",userInfo);
		
		//是否有管理权限
		view.addObject("power",power);
		//当期周数
		Integer nowWeekNum = DateTimeUtil.getWeekOfYear(DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_),DateTimeUtil.c_yyyy_MM_dd_);
		view.addObject("nowWeekNum",nowWeekNum);
		//当前所在的年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		view.addObject("nowYear",nowYear);
		
		String nowMonth = DateTimeUtil.getNowDateStr(DateTimeUtil.MM);
		view.addObject("nowMonth",nowMonth);
		
		view.addObject("weekReport",weekReport);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_WEEK);
		return view;
	}
	
	
	/**
	 * 周报查看权限验证
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authorCheck")
	public WeekReport authorCheck(Integer weekReportId){
		UserInfo userInfo = this.getSessionUser();
		WeekReport weekReport = weekReportService.getWeekById(weekReportId);
		if(null == userInfo){
			if(null == weekReport){
				weekReport = new WeekReport();
			}
			weekReport.setSucc(false);
			weekReport.setPromptMsg(CommonConstant.OFF_LINE_INFO);
			return weekReport;
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
		//有查看权限或是监督人员
		if(weekReportService.authorCheck(userInfo.getComId(),userInfo.getId(),weekReport.getReporterId(),weekReportId)|| isForceIn){
			weekReport.setSucc(true);
		}else{
			//查看验证，删除消息提醒
			todayWorksService.updateTodoWorkRead(weekReportId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK,0);
			
			weekReport.setSucc(false);
			weekReport.setPromptMsg("抱歉，你没有查看权限");
		}
		return weekReport;
	}
	/**
	 * 周报填写跳转页面 前一周还是后一周的
	 * @param flag
	 * @param preChooseDate
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/chooseWeekNumAdd")
	public ModelAndView chooseWeekNumAdd(HttpServletRequest request,String flag,String preChooseDate,String redirectPage) throws ParseException{
		String chooseDate = preChooseDate;
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		//选中时间所在周的第一天
		String firstDayOfWeek = DateTimeUtil.getFirstDayOfWeek(chooseDate, DateTimeUtil.c_yyyy_MM_dd_);
		if(nowDate.equals(firstDayOfWeek)){//当前时间是所在周的第一天则减一天
			chooseDate = DateTimeUtil.addDate(chooseDate, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DATE, -1);
		}
		if("0".equals(flag)){//填写前一周的数据
			chooseDate = DateTimeUtil.addDate(chooseDate, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DATE, -7);
		}else if("1".equals(flag)){//填写后一周的数据
			chooseDate = DateTimeUtil.addDate(chooseDate, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DATE, 7);
		}
		return this.addWeekRepPage(request,chooseDate,redirectPage);
	}
	
	/**
	 * 周报填写跳转页面
	 * @param chooseDate
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unused")
	@RequestMapping("/addWeekRepPage")
	public ModelAndView addWeekRepPage(HttpServletRequest request,String chooseDate,String redirectPage) throws ParseException{
		//选中的时间
		String tempNowDate = chooseDate;
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		//当前周数
		Integer nowWeekNum = DateTimeUtil.getWeekOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
		//没有选择时间，则判断是否在星期四之后
		if(null==tempNowDate || "".equals(tempNowDate)){
			Calendar cal = Calendar.getInstance();
			cal.setTime(DateTimeUtil.parseDate(nowDate, DateTimeUtil.c_yyyy_MM_dd_));
			int dayOfWeek = DateTimeUtil.getDay(cal);
			if(dayOfWeek<6 && dayOfWeek>1){//不是星期四之后，则减一周，上周周报
				nowDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
			}
			//选择的时间为整理后的当前时间
			tempNowDate = nowDate;
			nowWeekNum = DateTimeUtil.getWeekOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
		}
		
		//日期所在周数
		Integer weekNum = DateTimeUtil.getWeekOfYear(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
		
		UserInfo userInfo = this.getSessionUser();
		//所选日期所在周的第一天
		String weekS = DateTimeUtil.getFirstDayOfWeek(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
		//所选日期所在周的最后天
		String weekE = DateTimeUtil.getLastDayOfWeek(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
		
		String weeksYear = weekS.substring(0, 4);
		String weekEYear = weekE.substring(0, 4);
		String weekYear = weeksYear;
		if(!weeksYear.equals(weekEYear)){
			weekYear = weekEYear;
		}
		
		//取得所选日期所写周报
		WeekReport weekReport = weekReportService.initWeekReport(weekNum,userInfo,weekYear,weekS,weekE);
		
		ModelAndView view = new ModelAndView("/weekReport/addWeekReport");
		
		ModuleOperateConfig config = modOptConfService.getModuleOperateConfig(userInfo.getComId(), ConstantInterface.TYPE_WEEK, "update");
		if(null!=config){
			view.addObject("editWeek", "no");
		}
		
		if(null!=weekReport &&weekReport.getCountVal()>0 && 
				"0".equals(weekReport.getState())){//周报已经发布了
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK, weekReport.getId());
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}
			
		}
		
		view.addObject("sessionUser",userInfo);
		view.addObject("weekReport",weekReport);
		
		view.addObject("firstDayOfWeek",weekS);
		view.addObject("lastDayOfWeek",weekE);
		
		String preWeekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
		String preWeekE = DateTimeUtil.addDate(weekE, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
		
		//当前操作人员的入职时间
		String hireDateStr = null;
		if(null != userInfo){//系统已断开连接
			hireDateStr = userInfo.getHireDate();
			if(null!=hireDateStr){//入职时间不为空
				hireDateStr = hireDateStr.substring(0,10);
				
				//入职时间的星期一
				hireDateStr = DateTimeUtil.getFirstDayOfWeek(hireDateStr,DateTimeUtil.yyyy_MM_dd);
				//入职时间的周数的上周星期五
				hireDateStr = DateTimeUtil.addDate(hireDateStr, DateTimeUtil.yyyy_MM_dd,
						Calendar.DAY_OF_YEAR, -2);
				Date hireDate = DateTimeUtil.parseDate(hireDateStr, DateTimeUtil.yyyy_MM_dd);
				
				//当前时间所在周的星期一
				String chooseDateWeekSStr = DateTimeUtil.getFirstDayOfWeek(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
				//当前时间所在周的上周星期五
				chooseDateWeekSStr = DateTimeUtil.addDate(chooseDateWeekSStr, DateTimeUtil.c_yyyy_MM_dd_,
						Calendar.DAY_OF_YEAR, -2);
				Date chooseDateWeekS = DateTimeUtil.parseDate(chooseDateWeekSStr, DateTimeUtil.c_yyyy_MM_dd_);
				
				
				//入职时间在上周五到本周四之间的不需要显示上周的
				if(hireDate.getTime() > chooseDateWeekS.getTime()){
				}else{
					view.addObject("preWeekS",preWeekS);
				}
			}else{
				view.addObject("preWeekS",preWeekS);
			}
		}else{
			view.addObject("preWeekS",preWeekS);
		}
		
		view.addObject("preWeekE",preWeekE);
		
		boolean canNext = this.getNextWeekE(tempNowDate);
		if(canNext){
			String nextWeekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, 7);
			String nextWeekE = DateTimeUtil.addDate(weekE, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, 7);
			view.addObject("nextWeekS",nextWeekS);
			view.addObject("nextWeekE",nextWeekE);
		}
		
		//选中的时间年
		String year = weekYear;
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		//当前时间所在年
		view.addObject("nowYear",nowYear);
		//当前时间所在月
		view.addObject("nowWeekMonth",nowDate.substring(nowDate.indexOf("年")+1,nowDate.indexOf("月")));
		//当前时间所在周数
		view.addObject("nowWeekNum",nowWeekNum);
		
		//周报所在年
		view.addObject("year",year);
		//周报所在月
		view.addObject("pageWeekMonth", weekE.substring(weekE.indexOf("年")+1, weekE.indexOf("月")));
		//日期所在周数
		view.addObject("weekNum",weekNum);
		view.addObject("chooseDate",tempNowDate);
		
		view.addObject("userInfo",userInfo);
		
		
		return view;
	}
	
	/**
	 * 取得周报所选时间的下一周最后一天
	 * @param chooseDate
	 * @return
	 * @throws ParseException 
	 */
	private boolean getNextWeekE(String chooseDate) throws ParseException {
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateTimeUtil.parseDate(nowDate, DateTimeUtil.c_yyyy_MM_dd_));
		int dayOfWeek = DateTimeUtil.getDay(cal);
		if(dayOfWeek<6 && dayOfWeek>1){//不是星期四之后，则减一周，上周周报
			nowDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
		}
		//当前时间的周数最后一天
		String nowDateNextEStr = DateTimeUtil.getLastDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);
		nowDateNextEStr = DateTimeUtil.addDate(nowDateNextEStr, DateTimeUtil.c_yyyy_MM_dd_,  Calendar.DAY_OF_YEAR, 7);
		Date nowDateNextE = DateTimeUtil.parseDate(nowDateNextEStr, DateTimeUtil.c_yyyy_MM_dd_);
		
		String choseDateNextWeekE = DateTimeUtil.getLastDayOfWeek(chooseDate, DateTimeUtil.c_yyyy_MM_dd_);
		Date chooseDateNextE = DateTimeUtil.parseDate(choseDateNextWeekE, DateTimeUtil.c_yyyy_MM_dd_);
		if(nowDateNextEStr.equals(nowDateNextE)){
			return true;
		}else if(nowDateNextE.after(chooseDateNextE)){
			return true;
			
		}
		
		return false;
	}
	/***************************周报汇报范围******************************/
	/**
	 * 周报查看范围设置界面
	 * @return
	 */
	@RequestMapping(value="/weekViewSetPage")
	public ModelAndView weekViewSetPage(){
		ModelAndView mav = new ModelAndView("/weekReport/weekViewSet");
		UserInfo sessionUser = this.getSessionUser();
		//查询周报查看人员
		List<WeekViewer> listWeekViewer = weekReportService.listWeekViewer(sessionUser);
		mav.addObject("listWeekViewer", listWeekViewer);
		mav.addObject("userInfo", sessionUser);
		
		//获取个人直属上级集合
		List<ImmediateSuper> listImmediateSuper = userInfoService.listImmediateSuper(sessionUser);
		//生成任务参与人JSon字符串
		StringBuffer leaderJson = new StringBuffer("[");
		if(null!=listImmediateSuper && !listImmediateSuper.isEmpty()){
			for(ImmediateSuper vo:listImmediateSuper){
				leaderJson.append("{'userID':'"+vo.getLeader()+"','userName':'"+vo.getLeaderName()+"','gender':'"+vo.getGender()+"','uuid':'"+vo.getUuid()+"','fileName':'"+vo.getFileName()+"'},");	
			}
			leaderJson = new StringBuffer(leaderJson.substring(0,leaderJson.lastIndexOf(",")));
		}
		leaderJson.append("]");
		mav.addObject("leaderJson",leaderJson);
		return mav;
	}
	/**
	 * 设置周报查看范围
	 * @param weekViewerStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateWeekViewers",method = RequestMethod.POST)
	public Map<String, Object> updateWeekViewers(String weekViewerStr){
		UserInfo userInfo = this.getSessionUser();
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<WeekViewer> weekViewerList = JSONArray.parseArray(weekViewerStr, WeekViewer.class);
		String logContent = "";
		if(null!=weekViewerList && !weekViewerList.isEmpty()){
			for (WeekViewer weekViewer : weekViewerList) {
				weekViewer.setUserId(userInfo.getId());
				weekViewer.setComId(userInfo.getComId());
				logContent = logContent+","+ weekViewer.getViewerName();
			}
		}
		if(!"".equals(logContent)){
			logContent = logContent.substring(1,logContent.length());
		}
		
		weekReportService.updateWeekViewers(weekViewerList,userInfo,logContent);
		
		map.put("status", "y");
		//查询周报查看人员
		List<WeekViewer> listWeekViewer = weekReportService.listWeekViewer(userInfo);
		map.put("listWeekViewer", listWeekViewer);
		return map;
	}
	/**
	 * 删除周报查看人员
	 * @param weekViewer 周报查看人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delWeekViewer",method = RequestMethod.POST)
	public Map<String, Object> delWeekViewer(WeekViewer weekViewer){
		//当前操作人员
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){//服务器session用户数据丢失
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//删除周报查看人员
		weekReportService.delWeekViewer(weekViewer,userInfo);
		map.put("status", "y");
		return map;
	}
	/***************************周报汇报范围******************************/
	
	/**
	 * 填写周报
	 * @param request
	 * @param weekReport
	 * @param idType
	 * @param scopeTypeSel
	 * @return
	 * @throws org.apache.lucene.queryparser.classic.ParseException 
	 * @throws Exception 
	 */
	@RequestMapping(value="/addWeekReport",method = RequestMethod.POST)
	public ModelAndView addWeekReport(HttpServletRequest request,WeekReport weekReport,String idType,
			String scopeTypeSel,WeekReportPojo weekReportParam) throws Exception{
		
		String tempDate = weekReport.getChooseDate();
		if(null==tempDate || "".equals(tempDate)){
			tempDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		} 
		UserInfo userInfo = this.getSessionUser();
		//周报条目
		 List<WeekReportQ> weekReports = weekReportService.listWeekReportQ(weekReport.getId(),userInfo.getComId(),userInfo.getId());
		 List<WeekReportVal> weekReportVals = new ArrayList<WeekReportVal>();
		 for (WeekReportQ weekReportQ : weekReports) {
			 
			String reportName = weekReportQ.getReportName();
			
			WeekReportVal weekReportVal = new WeekReportVal();
			//企业
			weekReportVal.setComId(userInfo.getComId());
			//周报内容要求主键字符串
			String weekReportIdStr = reportName.substring(reportName.lastIndexOf("_")+1, reportName.length());
			weekReportVal.setQuestionId(Integer.parseInt(weekReportIdStr));
			//周报主键
			weekReportVal.setWeekReportId(weekReport.getId());
			//周报值
			String val = request.getParameter(reportName);
			weekReportVal.setReportValue(val);
			//没有填值则不算，空格不算
			if(null!=val && !"".equals(val) && !"".equals(val.trim()) ){
				weekReportVals.add(weekReportVal);
			}
		}
		weekReport.setWeekReportVals(weekReportVals);
		
		//设置成了分享
		if(null!=weekReport.getIsShare()){
			weekReport.setIsShare(1);
		}else{//设置成不分享
			weekReport.setIsShare(0);
		}
		//填写周报
		weekReportService.addWeekReport(weekReport,userInfo);
		String sid = this.getSid();
		ModelAndView view = new ModelAndView();
		
		
		
		if(null!=weekReportParam &&null!=weekReportParam.getViewType() && weekReportParam.getViewType()==1){
			view.setViewName("redirect:/weekReport/viewWeekReport");
			view.addObject("sid", sid);
			view.addObject("id", weekReport.getId());
			view.addObject("weekerId", weekReportParam.getWeekerId());
			view.addObject("depId", weekReportParam.getDepId());
			view.addObject("depName", weekReportParam.getDepName());
			view.addObject("startDate", weekReportParam.getStartDate());
			view.addObject("endDate", weekReportParam.getEndDate());
			view.addObject("weekName", weekReportParam.getWeekName());
			
		}else{
			view.setViewName("refreshParent");
			//view.setViewName("redirect:/weekReport/addWeekRepPage");
			view.addObject("chooseDate", tempDate);
		}
		
		String content="发布";
		if("1".equals(weekReport.getState())){
			content = "保存";
		}
		this.setNotification(Notification.SUCCESS, content+"成功");
		
		view.addObject("weekReportParam", weekReportParam);
		return view;
	}
	/**
	 * 查看周报
	 * @param request
	 * @param id 周报主键
	 * @param preId 前一个周报主键
	 * @param nextId 后一个周报主键
	 * @param weekReportParam 周报查询条件
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/viewWeekReport")
	public ModelAndView viewWeekReport(HttpServletRequest request,Integer id,Integer preId,Integer nextId,
			WeekReportPojo weekReportParam) throws ParseException{
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView();
		
		weekReportParam.setViewType(1);
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
	    //汇报人类别
	    String ownerType = weekReportParam.getWeekerType();
	    //若是没有下属，则没有负责人类别一说
	    if(userInfo.getCountSub()<=0 && null!=ownerType && "1".equals(ownerType)){
	    	weekReportParam.setWeekerType(null);
	    }
	    
		//取得所选周报
		WeekReport weekReportT = weekReportService.getWeekReportForView(id,userInfo,weekReportParam,isForceIn);
		if(null==weekReportT){//周报不存在
			//跳转
			view.setViewName("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有查看权限");
			
			//查看周报，删除消息提醒
			todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK,0);
			
			return view;
		}
		if(null!=preId && null!=nextId){//若是选中的时间对应的周报主键,恰好是当前的前一个或是后一个
			if(!id.equals(preId)&& !id.equals(nextId)){
				weekReportT.setPreId(preId);
				weekReportT.setNextId(nextId);
			}
		}
		
		//周报查看权限验证(有查看权限或是监督人员)
		if((weekReportService.authorCheck(userInfo.getComId(),userInfo.getId(),weekReportT.getReporterId(),weekReportT.getId()) || isForceIn)){
			
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}
			if(weekReportT.getReporterId().equals(userInfo.getId())){//若操作的是自己的周报
				WeekReport weekReport = weekReportService.initWeekReport(weekReportT.getWeekNum(), userInfo, weekReportT.getYear().toString(),
						weekReportT.getWeekS(), weekReportT.getWeekE());
				weekReport.setPreId(weekReportT.getPreId());
				weekReport.setNextId(weekReportT.getNextId());
				weekReport.setUserName(weekReportT.getUserName());
				view.setViewName("/weekReport/addWeekReport");
				view.addObject("weekReport", weekReport);
				
				ModuleOperateConfig config = modOptConfService.getModuleOperateConfig(userInfo.getComId(), ConstantInterface.TYPE_WEEK, "update");
				if(null!=config){
					view.addObject("editWeek", "no");
				}
			}else{
				// 周报附件
				weekReportT.setWeekReportFiles(weekReportDao.listWeekReportFiles(weekReportT.getId(), userInfo
								.getComId()));
				view.setViewName("/weekReport/viewWeekReport");
				view.addObject("weekReport", weekReportT);
			}
			
			//所选日期所在周的最后一天
			String weekS = weekReportT.getWeekS();
			String weekE = weekReportT.getWeekE();
			view.addObject("firstDayOfWeek",weekS);
			view.addObject("lastDayOfWeek",weekE);
			
			String preWeekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
			String preWeekE = DateTimeUtil.addDate(weekE, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
			view.addObject("preWeekS",preWeekS);
			view.addObject("preWeekE",preWeekE);
			
			boolean canNext = this.getNextWeekE(weekS);
			if(canNext){
				String nextWeekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, 7);
				String nextWeekE = DateTimeUtil.addDate(weekE, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, 7);
				view.addObject("nextWeekS",nextWeekS);
				view.addObject("nextWeekE",nextWeekE);
			}
			
			view.addObject("userInfo", userInfo);
			
			//所选日期所在年
			Integer pageWeekYear = weekReportT.getYear();
			//所在年
			view.addObject("pageWeekYear",pageWeekYear);
			//所在月
			view.addObject("pageWeekMonth", weekE.substring(weekE.indexOf("年")+1, weekE.indexOf("月")));
			
			view.addObject("weekReportParam", weekReportParam);
			
			//当前时间
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
			//当前周数
			Integer nowWeekNum = DateTimeUtil.getWeekOfYear(nowDate,DateTimeUtil.c_yyyy_MM_dd_);
			//所选时间所在年
			view.addObject("nowWeekYear", nowDate.substring(0,4));
			//所选时间所在月
			view.addObject("nowWeekMonth", nowDate.substring(nowDate.indexOf("年")+1, nowDate.indexOf("月")));
			//所选时间所在年周数
			view.addObject("nowWeekNum", nowWeekNum);
			
			//浏览的人员
			List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo,ConstantInterface.TYPE_WEEK,id);
			view.addObject("listViewRecord", listViewRecord);
			
		}else{//没有查看权限
			//跳转
			view.setViewName("/refreshParent");
		}
		//查看周报，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK,0);
		return view;
	}
	
	/***************************以下是周报模板设置************************************************/
	/**
	 * 设置周报模板跳转页面
	 * @return
	 */
	@RequestMapping("/addWeekRepModPage")
	public ModelAndView addWeekRepModPage(){
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/weekReport/weekRepCenter");
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			view = new ModelAndView("redirect:/weekReport/listPagedWeekRep?sid="+this.getSid());
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return view;
		}
		view.addObject("userInfo",userInfo);
		//周报模板
		WeekReportMod weekReportMod = weekReportService.initWeekReportMod(userInfo);
		view.addObject("weekReportMod", weekReportMod);
		Integer power =0;
	    //有下属
	    if(userInfo.getCountSub()>0){
	    	power++;
	    }
	  //验证当前登录人是否是督察人员
	  	boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
		if(isForceIn){
			power++;
		}
		view.addObject("power", power);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_WEEK);
		return view;
	}
	
	/**
	 * 修改模板是否显示下周计划
	 * @param weekReportMod
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateWeekPlane",method = RequestMethod.POST)
	public Map<String, String> updateWeekPlane(WeekReportMod weekReportMod){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		//修改模板是否显示下周计划
		weekReportService.updateWeekReportMod(weekReportMod);
		map.put("status", "y");
		String content = "设置周报填写下周计划";
		if("0".equals(weekReportMod.getHasPlan())){
			content = "设置周报不填写下周计划";
		}
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), content, 
				ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}
	/**
	 * 修改模板是否显示该问题
	 * @param weekRepModContent
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/hideContent",method = RequestMethod.POST)
	public Map<String, String> hideContent(WeekRepModContent weekRepModContent){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		//修改模板是否显示该问题
		weekRepModContent =  weekReportService.updateWeekRepModContent(weekRepModContent);
		
		map.put("status", "y");
		String content = "设置周报不显示该条目("+weekRepModContent.getModContent()+")";
		if("0".equals(weekRepModContent.getHideState())){
			content = "设置周报显示该条目("+weekRepModContent.getModContent()+")";
		}
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), content,
				ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}
	/**
	 * 修改模板该条目是否必填
	 * @param weekRepModContent
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateRequireContent",method = RequestMethod.POST)
	public Map<String, String> updateRequireContent(WeekRepModContent weekRepModContent){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		//修改模板是否显示该问题
		weekRepModContent =  weekReportService.updateWeekRepModContent(weekRepModContent);
		
		map.put("status", "y");
		String content = "设置周报该条目("+weekRepModContent.getModContent()+")必填";
		if("0".equals(weekRepModContent.getHideState())){
			content = "设置周报显示该条目("+weekRepModContent.getModContent()+")非必填";
		}
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), content,
				ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}
	/**
	 * 修改模板
	 * @param weekRepModContent
	 * @param members
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateWeekRepModContent",method = RequestMethod.POST)
	public Map<String, String> updateWeekRepModContent(WeekRepModContent weekRepModContent,String[] members,String[] deps){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		weekRepModContent.setComId(userInfo.getComId());
		//修改模板
		weekRepModContent = weekReportService.updateWeekRepModContent(weekRepModContent,members,deps);
		map.put("status", "y");
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "修改周报模板条目为("+weekRepModContent.getModContent()+")",
				ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}
	/**
	 * 添加模板条目
	 * @param weekRepModContent
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addWeekModContent",method = RequestMethod.POST)
	public Map<String, Object> addWeekModContent(WeekRepModContent weekRepModContent,String[] members,String[] deps){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		weekRepModContent.setCereaterId(userInfo.getId());
		weekRepModContent.setComId(userInfo.getComId());
		//默认是非系统级别的模板
		weekRepModContent.setSysState(0);
		//添加模板条目
		Integer id = weekReportService.addWeekModContent(weekRepModContent,members,deps);
		map.put("status", "y");
		map.put("id", id);
		
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加周报模板条目("+weekRepModContent.getModContent()+")", 
				ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}
	
	/**
	 *  删除模板条目
	 * @param id
	 * @param modId
	 * @param repLev
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delModContent",method = RequestMethod.POST)
	public Map<String, String> delModContent(Integer id,Integer modId, Integer repLev ){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0")){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		//删除模板条目
		WeekRepModContent weekRepModContent  = weekReportService.delModContent(id,modId,repLev,userInfo.getComId());
		
		map.put("status", "y");
		//系统日志
		if(1==repLev){
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除团队级别的条目("+weekRepModContent.getModContent()+")",
					ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		}else if(2==repLev){
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除部门级别的条目("+weekRepModContent.getModContent()+")",
					ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		}else if(3==repLev){
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除成员级别的条目("+weekRepModContent.getModContent()+")", 
					ConstantInterface.TYPE_WEEK, userInfo.getComId(),userInfo.getOptIP());
		}
		return map;
	}
	
	/***************************以上是周报模板设置************************************************/
	/**
	 * 周报日志
	 * @param weekReportId
	 * @return
	 */
	@RequestMapping("/weekRepLogPage")
	public ModelAndView weekRepLogPage(Integer weekReportId,String sid) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/weekReport/weekRepLog");
		//查看周报日志，删除消息提醒
		todayWorksService.updateTodoWorkRead(weekReportId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK,0);
		//周报日志
		List<WeekRepLog> weekRepLogs = weekReportService.listPagedWeekRepVoteLog(userInfo.getComId(),weekReportId,sid);
		mav.addObject("weekRepLogs",weekRepLogs);
		mav.addObject("sessionUser",userInfo);
		return mav;
	}
	/**
	 * 周报附件
	 * @param weekReportId
	 * @return
	 */
	@RequestMapping("/weekRepFilePage")
	public ModelAndView weekRepFile(Integer weekReportId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/weekReport/weekRepFiles");
		//查看周报附件，删除消息提醒
		todayWorksService.updateTodoWorkRead(weekReportId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK,0);
		//周报附件
		List<WeekRepUpfiles> weekRepFiles = weekReportService.listPagedWeekRepFiles(userInfo.getComId(),weekReportId);
		mav.addObject("weekRepFiles",weekRepFiles);
		mav.addObject("userInfo",userInfo);
		mav.addObject("weekReportId",weekReportId);
		return mav;
	}
	/***************************以下是反馈留言************************************************/
	/**
	 * 反馈留言
	 * @param weekReportId
	 * @return
	 */
	@RequestMapping("/weekRepTalkPage")
	public ModelAndView weekRepTalkPage(Integer weekReportId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/weekReport/weekRepTalk");
		//查看反馈留言，删除消息提醒
		todayWorksService.updateTodoWorkRead(weekReportId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK,0);
		//反馈留言
		List<WeekRepTalk> weekTalks = weekReportService.listPagedWeekRepTalk(weekReportId,userInfo.getComId(),"pc");
		mav.addObject("weekTalks",weekTalks);
		mav.addObject("weekReportId",weekReportId);
		mav.addObject("sessionUser",userInfo);
		
		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_WEEK);
		if(null!=listModuleOperateConfig){
			for(ModuleOperateConfig vo:listModuleOperateConfig){
				mav.addObject(vo.getOperateType(),ConstantInterface.MOD_OPT_STATE_YES);
			}
		}
		return mav;
	}
	/**
	 * 反馈留言回复
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/addWeekRepTalk",method = RequestMethod.POST)
	public Map<String, Object> addWeekRepTalk(WeekRepTalk weekRepTalk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		weekRepTalk.setComId(sessionUser.getComId());
		weekRepTalk.setTalker(sessionUser.getId());
		//反馈留言回复
		Integer id = weekReportService.addWeekRepTalk(weekRepTalk,sessionUser);
		//模块日志添加
		if(-1==weekRepTalk.getParentId()){
			weekReportService.addWeekRepLog(sessionUser.getComId(), weekRepTalk.getWeekReportId(), sessionUser.getId(), sessionUser.getUserName(), "参与反馈留言");
		}else{
			weekReportService.addWeekRepLog(sessionUser.getComId(), weekRepTalk.getWeekReportId(), sessionUser.getId(), sessionUser.getUserName(), "回复反馈留言");
		}
		map.put("status", "y");
		map.put("id", id);
		//用于返回页面拼接代码
		WeekRepTalk weekRepTalk4Page = weekReportService.getWeekRepTalk(id, sessionUser.getComId());
		map.put("weekRepTalk", weekRepTalk4Page);
		map.put("sessionUser", this.getSessionUser());
		
		ModuleOperateConfig modOptConfig = modOptConfService.getModuleOperateConfig(sessionUser.getComId(),
				ConstantInterface.TYPE_WEEK, "delete");
		if(null!=modOptConfig){
			map.put("delWeek","no");	
		}
		return map;
	}
	/**
	 * 删除反馈留言回复
	 * @param weekRepTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/delWeekRepTalk",method = RequestMethod.POST)
	public Map<String, Object> delWeekRepTalk(WeekRepTalk weekRepTalk,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		weekRepTalk.setComId(sessionUser.getComId());
		//要删除的回复所有子节点和自己
		List<Integer> childIds = weekReportService.delWeekRepTalk(weekRepTalk,delChildNode,sessionUser);
		map.put("status", "y");
		map.put("childIds", childIds);
		//模块日志添加
		weekReportService.addWeekRepLog(sessionUser.getComId(), weekRepTalk.getWeekReportId(), sessionUser.getId(), sessionUser.getUserName(), "删除反馈留言");
		
		return map;
	}
	
	/***************************以上是反馈留言************************************************/
	/**
	 * 取得时间轴年所需信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/setFormWeekS")
	public Map<String,Object> setFormWeekS(String time,Integer weekNum) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		
		String weekS = time;
		//所选日期所在周的最后天
		String weekE = DateTimeUtil.getLastDayOfWeek(weekS,DateTimeUtil.c_yyyy_MM_dd_);
		//所选日期坐在年
		String weekYear = weekE.substring(0, 4);
		//所选日期坐在年
		String weekSYear = weekS.substring(0, 4);
		if(!weekSYear.equals(weekYear)){//跨年了
			weekS = weekSYear+"年1月1日";
		}
		//所选日期的年周数
		Integer selectWeekNum = DateTimeUtil.getWeekOfYear(weekS, DateTimeUtil.c_yyyy_MM_dd_);
		
		//当前时间
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		//当前时间在周的第一天
		String nowWeekS = DateTimeUtil.getFirstDayOfWeek(nowDateTime,DateTimeUtil.c_yyyy_MM_dd_);
		//当前时间在周的最后天
		String nowWeekE = DateTimeUtil.getLastDayOfWeek(nowDateTime,DateTimeUtil.c_yyyy_MM_dd_);
		//当前时间所在年
		String nowWeekYear = nowWeekE.substring(0, 4);
		if(weekYear.equals(nowWeekYear)){//同一年
			Integer nowWeekNum = DateTimeUtil.getWeekOfYear(nowDateTime, DateTimeUtil.c_yyyy_MM_dd_);
			if(weekNum>=nowWeekNum){//若是比当前所在周的时间大，则取当前时间
				map.put("weeks", nowWeekS);
				map.put("status", "y");
				return map;
			}
		}
		
		
		//所选时间
		if(selectWeekNum.equals(weekNum)){
			map.put("weeks", weekS);
			map.put("status", "y");
			return map;
		}
		
		//所选时间为一年最后一周
		if(weekNum.equals(53)){
			if(selectWeekNum.equals(1)){//同期是第二年的第一周，则减一周
				weekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, -7);
				map.put("weeks", weekS);
				map.put("status", "y");
			}else if(selectWeekNum.equals(52)){//同期是当年的倒数第二或第一周
				map.put("weeks", weekS);
				//添加一周
				weekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, 7);
				selectWeekNum = DateTimeUtil.getWeekOfYear(weekS, DateTimeUtil.c_yyyy_MM_dd_);
				//添加一周后在当年，则返回修改后的日期
				if(selectWeekNum.equals(53)){
					map.put("weeks", weekS);
				}
			}
			map.put("status", "y");
			return map;
		}else if(weekNum.equals(52)){
			if(selectWeekNum.equals(1) || selectWeekNum.equals(53)){//同期是第二年的第一周，则减一周
				weekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, -7);
				map.put("weeks", weekS);
				map.put("status", "y");
			}else if(selectWeekNum.equals(51)){
				weekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, 7);
				map.put("weeks", weekS);
				map.put("status", "y");
			}
			return map;
		}
		if(selectWeekNum>weekNum){//所选日期比较大
			for(int i=0;i<5;i++){
				weekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, -7);
				selectWeekNum = DateTimeUtil.getWeekOfYear(weekS, DateTimeUtil.c_yyyy_MM_dd_);
				if(selectWeekNum.equals(weekNum)){
					break;
				}
			}
		}else if(selectWeekNum<weekNum){
			for(int i=0;i<5;i++){
				weekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, 7);
				selectWeekNum = DateTimeUtil.getWeekOfYear(weekS, DateTimeUtil.c_yyyy_MM_dd_);
				if(selectWeekNum.equals(weekNum)){
					break;
				}
			}
		}
		map.put("weeks", weekS);
		map.put("status", "y");
		
		return map;
		
	}
	/**
	 * 取得时间轴所需信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxGetTimeLime")
	public Map<String,Object> ajaxGetTimeLime(Integer year,Integer month) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		//取得本月的所在年的周数
		List<WeekReport> weekReports = new ArrayList<WeekReport>();
		String firstMonDay = year+"年"+month+"月1日";
		//所选月份第一周第一天的日期
		String weekS = DateTimeUtil.getFirstDayOfWeek(firstMonDay, DateTimeUtil.c_yyyy_MM_dd_);
		//所选月份第一周最后一天的日期
		String weekE = DateTimeUtil.getLastDayOfWeek(firstMonDay, DateTimeUtil.c_yyyy_MM_dd_);
		//所选月份第一天所在年的周数
		Integer nowWeekNum = DateTimeUtil.getWeekOfYear(firstMonDay,DateTimeUtil.c_yyyy_MM_dd_);
		
		//一周最后一天所在月份
		Integer weeEM = Integer.parseInt(weekE.substring(5, 7));
		do{
			WeekReport weekReport = new WeekReport();
			weekReport.setWeekE(weekE);
			weekReport.setWeekS(weekS);
			weekReport.setWeekNum(nowWeekNum);
			
			weekReports.add(weekReport);
			
			weekS = DateTimeUtil.addDate(weekS, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, 7);
			weekE = DateTimeUtil.addDate(weekE, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_YEAR, 7);
			weeEM = Integer.parseInt(weekE.substring(5, 7));
			
			nowWeekNum = nowWeekNum+1;
			
		}while(weeEM.equals(month));
		
		map.put("weeks", weekReports);
		map.put("status", "y");
		
		return map;
		
	}
	/**
	 * 验证周报发布时间时间
	 * @param chooseDateStr 选中的时间，一般为一周第一天
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="/checkWeekNum")
	public Map<String,Object> checkWeekNum(String chooseDateStr) throws ParseException{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//当前操作人员的入职时间
		String hireDateStr = null;
		if(null != sessionUser){//系统已断开连接
			hireDateStr = sessionUser.getHireDate();
		}
		
		//当前时间
		String nowDateStr = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateTimeUtil.parseDate(nowDateStr, DateTimeUtil.c_yyyy_MM_dd_));
		int dayOfWeek = DateTimeUtil.getDay(cal);
		
		//先判断入职时间
		if(null!=hireDateStr){//入职时间不为空
			/**
			 * 1、若是入职时间在当前时间所在同周数，若是星期五之前（不包括星期五），则不需要写上周周报，否则填写本周周报
			 * 2、若是在入职时间小于当前时间所在周，则需要填写周报
			 */
			//当前日期所在周的星期一
			String nowDateWeekSStr = DateTimeUtil.getFirstDayOfWeek(nowDateStr,DateTimeUtil.c_yyyy_MM_dd_);
			Date nowDateWeekS = DateTimeUtil.parseDate(nowDateWeekSStr, DateTimeUtil.c_yyyy_MM_dd_);
			
			//当前时间所在周的星期五
			String nowDateWeekEStr = DateTimeUtil.getLastDayOfWeek(nowDateWeekSStr,DateTimeUtil.c_yyyy_MM_dd_);
			nowDateWeekEStr = DateTimeUtil.addDate(nowDateWeekEStr, DateTimeUtil.c_yyyy_MM_dd_,
					Calendar.DAY_OF_YEAR, -2);
			Date nowDateWeekE = DateTimeUtil.parseDate(nowDateWeekEStr, DateTimeUtil.c_yyyy_MM_dd_);
			
			//入职时间
			hireDateStr = hireDateStr.substring(0,10);
			Date hireDate = DateTimeUtil.parseDate(hireDateStr, DateTimeUtil.yyyy_MM_dd);
			
			
			//入职时间在本周一到五，今天非五六日，需要提示不写周报
			if(hireDate.getTime() >= nowDateWeekS.getTime() 
					&& hireDate.getTime() <nowDateWeekE.getTime()){//入职时间不小于当前时间所在周的一周的星期五，则不用写周报
				
				//选中的时间
				String chooseDateWeekSStr = DateTimeUtil.getFirstDayOfWeek(chooseDateStr,DateTimeUtil.c_yyyy_MM_dd_);
				Date chooseDateWeekS = DateTimeUtil.parseDate(chooseDateWeekSStr, DateTimeUtil.c_yyyy_MM_dd_);
				/**
				 * 1、若是入职时间的所在周的星期一与选中时间的星期一相同,且为星期一到星期四，则提示不能发布本周周报
				 * 2、若是
				 */
				if(nowDateWeekS.getTime() == chooseDateWeekS.getTime() && (
						dayOfWeek<6 && dayOfWeek>1)){
					//日期所在周数
					Integer weekNum = DateTimeUtil.getWeekOfYear(nowDateWeekSStr,DateTimeUtil.c_yyyy_MM_dd_);
					
					//所选日期所在周的星期天
					String weekE = DateTimeUtil.getLastDayOfWeek(nowDateWeekSStr,DateTimeUtil.c_yyyy_MM_dd_);
					
					//构建年份
					String weeksYear = nowDateWeekSStr.substring(0, 4);
					String weekEYear = weekE.substring(0, 4);
					String weekYear = weeksYear;
					if(!weeksYear.equals(weekEYear)){
						weekYear = weekEYear;
					}
					
					map.put("status", "f");
					map.put("info", "当前只能汇报<b>"+weekYear+"年第"+weekNum+"周</b>("+nowDateWeekSStr.substring(5, 11)+"~"+weekE.substring(5, 11)+")之前的周报");
				
					return map;
				}
				if(dayOfWeek<6 && dayOfWeek>1){//星期一到星期四写的上周周报，入职人员不用写
					map.put("status", "f");
					map.put("info", "入职时间前的周报不用写！");
					return map;
				}
			}
		}
		
		//再判断周数
		if(dayOfWeek<6 && dayOfWeek>1){//不是星期四之后，则减一周，上周周报
			nowDateStr = DateTimeUtil.addDate(nowDateStr, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -7);
		}
		
		//当前日期所在周的星期一
		String nowDateWeekSStr = DateTimeUtil.getFirstDayOfWeek(nowDateStr,DateTimeUtil.c_yyyy_MM_dd_);
		Date nowDateWeekS = DateTimeUtil.parseDate(nowDateWeekSStr, DateTimeUtil.c_yyyy_MM_dd_);
		
		//选中的时间
		Date chooseDate = DateTimeUtil.parseDate(chooseDateStr, DateTimeUtil.c_yyyy_MM_dd_);
		
		//当前时间的星期一不小于选中时间（一般是一周的星期一），可以发布
		if(nowDateWeekS.getTime() >= chooseDate.getTime()){
			map.put("status", "y");
		}else{//构建不能发布周报的提示
			//日期所在周数
			Integer weekNum = DateTimeUtil.getWeekOfYear(nowDateWeekSStr,DateTimeUtil.c_yyyy_MM_dd_);
			
			//所选日期所在周的星期天
			String weekE = DateTimeUtil.getLastDayOfWeek(nowDateWeekSStr,DateTimeUtil.c_yyyy_MM_dd_);
			
			//构建年份
			String weeksYear = nowDateWeekSStr.substring(0, 4);
			String weekEYear = weekE.substring(0, 4);
			String weekYear = weeksYear;
			if(!weeksYear.equals(weekEYear)){
				weekYear = weekEYear;
			}
			
			map.put("status", "f");
			map.put("info", "当前只能汇报<b>"+weekYear+"年第"+weekNum+"周</b>("+nowDateWeekSStr.substring(5, 11)+"~"+weekE.substring(5, 11)+")之前的周报");
		}
		return map;
	}
	/**
	 * 添加修改提交设置界面
	 * @return
	 */
	@RequestMapping("/addSubTimeSetPage")
	public ModelAndView addSubTimeSetPage(){
		ModelAndView view = new ModelAndView("/weekReport/subTimeSet");
		UserInfo userInfo = this.getSessionUser();
		
		SubTimeSet subTimeSet = weekReportService.querySubTimeSet(userInfo);
		view.addObject("subTimeSet",subTimeSet);
		view.addObject("userInfo",userInfo);
		return view;
	}
	/**
	 * 修改设置
	 * @param subTimeSet
	 * @return
	 */
	@RequestMapping("/updateSubTimeSet")
	public ModelAndView updateSubTimeSet(SubTimeSet subTimeSet){
		ModelAndView view = new ModelAndView("/refreshParent");
		UserInfo userInfo = this.getSessionUser();
		subTimeSet.setComId(userInfo.getComId());
		weekReportService.updateSubTimeSet(subTimeSet);
		view.addObject("subTimeSet",subTimeSet);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return view;
	}
	/**
	 * 异步取得团队信息
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value="/findOrgInfo",method = RequestMethod.GET)
	public Map<String, Object> findOrgInfo(Integer weekYear,Integer weekNum) throws ParseException{
		Map<String, Object> map = new HashMap<>();
		Organic organic = organicService.getOrgInfo(this.getSessionUser().getComId());
		map.put("organic", organic);
		//注册时间
		String registDateTime = organic.getRecordCreateTime();
		
		//注册年份
		String registYear = registDateTime.substring(0,4);
		map.put("registYear", registYear);
		//注册时所在周数
		Integer registWeekNum = DateTimeUtil.getWeekOfYear(registDateTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		map.put("registWeekNum", registWeekNum);
		
		//当前时间
		String nowDateTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//当前年份
		Integer nowYear = Integer.parseInt(nowDateTime.substring(0,4));
		map.put("nowYear", nowYear);
		
		Integer nowWeekNum = DateTimeUtil.getWeekOfYear(nowDateTime,DateTimeUtil.yyyy_MM_dd);
		map.put("nowWeekNum", nowWeekNum);
		
		
		int totalNum = 0;
		List<Integer> listWeeks = new ArrayList<Integer>();
		if(null == weekNum){//没有选择周数，选取当前时间的周数
			if(nowYear.equals(weekYear) || null== weekYear){//是当年的数据信息
				for(int weekNumIndex= nowWeekNum ; weekNumIndex > 0 ; weekNumIndex--){//添加9条数据
					listWeeks.add(weekNumIndex);
					totalNum ++ ;
					if(totalNum >= 9){
						break;
					} 
				}
				map.put("listWeeks", listWeeks);
				
				return map;
			}
			
			@SuppressWarnings("static-access")
			String date = "%s-%s-%s".format("%s-%s-%s", new Object[]{weekYear,"12","31"});
			nowWeekNum = DateTimeUtil.getWeekOfYear(date,DateTimeUtil.yyyy_MM_dd);
			if(nowWeekNum == 1){
				date = DateTimeUtil.addDate(date, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -7);
				nowWeekNum = DateTimeUtil.getWeekOfYear(date,DateTimeUtil.yyyy_MM_dd);
			}
			
			for(int weekNumIndex= nowWeekNum ; weekNumIndex > 0 ; weekNumIndex--){
				listWeeks.add(weekNumIndex);
				totalNum ++ ;
				if(totalNum >= 9){
					break;
				} 
			}
			map.put("listWeeks", listWeeks);
			
			return map;
		}
		
		//选取了周数的
		if(nowYear.equals(weekYear)){
			if(weekNum<=5){
				for(int i=9;i>0;i--){
					listWeeks.add(i);
				}
				map.put("listWeeks", listWeeks);
				
				return map;
			}
			
			if((nowWeekNum - 5) <= weekNum){
				
				for(int weekNumIndex = nowWeekNum ; weekNumIndex > 0 ; weekNumIndex--){
					listWeeks.add(weekNumIndex);
					totalNum ++ ;
					if(totalNum >= 9){
						break;
					} 
				}
				map.put("listWeeks", listWeeks);
				
				return map;
			}
			
			
			for(int weekNumIndex = (weekNum+4) ; weekNumIndex > 0 ; weekNumIndex--){
				listWeeks.add(weekNumIndex);
				totalNum ++ ;
				if(totalNum >= 9){
					break;
				} 
			}
			map.put("listWeeks", listWeeks);
			
			return map;
		}
		
		String date = "%s-%s-%s";
		Integer lastWeekNum = DateTimeUtil.getWeekOfYear(date.format(date, new Object[]{weekYear,"12","31"}),DateTimeUtil.yyyy_MM_dd);
		if(lastWeekNum >= weekNum ){
			if(weekNum+4 >= lastWeekNum){
				nowWeekNum = lastWeekNum;
			}else{
				nowWeekNum = weekNum + 4;
			}
		}else{
			nowWeekNum = weekNum + 4;
		}
		for(int weekNumIndex= nowWeekNum ; weekNumIndex > 0 ; weekNumIndex--){
			listWeeks.add(weekNumIndex);
			totalNum ++ ;
			if(totalNum >= 9){
				break;
			} 
		}
		map.put("listWeeks", listWeeks);
		
		return map;
	}
	
	/**
	 * 异步取得周报数据
	 * @param weekReport 周报查询条件
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/ajaxListPagedWeekreport")
	public Map<String, Object> ajaxListPagedWeekreport(WeekReportPojo weekReport
			,Integer pageNum, Integer pageSize) throws ParseException{
		Map<String, Object> map = new HashMap<String, Object>();
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		UserInfo userInfo = this.getSessionUser();
		//周报列表
		PageBean<WeekReport> result = weekReportService.listPagedWeekReport(weekReport,userInfo);
		
		map.put("pageBean", result);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 删除周报附件与留言附件
	 * @param weekReportId
	 * @param wpUpFileId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delWpUpfile")
	public Map<String, Object> delWpUpfile(Integer weekReportId,Integer wpUpFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		weekReportService.delWpUpfile(wpUpFileId,type,userInfo,weekReportId);
		map.put("status", "y");
		return map;
	}
	
}