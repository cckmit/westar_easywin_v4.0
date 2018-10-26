package com.westar.core.web.controller;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.RsdaTrance;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.RsdaTranceService;
import com.westar.core.service.UserInfoService;

/**
 * 人事调动
 * @author zzq
 *
 */
@Controller
@RequestMapping("/rsdaTrance")
public class RsdaTranceController extends BaseController{

	@Autowired
	RsdaTranceService rsdaTranceService;
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 分页查询人事调动信息
	 * @param rsdaTrance
	 * @return
	 */
	@RequestMapping("/listPagedRsdaTrance")
	public ModelAndView listPagedRsdaTrance(RsdaTrance rsdaTrance){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaTrance> listRsdaTrance = rsdaTranceService.listPagedRsdaTrance(sessionUser,rsdaTrance);
		mav.addObject("listRsdaTrance", listRsdaTrance);
		
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_RSDA);
		return mav;
	}
	
	/**
	 * 分页查询指定人员人事调动信息
	 * @param rsdaTrance
	 * @return
	 */
	@RequestMapping("/listPagedUserRsdaTrance")
	public ModelAndView listPagedUserRsdaTrance(RsdaTrance rsdaTrance){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/listUserRsdaTrance");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaTrance> listRsdaTrance = rsdaTranceService.listPagedRsdaTrance(sessionUser,rsdaTrance);
		mav.addObject("listRsdaTrance", listRsdaTrance);
		
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 跳转添加人员的人事调动界面
	 * @param userId 人员主键
	 * @return
	 */
	@RequestMapping("/addRsdaTrancePage")
	public ModelAndView addRsdaTrancePage(Integer userId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/addRsdaTrance");
		UserInfo sessionUser = this.getSessionUser();
		if(null!=userId && userId>0){
			UserInfo userInfo = userInfoService.getUserBaseInfo(sessionUser.getComId(), userId);
			mav.addObject("userInfo", userInfo);
		}
		return mav;
	}
	
	/**
	 * 添加人员的人事调动
	 * @param rsdaTrance 工作经历
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/addRsdaTrance")
	public ModelAndView addRsdaTrance(RsdaTrance rsdaTrance,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaTranceService.addRsdaTrance(sessionUser,rsdaTrance);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	
	/**
	 * 跳转修改人员的人事调动
	 * @param jobHisId 人员主键
	 * @return
	 */
	@RequestMapping("/updateRsdaTrancePage")
	public ModelAndView updateRsdaTrancePage(Integer radaTranceId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/updateRsdaTrance");
		UserInfo sessionUser = this.getSessionUser();
		RsdaTrance rsdaTrance = rsdaTranceService.queryRsdaTranceById(sessionUser,radaTranceId);
		mav.addObject("rsdaTrance", rsdaTrance);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 修改人员的人事调动
	 * @param rsdaTrance 人事调动
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaTrance")
	public ModelAndView updateRsdaTrance(RsdaTrance rsdaTrance,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaTranceService.updateRsdaTrance(sessionUser,rsdaTrance);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	/**
	 * 查看人事调动信息
	 * @param radaTranceId 人事调动主键
	 * @return
	 */
	@RequestMapping("/viewRsdaTrancePage")
	public ModelAndView viewRsdaTrancePage(Integer radaTranceId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/viewRsdaTrance");
		UserInfo sessionUser = this.getSessionUser();
		RsdaTrance rsdaTrance = rsdaTranceService.queryRsdaTranceById(sessionUser,radaTranceId);
		mav.addObject("rsdaTrance", rsdaTrance);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 删除人员的工作经历
	 * @param ids 工作经历的主键
	 * @param redirectPage 删除后的跳转界面
	 * @return
	 */
	@RequestMapping("/deleteRsdaTrance")
	public ModelAndView deleteRsdaTrance(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaTranceService.deleteRsdaTrance(sessionUser,ids);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		return mav;
	}
	
}