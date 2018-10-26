package com.westar.core.web;

public class PaginationContext {
	
	private static int DEFAULT_PAGE_SIZE = 15;
	
	/**
	 * 记录起始索引位置
	 */
	private static ThreadLocal<Integer> offset = new ThreadLocal<Integer>();
	
	/**
	 * 每页显示多少条记录
	 */
	private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	
	/**
	 * 总记录数
	 */
	private static ThreadLocal<Integer> totalCount = new ThreadLocal<Integer>();
	
	public static void setOffset(int _offset){
		offset.set(_offset);
	}

	public static int getOffset(){
		Integer _offset=offset.get();
		if(_offset==null){
			_offset=0;
		}
		return _offset;
	}
	
	public static void removeOffset(){
		offset.remove();
	}
	
	public static void setPageSize(int _pageSize){
		pageSize.set(_pageSize);
	}
	
	public static int getPageSize(){
		Integer _pageSize=pageSize.get();
		if(_pageSize==null){
			_pageSize=DEFAULT_PAGE_SIZE;
		}
		return _pageSize;
	}
	
	public static void removePageSize(){
		pageSize.remove();
	}
	
	public static void setTotalCount(int _totalCount){
		totalCount.set(_totalCount);
	}
	
	public static int getTotalCount(){
		Integer _totalCount=totalCount.get();
		if(_totalCount==null){
			_totalCount=0;
		}
		return _totalCount;
	}
	
	public static void removeTotalCount(){
		totalCount.remove();
	}
}
