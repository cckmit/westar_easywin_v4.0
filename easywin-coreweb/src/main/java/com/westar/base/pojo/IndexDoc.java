package com.westar.base.pojo;

/**
 * 
 * @author lj
 *
 */

/**
 * 创建索引时，必须含有字段如下
 * List<IndexDoc> listIndexDoc = new ArrayList<IndexDoc>();
		listIndexDoc = new ArrayList<IndexDoc>();
			//索引主键
			IndexDoc doc = new IndexDoc();
			doc.setColName("id");
			doc.setStore(true);
			doc.setIndex("index_NOT_ANALYZED");
			doc.setColValue(userInfo.getComId()+"_"+BusinessTypeConstant.type_item+"_"+id);
			listIndexDoc.add(doc);
			//企业编号
			doc = new IndexDoc();
			doc.setColName("comId");
			doc.setStore(true);
			doc.setIndex("index_NOT_ANALYZED");
			doc.setColValue(userInfo.getComId().toString());
			listIndexDoc.add(doc);
			//业务主键
			doc = new IndexDoc();
			doc.setColName("busId");
			doc.setStore(true);
			doc.setIndex("index_NO");
			doc.setColValue(NumericUtils.intToPrefixCoded(id));
			listIndexDoc.add(doc);
			//业务类型
			doc = new IndexDoc();
			doc.setColName("busType");
			doc.setStore(true);
			doc.setIndex("index_NOT_ANALYZED");
			doc.setColValue(BusinessTypeConstant.type_item);
			listIndexDoc.add(doc);
			//业务名称
			doc = new IndexDoc();
			doc.setColName("busName");
			doc.setStore(true);
			doc.setIndex("index_NO");
			doc.setColValue(item.getItemName());
			listIndexDoc.add(doc);
			//业务URL
			doc = new IndexDoc();
			doc.setColName("url");
			doc.setStore(true);
			doc.setIndex("index_NO");
			doc.setColValue("/crm/viewCustomer");
			listIndexDoc.add(doc);
			//创建日期
			doc = new IndexDoc();
			doc.setColName("createDate");
			doc.setStore(true);
			doc.setIndex("index_NOT_ANALYZED");
			doc.setColValue(DateTools.dateToString(new Date(),DateTools.Resolution.DAY));
			listIndexDoc.add(doc);
			//索引权限人
			doc = new IndexDoc();
			doc.setColName("sharerId");
			doc.setStore(true);
			doc.setIndex("index_NOT_ANALYZED");
			doc.setColValue(key.toString());
			listIndexDoc.add(doc);
			//索引内容
			doc = new IndexDoc();
			doc.setColName("content");
			doc.setStore(false);
			doc.setIndex("index_ANALYZED");
			doc.setColValue(attStr+userMap.get(key).getUserName()+";");
			listIndexDoc.add(doc);
 * 
 * 
 */
public class IndexDoc {

	//分词索引
	public static final String ANALYZED = "ANALYZED";
	//不分词索引
	public static final String NOT_ANALYZED ="NOT_ANALYZED";
	
	//类型
	public static final String STRING = "STRING";
	public static final String TEXT = "TEXT";
	public static final String INT = "INT";
	public static final String LONG = "LONG";
	
	//列名称
	private String colName;
	
	//列存储值
	//数据类型是数字或日期类型时需要用工具类NumericUtils、DateTools转为成相应字符串存储
	private String colValue;
	
	//是否存储
	private boolean store;
	
	//是否创建索引
	private String isIndex;
	
	private String type;

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public boolean getStore() {
		return store;
	}

	public void setStore(boolean store) {
		this.store = store;
	}

	public String getColValue() {
		return colValue;
	}
	//数据类型是数字或日期类型时需要用工具类NumericUtils、DateTools转为成相应字符串存储
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}

	public String getIsIndex() {
		return isIndex;
	}

	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
