package com.westar.base.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 项目进度模板
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ItemProgressTemplate {
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
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 模板名
	*/
	@Filed
	private String templateName;

	/****************以上主要为系统表字段********************/
	/** 
	* 创建人姓名
	*/
	private String creatorName;
	/** 
	* 模板进度list
	*/
	private List<ItemTemplateProgress> listsItemTemplateProgress;

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
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 模板名
	* @param templateName
	*/
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/** 
	* 模板名
	* @return
	*/
	public String getTemplateName() {
		return templateName;
	}

	/** 
	* 创建人姓名
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建人姓名
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 模板进度list
	* @return
	*/
	public List<ItemTemplateProgress> getListsItemTemplateProgress() {
		return listsItemTemplateProgress;
	}

	/** 
	* 模板进度list
	* @param listsItemTemplateProgress
	*/
	public void setListsItemTemplateProgress(List<ItemTemplateProgress> listsItemTemplateProgress) {
		this.listsItemTemplateProgress = listsItemTemplateProgress;
	}
}
