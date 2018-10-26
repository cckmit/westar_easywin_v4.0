package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 在线编辑器的附件
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WebeditorFileData {
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
	* 企业主键
	*/
	@Filed
	private Integer comId;
	/** 
	* 文件名
	*/
	@Filed
	private String filename;
	/** 
	* 附件关联的类别   news网站新闻
	*/
	@Filed
	private String relationType;
	/** 
	* 关联的表的ID 如：网站新闻的附件就是关联news表的ID
	*/
	@Filed
	private Integer relationId;

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
	* 企业主键
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业主键
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 文件名
	* @param filename
	*/
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/** 
	* 文件名
	* @return
	*/
	public String getFilename() {
		return filename;
	}

	/** 
	* 附件关联的类别   news网站新闻
	* @param relationType
	*/
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	/** 
	* 附件关联的类别   news网站新闻
	* @return
	*/
	public String getRelationType() {
		return relationType;
	}

	/** 
	* 关联的表的ID 如：网站新闻的附件就是关联news表的ID
	* @param relationId
	*/
	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	/** 
	* 关联的表的ID 如：网站新闻的附件就是关联news表的ID
	* @return
	*/
	public Integer getRelationId() {
		return relationId;
	}
}
