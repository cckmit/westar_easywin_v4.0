package com.westar.core.web.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Daily;
import com.westar.base.model.DailyMod;
import com.westar.base.model.DailyModContent;
import com.westar.base.model.DailyQ;
import com.westar.base.model.DailyShareGroup;
import com.westar.base.model.DailyTalk;
import com.westar.base.model.DailyUpfiles;
import com.westar.base.model.DailyVal;
import com.westar.base.model.DailyViewer;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.ModuleOperateConfig;
import com.westar.base.model.MsgShare;
import com.westar.base.model.Organic;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.DailyPojo;
import com.westar.base.pojo.Notification;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.ZTreePojo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.DailyDao;
import com.westar.core.dao.ModOptConfDao;
import com.westar.core.dao.OrganicDao;
import com.westar.core.service.DailyService;
import com.westar.core.service.ForceInPersionService;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

/**
 * 
 * 描述:日报分享功能
 * @author lxl
 * @date 2018年8月25日 下午1:13:24
 */
@Controller
@RequestMapping("/daily")
public class DailyController extends BaseController{

	@Autowired
	DailyService dailyService;

	@Autowired
	ForceInPersionService forceInService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	SystemLogService systemLogService;

	@Autowired
	ViewRecordService viewRecordService;

	@Autowired
	ModOptConfDao modOptConfService;

	@Autowired
	OrganicDao organicService;

	@Autowired
	MsgShareService msgShareService;

	@Autowired
	TodayWorksService todayWorksService;

	@Autowired
	DailyDao dailyDao;

	/***************************以下是分享模块************************************************/

