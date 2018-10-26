package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 系统升级日志表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SysUpgradeLog {
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
	* 版本
	*/
	@Filed
	private String versionName;
	/** 
	* 升级内容
	*/
	@Filed
	private String content;
	/** 
	* 平台类型  0PC1安卓2苹果
	*/
	@Filed
	private Integer terraceType;

	/****************以上主要为系统表字段********************/
	private String upgradeTime;
	private String version;

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
	* 版本
	* @param versionName
	*/
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/** 
	* 版本
	* @return
	*/
	public String getVersionName() {
		return versionName;
	}

	/** 
	* 升级内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 升级内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 平台类型  0PC1安卓2苹果
	* @param terraceType
	*/
	public void setTerraceType(Integer terraceType) {
		this.terraceType = terraceType;
	}

	/** 
	* 平台类型  0PC1安卓2苹果
	* @return
	*/
	public Integer getTerraceType() {
		return terraceType;
	}

	public void setUpgradeTime(String upgradeTime) {
		this.upgradeTime = upgradeTime;
	}

	public String getUpgradeTime() {
		return upgradeTime;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}
