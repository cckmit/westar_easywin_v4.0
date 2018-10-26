package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 人事调动附件
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RsdaTranceFile {
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
	* 调动主键
	*/
	@Filed
	private Integer tranceId;
	/** 
	* 附件主键
	*/
	@Filed
	private Integer upfileId;

	/****************以上主要为系统表字段********************/
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件uuid
	*/
	private String fileUuid;
	/** 
	* 附件后缀
	*/
	private String fileExt;
	/** 
	* 1是图片0非图片
	*/
	private String isPic;

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
	* 调动主键
	* @param tranceId
	*/
	public void setTranceId(Integer tranceId) {
		this.tranceId = tranceId;
	}

	/** 
	* 调动主键
	* @return
	*/
	public Integer getTranceId() {
		return tranceId;
	}

	/** 
	* 附件主键
	* @param upfileId
	*/
	public void setUpfileId(Integer upfileId) {
		this.upfileId = upfileId;
	}

	/** 
	* 附件主键
	* @return
	*/
	public Integer getUpfileId() {
		return upfileId;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFileName() {
		return fileName;
	}

	/** 
	* 附件名称
	* @param fileName
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	* 附件uuid
	* @return
	*/
	public String getFileUuid() {
		return fileUuid;
	}

	/** 
	* 附件uuid
	* @param fileUuid
	*/
	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
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
}
