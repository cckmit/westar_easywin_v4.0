package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 流程表单多数据
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SpFlowOptData {
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
	* 流程表单数据
	*/
	@Filed
	private Integer formFlowDataId;
	/** 
	* 名称
	*/
	@Filed
	private Integer optionId;
	/** 
	* 配置项值
	*/
	@Filed
	private String content;
	/** 
	* 流程实例化主键
	*/
	@Filed
	private Integer instanceId;
	/** 
	* 配置项值类型 0普通 1长类型
	*/
	@Filed
	private String valType;
	/** 
	* 文本框内容
	*/
	@Filed
	private String contentMore;
	/** 
	* 关联模块类型
	*/
	@Filed
	private String dataBustype;

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
	* 流程表单数据
	* @param formFlowDataId
	*/
	public void setFormFlowDataId(Integer formFlowDataId) {
		this.formFlowDataId = formFlowDataId;
	}

	/** 
	* 流程表单数据
	* @return
	*/
	public Integer getFormFlowDataId() {
		return formFlowDataId;
	}

	/** 
	* 名称
	* @param optionId
	*/
	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	/** 
	* 名称
	* @return
	*/
	public Integer getOptionId() {
		return optionId;
	}

	/** 
	* 配置项值
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 配置项值
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 流程实例化主键
	* @param instanceId
	*/
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	/** 
	* 流程实例化主键
	* @return
	*/
	public Integer getInstanceId() {
		return instanceId;
	}

	/** 
	* 配置项值类型 0普通 1长类型
	* @param valType
	*/
	public void setValType(String valType) {
		this.valType = valType;
	}

	/** 
	* 配置项值类型 0普通 1长类型
	* @return
	*/
	public String getValType() {
		return valType;
	}

	/** 
	* 文本框内容
	* @param contentMore
	*/
	public void setContentMore(String contentMore) {
		this.contentMore = contentMore;
	}

	/** 
	* 文本框内容
	* @return
	*/
	public String getContentMore() {
		return contentMore;
	}

	/** 
	* 关联模块类型
	* @param dataBustype
	*/
	public void setDataBustype(String dataBustype) {
		this.dataBustype = dataBustype;
	}

	/** 
	* 关联模块类型
	* @return
	*/
	public String getDataBustype() {
		return dataBustype;
	}
}
