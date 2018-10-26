package com.westar.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.westar.base.annotation.Filed;
import com.westar.base.model.AttenceTime;
import com.westar.base.model.AttenceType;
import com.westar.base.model.AttenceWeek;
import com.westar.base.model.BiaoQing;
import com.westar.base.model.DataDic;
import com.westar.base.model.FileViewScope;
import com.westar.base.model.MeetRegular;
import com.westar.base.model.Meeting;
import com.westar.base.model.MsgShare;
import com.westar.base.model.Schedule;
import com.westar.base.model.SelfGroup;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UsedGroup;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.IndexDoc;
import com.westar.core.service.UploadService;
import com.westar.core.web.BiaoQingContext;
import com.westar.core.web.DataDicContext;

public class CommonUtil {
	
	//分享范围类型-所有同事
	public static final int ALL_COLLEAGUE = 0;
	//分享范围类型-根据群组ID界定
	public static final int BY_GROUPID = 1;
	//分享范围类型-自己
	public static final int MYSELF = 2;


	private static Logger logger = Logger.getLogger(Encodes.class);

	/**
	 * 数组合并 多个数组合并成一个
	 * 
	 * @param strs
	 * @return
	 */
	public static Object addArray(Object[]... strs) {
		Object[] rs = (Object[]) new Object[0];
		for (Object[] str : strs) {
			rs = (Object[]) ArrayUtils.addAll(rs, str);
		}
		return rs;
	}

	/**
	 * 字符串匹配，把汉字替换成表情图片地址
	 * 
	 * @param content
	 * @return
	 */
	public static String biaoQingReplace(String content) {
		List<BiaoQing> listBiaoQing = BiaoQingContext.getInstance()
				.getBiaoQingList();
		if (null != listBiaoQing && listBiaoQing.size() > 0) {
			for (BiaoQing vo : listBiaoQing) {
				content = content.replace(vo.getImgDescribe(), "<img src=\""
						+ vo.getImgPath() + "\" width=\"24\" height=\"24\" />");
			}
		}
		return content;
	}

	/**
	 * 被邀请人员(去重处理)
	 * 
	 * @param invUsers
	 * @return
	 */
	public static Set<String> removeRepAccount(String[] invUsers) {
		Set<String> set = new HashSet<String>();
		for (String email : invUsers) {
			if (!"".equals(email)) {
				set.add(email);
			}
		}
		return set;
	}

	/**
	 * 获取个人的私有组json字符串
	 * 
	 * @param listSelfGroup
	 *            个人分组集合
	 * @param grpType
	 * @param grpIds
	 * @return
	 */
	public static String selfGroupJson(List<SelfGroup> listSelfGroup,
			Set<Integer> grpIds, Integer grpType) {
		// 生成ztree所需数组格式字符串
		StringBuffer str = new StringBuffer(
				"[{id:-1, pId:0, name:'所有同事',ztype:'0'},");
		if (-1 == grpType) {
			str = new StringBuffer(
					"[{id:-1, pId:0, name:'所有同事',ztype:'0',checked:true},");
		}

		if (null != listSelfGroup && listSelfGroup.size() > 0) {
			for (SelfGroup vo : listSelfGroup) {
				if (grpIds.contains(vo.getId())) {
					str.append("{id:" + vo.getId() + ", pId:0, name:'"
							+ vo.getGrpName() + "',ztype:'1',checked:true},");
				} else {
					str.append("{id:" + vo.getId() + ", pId:0, name:'"
							+ vo.getGrpName() + "',ztype:'1'},");
				}
			}
		}

		if (2 == grpType) {
			str.append("{id:2, pId:0, name:'我自己',ztype:'2',checked:true}]");
		} else {
			str.append("{id:2, pId:0, name:'我自己',ztype:'2'}]");
		}
		return str.toString();
	}

	/**
	 * 获取个人的私有组json字符串
	 * 
	 * @param listSelfGroup
	 *            个人分组集合
	 * @return
	 */
	public static String selfGroup2Json(List<SelfGroup> listSelfGroup) {
		// 生成ztree所需数组格式字符串
		StringBuffer str = new StringBuffer(
				"[{id:-1, pId:null, name:'所有分组',open:true},");
		str.append("{id:1, pId:-1, name:'所有同事',open:true},");
		if (null != listSelfGroup && listSelfGroup.size() > 0) {
			str.append("{id:0, pId:-1, name:'自定义',open:true},");
			for (SelfGroup vo : listSelfGroup) {
				str.append("{id:" + vo.getId() + ", pId:0, name:'"
						+ vo.getGrpName() + "'},");
			}
		}
		str.append("{id:2, pId:-1, name:'我自己',open:true}]");
		return str.toString();
	}
	
	/**
	 * 最近一次使用的分组的类型，分组名称以及自定义所有的分组
	 * @param usedGroups 上次使用的分组集合
	 * @param listSelfGroup 自定义的所有分组集合
	 * @return
	 */
	public static Map<String, String> usedGrpJson(List<UsedGroup> usedGroups, List<SelfGroup> listSelfGroup) {
		//存放分组类型和分组名称
		Map<String, String> map = new HashMap<String, String>();
		//上次使用的分组
		Set<Integer> grpIds = new HashSet<Integer>();
		//上次使用的分组类型
		Integer grpType = 0;
		//上次使用的分组主键和类型
		String idType="";
		//上次使用的分组名称
		String scopeTypeSel="";
		//上次使用过分组，则进行解析
		if(null!=usedGroups && usedGroups.size()>0){
			for (UsedGroup usedGroup : usedGroups) {
				if("0".equals(usedGroup.getGroupType())){
					grpType = -1;
					idType = "-1@0";
					scopeTypeSel="所有同事";
				}else if("2".equals(usedGroup.getGroupType())){
					grpType = 2;
					idType = "2@2";
					scopeTypeSel="我自己";
				}else{
					grpType = 1;
					grpIds.add(usedGroup.getGrpId());
					idType += usedGroup.getGrpId()+"@1,";
					scopeTypeSel+=usedGroup.getGrpName()+",";
					grpIds.add(usedGroup.getGrpId());
				}
			}
			if(idType.indexOf(",")>=0){
				idType = idType.substring(0,idType.length()-1);
				scopeTypeSel = scopeTypeSel.substring(0,scopeTypeSel.length()-1);
			}
			
		}else{//默认有一个
			grpType = -1;
			idType = "-1@0";
			scopeTypeSel="所有同事";
		}
		// 生成ztree所需数组格式字符串
		StringBuffer str = new  StringBuffer();
		if (2 == grpType) {
			str.append("[{id:2, pId:0, name:'我自己',ztype:'2',checked:true},");
		} else {
			str.append("[{id:2, pId:0, name:'我自己',ztype:'2'},");
		}
		
		
		//选中上次使用过的数据
		if (null != listSelfGroup && listSelfGroup.size() > 0) {
			for (SelfGroup vo : listSelfGroup) {
				if (grpIds.contains(vo.getId())) {
					str.append("{id:" + vo.getId() + ", pId:0, name:'"
							+ vo.getGrpName() + "',ztype:'1',checked:true},");
				} else {
					str.append("{id:" + vo.getId() + ", pId:0, name:'"
							+ vo.getGrpName() + "',ztype:'1'},");
				}
			}
		}

		if (-1 == grpType) {
			str.append( "{id:-1, pId:0, name:'所有同事',ztype:'0',checked:true}]");
		}else{
			str.append("{id:-1, pId:0, name:'所有同事',ztype:'0'}]");
		}
		
		map.put("idType", idType);
		map.put("scopeTypeSel", scopeTypeSel);
		map.put("selfGroupStr", str.toString());
		
		return map;
	}
	/**
	 * 最近一次使用的分组的类型，分组名称以及自定义所有的分组
	 * @param listSelfGroup 自定义的所有分组集合
	 * @return
	 */
	public static Map<String, String> viewGrpJson(Integer scopeType,List<FileViewScope> fileviewScopes, List<SelfGroup> listSelfGroup) {
		//存放分组类型和分组名称
		Map<String, String> map = new HashMap<String, String>();
		//使用的分组
		Set<Integer> grpIds = new HashSet<Integer>();
		//使用的分组类型
		Integer grpType = 0;
		//使用的分组主键和类型
		String idType="";
		//使用的分组名称
		String scopeTypeSel="";
		
		if(scopeType == 0) {
			grpType = -1;
			idType = "-1@0";
			scopeTypeSel="所有同事";
		}else if(scopeType == 1) {
			if(!CommonUtil.isNull(fileviewScopes)) {
				for(FileViewScope fileviewScope:fileviewScopes) {
					grpType = 1;
					grpIds.add(fileviewScope.getGrpId());
					idType += fileviewScope.getGrpId()+"@1,";
					scopeTypeSel+=fileviewScope.getGrpName()+",";
					grpIds.add(fileviewScope.getGrpId());
				}
			}else {
				grpType = -1;
				idType = "-1@0";
				scopeTypeSel="所有同事";
			}
		}else {
			grpType = 2;
			idType = "2@2";
			scopeTypeSel="我自己";
		}
		// 生成ztree所需数组格式字符串
		StringBuffer str = new StringBuffer(
				"[{id:-1, pId:0, name:'所有同事',ztype:'0'},");
		if (-1 == grpType) {
			str = new StringBuffer(
					"[{id:-1, pId:0, name:'所有同事',ztype:'0',checked:true},");
		}
		//选中上次使用过的数据
		if (null != listSelfGroup && listSelfGroup.size() > 0) {
			for (SelfGroup vo : listSelfGroup) {
				if (grpIds.contains(vo.getId())) {
					str.append("{id:" + vo.getId() + ", pId:0, name:'"
							+ vo.getGrpName() + "',ztype:'1',checked:true},");
				} else {
					str.append("{id:" + vo.getId() + ", pId:0, name:'"
							+ vo.getGrpName() + "',ztype:'1'},");
				}
			}
		}
		
		if (2 == grpType) {
			str.append("{id:2, pId:0, name:'我自己',ztype:'2',checked:true}]");
		} else {
			str.append("{id:2, pId:0, name:'我自己',ztype:'2'}]");
		}
		
		map.put("idType", idType);
		map.put("scopeTypeSel", scopeTypeSel);
		map.put("selfGroupStr", str.toString());
		
		return map;
	}