	/**
	 * 分享填写跳转页面
	 * @param chooseDate
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/addDailyPage")
	public ModelAndView addDailyPage(HttpServletRequest request,String chooseDate,String idType) throws ParseException{
		//选中的时间
		String tempNowDate = chooseDate;
		//没有选择时间，则给定当前日期
		if(null==tempNowDate || "".equals(tempNowDate)){
			//当前时间
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			tempNowDate = nowDate;
		}

		UserInfo userInfo = this.getSessionUser();

		//取得所选日期所写分享
		Daily daily = dailyService.initDaily(userInfo,tempNowDate);

		ModelAndView view = new ModelAndView("/daily/addDaily");

		ModuleOperateConfig config = modOptConfService.getModuleOperateConfig(userInfo.getComId(), ConstantInterface.TYPE_DAILY, "update");
		if(null!=config){
			//是否有修改权限
			view.addObject("editAuthor", "no");
		}

		if(null!=daily &&daily.getCountVal()>0 &&
				"0".equals(daily.getState())){//分享已经发布了
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY, daily.getId());
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}

		}

		view.addObject("nowDate",DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		//当前所在的年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		view.addObject("nowYear",nowYear);
		//当前所在月份
		String nowMonth = DateTimeUtil.getNowDateStr(DateTimeUtil.MM);
		view.addObject("nowMonth",nowMonth);

		view.addObject("sessionUser",userInfo);

		view.addObject("daily",daily);

		view.addObject("chooseDate",tempNowDate);

		view.addObject("userInfo",userInfo);


		//上次使用的分组
		List<UsedGroup> usedGroups = null;
		if(null==idType || "".equals(idType)){
			usedGroups = userInfoService.listUsedGroup(userInfo.getComId(),userInfo.getId());
		}else{
			usedGroups = new ArrayList<UsedGroup>();
			String[] groupIdS = idType.split(",");
			if(null!=groupIdS && groupIdS.length>0){
				for(String str :groupIdS){
					if("0".equals(str.split("@")[1])){
						//所有人
						UsedGroup usedGroup = new UsedGroup();
						usedGroup.setGroupType("0");
						usedGroups.add(usedGroup);
						break;
					}else if("2".equals(str.split("@")[1])){

						//自己
						UsedGroup usedGroup = new UsedGroup();
						usedGroup.setGroupType("2");
						usedGroups.add(usedGroup);
						break;
					}else{
						UsedGroup usedGroup = new UsedGroup();
						usedGroup.setGroupType("1");
						usedGroup.setGrpId(Integer.parseInt(str.split("@")[0]));
						usedGroups.add(usedGroup);
					}
				}
			}
		}
		//个人组群集合
		List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(userInfo.getComId(),userInfo.getId());
		//最近一次使用的分组的类型，分组名称以及自定义所有的分组
		Map<String, String> grpMap = CommonUtil.usedGrpJson(usedGroups,listSelfGroup);
		//最近一次使用的分组的类型
		view.addObject("idType", grpMap.get("idType"));
		if(null==idType || "".equals(idType)){
			//分组名称
			view.addObject("scopeTypeSel", (grpMap.get("scopeTypeSel") == null || grpMap.get("scopeTypeSel").equals("null")) ? "" : grpMap.get("scopeTypeSel"));
		}
		//自定义所有的分组
		view.addObject("selfGroupStr", grpMap.get("selfGroupStr"));

		return view;
	}


	/**
	 * 验证选择的日期是否有效
	 * @param chooseDateStr 选中的时间
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="/checkDailyDate")
	public Map<String,Object> checkDailyDate(String chooseDateStr) throws ParseException{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//当前操作人员的入职时间
		String hireDateStr = null;
		if(null != sessionUser){//系统已断开连接
			hireDateStr = sessionUser.getHireDate();
		}

		//当前时间
		Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(chooseDateStr + " 00:00:00");


		//先判断入职时间
		if(null!=hireDateStr){//入职时间不为空
			//入职时间
			Date hireDate = DateTimeUtil.parseDate(hireDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);

			//入职时间不能大于选择时间
			if(hireDate.getTime() >= now.getTime()){
				map.put("status","n");
				map.put("info","选择的日期不能大于你的入职日期");
			}else{
				map.put("status","y");
			}
		}else{
			map.put("status","n");
			map.put("info","未查询到你的入职日期");
		}

		return map;
	}

	/**
	 * 填写分享
	 * @param request
	 * @param daily
	 * @return
	 * @throws org.apache.lucene.queryparser.classic.ParseException
	 * @throws Exception
	 */
	@RequestMapping(value="/addDaily",method = RequestMethod.POST)
	public ModelAndView addDaily(HttpServletRequest request,Daily daily,DailyPojo dailyParam,String idType) throws Exception{

		String tempDate = daily.getChooseDate();
		if(null==tempDate || "".equals(tempDate)){
			tempDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		}
		UserInfo userInfo = this.getSessionUser();
		//分享条目
		List<DailyQ> dailies = dailyService.listDailyQ(daily.getId(),userInfo.getComId(),userInfo.getId());
		List<DailyVal> dailyVals = new ArrayList<DailyVal>();
		//分享值，此处可能有问题，当某一条未填写就发布时，可能造成条目和条目值不匹配问题
		String[] val = request.getParameterValues("qVal");
		for (int i=0;i<dailies.size();i++) {
			DailyVal dailyVal = new DailyVal();
			//企业
			dailyVal.setComId(userInfo.getComId());
			//分享内容要求主键字符串
			String dailyIdStr = dailies.get(i).getId() + "";

			dailyVal.setQuestionId(Integer.parseInt(dailyIdStr));
			//分享主键
			dailyVal.setDailyId(daily.getId());
			dailyVal.setDailyValue((null!=val && !"".equals(val) && !"".equals(val[i].trim())) ? val[i] : "未填写");
			dailyVals.add(dailyVal);
		}
		daily.setDailyVals(dailyVals);

		MsgShare msgShare = new MsgShare();
		//获取信息分享范围
		String[] groupIdS = idType.split(",");
		if(null!=groupIdS && groupIdS.length>0){
			List<DailyShareGroup> listDailyShareGroup = new ArrayList<DailyShareGroup>();
			List<ShareGroup> listShareGroup = new ArrayList<ShareGroup>();
			for(String str :groupIdS){
				if("0".equals(str.split("@")[1])){
					//所有人
					msgShare.setScopeType(ALL_COLLEAGUE);
					daily.setScopeType(ALL_COLLEAGUE);
					break;
				}else if("2".equals(str.split("@")[1])){
					//自己
					msgShare.setScopeType(MYSELF);
					daily.setScopeType(MYSELF);
					break;
				}else{
					//根据组界定范围
					msgShare.setScopeType(BY_GROUPID);
					daily.setScopeType(BY_GROUPID);
					ShareGroup shareGroup = new ShareGroup();
					shareGroup.setComId(userInfo.getComId());
					shareGroup.setGrpId(Integer.parseInt(str.split("@")[0]));
					listShareGroup.add(shareGroup);

					DailyShareGroup dailyShareGroup = new DailyShareGroup();
					dailyShareGroup.setComId(userInfo.getComId());
					dailyShareGroup.setGrpId(Integer.parseInt(str.split("@")[0]));
					listDailyShareGroup.add(dailyShareGroup);
				}
			}
			msgShare.setShareGroup(listShareGroup);
			daily.setListDailyShareGroup(listDailyShareGroup);
		}

		//填写分享
		dailyService.addDaily(daily,userInfo,msgShare);
		String sid = this.getSid();
		ModelAndView view = new ModelAndView();



		if(null!=dailyParam &&null!=dailyParam.getViewType() && dailyParam.getViewType()==1){
			view.setViewName("redirect:/daily/viewDaily");
			view.addObject("sid", sid);
			view.addObject("id", daily.getId());
			view.addObject("dailierId", dailyParam.getDailierId());
			view.addObject("depId", dailyParam.getDepId());
			view.addObject("depName", dailyParam.getDepName());
			view.addObject("startDate", dailyParam.getStartDate());
			view.addObject("endDate", dailyParam.getEndDate());
			view.addObject("dailyName", dailyParam.getDailyName());

		}else{
			view.setViewName("refreshParent");
			view.addObject("chooseDate", tempDate);
		}

		String content="发布";
		if("1".equals(daily.getState())){
			content = "保存";
		}
		this.setNotification(Notification.SUCCESS, content+"成功");

		view.addObject("dailyParam", dailyParam);

		view.addObject("nowDate",DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd));
		//当前所在的年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		view.addObject("nowYear",nowYear);
		//当前所在月份
		String nowMonth = DateTimeUtil.getNowDateStr(DateTimeUtil.MM);
		view.addObject("nowMonth",nowMonth);
		return view;
	}

	/**
	 * 分享列表
	 * @param request
	 * @param dailyPojo 分享
	 * @return org.springframework.web.servlet.ModelAndView
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:31
	 */
	@RequestMapping("/listPagedDaily")
	public ModelAndView listPagedDaily(HttpServletRequest request, DailyPojo dailyPojo) throws ParseException{
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		dailyPojo.setComId(userInfo.getComId());
		ModelAndView view = new ModelAndView("/daily/dailyCenter");

		//是否有下属
		Integer  power = 0;
		//是否是督察
		Integer  isForce = 0;

		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		//当前日期，用于js判断
		view.addObject("nowDate",nowDate);
		//当前所在的年份
		String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		view.addObject("nowYear",nowYear);
		//当前所在月份
		String nowMonth = DateTimeUtil.getNowDateStr(DateTimeUtil.MM);
		view.addObject("nowMonth",nowMonth);

		//如果初次访问没有传入年值则添加当前年
		dailyPojo.setDailyYear(dailyPojo.getDailyYear() == null ? Integer.parseInt(nowYear) : dailyPojo.getDailyYear());

		//汇报人类别
		String ownerType = dailyPojo.getDailierType();
		//若是没有下属，则没有负责人类别一说
		if(userInfo.getCountSub()<=0 && null!=ownerType && "1".equals(ownerType)){
			dailyPojo.setDailierType(null);
		}
		//有下属
		if(userInfo.getCountSub()>0){
			power++;
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		if(isForceIn){
			isForce++;
		}

		//初始化汇报人名称
		if(null!=dailyPojo.getDailierId() && dailyPojo.getDailierId()!=0){
			dailyPojo.setUserName(userInfoService.getUserInfo(userInfo.getComId(),dailyPojo.getDailierId()).getUserName());
		}

		view.addObject("userInfo",userInfo);

		//是否有下属
		view.addObject("power",power);
		//是否有管理权限
		view.addObject("isForce",isForce);
		//分享bean
		view.addObject("daily",dailyPojo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_DAILY);

		//默认最小日期
		view.addObject("startDate",nowYear + "-01-01");
		//默认最大日期
		view.addObject("endDate",nowDate);
		return view;
	}

	/**
	 * 异步取得分享数据
	 * @param daily
	 * @param pageNum
	 * @param pageSize
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @author Administrator
	 * @date 2018/6/22 0022 17:35
	 */
	@ResponseBody
	@RequestMapping("/ajaxListPagedDaily")
	public Map<String, Object> ajaxListPagedDaily(DailyPojo daily
			,Integer pageNum, Integer pageSize,String chooseMonth) throws ParseException{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();

		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());

		/*****************************参数回传***********************************/
		//查询的开始时间
		String startDate = daily.getStartDate();
		if(StringUtils.isEmpty(startDate)){
			Integer year = daily.getDailyYear();
			startDate = year+"-01-01";
			daily.setStartDate(startDate);
		}
		//查询的结束时间
		String endDate = daily.getEndDate();
		
		if(StringUtils.isNotEmpty(chooseMonth)){
			String nowMonth = Integer.parseInt(DateTimeUtil.getNowDateStr(DateTimeUtil.MM))+"" ;
			String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
			if(nowMonth.equals(chooseMonth) && nowYear.equals(daily.getDailyYear().toString())){
				endDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			}else{
				Calendar calendar = Calendar.getInstance();
				calendar.set(daily.getDailyYear(),Integer.parseInt(chooseMonth),1);
				calendar.getTime();
				Long dateLong = calendar.getTime().getTime() - 24L * 60 * 60 * 1000;
				Date date = new Date(dateLong);
				endDate = DateTimeUtil.formatDate(date, DateTimeUtil.yyyy_MM_dd);
			}
			daily.setEndDate(endDate);
		}
		if(StringUtils.isEmpty(endDate)){
			Integer year = daily.getDailyYear();
			Integer nowYear = Integer.parseInt(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy));
			if(year.equals(nowYear)){
				endDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			}else{
				endDate = year+"-12-31";
			}
			daily.setEndDate(endDate);
		}
		//分享列表
		PageBean<Daily> result = dailyService.listPagedDaily(daily,userInfo);
		map.put("pageBean", result);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}

	/**
	 * 获取团队的成立时间，用于筛选
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="/findOrgInfo",method = RequestMethod.GET)
	public Map<String, Object> findOrgInfo(Integer dailyYear,Integer dailyNum,String dailierType) throws ParseException{
		Map<String, Object> map = new HashMap<>();
		UserInfo userInfo = this.getSessionUser();
		UserInfo userInfoT = (UserInfo) dailyDao.objectQuery(UserInfo.class,userInfo.getId());
		Organic organic = organicService.getOrgInfo(this.getSessionUser().getComId());
		map.put("organic", organic);
		//注册时间
		String registerTime = organic.getRecordCreateTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String userRegisterTime = userInfoT.getRecordCreateTime();
		if(null != dailierType && dailierType.equals("0")){
			//用户注册日期和公司注册日期谁大取谁
			map.put("registerTime",sdf.parse(registerTime).getTime() > sdf.parse(userRegisterTime).getTime() ? registerTime : userRegisterTime);
		}else{
			map.put("registerTime",registerTime);
		}

		String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		map.put("nowTime",nowTime);

		return map;
	}

	/**
	 * 日报权限验证
	 * @param dailyId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/authorCheck")
	public Daily authorCheck(Integer dailyId){
		UserInfo userInfo = this.getSessionUser();
		Daily daily = dailyService.getDailyById(dailyId);
		if(null == userInfo){
			if(null == daily){
				daily = new Daily();
			}
			daily.setSucc(false);
			daily.setPromptMsg(CommonConstant.OFF_LINE_INFO);
			return daily;
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		//有查看权限或是监督人员
		if(dailyService.authorCheck(userInfo.getComId(),userInfo.getId(),daily.getReporterId(),dailyId)|| isForceIn){
			daily.setSucc(true);
		}else{
			//查看验证，删除消息提醒
			todayWorksService.updateTodoWorkRead(dailyId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY,0);

			daily.setSucc(false);
			daily.setPromptMsg("抱歉，你没有查看权限");
		}
		return daily;
	}


	/**
	 * 查看分享
	 * @param request
	 * @param id 分享主键
	 * @param preId 前一个分享主键
	 * @param nextId 后一个分享主键
	 * @param dailyParam 分享查询条件
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/viewDaily")
	public ModelAndView viewDaily(HttpServletRequest request,Integer id,Integer preId,Integer nextId,
								  DailyPojo dailyParam) throws ParseException{
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView();

		dailyParam.setViewType(1);
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		//汇报人类别
		String ownerType = dailyParam.getDailierType();
		//若是没有下属，则没有负责人类别一说
		if(userInfo.getCountSub()<=0 && null!=ownerType && "1".equals(ownerType)){
			dailyParam.setDailierType(null);
		}

		//取得所选分享
		Daily dailyT = dailyService.getDailyForView(id,userInfo,dailyParam,isForceIn);
		if(null==dailyT){//分享不存在
			//跳转
			view.setViewName("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有查看权限");

			//查看分享，删除消息提醒
			todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY,0);

			return view;
		}
		if(null!=preId && null!=nextId){//若是选中的时间对应的分享主键,恰好是当前的前一个或是后一个
			if(!id.equals(preId)&& !id.equals(nextId)){
				dailyT.setPreId(preId);
				dailyT.setNextId(nextId);
			}
		}

		//分享查看权限验证(有查看权限或是监督人员)
		if((dailyService.authorCheck(userInfo.getComId(),userInfo.getId(),dailyT.getReporterId(),dailyT.getId()) || isForceIn)){

			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				viewRecordService.addViewRecord(userInfo,viewRecord);
			}

			if(dailyParam.getMouldViewType() != null && dailyParam.getMouldViewType() == 1){
				// 分享附件
				dailyT.setDailyFiles(dailyDao.listDailyFiles(dailyT.getId(), userInfo
						.getComId()));
				view.setViewName("/daily/dailyShareView");
				view.addObject("daily", dailyT);
				view.addObject("mouldViewType",dailyParam.getMouldViewType());
			}else if(dailyT.getReporterId().equals(userInfo.getId())){//若操作的是自己的分享
				Daily daily = dailyService.initDaily(userInfo, dailyT.getDailyDate());
				daily.setPreId(dailyT.getPreId());
				daily.setNextId(dailyT.getNextId());
				daily.setUserName(dailyT.getUserName());
				daily.setCountQues(dailyT.getCountQues());
				//设置留言及附件显示总数
				daily.setTalkNum(dailyT.getTalkNum());
				daily.setFileNum(dailyT.getFileNum());
				view.setViewName("/daily/addDaily");
				view.addObject("daily", daily);

				ModuleOperateConfig config = modOptConfService.getModuleOperateConfig(userInfo.getComId(), ConstantInterface.TYPE_DAILY, "update");
				if(null!=config){
					view.addObject("editDaily", "no");
				}

				//上次使用的分组
				List<UsedGroup> usedGroups = userInfoService.listUsedGroup(userInfo.getComId(),userInfo.getId());
				//个人组群集合
				List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(userInfo.getComId(),userInfo.getId());
				//最近一次使用的分组的类型，分组名称以及自定义所有的分组
				Map<String, String> grpMap = CommonUtil.usedGrpJson(usedGroups,listSelfGroup);

				//字符串转对象集合
				String selfGroupStr = grpMap.get("selfGroupStr");
				List<ZTreePojo> zTreePojos = JSONArray.parseArray(selfGroupStr, ZTreePojo.class);
				//处理默认
				if(null != daily.getScopeTypeSel() && !daily.getScopeTypeSel().equals("")){
					String[] sels = daily.getScopeTypeSel().split(",");
					if(null != sels && sels.length > 0){
						for(ZTreePojo zTreePojo : zTreePojos){
							//将每个pojo的初始选中状态都设为null
							zTreePojo.setChecked(null);
							for(int i=0;i<sels.length;i++){
								if(zTreePojo.getName().equals(sels[i])){
									//当当前分享已经选择了分组，则将默认选中设置为已选择的分组
									zTreePojo.setChecked("true");
									break;
								}
							}
						}
					}
				}

				//对象集合转字符串
				Gson gson = new Gson();
				//必须替换双引号为单引号，插件识别双引号时会出错
				String realSelfGroup = gson.toJson(zTreePojos).replaceAll("\"","\'");
				grpMap.put("selfGroupStr",realSelfGroup);

				//最近一次使用的分组的类型
				String dailyType = daily.getScopeTypeStr();
				String type = grpMap.get("idType");
				view.addObject("idType", (dailyType == null || dailyType.equals("")) ? (type == null || type.equals("")) ? "" : type : dailyType);
				//分组名称
				String dailySel = daily.getScopeTypeSel();
				String sel = grpMap.get("scopeTypeSel");
				view.addObject("scopeTypeSel", (dailySel == null || dailySel.equals("")) ? (sel == null || sel.equals("null")) ? "" : sel : dailySel);
				//自定义所有的分组
				view.addObject("selfGroupStr", grpMap.get("selfGroupStr"));

			}else{
				// 分享附件
				dailyT.setDailyFiles(dailyDao.listDailyFiles(dailyT.getId(), userInfo
						.getComId()));
				view.setViewName("/daily/viewDaily");
				view.addObject("daily", dailyT);
			}

			view.addObject("userInfo", userInfo);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dailyT.getDailyDate()));
			//所在月
			view.addObject("pageDailyMonth", calendar.get(Calendar.MONTH));

			view.addObject("dailyParam", dailyParam);

			//选择日期
			view.addObject("chooseDate",dailyT.getChooseDate() == null ? dailyT.getDailyDate() : dailyT.getChooseDate());

			//当前时间
			String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			view.addObject("nowDate", nowDate);
			//所选时间所在年
			view.addObject("nowYear", nowDate.substring(0,4));
			//所选时间所在月
			view.addObject("nowMonth", nowDate.substring(5,7));

			//浏览的人员
			List<ViewRecord> listViewRecord = viewRecordService.listViewRecord(userInfo,ConstantInterface.TYPE_DAILY,id);
			view.addObject("listViewRecord", listViewRecord);

		}else{//没有查看权限
			//跳转
			view.setViewName("/refreshParent");
		}

		//查看分享，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY,0);
		return view;
	}

	/**
	 * 分享附件
	 * @param dailyId
	 * @return
	 */
	@RequestMapping("/dailyFilePage")
	public ModelAndView dailyFilePage(Integer dailyId,Integer mouldViewType) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav;
		if(mouldViewType != null && mouldViewType == 1){
			mav = new ModelAndView("/daily/dailyShareFiles");
		}else{
			mav = new ModelAndView("/daily/dailyFiles");
		}
		//查看周报附件，删除消息提醒
		todayWorksService.updateTodoWorkRead(dailyId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_DAILY,0);
		//周报附件
		List<DailyUpfiles> dailyFiles = dailyService.listPagedDailyFiles(userInfo.getComId(),dailyId);
		mav.addObject("dailyFiles",dailyFiles);
		mav.addObject("userInfo",userInfo);
		mav.addObject("dailyId",dailyId);
		return mav;
	}


