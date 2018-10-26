package com.westar.core.web.controller;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.RsdaIncentive;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.RsdaIncService;
import com.westar.core.service.UserInfoService;

/**
 * 奖惩模块
 * @author zzq
 *
 */
@Controller
@RequestMapping("/rsdaInc")
public class RsdaIncController extends BaseController{

	@Autowired
	RsdaIncService rsdaIncService;
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 分页查询激励机制信息
	 * @param jobHistory
	 * @return
	 */
	@RequestMapping("/listPagedRsdaInc")
	public ModelAndView listPagedRsdaInc(RsdaIncentive rsdaInc){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaIncentive> listRsdaInc = rsdaIncService.listPagedRsdaInc(sessionUser,rsdaInc);
		mav.addObject("listRsdaInc", listRsdaInc);
		
		mav.addObject("userInfo", sessionUser);
		
		mav.addObject("rsdaInc", rsdaInc);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_RSDA);
		return mav;
	}
	
	/**
	 * 列出指定人员的奖惩信息
	 * @param rsdaInc
	 * @return
	 */
	@RequestMapping("/listPagedUserRsdaInc")
	public ModelAndView listPagedUserRsdaInc(RsdaIncentive rsdaInc){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/listUserRsdaInc");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaIncentive> listRsdaInc = rsdaIncService.listPagedRsdaInc(sessionUser,rsdaInc);
		mav.addObject("listRsdaInc", listRsdaInc);
		
		mav.addObject("userInfo", sessionUser);
		
		return mav;
	}
	
	/**
	 * 跳转添加人员的激励机制
	 * @param userId 人员主键
	 * @return
	 */
	@RequestMapping("/addRsdaIncPage")
	public ModelAndView addRsdaIncPage(Integer userId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/addRsdaInc");
		UserInfo sessionUser = this.getSessionUser();
		if(null!=userId && userId>0){
			UserInfo userInfo = userInfoService.getUserBaseInfo(sessionUser.getComId(), userId);
			mav.addObject("userInfo", userInfo);
		}
		return mav;
	}
	/**
	 * 添加人员的激励机制
	 * @param rsdaInc 激励机制
	 * @param iframeTag 跳转页面
	 * @return
	 */
	@RequestMapping("/addRsdaInc")
	public ModelAndView addRsdaInc(RsdaIncentive rsdaInc,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaIncService.addRsdaInc(sessionUser, rsdaInc);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
		
	}
	
	/**
	 * 跳转修改人员的激励机制
	 * @param rsdaIncId 人员主键
	 * @return
	 */
	@RequestMapping("/updateRsdaIncPage")
	public ModelAndView updateRsdaIncPage(Integer rsdaIncId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/updateRsdaInc");
		UserInfo sessionUser = this.getSessionUser();
		RsdaIncentive rsdaInc = rsdaIncService.queryRsdaIncById(sessionUser, rsdaIncId);
		mav.addObject("rsdaInc", rsdaInc);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 修改人员的激励机制
	 * @param jobHistory 工作经历
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaInc")
	public ModelAndView updateRsdaInc(RsdaIncentive rsdaInc,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaIncService.updateRsdaInc(sessionUser, rsdaInc);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	/**
	 * 查看奖励信息
	 * @param rsdaIncId
	 * @return
	 */
	@RequestMapping("/viewRsdaIncPage")
	public ModelAndView viewRsdaIncPage(Integer rsdaIncId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/viewRsdaInc");
		UserInfo sessionUser = this.getSessionUser();
		RsdaIncentive rsdaInc = rsdaIncService.queryRsdaIncById(sessionUser, rsdaIncId);
		mav.addObject("rsdaInc", rsdaInc);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 删除人员的激励机制
	 * @param ids 激励机制的主键
	 * @param redirectPage 删除后的跳转界面
	 * @return
	 */
	@RequestMapping("/deleteRsdaInc")
	public ModelAndView deleteRsdaInc(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaIncService.deleteRsdaInc(sessionUser,ids);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		return mav;
	}
	
	
}