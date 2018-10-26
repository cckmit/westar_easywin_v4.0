package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 模块与审批流程之间关联
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BusMapFlow {
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
	* 企业编号 0为系统默认
	*/
	@Filed
	private Integer comId;
	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 关联流程主键
	*/
	@Filed
	private Integer flowId;
	/** 
	* 配置描述
	*/
	@Filed
	private String description;

	/****************以上主要为系统表字段********************/
	/** 
	* 关联流程名称
	*/
	private String flowName;
	/** 
	* 键值字符串数组
	*/
	private String[] keyValueArray;
	/** 
	* 数据关联权限控制集合
	*/
	private List<BusMapFlowAuthDep> listBusMapFlowAuthDep;
	/** 
	* 借款业务配置信息
	*/
	private BusMapFlow busMapFlowOfLoan;
	/** 
	* 报销业务配置信息
	*/
	private BusMapFlow busMapFlowOfWriteOff;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;
	/** 
	* 列表排序规则
	*/
	private String orderBy;
	/** 
	* 键值字符串数组(业务数据与表单数据映射关系)
	*/
	private String[] formColMapBusAttrs;

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
	* 企业编号 0为系统默认
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号 0为系统默认
	* @return
	*/
	public Integer getComId() {
		return comId;
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
	* 关联流程主键
	* @param flowId
	*/
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	/** 
	* 关联流程主键
	* @return
	*/
	public Integer getFlowId() {
		return flowId;
	}

	/** 
	* 配置描述
	* @param description
	*/
	public void setDescription(String description) {
		this.description = description;
	}

	/** 
	* 配置描述
	* @return
	*/
	public String getDescription() {
		return description;
	}

	/** 
	* 键值字符串数组
	* @return
	*/
	public String[] getKeyValueArray() {
		return keyValueArray;
	}

	/** 
	* 键值字符串数组
	* @param keyValueArray
	*/
	public void setKeyValueArray(String[] keyValueArray) {
		this.keyValueArray = keyValueArray;
	}

	/** 
	* 数据关联权限控制集合
	* @return
	*/
	public List<BusMapFlowAuthDep> getListBusMapFlowAuthDep() {
		return listBusMapFlowAuthDep;
	}

	/** 
	* 数据关联权限控制集合
	* @param listBusMapFlowAuthDep
	*/
	public void setListBusMapFlowAuthDep(List<BusMapFlowAuthDep> listBusMapFlowAuthDep) {
		this.listBusMapFlowAuthDep = listBusMapFlowAuthDep;
	}

	/** 
	* 关联流程名称
	* @return
	*/
	public String getFlowName() {
		return flowName;
	}

	/** 
	* 关联流程名称
	* @param flowName
	*/
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/** 
	* 借款业务配置信息
	* @return
	*/
	public BusMapFlow getBusMapFlowOfLoan() {
		return busMapFlowOfLoan;
	}

	/** 
	* 借款业务配置信息
	* @param busMapFlowOfLoan
	*/
	public void setBusMapFlowOfLoan(BusMapFlow busMapFlowOfLoan) {
		this.busMapFlowOfLoan = busMapFlowOfLoan;
	}

	/** 
	* 报销业务配置信息
	* @return
	*/
	public BusMapFlow getBusMapFlowOfWriteOff() {
		return busMapFlowOfWriteOff;
	}

	/** 
	* 报销业务配置信息
	* @param busMapFlowOfWriteOff
	*/
	public void setBusMapFlowOfWriteOff(BusMapFlow busMapFlowOfWriteOff) {
		this.busMapFlowOfWriteOff = busMapFlowOfWriteOff;
	}

	/** 
	* 查询的时间起
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的时间起
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的时间止
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的时间止
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 列表排序规则
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 列表排序规则
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/** 
	* 键值字符串数组(业务数据与表单数据映射关系)
	* @return
	*/
	public String[] getFormColMapBusAttrs() {
		return formColMapBusAttrs;
	}

	/** 
	* 键值字符串数组(业务数据与表单数据映射关系)
	* @param formColMapBusAttrs
	*/
	public void setFormColMapBusAttrs(String[] formColMapBusAttrs) {
		this.formColMapBusAttrs = formColMapBusAttrs;
	}
}
