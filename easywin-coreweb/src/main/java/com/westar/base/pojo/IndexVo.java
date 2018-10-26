package com.westar.base.pojo;

import java.util.Date;

public class IndexVo<T> {

	//索引主键
	private String id;
	
	//企业主键
	private Integer comId;
	
	//业务主键
	private Integer busId;
	
	//业务类型
	private String busType;
	
	//业务名称 
	private String busName;
	
	//业务穿透地址
	private String url;
	
	//分享人用户ID
	private Integer sharerId;
	
	//索引字段
	private String content;
	
	//创建日期
	private Date createDate;
	
	//数据
    private T data;
    
    //新增URL
    private String addUrl;
    
    //新增模块数据名称描述
    private String addName;
    // 关联模块主键
    private Integer relatedBusId;
    //关联模块类型
    private String relatedBusType;
    
    //创建时间（显示）
    private String recordCreateTime;
    
    //栏目宽度
    private Integer width;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSharerId() {
		return sharerId;
	}

	public void setSharerId(Integer sharerId) {
		this.sharerId = sharerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getComId() {
		return comId;
	}

	public void setComId(Integer comId) {
		this.comId = comId;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getAddUrl() {
		return addUrl;
	}

	public void setAddUrl(String addUrl) {
		this.addUrl = addUrl;
	}

	public String getAddName() {
		return addName;
	}

	public void setAddName(String addName) {
		this.addName = addName;
	}

	public String getRecordCreateTime() {
		return recordCreateTime;
	}

	public void setRecordCreateTime(String recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	public Integer getRelatedBusId() {
		return relatedBusId;
	}

	public void setRelatedBusId(Integer relatedBusId) {
		this.relatedBusId = relatedBusId;
	}

	public String getRelatedBusType() {
		return relatedBusType;
	}

	public void setRelatedBusType(String relatedBusType) {
		this.relatedBusType = relatedBusType;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	
}