//	/**
//	 * AJAX新增分享
//	 * @param msgShare
//	 * @param idType
//	 * @throws IOException
//	 * @throws IOException
//	 */
//	@ResponseBody
//	@RequestMapping("/ajaxAddDaily")
//	public Map<String, Object> ajaxAddDaily(MsgShare msgShare,String idType){
//		Map<String, Object> map = new HashMap<String, Object>();
//		UserInfo userInfo = this.getSessionUser();
//
//		//判断是否已存在daily
//		boolean flag = true;
//		String tempDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
//		Daily daily = dailyService.getDaily(userInfo.getComId(),userInfo.getId(),tempDate);
//		if(null == daily){//为空则默认设置
//			daily.setReporterId(userInfo.getId());
//			daily.setComId(userInfo.getComId());
//			daily.setDailyDate(tempDate);
//			daily.setDailyName(userInfo.getUserName() + " " + tempDate + " 分享");
//			flag = false;
//		}
//
//		//获取信息分享范围
//		String[] groupIdS = idType.split(",");
//		List<DailyShareGroup> listDailyShareGroup = new ArrayList<DailyShareGroup>();
//		if(null!=groupIdS && groupIdS.length>0){
//			for(String str :groupIdS){
//				if("0".equals(str.split("@")[1])){
//					//所有人
//					msgShare.setScopeType(allColleague);
//					daily.setScopeType(allColleague);
//					break;
//				}else if("2".equals(str.split("@")[1])){
//					//自己
//					msgShare.setScopeType(mySelf);
//					daily.setScopeType(mySelf);
//					break;
//				}else{
//					//自定义
//					daily.setScopeType(byGroupId);
//
//					DailyShareGroup dailyShareGroup = new DailyShareGroup();
//					dailyShareGroup.setComId(userInfo.getComId());
//					dailyShareGroup.setGrpId(Integer.parseInt(str.split("@")[0]));
//					listDailyShareGroup.add(dailyShareGroup);
//				}
//			}
//		}
//
//		if(flag){//已存在则更新信息
//			//移除之前存放的分组
//			dailyDao.delByField("dailyShareGroup",new String[]{"comId","dailyId"},new Object[]{daily.getComId(),daily.getId()});
//			dailyDao.update(daily);
//
//		}else{//不存在则添加基础信息
//			Integer id = dailyDao.add(daily);
//			daily.setId(id);
//		}
//
//		//添加分享组
//		for(DailyShareGroup dailyShareGroup : listDailyShareGroup){
//			//设置分享id
//			dailyShareGroup.setDailyId(daily.getId());
//			//添加
//			dailyDao.add(dailyShareGroup);
//		}
//
//		//重新解析信息分享信息
//		msgShare = CommonUtil.getMsgShare(idType, "1", msgShare.getContent(), userInfo);
//		//创建人姓名
//		msgShare.setCreatorName(userInfo.getUserName());
//		//工作类型为分享
//		msgShare.setTraceType(0);
//		//设置公开类型 默认公开
//		msgShare.setIsPub(1);
//		msgShare.setModId(1);
//		msgShare.setType(ConstantInterface.TYPE_DAILY);
//		Integer id = msgShareService.addMsgShare(msgShare,userInfo);
//
//		MsgShare vo = msgShareService.getMsgShareById(userInfo.getComId(), id);
//		//表情替换
//		String content = StringUtil.toHtml(StringUtil.cutString(vo.getContent(),140));
//		vo.setContent(content);
//		map.put("msgShare", vo);
//		map.put("status", "y");
//		return map;
//	}

	/***************************以下是分享模板设置************************************************/
	/**
	 * 分享模块配置
	 * @param busType 暂未使用
	 * @return org.springframework.web.servlet.ModelAndView
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:33
	 */
	@RequestMapping("/dailyModConfPage")
	public ModelAndView dailyModConfPage(String busType){
		ModelAndView view = new ModelAndView("/daily/dailyConfCenter");
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0") && userInfo.getCountSub() <= 0){//没有管理权限的直接跳转到主页面
			view = new ModelAndView("redirect:/daily/listPagedDaily?sid="+this.getSid());
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return view;
		}
		view.addObject("userInfo",userInfo);
		//分享模板
		DailyMod dailyMod = dailyService.initDailyMod(userInfo);
		view.addObject("dailyMod", dailyMod);
		//是否有下属
		Integer  power = 0;
		//是否是督察
		Integer  isForce = 0;
		//有下属
		if(userInfo.getCountSub()>0){
			power++;
		}
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		if(isForceIn){
			isForce++;
		}

		//是否有下属
		view.addObject("power",power);
		//是否有管理权限
		view.addObject("isForce",isForce);

		view.addObject("toPage","../daily/dailyModConf.jsp");

		view.addObject("busType", busType);

		view.addObject("homeFlag",ConstantInterface.TYPE_DAILY);
		return view;
	}

	/**
	 * 添加分享内容
	 * @param dailyModContent
	 * @param members 成员
	 * @param deps 部门主键
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:33
	 */
	@ResponseBody
	@RequestMapping(value="/addDailyModContent",method = RequestMethod.POST)
	public Map<String, Object> addDailyModContent(DailyModContent dailyModContent, String[] members, String[] deps){
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(userInfo.getAdmin().equals("0") && userInfo.getCountSub() <= 0){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		dailyModContent.setCereaterId(userInfo.getId());
		dailyModContent.setComId(userInfo.getComId());
		//默认是非系统级别的模板
		dailyModContent.setSysState(0);
		//第一次进来时为null
		dailyModContent.setIsRequire(dailyModContent.getIsRequire() == null ? "0" : "1");
		//添加模板条目
		Integer id = dailyService.addDailyModContent(dailyModContent,members,deps);
		map.put("status", "y");
		map.put("id", id);

		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "添加分享模板条目("+dailyModContent.getModContent()+")",
				ConstantInterface.TYPE_DAILY, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}


	/**
	 * 删除模块内容
	 * @param id 分享主键
	 	 * @param modId 模块id
	 	 * @param dailyLev 分享等级
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:34
	 */
	@ResponseBody
	@RequestMapping(value="/delDailyModContent",method = RequestMethod.POST)
	public Map<String, String> delDailyModContent(Integer id,Integer modId, Integer dailyLev ){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0") && userInfo.getCountSub() <= 0){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		//删除模板条目
		DailyModContent dailyModContent  = dailyService.delModContent(id,modId,dailyLev,userInfo.getComId());

		map.put("status", "y");
		//系统日志
		if(1==dailyLev){
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除团队级别的条目("+dailyModContent.getModContent()+")",
					ConstantInterface.TYPE_DAILY, userInfo.getComId(),userInfo.getOptIP());
		}else if(2==dailyLev){
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除部门级别的条目("+dailyModContent.getModContent()+")",
					ConstantInterface.TYPE_DAILY, userInfo.getComId(),userInfo.getOptIP());
		}else if(3==dailyLev){
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "删除成员级别的条目("+dailyModContent.getModContent()+")",
					ConstantInterface.TYPE_DAILY, userInfo.getComId(),userInfo.getOptIP());
		}
		return map;
	}


	/**
	 *
	 * 修改模板
	 * @param dailyModContent 分享模块内容
	 * @param members 成员主键数组
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:41
	 */
	@ResponseBody
	@RequestMapping(value="/updateDailyModContent",method = RequestMethod.POST)
	public Map<String, String> updateDailyModContent(DailyModContent dailyModContent,String[] members,String[] deps){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0") && userInfo.getCountSub() <= 0){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		dailyModContent.setComId(userInfo.getComId());
		//修改模板
		dailyModContent = dailyService.updateDailyModContent(dailyModContent,members,deps);
		map.put("status", "y");
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "修改分享模板条目为("+dailyModContent.getModContent()+")",
				ConstantInterface.TYPE_DAILY, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}

	/**
	 * 隐藏
	 * @param dailyModContent 分享模块内容
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:42
	 */
	@ResponseBody
	@RequestMapping(value="/hideDailyContent",method = RequestMethod.POST)
	public Map<String, String> hideDailyContent(DailyModContent dailyModContent){
		UserInfo userInfo = this.getSessionUser();
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0") && userInfo.getCountSub() <= 0){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		//修改模板是否显示该问题
		dailyModContent =  dailyService.updateDailyModContent(dailyModContent,null,null);

		map.put("status", "y");
		String content = "设置分享不显示该条目("+dailyModContent.getModContent()+")";
		if("0".equals(dailyModContent.getHideState())){
			content = "设置分享显示该条目("+dailyModContent.getModContent()+")";
		}
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), content,
				ConstantInterface.TYPE_DAILY, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}

	/**
	 * 修改模板该条目是否必填
	 * @param dailyModContent 分享模块内容
	 	 * @param members 成员主键数组
	 	 * @param deps 部门主键数组
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:42
	 */
	@ResponseBody
	@RequestMapping(value="/updateDailyRequireContent",method = RequestMethod.POST)
	public Map<String, String> updateDailyRequireContent(DailyModContent dailyModContent,String[] members,String[] deps){
		UserInfo userInfo = this.getSessionUser();
		dailyModContent.setComId(userInfo.getComId());
		Map<String,String> map = new HashMap<String, String>();
		if(userInfo.getAdmin().equals("0") && userInfo.getCountSub() <= 0){//没有审核权限的直接跳转到主页面
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return map;
		}
		//修改模板是否显示该问题
		dailyModContent =  dailyService.updateDailyModContent(dailyModContent,members,deps);

		map.put("status", "y");
		String content = "设置分享该条目("+dailyModContent.getModContent()+")必填";
		if("0".equals(dailyModContent.getHideState())){
			content = "设置分享显示该条目("+dailyModContent.getModContent()+")非必填";
		}
		//系统日志
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), content,
				ConstantInterface.TYPE_DAILY, userInfo.getComId(),userInfo.getOptIP());
		return map;
	}

	/**
	 * 分享报查看范围设置界面
	 * @return
	 */
	@RequestMapping(value="/dailyViewSetPage")
	public ModelAndView dailyViewSetPage(){
		ModelAndView mav = new ModelAndView("/daily/dailyViewSet");
		UserInfo sessionUser = this.getSessionUser();
		//查询分享查看人员
		List<DailyViewer> listDailyViewer = dailyService.listDailyViewer(sessionUser);
		mav.addObject("listDailyViewer", listDailyViewer);

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
	 * 设置分享查看范围
	 * @param dailyViewerStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateDailyViewers",method = RequestMethod.POST)
	public Map<String, Object> updateDailyViewers(String dailyViewerStr){
		UserInfo userInfo = this.getSessionUser();

		Map<String,Object> map = new HashMap<String, Object>();

		List<DailyViewer> dailyViewerList = JSONArray.parseArray(dailyViewerStr, DailyViewer.class);
		String logContent = "";
		if(null!=dailyViewerList && !dailyViewerList.isEmpty()){
			for (DailyViewer dailyViewer : dailyViewerList) {
				dailyViewer.setUserId(userInfo.getId());
				dailyViewer.setComId(userInfo.getComId());
				logContent = logContent+","+ dailyViewer.getViewerName();
			}
		}
		if(!"".equals(logContent)){
			logContent = logContent.substring(1,logContent.length());
		}

		dailyService.updateDailyViewers(dailyViewerList,userInfo,logContent);

		map.put("status", "y");
		//查询分享查看人员
		List<DailyViewer> listDailyViewer = dailyService.listDailyViewer(userInfo);
		map.put("listDailyViewer", listDailyViewer);
		return map;
	}

	/**
	 * 删除分享查看人员
	 * @param dailyViewer 分享查看人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delDailyViewer",method = RequestMethod.POST)
	public Map<String, Object> delDailyViewer(DailyViewer dailyViewer){
		//当前操作人员
		UserInfo userInfo = this.getSessionUser();
		Map<String,Object> map = new HashMap<String, Object>();
		if(null==userInfo){//服务器session用户数据丢失
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//删除分享查看人员
		dailyService.delDailyViewer(dailyViewer,userInfo);
		map.put("status", "y");
		return map;
	}

	/**********************************分享留言******************************************/
	/**
	 * 反馈留言
	 * @param dailyId
	 * @return
	 */
	@RequestMapping("/dailyTalkPage")
	public ModelAndView dailyTalkPage(Integer dailyId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/daily/dailyTalk");
		//查看反馈留言，删除消息提醒
		todayWorksService.updateTodoWorkRead(dailyId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK,0);
		//反馈留言
		List<DailyTalk> dailyTalks = dailyService.listPagedDailyTalk(dailyId,userInfo.getComId(),"pc");
		mav.addObject("dailyTalks",dailyTalks);
		mav.addObject("dailyId",dailyId);
		mav.addObject("sessionUser",userInfo);

		//获取模块操作权限
		List<ModuleOperateConfig> listModuleOperateConfig = modOptConfService.listModuleOperateConfig(userInfo.getComId(),ConstantInterface.TYPE_DAILY);
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
	@RequestMapping(value="/addDailyTalk",method = RequestMethod.POST)
	public Map<String, Object> addDailyTalk(DailyTalk dailyTalk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		dailyTalk.setComId(sessionUser.getComId());
		dailyTalk.setTalker(sessionUser.getId());
		//反馈留言回复
		Integer id = dailyService.addDailyTalk(dailyTalk,sessionUser);
		//模块日志添加
		if(-1==dailyTalk.getParentId()){
			dailyService.addDailyLog(sessionUser.getComId(), dailyTalk.getDailyId(), sessionUser.getId(), sessionUser.getUserName(), "参与反馈留言");
		}else{
			dailyService.addDailyLog(sessionUser.getComId(), dailyTalk.getDailyId(), sessionUser.getId(), sessionUser.getUserName(), "回复反馈留言");
		}
		map.put("status", "y");
		map.put("id", id);
		//用于返回页面拼接代码
		DailyTalk dailyTalk4Page = dailyService.getDailyTalk(id, sessionUser.getComId());
		map.put("dailyTalk", dailyTalk4Page);
		map.put("sessionUser", this.getSessionUser());

		ModuleOperateConfig modOptConfig = modOptConfService.getModuleOperateConfig(sessionUser.getComId(),
				ConstantInterface.TYPE_WEEK, "delete");
		if(null!=modOptConfig){
			map.put("delDaily","no");
		}
		return map;
	}

	/**
	 * 删除分享附件及留言附件
	 * @param dailyId
	 * @param dailyUpFileId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delDailyUpfile")
	public Map<String, Object> delDailyUpfile(Integer dailyId,Integer dailyUpFileId,String type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		dailyService.delDailyUpfile(dailyUpFileId,type,userInfo,dailyId);
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 删除分享留言
	 * @param dailyTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delDailyTalk")
	public Map<String, Object> delDailyTalk(DailyTalk dailyTalk,String delChildNode) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		dailyService.delDailyTalk(dailyTalk,delChildNode,userInfo);
		map.put("status", "y");
		return map;
	}
	
}