package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 序列编号
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SerialNum {
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
	* 使用年份
	*/
	@Filed
	private Integer year;
	/** 
	* 起始值
	*/
	@Filed
	private String startNum;
	/** 
	* 序列规则
	*/
	@Filed
	private String serialType;
	/** 
	* 序列格式化
	*/
	@Filed
	private String serialFormat;
	/** 
	* 描述
	*/
	@Filed
	private String remark;

	/****************以上主要为系统表字段********************/
	/** 
	* 需要排除的数据
	*/
	private Integer exceptId;

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
	* 使用年份
	* @param year
	*/
	public void setYear(Integer year) {
		this.year = year;
	}

	/** 
	* 使用年份
	* @return
	*/
	public Integer getYear() {
		return year;
	}

	/** 
	* 起始值
	* @param startNum
	*/
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}

	/** 
	* 起始值
	* @return
	*/
	public String getStartNum() {
		return startNum;
	}

	/** 
	* 序列规则
	* @param serialType
	*/
	public void setSerialType(String serialType) {
		this.serialType = serialType;
	}

	/** 
	* 序列规则
	* @return
	*/
	public String getSerialType() {
		return serialType;
	}

	/** 
	* 序列格式化
	* @param serialFormat
	*/
	public void setSerialFormat(String serialFormat) {
		this.serialFormat = serialFormat;
	}

	/** 
	* 序列格式化
	* @return
	*/
	public String getSerialFormat() {
		return serialFormat;
	}

	/** 
	* 描述
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 描述
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 需要排除的数据
	* @return
	*/
	public Integer getExceptId() {
		return exceptId;
	}

	/** 
	* 需要排除的数据
	* @param exceptId
	*/
	public void setExceptId(Integer exceptId) {
		this.exceptId = exceptId;
	}
}
