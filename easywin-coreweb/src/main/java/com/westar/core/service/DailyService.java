package com.westar.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.Daily;
import com.westar.base.model.DailyLog;
import com.westar.base.model.DailyMod;
import com.westar.base.model.DailyModContDep;
import com.westar.base.model.DailyModContMember;
import com.westar.base.model.DailyModContent;
import com.westar.base.model.DailyPlan;
import com.westar.base.model.DailyQ;
import com.westar.base.model.DailyShareGroup;
import com.westar.base.model.DailyShareUser;
import com.westar.base.model.DailyTalk;
import com.westar.base.model.DailyTalkFile;
import com.westar.base.model.DailyUpfiles;
import com.westar.base.model.DailyVal;
import com.westar.base.model.DailyViewer;
import com.westar.base.model.DataDic;
import com.westar.base.model.Department;
import com.westar.base.model.FileDetail;
import com.westar.base.model.ImmediateSuper;
import com.westar.base.model.MsgShare;
import com.westar.base.model.MsgShareLog;
import com.westar.base.model.MsgShareTalk;
import com.westar.base.model.MsgShareTalkUpfile;
import com.westar.base.model.ShareGroup;
import com.westar.base.model.ShareUser;
import com.westar.base.model.SubTimeSet;
import com.westar.base.model.TodayWorks;
import com.westar.base.model.Upfiles;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.DailyPojo;
import com.westar.base.pojo.IndexDoc;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.StringUtil;
import com.westar.base.util.ThreadPoolExecutor;
import com.westar.core.dao.DailyDao;
import com.westar.core.dao.DataDicDao;
import com.westar.core.dao.MsgShareDao;
import com.westar.core.web.PaginationContext;

@Service
public class DailyService {

	@Autowired
	DailyDao dailyDao;

	@Autowired
	SystemLogService systemLogService;

	@Autowired
	DataDicDao dataDicDao;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	UploadService uploadService;

	@Autowired
	TodayWorksService todayWorksService;

	@Autowired
	FileCenterService fileCenterService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	ForceInPersionService forceInService;

	@Autowired
    MsgShareDao msgShareDao;

	@Autowired
	JiFenService jiFenService;

	@Autowired
	MsgShareService msgShareService;

    @Autowired
    ViewRecordService viewRecordService;

    /**
	 * 初始化分享模块
	 * @param sessionUser 当前登录用户
	 * @return com.westar.base.model.DailyMod
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:35
	 */
	public DailyMod initDailyMod(UserInfo sessionUser) {
		DailyMod dailyMod = dailyDao.getDailyModMod(sessionUser
				.getComId());
		// 团队级要求
		List<DailyModContent> contentTeamLevs = new ArrayList<DailyModContent>();
		// 部门级要求
		List<DailyModContent> contentGroupLevs = new ArrayList<DailyModContent>();
		// 成员级要求
		List<DailyModContent> contentMemLevs = new ArrayList<DailyModContent>();
		if (null == dailyMod) {// 若是没有模板，则默认一个模板
			dailyMod = new DailyMod();
			// 创建人
			dailyMod.setCereaterId(sessionUser.getId());
			// 公司
			dailyMod.setComId(sessionUser.getComId());
			// 名称
			dailyMod.setModName("默认模板");
			// 是否有今日计划
			dailyMod.setHasPlan("1");
			// 模板版本
			dailyMod.setVersion(1);
			// 模板主键
			Integer id = dailyDao.add(dailyMod);
			dailyMod.setId(id);

			// 取得默认的模板条目
			List<DataDic> dataDics = dataDicDao.listDataDic("dailyModContent");
			for (DataDic dataDic : dataDics) {
				// 模板内容
				DailyModContent dailyModContent = new DailyModContent();
				// 创建人
				dailyModContent.setCereaterId(sessionUser.getId());
				// 公司
				dailyModContent.setComId(sessionUser.getComId());
				// 所属模板
				dailyModContent.setModId(id);
				// 分享填写时显示
				dailyModContent.setHideState("0");
				// 默认只有团队级别
				dailyModContent.setDailyLev("1");
				// 默认是系统级别的
				dailyModContent.setSysState(1);
				// 模板条目内容
				dailyModContent.setModContent(dataDic.getZvalue());
				//默认不是必填
				dailyModContent.setIsRequire("1");
				// 模板条目主键
				Integer contentId = dailyDao.add(dailyModContent);
				dailyModContent.setId(contentId);

				contentTeamLevs.add(dailyModContent);
			}
			systemLogService.addSystemLog(sessionUser.getId(), sessionUser
							.getUserName(), "初始化分享模板", ConstantInterface.TYPE_DAILY,
					sessionUser.getComId(),sessionUser.getOptIP());
		} else {
			// 模板团队级条目
			contentTeamLevs = dailyDao.listDailyModContent(dailyMod
					.getId(), dailyMod.getComId(), "1");

			// 模板部门级条目
			contentGroupLevs = dailyDao.listDailyModContent(
					dailyMod.getId(), dailyMod.getComId(), "2");
			for (DailyModContent contentDepLev : contentGroupLevs) {
				List<DailyModContDep> modContDeps = new ArrayList<DailyModContDep>();
				modContDeps = dailyDao.listDailyModContDep(contentDepLev
						.getId(), contentDepLev.getComId(), contentDepLev
						.getModId());
				contentDepLev.setListDeps(modContDeps);
			}

			// 模板成员级条目
			contentMemLevs = dailyDao.listDailyModContent(dailyMod
					.getId(), dailyMod.getComId(), "3");
			for (DailyModContent contentMemLev : contentMemLevs) {
				List<DailyModContMember> modContMembers = new ArrayList<DailyModContMember>();
				modContMembers = dailyDao.listDailyModContMember(contentMemLev
						.getId(), contentMemLev.getComId(), contentMemLev
						.getModId());
				contentMemLev.setListMembers(modContMembers);
			}
		}
		// 模板团队级条目
		dailyMod.setContentTeamLevs(contentTeamLevs);
		// 模板部门级条目
		dailyMod.setContentGroupLevs(contentGroupLevs);
		// 模板成员级条目
		dailyMod.setContentMemLevs(contentMemLevs);
		return dailyMod;
	}


	/**
	 * 分享列表，暂未使用
	 * @param dailyPojo 分享，用于查询
 	 * @param userInfo 当前登录用户
 	 * @param isForceInPersion 日否为督察人员
	 * @return java.util.List<com.westar.base.model.Daily>
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:35
	 */
	public List<Daily> listPagedDaily(DailyPojo dailyPojo,
									  UserInfo userInfo, boolean isForceInPersion) throws ParseException {

		//部门查询
		if(null!=dailyPojo.getDepId()){
			//查询本部门和下级部门信息
			List<Department> listTreeDeps = departmentService.listTreeSonDep(dailyPojo.getDepId(), ConstantInterface.SYS_ENABLED_YES, userInfo.getComId());
			//将数据整理成查询条件
			List<Integer> depIds = new ArrayList<Integer>();
			//有查询结果需要遍历
			if(null != listTreeDeps && !listTreeDeps.isEmpty()){
				//遍历部门主键信息
				for (Department department : listTreeDeps) {
					depIds.add(department.getId());
				}
				//数据条件存入
				dailyPojo.setListTreeDeps(depIds.toArray(new Integer[]{}));
			}
		}
		//分享列表数据查询
		List<Daily> list = dailyDao.listPagedDaily(dailyPojo,
				userInfo,isForceInPersion);

		return list;
	}

	/**
	 * 添加分享模块内容
	 * @param dailyModContent 分享模块内容
 	 * @param members 成员主键数组
 	 * @param deps 部门主键数组
	 * @return java.lang.Integer
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:35
	 */
	public Integer addDailyModContent(DailyModContent dailyModContent,
									 String[] members, String[] deps) {

		// 修改模板版本号
		DailyMod temp = (DailyMod) dailyDao.objectQuery(
				DailyMod.class, dailyModContent.getModId());
		if (null != temp) {
			DailyMod dailyMod = new DailyMod();
			dailyMod.setId(dailyModContent.getModId());
			dailyMod.setVersion(temp.getVersion() + 1);
			dailyDao.update(dailyMod);
		}
		// 模板条目主键
		Integer id = dailyDao.add(dailyModContent);
		// 成员信息
		if (null != members && members.length > 0) {
			for (String memberId : members) {
				DailyModContMember dailyModContMember = new DailyModContMember();
				// 公司编号
				dailyModContMember.setComId(dailyModContent.getComId());
				// 模板内容主键
				dailyModContMember.setModContId(id);
				// 模板主键
				dailyModContMember.setModId(dailyModContent.getModId());
				// 人员主键
				dailyModContMember.setMemberId(Integer.parseInt(memberId));
				//是否必填
				dailyModContMember.setIsRequire(dailyModContent.getIsRequire());

				dailyDao.add(dailyModContMember);
			}
		}
		// 部门信息
		if (null != deps && deps.length > 0) {
			for (String depId : deps) {
				DailyModContDep dailyModContDep = new DailyModContDep();
				// 公司编号
				dailyModContDep.setComId(dailyModContent.getComId());
				// 模板内容主键
				dailyModContDep.setModContId(id);
				// 模板主键
				dailyModContDep.setModId(dailyModContent.getModId());
				// 部门主键
				dailyModContDep.setDepId(Integer.parseInt(depId));
				//是否必填
				dailyModContDep.setIsRequire(dailyModContent.getIsRequire());

				dailyDao.add(dailyModContDep);
			}
		}
		return id;
	}

	/**
	 * 删除模块内容
	 * @param id 分享主键
 	 * @param modId 模块主键
 	 * @param dailyLev 分享等级
 	 * @param comId 公司主键
	 * @return com.westar.base.model.DailyModContent
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:36
	 */
	public DailyModContent delModContent(Integer id, Integer modId,
										   Integer dailyLev, Integer comId) {
		// 修改模板版本号
		DailyMod temp = (DailyMod) dailyDao.objectQuery(
				DailyMod.class, modId);
		if (null != temp) {
			DailyMod dailyMod = new DailyMod();
			dailyMod.setId(modId);
			dailyMod.setVersion(temp.getVersion() + 1);
			dailyDao.update(dailyMod);
		}
		// 模板条目
		DailyModContent dailyModContent = (DailyModContent) dailyDao
				.objectQuery(DailyModContent.class, id);
		if (2 == dailyLev) {// 删除部门
			dailyDao.delByField("dailyModContDep", new String[] { "comId",
					"modId", "modContId" }, new Object[] { comId, modId, id });
		} else if (3 == dailyLev) {// 删除成员
			dailyDao.delByField("dailyModContMember", new String[] { "comId",
					"modId", "modContId" }, new Object[] { comId, modId, id });
		}
		// 删除条目
		dailyDao.delById(DailyModContent.class, id);

		return dailyModContent;
	}


