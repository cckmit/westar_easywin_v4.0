package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 人事档案其他
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RsdaOther {
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
	* 档案人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 行政级别
	*/
	@Filed
	private String adminLevel;
	/** 
	* 员工类型
	*/
	@Filed
	private String employeeType;
	/** 
	* 工号
	*/
	@Filed
	private String jobNumber;
	/** 
	* 职务名称
	*/
	@Filed
	private String dutyName;
	/** 
	* 在职状态
	*/
	@Filed
	private String payrollType;
	/** 
	* 岗位
	*/
	@Filed
	private String gwName;
	/** 
	* 职称
	*/
	@Filed
	private String zcName;
	/** 
	* 紧急联系人
	*/
	@Filed
	private String urgenterName;
	/** 
	* 紧急联系电话
	*/
	@Filed
	private String urgenterTel;

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
	* 档案人员主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 档案人员主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 行政级别
	* @param adminLevel
	*/
	public void setAdminLevel(String adminLevel) {
		this.adminLevel = adminLevel;
	}

	/** 
	* 行政级别
	* @return
	*/
	public String getAdminLevel() {
		return adminLevel;
	}

	/** 
	* 员工类型
	* @param employeeType
	*/
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	/** 
	* 员工类型
	* @return
	*/
	public String getEmployeeType() {
		return employeeType;
	}

	/** 
	* 工号
	* @param jobNumber
	*/
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	/** 
	* 工号
	* @return
	*/
	public String getJobNumber() {
		return jobNumber;
	}

	/** 
	* 职务名称
	* @param dutyName
	*/
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}

	/** 
	* 职务名称
	* @return
	*/
	public String getDutyName() {
		return dutyName;
	}

	/** 
	* 在职状态
	* @param payrollType
	*/
	public void setPayrollType(String payrollType) {
		this.payrollType = payrollType;
	}

	/** 
	* 在职状态
	* @return
	*/
	public String getPayrollType() {
		return payrollType;
	}

	/** 
	* 岗位
	* @param gwName
	*/
	public void setGwName(String gwName) {
		this.gwName = gwName;
	}

	/** 
	* 岗位
	* @return
	*/
	public String getGwName() {
		return gwName;
	}

	/** 
	* 职称
	* @param zcName
	*/
	public void setZcName(String zcName) {
		this.zcName = zcName;
	}

	/** 
	* 职称
	* @return
	*/
	public String getZcName() {
		return zcName;
	}

	/** 
	* 紧急联系人
	* @param urgenterName
	*/
	public void setUrgenterName(String urgenterName) {
		this.urgenterName = urgenterName;
	}

	/** 
	* 紧急联系人
	* @return
	*/
	public String getUrgenterName() {
		return urgenterName;
	}

	/** 
	* 紧急联系电话
	* @param urgenterTel
	*/
	public void setUrgenterTel(String urgenterTel) {
		this.urgenterTel = urgenterTel;
	}

	/** 
	* 紧急联系电话
	* @return
	*/
	public String getUrgenterTel() {
		return urgenterTel;
	}
}
