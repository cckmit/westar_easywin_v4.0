package com.westar.core.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.IndexView;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.LuceneManager;
import com.westar.core.web.PaginationContext;

@Service
public class IndexService {
	private static final Log loger = LogFactory.getLog(IndexService.class);
	@Autowired
	UserInfoService userInfoService;

	/**
	 * 为文档对象创建索引
	 * @param indexDoc
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean addIndex2_0(List<IndexDoc> listIndexDoc,UserInfo userInfo) throws Exception{
		if(null==listIndexDoc) {
			return false;
		}
		boolean flag = true;
		try {
			//创建IndexWriter对象
			IndexWriter indexWriter = LuceneManager.getInstance().getIndexWriter(userInfo);
			Document doc = new Document();
			//创建Field对象，并放入doc对象中
			for(IndexDoc vo:listIndexDoc){
				if(IndexDoc.STRING.equals(vo.getType())){
					doc.add(new StringField(vo.getColName(),vo.getColValue(),(vo.getStore()?Store.YES:Store.NO)));
					doc.add(new StringField(vo.getColName(),vo.getColValue(),(vo.getStore()?Store.YES:Store.NO)));
				}else if(IndexDoc.TEXT.equals(vo.getType())){
					doc.add(new TextField(vo.getColName(),vo.getColValue(),(vo.getStore()?Store.YES:Store.NO)));
				}else if(IndexDoc.INT.equals(vo.getType())){
					doc.add(new IntField(vo.getColName(),Integer.parseInt(vo.getColValue()),(vo.getStore()?Store.YES:Store.NO)));
				}else if(IndexDoc.LONG.equals(vo.getType())){
					doc.add(new LongField(vo.getColName(),Long.parseLong(vo.getColValue()),(vo.getStore()?Store.YES:Store.NO)));
				}
			}
			//写入IndexWriter
			indexWriter.addDocument(doc);
			indexWriter.close();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			throw e;
		}
		
		return flag;
	}
	/**
	 * 索引更新
	 * @param index_key 索引主键
	 * @param listIndexDoc 更新内容
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateIndex2_0(String index_key,List<IndexDoc> listIndexDoc,UserInfo userInfo) throws Exception{
		if(null==listIndexDoc){
			return false;
		}
		boolean flag = true;
		try {
			//创建IndexWriter对象
			IndexWriter indexWriter = LuceneManager.getInstance().getIndexWriter(userInfo);
			Document doc = new Document();
			//创建Field对象，并放入doc对象中
			for(IndexDoc vo:listIndexDoc){
				if(IndexDoc.STRING.equals(vo.getType())){
					doc.add(new StringField(vo.getColName(),vo.getColValue(),(vo.getStore()?Store.YES:Store.NO)));
					doc.add(new StringField(vo.getColName(),vo.getColValue(),(vo.getStore()?Store.YES:Store.NO)));
				}else if(IndexDoc.TEXT.equals(vo.getType())){
					doc.add(new TextField(vo.getColName(),vo.getColValue(),(vo.getStore()?Store.YES:Store.NO)));
				}else if(IndexDoc.INT.equals(vo.getType())){
					doc.add(new IntField(vo.getColName(),Integer.parseInt(vo.getColValue()),(vo.getStore()?Store.YES:Store.NO)));
				}else if(IndexDoc.LONG.equals(vo.getType())){
					doc.add(new LongField(vo.getColName(),Long.parseLong(vo.getColValue()),(vo.getStore()?Store.YES:Store.NO)));
				}
			}
			//写入IndexWriter
			indexWriter.updateDocument(new Term("index_key", index_key), doc);
			indexWriter.close();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			throw e;
		}
		
		return flag;
	}
	/**
	 * 在企业内部检索
	 * @param userInfo
	 * @param searchStr
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<IndexView> searchIndexInCom(UserInfo userInfo,String searchStr) throws CorruptIndexException,IOException, java.text.ParseException, ParseException{
		List<IndexView> listIndexVo =null;
		try {
			IndexReader reader = LuceneManager.getInstance().getIndexReader(userInfo);
			//创建 IndexSearcher对象
	        IndexSearcher indexSearch=new IndexSearcher(reader);//检索工具  
			BooleanQuery bQuery = new BooleanQuery();
			//权限限制范围查询sharerId
			String[] strs = new String[]{userInfo.getComId().toString(),searchStr,userInfo.getId().toString()};
			String[] fields = new String[]{"comId","content","sharerId"};
			Occur[] flags = new Occur[]{Occur.MUST,Occur.MUST,Occur.MUST};
			Analyzer analyzer = new IKAnalyzer(true);
			Query query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			
			bQuery.add(query,Occur.SHOULD);
			//非权限范围查询
			strs = new String[]{userInfo.getComId().toString(),searchStr,ConstantInterface.TYPE_QUES};
			fields = new String[]{"comId","content","busType"};
			flags = new Occur[]{Occur.MUST,Occur.MUST,Occur.MUST};
			analyzer = new IKAnalyzer(true);
			query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			bQuery.add(query,Occur.SHOULD);
			
			//构建查询query
			//需要比对字段
			fields = new String[]{"comId","content","grpId"};
			//字段间查询关系
			flags = new Occur[]{Occur.MUST,Occur.MUST,Occur.MUST};
			//生成解析器
			analyzer = new IKAnalyzer(true);
			//查询字段对应值
			//所有人分组
			strs = new String[]{userInfo.getComId().toString(),searchStr,"all"};
			//生成查询对象query
			query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			//加入BooleanQuery对象
			bQuery.add(query,Occur.SHOULD);
			//个人分组
			strs = new String[]{userInfo.getComId().toString(),searchStr,"self_"+userInfo.getId()};
			//生成查询对象query
			query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			//加入BooleanQuery对象
			bQuery.add(query,Occur.SHOULD);
			//当前登录人所在分组query对象生成
			//获取当前登录人所在的分组
			List<SelfGroup> listSelfGroupIn = userInfoService.listSelfGroupUserIn(userInfo.getComId(),userInfo.getId());
			if(null!=listSelfGroupIn && !listSelfGroupIn.isEmpty()){
				for(SelfGroup vo:listSelfGroupIn){
					//所在分组
					strs = new String[]{userInfo.getComId().toString(),searchStr,vo.getId().toString()};
					//生成查询对象query
					query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
					//加入BooleanQuery对象
					bQuery.add(query,Occur.SHOULD);
				}
			}
			if (indexSearch!=null) {
				//结果集排序
				Sort sort = new Sort(new SortField("createDate", SortField.Type.STRING_VAL, true));// false升序true降序
				//根据索引主键去重
//				DuplicateFilter filter = new DuplicateFilter("index_key");//作为是否重复的判断依据field
				//多query查询结果
//				TopDocs hits = indexSearch.search(bQuery,filter,100);
				TopDocs hits = indexSearch.search(bQuery,100,sort);
				//hits.totalHits表示一共搜到多少个
				if(null!=hits && hits.totalHits>0){
					//生成IndexVo结果集
					listIndexVo = this.document2Vo(hits, indexSearch);
				}
			}
			reader.close();
		} catch (CorruptIndexException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} 
		return listIndexVo;
	}
	/**
	 * 根据输入字符串，在整个团队下面筛选，不进行权限验证
	 * @param userInfo
	 * @param searchStr
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws java.text.ParseException
	 * @throws ParseException
	 */
	public List<IndexView> searchIndexInComV2(UserInfo userInfo,String searchStr) throws CorruptIndexException,IOException, java.text.ParseException, ParseException{
		if(null==searchStr || "".equals(searchStr.trim())){return null;}
		List<IndexView> listIndexVo =null;
		IndexReader reader = LuceneManager.getInstance().getIndexReader(userInfo);
		try {
			if(null==reader){
				return null;
			}
			//创建 IndexSearcher对象
	        IndexSearcher indexSearch=new IndexSearcher(reader);//检索工具  
			BooleanQuery bQuery = new BooleanQuery();  
			
			String[] strs = new String[]{userInfo.getComId().toString(),searchStr};
			String[] fields = new String[]{"comId","content"};
			Occur[] flags = new Occur[]{Occur.MUST,Occur.MUST};
			Analyzer analyzer = new IKAnalyzer(true);
			Query query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			bQuery.add(query,Occur.SHOULD);
			
			strs = new String[]{userInfo.getComId().toString(),searchStr};
			fields = new String[]{"comId","busName"};
			flags = new Occur[]{Occur.MUST,Occur.MUST};
			query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			bQuery.add(query,Occur.SHOULD);
			
			if (indexSearch!=null) {
				//结果集排序
				Sort sort = new Sort(new SortField(null, SortField.Type.DOC, true));// false升序true降序
				//根据索引主键去重
//				DuplicateFilter filter = new DuplicateFilter("index_key");//作为是否重复的判断依据field
				//多query查询结果
//				TopDocs hits = indexSearch.search(bQuery,filter,100);
//				TopDocs hits = indexSearch.search(bQuery,10,sort);
				ScoreDoc before = null;
				if(PaginationContext.getOffset() > 0){
//					TopDocs docsBefore = indexSearch.search(query, (PaginationContext.getOffset()-1)*PaginationContext.getPageSize());
					TopDocs docsBefore = indexSearch.search(bQuery,PaginationContext.getOffset(),sort);
					ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
					if(scoreDocs.length > 0){
						before = scoreDocs[scoreDocs.length - 1];
					}
				}
//				TopDocs hits = indexSearch.searchAfter(before,bQuery,PaginationContext.getPageSize());
				TopDocs hits = indexSearch.searchAfter(before, bQuery,PaginationContext.getPageSize(),sort);
				PaginationContext.setTotalCount(hits.totalHits);
				//hits.totalHits表示一共搜到多少个
				if(null!=hits && hits.totalHits>0){
					//生成IndexVo结果集
					listIndexVo = this.document2Vo(hits, indexSearch);
				}
			}
		} catch (CorruptIndexException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally{
			if(null==reader){
				return null;
			}
			reader.close();
		}
		return listIndexVo;
	}
	/**
	 * 在个人权限内检索
	 * @param userInfo
	 * @param busType
	 * @param searchStr
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<IndexView> searchIndexInPersion(UserInfo userInfo,String busType,String searchStr) throws CorruptIndexException,IOException,ParseException, java.text.ParseException{
		List<IndexView> listIndexVo =null;
		try {
			IndexReader reader = LuceneManager.getInstance().getIndexReader(userInfo);
			//创建 IndexSearcher对象
	        IndexSearcher indexSearch=new IndexSearcher(reader);//检索工具  
	        if(null!=indexSearch){
			    BooleanQuery bQuery = new BooleanQuery();
				//权限限制范围查询sharerId
				String[] strs = new String[]{userInfo.getComId().toString(),busType,searchStr,userInfo.getId().toString()};
				String[] fields = new String[]{"comId","busType","content","sharerId"};
				Occur[] flags = new Occur[]{Occur.MUST,Occur.MUST,Occur.MUST,Occur.MUST};
				Analyzer analyzer = new IKAnalyzer(true);
				Query query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
				bQuery.add(query,Occur.SHOULD);

				//结果集排序
				Sort sort = new Sort(new SortField("createDate", SortField.Type.STRING_VAL, true));// false升序true降序
				//根据索引主键去重
//				DuplicateFilter filter = new DuplicateFilter("index_key");//作为是否重复的判断依据field
				//多query查询结果
//				TopDocs hits = indexSearch.search(bQuery,filter,100);
//				TopDocs hits = indexSearch.search(bQuery,filter,100, sort);
				TopDocs hits = indexSearch.search(bQuery,100,sort);
				//hits.totalHits表示一共搜到多少个
				if(null!=hits && hits.totalHits>0){
					//生成IndexVo结果集
					listIndexVo = this.document2Vo(hits, indexSearch);
				}
	        }
			reader.close();
		} catch (CorruptIndexException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return listIndexVo;
	}
	/**
	 * 以组范围内索引检索
	 * @param userInfo
	 * @param busType
	 * @param searchStr
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<IndexView> searchIndexInGroup(UserInfo userInfo,String busType,String searchStr) throws CorruptIndexException,IOException,ParseException, java.text.ParseException{
		List<IndexView> listIndexVo =null;
		try {
			IndexReader reader = LuceneManager.getInstance().getIndexReader(userInfo);
			//创建 IndexSearcher对象
	        IndexSearcher indexSearch=new IndexSearcher(reader);//检索工具  
	        if(null!=indexSearch){
			    BooleanQuery bQuery = new BooleanQuery();
			    //组织范围界定
			    QueryParser queryParser = new QueryParser("comId", new IKAnalyzer(true));// 实例查询条件类
			    Query query = queryParser.parse(userInfo.getComId().toString());
			    bQuery.add(query, Occur.MUST);
			    //业务类型界定
			    queryParser = new QueryParser("busType", new IKAnalyzer(true));// 实例查询条件类
			    query = queryParser.parse(busType);
			    bQuery.add(query, Occur.MUST);
			    //查询字符串比对
				queryParser = new QueryParser("content", new IKAnalyzer(true));// 实例查询条件类
			    query = queryParser.parse(searchStr);
			    bQuery.add(query, Occur.MUST);
			    //所有人分组界定
			    queryParser = new QueryParser("grpId", new IKAnalyzer(true));// 实例查询条件类
			    query = queryParser.parse("all");
			    bQuery.add(query, Occur.SHOULD);
			    //所有人分组界定
			    queryParser = new QueryParser("grpId", new IKAnalyzer(true));// 实例查询条件类
			    query = queryParser.parse("self_"+userInfo.getId());
			    bQuery.add(query, Occur.SHOULD);
				//当前登录人所在分组query对象生成
				//获取当前登录人所在的分组
				List<SelfGroup> listSelfGroupIn = userInfoService.listSelfGroupUserIn(userInfo.getComId(),userInfo.getId());
				if(null!=listSelfGroupIn && !listSelfGroupIn.isEmpty()){
					for(SelfGroup vo:listSelfGroupIn){
						//所在分组
					    queryParser = new QueryParser("grpId", new IKAnalyzer(true));// 实例查询条件类
					    query = queryParser.parse(vo.getId().toString());
					  //加入BooleanQuery对象
					    bQuery.add(query, Occur.SHOULD);
					}
				}
				//结果集排序
				Sort sort = new Sort(new SortField("createDate", SortField.Type.STRING_VAL, true));// false升序true降序
//				//根据索引主键去重
//				DuplicateFilter filter = new DuplicateFilter("index_key");//作为是否重复的判断依据field
//				filter.setKeepMode(DuplicateFilter.KeepMode.KM_USE_FIRST_OCCURRENCE);
//				filter.setProcessingMode(DuplicateFilter.ProcessingMode.PM_FAST_INVALIDATION);
				//多query查询结果
				TopDocs hits = indexSearch.search(bQuery,100,sort);
//				TopDocs hits = indexSearch.search(bQuery,filter,100, sort);
				//hits.totalHits表示一共搜到多少个
				if(null!=hits && hits.totalHits>0){
					//生成IndexVo结果集
					listIndexVo = this.document2Vo(hits, indexSearch);}
	        }
			reader.close();
		} catch (CorruptIndexException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		}
		return listIndexVo;
	}
	/**
	 * 在业务模块下检索
	 * @param userInfo
	 * @param busType
	 * @param searchStr
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<IndexView> searchIndexInModule(UserInfo userInfo,String busType,String searchStr) throws CorruptIndexException,IOException,ParseException, java.text.ParseException{
		if(null==searchStr || "".equals(searchStr.trim())){return null;}
		List<IndexView> listIndexVo =null;
		IndexReader reader = LuceneManager.getInstance().getIndexReader(userInfo);
		try {
			if(null==reader) {
				return null;
			}
			//创建 IndexSearcher对象
	        IndexSearcher indexSearch=new IndexSearcher(reader);//检索工具  
//			//创建QueryParser对象,第一个参数表示Lucene的版本,第二个表示搜索Field的字段,第三个表示搜索使用分词器
//			QueryParser queryParser = new MultiFieldQueryParser(,new String[]{"sharerName","content"},new StandardAnalyzer());
//			//生成Query对象
//			BooleanQuery m_BooleanQuery = new BooleanQuery();
//			Query query = queryParser.parse(searchStr);
//			m_BooleanQuery.add(query, Occur.MUST);
//			//搜索结果 TopDocs里面有scoreDocs[]数组，里面保存着索引值
//			TopDocs hits = indexSearch.search(m_BooleanQuery,5);
	        if(null!=indexSearch){
			    BooleanQuery bQuery = new BooleanQuery();
			    
			    //组织范围界定
			    QueryParser queryParser = new QueryParser("comId", new IKAnalyzer(true));// 实例查询条件类
			    Query query = queryParser.parse(userInfo.getComId().toString());
			    bQuery.add(query, Occur.MUST);
			    if(null!=busType && !"".equals(busType.trim())){
				    //业务类型界定
				    queryParser = new QueryParser("busType", new IKAnalyzer(true));// 实例查询条件类
				    query = queryParser.parse(busType);
				    bQuery.add(query, Occur.MUST);
			    }
			    //查询字符串比对
				queryParser = new QueryParser("content", new IKAnalyzer(true));// 实例查询条件类
			    query = queryParser.parse(searchStr);
			    bQuery.add(query, Occur.MUST);
			    //查询字符串比对
//				queryParser = new QueryParser("busName", new IKAnalyzer(true));// 实例查询条件类
//			    query = queryParser.parse(searchStr);
//			    bQuery.add(query, Occur.SHOULD);
				//结果集排序
				Sort sort = new Sort(new SortField(null, SortField.Type.DOC, true));// false升序true降序
//				TopDocs hits = indexSearch.search(bQuery,100,sort);
				ScoreDoc before = null;
				if(PaginationContext.getOffset() > 0){
//					TopDocs docsBefore = indexSearch.search(query, (PaginationContext.getOffset()-1)*PaginationContext.getPageSize());
					TopDocs docsBefore = indexSearch.search(bQuery,PaginationContext.getOffset(),sort);
					ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
					if(scoreDocs.length > 0){
						before = scoreDocs[scoreDocs.length - 1];
					}
				}
//				TopDocs hits = indexSearch.searchAfter(before,bQuery,PaginationContext.getPageSize());
				TopDocs hits = indexSearch.searchAfter(before, bQuery,PaginationContext.getPageSize(),sort);
				PaginationContext.setTotalCount(hits.totalHits);
				
				//hits.totalHits表示一共搜到多少个
				if(null!=hits && hits.totalHits>0){
					//生成IndexVo结果集
					listIndexVo = this.document2Vo(hits, indexSearch);
				}
	        }
		} catch (CorruptIndexException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		}finally{
			if(null==reader) {
				return null;
			}
			reader.close();
		}
		return listIndexVo;
	}
	/**
	 * 在附件管理中心下进行附件索引查询
	 * @param userInfo
	 * @param searchStr
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<IndexView> searchIndexInFileCenter(UserInfo userInfo,String searchStr) throws CorruptIndexException,IOException,ParseException, java.text.ParseException{
		List<IndexView> listIndexVo =null;
		try {
			IndexReader reader = LuceneManager.getInstance().getIndexReader(userInfo);
			//创建 IndexSearcher对象
	        IndexSearcher indexSearch=new IndexSearcher(reader);//检索工具  
	        if(null!=indexSearch){
			    BooleanQuery bQuery = new BooleanQuery();
			    //组织范围界定
			    QueryParser queryParser = new QueryParser("comId", new IKAnalyzer(true));// 实例查询条件类
			    Query query = queryParser.parse(userInfo.getComId().toString());
			    bQuery.add(query, Occur.MUST);
			    //索引类型界定
			    queryParser = new QueryParser("dataType", new IKAnalyzer(true));// 实例查询条件类
			    query = queryParser.parse("upfile");
			    bQuery.add(query, Occur.MUST);
			    //查询字符串比对
				queryParser = new QueryParser("content", new IKAnalyzer(true));// 实例查询条件类
			    query = queryParser.parse(searchStr);
			    bQuery.add(query, Occur.MUST);
				//结果集排序
				Sort sort = new Sort(new SortField("createDate", SortField.Type.STRING_VAL, true));// false升序true降序
				TopDocs hits = indexSearch.search(bQuery,100,sort);
				//hits.totalHits表示一共搜到多少个
				if(null!=hits && hits.totalHits>0){
					//生成IndexVo结果集
					listIndexVo = this.document2Vo(hits, indexSearch);
				}
	        }
			reader.close();
		} catch (CorruptIndexException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		}
		return listIndexVo;
	}
	/**
	 * 删除索引库中对应索引
	 * @param index_key
	 * @param userInfo
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public boolean delIndex2_0(String index_key,UserInfo userInfo) throws CorruptIndexException, IOException{
		if(null==index_key || "".equals(index_key)){
			return false;
		}
		boolean flag = true;
		try {
			IndexWriter indexWriter = LuceneManager.getInstance().getIndexWriter(userInfo);
			// 删除filename为time.txt的Document
			indexWriter.deleteDocuments(new Term("index_key",index_key));
			// 优化
			indexWriter.forceMerge(1);
			// 提交事务
			indexWriter.commit();
			//loger.info("是否有删除=" + indexWriter.hasDeletions());
			// 如果不indexWriter.optimize()以下两个会有区别
			//loger.info("一共有" + indexWriter.maxDoc() + "索引");
			//loger.info("还剩" + indexWriter.numDocs() + "索引");
			indexWriter.close();
		} catch (CorruptIndexException e) {
			flag = false;
			throw e;
		} catch(IOException e){
			flag = false;
			throw e;
		}
		return flag;
	}
	/**
	 * 删除依据组ID创建的索引
	 * @param userInfo
	 * @param grpId
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	public boolean delIndexByGrpId2_0(UserInfo userInfo,Integer grpId) throws CorruptIndexException, IOException, ParseException{
		if(null==grpId || "".equals(grpId)) {
			return false;
		}
		boolean flag = true;
		try {
			IndexWriter indexWriter = LuceneManager.getInstance().getIndexWriter(userInfo);
			//需要比对字段
			String[] fields = new String[]{"comId","grpId"};
			//字段间查询关系
			Occur[] flags = new Occur[]{Occur.MUST,Occur.MUST};
			//生成解析器
			Analyzer analyzer = new IKAnalyzer(true);
			//查询字段对应值
			//所有人分组
			String[] strs = new String[]{userInfo.getComId().toString(),grpId.toString()};
			//生成查询对象query
			Query query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			//删除满足查询条件的结果集
			indexWriter.deleteDocuments(query);
			// 优化
			indexWriter.forceMerge(1);
			// 提交事务
			indexWriter.commit();
			//loger.info("是否有删除=" + indexWriter.hasDeletions());
			// 如果不indexWriter.optimize()以下两个会有区别
			//loger.info("一共有" + indexWriter.maxDoc() + "索引");
			//loger.info("还剩" + indexWriter.numDocs() + "索引");
			indexWriter.close();
		} catch (CorruptIndexException e) {
			flag = false;
			throw e;
		} catch(IOException e){
			flag = false;
			throw e;
		}
		return flag;
	}
	/**
	 * 根据附件主键以及模块类型删除附件索引
	 * @param userInfo
	 * @param busId 业务主键
	 * @param busType 业务类型
	 * @param upfileId 附件主键
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	public boolean delUpfileIndex2_0(UserInfo userInfo,Integer upfileId,String busType,Integer busId) throws CorruptIndexException, IOException, ParseException{
		if((null==upfileId || upfileId==0) || (null==busId || busId==0) || 
				(null==busType || "".equals(busType.trim()))) {
			return false;
		}
		boolean flag = true;
		try {
			IndexWriter indexWriter = LuceneManager.getInstance().getIndexWriter(userInfo);
			//需要比对字段
			String[] fields = new String[]{"index_key","busType","busId"};
			//字段间查询关系
			Occur[] flags = new Occur[]{Occur.MUST,Occur.MUST,Occur.MUST};
			//生成解析器
			Analyzer analyzer = new IKAnalyzer(true);
			//查询字段对应值
			//所有人分组
			String[] strs = new String[]{userInfo.getComId() +"_"+ConstantInterface.TYPE_FILE+"_"+upfileId,busType,busId.toString()};
			//生成查询对象query
			Query query = MultiFieldQueryParser.parse(strs,fields,flags,analyzer);
			//删除满足查询条件的结果集
			indexWriter.deleteDocuments(query);
			// 优化
			indexWriter.forceMerge(1);
			// 提交事务
			indexWriter.commit();
			//loger.info("删除条件:"+"ID:"+userInfo.getComId() +"_"+BusinessTypeConstant.type_file+"_"+upfileId+" busType:"+busType+" busId: "+busId.toString());
			//loger.info("delUpfileIndex 是否有删除=" + indexWriter.hasDeletions());
			// 如果不indexWriter.optimize()以下两个会有区别
			//loger.info("delUpfileIndex 一共有" + indexWriter.maxDoc() + "索引");
			//loger.info("delUpfileIndex 还剩" + indexWriter.numDocs() + "索引");
			indexWriter.close();
		} catch (CorruptIndexException e) {
			flag = false;
			throw e;
		} catch(IOException e){
			flag = false;
			throw e;
		}
		return flag;
	}
	/**
	 * 查询结果转换成IndexVo集合
	 * @param hits
	 * @param indexSearch
	 * @return
	 * @throws IOException
	 * @throws java.text.ParseException
	 */
	private List<IndexView> document2Vo(TopDocs hits,IndexSearcher indexSearch) throws IOException, java.text.ParseException{
		List<IndexView> listIndexVo = null;
		if(null!=hits && hits.totalHits>0){
			listIndexVo = new ArrayList<IndexView>();
//			Map<String,IndexVo> indexVoMap = new java.util.Hashtable<String, IndexVo>();
			IndexView vo =null;
			//循环hits.scoreDocs数据，并使用indexSearch.doc方法把Document还原，再拿出对应的字段的值
			for (int i = 0; i < hits.scoreDocs.length; i++) {
				vo = new IndexView();
				ScoreDoc sdoc = hits.scoreDocs[i];
				Document doc = indexSearch.doc(sdoc.doc);
				vo.setId(doc.get("index_key"));
				vo.setComId(Integer.parseInt(doc.get("comId")));
				vo.setBusId(Integer.parseInt(doc.get("busId")));
				vo.setBusType(doc.get("busType"));
				
				String busName = doc.get("busName").replaceAll("\\n", "").replace("\\t", "").replace(" ", "");
				
				vo.setBusName(busName);
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date createDate = DateTools.stringToDate(doc.get("createDate"));
			    vo.setRecordCreateTime(formatter.format(createDate));
				vo.setCreateDate(null);
			    if("013".equals(doc.get("busType"))){
			    	if(null!=doc.get("relatedBusId") && !"".equals(doc.get("relatedBusId").trim())){
				    	vo.setRelatedBusId(Integer.parseInt(doc.get("relatedBusId")));
			    	}
			    	vo.setRelatedBusType(doc.get("relatedBusType"));
			    }
			    listIndexVo.add(vo);
				//放入MAP去重
//				indexVoMap.put(doc.get("index_key"),vo);
			}
//			if(null!=indexVoMap && !indexVoMap.isEmpty()){
//				listIndexVo = new ArrayList<IndexVo>();
//				Set<String> keys = indexVoMap.keySet();
//				for(String key : keys){
//					listIndexVo.add(indexVoMap.get(key));
//				}
//			}
		}
		return listIndexVo;
	}
}
