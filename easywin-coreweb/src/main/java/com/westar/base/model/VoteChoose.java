package com.westar.base.model;

import java.util.List;
import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 投票选项
 */
@Table
@JsonInclude(Include.NON_NULL)
public class VoteChoose {
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
	* 所属投票
	*/
	@Filed
	private Integer voteId;
	/** 
	* 选项内容
	*/
	@Filed
	private String content;
	/** 
	* 原图像
	*/
	@Filed
	private Integer original;
	/** 
	* 大图像
	*/
	@Filed
	private Integer large;
	/** 
	* 中图像
	*/
	@Filed
	private Integer middle;

	/****************以上主要为系统表字段********************/
	/** 
	* 原图像的路径（用于表单传值）
	*/
	private String orgImgPath;
	/** 
	* 用于显示原图像
	*/
	private String orgImgUuid;
	private String orgImgName;
	/** 
	* 大图像的路径（用于表单传值）
	*/
	private String largeImgPath;
	/** 
	* 用于显示大图像
	*/
	private String largeImgUuid;
	private String largeImgName;
	/** 
	* 中图像的路径（用于表单传值）
	*/
	private String midImgPath;
	/** 
	* 用于显示中图像
	*/
	private String midImgUuid;
	private String midImgName;
	/** 
	* 该选项的投票总数
	*/
	private Integer total;
	/** 
	* 该选项的操作员的投票
	*/
	private Integer chooseState;
	/** 
	* 选项的所有投票人
	*/
	private List<Voter> voters;

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
	* 所属投票
	* @param voteId
	*/
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}

	/** 
	* 所属投票
	* @return
	*/
	public Integer getVoteId() {
		return voteId;
	}

	/** 
	* 选项内容
	* @param content
	*/
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	* 选项内容
	* @return
	*/
	public String getContent() {
		return content;
	}

	/** 
	* 原图像
	* @param original
	*/
	public void setOriginal(Integer original) {
		this.original = original;
	}

	/** 
	* 原图像
	* @return
	*/
	public Integer getOriginal() {
		return original;
	}

	/** 
	* 大图像
	* @param large
	*/
	public void setLarge(Integer large) {
		this.large = large;
	}

	/** 
	* 大图像
	* @return
	*/
	public Integer getLarge() {
		return large;
	}

	/** 
	* 中图像
	* @param middle
	*/
	public void setMiddle(Integer middle) {
		this.middle = middle;
	}

	/** 
	* 中图像
	* @return
	*/
	public Integer getMiddle() {
		return middle;
	}

	/** 
	* 原图像的路径（用于表单传值）
	* @return
	*/
	public String getOrgImgPath() {
		return orgImgPath;
	}

	/** 
	* 原图像的路径（用于表单传值）
	* @param orgImgPath
	*/
	public void setOrgImgPath(String orgImgPath) {
		this.orgImgPath = orgImgPath;
	}

	/** 
	* 大图像的路径（用于表单传值）
	* @return
	*/
	public String getLargeImgPath() {
		return largeImgPath;
	}

	/** 
	* 大图像的路径（用于表单传值）
	* @param largeImgPath
	*/
	public void setLargeImgPath(String largeImgPath) {
		this.largeImgPath = largeImgPath;
	}

	/** 
	* 中图像的路径（用于表单传值）
	* @return
	*/
	public String getMidImgPath() {
		return midImgPath;
	}

	/** 
	* 中图像的路径（用于表单传值）
	* @param midImgPath
	*/
	public void setMidImgPath(String midImgPath) {
		this.midImgPath = midImgPath;
	}

	/** 
	* 用于显示原图像
	* @return
	*/
	public String getOrgImgUuid() {
		return orgImgUuid;
	}

	/** 
	* 用于显示原图像
	* @param orgImgUuid
	*/
	public void setOrgImgUuid(String orgImgUuid) {
		this.orgImgUuid = orgImgUuid;
	}

	public String getOrgImgName() {
		return orgImgName;
	}

	public void setOrgImgName(String orgImgName) {
		this.orgImgName = orgImgName;
	}

	/** 
	* 用于显示大图像
	* @return
	*/
	public String getLargeImgUuid() {
		return largeImgUuid;
	}

	/** 
	* 用于显示大图像
	* @param largeImgUuid
	*/
	public void setLargeImgUuid(String largeImgUuid) {
		this.largeImgUuid = largeImgUuid;
	}

	public String getLargeImgName() {
		return largeImgName;
	}

	public void setLargeImgName(String largeImgName) {
		this.largeImgName = largeImgName;
	}

	/** 
	* 用于显示中图像
	* @return
	*/
	public String getMidImgUuid() {
		return midImgUuid;
	}

	/** 
	* 用于显示中图像
	* @param midImgUuid
	*/
	public void setMidImgUuid(String midImgUuid) {
		this.midImgUuid = midImgUuid;
	}

	public String getMidImgName() {
		return midImgName;
	}

	public void setMidImgName(String midImgName) {
		this.midImgName = midImgName;
	}

	/** 
	* 该选项的投票总数
	* @return
	*/
	public Integer getTotal() {
		return total;
	}

	/** 
	* 该选项的投票总数
	* @param total
	*/
	public void setTotal(Integer total) {
		this.total = total;
	}

	/** 
	* 选项的所有投票人
	* @return
	*/
	public List<Voter> getVoters() {
		return voters;
	}

	/** 
	* 选项的所有投票人
	* @param voters
	*/
	public void setVoters(List<Voter> voters) {
		this.voters = voters;
	}

	/** 
	* 该选项的操作员的投票
	* @return
	*/
	public Integer getChooseState() {
		return chooseState;
	}

	/** 
	* 该选项的操作员的投票
	* @param chooseState
	*/
	public void setChooseState(Integer chooseState) {
		this.chooseState = chooseState;
	}
}
