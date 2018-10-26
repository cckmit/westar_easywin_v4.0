package com.westar.core.web.controller;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.RsdaLeave;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.RsdaLeaveService;
import com.westar.core.service.UserInfoService;

/**
 * 离职管理
 * @author zzq
 *
 */
@Controller
@RequestMapping("/rsdaLeave")
public class RsdaLeaveController extends BaseController{

	@Autowired
	RsdaLeaveService rsdaLeaveService;
	
	@Autowired
	UserInfoService userInfoService;
	/**
	 * 分页查询离职信息
	 * @param rsdaLeave 离职的查询条件
	 * @return
	 */
	@RequestMapping("/listPagedRsdaLeave")
	public ModelAndView listPagedRsdaLeave(RsdaLeave rsdaLeave){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaLeave> listRsdaLeaves = rsdaLeaveService.listPagedRsdaLeave(sessionUser,rsdaLeave);
		mav.addObject("listRsdaLeaves", listRsdaLeaves);
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_RSDA);
		return mav;
	}
	/**
	 * 分页查询指定人员离职信息
	 * @param rsdaLeave 离职人员
	 * @return
	 */
	@RequestMapping("/listPagedUserRsdaLeave")
	public ModelAndView listPagedUserJobHistory(RsdaLeave rsdaLeave){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/listUserRsdaLeave");
		UserInfo sessionUser = this.getSessionUser();
		List<RsdaLeave> listRsdaLeaves = rsdaLeaveService.listPagedRsdaLeave(sessionUser,rsdaLeave);
		mav.addObject("listRsdaLeaves", listRsdaLeaves);
		
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 跳转添加人员的离职经历
	 * @param userId 人员主键
	 * @return
	 */
	@RequestMapping("/addRsdaLeavePage")
	public ModelAndView addRsdaLeavePage(Integer userId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/addRsdaLeave");
		UserInfo sessionUser = this.getSessionUser();
		if(null!=userId && userId>0){
			UserInfo userInfo = userInfoService.getUserBaseInfo(sessionUser.getComId(), userId);
			mav.addObject("userInfo", userInfo);
		}
		
		return mav;
	}
	/**
	 * 添加人员的离职经历
	 * @param rsdaLeave 离职经历
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/addRsdaLeave")
	public ModelAndView addRsdaLeave(RsdaLeave rsdaLeave,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaLeaveService.addRsdaLeave(sessionUser,rsdaLeave);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	
	/**
	 * 跳转修改人员的离职经历
	 * @param rsdaLeaveId 离职主键
	 * @return
	 */
	@RequestMapping("/updateRsdaLeavePage")
	public ModelAndView updateJobHistoryPage(Integer rsdaLeaveId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/updateRsdaLeave");
		UserInfo sessionUser = this.getSessionUser();
		RsdaLeave rsdaLeave = rsdaLeaveService.queryRsdaLeaveById(sessionUser,rsdaLeaveId);
		mav.addObject("rsdaLeave", rsdaLeave);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 修改人员的离职经历
	 * @param rsdaLeave 离职经历
	 * @param iframeTag 跳转页面
	 * @return
	 */
	@RequestMapping("/updateRsdaLeave")
	public ModelAndView updateRsdaLeave(RsdaLeave rsdaLeave,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		rsdaLeaveService.updateRsdaLeave(sessionUser,rsdaLeave);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	/**
	 * 查看离职经历信息
	 * @param rsdaLeaveId 工作经历主键
	 * @return
	 */
	@RequestMapping("/viewRsdaLeavePage")
	public ModelAndView viewRsdaLeavePage(Integer rsdaLeaveId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/viewRsdaLeave");
		UserInfo sessionUser = this.getSessionUser();
		RsdaLeave rsdaLeave = rsdaLeaveService.queryRsdaLeaveById(sessionUser,rsdaLeaveId);
		mav.addObject("rsdaLeave", rsdaLeave);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	/**
	 * 删除人员的离职经历
	 * @param ids 离职经历的主键
	 * @param redirectPage 删除后的跳转界面
	 * @return
	 */
	@RequestMapping("/deleteRsdaLeave")
	public ModelAndView deleteRsdaLeave(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		rsdaLeaveService.deleteRsdaLeave(sessionUser,ids);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		return mav;
	}
	
	
}