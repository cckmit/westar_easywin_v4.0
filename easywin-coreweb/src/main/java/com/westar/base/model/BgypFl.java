package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 办公用品分类表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BgypFl {
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
	* 父级Id
	*/
	@Filed
	private Integer parentId;
	/** 
	* 分类代码
	*/
	@Filed
	private String flCode;
	/** 
	* 分类名称
	*/
	@Filed
	private String flName;

	/****************以上主要为系统表字段********************/
	/** 
	* 父类名称
	*/
	private String pFlName;

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
	* 父级Id
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 父级Id
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 分类代码
	* @param flCode
	*/
	public void setFlCode(String flCode) {
		this.flCode = flCode;
	}

	/** 
	* 分类代码
	* @return
	*/
	public String getFlCode() {
		return flCode;
	}

	/** 
	* 分类名称
	* @param flName
	*/
	public void setFlName(String flName) {
		this.flName = flName;
	}

	/** 
	* 分类名称
	* @return
	*/
	public String getFlName() {
		return flName;
	}

	public String getpFlName() {
		return pFlName;
	}

	public void setpFlName(String pFlName) {
		this.pFlName = pFlName;
	}
}
