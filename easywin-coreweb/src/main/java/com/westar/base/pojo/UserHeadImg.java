package com.westar.base.pojo;

/**
 * 人员头像信息
 * @author zzq
 *
 */
public class UserHeadImg {

	//人员性别
	private Integer userGender;
	//人员姓名
	private String userName;
	//人员头像的uuid
	private String userImgUuid;
	//人员头像名称
	private String userImgName;
	
	public Integer getUserGender() {
		return userGender;
	}
	public void setUserGender(Integer userGender) {
		this.userGender = userGender;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImgUuid() {
		return userImgUuid;
	}
	public void setUserImgUuid(String userImgUuid) {
		this.userImgUuid = userImgUuid;
	}
	public String getUserImgName() {
		return userImgName;
	}
	public void setUserImgName(String userImgName) {
		this.userImgName = userImgName;
	}
}
