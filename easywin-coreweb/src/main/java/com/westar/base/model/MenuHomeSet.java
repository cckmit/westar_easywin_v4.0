package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 个人卡片设置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class MenuHomeSet {
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
	* 业务类型
	*/
	@Filed
	private String busType;
	/** 
	* 开启状态  参见数据字典enabled
	*/
	@Filed
	private String opened;
	/** 
	* 菜单排序号
	*/
	@Filed
	private Integer orderNo;
	/** 
	* 栏目占据宽度0/半屏1/全屏
	*/
	@Filed
	private Integer width;

	/****************以上主要为系统表字段********************/
	/** 
	* 菜单名称
	*/
	private String title;

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
	* 栏目占据宽度0/半屏1/全屏
	* @return
	*/
	public Integer getWidth() {
		return width;
	}

	/** 
	* 栏目占据宽度0/半屏1/全屏
	* @param width
	*/
	public void setWidth(Integer width) {
		this.width = width;
	}

	/** 
	* 菜单名称
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 菜单名称
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}
}
