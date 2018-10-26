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

import com.westar.base.model.Department;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.PinyinToolkit;
import com.westar.core.service.DepartmentService;
import com.westar.core.service.MsgShareService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.UserInfoService;

/**
 * 
 * 描述:不猛操作控制类
 * @author zzq
 * @date 2018年8月25日 上午11:51:57
 */
@Controller
@RequestMapping("/department")
public class DepartmentController extends BaseController{

	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	SystemLogService systemLogService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	MsgShareService msgShareService;
	
	/**
	 * 维护部门框架
	 * @return
	 */
	@RequestMapping("/framesetDepPage")
	public ModelAndView framesetDepPage() {
		return new ModelAndView("/organic/organicCenter").addObject("userInfo", this.getSessionUser());
	}
	
	/**
	 * 维护部门操作页
	 * @return
	 */
	@RequestMapping("/treeDepPage")
	public ModelAndView treeDepPage() {
		String orgName = this.getSessionUser().getOrgName();
		if(null==orgName||"".equals(orgName)){
			orgName = this.getSessionUser().getComId()+"";
		}
		UserInfo sessionUser = userInfoService.getUserInfo(this.getSessionUser().getComId(), this.getSessionUser().getId());
		return new ModelAndView("/department/treeDep").addObject("orgName",orgName).addObject("sessionUser", sessionUser);
	}
	
	/**
	 * 查询部门信息 部门维护部门树
	 * @param id 部门主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listTreeDep")
	public List<Department> listTreeDep(Integer id) {
		UserInfo sessionUser = this.getSessionUser();
		List<Department> list = departmentService.listTreeDep(id, null,sessionUser.getComId());
		return list;
	}
	

	/**
	 * 新增部门页面
	 * @param dep
	 * @return
	 */
	@RequestMapping("/addDepPage")
	public ModelAndView addDepPage(Department dep) {
		ModelAndView mav = new ModelAndView("/department/addDep");
		return mav;
	}

	/**
	 * 新增部门
	 * @param dep
	 * @return
	 */
	@RequestMapping(value = "/addDep", method = RequestMethod.POST)
	public ModelAndView addDep(Department dep) {
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限的
			ModelAndView mav = new ModelAndView("/bodyRefresh");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return mav;
		}else{
			//企业主键
			dep.setComId(userInfo.getComId());
			// 默认启用
			dep.setEnabled(ConstantInterface.SYS_ENABLED_YES);
			
			dep.setCreator(userInfo.getId());
			if(null!=dep.getDepName()&&!"".equals(dep.getDepName())){
				dep.setAllSpelling(PinyinToolkit.cn2Spell(dep.getDepName()));
				dep.setFirstSpelling(PinyinToolkit.cn2FirstSpell(dep.getDepName()));
			}
			//添加部门
			Integer id = departmentService.addDepartment(dep);
			dep.setId(id);
			//添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "新增部门\""+dep.getDepName()+"\"",
					ConstantInterface.TYPE_DEP,userInfo.getComId(),userInfo.getOptIP());
			
