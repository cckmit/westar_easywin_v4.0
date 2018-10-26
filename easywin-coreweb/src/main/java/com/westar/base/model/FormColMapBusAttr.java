package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 业务数据与表单数据映射关系
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FormColMapBusAttr {
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
	* 模块操作与审批流程之间关联主键
	*/
	@Filed
	private Integer busMapFlowId;
	/** 
	* 映射对象关键字
	*/
	@Filed
	private String busAttr;
	/** 
	* 表单控件主键
	*/
	@Filed
	private String formCol;

	/****************以上主要为系统表字段********************/
	/** 
	* 组件类型
	*/
	private String componentkey;

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
	* 模块操作与审批流程之间关联主键
	* @param busMapFlowId
	*/
	public void setBusMapFlowId(Integer busMapFlowId) {
		this.busMapFlowId = busMapFlowId;
	}

	/** 
	* 模块操作与审批流程之间关联主键
	* @return
	*/
	public Integer getBusMapFlowId() {
		return busMapFlowId;
	}

	/** 
	* 映射对象关键字
	* @param busAttr
	*/
	public void setBusAttr(String busAttr) {
		this.busAttr = busAttr;
	}

	/** 
	* 映射对象关键字
	* @return
	*/
	public String getBusAttr() {
		return busAttr;
	}

	/** 
	* 表单控件主键
	* @param formCol
	*/
	public void setFormCol(String formCol) {
		this.formCol = formCol;
	}

	/** 
	* 表单控件主键
	* @return
	*/
	public String getFormCol() {
		return formCol;
	}

	/** 
	* 组件类型
	* @return
	*/
	public String getComponentkey() {
		return componentkey;
	}

	/** 
	* 组件类型
	* @param componentkey
	*/
	public void setComponentkey(String componentkey) {
		this.componentkey = componentkey;
	}
}
