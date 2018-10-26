package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分标准
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Jfzb {
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
	* 录入人员
	*/
	@Filed
	private Integer recorderId;
	/** 
	* 一级指标
	*/
	@Filed
	private Integer jfzbTypeId;
	/** 
	* 二级指标
	*/
	@Filed
	private String leveTwo;
	/** 
	* 考核标准
	*/
	@Filed
	private String describe;
	/** 
	* 备注
	*/
	@Filed
	private String remark;
	/** 
	* 最高分
	*/
	@Filed
	private String jfTop;
	/** 
	* 最低分
	*/
	@Filed
	private String jfBottom;

	/****************以上主要为系统表字段********************/
	/** 
	* 积分指标类型
	*/
	private String jfzbTypeName;
	/** 
	* 适用的部门名称
	*/
	private String depName;
	/** 
	* 头像信息
	*/
	private String recorderUuid;
	private String recorderGender;
	/** 
	* 录入人员名称
	*/
	private String recorderName;
	/** 
	* 适用范围
	*/
	private List<JfzbDepScope> listJfzbDepScope;
	/** 
	* 适用人员主键
	*/
	private Integer scopeUserId;

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
	* 录入人员
	* @param recorderId
	*/
	public void setRecorderId(Integer recorderId) {
		this.recorderId = recorderId;
	}

	/** 
	* 录入人员
	* @return
	*/
	public Integer getRecorderId() {
		return recorderId;
	}

	/** 
	* 一级指标
	* @param jfzbTypeId
	*/
	public void setJfzbTypeId(Integer jfzbTypeId) {
		this.jfzbTypeId = jfzbTypeId;
	}

	/** 
	* 一级指标
	* @return
	*/
	public Integer getJfzbTypeId() {
		return jfzbTypeId;
	}

	/** 
	* 二级指标
	* @param leveTwo
	*/
	public void setLeveTwo(String leveTwo) {
		this.leveTwo = leveTwo;
	}

	/** 
	* 二级指标
	* @return
	*/
	public String getLeveTwo() {
		return leveTwo;
	}

	/** 
	* 考核标准
	* @param describe
	*/
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/** 
	* 考核标准
	* @return
	*/
	public String getDescribe() {
		return describe;
	}

	/** 
	* 备注
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 备注
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 最高分
	* @param jfTop
	*/
	public void setJfTop(String jfTop) {
		this.jfTop = jfTop;
	}

	/** 
	* 最高分
	* @return
	*/
	public String getJfTop() {
		return jfTop;
	}

	/** 
	* 最低分
	* @param jfBottom
	*/
	public void setJfBottom(String jfBottom) {
		this.jfBottom = jfBottom;
	}

	/** 
	* 最低分
	* @return
	*/
	public String getJfBottom() {
		return jfBottom;
	}

	/** 
	* 积分指标类型
	* @return
	*/
	public String getJfzbTypeName() {
		return jfzbTypeName;
	}

	/** 
	* 积分指标类型
	* @param jfzbTypeName
	*/
	public void setJfzbTypeName(String jfzbTypeName) {
		this.jfzbTypeName = jfzbTypeName;
	}

	/** 
	* 适用的部门名称
	* @return
	*/
	public String getDepName() {
		return depName;
	}

	/** 
	* 适用的部门名称
	* @param depName
	*/
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 录入人员名称
	* @return
	*/
	public String getRecorderName() {
		return recorderName;
	}

	/** 
	* 录入人员名称
	* @param recorderName
	*/
	public void setRecorderName(String recorderName) {
		this.recorderName = recorderName;
	}

	/** 
	* 适用范围
	* @return
	*/
	public List<JfzbDepScope> getListJfzbDepScope() {
		return listJfzbDepScope;
	}

	/** 
	* 适用范围
	* @param listJfzbDepScope
	*/
	public void setListJfzbDepScope(List<JfzbDepScope> listJfzbDepScope) {
		this.listJfzbDepScope = listJfzbDepScope;
	}

	/** 
	* 头像信息
	* @return
	*/
	public String getRecorderUuid() {
		return recorderUuid;
	}

	/** 
	* 头像信息
	* @param recorderUuid
	*/
	public void setRecorderUuid(String recorderUuid) {
		this.recorderUuid = recorderUuid;
	}

	public String getRecorderGender() {
		return recorderGender;
	}

	public void setRecorderGender(String recorderGender) {
		this.recorderGender = recorderGender;
	}

	/** 
	* 适用人员主键
	* @return
	*/
	public Integer getScopeUserId() {
		return scopeUserId;
	}

	/** 
	* 适用人员主键
	* @param scopeUserId
	*/
	public void setScopeUserId(Integer scopeUserId) {
		this.scopeUserId = scopeUserId;
	}
}
