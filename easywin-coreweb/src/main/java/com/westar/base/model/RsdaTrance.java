package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.UserHeadImg;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 人事调动
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RsdaTrance extends UserHeadImg {
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
	* 调动人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 调动日期
	*/
	@Filed
	private String tranceDate;
	/** 
	* 调动类型
	*/
	@Filed
	private String tranceType;
	/** 
	* 调动前单位
	*/
	@Filed
	private String trancePreOrg;
	/** 
	* 调动后单位
	*/
	@Filed
	private String tranceAftOrg;
	/** 
	* 调动说明
	*/
	@Filed
	private String remark;
	/** 
	* 调动前部门
	*/
	@Filed
	private Integer trancePreDepId;
	/** 
	* 调动后部门
	*/
	@Filed
	private Integer tranceAftDepId;

	/****************以上主要为系统表字段********************/
	/** 
	* 人事调动附件
	*/
	private List<RsdaTranceFile> listTranceFiles;
	private String tranceTypeName;
	private String trancePreDepName;
	private String tranceAftDepName;
	/** 
	* 查询的开始时间
	*/
	private String startDate;
	/** 
	* 查询的截止时间
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
	* 调动人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 调动人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 调动日期
	* @param tranceDate
	*/
	public void setTranceDate(String tranceDate) {
		this.tranceDate = tranceDate;
	}

	/** 
	* 调动日期
	* @return
	*/
	public String getTranceDate() {
		return tranceDate;
	}

	/** 
	* 调动类型
	* @param tranceType
	*/
	public void setTranceType(String tranceType) {
		this.tranceType = tranceType;
	}

	/** 
	* 调动类型
	* @return
	*/
	public String getTranceType() {
		return tranceType;
	}

	/** 
	* 调动前单位
	* @param trancePreOrg
	*/
	public void setTrancePreOrg(String trancePreOrg) {
		this.trancePreOrg = trancePreOrg;
	}

	/** 
	* 调动前单位
	* @return
	*/
	public String getTrancePreOrg() {
		return trancePreOrg;
	}

	/** 
	* 调动后单位
	* @param tranceAftOrg
	*/
	public void setTranceAftOrg(String tranceAftOrg) {
		this.tranceAftOrg = tranceAftOrg;
	}

	/** 
	* 调动后单位
	* @return
	*/
	public String getTranceAftOrg() {
		return tranceAftOrg;
	}

	public void setTrancePreDepName(String trancePreDepName) {
		this.trancePreDepName = trancePreDepName;
	}

	public String getTrancePreDepName() {
		return trancePreDepName;
	}

	public void setTranceAftDepName(String tranceAftDepName) {
		this.tranceAftDepName = tranceAftDepName;
	}

	public String getTranceAftDepName() {
		return tranceAftDepName;
	}

	/** 
	* 调动说明
	* @param remark
	*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	* 调动说明
	* @return
	*/
	public String getRemark() {
		return remark;
	}

	/** 
	* 人事调动附件
	* @return
	*/
	public List<RsdaTranceFile> getListTranceFiles() {
		return listTranceFiles;
	}

	/** 
	* 人事调动附件
	* @param listTranceFiles
	*/
	public void setListTranceFiles(List<RsdaTranceFile> listTranceFiles) {
		this.listTranceFiles = listTranceFiles;
	}

	/** 
	* 查询的开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 查询的开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 查询的截止时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的截止时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 调动前部门
	* @param trancePreDepId
	*/
	public void setTrancePreDepId(Integer trancePreDepId) {
		this.trancePreDepId = trancePreDepId;
	}

	/** 
	* 调动前部门
	* @return
	*/
	public Integer getTrancePreDepId() {
		return trancePreDepId;
	}

	/** 
	* 调动后部门
	* @param tranceAftDepId
	*/
	public void setTranceAftDepId(Integer tranceAftDepId) {
		this.tranceAftDepId = tranceAftDepId;
	}

	/** 
	* 调动后部门
	* @return
	*/
	public Integer getTranceAftDepId() {
		return tranceAftDepId;
	}

	public String getTranceTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("tranceType", tranceType);
	}

	public void setTranceTypeName(String tranceTypeName) {
		this.tranceTypeName = tranceTypeName;
	}
}
