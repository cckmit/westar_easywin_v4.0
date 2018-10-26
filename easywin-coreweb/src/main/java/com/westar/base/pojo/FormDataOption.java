package com.westar.base.pojo;
import java.io.Serializable;

import com.westar.base.annotation.Filed;

/**
*表单数据选项对象
*/
public class FormDataOption implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5239290230037589822L;
	
	//标题
	String title;
	//组件类型
	String componentKey;
	Integer id;
	//选项主键
	private Integer optionId;
	//选项内容
	private String content;
	//所属动态表单标识
	private Integer subFormId;
	//动态表单行数
	private Integer maxIndex;
	//动态表单是否移除过数据
	private Integer removeRowNumber;
	/*关联模块类型*/
	private String dataBustype;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComponentKey() {
		return componentKey;
	}
	public void setComponentKey(String componentKey) {
		this.componentKey = componentKey;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOptionId() {
		return optionId;
	}
	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getSubFormId() {
		return subFormId;
	}
	public void setSubFormId(Integer subFormId) {
		this.subFormId = subFormId;
	}
	public Integer getMaxIndex() {
		return maxIndex;
	}
	public void setMaxIndex(Integer maxIndex) {
		this.maxIndex = maxIndex;
	}
	public Integer getRemoveRowNumber() {
		return removeRowNumber;
	}
	public void setRemoveRowNumber(Integer removeRowNumber) {
		this.removeRowNumber = removeRowNumber;
	}
	public String getDataBustype() {
		return dataBustype;
	}
	public void setDataBustype(String dataBustype) {
		this.dataBustype = dataBustype;
	}
}
