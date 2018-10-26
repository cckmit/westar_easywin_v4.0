package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 行政区划
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Region {
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
	private String comId;
	/** 
	* 行政区划代码
	*/
	@Filed
	private String regionCode;
	/** 
	* 行政区划名称
	*/
	@Filed
	private String regionName;
	/** 
	* 父行政区划
	*/
	@Filed
	private Integer parentId;
	/** 
	* 层级
	*/
	@Filed
	private Integer regionLevel;
	/** 
	* 排序，用来调整顺序
	*/
	@Filed
	private Integer regionOrder;
	/** 
	* 行政区划英文名称
	*/
	@Filed
	private String regionPingying;
	/** 
	* 行政区划简称
	*/
	@Filed
	private String regionShortpy;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否为叶子
	*/
	private Integer isLeaf;
	/** 
	* 是否需要导入自身
	*/
	private Integer needMod;
	/** 
	* 是否需要导入节点
	*/
	private Integer needLeafMod;
	/** 
	* 已导的区域主键
	*/
	private Integer areaId;

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
	public void setComId(String comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号
	* @return
	*/
	public String getComId() {
		return comId;
	}

	/** 
	* 行政区划代码
	* @param regionCode
	*/
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	/** 
	* 行政区划代码
	* @return
	*/
	public String getRegionCode() {
		return regionCode;
	}

	/** 
	* 行政区划名称
	* @param regionName
	*/
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/** 
	* 行政区划名称
	* @return
	*/
	public String getRegionName() {
		return regionName;
	}

	/** 
	* 父行政区划
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 父行政区划
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 层级
	* @param regionLevel
	*/
	public void setRegionLevel(Integer regionLevel) {
		this.regionLevel = regionLevel;
	}

	/** 
	* 层级
	* @return
	*/
	public Integer getRegionLevel() {
		return regionLevel;
	}

	/** 
	* 排序，用来调整顺序
	* @param regionOrder
	*/
	public void setRegionOrder(Integer regionOrder) {
		this.regionOrder = regionOrder;
	}

	/** 
	* 排序，用来调整顺序
	* @return
	*/
	public Integer getRegionOrder() {
		return regionOrder;
	}

	/** 
	* 行政区划英文名称
	* @param regionPingying
	*/
	public void setRegionPingying(String regionPingying) {
		this.regionPingying = regionPingying;
	}

	/** 
	* 行政区划英文名称
	* @return
	*/
	public String getRegionPingying() {
		return regionPingying;
	}

	/** 
	* 行政区划简称
	* @param regionShortpy
	*/
	public void setRegionShortpy(String regionShortpy) {
		this.regionShortpy = regionShortpy;
	}

	/** 
	* 行政区划简称
	* @return
	*/
	public String getRegionShortpy() {
		return regionShortpy;
	}

	/** 
	* 是否为叶子
	* @return
	*/
	public Integer getIsLeaf() {
		return isLeaf;
	}

	/** 
	* 是否为叶子
	* @param isLeaf
	*/
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	/** 
	* 是否需要导入自身
	* @return
	*/
	public Integer getNeedMod() {
		return needMod;
	}

	/** 
	* 是否需要导入自身
	* @param needMod
	*/
	public void setNeedMod(Integer needMod) {
		this.needMod = needMod;
	}

	/** 
	* 是否需要导入节点
	* @return
	*/
	public Integer getNeedLeafMod() {
		return needLeafMod;
	}

	/** 
	* 是否需要导入节点
	* @param needLeafMod
	*/
	public void setNeedLeafMod(Integer needLeafMod) {
		this.needLeafMod = needLeafMod;
	}

	/** 
	* 已导的区域主键
	* @return
	*/
	public Integer getAreaId() {
		return areaId;
	}

	/** 
	* 已导的区域主键
	* @param areaId
	*/
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
}
