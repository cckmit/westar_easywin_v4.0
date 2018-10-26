package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 帮助描述
 */
@Table
@JsonInclude(Include.NON_NULL)
public class HelpContent {
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
	* 帮助主键
	*/
	@Filed
	private Integer typeKey;
	/** 
	* 类型描述
	*/
	@Filed
	private String content;
	/** 
	* 更新时间
	*/
	@Filed
	private String modifyTime;

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
	* 帮助主键
	* @param typeKey
	*/
	public void setTypeKey(Integer typeKey) {
		this.typeKey = typeKey;
	}

	/** 
	* 帮助主键
	* @return
	*/
	public Integer getTypeKey() {
		return typeKey;
	}

	/** 
	* 类型描述
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 类型描述
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 更新时间
	* @param modifyTime
	*/
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/** 
	* 更新时间
	* @return
	*/
	public String getModifyTime() {
		return modifyTime;
	}
}
