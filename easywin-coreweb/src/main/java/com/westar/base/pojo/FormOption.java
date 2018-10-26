package com.westar.base.pojo;
import java.io.Serializable;
import java.util.List;
/**
*表单布局选项
*/


public class FormOption implements Serializable{

/*主键id*/
 private Integer id;

/*记录创建时间*/
 private String recordCreateTime;
 /*企业编号*/
 private Integer comId;
 /*表单模板主键*/
 private Integer formModId;
 /*模板版本主键*/

 private Integer formLayoutId;
 /*布局父主键*/

 private Integer layoutDetailId;
 /*布局组件类型*/

 private String componentKey;
 /*组件index*/

 private String index;
 /*组件排序*/

 private String order;
 /*组件排序*/

 private String name;
 /*组件排序*/

 private String selectionId;
 /*组件排序*/

 private String defOption;
 /*组件排序*/

 private String other;
 /*组件排序*/

 private String objId;
 /*组件排序*/

 private String parent;

/****************以上主要为系统表字段********************/

private List<FormOption> children;

private String fieldId;

/****************以上为自己添加字段********************/

 /**
 * 主键id
 * @param id
 */
 public void setId(Integer id) {
 	this.id=id;
 }

 /**
 * 主键id
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
 	this.recordCreateTime=recordCreateTime;
 }

 /**
 * 记录创建时间
 * @return
 */
 public String getRecordCreateTime() {
 	return recordCreateTime;
 }

 /**
 *企业编号
 * @param comId
 */
 public void setComId(Integer comId) {
 	this.comId = comId;
 }

 /**
 *企业编号
 * @return
 */
 public Integer getComId() {
 	return comId;
 }

 /**
 *表单模板主键
 * @param formModId
 */
 public void setFormModId(Integer formModId) {
 	this.formModId = formModId;
 }

 /**
 *表单模板主键
 * @return
 */
 public Integer getFormModId() {
 	return formModId;
 }

 /**
 *模板版本主键
 * @param formLayoutId
 */
 public void setFormLayoutId(Integer formLayoutId) {
 	this.formLayoutId = formLayoutId;
 }

 /**
 *模板版本主键
 * @return
 */
 public Integer getFormLayoutId() {
 	return formLayoutId;
 }

 /**
 *布局父主键
 * @param layoutDetailId
 */
 public void setLayoutDetailId(Integer layoutDetailId) {
 	this.layoutDetailId = layoutDetailId;
 }

 /**
 *布局父主键
 * @return
 */
 public Integer getLayoutDetailId() {
 	return layoutDetailId;
 }

 /**
 *布局组件类型
 * @param componentKey
 */
 public void setComponentKey(String componentKey) {
 	this.componentKey = componentKey;
 }

 /**
 *布局组件类型
 * @return
 */
 public String getComponentKey() {
 	return componentKey;
 }

 /**
 *组件index
 * @param index
 */
 public void setIndex(String index) {
 	this.index = index;
 }

 /**
 *组件index
 * @return
 */
 public String getIndex() {
 	return index;
 }

 /**
 *组件排序
 * @param order
 */
 public void setOrder(String order) {
 	this.order = order;
 }

 /**
 *组件排序
 * @return
 */
 public String getOrder() {
 	return order;
 }

 /**
 *组件排序
 * @param name
 */
 public void setName(String name) {
 	this.name = name;
 }

 /**
 *组件排序
 * @return
 */
 public String getName() {
 	return name;
 }

 /**
 *组件排序
 * @param selectionId
 */
 public void setSelectionId(String selectionId) {
 	this.selectionId = selectionId;
 }

 /**
 *组件排序
 * @return
 */
 public String getSelectionId() {
 	return selectionId;
 }

 /**
 *组件排序
 * @param defOption
 */
 public void setDefOption(String defOption) {
 	this.defOption = defOption;
 }

 /**
 *组件排序
 * @return
 */
 public String getDefOption() {
 	return defOption;
 }

 /**
 *组件排序
 * @param other
 */
 public void setOther(String other) {
 	this.other = other;
 }

 /**
 *组件排序
 * @return
 */
 public String getOther() {
 	return other;
 }

 /**
 *组件排序
 * @param objId
 */
 public void setObjId(String objId) {
 	this.objId = objId;
 }

 /**
 *组件排序
 * @return
 */
 public String getObjId() {
 	return objId;
 }

 /**
 *组件排序
 * @param parent
 */
 public void setParent(String parent) {
 	this.parent = parent;
 }

 /**
 *组件排序
 * @return
 */
 public String getParent() {
 	return parent;
 }

public List<FormOption> getChildren() {
	return children;
}

public void setChildren(List<FormOption> children) {
	this.children = children;
}

public String getFieldId() {
	return fieldId;
}

public void setFieldId(String fieldId) {
	this.fieldId = fieldId;
}

}
