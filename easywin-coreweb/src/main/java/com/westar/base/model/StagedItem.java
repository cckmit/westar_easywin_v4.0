package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.ItemStagedInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 项目阶段
 */
@Table
@JsonInclude(Include.NON_NULL)
public class StagedItem {
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
	* 项目主键
	*/
	@Filed
	private Integer itemId;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 阶段名称
	*/
	@Filed
	private String stagedName;
	/** 
	* 阶段排序
	*/
	@Filed
	private Integer stagedOrder;
	/** 
	* 阶段父ID
	*/
	@Filed
	private Integer parentId;

	/****************以上主要为系统表字段********************/
	/** 
	* 项目阶段明细json字符串
	*/
	private String itemStagedJsonStr;
	/** 
	* 项目名次
	*/
	private String itemName;
	/** 
	* 项目阶段所有后代集合
	*/
	private List<ItemStagedInfo> stagedItemChildren;
	/** 
	* 是否删除子集数据标识符
	*/
	private String delChildren;
	/** 
	* 返回提示信息
	*/
	private String promptMsg;
	/** 
	* 成功与否标识
	*/
	private boolean succ;
	/** 
	* 阶段是否可编辑
	*/
	private boolean stagedEnable;

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
	* 项目主键
	* @param itemId
	*/
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/** 
	* 项目主键
	* @return
	*/
	public Integer getItemId() {
		return itemId;
	}

	/** 
	* 创建人
	* @param creator
	*/
	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	/** 
	* 创建人
	* @return
	*/
	public Integer getCreator() {
		return creator;
	}

	/** 
	* 阶段名称
	* @param stagedName
	*/
	public void setStagedName(String stagedName) {
		this.stagedName = stagedName;
	}

	/** 
	* 阶段名称
	* @return
	*/
	public String getStagedName() {
		return stagedName;
	}

	/** 
	* 阶段排序
	* @param stagedOrder
	*/
	public void setStagedOrder(Integer stagedOrder) {
		this.stagedOrder = stagedOrder;
	}

	/** 
	* 阶段排序
	* @return
	*/
	public Integer getStagedOrder() {
		return stagedOrder;
	}

	/** 
	* 阶段父ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 阶段父ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 项目阶段明细json字符串
	* @return
	*/
	public String getItemStagedJsonStr() {
		return itemStagedJsonStr;
	}

	/** 
	* 项目阶段明细json字符串
	* @param itemStagedJsonStr
	*/
	public void setItemStagedJsonStr(String itemStagedJsonStr) {
		this.itemStagedJsonStr = itemStagedJsonStr;
	}

	/** 
	* 项目名次
	* @return
	*/
	public String getItemName() {
		return itemName;
	}

	/** 
	* 项目名次
	* @param itemName
	*/
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/** 
	* 项目阶段所有后代集合
	* @return
	*/
	public List<ItemStagedInfo> getStagedItemChildren() {
		return stagedItemChildren;
	}

	/** 
	* 项目阶段所有后代集合
	* @param stagedItemChildren
	*/
	public void setStagedItemChildren(List<ItemStagedInfo> stagedItemChildren) {
		this.stagedItemChildren = stagedItemChildren;
	}

	/** 
	* 是否删除子集数据标识符
	* @return
	*/
	public String getDelChildren() {
		return delChildren;
	}

	/** 
	* 是否删除子集数据标识符
	* @param delChildren
	*/
	public void setDelChildren(String delChildren) {
		this.delChildren = delChildren;
	}

	/** 
	* 返回提示信息
	* @return
	*/
	public String getPromptMsg() {
		return promptMsg;
	}

	/** 
	* 返回提示信息
	* @param promptMsg
	*/
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	public boolean isSucc() {
		return succ;
	}

	/** 
	* 成功与否标识
	* @param succ
	*/
	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	public boolean isStagedEnable() {
		return stagedEnable;
	}

	/** 
	* 阶段是否可编辑
	* @param stagedEnable
	*/
	public void setStagedEnable(boolean stagedEnable) {
		this.stagedEnable = stagedEnable;
	}
}
