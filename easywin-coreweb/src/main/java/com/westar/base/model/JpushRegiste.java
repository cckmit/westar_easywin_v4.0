package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 极光标识
 */
@Table
@JsonInclude(Include.NON_NULL)
public class JpushRegiste {
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
	* 用户主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 极光标识
	*/
	@Filed
	private String registrationId;
	/** 
	* 使用的源头 1 android 2 IOS
	*/
	@Filed
	private String appSource;

	/****************以上主要为系统表字段********************/

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
	* 用户主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 用户主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 极光标识
	* @param registrationId
	*/
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	/** 
	* 极光标识
	* @return
	*/
	public String getRegistrationId() {
		return registrationId;
	}

	/** 
	* 使用的源头 1 android 2 IOS
	* @param appSource
	*/
	public void setAppSource(String appSource) {
		this.appSource = appSource;
	}

	/** 
	* 使用的源头 1 android 2 IOS
	* @return
	*/
	public String getAppSource() {
		return appSource;
	}
}
