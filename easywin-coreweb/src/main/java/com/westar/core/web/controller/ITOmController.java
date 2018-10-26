package com.westar.core.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Customer;
import com.westar.base.model.EventPm;
import com.westar.base.model.IssuePm;
import com.westar.base.model.ModifyPm;
import com.westar.base.model.ReleasePm;
import com.westar.base.model.SpFlowInstance;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.EventPMTable;
import com.westar.base.pojo.IssuePMTable;
import com.westar.base.pojo.ModifyPMTable;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.ReleasePMTable;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.ITOmService;
import com.westar.core.web.FreshManager;
import com.westar.core.web.PaginationContext;

@Controller
@RequestMapping("iTOm")
public class ITOmController extends BaseController {
	
	@Autowired
	ITOmService iTOmService;
	/**
	 * 跳转运维中心
	 * @param request
	 * @param activityMenu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/itom_center")
	public ModelAndView itom_center(HttpServletRequest request,String activityMenu){
		//清除缓存中所有的操作
		FreshManager.removePreOpt(request);
		ModelAndView view = new ModelAndView("/itom/itom_center");
		view.addObject("activityMenu",activityMenu);
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ITOM);
		
	    String nowYear = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
	    view.addObject("nowYear",nowYear);
		return view;
	}
	/**
	 * 分页查询事件管理过程数据
	 * @param eventPm 事件管理过程查询条件 
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/eventPm/ajaxListPagedEventPm")
	public Map<String,Object> ajaxListPagedEventPm(EventPm eventPm,Integer pageNum, Integer pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		PageBean<EventPm> pageBean = iTOmService.listPagedEventPm(userInfo,eventPm);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	/**
	 * 分页查询问题管理过程数据
	 * @param eventPm 事件管理过程查询条件 
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/issuePm/ajaxListPagedIssuePm")
	public Map<String,Object> ajaxListPagedIssuePm(IssuePm issuePm,Integer pageNum, Integer pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		PageBean<IssuePm> pageBean = iTOmService.listPagedIssuePm(userInfo,issuePm);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	/**
	 * 分页查询问题管理过程数据
	 * @param eventPm 事件管理过程查询条件 
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modifyPm/ajaxListPagedModifyPm")
	public Map<String,Object> ajaxListPagedModifyPm(ModifyPm modifyPm,Integer pageNum, Integer pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		PageBean<ModifyPm> pageBean = iTOmService.listPagedModifyPm(userInfo,modifyPm);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	/**
	 * 分页查询问题管理过程数据
	 * @param eventPm 事件管理过程查询条件 
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/releasePm/ajaxListPagedReleasePm")
	public Map<String,Object> ajaxListPagedReleasePm(ReleasePm releasePm,Integer pageNum, Integer pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo userInfo = this.getSessionUser();
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		PageBean<ReleasePm> pageBean = iTOmService.listPagedReleasePm(userInfo,releasePm);
		map.put("pageBean", pageBean);
		map.put("status", "y");
		return map;
	}
	
	
	
	
	/**
	 * 事件过程分析
	 * @param eventPm 事件过程分析统计查询条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/eventPm/analyseEventPm")
	public Map<String,Object> analyseEventPm(EventPm eventPm){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<EventPMTable> eventPmTables = iTOmService.analyseEventPm(eventPm, sessionUser);
		map.put("status", "y");
		map.put("list", eventPmTables);
		return map;
	}
	/**
	 * 问题过程分析
	 * @param issuePm 事件过程分析统计查询条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/issuePm/analyseIssuePm")
	public Map<String,Object> analyseIssuePm(IssuePm issuePm){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<IssuePMTable> issuePmTables = iTOmService.analyseIssuePm(issuePm, sessionUser);
		map.put("status", "y");
		map.put("list", issuePmTables);
		return map;
	}
	/**
	 * 变更过程分析
	 * @param modifyPm 事件过程分析统计查询条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modifyPm/analyseModifyPm")
	public Map<String,Object> analyseModifyPm(ModifyPm modifyPm){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<ModifyPMTable> modifyPmTables = iTOmService.analyseModifyPm(modifyPm, sessionUser);
		map.put("status", "y");
		map.put("list", modifyPmTables);
		return map;
	}
	/**
	 * 发布过程分析
	 * @param releasePm 事件过程分析统计查询条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/releasePm/analyseReleasePm")
	public Map<String,Object> analyseReleasePm(ReleasePm releasePm){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<ReleasePMTable> modifyPmTables = iTOmService.analyseReleasePm(releasePm, sessionUser);
		map.put("status", "y");
		map.put("list", modifyPmTables);
		return map;
	}
	
	/**
	 * 跳转事件选择界面
	 * @return
	 */
	@RequestMapping(value = "/eventPm/listEventForRelevance")
	public ModelAndView listMoreEventForRelevance() {
		ModelAndView view = new ModelAndView("/itom/eventPm/listEventForRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 异步取得事件分页数
	 * @param eventPm
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/eventPm/ajaxListEventForSelect")
	public Map<String, Object> ajaxListEventForSelect(EventPm eventPm, Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<EventPm> pageBean = iTOmService.listPagedEventPm(userInfo,eventPm);
		map.put("pageBean", pageBean);
		
		map.put("status", "y");
		return map;
	}
	/**
	 * 跳转问题选择界面
	 * @return
	 */
	@RequestMapping(value = "/issuePm/listIssueForRelevance")
	public ModelAndView listMoreIssueForRelevance() {
		ModelAndView view = new ModelAndView("/itom/issuePm/listIssueForRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 异步取得问题分页数
	 * @param issuePm
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/issuePm/ajaxListIssueForSelect")
	public Map<String, Object> ajaxListIssueForSelect(IssuePm issuePm, Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<IssuePm> pageBean = iTOmService.listPagedIssuePm(userInfo,issuePm);
		map.put("pageBean", pageBean);
		
		map.put("status", "y");
		return map;
	}
	/**
	 * 跳转变更选择界面
	 * @return
	 */
	@RequestMapping(value = "/modifyPm/listModifyForRelevance")
	public ModelAndView listMoreModifyForRelevance() {
		ModelAndView view = new ModelAndView("/itom/modifyPm/listModifyForRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 异步取得变更分页数
	 * @param modifyPm
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modifyPm/ajaxListModifyForSelect")
	public Map<String, Object> ajaxListModifyForSelect(ModifyPm modifyPm, Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<ModifyPm> pageBean = iTOmService.listPagedModifyPm(userInfo,modifyPm);
		map.put("pageBean", pageBean);
		
		map.put("status", "y");
		return map;
	}
	/**
	 * 跳转发布选择界面
	 * @return
	 */
	@RequestMapping(value = "/releasePm/listReleaseForRelevance")
	public ModelAndView listMoreReleaseForRelevance() {
		ModelAndView view = new ModelAndView("/itom/releasePm/listReleaseForRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	/**
	 * 异步取得变更分页数
	 * @param releasePm
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/releasePm/ajaxListReleaseForSelect")
	public Map<String, Object> ajaxListReleaseForSelect(ReleasePm releasePm, Integer pageNum, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		pageNum = ((null == pageNum || "".equals(pageNum.toString().trim())) ? 0 : pageNum);
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<ReleasePm> pageBean = iTOmService.listPagedReleasePm(userInfo,releasePm);
		map.put("pageBean", pageBean);
		
		map.put("status", "y");
		return map;
	}
	
	/**
	 * 查看审批管理的运维管理信息
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @return
	 */
	@RequestMapping(value = "/viewItomDetail")
	public ModelAndView viewItomDetail(Integer busId,String busType) {
		UserInfo userInfo = this.getSessionUser();
		Integer instanceId = iTOmService.getItomDetailIns(userInfo,busId,busType);
		ModelAndView view = new ModelAndView("redirect:/workFlow/viewSpFlow?sid="+this.getSid()+"&instanceId="+instanceId);
		view.addObject("userInfo", userInfo);
		return view;
	}
}
