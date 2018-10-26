package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.BgypPojo;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 用品申领详情
 */
@Table
@JsonInclude(Include.NON_NULL)
public class BgypApplyDetail extends BgypPojo {
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
	* 用品申领Id
	*/
	@Filed
	private Integer bgypApplyId;
	/** 
	* 办公用品Id
	*/
	@Filed
	private Integer bgypItemId;
	/** 
	* 用品申领数量
	*/
	@Filed
	private Integer applyNum;

	/****************以上主要为系统表字段********************/
	/** 
	* 库存数
	*/
	private Integer storeNum;
	/** 
	* 申请中的数目
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
	* 用品申领Id
	* @param bgypApplyId
	*/
	public void setBgypApplyId(Integer bgypApplyId) {
		this.bgypApplyId = bgypApplyId;
	}

	/** 
	* 用品申领Id
	* @return
	*/
	public Integer getBgypApplyId() {
		return bgypApplyId;
	}

	/** 
	* 办公用品Id
	* @param bgypItemId
	*/
	public void setBgypItemId(Integer bgypItemId) {
		this.bgypItemId = bgypItemId;
	}

	/** 
	* 办公用品Id
	* @return
	*/
	public Integer getBgypItemId() {
		return bgypItemId;
	}

	/** 
	* 用品申领数量
	* @param applyNum
	*/
	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}

	/** 
	* 用品申领数量
	* @return
	*/
	public Integer getApplyNum() {
		return applyNum;
	}

	/** 
	* 申请中的数目
	* @return
	*/
	public Integer getApplyingNum() {
		return applyingNum;
	}

	/** 
	* 申请中的数目
	* @param applyingNum
	*/
	public void setApplyingNum(Integer applyingNum) {
		this.applyingNum = applyingNum;
	}

	/** 
	* 库存数
	* @return
	*/
	public Integer getStoreNum() {
		return storeNum;
	}

	/** 
	* 库存数
	* @param storeNum
	*/
	public void setStoreNum(Integer storeNum) {
		this.storeNum = storeNum;
	}
}
