package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 问题回答图片
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AnsFile {
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
	* 回答主键
	*/
	@Filed
	private Integer answerId;
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
	/** 
	* 所属提问
	*/
	@Filed
	private Integer quesId;

	/****************以上主要为系统表字段********************/
	/** 
	* 用于显示原图像
	*/
	private String orgFileUuid;
	private String orgFileName;
	/** 
	* 是否为图片1是0不是
	*/
	private String isPic;
	/** 
	* 是否为图片1是0不是
	*/
	private String fileExt;

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
	* 回答主键
	* @param answerId
	*/
	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	/** 
	* 回答主键
	* @return
	*/
	public Integer getAnswerId() {
		return answerId;
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
	* 所属提问
	* @param quesId
	*/
	public void setQuesId(Integer quesId) {
		this.quesId = quesId;
	}

	/** 
	* 所属提问
	* @return
	*/
	public Integer getQuesId() {
		return quesId;
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
	* 是否为图片1是0不是
	* @return
	*/
	public String getIsPic() {
		return isPic;
	}

	/** 
	* 是否为图片1是0不是
	* @param isPic
	*/
	public void setIsPic(String isPic) {
		this.isPic = isPic;
	}

	/** 
	* 是否为图片1是0不是
	* @return
	*/
	public String getFileExt() {
		return fileExt;
	}

	/** 
	* 是否为图片1是0不是
	* @param fileExt
	*/
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
}
