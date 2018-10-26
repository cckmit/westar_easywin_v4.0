package com.westar.core.web.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.PinyinToolkit;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.SelfGroupService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.UserInfoService;

@Controller
@RequestMapping("/selfGroup")
public class SelfGroupController extends BaseController{

	@Autowired
	SelfGroupService selfGroupService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	MsgShareService msgShareService;
	
	/**
	 * 分页查询个人分组信息
	 * @return
	 */
	@RequestMapping("/listUserGroup")
	public ModelAndView listUserGroup(SelfGroup group) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		UserInfo user = this.getSessionUser();
		group.setOwner(user.getId());
		group.setComId(user.getComId());
		List<SelfGroup> list = selfGroupService.listUserGroup(user,group);
		mav.addObject("list", list);
		mav.addObject("userInfo", user);
		mav.addObject("latestInfo", userInfoService.getUserInfo(user.getComId(),user.getId()));
		return mav;
	}
	
	/**
	 * 新增人员私有组的页面
	 * @return
	 */
	@RequestMapping("/addUserGroupPage")
	public ModelAndView addUserGroupPage() {
		return new ModelAndView("/userInfo/selfCenter").addObject("userInfo", this.getSessionUser());
	}
	/**
	 * 新增人员私有组的页面
	 * @return
	 */
	@RequestMapping("/addUseGroupPage")
	public ModelAndView addUseGroupPage() {
		return new ModelAndView("/selfGroup/addUseGroup").addObject("userInfo", this.getSessionUser());
	}
	/**
	 * 新增个人分组
	 * @param dep
	 * @return
	 */
	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public ModelAndView addGroup(SelfGroup group,String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		//企业主键
		group.setComId(userInfo.getComId());
		
		group.setOwner(userInfo.getId());
		if(null!=group.getGrpName()&&!"".equals(group.getGrpName())){
			group.setAllSpelling(PinyinToolkit.cn2Spell(group.getGrpName()));
			group.setFirstSpelling(PinyinToolkit.cn2FirstSpell(group.getGrpName()));
		}
		//添加个人分组
		Integer id = selfGroupService.addGroup(group,null);
		group.setId(id);
		//添加日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "新增分组\""+group.getGrpName()+"\"",
				ConstantInterface.TYPE_USER,userInfo.getComId(),userInfo.getOptIP());
		this.setNotification(Notification.SUCCESS, "添加成功!");
		
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 新增个人分组
	 * @param dep
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxAddGroup")
	public Map<String, Object> ajaxAddGroup(SelfGroup group,Integer[] userIds) {
		Map<String, Object> map  =  new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//企业主键
		group.setComId(sessionUser.getComId());
		
		group.setOwner(sessionUser.getId());
		String grpName = group.getGrpName();
		if(null!=grpName&&!"".equals(grpName)){
			group.setAllSpelling(PinyinToolkit.cn2Spell(group.getGrpName()));
			group.setFirstSpelling(PinyinToolkit.cn2FirstSpell(group.getGrpName()));
		}
		//添加个人分组
		selfGroupService.addAjaxGroup(group,userIds);
		//添加日志记录
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), "新增分组\""+group.getGrpName()+"\"",
				ConstantInterface.TYPE_USER,sessionUser.getComId(),sessionUser.getOptIP());
		
		//上次使用的分组
		List<UsedGroup> usedGroups = userInfoService.listUsedGroup(sessionUser.getComId(),sessionUser.getId());
		//个人组群集合
		List<SelfGroup> listSelfGroup = userInfoService.listSelfGroup(sessionUser.getComId(),sessionUser.getId());
		//最近一次使用的分组的类型，分组名称以及自定义所有的分组
		Map<String, String> grpMap = CommonUtil.usedGrpJson(usedGroups,listSelfGroup);
		//自定义所有的分组
		map.put("selfGroupStr", grpMap.get("selfGroupStr"));
		map.put("status", 'y');
		
		
		return map;
	}
	
	/**
	 * 人员私有组  批量删除
	 * @param ids
	 * @param redirectPage
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 */
	@RequestMapping("/delUserGroup")
	public ModelAndView delUserGroup(Integer[] ids, String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		try {
			selfGroupService.delSelfGroup(ids,userInfo);
			this.setNotification(Notification.SUCCESS, "删除成功!");
			//添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "批量删除分组",
					ConstantInterface.TYPE_USER,userInfo.getComId(),userInfo.getOptIP());
		} catch (Exception e) {
			this.setNotification(Notification.ERROR, "已有组群被还用，删除失败!");
		}
		return new ModelAndView("redirect:" + redirectPage);
	}
	/**
	 * 删除分组下组员
	 * @param groupId 群组主键
	 * @param delUserId 删除人主键
	 * @param redirectPage
	 * @return
	 */
	@RequestMapping("/delGroupUser")
	public ModelAndView delGroupUser(Integer groupId,Integer delUserId, String redirectPage) {
		UserInfo userInfo = this.getSessionUser();
		selfGroupService.delGroupUser(groupId,delUserId,userInfo);
		this.setNotification(Notification.SUCCESS, "删除成功!");
		return new ModelAndView("redirect:" + redirectPage);
	}
	
	/**
	 * 新增人员私有组的页面
	 * @return
	 */
	@RequestMapping("/updateUserGroupPage")
	public ModelAndView updateUserGroupPage(Integer id) {
		ModelAndView mav = new ModelAndView("/userInfo/selfCenter");
		SelfGroup group = selfGroupService.getGroupById(id,this.getSessionUser());
		mav.addObject("group", group);
		mav.addObject("userInfo", this.getSessionUser());
		return mav;
	}
	/**
	 * 人员私有组  更新修改
	 * @param userGroup
	 * @return
	 */
	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	public ModelAndView updateGroup(SelfGroup group, String redirectPage) {
		UserInfo curUser = this.getSessionUser();
		//分组所在的公司
		group.setComId(curUser.getComId());
		//重新生成全称和简称
		if(null!=group.getGrpName()&&!"".equals(group.getGrpName())){
			group.setAllSpelling(PinyinToolkit.cn2Spell(group.getGrpName()));
			group.setFirstSpelling(PinyinToolkit.cn2FirstSpell(group.getGrpName()));
		}
		//修改分组
		selfGroupService.updateSelfGroup(group,curUser);
		this.setNotification(Notification.SUCCESS, "修改组群成功!");
		
		return new ModelAndView("redirect:" + redirectPage);
	}
	
	/**
	 * 异步更新个人分组
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateGroupByAjax",method=RequestMethod.POST)
	public Map<String,Object> updateGroupByAjax(SelfGroup group) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//分组所在的公司
		group.setComId(userInfo.getComId());
		//重新生成全称和简称
		if(null!=group.getGrpName()&&!"".equals(group.getGrpName())){
			group.setAllSpelling(PinyinToolkit.cn2Spell(group.getGrpName()));
			group.setFirstSpelling(PinyinToolkit.cn2FirstSpell(group.getGrpName()));
		}
		selfGroupService.updateSelfGroupPersion(group,userInfo);
		map.put("status", "y");
		map.put("info","更新成功！");
		this.setNotification(Notification.SUCCESS, "更新成功！");
		return map;
	}
	
	/**
	 * 异步更新个人分组
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateSelfGroupName",method=RequestMethod.POST)
	public Map<String,Object> updateSelfGroupName(SelfGroup group) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(null==userInfo){
			map.put("status", "f");
			map.put("info", CommonConstant.OFF_LINE_INFO);
			return map;
		}
		//分组所在的公司
		group.setComId(userInfo.getComId());
		//重新生成全称和简称
		if(null!=group.getGrpName()&&!"".equals(group.getGrpName())){
			group.setAllSpelling(PinyinToolkit.cn2Spell(group.getGrpName()));
			group.setFirstSpelling(PinyinToolkit.cn2FirstSpell(group.getGrpName()));
		}
		selfGroupService.updateSelfGroupName(group,userInfo);
		map.put("status", "y");
		map.put("info","更新成功！");
		this.setNotification(Notification.SUCCESS, "更新成功！");
		return map;
	}
	
	/**
	 * 设置系统个人默认显示人员组
	 * @param groupId 分组主键
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/initDefGrpToShow")
	public Map<String, Object> initDefGrpToShow(Integer groupId) {
		Map<String, Object> map  =  new HashMap<String, Object>();
		map.put("status", 'n');
		UserInfo userInfo = this.getSessionUser();
		if(CommonUtil.isNull(userInfo)) {
			map.put("info","数据异常，请联系系统管理员！");
		}
		selfGroupService.initDefGrpToShow(groupId,userInfo);
		map.put("info","设置成功！");
		map.put("status", 'y');
		
		return map;
	}
	
}