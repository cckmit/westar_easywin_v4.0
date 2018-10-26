package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 表单数据与业务数据映射关系
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BusAttrMapFormCol {
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
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 表单控件主键
	*/
	@Filed
	private String formCol;
	/** 
	* 映射对象关键字
	*/
	@Filed
	private String busAttr;
	/** 
	* 默认数据不填写
	*/
	@Filed
	private String isRequire;

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
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
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
	* 默认数据不填写
	* @param isRequire
	*/
	public void setIsRequire(String isRequire) {
		this.isRequire = isRequire;
	}

	/** 
	* 默认数据不填写
	* @return
	*/
	public String getIsRequire() {
		return isRequire;
	}
}
