package com.westar.base.pojo;
import java.io.Serializable;
import java.util.List;

/**
*表单数据对象
*/
public class FormDataDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2539471306706278980L;
	//原字段内容
	private String oldContent;
	//当前字段内容
	private String content;
	//文本框内容
	private  FormDataOption dataText;
	//组件选项
	private  List<FormDataOption> dataOptions;
	//组件字段标识
	private  FormDataOption formField;
	//动态表单
	private  FormDataOption subForm;
	//动态表单行号
	private Integer dataIndex;
	
	//动态表单行关联的数据表
	private String busTableType;
	//动态表单行关联的数据表
	private Integer busTableId;
	
	public String getOldContent() {
		return oldContent;
	}
	public void setOldContent(String oldContent) {
		this.oldContent = oldContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public FormDataOption getDataText() {
		return dataText;
	}
	public void setDataText(FormDataOption dataText) {
		this.dataText = dataText;
	}
	public List<FormDataOption> getDataOptions() {
		return dataOptions;
	}
	public void setDataOptions(List<FormDataOption> dataOptions) {
		this.dataOptions = dataOptions;
	}
	public FormDataOption getFormField() {
		return formField;
	}
	public void setFormField(FormDataOption formField) {
		this.formField = formField;
	}
	public Integer getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}
	public FormDataOption getSubForm() {
		return subForm;
	}
	public void setSubForm(FormDataOption subForm) {
		this.subForm = subForm;
	}
	public Integer getBusTableId() {
		return busTableId;
	}
	public void setBusTableId(Integer busTableId) {
		this.busTableId = busTableId;
	}
	public String getBusTableType() {
		return busTableType;
	}
	public void setBusTableType(String busTableType) {
		this.busTableType = busTableType;
	}
}
