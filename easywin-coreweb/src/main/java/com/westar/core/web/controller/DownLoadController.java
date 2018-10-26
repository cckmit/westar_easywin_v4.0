package com.westar.core.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.FileDetail;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.FileUtil;
import com.westar.core.service.FileCenterService;
import com.westar.core.service.UploadService;

/**
 * 
 * 描述: 下载操作控制类
 * @author zzq
 * @date 2018年8月25日 上午11:51:35
 */
@Controller
@RequestMapping("/downLoad")
public class DownLoadController extends BaseController{

	@Autowired
	UploadService uploadService;
	
	@Autowired
	FileCenterService fileCenterService;
	/**
	 * 附件下载
	 * 
	 * @param request
	 * @param response
	 * @param uuid
	 *            附件表中的随机标识码
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/down/{uuid}/{filename}")
	public ModelAndView down(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable String uuid,@PathVariable String filename,Integer addLogstate) throws IOException {
		String basepath = FileUtil.getUploadBasePath();
		Upfiles upfiles = uploadService.getFileByUUid(uuid);
		if (null == upfiles) {
			return null;
		}
		if(null!=addLogstate && addLogstate==ConstantInterface.ENABLED_YES){//需要添加日志
			//文档中心日志添加
			UserInfo userInfo = this.getSessionUser();
			FileDetail fileDetail =  fileCenterService.getFileDetailByFileId(upfiles.getId(), userInfo);
			if(fileDetail != null) {
				fileCenterService.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "下载了文件");
			}
		}
		// 清空response
		response.reset();
		// 设置response的Header
		response.setContentType("application/octet-stream");
		// response.setHeader("Content-Disposition", "attachment;filename=\"" +
		// fileName + "\"");
//		response.setContentLengthLong(upfiles.getSizeb());
		response.setContentLength(upfiles.getSizeb());
		
		String agent = (String) request.getHeader("USER-AGENT");     
		if(agent != null &&  agent.toLowerCase().indexOf("firefox") > 0) {// 火狐      
		    String enableFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(upfiles.getFilename().getBytes("UTF-8")))) + "?=";    
		    response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);    
		} else { 
		    String enableFileName = new String(upfiles.getFilename().getBytes("GBK"), "ISO-8859-1");    
		    response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);    
		}  
		
		basepath = basepath.replaceAll("\\\\", "/");
		basepath = basepath.replaceAll("//", "/");
		upfiles.setFilepath(upfiles.getFilepath().replaceAll("\\\\", "/"));
		
		// response.setHeader("Connection", "close");
		OutputStream toClient = null;
		InputStream fis = null;
		try {
			// 以流的形式下载文件
			fis = new BufferedInputStream(new FileInputStream(basepath+ upfiles.getFilepath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
		} catch (Exception e) {
			/* 点击下载之后，又取消下载 异常不处理 */
			if (!e.getClass().getSimpleName().equals("ClientAbortException")) {
				// e.printStackTrace();
				System.err.println("附件：" + basepath + upfiles.getFilepath()+ "(" + upfiles.getFilename() + ")缺失");
			}
		} finally {
			if (null != fis) {
				fis.close();
			}
			if (null != toClient) {
				toClient.close();
			}
		}
		return null;
	}
	/**
	 * 用户头像
	 * @param request
	 * @param response
	 * @param comId
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/userImg/{comId}/{userId}")
	public ModelAndView userImg(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable String comId, @PathVariable String userId,String size) throws IOException {
		
		String basepath = FileUtil.getUploadBasePath();
		size = StringUtils.isEmpty(size)?"2":size;
		
		String msg = "/static/headImg/"+size+"2.jpg";
		String defFilePath = FileUtil.getRootPath()+msg;
		Upfiles upfiles = null;
		if(StringUtils.isNotEmpty(comId) && StringUtils.isNotEmpty(userId) 
				&& !"null".equals(comId) && !"null".equals(userId)){
			upfiles = uploadService.queryUserImgFile(comId,userId,size);
			if(null!=upfiles){
				if (StringUtils.isNotEmpty(upfiles.getFilepath()) && new File(basepath + upfiles.getFilepath()).exists()) {
					defFilePath = basepath+ upfiles.getFilepath();
				}else{
					msg = upfiles.getMsg();
					msg = "/static/headImg/"+size+msg+".jpg";
					defFilePath = FileUtil.getRootPath()+msg;
					
					upfiles.setFilepath(defFilePath);
					upfiles.setFilename("头像.jpg");
				}
			}
		}
		if (null == upfiles) {
			upfiles = new Upfiles();
			upfiles.setFilepath(defFilePath);
			upfiles.setFilename("头像.jpg");
		}
		
		// 清空response
		response.reset();
		// 设置response的Header
		response.setContentType("application/octet-stream");
		// response.setHeader("Content-Disposition", "attachment;filename=\"" +
		// fileName + "\"");
		String agent = (String) request.getHeader("USER-AGENT");
		if(agent != null &&  agent.toLowerCase().indexOf("firefox") > 0) {// 火狐      
			String enableFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(upfiles.getFilename().getBytes("UTF-8")))) + "?=";    
			response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);    
		} else { 
			String enableFileName = new String(upfiles.getFilename().getBytes("GBK"), "ISO-8859-1");    
			response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);    
		}  
		
		basepath = basepath.replaceAll("\\\\", "/");
		basepath = basepath.replaceAll("//", "/");
		upfiles.setFilepath(upfiles.getFilepath().replaceAll("\\\\", "/"));
		
		// response.setHeader("Connection", "close");
		OutputStream toClient = null;
		InputStream fis = null;
		try {
			// 以流的形式下载文件
			fis = new BufferedInputStream(new FileInputStream(defFilePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
		} catch (Exception e) {
			/* 点击下载之后，又取消下载 异常不处理 */
			if (!e.getClass().getSimpleName().equals("ClientAbortException")) {
				// e.printStackTrace();
				System.err.println("附件：" + basepath + upfiles.getFilepath()+ "(" + upfiles.getFilename() + ")缺失");
			}
		} finally {
			if (null != fis) {
				fis.close();
			}
			if (null != toClient) {
				toClient.close();
			}
		}
		return null;
	}
	
	
	/**
	 * 附件下载
	 * 
	 * @param request
	 * @param response
	 * @param uuid
	 *            附件表中的随机标识码
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/downViedo/{uuid}/{filename}")
	public ModelAndView downViedo(HttpServletRequest request, HttpServletResponse response, @PathVariable String uuid,@PathVariable String filename) throws IOException {
		Upfiles upfiles = uploadService.getFileByUUid(uuid);
		if (null == upfiles) {
			return null;
		}
		UserInfo userInfo = this.getSessionUser();
		FileDetail fileDetail =  fileCenterService.getFileDetailByFileId(upfiles.getId(), userInfo);
		if(fileDetail != null) {
			fileCenterService.addFileLog(userInfo.getComId(), fileDetail.getId(), userInfo.getId(),  userInfo.getUserName(), "下载了文件");
		}
		String path = FileUtil.getUploadBasePath()+upfiles.getFilepath();
		OutputStream toClient = null;
		InputStream fis = null;
		try {
			//以流的形式下载文件
			fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
		} catch (Exception e) {
			if (!e.getClass().getSimpleName().equals("ClientAbortException")) {
				System.err.println("附件：" + path+ "(" + upfiles.getFilename() + ")缺失");
			}
		} finally {
			if (null != fis) {
				fis.close();
			}
			if (null != toClient) {
				toClient.close();
			}
		}
		return null;
	}
	
	/**
	 * 下载邮箱临时文件
	 * @author hcj 
	 * @param request
	 * @param response
	 * @param filename
	 * @return
	 * @throws IOException 
	 * @date 2018年9月25日 下午2:34:05
	 */
	@RequestMapping(value = "/downTemp")
	public ModelAndView downTemp(HttpServletRequest request, HttpServletResponse response,String filename) throws IOException {
		// 清空response
		response.reset();
		// 设置response的Header
		response.setContentType("application/octet-stream");
		
		String agent = (String) request.getHeader("USER-AGENT");     
		if(agent != null &&  agent.toLowerCase().indexOf("firefox") > 0) {// 火狐      
		    String enableFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(filename.getBytes("UTF-8")))) + "?=";    
		    response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);    
		} else { 
		    String enableFileName = new String(filename.getBytes("GBK"), "ISO-8859-1");    
		    response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);    
		}  
		String basepath = FileUtil.getUploadBasePath();
		OutputStream toClient = null;
		InputStream fis = null;
		try {
			// 以流的形式下载文件
			fis = new BufferedInputStream(new FileInputStream(basepath+"/"+ filename));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
		} catch (Exception e) {
			/* 点击下载之后，又取消下载 异常不处理 */
			if (!e.getClass().getSimpleName().equals("ClientAbortException")) {
				System.err.println("附件：" + basepath + filename+ "(" + filename + ")缺失");
			}
		} finally {
			if (null != fis) {
				fis.close();
			}
			if (null != toClient) {
				toClient.close();
			}
		}
		return null;
	}
	
}
