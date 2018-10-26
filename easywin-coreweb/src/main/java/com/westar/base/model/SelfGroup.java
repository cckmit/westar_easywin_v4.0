package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 个人分组表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class SelfGroup {
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
	* 组名称
	*/
	@Filed
	private String grpName;
	/** 
	* 拼音 全拼
	*/
	@Filed
	private String allSpelling;
	/** 
	* 拼音 首字母
	*/
	@Filed
	private String firstSpelling;
	/** 
	* 组所有者
	*/
	@Filed
	private Integer owner;
	/** 
	* 分组排序号
	*/
	@Filed
	private Integer orderNo;

	/****************以上主要为系统表字段********************/
	/** 
	* 分组成员
	*/
	private List<UserInfo> listUser;
	/** 
	* 分组ID转成ID字符串
	*/
	private String grpIdStr;
	/** 
	* 项目组成员里是否与项目关联标识；0，未关联，1关联
	*/
	private int checked;
	private String groupType;

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
	* 组名称
	* @param grpName
	*/
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	/** 
	* 组名称
	* @return
	*/
	public String getGrpName() {
		return grpName;
	}

	/** 
	* 拼音 全拼
	* @param allSpelling
	*/
	public void setAllSpelling(String allSpelling) {
		this.allSpelling = allSpelling;
	}

	/** 
	* 拼音 全拼
	* @return
	*/
	public String getAllSpelling() {
		return allSpelling;
	}

	/** 
	* 拼音 首字母
	* @param firstSpelling
	*/
	public void setFirstSpelling(String firstSpelling) {
		this.firstSpelling = firstSpelling;
	}

	/** 
	* 拼音 首字母
	* @return
	*/
	public String getFirstSpelling() {
		return firstSpelling;
	}

	/** 
	* 组所有者
	* @param owner
	*/
	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	/** 
	* 组所有者
	* @return
	*/
	public Integer getOwner() {
		return owner;
	}

	/** 
	* 分组成员
	* @return
	*/
	public List<UserInfo> getListUser() {
		return listUser;
	}

	/** 
	* 分组成员
	* @param listUser
	*/
	public void setListUser(List<UserInfo> listUser) {
		this.listUser = listUser;
	}

	/** 
	* 分组排序号
	* @return
	*/
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 
	* 分组排序号
	* @param orderNo
	*/
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/** 
	* 分组ID转成ID字符串
	* @return
	*/
	public String getGrpIdStr() {
		return grpIdStr;
	}

	/** 
	* 分组ID转成ID字符串
	* @param grpIdStr
	*/
	public void setGrpIdStr(String grpIdStr) {
		this.grpIdStr = grpIdStr;
	}

	/** 
	* 项目组成员里是否与项目关联标识；0，未关联，1关联
	* @return
	*/
	public int getChecked() {
		return checked;
	}

	/** 
	* 项目组成员里是否与项目关联标识；0，未关联，1关联
	* @param checked
	*/
	public void setChecked(int checked) {
		this.checked = checked;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
}
