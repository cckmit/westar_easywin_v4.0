package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.westar.base.pojo.ModStaticVo;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 客户阶段
 */
@Table
@JsonInclude(Include.NON_NULL)
public class CustomerStage {
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
	* 阶段名称
	*/
	@Filed
	private String stageName;
	/** 
	* 客户阶段排序
	*/
	@Filed
	private Integer orderNum;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;

	/****************以上主要为系统表字段********************/
	/** 
	* boolean标识
	*/
	private boolean succ;
	/** 
	* 提示信息
	*/
	private String promptMsg;
	/** 
	* 用于统计不同客户分类的信息
	*/
	private List<ModStaticVo> listModStaticVos;

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
	* 阶段名称
	* @param stageName
	*/
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	/** 
	* 阶段名称
	* @return
	*/
	public String getStageName() {
		return stageName;
	}

	/** 
	* 客户阶段排序
	* @param orderNum
	*/
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/** 
	* 客户阶段排序
	* @return
	*/
	public Integer getOrderNum() {
		return orderNum;
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
	* 用于统计不同客户分类的信息
	* @return
	*/
	public List<ModStaticVo> getListModStaticVos() {
		return listModStaticVos;
	}

	/** 
	* 用于统计不同客户分类的信息
	* @param listModStaticVos
	*/
	public void setListModStaticVos(List<ModStaticVo> listModStaticVos) {
		this.listModStaticVos = listModStaticVos;
	}
}
