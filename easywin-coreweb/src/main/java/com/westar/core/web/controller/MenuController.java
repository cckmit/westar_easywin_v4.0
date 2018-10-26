package com.westar.core.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.AddSoon;
import com.westar.base.model.AddSoonSet;
import com.westar.base.model.Menu;
import com.westar.base.model.MenuHead;
import com.westar.base.model.MenuHomeSet;
import com.westar.base.model.MenuSet;
import com.westar.base.model.UserConf;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.Notification;
import com.westar.base.util.ConstantInterface;
import com.westar.core.service.MenuService;
import com.westar.core.service.SystemLogService;
import com.westar.core.service.UserConfService;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

	@Autowired
	MenuService menuService;

	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private UserConfService userConfService;
	/**
	 * 维护菜单框架页
	 * @return
	 */
	@RequestMapping("/framesetMenuPage")
	public ModelAndView framesetMenuPage() {
		return new ModelAndView("/menu/framesetMenu");
	}

	/**
	 * 维护菜单操作页
	 * @return
	 */
	@RequestMapping("/treeMenuPage")
	public ModelAndView treeMenuPage() {
		return new ModelAndView("/menu/treeMenu");
	}

	/**
	 * 查询菜单信息 菜单维护 菜单树
	 * @param id 菜单主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listTreeMenu")
	public List<Menu> listTreeMenu(Integer id) {
		List<Menu> list = menuService.listTreeMenu(id, null);
		return list;
	}
	
	/**
	 * 初始化菜单权限代码
	 * @return
	 */
	@RequestMapping("/initAuthCode")
	public ModelAndView initAuthCode(){
		menuService.initAuthCode();
		this.setNotification(Notification.SUCCESS, "初始化成功!");
		return new ModelAndView("/menu/framesetMenu");
	}
	
	/**
	 * 新增菜单页面
	 * @return
	 */
	@RequestMapping("/addMenuPage")
	public ModelAndView addMenuPage(Menu menu) {
		return new ModelAndView("/menu/addMenu", "menu", menu);
	}

	/**
	 * 新增菜单
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "/addMenu", method = RequestMethod.POST)
	public ModelAndView addMenu(Menu menu) {
		UserInfo userInfo = this.getSessionUser();
		//企业主键
		menu.setComId(userInfo.getComId());
		Integer[] idAndOrderNo = menuService.addMenu(menu);
		menu.setId(idAndOrderNo[0]);
		menu.setOrderNo(idAndOrderNo[1]);
		//添加日志记录
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "新增菜单\""+menu.getMenuName()+"\"",
				ConstantInterface.TYPE_MENU,userInfo.getComId(),userInfo.getOptIP());
		this.setNotification(Notification.SUCCESS, "添加成功!");
		return new ModelAndView("/menu/addMenuForward", "menu", menu);
	}

	/**
	 * 修改菜单页面
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateMenuPage")
	public ModelAndView updateMenuPage(Integer id) {
		Menu menu = menuService.getMenu(id);
		ModelAndView mav = new ModelAndView("/menu/updateMenu", "menu", menu);
		return mav;
	}

	/**
	 * 修改菜单
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
	public ModelAndView updateMenu(Menu menu) {
		menuService.updateMenu(menu);
		//添加日志记录
		UserInfo userInfo = this.getSessionUser();
		systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "修改菜单\""+menu.getMenuName()+"\"",
				ConstantInterface.TYPE_MENU,userInfo.getComId(),userInfo.getOptIP());
		this.setNotification(Notification.SUCCESS, "修改成功!");
		return new ModelAndView("/menu/updateMenuFoward", "menu", menu);
	}

	/**
	 * 禁用启用菜单
	 * @param ids 菜单主键id
	 * @param enabled 启用状态 0禁用 1启用
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMenuEnabled")
	public boolean updateMenuEnabled(Integer[] ids, String enabled) {
		try {
			menuService.updateMenuEnabled(ids, enabled);
			//日志内容
			String content = "批量禁用菜单";
			if(ConstantInterface.SYS_ENABLED_YES.equals(enabled)){
				content = "批量启用菜单";
			}
			//添加日志记录
			UserInfo userInfo = this.getSessionUser();
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), content,
					ConstantInterface.TYPE_MENU,userInfo.getComId(),userInfo.getOptIP());
			
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 根据id删除菜单
	 * @param ids  菜单主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delMenu")
	public boolean delMenu(Integer[] ids) {
		try {
			menuService.delMenu(ids);
			//添加日志记录
			UserInfo userInfo = this.getSessionUser();
			systemLogService.addSystemLog(userInfo.getId(), userInfo.getUserName(), "批量删除菜单",
					ConstantInterface.TYPE_MENU,userInfo.getComId(),userInfo.getOptIP());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 拖拽菜单
	 * @param id  菜单主键id
	 * @param parentId 父级菜单主键id
	 * @param orderNo 排序号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/dragMenu")
	public boolean dragMenu(Integer id, Integer parentId, Integer orderNo) {
		try {
			menuService.dragMenu(id, parentId, orderNo);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/*********************************顶部菜单显示********************************************************/
	
	/**
	 * 添加菜单使用频率
	 * @param busType 业务类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addMenuRate")
	public void addMenuRate(String busType) {
		UserInfo sessionUser = this.getSessionUser();
		if(null==sessionUser){
			return;
		}
		
		menuService.addMenuRate(sessionUser,busType);
	}
	
	/**
	 * 取得头部的显示菜单
	 */
	@ResponseBody
	@RequestMapping("/listHeadMenu")
	public Map<String, Object> listHeadMenu() {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			return map;
		}
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		
		List<MenuHead> menuHeadList = menuService.listHeadMenu(sessionUser);
		map.put("menuHeadList",menuHeadList);
		
		UserConf userConf = new UserConf(); 
		userConf.setSysConfCode("-1");
		userConf.setType(ConstantInterface.UCFG_MENU_NUM);
		UserConf userConfVo = userConfService.queryUserConf(sessionUser, userConf);
		if(null == userConfVo){
			map.put("menuNum",3);
		}else{
			map.put("menuNum",userConfVo.getOpenState());
		}
		return map;
	}
	
	/**
	 * 顶部菜单设置
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "/listMenuSet")
	public ModelAndView listMenuSet() {
		
		ModelAndView mav = new ModelAndView("/menu/listMenuSet");
		
		//添加日志记录
		UserInfo userInfo = this.getSessionUser();
		//查询菜单设置信息
		List<MenuSet> listMenuSet =  menuService.listMenuSet(userInfo);
		mav.addObject("listMenuSet", listMenuSet);
		return mav;
	}
	
	/**
	 * 修改头部菜单显示
	 * @param menuHead 头部菜单主键
	 * @param openState 开启状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMenuSet")
	public Map<String, Object> updateMenuSet(Integer menuHead,String openState) {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			return map;
		}
		
		menuService.updateMenuSet(sessionUser,menuHead,openState);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 首页模块设置
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "/listMenuHomeSet")
	public ModelAndView listMenuHomeSet() {
		
		ModelAndView mav = new ModelAndView("/menu/listMenuHomeSet");
		//添加日志记录
		UserInfo userInfo = this.getSessionUser();
		//查询菜单设置信息
		List<MenuHomeSet> listMenuHomeSet =  menuService.listMenuHomeSet(userInfo);
		mav.addObject("listMenuHomeSet", listMenuHomeSet);
		return mav;
	}
	
	/**
	 * 修改首页模块显示
	 * @param busType 首页某块类型
	 * @param openState 开启状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMenuHomeSet")
	public Map<String, Object> updateMenuHomeSet(String busType,String openState) {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		menuService.updateMenuHomeSet(sessionUser,busType,openState);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 修改模块展示的大小
	 * @param busType  首页某块类型
	 * @param width
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMenuWidth")
	public Map<String, Object> updateMenuWidth(String busType,Integer width){
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		menuService.updateMenuWidth(sessionUser,busType,width);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 修改首页模块显示
	 * @param busType 首页某块类型
	 * @param openState 开启状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMenuHomeOrder")
	public Map<String, Object> updateMenuHomeOrder(String menuHomeSetStr) {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		String[] menuHomeSetStrs = menuHomeSetStr.split("@");
		List<MenuHomeSet> list = new ArrayList<MenuHomeSet>();
		for (String json : menuHomeSetStrs) {
			Gson gson = new Gson();
			MenuHomeSet menuHomeSet = gson.fromJson(json, MenuHomeSet.class);
			menuHomeSet.setComId(sessionUser.getComId());
			menuHomeSet.setUserId(sessionUser.getId());
			list.add(menuHomeSet);
		}
		menuService.updateMenuHomeOrder(list);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 重置首页模块显示
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMenuHomeReset")
	public Map<String, Object> updateMenuHomeReset() {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, "f");
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		menuService.updateMenuHomeReset(sessionUser);
		//查询菜单设置信息
		List<MenuHomeSet> listMenuHomeSet =  menuService.listMenuHomeSet(sessionUser);
		map.put("listMenuHomeSet", listMenuHomeSet);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/*********************************快捷发布显示********************************************************/
	/**
	 * 获取当前用户快捷添加配置
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listAddSoonSetBySelf")
	public Map<String, Object> listAddSoonSetBySelf(){
		UserInfo userInfo = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==userInfo){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		List<AddSoon> listAddSoon = menuService.listAddSoonSetByState(userInfo,1);
		map.put("listAddSoon", listAddSoon);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}

	/**
	 * 所有快捷设置界面
	 * @return
	 */
	@RequestMapping("/listAddSoonSetOfAll")
	public ModelAndView listAddSoonSetOfAll(){
		ModelAndView mav = new ModelAndView("/menu/listAddSoonSet");
		UserInfo userInfo = this.getSessionUser();
		List<AddSoon> listAddSoon = menuService.listAddSoonSetByState(userInfo,null);
		mav.addObject("listAddSoon",listAddSoon);
		mav.addObject("userInfo", userInfo);
		return mav;
	}
	/**
	 * 修改快捷显示
	 * @param busType 首页某块类型
	 * @param openState 开启状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAddSoonSet")
	public Map<String, Object> updateAddSoonSet(String busType,Integer openState) {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		menuService.updateAddSoonSet(sessionUser,busType,openState);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	/**
	 * 修改首页模块顺序
	 * @param busType 首页某块类型
	 * @param openState 开启状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAddSoonOrder")
	public Map<String, Object> updateAddSoonOrder(String addSoonSetStr) {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		String[] addSoonSetStrs = addSoonSetStr.split("@");
		List<AddSoonSet> list = new ArrayList<AddSoonSet>();
		for (String json : addSoonSetStrs) {
			Gson gson = new Gson();
			AddSoonSet addSoonSet = gson.fromJson(json, AddSoonSet.class);
			addSoonSet.setComId(sessionUser.getComId());
			addSoonSet.setUserId(sessionUser.getId());
			list.add(addSoonSet);
		}
		menuService.updateAddSoonOrder(list);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
	
	/**
	 * 重置首页模块显示
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAddSoonReset")
	public Map<String, Object> updateAddSoonReset() {
		UserInfo sessionUser = this.getSessionUser();
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(null==sessionUser){
			map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_N);
			map.put(ConstantInterface.TYPE_INFO, CommonConstant.OFF_LINE_INFO);
			return map;
		}
		
		menuService.updateAddSoonReset(sessionUser);
		
		List<AddSoon> listAddSoon = menuService.listAddSoonSetByState(sessionUser,null);
		map.put("listAddSoon", listAddSoon);
		map.put(ConstantInterface.TYPE_STATUS, ConstantInterface.TYPE_STATUS_Y);
		return map;
	}
}
