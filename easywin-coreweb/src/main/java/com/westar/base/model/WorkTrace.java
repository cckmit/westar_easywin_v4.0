package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 系统操作轨迹
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WorkTrace {
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
	* 业务类别 字典表
	*/
	@Filed
	private String businessType;
	/** 
	* 工作轨迹的用户ID
	*/
	@Filed
	private Integer traceUserId;
	/** 
	* 操作内容
	*/
	@Filed
	private String content;
	/** 
	* 模块ID
	*/
	@Filed
	private Integer busId;

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
	* 业务类别 字典表
	* @param businessType
	*/
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/** 
	* 业务类别 字典表
	* @return
	*/
	public String getBusinessType() {
		return businessType;
	}

	/** 
	* 工作轨迹的用户ID
	* @param traceUserId
	*/
	public void setTraceUserId(Integer traceUserId) {
		this.traceUserId = traceUserId;
	}

	/** 
	* 工作轨迹的用户ID
	* @return
	*/
	public Integer getTraceUserId() {
		return traceUserId;
	}

	/** 
	* 操作内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 操作内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 模块ID
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 模块ID
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}
}
