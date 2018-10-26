package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 系统表情表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BiaoQing {
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
	* 表情引用地址
	*/
	@Filed
	private String imgPath;
	/** 
	* 表情描述
	*/
	@Filed
	private String imgDescribe;

	/****************以上主要为系统表字段********************/

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
	* 表情引用地址
	* @param imgPath
	*/
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	/** 
	* 表情引用地址
	* @return
	*/
	public String getImgPath() {
		return imgPath;
	}

	/** 
	* 表情描述
	* @param imgDescribe
	*/
	public void setImgDescribe(String imgDescribe) {
		this.imgDescribe = imgDescribe;
	}

	/** 
	* 表情描述
	* @return
	*/
	public String getImgDescribe() {
		return imgDescribe;
	}
}