	/**
	 * 修改模块内容
	 * @param dailyModContent 分享模块内容
 	 * @param members 成员主键数组
 	 * @param deps 部门主键数组
	 * @return com.westar.base.model.DailyModContent
	 * @author LiuXiaoLin
	 * @date 2018/6/13 0013 13:36
	 */
	public DailyModContent updateDailyModContent(
			DailyModContent dailyModContent, String[] members, String[] deps) {
		// 修改模板版本号
		DailyMod temp = (DailyMod) dailyDao.objectQuery(
				DailyMod.class, dailyModContent.getModId());
		if (null != temp) {
			DailyMod dailyMod = new DailyMod();
			dailyMod.setId(dailyModContent.getModId());
			dailyMod.setVersion(temp.getVersion() + 1);
			dailyDao.update(dailyMod);
		}
		// 修改模板条目
		dailyDao.update(dailyModContent);
		// 成员不为空则重新赋值
		if (null != members && members.length > 0) {
			// 删除原有的
			dailyDao.delByField("dailyModContMember", new String[] { "comid",
					"modId", "modContId" }, new Object[] {
					dailyModContent.getComId(), dailyModContent.getModId(),
					dailyModContent.getId() });
			for (String memberId : members) {
				DailyModContMember dailyModContMember = new DailyModContMember();
				// 公司编号
				dailyModContMember.setComId(dailyModContent.getComId());
				// 模板内容主键
				dailyModContMember.setModContId(dailyModContent.getId());
				// 模板主键
				dailyModContMember.setModId(dailyModContent.getModId());
				// 人员主键
				dailyModContMember.setMemberId(Integer.parseInt(memberId));

				dailyDao.add(dailyModContMember);
			}
		}
		// 部门不为空则重新赋值
		if (null != deps && deps.length > 0) {
			// 删除原有的
			dailyDao.delByField("dailyModContDep", new String[] { "comid",
					"modId", "modContId" }, new Object[] {
					dailyModContent.getComId(), dailyModContent.getModId(),
					dailyModContent.getId() });
			// 部门信息
			if (null != deps && deps.length > 0) {
				for (String depId : deps) {
					DailyModContDep dailyModContDep = new DailyModContDep();
					// 公司编号
					dailyModContDep.setComId(dailyModContent.getComId());
					// 模板内容主键
					dailyModContDep.setModContId(dailyModContent.getId());
					// 模板主键
					dailyModContDep.setModId(dailyModContent.getModId());
					// 部门主键
					dailyModContDep.setDepId(Integer.parseInt(depId));

					dailyDao.add(dailyModContDep);
				}
			}
		}
		return (DailyModContent) dailyDao.objectQuery(
				DailyModContent.class, dailyModContent.getId());
	}



	/**
	 *修改模板
	 *
	 * @param dailyMod
	 */
	public void updateDailyMod(DailyMod dailyMod) {
		DailyMod temp = (DailyMod) dailyDao.objectQuery(
				DailyMod.class, dailyMod.getId());
		if (null != temp) {
			dailyMod.setVersion(temp.getVersion() + 1);
		}
		dailyDao.update(dailyMod);

	}



	/**
	 * 取得所选日期所写分享
	 *
	 * @param userInfo
	 * @return
	 * @throws ParseException
	 */
	public Daily initDaily(UserInfo userInfo,String chooseDate) throws ParseException {
		// 分享
		Daily daily = dailyDao.getDaily(userInfo.getComId(),userInfo.getId(),chooseDate);
		// 分享是否填写
		boolean flag = false;
		// 已经初始化过分享了
		if (null != daily) {
			//查询显示留言附件总数
			Integer talkNum = dailyDao.countTalk(daily.getId(), userInfo.getComId());
			Integer fileNum = dailyDao.countFile(userInfo.getComId(), daily.getId());
			daily.setFileNum(fileNum);
			daily.setTalkNum(talkNum);
			
			if ("1".equals(daily.getHasPlan())) {// 需要填写计划
				// 今日计划
				List<DailyPlan> dailyPlans = dailyDao.listDailyPlan(daily.getId(), userInfo.getComId());
				if ((null != dailyPlans && dailyPlans.size() > 0) || daily.getCountVal() > 0) {// 要么填写过计划，要么填写过内容
					daily.setDailyPlans(dailyPlans);
					// 分享内容
					daily.setDailyQs(dailyDao.listDailyQ(daily.getId(), userInfo.getComId(), userInfo.getId()));
					flag = true;
				}
			} else if ("0".equals(daily.getHasPlan())) {// 不需要填写计划
				if (daily.getCountVal() > 0) {// 填写过内容
					// 分享内容
					daily.setDailyQs(dailyDao.listDailyQ(daily.getId(), userInfo.getComId(), userInfo.getId()));
					flag = true;
				}
			}
		}
		if (!flag) {
			// 没有初始化分享或是没有写分享
			daily = this.initDailyContent(userInfo,chooseDate);
			if (null != daily) {
				// 没有值
				daily.setCountVal(0);
			}else{
				return null;
			}
		}

		// 分享附件
		daily.setDailyFiles(dailyDao.listDailyFiles(daily.getId(), userInfo
						.getComId()));

		//分享范围组
		if(null != daily.getScopeType()){
			if(daily.getScopeType() == 1){//是否为自定义的分享
				List<DailyShareGroup> listDailyShareGroup = dailyDao.listDailyShareGroup(daily.getId());
				if(null != listDailyShareGroup && !listDailyShareGroup.isEmpty()){
					StringBuffer grpName = new StringBuffer();
					StringBuffer scopeStr = new StringBuffer();
					StringBuffer selStr = new StringBuffer();
					//拼接组名称用于页面显示
					for(int i=0;i<listDailyShareGroup.size();i++){
						DailyShareGroup dailyShareGroup = listDailyShareGroup.get(i);
						if(0 == i){
							grpName.append(dailyShareGroup.getGrpName());
							scopeStr.append(dailyShareGroup.getGrpId() + "@1");
							selStr.append(dailyShareGroup.getGrpName());
						}else{
							grpName.append("," + listDailyShareGroup.get(i).getGrpName());
							scopeStr.append("," + dailyShareGroup.getGrpId() + "@1");
							selStr.append("," + dailyShareGroup.getGrpName());
						}
					}
					daily.setScopeTypeStr(scopeStr.toString());
					daily.setScopeTypeSel(selStr.toString());
					daily.setGrpNameStr(grpName.toString());
				}
				daily.setListDailyShareGroup(listDailyShareGroup);
			}else if(daily.getScopeType() == 0){
				daily.setScopeStr("-1@0");
				daily.setScopeTypeSel("所有同事");
			}else{
				daily.setScopeStr("2@2");
				daily.setScopeTypeSel("我自己");
			}
		}
		return daily;
	}

	/**
	 * 初始化分享内容
	 *
	 * @param userInfo
	 * @return
	 */
	private Daily initDailyContent(UserInfo userInfo,String chooseDate) {

		// 取得模板的信息
		DailyMod dailyMod = dailyDao.getDailyMod(userInfo.getComId());
		if (null == dailyMod) {// 没有设置分享模板的应通知管理员
			dailyMod = this.initDailyMod(userInfo);
		}
		// 初始化模板条目
		List<DailyModContent> contentTeamLevs = dailyDao
				.initDailyContent(userInfo.getComId(), userInfo.getId(),userInfo.getDepId());
		if (null == contentTeamLevs || contentTeamLevs.size() == 0) {// 模板可用没有条目
			return null;
		}

		//如何模版数大于1说明有设置的模块，就去除默认模版
		if(contentTeamLevs.size()>1){
			contentTeamLevs.remove(contentTeamLevs.get(0));
		}

		// 是否已经初始化分享
		Daily daily = dailyDao.getDailyQ(userInfo.getComId(),userInfo.getId(),chooseDate);

		Integer dailyId;
		// 分享条目
		List<DailyQ> dailyQs = new ArrayList<DailyQ>();
		// 已经初始化分享,且版本没有变过
		if (null != daily && daily.getCountQues() > 0
				&& daily.getVersion().equals(dailyMod.getVersion()) ) {
			// 分享条目
			dailyQs = dailyDao.listDailyQ(daily.getId(),
					userInfo.getComId(), userInfo.getId());
			daily.setDailyQs(dailyQs);
			return daily;
		} else if (null != daily
				&& (daily.getVersion() != dailyMod.getVersion() || daily
				.getCountQues() == 0)) {// 已经初始化分享,版本变过了

			// 分享模板版本
			daily.setVersion(dailyMod.getVersion());
			// 是否有计划
			daily.setHasPlan(dailyMod.getHasPlan());

			dailyId = daily.getId();
			// 修改版本
			dailyDao.update(daily);
			// 删除旧版本的条目
			dailyDao.delByField("dailyQ", new String[] { "comId",
					"dailyId" }, new Object[] { userInfo.getComId(),
					dailyId });
		} else {// 还未初始化分享的，初始化一个
			// 分享
			daily = new Daily();
			// 企业
			daily.setComId(userInfo.getComId());
			// 汇报人
			daily.setReporterId(userInfo.getId());
			// 标题
			daily.setDailyName(userInfo.getUserName() + " " + chooseDate + " 分享");
			// 是否需要填写今日计划
			daily.setHasPlan(dailyMod.getHasPlan());
			// 分享为草稿状态
			daily.setState("1");
			//初始化时将范围设置为私有
			daily.setScopeType(2);
			// 分享模板版本
			daily.setVersion(dailyMod.getVersion());
			//分享日期
			daily.setDailyDate(chooseDate.substring(0,10));

			dailyId = dailyDao.add(daily);
			daily.setId(dailyId);
		}
		// 分享的条目
		for (DailyModContent dailyModContent : contentTeamLevs) {
			DailyQ dailyQ = new DailyQ();
			// 企业
			dailyQ.setComId(userInfo.getComId());
			// 分享要求
			dailyQ.setDailyName(dailyModContent.getModContent());
			// 分享主键
			dailyQ.setDailyId(dailyId);
			//是否必填
			dailyQ.setIsRequire(dailyModContent.getIsRequire());
			//是否系统默认的
			dailyQ.setSysState(dailyModContent.getSysState());

			Integer questionId = dailyDao.add(dailyQ);
			dailyQ.setId(questionId);

			dailyQs.add(dailyQ);
		}
		daily.setDailyQs(dailyQs);
		return daily;

	}

