package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 角色表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Role {
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
	* 角色名称
	*/
	@Filed
	private String roleName;
	/** 
	* 角色描述
	*/
	@Filed
	private String roleRemark;
	/** 
	* 是否启用：0未启用；1已启用
	*/
	@Filed
	private Integer enable;
	/** 
	* 角色创建人
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* 属于该角色的人员集合
	*/
	private List<RoleBindingUser> roleBindingUsers;

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
	* 角色名称
	* @param roleName
	*/
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/** 
	* 角色名称
	* @return
	*/
	public String getRoleName() {
		return roleName;
	}

	/** 
	* 角色描述
	* @param roleRemark
	*/
	public void setRoleRemark(String roleRemark) {
		this.roleRemark = roleRemark;
	}

	/** 
	* 角色描述
	* @return
	*/
	public String getRoleRemark() {
		return roleRemark;
	}

	/** 
	* 是否启用：0未启用；1已启用
	* @param enable
	*/
	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	/** 
	* 是否启用：0未启用；1已启用
	* @return
	*/
	public Integer getEnable() {
		return enable;
	}

	/** 
	* 角色创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 角色创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 属于该角色的人员
	* @return
	*/
	public List<RoleBindingUser> getBindingUsers() {
		return roleBindingUsers;
	}

	public void setBindingUsers(List<RoleBindingUser> roleBindingUsers) {
		this.roleBindingUsers = roleBindingUsers;
	}
}
