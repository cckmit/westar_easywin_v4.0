package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 分享模板内容所属成员
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DailyModContMember {
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
	* 模板编号
	*/
	@Filed
	private Integer modId;
	/** 
	* 模板内容主键
	*/
	@Filed
	private Integer modContId;
	/** 
	* 成员的主键 关联用户
	*/
	@Filed
	private Integer memberId;
	/** 
	* 是否必填，默认不是
	*/
	@Filed
	private String isRequire;

	/****************以上主要为系统表字段********************/
	/** 
	* 成员名称
	*/
	private String memberName;
	/** 
	* 成员性别
	*/
	private String gender;
	/** 
	* 成员头像
	*/
	private String imgUuid;
	/** 
	* 成员头像名称
	*/
	private String imgName;

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
	* 模板编号
	* @param modId
	*/
	public void setModId(Integer modId) {
		this.modId = modId;
	}

	/** 
	* 模板编号
	* @return
	*/
	public Integer getModId() {
		return modId;
	}

	/** 
	* 模板内容主键
	* @param modContId
	*/
	public void setModContId(Integer modContId) {
		this.modContId = modContId;
	}

	/** 
	* 模板内容主键
	* @return
	*/
	public Integer getModContId() {
		return modContId;
	}

	/** 
	* 成员的主键 关联用户
	* @param memberId
	*/
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/** 
	* 成员的主键 关联用户
	* @return
	*/
	public Integer getMemberId() {
		return memberId;
	}

	/** 
	* 是否必填，默认不是
	* @param isRequire
	*/
	public void setIsRequire(String isRequire) {
		this.isRequire = isRequire;
	}

	/** 
	* 是否必填，默认不是
	* @return
	*/
	public String getIsRequire() {
		return isRequire;
	}

	/** 
	* 成员名称
	* @return
	*/
	public String getMemberName() {
		return memberName;
	}

	/** 
	* 成员名称
	* @param memberName
	*/
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	/** 
	* 成员性别
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 成员性别
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 成员头像
	* @return
	*/
	public String getImgUuid() {
		return imgUuid;
	}

	/** 
	* 成员头像
	* @param imgUuid
	*/
	public void setImgUuid(String imgUuid) {
		this.imgUuid = imgUuid;
	}

	/** 
	* 成员头像名称
	* @return
	*/
	public String getImgName() {
		return imgName;
	}

	/** 
	* 成员头像名称
	* @param imgName
	*/
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
}
