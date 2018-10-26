package com.westar.base.pojo;

import java.io.Serializable;

import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;

@SuppressWarnings("serial")
public class FormModLayout implements Serializable{
	
	private FormMod formMod;
	
	private FormLayout formLayout;
	
	public FormMod getFormMod() {
		return formMod;
	}
	public void setFormMod(FormMod formMod) {
		this.formMod = formMod;
	}
	public FormLayout getFormLayout() {
		return formLayout;
	}
	public void setFormLayout(FormLayout formLayout) {
		this.formLayout = formLayout;
	}
	

}
