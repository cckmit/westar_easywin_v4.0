package com.westar.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 项目进度模板具体进度配置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ItemTemplateProgress {
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
	* 模板主键
	*/
	@Filed
	private Integer templateId;
	/** 
	* 进度名
	*/
	@Filed
	private String progressName;
	/** 
	* 进度排序
	*/
	@Filed
	private Integer progressOrder;

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
	* 模板主键
	* @param templateId
	*/
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	/** 
	* 模板主键
	* @return
	*/
	public Integer getTemplateId() {
		return templateId;
	}

	/** 
	* 进度名
	* @param progressName
	*/
	public void setProgressName(String progressName) {
		this.progressName = progressName;
	}

	/** 
	* 进度名
	* @return
	*/
	public String getProgressName() {
		return progressName;
	}

	/** 
	* 进度排序
	* @param progressOrder
	*/
	public void setProgressOrder(Integer progressOrder) {
		this.progressOrder = progressOrder;
	}

	/** 
	* 进度排序
	* @return
	*/
	public Integer getProgressOrder() {
		return progressOrder;
	}
}
