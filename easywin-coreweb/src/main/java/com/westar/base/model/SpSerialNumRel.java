package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 审批序列号使用记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpSerialNumRel {
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
	* 关联的序列标号主键
	*/
	@Filed
	private Integer serialNumId;
	/** 
	* 业务主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 业务类型
	*/
	@Filed
	private String busType;
	/** 
	* 使用值
	*/
	@Filed
	private String serialNum;

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
	* 关联的序列标号主键
	* @param serialNumId
	*/
	public void setSerialNumId(Integer serialNumId) {
		this.serialNumId = serialNumId;
	}

	/** 
	* 关联的序列标号主键
	* @return
	*/
	public Integer getSerialNumId() {
		return serialNumId;
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
	* 使用值
	* @param serialNum
	*/
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	/** 
	* 使用值
	* @return
	*/
	public String getSerialNum() {
		return serialNum;
	}
}
