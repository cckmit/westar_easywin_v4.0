package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 节假日日期
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FestModDate implements Cloneable {
	@Override
	protected FestModDate clone() throws CloneNotSupportedException {
		return (FestModDate) super.clone();
	}

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
	* 企业编号 0为系统默认
	*/
	@Filed
	private Integer comId;
	/** 
	* 关联festMod主键
	*/
	@Filed
	private Integer festModId;
	/** 
	* 节日日期
	*/
	@Filed
	private String festDate;
	/** 
	* 1休息日 2工作日
	*/
	@Filed
	private Integer status;

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
	* 企业编号 0为系统默认
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号 0为系统默认
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 关联festMod主键
	* @param festModId
	*/
	public void setFestModId(Integer festModId) {
		this.festModId = festModId;
	}

	/** 
	* 关联festMod主键
	* @return
	*/
	public Integer getFestModId() {
		return festModId;
	}

	/** 
	* 节日日期
	* @param festDate
	*/
	public void setFestDate(String festDate) {
		this.festDate = festDate;
	}

	/** 
	* 节日日期
	* @return
	*/
	public String getFestDate() {
		return festDate;
	}

	/** 
	* 1休息日 2工作日
	* @param status
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	* 1休息日 2工作日
	* @return
	*/
	public Integer getStatus() {
		return status;
	}
}
