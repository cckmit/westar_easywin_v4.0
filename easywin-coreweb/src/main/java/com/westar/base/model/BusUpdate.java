package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 业务更新表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BusUpdate {
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
	* 业务主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 更新类型1新建/2修改类型/3维护/4移交
	*/
	@Filed
	private String updateType;
	/** 
	* 更新内容
	*/
	@Filed
	private String content;
	/** 
	* 更新人
	*/
	@Filed
	private Integer userId;
	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;

	/****************以上主要为系统表字段********************/

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
	* 业务主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 业务主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 更新类型1新建/2修改类型/3维护/4移交
	* @param updateType
	*/
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	/** 
	* 更新类型1新建/2修改类型/3维护/4移交
	* @return
	*/
	public String getUpdateType() {
		return updateType;
	}

	/** 
	* 更新内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 更新内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 更新人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 更新人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
	}
}
