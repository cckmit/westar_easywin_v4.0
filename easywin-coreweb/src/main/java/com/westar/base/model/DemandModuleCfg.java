package com.westar.base.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;

/** 
 * 需求处理模板配置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DemandModuleCfg {
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
	* 需求模板名称
	*/
	@Filed
	private String modName;
	/** 
	* 操作者id
	*/
	@Filed
	private Integer userId;
	/** 
	* 更新时间
	*/
	@Filed
	private String modifyTime;
	/** 
	* 更新人员
	*/
	@Filed
	private Integer modifyUserId;

	/****************以上主要为系统表字段********************/
	/** 
	* 创建人名称
	*/
	private String userName;
	/** 
	* 更新人姓名
	*/
	private String modifyUserName;
	/** 
	* 需求处理模板步骤
	*/
	private List<DemandHandleStepCfg> listDemandHandleStepCfg;

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
	* 需求模板名称
	* @param modName
	*/
	public void setModName(String modName) {
		this.modName = modName;
	}

	/** 
	* 需求模板名称
	* @return
	*/
	public String getModName() {
		return modName;
	}

	/** 
	* 操作者id
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 操作者id
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 需求处理模板步骤
	* @return
	*/
	public List<DemandHandleStepCfg> getListDemandHandleStepCfg() {
		return listDemandHandleStepCfg;
	}

	/** 
	* 需求处理模板步骤
	* @param listDemandHandleStepCfg
	*/
	public void setListDemandHandleStepCfg(List<DemandHandleStepCfg> listDemandHandleStepCfg) {
		this.listDemandHandleStepCfg = listDemandHandleStepCfg;
	}

	/** 
	* 更新时间
	* @param modifyTime
	*/
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/** 
	* 更新时间
	* @return
	*/
	public String getModifyTime() {
		return modifyTime;
	}

	/** 
	* 创建人名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 创建人名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 更新人员
	* @param modifyUserId
	*/
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	/** 
	* 更新人员
	* @return
	*/
	public Integer getModifyUserId() {
		return modifyUserId;
	}

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}
	
	
}
