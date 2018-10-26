package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 密码修改的验证码
 */
@Table
@JsonInclude(Include.NON_NULL)
public class PassYzm {
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
	* 确认码
	*/
	@Filed
	private String passYzm;
	/** 
	* 邮箱
	*/
	@Filed
	private String account;

	/****************以上主要为系统表字段********************/
	/** 
	* 是否可用1可用0过期
	*/
	private Integer enabled;

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
	* 确认码
	* @param passYzm
	*/
	public void setPassYzm(String passYzm) {
		this.passYzm = passYzm;
	}

	/** 
	* 确认码
	* @return
	*/
	public String getPassYzm() {
		return passYzm;
	}

	/** 
	* 是否可用1可用0过期
	* @return
	*/
	public Integer getEnabled() {
		return enabled;
	}

	/** 
	* 是否可用1可用0过期
	* @param enabled
	*/
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	/** 
	* 邮箱
	* @param account
	*/
	public void setAccount(String account) {
		this.account = account;
	}

	/** 
	* 邮箱
	* @return
	*/
	public String getAccount() {
		return account;
	}
}
