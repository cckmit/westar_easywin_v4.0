package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 分享分享组
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DailyShareGroup {
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
	* 分享信息主键
	*/
	@Filed
	private Integer dailyId;
	/** 
	* 所属组主键
	*/
	@Filed
	private Integer grpId;

	/****************以上主要为系统表字段********************/
	/** 
	* 组名称
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
	* 分享信息主键
	* @param dailyId
	*/
	public void setDailyId(Integer dailyId) {
		this.dailyId = dailyId;
	}

	/** 
	* 分享信息主键
	* @return
	*/
	public Integer getDailyId() {
		return dailyId;
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
	* 组名称
	* @return
	*/
	public String getGrpName() {
		return grpName;
	}

	/** 
	* 组名称
	* @param grpName
	*/
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
}
