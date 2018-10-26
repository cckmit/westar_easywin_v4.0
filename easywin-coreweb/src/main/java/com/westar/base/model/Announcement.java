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
 * 公告表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Announcement {
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
	* 标题
	*/
	@Filed
	private String title;
	/** 
	* 公告类型  1通知/2通报/3决定/4其他/0无类型
	*/
	@Filed
	private String type;
	/** 
	* 重要级别 见字典grade
	*/
	@Filed
	private String grade;
	/** 
	* 公告创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 内容详情
	*/
	@Filed
	private String announRemark;
	/** 
	* 企业编号
	*/
	@Filed
	private Integer comId;
	/** 
	* 删除标识 0未删除，1预删除
	*/
	@Filed
	private Integer delState;
	/** 
	* 公告范围指定 0未指定（本部门全部人员），1指定
	*/
	@Filed
	private Integer scopeState;
	/** 
	* 查看次数
	*/
	@Filed
	private Integer readTime;

	/****************以上主要为系统表字段********************/
	private String startDate;
	private String endDate;
	/** 
	* 0过期/1通告中
	*/
	private String enabled;
	/** 
	* 创建人
	*/
	public String creatorName;
	/** 
	* 创建人性别
	*/
	public String creatorGender;
	/** 
	* 创建人UUID
	*/
	public String creatorUUID;
	/** 
	* UUID名
	*/
	public String creatorFileName;
	/** 
	* 关注标识
	*/
	public String attentionState;
	/** 
	* 附件集合
	*/
	public List<Upfiles> listUpfiles;
	/** 
	* 是否已读
	*/
	private Integer isRead;
	/** 
	* 排序
	*/
	private String orderBy;
	/** 
	* 公告范围
	*/
	public List<Integer> userIds;
	public List<Integer> depIds;
	/** 
	* 人员范围
	*/
	public List<AnnounScopeByUser> listScopeUser;
	/** 
	* 部门范围
	*/
	public List<AnnounScopeByDep> listScopeDep;
	/** 
	* 公告附件集合
	*/
	public List<AnnounUpfile> listAnnounFiles;
	/** 
	* 单位名称
	*/
	private String orgName;
	private List<UserInfo> listCreator;

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
	* 标题
	* @param title
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	* 标题
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/** 
	* 公告类型  1通知/2通报/3决定/4其他/0无类型
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 公告类型  1通知/2通报/3决定/4其他/0无类型
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 重要级别 见字典grade
	* @param grade
	*/
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/** 
	* 重要级别 见字典grade
	* @return
	*/
	public String getGrade() {
		return grade;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}

	/** 
	* 公告创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 公告创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 内容详情
	* @param announRemark
	*/
	public void setAnnounRemark(String announRemark) {
		this.announRemark = announRemark;
	}

	/** 
	* 内容详情
	* @return
	*/
	public String getAnnounRemark() {
		return announRemark;
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
	* 删除标识 0未删除，1预删除
	* @param delState
	*/
	public void setDelState(Integer delState) {
		this.delState = delState;
	}

	/** 
	* 删除标识 0未删除，1预删除
	* @return
	*/
	public Integer getDelState() {
		return delState;
	}

	/** 
	* 创建人
	* @return
	*/
	public String getCreatorName() {
		return creatorName;
	}

	/** 
	* 创建人
	* @param creatorName
	*/
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/** 
	* 创建人性别
	* @return
	*/
	public String getCreatorGender() {
		return creatorGender;
	}

	/** 
	* 创建人性别
	* @param creatorGender
	*/
	public void setCreatorGender(String creatorGender) {
		this.creatorGender = creatorGender;
	}

	/** 
	* 创建人UUID
	* @return
	*/
	public String getCreatorUUID() {
		return creatorUUID;
	}

	/** 
	* 创建人UUID
	* @param creatorUUID
	*/
	public void setCreatorUUID(String creatorUUID) {
		this.creatorUUID = creatorUUID;
	}

	/** 
	* UUID名
	* @return
	*/
	public String getCreatorFileName() {
		return creatorFileName;
	}

	/** 
	* UUID名
	* @param creatorFileName
	*/
	public void setCreatorFileName(String creatorFileName) {
		this.creatorFileName = creatorFileName;
	}

	/** 
	* 关注标识
	* @return
	*/
	public String getAttentionState() {
		return attentionState;
	}

	/** 
	* 关注标识
	* @param attentionState
	*/
	public void setAttentionState(String attentionState) {
		this.attentionState = attentionState;
	}

	/** 
	* 附件集合
	* @return
	*/
	public List<Upfiles> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 附件集合
	* @param listUpfiles
	*/
	public void setListUpfiles(List<Upfiles> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}

	/** 
	* 0过期/1通告中
	* @return
	*/
	public String getEnabled() {
		return enabled;
	}

	/** 
	* 0过期/1通告中
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** 
	* 是否已读
	* @return
	*/
	public Integer getIsRead() {
		return isRead;
	}

	/** 
	* 是否已读
	* @param isRead
	*/
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	/** 
	* 排序
	* @return
	*/
	public String getOrderBy() {
		return orderBy;
	}

	/** 
	* 排序
	* @param orderBy
	*/
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/** 
	* 公告范围
	* @return
	*/
	public List<Integer> getUserIds() {
		return userIds;
	}

	/** 
	* 公告范围
	* @param userIds
	*/
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public List<Integer> getDepIds() {
		return depIds;
	}

	public void setDepIds(List<Integer> depIds) {
		this.depIds = depIds;
	}

	/** 
	* 人员范围
	* @return
	*/
	public List<AnnounScopeByUser> getListScopeUser() {
		return listScopeUser;
	}

	/** 
	* 人员范围
	* @param listScopeUser
	*/
	public void setListScopeUser(List<AnnounScopeByUser> listScopeUser) {
		this.listScopeUser = listScopeUser;
	}

	/** 
	* 部门范围
	* @return
	*/
	public List<AnnounScopeByDep> getListScopeDep() {
		return listScopeDep;
	}

	/** 
	* 部门范围
	* @param listScopeDep
	*/
	public void setListScopeDep(List<AnnounScopeByDep> listScopeDep) {
		this.listScopeDep = listScopeDep;
	}

	/** 
	* 公告范围指定 0未指定（本部门全部人员），1指定
	* @param scopeState
	*/
	public void setScopeState(Integer scopeState) {
		this.scopeState = scopeState;
	}

	/** 
	* 公告范围指定 0未指定（本部门全部人员），1指定
	* @return
	*/
	public Integer getScopeState() {
		return scopeState;
	}

	/** 
	* 查看次数
	* @param readTime
	*/
	public void setReadTime(Integer readTime) {
		this.readTime = readTime;
	}

	/** 
	* 查看次数
	* @return
	*/
	public Integer getReadTime() {
		return readTime;
	}

	/** 
	* 公告附件集合
	* @return
	*/
	public List<AnnounUpfile> getListAnnounFiles() {
		return listAnnounFiles;
	}

	/** 
	* 公告附件集合
	* @param listAnnounFiles
	*/
	public void setListAnnounFiles(List<AnnounUpfile> listAnnounFiles) {
		this.listAnnounFiles = listAnnounFiles;
	}

	/** 
	* 单位名称
	* @return
	*/
	public String getOrgName() {
		return orgName;
	}

	/** 
	* 单位名称
	* @param orgName
	*/
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<UserInfo> getListCreator() {
		return listCreator;
	}

	public void setListCreator(List<UserInfo> listCreator) {
		this.listCreator = listCreator;
	}
}
