package com.westar.core.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.westar.base.model.Filecontent;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.FileInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.DifPic;
import com.westar.base.util.File2swfUtil;
import com.westar.base.util.FileUtil;
import com.westar.base.util.MathExtend;
import com.westar.base.util.StringUtil;
import com.westar.base.util.UUIDGenerator;
import com.westar.core.service.MeetingRoomService;
import com.westar.core.service.UploadService;
import com.westar.core.service.UserInfoService;
import com.westar.core.thread.FileIndexThread;

/**
 * 
 * 描述:文件上传控制层
 * @author zzq
 * @date 2018年8月30日 下午4:55:50
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

	@Autowired
	UploadService uploadService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	MeetingRoomService meetingRoomService;

	/**
	 * 在线编辑器 清空附件信息session
	 */
	@ResponseBody
	@RequestMapping("/cleanSession")
	public void cleanSession(HttpSession session) {
		session.removeAttribute("ewebeditorUpload");
	}

	/**
	 * 跳转到多文件上传页面
	 * @return
	 */
	@RequestMapping("/uploadFilePage")
	public ModelAndView uploadFilePage() {
		ModelAndView mav = new ModelAndView("/common/uploadFile");
		return mav;
	}

	/**
	 * 删除附件信息
	 * 
	 * @param request
	 * @param id
	 *            附件主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delFile")
	public Map<String, Object> delFile(HttpServletRequest request, Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String basepath = FileUtil.getUploadBasePath();
			UserInfo userInfo = this.getSessionUser();
			uploadService.delFile(id, basepath, userInfo);
			map.put("status", "y");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 删除附件信息
	 * 
	 * @param request
	 * @param upfileId
	 *            附件主键id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delFileById")
	public Map<String, Object> delFileById(HttpServletRequest request,
			Integer upfileId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String basepath = FileUtil.getUploadBasePath();
			uploadService.delFile(upfileId, basepath, this.getSessionUser());
			map.put("status", "y");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 百度控件上传附件（用于文件MD5验证，分块验证，和文件合并）
	 * @param status 验证类别
	 * @param fileInfo 上传的附件
	 * @param request
	 * @param fileVal
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping(value = "/addDepartFile", method = RequestMethod.POST)
	public Map<String, Object> addDepartFile(String status, FileInfo fileInfo,
			HttpServletRequest request, String fileVal) throws Exception {
		String ext = FileUtil.getExtend(fileInfo.getName());
		//替换文件名中特殊字符
		fileInfo.setName(StringUtil.change(fileInfo.getName()));
		// 取得当前操作员
		UserInfo sesionUser = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		// 企业号
		Integer comId = sesionUser.getComId();

		if (null == status || "".equals(status)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile(fileVal);

			if (file != null && !file.isEmpty()) {
				// 文件存放的临时路径
				String baseTempPath = "";
				if(FileUtil.isVideoFile(ext)){
					baseTempPath = FileUtil.getVideoTempPath(comId)+File.separator+fileInfo.getMd5();
				}else{
					baseTempPath = FileUtil.getUploadTempPath(comId)+ File.separator + fileInfo.getMd5();
				}

				File parentDir = new File(baseTempPath);
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}

				// 临时文件的路径
				String tempFilePath = baseTempPath + File.separator+ fileInfo.getName() + "_" + fileInfo.getChunk()+ ".part";

				File tempPartFile = new File(tempFilePath);
				FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);
			}
			map.put("status", "y");
			map.put("fileMd5", fileInfo.getMd5());
			return map;
		} else {
			// 文件存放的临时路径
			String baseTempPath = "";
			if(FileUtil.isVideoFile(ext)){
				baseTempPath = FileUtil.getVideoTempPath(comId);
			}else{
				baseTempPath = FileUtil.getUploadTempPath(comId);
			}
			// 附件原来的名称
			String fileName = fileInfo.getName();
			// 附件后缀
			String fileExt = fileInfo.getExt();

			if ("md5Check".equals(status)) {// 秒传验证,验证是否上传

				String md5 = fileInfo.getMd5();
				// MD5查询附件信息
				Upfiles upfile = uploadService.getFileByMD5(md5, comId);

				if (null == upfile) {// 附件不存在
					map.put("ifExist", ConstantInterface.FILE_STATE_MISS);
					return map;
				} else {// 附件存在
					String basepath = "";
					if(FileUtil.isVideoFile(ext)){
						basepath = FileUtil.getRootPath() + upfile.getFilepath();
					}else{
						basepath = FileUtil.getUploadBasePath() + upfile.getFilepath();
					}
					File oldFile = new File(basepath);
					if(oldFile.exists() && oldFile.isFile()){
						map.put("ifExist", ConstantInterface.FILE_STATE_EXIST);
						
						map.put("fileId", upfile.getId());
						map.put("fileName", upfile.getFilename());
						return map;
					}else{
						map.put("ifExist", ConstantInterface.FILE_STATE_MISS);
						return map;
					}
					
				}
			} else if ("chunkCheck".equals(status)) {// 分块验证 上传
				// 临时文件的路径
				String tempFilePath = baseTempPath + File.separator
						+ fileInfo.getMd5() + File.separator
						+ fileInfo.getName() + "_" + fileInfo.getChunkIndex()
						+ ".part";

				// 检查目标分片是否存在且完整
				if (FileUtil.chunkCheck(tempFilePath,
						Long.valueOf(fileInfo.getSize()))) {
					map.put("ifExist", ConstantInterface.FILE_STATE_EXIST);
					return map;
				} else {
					map.put("ifExist", ConstantInterface.FILE_STATE_MISS);
					return map;
				}
			} else if ("chunksMerge".equals(status)) { // 分块合并
				// 临时文件夹的路径
				String tempPFilePath = baseTempPath + File.separator+ fileInfo.getMd5();
				// 是否全部上传完成
				// 所有分片都存在才说明整个文件上传完成
				boolean uploadDone = true;
				for (int i = 0; i < fileInfo.getChunks(); i++) {
					File partFile = new File(tempPFilePath, fileInfo.getName()+ "_" + i + ".part");
					if (!partFile.exists()) {
						uploadDone = false;
					}
				}
				if (uploadDone) {
					// 从数据库根据MD5查询文件
					Upfiles objFile = uploadService.getFileByMD5(fileInfo.getMd5(), comId);
					String basepath = "";
					if(FileUtil.isVideoFile(ext)){
						basepath = FileUtil.getRootPath();
					}else{
						basepath = FileUtil.getUploadBasePath();
					}
					if (null != objFile) {
						String filePath = basepath + objFile.getFilepath();
						// 数据库有数据，但是文件不存在
						File checkFile = new File(filePath);
						if(checkFile.exists() && checkFile.isDirectory()){
							checkFile.delete();
						}
						if (!checkFile.exists()) {
							File parentFile = new File(checkFile.getParent());
							if(!parentFile.exists()){
								parentFile.mkdirs();
							}
							// 附件若存在，则重新上传
							for (int i = 0; i < fileInfo.getChunks(); i++) {
								// 附件分片
								File partFile = new File(tempPFilePath,fileInfo.getName() + "_" + i + ".part");
								// 合并后的文件
								FileOutputStream destTempfos = null;
								try {
									destTempfos = new FileOutputStream(checkFile, true);

									FileUtils.copyFile(partFile, destTempfos);
								} catch (Exception e) {
									throw e;
								} finally {
									if (null != destTempfos) {
										destTempfos.close();
									}
								}
							}
						}

						FileUtils.deleteDirectory(new File(tempPFilePath));
						map.put("fileId", objFile.getId());
						map.put("fileName", objFile.getFilename());
						return map;
					} else {
						ext = fileInfo.getExt().toLowerCase();
						String path = "";
						// 存放的文件夹
						if(FileUtil.isVideoFile(ext)){
							 path = FileUtil.getUploadVideoPath(comId);
						}else{
							path = FileUtil.getUploadPath(comId);
						}
						// 新生成的附件
						String uuid = UUIDGenerator.getUUID();
						// 存放的文件名称
						String destFileName = uuid + "."+ fileInfo.getExt().toLowerCase();
						// 存放后的文件
						File destFile = new File(basepath + path, destFileName);
						// 附件若存在，则重新上传
						for (int i = 0; i < fileInfo.getChunks(); i++) {
							// 附件分片
							File partFile = new File(tempPFilePath,
									fileInfo.getName() + "_" + i + ".part");
							// 合并后的文件
							FileOutputStream destTempfos = null;
							try {
								destTempfos = new FileOutputStream(destFile,true);

								FileUtils.copyFile(partFile, destTempfos);
							} catch (Exception e) {
								throw e;
							} finally {
								if (null != destTempfos) {
									destTempfos.close();
								}
							}

						}

						// 文件大小
						String fileSize = fileInfo.getSize();
						String sizeM = MathExtend.divide(fileSize,String.valueOf(1024), 2);
						String dw = "K";
						if (Float.parseFloat(sizeM) > 1024) {
							sizeM = MathExtend.divide(sizeM,String.valueOf(1024), 2);
							dw = "M";
							if (Float.parseFloat(sizeM) > 1024) {
								sizeM = MathExtend.divide(sizeM,String.valueOf(1024), 2);
								dw = "G";
							}
						}
						String md5 = fileInfo.getMd5();
						// 附件信息存库
						Upfiles upfiles = new Upfiles();
						upfiles.setComId(comId);
						upfiles.setUuid(uuid);
						upfiles.setFilename(fileName);
						upfiles.setFilepath(path + "/" + destFileName);
						upfiles.setFileExt(fileExt.toLowerCase());
						upfiles.setSizeb(Integer.parseInt(fileSize));
						upfiles.setSizem(sizeM + dw);
						upfiles.setMd5(md5);
						Integer id = uploadService.addFile(upfiles);
						upfiles.setId(id);

						map.put("fileId", id);
						map.put("fileName", fileName);
						// 需要存入数据库后才能删除文件夹
						FileUtils.deleteDirectory(new File(tempPFilePath));

					}

				}
				String path = "";
				if (path == null) {
					map.put("status", "f");
					return map;
				} else {

				}
			} else {

			}
		}
		map.put("status", "y");
		return map;
	}

	/**
	 * 百度控件上传图片附件
	 * @param status  验证类别
	 * @param fileInfo 上传的附件
	 * @param request
	 * @param fileVal
	 * @param tempDir
	 * @param meetingRoomId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addDepartImgFile", method = RequestMethod.POST)
	public Map<String, Object> addDepartImgFile(String status,
			FileInfo fileInfo, HttpServletRequest request, String fileVal,
			String tempDir,
			@RequestParam(required = false) Integer meetingRoomId)
			throws Exception {
		//替换文件名中特殊字符
				fileInfo.setName(StringUtil.change(fileInfo.getName()));
		// 取得当前操作员
		UserInfo sesionUser = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		// 企业号
		Integer comId = sesionUser.getComId();
		Integer userId = sesionUser.getId();

		// 用于临时存放图片
		String basepath = FileUtil.getRootPath();
		/* 所有附件都保存到uploads 不存在则新增文件夹 */
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdir();
		}
		// 每个人创建一个文件夹 /static/voteImg/公司主键/操作员主键
		String path = "/static" + "/temp/" + tempDir + "/" + comId + "/"+ userId;
		f = new File(basepath + path);
		if (!f.exists()) {
			f.mkdirs();
		}

		if (null == status || "".equals(status)) {// 用于文件上传
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile(fileVal);

			if (file != null && !file.isEmpty()) {
				// 临时文件的路径
				String tempFilePath = basepath + path + File.separator+ fileInfo.getMd5() + 
						File.separator+ fileInfo.getName() + "_" + fileInfo.getChunk()+ ".part";

				File tempPartFile = new File(tempFilePath);
				if (!tempPartFile.exists()) {
					FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);
				}
			}
			map.put("status", "y");
			map.put("fileMd5", fileInfo.getMd5());
			return map;
		} else {
			// 附件原来的名称
			String fileName = fileInfo.getName();
			// 附件后缀
			String fileExt = fileInfo.getExt();

			if ("chunkCheck".equals(status)) {// 分块验证

				// 临时文件的路径
				String tempFilePath = basepath + path + File.separator + fileInfo.getMd5() + File.separator
						+ fileInfo.getName() + "_" + fileInfo.getChunkIndex() + ".part";

				// 检查目标分片是否存在且完整
				if (FileUtil.chunkCheck(tempFilePath,Long.valueOf(fileInfo.getSize()))) {
					map.put("ifExist", ConstantInterface.FILE_STATE_EXIST);
					return map;
				} else {
					map.put("ifExist", ConstantInterface.FILE_STATE_MISS);
					return map;
				}
			} else if ("chunksMerge".equals(status)) { // 分块合并
				// 临时文件夹的路径
				String tempPFilePath = basepath + path + File.separator + fileInfo.getMd5();
				// 是否全部上传完成
				// 所有分片都存在才说明整个文件上传完成
				boolean uploadDone = true;
				for (int i = 0; i < fileInfo.getChunks(); i++) {
					File partFile = new File(tempPFilePath, fileInfo.getName() + "_" + i + ".part");
					if (!partFile.exists()) {
						uploadDone = false;
					}
				}
				if (uploadDone) {
					String picHead = comId + "_" + userId + "_";
					// 存放的文件名称 //原图像的路径
					String destFileName = picHead + UUIDGenerator.getUUID()+ "." + fileExt;
					String orgImgPath = path + "/" + destFileName;
					// 存放后的文件
					File destFile = new File(basepath + path, destFileName);
					// 附件若存在，则重新上传
					for (int i = 0; i < fileInfo.getChunks(); i++) {
						// 附件分片
						File partFile = new File(tempPFilePath,fileInfo.getName() + "_" + i + ".part");
						// 合并后的文件
						FileOutputStream destTempfos = null;
						try {
							destTempfos = new FileOutputStream(destFile, true);

							FileUtils.copyFile(partFile, destTempfos);
						} catch (Exception e) {
							throw e;
						} finally {
							if (null != destTempfos) {
								destTempfos.close();
							}
						}
					}
					if ("voteImg".equals(tempDir)) {
						// 中图像
						String midImgPath = path + "/" + picHead+ UUIDGenerator.getUUID() + "." + fileExt;
						// 大图像
						String largeImgPath = path + "/" + picHead+ UUIDGenerator.getUUID() + "." + fileExt;

						// 开始处理
						File originalImage = new File(basepath + orgImgPath);
						// 大图片
						DifPic.resize(originalImage, new File(basepath
								+ largeImgPath), 430, 1f);
						// 中图片
						DifPic.resize(originalImage, new File(basepath
								+ midImgPath), 130, 1f);
						// 删除原始图片
						map.put("status", "y");
						map.put("orgImgPath", orgImgPath);
						map.put("largeImgPath", largeImgPath);
						map.put("midImgPath", midImgPath);
						map.put("fileName", fileName);
					} else if ("headImg".equals(tempDir)) {
						// 大图像的路径
						String largeImgPath = path + "/" + picHead+ UUIDGenerator.getUUID() + "." + fileExt;
						// 开始处理
						File originalImage = new File(basepath + orgImgPath);
						// 大图片
						DifPic.resize(originalImage, new File(basepath+ largeImgPath), 210, 1f);

						map.put("status", "y");
						map.put("orgImgPath", orgImgPath);
						map.put("orgImgName", fileName);
						map.put("largeImgPath", largeImgPath);
					} else if ("meetingRoomImg".equals(tempDir)) {
						if (null != meetingRoomId && meetingRoomId > 0) {
							return this.getMeetImg(meetingRoomId, fileInfo,comId, destFile, tempPFilePath);
						} else {
							map.put("status", "y");
							map.put("orgImgPath", orgImgPath);
							map.put("fileName", fileName);
						}

					}
				}

			}
		}
		return map;
	}

	/**
	 * 会议室修改图片
	 * 
	 * @param meetingRoomId
	 *            会议室主键
	 * @param fileInfo
	 *            文件参数
	 * @param comId
	 *            企业号
	 * @param sourceFile
	 *            已合并的文件
	 * @param tempPFilePath
	 *            临时文件路径用于删除
	 * @return
	 * @throws IOException
	 */
	private Map<String, Object> getMeetImg(Integer meetingRoomId,
			FileInfo fileInfo, Integer comId, File sourceFile,
			String tempPFilePath) throws IOException {
		//替换文件名中特殊字符
				fileInfo.setName(StringUtil.change(fileInfo.getName()));
		Map<String, Object> map = new HashMap<String, Object>();

		String basepath = FileUtil.getUploadBasePath();
		/* 所有附件都保存到uploads 不存在则新增文件夹 */
		File f = new File(basepath);
		if (!f.exists()) {
			f.mkdir();
		}
		String fileMd5 = fileInfo.getMd5();
		// 从数据库根据MD5查询文件
		Upfiles objFile = uploadService.getFileByMD5(fileMd5, comId);

		if (null != objFile) {// 附件存在于数据库
			map.put("status", "y");
			map.put("orgImgPath", "/downLoad/down/" + objFile.getUuid() + "/"
					+ objFile.getFilename() + "?sid=" + this.getSid());
			// 修改会议室的图片主键
			meetingRoomService.updateMeetingRoomPicId(meetingRoomId,
					objFile.getId());
			String path = basepath + objFile.getFilepath();

			// 数据库有数据，但是文件不存在
			File checkFile = new File(path);
			if (!checkFile.exists()) {
				// 所在文件夹
				File dirFile = new File(path.substring(0,
						path.lastIndexOf("\\")));
				if (!dirFile.exists()) {// 文件夹不存在
					dirFile.mkdirs();
				}
				// 合并后的文件
				FileOutputStream destTempfos = null;
				try {
					destTempfos = new FileOutputStream(checkFile);
					// 重新存放
					FileUtils.copyFile(sourceFile, destTempfos);
					FileUtils.deleteDirectory(new File(tempPFilePath)
							.getParentFile());
				} catch (FileNotFoundException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} finally {
					if (null != destTempfos) {
						try {
							destTempfos.close();
						} catch (IOException e) {
							throw e;
						}
					}
				}

			}
		} else {
			// 文件大小
			String fileSize = fileInfo.getSize();
			// 文件大小
			String sizeM = MathExtend.divide(fileSize, String.valueOf(1024), 2);
			// 文件后缀
			String fileExt = fileInfo.getExt();

			String dw = "K";
			if (Float.parseFloat(sizeM) > 1024) {
				sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
				dw = "M";
				if (Float.parseFloat(sizeM) > 1024) {
					sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
					dw = "G";
				}
			}
			/* 所有附件都保存到uploads 不存在则新增文件夹 */
			f = new File(basepath);
			if (!f.exists()) {
				f.mkdir();
			}
			String path = "/" + "uploads" + "/" + comId;
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
			path = path + "/"
					+ DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
			f = new File(basepath + path);
			if (!f.exists()) {
				f.mkdir();
			}
			/* 每天一个文件夹 */
			path = path + "/"
					+ DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
			f = new File(basepath + path);
			if (!f.exists()) {
				f.mkdir();
			}

			// 新生成的附件
			String uuid = UUIDGenerator.getUUID();
			// 存放的文件名称
			String destFileName = uuid + "." + fileInfo.getExt().toLowerCase();

			path = path + "/" + destFileName;

			File destFile2 = new File(basepath + path);
			// 合并后的文件
			FileOutputStream destTempfos = null;
			try {
				destTempfos = new FileOutputStream(destFile2, true);
				FileUtils.copyFile(sourceFile, destTempfos);
			} catch (FileNotFoundException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} finally {
				if (null != destTempfos) {
					try {
						destTempfos.close();
					} catch (IOException e) {
						throw e;
					}
				}
			}

			// 附件信息存库
			Upfiles upfiles = new Upfiles();
			upfiles.setComId(comId);
			upfiles.setUuid(uuid);
			upfiles.setFilename(fileInfo.getName());
			upfiles.setFilepath(path);
			upfiles.setFileExt(fileExt.toLowerCase());
			upfiles.setSizeb(Integer.parseInt(fileSize));
			upfiles.setSizem(sizeM + dw);
			upfiles.setMd5(fileMd5);
			Integer id = uploadService.addFile(upfiles);
			upfiles.setId(id);

			// 若文件是文本类的文件
			if (FileUtil.getFileTypes().contains(fileExt)) {
				Filecontent filecontent = new Filecontent();

				filecontent.setComId(comId);
				filecontent.setFilePath(basepath + path);
				filecontent.setUpfilesId(id);
				new Thread(new FileIndexThread(uploadService, filecontent))
						.start();
			}
			map.put("status", "y");
			map.put("orgImgPath", "/downLoad/down/" + upfiles.getUuid() + "/"
					+ upfiles.getFilename() + "?sid=" + this.getSid());
			// 修改会议室的图片主键
			meetingRoomService.updateMeetingRoomPicId(meetingRoomId, id);

			try {
				FileUtils.deleteDirectory(new File(tempPFilePath)
						.getParentFile());
			} catch (IOException e) {
				throw e;
			}
		}
		return map;
	}
	/**
	 * 百度控件上传附件（用于文件MD5验证，分块验证，和文件合并）
	 * @param status 验证类别
	 * @param fileInfo 上传的附件
	 * @param request
	 * @param fileVal
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/loadUserFile", method = RequestMethod.POST)
	public Map<String, Object> loadUserFile(String status, FileInfo fileInfo,
			HttpServletRequest request, String fileVal) throws Exception {
		//替换文件名中特殊字符
				fileInfo.setName(StringUtil.change(fileInfo.getName()));
		// 取得当前操作员
		UserInfo sesionUser = this.getSessionUser();
		Map<String, Object> map = new HashMap<String, Object>();
		// 企业号
		Integer comId = sesionUser.getComId();
		// 当前操作人员
		Integer userId = sesionUser.getId();
		// 文件根路径
		String baseDirPath = FileUtil.getRootPath();

		String picHead = comId + "_" + userId + "_";

		// 文件相对路径的文件夹
		String relDirPath = "/static" + "/temp/" + "invUserFile" + "/" + comId
				+ "/" + userId + "/";
		if (null == status || "".equals(status)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile(fileVal);

			/* 所有附件都保存到uploads 不存在则新增文件夹 */
			File f = new File(baseDirPath);
			if (!f.exists()) {
				f.mkdir();
			}

			if (file != null && !file.isEmpty()) {
				// 文件绝对路径
				String absolDirPath = baseDirPath + relDirPath;
				// 文件存放的临时路径
				File parentDir = new File(absolDirPath);
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}

				// 临时文件的巨绝对路径
				String tempFilePath = absolDirPath + picHead
						+ fileInfo.getName() + "_" + fileInfo.getChunk()
						+ ".part";

				File tempPartFile = new File(tempFilePath);
				FileUtils.copyInputStreamToFile(file.getInputStream(),
						tempPartFile);
			}
			map.put("status", "y");
			map.put("fileMd5", fileInfo.getMd5());
			return map;
		} else {
			if ("chunksMerge".equals(status)) {// 分块合并
				// 临时文件夹的路径
				String tempPFilePath = baseDirPath + relDirPath;
				// 是否全部上传完成
				// 所有分片都存在才说明整个文件上传完成
				boolean uploadDone = true;
				for (int i = 0; i < fileInfo.getChunks(); i++) {
					File partFile = new File(tempPFilePath, picHead
							+ fileInfo.getName() + "_" + i + ".part");
					if (!partFile.exists()) {
						uploadDone = false;
					}
				}
				if (uploadDone) {
					// 新生成的附件
					String uuid = UUIDGenerator.getUUID();
					// 存放的文件名称
					String destFileName = picHead + uuid + "."
							+ fileInfo.getExt().toLowerCase();
					// 存放后的文件
					File destFile = new File(tempPFilePath, destFileName);

					// 附件若存在，则重新上传
					for (int i = 0; i < fileInfo.getChunks(); i++) {
						// 附件分片
						File partFile = new File(tempPFilePath, picHead
								+ fileInfo.getName() + "_" + i + ".part");
						// 合并后的文件
						FileOutputStream destTempfos = null;
						try {
							destTempfos = new FileOutputStream(destFile, true);

							FileUtils.copyFile(partFile, destTempfos);
						} catch (Exception e) {
							throw e;
						} finally {
							if (null != destTempfos) {
								destTempfos.close();
							}
						}
					}

					map.put("status", "y");
					map.put("orgPath", relDirPath + destFileName);

				} else {
					map.put("status", "f");
				}
			}
		}
		return map;
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
	@RequestMapping(value = "/down/{uuid}/{filename}")
	public ModelAndView down(HttpServletRequest request,HttpServletResponse response, @PathVariable String uuid)
			throws IOException {
		String basepath = FileUtil.getUploadBasePath();
		Upfiles upfiles = uploadService.getFileByUUid(uuid);
		// 清空response
		response.reset();
		// 设置response的Header
		response.reset();
		response.setContentType("application/octet-stream");
		// response.setHeader("Content-Disposition", "attachment;filename=\"" +
		// fileName + "\"");
		response.setHeader("Connection", "close");
		InputStream fis = null;
		OutputStream toClient = null;
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
				e.printStackTrace();
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
	@RequestMapping(value = "/viewOfficeFile/{uuid}/{fileExt}")
	public ModelAndView viewOfficeFile(HttpServletRequest request,HttpServletResponse response, @PathVariable String uuid,
			@PathVariable String fileExt) throws IOException {
		String basepath = FileUtil.getUploadBasePath();
		Upfiles upfiles = uploadService.getFileByUUid(uuid);
		// 清空response
		response.reset();
		// 设置response的Header
		response.reset();
		response.setContentType("application/octet-stream");
		// response.setHeader("Content-Disposition", "attachment;filename=\"" +
		// fileName + "\"");
		response.setHeader("Connection", "close");
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			List<Object> list = null;
			/* 文档 */
			String[] doc = { "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf" };
			Object[] s = (Object[]) CommonUtil.addArray(doc);
			list = Arrays.asList(s);
			// 是office文档 或是pdf文件
			if (list.contains(fileExt.toLowerCase())) {
				fis = File2swfUtil.convert2PDF(basepath + upfiles.getFilepath());
			} else if ("txt".equalsIgnoreCase(fileExt)) {
				// 文件
				fis = File2swfUtil.convertTxt2PDF(basepath+ upfiles.getFilepath());
			} else {
				// 非office文档
				// 以流的形式下载文件
				fis = new BufferedInputStream(new FileInputStream(basepath+ upfiles.getFilepath()));
			}
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
		} catch (Exception e) {
			/* 点击下载之后，又取消下载 异常不处理 */
			if (!e.getClass().getSimpleName().equals("ClientAbortException")) {
				e.printStackTrace();
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