package com.westar.base.util;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.westar.base.model.UserInfo;

public class LuceneManager {
	
	//私有的默认构造子  
    private LuceneManager(){} 
    //已经自行实例化   
    private static final LuceneManager SINGLE = new LuceneManager();  
    //静态工厂方法   
    public static LuceneManager getInstance() {  
        return SINGLE;  
    }
    
    /**
     * 初始化IndexWriter对象
     * 
     * @param userInfo 根据对象属性创建索引库路径
     * @return
     * @throws IOException 
     * @throws LockObtainFailedException 
     * @throws CorruptIndexException 
     */
    public synchronized IndexWriter getIndexWriter(UserInfo userInfo) throws CorruptIndexException, LockObtainFailedException, IOException{
    	//索引库是否存在
		boolean noHave = false;
		// 添加
		String basepath = FileUtil.getUploadBasePath();
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdir();
		}
		String path = File.separator + "indexes"+File.separator+userInfo.getComId();
		/* 所有索引都保存到indexes 不存在则新增文件夹 */
		f = new File(basepath + path);
		if (!f.exists()) {
			noHave = true;
			f.mkdirs();
		}
		//保存索引文件的地方
		String indexDir = basepath + path;
        Directory dir = new SimpleFSDirectory(new File(indexDir));
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, new IKAnalyzer(true));  
        if(noHave){
        	iwc.setOpenMode(OpenMode.CREATE);
        }else{
        	iwc.setOpenMode(OpenMode.APPEND);
        }
		//创建IndexWriter对象,第一个参数是Directory,第二个是分词器,第三个表示是否是创建,如果为false为在此基础上面修改,第四表示表示分词的最大值，比如说new
		//MaxFieldLength(2)，就表示两个字一分，一般用IndexWriter.MaxFieldLength.LIMITED
        IndexWriter writer = new IndexWriter(dir,iwc);
        return writer;
    }
    
    /**
     * 初始化IndexSearcher对象
     * 
     * @param userInfo 根据对象属性创建索引库路径
     * @return
     * @throws IOException 
     * @throws CorruptIndexException 
     */
    public synchronized IndexReader getIndexReader(UserInfo userInfo) throws CorruptIndexException, IOException{
    	String basepath = FileUtil.getUploadBasePath();
		File f = new File(basepath);
		if (!f.exists()) {
			return null;
		}
		String path = File.separator + "indexes"+File.separator+userInfo.getComId();
		/* 所有索引都保存到indexes 不存在返回null */
		f = new File(basepath + path);
		if (!f.exists()) {
			return null;
		}
		//保存索引文件的地方
		String indexDir = basepath + path;
        Directory dir = new SimpleFSDirectory(new File(indexDir));
		IndexReader reader = DirectoryReader.open(dir);
        return reader;
    }
}
