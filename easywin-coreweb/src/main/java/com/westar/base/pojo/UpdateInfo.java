package com.westar.base.pojo;

import java.io.Serializable;

public class UpdateInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8407085582831528013L;
	
	 //版本名称
	 private Integer version;
	 
	 //版本名称
	 private String versionName;  
	 
	 //程序下载地址
	 private String url;
	 
	 //描述
	 private String description;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	
	 
}