	/**
	 * 复制文件到数据库
	 * 
	 * @param logoFileIn
	 * @param uploadService
	 * @param user
	 * @param path
	 * @return
	 */
	public static Upfiles copyFile(File logoFileIn,
			UploadService uploadService, UserInfo user, String path) {
		File copyFile = null;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		
		String filePath = "";
		try {
			// 文件名
			String fileName = logoFileIn.getName();
			// 后缀
			String fileExt = FileUtil.getExtend(fileName);
			/* 如果不是允许的附件类型，直接返回 */
			if (!FileUtil.getAllowFileTypes().contains(fileExt)) {
			} else {
				String basepath = FileUtil.getUploadBasePath();
				/* 所有附件都保存到uploads 不存在则新增文件夹 */
				File f = new File(basepath);
				if (!f.exists()) {
					f.mkdir();
				}
				f = new File(basepath + path);
				if (!f.exists()) {
					f.mkdirs();
				}
				/* 每年一个文件夹 */
				path = path + File.separator
						+ DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy);
				f = new File(basepath + path);
				if (!f.exists()) {
					f.mkdir();
				}
				/* 每月一个文件夹 */
				path = path + File.separator
						+ DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
				f = new File(basepath + path);
				if (!f.exists()) {
					f.mkdir();
				}
				/* 每天一个文件夹 */
				path = path + File.separator
						+ DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
				f = new File(basepath + path);
				if (!f.exists()) {
					f.mkdir();
				}
				path = path + File.separator + UUIDGenerator.getUUID() + "."
						+ fileExt.toLowerCase();
				
				filePath = path;

				copyFile = new File(basepath + path);
				fi = new FileInputStream(logoFileIn);

				fo = new FileOutputStream(copyFile);

				in = fi.getChannel();// 得到对应的文件通道

				out = fo.getChannel();// 得到对应的文件通道

				in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			}
		} catch (Exception e) {

		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// 存放LOGO文件
		try {
			String md5 = FileMD5Util.getHash(copyFile.getAbsolutePath());
			Upfiles upfiles = uploadService.getFileByMD5(md5, user.getComId());
			if (null != upfiles) {
				String checkFilePath = FileUtil.getUploadBasePath()+upfiles.getFilepath();
				
				//数据库有数据，但是文件不存在
				File checkFile = new File(checkFilePath);
				if(!checkFile.exists()){
					//所在文件夹
					File dirFile = new File(path.substring(0, path.lastIndexOf("\\")));
					if(!dirFile.exists()){//文件夹不存在
						dirFile.mkdirs();
					}
					FileUtil.copyFile(new FileInputStream(logoFileIn), checkFilePath);
				}
				
				return upfiles;
			}
			// 后缀
			String fileExt = FileUtil.getExtend(copyFile.getName());
			String uuid = UUIDGenerator.getUUID();
			// 文件大小
			FileInputStream fis = new FileInputStream(copyFile);
			int newFileSize = fis.available();
			// 附件信息存库
			upfiles = new Upfiles();
			upfiles.setUuid(uuid);
			upfiles.setFilename(copyFile.getName());
			upfiles.setFilepath(filePath);
			upfiles.setFileExt(fileExt.toLowerCase());
			upfiles.setSizeb(newFileSize);
			String sizeM = MathExtend
					.divide(String.valueOf(upfiles.getSizeb()),
							String.valueOf(1024), 2);
			String dw = "K";
			if (Float.parseFloat(sizeM) > 1024) {
				sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
				dw = "M";
				if (Float.parseFloat(sizeM) > 1024) {
					sizeM = MathExtend.divide(sizeM, String.valueOf(1024), 2);
					dw = "G";
				}
			}
			upfiles.setSizem(sizeM + dw);
			upfiles.setComId(user.getComId());

			upfiles.setMd5(md5);

			Integer id = uploadService.addFile(upfiles);
			upfiles.setId(id);
//			// 保存附件二进制信息
//			FileData fileData = new FileData();
//			BeanUtilEx.copyIgnoreNulls(upfiles, fileData);
//			fileData.setUpfilesId(id);
//			fileData.setMd5(md5);
//			// 如果附件较大，存库耗时过长影响用户操作。存库的动作开启单独线程处理
//			new Thread(new FileInsertThread(uploadService, fileData)).start();

			return upfiles;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 系统字典表
	 * 
	 * @param type
	 *            数据类型
	 * @param code
	 *            数据类型唯一标识
	 * @return
	 */
	public static String dataDicZvalueByCode(String type, String code) {
		if (null == code){
			return null;
		}
		// 汇报进度具体化
		String zValue = null;
		List<DataDic> listTreeDataDic = DataDicContext.getInstance()
				.listTreeDataDicByType(type);
		if (null != listTreeDataDic && !listTreeDataDic.isEmpty()) {
			for (DataDic vo : listTreeDataDic) {
				if (null != vo.getCode() && null != code
						&& vo.getCode().equals(code)) {
					zValue = vo.getZvalue();
					break;
				}
			}
		}
		return zValue;
	}

	/**
	 * 表情包字符串生成
	 * 
	 * @return
	 */
	public static String biaoQingStr() {
		StringBuffer biaoQingStr = new StringBuffer();
		List<BiaoQing> biaoQingList = BiaoQingContext.getInstance().getBiaoQingList();
		if(null!=biaoQingList){
			for(BiaoQing vo:biaoQingList){
				biaoQingStr
				.append("<li><a href=\"#\"><img src=\""+vo.getImgPath()+"\" title=\""+vo.getImgDescribe()+"\" width=\"24\" height=\"24\" onclick=\"divClose('"+vo.getImgDescribe()+"','"+vo.getImgPath()+"');\"/></a></li>\n");
			}
		}
		return biaoQingStr.toString();
	}
	/**
	 * 构建附件上传控件
	 * @param name 控件关键字
	 * @param showName 显示值的关键字
	 * @param pifreamId 父页面iframe主键
	 * @param comId 企业编号
	 * @return
	 */
	public static String uploadFileTagStr(String name,String showName,String pifreamId,Integer comId,String sid){
		StringBuffer uploadFileTagStr = new StringBuffer();
		uploadFileTagStr.append("<div style=\"clear:both;width: 300px\" class=\"ws-process\">");
		 //用来存放文件信息
		uploadFileTagStr.append("	<div id=\"thelist"+name.replace(".", "_")+"\" class=\"uploader-list\"></div>");
		uploadFileTagStr.append("		<div class=\"btns btn-sm\">");
		uploadFileTagStr.append("		<div id=\"picker"+name.replace(".", "_")+"\">选择文件</div>");
		uploadFileTagStr.append("	</div>");
		    
		uploadFileTagStr.append("	<script type=\"text/javascript\">");
		uploadFileTagStr.append("		loadWebUpfiles('"+name.replace(".", "_")+"','"+sid+"',");
		uploadFileTagStr.append("'"+StringUtil.delNull(pifreamId)+"','picker"+name.replace(".", "_")+"','thelist"+name.replace(".", "_")+"','file"+name.replace(".", "_")+"');");
		uploadFileTagStr.append("	</script>");
		uploadFileTagStr.append("	<div style=\"position: relative; width: 350px; height: 90px;display: none\">");
		uploadFileTagStr.append("		<div style=\"float: left; width: <%=width%>\">");
		uploadFileTagStr.append("			<select listvalue=\""+showName+"\" id=\""+name.replace(".", "_")+"\" name=\""+name+"\" ondblclick=\"removeClick(this.id)\" multiple=\"multiple\" moreselect=\"true\" style=\"width: 100%; height: 90px;\">");
		uploadFileTagStr.append("			</select>");
		uploadFileTagStr.append("		</div>");
		uploadFileTagStr.append("	</div>");
		uploadFileTagStr.append("</div>");
		return uploadFileTagStr.toString();
	}

	/**
	 * 把bean的非空属性值连接成字符串 为String和Integer类型数据添加索引
	 * 
	 * @param source
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String objAttr2String(Object source, String str)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method[] sourceMethods = source.getClass().getMethods();
		StringBuffer newStr = new StringBuffer(null == str ? " ": str+",");
		for (int i = 0; i < sourceMethods.length; i++) {
			if (sourceMethods[i].getName().startsWith("get")) {
				Object value = sourceMethods[i].invoke(source); // 值
				// 为String和Integer类型数据添加索引
				if ((value instanceof String || value instanceof Integer)
						&& null != value && !"".equals(value) && !"0".equals(value)) {
					newStr.append(value+",");
				}
			}
		}
		return newStr.toString();
	}

	/**
	 * 索引集合公共部门创建
	 * 
	 * @param indexKey
	 *            索引主键
	 * 
	 * @param comId
	 *            企业编号
	 * @param busId
	 *            业务主键
	 * @param busType
	 *            业务类型
	 * @param busName
	 *            业务名称
	 * @param content
	 *            需创建的索引字符串
	 * @param createDate
	 *            数据存入数据库日期
	 * @return
	 */
	public static List<IndexDoc> toIndexDoc(String indexKey,Integer comId,Integer busId,
			String busType, String busName,String content,Date createDate) {
		if (null == indexKey || "".equals(indexKey) || null == comId || "".equals(comId) || null == busId || "".equals(busId)
				|| null == busType || "".equals(busType) || null == busName || "".equals(busName) 
				|| null == content || "".equals(content) || null == createDate || "".equals(createDate)) {
			return null;
		}
		List<IndexDoc> listIndexDoc = new ArrayList<IndexDoc>();
		IndexDoc doc = null;
		// 索引主键
		doc = new IndexDoc();
		doc.setColName("index_key");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(indexKey.toString());
		listIndexDoc.add(doc);
		// 企业编号
		doc = new IndexDoc();
		doc.setColName("comId");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(comId.toString());
		listIndexDoc.add(doc);
		// 业务主键
		doc = new IndexDoc();
		doc.setColName("busId");
		doc.setStore(true);
		doc.setType(IndexDoc.LONG);
		doc.setColValue(busId.toString());
		listIndexDoc.add(doc);
		// 业务类型
		doc = new IndexDoc();
		doc.setColName("busType");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(busType);
		listIndexDoc.add(doc);
		// 业务名称
		doc = new IndexDoc();
		doc.setColName("busName");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(busName);
		listIndexDoc.add(doc);
		//索引内容
		doc = new IndexDoc();
		doc.setColName("content");
		doc.setStore(false);
		doc.setType(IndexDoc.TEXT);
		doc.setColValue(content);
		listIndexDoc.add(doc);
		// 创建日期
		doc = new IndexDoc();
		doc.setColName("createDate");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		//DateTools.dateToString(createDate,DateTools.Resolution.DAY)
		doc.setColValue(DateTimeUtil.formatDate(createDate,DateTimeUtil.yyyyMMdd));
		listIndexDoc.add(doc);
		return listIndexDoc;
		
	}
	/**
	 * 索引集合公共部门创建
	 * 
	 * @param indexKey
	 *            索引主键
	 * 
	 * @param comId
	 *            企业编号
	 * @param busId
	 *            业务主键
	 * @param busType
	 *            业务类型
	 * @param busName
	 *            业务名称
	 * @param content
	 *            需创建的索引字符串
	 * @param createDate
	 *            数据存入数据库日期
	 * @param relatedBusId
	 *            关联模块主键
	 * @param relatedBusType
	 *            关联模块类型
	 * @return
	 */
	public static List<IndexDoc> toIndexDoc(String indexKey,Integer comId,Integer busId,
			String busType, String busName,String content,Date createDate,Integer relatedBusId,String relatedBusType) {
		if (null == indexKey || "".equals(indexKey) || null == comId || "".equals(comId) || null == busId || "".equals(busId)
				|| null == busType || "".equals(busType) || null == busName || "".equals(busName) 
				|| null == content || "".equals(content) || null == createDate || "".equals(createDate) 
				|| null == relatedBusId || "".equals(relatedBusId) || null == relatedBusType || "".equals(relatedBusType)) {
			return null;
		}
		List<IndexDoc> listIndexDoc = new ArrayList<IndexDoc>();
		IndexDoc doc = null;
		// 索引主键
		doc = new IndexDoc();
		doc.setColName("index_key");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(indexKey.toString());
		listIndexDoc.add(doc);
		// 企业编号
		doc = new IndexDoc();
		doc.setColName("comId");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(comId.toString());
		listIndexDoc.add(doc);
		// 业务主键
		doc = new IndexDoc();
		doc.setColName("busId");
		doc.setStore(true);
		doc.setType(IndexDoc.LONG);
		doc.setColValue(busId.toString());
		listIndexDoc.add(doc);
		// 业务类型
		doc = new IndexDoc();
		doc.setColName("busType");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(busType);
		listIndexDoc.add(doc);
		// 业务名称
		doc = new IndexDoc();
		doc.setColName("busName");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(busName);
		listIndexDoc.add(doc);
		//索引内容
		doc = new IndexDoc();
		doc.setColName("content");
		doc.setStore(false);
		doc.setType(IndexDoc.TEXT);
		doc.setColValue(content);
		listIndexDoc.add(doc);
		// 创建日期
		doc = new IndexDoc();
		doc.setColName("createDate");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(DateTools.dateToString(createDate,DateTools.Resolution.DAY));
		listIndexDoc.add(doc);
		// 关联业务主键
		doc = new IndexDoc();
		doc.setColName("relatedBusId");
		doc.setStore(true);
		doc.setType(IndexDoc.LONG);
		doc.setColValue(relatedBusId.toString());
		listIndexDoc.add(doc);
		// 关联业务类型
		doc = new IndexDoc();
		doc.setColName("relatedBusType");
		doc.setStore(true);
		doc.setType(IndexDoc.STRING);
		doc.setColValue(relatedBusType);
		listIndexDoc.add(doc);
		return listIndexDoc;
		
	}
	/**
	 * 创建模块分享的信息
	 * @param idType 分组主键和类型
	 * @param type 业务类型
	 * @param content 业务内容
	 * @param userInfo 
	 * @return
	 */
	public static MsgShare getMsgShare(String idType, String type,String content, UserInfo userInfo) {
		MsgShare msgShare = null;
		if(null!=idType && !StringUtil.isBlank(idType)){
			String[] groupIdS = StringUtil.split(idType, ",");
			if(null!=groupIdS && groupIdS.length>0){
				msgShare = new MsgShare();
				List<ShareGroup> listShareGroup = new ArrayList<ShareGroup>();
				//分享类型和范围
				for(String str :groupIdS){
					if("0".equals(str.split("@")[1])){
						//所有人
						msgShare.setScopeType(ALL_COLLEAGUE);
						break;
					}else if("2".equals(str.split("@")[1])){
						//自己
						msgShare.setScopeType(MYSELF);
						break;
					}else{
						//指定分组
						msgShare.setScopeType(BY_GROUPID);
						ShareGroup shareGroup = new ShareGroup();
						shareGroup.setComId(userInfo.getComId());
						shareGroup.setGrpId(Integer.parseInt(str.split("@")[0]));
						listShareGroup.add(shareGroup);
					}
				}
				//公司
				msgShare.setComId(userInfo.getComId());
				//内容
				msgShare.setContent(content);
				//类型
				msgShare.setType(type);
				//创建人
				msgShare.setCreator(userInfo.getId());
				//分享范围
				msgShare.setShareGroup(listShareGroup);
				
			}
		}
		return msgShare;
	}

	/**
	 * 取得客戶端地址
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("127.0.0.1")) {
			// 根据网卡取本机配置的IP
			InetAddress inet = null;
			try {
				inet = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			ip = inet.getHostAddress();
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
	     if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15   
	         if(ip.indexOf(",")>0){   
	             ip = ip.substring(0,ip.indexOf(","));   
	         }   
	     }
	     return ip;
	}

	/**
	 * 取得最近一次提醒时间
	 * @param clockDate 定时日期
	 * @param clockTime 定时时间
	 * @param clockRepType 周期类型
	 * @param clockRepDate 周期时间
	 * @return
	 */
	public static String getRecentClockDate(String clockDate, String clockTime,
			String clockRepType, String clockRepDate) {
		String clockNextDate = "";
		//开始时间
		Long clockDateTimeL = DateTimeUtil.parseDate(clockDate+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
		//当前时间
		Long nowDateTime = System.currentTimeMillis();
		//当前时间所在日期
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		//当前时间所在年
		Integer nowYear = DateTimeUtil.getYear();
		//当前时间所在月
		Integer nowMonth = DateTimeUtil.getMonth()+1;
		//当前时间所在周的时间
		Integer nowWeekDay = DateTimeUtil.getDay();
		
		if("0".equals(clockRepType)){//仅一次
			clockNextDate = clockDate+" "+clockTime;
		}else if("1".equals(clockRepType)){//每天
			if(clockDateTimeL<nowDateTime){//开始时间小于当前时间，则开始时间从当前开始
				//今天的时间
				Long nowDateTimeL = DateTimeUtil.parseDate(nowDate+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
				//今天的时间是否已过
				if(nowDateTimeL<nowDateTime){//今天时间已过
					//加一天
					clockDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
				}else{
					//就今天
					clockDate = nowDate;
				}
			}
			clockNextDate = clockDate+" "+clockTime;
		}else if("2".equals(clockRepType)){//每周
			if(null!=clockRepDate && clockRepDate.length()>0){
				//当前时间所在周数是否满足条件
				if(clockRepDate.indexOf(nowWeekDay.toString())>=0){
					//今天的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(nowDate+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//今天时间已过
						//加一天
						nowDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
						
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(DateTimeUtil.parseDate(nowDate, DateTimeUtil.yyyy_MM_dd).getTime());
						nowWeekDay = DateTimeUtil.getDay(c);
					}
				}
				
				while(clockRepDate.indexOf(nowWeekDay.toString())<0){
					//加一天
					nowDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
					
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(DateTimeUtil.parseDate(nowDate, DateTimeUtil.yyyy_MM_dd).getTime());
					nowWeekDay = DateTimeUtil.getDay(c);
				}
				
				clockDate = nowDate;
			}
			clockNextDate = clockDate+" "+clockTime;
		}else if("3".equals(clockRepType)){//每月
			//所在月份
			String monthDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM);
			//本月执行时间
			String thisMonthDay = monthDate+"-"+clockRepDate;
			if("29".equals(clockRepDate)){//设定每月29日执行
				if(2==nowMonth){//本月为2月
					thisMonthDay = DateTimeUtil.addDate(nowYear+"-"+3+"-"+"1", DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_MONTH, -1);
					//本月执行的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//本月时间已过
						clockDate =nowYear+"-"+"3"+"-"+29;
					}else{
						clockDate = thisMonthDay;
					}
				}else{
					//本月执行的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//本月时间已过
						clockDate = DateTimeUtil.addDate(thisMonthDay, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
					}else{
						clockDate = thisMonthDay;
					}
				}
			}else if("30".equals(clockRepDate)){//设定每月30日执行
				if(2==nowMonth){//本月为2月
					thisMonthDay = DateTimeUtil.addDate(nowYear+"-"+3+"-"+"1", DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_MONTH, -1);
					//本月执行的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//本月时间已过
						clockDate =nowYear+"-"+"3"+"-"+30;
					}else{
						clockDate = thisMonthDay;
					}
				}else{
					//本月执行的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//本月时间已过
						clockDate = DateTimeUtil.addDate(thisMonthDay, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
					}else{
						clockDate = thisMonthDay;
					}
				}
			}else if("31".equals(clockRepDate)){//设定每月31日执行
				if(2==nowMonth){//本月为2月
					thisMonthDay = DateTimeUtil.addDate(nowYear+"-"+(nowMonth+1)+"-"+"1", DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_MONTH, -1);
					//本月执行的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//本月时间已过
						clockDate =nowYear+"-"+"3"+"-"+31;
					}else{
						clockDate = thisMonthDay;
					}
				}else if( 4==nowMonth ||  6==nowMonth || 9==nowMonth || 11==nowMonth){//本月月小
					//月份最后一天
					thisMonthDay = DateTimeUtil.addDate(nowYear+"-"+(nowMonth+1)+"-"+"1", DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_MONTH, -1);
					//本月执行的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//本月时间已过
						clockDate =nowYear+"-"+(nowMonth+1)+"-"+31;
					}else{
						clockDate = thisMonthDay;
					}
				}else{//本月月大
					//本月执行的时间
					Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
					if(nowDateTimeL<nowDateTime){//本月时间已过
						clockDate = DateTimeUtil.addDate(thisMonthDay, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
					}else{
						clockDate = thisMonthDay;
					}
				}
			}else{//非特殊时间
				//本月执行的时间
				Long nowDateTimeL = DateTimeUtil.parseDate(thisMonthDay+" "+clockTime+":00", DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getTime();
				if(nowDateTimeL<nowDateTime){//本月时间已过
					clockDate = DateTimeUtil.addDate(thisMonthDay, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
				}else{
					clockDate = thisMonthDay;
				}
			}
			clockNextDate = clockDate+" "+clockTime;
		}else if("4".equals(clockRepType)){//每年
			//开始时间小于当前时间，则开始时间从当前开始
			if(clockDateTimeL<nowDateTime){
				//加一年
				clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.YEAR, 1);
			}
			clockNextDate = clockDate+" "+clockTime;
		}
		return clockNextDate;
	}
	/**
	 * 取得会议最近一次召开时间
	 * @param meeting
	 * @param meetRegular
	 * @return
	 */
	public static Map<String, String> getMeetThisDate(Meeting meeting,
			MeetRegular meetRegular) {
		//会议的开始时间
		String meetStartDateStr = meeting.getStartDate();
		//会议的结束时间
		String meetEndDateStr = meeting.getEndDate();
		//会议周期类型
		String meetingType = meeting.getMeetingType();
		
		//周期开始时间
		String regStartDateStr = meetRegular.getStartDate();
		//周期截止时间
		//String regEndDateStr = meetRegular.getEndDate();
		
		//重复时间
		String regularDate = meetRegular.getRegularDate();
		
		//会议开始时间
		String resStartDateStr = "";
		//会议截止时间
		String resEndDateStr = "";
		
		//开始时间
		Long meetStartTimeL = DateTimeUtil.parseDate(meetStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime();
		//截止时间
		Long meetEndTimeL = DateTimeUtil.parseDate(meetEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime();
		//时间差
		Long timeDiff = meetEndTimeL-meetStartTimeL;
		
		//开始日期
		//String startDateStr = meetStartDateStr.substring(0,10);
		//开始时间
		String startTimeStr = meetStartDateStr.substring(11,meetStartDateStr.length());
//		Date nowDate = new Date();
		Date nowDate = DateTimeUtil.parseDate("2016-01-12", DateTimeUtil.yyyy_MM_dd);
		if(meetingType.equals("1")){//每天
			resStartDateStr = regStartDateStr+" "+startTimeStr;
			Date resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			while(nowDate.getTime()>=resStartDate.getTime()){
				//会议下次开始时间
				resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
				Calendar.DAY_OF_YEAR, Integer.parseInt(regularDate));
				resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			}
			Long resEndDateL =  DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime()+timeDiff;
			resEndDateStr = DateTimeUtil.formatDate(new Date(resEndDateL), DateTimeUtil.yyyy_MM_dd_HH_mm);
		}else if(meetingType.equals("2")){//每周
			resStartDateStr = regStartDateStr+" "+startTimeStr;
			Date resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			Calendar c =Calendar.getInstance();
			c.setTime(resStartDate);
			Integer meetWeek = DateTimeUtil.getDay(c);
			while(!meetWeek.equals(Integer.parseInt(regularDate))){
				//会议下次开始时间
				resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
						Calendar.DAY_OF_YEAR, 1);
				resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				c =Calendar.getInstance();
				c.setTime(resStartDate);
				meetWeek = DateTimeUtil.getDay(c);
			}
			while(nowDate.getTime()>=resStartDate.getTime()){
				//会议下次开始时间
				resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
				Calendar.DAY_OF_YEAR, 7);
				resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			}
			Long resEndDateL =  DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime()+timeDiff;
			resEndDateStr = DateTimeUtil.formatDate(new Date(resEndDateL), DateTimeUtil.yyyy_MM_dd_HH_mm);
		}else if(meetingType.equals("3")){//每月
			resStartDateStr = regStartDateStr.substring(0,8)+regularDate+" "+startTimeStr;
			Date resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			Calendar cal =Calendar.getInstance();
			cal.setTime(resStartDate);
			
			//时间所在年
			Integer nowYear = cal.get(Calendar.YEAR);
			//时间所在月
			Integer nowMonth = Integer.parseInt(resStartDateStr.substring(5,7));
			if("29".equals(regularDate)){//设定每月29日执行
				if(2==nowMonth){
					resStartDateStr = nowYear+"-3-1 "+startTimeStr;
					resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
							Calendar.DAY_OF_YEAR, -1);
				}
			}else if("30".equals(regularDate)){
				if(2==nowMonth){
					resStartDateStr = nowYear+"-3-1 "+startTimeStr;
					resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
							Calendar.DAY_OF_YEAR, -1);
				}
			}else if("31".equals(regularDate)){
				if(2==nowMonth){
					resStartDateStr = nowYear+"-3-1 "+startTimeStr;
					resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
							Calendar.DAY_OF_YEAR, -1);
				}else if(4==nowMonth ||  6==nowMonth || 9==nowMonth || 11==nowMonth){
					resStartDateStr = nowYear+"-"+(nowMonth)+"-30 "+startTimeStr;
				}
			}
			resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			cal =Calendar.getInstance();
			cal.setTime(resStartDate);
			//时间所在年
			nowYear = cal.get(Calendar.YEAR);
			//时间所在月
			nowMonth = cal.get(Calendar.MONTH)+1;
			Date regDate = DateTimeUtil.parseDate(regStartDateStr+" "+startTimeStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			while(nowDate.getTime()>=resStartDate.getTime() || regDate.getTime()>= resStartDate.getTime()){
				if("29".equals(regularDate)){//设定每月29日执行
					if(2==nowMonth){
						resStartDateStr = nowYear+"-3-29 "+startTimeStr;
					}else{
						//加一月
						resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;;
					}
				}else if("30".equals(regularDate)){
					if(2==nowMonth){
						resStartDateStr = nowYear+"-3-30 "+startTimeStr;
					}else{
						//加一月
						resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;
					}
				}else if("31".equals(regularDate)){
					if(2==nowMonth){
						resStartDateStr = nowYear+"-3-30 "+startTimeStr;
					}else if(4==nowMonth ||  6==nowMonth || 9==nowMonth || 11==nowMonth){
						//加一月
						resStartDateStr = nowYear+"-"+(nowMonth+1)+"-31 "+startTimeStr;
					}else{
						//加一月
						resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;
					}
				}else{
					//加一月
					resStartDateStr = DateTimeUtil.addDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;
				}
				
				resStartDate = DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
				cal =Calendar.getInstance();
				cal.setTime(resStartDate);
				//时间所在年
				nowYear = cal.get(Calendar.YEAR);
				//时间所在月
				nowMonth = cal.get(Calendar.MONTH)+1;
			}
			Long resEndDateL =  DateTimeUtil.parseDate(resStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime()+timeDiff;
			resEndDateStr = DateTimeUtil.formatDate(new Date(resEndDateL), DateTimeUtil.yyyy_MM_dd_HH_mm);
		}
		Map<String, String> map = new HashMap<String, String>();
		//判断安徽一的开始时间是否
		if(null!=resStartDateStr && !"".equals(resStartDateStr)//下次开始时间不为空
				&& null!=resEndDateStr && !"".equals(resEndDateStr)){//下次结束时间不为空
			map.put("startDate", resStartDateStr);
			map.put("endDate", resEndDateStr);
		}
		return map;
	}
	/**
	 * 取得会议下次执行的开始时间和结束时间
	 * @param meeting 需要的是会议开始时间，会议结束时间，会议周期类型
	 * @param meetRegular 
	 * @return
	 */
	public static Map<String, String> getMeetNextDate(Meeting meeting,MeetRegular meetRegular) {
		//会议的开始时间
		String meetStartDateStr = meeting.getStartDate();
		//会议的结束时间
		String meetEndDateStr = meeting.getEndDate();
		//会议周期类型
		String meetingType = meeting.getMeetingType();
		//周期截止时间
		String regEndDateStr = meetRegular.getEndDate();
		//重复时间
		String regularDate = meetRegular.getRegularDate();
		
		//会议下次开始时间
		String resStartDate = "";
		//会议下次截止时间
		String resEndDate = "";
		if(meetingType.equals("1")){//每天
			//会议下次开始时间
			resStartDate = DateTimeUtil.addDate(meetStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
					Calendar.DAY_OF_YEAR, Integer.parseInt(regularDate));
			//会议下次截止时间
			resEndDate = DateTimeUtil.addDate(meetEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
					Calendar.DAY_OF_YEAR, Integer.parseInt(regularDate));
		}else if(meetingType.equals("2")){//每周
			//会议下次开始时间
			resStartDate = DateTimeUtil.addDate(meetStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
					Calendar.DAY_OF_YEAR, 7);
			//会议下次截止时间
			resEndDate = DateTimeUtil.addDate(meetEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm,
					Calendar.DAY_OF_YEAR, 7);
		}else if(meetingType.equals("3")){//每月
			
			//开始时间
			Long meetStartTimeL = DateTimeUtil.parseDate(meetStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime();
			//截止时间
			Long meetEndTimeL = DateTimeUtil.parseDate(meetEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime();
			//时间差
			Long timeDiff = meetEndTimeL-meetStartTimeL;
			
			//开始日期
			String startDateStr = meetStartDateStr.substring(0,10);
			//开始时间
			String startTimeStr = meetStartDateStr.substring(11,meetStartDateStr.length());
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd).getTime());
			//当前时间所在年
			Integer nowYear = cal.get(Calendar.YEAR);
			//当前时间所在月
			Integer nowMonth = cal.get(Calendar.MONTH)+1;
			
			if("29".equals(regularDate)){//设定每月29日执行
				if(2==nowMonth){
					resStartDate = nowYear+"-3-29 "+startTimeStr;
				}else{
					//加一月
					resStartDate = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;;
				}
			}else if("30".equals(regularDate)){
				if(2==nowMonth){
					resStartDate = nowYear+"-3-30 "+startTimeStr;
				}else{
					//加一月
					resStartDate = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;
				}
			}else if("31".equals(regularDate)){
				if(2==nowMonth){
					resStartDate = nowYear+"-3-30 "+startTimeStr;
				}else if(4==nowMonth ||  6==nowMonth || 9==nowMonth || 11==nowMonth){
					//加一月
					resStartDate = nowYear+"-"+(nowMonth+1)+"-31 "+startTimeStr;
				}else{
					//加一月
					resStartDate = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;
				}
			}else{
				//加一月
				resStartDate = DateTimeUtil.addDate(startDateStr, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1)+" "+startTimeStr;
			}
			Long resEndDateL =  DateTimeUtil.parseDate(resStartDate, DateTimeUtil.yyyy_MM_dd_HH_mm).getTime()+timeDiff;
			resEndDate = DateTimeUtil.formatDate(new Date(resEndDateL), DateTimeUtil.yyyy_MM_dd_HH_mm);
		}
		Map<String, String> map = null;
		//判断安徽一的开始时间是否
		if(null!=resStartDate && !"".equals(resStartDate)//下次开始时间不为空
				&& null!=resEndDate && !"".equals(resEndDate)){//下次结束时间不为空
			Date regularEndDate = DateTimeUtil.parseDate(regEndDateStr+" 00:01", DateTimeUtil.yyyy_MM_dd_HH_mm);
			Date meetNextStartDate = DateTimeUtil.parseDate(resStartDate.substring(0,10)+" 00:00", DateTimeUtil.yyyy_MM_dd_HH_mm);
			if(meetNextStartDate.before(regularEndDate)){//会议下次开始时间在周期截止时间之前
				map = new HashMap<String, String>();
				map.put("startDate", resStartDate);
				map.put("endDate", resEndDate);
			}
		}
		
		return map;
	}
	/**
	 * 取得闹铃下次执行时间
	 * @param clockThisDate 本次提醒日期时间
	 * @param clockRepType 周期类型
	 * @param clockRepDate 周期时间
	 * @param clockTime 提醒时间
	 * @return
	 */
	public static String getClockNextStartDate(String clockThisDate,
			String clockRepType, String clockRepDate, String clockTime) {
		String clockNextDate = "";
		//只取时间
		String clockDate = clockThisDate.substring(0,10);
		if("0".equals(clockRepType)){//仅一次
			clockNextDate = clockThisDate;//不提醒
		}else if("1".equals(clockRepType)){//每天
			clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
			clockNextDate = clockDate+" "+clockTime;
		}else if("2".equals(clockRepType)){//每周
			//加一天
			clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(DateTimeUtil.parseDate(clockDate, DateTimeUtil.yyyy_MM_dd).getTime());
			Integer nowWeekDay = DateTimeUtil.getDay(cal);
			
			while(clockRepDate.indexOf(nowWeekDay.toString())<0){
				//加一天
				clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
				
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(DateTimeUtil.parseDate(clockDate, DateTimeUtil.yyyy_MM_dd).getTime());
				nowWeekDay = DateTimeUtil.getDay(c);
			}
			clockNextDate = clockDate+" "+clockTime;
			
		}else if("3".equals(clockRepType)){//每月
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(DateTimeUtil.parseDate(clockDate, DateTimeUtil.yyyy_MM_dd).getTime());
			//当前时间所在年
			Integer nowYear = cal.get(Calendar.YEAR);
			//当前时间所在月
			Integer nowMonth = cal.get(Calendar.MONTH)+1;
			
			if("29".equals(clockRepDate)){//设定每月29日执行
				if(2==nowMonth){
					clockNextDate = nowYear+"-3-29 "+clockTime;
				}else{
					//加一月
					clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
					clockNextDate = clockDate+" "+clockTime;
				}
			}else if("30".equals(clockRepDate)){
				if(2==nowMonth){
					clockNextDate = nowYear+"-3-30 "+clockTime;
				}else{
					//加一月
					clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
					clockNextDate = clockDate+" "+clockTime;
				}
			}else if("31".equals(clockRepDate)){
				if(2==nowMonth){
					clockNextDate = nowYear+"-3-30 "+clockTime;
				}else if(4==nowMonth ||  6==nowMonth || 9==nowMonth || 11==nowMonth){
					//加一月
					clockNextDate = nowYear+"-"+(nowMonth+1)+"-31 "+clockTime;
				}else{
					//加一月
					clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
					clockNextDate = clockDate+" "+clockTime;
				}
			}else{
				//加一月
				clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.MONTH, 1);
				clockNextDate = clockDate+" "+clockTime;
			}
		}else if("4".equals(clockRepType)){//每年
			//加一年
			clockDate = DateTimeUtil.addDate(clockDate, DateTimeUtil.yyyy_MM_dd, Calendar.YEAR, 1);
			clockNextDate = clockDate+" "+clockTime;
		}
		return clockNextDate;
	}

	/**
	 * 计算日程时间
	 * @param schedule
	 * @return
	 */
	public static Map<String, String> getSchedulDateTime(Schedule schedule) {
		Map<String, String> map = new HashMap<String, String>();
		//日程设置时间起
		String scheStartDate = schedule.getScheStartDate();
		//日程设置时间止
		String scheEndDate = schedule.getScheEndDate();
		
		//日程执行时间起
		String dataTimeS = scheStartDate;
		if(scheStartDate.indexOf(":")>0){
			dataTimeS = dataTimeS+":00";
		}else{
			dataTimeS = dataTimeS+" 00:00:00";
		}
		//日程执行时间止
		String dataTimeE = scheEndDate;
		if(scheEndDate.indexOf(":")>0){
			dataTimeE = dataTimeE+":00";
		}else{
			dataTimeE = dataTimeE+" 23:59:59";
		}
		
		//是否重复
		Integer isRepeat = schedule.getIsRepeat();
		if(isRepeat.equals(1)){//需要重复执行
			//日程结束类型
			String repEndType = schedule.getRepEndType();
			//日程结束类型
			String repEndDate = schedule.getRepEndDate();
			//日程重复类型
			String repType = schedule.getRepType();
			//重复时间
			String repDate = schedule.getRepDate();
			if(repEndType.equals("1")){//按次数截至
				if(repType.equals("1")){//每天
					//repDate天一次，执行repEndDate次
					Integer repTime = Integer.parseInt(repDate)*Integer.parseInt(repEndDate);
					//执行时间止
					dataTimeE = DateTimeUtil.addDate(dataTimeE, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, 
							Calendar.DAY_OF_YEAR, repTime);
					
				}else if(repType.equals("2")){//每周
					String[] weekDays = repDate.split(" ");
					Integer weekDay = Integer.parseInt(weekDays[weekDays.length-1]);
					
					Calendar c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(dataTimeE, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					Integer dataTimeEWeek = DateTimeUtil.getDay(c);
					while(!weekDay.equals(dataTimeEWeek)){
						//执行时间止
						dataTimeE = DateTimeUtil.addDate(dataTimeE, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, 
								Calendar.DAY_OF_YEAR, 1);
						c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(dataTimeE, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						dataTimeEWeek = DateTimeUtil.getDay(c);
					}
					
					Integer repTime = Integer.parseInt(repEndDate);
					//执行时间止
					dataTimeE = DateTimeUtil.addDate(dataTimeE, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, 
							Calendar.DAY_OF_YEAR, repTime*7);
				}else if(repType.equals("3")){//每月
					//repDate月执行一次，执行repEndDate次
					Integer repTime = Integer.parseInt(repDate)*Integer.parseInt(repEndDate);
					//执行时间止
					dataTimeE = DateTimeUtil.addDate(dataTimeE, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, 
							Calendar.MONTH, repTime);
				}else if(repType.equals("4")){//每年
					//repDate年执行一次，执行repEndDate次
					Integer repTime = Integer.parseInt(repDate)*Integer.parseInt(repEndDate);
					//执行时间止
					dataTimeE = DateTimeUtil.addDate(dataTimeE, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, 
							Calendar.YEAR, repTime);
				}
				
			}else if(repEndType.equals("2")){//按时间截止
				dataTimeE = repEndDate+":00";
			}
		}
		map.put("dataTimeS",dataTimeS);
		map.put("dataTimeE", dataTimeE);
		return map;
	}

	/**
	 * 整理日程(重复)需要展示的数据
	 * @param schedule 日程数据
	 * @param startDateStr 展示页面开始时间
	 * @param endDateStr  展示页面结束时间
	 * @return
	 */
	public static List<Schedule> formatSchedule(Schedule schedule, String startDateStr, String endDateStr) {
		List<Schedule> schedules = new ArrayList<Schedule>();
		// 展示页面开始时间
		Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		// 展示页面结束时间
		Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//日程设置时间起
		String scheStartDateStr = schedule.getScheStartDate();
		//日程设置时间止
		String scheEndDateStr = schedule.getScheEndDate();
		//用于设置返回值的
		boolean flag = scheStartDateStr.indexOf(":")>0;
		//补全时间
		if(flag){
			scheStartDateStr = scheStartDateStr+":00";
			scheEndDateStr = scheEndDateStr+":00";
		}else{
			scheStartDateStr = scheStartDateStr+" 00:00:00";
			scheEndDateStr = scheEndDateStr+" 23:59:59";
		}
		//日程时间起转时间
		Date scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//日程时间止转时间
		Date scheEndDate = DateTimeUtil.parseDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
		//起止时间差
		Long timeDiff = scheEndDate.getTime()-scheStartDate.getTime();
		//开始时间是否在页面展示区域
		boolean isStartInPage = DateTimeUtil.isBetween(startDate, endDate,scheStartDate);
		//结束类型
		String repEndType = schedule.getRepEndType();
		//重复类型
		String repType = schedule.getRepType();
		
		if(repEndType.equals("0")){//永不结束
			if(repType.equals("1")){//每天
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				if(isStartInPage){//开始时间在页面上
					while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
						Schedule scheObj = new Schedule();
						BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
						if(flag){//需要长时间格式
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						//添加返回值
						schedules.add(scheObj);
						//重新设置开始时间
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.DAY_OF_YEAR, repTime);
						//重新设置结束时间
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.DAY_OF_YEAR, repTime);
						
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					}
				}else{//开始时间不在页面
					if(!scheEndDate.after(startDate)){
						//开始时间从页面开始
						scheStartDate = new Date(startDate.getTime()+1);
						//日程开始时间
						scheStartDateStr = startDateStr;
						//日程结束时间
						scheEndDateStr = DateTimeUtil.formatDate(new Date(startDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						if(flag){//需要长时间格式
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, repTime);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, repTime);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
					}
				}
			}else if(repType.equals("2")){//每周
				//每周指定星期数执行
				String[] repTimes = schedule.getRepDate().split(" ");
				String tempStart = scheStartDateStr;
				String tempEnd = scheEndDateStr;
				for (String weekNumStr : repTimes) {//指定的星期数
					Integer weekNum = Integer.parseInt(weekNumStr);
					scheStartDateStr = tempStart;
					scheEndDateStr = tempEnd;
					if(isStartInPage){//开始时间在页面上
						//开始时间在页面上的星期数
						Calendar c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						Integer dataTimeEWeek = DateTimeUtil.getDay(c);
						while(!weekNum.equals(dataTimeEWeek)){//不是指定的星期数
							//设置的开始时间加一天
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
							//设置的结束时间加一天
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
							//加一天后的星期数
							c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
							dataTimeEWeek = DateTimeUtil.getDay(c);
						}
						//设置的开始时间的日期
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, 7);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, 7);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
						
					}else{
						if(!scheEndDate.after(startDate)){
							//开始时间从页面开始
							scheStartDate = new Date(startDate.getTime()+1);
							//日程开始时间
							scheStartDateStr = startDateStr;
							//日程结束时间
							scheEndDateStr = DateTimeUtil.formatDate(new Date(startDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
							if(flag){//需要长时间格式
								schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//开始时间在页面上的星期数
							Calendar c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
							Integer dataTimeEWeek = DateTimeUtil.getDay(c);
							while(!weekNum.equals(dataTimeEWeek)){//不是指定的星期数
								//设置的开始时间加一天
								scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
								//设置的结束时间加一天
								scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
								//加一天后的星期数
								c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
								dataTimeEWeek = DateTimeUtil.getDay(c);
							}
							//设置的开始时间的日期
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
							while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
								Schedule scheObj = new Schedule();
								BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
								if(flag){//需要长时间格式
									scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
									scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
								}else{
									scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
									scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
								}
								//添加返回值
								schedules.add(scheObj);
								//重新设置开始时间
								scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
										Calendar.DAY_OF_YEAR, 7);
								//重新设置结束时间
								scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
										Calendar.DAY_OF_YEAR, 7);
								
								scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
							}
						}
					}
				}
			}else if(repType.equals("3")){//每月
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				if(isStartInPage){//开始时间在页面上
					while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
						Schedule scheObj = new Schedule();
						BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
						if(flag){//需要长时间格式
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						//添加返回值
						schedules.add(scheObj);
						//重新设置开始时间
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.MONTH, repTime);
						//重新设置结束时间
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.MONTH, repTime);
						
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					}
				}else{//开始时间不在页面
					if(!scheEndDate.after(startDate)){
						//日程开始时间
						scheStartDateStr = startDateStr.subSequence(0, 7)+scheStartDateStr.substring(7, scheStartDateStr.length());
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						//开始时间从页面开始
						scheStartDate = new Date(scheStartDate.getTime()+1);
						//日程结束时间
						scheEndDateStr = DateTimeUtil.formatDate(new Date(scheStartDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						if(flag){//需要长时间格式
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.MONTH, repTime);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.MONTH, repTime);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
					}
				}
			
			}else if(repType.equals("4")){//每年
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				if(isStartInPage){//开始时间在页面上
					while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
						Schedule scheObj = new Schedule();
						BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
						if(flag){//需要长时间格式
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						//添加返回值
						schedules.add(scheObj);
						//重新设置开始时间
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.YEAR, repTime);
						//重新设置结束时间
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.YEAR, repTime);
						
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					}
				}else{
					if(!scheEndDate.after(startDate)){
						//日程开始时间
						scheStartDateStr = startDateStr.subSequence(0,4)+scheStartDateStr.substring(4, scheStartDateStr.length());
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						//开始时间从页面开始
						scheStartDate = new Date(scheStartDate.getTime()+1);
						//日程结束时间
						scheEndDateStr = DateTimeUtil.formatDate(new Date(scheStartDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						if(flag){//需要长时间格式
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						while(DateTimeUtil.isBetween(startDate, endDate,scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.YEAR, repTime);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.YEAR, repTime);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
					}
				}
			}
			
		}else if(repEndType.equals("1")){//按次数截至
			
			Integer count=0;
			
			if(repType.equals("1")){//每天
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				//执行次数
				Integer repEndTime = Integer.parseInt(schedule.getRepEndDate());
				
				while(count<=repEndTime){
					count++;
					Schedule scheObj = new Schedule();
					BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
					if(flag){//需要长时间格式
						scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
						scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
					}else{
						scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
						scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
					}
					//添加返回值
					schedules.add(scheObj);
					//重新设置开始时间
					scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
							Calendar.DAY_OF_YEAR, repTime);
					//重新设置结束时间
					scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
							Calendar.DAY_OF_YEAR, repTime);
					
					scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				}
			}else if(repType.equals("2")){//每周
				//每repTime天执行一次
				String[] repTimes = schedule.getRepDate().split(" ");
				//执行次数
				Integer repEndTime = Integer.parseInt(schedule.getRepEndDate());
				String tempStart = scheStartDateStr;
				String tempEnd = scheEndDateStr;
				for (String weekNumStr : repTimes) {//指定的星期数
					count=0;
					Integer weekNum = Integer.parseInt(weekNumStr);
					scheStartDateStr = tempStart;
					scheEndDateStr = tempEnd;
					//开始时间在页面上的星期数
					Calendar c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					Integer dataTimeEWeek = DateTimeUtil.getDay(c);
					while(!weekNum.equals(dataTimeEWeek)){//不是指定的星期数
						//设置的开始时间加一天
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
						//设置的结束时间加一天
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
						//加一天后的星期数
						c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						dataTimeEWeek = DateTimeUtil.getDay(c);
					}
					//设置的开始时间的日期
					scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					while(count<=repEndTime){
						count++;
						Schedule scheObj = new Schedule();
						BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
						if(flag){//需要长时间格式
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						//添加返回值
						schedules.add(scheObj);
						//重新设置开始时间
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.DAY_OF_YEAR, 7);
						//重新设置结束时间
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.DAY_OF_YEAR, 7);
						
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					}
				}
				
			}else if(repType.equals("3")){//每月
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				//执行次数
				Integer repEndTime = Integer.parseInt(schedule.getRepEndDate());
				while(count<=repEndTime){
					count++;
					Schedule scheObj = new Schedule();
					BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
					if(flag){//需要长时间格式
						scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
						scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
					}else{
						scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
						scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
					}
					//添加返回值
					schedules.add(scheObj);
					//重新设置开始时间
					scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
							Calendar.MONTH, repTime);
					//重新设置结束时间
					scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
							Calendar.MONTH, repTime);
					
					scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				}
				
			}else if(repType.equals("4")){//每年
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				//执行次数
				Integer repEndTime = Integer.parseInt(schedule.getRepEndDate());
				while(count<=repEndTime){
					count++;
					Schedule scheObj = new Schedule();
					BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
					if(flag){//需要长时间格式
						scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
						scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
					}else{
						scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
						scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
					}
					//添加返回值
					schedules.add(scheObj);
					//重新设置开始时间
					scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
							Calendar.YEAR, repTime);
					//重新设置结束时间
					scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
							Calendar.YEAR, repTime);
					
					scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
				}
			}
		}else if(repEndType.equals("2")){//按时间截至
			//截止时间字符串
			String repEndDateStr = schedule.getRepEndDate()+":01";
			//截止时间
			Date repEndDate = DateTimeUtil.parseDate(repEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
			if(repType.equals("1")){//每天
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				if(isStartInPage){//开始时间在页面上
					while(repEndDate.after(scheStartDate)){
						Schedule scheObj = new Schedule();
						BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
						if(flag){//需要长时间格式
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						//添加返回值
						schedules.add(scheObj);
						//重新设置开始时间
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.DAY_OF_YEAR, repTime);
						//重新设置结束时间
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.DAY_OF_YEAR, repTime);
						
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					}
				}else{
					if(!scheEndDate.after(startDate)){
						//开始时间从页面开始
						scheStartDate = new Date(startDate.getTime()+1);
						//日程开始时间
						scheStartDateStr = startDateStr;
						//日程结束时间
						scheEndDateStr = DateTimeUtil.formatDate(new Date(startDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						if(flag){//需要长时间格式
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						while(repEndDate.after(scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, repTime);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, repTime);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
					}
				}
			}else if(repType.equals("2")){//每周
				//每周指定星期数执行
				String[] repTimes = schedule.getRepDate().split(" ");
				String tempStart = scheStartDateStr;
				String tempEnd = scheEndDateStr;
				for (String weekNumStr : repTimes) {//指定的星期数
					Integer weekNum = Integer.parseInt(weekNumStr);
					scheStartDateStr = tempStart;
					scheEndDateStr = tempEnd;
					if(isStartInPage){//开始时间在页面上
						//开始时间在页面上的星期数
						Calendar c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
						Integer dataTimeEWeek = DateTimeUtil.getDay(c);
						while(!weekNum.equals(dataTimeEWeek)){//不是指定的星期数
							//设置的开始时间加一天
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
							//设置的结束时间加一天
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
							//加一天后的星期数
							c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
							dataTimeEWeek = DateTimeUtil.getDay(c);
						}
						//设置的开始时间的日期
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						while(repEndDate.after(scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, 7);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.DAY_OF_YEAR, 7);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
						
					}else{
						if(!scheEndDate.after(startDate)){
							//开始时间从页面开始
							scheStartDate = new Date(startDate.getTime()+1);
							//日程开始时间
							scheStartDateStr = startDateStr;
							//日程结束时间
							scheEndDateStr = DateTimeUtil.formatDate(new Date(startDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
							if(flag){//需要长时间格式
								schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//开始时间在页面上的星期数
							Calendar c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
							Integer dataTimeEWeek = DateTimeUtil.getDay(c);
							while(!weekNum.equals(dataTimeEWeek)){//不是指定的星期数
								//设置的开始时间加一天
								scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
								//设置的结束时间加一天
								scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, Calendar.DAY_OF_YEAR, 1);
								//加一天后的星期数
								c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
								dataTimeEWeek = DateTimeUtil.getDay(c);
							}
							//设置的开始时间的日期
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
							while(repEndDate.after(scheStartDate)){
								Schedule scheObj = new Schedule();
								BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
								if(flag){//需要长时间格式
									scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
									scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
								}else{
									scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
									scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
								}
								//添加返回值
								schedules.add(scheObj);
								//重新设置开始时间
								scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
										Calendar.DAY_OF_YEAR, 7);
								//重新设置结束时间
								scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
										Calendar.DAY_OF_YEAR, 7);
								
								scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
							}
						}
					}
				}
			
			}else if(repType.equals("3")){//每月
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				if(isStartInPage){//开始时间在页面上
					while(repEndDate.after(scheStartDate)){
						Schedule scheObj = new Schedule();
						BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
						if(flag){//需要长时间格式
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						//添加返回值
						schedules.add(scheObj);
						//重新设置开始时间
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.MONTH, repTime);
						//重新设置结束时间
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.MONTH, repTime);
						
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					}
				}else{//开始时间不在页面
					if(!scheEndDate.after(startDate)){
						//日程开始时间
						scheStartDateStr = startDateStr.subSequence(0, 7)+scheStartDateStr.substring(7, scheStartDateStr.length());
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						//开始时间从页面开始
						scheStartDate = new Date(scheStartDate.getTime()+1);
						//日程结束时间
						scheEndDateStr = DateTimeUtil.formatDate(new Date(scheStartDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						if(flag){//需要长时间格式
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						while(repEndDate.after(scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.MONTH, repTime);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.MONTH, repTime);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
					}
				}
			
			
			}else if(repType.equals("4")){//每年
				//每repTime天执行一次
				Integer repTime = Integer.parseInt(schedule.getRepDate());
				if(isStartInPage){//开始时间在页面上
					while(repEndDate.after(scheStartDate)){
						Schedule scheObj = new Schedule();
						BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
						if(flag){//需要长时间格式
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						//添加返回值
						schedules.add(scheObj);
						//重新设置开始时间
						scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.YEAR, repTime);
						//重新设置结束时间
						scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
								Calendar.YEAR, repTime);
						
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
					}
				}else{
					if(!scheEndDate.after(startDate)){
						//日程开始时间
						scheStartDateStr = startDateStr.subSequence(0,4)+scheStartDateStr.substring(4, scheStartDateStr.length());
						scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						//开始时间从页面开始
						scheStartDate = new Date(scheStartDate.getTime()+1);
						//日程结束时间
						scheEndDateStr = DateTimeUtil.formatDate(new Date(scheStartDate.getTime()+timeDiff-1), DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						if(flag){//需要长时间格式
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
						}else{
							schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
							schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
						}
						while(repEndDate.after(scheStartDate)){
							Schedule scheObj = new Schedule();
							BeanUtilEx.copyIgnoreNulls(schedule, scheObj);
							if(flag){//需要长时间格式
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
							}else{
								scheObj.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
								scheObj.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
							}
							//添加返回值
							schedules.add(scheObj);
							//重新设置开始时间
							scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.YEAR, repTime);
							//重新设置结束时间
							scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss,
									Calendar.YEAR, repTime);
							
							scheStartDate = DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
						}
					}
				}
			}
		}
		
		return schedules;
	}
	/**
	 * 若是按照周数重复的，则需要重置起止时间
	 * @param schedule
	 * @return
	 */
	public static Schedule resetSchedule(Schedule schedule) {
		String repType = schedule.getRepType();
		if(schedule.getIsRepeat()==1 && !StringUtil.isBlank(repType)&& repType.equals("2")){//按照每周重复的，需要重新设置起止时间
			//设置的开始时间
			String scheStartDateStr = schedule.getScheStartDate();
			//设置的结束时间
			String scheEndDateStr = schedule.getScheEndDate();
			//用于设置返回值的
			boolean flag = scheStartDateStr.indexOf(":")>0;
			//补全时间
			if(flag){
				scheStartDateStr = scheStartDateStr+":00";
				scheEndDateStr = scheEndDateStr+":00";
			}else{
				scheStartDateStr = scheStartDateStr+" 00:00:00";
				scheEndDateStr = scheEndDateStr+" 23:59:59";
			}
			//设置的周数
			List<String> repDates = Arrays.asList(schedule.getRepDate().split(" "));
			//设置第一个星期数
			Calendar c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
			Integer dataTimeEWeek = DateTimeUtil.getDay(c);
			if(!repDates.contains(dataTimeEWeek.toString())){//若是开始时间不在指定的星期数里面，则需要处理
				//需要添加的天数
				while(!repDates.contains(dataTimeEWeek.toString())){
					//执行时间止
					scheStartDateStr = DateTimeUtil.addDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, 
							Calendar.DAY_OF_YEAR, 1);
					scheEndDateStr = DateTimeUtil.addDate(scheEndDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss, 
							Calendar.DAY_OF_YEAR, 1);
					c = DateTimeUtil.date2Calendar(DateTimeUtil.parseDate(scheStartDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
					dataTimeEWeek = DateTimeUtil.getDay(c);
				}
			}
			if(flag){//需要长时间格式
				schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.lastIndexOf(":")));
				schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.lastIndexOf(":")));
			}else{
				schedule.setScheStartDate(scheStartDateStr.substring(0, scheStartDateStr.indexOf(" ")));
				schedule.setScheEndDate(scheEndDateStr.substring(0, scheEndDateStr.indexOf(" ")));
			}
			
		}
		return schedule;
	}
	/**
	 * 将字符串的首字母大写
	 * @param str
	 * @return
	 */
	public static String toFirstLetterUpperCase(String str) {  
	    if(str == null || str.length() < 2){  
	        return str;  
	    }  
	     String firstLetter = str.substring(0, 1).toUpperCase();  
	     return firstLetter + str.substring(1, str.length());  
	 }  
	/**
	 * 取得App的下载地址
	 * @return
	 */
	public static String getAppLoadUrl(){
		InputStream inputStream= CommonUtil.class.getClassLoader().getResourceAsStream("android_version.xml");
		SAXReader reader = new SAXReader();
		reader.setEncoding("GB2312");
		org.dom4j.Document document;
		String loadUrl = "";
		try {
			document = reader.read(inputStream);
			//读取节点信息
			Node url=document.selectSingleNode("//info/url");
			loadUrl = url.getStringValue();
		} catch (DocumentException e) {
		}
		return loadUrl;
	}
	/**
	 * 验证是否是正确的电话号码
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean isPhone(String phoneNum) {
//		String pat = "(^[0-9]{3,4}-[0-9]{3,8}$)|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|17[1|5|7]|18[0|2|3|5|6|7|8|9])\\d{8}$";
		String pat = "^13[0-9]{9}$|14[0-9]{9}$|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$";
		boolean flag = false;
		if (!CommonUtil.isNull(phoneNum)) {
			if (phoneNum.matches(pat)) {
				flag = true;
			}
		}
		return flag;
	}
	
	
	/**
	 * 验证是否是正确的Email邮箱
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String pat = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
//		String str = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		boolean flag = false;
		if (!CommonUtil.isNull(email)) {
			if (email.matches(pat)) {
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 判断对象是否为空，如果对象为ArrayList，且不为空，则还要判断是否有长度
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		List list = null;
		String str = null;
		boolean flag = false;
		String[] strs = null;
		Long l;
		if (obj == null) {
			flag = true;
		} else {
			if (obj.getClass().getSimpleName().equals("ArrayList")) {
				list = (ArrayList) obj;
				if (list.size() == 0) {
					flag = true;
				}
			}
			if (obj.getClass().getSimpleName().equals("String[]")) {
				strs = (String[]) obj;
				if (strs.length == 1) {
					if (CommonUtil.isNull(strs[0])) {
						flag = true;
					}
				}
			}
			if (obj.getClass().getSimpleName().equals("String")) {
				str = (String) obj;
				if (str.equals("")) {
					flag = true;
				}
			}
			if (obj.getClass().getSimpleName().equals("Long")) {
				l = (Long) obj;
				if (l == 0) {
					flag = true;
				}
			}if (obj.getClass().getSimpleName().equals("Integer")) {
				if ((Integer) obj == 0) {
					flag = true;
				}
			}
		}
		return flag;
	}
	/**
	 * 获取长度为length整形随机数
	 * @param length
	 * @return
	 */
	public Integer randomNum(int length){
		String allChar = "0123456789";
		StringBuffer numStr = new StringBuffer();
        Random random = new Random();
        while(numStr.length()!=length){
        	numStr.append(allChar.charAt(random.nextInt(allChar.length())));
        	if(numStr.charAt(0)=='0'){
        		numStr = new StringBuffer(numStr.subSequence(1,numStr.length()));
        	}
        }
     return Integer.parseInt(numStr.toString());   
	}
	
	/**
	 * 生成流程步骤执行人字符串
	 * @param comId 团队主键
	 * @param userId 人员主键
	 * @return
	 */
	public static String buildFlowExetutor(Integer comId,Integer userId) {
		return comId+":"+userId;
	}
	
	/**
	 * 用正则表达式（整数）
		首先要import java.util.regex.Pattern 和 java.util.regex.Matcher
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
		if(isNull(str)){
			return false;
		}
		String patternStr = "[0-9]*";
	   Pattern pattern = Pattern.compile(patternStr); 
	   Matcher isNum = pattern.matcher(str);
	   if(!isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
	/**
	 * 是否为浮点数
	 * @param str
	 * @return
	 */
	public static boolean isFloat(String str){
		if(isNull(str)){
			return false;
		}
		String patternStr = "\\d+(\\.\\d+)?";
		Pattern pattern = Pattern.compile(patternStr); 
		Matcher isNum = pattern.matcher(str);
		if(!isNum.matches()){
	       return false; 
		} 
		return true; 
	}
	/**
	 * 取得有效的工作时间
	 * @param listAttenceTypes
	 * @return
	 */
	public static int getEffectWorkHour(List<AttenceType> listAttenceTypes) {
		//默认有效工作时间为8小时
		int effectHour = 8;
		if(null != listAttenceTypes && !listAttenceTypes.isEmpty()){
			//工作时间类型只取一个就行
			AttenceType attenceType = listAttenceTypes.get(0);
			//设定的工作日
			List<AttenceTime> listAttenceTime = attenceType.getListAttenceTimes();
			if( null!=listAttenceTime && !listAttenceTime.isEmpty()){
				//默认为 0小时
				effectHour = 0;
				for (AttenceTime attenceTime : listAttenceTime) {
					//签到时间str
					String dayTimeSStr = attenceTime.getDayTimeS()+":00";
					//签退时间str
					String dayTimeEStr = attenceTime.getDayTimeE()+":00";
					
					//签到时间
					Date dayTimeE = DateTimeUtil.parseDate(dayTimeEStr, DateTimeUtil.HH_mm_ss);
					//签退时间
					Date dayTimeS = DateTimeUtil.parseDate(dayTimeSStr, DateTimeUtil.HH_mm_ss);
					
					//两次时间差（毫秒级）
					Long timeDiff = dayTimeE.getTime()-dayTimeS.getTime();
					//有效的工作时间(小时数)
					effectHour += timeDiff / (60L*60*1000);
					
				}
			}
		}
		return effectHour;
	}

	/**
	 * 取得一天的工作时段
	 * @param listAttenceTypes
	 * @param ruleType 1标准考勤 3灵活考勤
	 * 
	 * 只有一个考勤时段
	 * 	MI-1 上午签到
	 *  AO-1 下午签退
	 * 有两个考勤时段
	 *  MI-1 上午签到
	 *  MO-1 上午签退
	 *  AI-1 下午签到
	 *  AO-1 下午签退
	 * 有三个考勤时段
	 *  MI-1 上午签到
	 *  MO-1 上午签退
	 *  AI-1 下午签到
	 *  AO-1 下午签退
	 *  NI-1 晚上签到
	 *  NO-1 晚上签退
	 * @return
	 */
	public static Map<Integer, Map<String,String>> getAttentcTime(
			List<AttenceType> listAttenceTypes, String ruleType) {
		
		Map<Integer, Map<String,String>> resultMap = new HashMap<Integer, Map<String,String>>();
		
		if("1".equals(ruleType)){
			Map<String,String> map = new HashMap<String, String>();
			if(null != listAttenceTypes && !listAttenceTypes.isEmpty()){
				//工作时间类型只取一个就行
				AttenceType attenceType = listAttenceTypes.get(0);
				//设定的工作日
				List<AttenceTime> listAttenceTime = attenceType.getListAttenceTimes();
				map = CommonUtil.getAttenceTime(listAttenceTime);
			}else{
				//默认打卡时间
				//上午签到
				map.put("MI-1", "09:00");
				//上午签退
				map.put("MO-1", "12:00");
				
				//下午签到
				map.put("AI-1", "13:00");
				//下午签退
				map.put("AO-1", "18:00");
			}
			resultMap.put(0, map);
		}else if("3".equals(ruleType)){
			if(null != listAttenceTypes && !listAttenceTypes.isEmpty()){
				for (AttenceType attenceType : listAttenceTypes) {
					//设定的工作时段
					List<AttenceTime> listAttenceTime = attenceType.getListAttenceTimes();
					Map<String,String> map = CommonUtil.getAttenceTime(listAttenceTime);
					
					//设定的工作日
					List<AttenceWeek> listAttenceWeeks = attenceType.getListAttenceWeeks();
					if(null!=listAttenceWeeks && !listAttenceWeeks.isEmpty()){
						for (AttenceWeek attenceWeek : listAttenceWeeks) {
							resultMap.put(attenceWeek.getWeekDay(), map);
						}
					}
				}
			}
			
		}
		return resultMap;
	}
	/**
	 * 取得签到和签退时间
	 * @param listAttenceTime
	 * @return
	 */
	private static Map<String, String> getAttenceTime(List<AttenceTime> listAttenceTime) {
		Map<String,String> map = new HashMap<String, String>();
		if( null!=listAttenceTime && !listAttenceTime.isEmpty()){
			if(listAttenceTime.size()==1){
				AttenceTime attenceTime = listAttenceTime.get(0);
				//上午签到
				map.put("MI-1", attenceTime.getDayTimeS());
				//下午签退
				map.put("AO-1", attenceTime.getDayTimeE());
			}else if(listAttenceTime.size()==2){
				AttenceTime attenceTimeM = listAttenceTime.get(0);
				//上午签到
				map.put("MI-1", attenceTimeM.getDayTimeS());
				//上午签退
				map.put("MO-1", attenceTimeM.getDayTimeE());
				
				AttenceTime attenceTimeA = listAttenceTime.get(1);
				//下午签到
				map.put("AI-1", attenceTimeA.getDayTimeS());
				//下午签退
				map.put("AO-1", attenceTimeA.getDayTimeE());
				
			}else if(listAttenceTime.size()==3){
				
				AttenceTime attenceTimeM = listAttenceTime.get(0);
				//上午签到
				map.put("MI-1", attenceTimeM.getDayTimeS());
				//上午签退
				map.put("MO-1", attenceTimeM.getDayTimeE());
				
				AttenceTime attenceTimeA = listAttenceTime.get(1);
				//下午签到
				map.put("AI-1", attenceTimeA.getDayTimeS());
				//下午签退
				map.put("AO-1", attenceTimeA.getDayTimeE());
				
				AttenceTime attenceTimeN = listAttenceTime.get(2);
				//晚上签到
				map.put("NI-1", attenceTimeN.getDayTimeS());
				//晚上签退
				map.put("NO-1", attenceTimeN.getDayTimeE());
			}
		}
		
		return map;
		
	}

	/**
	 * 取得工作时间
	 * @param map 考勤时间段
	 * @param dateStrYMD 
	 * @param dateTimeSStr 时间起
	 * @param dateTimeEStr 时间止
	 */
	public static long getWorkHour(Map<String, String> map, String dateStrYMD,
			String dateTimeSStr, String dateTimeEStr) {
		
		long workHourMiles = 0L;
		//考勤设置的长度
		int attenceTimeSize = map.size();
		if(attenceTimeSize==2){//只有一个考勤时段
			//上午签到时间
			String MI_1 = dateStrYMD+" "+map.get("MI-1");
			
			Date MI_1Date = DateTimeUtil.parseDate(MI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//下午签退时间
			String AO_1 = dateStrYMD+" "+map.get("AO-1");
			Date AO_1Date = DateTimeUtil.parseDate(AO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认开始时间为传入时间
			String startDateStr = dateTimeSStr;
			Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认结束时间为传入时间
			String endDateStr = dateTimeEStr;
			Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//排除无效时间
			if(startDate.getTime() >= AO_1Date.getTime() ||//开始时间大于下午签退时间
					endDate.getTime() <= MI_1Date.getTime()){//结束时间小于上午签到时间
				return 0L;
			}
			
			//统计有效时间
			if(startDate.getTime() < MI_1Date.getTime()){
				//开始时间小于上午签到时间，则开始时间为上午签到时间
				startDate = MI_1Date;
			}
			
			if(endDate.getTime()>AO_1Date.getTime()){
				//默认时间大于签退时间，则结束时间为签退时间
				endDate = AO_1Date;
			}
			//计算毫秒数
			workHourMiles = endDate.getTime() - startDate.getTime();
			
		}else if(attenceTimeSize==4){//有两个考勤时段
			
			//上午签到时间
			String MI_1 = dateStrYMD+" "+map.get("MI-1");
			Date MI_1Date = DateTimeUtil.parseDate(MI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//上午签退时间
			String MO_1 = dateStrYMD+" "+map.get("MO-1");
			Date MO_1Date = DateTimeUtil.parseDate(MO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//下午签到时间
			String AI_1 = dateStrYMD+" "+map.get("AI-1");
			Date AI_1Date = DateTimeUtil.parseDate(AI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//下午签退时间
			String AO_1 = dateStrYMD+" "+map.get("AO-1");
			Date AO_1Date = DateTimeUtil.parseDate(AO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认开始时间为传入时间
			String startDateStr = dateTimeSStr;
			Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认结束时间为传入时间
			String endDateStr = dateTimeEStr;
			Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//排除无效时间
			if(startDate.getTime() >= AO_1Date.getTime()//开始时间大于下午签退时间
				|| endDate.getTime() <= MI_1Date.getTime()//结束时间小于上午签到时间
				||(startDate.getTime()>= MO_1Date.getTime() && endDate.getTime() <= AI_1Date.getTime())){//不为午休时间
				return 0L;
			}
			
			//需要减少的时间
			Long descTime = 0L;
			if(startDate.getTime() < MO_1Date.getTime()){//开始时间在上午签退前
				if(endDate.getTime() > AI_1Date.getTime()){//结束时间在上午签退后，下午签退前，减去
						
					descTime = AI_1Date.getTime() - MO_1Date.getTime();
				}
			}
			
			if(startDate.getTime() < MI_1Date.getTime()){//上午签到前，为上午签到时间
				startDate = MI_1Date;
			}else if(startDate.getTime() >= MO_1Date.getTime()
					&& startDate.getTime() < AI_1Date.getTime()){//上午签到后，下午签到前，为下午签到时间
				startDate = AI_1Date;
			}
			
			if(endDate.getTime() >= MO_1Date.getTime()
					&& endDate.getTime() <= AI_1Date.getTime()){//上午签退后，下午签到前，为上午签退时间
				endDate = MO_1Date;
			}else if(endDate.getTime() > AO_1Date.getTime()){//下午签退后，晚上签到前，为下午签退时间
				endDate = AO_1Date;
			}
			//计算毫秒数
			workHourMiles = endDate.getTime() - startDate.getTime() - descTime;
			
		}else if(attenceTimeSize==6){//有三个考勤时段
			//上午签到时间
			String MI_1 = dateStrYMD+" "+map.get("MI-1");
			Date MI_1Date = DateTimeUtil.parseDate(MI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//上午签退时间
			String MO_1 = dateStrYMD+" "+map.get("MO-1");
			Date MO_1Date = DateTimeUtil.parseDate(MO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//下午签到时间
			String AI_1 = dateStrYMD+" "+map.get("AI-1");
			Date AI_1Date = DateTimeUtil.parseDate(AI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//下午签退时间
			String AO_1 = dateStrYMD+" "+map.get("AO-1");
			Date AO_1Date = DateTimeUtil.parseDate(AO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//晚上签到时间
			String NI_1 = dateStrYMD+" "+map.get("NI-1");
			Date NI_1Date = DateTimeUtil.parseDate(NI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//晚上签退时间
			String NO_1 = dateStrYMD+" "+map.get("NO-1");
			Date NO_1Date = DateTimeUtil.parseDate(NO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认开始时间为传入时间
			String startDateStr = dateTimeSStr;
			Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认结束时间为传入时间
			String endDateStr = dateTimeEStr;
			Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//排除无效时间
			if(startDate.getTime() >= NO_1Date.getTime()//开始时间大于晚上签退时间
				|| endDate.getTime() <= MI_1Date.getTime()//结束时间小于上午签到时间
				||(startDate.getTime()>= MO_1Date.getTime() && endDate.getTime() <= AI_1Date.getTime())//不为午休时间
				||(startDate.getTime()>= AO_1Date.getTime() && endDate.getTime() <= NI_1Date.getTime())){//不为晚休时间
				return 0L;
			}
			
			//需要减少的时间
			Long descTime = 0L;
			if(startDate.getTime() < MO_1Date.getTime()){//开始时间在上午
				if( endDate.getTime() >AI_1Date.getTime()
						&& endDate.getTime() <=AO_1Date.getTime()){//结束时间在下午
					descTime = AI_1Date.getTime() - MO_1Date.getTime();
				}else if(endDate.getTime() >NI_1Date.getTime()
						&& endDate.getTime() <=NO_1Date.getTime()){//结束时间在晚上
					descTime = AI_1Date.getTime() - MO_1Date.getTime()
							+ NI_1Date.getTime() - AO_1Date.getTime();
				}
			}else if(startDate.getTime() > MO_1Date.getTime() 
					&& startDate.getTime() < AO_1Date.getTime()){//开始时间在下午
				if(endDate.getTime() > NI_1Date.getTime()){//结束时间在晚上
					descTime = NI_1Date.getTime() - AO_1Date.getTime();
				}
			}
			
			if(startDate.getTime() < MI_1Date.getTime()){//上午签到前，为上午签到时间
				startDate = MI_1Date;
			}else if(startDate.getTime() >= MO_1Date.getTime()
					&& startDate.getTime() <= AI_1Date.getTime()){//上午签退后，下午签到前，为下午签到时间
				startDate = AI_1Date;
			}else if(startDate.getTime() >= AO_1Date.getTime()
					&& startDate.getTime() <= NI_1Date.getTime()){//晚上签到前，为晚上签到时间
				startDate = NI_1Date;
			}
			
			if(endDate.getTime() > MO_1Date.getTime()
					&& endDate.getTime() <= AI_1Date.getTime()){//上午签退后，下午签到前，为上午签退时间
				endDate = MO_1Date;
			}else if(endDate.getTime() > AO_1Date.getTime()
					&& endDate.getTime() <= NI_1Date.getTime()){//下午签退后，晚上签到前，为下午签退时间
				endDate = AO_1Date;
			}else if(endDate.getTime() > NO_1Date.getTime()){//晚上签到后，为晚上签退时间
				endDate = NO_1Date;
			}
			//计算毫秒数
			workHourMiles = endDate.getTime() - startDate.getTime() - descTime;
			
		}
		
		return workHourMiles;
		
	}

	/**
	 * 取得非工作时段的时间
	 * @param map 考勤时间段
	 * @param dateStrYMD 
	 * @param dateTimeSStr 时间起
	 * @param dateTimeEStr 时间止
	 * @return
	 */
	public static Long getOverTime(Map<String, String> map, String dateStrYMD,
			String dateTimeSStr, String dateTimeEStr) {
		long workHourMiles = 0L;
		//考勤设置的长度
		int attenceTimeSize = map.size();
		if(attenceTimeSize==2){//只有一个考勤时段
			//上午签到时间
			String MI_1 = dateStrYMD+" "+map.get("MI-1");
			
			Date MI_1Date = DateTimeUtil.parseDate(MI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//下午签退时间
			String AO_1 = dateStrYMD+" "+map.get("AO-1");
			Date AO_1Date = DateTimeUtil.parseDate(AO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认开始时间为传入时间
			String startDateStr = dateTimeSStr;
			Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认结束时间为传入时间
			String endDateStr = dateTimeEStr;
			Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			if(startDate.getTime() >= MI_1Date.getTime() 
					&& endDate.getTime() <= AO_1Date.getTime()){//加班时间在考勤时间内
				return 0L;
			}
			
			Long descTime = 0L;
			if(startDate.getTime() < MI_1Date.getTime() && endDate.getTime()>=AO_1Date.getTime()){//有考勤时段
				descTime = AO_1Date.getTime() - MI_1Date.getTime();
			}
			//开始时间在考勤时段
			if(startDate.getTime() >= MI_1Date.getTime() && startDate.getTime()<=AO_1Date.getTime()){
				startDate = AO_1Date;
			}
			//结束时间在考勤时段
			if(endDate.getTime() >= MI_1Date.getTime() && endDate.getTime()<=AO_1Date.getTime()){
				endDate = MI_1Date;
			}
			
			workHourMiles = endDate.getTime() - startDate.getTime() - descTime;
			
		}else if(attenceTimeSize==4){//只有两个考勤时段
			
			//上午签到时间
			String MI_1 = dateStrYMD+" "+map.get("MI-1");
			Date MI_1Date = DateTimeUtil.parseDate(MI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//上午签退时间
			String MO_1 = dateStrYMD+" "+map.get("MO-1");
			Date MO_1Date = DateTimeUtil.parseDate(MO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//下午签到时间
			String AI_1 = dateStrYMD+" "+map.get("AI-1");
			Date AI_1Date = DateTimeUtil.parseDate(AI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//下午签退时间
			String AO_1 = dateStrYMD+" "+map.get("AO-1");
			Date AO_1Date = DateTimeUtil.parseDate(AO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认开始时间为传入时间
			String startDateStr = dateTimeSStr;
			Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认结束时间为传入时间
			String endDateStr = dateTimeEStr;
			Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			
			if((startDate.getTime() >= MI_1Date.getTime() 
					&& endDate.getTime() <= MO_1Date.getTime())
					|| (startDate.getTime() >= AI_1Date.getTime() 
						&& endDate.getTime() <= AO_1Date.getTime())){//加班时间在考勤时间内
				return 0L;
			}
			Long descTime = 0L;
			if(startDate.getTime() < MI_1Date.getTime()){//开始时间在上午签到时间前
				if(endDate.getTime()>MO_1Date.getTime()//结束时间在上午签退后，下午签到前,减去上午工作时段时段
						&& endDate.getTime()<=AO_1Date.getTime()){
					descTime = MO_1Date.getTime() - MI_1Date.getTime();
				}else if(endDate.getTime() > AO_1Date.getTime()){//结束时间在下午签退后，减去上午和下午工作时段时段
					descTime = MO_1Date.getTime() - MI_1Date.getTime()
							+ AO_1Date.getTime() - AI_1Date.getTime();
				}
			}else if(startDate.getTime() < AI_1Date.getTime()){//开始时间在下午签到前
				if(endDate.getTime() > AO_1Date.getTime()){//结束时间在下午签退后，减去下午工作时段
					descTime = AO_1Date.getTime() - AI_1Date.getTime();
				}
			}
			
			if(startDate.getTime() >= MI_1Date.getTime() 
					&& startDate.getTime()<=MO_1Date.getTime()){//开始时间在上午考勤时段
				startDate = MO_1Date;
			}else if(startDate.getTime() >= AI_1Date.getTime() 
					&& startDate.getTime()<=AO_1Date.getTime()){//开始时间在下午考勤时段
				startDate = AO_1Date;
			}
			
			if(endDate.getTime() >= MI_1Date.getTime() 
					&& endDate.getTime() <= MO_1Date.getTime()){//结束时间在上午考勤
				endDate = MI_1Date;
			}else if(endDate.getTime() >= AI_1Date.getTime() 
					&& endDate.getTime() <= AO_1Date.getTime()){//结束时间在下午考勤
				endDate = AI_1Date;
			}
			workHourMiles = endDate.getTime() - startDate.getTime() - descTime;
			
		}else if(attenceTimeSize==6){//只有三个考勤时段
			//上午签到时间
			String MI_1 = dateStrYMD+" "+map.get("MI-1");
			Date MI_1Date = DateTimeUtil.parseDate(MI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//上午签退时间
			String MO_1 = dateStrYMD+" "+map.get("MO-1");
			Date MO_1Date = DateTimeUtil.parseDate(MO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//下午签到时间
			String AI_1 = dateStrYMD+" "+map.get("AI-1");
			Date AI_1Date = DateTimeUtil.parseDate(AI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//下午签退时间
			String AO_1 = dateStrYMD+" "+map.get("AO-1");
			Date AO_1Date = DateTimeUtil.parseDate(AO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//晚上签到时间
			String NI_1 = dateStrYMD+" "+map.get("NI-1");
			Date NI_1Date = DateTimeUtil.parseDate(NI_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			//晚上签退时间
			String NO_1 = dateStrYMD+" "+map.get("NO-1");
			Date NO_1Date = DateTimeUtil.parseDate(NO_1, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认开始时间为传入时间
			String startDateStr = dateTimeSStr;
			Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			//默认结束时间为传入时间
			String endDateStr = dateTimeEStr;
			Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
			
			if((startDate.getTime() >= MI_1Date.getTime() 
				&& endDate.getTime() <= MO_1Date.getTime())
				|| (startDate.getTime() >= AI_1Date.getTime() 
					&& endDate.getTime() <= AO_1Date.getTime())
					||(startDate.getTime() >= NI_1Date.getTime() 
							&& endDate.getTime() <= NO_1Date.getTime())){//加班时间在考勤时间内
				return 0L;
			}
			Long descTime = 0L;
			if(startDate.getTime() < MI_1Date.getTime()){//开始时间在上午签到时间前
				if(endDate.getTime()>MO_1Date.getTime()//结束时间在上午签退后，下午签到前，减去上午工作时段
						&& endDate.getTime()<=AO_1Date.getTime()){
					descTime = MO_1Date.getTime() - MI_1Date.getTime();
				}else if(endDate.getTime()>AO_1Date.getTime()//结束时间在下午签到后，晚上签到前,减去上午和下午工作时段
						&& endDate.getTime()<=NO_1Date.getTime()){//结束时间在晚上签退前
					descTime = MO_1Date.getTime() - MI_1Date.getTime()
							+ AO_1Date.getTime() - AI_1Date.getTime();
				}else if(endDate.getTime() > NO_1Date.getTime()){//结束时间在晚上签退后
					descTime = MO_1Date.getTime() - MI_1Date.getTime()
							+ AO_1Date.getTime() - AI_1Date.getTime()
							+ NO_1Date.getTime() - NI_1Date.getTime();
				}
			}else if(startDate.getTime() < AI_1Date.getTime()){//开始时间在下午签到前
				if(endDate.getTime() > AO_1Date.getTime()
						&& endDate.getTime() <= NO_1Date.getTime() ){//结束时间在晚上签退前
					descTime = AO_1Date.getTime() - AI_1Date.getTime();
				}else if(endDate.getTime() > NO_1Date.getTime()){//结束时间在晚上签退后
					descTime = AO_1Date.getTime() - AI_1Date.getTime()
							+ NO_1Date.getTime() - NI_1Date.getTime();
				}
			}else if(startDate.getTime() < NI_1Date.getTime()){//开始时间在晚上签到前
				if(endDate.getTime() > NO_1Date.getTime()){//结束时间在晚上签退后
					descTime = NO_1Date.getTime() - NI_1Date.getTime();
				}
			}
			
			if(startDate.getTime() >= MI_1Date.getTime() 
					&& startDate.getTime()<=MO_1Date.getTime()){//开始时间在上午考勤时段
				startDate = MO_1Date;
			}else if(startDate.getTime() >= AI_1Date.getTime() 
					&& startDate.getTime()<=AO_1Date.getTime()){//开始时间在下午考勤时段
				startDate = AO_1Date;
			}else if(startDate.getTime() >= NI_1Date.getTime() 
					&& startDate.getTime()<=NO_1Date.getTime()){//开始时间在晚上考勤时段
				startDate = NO_1Date;
			}
			
			if(endDate.getTime() >= MI_1Date.getTime() 
					&& endDate.getTime() <= MO_1Date.getTime()){//结束时间在上午考勤
				endDate = MI_1Date;
			}else if(endDate.getTime() >= AI_1Date.getTime() 
					&& endDate.getTime() <= AO_1Date.getTime()){//结束时间在下午考勤
				endDate = AI_1Date;
			}else if(endDate.getTime() >= NI_1Date.getTime() 
					&& endDate.getTime() <= NO_1Date.getTime()){//结束时间在晚上考勤
				endDate = NI_1Date;
			}
			
			workHourMiles = endDate.getTime() - startDate.getTime() - descTime;
		}
		return workHourMiles;
	}
	/**
	 * 判断属性是否变更
	 * @param userInfo 属性参数
	 * @param attrType 属性类型
	 * @param curUser 原属性对象
	 * @return
	 */
	public static boolean isAttrChanged(UserInfo userInfo, String attrType,
			UserInfo curUser) {
		//判断属性是否变更
		if ("nickname".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getNickname())) 
				&& userInfo.getNickname().equals(curUser.getNickname()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getNickname()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getNickname()))))) {
			return false;
		}else if ("userName".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getUserName())) 
				&& userInfo.getUserName().equals(curUser.getUserName()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getUserName()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getUserName()))))) {
			return false;
		}else if ("gender".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getGender()))
				&& userInfo.getGender().equals(curUser.getGender()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getGender()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getGender()))))) {
			return false;
		}else if ("birthday".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getBirthday()))
				&& userInfo.getBirthday().equals(curUser.getBirthday()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getBirthday()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getBirthday()))))) {
			return false;
		}else if ("job".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getJob()))
				&& userInfo.getJob().equals(curUser.getJob()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getJob()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getJob()))))) {
			return false;
		}else if ("qq".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getQq()))
				&& userInfo.getQq().equals(curUser.getQq()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getQq()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getQq()))))) {
			return false;
		}else if ("linePhone".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getLinePhone()))
				&& userInfo.getLinePhone().equals(curUser.getLinePhone()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getLinePhone()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getLinePhone()))))) {
			return false;
		}else if ("wechat".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getWechat()))
				&& userInfo.getWechat().equals(curUser.getWechat()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getWechat()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getWechat()))))) {
			return false;
		}else if ("hireDate".equals(attrType) && ((!StringUtil.isBlank(StringUtil.delNull(userInfo.getHireDate()))
				&& userInfo.getHireDate().equals(curUser.getHireDate()))
				|| (StringUtil.isBlank(StringUtil.delNull(userInfo.getHireDate()))
						&& StringUtil.isBlank(StringUtil.delNull(curUser.getHireDate()))))) {
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 构建流程名称
	 * @param userInfo
	 * @param flowModel
	 * @return
	 */
	public static String conStrFlowName(UserInfo userInfo, SpFlowModel flowModel) {
		String flowName =  userInfo.getUserName() + flowModel.getFlowName() + "-" + DateTimeUtil.getNowDateStr(5);// 流程名称形如：{user}请假单-{year}年{month}月{day}日
		String titleRule = flowModel.getTitleRule();
		if(!StringUtils.isEmpty(titleRule)){
			//用户名称
			String userName = userInfo.getUserName();
			//部门
			String depName = userInfo.getDepName();
			//一个月的日期数
			Integer day = DateTimeUtil.getMonthDay();
			//月份
			Integer month = DateTimeUtil.getMonth()+1;
			//年份
			Integer year = DateTimeUtil.getYear();
			
			Integer weekNum = 0;
			try {
				weekNum = DateTimeUtil.getWeekOfYear(DateTimeUtil.getNowDateStr(DateTimeUtil.c_yyyy_MM_dd_),DateTimeUtil.c_yyyy_MM_dd_);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//前一个月
			Integer preYear = year;
			Integer preMonth = month-1;
			if(preMonth == 0){
				preYear = preYear-1;
				preMonth = 12;
			}
			//后一个月
			Integer nextYear = year;
			Integer nextMonth = month+1;
			if(nextMonth==13){
				nextYear = nextYear+1;
				nextMonth = 1;
				
			}
			titleRule = titleRule.replaceAll("\\{user\\}", userName);
			titleRule = titleRule.replaceAll("\\{dept\\}", depName);
			if(titleRule.contains("premonth")){
				titleRule = titleRule.replaceAll("\\{year\\}", preYear.toString());
			}else if(titleRule.contains("nextmonth")){
				titleRule = titleRule.replaceAll("\\{year\\}", nextYear.toString());
			}else{
				titleRule = titleRule.replaceAll("\\{year\\}", year.toString());
			}
			
			titleRule = titleRule.replaceAll("\\{month\\}", month.toString());
			titleRule = titleRule.replaceAll("\\{premonth\\}", preMonth.toString());
			titleRule = titleRule.replaceAll("\\{nextmonth\\}", nextMonth.toString());
			titleRule = titleRule.replaceAll("\\{day\\}", day.toString());
			titleRule = titleRule.replaceAll("\\{yearweek\\}", weekNum.toString());
			
			flowName = titleRule;
		}
		return flowName;
	}

	/**
	 * 取得数据表的信息
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getSysColumn(String tableName) {
		Map<String,String> columns = new HashMap<>();
		try {
			Class clz = Class.forName("com.westar.base.model."+tableName);
			
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				if (field.getAnnotation(Filed.class) != null) {
					Filed filedAnnotion = field.getAnnotation(Filed.class);
					boolean formColumnflag = filedAnnotion.formColumn();
					if(formColumnflag){
						columns.put(field.getName().toLowerCase(),field.getName());
					}
				}
				
			}
		} catch (ClassNotFoundException e) {
			
		}
		return columns;
	}
	/**
	 * 取得数据表的信息
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> getSearchColumn(String tableName) {
		List<String> columns = new ArrayList<String>();
		try {
			Class clz = Class.forName("com.westar.base.model."+tableName);
			
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				if (field.getAnnotation(Filed.class) != null) {
					columns.add(field.getName().toLowerCase());
				}
				
			}
		} catch (ClassNotFoundException e) {
			
		}
		return columns;
	}
}
