package com.westar.core.service;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.westar.base.model.AddSoon;
import com.westar.base.model.AddSoonSet;
import com.westar.base.model.Menu;
import com.westar.base.model.MenuHead;
import com.westar.base.model.MenuHome;
import com.westar.base.model.MenuHomeSet;
import com.westar.base.model.MenuSet;
import com.westar.base.model.UserInfo;
import com.westar.core.dao.MenuDao;

@Service
public class MenuService {

	@Autowired
	MenuDao menuDao;

	/**
	 * 查询菜单树
	 * @param id 菜单主键id
	 * @return
	 */
	public List<Menu> listTreeMenu(Integer id, String enabled) {
		return menuDao.listTreeMenu(id, enabled);
	}

	/**
	 * 查询指定人员有权限并且启用的菜单 
	 * @param id菜单主键id
	 * @param userid 人员主键id
	 * @return
	 */
	public List<Menu> listRoleMenu(Integer id, Integer userid, String ip) {
		return menuDao.listRoleMenu(id, userid, ip);
	}
	
	/**
	 * 根据父级权限代码查询菜单
	 * @param authCode
	 * @param userId
	 * @return
	 */
	public List<Menu> listMenuByParentAuthCode(String parentAuthCode,Integer userId) {
		return menuDao.listMenuByParentAuthCode(parentAuthCode,userId);
	}

	/**
	 * 新增菜单
	 * @param menu
	 * @return
	 */
	public synchronized Integer[] addMenu(Menu menu) {
		menu.setEnabled("1"); // 默认启用
		int currentLevelMaxOrderNo = menuDao.getCurrentLevelMaxOrderNo(menu.getParentId());
		menu.setOrderNo(currentLevelMaxOrderNo + 1); // 默认排序号
		String authCode=RandomStringUtils.randomNumeric(6);
		//防止重复
		while(this.countByAuthCode(authCode)>0){
			authCode=RandomStringUtils.randomNumeric(6);
		}
		menu.setAuthCode(authCode);
		int id = menuDao.add(menu);
		return new Integer[] { id, menu.getOrderNo() };
	}
	
	/**
	 * 初始化菜单权限代码
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void initAuthCode(){
		List<Menu> list=this.listTreeMenu(null, null);
		if(list!=null){
			for (Menu menu : list) {
				String authCode=RandomStringUtils.randomNumeric(6);
				//防止重复
				while(this.countByAuthCode(authCode)>0){
					 authCode=RandomStringUtils.randomNumeric(6);
				}
				menu.setAuthCode(authCode);
				this.updateMenu(menu);
			}
		}
	}
	
	/**
	 * 查询某人是否具有某个菜单的权限
	 * @param authCode
	 * @param userId
	 * @return
	 */
	public boolean authAccess(String authCode,Integer userId){
		return menuDao.authAccess(authCode, userId);
	}
	
	/**
	 * 查询某人是否具有某个大模块的权限
	 * @param authCode
	 * @param userId
	 * @return
	 */
	public boolean authMenuAccess(String businesstype,Integer userId){
		return menuDao.authMenuAccess(businesstype, userId);
	}

	/**
	 * 查询菜单详细信息
	 * @param id 菜单主键id
	 * @return
	 */
	public Menu getMenu(Integer id) {
		return menuDao.getMenu(id);
	}

	/**
	 * 修改菜单
	 * @param menu
	 * @return
	 */
	public void updateMenu(Menu menu) {
		menuDao.update(menu);
	}

	/**
	 * 禁用启用菜单
	 * @param ids 菜单主键id
	 * @param enabled 启用状态 0禁用 1启用
	 */
	public void updateMenuEnabled(Integer[] ids, String enabled) {
		menuDao.updateMenuEnabled(ids, enabled);
	}

	/**
	 * 批量删除菜单
	 * @param ids 菜单主键id
	 */
	public void delMenu(Integer[] ids) {
		menuDao.delById(Menu.class, ids);
	}

