package com.westar.core.web.controller;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.JobHistory;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.JobHistoryService;
import com.westar.core.service.UserInfoService;

/**
 * 工作经历
 * @author zzq
 *
 */
@Controller
@RequestMapping("/jobHistory")
public class JobHistoryController extends BaseController{

	@Autowired
	JobHistoryService jobHistoryService;
	
	@Autowired
	UserInfoService userInfoService;
	/**
	 * 分页查询工作经历信息
	 * @param jobHistory
	 * @return
	 */
	@RequestMapping("/listPagedJobHistory")
	public ModelAndView listPagedJobHistory(JobHistory jobHistory){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<JobHistory> listJobHistory = jobHistoryService.listPagedJobHistory(sessionUser,jobHistory);
		mav.addObject("listJobHistory", listJobHistory);
		
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_RSDA);
		return mav;
	}
	/**
	 * 分页查询指定人员工作经历信息
	 * @param jobHistory
	 * @return
	 */
	@RequestMapping("/listPagedUserJobHistory")
	public ModelAndView listPagedUserJobHistory(JobHistory jobHistory){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/listUserJobHistory");
		UserInfo sessionUser = this.getSessionUser();
		List<JobHistory> listJobHistory = jobHistoryService.listPagedJobHistory(sessionUser,jobHistory);
		mav.addObject("listJobHistory", listJobHistory);
		
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 跳转添加人员的工作经历
	 * @param userId 人员主键
	 * @return
	 */
	@RequestMapping("/addJobHistoryPage")
	public ModelAndView addJobHistoryPage(Integer userId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/addJobHistory");
		UserInfo sessionUser = this.getSessionUser();
		if(null!=userId && userId>0){
			UserInfo userInfo = userInfoService.getUserBaseInfo(sessionUser.getComId(), userId);
			mav.addObject("userInfo", userInfo);
		}
		return mav;
	}
	/**
	 * 添加人员的工作经历
	 * @param jobHistory 工作经历
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/addJobHistory")
	public ModelAndView addJobHistory(JobHistory jobHistory,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		jobHistoryService.addJobHistory(sessionUser,jobHistory);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	
	/**
	 * 跳转修改人员的工作经历
	 * @param jobHisId 人员主键
	 * @return
	 */
	@RequestMapping("/updateJobHistoryPage")
	public ModelAndView updateJobHistoryPage(Integer jobHisId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/updateJobHistory");
		UserInfo sessionUser = this.getSessionUser();
		JobHistory jobHistory = jobHistoryService.queryJobHistoryById(sessionUser,jobHisId);
		mav.addObject("jobHistory", jobHistory);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	
	/**
	 * 修改人员的工作经历
	 * @param jobHistory 工作经历
	 * @param redirectPage 跳转页面
	 * @return
	 */
	@RequestMapping("/updateJobHistory")
	public ModelAndView updateJobHistory(JobHistory jobHistory,String iframeTag){
		UserInfo sessionUser = this.getSessionUser();
		jobHistoryService.updateJobHistory(sessionUser,jobHistory);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		if(!StringUtils.isEmpty(iframeTag)){
			return new ModelAndView("/closePage");
		}else{
			return new ModelAndView("/refreshParent");
		}
	}
	/**
	 * 查看工作经历信息
	 * @param jobHisId 工作经历主键
	 * @return
	 */
	@RequestMapping("/viewJobHistoryPage")
	public ModelAndView viewJobHistoryPage(Integer jobHisId){
		ModelAndView mav = new ModelAndView("/zhbg/rsda/viewJobHistory");
		UserInfo sessionUser = this.getSessionUser();
		JobHistory jobHistory = jobHistoryService.queryJobHistoryById(sessionUser,jobHisId);
		mav.addObject("jobHistory", jobHistory);
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	/**
	 * 删除人员的工作经历
	 * @param ids 工作经历的主键
	 * @param redirectPage 删除后的跳转界面
	 * @return
	 */
	@RequestMapping("/deleteJobHistory")
	public ModelAndView deleteJobHistory(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		jobHistoryService.deleteJobHistory(sessionUser,ids);
		this.setNotification(Notification.SUCCESS, "操作成功!");
		return mav;
	}
	
}