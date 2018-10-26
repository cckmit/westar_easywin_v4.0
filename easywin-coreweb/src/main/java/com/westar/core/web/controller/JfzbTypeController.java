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
import com.westar.base.model.JfzbType;
import com.westar.base.model.UserInfo;
import com.westar.core.service.JfzbTypeService;

/**
 * 描述:积分指标类型维护
 * @author zzq
 * @date 2018年1月18日 上午10:56:52
 */
@Controller
@RequestMapping("/jfzb/jfzbType")
public class JfzbTypeController extends BaseController{

	@Autowired
	JfzbTypeService jfzbTypeService;
	
	/**
	 * 积分指标类型维护
	 * @param jfzbType
	 * @return
	 */
	@RequestMapping("/listJfzbTypePage")
	public ModelAndView listJfzbTypePage() {
		ModelAndView view = new ModelAndView("/jfzb/jfzbType/listJfzbTypePage");
		UserInfo sessionUser = this.getSessionUser();
		view.addObject("userInfO", sessionUser);
		return view;
	}
	/**
	 * 异步获取积分指分类
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxListJfzbType")
	public Map<String,Object> ajaxListJfzbType() {
		Map<String,Object> map = new  HashMap<String,Object>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//取得团队的积分指标类型维护
		List<JfzbType> listJfzbTypes = jfzbTypeService.listJfzbType(sessionUser.getComId());
		map.put("status", "y");
		map.put("list", listJfzbTypes);
		return map;
				
	}
	

	/**
	 * 异步指标的最大排序号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxGetJfzbTypeOrder")
	public Map<String, Object> ajaxGetJfzbTypeOrder() {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Integer orderNum = jfzbTypeService.queryJfzbTypeOrderMax(sessionUser.getComId());
		// 取出 排序号
		map.put("orderNum", orderNum);

		map.put("status", "y");
		return map;
	}

	/**
	 * 异步添加积分指标类型
	 * @param jfzbType 积分指标类型
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxAddJfzbType")
	public Map<String, Object> ajaxAddJfzbType(JfzbType jfzbType) {

		UserInfo userInfo = this.getSessionUser();
		jfzbType.setComId(userInfo.getComId());
		
		Integer id = jfzbTypeService.addJfzbType(jfzbType, userInfo);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", "y");
		return map;
	}

	/**
	 * 更新客户类型名称
	 * 
	 * @param customerType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/typeNameUpdate")
	public Map<String,Object> typeNameUpdate(JfzbType jfzbType){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		jfzbType.setComId(sessionUser.getComId());
		jfzbTypeService.updateTypeName(jfzbType, sessionUser);
		map.put("status", "y");
		return map;
	}

	/**
	 * 更新积分指标类型排序
	 * @param jfzbType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/dicOrderUpdate")
	public Map<String,Object> dicOrderUpdate(JfzbType jfzbType){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		jfzbType.setComId(sessionUser.getComId());
		jfzbTypeService.dicOrderUpdate(jfzbType, sessionUser);
		map.put("status", "y");
		return map;
	}
	/**
	 * 删除积分指标类型
	 * @param jfzbType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delJfbzType")
	public Map<String,Object> delJfbzType(JfzbType jfzbType){
		Map<String,Object> map = new HashMap<String,Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		map = jfzbTypeService.delJfbzType(jfzbType, sessionUser);
		return map;
	}
	
}