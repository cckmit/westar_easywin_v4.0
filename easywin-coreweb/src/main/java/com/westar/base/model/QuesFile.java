package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 问题补充图片
 */
@Table
@JsonInclude(Include.NON_NULL)
public class QuesFile {
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
	* 问题补充主键
	*/
	@Filed
	private Integer quesId;
	/** 
	* 附件上传人
	*/
	@Filed
	private Integer userId;
	/** 
	* 排序号
	*/
	@Filed
	private Integer orderNo;
	/** 
	* 关联upfiles主键
	*/
	@Filed
	private Integer original;

	/****************以上主要为系统表字段********************/
	/** 
	* 用于显示原图像
	*/
	private String orgFileUuid;
	private String orgFileName;
	/** 
	* 附件上传人
	*/
	private String username;
	/** 
	* 附件上传时间
	*/
	private String upTime;
	/** 
	* 头像名称
	*/
	private String ImgName;
	/** 
	* 头像UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 1是图片0非图片
	*/
	private String isPic;
	/** 
	* 附件后缀
	*/
	private String fileExt;
	/** 
	* 来源区分
	*/
	private String type;
	/** 
	* 来源id
	*/
	private Integer sourceId;

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
	* 问题补充主键
	* @param quesId
	*/
	public void setQuesId(Integer quesId) {
		this.quesId = quesId;
	}

	/** 
	* 问题补充主键
	* @return
	*/
	public Integer getQuesId() {
		return quesId;
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
	* 排序号
	* @param orderNo
	*/
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/** 
	* 排序号
	* @return
	*/
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 
	* 用于显示原图像
	* @return
	*/
	public String getOrgFileUuid() {
		return orgFileUuid;
	}

	/** 
	* 用于显示原图像
	* @param orgFileUuid
	*/
	public void setOrgFileUuid(String orgFileUuid) {
		this.orgFileUuid = orgFileUuid;
	}

	public String getOrgFileName() {
		return orgFileName;
	}

	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}

	/** 
	* 关联upfiles主键
	* @param original
	*/
	public void setOriginal(Integer original) {
		this.original = original;
	}

	/** 
	* 关联upfiles主键
	* @return
	*/
	public Integer getOriginal() {
		return original;
	}

	/** 
	* 1是图片0非图片
	* @return
	*/
	public String getIsPic() {
		return isPic;
	}

	/** 
	* 1是图片0非图片
	* @param isPic
	*/
	public void setIsPic(String isPic) {
		this.isPic = isPic;
	}

	/** 
	* 附件上传人
	* @return
	*/
	public String getUsername() {
		return username;
	}

	/** 
	* 附件上传人
	* @param username
	*/
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	* 附件上传时间
	* @return
	*/
	public String getUpTime() {
		return upTime;
	}

	/** 
	* 附件上传时间
	* @param upTime
	*/
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	/** 
	* 头像名称
	* @return
	*/
	public String getImgName() {
		return ImgName;
	}

	/** 
	* 头像名称
	* @param ImgName
	*/
	public void setImgName(String imgName) {
		ImgName = imgName;
	}

	/** 
	* 头像UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 头像UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	/** 
	* 附件后缀
	* @return
	*/
	public String getFileExt() {
		return fileExt;
	}

	/** 
	* 附件后缀
	* @param fileExt
	*/
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/** 
	* 来源区分
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 来源区分
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 来源id
	* @return
	*/
	public Integer getSourceId() {
		return sourceId;
	}

	/** 
	* 来源id
	* @param sourceId
	*/
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
}
