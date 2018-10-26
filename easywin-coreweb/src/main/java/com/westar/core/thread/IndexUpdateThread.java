package com.westar.core.thread;

import java.util.List;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.IndexDoc;
import com.westar.core.service.IndexService;

public class IndexUpdateThread implements Runnable {
	
	private static final Log loger = LogFactory.getLog(IndexUpdateThread.class);

	/**
	 * 操作索引service
	 */
	private IndexService indexService;
	/**
	 * 所需创建索引Document集合
	 */
	private List<IndexDoc> listIndexDoc;
	/**
	 * 索引库索引主键
	 */
	private String index_key;
	/**
	 * 操作分类
	 */
	private String operateType;
	/**
	 * 当前操作人信息
	 */
	private UserInfo userInfo;

	/**
	 * 含参构造函数
	 * @param operateType
	 * @param indexService
	 * @param userInfo
	 * @param listIndexDoc
	 * @param index_key
	 */
	public IndexUpdateThread(String operateType, IndexService indexService,
			UserInfo userInfo, List<IndexDoc> listIndexDoc,String index_key) {
		this.operateType = operateType;
		this.indexService = indexService;
		this.userInfo = userInfo;
		this.listIndexDoc = listIndexDoc;
		this.index_key = index_key;
	}

	@Override
	public void run() {
		try {
			if("add".equals(operateType)){
				//只是添加索引
				indexService.addIndex2_0(listIndexDoc, userInfo);
			}else if("del".equals(operateType)){
				//根据主键删除索引
				indexService.delIndex2_0(index_key, userInfo);
			}else if("update".equals(operateType)){
				//根据主键更新索引（更新）
				indexService.updateIndex2_0(index_key, listIndexDoc, userInfo);
			}
		} catch (Exception e) {
			loger.info("子线程更新索引库失败!");
		}
	}

}
