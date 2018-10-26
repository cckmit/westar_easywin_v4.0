package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 上次操作的分组
 */
@Table
@JsonInclude(Include.NON_NULL)
public class UsedGroup {
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
	* 所属组主键
	*/
	@Filed
	private Integer grpId;
	/** 
	* 操作者id
	*/
	@Filed
	private Integer userId;
	/** 
	* 分组类型
	*/
	@Filed
	private String groupType;

	/****************以上主要为系统表字段********************/
	/** 
	* 分组名称
	*/
	private String grpName;

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
	* 所属组主键
	* @param grpId
	*/
	public void setGrpId(Integer grpId) {
		this.grpId = grpId;
	}

	/** 
	* 所属组主键
	* @return
	*/
	public Integer getGrpId() {
		return grpId;
	}

	/** 
	* 操作者id
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 操作者id
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 分组类型
	* @param groupType
	*/
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/** 
	* 分组类型
	* @return
	*/
	public String getGroupType() {
		return groupType;
	}

	/** 
	* 分组名称
	* @return
	*/
	public String getGrpName() {
		return grpName;
	}

	/** 
	* 分组名称
	* @param grpName
	*/
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
}
