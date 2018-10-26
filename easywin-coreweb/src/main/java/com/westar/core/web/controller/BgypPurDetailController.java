package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.BgypPurDetail;
import com.westar.base.model.UserInfo;
import com.westar.core.service.BgypPurDetailService;

/**
 * 采购单详情
 * @author zzq
 *
 */
@Controller
@RequestMapping("/bgypPurDetail")
public class BgypPurDetailController extends BaseController{

	@Autowired
	BgypPurDetailService bgypPurDetailService;
	
	/**
	 * 分页查询采购单详情
	 * @param bgypPurDetail
	 * @return
	 */
	@RequestMapping("/listPagedBgypPurDetail")
	public ModelAndView listPagedBgypPurDetail(BgypPurDetail bgypPurDetail){
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		List<BgypPurDetail> listBgypPurDetails =  bgypPurDetailService.listPagedBgypPurDetail(sessionUser,bgypPurDetail);
		mav.addObject("listBgypPurDetails", listBgypPurDetails);
		mav.addObject("UserInfo", sessionUser);
		return mav;
	}
	/**
	 * 异步查询采购单详情
	 * @param bgypPurDetail
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listBgypPurDetail")
	public Map<String,Object> listBgypPurDetail(BgypPurDetail bgypPurDetail){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<BgypPurDetail> listBgypPurDetails =  bgypPurDetailService.listBgypPurDetail(sessionUser,bgypPurDetail);
		map.put("listBgypPurDetails", listBgypPurDetails);
		return map;
	}
	
	/**
	 * 异步修改采购单详情条目
	 * @param bgypPurDetailId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateBgypPurDetails")
	public Map<String,Object> updateBgypPurDetail(String bgypPurDetailStr){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		bgypPurDetailService.updateBgypPurDetail(sessionUser,bgypPurDetailStr);
		return map;
	}
	/**
	 * 异步删除采购单详情条目
	 * @param bgypPurDetailId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delBgypPurDetail")
	public Map<String,Object> delBgypPurDetail(Integer bgypPurDetailId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		bgypPurDetailService.delBgypPurDetail(sessionUser,bgypPurDetailId);
		map.put("status", "y");
		return map;
	}
	
	
}