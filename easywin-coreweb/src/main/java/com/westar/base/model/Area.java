package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 区域表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Area {
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
	* 区域名称
	*/
	@Filed
	private String areaName;
	/** 
	* 区域父ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 排序
	*/
	@Filed
	private Integer areaOrder;
	/** 
	* 拼音 全拼
	*/
	@Filed
	private String allSpelling;
	/** 
	* 拼音 首字母
	*/
	@Filed
	private String firstSpelling;
	/** 
	* 模板区域ID
	*/
	@Filed
	private Integer regionId;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否可编辑标识符
	*/
	private boolean enable;
	/** 
	* 区域子集
	*/
	private List<Area> children;
	/** 
	* 子集数
	*/
	private Integer childSum;
	/** 
	* 是否为叶子节点
	*/
	private Integer isLeaf;
	/** 
	* 等级
	*/
	private Integer level;
	/** 
	* 模板父ID
	*/
	private Integer modParentId;
	/** 
	* 区域客户数
	*/
	private Integer crmSum;
	/** 
	* 是否需要导入自身
	*/
	private Integer needMod;
	/** 
	* 是否需要导入节点
	*/
	private Integer needLeafMod;

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
	* 区域名称
	* @param areaName
	*/
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/** 
	* 区域名称
	* @return
	*/
	public String getAreaName() {
		return areaName;
	}

	/** 
	* 区域父ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 区域父ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
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

	public boolean isEnable() {
		return enable;
	}

	/** 
	* 是否可编辑标识符
	* @param enable
	*/
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/** 
	* 排序
	* @param areaOrder
	*/
	public void setAreaOrder(Integer areaOrder) {
		this.areaOrder = areaOrder;
	}

	/** 
	* 排序
	* @return
	*/
	public Integer getAreaOrder() {
		return areaOrder;
	}

	/** 
	* 区域子集
	* @return
	*/
	public List<Area> getChildren() {
		return children;
	}

	/** 
	* 区域子集
	* @param children
	*/
	public void setChildren(List<Area> children) {
		this.children = children;
	}

	/** 
	* 子集数
	* @return
	*/
	public Integer getChildSum() {
		return childSum;
	}

	/** 
	* 子集数
	* @param childSum
	*/
	public void setChildSum(Integer childSum) {
		this.childSum = childSum;
	}

	/** 
	* 是否为叶子节点
	* @return
	*/
	public Integer getIsLeaf() {
		return isLeaf;
	}

	/** 
	* 是否为叶子节点
	* @param isLeaf
	*/
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	/** 
	* 等级
	* @return
	*/
	public Integer getLevel() {
		return level;
	}

	/** 
	* 等级
	* @param level
	*/
	public void setLevel(Integer level) {
		this.level = level;
	}

	/** 
	* 拼音 全拼
	* @param allSpelling
	*/
	public void setAllSpelling(String allSpelling) {
		this.allSpelling = allSpelling;
	}

	/** 
	* 拼音 全拼
	* @return
	*/
	public String getAllSpelling() {
		return allSpelling;
	}

	/** 
	* 拼音 首字母
	* @param firstSpelling
	*/
	public void setFirstSpelling(String firstSpelling) {
		this.firstSpelling = firstSpelling;
	}

	/** 
	* 拼音 首字母
	* @return
	*/
	public String getFirstSpelling() {
		return firstSpelling;
	}

	/** 
	* 模板区域ID
	* @param regionId
	*/
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	/** 
	* 模板区域ID
	* @return
	*/
	public Integer getRegionId() {
		return regionId;
	}

	/** 
	* 模板父ID
	* @return
	*/
	public Integer getModParentId() {
		return modParentId;
	}

	/** 
	* 模板父ID
	* @param modParentId
	*/
	public void setModParentId(Integer modParentId) {
		this.modParentId = modParentId;
	}

	/** 
	* 区域客户数
	* @return
	*/
	public Integer getCrmSum() {
		return crmSum;
	}

	/** 
	* 区域客户数
	* @param crmSum
	*/
	public void setCrmSum(Integer crmSum) {
		this.crmSum = crmSum;
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
}
