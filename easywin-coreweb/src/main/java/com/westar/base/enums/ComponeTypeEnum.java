package com.westar.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:表单组件类型
 * @author zzq
 * @date 2018年1月29日 下午2:37:26
 */
public enum ComponeTypeEnum {
	/**
	 * 文本框
	 */
	TEXT("文本框", "Text"),
	/**
	 * 多行文本
	 */
	TEXTAREA("多行文本", "TextArea"),
	/**
	 * 单选框
	 */
	RADIOBOX("单选框", "RadioBox"),
	/**
	 * 复选框
	 */
	CHECKBOX("复选框", "CheckBox"),
	/**
	 * 下拉菜单
	 */
	SELECT("下拉菜单", "Select"),
	/**
	 * 日期
	 */
	DATECOMPONENT("日期", "DateComponent"),
	/**
	 * 日期区间
	 */
	DATEINTERVAL("日期区间", "DateInterval"),
	/**
	 * 数字输入框
	 */
	NUMBERCOMPONENT("数字输入框", "NumberComponent"),
	/**
	 * 运算控件
	 */
	MONITOR("运算控件", "Monitor"),
	/**
	 * 用户选择
	 */
	EMPLOYEE("用户选择", "Employee"),
	/**
	 * 部门选择
	 */
	DEPARTMENT("部门选择", "Department"),
	/**
	 * 项目选择
	 */
	RELATEITEM("项目选择", "RelateItem"),
	/**
	 * 客户选择
	 */
	RELATECRM("客户选择", "RelateCrm"),
	/**
	 * 关联模块
	 */
	RELATEMOD("关联模块", "RelateMod"),
	/**
	 * 序列编号
	 */
	SERIALNUM("序列编号", "SerialNum"),
	/**
	 * 资料上传
	 */
	RELATEFILE("资料上传", "RelateFile"),
	/**
	 * 明细子表
	 */
	DATATABLE("明细子表", "DataTable");
	
	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;

	private ComponeTypeEnum(String desc, String value) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ComponeTypeEnum getEnum(String value) {
		ComponeTypeEnum resultEnum = null;
		ComponeTypeEnum[] enumAry = ComponeTypeEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue().equals(value)) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	public static Map<String, Map<String, Object>> toMap() {
		ComponeTypeEnum[] ary = ComponeTypeEnum.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", String.valueOf(ary[num].getValue()));
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		ComponeTypeEnum[] ary = ComponeTypeEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", String.valueOf(ary[i].getValue()));
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	/**
	 * 取枚举的json字符串
	 * 
	 * @return
	 */
	public static String getJsonStr() {
		ComponeTypeEnum[] enums = ComponeTypeEnum.values();
		StringBuffer jsonStr = new StringBuffer("[");
		for (ComponeTypeEnum senum : enums) {
			if (!"[".equals(jsonStr.toString())) {
				jsonStr.append(",");
			}
			jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("',value:'").append(senum.getValue()).append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
}
