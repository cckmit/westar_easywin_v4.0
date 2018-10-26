package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 固定资产减少记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class GdzcReduceRecord {
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
	* 固定资产主键
	*/
	@Filed
	private Integer gdzcId;
	/** 
	* 减少类型
	*/
	@Filed
	private Integer reduceType;
	/** 
	* 减少人
	*/
	@Filed
	private Integer reduceUser;
	/** 
	* 减少原因
	*/
	@Filed
	private String reduceReason;

	/****************以上主要为系统表字段********************/
	/** 
	* 减少类型名称
	*/
	private String reduceTypeName;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 委派人姓名
	*/
	private String executorName;
	/** 
	* 上传人头像uuid
	*/
	private String userUuid;
	/** 
	* 上传人头像名称
	*/
	private String userFileName;
	/** 
	* 固定资产附件
	*/
	private List<Upfiles> listGdzcUpfiles;

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
	* 固定资产主键
	* @param gdzcId
	*/
	public void setGdzcId(Integer gdzcId) {
		this.gdzcId = gdzcId;
	}

	/** 
	* 固定资产主键
	* @return
	*/
	public Integer getGdzcId() {
		return gdzcId;
	}

	/** 
	* 减少类型
	* @param reduceType
	*/
	public void setReduceType(Integer reduceType) {
		this.reduceType = reduceType;
	}

	/** 
	* 减少类型
	* @return
	*/
	public Integer getReduceType() {
		return reduceType;
	}

	/** 
	* 减少人
	* @param reduceUser
	*/
	public void setReduceUser(Integer reduceUser) {
		this.reduceUser = reduceUser;
	}

	/** 
	* 减少人
	* @return
	*/
	public Integer getReduceUser() {
		return reduceUser;
	}

	/** 
	* 减少类型名称
	* @return
	*/
	public String getReduceTypeName() {
		return reduceTypeName;
	}

	/** 
	* 减少类型名称
	* @param reduceTypeName
	*/
	public void setReduceTypeName(String reduceTypeName) {
		this.reduceTypeName = reduceTypeName;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 委派人姓名
	* @return
	*/
	public String getExecutorName() {
		return executorName;
	}

	/** 
	* 委派人姓名
	* @param executorName
	*/
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	/** 
	* 上传人头像uuid
	* @return
	*/
	public String getUserUuid() {
		return userUuid;
	}

	/** 
	* 上传人头像uuid
	* @param userUuid
	*/
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	/** 
	* 上传人头像名称
	* @return
	*/
	public String getUserFileName() {
		return userFileName;
	}

	/** 
	* 上传人头像名称
	* @param userFileName
	*/
	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	/** 
	* 减少原因
	* @param reduceReason
	*/
	public void setReduceReason(String reduceReason) {
		this.reduceReason = reduceReason;
	}

	/** 
	* 减少原因
	* @return
	*/
	public String getReduceReason() {
		return reduceReason;
	}

	/** 
	* 固定资产附件
	* @return
	*/
	public List<Upfiles> getListGdzcUpfiles() {
		return listGdzcUpfiles;
	}

	/** 
	* 固定资产附件
	* @param listGdzcUpfiles
	*/
	public void setListGdzcUpfiles(List<Upfiles> listGdzcUpfiles) {
		this.listGdzcUpfiles = listGdzcUpfiles;
	}
}
