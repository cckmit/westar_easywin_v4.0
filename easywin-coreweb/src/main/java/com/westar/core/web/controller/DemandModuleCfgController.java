package com.westar.core.web.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.DemandModuleCfg;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.HttpResult;
import com.westar.base.pojo.PageBean;
import com.westar.core.service.DemandModuleCfgService;

/**
 * 
 * 描述:需求处理模板配置
 * @author zzq
 * @date 2018年10月23日 下午1:34:23
 */
@Controller
@RequestMapping("/demandModuleCfg")
public class DemandModuleCfgController extends BaseController{

	@Autowired
	DemandModuleCfgService demandModuleCfgService;
	
	/**
	 *分页查询需求处理模板配置显示列表
	 * @return
	 */
	@RequestMapping("/listPagedDemandModuleCfg")
	public ModelAndView listPagedDemandModuleCfg(DemandModuleCfg demandModuleCfg){
		ModelAndView mav = new ModelAndView("/demand/demandCenter");
		UserInfo sessionUser = this.getSessionUser();
		
		PageBean<DemandModuleCfg> pageBean = demandModuleCfgService.listPagedDemandModuleCfg(sessionUser,demandModuleCfg);
		mav.addObject("pageBean", pageBean);
		
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 跳转到需求处理模板配置显示列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListPagedDemandModuleCfg")
	public HttpResult<List<DemandModuleCfg>> listDemandModuleCfg(DemandModuleCfg demandModuleCfg){
		UserInfo sessionUser = this.getSessionUser();
		List<DemandModuleCfg> list = demandModuleCfgService.listDemandModuleCfg(sessionUser,demandModuleCfg);
		return new HttpResult<List<DemandModuleCfg>>().ok(list);
	}
	
	
	
	/**
	 * 跳转到需求处理模板配置添加界面
	 * @return
	 */
	@RequestMapping("/addDemandModuleCfgPage")
	public ModelAndView addDemandModuleCfgPage(){
		ModelAndView mav = new ModelAndView("/demand/demand_module_cfg/addDemandModuleCfg");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 添加需求处理模板配置
	 * @param demandModuleCfg
	 * @return
	 */
	@RequestMapping("/addDemandModuleCfg")
	public ModelAndView addDemandModuleCfg(DemandModuleCfg demandModuleCfg){
		ModelAndView mav = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo sessionUser = this.getSessionUser();
		
//		demandModuleCfgService.addDemandModuleCfg(sessionUser,demandModuleCfg);
		
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 跳转到需求处理模板配置添加界面
	 * @param demandModuleCfgId 需求处理主键
	 * @return
	 */
	@RequestMapping("/updateDemandModuleCfgPage")
	public ModelAndView updateDemandModuleCfgPage(Integer demandModuleCfgId){
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		
		DemandModuleCfg demandModuleCfg = demandModuleCfgService.queryDemandModuleCfg(sessionUser,demandModuleCfgId);
		mav.addObject("demandModuleCfg", demandModuleCfg);
		
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 编辑需求处理模板配置添加界面
	 * @param demandModuleCfgId
	 * @return
	 */
	@RequestMapping("/updateDemandModuleCfg")
	public ModelAndView updateDemandModuleCfg(DemandModuleCfg demandModuleCfg){
		ModelAndView mav = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo sessionUser = this.getSessionUser();
		demandModuleCfgService.updateDemandModuleCfg(sessionUser,demandModuleCfg);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 删除需求处理模板配置
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping(value = "/delDemandModuleCfg", method = RequestMethod.POST)
	public ModelAndView delDemandModuleCfg(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		demandModuleCfgService.delDemandModuleCfg(sessionUser,ids);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 复制需求处理模板配置
	 * @param demandModuleCfgId 需求模板主键
	 * @return
	 */
	@RequestMapping(value = "/copyDemandModuleCfg", method = RequestMethod.POST)
	public ModelAndView copyDemandModuleCfg(Integer demandModuleCfgId){
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		demandModuleCfgService.copyDemandModuleCfg(sessionUser,demandModuleCfgId);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
}