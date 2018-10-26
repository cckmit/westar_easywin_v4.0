package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 任务讨论附件
 */
@Table
@JsonInclude(Include.NON_NULL)
public class TaskTalkUpfile {
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
	* 共享任务主键
	*/
	@Filed
	private Integer taskId;
	/** 
	* 共享任务主键
	*/
	@Filed
	private Integer taskTalkId;
	/** 
	* 关联upfiles主键
	*/
	@Filed
	private Integer upfileId;
	/** 
	* 附件上传人
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 附件名称
	*/
	private String filename;
	/** 
	* 0女1男
	*/
	private String gender;
	private String creatorName;
	/** 
	* 上传人头像uuid
	*/
	private String userUuid;
	/** 
	* 上传人头像名称
	*/
	private String userFileName;
	/** 
	* 1为图片0非图片
	*/
	private String isPic;
	/** 
	* 1为图片0非图片
	*/
	private String fileExt;

	/****************以上为自己添加字段********************/
	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFilename() {
		return filename;
	}

	/** 
	* 附件名称
	* @param filename
	*/
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 上传人头像uuid
	* @return
	*/
	public String getUserUuid() {
		return userUuid;
	}

	/** 
	* 上传人头像uuid
	* @param userUuid
	*/
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	/** 
	* 上传人头像名称
	* @return
	*/
	public String getUserFileName() {
		return userFileName;
	}

	/** 
	* 上传人头像名称
	* @param userFileName
	*/
	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

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
	* 共享任务主键
	* @param taskId
	*/
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/** 
	* 共享任务主键
	* @return
	*/
	public Integer getTaskId() {
		return taskId;
	}

	/** 
	* 共享任务主键
	* @param taskTalkId
	*/
	public void setTaskTalkId(Integer taskTalkId) {
		this.taskTalkId = taskTalkId;
	}

	/** 
	* 共享任务主键
	* @return
	*/
	public Integer getTaskTalkId() {
		return taskTalkId;
	}

	/** 
	* 关联upfiles主键
	* @param upfileId
	*/
	public void setUpfileId(Integer upfileId) {
		this.upfileId = upfileId;
	}

	/** 
	* 关联upfiles主键
	* @return
	*/
	public Integer getUpfileId() {
		return upfileId;
	}

	/** 
	* 附件上传人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 附件上传人
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 1为图片0非图片
	* @return
	*/
	public String getIsPic() {
		return isPic;
	}

	/** 
	* 1为图片0非图片
	* @param isPic
	*/
	public void setIsPic(String isPic) {
		this.isPic = isPic;
	}

	/** 
	* 1为图片0非图片
	* @return
	*/
	public String getFileExt() {
		return fileExt;
	}

	/** 
	* 1为图片0非图片
	* @param fileExt
	*/
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
}
