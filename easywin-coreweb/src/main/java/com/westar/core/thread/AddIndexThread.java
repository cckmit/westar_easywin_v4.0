package com.westar.core.thread;


import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.base.model.UserInfo;
import com.westar.base.util.LuceneManager;

public class AddIndexThread implements Runnable {
	
	private static final Log loger = LogFactory.getLog(AddIndexThread.class);

	//需添加索引对象
	private Document doc;
	//当前操作用户信息
	private UserInfo userInfo;
	
	//含参构造函数
	public AddIndexThread(UserInfo userInfo,Document doc){
		this.doc = doc;
		this.userInfo = userInfo;
	}
	
	@Override
	public void run()  {
		try {
			//创建IndexWriter对象
			IndexWriter indexWriter = LuceneManager.getInstance().getIndexWriter(userInfo);
			//写入IndexWriter
			indexWriter.addDocument(doc);
			//查看IndexWriter里面有多少个索引
			loger.info("numDocs:"+indexWriter.numDocs());
			indexWriter.close();
		} catch (Exception e) {
			loger.info("出错数据:企业编号:"+doc.get("comId")+" 名称:"+doc.get("busName")+" 类型:"+doc.get("busType")+" 业务主键:"+doc.get("busId"));
		}
	}

}
