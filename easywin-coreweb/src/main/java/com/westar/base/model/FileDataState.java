package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 附件是否从数据库下载的信息
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FileDataState {
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
	* 关联upfiles主键
	*/
	@Filed
	private Integer upfilesId;
	/** 
	* 服务器IP地址
	*/
	@Filed
	private String ipAddress;
	/** 
	* 状态 0未处理  1已处理
	*/
	@Filed
	private String state;

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
	* 关联upfiles主键
	* @param upfilesId
	*/
	public void setUpfilesId(Integer upfilesId) {
		this.upfilesId = upfilesId;
	}

	/** 
	* 关联upfiles主键
	* @return
	*/
	public Integer getUpfilesId() {
		return upfilesId;
	}

	/** 
	* 服务器IP地址
	* @param ipAddress
	*/
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/** 
	* 服务器IP地址
	* @return
	*/
	public String getIpAddress() {
		return ipAddress;
	}

	/** 
	* 状态 0未处理  1已处理
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 状态 0未处理  1已处理
	* @return
	*/
	public String getState() {
		return state;
	}
}
