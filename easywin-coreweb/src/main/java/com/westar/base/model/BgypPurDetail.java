package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 用品采购清单
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BgypPurDetail {
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
	* 采购清单Id
	*/
	@Filed
	private Integer purOrderId;
	/** 
	* 办公用品Id
	*/
	@Filed
	private Integer bgypItemId;
	/** 
	* 用品价格
	*/
	@Filed
	private String bgypPrice;
	/** 
	* 用品购买数量
	*/
	@Filed
	private Integer bgypNum;
	/** 
	* 入库状态0未入库 1已入库
	*/
	@Filed
	private String storeState;

	/****************以上主要为系统表字段********************/
	private String bgypName;
	private String bgypSpec;
	private String bgypUnit;
	/** 
	* 用品单位名称
	*/
	private String bgypUnitName;

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
	* 采购清单Id
	* @param purOrderId
	*/
	public void setPurOrderId(Integer purOrderId) {
		this.purOrderId = purOrderId;
	}

	/** 
	* 采购清单Id
	* @return
	*/
	public Integer getPurOrderId() {
		return purOrderId;
	}

	/** 
	* 办公用品Id
	* @param bgypItemId
	*/
	public void setBgypItemId(Integer bgypItemId) {
		this.bgypItemId = bgypItemId;
	}

	/** 
	* 办公用品Id
	* @return
	*/
	public Integer getBgypItemId() {
		return bgypItemId;
	}

	/** 
	* 用品价格
	* @param bgypPrice
	*/
	public void setBgypPrice(String bgypPrice) {
		this.bgypPrice = bgypPrice;
	}

	/** 
	* 用品价格
	* @return
	*/
	public String getBgypPrice() {
		return bgypPrice;
	}

	/** 
	* 用品购买数量
	* @param bgypNum
	*/
	public void setBgypNum(Integer bgypNum) {
		this.bgypNum = bgypNum;
	}

	/** 
	* 用品购买数量
	* @return
	*/
	public Integer getBgypNum() {
		return bgypNum;
	}

	public String getBgypName() {
		return bgypName;
	}

	public void setBgypName(String bgypName) {
		this.bgypName = bgypName;
	}

	public String getBgypSpec() {
		return bgypSpec;
	}

	public void setBgypSpec(String bgypSpec) {
		this.bgypSpec = bgypSpec;
	}

	public String getBgypUnit() {
		return bgypUnit;
	}

	public void setBgypUnit(String bgypUnit) {
		this.bgypUnit = bgypUnit;
	}

	/** 
	* 用品单位名称
	* @return
	*/
	public String getBgypUnitName() {
		return bgypUnitName;
	}

	/** 
	* 用品单位名称
	* @param bgypUnitName
	*/
	public void setBgypUnitName(String bgypUnitName) {
		this.bgypUnitName = bgypUnitName;
	}

	/** 
	* 入库状态0未入库 1已入库
	* @param storeState
	*/
	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}

	/** 
	* 入库状态0未入库 1已入库
	* @return
	*/
	public String getStoreState() {
		return storeState;
	}
}
