package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 需求文档说明
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DemandFile {
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
	* 文件上传人
	*/
	@Filed
	private Integer creator;
	/** 
	* 需求主键
	*/
	@Filed
	private Integer demandId;
	/** 
	* 附件主键
	*/
	@Filed
	private Integer upfileId;

	/****************以上主要为系统表字段********************/
	/** 
	* 文件名称
	*/
	private String fileName;

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
	* 文件上传人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 文件上传人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 需求主键
	* @param demandId
	*/
	public void setDemandId(Integer demandId) {
		this.demandId = demandId;
	}

	/** 
	* 需求主键
	* @return
	*/
	public Integer getDemandId() {
		return demandId;
	}

	/** 
	* 附件主键
	* @param upfileId
	*/
	public void setUpfileId(Integer upfileId) {
		this.upfileId = upfileId;
	}

	/** 
	* 附件主键
	* @return
	*/
	public Integer getUpfileId() {
		return upfileId;
	}

	/** 
	* 文件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 文件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
