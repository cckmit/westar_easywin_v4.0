package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.BgypApplyDetail;
import com.westar.base.model.UserInfo;
import com.westar.core.service.BgypApplyDetailService;

/**
 * 申领单的详细信息
 * @author zzq
 *
 */
@Controller
@RequestMapping("/bgypApplyDetail")
public class BgypApplyDetailController extends BaseController{

	@Autowired
	BgypApplyDetailService bgypApplyDetailService;
	
	/**
	 * 分页查询采购单详情
	 * @param bgypApplyDetail
	 * @return
	 */
	@RequestMapping("/listPagedBgypApplyDetail")
	public ModelAndView listPagedBgypApplyDetail(BgypApplyDetail bgypApplyDetail){
		ModelAndView mav = new ModelAndView();
		UserInfo sessionUser = this.getSessionUser();
		List<BgypApplyDetail> listBgypApplyDetails =  bgypApplyDetailService.listPagedBgypApplyDetail(sessionUser,bgypApplyDetail);
		mav.addObject("listBgypApplyDetails", listBgypApplyDetails);
		mav.addObject("UserInfo", sessionUser);
		return mav;
	}
	/**
	 * 异步查询采购单详情
	 * @param bgypApplyDetail
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listBgypApplyDetail")
	public Map<String,Object> listBgypApplyDetail(BgypApplyDetail bgypApplyDetail){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		List<BgypApplyDetail> listBgypApplyDetails =  bgypApplyDetailService.listBgypApplyDetail(sessionUser,bgypApplyDetail);
		map.put("listBgypApplyDetails", listBgypApplyDetails);
		return map;
	}
	/**
	 * 异步修改采购单详情条目
	 * @param bgypPurDetailId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateBgypApplyDetails")
	public Map<String,Object> updateBgypPurDetail(String bgypApplyDetailStr){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		bgypApplyDetailService.updateBgypApplyDetails(sessionUser,bgypApplyDetailStr);
		return map;
	}
	/**
	 * 异步删除采购单详情条目
	 * @param bgypPurDetailId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delBgypApplyDetail")
	public Map<String,Object> delBgypApplyDetail(Integer applyDetailId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		bgypApplyDetailService.delBgypApplyDetail(sessionUser,applyDetailId);
		map.put("status", "y");
		return map;
	}
	
	
}