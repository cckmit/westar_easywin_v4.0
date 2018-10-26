package com.westar.core.web.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Announcement;
import com.westar.base.model.Department;
import com.westar.base.model.InstituType;
import com.westar.base.model.Institution;
import com.westar.base.model.Talk;
import com.westar.base.model.UserInfo;
import com.westar.base.model.ViewRecord;
import com.westar.base.pojo.BusModVo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.InstitutionService;
import com.westar.core.service.LogsService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.TalkService;
import com.westar.core.service.TodayWorksService;
import com.westar.core.service.UserInfoService;
import com.westar.core.service.ViewRecordService;
import com.westar.core.web.FreshManager;

@Controller
@RequestMapping("/institution")
public class InstitutionController extends BaseController{

	@Autowired
	InstitutionService institutionService;
	
	@Autowired
	TalkService talkService;
	
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	LogsService logsService;
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	@Autowired
	ViewRecordService viewRecordService;
	
	/**
	 * 分页显示制度
	 * @param request
	 * @param institution
	 * @return
	 */
	@RequestMapping("/listPagedInstitu")
	public ModelAndView listPagedInstitu(HttpServletRequest request,Institution institution) {
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		UserInfo userInfo = this.getSessionUser();
		
		List<Institution> list = institutionService.listPagedInstitu(institution,userInfo);
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		
		//取得常用人员列表
		List<UserInfo> listCreators = userInfoService.listUsedUser(userInfo.getComId(),userInfo.getId(),5);
		mav.addObject("listCreators",listCreators);
		List<InstituType> listInstituType = institutionService.listInstituType(userInfo.getComId());		
		
		//初始化创建人名称
		if(null!=institution.getCreator() && institution.getCreator()!=0){
			UserInfo creator = userInfoService.getUserInfoById(institution.getCreator().toString());
			if(null!=creator){
				institution.setCreatorName(creator.getUserName());
			}
		}
		
		//初始化制度类型名称
		if(null!=institution.getInstituType() && institution.getInstituType()!=0){
			InstituType instituType = institutionService.queryInstituType(institution.getInstituType());
			if(null!=instituType){
				institution.setTypeName(instituType.getTypeName());
			}
		}
		mav.addObject("listInstituType",listInstituType);
		mav.addObject("list",list);
		mav.addObject("institution",institution);
		mav.addObject("userInfo",userInfo);
		
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return mav;
	}
	
