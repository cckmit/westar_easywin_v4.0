package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 个人菜单设置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MenuSet {
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
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 关联菜单
	*/
	@Filed
	private Integer menuHead;
	/** 
	* 开启状态  参见数据字典enabled
	*/
	@Filed
	private String opened;

	/****************以上主要为系统表字段********************/
	/** 
	* 菜单名称
	*/
	private String menuName;

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
	* 企业编号
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 关联菜单
	* @param menuHead
	*/
	public void setMenuHead(Integer menuHead) {
		this.menuHead = menuHead;
	}

	/** 
	* 关联菜单
	* @return
	*/
	public Integer getMenuHead() {
		return menuHead;
	}

	/** 
	* 开启状态  参见数据字典enabled
	* @param opened
	*/
	public void setOpened(String opened) {
		this.opened = opened;
	}

	/** 
	* 开启状态  参见数据字典enabled
	* @return
	*/
	public String getOpened() {
		return opened;
	}

	/** 
	* 菜单名称
	* @return
	*/
	public String getMenuName() {
		return menuName;
	}

	/** 
	* 菜单名称
	* @param menuName
	*/
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}
