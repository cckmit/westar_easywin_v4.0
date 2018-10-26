package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.core.web.DataDicContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 人事档案基本信息
 */
@Table
@JsonInclude(Include.NON_NULL)
public class RsdaBase {
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
	* 档案人员主键
	*/
	@Filed
	private Integer userId;
	/** 
	* 籍贯省
	*/
	@Filed
	private String nativePro;
	/** 
	* 籍贯市
	*/
	@Filed
	private String nativeCity;
	/** 
	* 婚姻状况
	*/
	@Filed
	private String marryStatus;
	/** 
	* 政治面貌
	*/
	@Filed
	private String politStatus;
	/** 
	* 家庭住址
	*/
	@Filed
	private String homeAdress;
	/** 
	* 入党时间
	*/
	@Filed
	private String politDate;
	/** 
	* 学历
	*/
	@Filed
	private String eduFormal;
	/** 
	* 学位
	*/
	@Filed
	private String degree;
	/** 
	* 毕业时间
	*/
	@Filed
	private String graduateDate;
	/** 
	* 毕业学校
	*/
	@Filed
	private String schoolName;
	/** 
	* 专业
	*/
	@Filed
	private String major;
	/** 
	* 备注
	*/
	@Filed
	private String remark;
	/** 
	* 族别
	*/
	@Filed
	private String nation;
	/** 
	* 身份证号
	*/
	@Filed
	private String idCardNum;
	/** 
	* 特长
	*/
	@Filed
	private String special;

	/****************以上主要为系统表字段********************/
	/** 
	* 人员名称
	*/
	private String userName;
	/** 
	* 部门主键
	*/
	private Integer depId;
	private String depName;
	/** 
	* 头像
	*/
	private String userImgUuid;
	private String userImgName;
	/** 
	* 邮件
	*/
	private String email;
	/** 
	* 族别名称
	*/
	private String nationName;
	/** 
	* 族籍贯省名称
	*/
	private String nativeProName;
	/** 
	* 婚姻状况的名称
	*/
	private String marryStatusName;
	/** 
	* 政治面貌名称
	*/
	private String politStatusName;
	/** 
	* 学历名称
	*/
	private String eduFormalName;
	/** 
	* 学位名称
	*/
	private String degreeName;
	/** 
	* 员工类型
	*/
	private String employeeType;
	/** 
	* 员工类型名称
	*/
	private String employeeTypeName;
	/** 
	* 在职状态名称
	*/
	private String payrollTypeName;
	/** 
	* 工号
	*/
	private String jobNumber;
	/** 
	* OA用户名
	*/
	private String nickName;
	/** 
	* 性别
	*/
	private String gender;
	/** 
	* 手机
	*/
	private String userTel;
	/** 
	* 人事档案其他的主键
	*/
	private Integer rsdaOtherId;
	private String dutyName;
	private String payrollType;
	private String gwName;
	private String zcName;
	private String urgenterName;
	private String urgenterTel;
	/** 
	* 学历基本附件
	*/
	private List<RsdaBaseFile> listRsdaBaseFiles;
	/** 
	* 人事档案其他信息
	*/
	private RsdaOther rsdaOther;
	/** 
	* 入职时间
	*/
	private String hireDate;
	/** 
	* 是否启用
	*/
	private String enabled;
	/** 
	* 查询的开始时间
	*/
	private String startDate;
	/** 
	* 查询的结束时间
	*/
	private String endDate;
	/** 
	* 模糊查询的内容
	*/
	private String searchContent;

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
	* 籍贯省
	* @param nativePro
	*/
	public void setNativePro(String nativePro) {
		this.nativePro = nativePro;
	}

	/** 
	* 籍贯省
	* @return
	*/
	public String getNativePro() {
		return nativePro;
	}

	/** 
	* 籍贯市
	* @param nativeCity
	*/
	public void setNativeCity(String nativeCity) {
		this.nativeCity = nativeCity;
	}

	/** 
	* 籍贯市
	* @return
	*/
	public String getNativeCity() {
		return nativeCity;
	}

