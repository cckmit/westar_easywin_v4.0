package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 头部快捷发布
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AddSoon {
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
	* 业务类型
	*/
	@Filed
	private String busType;
	/** 
	* 快捷菜单标题
	*/
	@Filed
	private String menuTitle;
	/** 
	* 0/1
	*/
	@Filed
	private Integer mod;
	/** 
	* 模块图标
	*/
	@Filed
	private String clz;
	/** 
	* 模块样式
	*/
	@Filed
	private String style;
	/** 
	* 是否默认
	*/
	@Filed
	private Integer isDefault;

	/****************以上主要为系统表字段********************/
	/** 
	* 启用状态
	*/
	private Integer openState;
	/** 
	* 排序
	*/
	private Integer orderNo;

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
	* 快捷菜单标题
	* @param menuTitle
	*/
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	/** 
	* 快捷菜单标题
	* @return
	*/
	public String getMenuTitle() {
		return menuTitle;
	}

	/** 
	* 0/1
	* @param mod
	*/
	public void setMod(Integer mod) {
		this.mod = mod;
	}

	/** 
	* 0/1
	* @return
	*/
	public Integer getMod() {
		return mod;
	}

	/** 
	* 模块图标
	* @param clz
	*/
	public void setClz(String clz) {
		this.clz = clz;
	}

	/** 
	* 模块图标
	* @return
	*/
	public String getClz() {
		return clz;
	}

	/** 
	* 模块样式
	* @param style
	*/
	public void setStyle(String style) {
		this.style = style;
	}

	/** 
	* 模块样式
	* @return
	*/
	public String getStyle() {
		return style;
	}

	/** 
	* 是否默认
	* @param isDefault
	*/
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	/** 
	* 是否默认
	* @return
	*/
	public Integer getIsDefault() {
		return isDefault;
	}

	/** 
	* 启用状态
	* @return
	*/
	public Integer getOpenState() {
		return openState;
	}

	/** 
	* 启用状态
	* @param openState
	*/
	public void setOpenState(Integer openState) {
		this.openState = openState;
	}

	/** 
	* 排序
	* @return
	*/
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 
	* 排序
	* @param orderNo
	*/
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
}
