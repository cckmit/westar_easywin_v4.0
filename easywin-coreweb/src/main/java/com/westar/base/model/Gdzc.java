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
 * 固定资产
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Gdzc {
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
	* 固定资产名称
	*/
	@Filed
	private String gdzcName;
	/** 
	* 固定资产编号
	*/
	@Filed
	private String gdzcNum;
	/** 
	* 资产类型
	*/
	@Filed
	private Integer ssType;
	/** 
	* 所属部门主键
	*/
	@Filed
	private Integer depId;
	/** 
	* 保管人主键
	*/
	@Filed
	private Integer manager;
	/** 
	* 残值率
	*/
	@Filed
	private Float residualRate;
	/** 
	* 折旧年限
	*/
	@Filed
	private Integer depreciationYear;
	/** 
	* 添加日期
	*/
	@Filed
	private String addDate;
	/** 
	* 添加类型
	*/
	@Filed
	private Integer addType;
	/** 
	* 资产原价
	*/
	@Filed
	private Float gdzcPrice;
	/** 
	* 状态0已删除/1使用中/2闲置/3维修中/4已减少
	*/
	@Filed
	private Integer state;

	/****************以上主要为系统表字段********************/
	/** 
	* 固定资产附件
	*/
	private List<Upfiles> listGdzcUpfiles;
	/** 
	* 资产类型名称
	*/
	private String ssTypeName;
	/** 
	* 添加类型名称
	*/
	private String addTypeName;
	private String depName;
	/** 
	* 0女1男
	*/
	private String gender;
	/** 
	* 委派人姓名
	*/
	private String managerName;
	/** 
	* 上传人头像uuid
	*/
	private String userUuid;
	/** 
	* 上传人头像名称
	*/
	private String userFileName;
	private String startDate;
	private String endDate;
	private String addStartDate;
	private String addEndDate;
	/** 
	* 附件总数
	*/
	private Integer fileNum;

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
	* 固定资产名称
	* @param gdzcName
	*/
	public void setGdzcName(String gdzcName) {
		this.gdzcName = gdzcName;
	}

	/** 
	* 固定资产名称
	* @return
	*/
	public String getGdzcName() {
		return gdzcName;
	}

	/** 
	* 固定资产编号
	* @param gdzcNum
	*/
	public void setGdzcNum(String gdzcNum) {
		this.gdzcNum = gdzcNum;
	}

	/** 
	* 固定资产编号
	* @return
	*/
	public String getGdzcNum() {
		return gdzcNum;
	}

	/** 
	* 资产类型
	* @param ssType
	*/
	public void setSsType(Integer ssType) {
		this.ssType = ssType;
	}

	/** 
	* 资产类型
	* @return
	*/
	public Integer getSsType() {
		return ssType;
	}

	/** 
	* 所属部门主键
	* @param depId
	*/
	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	/** 
	* 所属部门主键
	* @return
	*/
	public Integer getDepId() {
		return depId;
	}

	/** 
	* 保管人主键
	* @param manager
	*/
	public void setManager(Integer manager) {
		this.manager = manager;
	}

	/** 
	* 保管人主键
	* @return
	*/
	public Integer getManager() {
		return manager;
	}

	/** 
	* 残值率
	* @param residualRate
	*/
	public void setResidualRate(Float residualRate) {
		this.residualRate = residualRate;
	}

	/** 
	* 残值率
	* @return
	*/
	public Float getResidualRate() {
		return residualRate;
	}

	/** 
	* 折旧年限
	* @param depreciationYear
	*/
	public void setDepreciationYear(Integer depreciationYear) {
		this.depreciationYear = depreciationYear;
	}

	/** 
	* 折旧年限
	* @return
	*/
	public Integer getDepreciationYear() {
		return depreciationYear;
	}

	/** 
	* 添加日期
	* @param addDate
	*/
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	/** 
	* 添加日期
	* @return
	*/
	public String getAddDate() {
		return addDate;
	}

	/** 
	* 添加类型
	* @param addType
	*/
	public void setAddType(Integer addType) {
		this.addType = addType;
	}

	/** 
	* 添加类型
	* @return
	*/
	public Integer getAddType() {
		return addType;
	}

	/** 
	* 固定资产附件
	* @return
	*/
	public List<Upfiles> getListGdzcUpfiles() {
		return listGdzcUpfiles;
	}

	/** 
	* 固定资产附件
	* @param listGdzcUpfiles
	*/
	public void setListGdzcUpfiles(List<Upfiles> listGdzcUpfiles) {
		this.listGdzcUpfiles = listGdzcUpfiles;
	}

	/** 
	* 资产原价
	* @param gdzcPrice
	*/
	public void setGdzcPrice(Float gdzcPrice) {
		this.gdzcPrice = gdzcPrice;
	}

	/** 
	* 资产原价
	* @return
	*/
	public Float getGdzcPrice() {
		return gdzcPrice;
	}

	/** 
	* 资产类型名称
	* @return
	*/
	public String getSsTypeName() {
		return ssTypeName;
	}

	/** 
	* 资产类型名称
	* @param ssTypeName
	*/
	public void setSsTypeName(String ssTypeName) {
		this.ssTypeName = ssTypeName;
	}

	/** 
	* 添加类型名称
	* @return
	*/
	public String getAddTypeName() {
		return addTypeName;
	}

	/** 
	* 添加类型名称
	* @param addTypeName
	*/
	public void setAddTypeName(String addTypeName) {
		this.addTypeName = addTypeName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	/** 
	* 0女1男
	* @return
	*/
	public String getGender() {
		return gender;
	}

	/** 
	* 0女1男
	* @param gender
	*/
	public void setGender(String gender) {
		this.gender = gender;
	}

	/** 
	* 委派人姓名
	* @return
	*/
	public String getManagerName() {
		return managerName;
	}

	/** 
	* 委派人姓名
	* @param managerName
	*/
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	/** 
	* 上传人头像uuid
	* @return
	*/
	public String getUserUuid() {
		return userUuid;
	}

	/** 
	* 上传人头像uuid
	* @param userUuid
	*/
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	/** 
	* 上传人头像名称
	* @return
	*/
	public String getUserFileName() {
		return userFileName;
	}

	/** 
	* 上传人头像名称
	* @param userFileName
	*/
	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAddStartDate() {
		return addStartDate;
	}

	public void setAddStartDate(String addStartDate) {
		this.addStartDate = addStartDate;
	}

	public String getAddEndDate() {
		return addEndDate;
	}

	public void setAddEndDate(String addEndDate) {
		this.addEndDate = addEndDate;
	}

	/** 
	* 状态0已删除/1使用中/2闲置/3维修中/4已减少
	* @param state
	*/
	public void setState(Integer state) {
		this.state = state;
	}

	/** 
	* 状态0已删除/1使用中/2闲置/3维修中/4已减少
	* @return
	*/
	public Integer getState() {
		return state;
	}

	/** 
	* 附件总数
	* @return
	*/
	public Integer getFileNum() {
		return fileNum;
	}

	/** 
	* 附件总数
	* @param fileNum
	*/
	public void setFileNum(Integer fileNum) {
		this.fileNum = fileNum;
	}
}