			this.setNotification(Notification.SUCCESS, "添加成功!");
			return new ModelAndView("/department/addDepForward", "dep", dep);
		}
	}
	
	/**
	 * 异步添加部门信息
	 * @param dep
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxAddDep", method = RequestMethod.POST)
	public Map<String,Object> ajaxAddDep(Department dep) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限的
			map.put("status", "f");
			map.put("info", "抱歉，你没有管理权限");
			return map;
		}else{
			//企业主键
			dep.setComId(userInfo.getComId());
			// 默认启用
			dep.setEnabled(ConstantInterface.SYS_ENABLED_YES);
			
			dep.setCreator(userInfo.getId());
			if(null!=dep.getDepName()&&!"".equals(dep.getDepName())){
				dep.setAllSpelling(PinyinToolkit.cn2Spell(dep.getDepName()));
				dep.setFirstSpelling(PinyinToolkit.cn2FirstSpell(dep.getDepName()));
			}
			//添加部门
			Integer id = departmentService.addDepartment(dep);
			dep.setId(id);
			//添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "新增部门\""+dep.getDepName()+"\"",
					ConstantInterface.TYPE_DEP,userInfo.getComId(),userInfo.getOptIP());
			
			map.put("status", "y");
			map.put("dep", dep);
			return map;
		}
	}
	/**
	 * 设置部门成员
	 * @param dep
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateDepUsers", method = RequestMethod.POST)
	public Map<String,Object> updateDepUsers(Department dep) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo sessionUser = this.getSessionUser();
		//没有管理权限的
		if(sessionUser.getAdmin().equals("0")){
			map.put("status", "f");
			map.put("info", "抱歉，你没有管理权限");
			return map;
		}else{
			departmentService.updateDepUsers(dep, sessionUser);
			map.put("status", "y");
			return map;
		}
	}
	
	
	
	
	
	/**
	 * 修改部门页面
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateDepPage")
	public ModelAndView updateDepPage(Integer id) {
		UserInfo sessionUser = this.getSessionUser();
		//没有管理权限的
		if(sessionUser.getAdmin().equals("0")){
			ModelAndView mav = new ModelAndView("/bodyRefresh");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return mav;
		}else{
			//取得部门信息
			Department dep = departmentService.getDepWitnEnabledUser(id,sessionUser.getComId());
			ModelAndView mav = new ModelAndView("/department/updateDep", "dep", dep);
			return mav;
			
		}
	}

	/**
	 * 修改部门
	 * @param dep
	 * @return
	 */
	@RequestMapping(value = "/updateDep", method = RequestMethod.POST)
	public ModelAndView updateDep(Department dep) {
		UserInfo userInfo = this.getSessionUser();
		//没有管理权限的
		if(userInfo.getAdmin().equals("0")){
			ModelAndView mav = new ModelAndView("/bodyRefresh");
			this.setNotification(Notification.ERROR,"抱歉，你没有管理权限");
			return mav;
			
		}
		
		if(null!=dep.getDepName()&&!"".equals(dep.getDepName())){
			dep.setAllSpelling(PinyinToolkit.cn2Spell(dep.getDepName()));
			dep.setFirstSpelling(PinyinToolkit.cn2FirstSpell(dep.getDepName()));
		}
		departmentService.updateDep(dep,userInfo.getComId());
		//添加日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "修改部门\""+dep.getDepName()+"\"",
				ConstantInterface.TYPE_DEP,userInfo.getComId(),userInfo.getOptIP());
		
		//若是操作人员在本兮修改的部门中
		//修改session中的用户信息
		UserInfo user = userInfoService.getUserInfo(userInfo.getComId(),userInfo.getId());
		//设置下级个数
		user.setCountSub(userInfoService.countSubUser(userInfo.getId(), userInfo.getComId()));
		this.updateSessionUser(user);
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return new ModelAndView("/department/updateDepFoward", "dep", dep);
	}
	/**
	 * 一步修改部门修改
	 * @param dep
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxUpdateDep", method = RequestMethod.POST)
	public Map<String,Object> ajaxUpdateDep(Department dep) {

		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限的
			map.put("status", "f");
			map.put("info", "抱歉，你没有管理权限");
			return map;
		}else{

			if(null!=dep.getDepName()&&!"".equals(dep.getDepName())){
				dep.setAllSpelling(PinyinToolkit.cn2Spell(dep.getDepName()));
				dep.setFirstSpelling(PinyinToolkit.cn2FirstSpell(dep.getDepName()));
			}
			departmentService.updateDep(dep,userInfo.getComId());
			//添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "修改部门\""+dep.getDepName()+"\"",
					ConstantInterface.TYPE_DEP,userInfo.getComId(),userInfo.getOptIP());

			//若是操作人员在本兮修改的部门中
			//修改session中的用户信息
			UserInfo user = userInfoService.getUserInfo(userInfo.getComId(),userInfo.getId());
			//设置下级个数
			user.setCountSub(userInfoService.countSubUser(userInfo.getId(), userInfo.getComId()));
			this.updateSessionUser(user);

			map.put("status", "y");
			map.put("dep", dep);
			return map;
		}
	}

	/**
	 * 修改部门数据结构
	 * @param depId
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateDepStructure")
	public Map<String, String> updateDepStructure(Integer depId,Integer parentId) {
		UserInfo userInfo = this.getSessionUser();
		Map<String, String> map = new HashMap<String, String>();

		Department department = departmentService.getDep(depId,userInfo.getComId());
		department.setParentId(parentId);
		departmentService.updateDep(department,userInfo.getComId());
		map.put("status","y");


		Department currentDep = departmentService.getDep(depId,userInfo.getComId());
		Department parentDep = departmentService.getDep(parentId,userInfo.getComId());
		String content = "";
		if(!CommonUtil.isNull(parentDep)) {
			content = userInfo.getUserName() + "调整部门结构：将\""+ currentDep.getDepName() +"\"的上级部门修改为\"" + parentDep.getDepName() + "\"";
		}else {
			content = userInfo.getUserName() + "调整部门结构：取消了\""+ currentDep.getDepName() +"\"的上级部门";
		}
		//添加日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(),content ,
					ConstantInterface.TYPE_DEP,userInfo.getComId(),userInfo.getOptIP());

		return map;
	}

	/**
	 * 禁用启用部门
	 * @param ids 部门单主键id
	 * @param enabled 启用状态 0禁用 1启用
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateDepEnabled")
	public Map<String, String> updateDepEnabled(Integer[] ids, String enabled) {
		UserInfo sessionUser = this.getSessionUser();
		Map<String, String> map = new HashMap<String, String>();
		if(sessionUser.getAdmin().equals("0")){//没有管理权限的
			map.put("statue", "f");
			this.setNotification(Notification.ERROR, "你没有管理权限");
			return map;
		}
		try {
			departmentService.updateDepEnabled(ids, enabled,sessionUser.getComId());
			String content = "批量禁用部门";
			if(ConstantInterface.SYS_ENABLED_YES.equals(enabled)){
				content = "批量启用部门";
			}
			//添加日志记录
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), content,
					ConstantInterface.TYPE_DEP,sessionUser.getComId(),sessionUser.getOptIP());
			
			map.put("statue", "y");
			
		} catch (Exception e) {
			map.put("statue", "n");
			map.put("info", "系统错误，请联系管理人员！");
		}

		return map;
	}
	
	/**
	 * 删除部门
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delDep")
	public Map<String, String> delDep(Integer[] ids) {
		Map<String, String> map = new HashMap<String, String>();
		UserInfo userInfo = this.getSessionUser();
		if(userInfo.getAdmin().equals("0")){//没有管理权限的
			map.put("status", "f");
			this.setNotification(Notification.ERROR, "你没有管理权限");
			return map;
		}
		try {
			//批量删除部门
			departmentService.delDep(ids,userInfo.getComId());
			
			//添加日志记录
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "批量删除部门",
					ConstantInterface.TYPE_DEP,userInfo.getComId(),userInfo.getOptIP());
			//若是操作人员在本次删除的部门中
			//修改session中的用户信息
			UserInfo user = userInfoService.getUserInfo(userInfo.getComId(),userInfo.getId());
			//设置下级个数
			user.setCountSub(userInfoService.countSubUser(userInfo.getId(), userInfo.getComId()));
			this.updateSessionUser(user);
			map.put("statue", "y");
		} catch (Exception e) {
			map.put("statue", "n");
			map.put("info", "系统错误，请联系管理人员！");
			return map;
		}
		return map;
	}
	
	/**
	 * 查询部门的用户列表
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/listPagedUserForDep")
	public ModelAndView listPagedUserForDep(UserInfo userInfo) {
		//用户所在的公司
		userInfo.setComId(this.getSessionUser().getComId());
		//查询部门的用户列表
		List<UserInfo> list = userInfoService.listPagedUserForDep(userInfo);
		ModelAndView mav = new ModelAndView("/department/listPagedUserForDep", "list", list);
		return mav;
	}
	/**
	 * 树形查询部门的用户列表
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/listPagedUserTreeForDep")
	public ModelAndView listPagedUserTreeForDep(UserInfo userInfo) {
		//用户所在的公司
		userInfo.setComId(this.getSessionUser().getComId());
		List<UserInfo> list = userInfoService.listPagedUserTreeForDep(userInfo);
		ModelAndView mav = new ModelAndView("/department/listPagedUserForDep", "list", list);
		userInfo.setOrgName(this.getSessionUser().getOrgName());
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	
}