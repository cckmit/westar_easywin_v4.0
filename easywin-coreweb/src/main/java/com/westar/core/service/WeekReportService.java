package com.westar.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.DataDic;
import com.westar.base.model.Department;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.ModContDep;
import com.westar.base.model.ModContMember;
import com.westar.base.model.SubTimeSet;
import com.westar.base.model.TaskTalkUpfile;
import com.westar.base.model.TaskUpfile;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.model.WeekRepLog;
import com.westar.base.model.WeekRepModContent;
import com.westar.base.model.WeekRepTalk;
import com.westar.base.model.WeekRepTalkFile;
import com.westar.base.model.WeekRepUpfiles;
import com.westar.base.model.WeekReport;
import com.westar.base.model.WeekReportMod;
import com.westar.base.model.WeekReportPlan;
import com.westar.base.model.WeekReportQ;
import com.westar.base.model.WeekReportShareUser;
import com.westar.base.model.WeekReportVal;
import com.westar.base.model.WeekViewer;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.PageBean;
import com.westar.base.pojo.WeekReportPojo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.DataDicDao;
import com.westar.core.dao.MsgShareDao;
import com.westar.core.dao.WeekReportDao;
import com.westar.core.thread.IndexUpdateThread;
import com.westar.core.web.PaginationContext;

@Service
public class WeekReportService {

	@Autowired
	WeekReportDao weekReportDao;

	@Autowired
	DataDicDao dataDicDao;

	@Autowired
	SystemLogService systemLogService;

	@Autowired
	FileCenterService fileCenterService;
	
	@Autowired
	MsgShareService msgShareService;

	@Autowired
	IndexService indexService;
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	JiFenService jiFenService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	ForceInPersionService forceInService;

	@Autowired
	MsgShareDao msgShareDao;

	@Autowired
	ViewRecordService viewRecordService;
	
	@Autowired
	TodayWorksService todayWorksService;

