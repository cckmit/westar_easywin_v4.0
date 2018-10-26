package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.BgypFl;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.BgypFlService;

@Controller
@RequestMapping("/bgypFl")
public class BgypFlController extends BaseController{

	@Autowired
	BgypFlService bgypFlService;
	
	/**
	 * 办公用品列表分类页面
	 * @return
	 */
	@RequestMapping(value="/frameBgypflPage")
	public ModelAndView frameBgypflPage(){
		ModelAndView view = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo userInfo = this.getSessionUser();
		view.addObject("userInfo",userInfo);
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_BGYP);
		return view;
	}
	
	/**
	 * 取得团队的办公用品分类
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listTreeBgypFl")
	public List<BgypFl> listTreeBgypFl(){
		UserInfo sessionUser = this.getSessionUser();
		List<BgypFl> listBgypFl = bgypFlService.listTreeBgypFl(sessionUser.getComId());
		return listBgypFl;
	}
	/**
	 * 跳转添加办公用品界面
	 * @param parentId 父类主键
	 * @param pFlName 父类名称
	 * @return
	 */
	@RequestMapping(value="/addBgypFlPage")
	public ModelAndView addBgypFlPage(Integer parentId,String pFlName){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/addBgypFl");
		mav.addObject("parentId", parentId);
		mav.addObject("pFlName", pFlName);
		return mav;
	}
	
	/**
	 * 异步添加办公用品分类
	 * @param bgypFl 办公用品分类属性
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxAddBgypFl")
	public Map<String,Object> ajaxAddBgypFl(BgypFl bgypFl){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		map = bgypFlService.addBgypFl(sessionUser,bgypFl);
		return map;
	}
	/**
	 * 取得办公用品详情
	 * @param bgypFlId
	 * @return
	 */
	@RequestMapping(value="/updateBgypFlPage")
	public ModelAndView updateBgypFlPage(Integer bgypFlId){
		ModelAndView mav = new ModelAndView("/zhbg/bgyp/updateBgypFl");
		BgypFl bgypFl = bgypFlService.queryBgypFlDetailById(bgypFlId);
		mav.addObject("bgypFl", bgypFl);
		return mav;
	}
	/**
	 * 异步修改办公用品信息
	 * @param bgypFl 办公用品信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxUpdateBgypFl")
	public Map<String,Object> ajaxUpdateBgypFl(BgypFl bgypFl){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//头文件的显示
		map = bgypFlService.updateBgypFl(sessionUser,bgypFl);
		return map;
	}
	
	/**
	 * 异步删除办公用品分类集合
	 * @param bgypFlIds 办公用品主键集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxDeleteBgypFl")
	public Map<String,Object> ajaxDeleteBgypFl(Integer[] bgypFlIds){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//头文件的显示
		map = bgypFlService.deleteBgypFls(sessionUser,bgypFlIds);
		return map;
	}
	
	
	
}