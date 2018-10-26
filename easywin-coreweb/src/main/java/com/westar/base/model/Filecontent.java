package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 附件文件内容
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Filecontent {
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
	* 文件内容(超过4000字节)
	*/
	@Filed
	private String content;

	/****************以上主要为系统表字段********************/
	/** 
	* 文件路径
	*/
	private String filePath;

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
	* 文件内容(超过4000字节)
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 文件内容(超过4000字节)
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 文件路径
	* @return
	*/
	public String getFilePath() {
		return filePath;
	}

	/** 
	* 文件路径
	* @param filePath
	*/
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
