package com.westar.base.pojo;
import java.io.Serializable;

/**
*表单数据对象
*/
public class WorkFlowData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5854823398996121261L;
	
	private FormData formData;
	private Integer isDel;
	private String module;
	private String clientSource;
	public FormData getFormData() {
		return formData;
	}
	public void setFormData(FormData formData) {
		this.formData = formData;
	}
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getClientSource() {
		return clientSource;
	}
	public void setClientSource(String clientSource) {
		this.clientSource = clientSource;
	}
	
}
