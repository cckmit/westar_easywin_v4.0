package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 消费记录附件
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ConsumeUpfile {
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
	* 消费记录主键
	*/
	@Filed
	private Integer consumeId;
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
	private String creatorName;
	/** 
	* 1是图片0非图片
	*/
	private String isPic;
	/** 
	* 1是图片0非图片
	*/
	private String fileExt;
	/** 
	* 来源类别
	*/
	private String type;
	/** 
	* 来源主键
	*/
	private Integer busId;
	/** 
	* 来源名称
	*/
	private String busName;

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
	* 消费记录主键
	* @param consumeId
	*/
	public void setConsumeId(Integer consumeId) {
		this.consumeId = consumeId;
	}

	/** 
	* 消费记录主键
	* @return
	*/
	public Integer getConsumeId() {
		return consumeId;
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
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 0女1男
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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
	* 1是图片0非图片
	* @return
	*/
	public String getFileExt() {
		return fileExt;
	}

	/** 
	* 1是图片0非图片
	* @param fileExt
	*/
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
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
	* 来源主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 来源主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 来源名称
	* @return
	*/
	public String getBusName() {
		return busName;
	}

	/** 
	* 来源名称
	* @param busName
	*/
	public void setBusName(String busName) {
		this.busName = busName;
	}
}
