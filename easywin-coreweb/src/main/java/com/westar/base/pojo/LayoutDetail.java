package com.westar.base.pojo;
import java.io.Serializable;
import java.util.List;
/**
*表单布局详情
*/


public class LayoutDetail implements Serializable{

 /*表单模板主键*/
 private Integer formModId;
 /*模板版本主键*/
 private Integer formLayoutId;
 /*布局组件类型*/
 private String componentKey;
 /*标题*/
 private String title;
 /*布局方式*/
 private String titleLayout;
 /*组件大小*/
 private String size;
 /*内容*/
 private String content;
 /*组件排序*/
 private String order;
 /*是否默认*/
 private String isDefault;
 /*描述*/
 private String describe;
 /*隐藏标题*/
 private String isHideTitle;
 /*是否只读*/
 private String isReadOnly;
 /*临时ID*/
 private String tempId;
 /*组件index*/
 private String index;
 /*是否必填*/
 private String required;
 /*单个布局*/
 private String layout;
 /*日期格式化样式*/
 private String format;
 /*是否系统时间*/
 private String isSystemDate;
 /*是否单项*/
 private String isUnique;
 /*是否为当前人员*/
 private String isCurrentEmployee;
 /*是否为当前部门*/
 private String isCurrentDepartment;
 /*是否只读*/
 private String isReadonly;
 /*类型*/
 private String type;
 /*分割线颜色*/
 private String color;
 /*段落样式*/
 private String style;
 
 //行数
 private String rows;
 //列数
 private String cols;
 private List<Integer> thArray;
 private String tableId;
 
 //所在位置
 private String coordinate;
 private String rowSpan;
 private String colSpan;
 private String width;
 private String height;
 
 //计算控件
 //计算类型 date number
 private String monitorType;
 //是否可编辑 true
 private String isEdit;
 
 //大写转换工具，指定字段
 private String moneyTempColumn;
 //大写转换工具，指定字段
 private String moneyColumn;
 
 
 //计算时间类型 1休假时  2工作时
 private String calTimeType;
 
 //关联模块
 private String relateModType;
 //序列号信息
 private String serialNumId;
 private String serialNumRemark;
 /**
  * 子布局详情
  */
 private List<LayoutDetail> layoutDetail;
 //选中的结果
 private List<LayoutDetail> selects;
 //日期区间始
 private LayoutDetail start;
 //日期时间终
 private LayoutDetail end;
 //日期时间终
 private LayoutDetail NOShow;
 //下拉选项
 private List<FormOption> options;
 
 private List<LayoutDetail> fieldReads;
 
 private List<LayoutDetail> selectIds;
 
 private List<LayoutDetail> fieldWrites;
 //自身组件主键
 private Integer fieldId;
 //表单主键
 private Integer formId;
 
 private String parent;
 
 private String subFormId;
 private String defaultRows;
 private String showSreial;
 
 //计算控件的组件信息
 private List<MonitorField> monitorFields;
 //计算控件的临时信息
 private String monitorTempFields;
 
 private List<MonitorField> componentIds;
 
