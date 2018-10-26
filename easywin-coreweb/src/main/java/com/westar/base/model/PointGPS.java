package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * GPS位置
 */
@Table
@JsonInclude(Include.NON_NULL)
public class PointGPS {
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
	* 用户主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 业务主键
	*/
	@Filed
	private Integer busId;
	/** 
	* 纬度
	*/
	@Filed
	private String pointX;
	/** 
	* 经度
	*/
	@Filed
	private String pointY;
	/** 
	* 地点名称
	*/
	@Filed
	private String pointName;

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
	* 用户主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 用户主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
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
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 业务类型，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 业务主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 业务主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 纬度
	* @param pointX
	*/
	public void setPointX(String pointX) {
		this.pointX = pointX;
	}

	/** 
	* 纬度
	* @return
	*/
	public String getPointX() {
		return pointX;
	}

	/** 
	* 经度
	* @param pointY
	*/
	public void setPointY(String pointY) {
		this.pointY = pointY;
	}

	/** 
	* 经度
	* @return
	*/
	public String getPointY() {
		return pointY;
	}

	/** 
	* 地点名称
	* @param pointName
	*/
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	/** 
	* 地点名称
	* @return
	*/
	public String getPointName() {
		return pointName;
	}
}
