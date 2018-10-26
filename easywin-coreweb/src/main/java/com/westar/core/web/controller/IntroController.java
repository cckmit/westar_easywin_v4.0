package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.Intro;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.IntroService;

@Controller
@RequestMapping("/intro")
public class IntroController extends BaseController{

	@Autowired
	IntroService introService;
	
	/**
	 * 获取引导状态
	 * @param busType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxIntroState")
	public Map<String,Object> ajaxIntroState(String busType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Intro intro = introService.queryIntroState(busType,userInfo);
		//未进行引导
		if(null == intro){
			map.put("intro",true);
		}else{
			map.put("intro",false);
		}
		map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	
	/**
	 * 完成该模块引导，添加引导
	 * @param busType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/ajaxAddIntro")
	public Map<String,Object> ajaxAddIntro(String busType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		Intro intro = introService.queryIntroState(busType,userInfo);
		if(null == intro){
		  introService.addIntro(busType,userInfo);
		}
		map.put(ConstantInterface.TYPE_STATUS,ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
}