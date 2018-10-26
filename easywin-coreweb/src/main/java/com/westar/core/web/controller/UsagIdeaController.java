package com.westar.core.web.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.UsagIdea;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.core.service.UsagIdeaService;

@Controller
@RequestMapping("/usagIdea")
public class UsagIdeaController extends BaseController{

	@Autowired
	UsagIdeaService usagIdeaService;
	
	/**
	 * 分页查询常用意见
	 * @param usagIdea
	 * @return
	 */
	@RequestMapping("/listPagedUsagIdea")
	public ModelAndView listPagedUsagIdea(UsagIdea usagIdea){
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo userInfo = this.getSessionUser();
		//创建人
		usagIdea.setUserId(userInfo.getId());
		usagIdea.setComId(userInfo.getComId());
		//意见集合
		List<UsagIdea> list = usagIdeaService.listPagedUsagIdea(usagIdea);
		mav.addObject("list", list);
		mav.addObject("userInfo",userInfo);
		return mav;
		
	}
	/**
	 * 分页查询常用意见
	 * @param usagIdea
	 * @return
	 */
	@RequestMapping("/listPagedUsagIdeaForUse")
	public ModelAndView listPagedUsagIdeaForUse(UsagIdea usagIdea){
		ModelAndView mav = new ModelAndView("/usagidea/listPagedUsagIdeaForUse");
		UserInfo userInfo = this.getSessionUser();
		//创建人
		usagIdea.setUserId(userInfo.getId());
		usagIdea.setComId(userInfo.getComId());
		//意见集合
		List<UsagIdea> list = usagIdeaService.listPagedUsagIdea(usagIdea);
		mav.addObject("list", list);
		return mav;
		
	}
	
	/**
	 * 添加意见页面跳转
	 * @return
	 */
	@RequestMapping("/addUsagIdeaPage")
	public ModelAndView addUsagIdeaPage() {
		ModelAndView mav = new ModelAndView("/usagidea/addUsagIdea");
		return mav;
	}
	/**
	 * 添加意见
	 * @param usagIdea
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxAddUsagIdea",method = RequestMethod.POST)
	public Map<String,Object> ajaxAddUsagIdea(UsagIdea usagIdea){
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		//创建人
		usagIdea.setUserId(userInfo.getId());
		usagIdea.setComId(userInfo.getComId());
		
		usagIdeaService.addUsagIdea(usagIdea);
		
		this.setNotification(Notification.SUCCESS, "添加成功！");
		
		map.put("status", "y");
		
		return map;
	}
	/**
	 * 修改意见页面跳转
	 * @return
	 */
	@RequestMapping("/updateUsagIdeaPage")
	public ModelAndView updateUsagIdeaPage(Integer id) {
		ModelAndView mav = new ModelAndView("/usagidea/updateUsagIdea");
		UsagIdea usagIdea = usagIdeaService.getUsagIdeaById(id);
		mav.addObject("usagIdea", usagIdea);
		return mav;
	}
	/**
	 * 修改常用意见
	 * @param usagIdea
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/ajaxUpdateUsagIdea",method = RequestMethod.POST)
	public Map<String,Object> ajaxUpdateUsagIdea(UsagIdea usagIdea){
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		usagIdeaService.updateUsagIdea(usagIdea);
		this.setNotification(Notification.SUCCESS, "修改成功");
		
		map.put("status", "y");
		return map;
	}

	/**
	 * 删除常用意见
	 * @param ids
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping(value="/delUsagIdea")
	public ModelAndView delUsagIdea(Integer[] ids,String redirectPage) {
		usagIdeaService.delUsagIdea(ids);
		this.setNotification(Notification.SUCCESS, "删除成功！");
		return new ModelAndView("redirect:"+redirectPage);
	}
	
	
}