	/**
	 * 拖拽菜单
	 * @param id 菜单主键id
	 * @param parentId 父菜单主键id
	 * @param orderNo 排序号
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public synchronized void dragMenu(Integer id, Integer parentId, Integer orderNo) {
		// 位移
		List<Menu> listMenu = menuDao.listMenuByParentIdAndPreOrderNo(parentId, orderNo);
		if (listMenu != null && listMenu.size() > 0) {
			Integer[] ids = new Integer[listMenu.size()];
			for (int i = 0; i < listMenu.size(); i++) {
				ids[i] = listMenu.get(i).getId();
			}
			menuDao.offset(ids);
		}
		// 修改菜单
		Menu menu = new Menu();
		menu.setId(id);
		menu.setParentId(parentId);
		menu.setOrderNo(orderNo);
		menuDao.update(menu);
	}
	
	/**
	 * 根据权限代码查询菜单数量
	 * @param authCode
	 * @return
	 */
	public int countByAuthCode(String authCode) {
		return menuDao.countByAuthCode(authCode);
	}

	
	/**
	 * 添加菜单使用频率
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void addMenuRate(UserInfo sessionUser, String busType) {
		
		menuDao.addMenuRate(sessionUser,busType);
		
	}

	/**
	 * 取得头部的显示菜单
	 * @param sessionUser
	 */
	public List<MenuHead> listHeadMenu(UserInfo sessionUser) {
		List<MenuHead> list = menuDao.listHeadMenu(sessionUser.getComId(),sessionUser.getId());
		return list;
	}

	/**
	 * 查询菜单设置信息
	 * @param userInfo
	 * @return
	 */
	public List<MenuSet> listMenuSet(UserInfo userInfo) {
		List<MenuSet> list = menuDao.listMenuSet(userInfo.getComId(),userInfo.getId());
		return list;
	}
	/**
	 * 添修改头部菜单显示信息
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void updateMenuSet(UserInfo sessionUser,Integer menuHead, String openState) {
		menuDao.updateMenuSet(sessionUser,menuHead,openState);
	}

	/**
	 * 取得首页需要展示的模块
	 * @param comId 企业号
	 * @param userId 用户主键
	 * @return
	 */
	public List<MenuHome> listMenuHome(Integer comId, Integer userId,Integer countSub) {
		List<MenuHome> list = menuDao.listMenuHome(comId,userId,countSub);
		return list;
	}
	
	/**
	 * 首页模块展示设置
	 * @param userInfo
	 * @return
	 */
	public List<MenuHomeSet> listMenuHomeSet(UserInfo userInfo) {
		List<MenuHomeSet> list = menuDao.listMenuHomeSet(userInfo.getComId(),userInfo.getId(),userInfo.getCountSub());
		return list;
	}
	/**
	 * 添修 首页模块展示设置
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void updateMenuHomeSet(UserInfo sessionUser,String busType, String openState) {
		menuDao.updateMenuHomeSet(sessionUser,busType,openState);
	}
	/**
	 * 修改模块展示的大小
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 * @param width
	 */
	public void updateMenuWidth(UserInfo sessionUser,String busType, Integer width) {
		menuDao.updateMenuWidth(sessionUser,busType,width);
	}
	/**
	 * 修改首页展示排序
	 * @param list
	 */
	public void updateMenuHomeOrder(List<MenuHomeSet> list) {
		if(null!=list && list.size()>0){
			for (MenuHomeSet menuHomeSet : list) {
				menuDao.updateMenuHomeOrder(menuHomeSet);
			}
		}
		
	}

	/**
	 * 重置首页模块显示
	 * @param sessionUser
	 */
	public void updateMenuHomeReset(UserInfo sessionUser) {
		menuDao.delByField("menuHomeSet", new String[]{"comId","userId"}, 
				new Object[]{sessionUser.getComId(),sessionUser.getId()});
		
	}
	/**
	 * 获取快捷添加配置
	 * @param userInfo
	 * @param openState 启用状态筛选
	 * @return
	 */
	public List<AddSoon> listAddSoonSetByState(UserInfo userInfo,Integer openState) {
		return menuDao.listAddSoonSetByState(userInfo,openState);
	}
	/**
	 *  修改快捷显示
	 * @param sessionUser 当前操作人员
	 * @param busType 业务类型
	 */
	public void updateAddSoonSet(UserInfo sessionUser, String busType,Integer openState) {
		 menuDao.updateAddSoonSet(sessionUser, busType, openState);
	}
	/**
	 * 修改首页展示排序
	 * @param list
	 */
	public void updateAddSoonOrder(List<AddSoonSet> list) {
		if(null!=list && list.size()>0){
			for (AddSoonSet addSoonSet : list) {
				menuDao.updateAddSoonOrder(addSoonSet);
			}
		}
		
	}

	/**
	 * 重置首页模块显示
	 * @param sessionUser
	 */
	public void updateAddSoonReset(UserInfo sessionUser) {
		menuDao.delByField("addSoonSet", new String[]{"comId","userId"}, new Object[]{sessionUser.getComId(),sessionUser.getId()});
		
	}
}
