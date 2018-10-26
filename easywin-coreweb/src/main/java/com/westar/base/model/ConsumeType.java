package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 费用类型
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ConsumeType {
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
	* 类型名称
	*/
	@Filed
	private String typeName;
	/** 
	* 费用类型排序
	*/
	@Filed
	private Integer typeOrder;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 是否显示消费人数  0不显示1显示
	*/
	@Filed
	private Integer showConsumePersonNum;
	/** 
	* 是否显示消费开始日期  0不显示1显示
	*/
	@Filed
	private Integer showStartDate;
	/** 
	* 是否显示消费结束日期 0不显示1显示
	*/
	@Filed
	private Integer showEndDate;
	/** 
	* 是否显示出发地 0不显示1显示
	*/
	@Filed
	private Integer showLeavePlace;
	/** 
	* 是否显示目的地 0不显示1显示
	*/
	@Filed
	private Integer showArrivePlace;

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
	* 类型名称
	* @param typeName
	*/
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** 
	* 类型名称
	* @return
	*/
	public String getTypeName() {
		return typeName;
	}

	/** 
	* 费用类型排序
	* @param typeOrder
	*/
	public void setTypeOrder(Integer typeOrder) {
		this.typeOrder = typeOrder;
	}

	/** 
	* 费用类型排序
	* @return
	*/
	public Integer getTypeOrder() {
		return typeOrder;
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
	* 是否显示消费人数  0不显示1显示
	* @param showConsumePersonNum
	*/
	public void setShowConsumePersonNum(Integer showConsumePersonNum) {
		this.showConsumePersonNum = showConsumePersonNum;
	}

	/** 
	* 是否显示消费人数  0不显示1显示
	* @return
	*/
	public Integer getShowConsumePersonNum() {
		return showConsumePersonNum;
	}

	/** 
	* 是否显示消费开始日期  0不显示1显示
	* @param showStartDate
	*/
	public void setShowStartDate(Integer showStartDate) {
		this.showStartDate = showStartDate;
	}

	/** 
	* 是否显示消费开始日期  0不显示1显示
	* @return
	*/
	public Integer getShowStartDate() {
		return showStartDate;
	}

	/** 
	* 是否显示消费结束日期 0不显示1显示
	* @param showEndDate
	*/
	public void setShowEndDate(Integer showEndDate) {
		this.showEndDate = showEndDate;
	}

	/** 
	* 是否显示消费结束日期 0不显示1显示
	* @return
	*/
	public Integer getShowEndDate() {
		return showEndDate;
	}

	/** 
	* 是否显示出发地 0不显示1显示
	* @param showLeavePlace
	*/
	public void setShowLeavePlace(Integer showLeavePlace) {
		this.showLeavePlace = showLeavePlace;
	}

	/** 
	* 是否显示出发地 0不显示1显示
	* @return
	*/
	public Integer getShowLeavePlace() {
		return showLeavePlace;
	}

	/** 
	* 是否显示目的地 0不显示1显示
	* @param showArrivePlace
	*/
	public void setShowArrivePlace(Integer showArrivePlace) {
		this.showArrivePlace = showArrivePlace;
	}

	/** 
	* 是否显示目的地 0不显示1显示
	* @return
	*/
	public Integer getShowArrivePlace() {
		return showArrivePlace;
	}
}
