package com.westar.base.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 * 附件转换为swf用于预览
 * @author H87
 *
 */
public class File2swfUtil {
	
	private static final Log log = LogFactory.getLog(File2swfUtil.class);
	
	private static  OfficeManager officeManager;
	
	private static String OFFICE_HOME;//openOffice安装路径
	
	private static String PDF2SWF_PATH;//swftools工具的安装路径
	
	private static Integer port = 8100;//端口
	
	private static  String osName = System.getProperty("os.name");
	//初始化服务
	public static void init(){
		if(null==OFFICE_HOME || PDF2SWF_PATH ==null){
			
			if (Pattern.matches("Linux.*", osName)) {  
				OFFICE_HOME = PublicConfig.OFFICE_HOME.get("linux.OFFICE_HOME");  
		    } else if (Pattern.matches("Windows.*", osName)) {  
		    	OFFICE_HOME = PublicConfig.OFFICE_HOME.get("windows.OFFICE_HOME");  
		    } else if (Pattern.matches("Mac.*", osName)) {  
		    	OFFICE_HOME = "/Application/OpenOffice.org.app/Contents";  
		    } 
			
			if (Pattern.matches("Linux.*", osName)) {  
				PDF2SWF_PATH = PublicConfig.OFFICE_HOME.get("linux.swftool");  
		    } else if (Pattern.matches("Windows.*", osName)) {  
		    	//把格式转换工具放在root下，不再编译class
		    	File file = new File(File2swfUtil.class.getResource("/").getPath());
		    	String basePath = file.getParentFile().getParentFile().getPath()+"/static/plugins/tools";
		    	//路径不能有空格
		    	PDF2SWF_PATH = basePath+"/swftools/pdf2swf.exe";
		    } else if (Pattern.matches("Mac.*", osName)) {  
		    }
		}
		//启动服务
		if(officeManager==null){
			startService();
		}
	}
	