	/**
	 * 分享条目
	 *
	 * @param dailyId
	 *            分享
	 * @param comId
	 *            企业
	 * @param reporterId
	 *            汇报人
	 * @return
	 */
	public List<DailyQ> listDailyQ(Integer dailyId,Integer comId, Integer reporterId) {
		List<DailyQ> list = dailyDao.listDailyQ(dailyId,comId, reporterId);
		return list;
	}

	/**
	 * 填写分享
	 *
	 * @param daily
	 * @param userInfo
	 * @throws Exception
	 */
	public void addDaily(Daily daily, UserInfo userInfo,MsgShare msgShare) throws Exception {
		// 本次分享附件
		List<DailyUpfiles> preFiles = daily.getDailyFiles();
		// 本次附件的主键
		String fileIds = "0";
		if (null != preFiles && preFiles.size() > 0) {
			for (DailyUpfiles dailyUpfile : preFiles) {
				fileIds += "," + dailyUpfile.getUpfileId();
			}
		}
		// 需要删除的附件
		List<DailyUpfiles> listRemoveDailyFiles = dailyDao
				.listRemoveDailyFiles(userInfo.getComId(), daily.getId(),
						fileIds);
		if (null != listRemoveDailyFiles && listRemoveDailyFiles.size() > 0) {
			for (DailyUpfiles dailyUpfile : listRemoveDailyFiles) {
				// 文件详情
				FileDetail fileDetail = new FileDetail();
				//企业编号
				fileDetail.setComId(userInfo.getComId());
				// 所属模块
				fileDetail.setModuleType(ConstantInterface.TYPE_DAILY);
				// 所在企业编号
				fileDetail.setComId(userInfo.getComId());
				// 文件主键
				fileDetail.setFileId(dailyUpfile.getUpfileId());
				// 附件管理删除已归档文件
//				fileCenterService.delFile(fileDetail,userInfo);
				//删除附件讨论
				this.delDailyFileTalk(userInfo.getComId(),dailyUpfile.getDailyId(),dailyUpfile.getUpfileId());
			}
		}

		// 删除过去的附件
		dailyDao.delByField("dailyUpfiles", new String[] { "comId",
				"dailyId" }, new Object[] { userInfo.getComId(),
				daily.getId() });
		// 删除过去的今日计划
		dailyDao.delByField("dailyPlan", new String[] { "comId",
				"dailyId" }, new Object[] { userInfo.getComId(),
				daily.getId() });
		// 删除过去的赋值
		dailyDao.delByField("dailyVal", new String[] { "comId",
				"dailyId" }, new Object[] { userInfo.getComId(),
				daily.getId() });

		Daily dailyT = (Daily) dailyDao.objectQuery(Daily.class, daily.getId());
		if("1".equals(dailyT.getState())){//系统里存放的是待办
			if("0".equals(daily.getState())){//本次是发布
				daily.setRecordCreateTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
				// 修改分享
				dailyDao.update("update daily set state=:state,recordCreateTime=:recordCreateTime where id=:id", daily);
			}
		}else if("0".equals(dailyT.getState())){//系统里面是发布
			if("1".equals(daily.getState())){//本次是待办
				// 修改分享
				dailyDao.update("update daily set state=:state where id=:id", daily);
			}

		}

		String dailyFile = "";
		// 分享附件
		List<DailyUpfiles> dailyFiles = daily.getDailyFiles();
		if (null != dailyFiles && dailyFiles.size() > 0) {
			dailyFile +="<br><br><b>分享附件:</b>";
			for (DailyUpfiles dailyUpfile : dailyFiles) {

				Upfiles upfiles = (Upfiles) dailyDao.objectQuery(Upfiles.class, dailyUpfile.getUpfileId());
				//附件名称
				dailyFile +="<br>"+upfiles.getFilename();
				if(upfiles.getFileExt().equals("doc")||
						upfiles.getFileExt().equals("docx")||
						upfiles.getFileExt().equals("xls")||
						upfiles.getFileExt().equals("xlsx")||
						upfiles.getFileExt().equals("ppt")||
						upfiles.getFileExt().equals("pptx")){

					dailyFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"javascript:void(0)\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','@sid')\" title=\"下载\"></a>";
					dailyFile+="\n<a class=\"fa fa-eye\" href=\"javascript:void(0)\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','@sid','050','"+daily.getId()+"')\"></a>";
				}else if(upfiles.getFileExt().equals("jpg")||upfiles.getFileExt().equals("bmp")||upfiles.getFileExt().equals("png")
						|| upfiles.getFileExt().equals("gif")||upfiles.getFileExt().equals("jpeg")){
					dailyFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid=@sid\" title=\"下载\"></a>";
					dailyFile+="\n<a class=\"fa fa-eye\" href=\"javascript:void(0)\" title=\"预览\" onclick=\"showPic('/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"','@sid','"+upfiles.getId()+"','050','"+daily.getId()+"')\"></a>";

				}else if(upfiles.getFileExt().equals("pdf")||upfiles.getFileExt().equals("txt")){
					dailyFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"/downLoad/down/"+upfiles.getUuid()+"/"+upfiles.getFilename()+"?sid=@sid\" title=\"下载\"></a>";
					dailyFile+="\n<a class=\"fa fa-eye\" href=\"javascript:void(0)\" title=\"预览\" onclick=\"viewOfficePage('"+upfiles.getId()+"','"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','"+upfiles.getFileExt()+"','@sid','050','"+daily.getId()+"')\"></a>";
				}else{
					dailyFile+="<a class=\"fa fa-download\" style=\"padding-left: 10px;padding-right: 10px\" href=\"javascript:void(0)\" onclick=\"downLoad('"+upfiles.getUuid()+"','"+upfiles.getFilename()+"','@sid')\" title=\"下载\"></a>";
				}

				// 企业
				dailyUpfile.setComId(userInfo.getComId());
				// 分享主键
				dailyUpfile.setDailyId(daily.getId());
				// 上传人
				dailyUpfile.setUserId(userInfo.getId());

				dailyDao.add(dailyUpfile);
				//为分享附件创建索引
				uploadService.updateUpfileIndex(dailyUpfile.getUpfileId(), userInfo, "add",daily.getId(),ConstantInterface.TYPE_DAILY);
			}
		}
		//日计划
		String weekPlan = "";
		// 分享计划
		List<DailyPlan> dailyPlans = daily.getDailyPlans();
		if (null != dailyPlans && dailyPlans.size() > 0) {
			boolean falg = true;
			for (DailyPlan dailyPlan : dailyPlans) {
				// 计划时间和计内容没有填写

				if ( "0".equals(daily.getState()) && StringUtil.isBlank(dailyPlan.getPlanContent())) {//是发布的话两个都不能为空
					continue;
				}else if( "1".equals(daily.getState()) && (StringUtil.isBlank(dailyPlan.getPlanTime())
						&& StringUtil.isBlank(dailyPlan.getPlanContent()))) {//是存草稿的话两个不能同时为空
					continue;
				}
				if(falg){
					weekPlan +="<br><br><b>分享计划:</b>";
					falg = false;
				}

				if(!StringUtil.isBlank(dailyPlan.getPlanContent())){//计划内容不为空
					weekPlan+="<br>计划内容:"+dailyPlan.getPlanContent();
					if(!StringUtil.isBlank(dailyPlan.getPlanTime())){//计划时间不为空
						weekPlan+="<br>计划完成时间:"+dailyPlan.getPlanTime();
					}
				}
				// 企业
				dailyPlan.setComId(userInfo.getComId());
				// 分享主键
				dailyPlan.setDailyId(daily.getId());

				dailyDao.add(dailyPlan);
			}
		}

		String dailyQStr = "";
		// 分享条目值
		List<DailyVal> dailyVals = daily.getDailyVals();
		if (null != dailyVals && dailyVals.size() > 0) {
			for (DailyVal dailyVal : dailyVals) {
				DailyQ dailyQ = (DailyQ) dailyDao.objectQuery(DailyQ.class, dailyVal.getQuestionId());
				dailyDao.add(dailyVal);

				dailyQStr +="<br><b>"+dailyQ.getDailyName()+":</b>";
				dailyQStr +="<br>"+dailyVal.getDailyValue().replace("\n", "<br>");
			}
		}

		//设置默认值
		msgShare.setComId(dailyT.getComId());
		msgShare.setModId(dailyT.getId());
		msgShare.setContent((dailyQStr == null || dailyQStr.equals("")) ? "未填写" : dailyQStr);
		msgShare.setTraceType(0);
		msgShare.setIsPub(1);
		msgShare.setAction("post");
		msgShare.setCreator(dailyT.getReporterId());
		msgShare.setType(ConstantInterface.TYPE_DAILY);
		//根据当前的dailyid去获取分享表中是否存在数据
		MsgShare msgShareT = msgShareDao.getMsgShareByModId(ConstantInterface.TYPE_DAILY,dailyT.getId(),dailyT.getComId(),0);
		if(null == msgShareT){//不存在则添加
			//添加到分享表中，用于首页显示
			if(null!=daily.getState() && "0".equals(daily.getState())){
				Integer id = msgShareService.addMsgShare(msgShare,userInfo);
				msgShare.setId(id);
				msgShare.setRecordCreateTime(dailyT.getDailyDate() + " " + dailyT.getRecordCreateTime().substring(11,19));
				dailyDao.update("update msgShare a set a.recordCreateTime=:recordCreateTime where a.id=:id",msgShare);
			}
		}else{//存在则修改可能改变的内容和分享范围
			msgShare.setId(msgShareT.getId());
			dailyDao.update("update msgShare a set a.content=:content,a.scopeType=:scopeType where a.id=:id",msgShare);
		}

		//有可能是重新发布，所以直接删除原来可能存在的组
		dailyDao.delByField("dailyShareGroup","dailyId",dailyT.getId());
		// 配置信息分享范围
		List<DailyShareGroup> listDailyShareGroup = daily.getListDailyShareGroup();
		if (null != listDailyShareGroup && !listDailyShareGroup.isEmpty()) {
			for (DailyShareGroup dailyShareGroup : listDailyShareGroup) {
				// 分享信息主键
				dailyShareGroup.setDailyId(dailyT.getId());
				// 添加分享范围
				dailyDao.add(dailyShareGroup);
			}
		}

		//state为1是说明为存草稿。
		if(null != daily.getState() && daily.getState().equals("0")){
			//修改分享的范围
			dailyDao.update("update daily a set a.scopeType=:scopeType where a.id=:id",daily);
		}

		// 保存最近使用的分组
		msgShareService.updateUsedGrp(msgShare);

		String content="发布";

		if("0".equals(daily.getState())){//只有发布后才提醒别人

			//当前分享的汇报人设置的查看范围（没有上级）
			List<UserInfo> shares = dailyDao.listDailyViewUser(userInfo.getComId(),dailyT.getId(),userInfo.getId());
			//清理之前的待办消息
			for(int i=0;i<shares.size();i++){
				UserInfo userInfoT = shares.get(i);
				todayWorksService.delTodoWork(dailyT.getId(),userInfoT.getComId(),userInfoT.getId(),ConstantInterface.TYPE_DAILY,null);
			}
			if("1".equals(dailyT.getState())){//系统里存放的是待办
				//添加待办提醒通知
				todayWorksService.addTodayWorks(userInfo.getComId(),ConstantInterface.TYPE_DAILY,dailyT.getId(),"发布分享",shares,dailyT.getReporterId(),1);
			}else{
				content ="重新发布";
				//添加消息提醒通知
				todayWorksService.addTodayWorks(userInfo.getComId(),ConstantInterface.TYPE_DAILY,dailyT.getId(),"重新发布分享",shares,dailyT.getReporterId(),1);
			}
			if(null!=shares && shares.size()>0){
				for (UserInfo user : shares) {
					//取得待办事项主键
					TodayWorks todayWorks = todayWorksService.getUserTodayWork(userInfo.getComId(),user.getId(),
							daily.getId(),ConstantInterface.TYPE_DAILY,0);
					if(null!=todayWorks){
						JpushUtils.sendTodoMessage(userInfo.getComId(), user.getId(), userInfo.getId(),
								todayWorks.getId(), daily.getId(), ConstantInterface.TYPE_DAILY,0,userInfo.getUserName() + dailyT.getDailyDate() + "日分享");
					}
				}
			}

		}else{
			content = "保存";
		}

		content +=dailyT.getDailyDate() + "日分享";

		//添加工作轨迹
		systemLogService.addSystemLogWithTrace(userInfo,  userInfo.getId(), ConstantInterface.TYPE_DAILY, daily.getId(),
				content, content);

		if(!"".equals(dailyQStr)){
			content+="<br>"+dailyQStr;
			if(!"".equals(weekPlan)){
				content+=weekPlan;
			}
			if(!"".equals(dailyFile)){
				content+=dailyFile;
			}
		}

		this.addDailyLog(userInfo.getComId(), daily.getId(), userInfo.getId(), userInfo.getUserName(), content);

		//更新分享索引
		this.updateDailyIndex(daily.getId(),userInfo,"add");
	}


