package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 车辆保险记录
 */
@Table
@JsonInclude(Include.NON_NULL)
public class InsuranceRecord {
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
	* 车辆主键
	*/
	@Filed
	private Integer carId;
	/** 
	* 开始日期
	*/
	@Filed
	private String startDate;
	/** 
	* 结束日期
	*/
	@Filed
	private String endDate;
	/** 
	* 保险费
	*/
	@Filed
	private String insurancePrice;
	/** 
	* 保险类型02501强险/02502商业险
	*/
	@Filed
	private String insuranceType;

	/****************以上主要为系统表字段********************/
	/** 
	* 附件
	*/
	private List<Upfiles> listUpfiles;

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
	* 车辆主键
	* @param carId
	*/
	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	/** 
	* 车辆主键
	* @return
	*/
	public Integer getCarId() {
		return carId;
	}

	/** 
	* 开始日期
	* @param startDate
	*/
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/** 
	* 开始日期
	* @return
	*/
	public String getStartDate() {
		return startDate;
	}

	/** 
	* 结束日期
	* @param endDate
	*/
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 
	* 结束日期
	* @return
	*/
	public String getEndDate() {
		return endDate;
	}

	/** 
	* 保险费
	* @param insurancePrice
	*/
	public void setInsurancePrice(String insurancePrice) {
		this.insurancePrice = insurancePrice;
	}

	/** 
	* 保险费
	* @return
	*/
	public String getInsurancePrice() {
		return insurancePrice;
	}

	/** 
	* 保险类型02501强险/02502商业险
	* @param insuranceType
	*/
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	/** 
	* 保险类型02501强险/02502商业险
	* @return
	*/
	public String getInsuranceType() {
		return insuranceType;
	}

	/** 
	* 附件
	* @return
	*/
	public List<Upfiles> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 附件
	* @param listUpfiles
	*/
	public void setListUpfiles(List<Upfiles> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}
}
