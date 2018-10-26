package com.westar.core.web.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.PersonAttention;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.core.service.PersonAttentionService;

@Controller
@RequestMapping("/personAttention")
public class PersonAttentionController extends BaseController{

	@Autowired
	PersonAttentionService personAttentionService;
	
	/**
	 *分页查询关注人员
	 * @param personAttention
	 * @return
	 */
	@RequestMapping("/listPagedPersonAttention")
	public ModelAndView listPagedPersonAttention(PersonAttention personAttention) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo userInfo = this.getSessionUser();
		List<PersonAttention> lists = personAttentionService.listPagedPersonAttention(personAttention,userInfo);
		mav.addObject("lists", lists);
		mav.addObject("userInfo", userInfo);
		mav.addObject("personAttention", personAttention);
		return mav;
	}
	
	/**
	 * 关注人员新增
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addPersonAttention")
	public String addPersonAttention(Integer[] userIds) {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = personAttentionService.addPersonAttention(userIds, userInfo);
		if (succ) {
			return "添加成功";
		} else {
			return "添加失败";
		}
	}
	
	/**
	 * 关注人员更新
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePersonAttention")
	public String updatePersonAttention(Integer[] userIds) {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = personAttentionService.updatePersonAttention(userIds, userInfo);
		if (succ) {
			return "更新成功";
		} else {
			return "更新失败";
		}
	}
	
	/**
	 * 删除个人关注人员
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delPersonAttentions")
	public ModelAndView delPersonAttentions(Integer[] ids,String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		boolean succ = personAttentionService.delPersonAttentions(ids, userInfo);
		if (succ) {
			this.setNotification(Notification.SUCCESS, "删除成功");
		} else {
			this.setNotification(Notification.ERROR, "删除失败");
		}
		return new ModelAndView("redirect:"+redirectPage);
	}
	
}