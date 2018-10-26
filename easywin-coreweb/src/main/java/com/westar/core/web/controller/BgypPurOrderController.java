package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.BgypPurOrder;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.BgypPurOrderService;
import com.westar.core.service.TodayWorksService;
/**
 * 办公用品采购单
 * @author zzq
 *
 */
@Controller
@RequestMapping("/bgypPurOrder")
public class BgypPurOrderController extends BaseController{

	@Autowired
	BgypPurOrderService bgypPurOrderService;
	
	@Autowired
	TodayWorksService todayWorksService;
	
	/**
	 * 分页查询采购单
	 * @param bgypPurOrder
	 * @return
	 */
	@RequestMapping("/listPagedBgypPurOrder")
	public ModelAndView listPagedBgypPurOrder(BgypPurOrder bgypPurOrder){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<BgypPurOrder> listBgypPurOrders = bgypPurOrderService.listPagedBgypPurOrder(sessionUser,bgypPurOrder);
		mav.addObject("listBgypPurOrders", listBgypPurOrders);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("bgypPurOrder", bgypPurOrder);
		
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_BGYP);
		return mav;
	}
	/**
	 * 分页查询审核采购单
	 * @param bgypPurOrder
	 * @return
	 */
	@RequestMapping("/listPagedSpBgypPurOrder")
	public ModelAndView listPagedSpBgypPurOrder(BgypPurOrder bgypPurOrder){
		ModelAndView mav = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		
		List<BgypPurOrder> listBgypPurOrders = bgypPurOrderService.listPagedSpBgypPurOrder(sessionUser,bgypPurOrder);
		mav.addObject("listBgypPurOrders", listBgypPurOrders);
		mav.addObject("userInfo", sessionUser);
		mav.addObject("bgypPurOrder", bgypPurOrder);
		
		//头文件的显示
		mav.addObject("homeFlag",ConstantInterface.TYPE_BGYP);
		return mav;
	}
	/**
	 * 跳转到添加 办公用品采购单界面
	 * @return
	 */
	@RequestMapping("/addBgypPurOrderPage")
	public ModelAndView addBgypPurOrderPage(){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/addBgypPurOrder");
		UserInfo sessionUser = this.getSessionUser();
		mav.addObject("userInfo", sessionUser);
		return mav;
	}
	/**
	 * 添加 办公用品采购单
	 * @param bgypPurOrder
	 * @return
	 */
	@RequestMapping("/addBgypPurOrder")
	public ModelAndView addBgypPurOrder(BgypPurOrder bgypPurOrder){
		UserInfo sessionUser = this.getSessionUser();
		bgypPurOrderService.addBgypPurOrder(sessionUser,bgypPurOrder);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		ModelAndView mav = new ModelAndView("/refreshParent");
		return mav;
	}
	/**
	 * 查询采购清单信息
	 * @param purOrderId 采购清单主键
	 * @return
	 */
	@RequestMapping("/updateBgypPurOrderPage")
	public ModelAndView updateBgypPurOrderPage(Integer purOrderId){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/updateBgypPurOrder");
		UserInfo sessionUser = this.getSessionUser();
		BgypPurOrder bgypPurOrder = bgypPurOrderService.queryBgypPurOrderById(sessionUser.getComId(),purOrderId);
		mav.addObject("bgypPurOrder", bgypPurOrder);
		return mav;
	}
	/**
	 * 异步修改采购清单信息
	 * @param bgypPurOrder
	 * @return
	 */
	@RequestMapping("/updateBgypPurOrder")
	public ModelAndView updateBgypPurOrder(BgypPurOrder bgypPurOrder){
		UserInfo sessionUser = this.getSessionUser();
		bgypPurOrderService.updateBgypPurOrder(sessionUser,bgypPurOrder);
		
		this.setNotification(Notification.SUCCESS, "操作成功!");
		ModelAndView mav = new ModelAndView("/refreshParent");
		return mav;
	}
	
	/**
	 * 查看采购清单信息
	 * @param purOrderId
	 * @return
	 */
	@RequestMapping("/viewBgypPurOrderPage")
	public ModelAndView viewBgypPurOrder(Integer purOrderId){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/viewBgypPurOrder");
		UserInfo sessionUser = this.getSessionUser();
		BgypPurOrder bgypPurOrder = bgypPurOrderService.queryBgypPurOrderById(sessionUser.getComId(),purOrderId);
		mav.addObject("bgypPurOrder", bgypPurOrder);
		
		//删除消息提醒
		todayWorksService.updateTodoWorkRead(purOrderId,sessionUser.getComId(), sessionUser.getId(), 
				ConstantInterface.TYPE_BGYP_BUY_CHECK,0);
				
		
		return mav;
	}
	/**
	 * 修改采购单审核状态
	 * @param purOrderId
	 * @param purOrderState
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateBgypPurOrderState")
	public Map<String,Object> updateBgypPurOrderState(Integer purOrderId,Integer purUserId,
			String purOrderState,String spContent){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		
		BgypPurOrder bgypPurOrder = new BgypPurOrder();
		bgypPurOrder.setId(purOrderId);
		bgypPurOrder.setPurOrderState(purOrderState);
		bgypPurOrder.setPurUserId(purUserId);
		map = bgypPurOrderService.updateBgypPurOrderState(sessionUser,bgypPurOrder,spContent);
		return map;
	}
	/**
	 * 删除采购单
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/deleteBgypPurOrder")
	public ModelAndView deleteBgypPurOrder(Integer[] ids,String redirectPage){
		ModelAndView mav = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		if(null!=ids && ids.length>0){
			bgypPurOrderService.deleteBgypPurOrder(sessionUser,ids);
			this.setNotification(Notification.SUCCESS, "操作成功");
		}
		return mav;
	}
}