	/**
	 * 公司的周报模板（一个公司只有一个模板）
	 * 
	 * @param sessionUser
	 * @return
	 */
	public WeekReportMod initWeekReportMod(UserInfo sessionUser) {
		WeekReportMod weekReportMod = weekReportDao.getWeekReportMod(sessionUser
				.getComId());
		// 团队级要求
		List<WeekRepModContent> contentTeamLevs = new ArrayList<WeekRepModContent>();
		// 部门级要求
		List<WeekRepModContent> contentGroupLevs = new ArrayList<WeekRepModContent>();
		// 成员级要求
		List<WeekRepModContent> contentMemLevs = new ArrayList<WeekRepModContent>();
		if (null == weekReportMod) {// 若是没有模板，则默认一个模板
			weekReportMod = new WeekReportMod();
			// 创建人
			weekReportMod.setCereaterId(sessionUser.getId());
			// 公司
			weekReportMod.setComId(sessionUser.getComId());
			// 名称
			weekReportMod.setModName("默认模板");
			// 是否有下周计划
			weekReportMod.setHasPlan("1");
			// 模板版本
			weekReportMod.setVersion(1);
			// 模板主键
			Integer id = weekReportDao.add(weekReportMod);
			weekReportMod.setId(id);

			// 取得默认的模板条目
			List<DataDic> dataDics = dataDicDao.listDataDic("defModContent");
			for (DataDic dataDic : dataDics) {
				// 模板内容
				WeekRepModContent weekRepModContent = new WeekRepModContent();
				// 创建人
				weekRepModContent.setCereaterId(sessionUser.getId());
				// 公司
				weekRepModContent.setComId(sessionUser.getComId());
				// 所属模板
				weekRepModContent.setModId(id);
				// 周报填写时显示
				weekRepModContent.setHideState("0");
				// 默认只有团队级别
				weekRepModContent.setRepLev("1");
				// 默认是系统级别的
				weekRepModContent.setSysState(1);
				// 模板条目内容
				weekRepModContent.setModContent(dataDic.getZvalue());
				//默认不是必填
				weekRepModContent.setIsRequire("0");
				// 模板条目主键
				Integer contentId = weekReportDao.add(weekRepModContent);
				weekRepModContent.setId(contentId);

				contentTeamLevs.add(weekRepModContent);
			}
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser
					.getUserName(), "初始化周报模板", ConstantInterface.TYPE_WEEK,
					sessionUser.getComId(),sessionUser.getOptIP());
		} else {
			// 模板团队级条目
			contentTeamLevs = weekReportDao.listWeekRepModContent(weekReportMod
					.getId(), weekReportMod.getComId(), "1");

			// 模板部门级条目
			contentGroupLevs = weekReportDao.listWeekRepModContent(
					weekReportMod.getId(), weekReportMod.getComId(), "2");
			for (WeekRepModContent contentDepLev : contentGroupLevs) {
				List<ModContDep> modContDeps = new ArrayList<ModContDep>();
				modContDeps = weekReportDao.listModContDep(contentDepLev
						.getId(), contentDepLev.getComId(), contentDepLev
						.getModId());
				contentDepLev.setListDeps(modContDeps);
			}

			// 模板成员级条目
			contentMemLevs = weekReportDao.listWeekRepModContent(weekReportMod
					.getId(), weekReportMod.getComId(), "3");
			for (WeekRepModContent contentMemLev : contentMemLevs) {
				List<ModContMember> modContMembers = new ArrayList<ModContMember>();
				modContMembers = weekReportDao.listModContMember(contentMemLev
						.getId(), contentMemLev.getComId(), contentMemLev
						.getModId());
				contentMemLev.setListMembers(modContMembers);
			}
		}
		// 模板团队级条目
		weekReportMod.setContentTeamLevs(contentTeamLevs);
		// 模板部门级条目
		weekReportMod.setContentGroupLevs(contentGroupLevs);
		// 模板成员级条目
		weekReportMod.setContentMemLevs(contentMemLevs);
		return weekReportMod;
	}

	/**
	 *修改模板
	 * 
	 * @param weekReportMod
	 */
	public void updateWeekReportMod(WeekReportMod weekReportMod) {
		WeekReportMod temp = (WeekReportMod) weekReportDao.objectQuery(
				WeekReportMod.class, weekReportMod.getId());
		if (null != temp) {
			weekReportMod.setVersion(temp.getVersion() + 1);
		}
		weekReportDao.update(weekReportMod);

	}
	/**
	 * 修改周报条目信息
	 * @param weekRepModContent
	 * @return
	 */
	public WeekRepModContent updateWeekRepModContent(
			WeekRepModContent weekRepModContent) {
		return this.updateWeekRepModContent(weekRepModContent, null, null);
	}

	/**
	 * 修改模板是否显示该问题
	 * 
	 * @param weekRepModContent
	 * @param members
	 * @param deps
	 */
	public WeekRepModContent updateWeekRepModContent(
			WeekRepModContent weekRepModContent, String[] members, String[] deps) {
		// 修改模板版本号
		WeekReportMod temp = (WeekReportMod) weekReportDao.objectQuery(
				WeekReportMod.class, weekRepModContent.getModId());
		if (null != temp) {
			WeekReportMod weekReportMod = new WeekReportMod();
			weekReportMod.setId(weekRepModContent.getModId());
			weekReportMod.setVersion(temp.getVersion() + 1);
			weekReportDao.update(weekReportMod);
		}
		// 修改模板条目
		weekReportDao.update(weekRepModContent);
		// 成员不为空则重新赋值
		if (null != members && members.length > 0) {
			// 删除原有的
			weekReportDao.delByField("modContMember", new String[] { "comid",
					"modId", "modContId" }, new Object[] {
					weekRepModContent.getComId(), weekRepModContent.getModId(),
					weekRepModContent.getId() });
			for (String memberId : members) {
				ModContMember modContMember = new ModContMember();
				// 公司编号
				modContMember.setComId(weekRepModContent.getComId());
				// 模板内容主键
				modContMember.setModContId(weekRepModContent.getId());
				// 模板主键
				modContMember.setModId(weekRepModContent.getModId());
				// 人员主键
				modContMember.setMemberId(Integer.parseInt(memberId));

				weekReportDao.add(modContMember);
			}
		}
		// 部门不为空则重新赋值
		if (null != deps && deps.length > 0) {
			// 删除原有的
			weekReportDao.delByField("modContDep", new String[] { "comid",
					"modId", "modContId" }, new Object[] {
					weekRepModContent.getComId(), weekRepModContent.getModId(),
					weekRepModContent.getId() });
			// 部门信息
			if (null != deps && deps.length > 0) {
				for (String depId : deps) {
					ModContDep modContDep = new ModContDep();
					// 公司编号
					modContDep.setComId(weekRepModContent.getComId());
					// 模板内容主键
					modContDep.setModContId(weekRepModContent.getId());
					// 模板主键
					modContDep.setModId(weekRepModContent.getModId());
					// 部门主键
					modContDep.setDepId(Integer.parseInt(depId));

					weekReportDao.add(modContDep);
				}
			}
		}
		return (WeekRepModContent) weekReportDao.objectQuery(
				WeekRepModContent.class, weekRepModContent.getId());
	}

	/**
	 * 添加模板条目
	 * 
	 * @param weekRepModContent
	 * @param members
	 * @param deps
	 * @return
	 */
	public Integer addWeekModContent(WeekRepModContent weekRepModContent,
			String[] members, String[] deps) {

		// 修改模板版本号
		WeekReportMod temp = (WeekReportMod) weekReportDao.objectQuery(
				WeekReportMod.class, weekRepModContent.getModId());
		if (null != temp) {
			WeekReportMod weekReportMod = new WeekReportMod();
			weekReportMod.setId(weekRepModContent.getModId());
			weekReportMod.setVersion(temp.getVersion() + 1);
			weekReportDao.update(weekReportMod);
		}
		// 模板条目主键
		Integer id = weekReportDao.add(weekRepModContent);
		// 成员信息
		if (null != members && members.length > 0) {
			for (String memberId : members) {
				ModContMember modContMember = new ModContMember();
				// 公司编号
				modContMember.setComId(weekRepModContent.getComId());
				// 模板内容主键
				modContMember.setModContId(id);
				// 模板主键
				modContMember.setModId(weekRepModContent.getModId());
				// 人员主键
				modContMember.setMemberId(Integer.parseInt(memberId));

				weekReportDao.add(modContMember);
			}
		}
		// 部门信息
		if (null != deps && deps.length > 0) {
			for (String depId : deps) {
				ModContDep modContDep = new ModContDep();
				// 公司编号
				modContDep.setComId(weekRepModContent.getComId());
				// 模板内容主键
				modContDep.setModContId(id);
				// 模板主键
				modContDep.setModId(weekRepModContent.getModId());
				// 部门主键
				modContDep.setDepId(Integer.parseInt(depId));

				weekReportDao.add(modContDep);
			}
		}
		return id;
	}

	/**
	 * 取得所选日期所写周报
	 * 
	 * @param weekNum
	 * @param userInfo
	 * @param year
	 * @param weekE
	 * @param weekS
	 * @return
	 * @throws ParseException 
	 */
	public WeekReport initWeekReport(Integer weekNum, UserInfo userInfo,
			String year, String weekS, String weekE) throws ParseException {
		// 周报
		WeekReport weekReport = weekReportDao.getWeekReport(weekNum, userInfo
				.getComId(), userInfo.getId(), year);
		// 周报是否填写
		boolean falg = false;
		// 已经初始化过周报了
		if (null != weekReport) {
			//设置留言、附件总数
			Integer talkNum = weekReportDao.countTalk(weekReport.getId(), userInfo.getComId());
			Integer fileNum = weekReportDao.countFiles(userInfo.getComId(),weekReport.getId() );
			weekReport.setFileNum(fileNum);
			weekReport.setTalkNum(talkNum);
			if ("1".equals(weekReport.getHasPlan())) {// 需要填写计划
				// 下周计划
				List<WeekReportPlan> weekReportPlans = weekReportDao
						.listWeekReportPlan(weekReport.getId(), userInfo
								.getComId());
				if ((null != weekReportPlans && weekReportPlans.size() > 0)
						|| weekReport.getCountVal() > 0) {// 要么填写过计划，要么填写过内容
					weekReport.setWeekReportPlans(weekReportPlans);
					// 周报内容
					weekReport.setWeekReportQs(weekReportDao.listWeekReportQ(
							weekReport.getId(), userInfo.getComId(), userInfo
									.getId()));
					falg = true;
				}
			} else if ("0".equals(weekReport.getHasPlan())) {// 不需要填写计划
				if (weekReport.getCountVal() > 0) {// 填写过内容
					// 周报内容
					weekReport.setWeekReportQs(weekReportDao.listWeekReportQ(
							weekReport.getId(), userInfo.getComId(), userInfo
									.getId()));

					falg = true;
				}
			}
		}
		if (!falg) {
			// 没有初始化周报或是没有写周报
			weekReport = this.initWeekReportContent(weekNum, userInfo, year,
					weekS, weekE);
			if (null != weekReport) {
				// 没有值
				weekReport.setCountVal(0);
			}else{
				return null;
			}
		}
		// 周报附件
		weekReport.setWeekReportFiles(weekReportDao
				.listWeekReportFiles(weekReport.getId(), userInfo
						.getComId()));
		return weekReport;
	}

	/**
	 * 初始化周报内容
	 * 
	 * @param weekNum
	 * @param userInfo
	 * @param year
	 * @param weekE
	 * @param weekS
	 * @return
	 */
	private WeekReport initWeekReportContent(Integer weekNum,
			UserInfo userInfo, String year, String weekS, String weekE) {

		// 取得模板的信息
		WeekReportMod weekReportMod = weekReportDao.getWeekReportMod(userInfo
				.getComId());
		if (null == weekReportMod) {// 没有设置周报模板的应通知管理员
			weekReportMod = this.initWeekReportMod(userInfo);
		}
		// 初始化模板条目
		List<WeekRepModContent> contentTeamLevs = weekReportDao
				.initWeekRepContent(userInfo.getComId(), userInfo.getId(),
						userInfo.getDepId());
		if (null == contentTeamLevs || contentTeamLevs.size() == 0) {// 模板可用没有条目
			return null;
		}
		// 是否已经初始化周报
		WeekReport weekReport = weekReportDao.getWeekReportQ(weekNum, userInfo
				.getComId(), userInfo.getId(), year);

		Integer weekReportId = 0;
		// 周报条目
		List<WeekReportQ> weekReportQs = new ArrayList<WeekReportQ>();
		// 已经初始化周报,且版本没有变过
		if (null != weekReport && weekReport.getCountQues() > 0
				&& weekReport.getVersion().equals(weekReportMod.getVersion()) ) {
			// 周报条目
			weekReportQs = weekReportDao.listWeekReportQ(weekReport.getId(),
					userInfo.getComId(), userInfo.getId());
			weekReport.setWeekReportQs(weekReportQs);
			return weekReport;
		} else if (null != weekReport
				&& (weekReport.getVersion() != weekReportMod.getVersion() || weekReport
						.getCountQues() == 0)) {// 已经初始化周报,版本变过了

			// 周报模板版本
			weekReport.setVersion(weekReportMod.getVersion());
			// 是否有计划
			weekReport.setHasPlan(weekReportMod.getHasPlan());

			weekReportId = weekReport.getId();
			// 修改版本
			weekReportDao.update(weekReport);
			// 删除旧版本的条目
			weekReportDao.delByField("weekReportQ", new String[] { "comId",
					"weekReportId" }, new Object[] { userInfo.getComId(),
					weekReportId });
		} else {// 还未初始化周报的，初始化一个
			// 周报
			weekReport = new WeekReport();
			// 一周的第一天
			weekReport.setWeekS(weekS);
			// 一周的最后一天
			weekReport.setWeekE(weekE);
			// 企业
			weekReport.setComId(userInfo.getComId());
			// 汇报人
			weekReport.setReporterId(userInfo.getId());
			// 标题
			weekReport.setWeekRepName(userInfo.getUserName() + "周总结_" + year
					+ "年第" + weekNum + "周");
			// 周数
			weekReport.setWeekNum(weekNum);
			// 年
			weekReport.setYear(Integer.parseInt(year));
			// 是否需要填写下周计划
			weekReport.setHasPlan(weekReportMod.getHasPlan());
			// 周报为草稿状态
			weekReport.setState("1");
			// 周报模板版本
			weekReport.setVersion(weekReportMod.getVersion());

			weekReportId = weekReportDao.add(weekReport);
			weekReport.setId(weekReportId);
		}
		// 周报的条目
		for (WeekRepModContent weekRepModContent : contentTeamLevs) {
			WeekReportQ weekReportQ = new WeekReportQ();
			// 企业
			weekReportQ.setComId(userInfo.getComId());
			// 周报要求
			weekReportQ.setModReportName(weekRepModContent.getModContent());
			// 周报主键
			weekReportQ.setWeekReportId(weekReportId);
			weekReportQ.setIsRequire(weekRepModContent.getIsRequire());

			Integer questionId = weekReportDao.add(weekReportQ);
			weekReportQ.setId(questionId);
			// 字段名称
			weekReportQ.setReportName("DATA_" + year + "" + weekNum + "_"
					+ questionId);
			weekReportDao.update(weekReportQ);

			weekReportQs.add(weekReportQ);
		}
		weekReport.setWeekReportQs(weekReportQs);
		return weekReport;

	}

	/**
	 * 周报条目
	 * 
	 * @param weekReportId
	 *            周报
	 * @param comId
	 *            企业
	 * @param reporterId
	 *            汇报人
	 * @return
	 */
	public List<WeekReportQ> listWeekReportQ(Integer weekReportId,
			Integer comId, Integer reporterId) {
		List<WeekReportQ> list = weekReportDao.listWeekReportQ(weekReportId,
				comId, reporterId);
		return list;
	}

	/**
	 * 填写周报
	 * 
	 * @param weekReport
	 * @param userInfo
	 * @param msgShare 
	 * @throws Exception 
	 */
	public void addWeekReport(WeekReport weekReport, UserInfo userInfo) throws Exception {
		// 删除过去的附件
		weekReportDao.delByField("weekRepUpfiles", 
				new String[] { "comId","weekReportId" },
				new Object[] { userInfo.getComId(),weekReport.getId() });
		// 删除过去的下周计划
		weekReportDao.delByField("weekReportPlan", 
				new String[] { "comId","weekReportId" }, 
				new Object[] { userInfo.getComId(),weekReport.getId() });
		// 删除过去的赋值
		weekReportDao.delByField("weekReportVal",
				new String[] { "comId","weekReportId" }, 
				new Object[] { userInfo.getComId(),weekReport.getId() });
		
		WeekReport weekReportT = (WeekReport) weekReportDao.objectQuery(WeekReport.class, weekReport.getId());
		if("1".equals(weekReportT.getState())){//系统里存放的是待办
			if("0".equals(weekReport.getState())){//本次是发布
				weekReport.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
				// 修改周报
				weekReportDao.update("update weekReport set state=:state,recordCreateTime=:recordCreateTime where id=:id", weekReport);
			}
		}else if("0".equals(weekReportT.getState())){//系统里面是发布
			if("1".equals(weekReport.getState())){//本次是待办
				// 修改周报
				weekReportDao.update("update weekReport set state=:state where id=:id", weekReport);
			}
			
		}

		String weekFile = "";
		// 周报附件
		List<WeekRepUpfiles> weekReportFiles = weekReport.getWeekReportFiles();
		if (null != weekReportFiles && !weekReportFiles.isEmpty()) {
			weekFile +="<br><br><b>周报附件:</b>";
			
			//缓存附件主键信息
			List<Integer> listFileId = new ArrayList<Integer>();
			
			for (WeekRepUpfiles weekRepUpfile : weekReportFiles) {
				
				Upfiles upfiles = (Upfiles) weekReportDao.objectQuery(Upfiles.class, weekRepUpfile.getUpfileId());
				//附件名称
				weekFile +="<br>"+upfiles.getFilename();
				if(upfiles.getFileExt().equals("doc")||
						upfiles.getFileExt().equals("docx")||
						upfiles.getFileExt().equals("xls")||
						upfiles.getFileExt().equals("xlsx")||
						upfiles.getFileExt().equals("ppt")||
						upfiles.getFileExt().equals("pptx")){
					
						weekFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"javascript:void(0)\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','@sid')\" title=\"下载\"></a>";
						weekFile+="\n<a class=\"fa fa-eye\" href=\"javascript:void(0)\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','@sid','006','"+weekReport.getId()+"')\"></a>";
				}else if(upfiles.getFileExt().equals("jpg")||upfiles.getFileExt().equals("bmp")||upfiles.getFileExt().equals("png")
					|| upfiles.getFileExt().equals("gif")||upfiles.getFileExt().equals("jpeg")){
					weekFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid=@sid\" title=\"下载\"></a>";
					weekFile+="\n<a class=\"fa fa-eye\" href=\"javascript:void(0)\" title=\"预览\" onclick=\"showPic('/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"','@sid','"+upfiles.getId()+"','006','"+weekReport.getId()+"')\"></a>";
				
				}else if(upfiles.getFileExt().equals("pdf")||upfiles.getFileExt().equals("txt")){
					weekFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid=@sid\" title=\"下载\"></a>";
					weekFile+="\n<a class=\"fa fa-eye\" href=\"javascript:void(0)\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','@sid','006','"+weekReport.getId()+"')\"></a>";
				}else{
					weekFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"javascript:void(0)\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','@sid')\" title=\"下载\"></a>";
				}
				
				// 企业
				weekRepUpfile.setComId(userInfo.getComId());
				// 周报主键
				weekRepUpfile.setWeekReportId(weekReport.getId());
				// 上传人
				weekRepUpfile.setUserId(userInfo.getId());

				weekReportDao.add(weekRepUpfile);
				//为周报附件创建索引
				uploadService.updateUpfileIndex(weekRepUpfile.getUpfileId(), userInfo, "add",weekReport.getId(),ConstantInterface.TYPE_WEEK);
				
				listFileId.add(weekRepUpfile.getUpfileId());
			}
			//归档到文档中心
			fileCenterService.addModFile(userInfo,listFileId,weekReportT.getWeekRepName());
			
		}
		//周计划
		String weekPlan = "";
		// 周报计划
		List<WeekReportPlan> weekReportPlans = weekReport.getWeekReportPlans();
		if (null != weekReportPlans && weekReportPlans.size() > 0) {
			boolean falg = true;
			for (WeekReportPlan weekReportPlan : weekReportPlans) {
				// 计划时间和计内容没有填写
				
				if ( "0".equals(weekReport.getState()) && StringUtil.isBlank(weekReportPlan.getPlanContent())) {//是发布的话两个都不能为空
					continue;
				}else if( "1".equals(weekReport.getState()) && (StringUtil.isBlank(weekReportPlan.getPlanTime())
						&& StringUtil.isBlank(weekReportPlan.getPlanContent()))) {//是存草稿的话两个不能同时为空
					continue;
				}
				if(falg){
					weekPlan +="<br><br><b>周报计划:</b>";
					falg = false;
				}
				
				if(!StringUtil.isBlank(weekReportPlan.getPlanContent())){//计划内容不为空
					weekPlan+="<br>计划内容:"+weekReportPlan.getPlanContent();
					if(!StringUtil.isBlank(weekReportPlan.getPlanTime())){//计划时间不为空
						weekPlan+="<br>计划完成时间:"+weekReportPlan.getPlanTime();
					}
				}
				// 企业
				weekReportPlan.setComId(userInfo.getComId());
				// 周报主键
				weekReportPlan.setWeekReportId(weekReport.getId());

				weekReportDao.add(weekReportPlan);
			}
		}
		
		String weekQ = "";
		// 周报条目值
		List<WeekReportVal> weekReportVals = weekReport.getWeekReportVals();
		if (null != weekReportVals && weekReportVals.size() > 0) {
			for (WeekReportVal weekReportVal : weekReportVals) {
				WeekReportQ weekReportQ = (WeekReportQ) weekReportDao.objectQuery(WeekReportQ.class, weekReportVal.getQuestionId());
				weekReportDao.add(weekReportVal);
				
				weekQ +="<br><b>"+weekReportQ.getModReportName()+":</b>";
				weekQ +="<br>"+weekReportVal.getReportValue().replace("\n", "<br>");
			}
		}
		
		String content="发布";
		
		if("0".equals(weekReport.getState())){//只有发布后才提醒别人
			
			//当前周报的汇报人设置的查看范围（没有上级）
			List<UserInfo> shares = weekReportDao.listWeekRepViewUser(userInfo.getComId(),userInfo.getId());
			if("1".equals(weekReportT.getState())){//系统里存放的是待办
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo,userInfo.getId(), weekReport.getId(),"发布周报", ConstantInterface.TYPE_WEEK,shares,null);
			}else{
				content ="重新发布";
				//添加消息提醒通知
				todayWorksService.addTodayWorks(userInfo,null, weekReport.getId(),"重新发布周报", ConstantInterface.TYPE_WEEK,shares,null);
			}
			if(null!=shares && shares.size()>0){
				for (UserInfo user : shares) {
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),user.getId(),
							weekReport.getId(),ConstantInterface.TYPE_WEEK,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(userInfo.getComId(), user.getId(), userInfo.getId(),
								todayWorks.getId(), weekReport.getId(), ConstantInterface.TYPE_WEEK,0,userInfo.getUserName()+weekReportT.getYear()+"年第"+weekReportT.getWeekNum()+"周周报");
					}
				}
			}
			
		}else{
			content = "保存";
		}
		
		content +=weekReportT.getYear()+"年第"+weekReportT.getWeekNum()+"周周报";
		
		//添加工作轨迹
		systemLogService.addSystemLogWithTrace(userInfo,  userInfo.getId(), ConstantInterface.TYPE_WEEK, weekReport.getId(),
				content, content);
		
		if(!"".equals(weekQ)){
			content+="<br>"+weekQ;
			if(!"".equals(weekPlan)){
				content+=weekPlan;
			}
			if(!"".equals(weekFile)){
				content+=weekFile;
			}
		}
		this.addWeekRepLog(userInfo.getComId(), weekReport.getId(), userInfo.getId(), userInfo.getUserName(), content);
		//更新周报索引
		this.updateWeekReportIndex(weekReport.getId(),userInfo,"add");
	}
	/**
	 * 修改周报查看人员
	 * @param weekViewerList
	 * @param sessionUser
	 * @param logContent 
	 */
	public void updateWeekViewers(List<WeekViewer> weekViewerList, UserInfo sessionUser, String logContent){
		//本次移除的人员
		List<WeekViewer> removePerson = weekReportDao.listWeekViewerForRemove(weekViewerList,sessionUser);
		Integer comId = sessionUser.getComId();
		Integer userId = sessionUser.getId();
		if(null!=removePerson && !removePerson.isEmpty()){
			//自己已发布的周报集合但还有上级没有处理的
			List<WeekReport> weekList = weekReportDao.listWeekReport(userId,comId,true);
			if( null != weekList && !weekList.isEmpty()){
				for (WeekViewer weekViewerRemove : removePerson) {
					for (WeekReport weekReport : weekList) {
						//删除消息提醒中不在分组的成员的消息
						weekReportDao.delByField("todayWorks", new String[]{"comId","busType","busId","userId"}, 
								new Object[]{comId,ConstantInterface.TYPE_WEEK,weekReport.getId(),weekViewerRemove.getId()});
					}
				}
			}
			
			
		}
		
		// 删除范围
		weekReportDao.delByField("weekViewer", new String[] { "comId",
		"userId" }, new Object[] { comId, userId });
		// 设置范围
		if (null != weekViewerList && !weekViewerList.isEmpty()) {
			for (WeekViewer weekViewer : weekViewerList) {
				weekReportDao.add(weekViewer);
			}
		}
		String content = "设置周报查看人员为("+logContent+")";
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), content, 
				ConstantInterface.TYPE_WEEK, sessionUser.getComId(),sessionUser.getOptIP());
		
	}
	/**
	 * 删除周报查看人员
	 * @param userInfo
	 */
	public void delWeekViewer(WeekViewer weekViewer,UserInfo userInfo){
		//团队号
		Integer comId = userInfo.getComId();
		//周报设置人员
		Integer userId = userInfo.getId();
		//移除的周报查看人员
		Integer viewerId = weekViewer.getViewerId();
		
		List<ImmediateSuper> immediateSupers = userInfoService.listImmediateSuper(userInfo);
		//默认删除人员不是上级
		Boolean isLeader = false;
		if(null!=immediateSupers && !immediateSupers.isEmpty()){
			for (ImmediateSuper immediateSuper : immediateSupers) {
				Integer leaderId = immediateSuper.getLeader();
				if(leaderId.equals(viewerId)){
					isLeader = true;
				}
			}
		}
		if(!userId.equals(viewerId) && !isLeader){//不是周报的发起人也不是上级
			//自己已发布的周报集合但还有上级没有处理的
			List<WeekReport> weekList = weekReportDao.listWeekReport(userId,comId,true);
			if( null != weekList && !weekList.isEmpty()){
				for (WeekReport weekReport : weekList) {
					//删除消息提醒中不在分组的成员的消息
					weekReportDao.delByField("todayWorks", new String[]{"comId","busType","busId","userId"}, 
							new Object[]{comId,ConstantInterface.TYPE_WEEK,weekReport.getId(),viewerId});
				}
			}
		}
		// 删除范围
		weekReportDao.delByField("weekViewer", new String[] { "comId",
				"userId","viewerId"}, new Object[] { comId, userId,viewerId });
	}
	
	/**
	 * 周报列表
	 * 
	 * @param weekReport
	 * @param userInfo
	 * @param  
	 * @return
	 * @throws ParseException 
	 */
	public PageBean<WeekReport> listPagedWeekReport(WeekReportPojo weekReport,
			UserInfo userInfo) throws ParseException {
		boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
		//部门查询
		if(null!=weekReport.getDepId()){
			//查询本部门和下级部门信息
			List<Department> listTreeDeps = departmentService.listTreeSonDep(weekReport.getDepId(), ConstantInterface.SYS_ENABLED_YES, userInfo.getComId());
			//将数据整理成查询条件
			List<Integer> depIds = new ArrayList<Integer>();
			//有查询结果需要遍历
			if(null != listTreeDeps && !listTreeDeps.isEmpty()){
				//遍历部门主键信息
				for (Department department : listTreeDeps) {
					depIds.add(department.getId());
				}
				//数据条件存入
				weekReport.setListTreeDeps(depIds.toArray(new Integer[]{}));
			}
		}
		PageBean<WeekReport> pageBean = new PageBean<WeekReport>();
		//周报列表数据查询
		List<WeekReport> recordList = weekReportDao.listPagedWeekReport(weekReport,
				userInfo,isForceInPersion);
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 获取个人权限下的所有周报（不分页）
	 * @param weekReport
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 * @throws ParseException
	 */
	public List<WeekReport> listWeekReportOfAll(WeekReportPojo weekReport,
			UserInfo userInfo, boolean isForceInPersion) throws ParseException {
		List<WeekReport> list = weekReportDao.listWeekReportOfAll(weekReport,
				userInfo,isForceInPersion);
		return list;
	}
	/**
	 * 获取团队周报主键集合
	 * @param userInfo
	 * @return
	 * @throws ParseException
	 */
	public List<WeekReport> listWeekReportOfAll(UserInfo userInfo) throws ParseException {
		List<WeekReport> list = weekReportDao.listWeekReportOfAll(userInfo);
		return list;
	}
	/**
	 * 获取本周的满足查询条件的数目
	 * @param weekDoneState 0本周已汇报 1本周未汇报(不能为空)
	 * @param userInfo 当前操作人员
	 * @param isForceInPersion 是否强制参与人
	 * @return
	 * @throws ParseException
	 */
	public Integer getWeekReportCount(String weekDoneState,UserInfo userInfo, 
			boolean isForceInPersion) throws ParseException {
		Integer count = weekReportDao.getWeekReportCount(weekDoneState,
				userInfo,isForceInPersion);
		return count;
	}

	/**
	 * 取得所选周报
	 * 
	 * @param id
	 * @param userInfo
	 * @param forceInPersion 
	 * @return
	 * @throws ParseException 
	 */
	public WeekReport getWeekReportForView(Integer id, UserInfo userInfo,WeekReportPojo weekReportParam, boolean forceInPersion) throws ParseException {
		WeekReport weekReport = weekReportDao.getWeekReportForView(id, userInfo,weekReportParam,forceInPersion);
		if (null != weekReport) {
			//设置留言、附件总数
			Integer talkNum = weekReportDao.countTalk(id, userInfo.getComId());
			Integer fileNum = weekReportDao.countFiles(userInfo.getComId(),id );
			weekReport.setFileNum(fileNum);
			weekReport.setTalkNum(talkNum);
			
			if ("1".equals(weekReport.getHasPlan())) {// 需要填写计划
				// 下周计划
				List<WeekReportPlan> weekReportPlans = weekReportDao
						.listWeekReportPlan(weekReport.getId(), userInfo
								.getComId());
				if ((null != weekReportPlans && weekReportPlans.size() > 0)
						|| weekReport.getCountVal() > 0) {// 要么填写过计划，要么填写过内容
					weekReport.setWeekReportPlans(weekReportPlans);
					// 周报内容
					weekReport.setWeekReportQs(weekReportDao.listWeekReportQ(
							weekReport.getId(), userInfo.getComId(), weekReport
									.getReporterId()));
				}
			} else if ("0".equals(weekReport.getHasPlan())
					&& weekReport.getCountVal() > 0) {// 不需要填写计划，填写过内容

				// 周报内容
				weekReport.setWeekReportQs(weekReportDao.listWeekReportQ(
						weekReport.getId(), userInfo.getComId(), weekReport
								.getReporterId()));

			}
		}
		return weekReport;
	}

	/**
	 * 周报日志
	 * 
	 * @param comId
	 * @param weekReportId
	 * @return
	 */
	public List<WeekRepLog> listPagedWeekRepVoteLog(Integer comId,
			Integer weekReportId,String sid) {
		// 周报日志
		List<WeekRepLog> logs = weekReportDao.listPagedVoteLog(weekReportId,
				comId,sid);
		return logs;
	}

	/**
	 * 反馈留言
	 * 
	 * @param weekReportId
	 * @param comId
	 * @return
	 */
	public List<WeekRepTalk> listPagedWeekRepTalk(Integer weekReportId,
			Integer comId,String dest) {
		// 反馈留言
		List<WeekRepTalk> list = weekReportDao.listPagedWeekRepTalk(
				weekReportId, comId);
		List<WeekRepTalk> weekRepTalks = new ArrayList<WeekRepTalk>(); 
		for (WeekRepTalk weekRepTalk : list) {
			if(!"app".equals(dest)){//非手机端字符转换
				//回复内容转换
				weekRepTalk.setContent(StringUtil.toHtml(weekRepTalk.getContent()));
			}
			//讨论的附件
			weekRepTalk.setListWeekRepTalkFile(weekReportDao.listWeekRepTalkFile(comId,weekReportId,weekRepTalk.getId()));
			weekRepTalks.add(weekRepTalk);
		}
		return list;
	}

	/**
	 * 把待办事项更新为普通消息
	 *
	 * @param busType
	 *            业务类型标识
	 * @param busId
	 *            业务类型主键
	 * @param owner
	 *            消息所有者
	 */
	public void updateTodayWorksBusSpecTo0(String busType, Integer busId, Integer owner) {
		TodayWorks todayWork = new TodayWorks();
		todayWork.setBusType(busType);
		todayWork.setBusId(busId);
		todayWork.setUserId(owner);
		todayWork.setBusSpec(ConstantInterface.TYPE_TODAYWORKS_BUSSPEC_0);// 待办事项更新为普通消息0
		msgShareDao.update("update todayWorks a set a.busSpec=:busSpec "
				+ "where a.busType=:busType and a.busId=:busId and a.userId=:userId", todayWork);
	}

	/**
	 * 反馈留言回复
	 * 
	 * @param weekRepTalk
	 * @return
	 * @throws Exception 
	 */
	public Integer addWeekRepTalk(WeekRepTalk weekRepTalk,UserInfo userInfo) throws Exception {
		Integer id = weekReportDao.add(weekRepTalk);
		WeekReport weekReport = (WeekReport) msgShareDao.objectQuery(WeekReport.class, weekRepTalk.getWeekReportId());
		//讨论附件
		Integer[] upfilesId = weekRepTalk.getUpfilesId();
		if(null!=upfilesId && upfilesId.length>0){
			for (Integer upfileId : upfilesId) {
				WeekRepTalkFile weekRepTalkFile = new WeekRepTalkFile();
				//企业编号
				weekRepTalkFile.setComId(weekRepTalk.getComId());
				//周报主键
				weekRepTalkFile.setWeekReportId(weekRepTalk.getWeekReportId());
				//讨论的主键
				weekRepTalkFile.setTalkId(id);
				//附件主键
				weekRepTalkFile.setUpfileId(upfileId);
				//上传人
				weekRepTalkFile.setUserId(weekRepTalk.getTalker());
				
				weekReportDao.add(weekRepTalkFile);
				//为反馈留言附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add",weekRepTalk.getWeekReportId(),ConstantInterface.TYPE_WEEK);
			}
			//添加到文档中心
			fileCenterService.addModFile(userInfo, Arrays.asList(upfilesId), weekReport.getWeekRepName());
		
		}
		//添加信息分享人员
		List<WeekReportShareUser> listWeekReportShareUser = weekRepTalk.getListWeekReportShareUser();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listWeekReportShareUser && !listWeekReportShareUser.isEmpty()){
			for (WeekReportShareUser weekReportShareUser : listWeekReportShareUser) {
				//人员主键
				Integer userId = weekReportShareUser.getUserId();
				pushUserIdSet.add(userId);
				//删除上次的分享人员
				msgShareDao.delByField("weekReportShareUser", new String[]{"comId","weekReportId","userId"},
						new Object[]{userInfo.getComId(),weekRepTalk.getWeekReportId(),userId});
				weekReportShareUser.setWeekReportId(weekRepTalk.getWeekReportId());
				weekReportShareUser.setComId(userInfo.getComId());
				msgShareDao.add(weekReportShareUser);
			}
		}

		//分享信息查看
		List<UserInfo> weekShares = new ArrayList<UserInfo>();
		
		if (null != weekReport) {
			//查询消息的推送人员
			weekShares = msgShareDao.listPushTodoUserForWeekReport(weekRepTalk.getWeekReportId(), userInfo.getComId(),pushUserIdSet);
			//本来是为了区分@和推送，所以在@的时候需要去除掉已经推送的人。
			// 但是推送人实现比较复杂，而且实现区分后与分享模块的方式不同，所以暂时放弃
//            for(int i=0;i<weekShares.size();i++){
//            	for(int j=0;j<shares.size();j++){
//            		if(weekShares.get(i).getId().equals(shares.get(j).getId())){
//            			weekShares.remove(weekShares.get(i));
//					}
//				}
//			}
			Iterator<UserInfo> userids =  weekShares.iterator();
			for(;userids.hasNext();){
				UserInfo user = userids.next();
				if(user.getId().equals(userInfo.getId())){
					userids.remove();
					weekShares.remove(user);
				}
				//设置全部普通消息
				updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_WEEK, weekRepTalk.getWeekReportId(), user.getId());
			}
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_WEEK, weekRepTalk.getWeekReportId(), "参与周报讨论:" + weekRepTalk.getContent(),
					weekShares, userInfo.getId(), 1);

			//清除上次查看记录
			viewRecordService.delViewRecord(userInfo,weekShares,weekRepTalk.getWeekReportId(),ConstantInterface.TYPE_WEEK);
		}

		//更新周报索引
