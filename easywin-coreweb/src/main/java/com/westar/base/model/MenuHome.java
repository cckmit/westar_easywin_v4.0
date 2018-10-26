package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 头部卡片
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MenuHome {
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
	* 标题
	*/
	@Filed
	private String title;
	/** 
	* 添加动作地址
	*/
	@Filed
	private String actionAdd;
	/** 
	* 查看更多地址
	*/
	@Filed
	private String actionMore;
	/** 
	* 业务类型
	*/
	@Filed
	private String busType;
	/** 
	* 新增按钮名称
	*/
	@Filed
	private String addName;
	/** 
	* 是否默认
	*/
	@Filed
	private String isDefault;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否开启
	*/
	private String openState;
	/** 
	* 栏目占据宽度
	*/
	private Integer width;

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
	* 标题
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	* 标题
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 添加动作地址
	* @param actionAdd
	*/
	public void setActionAdd(String actionAdd) {
		this.actionAdd = actionAdd;
	}

	/** 
	* 添加动作地址
	* @return
	*/
	public String getActionAdd() {
		return actionAdd;
	}

	/** 
	* 查看更多地址
	* @param actionMore
	*/
	public void setActionMore(String actionMore) {
		this.actionMore = actionMore;
	}

	/** 
	* 查看更多地址
	* @return
	*/
	public String getActionMore() {
		return actionMore;
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
	* 新增按钮名称
	* @param addName
	*/
	public void setAddName(String addName) {
		this.addName = addName;
	}

	/** 
	* 新增按钮名称
	* @return
	*/
	public String getAddName() {
		return addName;
	}

	/** 
	* 是否开启
	* @return
	*/
	public String getOpenState() {
		return openState;
	}

	/** 
	* 是否开启
	* @param openState
	*/
	public void setOpenState(String openState) {
		this.openState = openState;
	}

	/** 
	* 是否默认
	* @param isDefault
	*/
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	/** 
	* 是否默认
	* @return
	*/
	public String getIsDefault() {
		return isDefault;
	}

	/** 
	* 栏目占据宽度
	* @return
	*/
	public Integer getWidth() {
		return width;
	}

	/** 
	* 栏目占据宽度
	* @param width
	*/
	public void setWidth(Integer width) {
		this.width = width;
	}
}
