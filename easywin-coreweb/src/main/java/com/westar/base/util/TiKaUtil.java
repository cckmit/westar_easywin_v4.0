package com.westar.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class TiKaUtil {
	 public static String parseFile(File file){  
	        Parser parser = new AutoDetectParser();  
	        InputStream input = null;  
	        try{  
	            Metadata metadata = new Metadata();  
	            metadata.set(Metadata.CONTENT_ENCODING, "utf-8");  
	            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());  
	            input = new FileInputStream(file); 
	            ContentHandler handler = new BodyContentHandler(-1);//���ļ�����100000ʱ��new BodyContentHandler(1024*1024*10);   
	            ParseContext context = new ParseContext();  
	            context.set(Parser.class,parser);  
	            //TODO 文件过大会使得内存溢出
	            parser.parse(input,handler, metadata,context);  
	            return handler.toString();  
	        }catch (Exception e){  
	            e.printStackTrace();  
	        }finally {  
	            try {  
	                if(input!=null){
	                	input.close();  
	                }
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return null;  
	  
	    }  
	  
	    public static void main(String argt0[])throws Exception{  
	        parseFile(new File("E:\\xinjinJIaotong.rar")); 
	    } 
}
