package com.westar.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 项目进度
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ItemProgress {
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
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 项目主键
	*/
	@Filed
	private Integer itemId;
	/** 
	* 进度负责人
	*/
	@Filed
	private Integer userId;
	/** 
	* 进度名
	*/
	@Filed
	private String progressName;
	/** 
	* 进度排序
	*/
	@Filed
	private Integer progressOrder;
	/** 
	* 完成时间
	*/
	@Filed
	private String finishTime;
	/** 
	* 开始时间
	*/
	@Filed
	private String startTime;

	/****************以上主要为系统表字段********************/
	/** 
	* 进度负责人名
	*/
	private String userName;

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
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 项目主键
	* @param itemId
	*/
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/** 
	* 项目主键
	* @return
	*/
	public Integer getItemId() {
		return itemId;
	}

	/** 
	* 进度负责人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 进度负责人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 进度名
	* @param progressName
	*/
	public void setProgressName(String progressName) {
		this.progressName = progressName;
	}

	/** 
	* 进度名
	* @return
	*/
	public String getProgressName() {
		return progressName;
	}

	/** 
	* 进度排序
	* @param progressOrder
	*/
	public void setProgressOrder(Integer progressOrder) {
		this.progressOrder = progressOrder;
	}

	/** 
	* 进度排序
	* @return
	*/
	public Integer getProgressOrder() {
		return progressOrder;
	}

	/** 
	* 完成时间
	* @param finishTime
	*/
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	/** 
	* 完成时间
	* @return
	*/
	public String getFinishTime() {
		return finishTime;
	}

	/** 
	* 开始时间
	* @return
	*/
	public String getStartTime() {
		return startTime;
	}

	/** 
	* 开始时间
	* @param startTime
	*/
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/** 
	* 进度负责人名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 进度负责人名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
