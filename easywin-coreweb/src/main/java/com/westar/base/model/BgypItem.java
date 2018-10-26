package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 办公用品条目
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BgypItem {
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
	* 分类Id
	*/
	@Filed
	private Integer flId;
	/** 
	* 用品代码
	*/
	@Filed
	private String bgypCode;
	/** 
	* 用品名称
	*/
	@Filed
	private String bgypName;
	/** 
	* 用品规格
	*/
	@Filed
	private String bgypSpec;
	/** 
	* 用品单位
	*/
	@Filed
	private String bgypUnit;
	/** 
	* 用品购买数量
	*/
	@Filed
	private Integer storeNum;

	/****************以上主要为系统表字段********************/
	/** 
	* 所属分类名称
	*/
	private String flName;
	/** 
	* 所属分类名称
	*/
	private String bgypUnitName;
	/** 
	* 正在申领数
	*/
	private Integer applyingNum;

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
	* 分类Id
	* @param flId
	*/
	public void setFlId(Integer flId) {
		this.flId = flId;
	}

	/** 
	* 分类Id
	* @return
	*/
	public Integer getFlId() {
		return flId;
	}

	/** 
	* 用品代码
	* @param bgypCode
	*/
	public void setBgypCode(String bgypCode) {
		this.bgypCode = bgypCode;
	}

	/** 
	* 用品代码
	* @return
	*/
	public String getBgypCode() {
		return bgypCode;
	}

	/** 
	* 用品名称
	* @param bgypName
	*/
	public void setBgypName(String bgypName) {
		this.bgypName = bgypName;
	}

	/** 
	* 用品名称
	* @return
	*/
	public String getBgypName() {
		return bgypName;
	}

	/** 
	* 用品规格
	* @param bgypSpec
	*/
	public void setBgypSpec(String bgypSpec) {
		this.bgypSpec = bgypSpec;
	}

	/** 
	* 用品规格
	* @return
	*/
	public String getBgypSpec() {
		return bgypSpec;
	}

	/** 
	* 用品单位
	* @param bgypUnit
	*/
	public void setBgypUnit(String bgypUnit) {
		this.bgypUnit = bgypUnit;
	}

	/** 
	* 用品单位
	* @return
	*/
	public String getBgypUnit() {
		return bgypUnit;
	}

	/** 
	* 所属分类名称
	* @return
	*/
	public String getFlName() {
		return flName;
	}

	/** 
	* 所属分类名称
	* @param flName
	*/
	public void setFlName(String flName) {
		this.flName = flName;
	}

	/** 
	* 所属分类名称
	* @return
	*/
	public String getBgypUnitName() {
		return bgypUnitName;
	}

	/** 
	* 所属分类名称
	* @param bgypUnitName
	*/
	public void setBgypUnitName(String bgypUnitName) {
		this.bgypUnitName = bgypUnitName;
	}

	/** 
	* 用品购买数量
	* @param storeNum
	*/
	public void setStoreNum(Integer storeNum) {
		this.storeNum = storeNum;
	}

	/** 
	* 用品购买数量
	* @return
	*/
	public Integer getStoreNum() {
		return storeNum;
	}

	/** 
	* 正在申领数
	* @return
	*/
	public Integer getApplyingNum() {
		return applyingNum;
	}

	/** 
	* 正在申领数
	* @param applyingNum
	*/
	public void setApplyingNum(Integer applyingNum) {
		this.applyingNum = applyingNum;
	}
}