//		this.updateWeekReportIndex(weekRepTalk.getWeekReportId(),userInfo,"update");

		//修改积分
		jiFenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_WEEKTALK,
				"参与周报评论",weekRepTalk.getWeekReportId());
		return id;
	}

	/**
	 * 模块日志添加
	 * 
	 * @param comId
	 * @param weekReportId
	 * @param userId
	 * @param userName
	 * @param content
	 */
	public void addWeekRepLog(Integer comId, Integer weekReportId,
			Integer userId, String userName, String content) {
		WeekRepLog weekRepLog = new WeekRepLog(comId, weekReportId, userId,
				content, userName);
		weekReportDao.add(weekRepLog);
	}

	/**
	 * 反馈留言
	 * 
	 * @param id
	 * @param comId
	 * @return
	 */
	public WeekRepTalk getWeekRepTalk(Integer id, Integer comId) {
		WeekRepTalk weekRepTalk = weekReportDao.getWeekRepTalk(id, comId);
		if(null!=weekRepTalk){
			//回复内容转换
			String content = StringUtil.toHtml(weekRepTalk.getContent());
			//讨论的附件
			weekRepTalk.setListWeekRepTalkFile(weekReportDao.listWeekRepTalkFile(comId,weekRepTalk.getWeekReportId(),weekRepTalk.getId()));
			weekRepTalk.setContent(content);
		}
		return weekRepTalk;
	}

	/**
	 * 删除反馈留言回复
	 * 
	 * @param weekRepTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception 
	 */
	public List<Integer> delWeekRepTalk(WeekRepTalk weekRepTalk,
			String delChildNode,UserInfo userInfo) throws Exception {
		List<Integer> childIds = new ArrayList<Integer>();
		// 删除自己
		if (null == delChildNode) {
			childIds.add(weekRepTalk.getId());
			//删除附件
			weekReportDao.delByField("weekRepTalkFile", new String[]{"comId","talkId"}, 
					new Object[]{weekRepTalk.getComId(),weekRepTalk.getId()});
			
			weekReportDao.delById(WeekRepTalk.class, weekRepTalk.getId());
		} else if ("yes".equals(delChildNode)) {// 删除自己和所有的子节点
			//待删除的讨论
			List<WeekRepTalk> listWeekRepTalk = weekReportDao.listWeekRepTalkForDel(weekRepTalk.getComId(), weekRepTalk.getId());
			for (WeekRepTalk talk : listWeekRepTalk) {
				childIds.add(talk.getId());
				
				//删除附件
				weekReportDao.delByField("weekRepTalkFile", new String[]{"comId","talkId"}, 
						new Object[]{talk.getComId(),talk.getId()});
			}
			//删除当前节点及其子节点回复
			weekReportDao.delWeekRepTalk(weekRepTalk.getId(),weekRepTalk.getComId());
		} else if ("no".equals(delChildNode)) {// 删除自己,将子节点提高一级
			childIds.add(weekRepTalk.getId());
			//删除附件
			weekReportDao.delByField("weekRepTalkFile", new String[]{"comId","talkId"}, 
					new Object[]{weekRepTalk.getComId(),weekRepTalk.getId()});
			
			weekReportDao.updateWeekRepTalkParentId(weekRepTalk.getId(),
					weekRepTalk.getComId());
			weekReportDao.delById(WeekRepTalk.class, weekRepTalk.getId());
		}
		
		//当前周报
		WeekReport weekReport = (WeekReport) weekReportDao.objectQuery(WeekReport.class, weekRepTalk.getWeekReportId());
		//当前周报的汇报人
		Integer reporterId = weekReport.getReporterId();
		
		//当前周报的汇报人设置的查看范围（没有上级）
		List<UserInfo> shares = weekReportDao.listWeekRepViewUser(userInfo.getComId(),userInfo.getId());
		if(!reporterId.equals(userInfo.getId())){//当前操作人员不是周报汇报人
			UserInfo reporter = weekReportDao.getWeekSelf(userInfo.getComId(),reporterId);
			shares.add(reporter);
		}
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, weekRepTalk.getWeekReportId(),"删除周报讨评论", ConstantInterface.TYPE_WEEK,shares,null);
		//修改积分
		jiFenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_TALKDEL,"删除周报讨评论",weekRepTalk.getWeekReportId());
		return childIds;
	}

	/**
	 * 周报附件
	 * 
	 * @param comId
	 * @param weekReportId
	 * @return
	 */
	public List<WeekRepUpfiles> listPagedWeekRepFiles(Integer comId,
			Integer weekReportId) {
		// 周报附件
		List<WeekRepUpfiles> list = weekReportDao.listPagedWeekRepFiles(comId,weekReportId);
		return list;
	}

	/**
	 * 删除模板条目
	 * 
	 * @param id
	 * @param modId
	 * @param repLev 1团队级别 2部门级别 3成员级别
	 * @param comId
	 */
	public WeekRepModContent delModContent(Integer id, Integer modId,
			Integer repLev, Integer comId) {
		// 修改模板版本号
		WeekReportMod temp = (WeekReportMod) weekReportDao.objectQuery(
				WeekReportMod.class, modId);
		if (null != temp) {
			WeekReportMod weekReportMod = new WeekReportMod();
			weekReportMod.setId(modId);
			weekReportMod.setVersion(temp.getVersion() + 1);
			weekReportDao.update(weekReportMod);
		}
		// 模板条目
		WeekRepModContent weekRepModContent = (WeekRepModContent) weekReportDao
				.objectQuery(WeekRepModContent.class, id);
		if (2 == repLev) {// 删除部门
			weekReportDao.delByField("modContDep", new String[] { "comId",
					"modId", "modContId" }, new Object[] { comId, modId, id });
		} else if (3 == repLev) {// 删除成员
			weekReportDao.delByField("modContMember", new String[] { "comId",
					"modId", "modContId" }, new Object[] { comId, modId, id });
		}
		// 删除条目
		weekReportDao.delById(WeekRepModContent.class, id);

		return weekRepModContent;
	}

	/**
	 * 当前时间所在周的周报
	 * 
	 * @param weekNum
	 * @param comId
	 * @param userId
	 * @param year
	 * @return
	 */
	public WeekReport getWeekReport(Integer weekNum, Integer comId,
			Integer userId, String year) {
		// 周报
		WeekReport weekReport = weekReportDao.getWeekReport(weekNum, comId,
				userId, year);
		return weekReport;
	}

	/**
	 * 周报查看权限验证
	 * @param comId
	 * @param userId 查看人
	 * @param reporterId 汇报人
	 * @return
	 */
	public boolean authorCheck(Integer comId, Integer userId, Integer reporterId,Integer busId) {
		List<WeekViewer> listWeekReport = weekReportDao.authorCheck(comId,userId,reporterId,busId);
		if(null!=listWeekReport && !listWeekReport.isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 取得所选周报
	 * @param year 年
	 * @param weekNum 周数
	 * @param userInfo 汇报人
	 * @param weekS 一周开始
	 * @param weekE 一周结束
	 * @return
	 */
	public WeekReport getWeekReport(String year, Integer weekNum,
			UserInfo userInfo, String weekS, String weekE) {
		
		// 取得模板的信息
		WeekReportMod weekReportMod = weekReportDao.getWeekReportMod(userInfo
				.getComId());
		// 初始化模板条目
		List<WeekRepModContent> contentTeamLevs = weekReportDao
				.initWeekRepContent(userInfo.getComId(), userInfo.getId(),
						userInfo.getDepId());
		
		// 周报
		WeekReport weekReport = new WeekReport();
		// 一周的第一天
		weekReport.setWeekS(weekS);
		// 一周的最后一天
		weekReport.setWeekE(weekE);
		// 企业
		weekReport.setComId(userInfo.getComId());
		// 汇报人
		weekReport.setReporterId(userInfo.getId());
		// 标题
		weekReport.setWeekRepName(userInfo.getUserName() + "周总结_" + year
				+ "年第" + weekNum + "周");
		// 周数
		weekReport.setWeekNum(weekNum);
		weekReport.setUserName(userInfo.getUserName());
		// 年
		weekReport.setYear(Integer.parseInt(year));
		// 是否需要填写下周计划
		weekReport.setHasPlan(weekReportMod.getHasPlan());
		// 周报为草稿状态
		weekReport.setState("1");
		// 周报模板版本
		weekReport.setVersion(weekReportMod.getVersion());
		
		// 周报条目
		List<WeekReportQ> weekReportQs = new ArrayList<WeekReportQ>();
		// 周报的条目
		for (WeekRepModContent weekRepModContent : contentTeamLevs) {
			WeekReportQ weekReportQ = new WeekReportQ();
			// 企业
			weekReportQ.setComId(userInfo.getComId());
			// 周报要求
			weekReportQ.setModReportName(weekRepModContent.getModContent());

			weekReportQs.add(weekReportQ);
		}
		weekReport.setWeekReportQs(weekReportQs);
		return weekReport;
	}
	/**
	 * 更新周报索引
	 * @param weekReportId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception 
	 */
	public void updateWeekReportIndex(Integer weekReportId,UserInfo userInfo,String opType) throws Exception{
		//更新周报索引
		WeekReport weekReport = weekReportDao.getWeekReport(weekReportId,userInfo.getComId());
		if(null==weekReport){return;}
		//连接索引字符串
//		StringBuffer attStr = new StringBuffer(weekReport.getWeekRepName()+","+weekReport.getUserName()+",");
		StringBuffer attStr = new StringBuffer(weekReport.getWeekRepName());
		//获取周报计划为其创建索引
//		List<WeekReportPlan> listWeekReportPlan = weekReportDao.listWeekReportPlan4Index(userInfo.getComId(),weekReportId);
//		for(WeekReportPlan vo : listWeekReportPlan){
//			attStr.append(vo.getPlanContent()+",");
//		}
		//获取周报汇报内容为其创建索引
//		List<WeekReportVal> listWeekReportVal = weekReportDao.listWeekReportVal4Index(userInfo.getComId(),weekReportId);
//		for(WeekReportVal vo : listWeekReportVal){
//			attStr.append(vo.getReportValue()+",");
//		}
		//获取反馈留言为其创建索引
//		List<WeekRepTalk> listWeekRepTalkVal = weekReportDao.listWeekRepTalk4Index(userInfo.getComId(),weekReportId);
//		for(WeekRepTalk vo : listWeekRepTalkVal){
//			attStr.append(vo.getContent()+","+vo.getTalkerName()+",");
//		}
		// 周报附件
//		List<WeekRepUpfiles> list = weekReportDao.listPagedWeekRepFiles(userInfo.getComId(),weekReportId);
//		if(null!=list){
//			Upfiles upfile = null;
//			for(WeekRepUpfiles vo:list){
//				//附件内容添加
//				upfile = uploadService.queryUpfile4Index(vo.getUpfileId(),userInfo.getComId());
//				//附件名称
//				attStr.append(upfile.getFilename()+",");
//				//附件内容
//				attStr.append(upfile.getFileContent()+",");
//			}
//		}
		//单线程池
		ExecutorService pool = ThreadPoolExecutor.getInstance();
		String index_key = userInfo.getComId()+"_"+ConstantInterface.TYPE_WEEK+"_"+weekReportId;
		//为周报创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				index_key,userInfo.getComId(),weekReportId,ConstantInterface.TYPE_WEEK,
				weekReport.getWeekRepName(),attStr.toString(),
				DateTimeUtil.parseDate(weekReport.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,index_key));
		}else{
			throw new NullPointerException("创建索引对象不能为空！");
		}
		
	}
	/**
	 * 周报信息推送范围
	 * @param comId
	 * @param busId
	 * @return
	 */
	public List<UserInfo> listWeekViewers(Integer comId, Integer userId, Integer busId) {
		//当前周报
		WeekReport weekReport = (WeekReport) weekReportDao.objectQuery(WeekReport.class, busId);
		//当前周报的汇报人
		Integer reporterId = weekReport.getReporterId();
		
		//当前周报的汇报人设置的查看范围（没有上级）
		List<UserInfo> shares = weekReportDao.listWeekRepViewUser(comId,userId);
		if(!reporterId.equals(userId)){//当前操作人员不是周报汇报人
			UserInfo reporter = weekReportDao.getWeekSelf(comId,reporterId);
			shares.add(reporter);
		}
		return shares;
	}
	/**
	 * 查询周报查看人员
	 * @param sessionUser
	 * @return
	 */
	public List<WeekViewer> listWeekViewer(UserInfo sessionUser) {
		return weekReportDao.listWeekViewer(sessionUser);
	}

	/**
	 * 更具周报主键查询周报信息
	 * @param weekReportId
	 * @return
	 */
	public WeekReport getWeekById(Integer weekReportId) {
		return (WeekReport) weekReportDao.objectQuery(WeekReport.class, weekReportId);
	}
	
	/**
	 * 周报查看权限验证
	 * @param userInfo 当前操作人
	 * @param reporterId 周报主键
	 * @param clockId
	 * @return
	 */
	public boolean authorCheck(UserInfo userInfo,Integer reporterId,Integer clockId,Integer busId){
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_WEEK);
		if(isForceIn){
			return true;
		}else{
			List<WeekViewer> listWeekReport = weekReportDao.authorCheck(userInfo.getComId(),userInfo.getId(),reporterId,busId);
			if(null!=listWeekReport && !listWeekReport.isEmpty()){
				return true;
			}else{
				//查看验证，删除消息提醒
				todayWorksService.updateTodoWorkRead(reporterId,userInfo.getComId(), 
						userInfo.getId(), ConstantInterface.TYPE_WEEK,clockId);
				return false;
			}
		}
	}
	/**
	 * 查询企业提交周报时间设置
	 * @param userInfo
	 * @return
	 */
	public SubTimeSet querySubTimeSet(UserInfo userInfo) {
		return weekReportDao.querySubTimeSet(userInfo);
	}
	/**
	 * 修改设置
	 * @param subTimeSet
	 */
	public void updateSubTimeSet(SubTimeSet subTimeSet) {
		Integer updateNum = weekReportDao.excuteSql("update subTimeSet set  timeType=? where comid=?",new Object[] { subTimeSet.getTimeType(),subTimeSet.getComId()});
		if(updateNum <=0){
			weekReportDao.add(subTimeSet);
		}
	}
	
	/**
	 * 删除周报附件、留言附件
	 * @param wpUpFileId
	 * @param type
	 * @param userInfo
	 * @param weekReportId
	 * @throws Exception
	 */
	public void delWpUpfile(Integer wpUpFileId, String type, UserInfo userInfo, Integer weekReportId) throws Exception {
		if(type.equals("week")){
			WeekRepUpfiles weekRepUpfiles = (WeekRepUpfiles) weekReportDao.objectQuery(WeekRepUpfiles.class, wpUpFileId);
			weekReportDao.delById(WeekRepUpfiles.class, wpUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) weekReportDao.objectQuery(Upfiles.class, weekRepUpfiles.getUpfileId());
			this.addWeekRepLog(userInfo.getComId(),weekReportId,userInfo.getId(),userInfo.getUserName(),"删除了周报附件："+upfiles.getFilename());
		}else{
			WeekRepTalkFile weekRepTalkFile = (WeekRepTalkFile) weekReportDao.objectQuery(WeekRepTalkFile.class, wpUpFileId);
			weekReportDao.delById(WeekRepTalkFile.class, wpUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) weekReportDao.objectQuery(Upfiles.class, weekRepTalkFile.getUpfileId());
			this.addWeekRepLog(userInfo.getComId(),weekReportId,userInfo.getId(),userInfo.getUserName(),"删除了周报留言附件："+upfiles.getFilename());
		}
		
	}
}
