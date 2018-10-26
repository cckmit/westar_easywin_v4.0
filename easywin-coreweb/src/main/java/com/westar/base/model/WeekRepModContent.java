package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 周报模板内容
 */
@Table
@JsonInclude(Include.NON_NULL)
public class WeekRepModContent {
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
	* 模板编号
	*/
	@Filed
	private Integer modId;
	/** 
	* 周报内容层级，见repLev
	*/
	@Filed
	private String repLev;
	/** 
	* 模板各级内容要求
	*/
	@Filed
	private String modContent;
	/** 
	* 模板是否隐藏 0显示1隐藏
	*/
	@Filed
	private String hideState;
	/** 
	* 创建人
	*/
	@Filed
	private Integer cereaterId;
	/** 
	* 是否为系统模板 0 不是 1是
	*/
	@Filed
	private Integer sysState;
	/** 
	* 是否必填，默认不是
	*/
	@Filed
	private String isRequire;

	/****************以上主要为系统表字段********************/
	/** 
	* 内容所属部门
	*/
	private List<ModContDep> listDeps;
	/** 
	* 模板内容所属成员
	*/
	private List<ModContMember> listMembers;

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
	* 模板编号
	* @param modId
	*/
	public void setModId(Integer modId) {
		this.modId = modId;
	}

	/** 
	* 模板编号
	* @return
	*/
	public Integer getModId() {
		return modId;
	}

	/** 
	* 周报内容层级，见repLev
	* @param repLev
	*/
	public void setRepLev(String repLev) {
		this.repLev = repLev;
	}

	/** 
	* 周报内容层级，见repLev
	* @return
	*/
	public String getRepLev() {
		return repLev;
	}

	/** 
	* 模板各级内容要求
	* @param modContent
	*/
	public void setModContent(String modContent) {
		this.modContent = modContent;
	}

	/** 
	* 模板各级内容要求
	* @return
	*/
	public String getModContent() {
		return modContent;
	}

	/** 
	* 模板是否隐藏 0显示1隐藏
	* @param hideState
	*/
	public void setHideState(String hideState) {
		this.hideState = hideState;
	}

	/** 
	* 模板是否隐藏 0显示1隐藏
	* @return
	*/
	public String getHideState() {
		return hideState;
	}

	/** 
	* 创建人
	* @param cereaterId
	*/
	public void setCereaterId(Integer cereaterId) {
		this.cereaterId = cereaterId;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCereaterId() {
		return cereaterId;
	}

	/** 
	* 内容所属部门
	* @return
	*/
	public List<ModContDep> getListDeps() {
		return listDeps;
	}

	/** 
	* 内容所属部门
	* @param listDeps
	*/
	public void setListDeps(List<ModContDep> listDeps) {
		this.listDeps = listDeps;
	}

	/** 
	* 模板内容所属成员
	* @return
	*/
	public List<ModContMember> getListMembers() {
		return listMembers;
	}

	/** 
	* 模板内容所属成员
	* @param listMembers
	*/
	public void setListMembers(List<ModContMember> listMembers) {
		this.listMembers = listMembers;
	}

	/** 
	* 是否为系统模板 0 不是 1是
	* @param sysState
	*/
	public void setSysState(Integer sysState) {
		this.sysState = sysState;
	}

	/** 
	* 是否为系统模板 0 不是 1是
	* @return
	*/
	public Integer getSysState() {
		return sysState;
	}

	/** 
	* 是否必填，默认不是
	* @param isRequire
	*/
	public void setIsRequire(String isRequire) {
		this.isRequire = isRequire;
	}

	/** 
	* 是否必填，默认不是
	* @return
	*/
	public String getIsRequire() {
		return isRequire;
	}
}
