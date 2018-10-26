package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 帮助分类
 */
@Table
@JsonInclude(Include.NON_NULL)
public class HelpType {
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
	* 类型描述
	*/
	@Filed
	private String name;
	/** 
	* 描述
	*/
	@Filed
	private String describe;
	/** 
	* 父级主键
	*/
	@Filed
	private Integer pId;
	/** 
	* 排序-升序排列
	*/
	@Filed
	private Integer orderNum;
	/** 
	* 配置是否开启，0未开启
	*/
	@Filed
	private Integer openState;
	/** 
	* 更新时间
	*/
	@Filed
	private String modifyTime;

	/****************以上主要为系统表字段********************/
	/** 
	* 下一个排序序号
	*/
	private Integer nextOrderNum;
	/** 
	* 子集数量
	*/
	private Integer sunNum;
	/** 
	* 解答明细
	*/
	private String content;
	/** 
	* 父级名称
	*/
	private String pName;
	/** 
	* 解答集合
	*/
	private List<HelpType> ansList;

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
	* 类型描述
	* @param name
	*/
	public void setName(String name) {
		this.name = name;
	}

	/** 
	* 类型描述
	* @return
	*/
	public String getName() {
		return name;
	}

	/** 
	* 描述
	* @param describe
	*/
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/** 
	* 描述
	* @return
	*/
	public String getDescribe() {
		return describe;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	/** 
	* 排序-升序排列
	* @param orderNum
	*/
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/** 
	* 排序-升序排列
	* @return
	*/
	public Integer getOrderNum() {
		return orderNum;
	}

	/** 
	* 配置是否开启，0未开启
	* @param openState
	*/
	public void setOpenState(Integer openState) {
		this.openState = openState;
	}

	/** 
	* 配置是否开启，0未开启
	* @return
	*/
	public Integer getOpenState() {
		return openState;
	}

	/** 
	* 更新时间
	* @param modifyTime
	*/
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/** 
	* 更新时间
	* @return
	*/
	public String getModifyTime() {
		return modifyTime;
	}

	/** 
	* 下一个排序序号
	* @return
	*/
	public Integer getNextOrderNum() {
		return nextOrderNum;
	}

	/** 
	* 下一个排序序号
	* @param nextOrderNum
	*/
	public void setNextOrderNum(Integer nextOrderNum) {
		this.nextOrderNum = nextOrderNum;
	}

	/** 
	* 子集数量
	* @return
	*/
	public Integer getSunNum() {
		return sunNum;
	}

	/** 
	* 子集数量
	* @param sunNum
	*/
	public void setSunNum(Integer sunNum) {
		this.sunNum = sunNum;
	}

	/** 
	* 解答明细
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 解答明细
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	/** 
	* 解答集合
	* @return
	*/
	public List<HelpType> getAnsList() {
		return ansList;
	}

	/** 
	* 解答集合
	* @param ansList
	*/
	public void setAnsList(List<HelpType> ansList) {
		this.ansList = ansList;
	}
}
