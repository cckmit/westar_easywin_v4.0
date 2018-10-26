package com.westar.base.model;

import java.io.Serializable;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 查看记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ViewRecord implements Serializable {
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
	* 浏览人
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
	* 图片信息
	*/
	private String imgUuid;
	private String imgName;
	/** 
	* 性别
	*/
	private String gender;
	/** 
	* 查看人姓名
	*/
	private String userName;

	/****************以上为自己添加字段********************/
	public ViewRecord() {
	}

	public ViewRecord(Integer comId, Integer userId, String busType, Integer busId) {
		super();
		this.comId = comId;
		this.userId = userId;
		this.busType = busType;
		this.busId = busId;
	}

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
	* 浏览人
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 浏览人
	* @return
	*/
	public Integer getUserId() {
		return userId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((busId == null) ? 0 : busId.hashCode());
		result = prime * result + ((busType == null) ? 0 : busType.hashCode());
		result = prime * result + ((comId == null) ? 0 : comId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ViewRecord other = (ViewRecord) obj;
		if (busId == null) {
			if (other.busId != null) {
				return false;
			}
		} else if (!busId.equals(other.busId)) {
			return false;
		}
		if (busType == null) {
			if (other.busType != null) {
				return false;
			}
		} else if (!busType.equals(other.busType)) {
			return false;
		}
		if (comId == null) {
			if (other.comId != null) {
				return false;
			}
		} else if (!comId.equals(other.comId)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	/** 
	* 图片信息
	* @return
	*/
	public String getImgUuid() {
		return imgUuid;
	}

	/** 
	* 图片信息
	* @param imgUuid
	*/
	public void setImgUuid(String imgUuid) {
		this.imgUuid = imgUuid;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	/** 
	* 性别
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 性别
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 查看人姓名
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 查看人姓名
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
