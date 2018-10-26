package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.BgypApply;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.BgypApplyService;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.TodayWorksService;

/**
 * 申领记录
 * @author zzq
 *
 */
@Controller
@RequestMapping("/bgypApply")
public class BgypApplyController extends BaseController{

	@Autowired
	BgypApplyService bgypApplyService;
	
	@Autowired
	TodayWorksService todayWorksService;
	/**
	 * 分页查询申领记录信息
	 * @param bgypPurOrder
	 * @return
	 */
	@RequestMapping("/listPagedBgypApply")
	public ModelAndView listPagedBgypApply(BgypApply bgypApply){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<BgypApply> listBgypApplys = bgypApplyService.listPagedBgypApply(sessionUser,bgypApply);
		
		mav.addObject("listBgypApplys", listBgypApplys);
		
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_BGYP);
		return mav;
	}
	/**
	 * 分页查询申领记录信息
	 * @param bgypPurOrder
	 * @return
	 */
	@RequestMapping("/listPagedSpBgypApply")
	public ModelAndView listPagedSpBgypApply(BgypApply bgypApply){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<BgypApply> listBgypApplys = bgypApplyService.listPagedSpBgypApply(sessionUser,bgypApply);
		
		mav.addObject("listBgypApplys", listBgypApplys);
		
		mav.addObject("userInfo", sessionUser);
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_BGYP);
		return mav;
	}
	/**
	 * 跳转到添加申领界面
	 * @return
	 */
	@RequestMapping("/addBgypApplyPage")
	public ModelAndView addBgypApplyPage(){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/addBgypApply");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	/**
	 * 添加 办公用品采购单
	 * @param bgypPurOrder
	 * @return
	 */
	@RequestMapping("/addBgypApply")
	public ModelAndView addBgypApply(BgypApply bgypApply){
		UserInfo sessionUser = this.getSessionUser();
		bgypApplyService.addBgypApply(sessionUser,bgypApply);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		ModelAndView mav = new ModelAndView("/refreshParent");
		return mav;
	}
	/**
	 * 查询采购清单信息
	 * @param purOrderId 采购清单主键
	 * @return
	 */
	@RequestMapping("/updateBgypApplyPage")
	public ModelAndView updateBgypApplyPage(Integer applyId){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/updateBgypApply");
		UserInfo sessionUser = this.getSessionUser();
		BgypApply bgypApply = bgypApplyService.queryBgypApplyById(sessionUser.getComId(),applyId);
		mav.addObject("bgypApply", bgypApply);
		return mav;
	}
	/**
	 * 异步修改申领单信息
	 * @param bgypApply 申领单信息
	 * @return
	 */
	@RequestMapping("/updateBgypApply")
	public ModelAndView updateBgypApply(BgypApply bgypApply){
		UserInfo sessionUser = this.getSessionUser();
		bgypApplyService.updateBgypApply(sessionUser,bgypApply);
		
		this.setNotification(Notification.SUCCESS, "操作成功!");
		ModelAndView mav = new ModelAndView("/refreshParent");
		return mav;
	}
	/**
	 * 查看申领单信息
	 * @param applyId 申领单的主键
	 * @return
	 */
	@RequestMapping("/viewBgypApplyPage")
	public ModelAndView viewBgypApplyPage(Integer applyId){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/viewBgypApply");
		UserInfo sessionUser = this.getSessionUser();
		//查询申领单的基本信息
		BgypApply bgypApply = bgypApplyService.queryBgypApplyById(sessionUser.getComId(),applyId);
		mav.addObject("bgypApply", bgypApply);
		
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(applyId,sessionUser.getComId(), sessionUser.getId(), 
				ConstantInterface.TYPE_BGYP_APPLY_CHECK,0);
				
		return mav;
	}
	
	/**
	 * 修改申领单审核状态
	 * @param applyId 申领单的主键
	 * @param applyState 申领但的审核状态
	 * @param reason 审核不通过的理由
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateBgypApplyState")
	public Map<String,Object> updateBgypApplyState(Integer applyId,Integer applyUserId,String applyState,String spContent){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		
		//构建数据信息
		BgypApply bgypApply = new BgypApply();
		bgypApply.setId(applyId);
		bgypApply.setApplyCheckState(applyState);
		bgypApply.setApplyUserId(applyUserId);
		//修改审核状态
		map = bgypApplyService.updateBgypApplyState(sessionUser,bgypApply,spContent);
		return map;
	}
	/**
	 * 删除申领单
	 * @param ids 申领记录的主键
	 * @param redirectPage 删除后的跳转记录
	 * @return
	 */
	@RequestMapping("/deleteBgypApply")
	public ModelAndView deleteBgypPurOrder(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		if(null!=ids && ids.length>0){
			bgypApplyService.deleteBgypApply(sessionUser,ids);
			this.setNotification(Notification.SUCCESS, "操作成功");
		}
		return mav;
	}
	
}