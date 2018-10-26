package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Jfzb;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.JfzbService;

/**
 * 描述:积分指标
 * @author zzq
 * @date 2018年1月18日 上午10:55:04
 */
@Controller
@RequestMapping("/jfzb")
public class JfzbController extends BaseController{

	@Autowired
	JfzbService jfzbService;
	/**
	 * 分页查询积分指标信息仅用于查询和查看
	 * @param jfzb
	 * @return
	 */
	@RequestMapping("/listPagedJfzb")
	public ModelAndView listPagedJfzb(Jfzb jfzb) {
		
		ModelAndView view = new ModelAndView("/zhbg/zhbgCenter");
		UserInfo sessionUser = this.getSessionUser();
		List<Jfzb> list = jfzbService.listPagedJfbz(jfzb,sessionUser);
		view.addObject("userInfo", sessionUser);
		view.addObject("list", list);
		
		//头文件的显示
		view.addObject("homeFlag",ConstantInterface.TYPE_ZHBG);
		return view;
		
	}
	/**
	 * 跳转到添加积分指标界面
	 * @return
	 */
	@RequestMapping("/addJfzbPage")
	public ModelAndView addJfzbPage() {
		ModelAndView view = new ModelAndView("/jfzb/addJfzb");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		return view;
	}
	/**
	 * 添加积分指标
	 * @param jfzb
	 * @return
	 */
	@RequestMapping("/addJfzb")
	public ModelAndView addJfzb(Jfzb jfzb) {
		ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo sessionUser = this.getSessionUser();
		jfzbService.addJfzb(jfzb,sessionUser);
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return view;
	}
	/**
	 * 跳转修改积分指标界面
	 * @param jfzbId
	 * @return
	 */
	@RequestMapping("/updateJfzbPage")
	public ModelAndView updateJfzbPage(Integer jfzbId){
		ModelAndView view = new ModelAndView("/jfzb/updateJfzb");
		UserInfo sessionUser = this.getSessionUser();
		Jfzb jfzb = jfzbService.queryJfzbById(jfzbId);
		view.addObject("jfzb", jfzb);
		view.addObject("userInfo", sessionUser);
		return view;
	}
	/**
	 * 修改积分标准信息
	 * @param jfzb 积分标准信息
	 * @return
	 */
	@RequestMapping("/updateJfzb")
	public ModelAndView updateJfzb(Jfzb jfzb) {
		ModelAndView view = new ModelAndView(this.LAYER_CLOSE_REFRESHPARENT);
		UserInfo sessionUser = this.getSessionUser();
		jfzbService.updateJfzb(jfzb,sessionUser);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return view;
	}
	/**
	 * 查看积分标准信息
	 * @param jfzbId
	 * @return
	 */
	@RequestMapping("/viewJfzbPage")
	public ModelAndView viewJfzbPage(Integer jfzbId){
		ModelAndView view = new ModelAndView("/jfzb/viewJfzb");
		UserInfo sessionUser = this.getSessionUser();
		Jfzb jfzb = jfzbService.queryJfzbById(jfzbId);
		view.addObject("jfzb", jfzb);
		view.addObject("userInfo", sessionUser);
		return view;
	}
	/**
	 * 删除积分标准
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/deleteJfzb")
	public ModelAndView deleteJfzb(Integer[] ids,String redirectPage){
		ModelAndView view = new ModelAndView("redirect:"+redirectPage);
		UserInfo sessionUser = this.getSessionUser();
		jfzbService.deleteJfzb(ids,sessionUser);
		this.setNotification(Notification.SUCCESS, "删除成功!");
		return view;
		
	}
	/**
	 * 跳转到积分标准选择界面
	 * @return
	 */
	@RequestMapping("/lisJfzbForRelevance")
	public ModelAndView lisJfzbForRelevance() {
		
		ModelAndView view = new ModelAndView("/jfzb/lisJfzbForRelevance");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfo", sessionUser);
		return view;
		
	}
	/**
	 * 异步获取积分指标信息
	 * @param jfzb 积分指标
	 * @param pageNum 页码
	 * @param pageSize 分页信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListJfzbForSelect")
	public Map<String, Object> ajaxListJfzbForSelect(Jfzb jfzb) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if (null == userInfo) {
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		
		List<Jfzb> lists = jfzbService.listAllJfzb(jfzb, userInfo);
		map.put("lists", lists);
		
		map.put("status", "y");
		return map;
	}
	
}