	/**
	 * 修改分享查看人员
	 * @param dailyViewerList
	 * @param sessionUser
	 * @param logContent
	 */
	public void updateDailyViewers(List<DailyViewer> dailyViewerList, UserInfo sessionUser, String logContent){
		//本次移除的人员
		List<DailyViewer> removePerson = dailyDao.listDailyViewerForRemove(dailyViewerList,sessionUser);
		Integer comId = sessionUser.getComId();
		Integer userId = sessionUser.getId();
		if(null!=removePerson && !removePerson.isEmpty()){
			//自己已发布的分享集合但还有上级没有处理的
			List<Daily> dailyList = dailyDao.listDaily(userId,comId,true);
			if( null != dailyList && !dailyList.isEmpty()){
				for (DailyViewer dailyViewerRemove : removePerson) {
					for (Daily daily : dailyList) {
						//删除消息提醒中不在分组的成员的消息
						dailyDao.delByField("todayWorks", new String[]{"comId","busType","busId","userId"},
								new Object[]{comId,ConstantInterface.TYPE_DAILY,daily.getId(),dailyViewerRemove.getId()});
					}
				}
			}


		}

		// 删除范围
		dailyDao.delByField("dailyViewer", new String[] { "comId",
				"userId" }, new Object[] { comId, userId });
		// 设置范围
		if (null != dailyViewerList && !dailyViewerList.isEmpty()) {
			for (DailyViewer dailyViewer : dailyViewerList) {
				dailyDao.add(dailyViewer);
			}
		}
		String content = "设置分享查看人员为("+logContent+")";
		systemLogService.addSystemLog(sessionUser.getId(), sessionUser.getUserName(), content,
				ConstantInterface.TYPE_DAILY, sessionUser.getComId(),sessionUser.getOptIP());

	}
	/**
	 * 删除分享查看人员
	 * @param userInfo
	 */
	public void delDailyViewer(DailyViewer dailyViewer,UserInfo userInfo){
		//团队号
		Integer comId = userInfo.getComId();
		//分享设置人员
		Integer userId = userInfo.getId();
		//移除的分享查看人员
		Integer viewerId = dailyViewer.getViewerId();

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
		if(!userId.equals(viewerId) && !isLeader){//不是分享的发起人也不是上级
			//自己已发布的分享集合但还有上级没有处理的
			List<Daily> dailyList = dailyDao.listDaily(userId,comId,true);
			if( null != dailyList && !dailyList.isEmpty()){
				for (Daily daily : dailyList) {
					//删除消息提醒中不在分组的成员的消息
					dailyDao.delByField("todayWorks", new String[]{"comId","busType","busId","userId"},
							new Object[]{comId,ConstantInterface.TYPE_DAILY,daily.getId(),viewerId});
				}
			}
		}
		// 删除范围
		dailyDao.delByField("dailyViewer", new String[] { "comId",
				"userId","viewerId"}, new Object[] { comId, userId,viewerId });
	}

