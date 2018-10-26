package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 产品版本树
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ProVerTree {
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
	* 版本号
	*/
	@Filed
	private String version;
	/** 
	* 上个版本
	*/
	@Filed
	private String parent;
	/** 
	* 产品主键
	*/
	@Filed
	private Integer proId;

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
	* 版本号
	* @param version
	*/
	public void setVersion(String version) {
		this.version = version;
	}

	/** 
	* 版本号
	* @return
	*/
	public String getVersion() {
		return version;
	}

	/** 
	* 上个版本
	* @param parent
	*/
	public void setParent(String parent) {
		this.parent = parent;
	}

	/** 
	* 上个版本
	* @return
	*/
	public String getParent() {
		return parent;
	}

	/** 
	* 产品主键
	* @param proId
	*/
	public void setProId(Integer proId) {
		this.proId = proId;
	}

	/** 
	* 产品主键
	* @return
	*/
	public Integer getProId() {
		return proId;
	}
}
