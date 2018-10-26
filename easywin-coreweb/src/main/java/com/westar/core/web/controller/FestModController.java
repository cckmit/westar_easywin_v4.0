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
import com.westar.base.model.AttenceRule;
import com.westar.base.model.FestMod;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.service.AttenceService;
import com.westar.core.service.FestModService;

/**
 * 
 * 描述: 节假日控制类
 * @author zzq
 * @date 2018年8月25日 上午11:51:16
 */
@Controller
@RequestMapping("/festMod")
public class FestModController extends BaseController{

	@Autowired
	FestModService festModService;
	
	@Autowired
	AttenceService attenceService;
	
	/**
	 * 查询节假日维护
	 * @return
	 */
	@RequestMapping("/listFestMod")
	public ModelAndView listFestMod(FestMod festMod,String sid) {
		UserInfo sessionUser = this.getSessionUser();
		if(sessionUser.getAdmin().equals("0")){//非管理人员不得操作
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/index?sid="+sid);
			this.setNotification(Notification.ERROR, "抱歉，你没有管理权限！");
			return mav;
		}
		
		//当前年份
		Integer nowYear = DateTimeUtil.getYear();
		//选中的年份
		Integer year = festMod.getYear();
		if(null == year || year == 0){//没有选择，默认本年
			festMod.setYear(nowYear);
		}
		
		String states = festMod.getStatus();
		if(CommonUtil.isNull(states)){//默认查询假期
			festMod.setStatus("1");
		}
		
		
		//查询采用的考勤制度
		AttenceRule attenceRule = attenceService.getAttenceRule(null,sessionUser.getComId());
		String ruleType = attenceRule.getRuleType();
		
		//查询的节假日
		List<FestMod> listFestMod = festModService.listFestMod(sessionUser,festMod,ruleType);
		
		
//		ModelAndView mav = new ModelAndView("/organic/organicCenter");
		
		ModelAndView mav = new ModelAndView("/adminCfg/adminCfg_center");
		mav.addObject("toPage","../festMod/listFestMod.jsp");
		
		//当前操作员
		mav.addObject("userInfo", sessionUser);
		//考勤制度
		mav.addObject("ruleType", ruleType);
		//节假日
		mav.addObject("listFestMod", listFestMod);
		
		return mav;
	}
	/**
	 * 跳转添加节假日维护界面
	 * @return
	 */
	@RequestMapping("/addFestModPage")
	public ModelAndView addFestModPage(){
		ModelAndView mav = new ModelAndView("/festMod/addFestMod");
		return mav;
	}
	
	/**
	 * 添加节假日维护
	 * @param festMod
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addFestMod")
	public Map<String,Object> addFestMod(FestMod festMod){
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//添加节假日维护
		festModService.addFestMod(festMod,sessionUser);
		map.put("status", "y");
		this.setNotification(Notification.SUCCESS, "添加成功！");
		return map;
	}
	/**
	 * 跳转添加节假日维护界面
	 * @return
	 */
	@RequestMapping("/updateFestModPage")
	public ModelAndView updateFestModPage(Integer festModId){
		ModelAndView mav = new ModelAndView("/festMod/updateFestMod");
		FestMod festMod = festModService.getFestModById(festModId);
		mav.addObject("festMod", festMod);
		return mav;
	}
	/**
	 * 修改节假日维护
	 * @param festMod
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateFestMod")
	public Map<String,Object> updateFestMod(FestMod festMod){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//修改节假日维护
		festModService.updateFestMod(festMod,sessionUser);
		this.setNotification(Notification.SUCCESS, "修改成功！");
		map.put("status", "y");
		return map;
	}
	/**
	 * 删除节假日维护
	 * @param festModId
	 * @param comId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delFestMod")
	public Map<String,Object> delFestMod(Integer festModId,Integer comId){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}else if(null==comId){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}else if(!sessionUser.getComId().equals(comId)){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//修改节假日维护
		festModService.delFestMod(festModId,sessionUser);
		map.put("status", "y");
		this.setNotification(Notification.SUCCESS, "删除成功！");
		return map;
	}
	
	/**
	 * 计算工作时间
	 * @param dateS 日期开始
	 * @param dateE 日期结束
	 * @param calTimeType 计算类型 1计算工作时段 2计算非工作时段
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/calDateTime")
	public Map<String,Object> calDateTime(String dateS,String dateE,String calTimeType){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		if(null == sessionUser){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}else{
			Double hours = 0.0D;
			if("1".equals(calTimeType)){//计算工作时段
				hours = festModService.calWorkTime(sessionUser.getComId(), dateS, dateE);
			}else if("2".equals(calTimeType)){//计算非工作时段
				hours = festModService.calOverTime(sessionUser.getComId(), dateS, dateE);
			}
			map.put("hour", hours.toString());
			map.put("status", "y");
		}
		return map;
	}
}