package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 强制参与人列表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class ForceInPersion {
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
	* 强制参与类型，列值于BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String type;
	/** 
	* 强制参与人主键
	*/
	@Filed
	private Integer userId;

	/****************以上主要为系统表字段********************/
	/** 
	* 客户中心督察人员集合
	*/
	private List<CustomerSharer> listCustomerSharer;
	/** 
	* 项目中心督察人员集合
	*/
	private List<ItemSharer> listItemSharer;
	/** 
	* 任务中心督察人员集合
	*/
	private List<TaskSharer> listTaskSharer;
	/** 
	* 客户分享人json字符串
	*/
	private String sharerJson;
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 参与人姓名
	*/
	private String sharerName;
	/** 
	* 附件名称
	*/
	private String filename;
	/** 
	* 附件UUID
	*/
	private String uuid;
	/** 
	* 0女1男
	*/
	private String gender;

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
	* 强制参与类型，列值于BusinessTypeConstant常量一一对应
	* @param type
	*/
	public void setType(String type) {
		this.type = type;
	}

	/** 
	* 强制参与类型，列值于BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getType() {
		return type;
	}

	/** 
	* 强制参与人主键
	* @param userId
	*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 
	* 强制参与人主键
	* @return
	*/
	public Integer getUserId() {
		return userId;
	}

	/** 
	* 客户中心督察人员集合
	* @return
	*/
	public List<CustomerSharer> getListCustomerSharer() {
		return listCustomerSharer;
	}

	/** 
	* 客户中心督察人员集合
	* @param listCustomerSharer
	*/
	public void setListCustomerSharer(List<CustomerSharer> listCustomerSharer) {
		this.listCustomerSharer = listCustomerSharer;
	}

	/** 
	* 客户分享人json字符串
	* @return
	*/
	public String getSharerJson() {
		return sharerJson;
	}

	/** 
	* 客户分享人json字符串
	* @param sharerJson
	*/
	public void setSharerJson(String sharerJson) {
		this.sharerJson = sharerJson;
	}

	public boolean isSucc() {
		return succ;
	}

	/** 
	* boolean标识
	* @param succ
	*/
	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	/** 
	* 提示信息
	* @return
	*/
	public String getPromptMsg() {
		return promptMsg;
	}

	/** 
	* 提示信息
	* @param promptMsg
	*/
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	/** 
	* 项目中心督察人员集合
	* @return
	*/
	public List<ItemSharer> getListItemSharer() {
		return listItemSharer;
	}

	/** 
	* 项目中心督察人员集合
	* @param listItemSharer
	*/
	public void setListItemSharer(List<ItemSharer> listItemSharer) {
		this.listItemSharer = listItemSharer;
	}

	/** 
	* 任务中心督察人员集合
	* @return
	*/
	public List<TaskSharer> getListTaskSharer() {
		return listTaskSharer;
	}

	/** 
	* 任务中心督察人员集合
	* @param listTaskSharer
	*/
	public void setListTaskSharer(List<TaskSharer> listTaskSharer) {
		this.listTaskSharer = listTaskSharer;
	}

	/** 
	* 参与人姓名
	* @return
	*/
	public String getSharerName() {
		return sharerName;
	}

	/** 
	* 参与人姓名
	* @param sharerName
	*/
	public void setSharerName(String sharerName) {
		this.sharerName = sharerName;
	}

	/** 
	* 附件名称
	* @return
	*/
	public String getFilename() {
		return filename;
	}

	/** 
	* 附件名称
	* @param filename
	*/
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/** 
	* 附件UUID
	* @return
	*/
	public String getUuid() {
		return uuid;
	}

	/** 
	* 附件UUID
	* @param uuid
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
}
