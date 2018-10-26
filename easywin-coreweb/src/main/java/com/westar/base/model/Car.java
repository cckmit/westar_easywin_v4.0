package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.CarApplyByDate;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 车辆表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class Car {
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
	* 添加人
	*/
	@Filed
	private Integer creator;
	/** 
	* 车辆型号
	*/
	@Filed
	private String carModel;
	/** 
	* 车牌号
	*/
	@Filed
	private String carNum;
	/** 
	* 排量
	*/
	@Filed
	private String displacement;
	/** 
	* 颜色
	*/
	@Filed
	private String color;
	/** 
	* 座位数
	*/
	@Filed
	private Integer seatNum;
	/** 
	* 发动机编号-车架号后6位
	*/
	@Filed
	private String engineNumAfterSix;
	/** 
	* 发动机编号-登记号后7位
	*/
	@Filed
	private String engineNumAfterSeven;
	/** 
	* 发动机号
	*/
	@Filed
	private String engineNum;
	/** 
	* 购置日期
	*/
	@Filed
	private String buyDate;
	/** 
	* 购置价格
	*/
	@Filed
	private String buyPrice;
	/** 
	* 购置税价格
	*/
	@Filed
	private String buyTaxPrice;
	/** 
	* 年检日期
	*/
	@Filed
	private String annualInspection;
	/** 
	* 车辆类型
	*/
	@Filed
	private Integer carTypeId;
	/** 
	* 0可用/1损坏/2维修/3报废
	*/
	@Filed
	private Integer stateType;
	/** 
	* 范围类型0不限范围/1范围
	*/
	@Filed
	private Integer scopeType;

	/****************以上主要为系统表字段********************/
	/** 
	* 车辆使用人员范围
	*/
	private List<CarScopeUser> listScopeUser;
	/** 
	* 车辆使用部门范围
	*/
	private List<CarScopeDep> listScopeDep;
	/** 
	* 强险记录
	*/
	private InsuranceRecord strongInsurance;
	/** 
	* 商业险记录
	*/
	private InsuranceRecord syInsurance;
	/** 
	* 车辆附件
	*/
	private List<Upfiles> listCarUpfiles;
	/** 
	* 强险到期日期
	*/
	private String qxEndDate;
	/** 
	* 商业险到期日期
	*/
	private String syxEndDate;
	/** 
	* 开始时间
	*/
	private String startDate;
	/** 
	* 结束时间
	*/
	private String endDate;
	private List<CarApplyByDate> listCarApply;
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
	* 添加人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 添加人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 车辆型号
	* @param carModel
	*/
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	/** 
	* 车辆型号
	* @return
	*/
	public String getCarModel() {
		return carModel;
	}

	/** 
	* 车牌号
	* @param carNum
	*/
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	/** 
	* 车牌号
	* @return
	*/
	public String getCarNum() {
		return carNum;
	}

	/** 
	* 排量
	* @param displacement
	*/
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	/** 
	* 排量
	* @return
	*/
	public String getDisplacement() {
		return displacement;
	}

	/** 
	* 颜色
	* @param color
	*/
	public void setColor(String color) {
		this.color = color;
	}

	/** 
	* 颜色
	* @return
	*/
	public String getColor() {
		return color;
	}

	/** 
	* 座位数
	* @param seatNum
	*/
	public void setSeatNum(Integer seatNum) {
		this.seatNum = seatNum;
	}

	/** 
	* 座位数
	* @return
	*/
	public Integer getSeatNum() {
		return seatNum;
	}

	/** 
	* 发动机编号-车架号后6位
	* @param engineNumAfterSix
	*/
	public void setEngineNumAfterSix(String engineNumAfterSix) {
		this.engineNumAfterSix = engineNumAfterSix;
	}

	/** 
	* 发动机编号-车架号后6位
	* @return
	*/
	public String getEngineNumAfterSix() {
		return engineNumAfterSix;
	}

	/** 
	* 发动机编号-登记号后7位
	* @param engineNumAfterSeven
	*/
	public void setEngineNumAfterSeven(String engineNumAfterSeven) {
		this.engineNumAfterSeven = engineNumAfterSeven;
	}

	/** 
	* 发动机编号-登记号后7位
	* @return
	*/
	public String getEngineNumAfterSeven() {
		return engineNumAfterSeven;
	}

	/** 
	* 发动机号
	* @param engineNum
	*/
	public void setEngineNum(String engineNum) {
		this.engineNum = engineNum;
	}

	/** 
	* 发动机号
	* @return
	*/
	public String getEngineNum() {
		return engineNum;
	}

	/** 
	* 购置日期
	* @param buyDate
	*/
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	/** 
	* 购置日期
	* @return
	*/
	public String getBuyDate() {
		return buyDate;
	}

	/** 
	* 购置价格
	* @param buyPrice
	*/
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

	/** 
	* 购置价格
	* @return
	*/
	public String getBuyPrice() {
		return buyPrice;
	}

	/** 
	* 购置税价格
	* @param buyTaxPrice
	*/
	public void setBuyTaxPrice(String buyTaxPrice) {
		this.buyTaxPrice = buyTaxPrice;
	}

	/** 
	* 购置税价格
	* @return
	*/
	public String getBuyTaxPrice() {
		return buyTaxPrice;
	}

	/** 
	* 年检日期
	* @param annualInspection
	*/
	public void setAnnualInspection(String annualInspection) {
		this.annualInspection = annualInspection;
	}

	/** 
	* 年检日期
	* @return
	*/
	public String getAnnualInspection() {
		return annualInspection;
	}

	/** 
	* 车辆类型
	* @param carTypeId
	*/
	public void setCarTypeId(Integer carTypeId) {
		this.carTypeId = carTypeId;
	}

	/** 
	* 车辆类型
	* @return
	*/
	public Integer getCarTypeId() {
		return carTypeId;
	}

	/** 
	* 车辆使用人员范围
	* @return
	*/
	public List<CarScopeUser> getListScopeUser() {
		return listScopeUser;
	}

	/** 
	* 车辆使用人员范围
	* @param listScopeUser
	*/
	public void setListScopeUser(List<CarScopeUser> listScopeUser) {
		this.listScopeUser = listScopeUser;
	}

	/** 
	* 车辆使用部门范围
	* @return
	*/
	public List<CarScopeDep> getListScopeDep() {
		return listScopeDep;
	}

	/** 
	* 车辆使用部门范围
	* @param listScopeDep
	*/
	public void setListScopeDep(List<CarScopeDep> listScopeDep) {
		this.listScopeDep = listScopeDep;
	}

	/** 
	* 强险记录
	* @return
	*/
	public InsuranceRecord getStrongInsurance() {
		return strongInsurance;
	}

	/** 
	* 强险记录
	* @param strongInsurance
	*/
	public void setStrongInsurance(InsuranceRecord strongInsurance) {
		this.strongInsurance = strongInsurance;
	}

	/** 
	* 商业险记录
	* @return
	*/
	public InsuranceRecord getSyInsurance() {
		return syInsurance;
	}

	/** 
	* 商业险记录
	* @param syInsurance
	*/
	public void setSyInsurance(InsuranceRecord syInsurance) {
		this.syInsurance = syInsurance;
	}

	/** 
	* 车辆附件
	* @return
	*/
	public List<Upfiles> getListCarUpfiles() {
		return listCarUpfiles;
	}

	/** 
	* 车辆附件
	* @param listCarUpfiles
	*/
	public void setListCarUpfiles(List<Upfiles> listCarUpfiles) {
		this.listCarUpfiles = listCarUpfiles;
	}

	/** 
	* 强险到期日期
	* @return
	*/
	public String getQxEndDate() {
		return qxEndDate;
	}

	/** 
	* 强险到期日期
	* @param qxEndDate
	*/
	public void setQxEndDate(String qxEndDate) {
		this.qxEndDate = qxEndDate;
	}

	/** 
	* 商业险到期日期
	* @return
	*/
	public String getSyxEndDate() {
		return syxEndDate;
	}

	/** 
	* 商业险到期日期
	* @param syxEndDate
	*/
	public void setSyxEndDate(String syxEndDate) {
		this.syxEndDate = syxEndDate;
	}

	/** 
	* 0可用/1损坏/2维修/3报废
	* @param stateType
	*/
	public void setStateType(Integer stateType) {
		this.stateType = stateType;
	}

	/** 
	* 0可用/1损坏/2维修/3报废
	* @return
	*/
	public Integer getStateType() {
		return stateType;
	}

	/** 
	* 范围类型0不限范围/1范围
	* @param scopeType
	*/
	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	/** 
	* 范围类型0不限范围/1范围
	* @return
	*/
	public Integer getScopeType() {
		return scopeType;
	}

	/** 
	* 开始时间
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 开始时间
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 结束时间
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 结束时间
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<CarApplyByDate> getListCarApply() {
		return listCarApply;
	}

	public void setListCarApply(List<CarApplyByDate> listCarApply) {
		this.listCarApply = listCarApply;
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
