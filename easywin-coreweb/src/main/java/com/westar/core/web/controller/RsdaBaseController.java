package com.westar.core.web.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.RsdaBase;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.RsdaBaseService;

@Controller
@RequestMapping("/rsdaBase")
public class RsdaBaseController extends BaseController{

	@Autowired
	RsdaBaseService rsdaBaseService;
	
	/**
	 * 分页查询人员的档案信息
	 * @param rsdaBase
	 * @return
	 */
	@RequestMapping("/listPagedRsdaBase")
	public ModelAndView listPagedRsdaBase(RsdaBase rsdaBase){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		
		mav.addObject("userInfo", sessionUser);
		List<RsdaBase> listRsdaBases = rsdaBaseService.listPagedRsdaBase(sessionUser,rsdaBase);
		
		mav.addObject("listRsdaBases",listRsdaBases);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_RSDA);
		return mav;
	}
	/**
	 * 通过人员主键信息查看档案信息
	 * @param userId 查看的人员主键信息
	 * @return
	 */
	@RequestMapping("/viewRsdaBaseByUserId")
	public ModelAndView viewRsdaBaseByUserId(Integer userId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/viewRsdaBase");
		UserInfo sessionUser = this.getSessionUser();
		
		mav.addObject("userInfo", sessionUser);
		RsdaBase rsdaBase = rsdaBaseService.queryRsdaBaseByUserId(sessionUser,userId);
		mav.addObject("rsdaBase",rsdaBase);
		return mav;
	}
	/**
	 * 通过人员主键信息查看档案信息
	 * @param rsdaId 查看的人员主键信息
	 * @return
	 */
	@RequestMapping("/viewRsdaBaseByRsdaId")
	public ModelAndView viewRsdaBaseByRsdaId(Integer rsdaId){
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		
		mav.addObject("userInfo", sessionUser);
		RsdaBase rsdaBase = rsdaBaseService.queryRsdaBaseByRsdaId(sessionUser,rsdaId);
		mav.addObject("rsdaBase",rsdaBase);
		return mav;
	}
	
	/**
	 * 跳转修改人事档案界面
	 * @param userId 人员主键
	 * @return
	 */
	@RequestMapping("/updateRsdaBasePage")
	public ModelAndView updateRsdaBasePage(Integer userId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/updateRsdaBase");
		UserInfo sessionUser = this.getSessionUser();
		RsdaBase rsdaBase = rsdaBaseService.queryRsdaBaseByUserId(sessionUser,userId);
		mav.addObject("rsdaBase",rsdaBase);
		mav.addObject("userInfo",sessionUser);
		return mav;
	}
	/**
	 * 修改人事档案信息
	 * @param rsdaBase 人事档案信息
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaBaseInfo")
	public ModelAndView updateRsdaBaseInfo(RsdaBase rsdaBase,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaBaseService.updateRsdaBaseInfo(sessionUser,rsdaBase);
		return mav;
	}
	/**
	 * 修改人事档案信息
	 * @param rsdaBase 人事档案信息
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaBaseEdu")
	public ModelAndView updateRsdaBaseEdu(RsdaBase rsdaBase,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaBaseService.updateRsdaBaseInfo(sessionUser,rsdaBase);
		return mav;
	}
	/**
	 * 修改人事档案信息
	 * @param rsdaBase 人事档案信息
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaBaseOhter")
	public ModelAndView updateRsdaBaseOhter(RsdaBase rsdaBase,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaBaseService.updateRsdaBaseOhter(sessionUser,rsdaBase);
		return mav;
	}
	/**
	 * 修改人事档案信息
	 * @param rsdaBase 人事档案信息
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaBaseJob")
	public ModelAndView updateRsdaBaseJob(RsdaBase rsdaBase,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaBaseService.updateRsdaBaseJob(sessionUser,rsdaBase);
		return mav;
	}
	
	
}