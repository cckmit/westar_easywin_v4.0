package com.westar.core.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westar.base.model.DataDic;

public class DataDicContext {

	private static DataDicContext instance;

	private DataDicContext() {
	}

	/**
	 * 字典表所有记录
	 * 按树形保存
	 */
	private Map<String, Map<String, List<DataDic>>> mapTree;
	/**
	 * type下的字典表数据不按树形，全部保存到一个list里面
	 */
	private Map<String, List<DataDic>> mapList;
	/**
	 * 使得能够快速检索到一个具体的字典表字段
	 */
	private Map<String, Map<String, DataDic>> mapMap;

	public Map<String, Map<String, List<DataDic>>> getMapTree() {
		if (mapTree == null) {
			mapTree = new HashMap<String, Map<String, List<DataDic>>>();
		}
		return mapTree;
	}

	public void setMapTree(Map<String, Map<String, List<DataDic>>> mapTree) {
		this.mapTree = mapTree;
	}

	public Map<String, List<DataDic>> getMapList() {
		if (mapList == null) {
			mapList = new HashMap<String, List<DataDic>>();
		}
		return mapList;
	}

	public void setMapList(Map<String, List<DataDic>> mapList) {
		this.mapList = mapList;
	}

	public Map<String, Map<String, DataDic>> getMapMap() {
		if (mapMap == null) {
			mapMap = new HashMap<String, Map<String, DataDic>>();
		}
		return mapMap;
	}

	public void setMapMap(Map<String, Map<String, DataDic>> mapMap) {
		this.mapMap = mapMap;
	}

	public static DataDicContext getInstance() {
		if (instance == null) {
			instance = new DataDicContext();
		}
		return instance;
	}

	/**
	 * 根据类型获得字典数据树形列表
	 * @param type
	 * @return
	 */
	public List<DataDic> listTreeDataDicByType(String type) {
		List<DataDic> list = this.getMapList().get(type);
		return list;
	}

	/**
	 * 根据类型及父节点查询字典表数据
	 * @param type
	 * @param parentCode
	 * @return
	 */
	public List<DataDic> listDataDicByParent(String type, String parentCode) {
		Map<String, List<DataDic>> map = this.getMapTree().get(type);
		List<DataDic> list = map.get(parentCode);
		return list;
	}

	/**
	 * 根据类型和代码获取数据字典路径值
	 * @param type 类别
	 * @param code 字典代码
	 * @return
	 */
	public String getCurrentPathZvalue(String type, String code) {
		String pathZvalue = "";
		DataDic v = this.getMapMap().get(type).get(code);
		if (null != v) {
			pathZvalue = v.getZvalue();
		}
		return pathZvalue;
	}

	/**
	 * 根据类型和代码获取数据字典信息
	 * @param type  类别
	 * @param code  字典代码
	 * @return
	 */
	public DataDic getCurrentPathDataDict(String type, String code) {
		return this.getMapMap().get(type).get(code);
	}

}
