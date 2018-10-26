package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 积分下属范围
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JfSubUserScope {
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
	* 设置人员
	*/
	@Filed
	private Integer leaderId;
	/** 
	* 下属主键
	*/
	@Filed
	private Integer subUserId;
	/** 
	* 是否参与积分默认1是 0不是 
	*/
	@Filed
	private String needScore;

	/****************以上主要为系统表字段********************/
	/** 
	* 下属名字
	*/
	private String subUserName;

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
	* 设置人员
	* @param leaderId
	*/
	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	/** 
	* 设置人员
	* @return
	*/
	public Integer getLeaderId() {
		return leaderId;
	}

	/** 
	* 下属主键
	* @param subUserId
	*/
	public void setSubUserId(Integer subUserId) {
		this.subUserId = subUserId;
	}

	/** 
	* 下属主键
	* @return
	*/
	public Integer getSubUserId() {
		return subUserId;
	}

	/** 
	* 下属名字
	* @return
	*/
	public String getSubUserName() {
		return subUserName;
	}

	/** 
	* 下属名字
	* @param subUserName
	*/
	public void setSubUserName(String subUserName) {
		this.subUserName = subUserName;
	}

	/** 
	* 是否参与积分默认1是 0不是 
	* @param needScore
	*/
	public void setNeedScore(String needScore) {
		this.needScore = needScore;
	}

	/** 
	* 是否参与积分默认1是 0不是 
	* @return
	*/
	public String getNeedScore() {
		return needScore;
	}
}
