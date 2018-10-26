package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.UserConf;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.UserConfService;

/**
 * 
 * 描述:人员配置的实体类
 * @author zzq
 * @date 2018年8月25日 上午11:54:09
 */
@Controller
@RequestMapping("/userConf")
public class UserConfController extends BaseController{

	@Autowired
	UserConfService userConfService;
	
	/*********************以下是消息设置***************************************/
	/**
	 * 弹窗消息通知
	 * @return
	 */
	@RequestMapping("/msgShowSettingPage")
	public ModelAndView msgShowSettingPage(){
		
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo userInfo = this.getSessionUser();
		
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	
	/**
	 * 异步取得个人配置信息
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajacListUserConfig")
	public Map<String, Object> ajacListUserConfig(String type){
		//当前操作员
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(type.equals(ConstantInterface.UCFG_SYS_SWITCH)){
			List<UserConf> listUserConfig = userConfService.listUserConfig(sessionUser,type);
			map.put("listUserConfig", listUserConfig);
		}else if(type.equals(ConstantInterface.UCFG_MENU_NUM)){
			UserConf userConf = new UserConf();
			userConf.setSysConfCode("-1");
			userConf.setType(type);
			UserConf userConfVo = userConfService.queryUserConf(sessionUser, userConf);
			map.put("userConf", userConfVo);
		}
		
		
		map.put("status", "y");
		return map;
	}
	
	
	/**
	 * 平台开关设置
	 * @param sysConfCode
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/switchSet")
	public Map<String, Object> switchSet(UserConf userConf){
		//当前操作员
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String sysConfCode = userConf.getSysConfCode();
		//修改配置信息信息
		Integer openState = userConfService.updateUserConf(sessionUser, userConf);
		if(null!=sysConfCode && ConstantInterface.MOD_OPT_STATE_YES.equals(sysConfCode)){
			//当前操作员的弹窗设置
			sessionUser.setAutoEject(openState.toString());
			this.updateSessionUser(sessionUser);
		}
		map.put("status", "y");
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/updateUserConf")
	public Map<String, Object> updateUserConf(UserConf userConf){
		//当前操作员
		UserInfo sessionUser = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		userConfService.updateUserConf(sessionUser, userConf);
		map.put("status", "y");
		return map;
	}
	/*********************以上是消息设置***************************************/
	
	
	
	
}