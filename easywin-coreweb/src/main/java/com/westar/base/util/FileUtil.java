package com.westar.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 文件操作工具类
 */
public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);
	// 项目路径
	private static String rootPath;

	public static String getRootPath() {
		return rootPath;
	}

	public static void setRootPath(String rootPath) {
		FileUtil.rootPath = rootPath;
	}

	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String getExtend(String fileName) {
		String extend = "";
		if (!StringUtils.isEmpty(fileName)) {
			int index = fileName.lastIndexOf('.');
			if (index > 0 && index < fileName.length() - 1) {
				extend = fileName.substring(index + 1).toLowerCase();
			}
		}
		return extend;
	}

	public static void main(String[] args) {
		FileUtil.getAllowFileTypes();
	}

	/**
	 * 定义允许上传的附件类别
	 * @return
	 */
	public static List<Object> getAllowFileTypes() {
		List<Object> list = null;
		/* 图片 */
		String[] pic = { "gif", "jpg", "jpeg", "png", "bmp" };
		/* 文档 */
		String[] doc = { "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt" };
		/* 音频 */
		String[] mp3 = { "mp3", "amr" };
		/* pdf */
		String[] pdf = { "pdf" };
		/* 视频 */
		String[] avi = { "avi", "wma", "rmvb", "rm", "flash", "flv", "mp4", "mid", "3gp" };
		/* 其他 */
		String[] other = { "rar", "zip" };
		Object[] s = (Object[]) CommonUtil.addArray(pic, doc, mp3, pdf , avi, other);
		list = Arrays.asList(s);
		return list;
	}
	/**
	 * 定义允许解析內容的的附件类别
	 * @return
	 */
	public static List<Object> getFileTypes() {
		List<Object> list = null;
		/* 文档 */
		String[] doc = { "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt" };
		Object[] s = (Object[]) CommonUtil.addArray(doc);
		list = Arrays.asList(s);
		return list;
	}

	/**
	 * 获取文件名称（不含后缀名）
	 * @param fileName
	 * @return
	 */
	public static String getFilePrefix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}

	/**
	 * 获取附件目录
	 * @return
	 */
	public static String getUploadBasePath() {
		String basepath = getRootPath() + "/WEB-INF";
		return basepath;
	}

	/**
	 * 把文件流生成到磁盘中
	 * @param fi
	 * @param outputFile
	 */
	public static void copyFile(FileInputStream fi, String outputFile) {
		File tFile = new File(outputFile);
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			if (!tFile.getParentFile().exists()) {
				tFile.getParentFile().mkdirs();
			}
			fo = new FileOutputStream(tFile);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (Exception ex) {
			logger.error("文件复制失败。", ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (fi != null) {
					fi.close();
				}
				if (fo != null) {
					fo.close();
				}
			} catch (IOException ex) {
				logger.error("关闭文件资源失败。", ex);
			}
		}

	}

	/**
	 * 创建多级目录
	 * @param path
	 * @return
	 */
	public static boolean creatDirs(String path) {
		if (path.contains(".")) {
			path = new File(path).getParentFile().getPath();
		}
		File aFile = new File(path);
		if (!aFile.exists()) {
			return aFile.mkdirs();
		} else {
			return true;
		}
	}
	
	/**
     * 分片验证
     * 验证对应分片文件是否存在，大小是否吻合
     * @param file  分片文件的路径
     * @param size  分片文件的大小
     * @return
     */
    public static boolean chunkCheck(String file, Long size){
        //检查目标分片是否存在且完整
        File target = new File(file);
        if(target.isFile() && size == target.length()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 取得附件上传的临时路径
     * @param comId
     * @return
     */
	public static String getUploadTempPath(Integer comId) {
		String basepath = FileUtil.getUploadBasePath();
		/* 所有附件都保存到uploads 不存在则新增文件夹 */
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdirs();
		}
		String path = "/" + "uploads"+"/"+comId+"/temp";
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdirs();
		}
		return basepath+path;
	}

	/**
	 * 取得附件上传后存放的路径
	 * @param comId
	 * @return
	 */
	public static String getUploadPath(Integer comId) {
		String basepath = FileUtil.getUploadBasePath();
		/* 所有附件都保存到uploads 不存在则新增文件夹 */
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdir();
		}
		String path = "/" + "uploads"+"/"+comId;
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdirs();
		}
		/* 每年一个文件夹 */
		path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdir();
		}
		/* 每月一个文件夹 */
		path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdir();
		}
		/* 每天一个文件夹 */
		path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdir();
		}
		return path;
	}
	/**
	 * 是否是视频文件
	 * @param ext
	 * @return
	 */
	public static boolean isVideoFile(String ext){
		if(ext.equals("3gp")||ext.equals("flv") ||ext.equals("avi") ||ext.equals("mkv") ||ext.equals("mov") ||ext.equals("mp4")
				||ext.equals("mpg") ||ext.equals("rmvb")||ext.equals("swf")||ext.equals("mid")||ext.equals("wmv")
				||ext.equals("mp3") ||ext.equals("m4a") ){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 取得视频附件上传的临时路径
	 * @param comId
	 * @return
	 */
	public static String getVideoTempPath(Integer comId) {
		String basepath = getRootPath();
		/* 所有附件都保存到uploads 不存在则新增文件夹 */
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdirs();
		}
		String path = "/" + "static"+"/"+"video"+"/"+comId+"/temp";
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdirs();
		}
		return basepath+path;
	}
	/**
	 * 取得视频上传后存放的路径
	 * @param comId
	 * @return
	 */
	public static String getUploadVideoPath(Integer comId) {
		String basepath = getRootPath();
		/* 所有附件都保存到uploads 不存在则新增文件夹 */
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdir();
		}
		String path = "/" + "static"+"/"+ "video"+"/"+comId;
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdirs();
		}
		/* 每年一个文件夹 */
		path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdir();
		}
		/* 每月一个文件夹 */
		path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdir();
		}
		/* 每天一个文件夹 */
		path = path + "/" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdir();
		}
		return path;
	}
	/********************************APP ********************************/
	
	/**
	 * 获取APP目录
	 * @return
	 */
	public static String getUploadAppBasePath() {
		String basepath = getRootPath() + "/static/android";
		return basepath;
	}

	 /**
     * 取得app上传的临时路径
     * @return
     */
	public static String getUploadAppTempPath() {
		String basepath = FileUtil.getUploadAppBasePath();
		/* 所有附件都保存到uploads 不存在则新增文件夹 */
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdirs();
		}
		String path = "/temp";
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdirs();
		}
		return basepath+path;
	}
	/**
	 * App路径
	 * @return
	 */
	public static String getOaAppPath() {
		String path = FileUtil.getUploadAppBasePath()+File.separator+"easywin.apk";
		return path;
	}
	/**
	 * 重命名成新APP名称
	 * @param newPath 新名称路径
	 * @return
	 */
	public static boolean renameOldApp(String newPath) {
		File f = new File(FileUtil.getOaAppPath());
		if (!f.exists()) {
			 return true;
		}else{
			return f.renameTo(new File(newPath));
		}
	}
	/**
	 * 重命名成默认APP名称
	 * @param tempFilePath 指定文件路径
	 * @throws Exception
	 */
	public static boolean renameApp(String tempFilePath) {
		File f = new File(tempFilePath);
		return f.renameTo(new File(FileUtil.getOaAppPath()));
	}

}