	/** 
	* 婚姻状况
	* @param marryStatus
	*/
	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}

	/** 
	* 婚姻状况
	* @return
	*/
	public String getMarryStatus() {
		return marryStatus;
	}

	/** 
	* 政治面貌
	* @param politStatus
	*/
	public void setPolitStatus(String politStatus) {
		this.politStatus = politStatus;
	}

	/** 
	* 政治面貌
	* @return
	*/
	public String getPolitStatus() {
		return politStatus;
	}

	/** 
	* 家庭住址
	* @param homeAdress
	*/
	public void setHomeAdress(String homeAdress) {
		this.homeAdress = homeAdress;
	}

	/** 
	* 家庭住址
	* @return
	*/
	public String getHomeAdress() {
		return homeAdress;
	}

	/** 
	* 入党时间
	* @param politDate
	*/
	public void setPolitDate(String politDate) {
		this.politDate = politDate;
	}

	/** 
	* 入党时间
	* @return
	*/
	public String getPolitDate() {
		return politDate;
	}

	/** 
	* 学历
	* @param eduFormal
	*/
	public void setEduFormal(String eduFormal) {
		this.eduFormal = eduFormal;
	}

	/** 
	* 学历
	* @return
	*/
	public String getEduFormal() {
		return eduFormal;
	}

	/** 
	* 学位
	* @param degree
	*/
	public void setDegree(String degree) {
		this.degree = degree;
	}

	/** 
	* 学位
	* @return
	*/
	public String getDegree() {
		return degree;
	}

	/** 
	* 毕业时间
	* @param graduateDate
	*/
	public void setGraduateDate(String graduateDate) {
		this.graduateDate = graduateDate;
	}

	/** 
	* 毕业时间
	* @return
	*/
	public String getGraduateDate() {
		return graduateDate;
	}

	/** 
	* 毕业学校
	* @param schoolName
	*/
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	/** 
	* 毕业学校
	* @return
	*/
	public String getSchoolName() {
		return schoolName;
	}

	/** 
	* 专业
	* @param major
	*/
	public void setMajor(String major) {
		this.major = major;
	}

	/** 
	* 专业
	* @return
	*/
	public String getMajor() {
		return major;
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
	* 学历基本附件
	* @return
	*/
	public List<RsdaBaseFile> getListRsdaBaseFiles() {
		return listRsdaBaseFiles;
	}

	/** 
	* 学历基本附件
	* @param listRsdaBaseFiles
	*/
	public void setListRsdaBaseFiles(List<RsdaBaseFile> listRsdaBaseFiles) {
		this.listRsdaBaseFiles = listRsdaBaseFiles;
	}

	/** 
	* 人员名称
	* @return
	*/
	public String getUserName() {
		return userName;
	}

	/** 
	* 人员名称
	* @param userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	* 部门主键
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 部门主键
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 头像
	* @return
	*/
	public String getUserImgUuid() {
		return userImgUuid;
	}

	/** 
	* 头像
	* @param userImgUuid
	*/
	public void setUserImgUuid(String userImgUuid) {
		this.userImgUuid = userImgUuid;
	}

	public String getUserImgName() {
		return userImgName;
	}

	public void setUserImgName(String userImgName) {
		this.userImgName = userImgName;
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
	* 手机
	* @return
	*/
	public String getUserTel() {
		return userTel;
	}

	/** 
	* 手机
	* @param userTel
	*/
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	/** 
	* 入职时间
	* @return
	*/
	public String getHireDate() {
		return hireDate;
	}

	/** 
	* 入职时间
	* @param hireDate
	*/
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	/** 
	* 是否启用
	* @return
	*/
	public String getEnabled() {
		return enabled;
	}

	/** 
	* 是否启用
	* @param enabled
	*/
	public void setEnabled(String enabled) {
		this.enabled = enabled;
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
	* 查询的结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 查询的结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 政治面貌名称
	* @return
	*/
	public String getPolitStatusName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("politStatus", politStatus);
	}

	/** 
	* 政治面貌名称
	* @param politStatusName
	*/
	public void setPolitStatusName(String politStatusName) {
		this.politStatusName = politStatusName;
	}

	/** 
	* OA用户名
	* @return
	*/
	public String getNickName() {
		return nickName;
	}

	/** 
	* OA用户名
	* @param nickName
	*/
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/** 
	* 人事档案其他信息
	* @return
	*/
	public RsdaOther getRsdaOther() {
		return rsdaOther;
	}

	/** 
	* 人事档案其他信息
	* @param rsdaOther
	*/
	public void setRsdaOther(RsdaOther rsdaOther) {
		this.rsdaOther = rsdaOther;
	}

	/** 
	* 族别
	* @param nation
	*/
	public void setNation(String nation) {
		this.nation = nation;
	}

	/** 
	* 族别
	* @return
	*/
	public String getNation() {
		return nation;
	}

	/** 
	* 身份证号
	* @param idCardNum
	*/
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	/** 
	* 身份证号
	* @return
	*/
	public String getIdCardNum() {
		return idCardNum;
	}

	/** 
	* 特长
	* @param special
	*/
	public void setSpecial(String special) {
		this.special = special;
	}

	/** 
	* 特长
	* @return
	*/
	public String getSpecial() {
		return special;
	}

	/** 
	* 族别名称
	* @return
	*/
	public String getNationName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("nation", nation);
	}

	/** 
	* 族别名称
	* @param nationName
	*/
	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	/** 
	* 族籍贯省名称
	* @return
	*/
	public String getNativeProName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("nativePro", nativePro);
	}

	/** 
	* 族籍贯省名称
	* @param nativeProName
	*/
	public void setNativeProName(String nativeProName) {
		this.nativeProName = nativeProName;
	}

	/** 
	* 婚姻状况的名称
	* @return
	*/
	public String getMarryStatusName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("marryStatus", marryStatus);
	}

	/** 
	* 婚姻状况的名称
	* @param marryStatusName
	*/
	public void setMarryStatusName(String marryStatusName) {
		this.marryStatusName = marryStatusName;
	}

	/** 
	* 学历名称
	* @return
	*/
	public String getEduFormalName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("eduFormalType", eduFormal);
	}

	/** 
	* 学历名称
	* @param eduFormalName
	*/
	public void setEduFormalName(String eduFormalName) {
		this.eduFormalName = eduFormalName;
	}

	/** 
	* 学位名称
	* @return
	*/
	public String getDegreeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("degreeType", degree);
	}

	/** 
	* 学位名称
	* @param degreeName
	*/
	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	/** 
	* 员工类型名称
	* @return
	*/
	public String getEmployeeTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("employeeType", employeeType);
	}

	/** 
	* 员工类型名称
	* @param employeeTypeName
	*/
	public void setEmployeeTypeName(String employeeTypeName) {
		this.employeeTypeName = employeeTypeName;
	}

	/** 
	* 工号
	* @return
	*/
	public String getJobNumber() {
		return jobNumber;
	}

	/** 
	* 工号
	* @param jobNumber
	*/
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	/** 
	* 员工类型
	* @return
	*/
	public String getEmployeeType() {
		return employeeType;
	}

	/** 
	* 员工类型
	* @param employeeType
	*/
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getDutyName() {
		return dutyName;
	}

	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}

	public String getPayrollType() {
		return payrollType;
	}

	public void setPayrollType(String payrollType) {
		this.payrollType = payrollType;
	}

	public String getGwName() {
		return gwName;
	}

	public void setGwName(String gwName) {
		this.gwName = gwName;
	}

	public String getZcName() {
		return zcName;
	}

	public void setZcName(String zcName) {
		this.zcName = zcName;
	}

	public String getUrgenterName() {
		return urgenterName;
	}

	public void setUrgenterName(String urgenterName) {
		this.urgenterName = urgenterName;
	}

	public String getUrgenterTel() {
		return urgenterTel;
	}

	public void setUrgenterTel(String urgenterTel) {
		this.urgenterTel = urgenterTel;
	}

	/** 
	* 人事档案其他的主键
	* @return
	*/
	public Integer getRsdaOtherId() {
		return rsdaOtherId;
	}

	/** 
	* 人事档案其他的主键
	* @param rsdaOtherId
	*/
	public void setRsdaOtherId(Integer rsdaOtherId) {
		this.rsdaOtherId = rsdaOtherId;
	}

	/** 
	* 邮件
	* @return
	*/
	public String getEmail() {
		return email;
	}

	/** 
	* 邮件
	* @param email
	*/
	public void setEmail(String email) {
		this.email = email;
	}

	/** 
	* 在职状态名称
	* @return
	*/
	public String getPayrollTypeName() {
		return DataDicContext.getInstance().getCurrentPathZvalue("payrollType", payrollType);
	}

	/** 
	* 在职状态名称
	* @param payrollTypeName
	*/
	public void setPayrollTypeName(String payrollTypeName) {
		this.payrollTypeName = payrollTypeName;
	}

	/** 
	* 模糊查询的内容
	* @return
	*/
	public String getSearchContent() {
		return searchContent;
	}

	/** 
	* 模糊查询的内容
	* @param searchContent
	*/
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
}
