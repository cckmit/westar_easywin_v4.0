package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 部门信息
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Department {
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
	* 部门名称
	*/
	@Filed
	private String depName;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 启用状态 参见数据字典enabled
	*/
	@Filed
	private String enabled;
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
	* 部门父节点ID
	*/
	@Filed
	private Integer parentId;

	/****************以上主要为系统表字段********************/
	/** 
	* 创建人姓名
	*/
	private String creatorName;
	/** 
	* 部门父节点
	*/
	private String parentName;
	/** 
	* 部门成员
	*/
	private List<UserInfo> listUser;
	/** 
	* 部门成员的Id
	*/
	private String userIds;
	/** 
	* 树状层级
	*/
	private Integer depLevel;
	/** 
	* 树状层级
	*/
	private Integer rootId;
	/** 
	* 是否为默认部门0不是1是
	*/
	private Integer defDep;
	/** 
	* 是否为子节点0不是，1是
	*/
	private Integer isLeaf;

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
	* 部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
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
	* 启用状态 参见数据字典enabled
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** 
	* 启用状态 参见数据字典enabled
	* @return
	*/
	public String getEnabled() {
		return enabled;
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
	* 部门父节点ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 部门父节点ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 部门父节点
	* @return
	*/
	public String getParentName() {
		return parentName;
	}

	/** 
	* 部门父节点
	* @param parentName
	*/
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/** 
	* 部门成员的Id
	* @return
	*/
	public String getUserIds() {
		return userIds;
	}

	/** 
	* 部门成员的Id
	* @param userIds
	*/
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	/** 
	* 部门成员
	* @return
	*/
	public List<UserInfo> getListUser() {
		return listUser;
	}

	/** 
	* 部门成员
	* @param listUser
	*/
	public void setListUser(List<UserInfo> listUser) {
		this.listUser = listUser;
	}

	/** 
	* 树状层级
	* @return
	*/
	public Integer getDepLevel() {
		return depLevel;
	}

	/** 
	* 树状层级
	* @param depLevel
	*/
	public void setDepLevel(Integer depLevel) {
		this.depLevel = depLevel;
	}

	/** 
	* 树状层级
	* @return
	*/
	public Integer getRootId() {
		return rootId;
	}

	/** 
	* 树状层级
	* @param rootId
	*/
	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}

	/** 
	* 是否为默认部门0不是1是
	* @return
	*/
	public Integer getDefDep() {
		return defDep;
	}

	/** 
	* 是否为默认部门0不是1是
	* @param defDep
	*/
	public void setDefDep(Integer defDep) {
		this.defDep = defDep;
	}

	/** 
	* 是否为子节点0不是，1是
	* @return
	*/
	public Integer getIsLeaf() {
		return isLeaf;
	}

	/** 
	* 是否为子节点0不是，1是
	* @param isLeaf
	*/
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
}
