package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 系统菜单
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Menu {
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
	* 菜单名称
	*/
	@Filed
	private String menuName;
	/** 
	* 菜单父节点ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 动作地址
	*/
	@Filed
	private String action;
	/** 
	* 菜单对应图片
	*/
	@Filed
	private String image;
	/** 
	* 菜单排序号
	*/
	@Filed
	private Integer orderNo;
	/** 
	* 启用状态  参见数据字典enabled
	*/
	@Filed
	private String enabled;
	/** 
	* 备注
	*/
	@Filed
	private String remark;
	/** 
	* 权限代码
	*/
	@Filed
	private String authCode;
	/** 
	* 菜单的target 参见数据字典menuTarget
	*/
	@Filed
	private String menuTarget;
	/** 
	* 访问权限  参见数据字典menuRole 0公用 1本机
	*/
	@Filed
	private String menuRole;

	/****************以上主要为系统表字段********************/
	/** 
	* 父级菜单名称
	*/
	private String parentName;
	/** 
	* 菜单层级
	*/
	private Integer menuLevel;
	/** 
	* 路径全称
	*/
	private String pathName;
	/** 
	* 菜单是否被选中（用于树形结构展示）
	*/
	private boolean checked;
	private List<Menu> children;

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
	* 菜单名称
	* @param menuName
	*/
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/** 
	* 菜单名称
	* @return
	*/
	public String getMenuName() {
		return menuName;
	}

	/** 
	* 菜单父节点ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 菜单父节点ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 动作地址
	* @param action
	*/
	public void setAction(String action) {
		this.action = action;
	}

	/** 
	* 动作地址
	* @return
	*/
	public String getAction() {
		return action;
	}

	/** 
	* 菜单对应图片
	* @param image
	*/
	public void setImage(String image) {
		this.image = image;
	}

	/** 
	* 菜单对应图片
	* @return
	*/
	public String getImage() {
		return image;
	}

	/** 
	* 菜单排序号
	* @param orderNo
	*/
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/** 
	* 菜单排序号
	* @return
	*/
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 
	* 启用状态  参见数据字典enabled
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** 
	* 启用状态  参见数据字典enabled
	* @return
	*/
	public String getEnabled() {
		return enabled;
	}

	/** 
	* 备注
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 备注
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 权限代码
	* @param authCode
	*/
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/** 
	* 权限代码
	* @return
	*/
	public String getAuthCode() {
		return authCode;
	}

	/** 
	* 父级菜单名称
	* @return
	*/
	public String getParentName() {
		return parentName;
	}

	/** 
	* 父级菜单名称
	* @param parentName
	*/
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/** 
	* 路径全称
	* @return
	*/
	public String getPathName() {
		return pathName;
	}

	/** 
	* 路径全称
	* @param pathName
	*/
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public boolean isChecked() {
		return checked;
	}

	/** 
	* 菜单是否被选中（用于树形结构展示）
	* @param checked
	*/
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	/** 
	* 菜单层级
	* @return
	*/
	public Integer getMenuLevel() {
		return menuLevel;
	}

	/** 
	* 菜单层级
	* @param menuLevel
	*/
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	/** 
	* 菜单的target 参见数据字典menuTarget
	* @param menuTarget
	*/
	public void setMenuTarget(String menuTarget) {
		this.menuTarget = menuTarget;
	}

	/** 
	* 菜单的target 参见数据字典menuTarget
	* @return
	*/
	public String getMenuTarget() {
		return menuTarget;
	}

	/** 
	* 访问权限  参见数据字典menuRole 0公用 1本机
	* @param menuRole
	*/
	public void setMenuRole(String menuRole) {
		this.menuRole = menuRole;
	}

	/** 
	* 访问权限  参见数据字典menuRole 0公用 1本机
	* @return
	*/
	public String getMenuRole() {
		return menuRole;
	}
}
