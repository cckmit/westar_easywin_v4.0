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
 * 固定资产维护记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class GdzcMaintainRecord {
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
	* 开始日期
	*/
	@Filed
	private String startDate;
	/** 
	* 结束日期
	*/
	@Filed
	private String endDate;
	/** 
	* 维护原因
	*/
	@Filed
	private String maintainReason;
	/** 
	* 维护类型
	*/
	@Filed
	private Integer maintainType;
	/** 
	* 委派人
	*/
	@Filed
	private Integer executor;

	/****************以上主要为系统表字段********************/
	/** 
	* 固定资产附件
	*/
	private List<Upfiles> listGdzcUpfiles;
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
	* 维护类型名
	*/
	private String maintainTypeName;
	/** 
	* 维修状态1完成
	*/
	private Integer maintainState;

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
	* 开始日期
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 开始日期
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 结束日期
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 结束日期
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 维护原因
	* @param maintainReason
	*/
	public void setMaintainReason(String maintainReason) {
		this.maintainReason = maintainReason;
	}

	/** 
	* 维护原因
	* @return
	*/
	public String getMaintainReason() {
		return maintainReason;
	}

	/** 
	* 维护类型
	* @param maintainType
	*/
	public void setMaintainType(Integer maintainType) {
		this.maintainType = maintainType;
	}

	/** 
	* 维护类型
	* @return
	*/
	public Integer getMaintainType() {
		return maintainType;
	}

	/** 
	* 委派人
	* @param executor
	*/
	public void setExecutor(Integer executor) {
		this.executor = executor;
	}

	/** 
	* 委派人
	* @return
	*/
	public Integer getExecutor() {
		return executor;
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
	* 维护类型名
	* @return
	*/
	public String getMaintainTypeName() {
		return maintainTypeName;
	}

	/** 
	* 维护类型名
	* @param maintainTypeName
	*/
	public void setMaintainTypeName(String maintainTypeName) {
		this.maintainTypeName = maintainTypeName;
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

	/** 
	* 维修状态1完成
	* @return
	*/
	public Integer getMaintainState() {
		return maintainState;
	}

	/** 
	* 维修状态1完成
	* @param maintainState
	*/
	public void setMaintainState(Integer maintainState) {
		this.maintainState = maintainState;
	}
}
