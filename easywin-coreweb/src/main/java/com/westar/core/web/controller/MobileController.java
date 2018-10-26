//package com.westar.core.web.controller;
//
//import java.io.BufferedInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import com.google.gson.Gson;
//import com.westar.base.cons.SessionKeyConstant;
//import com.westar.base.model.Area;
//import com.westar.base.model.Attention;
//import com.westar.base.model.Customer;
//import com.westar.base.model.CustomerHandOver;
//import com.westar.base.model.CustomerLog;
//import com.westar.base.model.CustomerType;
//import com.westar.base.model.CustomerUpfile;
//import com.westar.base.model.DataDic;
//import com.westar.base.model.Department;
//import com.westar.base.model.FeedBackInfo;
//import com.westar.base.model.FeedBackType;
//import com.westar.base.model.Filecontent;
//import com.westar.base.model.Item;
//import com.westar.base.model.ItemHandOver;
//import com.westar.base.model.ItemLog;
//import com.westar.base.model.ItemTalk;
//import com.westar.base.model.ItemUpfile;
//import com.westar.base.model.LinkMan;
//import com.westar.base.model.MsgShare;
//import com.westar.base.model.Organic;
//import com.westar.base.model.SelfGroup;
//import com.westar.base.model.Task;
//import com.westar.base.model.TaskLog;
//import com.westar.base.model.TaskTalk;
//import com.westar.base.model.TaskUpfile;
//import com.westar.base.model.TodayWorks;
//import com.westar.base.model.Upfiles;
//import com.westar.base.model.UserInfo;
//import com.westar.base.model.ViewRecord;
//import com.westar.base.model.WeekRepLog;
//import com.westar.base.model.WeekRepTalk;
//import com.westar.base.model.WeekRepUpfiles;
//import com.westar.base.model.WeekReport;
//import com.westar.base.pojo.FlowRecord;
//import com.westar.base.pojo.WeekReportPojo;
//import com.westar.base.util.ConstantInterface;
//import com.westar.base.util.CommonUtil;
//import com.westar.base.util.DateTimeUtil;
//import com.westar.base.util.Encodes;
//import com.westar.base.util.FileMD5Util;
//import com.westar.base.util.FileUtil;
//import com.westar.base.util.MathExtend;
//import com.westar.base.util.UUIDGenerator;
//import com.westar.core.service.AttentionService;
//import com.westar.core.service.CrmService;
//import com.westar.core.service.DepartmentService;
//import com.westar.core.service.ForceInPersionService;
//import com.westar.core.service.ItemService;
//import com.westar.core.service.JiFenService;
//import com.westar.core.service.MsgShareService;
//import com.westar.core.service.OrganicService;
//import com.westar.core.service.RecycleBinService;
//import com.westar.core.service.SelfGroupService;
//import com.westar.core.service.SystemLogService;
//import com.westar.core.service.TaskService;
//import com.westar.core.service.UploadService;
//import com.westar.core.service.UserInfoService;
//import com.westar.core.service.WeekReportService;
//import com.westar.core.thread.FileIndexThread;
//import com.westar.core.web.DataDicContext;
//import com.westar.core.web.FreshManager;
//import com.westar.core.web.PaginationContext;
//import com.westar.core.web.SessionContext;
//
///**
// * 移动端应用控制类
// * @author lj
// *
// */
//@Controller
//@RequestMapping("/mobile")
//public class MobileController extends BaseController {
//	
//	// 团队service
//	@Autowired
//	OrganicService organicService;
//	
//	// 用户service
//	@Autowired
//	UserInfoService userInfoService;
//	
//	// 回收箱service
//	@Autowired
//	RecycleBinService recycleBinService;
//	
//	// 任务service
//	@Autowired
//	TaskService taskService;
//	
//	// 系统日志service
//	@Autowired
//	SystemLogService systemLogService;
//	
//	// 积分service
//	@Autowired
//	JiFenService jifenService; 
//	
//	// 信息分享service
//	@Autowired
//	MsgShareService msgShareService;
//	
//	// 私有组service
//	@Autowired
//	SelfGroupService selfGroupService;
//
//	// 部门service
//	@Autowired
//	DepartmentService departmentService;
//	
//	// 关注service
//	@Autowired
//	AttentionService attentionService;
//	
//	// 项目service
//	@Autowired
//	ItemService itemService;
//	
//	// 客户service
//	@Autowired
//	CrmService crmService;
//	
//	// 周报service
//	@Autowired
//	WeekReportService weekReportService;
//	
//	//附件上传services
//	@Autowired
//	UploadService uploadService;
//	
//	@Autowired
//	ForceInPersionService forceInService;
//
//	/**
//	 * 验证登录人是否合理以及获取参加的所有团队集合
//	 * @param loginName
//	 * @param password
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/mobileLogin")
//	public String mobileLogin(String loginName, String password){
//		StringBuffer orgJson = new StringBuffer();
//		String passwordMD5 = Encodes.encodeMd5(password);
//		//查询用户所在的激活企业
//		List<Organic> listOrg = organicService.listUserOrg(loginName.toLowerCase(),passwordMD5);
//		if(null==listOrg || listOrg.size()==0){//没有激活的
//			UserInfo user = userInfoService.getUserInfoByAccount(loginName.toLowerCase());
//			if(null==user){
//				orgJson.append("{info:'用户不存在！'}");
//				return orgJson.toString();
//			}else{
//				//验证用户登陆信息
//				if(!passwordMD5.equals(user.getPassword())){
//					orgJson.append("{info:'密码错误！'}");
//					return orgJson.toString();
//				}else{
//					orgJson.append("{info:'您的账号没有激活,或是团队号停用!'}");
//					return orgJson.toString();
//				}
//			}
//		}else{
//			Gson gson = new Gson();
//			orgJson.append("{info:'验证成功！',loginName:'"+loginName+"',password:'"+passwordMD5+"',data:"+gson.toJson(listOrg)+"}");
//		}
//		return orgJson.toString();
//	}
//	/**
//	 * 从移动端本地保存的用户信息直接登录系统
//	 * @param loginName
//	 * @param password
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/mobileLoginByAuto")
//	public String mobileLoginByAuto(String loginName, String password){
//		StringBuffer orgJson = new StringBuffer();
//		//查询用户所在的激活企业
//		List<Organic> listOrg = organicService.listUserOrg(loginName.toLowerCase(),password);
//		if(null==listOrg || listOrg.size()==0){//没有激活的
//			UserInfo user = userInfoService.getUserInfoByAccount(loginName.toLowerCase());
//			if(null==user){
//				orgJson.append("{info:'用户不存在！'}");
//				return orgJson.toString();
//			}else{
//				//验证用户登陆信息
//				if(!password.equals(user.getPassword())){
//					orgJson.append("{info:'密码错误！'}");
//					return orgJson.toString();
//				}else{
//					orgJson.append("{info:'您的账号没有激活,或是团队号停用!'}");
//					return orgJson.toString();
//				}
//			}
//		}else{
//			Gson gson = new Gson();
//			orgJson.append("{info:'验证成功！',loginName:'"+loginName+"',password:'"+password+"',data:"+gson.toJson(listOrg)+"}");
//		}
//		return orgJson.toString();
//	}
//	/**
//	 * 登入所选企业平台(移动端登入)
//	 * @param loginName 用户名
//	 * @param password MD5密码
//	 * @param comId 所需企业主键
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/loginInOrg")
//	public String loginInOrg(HttpSession session,String loginName, String password,String comId){
//		StringBuffer infoJson = new StringBuffer();
//		final UserInfo userInfo = userInfoService.userAuth(loginName.toLowerCase(),password,comId);
//		if (userInfo != null) {
//			String sid = this.setSessionObj(SessionKeyConstant.USER_CONTEXT, userInfo);
//			SessionContext.removeSessionUser(userInfo.getId());
//			SessionContext.addSessionUser(userInfo.getId(), sid, session);
//			//删除回收箱中超过三天的数据(数据量不大，不需要开启新的线程)
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						recycleBinService.delAllOverTri(userInfo);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//				}
//			}).start();
//			//删除自己的无效任务
//			taskService.delUnusedTask(userInfo);
//			//添加日志记录 
//			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),"从移动端登入平台",ConstantInterface.TYPE_LOGIN,userInfo.getComId());
//			//每日登录积分
//			jifenService.addJifen(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_LOGIN,"每日登录系统",0);
//			infoJson.append("{loginName:'"+loginName+"',password:'"+password+"',sid:'"+sid+"',succ:'yes',orgName:'"+userInfo.getOrgName()+"',comId:'"+userInfo.getComId()+"',userId:'"+userInfo.getId()+"'}");
//		} else {
//			infoJson.append("{info:'登入异常，请及时联系管理员！',succ:'no'}");
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取首页提示信息
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/homeMsgs")
//	public String homeMsgs(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			}
//			//总提醒数
//			Integer noReadNums = msgShareService.countNoRead(userInfo.getComId(),userInfo.getId());
//			//总待办事项数
//			Integer todoNums = msgShareService.countTodo(userInfo.getComId(),userInfo.getId());
//			//总关注未读消息数
//			Integer attenNums = msgShareService.countAttenNoRead(userInfo.getComId(),userInfo.getId());
//			Gson gson = new Gson();
//			infoJson.append("{noReadNums:'"+noReadNums+"',todoNums:'"+todoNums+"',attenNums:'"+attenNums+"',succ:'yes',user:"+gson.toJson(userInfo)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		
//		return infoJson.toString();
//	}
//	/**
//	 * 获取待办事项
//	 * @param pageNum 加载次数
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/viewFromHome")
//	public String viewFromHome(Integer pageNum,String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type.trim())){
//				infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//				return infoJson.toString();
//			}
//			Gson gson = new Gson();
//			//pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//			//一次加载行数
//			//PaginationContext.setPageSize(9);
//			//列表数据起始索引位置
//			//PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			if("listWorkToDo".equals(type)){
//				//待办事项
//				//过滤条件todayWorks
//				List<TodayWorks> list = msgShareService.listWorksTodo(new TodayWorks(),userInfo.getComId(),userInfo.getId());
//				//业务类型处理
//				List<TodayWorks> newList = new ArrayList<TodayWorks>();
//				for(int i=0;i<list.size();i++){
//					if("015"==list.get(i).getBusType()){
//						list.get(i).setModuleTypeName("加入申请");
//					}else{
//						list.get(i).setModuleTypeName(DataDicContext.getInstance().getCurrentPathZvalue("moduleType",list.get(i).getBusType()).substring(0,2));
//					}
//					newList.add(list.get(i));
//				}
//				infoJson.append("{succ:'yes',data:"+gson.toJson(newList)+",user:"+gson.toJson(userInfo)+"}");
//			}else if("listOfMyAtten".equals(type)){
//				//我的关注
//				Attention atten = new Attention();
//				//企业号
//				atten.setComId(userInfo.getComId());
//				//操作员Id
//				atten.setUserId(userInfo.getId());
//				//分页查询关注信息
//				//List<Attention> list = attentionService.listpagedAtten(atten);
//				List<Attention> list = attentionService.listMyAtten(atten);
//				List<Attention> newList = new ArrayList<Attention>();
//				for(int i=0;i<list.size();i++){
//					list.get(i).setBusTypeName(DataDicContext.getInstance().getCurrentPathZvalue("moduleType",list.get(i).getBusType()).substring(0,2));
//					newList.add(list.get(i));
//				}
//				infoJson.append("{succ:'yes',data:"+gson.toJson(newList)+",user:"+gson.toJson(userInfo)+"}");
//			}else if("listMsgNoRead".equals(type)){
//				List<TodayWorks> newList = new ArrayList<TodayWorks>();
//				//未读消息
//				//List<TodayWorks> list = msgShareService.listPagedMsgNoRead(todayWorks,userInfo.getComId(),userInfo.getId());
//				List<TodayWorks> list = msgShareService.listMsgNoRead(new TodayWorks(),userInfo.getComId(),userInfo.getId(),null);
//				for(int i=0;i<list.size();i++){
//					if("015".equals(list.get(i).getBusType())){
//						list.get(i).setModuleTypeName("加入申请");
//					}else if("1".equals(list.get(i).getBusType())){
//						list.get(i).setModuleTypeName("分享");
//					}else if("0".equals(list.get(i).getBusType())){
//						list.get(i).setModuleTypeName("普通闹铃");
//					}else{
//						list.get(i).setModuleTypeName(DataDicContext.getInstance().getCurrentPathZvalue("moduleType",list.get(i).getBusType()).substring(0,2));
//					}
//					newList.add(list.get(i));
//				}
//				infoJson.append("{succ:'yes',data:"+gson.toJson(list)+",user:"+gson.toJson(userInfo)+"}");
//			}else{
//				infoJson.append("{info:'研发中，慢慢期待！',succ:'no'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		
//		return infoJson.toString();
//	}
//	/**
//	 * 获取数据详情
//	 * @param request
//	 * @param busType 业务类型
//	 * @param busId 业务主键
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/viewDateById")
//	public String viewDateById(HttpServletRequest request,String busType,Integer busId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null == busType || "".equals(busType.trim()) || null==busId || 0==busId){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			if(ConstantInterface.TYPE_TASK.equals(busType)){
//				//任务查看
//				infoJson.append(this.taskDta(request, busId));
//			}else if(ConstantInterface.TYPE_ITEM.equals(busType)){
//				//项目查看
//				infoJson.append(this.itemOfJson(request, busId));
//			}else if(ConstantInterface.TYPE_CRM.equals(busType)){
//				//客户查看
//				infoJson.append(this.crmOfJson(request, busId));
//			}else if(ConstantInterface.TYPE_WEEK.equals(busType)){
//				//周报查看
//				infoJson.append(this.weeklyRepOfJson(request, busId));
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		
//		return infoJson.toString();
//	}
//	/***************************************************周报模块********************************************************/
//	/**
//	 * 获取周报的JSON数据
//	 * @param request
//	 * @param weeklyRepId 周报主键
//	 * @return
//	 * @throws ParseException 
//	 */
//	private String weeklyRepOfJson(HttpServletRequest request,Integer weeklyRepId) throws ParseException{
//		StringBuffer infoJson = new StringBuffer();
//		UserInfo userInfo = this.getSessionUser();
//		if(null==userInfo || null==weeklyRepId || 0==weeklyRepId){
//			return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//		}
//		//JSON对象转换
//		Gson gson = new Gson();
//
//		//验证当前登录人是否是督察人员
//		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
//		//客户查看权限验证
//		//验证查看人权限
//		if((weekReportService.authorCheck(userInfo.getComId(),weeklyRepId,userInfo.getId()) 
//				|| isForceIn)){//有查看权限或是监督人员
//			WeekReportPojo weekReportParam = new WeekReportPojo();
//			weekReportParam.setViewType(1);
//			//取得所选周报
//			WeekReport weekReport = weekReportService.getWeekReportForView(weeklyRepId,userInfo,weekReportParam,isForceIn);
//			infoJson.append("{succ:'yes',data:"+gson.toJson(weekReport)+",user:"+gson.toJson(userInfo)+"}");
//			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK, weeklyRepId);
//			//取得是否添加浏览记录
//			boolean bool = FreshManager.checkOpt(request, viewRecord);
//			if(bool){
//				//添加查看记录
//				userInfoService.addViewRecord(userInfo,viewRecord);
//			}
//		}else{
//			infoJson.append("{info:'抱歉，你没有查看权限！',succ:'no'}");
//		}
//		//查看周报，删除消息提醒
//		msgShareService.updateTodoWorkRead(weeklyRepId,userInfo.getComId(),userInfo.getId(),ConstantInterface.TYPE_WEEK,0);
//		return infoJson.toString();
//	}
//
//	/**
//	 * 获取周报属性JSON数据
//	 * @param repId 周报主键
//	 * @param pageNum 页码
//	 * @param dataType 属性类别
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/weeklyRepAttrOfJson")
//	public String weeklyRepAttrOfJson(Integer repId,Integer pageNum,String dataType,String sid){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==repId || 0==repId || null ==dataType || "".equals(dataType.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//			//一次加载行数
//			PaginationContext.setPageSize(6);
//			//列表数据起始索引位置
//			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			//JSON对象转换
//			Gson gson = new Gson();
//			if("talk".equals(dataType)){
//				//反馈留言
//				List<WeekRepTalk> weekTalks = weekReportService.listPagedWeekRepTalk(repId,userInfo.getComId(),"app");
//				infoJson.append("{succ:'yes',data:"+gson.toJson(weekTalks)+"}");
//			}else if("doc".equals(dataType)){
//				//周报附件
//				List<WeekRepUpfiles> weekRepFiles = weekReportService.listPagedWeekRepFiles(userInfo.getComId(),repId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(weekRepFiles)+"}");
//			}else if("checkView".equals(dataType)){
//				//浏览的人员
//				List<ViewRecord> listViewRecord = userInfoService.listViewRecord(userInfo,ConstantInterface.TYPE_WEEK,repId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listViewRecord)+"}");
//			}else if("log".equals(dataType)){
//				//周报日志
//				List<WeekRepLog> weekRepLogs = weekReportService.listPagedWeekRepVoteLog(userInfo.getComId(),repId,sid);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(weekRepLogs)+"}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 周报留言添加
//	 * @param request
//	 * @param weekRepTalkStr 周报留言数据对象字符串
//	 * @param userInfoStr 操作人数据对象字符串
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/weeklyRepTalkReply")
//	public String weeklyRepTalkReply(HttpServletRequest request,String weekRepTalkStr,String userInfoStr){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==userInfoStr || "".equals(userInfoStr.trim()) || null==weekRepTalkStr || "".equals(weekRepTalkStr.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			//JSON对象转换
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			WeekRepTalk weekRepTalk = gson.fromJson(weekRepTalkStr, WeekRepTalk.class);
//			weekRepTalk.setComId(userInfo.getComId());
//			weekRepTalk.setTalker(userInfo.getId());
//			//附件
//			List<Integer> upfileIds = new ArrayList<Integer>();
//			if(null!=weekRepTalk.getFilesName() && weekRepTalk.getFilesName().length>0){
//				for(String upFileName:weekRepTalk.getFilesName()){
//					Integer fileId = this.uploadeFile(request, upFileName,userInfo.getComId());
//					if(fileId!=0)upfileIds.add(fileId);
//				}
//			}
//			//list转数组
//			Integer[] args = (Integer[])upfileIds.toArray(new Integer[upfileIds.size()]);
//			weekRepTalk.setUpfilesId(args);
//			//反馈留言回复
//			Integer talkId = weekReportService.addWeekRepTalk(weekRepTalk,userInfo);
//			//模块日志添加
//			if(-1==weekRepTalk.getParentId()){
//				weekReportService.addWeekRepLog(userInfo.getComId(), weekRepTalk.getWeekReportId(), userInfo.getId(), userInfo.getUserName(), "参与反馈留言");
//			}else{
//				weekReportService.addWeekRepLog(userInfo.getComId(), weekRepTalk.getWeekReportId(), userInfo.getId(), userInfo.getUserName(), "回复反馈留言");
//			}
//			infoJson.append("{succ:'yes',talkId:"+talkId+",pId:"+((null==weekRepTalk.getParentId() || 0==weekRepTalk.getParentId())?-1:weekRepTalk.getParentId())+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***
//	 * 获取汇报周报配置信息
//	 * @param request
//	 * @param chooseDate 需要汇报的周报日期
//	 * @return
//	 * @throws ParseException
//	 */
//	@ResponseBody
//	@RequestMapping(value="/weeklyRepConfigOfJson")
//	private String weeklyRepConfigOfJson(HttpServletRequest request,String chooseDate) throws ParseException{
//		StringBuffer infoJson = new StringBuffer();
//		UserInfo userInfo = this.getSessionUser();
//		if(null==userInfo){
//			return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//		}
//		//JSON对象转换
//		Gson gson = new Gson();
//		//选中的时间
//		String tempNowDate = chooseDate;
//		//当前时间
//		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_);
//		//当前时间的第一周
//		String nowWeekDay = DateTimeUtil.getFirstDayOfWeek(nowDate, DateTimeUtil.c_yyyy_MM_dd_);
//		boolean flag = false;//当前时间是本周第一天,默认不是
//		if(nowDate.equals(nowWeekDay)){//当前时间是本周第一天，则减一天
//			nowDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -1);
//			flag = true;
//		}
//		if(null==tempNowDate || "".equals(tempNowDate)){
//			//默认为当前时间
//			tempNowDate = nowDate;
//		}else if(tempNowDate.equals(nowWeekDay) && flag){//选中时间为当前日期所在周的第一天，则减一天
//			//填写周报默认减一天
//			tempNowDate = DateTimeUtil.addDate(tempNowDate, DateTimeUtil.c_yyyy_MM_dd_, Calendar.DAY_OF_MONTH, -1);
//		}
//		//日期所在周数
//		Integer weekNum = DateTimeUtil.getWeekOfYear(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
//		//所选日期所在周的第一天
//		String weekS = DateTimeUtil.getFirstDayOfWeek(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
//		//所选日期所在周的最后天
//		String weekE = DateTimeUtil.getLastDayOfWeek(tempNowDate,DateTimeUtil.c_yyyy_MM_dd_);
//		
//		String weeksYear = weekS.substring(0, 4);
//		String weekEYear = weekE.substring(0, 4);
//		String weekYear = weeksYear;
//		if(!weeksYear.equals(weekEYear)){
//			weekYear = weekEYear;
//		}
//		//取得所选日期所写周报
//		WeekReport weekReport = weekReportService.initWeekReport(weekNum,userInfo,weekYear,weekS,weekE);
//		weekReport.setWeekS(chooseDate);
//		weekReport.setUserName(userInfo.getUserName());
//		if(null!=weekReport &&weekReport.getCountVal()>0 && 
//				"0".equals(weekReport.getState())){//周报已经发布了
//			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_WEEK, weekReport.getId());
//			//取得是否添加浏览记录
//			boolean bool = FreshManager.checkOpt(request, viewRecord);
//			if(bool){
//				//添加查看记录
//				userInfoService.addViewRecord(userInfo,viewRecord);
//			}
//		}
//		infoJson.append("{succ:'yes',data:"+gson.toJson(weekReport)+",user:"+gson.toJson(userInfo)+"}");
//		return infoJson.toString();
//	}
//	/**
//	 * 周报发布
//	 * @param weeklyRepId
//	 * @param weekReportStr
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/weeklyRepAdd")
//	public String weeklyRepAdd(Integer weeklyRepId,String weekReportStr){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				WeekReport weekReport = gson.fromJson(weekReportStr, WeekReport.class);
//				//发布状态
//				weekReport.setState("0");
//				//发布周报
//				weekReportService.addWeekReport(weekReport,userInfo,new MsgShare());
//				//周报日志
//				weekReportService.addWeekRepLog(userInfo.getComId(), weekReport.getId(), userInfo.getId(), userInfo.getUserName(),"移动端发布周报。");
//				infoJson.append("{info:'周报发布成功！',succ:'yes'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***************************************************客户模块********************************************************/
//	/**
//	 * 获取客户的JSON数据
//	 * @param request
//	 * @param crmId 客户主键
//	 * @return
//	 */
//	private String crmOfJson(HttpServletRequest request,Integer crmId){
//		StringBuffer infoJson = new StringBuffer();
//		UserInfo userInfo = this.getSessionUser();
//		//JSON对象转换
//		Gson gson = new Gson();
//		//客户查看权限验证
//		//验证当前登录人是否是督察人员
//		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
//		//验证查看人权限
//		if(crmService.authorCheck(userInfo.getComId(),crmId,userInfo.getId())
//				|| isForceIn){//有查看权限或是监督人员
//			Customer customer = crmService.queryCustomer(userInfo,crmId);
//			infoJson.append("{succ:'yes',data:"+gson.toJson(customer)+",user:"+gson.toJson(userInfo)+"}");
//			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_CRM,crmId);
//			//取得是否添加浏览记录
//			boolean bool = FreshManager.checkOpt(request, viewRecord);
//			if(bool){
//				//添加查看记录
//				userInfoService.addViewRecord(userInfo,viewRecord);
//			}
//		}else{
//			infoJson.append("{info:'抱歉，你没有查看权限！',succ:'no'}");
//		}
//		//查看客户信息，删除客户提醒
//		msgShareService.updateTodoWorkRead(crmId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_CRM,0);
//		return infoJson.toString();
//	}
//	/**
//	 * 获取客户属性JSON数据
//	 * @param crmId 客户主键
//	 * @param pageNum 页码
//	 * @param dataType 属性类别
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmAttrOfJson")
//	public String crmAttrOfJson(Integer crmId,Integer pageNum,String dataType){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==crmId || 0==crmId || null ==dataType || "".equals(dataType.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//			//一次加载行数
//			PaginationContext.setPageSize(6);
//			//列表数据起始索引位置
//			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			//JSON对象转换
//			Gson gson = new Gson();
//			if("talk".equals(dataType)){
//				//获取客户维护记录
//				List<FeedBackInfo> listFeedBackInfo = crmService.listFeedBackInfo(userInfo.getComId(),crmId,"app");
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listFeedBackInfo)+"}");
//			}else if("docCrm".equals(dataType)){
//				//客户文档查看
//				List<CustomerUpfile> listCustomerUpfile = crmService.listPagedCustomerUpfile(userInfo.getComId(),crmId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomerUpfile)+"}");
//			}else if("flowRecord".equals(dataType)){
//				//移交记录
//				List<FlowRecord> listFlowRecord = new ArrayList<FlowRecord>();
//				listFlowRecord = crmService.listFlowRecord(crmId, userInfo.getComId());
//				//客户详情
//				Customer crm = null;
//				if(null==listFlowRecord || listFlowRecord.size()==0){
//					crm=crmService.getCrmById(userInfo,crmId);
//					
//					FlowRecord flowRecord = new FlowRecord();
//					flowRecord.setAcceptDate(crm.getRecordCreateTime());
//					flowRecord.setUserName(crm.getOwnerName());
//					flowRecord.setUuid(crm.getUuid());
//					flowRecord.setGender(crm.getGender());
//					listFlowRecord.add(flowRecord);
//				}
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listFlowRecord)+"}");
//			}else if("checkViewCrm".equals(dataType)){
//				//客户浏览记录
//				List<ViewRecord> listViewRecord = userInfoService.listViewRecord(userInfo,ConstantInterface.TYPE_CRM,crmId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listViewRecord)+"}");
//			}else if("logCrm".equals(dataType)){
//				//客户日志
//				List<CustomerLog> listCustomerLog = crmService.listCustomerLog(userInfo.getComId(),crmId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomerLog)+"}");
//			}else if("crmTask".equals(dataType)){
//				//客户任务
//				//List<Task> listTaskOfItem = itemService.listTaskOfItem(userInfo.getComId(),crmId);
//				//infoJson.append("{succ:'yes',data:"+gson.toJson(listTaskOfItem)+"}");
//				infoJson.append("{succ:'yes',data:[]}");
//			}else if("linkMan".equals(dataType)){
//				//客户联系人
//				List<LinkMan> listLinkMan= crmService.listCrmLinkMan(userInfo.getComId(), crmId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listLinkMan)+"}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 客户移交
//	 * @param crmId 客户主键
//	 * @param toUser 接收人
//	 * @param replayContent 移交说明
//	 * @param feedBackTypeId 反馈类型主键
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmHandOver")
//	public String crmHandOver(Integer crmId,Integer toUser,String replayContent,Integer feedBackTypeId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==crmId || 0==crmId || null==toUser || 0==toUser){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			CustomerHandOver customerHandOver = new CustomerHandOver();
//			customerHandOver.setFromUser(userInfo.getId());
//			customerHandOver.setComId(userInfo.getComId());
//			customerHandOver.setToUser(toUser);
//			customerHandOver.setCustomerId(crmId);
//			customerHandOver.setReplayContent(replayContent);
//			if(null!=feedBackTypeId && 0!=feedBackTypeId && !"".equals(feedBackTypeId.toString().trim())){
//				customerHandOver.setFeedBackTypeId(feedBackTypeId);
//			}
//			
//			Customer customer = crmService.getCrmById(crmId);
//			if(customer.getOwner().equals(customerHandOver.getToUser())){
//				infoJson.append("{info:'移交失败，不能移交给自己!',succ:'no'}");
//			}else{
//				boolean succ = crmService.addCustomerHandOver(new CustomerHandOver[]{customerHandOver}, userInfo,null);
//				if(succ){
//					infoJson.append("{info:'移交成功！',succ:'yes'}");
//				}else{
//					infoJson.append("{info:'客户移交失败!',succ:'no'}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 客户反馈记录添加
//	 * @param request
//	 * @param feedBackInfoStr 客户反馈对象字符串
//	 * @param userInfoStr 操作人对象字符串
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmTalkReply")
//	public String crmTalkReply(HttpServletRequest request,String feedBackInfoStr,String userInfoStr){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==userInfoStr || "".equals(userInfoStr.trim()) || null==feedBackInfoStr || "".equals(feedBackInfoStr.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			FeedBackInfo info = gson.fromJson(feedBackInfoStr, FeedBackInfo.class);
//			info.setComId(userInfo.getComId());
//			info.setUserId(userInfo.getId());
//			List<Integer> upfileIds = new ArrayList<Integer>();
//			if(null!=info.getFilesName() && info.getFilesName().length>0){
//				for(String upFileName:info.getFilesName()){
//					Integer fileId = this.uploadeFile(request, upFileName,userInfo.getComId());
//					if(fileId!=0)upfileIds.add(fileId);
//				}
//			}
//			//list转数组
//			Integer[] args = (Integer[])upfileIds.toArray(new Integer[upfileIds.size()]);
//			info.setUpfilesId(args);
//			//存储维护信息
//			Integer talkId = crmService.addFeedBackInfo(info,null,userInfo);
//			infoJson.append("{succ:'yes',talkId:"+talkId+",pId:"+((null==info.getParentId() || "".equals(info.getParentId()))?-1:info.getParentId())+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 客户添加信息初始化
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmAddConfigJson")
//	public String crmAddConfigJson(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				//获取客户区域数据源
//				List<Area> listArea = crmService.listArea(userInfo.getComId());
//				//获取客户类型数据源
//				List<CustomerType> listCustomerType = crmService.listCustomerType(userInfo.getComId());
//				infoJson.append("{succ:'yes',user:"+gson.toJson(userInfo)+",area:"+gson.toJson(listArea.get(0))+",customerType:"+gson.toJson(listCustomerType.get(0))+"}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取反馈类型JSON
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfFeedBackType")
//	public String jsonOfFeedBackType(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			//JSON对象转换
//			Gson gson = new Gson();
//			//获取反馈类型集合
//			List<FeedBackType> listFeedBackType = crmService.listFeedBackType(userInfo.getComId());
//			infoJson.append("{succ:'yes',data:"+gson.toJson(listFeedBackType)+",user:"+gson.toJson(userInfo)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取客户类型JSON
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfCrmType")
//	public String jsonOfCrmType(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			//JSON对象转换
//			Gson gson = new Gson();
//			//获取客户类型数据源
//			List<CustomerType> listCustomerType = crmService.listCustomerType(userInfo.getComId());
//			infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomerType)+",user:"+gson.toJson(userInfo)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取客户区域JSON
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfArea")
//	public String jsonOfArea(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			//JSON对象转换
//			Gson gson = new Gson();
//			//获取客户区域数据源
//			List<Area> listArea = crmService.listArea(userInfo.getComId());
//			infoJson.append("{succ:'yes',data:"+gson.toJson(listArea)+",user:"+gson.toJson(userInfo)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取客户反馈记录JSON数据
//	 * @param talkId
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmTalkOfJson")
//	public String crmTalkOfJson(Integer talkId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==talkId || 0==talkId){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			//JSON对象转换
//			Gson gson = new Gson();
//			//获取维护记录信息
//			FeedBackInfo feedBackInfo = crmService.queryFeedBackInfo(userInfo.getComId(), talkId);
//			infoJson.append("{succ:'yes',data:"+gson.toJson(feedBackInfo)+",user:"+gson.toJson(userInfo)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 客户属性更新
//	 * @param crmId 客户主键
//	 * @param attrType 属性类型
//	 * @param attrVal 属性值
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmAttrUpdate")
//	public String crmAttrUpdate(Integer crmId,String attrType,String attrVal){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==crmId || "".equals(crmId) || null==attrType || "".equals(attrType)|| null==attrVal || "".equals(attrVal)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Customer customer = new Customer();
//			//企业号
//			customer.setComId(userInfo.getComId());
//			//主键
//			customer.setId(crmId);
//			if("customerName".equals(attrType)){
//				//客户名称更新
//				customer.setCustomerName(attrVal);
//				crmService.updateCustomerName(customer,userInfo);
//			}else if("customerRemark".equals(attrType)){
//				//客户备注更新
//				customer.setCustomerRemark(attrVal);
//				crmService.updateCustomerRemark(customer,userInfo);
//			}else if("areaName".equals(attrType)){
//				//区域更新
//				Integer areaId = Integer.parseInt(attrVal);
//				customer.setAreaId(areaId);
//				Area area = crmService.getAreaById(areaId);
//				customer.setAreaName(area.getAreaName());
//				crmService.updateCustomerAreaId(customer,userInfo);
//			}else if("typeName".equals(attrType)){
//				//类型更新
//				Integer typeId = Integer.parseInt(attrVal);
//				CustomerType customerType = crmService.queryCustomerType(userInfo.getComId(),typeId);
//				customer.setTypeName(customerType.getTypeName());
//				customer.setCustomerTypeId(typeId);
//				crmService.updateCustomerTypeId(customer,userInfo);
//			}else if("linePhone".equals(attrType)){
//				//联系电话更新
//				customer.setLinePhone(attrVal);
//				crmService.updateCustomerLinePhone(customer,userInfo);
//			}else if("fax".equals(attrType)){
//				//传真更新
//				customer.setFax(attrVal);
//				crmService.updateCustomerFax(customer,userInfo);
//			}else if("postCode".equals(attrType)){
//				//邮编更新
//				customer.setPostCode(Integer.parseInt(attrVal));
//				crmService.updateCustomerPostCode(customer,userInfo);
//			}else if("address".equals(attrType)){
//				//联系地址更新
//				customer.setAddress(attrVal);
//				crmService.updateCustomerAddress(customer,userInfo);
//			}
//			infoJson.append("{succ:'yes',info:'更新成功！'}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 客户新增
//	 * @param request
//	 * @param customerStr 客户数据对象字符串
//	 * @param userInfoStr 操作者数据对象字符串
//	 * @param addType 添加类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmAdd")
//	public String crmAdd(HttpServletRequest request,String customerStr,String userInfoStr,String addType){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==userInfoStr || "".equals(userInfoStr.trim()) || null==customerStr || "".equals(customerStr.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			Customer customer = gson.fromJson(customerStr, Customer.class);
//			//企业号
//			customer.setComId(userInfo.getComId());
//			//创建人
//			customer.setCreator(userInfo.getId());
//			//删除标识(正常)
//			customer.setDelState(0);
//			//附件
//			List<CustomerUpfile> listCustomerUpfile = new ArrayList<CustomerUpfile>();
//			if(null!=customer.getFilesName() && customer.getFilesName().length>0){
//				CustomerUpfile customerUpfile = null;
//				for(String upFileName:customer.getFilesName()){
//					Integer fileId = this.uploadeFile(request, upFileName,userInfo.getComId());
//					customerUpfile= new CustomerUpfile();
//					if(fileId!=0){
//						customerUpfile.setUpfileId(fileId);
//						listCustomerUpfile.add(customerUpfile);
//					}
//				}
//			}
//			customer.setListUpfiles(listCustomerUpfile);
//			//添加客户并保存
//			Integer crmId = crmService.addCustomer(customer, null,userInfo);
//			if(null !=addType && "perfect".equals(addType)){
//				//获取客户详情
//				customer = crmService.queryCustomer(userInfo,crmId);
//				infoJson.append("{succ:'yes',addType:'"+addType+"',info:'发布成功！',data:"+gson.toJson(customer)+"}");
//			}else{
//				infoJson.append("{succ:'yes',addType:'"+addType+"',info:'发布成功！'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***
//	 * 获取客户联系人JSON数据
//	 * @param crmId 客户主键
//	 * @param linkManId 联系人主键
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmLinkManOfJson")
//	public String crmLinkManOfJson(Integer crmId,Integer linkManId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==crmId || 0==crmId || null==linkManId || 0==linkManId){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Customer customer = crmService.queryCustomer(userInfo, crmId);
//			LinkMan linkMan = crmService.queryLinkMan(userInfo.getComId(),crmId,linkManId);
//			Gson gson = new Gson();
//			infoJson.append("{succ:'yes',data:"+gson.toJson(linkMan)+",user:"+gson.toJson(userInfo)+",owner:"+customer.getOwner()+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 客户联系人更新
//	 * @param crmId 客户主键
//	 * @param linkManId 联系人主键
//	 * @param attrType 更新属性类型
//	 * @param attrVal 更新属性值
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmLinkManUpdate")
//	public String crmLinkManUpdate(Integer crmId,Integer linkManId,String attrType,String attrVal){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==crmId || 0==crmId || null==linkManId || 0==linkManId || null==attrType || "".equals(attrType)|| null==attrVal || "".equals(attrVal)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			LinkMan linkMan = new LinkMan();
//			//企业号
//			linkMan.setComId(userInfo.getComId());
//			//主键
//			linkMan.setId(linkManId);
//			//客户主键
//			linkMan.setCustomerId(crmId);
//			if("linkManName".equals(attrType)){
//				//联系人姓名更新
//				linkMan.setLinkManName(attrVal);
//				crmService.updateLinkManName(linkMan, userInfo);
//			}else if("post".equals(attrType)){
//				//职务更新
//				linkMan.setPost(attrVal);
//				crmService.updateLinkManPost(linkMan, userInfo);
//			}else if("movePhone".equals(attrType)){
//				//移动电话更新
//				linkMan.setMovePhone(attrVal);
//				crmService.updateLinkManMovePhone(linkMan, userInfo);
//			}else if("email".equals(attrType)){
//				//电子邮件更新
//				linkMan.setEmail(attrVal);
//				crmService.updateLinkManEmail(linkMan, userInfo);
//			}else if("wechat".equals(attrType)){
//				//微信更新
//				linkMan.setWechat(attrVal);
//				crmService.updateLinkManWechat(linkMan, userInfo);
//			}else if("qq".equals(attrType)){
//				//qq更新
//				linkMan.setQq(attrVal);
//				crmService.updateLinkManQq(linkMan, userInfo);
//			}else if("linePhone".equals(attrType)){
//				//座机更新
//				linkMan.setLinePhone(attrVal);
//				crmService.updateLinkManLinePhone(linkMan, userInfo);
//			}
//			infoJson.append("{succ:'yes',info:'更新成功！'}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 新增客户联系人
//	 * @param linkManStr
//	 * @param addType
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/crmLinkManAdd")
//	public String crmLinkManAdd(String linkManStr,String addType){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==linkManStr || "".equals(linkManStr)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Gson gson = new Gson();
//			LinkMan linkMan = gson.fromJson(linkManStr,LinkMan.class);
//			//企业号
//			linkMan.setComId(userInfo.getComId());
//			//添加客户联系人
//			Integer linkManId = crmService.addLinkMan(linkMan, userInfo);
//			//获取客户联系人详情
//			linkMan = crmService.queryLinkMan(userInfo.getComId(),linkMan.getCustomerId(),linkManId);
//			infoJson.append("{succ:'yes',addType:'"+addType+"',info:'发布成功！',data:"+gson.toJson(linkMan)+",user:"+gson.toJson(userInfo)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	
//	/***************************************************项目模块********************************************************/
//	/**
//	 * 获取项目JSON数据
//	 * @param request
//	 * @param itemId
//	 * @return
//	 */
//	private String itemOfJson(HttpServletRequest request,Integer itemId){
//		StringBuffer infoJson = new StringBuffer();
//		UserInfo userInfo = this.getSessionUser();
//		//JSON对象转换
//		Gson gson = new Gson();
//		//验证当前登录人是否是督察人员
//		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
//		//验证查看人权限
//		if(itemService.authorCheck(userInfo.getComId(),itemId,userInfo.getId()) || isForceIn){
//			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEM, itemId);
//			//取得是否添加浏览记录
//			boolean bool = FreshManager.checkOpt(request, viewRecord);
//			if(bool){
//				//添加查看记录
//				userInfoService.addViewRecord(userInfo,viewRecord);
//			}
//			Item item = itemService.getItem(itemId,userInfo);
//			//汇报进度具体化
//			item.setItemProgressDescribe("0%");
//			List<DataDic> listTreeDataDic=DataDicContext.getInstance().listTreeDataDicByType("progress");
//			if(null!=listTreeDataDic && !listTreeDataDic.isEmpty()){
//				for(DataDic vo:listTreeDataDic){
//					if(null !=vo.getCode() && null!=item.getItemProgress() && vo.getCode().equals(item.getItemProgress().toString())){
//						item.setItemProgressDescribe(vo.getZvalue());
//						break;
//					}
//				}
//			}
//			infoJson.append("{succ:'yes',data:"+gson.toJson(item)+",user:"+gson.toJson(userInfo)+"}");
//		}else{
//			infoJson.append("{info:'抱歉，你没有查看权限！',succ:'no'}");
//		}
//		//查看项目，删出消息提醒
//		msgShareService.updateTodoWorkRead(itemId, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_ITEM,0);
//		return infoJson.toString();
//	}
//	/**
//	 * 项目属性JSON获取
//	 * @param itemId 项目主键
//	 * @param pageNum 分页页码
//	 * @param dataType 属性类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfItemAttr")
//	public String itemAttrOfJson(Integer itemId,Integer pageNum,String dataType){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==itemId || 0==itemId || null ==dataType || "".equals(dataType.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//			//一次加载行数
//			PaginationContext.setPageSize(6);
//			//列表数据起始索引位置
//			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			//JSON对象转换
//			Gson gson = new Gson();
//			if("talk".equals(dataType)){
//				//获取项目留言集合
//				List<ItemTalk> listItemTalk = itemService.listItemTalk(itemId, userInfo.getComId(),"app");
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listItemTalk)+"}");
//			}else if("sonItem".equals(dataType)){
//				//获取子项目集合
//				List<Item> listSonItem = itemService.listSonItem(itemId,userInfo.getComId());
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listSonItem)+"}");
//			}else if("docItem".equals(dataType)){
//				//项目文档查看
//				List<ItemUpfile> listItemUpfile = itemService.listPagedItemUpfile(itemId, userInfo.getComId());
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listItemUpfile)+"}");
//			}else if("flowRecord".equals(dataType)){
//				//项目移交日志
//				//移交记录
//				List<FlowRecord> listFlowRecord = new ArrayList<FlowRecord>();
//				listFlowRecord = itemService.listFlowRecord(itemId, userInfo.getComId());
//				//项目详情状态
//				Item item = null;
//				if(null==listFlowRecord || listFlowRecord.size()==0){
//					item=itemService.getItemById(itemId, userInfo);
//
//					FlowRecord flowRecord = new FlowRecord();
//					flowRecord.setAcceptDate(item.getRecordCreateTime());
//					flowRecord.setUserName(item.getOwnerName());
//					flowRecord.setUuid(item.getUuid());
//					flowRecord.setGender(item.getGender());
//					listFlowRecord.add(flowRecord);
//				}else{
//					item = itemService.getItemById(itemId);
//				}
//				
//				//项目办结
//				if(item.getState()==4){
//					FlowRecord flowRecord = listFlowRecord.remove(0);
//					flowRecord.setState(ConstantInterface.FINISHED_YES);
//					listFlowRecord.add(0, flowRecord);
//				}
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listFlowRecord)+"}");
//			}else if("checkViewItem".equals(dataType)){
//				//项目浏览记录
//				List<ViewRecord> listViewRecord = userInfoService.listViewRecord(userInfo,ConstantInterface.TYPE_ITEM,itemId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listViewRecord)+"}");
//			}else if("logItem".equals(dataType)){
//				//项目日志
//				List<ItemLog> listItemLog = itemService.listItemLog(itemId, userInfo.getComId());
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listItemLog)+"}");
//			}else if("itemTask".equals(dataType)){
//				//项目任务
//				List<Task> listTaskOfItem = itemService.listTaskOfItem(userInfo.getComId(),itemId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listTaskOfItem)+"}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 项目移交
//	 * @param itemId 项目主键
//	 * @param fromUser 项目移交人
//	 * @param toUser 移交对象
//	 * @param replayContent 移交说明
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/itemHandOver")
//	public String itemHandOver(Integer itemId,Integer toUser,String replayContent){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==itemId || 0==itemId || null==toUser || 0==toUser){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Item item = itemService.getItemById(itemId,userInfo);
//			ItemHandOver itemHandOver = new ItemHandOver();
//			itemHandOver.setComId(userInfo.getComId());
//			itemHandOver.setItemId(itemId);
//			itemHandOver.setToUser(toUser);
//			itemHandOver.setReplayContent(replayContent);
//			itemHandOver.setFromUser(item.getOwner());
//			if(item.getOwner().equals(toUser)){
//				infoJson.append("{info:'移交失败，不能移交给自己!',succ:'no'}");
//			}else{
//				boolean succ = itemService.addItemHandOver(new ItemHandOver[]{itemHandOver},userInfo,null);
//				if(succ){
//					infoJson.append("{info:'移交成功！',succ:'yes'}");
//				}else{
//					infoJson.append("{info:'客户移交失败!',succ:'no'}");
//				}
//			}
//			infoJson.append("{info:'移交成功！',succ:'yes'}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 项目留言
//	 * @param request
//	 * @param itemTalkStr 留言对象数据字符串
//	 * @param userInfoStr 操作人数据对象字符串
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/replyItemTalk")
//	public String itemTalkReply(HttpServletRequest request,String itemTalkStr,String userInfoStr){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==itemTalkStr || "".equals(itemTalkStr.trim())|| null==userInfoStr || "".equals(userInfoStr.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			ItemTalk itemTalk = gson.fromJson(itemTalkStr,ItemTalk.class);
//			itemTalk.setComId(userInfo.getComId());
//			itemTalk.setUserId(userInfo.getId());
//			//附件
//			List<Integer> upfileIds = new ArrayList<Integer>();
//			if(null!=itemTalk.getFilesName() && itemTalk.getFilesName().length>0){
//				for(String upFileName:itemTalk.getFilesName()){
//					Integer fileId = this.uploadeFile(request, upFileName,userInfo.getComId());
//					if(fileId!=0)upfileIds.add(fileId);
//				}
//			}
//			//list转数组
//			Integer[] args = (Integer[])upfileIds.toArray(new Integer[upfileIds.size()]);
//			itemTalk.setUpfilesId(args);
//			Integer talkId = itemService.addItemTalk(itemTalk,userInfo);
//			//模块日志添加
//			itemService.addItemLog(userInfo.getComId(),itemTalk.getItemId(), userInfo.getId(), userInfo.getUserName(), "添加新评论");
//			infoJson.append("{succ:'yes',talkId:"+talkId+",pId:"+((null==itemTalk.getParentId() || "".equals(itemTalk.getParentId()))?-1:itemTalk.getParentId())+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取项目留言对象
//	 * @param talkId 留言主键
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfItemTalk")
//	public String itemTalkOfJson(Integer talkId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==talkId || "".equals(talkId)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			ItemTalk itemTalk = itemService.queryItemTalk(talkId,userInfo.getComId());
//			Gson gson = new Gson();
//			infoJson.append("{succ:'yes',data:"+gson.toJson(itemTalk)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 项目状态更新
//	 * @param itemId 项目主键
//	 * @param state 项目状态标识
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/itemStateUpdat")
//	public String itemStateUpdat(Integer itemId,Integer state){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==itemId || "".equals(itemId) || null==state || "".equals(state)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Item item = new Item();
//			item.setComId(userInfo.getComId());
//			item.setId(itemId);
//			item.setState(state);
//			String stateDis =null;
//			if(4==item.getState()){
//				stateDis ="完成";
//			}else if(3==item.getState()){
//				stateDis ="挂起";
//			}else if(1==item.getState()){
//				stateDis ="执行";
//			}
//			//默认操作成功
//			boolean succ = true;
//			if(4==item.getState()){//标记完成
//				succ = itemService.updateDoneItem(item,userInfo,stateDis);
//			}else if(1==item.getState()){//重启
//				succ = itemService.updateRestarItem(item,userInfo,stateDis);
//			}
//			if(succ){
//				//模块日志添加
//				itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目标记为\""+stateDis+"\"");
//				infoJson.append("{info:'标记成功！',succ:'yes'}");
//			}else{
//				//模块日志添加
//				itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目标记为\""+stateDis+"\"失败");
//				infoJson.append("{info:'标记失败！',succ:'no'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 添加项目
//	 * @param request
//	 * @param itemStr 项目对象数据字符串
//	 * @param userInfoStr 操作人对象数据字符串
//	 * @param addType 新增类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/itemAdd")
//	public String itemAdd(HttpServletRequest request,String itemStr,String userInfoStr,String addType){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==userInfoStr || "".equals(userInfoStr.trim()) || null==itemStr || "".equals(itemStr.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			Item item = gson.fromJson(itemStr, Item.class);
//			//企业号
//			item.setComId(userInfo.getComId());
//			//创建人
//			item.setCreator(userInfo.getId());
//			//删除标识(正常)
//			item.setDelState(0);
//			//项目状态标识
//			item.setState(1);
//			//附件
//			List<Upfiles> listUpfiles = new ArrayList<Upfiles>();
//			if(null!=item.getFilesName() && item.getFilesName().length>0){
//				Upfiles file = null;
//				for(String upFileName:item.getFilesName()){
//					Integer fileId = this.uploadeFile(request, upFileName,userInfo.getComId());
//					file= new Upfiles();
//					if(fileId!=0){
//						file.setId(fileId);
//						listUpfiles.add(file);
//					}
//				}
//			}
//			item.setListUpfiles(listUpfiles);
//			
//			Integer id = itemService.addItem(item,null,userInfo);
//			if(null !=addType && "perfect".equals(addType)){
//				//获取项目详情
//				item = itemService.getItemById(id, userInfo);
//				infoJson.append("{succ:'yes',addType:'"+addType+"',info:'发布成功！',data:"+gson.toJson(item)+"}");
//			}else{
//				infoJson.append("{succ:'yes',addType:'"+addType+"',info:'发布成功！'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 项目属性更新
//	 * @param itemId 项目主键
//	 * @param attrType 属性类型
//	 * @param attrVal 属性值
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/itemAttrUpdate")
//	public String itemAttrUpdate(Integer itemId,String attrType,String attrVal){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==itemId || "".equals(itemId) || null==attrType || "".equals(attrType)|| null==attrVal || "".equals(attrVal)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Item item = new Item();
//			//企业号
//			item.setComId(userInfo.getComId());
//			//主键
//			item.setId(itemId);
//			if("itemName".equals(attrType)){
//				item.setItemName(attrVal);
//				boolean succ = itemService.updateItemName(item,userInfo);
//				if(succ){
//					//模块日志添加
//					itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目名称变更为\""+item.getItemName()+"\"");
//					infoJson.append("{succ:'yes',info:'更新成功！'}");
//				}else{
//					//模块日志添加
//					itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目名称变更为\""+item.getItemName()+"\"失败");
//					infoJson.append("{succ:'no',info:'更新失败！'}");
//				}
//			}else if("itemRemark".equals(attrType)){
//				item.setItemRemark(attrVal);
//				boolean succ = itemService.updateItemItemRemark(item,userInfo);
//				if(succ){
//					//模块日志添加
//					itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目说明变更成功");
//					infoJson.append("{succ:'yes',info:'更新成功！'}");
//				}else{
//					//模块日志添加
//					itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "项目说明变更失败");
//					infoJson.append("{succ:'no',info:'更新失败！'}");
//				}
//			}else if("itemProgress".equals(attrType)){
//				item.setItemProgress(Integer.parseInt(attrVal));
//				String itemProgressDescribe = CommonUtil.dataDicZvalueByCode("progress",null!=item.getItemProgress()?item.getItemProgress().toString():null);
//				item.setItemProgressDescribe(itemProgressDescribe);
//				item.setItemProgressDescribe(itemProgressDescribe);
//				boolean succ = itemService.updateItemProgress(item,userInfo);
//				if(succ){
//					//模块日志添加
//					itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "汇报进度:\""+itemProgressDescribe+"\"");
//					infoJson.append("{succ:'yes',info:'更新成功！'}");
//				}else{
//					//模块日志添加
//					itemService.addItemLog(userInfo.getComId(),item.getId(), userInfo.getId(), userInfo.getUserName(), "汇报进度:\""+itemProgressDescribe+"\"失败");
//					infoJson.append("{succ:'no',info:'更新失败！'}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***************************************************任务模块********************************************************/
//	/**
//	 * 获取任务数据
//	 * @param request
//	 * @param busId
//	 * @return
//	 */
//	private String taskDta(HttpServletRequest request,Integer taskId){
//		StringBuffer infoJson = new StringBuffer();
//		UserInfo userInfo = this.getSessionUser();
//		//JSON对象转换
//		Gson gson = new Gson();
//		//验证当前登录人是否是督察人员
//		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
//		ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,taskId);
//		//取得是否添加浏览记录
//		boolean bool = FreshManager.checkOpt(request, viewRecord);
//		//客户查看权限验证
//		if(taskService.authorCheck(userInfo.getComId(),taskId,userInfo.getId()) || 
//				isForceIn){//有查看权限，监督人员
//			if(bool){
//				//添加查看记录
//				userInfoService.addViewRecord(userInfo,viewRecord);
//			}
//			Task task = taskService.getTask(taskId,userInfo);
////			//汇报进度具体化
////			task.setTaskProgressDis("0%");
////			List<DataDic> listTreeDataDic=DataDicContext.getInstance().listTreeDataDicByType("progress");
////			if(null!=listTreeDataDic && !listTreeDataDic.isEmpty()){
////				for(DataDic vo:listTreeDataDic){
////					if(null !=vo.getCode() && null!=task.getTaskProgress() && vo.getCode().equals(task.getTaskProgress().toString())){
////						task.setTaskProgressDis(vo.getZvalue());
////						break;
////					}
////				}
////			}
//			//初始化任务紧急度
//			String gradeName = DataDicContext.getInstance().getCurrentPathZvalue("grade",task.getGrade());
//			task.setGradeName(gradeName);
//			infoJson.append("{succ:'yes',data:"+gson.toJson(task)+",user:"+gson.toJson(userInfo)+"}");
//		}else{
//			infoJson.append("{info:'抱歉，你没有查看权限！',succ:'no'}");
//		}
//		//查看待办任务，删除消息提醒
//		msgShareService.updateTodoWorkRead(taskId,userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_TASK,0);
//		return infoJson.toString();
//	}
//	/**
//	 * 获取任务数据
//	 * @param taskId 任务主键
//	 * @param pageNum 加载次数
//	 * @param dataType 数据类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfTaskData")
//	public String jsonOfTaskData(Integer taskId,Integer pageNum,String dataType){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==taskId || 0==taskId || null ==dataType || "".equals(dataType.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//			//一次加载行数
//			PaginationContext.setPageSize(6);
//			//列表数据起始索引位置
//			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//			//JSON对象转换
//			Gson gson = new Gson();
//			if("talk".equals(dataType)){
//				//获取任务讨论数据
//				List<TaskTalk> listTaskTalk = taskService.listTaskTalk(taskId, userInfo.getComId());
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listTaskTalk)+"}");
//			}else if("sonTask".equals(dataType)){
//				//获取母任务下的所有子任务集合
//				List<Task> listSonTask = taskService.listSonTask(taskId,userInfo.getComId());
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listSonTask)+"}");
//			}else if("docTask".equals(dataType)){
//				//任务文档查看
//				List<TaskUpfile> listTaskUpfile = taskService.listPagedTaskUpfile(taskId, userInfo.getComId());
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listTaskUpfile)+"}");
//			}else if("cooperateRedTask".equals(dataType)){
//				//任务协作日志
//				List<FlowRecord> listFlowRecord = taskService.listFlowRecord(taskId, userInfo.getComId());
//				//任务详情状态
//				Task task = null;
//				if(null==listFlowRecord || listFlowRecord.size()==0){
//					task=taskService.getTaskById(taskId, userInfo);
//					
//					FlowRecord flowRecord = new FlowRecord();
//					flowRecord.setAcceptDate(task.getRecordCreateTime());
//					flowRecord.setUserName(task.getOwnerName());
//					flowRecord.setUuid(task.getUuid());
//					flowRecord.setGender(task.getGender());
//					listFlowRecord.add(flowRecord);
//				}else{
//					task = taskService.getTaskById(taskId);
//				}
//				//任务办结
//				if(4==task.getState()){
//					FlowRecord flowRecord = listFlowRecord.remove(0);
//					flowRecord.setState(ConstantInterface.FINISHED_YES);
//					listFlowRecord.add(0,flowRecord);
//				}
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listFlowRecord)+"}");
//			}else if("checkViewTask".equals(dataType)){
//				//任务浏览记录
//				List<ViewRecord> listViewRecord = userInfoService.listViewRecord(userInfo,ConstantInterface.TYPE_TASK,taskId);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listViewRecord)+"}");
//			}else if("logTask".equals(dataType)){
//				//任务日志
//				List<TaskLog> listTaskLog = taskService.listTaskLog(taskId, userInfo.getComId());
//				infoJson.append("{succ:'yes',data:"+gson.toJson(listTaskLog)+"}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取企业人员
//	 * @param type 人员列表分类
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfUsers")
//	public String jsonOfUsers(String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type.trim())){
//				infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//				return infoJson.toString();
//			}
//			Gson gson = new Gson();
//			if("userOfCom".equals(type)){
//				//列出常用的人员
//				List<UserInfo> list = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),10);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(list)+"}");
//			}else if("userOfGroup".equals(type)){
//				// 查询所有启用的组织结构树 默认查询所有已启用的机构
//				SelfGroup selfGroup = new SelfGroup();
//				selfGroup.setComId(userInfo.getComId());
//				selfGroup.setOwner(userInfo.getId());
//				List<SelfGroup> listGroup = selfGroupService.listUserGroup(selfGroup);
//				if(null!=listGroup){
//					List<SelfGroup> listGroupUser = new ArrayList<SelfGroup>();
//					List<UserInfo> listUser = null;
//					for(int i=0;i<listGroup.size();i++){
//						//初始化需要查询的私有组主键
//						userInfo.setGrpId(listGroup.get(i).getId());
//						// 查选自己私有组以及私有组下面成员
//						listUser = userInfoService.listUserOfGroup(userInfo);
//						listGroup.get(i).setListUser(listUser);
//						listGroupUser.add(listGroup.get(i));
//					}
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listGroupUser)+"}");
//				}
//			}else if("userOfDep".equals(type)){
//				// 查询所有启用的组织结构树 默认查询所有已启用的机构
//				Department dep = new Department();
//				dep.setComId(userInfo.getComId());
//				if(null != dep && null ==dep.getParentId()){
//					dep.setParentId(-1);
//				}
//				dep.setEnabled(ConstantInterface.ENABLED_YES.toString());
//				List<Department> listDep = departmentService.listTreeOrganization(dep);
//				if(null!=listDep){
//					List<Department> listDepUser = new ArrayList<Department>();
//					List<UserInfo> listUser = null;
//					for(int i=0;i<listDep.size();i++){
//						//初始化需要查询的私有组主键
//						userInfo.setDepId(listDep.get(i).getId());
//						// 查询所有启用的组织以及组织下面人员
//						listUser = userInfoService.listUserOfDep(userInfo);
//						listDep.get(i).setListUser(listUser);
//						listDepUser.add(listDep.get(i));
//					}
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listDepUser)+"}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		
//		return infoJson.toString();
//	}
//	/**
//	 * 移动APP人员信息访问
//	 * @param type 查看人员集合信息类型
//	 * @param comId 团队主键
//	 * @param userId 操作人主键
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfUsersByMobileOrg")
//	public String jsonOfUsersByMobileOrg(String type,Integer comId,Integer userId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==type || "".equals(type.trim()) || null==comId || comId==0 || null==userId || userId==0){
//				infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//				return infoJson.toString();
//			}
//			UserInfo userInfo = userInfoService.getUserInfo(comId,userId);
//			Gson gson = new Gson();
//			if("userOfCom".equals(type)){
//				//列出常用的人员
//				List<UserInfo> list = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),10);
//				infoJson.append("{succ:'yes',data:"+gson.toJson(list)+"}");
//			}else if("userOfGroup".equals(type)){
//				// 查询所有启用的组织结构树 默认查询所有已启用的机构
//				SelfGroup selfGroup = new SelfGroup();
//				selfGroup.setComId(userInfo.getComId());
//				selfGroup.setOwner(userInfo.getId());
//				List<SelfGroup> listGroup = selfGroupService.listUserGroup(selfGroup);
//				if(null!=listGroup){
//					List<SelfGroup> listGroupUser = new ArrayList<SelfGroup>();
//					List<UserInfo> listUser = null;
//					for(int i=0;i<listGroup.size();i++){
//						//初始化需要查询的私有组主键
//						userInfo.setGrpId(listGroup.get(i).getId());
//						// 查选自己私有组以及私有组下面成员
//						listUser = userInfoService.listUserOfGroup(userInfo);
//						listGroup.get(i).setListUser(listUser);
//						listGroupUser.add(listGroup.get(i));
//					}
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listGroupUser)+"}");
//				}
//			}else if("userOfDep".equals(type)){
//				// 查询所有启用的组织结构树 默认查询所有已启用的机构
//				Department dep = new Department();
//				dep.setComId(userInfo.getComId());
//				if(null != dep && null ==dep.getParentId()){
//					dep.setParentId(-1);
//				}
//				dep.setEnabled(ConstantInterface.ENABLED_YES.toString());
//				List<Department> listDep = departmentService.listTreeOrganization(dep);
//				if(null!=listDep){
//					List<Department> listDepUser = new ArrayList<Department>();
//					List<UserInfo> listUser = null;
//					for(int i=0;i<listDep.size();i++){
//						//初始化需要查询的私有组主键
//						userInfo.setDepId(listDep.get(i).getId());
//						// 查询所有启用的组织以及组织下面人员
//						listUser = userInfoService.listUserOfDep(userInfo);
//						listDep.get(i).setListUser(listUser);
//						listDepUser.add(listDep.get(i));
//					}
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listDepUser)+"}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		
//		return infoJson.toString();
//	}
//	/**
//	 * 只为获取登录用户信息
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonUserInfoOfMine")
//	public String jsonUserInfoOfMine(){
//		StringBuffer infoJson = new StringBuffer();
//		UserInfo userInfo = this.getSessionUser();
//		Gson gson = new Gson();
//		if (userInfo != null) {
//			infoJson.append("{succ:'yes',data:"+gson.toJson(userInfo)+"}");
//		} else {
//			infoJson.append("{info:'登入异常，请及时联系管理员！',succ:'no'}");
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 任务留言
//	 * @param taskId 任务主键
//	 * @param pId 留言父节点
//	 * @param msgContent 留言内容
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/replyTaskTalk")
//	public String replyTaskTalk(HttpServletRequest request,String taskTalkStr,String userInfoStr){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==userInfoStr || "".equals(userInfoStr.trim()) || null==taskTalkStr || "".equals(taskTalkStr.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			TaskTalk taskTalk = gson.fromJson(taskTalkStr, TaskTalk.class);
//			//企业主键
//			taskTalk.setComId(userInfo.getComId());
//			//留言人主键
//			taskTalk.setSpeaker(userInfo.getId());
//			//附件
//			List<Integer> upfileIds = new ArrayList<Integer>();
//			if(null!=taskTalk.getFilesName() && taskTalk.getFilesName().length>0){
//				for(String upFileName:taskTalk.getFilesName()){
//					Integer fileId = this.uploadeFile(request, upFileName,userInfo.getComId());
//					if(fileId!=0)upfileIds.add(fileId);
//				}
//			}
//			//list转数组
//			Integer[] args = (Integer[])upfileIds.toArray(new Integer[upfileIds.size()]);
//			taskTalk.setUpfilesId(args);
//			//添加留言
//			Integer talkId = taskService.addTaskTalk(taskTalk,userInfo);
//			//模块日志添加
//			taskService.addTaskLog(userInfo.getComId(),taskTalk.getTaskId(), userInfo.getId(), userInfo.getUserName(), "添加留言");
//			infoJson.append("{succ:'yes',talkId:"+talkId+",pId:"+((null==taskTalk.getParentId() || "".equals(taskTalk.getParentId()))?-1:taskTalk.getParentId())+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		
//		return infoJson.toString();
//	}
//	/**
//	 * 获取任务留言对象
//	 * @param talkId
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfTaskTalk")
//	public String jsonOfTaskTalk(Integer talkId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==talkId || "".equals(talkId)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			TaskTalk taskTalk = taskService.queryTaskTalk(talkId,userInfo.getComId());
//			Gson gson = new Gson();
//			infoJson.append("{succ:'yes',data:"+gson.toJson(taskTalk)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 根据主键获取人员信息
//	 * @param userId 查询ID
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonUserInfoById")
//	public String jsonUserInfoById(Integer userId){
//		StringBuffer infoJson = new StringBuffer();
//		UserInfo userInfo = this.getSessionUser();
//		Gson gson = new Gson();
//		if (userInfo != null) {
//			userInfo = userInfoService.getUserInfo(userInfo.getComId(),userId);
//			infoJson.append("{succ:'yes',data:"+gson.toJson(userInfo)+"}");
//		} else {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 根据条件获取用户信息
//	 * @param comId 团队主键
//	 * @param userId 用户主键
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonUserInfoByCon")
//	public String jsonUserInfoByCon(Integer comId,Integer userId) throws Exception{
//		StringBuffer infoJson = new StringBuffer();
//		if(null==comId || comId==0 || null==userId || userId==0){
//			return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//		}
//		Gson gson = new Gson();
//		UserInfo userInfo = null;
//		try {
//			userInfo = userInfoService.getUserInfo(comId,userId);
//			infoJson.append("{succ:'yes',data:"+gson.toJson(userInfo)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			throw e;
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 验证任务是否完成
//	 * @param taskId
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/checkTaskState")
//	public String checkTaskState(Integer taskId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==taskId || "".equals(taskId)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Task task = taskService.getTaskById(taskId);
//			if(null!=task && 4==task.getState()){
//				infoJson.append("{info:'任务已经结束！',succ:'no'}");
//			}else{
//				infoJson.append("{succ:'yes'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 任务协同
//	 * @param talkId 任务主键
//	 * @param dealTimeLimit 任务办理时限
//	 * @param cooperateExplain 委托说明
//	 * @param executor 下一步骤执行人                                                                   
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/taskCooperateConfig")
//	public String taskCooperateConfig(Integer taskId,String dealTimeLimit,String cooperateExplain,Integer executor){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==taskId || "".equals(taskId) || null==executor || "".equals(executor)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Task task = new Task();
//			task.setComId(userInfo.getComId());
//			task.setOperator(userInfo.getId());
//			task.setExecutor(executor);
//			task.setId(taskId);
//			task.setDealTimeLimit(dealTimeLimit);
//			task.setCooperateExplain(cooperateExplain);
//			UserInfo executorInfo =  userInfoService.getUserInfo(userInfo.getComId(),task.getExecutor());
//			task.setExecutorName(executorInfo.getUserName());
//			taskService.taskCooperateConfig(task,null,userInfo,null);
//			task = taskService.getTaskById(task.getId());
//			//模块日志添加
//			taskService.addTaskLog(userInfo.getComId(),task.getId(),userInfo.getId(),userInfo.getUserName(),"任务\""+task.getTaskName()+"\"委托给\""+executorInfo.getUserName()+"\"协同办理");
//			infoJson.append("{info:'委托成功！',succ:'yes'}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 标记任务状态
//	 * @param taskId
//	 * @param state
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/updatTaskState")
//	public String updatTaskState(Integer taskId,Integer state){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==taskId || "".equals(taskId) || null==state || "".equals(state)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Task task = new Task();
//			task.setComId(userInfo.getComId());
//			task.setId(taskId);
//			task.setState(state);
//			//改变任务执行状态为4完成状态
//			String stateDis =null;
//			if(4==state){
//				stateDis ="完成";
//			}else if(3==state){
//				stateDis ="挂起";
//			}else if(1==state){
//				stateDis ="执行";
//			}
//			//默认操作成功
//			boolean succ = true;
//			if(4==state){//标记完成
//				succ = taskService.updateDoneTask(task,userInfo,stateDis);
//			}else if(1==state){//重启
//				succ = taskService.updateRestarTask(task,userInfo,stateDis);
//			}
//			if(succ){
//				//模块日志添加
//				taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateDis+"\"成功");
//				infoJson.append("{info:'标记成功！',succ:'yes'}");
//			}else{
//				//模块日志添加
//				taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务标记为\""+stateDis+"\"失败");
//				infoJson.append("{info:'标记失败！',succ:'no'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 数据关注
//	 * @param busType 关注数据类型
//	 * @param busId 关注数据主键
//	 * @param attentionState 标记标识
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/changeAtten")
//	public String changeAtten(String busType,Integer busId,Integer attentionState){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==busType || "".equals(busType) || null==busId || "".equals(busId) || null==attentionState || "".equals(attentionState)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Attention atten = new Attention();
//			//所在企业
//			atten.setComId(userInfo.getComId());
//			//关注的人员
//			atten.setUserId(userInfo.getId());
//			atten.setBusType(busType);
//			atten.setBusId(busId);
//			//修改数据
//			attentionService.delAttention(atten,userInfo,attentionState);
//			Gson gson = new Gson();
//			if(busType.equals(ConstantInterface.TYPE_TASK)){
//				//获取任务详情
//				Task task = taskService.getTask(busId, userInfo);
//				if(attentionState==0){
//					infoJson.append("{succ:'yes',info:'关注成功！',data:"+gson.toJson(task)+",user:"+gson.toJson(userInfo)+"}");
//				}else{
//					infoJson.append("{succ:'yes',info:'取消了关注！',data:"+gson.toJson(task)+",user:"+gson.toJson(userInfo)+"}");
//				}
//			}else if(busType.equals(ConstantInterface.TYPE_ITEM)){
//				//获取项目详情
//				Item item = itemService.getItem(busId, userInfo);
//				if(attentionState==0){
//					infoJson.append("{succ:'yes',info:'关注成功！',data:"+gson.toJson(item)+",user:"+gson.toJson(userInfo)+"}");
//				}else{
//					infoJson.append("{succ:'yes',info:'取消了关注！',data:"+gson.toJson(item)+",user:"+gson.toJson(userInfo)+"}");
//				}
//			}else if(busType.equals(ConstantInterface.TYPE_CRM)){
//				//获取客户详情
//				Customer customer = crmService.queryCustomer(userInfo,busId);
//				if(attentionState==0){
//					infoJson.append("{succ:'yes',info:'关注成功！',data:"+gson.toJson(customer)+",user:"+gson.toJson(userInfo)+"}");
//				}else{
//					infoJson.append("{succ:'yes',info:'取消了关注！',data:"+gson.toJson(customer)+",user:"+gson.toJson(userInfo)+"}");
//				}
//			}
//			
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 新任务发布
//	 * @param request
//	 * @param taskStr 任务对象数据字符串
//	 * @param userInfoStr 操作用户对象数据字符串
//	 * @param addType 添加类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/addTask")
//	public String addTask(HttpServletRequest request,String taskStr,String userInfoStr,String addType){
//		StringBuffer infoJson = new StringBuffer();
//		//TODO 检查一下任务添加关联模块类型是否还有漏洞
//		try {
//			if(null==userInfoStr || "".equals(userInfoStr.trim()) || null==taskStr || "".equals(taskStr.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			Task task = gson.fromJson(taskStr, Task.class);
//			//企业号
//			task.setComId(userInfo.getComId());
//			//创建人
//			task.setCreator(userInfo.getId());
//			//负责人
//			task.setOwner(userInfo.getId());
//			//删除标识(正常)
//			task.setDelState(0);
//			//任务状态标识
//			task.setState(1);
//			//附件
//			List<Upfiles> listUpfiles = new ArrayList<Upfiles>();
//			if(null!=task.getFilesName() && task.getFilesName().length>0){
//				Upfiles file = null;
//				for(String upFileName:task.getFilesName()){
//					Integer fileId = this.uploadeFile(request, upFileName,userInfo.getComId());
//					file= new Upfiles();
//					if(fileId!=0){
//						file.setId(fileId);
//						listUpfiles.add(file);
//					}
//				}
//			}
//			task.setListUpfiles(listUpfiles);
//			
//			Integer taskId = taskService.addTask(task,null,userInfo,null);
//			if(null !=addType && "perfect".equals(addType)){
//				//获取任务详情
//				task = taskService.getTask(taskId, userInfo);
//				infoJson.append("{succ:'yes',addType:'"+addType+"',info:'发布成功！',data:"+gson.toJson(task)+"}");
//			}else{
//				infoJson.append("{succ:'yes',addType:'"+addType+"',info:'发布成功！',taskId:'"+taskId+"'}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 更新任务属性
//	 * @param taskId 任务主键
//	 * @param attrType 需要更新的属性类型标识
//	 * @param attrVal 所改变的属性值
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/updateTaskAttr")
//	public String updateTaskAttr(Integer taskId,String attrType,String attrVal){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==taskId || "".equals(taskId) || null==attrType || "".equals(attrType)|| null==attrVal || "".equals(attrVal)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			Task task = new Task();
//			//企业号
//			task.setComId(userInfo.getComId());
//			//任务主键
//			task.setId(taskId);
//			if("taskName".equals(attrType)){
//				task.setTaskName(attrVal);
//				boolean succ = taskService.updateTaskName(task,userInfo);
//				if(succ){
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务名称变更为\""+task.getTaskName()+"\"成功");
//					infoJson.append("{succ:'yes',info:'更新成功！'}");
//				}else{
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务名称变更为\""+task.getTaskName()+"\"失败");
//					infoJson.append("{succ:'no',info:'更新失败！'}");
//				}
//			}else if("taskRemark".equals(attrType)){
//				task.setTaskRemark(attrVal);
//				boolean succ = taskService.updateTaskTaskRemark(task,userInfo);
//				if(succ){
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务说明变更成功");
//					infoJson.append("{succ:'yes',info:'更新成功！'}");
//				}else{
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务说明变更失败");
//					infoJson.append("{succ:'no',info:'更新失败！'}");
//				}
//			}else if("taskProgress".equals(attrType)){
//				task.setTaskProgress(Integer.parseInt(attrVal));
//				String taskprogressDescribe = CommonUtil.dataDicZvalueByCode("progress",null!=task.getTaskProgress()?task.getTaskProgress().toString():null);
//				task.setTaskprogressDescribe(taskprogressDescribe);
//				boolean succ = taskService.updateTaskProgress(task,userInfo);
//				if(succ){
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "汇报进度为\""+taskprogressDescribe+"\"成功");
//					infoJson.append("{succ:'yes',info:'更新成功！'}");
//				}else{
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "汇报为\""+taskprogressDescribe+"\"失败");
//					infoJson.append("{succ:'no',info:'更新失败！'}");
//				}
//			}else if("grade".equals(attrType)){
//				task.setGrade(attrVal);
//				task.setComId(userInfo.getComId());
//				String gradeName = DataDicContext.getInstance().getCurrentPathZvalue("grade", task.getGrade());
//				boolean succ = taskService.updateTaskGrade(task,userInfo,gradeName);
//				if(succ){
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务的重要紧急性更新为\""+gradeName+"\"成功");
//					infoJson.append("{succ:'yes',info:'更新成功！'}");
//				}else{
//					//模块日志添加
//					taskService.addTaskLog(userInfo.getComId(),task.getId(), userInfo.getId(), userInfo.getUserName(), "任务的重要紧急性更新为\""+gradeName+"\"失败");
//					infoJson.append("{succ:'no',info:'更新失败！'}");
//				}
//			}
//			
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 发布任务初始化
//	 * @param type 字典表查找类型
//	 * @param pTaskId 母任务主键
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/taskAddConfigJson")
//	public String taskAddConfigJson(String type,Integer pTaskId){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			List<DataDic> listTreeDataDic=DataDicContext.getInstance().listTreeDataDicByType(type);
//			//获取母任务信息
//			Task pTask = taskService.getTask((null==pTaskId?0:pTaskId),userInfo);
//			Gson gson = new Gson();
//			infoJson.append("{succ:'yes',user:"+gson.toJson(userInfo)+",dataDic:"+gson.toJson(listTreeDataDic.get(1))+",pTask:"+gson.toJson(pTask)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取字典表数据
//	 * @param type
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfDataDic")
//	public String jsonOfDataDic(String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type)){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			List<DataDic> listTreeDataDic=DataDicContext.getInstance().listTreeDataDicByType(type);
//			Gson gson = new Gson();
//			infoJson.append("{succ:'yes',data:"+gson.toJson(listTreeDataDic)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 当前操作人的JSON数据
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfOperator")
//	public String jsonOfOperator(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				infoJson.append("{succ:'yes',data:"+gson.toJson(userInfo)+"}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/*****************************应用中心**********************************************/
//	/**
//	 * 应用中心选择
//	 * @param type 中心类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/appCenterOfJson")
//	public String appCenterOfJson(String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				if(type.equals(ConstantInterface.TYPE_TASK)){
//					//任务中心
//					//验证当前登录人是否是督察人员
//					boolean isForceIn = forceInService.isForceInPersion(userInfo, type);
//					//先统计任务数据
//					Task taskCount = taskService.taskCount(userInfo,isForceIn);
//					Gson gson = new Gson();
//					infoJson.append("{succ:'yes',taskCount:"+gson.toJson(taskCount)+",data:"+this.jsonOfTaskCenter()+"}");
//				}else if(type.equals(ConstantInterface.TYPE_CRM)){
//					//客户中心
//					//Gson gson = new Gson();
//					infoJson.append("{succ:'yes',crmCount:[],data:"+this.jsonOfCrmCenter()+"}");
//				}else if(type.equals(ConstantInterface.TYPE_ITEM)){
//					//项目中心
//					//Gson gson = new Gson();
//					infoJson.append("{succ:'yes',itemCount:[],data:"+this.jsonOfItemCenter()+"}");
//				}else if(type.equals(ConstantInterface.TYPE_WEEK)){
//					//周报中心
//					//Gson gson = new Gson();//验证当前登录人是否是督察人员
//					//验证当前登录人是否是督察人员
//					boolean isForceIn = forceInService.isForceInPersion(userInfo, type);
//					Integer repNum = weekReportService.getWeekReportCount("0", userInfo, isForceIn);
//					Integer unRepNum = weekReportService.getWeekReportCount("1", userInfo, isForceIn);
//					infoJson.append("{succ:'yes',weeklyReportCount:{repNum:"+repNum+",unRepNum:"+unRepNum+"},data:"+this.jsonOfWeeklyReportCenter()+"}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***
//	 * 任务中心数据
//	 * @return
//	 */
//	private String jsonOfTaskCenter(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
//				Task task = new Task();
//				//初始化执行人为当前操作人
//				task.setExecutor(userInfo.getId());
//				//任务状态标记为执行状态
//				task.setState(1);
//				//获取个人所有的待办任务
//				List<Task> taskToDoListOfAll = taskService.taskToDoListOfAll(task, userInfo);
//				infoJson.append("{toDoNum:"+taskToDoListOfAll.size());
//				//下属执行
//				if(userInfo.getCountSub()>0){
//					task = new Task();
//					task.setExecuType("1");
//					List<Task> listSubTaskOfAll = taskService.listTaskOfAll(task,userInfo,isForceIn);
//					infoJson.append(",subToDoNum:"+listSubTaskOfAll.size());
//				}else{
//					infoJson.append(",subToDoNum:0");
//				}
//				//逾期任务
//				task = new Task();
//				List<Task> listOverdueTaskOfAll = taskService.listOverdueTaskOfAll(task,userInfo,isForceIn);
//				infoJson.append(",overdueNum:"+listOverdueTaskOfAll.size());
//				//关注任务
//				Attention atten = new Attention();
//				//企业号
//				atten.setComId(userInfo.getComId());
//				//操作员Id
//				atten.setUserId(userInfo.getId());
//				//类型
//				atten.setBusType(ConstantInterface.TYPE_TASK);
//				//分页查询关注信息
//				List<Attention> listAttenOfAll = attentionService.listAttenOfAll(atten);
//				infoJson.append(",attenNum:"+listAttenOfAll.size());
//				//经办任务
//				task = new Task();
//				task.setOperator(userInfo.getId());
//				List<Task> listOperateTaskOfAll = taskService.listTaskOfAll(task,userInfo,isForceIn);
//				infoJson.append(",operateNum:"+listOperateTaskOfAll.size());
//				//我负责的
//				task = new Task();
//				//初始化负责人为当前操作人
//				task.setOwner(userInfo.getId());
//				//执行人类别
//				task.setExecuType(null);
//				//分页查询负责的任务
//				List<Task> listChargeTaskOfAll = taskService.listChargeTaskOfAll(task,userInfo);
//				infoJson.append(",chargeNum:"+listChargeTaskOfAll.size());
//				//所有任务
//				task = new Task();
//				List<Task> listTaskOfAll = taskService.listTaskOfAll(task,userInfo,isForceIn);
//				infoJson.append(",allTaskNum:"+listTaskOfAll.size());
//				infoJson.append("}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***
//	 * 获取任务分页集合
//	 * @param pageNum 页码
//	 * @param type 类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfTaskCenterList")
//	public String jsonOfTaskCenterList(Integer pageNum,String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//				//一次加载行数
//				PaginationContext.setPageSize(9);
//				//列表数据起始索引位置
//				PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_TASK);
//				Task task = new Task();;
//				if("toDo".equals(type)){
//					//初始化执行人为当前操作人
//					task.setExecutor(userInfo.getId());
//					//任务状态标记为执行状态
//					task.setState(1);
//					//获取个人所有的待办任务
//					List<Task> taskToDoList = taskService.taskToDoList(task, userInfo);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(taskToDoList)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("subToDo".equals(type)){
//					//下属执行
//					if(userInfo.getCountSub()>0){
//						task.setExecuType("1");
//						List<Task> listSubTask = taskService.listPageTask(task,userInfo);
//						infoJson.append("{succ:'yes',data:"+gson.toJson(listSubTask)+",user:"+gson.toJson(userInfo)+"}");
//					}else{
//						infoJson.append("{succ:'yes',data:[],user:"+gson.toJson(userInfo)+"}");
//					}
//				}else if("overdue".equals(type)){
//					//逾期任务
//					List<Task> listOverdueTask = taskService.listOverdueTask(task,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listOverdueTask)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("atten".equals(type)){
//					//关注任务
//					Attention atten = new Attention();
//					//企业号
//					atten.setComId(userInfo.getComId());
//					//操作员Id
//					atten.setUserId(userInfo.getId());
//					//类型
//					List<String> list = new ArrayList<String>();
//					list.add(ConstantInterface.TYPE_TASK);
//					//分页查询关注信息
//					List<Attention> listAttenOfAll = attentionService.listpagedAtten(atten,list);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listAttenOfAll)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("operate".equals(type)){
//					//经办任务
//					task.setOperator(userInfo.getId());
//					List<Task> listPageTask = taskService.listPageTask(task,userInfo);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listPageTask)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("charge".equals(type)){
//					//我负责的
//					//初始化负责人为当前操作人
//					task.setOwner(userInfo.getId());
//					//执行人类别
//					task.setExecuType(null);
//					//分页查询负责的任务
//					List<Task> listPageChargeTask = taskService.listPageChargeTask(task,userInfo);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listPageChargeTask)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("allTask".equals(type)){
//					//所有任务
//					List<Task> listPageTask = taskService.listPageTask(task,userInfo);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listPageTask)+",user:"+gson.toJson(userInfo)+"}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 客户中心数据获取
//	 * @return
//	 */
//	private String jsonOfCrmCenter(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
//			    Customer customer = new Customer();
//			    customer.setComId(userInfo.getComId());
//			    //若是没有下属，则没有负责人类别一说
//			    if(userInfo.getCountSub()<=0){
//			    	customer.setOwnerType(null);
//			    }
//			    //获取所有客户
//				List<Customer> listCustomer = crmService.listCustomerOfAll(customer,userInfo.getId(),isForceIn);
//				infoJson.append("{allNum:"+listCustomer.size());
//			    //获取我的客户
//				customer = new Customer();
//			    customer.setComId(userInfo.getComId());
//			    customer.setOwnerType("0");
//				listCustomer = crmService.listCustomerOfAll(customer,userInfo.getId(),isForceIn);
//				infoJson.append(",mineNum:"+listCustomer.size());
//				//获取下属客户
//				customer = new Customer();
//			    customer.setComId(userInfo.getComId());
//			    customer.setOwnerType("1");
//				listCustomer = crmService.listCustomerOfAll(customer,userInfo.getId(),isForceIn);
//				infoJson.append(",subNum:"+listCustomer.size());
//				//关注客户
//				Attention atten = new Attention();
//				//企业号
//				atten.setComId(userInfo.getComId());
//				//操作员Id
//				atten.setUserId(userInfo.getId());
//				//类型
//				atten.setBusType(ConstantInterface.TYPE_CRM);
//				//分页查询关注信息
//				List<Attention> listAttenOfAll = attentionService.listAttenOfAll(atten);
//				infoJson.append(",attenNum:"+listAttenOfAll.size());
//				//移交客户
//				customer = new Customer();
//			    customer.setComId(userInfo.getComId());
//				List<Customer> listCustomerHands = crmService.listCustomerHandsOfAll(customer,userInfo.getId());
//				infoJson.append(",handsNum:"+listCustomerHands.size());
//				//本月新增
//				customer = new Customer();
//			    customer.setComId(userInfo.getComId());
//				List<Customer> listCustomerAddByMonth = crmService.listCustomerAddByMonthOfAll(customer,userInfo.getId(),isForceIn);
//				infoJson.append(",addByMonthNum:"+listCustomerAddByMonth.size());
//				infoJson.append("}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***
//	 * 获取客户分页集合
//	 * @param pageNum 页码
//	 * @param type 类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfCrmCenterList")
//	public String jsonOfCrmCenterList(Integer pageNum,String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//				//一次加载行数
//				PaginationContext.setPageSize(9);
//				//列表数据起始索引位置
//				PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
//			    Customer customer = new Customer();
//			    customer.setComId(userInfo.getComId());
//			    //若是没有下属，则没有负责人类别一说
//			    if(userInfo.getCountSub()<=0){
//			    	customer.setOwnerType(null);
//			    }
//				if("allCrm".equals(type)){
//					//获取所有客户
//					List<Customer> listCustomer = crmService.listCustomerForPage(customer,userInfo.getId(),isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomer)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("myCrm".equals(type)){
//					//获取我的客户
//					customer = new Customer();
//				    customer.setComId(userInfo.getComId());
//				    customer.setOwnerType("0");
//				    List<Customer> listCustomer = crmService.listCustomerForPage(customer,userInfo.getId(),isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomer)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("subCrm".equals(type)){
//					//获取下属客户
//					customer = new Customer();
//				    customer.setComId(userInfo.getComId());
//				    customer.setOwnerType("1");
//				    List<Customer> listCustomer = crmService.listCustomerForPage(customer,userInfo.getId(),isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomer)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("attenCrm".equals(type)){
//					//关注客户
//					Attention atten = new Attention();
//					//企业号
//					atten.setComId(userInfo.getComId());
//					//操作员Id
//					atten.setUserId(userInfo.getId());
//					//类型
//					List<String> list = new ArrayList<String>();
//					list.add(ConstantInterface.TYPE_CRM);
//					//分页查询关注信息
//					List<Attention> listAttenOfAll = attentionService.listpagedAtten(atten,list);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listAttenOfAll)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("handsCrm".equals(type)){
//					//移交客户
//					customer = new Customer();
//				    customer.setComId(userInfo.getComId());
//					List<Customer> listCustomerHands = crmService.listCustomerHandsForPage(customer,userInfo.getId());
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomerHands)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("addByMonthCrm".equals(type)){
//					//本月新增
//					customer = new Customer();
//				    customer.setComId(userInfo.getComId());
//					List<Customer> listCustomerAddByMonth = crmService.listCustomerAddByMonthForPage(customer,userInfo.getId(),isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listCustomerAddByMonth)+",user:"+gson.toJson(userInfo)+"}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 项目中心数据获取
//	 * @return
//	 */
//	private String jsonOfItemCenter(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
//				Item item = new Item();
//			    //若是没有下属，则没有负责人类别一说
//			    if(userInfo.getCountSub()<=0){
//			    	item.setOwnerType(null);
//			    }
//			    //获取所有项目
//				List<Item> listItem = itemService.listItemOfAll(item,userInfo,isForceIn);
//				infoJson.append("{allNum:"+listItem.size());
//			    //获取我的项目
//				item = new Item();
//			    item.setOwnerType("0");
//			    listItem = itemService.listItemOfAll(item,userInfo,isForceIn);
//				infoJson.append(",mineNum:"+listItem.size());
//				//获取下属项目
//				item = new Item();
//			    item.setOwnerType("1");
//			    listItem = itemService.listItemOfAll(item,userInfo,isForceIn);
//				infoJson.append(",subNum:"+listItem.size());
//				//关注项目
//				Attention atten = new Attention();
//				//企业号
//				atten.setComId(userInfo.getComId());
//				//操作员Id
//				atten.setUserId(userInfo.getId());
//				//类型
//				atten.setBusType(ConstantInterface.TYPE_ITEM);
//				//分页查询关注信息
//				List<Attention> listAttenOfAll = attentionService.listAttenOfAll(atten);
//				infoJson.append(",attenNum:"+listAttenOfAll.size());
//				//移交项目
//				List<Item> listItemHands = itemService.listItemHandsOfAll(new Item(),userInfo);
//				infoJson.append(",handsNum:"+listItemHands.size());
//				//本月新增
//				List<Item> listItemAddByMonth = itemService.listItemAddByMonthOfAll(new Item(),userInfo,isForceIn);
//				infoJson.append(",addByMonthNum:"+listItemAddByMonth.size());
//				infoJson.append("}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***
//	 * 获取项目分页集合
//	 * @param pageNum 页码
//	 * @param type 类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfItemCenterList")
//	public String jsonOfItemCenterList(Integer pageNum,String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//				//一次加载行数
//				PaginationContext.setPageSize(9);
//				//列表数据起始索引位置
//				PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
//				Item item = new Item();
//				item.setComId(userInfo.getComId());
//			    //若是没有下属，则没有负责人类别一说
//			    if(userInfo.getCountSub()<=0){
//			    	item.setOwnerType(null);
//			    }
//				if("allItem".equals(type)){
//					//获取所有项目
//					List<Item> listItem = itemService.listItem(item,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listItem)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("myItem".equals(type)){
//					//获取我的项目
//					item = new Item();
//				    item.setComId(userInfo.getComId());
//				    item.setOwnerType("0");
//				    List<Item> listItem = itemService.listItem(item,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listItem)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("subItem".equals(type)){
//					//获取下属项目
//					item = new Item();
//				    item.setComId(userInfo.getComId());
//				    item.setOwnerType("1");
//				    List<Item> listItem = itemService.listItem(item,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listItem)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("attenItem".equals(type)){
//					//关注项目
//					Attention atten = new Attention();
//					//企业号
//					atten.setComId(userInfo.getComId());
//					//操作员Id
//					atten.setUserId(userInfo.getId());
//					//类型
//					List<String> list = new ArrayList<String>();
//					list.add(ConstantInterface.TYPE_ITEM);
//					//分页查询关注信息
//					List<Attention> listAttenOfAll = attentionService.listpagedAtten(atten,list);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listAttenOfAll)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("handsItem".equals(type)){
//					//移交项目
//					List<Item> listItemHands = itemService.listItemHandsForPage(new Item(),userInfo);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listItemHands)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("addByMonthItem".equals(type)){
//					//本月新增
//					List<Item> listItemAddByMonth = itemService.listItemAddByMonthForPage(new Item(),userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listItemAddByMonth)+",user:"+gson.toJson(userInfo)+"}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 周报中心数据获取
//	 * @return
//	 */
//	private String jsonOfWeeklyReportCenter(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
//				WeekReportPojo weekReport = new WeekReportPojo();
//			    //若是没有下属，则没有负责人类别一说
//			    if(userInfo.getCountSub()<=0){
//			    	weekReport.setWeekerType(null);
//			    }
//				//周报列表
//				List<WeekReport> listReport = weekReportService.listWeekReportOfAll(weekReport,userInfo,isForceIn);
//				infoJson.append("{allNum:"+listReport.size());
//			    //获取我的汇报
//				weekReport = new WeekReportPojo();
//				weekReport.setWeekerType("0");
//				listReport = weekReportService.listWeekReportOfAll(weekReport,userInfo,isForceIn);
//				infoJson.append(",mineNum:"+listReport.size());
//				//获取下属汇报
//				weekReport = new WeekReportPojo();
//				weekReport.setWeekerType("1");
//				listReport = weekReportService.listWeekReportOfAll(weekReport,userInfo,isForceIn);
//				infoJson.append(",subNum:"+listReport.size());
//				//本周未汇报
//				weekReport = new WeekReportPojo();
//				weekReport.setWeekDoneState("1");
//				listReport = weekReportService.listWeekReportOfAll(weekReport,userInfo,isForceIn);
//				infoJson.append(",unRepNum:"+listReport.size());
//				//本月已汇报
//				weekReport = new WeekReportPojo();
//				weekReport.setWeekDoneState("0");
//				listReport = weekReportService.listWeekReportOfAll(weekReport,userInfo,isForceIn);
//				infoJson.append(",repNum:"+listReport.size());
//				infoJson.append("}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/***
//	 * 获取周报分页集合
//	 * @param pageNum 页码
//	 * @param type 类型
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfWeeklyReportCenterList")
//	public String jsonOfWeeklyReportCenterList(Integer pageNum,String type){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo || null==type || "".equals(type.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				pageNum = ((null==pageNum || "".equals(pageNum.toString().trim()))?0:pageNum);
//				//一次加载行数
//				PaginationContext.setPageSize(9);
//				//列表数据起始索引位置
//				PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
//
//				//验证当前登录人是否是督察人员
//				boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
//				WeekReportPojo weekReport = new WeekReportPojo();
//			    //若是没有下属，则没有负责人类别一说
//			    if(userInfo.getCountSub()<=0){
//			    	weekReport.setWeekerType(null);
//			    }
//				if("allWeeklyReport".equals(type)){
//					//获取所有汇报
//					List<WeekReport> listReport = weekReportService.listPagedWeekReport(weekReport,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listReport)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("myWeeklyReport".equals(type)){
//					//获取我的汇报
//					weekReport = new WeekReportPojo();
//				    weekReport.setWeekerType("0");
//					List<WeekReport> listReport = weekReportService.listPagedWeekReport(weekReport,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listReport)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("subWeeklyReport".equals(type)){
//					//获取下属汇报
//					weekReport = new WeekReportPojo();
//				    weekReport.setWeekerType("1");
//					List<WeekReport> listReport = weekReportService.listPagedWeekReport(weekReport,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listReport)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("unRepWeeklyReport".equals(type)){
//					//本周未汇报周报
//					weekReport = new WeekReportPojo();
//				    weekReport.setWeekDoneState("1");
//					List<WeekReport> listReport = weekReportService.listPagedWeekReport(weekReport,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listReport)+",user:"+gson.toJson(userInfo)+"}");
//				}else if("repWeeklyReport".equals(type)){
//					//本周已汇报周报
//					weekReport = new WeekReportPojo();
//				    weekReport.setWeekDoneState("0");
//					List<WeekReport> listReport = weekReportService.listPagedWeekReport(weekReport,userInfo,isForceIn);
//					infoJson.append("{succ:'yes',data:"+gson.toJson(listReport)+",user:"+gson.toJson(userInfo)+"}");
//				}
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/**
//	 * 获取个人中心JSON数据
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jsonOfSelfCenter")
//	public String jsonOfSelfCenter(){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			UserInfo userInfo = this.getSessionUser();
//			if(null==userInfo){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}else{
//				Gson gson = new Gson();
//				infoJson.append("{succ:'yes',data:"+gson.toJson(userInfo)+"");
//				//总待办事项数
//				Integer todoNums = msgShareService.countTodo(userInfo.getComId(),userInfo.getId());
//				infoJson.append(",todoNums:"+todoNums);
//				Attention atten = new Attention();
//				//企业号
//				atten.setComId(userInfo.getComId());
//				//操作员Id
//				atten.setUserId(userInfo.getId());
//				//总关注数
//				List<Attention> listAttention = attentionService.listAttenOfAll(atten);
//				infoJson.append(",attenNum:"+listAttention.size());
//				//待办任务
//				Task task = new Task();
//				//初始化执行人为当前操作人
//				task.setExecutor(userInfo.getId());
//				//任务状态标记为执行状态
//				task.setState(1);
//				//获取个人所有的待办任务
//				List<Task> taskToDoList = taskService.taskToDoListOfAll(task, userInfo);
//				infoJson.append(",taskToDoNum:"+taskToDoList.size());
//				//获取我的客户
//				//验证当前登录人是否是督察人员
//				boolean isCrmForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_CRM);
//				Customer customer = new Customer();
//			    customer.setComId(userInfo.getComId());
//			    customer.setOwnerType("0");
//			    List<Customer> listCustomer = crmService.listCustomerOfAll(customer,userInfo.getId(),isCrmForceIn);
//				infoJson.append(",myCrmNum:"+listCustomer.size());
//			    //获取我的项目
//				//验证当前登录人是否是督察人员
//				boolean isItemForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_ITEM);
//			    Item item = new Item();
//			    item.setOwnerType("0");
//			    List<Item> listItem = itemService.listItemOfAll(item,userInfo,isItemForceIn);
//			    infoJson.append(",myItemNum:"+listItem.size());
//			    
//			  //验证当前登录人是否是督察人员
//				boolean isWeekForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
//			    //获取我的汇报
//			    WeekReportPojo weekReport = new WeekReportPojo();
//				weekReport.setWeekerType("0");
//				List<WeekReport> listReport = weekReportService.listWeekReportOfAll(weekReport,userInfo,isWeekForceIn);
//				infoJson.append(",myWeeklyRepNum:"+listReport.size());
//				infoJson.append("}");
//			}
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//	/*************************************附件上传******************************************************/
//	/**
//	 * 附件上传
//	 * @param request 
//	 * @param upFileName 上传文件控件的名称
//	 * @param comId 团队主键
//	 * @return
//	 * @throws Exception
//	 */
//	private Integer uploadeFile(HttpServletRequest request,String upFileName,Integer comId) throws Exception {
//		//构建返回值
//		Integer fileId = 0;
//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//		MultipartFile file = multipartRequest.getFile(upFileName);
//		// 文件名
//		String fileName = file.getOriginalFilename();
//		// 文件大小
//		Long fileSize = file.getSize();
//		String sizeM = MathExtend.divide(String.valueOf(fileSize.intValue()), String.valueOf(1024), 2);
//		String dw = "K";
//		if (Float.parseFloat(sizeM) > 1024) {
//			sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
//			dw = "M";
//			if (Float.parseFloat(sizeM) > 1024) {
//				sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
//				dw = "G";
//			}
//		}
//		
//		String fileMd5 = FileMD5Util.getFileMd5(file);
//		//从数据库根据MD5查询文件
//		Upfiles objFile = uploadService.getFileByMD5(fileMd5,comId);
//		String basepath = FileUtil.getUploadBasePath();
//		//数据库已有附件
//		if(null!=objFile){
//			fileId = objFile.getId();
//			String path = basepath+objFile.getFilepath();
//			//数据库有数据，但是文件不存在
//			File checkFile = new File(path);
//			if(!checkFile.exists()){
//				//所在文件夹
//				File dirFile = new File(path.substring(0, path.lastIndexOf("\\")));
//				if(!dirFile.exists()){//文件夹不存在
//					dirFile.mkdirs();
//				}
//				//重新存放
//				DataOutputStream out = new DataOutputStream(new FileOutputStream(path));// 存放文件的绝对路径
//				InputStream is = null;// 附件输入流
//				is = file.getInputStream();
//				byte[] buf = new byte[1024*1024*5];
//				int len = 0;
//				while ((len = is.read(buf)) > 0) {
//					out.write(buf, 0, len);
//				}
//				is.close();
//				out.close();
//			}
//			
//		}else{
//			// 后缀
//			String fileExt = FileUtil.getExtend(fileName);
//			/* 如果不是允许的附件类型，直接返回 */
//			if (!FileUtil.getAllowFileTypes().contains(fileExt)) {
//				System.out.println("不被允许上传的文件:"+fileName);
//				return 0;
//			}
//			/* 所有附件都保存到uploads 不存在则新增文件夹 */
//			File f = new File(basepath);
//			if (!f.exists()) {
//				f.mkdir();
//			}
//			String path = "/" + "uploads"+"/"+comId;
//			f = new File(basepath + path);
//			if (!f.exists()) {
//				f.mkdirs();
//			}
//			/* 每年一个文件夹 */
//			path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
//			f = new File(basepath + path);
//			if (!f.exists()) {
//				f.mkdir();
//			}
//			/* 每月一个文件夹 */
//			path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
//			f = new File(basepath + path);
//			if (!f.exists()) {
//				f.mkdir();
//			}
//			/* 每天一个文件夹 */
//			path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
//			f = new File(basepath + path);
//			if (!f.exists()) {
//				f.mkdir();
//			}
//			path = path + "/" + UUIDGenerator.getUUID() + "." + fileExt.toLowerCase();
//			
//			DataOutputStream out = new DataOutputStream(new FileOutputStream(basepath + path));// 存放文件的绝对路径
//			InputStream is = null;// 附件输入流
//			is = file.getInputStream();
//			byte[] buf = new byte[1024*1024*5];
//			int len = 0;
//			while ((len = is.read(buf)) > 0) {
//				out.write(buf, 0, len);
//			}
//			is.close();
//			out.close();
//			
//			// 附件信息存库
//			Upfiles upfiles = new Upfiles();
//			upfiles.setComId(comId);
//			upfiles.setUuid(UUIDGenerator.getUUID());
//			upfiles.setFilename(fileName);
//			upfiles.setFilepath(path);
//			upfiles.setFileExt(fileExt.toLowerCase());
//			upfiles.setSizeb(fileSize.intValue());
//			upfiles.setSizem(sizeM + dw);
//			upfiles.setMd5(fileMd5);
//			fileId = uploadService.addFile(upfiles);
//			upfiles.setId(fileId);
//			
//			//若文件是文本类的文件
//			if(FileUtil.getFileTypes().contains(fileExt)){
//				Filecontent filecontent = new Filecontent();
//				
//				filecontent.setComId(comId);
//				filecontent.setFilePath(basepath + path);
//				filecontent.setUpfilesId(fileId);
//				new Thread(new FileIndexThread(uploadService, filecontent)).start();
//			}
//		}
//		return fileId;
//	}
//	/***
//	 * 把能需要访问的文件下载到能直接访问的文件下面
//	 * @param userInfoStr
//	 * @param uuid
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/pathOfFileForView")
//	public String pathOfFileForView(String userInfoStr,String uuid){
//		StringBuffer infoJson = new StringBuffer();
//		try {
//			if(null==userInfoStr || "".equals(userInfoStr.trim()) || null==uuid || "".equals(uuid.trim())){
//				return infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}").toString();
//			}
//			//JSON对象转换
//			Gson gson = new Gson();
//			UserInfo userInfo = gson.fromJson(userInfoStr,UserInfo.class);
//			Upfiles upfile = uploadService.getFileByUUid(uuid);
//			//文件存放路径
//			StringBuffer filePath = new StringBuffer(); 
//			//存放在static文件夹下面
//			filePath.append("/static");
//			//统一存放文件夹
//			filePath.append("/comFiles");
//			//路径验证
//			File file = new File(FileUtil.getRootPath()+filePath.toString());
//			if (!file.exists()) {
//				file.mkdir();
//			}
//			//团队企业号作为分界文件夹名称
//			filePath.append("/"+userInfo.getComId());
//			file = new File(FileUtil.getRootPath()+filePath.toString());
//			if (!file.exists()) {
//				file.mkdir();
//			}
//			//文件的UUID作为分界文件夹名称
//			filePath.append("/"+upfile.getUuid());
//			file = new File(FileUtil.getRootPath()+filePath.toString());
//			if (!file.exists()) {
//				file.mkdir();
//				// 以流的形式下载文件
//				String fileStorePath = FileUtil.getUploadBasePath() + upfile.getFilepath();
//				InputStream fis = new BufferedInputStream(new FileInputStream(fileStorePath));
//				//文件名称
//				filePath.append("/"+upfile.getUuid() + "." + upfile.getFileExt().toLowerCase());
//				// 以流的形式写入文件
//				DataOutputStream out = new DataOutputStream(new FileOutputStream(FileUtil.getRootPath()+filePath.toString()));// 存放文件的绝对路径
//				byte[] buf = new byte[1024*1024*5];
//				int len = 0;
//				while ((len = fis.read(buf)) > 0) {
//					out.write(buf, 0, len);
//				}
//				fis.close();
//				out.close();
//			}else{
//				//文件名称
//				filePath.append("/"+upfile.getUuid() + "." + upfile.getFileExt().toLowerCase());
//			}
//			upfile.setFilepath(filePath.toString());
//			infoJson.append("{succ:'yes',data:"+gson.toJson(upfile)+"}");
//		} catch (Exception e) {
//			infoJson.append("{info:'获取数据异常，请及时联系管理员！',succ:'no'}");
//			e.printStackTrace();
//		}
//		return infoJson.toString();
//	}
//}
