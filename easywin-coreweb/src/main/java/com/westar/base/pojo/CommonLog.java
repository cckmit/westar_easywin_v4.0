package com.westar.base.pojo;


public class CommonLog {
	/*记录创建时间*/
	 private String recordCreateTime;
	 
	 /*企业编号*/
	 private Integer comId;

	 /*操作内容*/
	 private String content;
	 /*操作者姓名*/
	 private String userName;

	 //附件名称
	 private String filename;
	 
	//附件UUID
	private Integer userId;
	 
	 //附件UUID
	 private String uuid;
	 
	 //0女1男
	 private String gender;
	 

	public String getRecordCreateTime() {
		return recordCreateTime;
	}

	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	public Integer getComId() {
		return comId;
	}

	public void setComId(Integer comId) {
		this.comId = comId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	 
}