 //子表单关联的table
 private String relateTable;
 //table的字段
 private String tableColumn;
 //采用的字典表
 private String sysDataDic;
 
 

public Integer getFormModId() {
	return formModId;
}

public void setFormModId(Integer formModId) {
	this.formModId = formModId;
}

public Integer getFormLayoutId() {
	return formLayoutId;
}

public void setFormLayoutId(Integer formLayoutId) {
	this.formLayoutId = formLayoutId;
}

public String getComponentKey() {
	return componentKey;
}

public void setComponentKey(String componentKey) {
	this.componentKey = componentKey;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getTitleLayout() {
	return titleLayout;
}

public void setTitleLayout(String titleLayout) {
	this.titleLayout = titleLayout;
}

public String getSize() {
	return size;
}

public void setSize(String size) {
	this.size = size;
}

public String getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}

public String getOrder() {
	return order;
}

public void setOrder(String order) {
	this.order = order;
}

public String getIsDefault() {
	return isDefault;
}

public void setIsDefault(String isDefault) {
	this.isDefault = isDefault;
}

public String getDescribe() {
	return describe;
}

public void setDescribe(String describe) {
	this.describe = describe;
}

public String getIsHideTitle() {
	return isHideTitle;
}

public void setIsHideTitle(String isHideTitle) {
	this.isHideTitle = isHideTitle;
}

public String getIsReadOnly() {
	return isReadOnly;
}

public void setIsReadOnly(String isReadOnly) {
	this.isReadOnly = isReadOnly;
}

public String getTempId() {
	return tempId;
}

public void setTempId(String tempId) {
	this.tempId = tempId;
}

public String getIndex() {
	return index;
}

public void setIndex(String index) {
	this.index = index;
}

public String getRequired() {
	return required;
}

public void setRequired(String required) {
	this.required = required;
}

public String getLayout() {
	return layout;
}

public void setLayout(String layout) {
	this.layout = layout;
}

public String getFormat() {
	return format;
}

public void setFormat(String format) {
	this.format = format;
}

public String getIsSystemDate() {
	return isSystemDate;
}

public void setIsSystemDate(String isSystemDate) {
	this.isSystemDate = isSystemDate;
}

public String getIsUnique() {
	return isUnique;
}

public void setIsUnique(String isUnique) {
	this.isUnique = isUnique;
}

public String getIsCurrentEmployee() {
	return isCurrentEmployee;
}

public void setIsCurrentEmployee(String isCurrentEmployee) {
	this.isCurrentEmployee = isCurrentEmployee;
}

public String getIsReadonly() {
	return isReadonly;
}

public void setIsReadonly(String isReadonly) {
	this.isReadonly = isReadonly;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getColor() {
	return color;
}

public void setColor(String color) {
	this.color = color;
}

public String getStyle() {
	return style;
}

public void setStyle(String style) {
	this.style = style;
}

public List<LayoutDetail> getLayoutDetail() {
	return layoutDetail;
}

public void setLayoutDetail(List<LayoutDetail> layoutDetail) {
	this.layoutDetail = layoutDetail;
}

public List<LayoutDetail> getSelects() {
	return selects;
}

public void setSelects(List<LayoutDetail> selects) {
	this.selects = selects;
}

public LayoutDetail getStart() {
	return start;
}

public void setStart(LayoutDetail start) {
	this.start = start;
}

public LayoutDetail getEnd() {
	return end;
}

public void setEnd(LayoutDetail end) {
	this.end = end;
}

public List<FormOption> getOptions() {
	return options;
}

public void setOptions(List<FormOption> options) {
	this.options = options;
}

public List<LayoutDetail> getFieldReads() {
	return fieldReads;
}

public void setFieldReads(List<LayoutDetail> fieldReads) {
	this.fieldReads = fieldReads;
}

public List<LayoutDetail> getSelectIds() {
	return selectIds;
}

public void setSelectIds(List<LayoutDetail> selectIds) {
	this.selectIds = selectIds;
}

public List<LayoutDetail> getFieldWrites() {
	return fieldWrites;
}

public void setFieldWrites(List<LayoutDetail> fieldWrites) {
	this.fieldWrites = fieldWrites;
}

public Integer getFieldId() {
	return fieldId;
}

public void setFieldId(Integer fieldId) {
	this.fieldId = fieldId;
}

public Integer getFormId() {
	return formId;
}

public void setFormId(Integer formId) {
	this.formId = formId;
}

public String getParent() {
	return parent;
}

public void setParent(String parent) {
	this.parent = parent;
}

public String getRows() {
	return rows;
}

public void setRows(String rows) {
	this.rows = rows;
}

public String getCols() {
	return cols;
}

public void setCols(String cols) {
	this.cols = cols;
}

public String getCoordinate() {
	return coordinate;
}

public void setCoordinate(String coordinate) {
	this.coordinate = coordinate;
}

public String getRowSpan() {
	return rowSpan;
}

public void setRowSpan(String rowSpan) {
	this.rowSpan = rowSpan;
}

public String getColSpan() {
	return colSpan;
}

public void setColSpan(String colSpan) {
	this.colSpan = colSpan;
}

public String getWidth() {
	return width;
}

public void setWidth(String width) {
	this.width = width;
}

public String getHeight() {
	return height;
}

public void setHeight(String height) {
	this.height = height;
}

public LayoutDetail getNOShow() {
	return NOShow;
}

public void setNOShow(LayoutDetail nOShow) {
	NOShow = nOShow;
}

public String getSubFormId() {
	return subFormId;
}

public void setSubFormId(String subFormId) {
	this.subFormId = subFormId;
}

public String getDefaultRows() {
	return defaultRows;
}

public void setDefaultRows(String defaultRows) {
	this.defaultRows = defaultRows;
}

public String getShowSreial() {
	return showSreial;
}

public void setShowSreial(String showSreial) {
	this.showSreial = showSreial;
}

public List<Integer> getThArray() {
	return thArray;
}

public void setThArray(List<Integer> thArray) {
	this.thArray = thArray;
}

public String getTableId() {
	return tableId;
}

public void setTableId(String tableId) {
	this.tableId = tableId;
}

public String getIsCurrentDepartment() {
	return isCurrentDepartment;
}

public void setIsCurrentDepartment(String isCurrentDepartment) {
	this.isCurrentDepartment = isCurrentDepartment;
}

public String getMonitorType() {
	return monitorType;
}

public void setMonitorType(String monitorType) {
	this.monitorType = monitorType;
}

public String getIsEdit() {
	return isEdit;
}

public void setIsEdit(String isEdit) {
	this.isEdit = isEdit;
}

public String getCalTimeType() {
	return calTimeType;
}

public void setCalTimeType(String calTimeType) {
	this.calTimeType = calTimeType;
}

public List<MonitorField> getMonitorFields() {
	return monitorFields;
}

public void setMonitorFields(List<MonitorField> monitorFields) {
	this.monitorFields = monitorFields;
}

public List<MonitorField> getComponentIds() {
	return componentIds;
}

public void setComponentIds(List<MonitorField> componentIds) {
	this.componentIds = componentIds;
}

public String getMonitorTempFields() {
	return monitorTempFields;
}

public void setMonitorTempFields(String monitorTempFields) {
	this.monitorTempFields = monitorTempFields;
}

public String getRelateModType() {
	return relateModType;
}

public void setRelateModType(String relateModType) {
	this.relateModType = relateModType;
}

public String getSerialNumId() {
	return serialNumId;
}

public void setSerialNumId(String serialNumId) {
	this.serialNumId = serialNumId;
}

public String getSerialNumRemark() {
	return serialNumRemark;
}

public void setSerialNumRemark(String serialNumRemark) {
	this.serialNumRemark = serialNumRemark;
}

public String getRelateTable() {
	return relateTable;
}

public void setRelateTable(String relateTable) {
	this.relateTable = relateTable;
}

public String getTableColumn() {
	return tableColumn;
}

public void setTableColumn(String tableColumn) {
	this.tableColumn = tableColumn;
}

public String getSysDataDic() {
	return sysDataDic;
}

public void setSysDataDic(String sysDataDic) {
	this.sysDataDic = sysDataDic;
}

public String getMoneyColumn() {
	return moneyColumn;
}

public void setMoneyColumn(String moneyColumn) {
	this.moneyColumn = moneyColumn;
}

public String getMoneyTempColumn() {
	return moneyTempColumn;
}

public void setMoneyTempColumn(String moneyTempColumn) {
	this.moneyTempColumn = moneyTempColumn;
}


}
