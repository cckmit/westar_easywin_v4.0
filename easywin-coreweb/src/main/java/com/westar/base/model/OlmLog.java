package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 外部联系人日志表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OlmLog {
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
	* 外部联系人主键
	*/
	@Filed
	private Integer outLinkManId;
	/** 
	* 操作者id
	*/
	@Filed
	private Integer userId;
	/** 
	* 操作内容
	*/
	@Filed
	private String content;

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
	* 外部联系人主键
	* @param outLinkManId
	*/
	public void setOutLinkManId(Integer outLinkManId) {
		this.outLinkManId = outLinkManId;
	}

	/** 
	* 外部联系人主键
	* @return
	*/
	public Integer getOutLinkManId() {
		return outLinkManId;
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
}
