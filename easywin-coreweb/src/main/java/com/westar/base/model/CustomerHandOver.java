package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 客户移交记录表
 */
@Table
@JsonInclude(Include.NON_NULL)
public class CustomerHandOver {
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
	* 客户主键
	*/
	@Filed
	private Integer customerId;
	/** 
	* 移交人ID
	*/
	@Filed
	private Integer fromUser;
	/** 
	* 接收人ID
	*/
	@Filed
	private Integer toUser;

	/****************以上主要为系统表字段********************/
	/** 
	* 私有组ID和组类型组合字符串
	*/
	private String groupIdAndType;
	/** 
	* 是否分享信息
	*/
	private String shareMsg;
	/** 
	* 移交方式；批量还是单个
	*/
	private String handType;
	private String replayContent;
	private Integer feedBackTypeId;

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
	* 客户主键
	* @param customerId
	*/
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/** 
	* 客户主键
	* @return
	*/
	public Integer getCustomerId() {
		return customerId;
	}

	/** 
	* 移交人ID
	* @param fromUser
	*/
	public void setFromUser(Integer fromUser) {
		this.fromUser = fromUser;
	}

	/** 
	* 移交人ID
	* @return
	*/
	public Integer getFromUser() {
		return fromUser;
	}

	/** 
	* 接收人ID
	* @param toUser
	*/
	public void setToUser(Integer toUser) {
		this.toUser = toUser;
	}

	/** 
	* 接收人ID
	* @return
	*/
	public Integer getToUser() {
		return toUser;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @return
	*/
	public String getGroupIdAndType() {
		return groupIdAndType;
	}

	/** 
	* 私有组ID和组类型组合字符串
	* @param groupIdAndType
	*/
	public void setGroupIdAndType(String groupIdAndType) {
		this.groupIdAndType = groupIdAndType;
	}

	/** 
	* 是否分享信息
	* @return
	*/
	public String getShareMsg() {
		return shareMsg;
	}

	/** 
	* 是否分享信息
	* @param shareMsg
	*/
	public void setShareMsg(String shareMsg) {
		this.shareMsg = shareMsg;
	}

	/** 
	* 移交方式；批量还是单个
	* @return
	*/
	public String getHandType() {
		return handType;
	}

	/** 
	* 移交方式；批量还是单个
	* @param handType
	*/
	public void setHandType(String handType) {
		this.handType = handType;
	}

	public String getReplayContent() {
		return replayContent;
	}

	public void setReplayContent(String replayContent) {
		this.replayContent = replayContent;
	}

	public Integer getFeedBackTypeId() {
		return feedBackTypeId;
	}

	public void setFeedBackTypeId(Integer feedBackTypeId) {
		this.feedBackTypeId = feedBackTypeId;
	}
}
