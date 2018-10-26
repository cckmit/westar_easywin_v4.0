package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 产品附件关联
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ProUpFiles {
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
	* 产品主键
	*/
	@Filed
	private Integer proId;
	/** 
	* 附件id
	*/
	@Filed
	private Integer upFileId;
	/** 
	* 操作人
	*/
	@Filed
	private Integer uploader;

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
	private String uploaderName;
	/** 
	* 上传人头像uuid
	*/
	private String userUuid;
	/** 
	* 上传人头像名称
	*/
	private String userFileName;
	/** 
	* 附件来源的任务名称
	*/
	private String proName;
	/** 
	* 来源类别
	*/
	private String type;
	/** 
	* 附件来源的任务名称
	*/
	private String fileExt;
	/** 
	* 文件大小
	*/
	private String sizem;
	/** 
	* 任务留言主键
	*/
	private String proTalkId;
	/** 
	* 排序
	*/
	private String order;

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
	* 产品主键
	* @param proId
	*/
	public void setProId(Integer proId) {
		this.proId = proId;
	}

	/** 
	* 产品主键
	* @return
	*/
	public Integer getProId() {
		return proId;
	}

	/** 
	* 附件id
	* @param upFileId
	*/
	public void setUpFileId(Integer upFileId) {
		this.upFileId = upFileId;
	}

	/** 
	* 附件id
	* @return
	*/
	public Integer getUpFileId() {
		return upFileId;
	}

	/** 
	* 操作人
	* @param uploader
	*/
	public void setUploader(Integer uploader) {
		this.uploader = uploader;
	}

	/** 
	* 操作人
	* @return
	*/
	public Integer getUploader() {
		return uploader;
	}

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

	public String getUploaderName() {
		return uploaderName;
	}

	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
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
	* 附件来源的任务名称
	* @return
	*/
	public String getProName() {
		return proName;
	}

	/** 
	* 附件来源的任务名称
	* @param proName
	*/
	public void setProName(String proName) {
		this.proName = proName;
	}

	/** 
	* 来源类别
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 来源类别
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 附件来源的任务名称
	* @return
	*/
	public String getFileExt() {
		return fileExt;
	}

	/** 
	* 附件来源的任务名称
	* @param fileExt
	*/
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/** 
	* 文件大小
	* @return
	*/
	public String getSizem() {
		return sizem;
	}

	/** 
	* 文件大小
	* @param sizem
	*/
	public void setSizem(String sizem) {
		this.sizem = sizem;
	}

	/** 
	* 任务留言主键
	* @return
	*/
	public String getProTalkId() {
		return proTalkId;
	}

	/** 
	* 任务留言主键
	* @param proTalkId
	*/
	public void setProTalkId(String proTalkId) {
		this.proTalkId = proTalkId;
	}

	/** 
	* 排序
	* @return
	*/
	public String getOrder() {
		return order;
	}

	/** 
	* 排序
	* @param order
	*/
	public void setOrder(String order) {
		this.order = order;
	}
}
