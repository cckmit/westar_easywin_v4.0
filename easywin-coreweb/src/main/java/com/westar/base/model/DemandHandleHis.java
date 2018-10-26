package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 需求处理记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DemandHandleHis {
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
	* 处理人
	*/
	@Filed
	private Integer userId;
	/** 
	* 处理时间
	*/
	@Filed
	private String finishTime;
	/** 
	* 处理结果
	*/
	@Filed
	private String state;
	/** 
	* 处理结果描述
	*/
	@Filed
	private String content;
	/** 
	* 是否为最近处理步骤 0不是 1是
	*/
	@Filed
	private String curStep;
	/** 
	* 需求主键
	*/
	@Filed
	private Integer demandId;
	/** 
	* 处理步骤
	*/
	@Filed
	private String step;

	/****************以上主要为系统表字段********************/
	/** 
	* 阶段负责人名称
	*/
	private String userName;
	/** 
	* 阶段名称
	*/
	private String stageName;

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
	* 处理人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 处理人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 处理时间
	* @param finishTime
	*/
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	/** 
	* 处理时间
	* @return
	*/
	public String getFinishTime() {
		return finishTime;
	}

	/** 
	* 处理结果
	* @param state
	*/
	public void setState(String state) {
		this.state = state;
	}

	/** 
	* 处理结果
	* @return
	*/
	public String getState() {
		return state;
	}

	/** 
	* 处理结果描述
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 处理结果描述
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 是否为最近处理步骤 0不是 1是
	* @param curStep
	*/
	public void setCurStep(String curStep) {
		this.curStep = curStep;
	}

	/** 
	* 是否为最近处理步骤 0不是 1是
	* @return
	*/
	public String getCurStep() {
		return curStep;
	}

	/** 
	* 需求主键
	* @param demandId
	*/
	public void setDemandId(Integer demandId) {
		this.demandId = demandId;
	}

	/** 
	* 需求主键
	* @return
	*/
	public Integer getDemandId() {
		return demandId;
	}

	/** 
	* 处理步骤
	* @param step
	*/
	public void setStep(String step) {
		this.step = step;
	}

	/** 
	* 处理步骤
	* @return
	*/
	public String getStep() {
		return step;
	}

	/** 
	* 阶段负责人名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 阶段负责人名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 阶段名称
	* @return
	*/
	public String getStageName() {
		return stageName;
	}

	/** 
	* 阶段名称
	* @param stageName
	*/
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
}
