package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 团队升级订单配置表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class OrgUpgrade {
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
	* 企业编号 0为系统默认
	*/
	@Filed
	private Integer comId;
	/** 
	* 关联orders主键
	*/
	@Filed
	private Integer orderId;
	/** 
	* 关联discountStandards主键
	*/
	@Filed
	private Integer discountId;
	/** 
	* 关联chargingStandards主键
	*/
	@Filed
	private Integer chargId;
	/** 
	* 人数范围
	*/
	@Filed
	private Integer usersNum;
	/** 
	* 租用年限
	*/
	@Filed
	private Integer years;

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
	* 企业编号 0为系统默认
	* @param comId
	*/
	public void setComId(Integer comId) {
		this.comId = comId;
	}

	/** 
	* 企业编号 0为系统默认
	* @return
	*/
	public Integer getComId() {
		return comId;
	}

	/** 
	* 关联orders主键
	* @param orderId
	*/
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/** 
	* 关联orders主键
	* @return
	*/
	public Integer getOrderId() {
		return orderId;
	}

	/** 
	* 关联discountStandards主键
	* @param discountId
	*/
	public void setDiscountId(Integer discountId) {
		this.discountId = discountId;
	}

	/** 
	* 关联discountStandards主键
	* @return
	*/
	public Integer getDiscountId() {
		return discountId;
	}

	/** 
	* 关联chargingStandards主键
	* @param chargId
	*/
	public void setChargId(Integer chargId) {
		this.chargId = chargId;
	}

	/** 
	* 关联chargingStandards主键
	* @return
	*/
	public Integer getChargId() {
		return chargId;
	}

	/** 
	* 人数范围
	* @param usersNum
	*/
	public void setUsersNum(Integer usersNum) {
		this.usersNum = usersNum;
	}

	/** 
	* 人数范围
	* @return
	*/
	public Integer getUsersNum() {
		return usersNum;
	}

	/** 
	* 租用年限
	* @param years
	*/
	public void setYears(Integer years) {
		this.years = years;
	}

	/** 
	* 租用年限
	* @return
	*/
	public Integer getYears() {
		return years;
	}
}
