package com.westar.base.model;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
/**
*文档中心日志表
*/


@Table
public class FileLog{

/*主键id*/
 @Identity
 private Integer id;

/*记录创建时间*/
 @DefaultFiled
 private String recordCreateTime;
 /*企业编号*/
 @Filed
 private Integer comId;
 /*分享信息主键*/
 @Filed
 private Integer fileDetailId;
 /*操作者id*/
 @Filed
 private Integer userId;
 /*操作内容*/
 @Filed
 private String content;
 /*操作者姓名*/
 @Filed
 private String userName;

/****************以上主要为系统表字段********************/

 	public FileLog() {
	}

	/** 
	* * * * @param comId 企业编号 * @param fileDetailId 信息主键 * @param userId 发言人主键 * @param userName 发言人姓名 * @param content 日志内容 		
	*/
	public FileLog(Integer comId, Integer fileDetailId, Integer userId, String content, String userName) {
		super();
		this.comId = comId;
		this.fileDetailId = fileDetailId;
		this.userId = userId;
		this.content = content;
		this.userName = userName;
	}

	private String speakerName;
	/** 
	* 附件名称
	*/
	private String filename;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;

/****************以上为自己添加字段********************/

 /**
 * 主键id
 * @param id
 */
 public void setId(Integer id) {
 	this.id=id;
 }

 /**
 * 主键id
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
 	this.recordCreateTime=recordCreateTime;
 }

 /**
 * 记录创建时间
 * @return
 */
 public String getRecordCreateTime() {
 	return recordCreateTime;
 }

 /**
 *企业编号
 * @param comId
 */
 public void setComId(Integer comId) {
 	this.comId = comId;
 }

 /**
 *企业编号
 * @return
 */
 public Integer getComId() {
 	return comId;
 }

 /**
 *分享信息主键
 * @param fileDetailId
 */
 public void setFileDetailId(Integer fileDetailId) {
 	this.fileDetailId = fileDetailId;
 }

 /**
 *分享信息主键
 * @return
 */
 public Integer getFileDetailId() {
 	return fileDetailId;
 }

 /**
 *操作者id
 * @param userId
 */
 public void setUserId(Integer userId) {
 	this.userId = userId;
 }

 /**
 *操作者id
 * @return
 */
 public Integer getUserId() {
 	return userId;
 }

 /**
 *操作内容
 * @param content
 */
 public void setContent(String content) {
 	this.content = content;
 }

 /**
 *操作内容
 * @return
 */
 public String getContent() {
 	return content;
 }

 /**
 *操作者姓名
 * @param userName
 */
 public void setUserName(String userName) {
 	this.userName = userName;
 }

 /**
 *操作者姓名
 * @return
 */
 public String getUserName() {
 	return userName;
 }

public String getSpeakerName() {
	return speakerName;
}

public void setSpeakerName(String speakerName) {
	this.speakerName = speakerName;
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

 
 
}
