package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 头部菜单
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MenuHead {
	/** 
	* id主键
	*/
	@Identity
	private Integer id;
	/** 
	* 记录创建时间
	*/
	@DefaultFiled
	private String recordCreateTime;
	/** 
	* 菜单名称
	*/
	@Filed
	private String menuName;
	/** 
	* 动作地址
	*/
	@Filed
	private String action;
	/** 
	* 头部对应class
	*/
	@Filed
	private String clzHead;
	/** 
	* 菜单对应class
	*/
	@Filed
	private String clzMenu;
	/** 
	* 业务类型
	*/
	@Filed
	private String busType;
	/** 
	* 菜单排序号
	*/
	@Filed
	private Integer orderNo;
	/** 
	* 启用状态  参见数据字典enabled
	*/
	@Filed
	private String enabled;
	/** 
	* 菜单类型
	*/
	@Filed
	private String menuType;
	/** 
	* 激活对应颜色class
	*/
	@Filed
	private String enableClz;

	/****************以上主要为系统表字段********************/
	private Integer usedRate;
	/** 
	* 企业号
	*/
	private Integer comId;
	/** 
	* 用户主键
	*/
	private Integer userId;

	/****************以上为自己添加字段********************/
	/** 
	* id主键
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** 
	* id主键
	* @return
	*/
	public Integer getId() {
		return id;
	}

	/** 
	* 记录创建时间
	* @param recordCreateTime
	*/
	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	/** 
	* 记录创建时间
	* @return
	*/
	public String getRecordCreateTime() {
		return recordCreateTime;
	}

	/** 
	* 菜单名称
	* @param menuName
	*/
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/** 
	* 菜单名称
	* @return
	*/
	public String getMenuName() {
		return menuName;
	}

	/** 
	* 动作地址
	* @param action
	*/
	public void setAction(String action) {
		this.action = action;
	}

	/** 
	* 动作地址
	* @return
	*/
	public String getAction() {
		return action;
	}

	/** 
	* 头部对应class
	* @param clzHead
	*/
	public void setClzHead(String clzHead) {
		this.clzHead = clzHead;
	}

	/** 
	* 头部对应class
	* @return
	*/
	public String getClzHead() {
		return clzHead;
	}

	/** 
	* 菜单对应class
	* @param clzMenu
	*/
	public void setClzMenu(String clzMenu) {
		this.clzMenu = clzMenu;
	}

	/** 
	* 菜单对应class
	* @return
	*/
	public String getClzMenu() {
		return clzMenu;
	}

	/** 
	* 业务类型
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 菜单排序号
	* @param orderNo
	*/
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/** 
	* 菜单排序号
	* @return
	*/
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 
	* 启用状态  参见数据字典enabled
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** 
	* 启用状态  参见数据字典enabled
	* @return
	*/
	public String getEnabled() {
		return enabled;
	}

	public Integer getUsedRate() {
		return usedRate;
	}

	public void setUsedRate(Integer usedRate) {
		this.usedRate = usedRate;
	}

	/** 
	* 企业号
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 企业号
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 用户主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 用户主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 菜单类型
	* @param menuType
	*/
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	/** 
	* 菜单类型
	* @return
	*/
	public String getMenuType() {
		return menuType;
	}

	/** 
	* 激活对应颜色class
	* @param enableClz
	*/
	public void setEnableClz(String enableClz) {
		this.enableClz = enableClz;
	}

	/** 
	* 激活对应颜色class
	* @return
	*/
	public String getEnableClz() {
		return enableClz;
	}
}
