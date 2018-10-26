package com.westar.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

public class Md5CaculateUtil {
	private static final Log loger = LogFactory.getLog(Md5CaculateUtil.class);
	private Md5CaculateUtil(){
		          
		      }
		      
		      private static char[] hexChar = {
		          '0','1','2','3','4','5','6','7','8','9',
		          'a','b','c','d','e','f'
		      };
		      
		      public static String getHash(String fileName,String hashType) throws IOException, NoSuchAlgorithmException{
		          
		          File f = new File(fileName);
		          loger.info(" -------------------------------------------------------------------------------");
		          loger.info("|��ǰ�ļ����:"+f.getName());
		          loger.info("|��ǰ�ļ���С:"+(f.length()/1024/1024)+"MB");
		          loger.info("|��ǰ�ļ�·��[���]:"+f.getAbsolutePath());
		          loger.info("|��ǰ�ļ�·��[---]:"+f.getCanonicalPath());
		          loger.info(" -------------------------------------------------------------------------------");
		          
		          InputStream ins = new FileInputStream(f);
		          
		          byte[] buffer = new byte[8192];
		          MessageDigest md5 = MessageDigest.getInstance(hashType);
		          
		          int len;
		          while((len = ins.read(buffer)) != -1){
		              md5.update(buffer, 0, len);
		          }
		  
		          ins.close();
		  //        Ҳ������apache�Դ�ļ���MD5����
		          return DigestUtils.md5Hex(md5.digest());
		  //        �Լ�д��ת����MD5����
		  //        return toHexString(md5.digest());
		      }
		      
		      public static String getHash2(String fileName){
		          File f = new File(fileName);
		          return String.valueOf(f.lastModified());
		      }
		      
		      
		      protected static String toHexString(byte[] b){
		          StringBuilder sb = new StringBuilder(b.length*2);
		          for(int i=0;i<b.length;i++){
		              sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
		              sb.append(hexChar[b[i] & 0x0f]);
		          }
		         return sb.toString();
		      }
		      
		      /*
		       * ��ȡMessageDigest֧�ּ��ּ����㷨
		       */
		      @SuppressWarnings({ "rawtypes", "unchecked" })
		      private static String[] getCryptolmpls(String serviceType){
		          
		          Set result = new HashSet();
		  //        all providers
		          Provider[] providers = Security.getProviders();
		          for(int i=0;i<providers.length;i++){
		  //            get services provided by each provider
		              Set keys = providers[i].keySet();
		              for(Iterator it = keys.iterator();it.hasNext();){
		                  String key = it.next().toString();
		                  key = key.split(" ")[0];
		                  
		                  if(key.startsWith(serviceType+".")){
		                      result.add(key.substring(serviceType.length()+1));
		                  }else if(key.startsWith("Alg.Alias."+serviceType+".")){
		                      result.add(key.substring(serviceType.length()+11));
		                  }
		              }
		          }
		          return (String[]) result.toArray(new String[result.size()]);
		      }
		      
		      
		      public static void main(String[] args) throws Exception, Exception {
		  //        ���÷���
		  //        String[] names = getCryptolmpls("MessageDigest");
		 //        for(String name:names){
		 //            loger.info(name);
		//        }
		         long start = System.currentTimeMillis();
		         loger.info("��ʼ�����ļ�MD5ֵ,���Ժ�...");
		         String fileName = "F:/oracleORG/win32_11gR2_database_1of2 (1).zip";
		 ////        String fileName = "E:\\SoTowerStudio-3.1.0.exe";
		         String hashType = "MD5";
		         String hash = getHash(fileName,hashType);
		         loger.info("MD5:"+hash);
//		         hash = getHash("F:\\22.docx",hashType);
//		         loger.info("MD5:"+hash);
		         long end = System.currentTimeMillis();
		         loger.info("һ����ʱ:"+(end-start)+"����");
		     }
}