	/**
	 * 分享列表
	 *
	 * @param daily
	 * @param userInfo
	 * @param
	 * @return
	 * @throws ParseException
	 */
	public PageBean<Daily> listPagedDaily(DailyPojo daily,UserInfo userInfo) throws ParseException {
		boolean isForceInPersion = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		//部门查询
		if(null!=daily.getDepId()){
			//查询本部门和下级部门信息
			List<Department> listTreeDeps = departmentService.listTreeSonDep(daily.getDepId(), ConstantInterface.SYS_ENABLED_YES, userInfo.getComId());
			//将数据整理成查询条件
			List<Integer> depIds = new ArrayList<Integer>();
			//有查询结果需要遍历
			if(null != listTreeDeps && !listTreeDeps.isEmpty()){
				//遍历部门主键信息
				for (Department department : listTreeDeps) {
					depIds.add(department.getId());
				}
				//数据条件存入
				daily.setListTreeDeps(depIds.toArray(new Integer[]{}));
			}
		}
		PageBean<Daily> pageBean = new PageBean<Daily>();
		//分享列表数据查询
		List<Daily> recordList = dailyDao.listPagedDaily(daily,userInfo,isForceInPersion);
		pageBean.setRecordList(recordList);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 获取个人权限下的所有分享（不分页）
	 * @param daily
	 * @param userInfo
	 * @param isForceInPersion
	 * @return
	 * @throws ParseException
	 */
	public List<Daily> listDailyOfAll(DailyPojo daily,
												UserInfo userInfo, boolean isForceInPersion) throws ParseException {
		List<Daily> list = dailyDao.listDailyOfAll(daily,
				userInfo,isForceInPersion);
		return list;
	}
	/**
	 * 获取团队分享主键集合
	 * @param userInfo
	 * @return
	 * @throws ParseException
	 */
	public List<Daily> listDailyOfAll(UserInfo userInfo) throws ParseException {
		List<Daily> list = dailyDao.listDailyOfAll(userInfo);
		return list;
	}
	/**
	 * 获取本日的满足查询条件的数目
	 * @param weekDoneState 0本日已汇报 1本日未汇报(不能为空)
	 * @param userInfo 当前操作人员
	 * @param isForceInPersion 是否强制参与人
	 * @return
	 * @throws ParseException
	 */
//	public Integer getDailyCount(String weekDoneState,UserInfo userInfo,
//									  boolean isForceInPersion) throws ParseException {
//		Integer count = dailyDao.getDailyCount(weekDoneState,
//				userInfo,isForceInPersion);
//		return count;
//	}

	/**
	 * 取得所选分享
	 *
	 * @param id
	 * @param userInfo
	 * @param forceInPersion
	 * @return
	 * @throws ParseException
	 */
	public Daily getDailyForView(Integer id, UserInfo userInfo,DailyPojo dailyParam, boolean forceInPersion) throws ParseException {
		Daily daily = dailyDao.getDailyForView(id, userInfo,dailyParam,forceInPersion);
		if (null != daily) {
			List<DailyTalk> listWeekTalk =this.listPagedDailyTalk(id, userInfo.getComId(), "app");
			daily.setListWeekTalk(listWeekTalk);
			
			//查询显示留言附件总数
			Integer talkNum = dailyDao.countTalk(id, userInfo.getComId());
			Integer fileNum = dailyDao.countFile(userInfo.getComId(), id);
			daily.setFileNum(fileNum);
			daily.setTalkNum(talkNum);
			
			if ("1".equals(daily.getHasPlan())) {// 需要填写计划
				// 今日计划
				List<DailyPlan> dailyPlans = dailyDao
						.listDailyPlan(daily.getId(), userInfo
								.getComId());
				if ((null != dailyPlans && dailyPlans.size() > 0)
						|| daily.getCountVal() > 0) {// 要么填写过计划，要么填写过内容
					daily.setDailyPlans(dailyPlans);
					// 分享内容
					daily.setDailyQs(dailyDao.listDailyQ(daily.getId(), userInfo.getComId(), daily
									.getReporterId()));
				}
			} else if ("0".equals(daily.getHasPlan())
					&& daily.getCountVal() > 0) {// 不需要填写计划，填写过内容

				// 分享内容
				daily.setDailyQs(dailyDao.listDailyQ(
						daily.getId(), userInfo.getComId(), daily
								.getReporterId()));

			}
		}
		return daily;
	}

	/**
	 * 分享日志
	 *
	 * @param comId
	 * @param dailyId
	 * @return
	 */
	public List<DailyLog> listPagedDailyVoteLog(Integer comId,
													Integer dailyId, String sid) {
		// 分享日志
		List<DailyLog> logs = dailyDao.listPagedVoteLog(dailyId,
				comId,sid);
		return logs;
	}

	/**
	 * 反馈留言
	 *
	 * @param dailyId
	 * @param comId
	 * @return
	 */
	public List<DailyTalk> listPagedDailyTalk(Integer dailyId,Integer comId, String dest) {
		// 反馈留言
		List<DailyTalk> list = dailyDao.listPagedDailyTalk(
				dailyId, comId);
		List<DailyTalk> dailyTalks = new ArrayList<DailyTalk>();
		for (DailyTalk dailyTalk : list) {
			if(!"app".equals(dest)){//非手机端字符转换
				//回复内容转换
				dailyTalk.setContent(StringUtil.toHtml(dailyTalk.getContent()));
			}
			//讨论的附件
			dailyTalk.setListDailyTalkFile(dailyDao.listDailyTalkFile(comId,dailyId,dailyTalk.getId()));
			dailyTalks.add(dailyTalk);
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
	 * @param dailyTalk
	 * @return
	 * @throws Exception
	 */
	public Integer addDailyTalk(DailyTalk dailyTalk,UserInfo userInfo) throws Exception {
		Integer id = dailyDao.add(dailyTalk);
		//讨论附件
		Integer[] upfilesId = dailyTalk.getUpfilesId();
		if(null!=upfilesId && upfilesId.length>0){
			for (Integer upfileId : upfilesId) {
				DailyTalkFile dailyTalkFile = new DailyTalkFile();
				//企业编号
				dailyTalkFile.setComId(dailyTalk.getComId());
				//分享主键
				dailyTalkFile.setDailyId(dailyTalk.getDailyId());
				//讨论的主键
				dailyTalkFile.setTalkId(id);
				//附件主键
				dailyTalkFile.setUpfileId(upfileId);
				//上传人
				dailyTalkFile.setUserId(dailyTalk.getTalker());

				dailyDao.add(dailyTalkFile);
				//为反馈留言附件创建索引
				uploadService.updateUpfileIndex(upfileId, userInfo, "add",dailyTalk.getDailyId(),ConstantInterface.TYPE_DAILY);
			}
		}

//		分享的查看人
		List<UserInfo> shares = this.listDailyViewers(userInfo.getComId(), userInfo.getId(), dailyTalk.getDailyId());
//		添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, dailyTalk.getDailyId(),"参与分享评论:"+dailyTalk.getContent(), ConstantInterface.TYPE_DAILY,shares,null);


		//添加信息分享人员
		List<DailyShareUser> listDailyShareUser = dailyTalk.getListDailyShareUser();
		Set<Integer> pushUserIdSet = new HashSet<Integer>();
		if(null != listDailyShareUser && !listDailyShareUser.isEmpty()){
			for (DailyShareUser dailyShareUser : listDailyShareUser) {
				//人员主键
				Integer userId = dailyShareUser.getUserId();
				pushUserIdSet.add(userId);
				//删除上次的分享人员
				msgShareDao.delByField("dailyShareUser", new String[]{"comId","dailyId","userId"},
						new Object[]{userInfo.getComId(),dailyTalk.getDailyId(),userId});
				dailyShareUser.setDailyId(dailyTalk.getDailyId());
				dailyShareUser.setComId(userInfo.getComId());
				msgShareDao.add(dailyShareUser);
			}
		}

		//分享信息查看
		List<UserInfo> dailyShares = new ArrayList<UserInfo>();
		Daily daily = (Daily) msgShareDao.objectQuery(Daily.class, dailyTalk.getDailyId());
		if (null != daily) {
			//查询消息的推送人员
            dailyShares = msgShareDao.listPushTodoUserForDaily(dailyTalk.getDailyId(), userInfo.getComId(),pushUserIdSet);
			//本来是为了区分@和推送，所以在@的时候需要去除掉已经推送的人。
			// 但是推送人实现比较复杂，而且实现区分后与分享模块的方式不同，所以暂时放弃
//            for(int i=0;i<weekShares.size();i++){
//            	for(int j=0;j<shares.size();j++){
//            		if(weekShares.get(i).getId().equals(shares.get(j).getId())){
//            			weekShares.remove(weekShares.get(i));
//					}
//				}
//			}
			Iterator<UserInfo> userids =  dailyShares.iterator();
			for(;userids.hasNext();){
				UserInfo user = userids.next();
				if(user.getId().equals(userInfo.getId())){
					userids.remove();
                    dailyShares.remove(user);
				}
				//设置全部普通消息
				updateTodayWorksBusSpecTo0(ConstantInterface.TYPE_DAILY, dailyTalk.getDailyId(), user.getId());
			}
			// 添加待办提醒通知
			todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_DAILY, dailyTalk.getDailyId(), "参与分享讨论:" + dailyTalk.getContent(),
                    dailyShares, userInfo.getId(), 1);

			//清除上次查看记录
			viewRecordService.delViewRecord(userInfo,dailyShares,dailyTalk.getDailyId(),ConstantInterface.TYPE_DAILY);
		}

		//更新分享索引
//		this.updateDailyIndex(dailyTalk.getDailyId(),userInfo,"update");

		//修改积分
		jiFenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_DAILYTALK,
				"参与分享评论",dailyTalk.getDailyId());
		return id;
	}

	/**
	 * 模块日志添加
	 *
	 * @param comId
	 * @param dailyId
	 * @param userId
	 * @param userName
	 * @param content
	 */
	public void addDailyLog(Integer comId, Integer dailyId,
							  Integer userId, String userName, String content) {
		DailyLog dailyLog = new DailyLog(comId, dailyId, userId,
				content, userName);
		dailyDao.add(dailyLog);
	}

	/**
	 * 反馈留言
	 *
	 * @param id
	 * @param comId
	 * @return
	 */
	public DailyTalk getDailyTalk(Integer id, Integer comId) {
		DailyTalk dailyTalk = dailyDao.getDailyTalk(id, comId);
		if(null!=dailyTalk){
			//回复内容转换
			String content = StringUtil.toHtml(dailyTalk.getContent());
			//讨论的附件
			dailyTalk.setListDailyTalkFile(dailyDao.listDailyTalkFile(comId,dailyTalk.getDailyId(),dailyTalk.getId()));
			dailyTalk.setContent(content);
		}
		return dailyTalk;
	}

	/**
	 * 删除反馈留言回复
	 *
	 * @param dailyTalk
	 * @param delChildNode
	 * @return
	 * @throws Exception
	 */
	public List<Integer> delDailyTalk(DailyTalk dailyTalk,
										String delChildNode,UserInfo userInfo) throws Exception {
		List<Integer> childIds = new ArrayList<Integer>();
		// 删除自己
		if (null == delChildNode) {
			childIds.add(dailyTalk.getId());
			//讨论的附件
			List<DailyTalkFile> dailyTalkFiles = dailyDao.listDailyTalkFile(dailyTalk.getComId(),
					dailyTalk.getDailyId(), dailyTalk.getId());
			for (DailyTalkFile dailyTalkFile : dailyTalkFiles) {
				//文件详情
				FileDetail fileDetail = new FileDetail();
				//企业编号
				fileDetail.setComId(dailyTalk.getComId());
				//所属模块
//				fileDetail.setModuleType(ConstantInterface.TYPE_DAILYTALK);
				//文件主键
				fileDetail.setFileId(dailyTalkFile.getUpfileId());
				fileCenterService.delFile(fileDetail,userInfo);
				//删除附件讨论
				this.delDailyFileTalk(dailyTalk.getComId(), dailyTalkFile.getDailyId(), dailyTalkFile.getUpfileId());
			}
			//删除附件
			dailyDao.delByField("dailyTalkFile", new String[]{"comId","talkId"},
					new Object[]{dailyTalk.getComId(),dailyTalk.getId()});

			dailyDao.delById(DailyTalk.class, dailyTalk.getId());
		} else if ("yes".equals(delChildNode)) {// 删除自己和所有的子节点
			//待删除的讨论
			List<DailyTalk> listDailyTalk = dailyDao.listDailyTalkForDel(dailyTalk.getComId(), dailyTalk.getId());
			for (DailyTalk talk : listDailyTalk) {
				childIds.add(talk.getId());
				//讨论的附件
				List<DailyTalkFile> dailyTalkFiles = dailyDao.listDailyTalkFile(talk.getComId(),
						talk.getDailyId(), talk.getId());
				for (DailyTalkFile dailyTalkFile : dailyTalkFiles) {
					//文件详情
					FileDetail fileDetail = new FileDetail();
					//企业编号
					fileDetail.setComId(dailyTalk.getComId());
					//所属模块
//					fileDetail.setModuleType(ConstantInterface.TYPE_DAILYTALK);
					//文件主键
					fileDetail.setFileId(dailyTalkFile.getUpfileId());
					fileCenterService.delFile(fileDetail,userInfo);
					//删除附件讨论
					this.delDailyFileTalk(dailyTalk.getComId(), dailyTalkFile.getDailyId(), dailyTalkFile.getUpfileId());
				}

				//删除附件
				dailyDao.delByField("dailyTalkFile", new String[]{"comId","talkId"},
						new Object[]{talk.getComId(),talk.getId()});
			}
			//删除当前节点及其子节点回复
			dailyDao.delDailyTalk(dailyTalk.getId(),dailyTalk.getComId());
		} else if ("no".equals(delChildNode)) {// 删除自己,将子节点提高一级
			childIds.add(dailyTalk.getId());
			//讨论的附件
			List<DailyTalkFile> dailyTalkFiles = dailyDao.listDailyTalkFile(dailyTalk.getComId(),
					dailyTalk.getDailyId(), dailyTalk.getId());
			for (DailyTalkFile dailyTalkFile : dailyTalkFiles) {
				//文件详情
				FileDetail fileDetail = new FileDetail();
				//企业编号
				fileDetail.setComId(dailyTalkFile.getComId());
				//所属模块
//				fileDetail.setModuleType(ConstantInterface.TYPE_DAILYTALK);
				//文件主键
				fileDetail.setFileId(dailyTalkFile.getUpfileId());
				fileCenterService.delFile(fileDetail,userInfo);
				//删除附件讨论
				this.delDailyFileTalk(dailyTalk.getComId(), dailyTalkFile.getDailyId(), dailyTalkFile.getUpfileId());
			}

			//删除附件
			dailyDao.delByField("dailyTalkFile", new String[]{"comId","talkId"},
					new Object[]{dailyTalk.getComId(),dailyTalk.getId()});

			dailyDao.updateDailyTalkParentId(dailyTalk.getId(),
					dailyTalk.getComId());
			dailyDao.delById(DailyTalk.class, dailyTalk.getId());
		}

		//当前分享
		Daily daily = (Daily) dailyDao.objectQuery(Daily.class, dailyTalk.getDailyId());
		//当前分享的汇报人
		Integer reporterId = daily.getReporterId();

		//当前分享的汇报人设置的查看范围（没有上级）
		List<UserInfo> shares = dailyDao.listDailyViewUser(userInfo.getComId(),daily.getId(),userInfo.getId());
		if(!reporterId.equals(userInfo.getId())){//当前操作人员不是分享汇报人
			UserInfo reporter = dailyDao.getDailySelf(userInfo.getComId(),reporterId);
			shares.add(reporter);
		}
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo,null, dailyTalk.getDailyId(),"删除分享讨评论", ConstantInterface.TYPE_DAILY,shares,null);

		//更新分享索引
//		this.updateDailyIndex(dailyTalk.getDailyId(),userInfo,"update");
		//修改积分
		jiFenService.addJifen(userInfo.getComId(), userInfo.getId(),ConstantInterface.TYPE_TALKDEL,
				"删除分享讨评论",dailyTalk.getDailyId());
		return childIds;
	}

