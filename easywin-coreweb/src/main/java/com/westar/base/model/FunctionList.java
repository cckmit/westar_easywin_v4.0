package com.westar.base.model;

import com.westar.base.annotation.DefaultFiled;
import com.westar.base.annotation.Filed;
import com.westar.base.annotation.Identity;
import com.westar.base.annotation.Table;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** 
 * 功能清单
 */
@Table
@JsonInclude(Include.NON_NULL)
public class FunctionList {
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
	* 清单父节点ID
	*/
	@Filed
	private Integer parentId;
	/** 
	* 功能名称
	*/
	@Filed
	private String functionName;
	/** 
	* 创建人
	*/
	@Filed
	private Integer creator;
	/** 
	* 功能描述
	*/
	@Filed
	private String functionDescribe;
	/** 
	* 模块区分，列值与BusinessTypeConstant常量一一对应
	*/
	@Filed
	private String busType;
	/** 
	* 关联模块主键
	*/
	@Filed
	private Integer busId;

	/****************以上主要为系统表字段********************/
	/** 
	* 模块名
	*/
	private String busName;
	/** 
	* 测试日期
	*/
	private String testDate;
	/** 
	* 父页面名称
	*/
	private String iframe;
	/** 
	* 上传文件
	*/
	private List<Upfiles> listUpfiles;

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
	* 清单父节点ID
	* @param parentId
	*/
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/** 
	* 清单父节点ID
	* @return
	*/
	public Integer getParentId() {
		return parentId;
	}

	/** 
	* 功能名称
	* @param functionName
	*/
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/** 
	* 功能名称
	* @return
	*/
	public String getFunctionName() {
		return functionName;
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
	* 功能描述
	* @param functionDescribe
	*/
	public void setFunctionDescribe(String functionDescribe) {
		this.functionDescribe = functionDescribe;
	}

	/** 
	* 功能描述
	* @return
	*/
	public String getFunctionDescribe() {
		return functionDescribe;
	}

	/** 
	* 模块区分，列值与BusinessTypeConstant常量一一对应
	* @param busType
	*/
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/** 
	* 模块区分，列值与BusinessTypeConstant常量一一对应
	* @return
	*/
	public String getBusType() {
		return busType;
	}

	/** 
	* 关联模块主键
	* @param busId
	*/
	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	/** 
	* 关联模块主键
	* @return
	*/
	public Integer getBusId() {
		return busId;
	}

	/** 
	* 模块名
	* @return
	*/
	public String getBusName() {
		return busName;
	}

	/** 
	* 模块名
	* @param busName
	*/
	public void setBusName(String busName) {
		this.busName = busName;
	}

	/** 
	* 测试日期
	* @return
	*/
	public String getTestDate() {
		return testDate;
	}

	/** 
	* 测试日期
	* @param testDate
	*/
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	/** 
	* 父页面名称
	* @return
	*/
	public String getIframe() {
		return iframe;
	}

	/** 
	* 父页面名称
	* @param iframe
	*/
	public void setIframe(String iframe) {
		this.iframe = iframe;
	}

	/** 
	* 上传文件
	* @return
	*/
	public List<Upfiles> getListUpfiles() {
		return listUpfiles;
	}

	/** 
	* 上传文件
	* @param listUpfiles
	*/
	public void setListUpfiles(List<Upfiles> listUpfiles) {
		this.listUpfiles = listUpfiles;
	}
}
