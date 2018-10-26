package com.westar.base.pojo;


/**
 * 
 * 描述:公用的附件查询展示数据
 * @author zzq
 * @date 2018年10月12日 上午9:34:02
 */
public class BaseUpfile {
	/** 
	* 记录创建时间
	*/
	private String recordCreateTime;
	/** 
	* 企业编号
	*/
	private Integer comId;
	/** 
	* 共享任务主键
	*/
	private Integer busId;
	/** 
	* 关联upfiles主键
	*/
	private Integer upfileId;
	/** 
	* 附件上传人
	*/
	private Integer userId;

	/** 
	* 附件userName
	*/
	private String userName;
	/** 
	 * 附件UUID
	 */
	private String uuid;
	/** 
	* 附件名称
	*/
	private String fileName;
	/** 
	* 附件来源的任务名称
	*/
	private String fileExt;
	/**
	 * 是否为图片
	 */
	private String isPic;
	/** 
	* 文件大小
	*/
	private String sizem;
	/** 
	 * 文件来源
	 */
	private String sourceFrom;
	
	/** 
	 * 文件来源模块名称
	 */
	private String sourceName;
	
	/**
	 * 子模块的数据信息
	 */
	private Integer subBusId;
	
	
	
	public String getRecordCreateTime() {
		return recordCreateTime;
	}
	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}
	public Integer getComId() {
		return comId;
	}
	public void setComId(Integer comId) {
		this.comId = comId;
	}
	public Integer getBusId() {
		return busId;
	}
	public void setBusId(Integer busId) {
		this.busId = busId;
	}
	public Integer getUpfileId() {
		return upfileId;
	}
	public void setUpfileId(Integer upfileId) {
		this.upfileId = upfileId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public String getSizem() {
		return sizem;
	}
	public void setSizem(String sizem) {
		this.sizem = sizem;
	}
	public String getSourceFrom() {
		return sourceFrom;
	}
	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public Integer getSubBusId() {
		return subBusId;
	}
	public void setSubBusId(Integer subBusId) {
		this.subBusId = subBusId;
	}
	public String getIsPic() {
		return isPic;
	}
	public void setIsPic(String isPic) {
		this.isPic = isPic;
	}
	
	
}
