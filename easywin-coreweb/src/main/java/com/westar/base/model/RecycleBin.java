package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 回收箱
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RecycleBin {
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
	* 删除人主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 模块类型 见系统常量
	*/
	@Filed
	private String busType;
	/** 
	* 模块主键
	*/
	@Filed
	private Integer busId;

	/****************以上主要为系统表字段********************/
	/** 
	* 业务名称
	*/
	private String busTypeName;
	/** 
	* 模块名称
	*/
	private String moduleTypeName;
	/** 
	* 合计
	*/
	private int sum;
	/** 
	* 查询条件描述
	*/
	private String content;
	/** 
	* 查询的时间起
	*/
	private String startDate;
	/** 
	* 查询的时间止
	*/
	private String endDate;

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
	* 删除人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 删除人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 业务名称
	* @return
	*/
	public String getBusTypeName() {
		return busTypeName;
	}

	/** 
	* 业务名称
	* @param busTypeName
	*/
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}

	/** 
	* 模块名称
	* @return
	*/
	public String getModuleTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("moduleType", busType);
	}

	/** 
	* 模块名称
	* @param moduleTypeName
	*/
	public void setModuleTypeName(String moduleTypeName) {
		this.moduleTypeName = moduleTypeName;
	}

	/** 
	* 合计
	* @return
	*/
	public int getSum() {
		return sum;
	}

	/** 
	* 合计
	* @param sum
	*/
	public void setSum(int sum) {
		this.sum = sum;
	}

	/** 
	* 查询的时间起
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的时间起
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的时间止
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的时间止
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 模块类型 见系统常量
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块类型 见系统常量
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 模块主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 模块主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 查询条件描述
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 查询条件描述
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}
}
