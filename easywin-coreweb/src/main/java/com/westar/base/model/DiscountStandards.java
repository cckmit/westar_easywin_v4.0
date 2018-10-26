package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 折扣标准表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class DiscountStandards {
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
	* 取值范围：0~1之间
	*/
	@Filed
	private Float discount;
	/** 
	* 描述
	*/
	@Filed
	private String describle;
	/** 
	* 折扣标准
	*/
	@Filed
	private Integer discountStandard;
	/** 
	* 折扣类型
	*/
	@Filed
	private String discountType;

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
	* 取值范围：0~1之间
	* @param discount
	*/
	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	/** 
	* 取值范围：0~1之间
	* @return
	*/
	public Float getDiscount() {
		return discount;
	}

	/** 
	* 描述
	* @param describle
	*/
	public void setDescrible(String describle) {
		this.describle = describle;
	}

	/** 
	* 描述
	* @return
	*/
	public String getDescrible() {
		return describle;
	}

	/** 
	* 折扣标准
	* @param discountStandard
	*/
	public void setDiscountStandard(Integer discountStandard) {
		this.discountStandard = discountStandard;
	}

	/** 
	* 折扣标准
	* @return
	*/
	public Integer getDiscountStandard() {
		return discountStandard;
	}

	/** 
	* 折扣类型
	* @param discountType
	*/
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	/** 
	* 折扣类型
	* @return
	*/
	public String getDiscountType() {
		return discountType;
	}
}