	public static String getOfficeHome() {  
	    if (Pattern.matches("Linux.*", osName)) {  
	        return PublicConfig.OFFICE_HOME.get("linux.OFFICE_HOME");  
	    } else if (Pattern.matches("Windows.*", osName)) {  
	        return PublicConfig.OFFICE_HOME.get("windows.OFFICE_HOME");  
	    } else if (Pattern.matches("Mac.*", osName)) {  
	        return "/Application/OpenOffice.org.app/Contents";  
	    }  
	    return null;  
	}  
	/**
	 * 将附件转换为PDF
	 * @param inputOrgFile 输入文件的地址
	 * @param pdfputFile 输出文件的地址
	 * @throws FileNotFoundException 
	 */
	public static InputStream convert2PDF(String inputOrgFile) throws FileNotFoundException{
		if(!new File(inputOrgFile).exists()){
			return null;
		}
		init();
		// 后缀
		String fileExt = FileUtil.getExtend(inputOrgFile);
		//需要转换文件的名称
		String fileOrgName=inputOrgFile.substring(0,inputOrgFile.lastIndexOf("."));
		//转换后pdf文件
		String outputPdfFile = fileOrgName+".pdf";
		//转换后swf文件
		String outputSwfFile = fileOrgName+".swf";
		
		//转换后的swf文件
		File swfFile = new File(outputSwfFile);
		//转换后的pdf文件
		File pdfFile=new File(outputPdfFile);
		
		if(swfFile.exists()){//swf文件存在
		}else if(pdfFile.exists()){//swf文件不存在，pdf文件存在
			pdf2swf(outputPdfFile, outputSwfFile);
		}else{
			//先转换成pdf
			OfficeDocumentConverter  converter=new OfficeDocumentConverter(officeManager);
			try {
				converter.convert(new File(inputOrgFile),new File(outputPdfFile));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			//pdf转swf
			pdf2swf(outputPdfFile, outputSwfFile);
			
		}
		if(!fileExt.equals("pdf")){
			pdfFile.delete();
		}
		
		return new FileInputStream(swfFile);
	}
	/**
	 * 将附件转换为PDF
	 * @param inputOrgFile 输入文件的地址
	 * @param pdfputFile 输出文件的地址
	 * @throws FileNotFoundException 
	 */
	public static InputStream convertTxt2PDF(String inputOrgFile) throws FileNotFoundException{
		if(!new File(inputOrgFile).exists()){
			return null;
		}
		init();
		//需要转换文件的名称
		String fileOrgName=inputOrgFile.substring(0,inputOrgFile.lastIndexOf("."));
		//转换后pdf文件
		String odtFile = fileOrgName+".odt";
		FileUtil.copyFile(new FileInputStream(new File(inputOrgFile)),odtFile);
		//转换后pdf文件
		String outputPdfFile = fileOrgName+".pdf";
		//转换后swf文件
		String outputSwfFile = fileOrgName+".swf";
		inputOrgFile = odtFile;
		
		//转换后的swf文件
		File swfFile = new File(outputSwfFile);
		//转换后的pdf文件
		File pdfFile=new File(outputPdfFile);
		
		if(swfFile.exists()){//swf文件存在
		}else if(pdfFile.exists()){//swf文件不存在，pdf文件存在
			pdf2swf(outputPdfFile, outputSwfFile);
		}else{
			//先转换成pdf
			OfficeDocumentConverter  converter=new OfficeDocumentConverter(officeManager);
			try {
				converter.convert(new File(inputOrgFile),new File(outputPdfFile));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			//pdf转swf
			pdf2swf(outputPdfFile, outputSwfFile);
			
		}
		pdfFile.delete();
		new File(odtFile).delete();
		
		return new FileInputStream(swfFile);
	}
	/**
	 * 启动openOffice服务
	 */
	private static void startService() {
		try { 
			boolean stateFlag = false;
			log.info("准备启动服务....");  
		    try {  
		    	log.info("尝试连接已启动的服务...");  
		        ExternalOfficeManagerConfiguration externalProcessOfficeManager = new ExternalOfficeManagerConfiguration();  
		        externalProcessOfficeManager.setConnectOnStart(true);  
		        externalProcessOfficeManager.setPortNumber(port);  
		        officeManager = externalProcessOfficeManager.buildOfficeManager();  
		        officeManager.start();  
		        log.info("office转换服务启动成功!"); 
		        stateFlag = true;
		    } catch (Exception ex) {  
		        log.info("没有已启动的服务...");  
		    }  
		  if(!stateFlag){
			  log.info("创建并连接新服务...");  
			  
			  DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();  
			  
			  configuration.setOfficeHome(OFFICE_HOME);  
			  configuration.setPortNumbers(port);  
			  configuration.setTaskExecutionTimeout(1000 * 60 * 5L);  
			  configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);  
//		    if (CONNECTION_PROTOCOL != null) {  
//		        configuration.setConnectionProtocol(CONNECTION_PROTOCOL);  
//		    }  
//		    if (OFFICE_PROFILE != null) {  
//		        configuration.setTemplateProfileDir(OFFICE_PROFILE);  
//		    }  
			  officeManager = configuration.buildOfficeManager();  
			  officeManager.start();  
			  log.info("office转换服务启动成功!");  
		  }
		} catch (Exception ce) {  
		    log.error("office转换服务启动失败!详细信息:" + ce);  
		}  
	}
	/**
	 * 关闭服务
	 */
	@SuppressWarnings("unused")
	private static void stopService() {
		log.info("关闭office转换服务....");
		if(officeManager!=null){
			officeManager.stop();
		}
		log.info("关闭office转换成功!");
	}
	/**
	 * pdf转swf
	 * @param inputFile
	 * @param outputFile
	 */
	public static void pdf2swf(String inputFile,String outputFile){
		File swfFile=new File(outputFile);
		try {
			/* languagedir=D:\\xpdf-chinese-simplified用于处理pdf转换时出现的乱码
			 * 处理pdf转换的乱码需要进行三步 
			 * 
			 *  1下载XPDF：ftp://ftp.foolabs.com/pub/xpdf/xpdf-chinese-simplified.tar.gz解压到 D:\xpdf-chinese-simplified
			 *  2下载字体:http://blog.pjoke.com/wp-content/uploads/2009/02/font.zip解压到 D:\xpdf-chinese-simplified\CMap\
			 *  3配置打开并修改xpdf-chinese-simplified目录下的add-to-xpdfrc文件。将里面的路径设为自己的路径：
			 *   
			*/
			String command=PDF2SWF_PATH+" "+inputFile+" -o "+swfFile +" -T 9 -G -s poly2bitmap";//-slanguagedir=D:\\xpdf-chinese-simplified是用来处理转换时出现的中文乱码
			Process proc = Runtime.getRuntime().exec(command);
			InputStream stderr = proc.getInputStream();
		    byte buffer[]=new byte[1024];
		    int c;
		    while ((c = stderr.read(buffer)) != -1){
		    }
		    stderr.close();
		} catch (IOException e) {
			log.info("转换文档为swf文件失败！",e);
			e.printStackTrace();
		}
	}

}