	/**
	 * 分享附件
	 *
	 * @param comId
	 * @param dailyId
	 * @return
	 */
	public List<DailyUpfiles> listPagedDailyFiles(Integer comId,Integer dailyId) {
		// 分享附件
		List<DailyUpfiles> list = dailyDao.listPagedDailyFiles(comId,dailyId);
		return list;
	}


	/**
	 * 当前时间所在日的分享
	 *
	 * @param comId
	 * @param userId
	 * @return
	 */
	public Daily getDaily(Integer comId,Integer userId,String chooseDate) {
		// 分享
		Daily daily = dailyDao.getDaily(comId, userId, chooseDate);
		return daily;
	}

	/**
	 * 分享查看权限验证
	 * @param comId
	 * @param userId 查看人
	 * @param reporterId 汇报人
	 * @return
	 */
	public boolean authorCheck(Integer comId, Integer userId, Integer reporterId,Integer busId) {
		List<DailyViewer> listDaily = dailyDao.authorCheck(comId,userId,reporterId,busId);
		if(null!=listDaily && !listDaily.isEmpty()){
			for (DailyViewer dailyViewer : listDaily) {
				if(dailyViewer.getViewerId().equals(userId)) {
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}

//	/**
//	 * 取得所选分享
//	 * @param year 年
//	 * @param weekNum 日数
//	 * @param userInfo 汇报人
//	 * @return
//	 */
//	public Daily getDaily(String year, Integer weekNum,UserInfo userInfo) {
//
//		// 取得模板的信息
//		DailyMod dailyMod = dailyDao.getDailyMod(userInfo.getComId());
//		// 初始化模板条目
//		List<DailyModContent> contentTeamLevs = dailyDao.initDailyContent(userInfo.getComId(), userInfo.getId(),userInfo.getDepId());
//
//		// 分享
//		Daily daily = new Daily();
//		// 企业
//		daily.setComId(userInfo.getComId());
//		// 汇报人
//		daily.setReporterId(userInfo.getId());
//		// 标题
//		daily.setDailyName(userInfo.getUserName() + "日总结_" + year
//				+ "年第" + weekNum + "日");
//		daily.setUserName(userInfo.getUserName());
//		// 是否需要填写今日计划
//		daily.setHasPlan(dailyMod.getHasPlan());
//		// 分享为草稿状态
//		daily.setState("1");
//		// 分享模板版本
//		daily.setVersion(dailyMod.getVersion());
//
//		// 分享条目
//		List<DailyQ> dailyQs = new ArrayList<DailyQ>();
//		// 分享的条目
//		for (DailyModContent dailyModContent : contentTeamLevs) {
//			DailyQ dailyQ = new DailyQ();
//			// 企业
//			dailyQ.setComId(userInfo.getComId());
//
//			dailyQs.add(dailyQ);
//		}
//		daily.setDailyQs(dailyQs);
//		return daily;
//	}
	/**
	 * 更新分享索引
	 * @param dailyId
	 * @param userInfo
	 * @param opType 操作类型;添加?更新?
	 * @throws Exception
	 */
	public void updateDailyIndex(Integer dailyId,UserInfo userInfo,String opType) throws Exception{
		//更新分享索引
		Daily daily = dailyDao.getDaily(dailyId,userInfo.getComId());
		if(null==daily){return;}
		//连接索引字符串
//		StringBuffer attStr = new StringBuffer(daily.getDailyName()+","+daily.getUserName()+",");
		StringBuffer attStr = new StringBuffer(daily.getDailyName());
		//获取分享计划为其创建索引
//		List<DailyPlan> listDailyPlan = dailyDao.listDailyPlan4Index(userInfo.getComId(),dailyId);
//		for(DailyPlan vo : listDailyPlan){
//			attStr.append(vo.getPlanContent()+",");
//		}
		//获取分享汇报内容为其创建索引
//		List<DailyVal> listDailyVal = dailyDao.listDailyVal4Index(userInfo.getComId(),dailyId);
//		for(DailyVal vo : listDailyVal){
//			attStr.append(vo.getDailyValue()+",");
//		}
		//获取反馈留言为其创建索引
//		List<DailyTalk> listDailyTalkVal = dailyDao.listDailyTalk4Index(userInfo.getComId(),dailyId);
//		for(DailyTalk vo : listDailyTalkVal){
//			attStr.append(vo.getContent()+","+vo.getTalkerName()+",");
//		}
		// 分享附件
//		List<DailyUpfiles> list = dailyDao.listPagedDailyFiles(userInfo.getComId(),dailyId);
//		if(null!=list){
//			Upfiles upfile = null;
//			for(DailyUpfiles vo:list){
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
		String index_key = userInfo.getComId()+"_"+ConstantInterface.TYPE_DAILY+"_"+dailyId;
		//为分享创建索引
		List<IndexDoc> listIndexDoc = CommonUtil.toIndexDoc(
				index_key,userInfo.getComId(),dailyId,ConstantInterface.TYPE_DAILY,
				daily.getDailyName(),attStr.toString(),
				DateTimeUtil.parseDate(daily.getRecordCreateTime(),0));
		if(null!=listIndexDoc){
			//根据主键跟新索引
//			pool.execute(new IndexUpdateThread(opType,indexService,userInfo,listIndexDoc,index_key));
		}else{
			throw new NullPointerException("创建索引对象不能为空！");
		}

	}



	/**
	 * 删除附件留言
	 * @param comId
	 * @param dailyId
	 * @param upfileId
	 */
	private void delDailyFileTalk(Integer comId, Integer dailyId,
								 Integer upfileId) {
		//查询该模块其他评论是否有相同的附件
//		List<DailyTalkFile> list = dailyDao.listDailySimUpfiles(comId,dailyId,upfileId);
		List<DailyTalkFile> list = null;
		//在其他地方没有使用该附件，则删除附件评论,；在其他地方有使用该附件，则不处理
		if(null!=list && list.size()==1){
			dailyDao.delByField("fileTalk", new String[]{"comId","fileId","busId","busType"},
					new Object[]{comId,upfileId,dailyId,ConstantInterface.TYPE_DAILY});
		}
	}

	/**
	 * 分享信息推送范围
	 * @param comId
	 * @param busId
	 * @return
	 */
	public List<UserInfo> listDailyViewers(Integer comId, Integer userId, Integer busId) {
		//当前分享
		Daily daily = (Daily) dailyDao.objectQuery(Daily.class, busId);
		//当前分享的汇报人
		Integer reporterId = daily.getReporterId();

		//当前分享的汇报人设置的查看范围（没有上级）
		List<UserInfo> shares = dailyDao.listDailyViewUser(comId,busId,userId);
		if(!reporterId.equals(userId)){//当前操作人员不是分享汇报人
//			UserInfo reporter = dailyDao.getDailySelf(comId,reporterId);
//			shares.add(reporter);
		}
		return shares;
	}
	/**
	 * 查询分享查看人员
	 * @param sessionUser
	 * @return
	 */
	public List<DailyViewer> listDailyViewer(UserInfo sessionUser) {
		return dailyDao.listDailyViewer(sessionUser);
	}

	/**
	 * 根据分享主键查询分享信息
	 * @param dailyId
	 * @return
	 */
	public Daily getDailyById(Integer dailyId) {
		return (Daily) dailyDao.objectQuery(Daily.class, dailyId);
	}

	/**
	 * 分享查看权限验证
	 * @param userInfo 当前操作人
	 * @param reporterId 分享主键
	 * @param clockId
	 * @return
	 */
	public boolean authorCheck(UserInfo userInfo,Integer reporterId,Integer clockId,Integer busId){
		//验证当前登录人是否是督察人员
		boolean isForceIn = forceInService.isForceInPersion(userInfo, ConstantInterface.TYPE_DAILY);
		if(isForceIn){
			return true;
		}else{
			List<DailyViewer> listDaily = dailyDao.authorCheck(userInfo.getComId(),userInfo.getId(),reporterId,busId);
			if(null!=listDaily && !listDaily.isEmpty()){
				return true;
			}else{
				//查看验证，删除消息提醒
				todayWorksService.updateTodoWorkRead(reporterId,userInfo.getComId(),
						userInfo.getId(), ConstantInterface.TYPE_DAILY,clockId);
				return false;
			}
		}
	}

	/**
	 * 分享发布情况统计
	 * @param daily
	 * @param userInfo
	 * @param isForceIn
	 * @return
	 * @throws ParseException
	 */
	public List<Daily> listDailyStatistics(DailyPojo daily, UserInfo userInfo, boolean isForceIn) throws ParseException {


		List<Department> listDep = daily.getListDep();
		//部门查询
		if(null!=listDep && !listDep.isEmpty()){
			//将数据整理成查询条件
			List<Integer> depIds = new ArrayList<Integer>();
			for (Department department : listDep) {
				//查询本部门和下级部门信息
				List<Department> listTreeDeps = departmentService.listTreeSonDep(department.getId(), ConstantInterface.SYS_ENABLED_YES, userInfo.getComId());
				//有查询结果需要遍历
				if(null != listTreeDeps && !listTreeDeps.isEmpty()){
					//遍历部门主键信息
					for (Department sonDepartment : listTreeDeps) {
						if(!depIds.contains(sonDepartment.getId())){
							depIds.add(sonDepartment.getId());
						}
					}
				}
			}
			//数据条件存入
			daily.setListTreeDeps(depIds.toArray(new Integer[]{}));
		}

		//本日所在日数
		Integer weekNum = daily.getDailyNum();
		//日数所在年
//		Integer weekYear = daily.getDailyYear();
		Integer weekYear = null;
		if(null != weekYear || null != weekNum){//按照年份和日数选择的话就没有时间区间信息
			daily.setStartDate("");
			daily.setEndDate("");
		}
		List<Daily> dailys =  dailyDao.listDailyStatistics(daily, userInfo, isForceIn);
		return dailys;
	}
	/**
	 * 查询企业提交分享时间设置
	 * @param userInfo
	 * @return
	 */
	public SubTimeSet querySubTimeSet(UserInfo userInfo) {
		return dailyDao.querySubTimeSet(userInfo);
	}
	/**
	 * 修改设置
	 * @param subTimeSet
	 */
	public void updateSubTimeSet(SubTimeSet subTimeSet) {
		Integer updateNum = dailyDao.excuteSql("update subTimeSet set  timeType=? where comid=?",new Object[] { subTimeSet.getTimeType(),subTimeSet.getComId()});
		if(updateNum <=0){
			dailyDao.add(subTimeSet);
		}
	}


	/**
	 * 初始化添加分享信息
	 * @param msgShares 分享信息
	 * @param user_date_dailyIdMap 人员日期对应分享主键
	 * @param daily_QIdMap 分享主键对应问题主键
	 * @param dailyId_contentMap 分享主键对应内容主键
	 */
	public void addDailyReportbyInit(List<MsgShare> msgShares,
			Map<String, Integer> user_date_dailyIdMap,
			Map<Integer, Integer> daily_QIdMap, Map<Integer, DailyVal> dailyId_contentMap) {
		if(null!=msgShares && !msgShares.isEmpty() ){
			for (MsgShare msgShare : msgShares) {
				//分享日期
				String dailyDate = msgShare.getRecordCreateTime().substring(0,10);
				//人员日期
				String key = msgShare.getCreator()+"_"+dailyDate;
				//默认分享主键
				Integer dailyId = user_date_dailyIdMap.get(key);
				if( null == dailyId){//没有分享主键，则初始化一条分享信息
					dailyId = this.initAddDaily(msgShare);
					//存放人员日期对应的分享
					user_date_dailyIdMap.put(key, dailyId);
				}
				//默认分享问题主键
				Integer dailyQId  = daily_QIdMap.get(dailyId);
				if(null == dailyQId){
					//初始化一天分享问题
					dailyQId = this.initAddDailyQ(msgShare,dailyId);
					//存放分享问题信息
					daily_QIdMap.put(dailyId, dailyQId);
				}

				//处理条目值
				this.initAddDailyVal(msgShare,dailyId,dailyQId,dailyId_contentMap);

				//转移分享留言
				List<MsgShareTalk> msgShareTalks = msgShareService.listMsgShareTalk(msgShare.getId(), msgShare.getComId());
				this.addDailyTalk(msgShareTalks,dailyId);
				
				//转移分享日志
				List<MsgShareLog> msgShareLogs = msgShareService.listMsgShareLogOfAll(msgShare.getId(), msgShare.getComId());
				this.addDailyLog(msgShareLogs,dailyId);

				//转移分享查看人员
				List<ShareUser> shareList = msgShareService.listAllShareUser(msgShare.getId(),msgShare.getComId());
				this.addDailyViewer(shareList,dailyId,msgShare.getComId());

				//当分享范围为自定义时去查询分享组
				if(msgShare.getScopeType() != 2 && msgShare.getScopeType() != 0){
					//转移分享分享组
					List<ShareGroup> groupList = msgShareService.listShareGroup(msgShare.getId(),msgShare.getComId());
					this.addDailyGroup(groupList,dailyId,msgShare.getComId());
				}
			}
		}
		
	}

	/**
	 * 添加分享分享组
	 * @param groupList 分享组集合
	 * @param dailyId 分享主键
	 * @param comId 所在团队
	 */
	private void addDailyGroup(List<ShareGroup> groupList, Integer dailyId, Integer comId) {
		if(null != groupList && !groupList.isEmpty()){
			for(ShareGroup group : groupList){
				dailyDao.delByField("dailyShareGroup",new String[]{"comid","dailyId","grpId"},new Object[]{comId,dailyId,group.getGrpId()});
				DailyShareGroup dailyShareGroup = new DailyShareGroup();
				dailyShareGroup.setDailyId(dailyId);
				dailyShareGroup.setGrpId(group.getGrpId());
				dailyShareGroup.setComId(group.getComId());
				dailyDao.add(dailyShareGroup);
			}
		}
	}

	/**
	 * 添加分享查看人员
	 * @param shareList 查看人员集合
	 * @param dailyId 分享主键
	 * @param comId 团队号
	 */
	private void addDailyViewer(List<ShareUser> shareList, Integer dailyId,Integer comId) {
		if(null != shareList && !shareList.isEmpty()){
			for(ShareUser shareUser : shareList){
				dailyDao.delByField("dailyShareUser",new String[]{"comid","dailyId","userId"},new Object[]{comId,dailyId,shareUser.getUserId()});
				DailyShareUser dailyShareUser = new DailyShareUser();
				dailyShareUser.setComId(comId);
				dailyShareUser.setUserId(shareUser.getUserId());
				dailyShareUser.setDailyId(dailyId);
				dailyDao.add(dailyShareUser);
			}
		}
	}

	/**
	 * 添加分享日志
	 * @param msgShareLogs 日志信息
	 * @param dailyId 分享主键
	 */
	private void addDailyLog(List<MsgShareLog> msgShareLogs, Integer dailyId) {
		for(MsgShareLog msgShareLog : msgShareLogs){
			DailyLog dailyLog = new DailyLog();
			dailyLog.setDailyId(dailyId);
			dailyLog.setComId(msgShareLog.getComId());
			dailyLog.setContent(msgShareLog.getContent());
			dailyLog.setRecordCreateTime(msgShareLog.getRecordCreateTime());
			dailyLog.setUserId(msgShareLog.getUserId());
			dailyLog.setUserName(msgShareLog.getUserName());
			Integer id = dailyDao.add(dailyLog);
			dailyLog.setId(id);
			dailyDao.update("update dailyLog a set a.recordCreateTime=:recordCreateTime where a.id=:id",dailyLog);
		}
	}


	/**
	 * 添加留言信息
	 * @param msgShareTalks
	 * @param dailyId
	 */
	private void addDailyTalk(List<MsgShareTalk> msgShareTalks, Integer dailyId) {
		if(null!=msgShareTalks && !msgShareTalks.isEmpty()){
			Map<Integer, Integer> talkPIdMap = new HashMap<Integer, Integer>();
			for (MsgShareTalk msgShareTalk : msgShareTalks) {
				Integer preTalkId = msgShareTalk.getId();
				Integer prePTalkId = msgShareTalk.getParentId();
				Integer parentId = talkPIdMap.get(prePTalkId);
				if(null==parentId){
					parentId = -1;
				}
				DailyTalk dailyTalk = new DailyTalk();
				dailyTalk.setParentId(parentId);
				dailyTalk.setComId(msgShareTalk.getComId());
				dailyTalk.setDailyId(dailyId);
				dailyTalk.setContent(msgShareTalk.getContent());
				dailyTalk.setTalker(msgShareTalk.getSpeaker());
				dailyTalk.setPtalker(msgShareTalk.getPtalker());
				
				Integer dailyTalkId = dailyDao.add(dailyTalk);
				dailyTalk.setId(dailyTalkId);
				dailyTalk.setRecordCreateTime(msgShareTalk.getRecordCreateTime());
				dailyDao.update("update dailyTalk a set a.recordCreateTime=:recordCreateTime where a.id=:id",dailyTalk);
				talkPIdMap.put(preTalkId, dailyTalkId);
				
				//添加留言附件信息
				List<MsgShareTalkUpfile> msgShareTalkUpfiles = msgShareTalk.getMsgShareTalkUpfiles();
				if(null!=msgShareTalkUpfiles && !msgShareTalkUpfiles.isEmpty()){
					for (MsgShareTalkUpfile msgShareTalkUpfile : msgShareTalkUpfiles) {
						DailyTalkFile dailyTalkFile = new DailyTalkFile();
						dailyTalkFile.setComId(msgShareTalk.getComId());
						dailyTalkFile.setDailyId(dailyId);
						dailyTalkFile.setTalkId(dailyTalkId);
						dailyTalkFile.setUpfileId(msgShareTalkUpfile.getUpfileId());
						dailyTalkFile.setUserId(msgShareTalkUpfile.getUserId());
						Integer dailyTalkFileId = dailyDao.add(dailyTalkFile);
						
						dailyTalkFile.setId(dailyTalkFileId);
						dailyTalkFile.setRecordCreateTime(msgShareTalkUpfile.getRecordCreateTime());
						dailyDao.update("update dailyTalkFile a set a.recordCreateTime=:recordCreateTime where a.id=:id",dailyTalkFile);
					}
				}
				
			}
		}
		
	}

	/**
	 * 添加分享内容
	 * @param msgShare 分享信息
	 * @param dailyId 分享主键
	 * @param dailyQId 分享问题主键
	 * @param dailyId_contentMap 分享内容缓存数据
	 */
	private void initAddDailyVal(MsgShare msgShare, Integer dailyId, 
			Integer dailyQId, Map<Integer, DailyVal> dailyId_contentMap) {
		DailyVal dailyVal = dailyId_contentMap.get(dailyId);
		if(!dailyId_contentMap.containsKey(dailyId) || null == dailyVal){//需要添加
			dailyVal = new DailyVal();
			// 企业
			dailyVal.setComId(msgShare.getComId());
			dailyVal.setQuestionId(dailyQId);
			dailyVal.setDailyId(dailyId);
			dailyVal.setDailyValue(msgShare.getContent());
			Integer id = dailyDao.add(dailyVal);
			dailyVal.setId(id);
			dailyId_contentMap.put(dailyId, dailyVal);
		}else{
			dailyVal.setDailyValue(dailyVal.getDailyValue() + "<br/>" + "<br/>" + msgShare.getContent());
			dailyDao.update(dailyVal);
			dailyId_contentMap.put(dailyId, dailyVal);
		}
	}

	/**
	 * 添加日报栏目问题信息
	 * @param msgShare
	 * @param dailyId
	 * @return
	 */
	private Integer initAddDailyQ(MsgShare msgShare, Integer dailyId) {
		DailyQ dailyQ = new DailyQ();
		// 企业
		dailyQ.setComId(msgShare.getComId());
		// 分享要求
		dailyQ.setDailyName("分享内容");
		// 分享主键
		dailyQ.setDailyId(dailyId);
		//分享必填
		dailyQ.setIsRequire("1");
		//是系统条目
		dailyQ.setSysState(1);

		Integer dailyQId = dailyDao.add(dailyQ);
		dailyQ.setId(dailyQId);
		
		return dailyQId;
	}

	/**
	 * 添加日版信息
	 * @param msgShare
	 * @return
	 */
	private Integer initAddDaily(MsgShare msgShare) {
		Daily daily = new Daily();
		daily.setComId(msgShare.getComId());
		daily.setReporterId(msgShare.getCreator());
		String dailyDate = msgShare.getRecordCreateTime().substring(0,10);
		String dailyName = msgShare.getCreatorName()+" "+dailyDate+" 分享";
		daily.setDailyName(dailyName);
		daily.setHasPlan("0");
		daily.setState("0");
		daily.setDailyDate(dailyDate);
		daily.setVersion(1);
		daily.setScopeType(msgShare.getScopeType());
		
		Integer dailyId = dailyDao.add(daily);
		daily.setId(dailyId);
		daily.setRecordCreateTime(msgShare.getRecordCreateTime());
		dailyDao.update("update Daily a set a.recordCreateTime=:recordCreateTime where a.id=:id",daily);
		return dailyId;
	}

	/**
	 * 删除分享附件及留言附件
	 * @param dailyUpFileId
	 * @param type
	 * @param userInfo
	 * @param dailyId
	 */
	public void delDailyUpfile(Integer dailyUpFileId, String type, UserInfo userInfo, Integer dailyId) {
		if(type.equals("daily")){
			DailyUpfiles dailyUpfiles = (DailyUpfiles) dailyDao.objectQuery(DailyUpfiles.class, dailyUpFileId);
			dailyDao.delById(DailyUpfiles.class, dailyUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) dailyDao.objectQuery(Upfiles.class, dailyUpfiles.getUpfileId());
			this.addDailyLog(userInfo.getComId(),dailyId,userInfo.getId(),userInfo.getUserName(),"删除了分享附件："+upfiles.getFilename());
		}else{
			DailyTalkFile dailyTalkFile = (DailyTalkFile) dailyDao.objectQuery(DailyTalkFile.class, dailyUpFileId);
			dailyDao.delById(DailyTalkFile.class, dailyUpFileId);
			//模块日志添加
			Upfiles upfiles = (Upfiles) dailyDao.objectQuery(Upfiles.class, dailyTalkFile.getUpfileId());
			this.addDailyLog(userInfo.getComId(),dailyId,userInfo.getId(),userInfo.getUserName(),"删除了分享留言附件："+upfiles.getFilename());
		}
		
	}


	/**
	 * 分页查询日报用于分享
	 * @param orgNum 团队号
	 * @return
	 */
	public List<Daily> listPagedDailyForMsg(Integer orgNum) {
		return dailyDao.listPagedDailyForMsg(orgNum);
	}


	/**
	 * 查询分享组信息
	 * @param dailyId
	 * @return
	 */
	public List<DailyShareGroup> listDailyShareGroup(Integer dailyId) {
		return dailyDao.listDailyShareGroup(dailyId);
	}


	/**
	 * 通过分享添加日报信息
	 * @param msgShare
	 * @param userInfo
	 * @return
	 */
	public Integer addDailyByMsgShare(MsgShare msgShare, UserInfo userInfo) {
		 String content = msgShare.getContent();
		 //添加日报信息
		 Daily dailyObj = this.initOrUpdateDaily(userInfo,content,msgShare.getScopeType());
		 //删除分享组
		 dailyDao.delByField("dailyShareGroup","dailyId",dailyObj.getId());
		// 配置信息分享范围
		List<ShareGroup> listShareGroup = msgShare.getShareGroup();
		if (null != listShareGroup && !listShareGroup.isEmpty()) {
			for (ShareGroup shareGroup : listShareGroup) {
				DailyShareGroup dailyShareGroup = new DailyShareGroup();
				dailyShareGroup.setComId(userInfo.getComId());
				dailyShareGroup.setDailyId(dailyObj.getId());
				dailyShareGroup.setGrpId(shareGroup.getGrpId());
				// 添加分享范围
				dailyDao.add(dailyShareGroup);
			}
		}
		
		//信息分享组群人
		List<UserInfo> shares = new ArrayList<UserInfo>();
		if(msgShare.getScopeType()==0){//所有人
			shares = userInfoService.listUser(userInfo.getComId());
		}else if(msgShare.getScopeType()==1){//自定义
			//配置信息分享范围
			String grpIds = "";
			if(null!=listShareGroup && listShareGroup.size()>0){
				for (ShareGroup shareGroup : listShareGroup) {
					grpIds +=shareGroup.getGrpId()+",";
				}
			}
			grpIds +="-1";
			//信息分享组群人
			shares = msgShareService.listScopeUser(grpIds, userInfo.getComId());
		}
		
		//删除所有待办
		todayWorksService.delTodoWork(dailyObj.getId(), userInfo.getComId(),ConstantInterface.TYPE_DAILY,"1");
		//添加待办提醒通知
		todayWorksService.addTodayWorks(userInfo.getComId(), ConstantInterface.TYPE_DAILY, dailyObj.getId(), 
				content, shares, userInfo.getId(), 1);
		
		msgShare.setModId(dailyObj.getId());
		msgShare.setType(ConstantInterface.TYPE_DAILY);
		msgShare.setAction("post");
		Integer msgId  = msgShareService.addMsgShare(msgShare, userInfo);
		
			
		return msgId;
	}


	/**
	 * 初始化日报信息
	 * @param userInfo
	 * @param content
	 * @param scopeType 
	 * @return
	 */
	private Daily initOrUpdateDaily(UserInfo userInfo, String content, Integer scopeType) {
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		DailyMod dailyMod = dailyDao.getDailyModMod(userInfo.getComId());
		
		Daily daily = dailyDao.getDaily(userInfo.getComId(), userInfo.getId(), nowDate);
		if(null == daily){
			daily = new Daily();
			daily.setComId(userInfo.getComId());
			daily.setReporterId(userInfo.getId());
			
			String dailyName = userInfo.getUserName()+" "+nowDate+" 分享";
			daily.setDailyName(dailyName);
			
			daily.setHasPlan(dailyMod.getHasPlan());
			daily.setState("0");
			
			daily.setDailyDate(nowDate);
			daily.setVersion(dailyMod.getVersion());
			daily.setScopeType(scopeType);
			
			Integer dailyId = dailyDao.add(daily);
			daily.setId(dailyId);
			
			List<DailyModContent> contentTeamLevs = dailyDao.queryDailySysContent(userInfo.getComId());
			if(null!=contentTeamLevs && !contentTeamLevs.isEmpty()){
				// 分享的条目
				for (DailyModContent dailyModContent : contentTeamLevs) {
					DailyQ dailyQ = new DailyQ();
					// 企业
					dailyQ.setComId(userInfo.getComId());
					// 分享要求
					dailyQ.setDailyName(dailyModContent.getModContent());
					// 分享主键
					dailyQ.setDailyId(dailyId);
					dailyQ.setIsRequire(dailyModContent.getIsRequire());
					
					dailyQ.setSysState(dailyModContent.getSysState());

					Integer questionId = dailyDao.add(dailyQ);
					
					DailyVal dailyVal = new DailyVal();
					dailyVal.setComId(userInfo.getComId());
					dailyVal.setQuestionId(questionId);
					dailyVal.setDailyId(dailyId);
					dailyVal.setDailyValue(content);
					dailyDao.add(dailyVal);
					
					daily.setWeeklyDone(content);
				}
			}
			return daily;
		}
		daily.setState("0");
		dailyDao.update(daily);
		
		//已有日报
		Integer dailyId = daily.getId();
		//查询是否有默认的系统条目
		List<DailyQ> listDailyQ =  dailyDao.queryDailySysQues(userInfo.getComId(),dailyId);
		if(null == listDailyQ || listDailyQ.isEmpty()){
			List<DailyModContent> contentTeamLevs = dailyDao.queryDailySysContent(userInfo.getComId());
			if(null!=contentTeamLevs && !contentTeamLevs.isEmpty()){
				// 分享的条目
				for (DailyModContent dailyModContent : contentTeamLevs) {
					DailyQ dailyQ = new DailyQ();
					// 企业
					dailyQ.setComId(userInfo.getComId());
					// 分享要求
					dailyQ.setDailyName(dailyModContent.getModContent());
					// 分享主键
					dailyQ.setDailyId(dailyId);
					dailyQ.setIsRequire(dailyModContent.getIsRequire());
					
					dailyQ.setSysState(dailyModContent.getSysState());

					Integer questionId = dailyDao.add(dailyQ);
					
					DailyVal dailyVal = new DailyVal();
					dailyVal.setComId(userInfo.getComId());
					dailyVal.setQuestionId(questionId);
					dailyVal.setDailyId(dailyId);
					dailyVal.setDailyValue(content);
					dailyDao.add(dailyVal);
					
					daily.setWeeklyDone(content);
				}
			}
			return daily;
		}
		
		//条目已建立
		DailyQ dailyQ = listDailyQ.get(0);
		Integer questionId = dailyQ.getId();
		
		DailyVal dailyVal = dailyDao.queryDailyVal(userInfo.getComId(), dailyId, questionId);
		if(null == dailyVal){
			dailyVal = new DailyVal();
			dailyVal.setComId(userInfo.getComId());
			dailyVal.setQuestionId(questionId);
			dailyVal.setDailyId(dailyId);
			dailyVal.setDailyValue(content);
			dailyDao.add(dailyVal);
			
			daily.setWeeklyDone(content);
			
			return daily;
		}
		
		String actContent = dailyVal.getDailyValue() + content;
		dailyVal.setDailyValue(actContent);
		dailyDao.update(dailyVal);
		
		daily.setWeeklyDone(actContent);
		
		return daily;
	}
	
	/**
	 * 获取日报详情
	 * @param dailyId
	 * @param comId
	 * @return
	 */
	public List<DailyUpfiles> listDailyFiles(Integer dailyId, Integer comId){
		return dailyDao.listDailyFiles(dailyId,comId);
	}
}