	/**
	 * 至制度添加页面
	 * @return
	 */
	@RequestMapping("/addInstituPage")
	public ModelAndView addInstituPage(){
		ModelAndView mav = new ModelAndView("/institution/addInstitution");
		UserInfo userInfo = this.getSessionUser();
		List<InstituType> listInstituType = institutionService.listInstituType(userInfo.getComId());
		mav.addObject("listInstituType",listInstituType);
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	/**
	 *  添加制度
	 * @param institution 制度
	 * @param sendWay
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addInstitu")
	public ModelAndView addInstitu(Institution institution,String[] sendWay) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		
		institutionService.addInstitu(institution, userInfo, sendWay);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		ModelAndView view = new ModelAndView("/refreshParent");
		return view;
	}
	/**
	 * 查看制度
	 * @param request
	 * @param redirectPage
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewInstitu")
	public ModelAndView viewInstitu(HttpServletRequest request,String redirectPage,Integer id) throws Exception{
		ModelAndView mav = null ;
		UserInfo userInfo = this.getSessionUser();
		//制度查看权限验证
		if(!institutionService.authorCheck(userInfo.getComId(),id,userInfo.getId())){
			mav = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有查看制度权限");
		}else{
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_INSTITUTION, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				String viewType = viewRecordService.addViewRecordReturn(userInfo,viewRecord);
				if(ConstantInterface.TYPE_ADD.equals(viewType)){
					//增加查看次数
					institutionService.updateInstituRead(id);
				}
			}
			
			Institution institution = institutionService.getInstitutionInfo(id,userInfo);
			mav = new ModelAndView("/institution/viewInstitution");
			//获取制度前五条留言
			List<Talk> talks = talkService.listFiveTalk(id,userInfo.getComId(),ConstantInterface.TYPE_INSTITUTION);
			
			List<Department> scopeDep = institutionService.listInstituScopeDep(institution.getComId(), institution.getId());
			List<UserInfo> scopeUser = institutionService.listInstituScopeUser(institution.getComId(), institution.getId());
			
			
			mav.addObject("talks",talks);
			mav.addObject("busId", id);
			mav.addObject("institution", institution);
			mav.addObject("scopeDep", scopeDep);
			mav.addObject("scopeUser", scopeUser);
			mav.addObject("userInfo", userInfo);
			
			//查看人数,范围总人数
			List<UserInfo> listViewUser =new ArrayList<UserInfo>(); 
			Integer countRead = 0;
			if(institution.getScopeState() == 0){
				listViewUser = userInfoService.listAllEnabledUser(userInfo);
				countRead = userInfoService.countAllEnabledRead(userInfo.getComId(), id);
			}else{
				listViewUser = userInfoService.listInstituViewUser(userInfo.getComId(), id, institution.getCreator());
				countRead = userInfoService.countInstituRead(userInfo.getComId(), id, institution.getCreator());
			}
			mav.addObject("totalNum", listViewUser.size());
			mav.addObject("readNum", countRead);
		}
		//查看制度详情，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_INSTITUTION,0);
		return mav;
	}
	/**
	 * 修改制度
	 * @param request
	 * @param redirectPage
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editInstitu")
	public ModelAndView editInstitu(HttpServletRequest request,String redirectPage,Integer id) throws Exception{
		ModelAndView mav = null ;
		UserInfo userInfo = this.getSessionUser();
			
		Institution institution = institutionService.getInstitutionInfo(id,userInfo);
		if(institution.getCreator() == userInfo.getComId() || Integer.valueOf(userInfo.getAdmin())>0){
			ViewRecord viewRecord = new ViewRecord(userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_INSTITUTION, id);
			//取得是否添加浏览记录
			boolean bool = FreshManager.checkOpt(request, viewRecord);
			if(bool){
				//添加查看记录
				String viewType = viewRecordService.addViewRecordReturn(userInfo,viewRecord);
				if(ConstantInterface.TYPE_ADD.equals(viewType)){
					//增加查看次数
					institutionService.updateInstituRead(id);
				}
			}
			mav = new ModelAndView("/institution/editInstitution");
			
			List<Department> scopeDep = institutionService.listInstituScopeDep(institution.getComId(), institution.getId());
			List<UserInfo> scopeUser = institutionService.listInstituScopeUser(institution.getComId(), institution.getId());
			List<InstituType> listInstituType = institutionService.listInstituType(userInfo.getComId());	
			
			mav.addObject("listInstituType", listInstituType);
			mav.addObject("scopeDep", scopeDep);
			mav.addObject("scopeUser", scopeUser);
			mav.addObject("userInfo", userInfo);
			mav.addObject("busId", id);
		}else{
			mav = new ModelAndView("/refreshParent");
			this.setNotification(Notification.ERROR,"抱歉，你没有编辑制度权限");
			
		}
		//查看制度详情，删除消息提醒
		todayWorksService.updateTodoWorkRead(id, userInfo.getComId(), userInfo.getId(), ConstantInterface.TYPE_INSTITUTION,0);
		mav.addObject("institution", institution);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	/**
	 * 制度说明更新
	 * @param institution
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateInstitumRemark")
	public String updateInstitumRemark(Institution institution) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		institution.setComId(userInfo.getComId());
		boolean succ = institutionService.updateInstitumRemark(institution, userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	
	/**
	 * 制度类型更新
	 * @param institution
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateInstituType")
	public Map<String,Object> updateInstituType(Institution institution) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		institution.setComId(userInfo.getComId());
		boolean succ = institutionService.updateInstituType(institution, userInfo);
		if(succ){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
			map.put(ConstantInterface.TYPE_INFO,"更改制度成功");
		}else{
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO,"更改制度失败");
		}
		return map;
	}
	/**
	 * 删除制度
	 * @param id 制度主键
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delInstitution")
	public Map<String, Object> delInstitution(Integer id) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Boolean flag =  institutionService.delPreInstitution(new Integer[]{id},sessionUser);
		if(!flag){
			this.setNotification(Notification.ERROR, "制度已被删除！");
		}else{
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "删除制度",
					ConstantInterface.TYPE_INSTITUTION, sessionUser.getComId(),sessionUser.getOptIP());
			this.setNotification(Notification.SUCCESS, "删除成功！");
		}
		map.put("status", "y");
		return map;
	}
	/**
	 * 批量删除制度
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/delBatchInstitution")
	public ModelAndView delBatchInstitution(Integer[] ids,String redirectPage) throws Exception{
		UserInfo sessionUser = this.getSessionUser();
		Boolean falg = institutionService.delPreInstitution(ids,sessionUser);
		if(!falg){
			this.setNotification(Notification.ERROR, "制度已被删除！");
		}else{
			//系统日志
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "批量删除制度",
					ConstantInterface.TYPE_INSTITUTION, sessionUser.getComId(),sessionUser.getOptIP());
			this.setNotification(Notification.SUCCESS, "删除成功！");
		}
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		return mav;
	}
	
	/**
	 * 制度标题变更
	 * @param institution
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/institutionTitleUpdate")
	public String institutionTitleUpdate(Institution institution) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		institution.setComId(userInfo.getComId());
		boolean succ = institutionService.updateInstituTitle(institution, userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	
	
	/**
	 *添加制度人员范围
	 * @param institution
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addInstituScopeUser")
	public String addInstituScopeUser(Integer institutionId,Integer userId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = institutionService.addInstituScopeUser(institutionId,userId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 *添加制度部门范围
	 * @param institution
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addInstituScopeDep")
	public String addInstituScopeDep(Integer institutionId,Integer depId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = institutionService.addInstituScopeDep(institutionId,depId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 *删除制度人员范围
	 * @param institution
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delInstituScopeUser")
	public String delInstituScopeUser(Integer institutionId,Integer userId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = institutionService.delInstituScopeUser(institutionId,userId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 *删除制度部门范围
	 * @param institution
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delInstituScopeDep")
	public String delInstituScopeDep(Integer institutionId,Integer depId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		boolean succ = institutionService.delInstituScopeDep(institutionId,depId ,userInfo);
		if(succ){
			return "更新成功";
		}else{
			return "更新失败";
		}
	}
	/**
	 * 制度查看讨论页面
	 * @param institutionId 主键
	 * @return
	 */
	@RequestMapping("/instituTalkPage")
	public ModelAndView institutionTalkPage(Integer institutionId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/institution/instituTalk");
		
		//讨论
		List<Talk> talks = talkService.listPagedTalk(institutionId,userInfo.getComId(),ConstantInterface.TYPE_INSTITUTION);
		mav.addObject("talks",talks);
		mav.addObject("busId",institutionId);
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	/**
	 * 讨论回复
	 * @param talk
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/addInstituTalk")
	public Map<String, Object> addInstituTalk(Talk talk) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		talk.setComId(sessionUser.getComId());
		talk.setTalker(sessionUser.getId());
		
		//公告信息
		Institution announcement = institutionService.getInstitutionInfo(talk.getBusId(), sessionUser);
		
		BusModVo busModVo = new BusModVo(talk.getBusId(),ConstantInterface.TYPE_INSTITUTION,announcement.getTitle());
		Integer talkId = talkService.addTalk(talk,sessionUser,busModVo);
		
		map.put("status", "y");
		map.put("talkId", talkId);
		//用于返回页面拼接代码
		Talk talk4Page = talkService.getTalk(talkId, sessionUser.getComId(),ConstantInterface.TYPE_INSTITUTION);
		map.put("talk", talk4Page);
		map.put("userInfo", sessionUser);
		return map;
	}
	/**
	 * 删除讨论回复
	 * @param voteTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/delInstituTalk")
	public Map<String, Object> delInstituTalk(Talk talk,String delChildNode) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		talk.setComId(sessionUser.getComId());
		
		//要删除的回复所有子节点和自己
		List<Integer> childIds = talkService.delTalk(talk,delChildNode,sessionUser,ConstantInterface.TYPE_INSTITUTION);
		map.put("status", "y");
		map.put("childIds", childIds);
		return map;
	}
	/**
	 * 删除查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param institutionId 制度主键
	 */
	@ResponseBody
	@RequestMapping("/delScope")
	public Map<String, Object> delScope(String type,Integer busId,Integer institutionId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		institutionService.delScope(type, sessionUser, busId, institutionId);
		boolean changeScope = institutionService.delAfterUpdateScope(institutionId,sessionUser.getComId());
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		map.put("changeScope",changeScope);
		return map;
	}
	/**
	 * 删除查看范围
	 * @param type
	 * @param comId 类型
	 * @param busId 主键
	 * @param institutionId 制度主键
	 */
	@ResponseBody
	@RequestMapping("/updateScope")
	public Map<String, Object> updateScope(String type,Integer[] busId,Integer institutionId) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		institutionService.updateScope(type, busId, institutionId,sessionUser);
		institutionService.addBeforeUpdateScope(institutionId,sessionUser.getComId());
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/***************************** 以下是制度类型配置 *****************************************/
	/**
	 * 制度类型配置界面
	 * @return
	 */
	@RequestMapping("/listInstituTypePage")
	public ModelAndView listInstituTypePage(){
		ModelAndView view = new ModelAndView("/institution/listInstituType");
		UserInfo userInfo = this.getSessionUser();
		List<InstituType> listInstituType = institutionService.listInstituType(userInfo.getComId());
		
		view.addObject("listInstituType",listInstituType);
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_CLGL);
		return view;
	}
	/**
	 * 制度类型最大序号
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetInstituTypeOrder")
	public Map<String, Object> ajaxGetInstituTypeOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		// 获取最大序号
		Integer orderNum = institutionService.queryInstituTypeOrderMax(sessionUser.getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}

	/**
	 * 添加制度类型
	 * @param institutionType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddInstituType")
	public Map<String, Object> ajaxAddInstituType(InstituType institutionType) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		institutionType.setComId(userInfo.getComId());
		institutionType.setCreator(userInfo.getId());
		
		// 添加制度类型
		Integer id = institutionService.addInstituType(institutionType, userInfo);

		institutionType.setId(id);
		institutionType.setOrderNum(institutionType.getOrderNum() + 1);

		
		map.put("institutionType", institutionType);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;

	}
	/**
	 * 修改类型名称
	 * @param institutionType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateInstituTypeName")
	public String updateInstituTypeName(InstituType institutionType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		institutionType.setComId(userInfo.getComId());
		boolean succ = institutionService.updateInstituTypeName(institutionType, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	/**
	 * 修改类型序号
	 * @param institutionType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateInstituTypeOrder")
	public String updateInstituTypeOrder(InstituType institutionType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		institutionType.setComId(userInfo.getComId());
		boolean succ = institutionService.updateInstituTypeOrder(institutionType,userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	/**
	 * 删除类型
	 * @param institutionType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delInstituType")
	public InstituType delInstituType(InstituType institutionType)throws Exception {
		UserInfo userInfo = this.getSessionUser();
		institutionType.setComId(userInfo.getComId());
		boolean succ = institutionService.delInstituType(institutionType, userInfo);
		institutionType.setSucc(succ);
		if (succ) {
			institutionType.setPromptMsg("删除成功");
		} else {
			institutionType.setPromptMsg("该类型已使用,删除失败");
		}
		return institutionType;
	}
	/***************************** 以上是制度类型配置 *****************************************/	
	/**
	 *  查看人员及其查看状态
	 * @param instituId 制度
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listViewRecord")
	public ModelAndView listViewRecord(Integer instituId) throws Exception{
		UserInfo userInfo = this.getSessionUser();
		ModelAndView view = new ModelAndView("/institution/listViewRecord");
		
		Institution institution = institutionService.getInstitutionInfo(instituId, userInfo);
		List<UserInfo> listUser = new ArrayList<UserInfo>();
		if(institution.getScopeState() == 0){
			listUser = userInfoService.listAllEnabledRead(userInfo.getComId(), instituId);
		}else{
			listUser = userInfoService.pagedInstituViewUser(userInfo.getComId(), instituId,institution.getCreator());
		}
		
		view.addObject("listUser",listUser);
		return view;
	}
}