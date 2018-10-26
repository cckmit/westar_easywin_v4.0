package com.westar.base.pojo;

//存放键值对
public class KeyValue implements Cloneable {
	
	@Override
	public KeyValue clone() throws CloneNotSupportedException {
		return (KeyValue)super.clone();
	}
	private String key;//键
	private String value;//值
	private String checked;//是否选中
	private String isRequired;//是否必填
	private String formControlKey;//关联表单主键
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getFormControlKey() {
		return formControlKey;
	}
	public void setFormControlKey(String formControlKey) {
		this.formControlKey = formControlKey;
	}
	public String getIsRequired() {
		return (null == isRequired || "".equals(isRequired))?"0":isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	
}
