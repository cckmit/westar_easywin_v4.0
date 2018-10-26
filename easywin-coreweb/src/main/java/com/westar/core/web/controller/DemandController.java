package com.westar.core.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.DemandProcess;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.BaseUpfile;
import com.westar.base.pojo.CommonLog;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.PageBean;
import com.westar.core.service.DemandService;
import com.westar.core.web.PaginationContext;

/**
 * 
 * 描述: 需求管理的控制层
 * @author zzq
 * @date 2018年10月9日 下午5:53:34
 */
@Controller
@RequestMapping("/demand")
public class DemandController extends BaseController{

	@Autowired
	DemandService demandService;
	
	/**
	 * 分页查询自己的发布的需求信息
	 * @param demandProcess
	 * @return
	 */
	@RequestMapping("/listPagedMineDemand")
	public ModelAndView listPagedMineDemand(DemandProcess demandProcess){
		ModelAndView mav = new ModelAndView("/demand/demandCenter");
		UserInfo sessionUser = this.getSessionUser();
		//分页查询自己的发布的需求信息
		PageBean<DemandProcess> pageBean = demandService.listPagedMineDemand(sessionUser,demandProcess);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("pageBean", pageBean);
		return mav;
	}
	/**
	 * 分页查询需要签收的需求信息
	 * @param demandProcess
	 * @return
	 */
	@RequestMapping("/listPagedDemandForAccept")
	public ModelAndView listPagedDemandForAccept(DemandProcess demandProcess){
		ModelAndView mav = new ModelAndView("/demand/demandCenter");
		UserInfo sessionUser = this.getSessionUser();
		//分页查询自己的发布的需求信息
		PageBean<DemandProcess> pageBean = demandService.listPagedDemandForAccept(sessionUser,demandProcess);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("pageBean", pageBean);
		return mav;
	}
	/**
	 * 分页查询需要处理的需求信息
	 * @param demandProcess
	 * @return
	 */
	@RequestMapping("/listPagedDemandForHandle")
	public ModelAndView listPagedDemandForHandle(DemandProcess demandProcess){
		ModelAndView mav = new ModelAndView("/demand/demandCenter");
		UserInfo sessionUser = this.getSessionUser();
		//分页查询自己的发布的需求信息
		PageBean<DemandProcess> pageBean = demandService.listPagedDemandForHandle(sessionUser,demandProcess);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("pageBean", pageBean);
		return mav;
	}
	/**
	 * 分页查询需要成果确认的需求信息
	 * @param demandProcess
	 * @return
	 */
	@RequestMapping("/listPagedDemandForConfirm")
	public ModelAndView listPagedDemandForConfirm(DemandProcess demandProcess){
		ModelAndView mav = new ModelAndView("/demand/demandCenter");
		UserInfo sessionUser = this.getSessionUser();
		//分页查询自己的发布的需求信息
		PageBean<DemandProcess> pageBean = demandService.listPagedDemandForConfirm(sessionUser,demandProcess);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("pageBean", pageBean);
		return mav;
	}
	/**
	 * 分页查询需要成果确认的需求信息
	 * @param demandProcess
	 * @return
	 */
	@RequestMapping("/listPagedDemandForAll")
	public ModelAndView listPagedDemandForAll(DemandProcess demandProcess){
		ModelAndView mav = new ModelAndView("/demand/demandCenter");
		UserInfo sessionUser = this.getSessionUser();
		//分页查询自己的发布的需求信息
		PageBean<DemandProcess> pageBean = demandService.listPagedDemandForAll(sessionUser,demandProcess);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("pageBean", pageBean);
		return mav;
	}
	
	/**
	 * 跳转到发布的需求信息
	 * @return
	 */
	@RequestMapping("/addDemandPage")
	public ModelAndView addDemandPage(){
		ModelAndView mav = new ModelAndView("/demand/addDemand");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	/**
	 * 发布的需求信息
	 * @param demandProcess
	 * @return
	 */
	@RequestMapping("/addDemand")
	public ModelAndView addDemand(DemandProcess demandProcess){
		ModelAndView mav = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo sessionUser = this.getSessionUser();
		demandService.addDemand(sessionUser,demandProcess);
		return mav;
	}
	
	/**
	 * 需求发布人员查看需求信息
	 * @param demandId 需求主键
	 * @return
	 */
	@RequestMapping("/viewDemandPage")
	public ModelAndView viewDemandForCreator(Integer demandId){
		ModelAndView mav = new ModelAndView("/demand/viewDemand");
		UserInfo sessionUser = this.getSessionUser();
		DemandProcess demandProcess = demandService.queryDemandForView(sessionUser,demandId);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("demandProcess", demandProcess);
		return mav;
	} 
	
	/**
	 * 获取任务附件
	 * @param taskUpfile
	 * @return
	 */
	@RequestMapping("/listPagedDemandUpfilePage")
	public ModelAndView listPagedDemandUpfilePage(Integer busId,String busType){
		ModelAndView view = new ModelAndView("/common/listUpFiles");
		UserInfo userInfo = this.getSessionUser();
		
		PageBean<BaseUpfile> pageBean = demandService.listPagedDemandUpfile(userInfo,busId);
		view.addObject("pageBean",pageBean);
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 日志记录
	 * 
	 * @param busType
	 *            业务类型
	 * @param busId
	 *            业务主键
	 * @param ifreamName
	 *            所在的ifream
	 * @return
	 */
	@RequestMapping("/listPagedDemandLog")
	public ModelAndView listPagedDemandLog(String busType, Integer busId) {
		UserInfo userInfo = this.getSessionUser();
		ModelAndView mav = new ModelAndView("/common/listLogs");
		//日志记录
		PageBean<CommonLog> pageBean = demandService.listPagedDemandLog(userInfo, busId);
		mav.addObject("pageBean", pageBean);
		return mav;
	}
	
	/**
	 * 获取项目列表FOR关联
	 * 
	 * @return
	 */
	@RequestMapping(value = "/demandRelevancePage")
	public ModelAndView demandRelevancePage() {
		ModelAndView view = new ModelAndView("/common/demand/demandRelevance");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo", userInfo);
		return view;
	}
	
	/**
	 * 异步取得需求分页数
	 * @param demandProcess 需求的查询条件
	 * @param pageNum 页码
	 * @param pageSize 分页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListPagedDemandForSelect")
	public HttpResult<PageBean<DemandProcess>> ajaxListPagedDemandForSelect(DemandProcess demandProcess,  
			Integer pageNum, Integer pageSize) {
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			return new HttpResult<PageBean<DemandProcess>>().error(CommonConstant.OFF_LINE_INFO);
		}
		pageNum = null == pageNum ? 0 : pageNum;
		// 一次加载行数
		PaginationContext.setPageSize(pageSize);
		// 列表数据起始索引位置
		PaginationContext.setOffset(pageNum * PaginationContext.getPageSize());
		
		PageBean<DemandProcess> pageBean = demandService.listPagedDemandForSelect(userInfo, demandProcess);
		return new HttpResult<PageBean<DemandProcess>>().ok(pageBean);
	}
	/**
	 * 受理需求
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doHandleDemand")
	public HttpResult<String> doHandleDemand(DemandProcess demandProcess){
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			return new HttpResult<String>().error(CommonConstant.OFF_LINE_INFO);
		}
		return demandService.doHandleDemand(userInfo,demandProcess);
	}
			
	
	
}