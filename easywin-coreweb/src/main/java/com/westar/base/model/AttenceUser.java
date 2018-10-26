package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 考勤人员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class AttenceUser {
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
	* 人员编号
	*/
	@Filed
	private String enrollNumber;
	/** 
	* 名称
	*/
	@Filed
	private String name;
	/** 
	* 3管理员/0普通用户 
	*/
	@Filed
	private Integer privilege;

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
	* 人员编号
	* @param enrollNumber
	*/
	public void setEnrollNumber(String enrollNumber) {
		this.enrollNumber = enrollNumber;
	}

	/** 
	* 人员编号
	* @return
	*/
	public String getEnrollNumber() {
		return enrollNumber;
	}

	/** 
	* 名称
	* @param name
	*/
	public void setName(String name) {
		this.name = name;
	}

	/** 
	* 名称
	* @return
	*/
	public String getName() {
		return name;
	}

	/** 
	* 3管理员/0普通用户 
	* @param privilege
	*/
	public void setPrivilege(Integer privilege) {
		this.privilege = privilege;
	}

	/** 
	* 3管理员/0普通用户 
	* @return
	*/
	public Integer getPrivilege() {
		return privilege;
	}
}
