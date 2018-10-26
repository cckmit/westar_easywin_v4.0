package com.westar.core.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westar.base.model.BiaoQing;

public class BiaoQingContext {

	private static BiaoQingContext instance;

	private BiaoQingContext() {
	}

	/**
	 * 存放表情集合
	 */
	private List<BiaoQing> biaoQingList;
	/**
	 * 对象map
	 */
	private Map<String, BiaoQing> mapBiaoQing;
	
	public List<BiaoQing> getBiaoQingList(){
		if (biaoQingList == null) {
			biaoQingList = new ArrayList<BiaoQing>();
		}
		return biaoQingList;
	}

	public Map<String, BiaoQing> getMapBiaoQing() {
		if (mapBiaoQing == null) {
			mapBiaoQing = new HashMap<String, BiaoQing>();
		}
		return mapBiaoQing;
	}
	
	public static BiaoQingContext getInstance() {
		if (instance == null) {
			instance = new BiaoQingContext();
		}
		return instance;
	}
}
