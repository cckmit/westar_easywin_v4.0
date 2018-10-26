package com.westar.core.web.controller;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.RsdaResume;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.RsdaResumeService;
import com.westar.core.service.UserInfoService;

/**
 * 人员复职
 * @author zzq
 *
 */
@Controller
@RequestMapping("/rsdaResume")
public class RsdaResumeController extends BaseController{

	@Autowired
	RsdaResumeService rsdaResumeService;
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 分页查询复职信息
	 * @param rsdaResume 人员复职信息
	 * @return
	 */
	@RequestMapping("/listPagedRsdaResume")
	public ModelAndView listPagedRsdaResume(RsdaResume rsdaResume){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaResume> listRsdaResumes = rsdaResumeService.listPagedRsdaResume(sessionUser,rsdaResume);
		mav.addObject("listRsdaResumes", listRsdaResumes);
		
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_RSDA);
		return mav;
	}
	/**
	 * 分页查询指定人员复职信息
	 * @param rsdaResume人员复职信息
	 * @return
	 */
	@RequestMapping("/listPagedUserRsdaResume")
	public ModelAndView listPagedUserRsdaResume(RsdaResume rsdaResume){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/listUserRsdaResume");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaResume> listRsdaResumes = rsdaResumeService.listPagedRsdaResume(sessionUser,rsdaResume);
		mav.addObject("listRsdaResumes", listRsdaResumes);
		
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 跳转添加人员的复职信息
	 * @param userId 人员主键
	 * @return
	 */
	@RequestMapping("/addRsdaResumePage")
	public ModelAndView addRsdaResumePage(Integer userId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/addRsdaResume");
		UserInfo sessionUser = this.getSessionUser();
		if(null!=userId && userId>0){
			UserInfo userInfo = userInfoService.getUserBaseInfo(sessionUser.getComId(), userId);
			mav.addObject("userInfo", userInfo);
		}
		return mav;
	}
	/**
	 * 添加人员的复职信息
	 * @param rsdaResume 复职信息
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/addRsdaResume")
	public ModelAndView addRsdaResume(RsdaResume rsdaResume,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaResumeService.addRsdaResume(sessionUser,rsdaResume);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	
	/**
	 * 跳转修改人员的复职信息
	 * @param resumeId 复职信息主键
	 * @return
	 */
	@RequestMapping("/updateRsdaResumePage")
	public ModelAndView updateRsdaResumePage(Integer resumeId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/updateRsdaResume");
		UserInfo sessionUser = this.getSessionUser();
		RsdaResume rsdaResume = rsdaResumeService.queryRsdaResumeById(sessionUser,resumeId);
		mav.addObject("rsdaResume", rsdaResume);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 修改人员的复职信息
	 * @param rsdaResume 工作经历
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaResume")
	public ModelAndView updateRsdaResume(RsdaResume rsdaResume,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaResumeService.updateRsdaResume(sessionUser,rsdaResume);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	/**
	 * 查看复职信息
	 * @param resumeId 工作经历主键
	 * @return
	 */
	@RequestMapping("/viewRsdaResumePage")
	public ModelAndView viewRsdaResumePage(Integer resumeId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/viewRsdaResume");
		UserInfo sessionUser = this.getSessionUser();
		RsdaResume rsdaResume = rsdaResumeService.queryRsdaResumeById(sessionUser,resumeId);
		mav.addObject("rsdaResume", rsdaResume);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	/**
	 * 删除人员的工作经历
	 * @param ids 工作经历的主键
	 * @param redirectPage 删除后的跳转界面
	 * @return
	 */
	@RequestMapping("/deleteRsdaResume")
	public ModelAndView deleteRsdaResume(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaResumeService.deleteRsdaResume(sessionUser,ids);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		return mav;
	}
	
}