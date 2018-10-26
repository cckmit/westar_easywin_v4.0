package com.westar.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.westar.base.pojo.IndexDoc;


public class IndexDocArrayList extends ArrayList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2417349464294946393L;
	
	public IndexDocArrayList(String[] newAttrs){
		List<IndexDoc> list = new ArrayList<IndexDoc>();
		//索引主键
		IndexDoc doc = new IndexDoc();
		doc.setColName("id");
		doc.setStore(true);
		doc.setIsIndex("index_NOT_ANALYZED");
		//业务主键
		doc = new IndexDoc();
		doc.setColName("busId");
		doc.setStore(true);
		doc.setIsIndex("index_NO");
		//业务类型
		doc = new IndexDoc();
		doc.setColName("busType");
		doc.setStore(true);
		doc.setIsIndex("index_NOT_ANALYZED");
		//业务名称
		doc = new IndexDoc();
		doc.setColName("busName");
		doc.setStore(true);
		doc.setIsIndex("index_NO");
		//业务URL
		doc = new IndexDoc();
		doc.setColName("url");
		doc.setStore(true);
		doc.setIsIndex("index_NO");
		//索引权限人
		doc = new IndexDoc();
		doc.setColName("sharerId");
		doc.setStore(true);
		doc.setIsIndex("index_NOT_ANALYZED");
		//索引内容
		doc = new IndexDoc();
		doc.setColName("content");
		doc.setStore(false);
		doc.setIsIndex("index_ANALYZED");
		//创建日期
		doc = new IndexDoc();
		doc.setColName("createDate");
		doc.setStore(true);
		doc.setIsIndex("index_NOT_ANALYZED");
		//创建返回list
		this.indexDocList(list, newAttrs);
	}
	/**
	 * 两数组合并
	 * @param first
	 * @param second
	 * @return
	 */
	public static String[] concat(String[] first, String[] second) {  
	  String[] result = Arrays.copyOf(first, first.length + second.length);  
	  System.arraycopy(second, 0, result, first.length, second.length);  
	  return result;  
	}
	private List<IndexDoc> indexDocList(List<IndexDoc> list,String[] newAttrs){
		if(null!=newAttrs && newAttrs.length>0){
			IndexDoc doc = null;
			for(String attr:newAttrs){
				doc = new IndexDoc();
				doc.setColName(attr);
				doc.setStore(true);
				doc.setIsIndex("index_NO");
				list.add(doc);
			}
		}
		return list;
	}
}
