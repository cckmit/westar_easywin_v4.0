package com.westar.core.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.model.AttenceConnSet;
import com.westar.base.model.AttenceDetail;
import com.westar.base.model.AttenceRecord;
import com.westar.base.model.AttenceRecordDetail;
import com.westar.base.model.AttenceRecordUpload;
import com.westar.base.model.AttenceRule;
import com.westar.base.model.AttenceState;
import com.westar.base.model.AttenceTime;
import com.westar.base.model.AttenceType;
import com.westar.base.model.AttenceUser;
import com.westar.base.model.AttenceWeek;
import com.westar.base.model.FeeBudget;
import com.westar.base.model.Leave;
import com.westar.base.model.OverTime;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.ZkemSDK;
import com.westar.core.dao.AttenceDao;
import com.westar.core.web.PaginationContext;

import flex.messaging.io.ArrayList;

/**
 * 暂时考虑一个团队只有一种考勤制度
 * @author H87
 *
 */
@Service
public class AttenceService {

	@Autowired
	AttenceDao attenceDao;
	
	@Autowired
	UserInfoService userInfoService;

	@Autowired
	FestModService festModService;
	
	@Autowired
	FinancialService financialService;
	
	@Autowired
	ForceInPersionService forceInService;
	
	private static final Logger logger = LoggerFactory.getLogger(AttenceService.class);
	/**
	 * 取得考勤规则
	 * @param comId
	 * @return
	 */
	public AttenceRule getAttenceRule(Integer id,Integer comId) {
		
		AttenceRule attenceRule = null;
		if(null==id){//没有主键则查询系统默认的
			attenceRule = attenceDao.getAttenceRule(comId,1);
			if(null == attenceRule){
				attenceRule = new AttenceRule();
				//设置团队号
				attenceRule.setComId(comId);
				//设置是系统级别的
				attenceRule.setIsSystem(1);
				//初始化系统烤考勤规则
				Integer attenceRuleId = addAttenceRule(attenceRule);
				attenceRule.setId(attenceRuleId);
			}
		}else{
			attenceRule = (AttenceRule) attenceDao.objectQuery(AttenceRule.class, id);
		}
		return attenceRule;
	}
	/**
	 * 初始化考勤制度
	 * @param attenceRule 企业号是必须的和系统标识
	 */
	public Integer addAttenceRule(AttenceRule attenceRule){
		
		Integer attenceRuleId = 0;
		//取得团队号
		Integer comId=  attenceRule.getComId();
		//是系统级别的
		Integer isSystem = attenceRule.getIsSystem();
		
		if(null!=isSystem && isSystem==1){//添加系统级别的规则
			
			/**********添加考勤制度***************/
			//默认名称
			attenceRule.setRuleName("团队考勤规则");
			//默认设置为 标准考勤制度
			attenceRule.setRuleType("1");
			
			//添加考勤制度
			attenceRuleId = attenceDao.add(attenceRule);
			
			/*********添加工作日类型****************/
			//工作日类型设定
			AttenceType attenceType = new AttenceType();
			//设置团队号
			attenceType.setComId(comId);
			//设置关联规则
			attenceType.setAttenceRuleId(attenceRuleId);
			//不考虑单双周
			attenceType.setWeekType(-1);
			//添加工作日类型
			Integer attenceTypeId = attenceDao.add(attenceType);
			
			/************初始化工作日*************/
			//初始化工作日
			for (int i = 1; i < 6; i++) {
				AttenceWeek attenceWeek = new AttenceWeek();
				//设置团队号
				attenceWeek.setComId(comId);
				//设置关联规则
				attenceWeek.setAttenceRuleId(attenceRuleId);
				//设置关联类型
				attenceWeek.setAttenceTypeId(attenceTypeId);
				//设置星期数
				attenceWeek.setWeekDay(i);
				
				attenceDao.add(attenceWeek);
			}
			
			/**********默认工作时段***************/
			//工作时段
			AttenceTime attenceTime = new AttenceTime();
			//设置团队号
			attenceTime.setComId(comId);
			//设置关联规则主键
			attenceTime.setAttenceRuleId(attenceRuleId);
			//设置关联规则类型
			attenceTime.setAttenceTypeId(attenceTypeId);
			//签到时间
			attenceTime.setDayTimeS("09:00");
			//签退时间
			attenceTime.setDayTimeE("18:00");
			
			attenceDao.add(attenceTime);
			
		}else{
			//添加考勤制度
			attenceRuleId = attenceDao.add(attenceRule);
			
			attenceRule.setId(attenceRuleId);
			//添加考勤明细
			this.addAttenceRuleDetail(attenceRule);
		}
		
		return attenceRuleId;
		
	}
	/**
	 * 修改考勤规则
	 * @param attenceRule
	 */
	public void updateAttenceRule(AttenceRule attenceRule){
		//取得团队号
		Integer comId=  attenceRule.getComId();
		//考勤规则主键
		Integer attenceRuleId = attenceRule.getId();
		
		//修改关联规则
		attenceDao.update(attenceRule);
		
		//删除旧的考勤明细
		attenceDao.delByField("attenceTime", new String[]{"comId","attenceRuleId"}, new Object[]{comId,attenceRuleId});
		attenceDao.delByField("attenceWeek", new String[]{"comId","attenceRuleId"}, new Object[]{comId,attenceRuleId});
		attenceDao.delByField("attenceType", new String[]{"comId","attenceRuleId"}, new Object[]{comId,attenceRuleId});
		
		//添加考勤明细
		this.addAttenceRuleDetail(attenceRule);
		
	}
	/**
	 * 添加考勤明细
	 * @param attenceRule
	 */
	public void  addAttenceRuleDetail(AttenceRule attenceRule){
		//取得团队号
		Integer comId=  attenceRule.getComId();
		//考勤规则主键
		Integer attenceRuleId = attenceRule.getId();
		
		//设置的工作日类型
		List<AttenceType> listAttenceTypes = attenceRule.getListAttenceTypes();
		
		if(null!=listAttenceTypes && !listAttenceTypes.isEmpty()){
			for (AttenceType attenceType : listAttenceTypes) {
				//设置团队号
				attenceType.setComId(comId);
				//关联规则主键
				attenceType.setAttenceRuleId(attenceRuleId);
				//添加工作日类型
				Integer attenceTypeId = attenceDao.add(attenceType);
				
				//工作日设定(星期)
				List<AttenceWeek> listAttenceWeeks= attenceType.getListAttenceWeeks();
				if(null!=listAttenceWeeks && !listAttenceWeeks.isEmpty()){
					for (AttenceWeek attenceWeek : listAttenceWeeks) {
						//设置团队号
						attenceWeek.setComId(comId);
						//设置关联规则主键
						attenceWeek.setAttenceRuleId(attenceRuleId);
						//设置关联类型主键
						attenceWeek.setAttenceTypeId(attenceTypeId);
						
						attenceDao.add(attenceWeek);
					}
				}
				List<AttenceTime> listAttenceTime = attenceType.getListAttenceTimes();
				if(null!=listAttenceTime && !listAttenceTime.isEmpty()){
					for (AttenceTime attenceTime : listAttenceTime) {
						//设置团队号
						attenceTime.setComId(comId);
						//设置关联规则主键
						attenceTime.setAttenceRuleId(attenceRuleId);
						//设置关联类型主键
						attenceTime.setAttenceTypeId(attenceTypeId);
						
						attenceDao.add(attenceTime);
					}
				}
			}
		}
	}
	/**
	 * 取得工作日类型集合
	 * @param comId 团队号
	 * @param attenceRuleId 考勤规则主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceType> listAttenceTypes(Integer comId,Integer attenceRuleId) {
		List<AttenceType> result = new ArrayList();
		//取得工作日类型集合
		List<AttenceType> listAttenceTypes = attenceDao.listAttenceTypes(comId,attenceRuleId);
		//已设定工作日类型
		if(null != listAttenceTypes && !listAttenceTypes.isEmpty()){
			for (AttenceType attenceType : listAttenceTypes) {
				//取得工作日类型主键
				Integer attenceTypeId = attenceType.getId();
				
				//查询工作日的星期数
				List<AttenceWeek> listAttenceWeeks = attenceDao.listAttenceWeeks(comId,attenceRuleId,attenceTypeId);
				//设置工作日的星期数
				attenceType.setListAttenceWeeks(listAttenceWeeks);
				
				//查询工作时段集合
				List<AttenceTime> listAttenceTimes = attenceDao.listAttenceTimes(comId,attenceRuleId,attenceTypeId);
				attenceType.setListAttenceTimes(listAttenceTimes);
				
				result.add(attenceType);
			}
		}
		return result;
	}
	/**
	 * 权限下请假列表
	 * @param curUser
	 * @param leave
	 * @return
	 */
	public PageBean<Leave> listPagedLeave(UserInfo curUser, Leave leave) {
		PageBean<Leave> pageBean = new PageBean<Leave>();
		//验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ATTENCE);
		List<Leave> listData = attenceDao.listPagedLeave(curUser,leave,isForceInPersion);
		pageBean.setRecordList(listData);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	/**
	 * 权限下加班列表
	 * @param curUser
	 * @param overTime
	 * @return
	 */
	public PageBean<OverTime> listPagedOverTime(UserInfo curUser, OverTime overTime) {
		PageBean<OverTime> pageBean = new PageBean<OverTime>();
		//验证当前登录人是否是督察人员
		boolean isForceInPersion = forceInService.isForceInPersion(curUser, ConstantInterface.TYPE_ATTENCE);
		List<OverTime> listData = attenceDao.listPagedOverTime(curUser,overTime,isForceInPersion);
		pageBean.setRecordList(listData);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
		return pageBean;
	}
	
	
	/***************************************考勤*****************************************/
	/**
	 * 连接考勤机
	 * @param userInfo
	 * @return
	 */
	public ZkemSDK getConnect(UserInfo userInfo){
		
		AttenceConnSet attenceConnSet = this.queryAttenceConnSet(userInfo);
		if(attenceConnSet.getType() == 1){
			ZkemSDK sdk = new ZkemSDK();
			boolean  connFlag = sdk.connect(attenceConnSet.getConnIP(),attenceConnSet.getPort());
			if(connFlag)
			{
				return sdk;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	/**
	 * 查询考勤记录
	 * @param userInfo
	 * @param attenceRecord
	 * @return
	 */
	public List<AttenceRecord> listAttenceRecord(UserInfo userInfo,AttenceRecord attenceRecord){
		return attenceDao.listAttenceRecord(userInfo,attenceRecord);
	}
	
	/**
	 * 同步考勤记录
	 * @param listLogData
	 * @param userInfo
	 */
	public void uploadAttenceRecord(List<Map<String,Object>> listLogData,UserInfo userInfo){
		//处理数据
		for(Map<String, Object> one : listLogData){
			String enrollNumber = (String) one.get("EnrollNumber");//编号
			String time = (String)one.get("Time");//打卡时间
			Integer verifyMode = (Integer)one.get("VerifyMode");//打卡验证
			Integer inOutMode = (Integer)one.get("InOutMode");//打卡类型
			
			AttenceRecord attenceRecord = new AttenceRecord();
			attenceRecord.setEnrollNumber(enrollNumber);
			attenceRecord.setInOutMode(inOutMode);
			attenceRecord.setTime(time);
			attenceRecord.setVerifyMode(verifyMode);
			attenceRecord.setComId(userInfo.getComId());
			
			attenceDao.add(attenceRecord);
		}
				
		AttenceRecordUpload upload = new AttenceRecordUpload();
		upload.setComId(userInfo.getComId());
		upload.setLastTime(DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss));
		attenceDao.add(upload);
	}
	/**
	 * 查询连接配置
	 * @param userInfo
	 */
	public AttenceConnSet queryAttenceConnSet(UserInfo userInfo) {
		return attenceDao.queryAttenceConnSet(userInfo);
	}
	/**
	 * 添加团队考勤设置
	 * @param userInfo
	 * @param attenceConnSet
	 */
	public Integer addAttenceConnSet(UserInfo userInfo, AttenceConnSet attenceConnSet) {
		attenceConnSet.setComId(userInfo.getComId());
		return attenceDao.add(attenceConnSet);
	}
	/**
	 * 修改团队考勤设置
	 * @param userInfo
	 * @param attenceConnSet
	 */
	public void updateAttenceConnSet(UserInfo userInfo, AttenceConnSet attenceConnSet) {
		attenceDao.update(attenceConnSet);
	}
	/**
	 * 获取上次上传的时间
	 * @param userInfo
	 * @return
	 */
	public AttenceRecordUpload queryLastUpload(UserInfo userInfo){
		return attenceDao.queryLastUpload(userInfo);
	}
	/**
	 * 某时间考勤状况
	 * @param userInfo
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> listUserAttenceState(UserInfo userInfo,AttenceRecord attenceVo,boolean isForceIn){
		String date = attenceVo.getSearchDate();
		List<UserInfo> listEnabledUser = userInfoService.listUserByAttence(userInfo,attenceVo.getListCreator(),attenceVo.getListDep(),isForceIn);
		for(UserInfo enabledUser :listEnabledUser){
			if(null == enabledUser.getEnrollNumber() || enabledUser.getEnrollNumber().equals("0")){//未设置编号
				continue;
			}else{
				AttenceRecord attenceRecord = new AttenceRecord();
				//设置了编号，取打卡记录
				List<AttenceRecord> listAttence = attenceDao.listAttenceByUser(enabledUser,date);
				
				/**************************节假日与否**************************/
				//获取考勤规则
				AttenceRule attenceRule = this.getAttenceRule(null, userInfo.getComId());
				//获取该日期的节假日类型
				String isWorkDay = festModService.isWorkDay(userInfo.getComId(), attenceRule.getRuleType(), date);
				//获取该日期的星期数
				Date dateDate = DateTimeUtil.parseDate(date, DateTimeUtil.yyyy_MM_dd);
				Integer weekNum = DateTimeUtil.getDay(DateTimeUtil.date2Calendar(dateDate));
				
				List<AttenceTime> listAttenceTime = new ArrayList();//考勤时间段
				Boolean isFestDay = false;//是否是节假日
				
				//判断当日是否是节假日
				if(attenceRule.getRuleType().equals("1")){//标准考勤
					// 休假
					if((DateTimeUtil.isWeekend(dateDate) && null ==isWorkDay)//周末未设置加班
							||(null !=isWorkDay && isWorkDay.equals("1"))){//设定或者国定节假日
						isFestDay = true;
					}else{
						listAttenceTime = attenceDao.listAttenceTimes(userInfo.getComId(),attenceRule.getId(), null);
					}
				}else{
					listAttenceTime = attenceDao.queryAttenceTypeByWeek(userInfo.getComId(), weekNum, attenceRule.getId());
					if(!(null!=isWorkDay && isWorkDay.equals("1"))//不是国家或者自定节假日
							&&(null !=listAttenceTime && !listAttenceTime.isEmpty())){//考勤日期
						
					}else{//休假
						isFestDay = true;
					}
				}
				/**************************节假日与否**************************/
				String dayTimeST = "";//规定签到时间
				String dayTimeET = "";//规定签退时间
				for(int i = 0;i<listAttenceTime.size();i++){
					if(i==0){
						dayTimeST = listAttenceTime.get(i).getDayTimeS();
					}else if(i == listAttenceTime.size()-1){
						dayTimeET = listAttenceTime.get(i).getDayTimeE();
					}
				}
				//上次同步时间
				AttenceRecordUpload lastUpload =  this.queryLastUpload(userInfo);
				if(null!=lastUpload && null != lastUpload.getLastTime()){
					
				
				/**************************是否有考勤记录**************************/				
				if(null != listAttence && !listAttence.isEmpty()){//有记录考勤记录
					attenceRecord.setAttenceType(1);
					
					String dayTimeS = "";//签到时间
					String dayTimeE = "";//签退时间
					for(int i = 0;i<listAttence.size();i++){
						if(i==0){
							dayTimeS = listAttence.get(i).getTime().substring(11, 16);
						}else if(i == listAttence.size()-1){
							dayTimeE = listAttence.get(i).getTime().substring(11, 16);
						}
					}
					if(!isFestDay){//考勤日期,非节假日
						if(lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00")>=0 && lastUpload.getLastTime().compareTo(date+" "+dayTimeET+":00")<0){
							//同步至当前查询日期签到与签退当中
							if(dayTimeS.compareTo(dayTimeST)>0){//考勤时间比规定时间迟  （请假，外出，出差，迟到）
								//判断是否请假,出差,外出
								List<Leave> listLeave = attenceDao.listLeaveByDateTime(enabledUser, dayTimeST);
								if(null != listLeave && !listLeave.isEmpty()){
									attenceRecord.setRecordType(2);//请假
								}else{
									List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(enabledUser, date);
									if(null != listLoanApply && !listLoanApply.isEmpty()){
										attenceRecord.setRecordType(3);//出差
									}else{
										attenceRecord.setUnusualType(2);//迟到
									}
								}
							}
							dayTimeE = "";//未同步下班
						}else{//已同步此日签退数据
							if(dayTimeS.compareTo(dayTimeET)>0){//签到时间 在规定签退之后,签到时间无效
								dayTimeS = "";
								attenceRecord.setUnusualType(1);//异常
							}else{
								if(dayTimeS.compareTo(dayTimeST)>0 ){//考勤时间比规定时间迟  （请假，外出，出差，迟到）
									//判断是否请假,出差,外出
									List<Leave> listLeave = attenceDao.listLeaveByDateTime(enabledUser, dayTimeST);
									if(null != listLeave && !listLeave.isEmpty()){
										attenceRecord.setRecordType(2);//请假
									}else{
										List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(enabledUser, date);
										if(null != listLoanApply && !listLoanApply.isEmpty()){
											attenceRecord.setRecordType(3);//出差
										}else{
											attenceRecord.setUnusualType(2);//迟到
										}
									}
								}
							}
							if(dayTimeE.compareTo(dayTimeST)<=0){//签退时间 在规定签到之前,签退时间无效
								dayTimeE = "";
								attenceRecord.setUnusualType(1);//异常
							}else{
								if(dayTimeE.compareTo(dayTimeET)<0){//签退时间 在规定签退之前,（请假，外出，出差，早退）
									//判断是否请假,出差,外出
									List<Leave> listLeave = attenceDao.listLeaveByDateTime(enabledUser, dayTimeET);
									if(null != listLeave && !listLeave.isEmpty()){
										attenceRecord.setRecordType(2);//请假
									}else{
										List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(enabledUser, date);
										if(null != listLoanApply && !listLoanApply.isEmpty()){
											attenceRecord.setRecordType(3);//出差
										}else{
											attenceRecord.setUnusualType(3);//早退
										}
									}
								}
							}
						}
					}else{//节假日 加班，直接记录考勤时间
						attenceRecord.setRecordType(1);
					}
					attenceRecord.setDayTimeS(dayTimeS);
					attenceRecord.setDayTimeE(dayTimeE);
				}else{//未考勤 判断是否出差,请假
					attenceRecord.setAttenceType(0);//无记录
					
					List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(enabledUser, date);
					if(null != listLoanApply && !listLoanApply.isEmpty()){
						attenceRecord.setRecordType(3);//出差
						enabledUser.setAttenceRecord(attenceRecord);
						continue;
					}
					
					if(isFestDay){
						attenceRecord.setRecordType(4);//休假
						enabledUser.setAttenceRecord(attenceRecord);
						continue;
					}else{
						List<Leave> listLeave = attenceDao.listLeaveByDate(enabledUser, date);
						if(null != listLeave && !listLeave.isEmpty()){
							attenceRecord.setRecordType(2);//请假
							enabledUser.setAttenceRecord(attenceRecord);
							continue;
						}
						if(lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00")>=0 && lastUpload.getLastTime().compareTo(date+" "+dayTimeET+":00")<0){
							attenceRecord.setRecordType(5);//未打卡
						}else if(lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00") <0 ){
							attenceRecord.setRecordType(6);//未同步
						}else{
							attenceRecord.setRecordType(0);//旷工
						}
					}
					
				}
				}else{
					attenceRecord.setAttenceType(0);//无记录
				}
				/**************************是否有考勤记录**************************/	
				enabledUser.setAttenceRecord(attenceRecord);
			}
		}
		return listEnabledUser;
	}

	/**
	 * 个人时间考勤记录
	 * 
	 * @param curUser
	 * @param attenceRecord
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<AttenceRecord> listAttenceRecordOfSelf(UserInfo curUser, AttenceRecord attenceRecordVo){
		if(null == attenceRecordVo ||null == attenceRecordVo.getUserId()){
			attenceRecordVo.setUserId(curUser.getId());
		}
		UserInfo userInfo = userInfoService.getUserBaseInfo(curUser.getComId(), attenceRecordVo.getUserId());
		if(null == attenceRecordVo.getUserName()){
			attenceRecordVo.setUserName(userInfo.getUserName());
		}
		if (null == userInfo.getEnrollNumber()) {
			return null;
		} else {
			List<AttenceRecord> listAttenceRecord = new ArrayList();
			String startDate = attenceRecordVo.getStartDate();
			String endDate = attenceRecordVo.getEndDate();
			Date dBegin = DateTimeUtil.parseDate(startDate, DateTimeUtil.yyyy_MM_dd);
			Date dEnd = DateTimeUtil.parseDate(endDate, DateTimeUtil.yyyy_MM_dd);
			// 获取查询日期之间的所有日期
			List<Date> listDate = DateTimeUtil.findDates(dBegin, dEnd);
			if(null !=attenceRecordVo && null !=attenceRecordVo.getOrderBy() && attenceRecordVo.getOrderBy().equals("asc")){
				
			}else{
				//反转降序排列时间
				Collections.reverse(listDate);  
			}
			for (Date oneDate : listDate) {
				String date = DateTimeUtil.formatDate(oneDate, DateTimeUtil.yyyy_MM_dd);

				AttenceRecord attenceRecord = new AttenceRecord();
				attenceRecord.setSearchDate(date);
				attenceRecord.setEnrollNumber(userInfo.getEnrollNumber());
				// 设置了编号，取打卡记录
				List<AttenceRecord> listAttence = attenceDao.listAttenceByUser(userInfo, date);

				// 获取考勤规则
				AttenceRule attenceRule = this.getAttenceRule(null, userInfo.getComId());
				// 获取该日期的节假日类型
				String isWorkDay = festModService.isWorkDay(userInfo.getComId(), attenceRule.getRuleType(), date);
				// 获取该日期的星期数
				Date dateDate = DateTimeUtil.parseDate(date, DateTimeUtil.yyyy_MM_dd);
				Integer weekNum = DateTimeUtil.getDay(DateTimeUtil.date2Calendar(dateDate));

				List<AttenceTime> listAttenceTime = new ArrayList();// 考勤时间段
				Boolean isFestDay = false;// 是否是节假日

				// 判断当日是否是节假日
				if (attenceRule.getRuleType().equals("1")) {// 标准考勤
					// 休假
					if((DateTimeUtil.isWeekend(dateDate) && null ==isWorkDay)//周末未设置加班
							||(null !=isWorkDay && isWorkDay.equals("1"))){//设定或者国定节假日
						isFestDay = true;
					} else {
						listAttenceTime = attenceDao.listAttenceTimes(userInfo.getComId(), attenceRule.getId(),null);
					}
				} else {
					listAttenceTime = attenceDao.queryAttenceTypeByWeek(userInfo.getComId(), weekNum,attenceRule.getId());
					if (!(null != isWorkDay && isWorkDay.equals("1"))// 不是国家或者自定节假日
							&& (null != listAttenceTime && !listAttenceTime.isEmpty())) {// 考勤日期
					} else {// 休假
						isFestDay = true;
					}
				}
				
				String dayTimeST = "";// 规定签到时间
				String dayTimeET = "";// 规定签退时间
				for (int i = 0; i < listAttenceTime.size(); i++) {
					if (i == 0) {
						dayTimeST = listAttenceTime.get(i).getDayTimeS();
					} else if (i == listAttenceTime.size() - 1) {
						dayTimeET = listAttenceTime.get(i).getDayTimeE();
					}
				}
				// 上次同步时间
				AttenceRecordUpload lastUpload = this.queryLastUpload(userInfo);
				if(null != lastUpload && null!=lastUpload.getLastTime()){
					
				
				if (null != listAttence && !listAttence.isEmpty()) {
					attenceRecord.setAttenceType(1);// 有记录

					String dayTimeS = "";// 签到时间
					String dayTimeE = "";// 签退时间
					for (int i = 0; i < listAttence.size(); i++) {
						if (i == 0) {
							dayTimeS = listAttence.get(i).getTime().substring(11, 16);
						} else if (i == listAttence.size() - 1) {
							dayTimeE = listAttence.get(i).getTime().substring(11, 16);
						}
					}

					if (!isFestDay) {// 考勤日期
						if(lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00")>=0 && lastUpload.getLastTime().compareTo(date+" "+dayTimeET+":00")<0){
							//同步至当前查询日期签到与签退当中
							if(dayTimeS.compareTo(dayTimeST)>0){//考勤时间比规定时间迟  （请假，外出，出差，迟到）
								//判断是否请假,出差,外出
								List<Leave> listLeave = attenceDao.listLeaveByDateTime(curUser, dayTimeST);
								if(null != listLeave && !listLeave.isEmpty()){
									attenceRecord.setRecordType(2);//请假
								}else{
									List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(curUser, date);
									if(null != listLoanApply && !listLoanApply.isEmpty()){
										attenceRecord.setRecordType(3);//出差
									}else{
										attenceRecord.setUnusualType(2);//迟到
									}
								}
							}
							dayTimeE = "";//未同步下班
						}else{//已同步此日签退数据
							if(dayTimeS.compareTo(dayTimeET)>0){//签到时间 在规定签退之后,签到时间无效
								dayTimeS = "";
								attenceRecord.setUnusualType(1);//异常
							}else{
								if(dayTimeS.compareTo(dayTimeST)>0 ){//考勤时间比规定时间迟  （请假，外出，出差，迟到）
									//判断是否请假,出差,外出
									List<Leave> listLeave = attenceDao.listLeaveByDateTime(curUser, dayTimeST);
									if(null != listLeave && !listLeave.isEmpty()){
										attenceRecord.setRecordType(2);//请假
									}else{
										List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(curUser, date);
										if(null != listLoanApply && !listLoanApply.isEmpty()){
											attenceRecord.setRecordType(3);//出差
										}else{
											attenceRecord.setUnusualType(2);//迟到
										}
									}
								}
							}
							if(dayTimeE.compareTo(dayTimeST)<=0){//签退时间 在规定签到之前,签退时间无效
								dayTimeE = "";
								attenceRecord.setUnusualType(1);//异常
							}else{
								if(dayTimeE.compareTo(dayTimeET)<0){//签退时间 在规定签退之前,（请假，外出，出差，早退）
									//判断是否请假,出差,外出
									List<Leave> listLeave = attenceDao.listLeaveByDateTime(curUser, dayTimeET);
									if(null != listLeave && !listLeave.isEmpty()){
										attenceRecord.setRecordType(2);//请假
									}else{
										List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(curUser, date);
										if(null != listLoanApply && !listLoanApply.isEmpty()){
											attenceRecord.setRecordType(3);//出差
										}else{
											attenceRecord.setUnusualType(3);//早退
										}
									}
								}
							}
						}
					} else {// 加班，直接记录考勤时间
						attenceRecord.setRecordType(1);
					}
					attenceRecord.setDayTimeS(dayTimeS);
					attenceRecord.setDayTimeE(dayTimeE);
				} else {// 未考勤 判断是否出差,请假
					attenceRecord.setAttenceType(0);// 无记录

					List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(userInfo, date);
					if (null != listLoanApply && !listLoanApply.isEmpty()) {
						attenceRecord.setRecordType(3);// 出差
						listAttenceRecord.add(attenceRecord);
						continue;
					}
					
					if(isFestDay){
						continue;
					}else{
						List<Leave> listLeave = attenceDao.listLeaveByDate(userInfo, date);
						if (null != listLeave && !listLeave.isEmpty()) {
							attenceRecord.setRecordType(2);// 请假
							listAttenceRecord.add(attenceRecord);
							continue;
						}
						if(lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00")>=0 && lastUpload.getLastTime().compareTo(date+" "+dayTimeET+":00")<0){
							attenceRecord.setRecordType(5);//未打卡
						}else if(lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00") <0 ){
							attenceRecord.setRecordType(6);//未同步
						}else{
							attenceRecord.setRecordType(0);//旷工
						}
					}
				}
				}
				listAttenceRecord.add(attenceRecord);
			}
			return listAttenceRecord;
		}
	}
	
	/**
	 * 考勤统计列表
	 * 
	 * @param userInfo
	 * @param attenceRecord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserInfo> attenceStatistics(UserInfo userInfo, AttenceRecord attenceRecord, boolean isForceIn) {
		List<UserInfo> listUserInfo = userInfoService.listUserByAttence(userInfo, attenceRecord.getListCreator(),
				attenceRecord.getListDep(), isForceIn);

		for (UserInfo userInfoT : listUserInfo) {
			// 获取该时间段内的请假集合
			JSONObject leaveObj = attenceDao.attenceDetailStatistics(userInfoT, attenceRecord,ConstantInterface.TYPE_LEAVE);
			String hours = leaveObj.getString("TOTALSUM");// 时长
			userInfoT.setLeaveTotal(hours);
			
			Integer leaveTimeT = leaveObj.getInteger("TOTALNUM");
			userInfoT.setLeaveTimeT(leaveTimeT);
			
			JSONObject overTimeObj = attenceDao.attenceDetailStatistics(userInfoT, attenceRecord,ConstantInterface.TYPE_OVERTIME);
			OverTime overTime = new OverTime();
			
			Integer overTimeT = overTimeObj.getInteger("TOTALNUM");
			overTime.setOverTimeT(overTimeT);
			
			String overTimeTotal = overTimeObj.getString("TOTALSUM");// 时长
			overTime.setOverTimeTotal(overTimeTotal);
			
			userInfoT.setOverTime(overTime);

			if (null == userInfoT.getEnrollNumber()) {
				continue;
			}

			Integer lateTime = 0;// 迟到次数
			Integer leaveEarlyTime = 0;// 早退次数
			Integer unusualTime = 0;// 异常次数
			Integer absentTime = 0;// 旷工
			// 考勤异常次数2018-01-01 11-11-11
			// 获取查询日期之间的所有日期
			if(attenceRecord.getStartDate().equals("")){
				attenceRecord.setStartDate(DateTimeUtil.getNowDateStr(0).substring(0,7) + "-01");
			}
			if(attenceRecord.getEndDate().equals("")){
				attenceRecord.setEndDate(DateTimeUtil.getNowDateStr(0));
			}
			Date dBegin = DateTimeUtil.parseDate(attenceRecord.getStartDate(), DateTimeUtil.yyyy_MM_dd);
			Date dEnd = DateTimeUtil.parseDate(attenceRecord.getEndDate(), DateTimeUtil.yyyy_MM_dd);
			List<Date> listDate = DateTimeUtil.findDates(dBegin, dEnd);
			// 反转降序排列时间
			for (Date oneDate : listDate) {
				String date = DateTimeUtil.formatDate(oneDate, DateTimeUtil.yyyy_MM_dd);

				// 设置了编号，取打卡记录
				List<AttenceRecord> listAttence = attenceDao.listAttenceByUser(userInfoT, date);

				// 获取考勤规则
				AttenceRule attenceRule = this.getAttenceRule(null, userInfo.getComId());
				// 获取该日期的节假日类型
				String isWorkDay = festModService.isWorkDay(userInfo.getComId(), attenceRule.getRuleType(), date);
				// 获取该日期的星期数
				Date dateDate = DateTimeUtil.parseDate(date, DateTimeUtil.yyyy_MM_dd);
				Integer weekNum = DateTimeUtil.getDay(DateTimeUtil.date2Calendar(dateDate));

				List<AttenceTime> listAttenceTime = new ArrayList();// 考勤时间段
				Boolean isFestDay = false;// 是否是节假日

				// 判断当日是否是节假日
				if (attenceRule.getRuleType().equals("1")) {// 标准考勤
					// 休假
					if ((DateTimeUtil.isWeekend(dateDate) && null == isWorkDay)// 周末未设置加班
							|| (null != isWorkDay && isWorkDay.equals("1"))) {// 设定或者国定节假日
						isFestDay = true;
					} else {
						listAttenceTime = attenceDao.listAttenceTimes(userInfo.getComId(), attenceRule.getId(), null);
					}
				} else {
					listAttenceTime = attenceDao.queryAttenceTypeByWeek(userInfo.getComId(), weekNum,
							attenceRule.getId());
					if (!(null != isWorkDay && isWorkDay.equals("1"))// 不是国家或者自定节假日
							&& (null != listAttenceTime && !listAttenceTime.isEmpty())) {// 考勤日期

					} else {// 休假
						isFestDay = true;
					}
				}
				String dayTimeST = "";// 规定签到时间
				String dayTimeET = "";// 规定签退时间
				for (int i = 0; i < listAttenceTime.size(); i++) {
					if (i == 0) {
						dayTimeST = listAttenceTime.get(i).getDayTimeS();
					} else if (i == listAttenceTime.size() - 1) {
						dayTimeET = listAttenceTime.get(i).getDayTimeE();
					}
				}
				
				// 上次同步时间
				AttenceRecordUpload lastUpload = this.queryLastUpload(userInfo);
				if(null != lastUpload && null!=lastUpload.getLastTime()){
				if (null != listAttence && !listAttence.isEmpty()) {

					String dayTimeS = "";// 签到时间
					String dayTimeE = "";// 签退时间
					for (int i = 0; i < listAttence.size(); i++) {
						if (i == 0) {
							dayTimeS = listAttence.get(i).getTime().substring(11, 16);
						} else if (i == listAttence.size() - 1) {
							dayTimeE = listAttence.get(i).getTime().substring(11, 16);
						}
					}

					if (!isFestDay) {// 考勤日期
						if (lastUpload.getLastTime().compareTo(date + " " + dayTimeST + ":00") >= 0
								&& lastUpload.getLastTime().compareTo(date + " " + dayTimeET + ":00") < 0) {
							// 同步至当前查询日期签到与签退当中
							if (dayTimeS.compareTo(dayTimeST) > 0) {// 考勤时间比规定时间迟
																	// （请假，外出，出差，迟到）
								// 判断是否请假,出差,外出
								List<Leave> listLeave = attenceDao.listLeaveByDateTime(userInfoT, dayTimeST);
								if (null != listLeave && !listLeave.isEmpty()) {
									attenceRecord.setRecordType(2);// 请假
								} else {
									List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(userInfoT,
											date);
									if (null != listLoanApply && !listLoanApply.isEmpty()) {
										attenceRecord.setRecordType(3);// 出差
									} else {
										lateTime++;// 迟到
									}
								}
							}
							dayTimeE = "";// 未同步下班
						} else {// 已同步此日签退数据
							if (dayTimeS.compareTo(dayTimeET) > 0) {// 签到时间
																	// 在规定签退之后,签到时间无效
								dayTimeS = "";
								unusualTime++;// 异常
							} else {
								if (dayTimeS.compareTo(dayTimeST) > 0) {// 考勤时间比规定时间迟
																		// （请假，外出，出差，迟到）
									// 判断是否请假,出差,外出
									List<Leave> listLeave = attenceDao.listLeaveByDateTime(userInfoT, dayTimeST);
									if (null != listLeave && !listLeave.isEmpty()) {
										attenceRecord.setRecordType(2);// 请假
									} else {
										List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(userInfoT,
												date);
										if (null != listLoanApply && !listLoanApply.isEmpty()) {
											attenceRecord.setRecordType(3);// 出差
										} else {
											lateTime++;// 迟到
										}
									}
								}
							}
							if (dayTimeE.compareTo(dayTimeST) <= 0) {// 签退时间
																		// 在规定签到之前,签退时间无效
								dayTimeE = "";
								unusualTime++;// 异常
							} else {
								if (dayTimeE.compareTo(dayTimeET) < 0) {// 签退时间
																		// 在规定签退之前,（请假，外出，出差，早退）
									// 判断是否请假,出差,外出
									List<Leave> listLeave = attenceDao.listLeaveByDateTime(userInfoT, dayTimeET);
									if (null != listLeave && !listLeave.isEmpty()) {
										attenceRecord.setRecordType(2);// 请假
									} else {
										List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(userInfoT,
												date);
										if (null != listLoanApply && !listLoanApply.isEmpty()) {
											attenceRecord.setRecordType(3);// 出差
										} else {
											leaveEarlyTime++;// 早退
										}
									}
								}
							}
						}
					}
				} else {// 未考勤 判断是否出差,请假
					List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(userInfoT, date);
					if (null != listLoanApply && !listLoanApply.isEmpty()) {
						continue;
					}

					if (isFestDay) {
						continue;
					} else {
						List<Leave> listLeave = attenceDao.listLeaveByDate(userInfoT, date);
						if (null != listLeave && !listLeave.isEmpty()) {
							continue;
						}
						if(lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00")>=0 && lastUpload.getLastTime().compareTo(date+" "+dayTimeET+":00")<0){
							unusualTime++;// 异常
						}else{
							absentTime++;
						}
					}
					
				}
				}
			}
			userInfoT.setAbsentTime(absentTime);
			userInfoT.setUnusualTime(unusualTime);
			userInfoT.setLateTime(lateTime);
			userInfoT.setLeaveEarlyTime(leaveEarlyTime);
		}
		return listUserInfo;
	}
	/**
	 * 获取考勤机内人员
	 * @param allUserInfo
	 * @param userInfo
	 */
	public void uploadAttenceUser(List<Map<String, Object>> allUserInfo, UserInfo userInfo) {
		attenceDao.delTable("attenceUser");
		//处理数据
		for(Map<String, Object> one : allUserInfo){
			String enrollNumber = (String) one.get("EnrollNumber");//编号
			String name = (String)one.get("Name");//人员名称
			Integer privilege = (Integer)one.get("Privilege");//权限
			
			AttenceUser attenceUser = new AttenceUser();
			attenceUser.setComId(userInfo.getComId());
			attenceUser.setEnrollNumber(enrollNumber);
			attenceUser.setName(name);
			attenceUser.setPrivilege(privilege);
			attenceDao.add(attenceUser);
		}
		
	}
	/**
	 * 分页查询  考勤人员
	 * @param attenceUser
	 * @param userInfo
	 * @return
	 */
	public List<AttenceUser> listPageAttenceUser(AttenceUser attenceUser,UserInfo userInfo){
		return attenceDao.listPageAttenceUser(attenceUser, userInfo);
	}

	/**
	 * 检查今日的考勤
	 * @param
	 * @return void
	 * @author LiuXiaoLin
	 * @date 2018/6/8 0008 16:58
	 */
	public void checkAttendance(String date){
		//取出所有公司编号，并填充对应的公司员工
		List<UserInfo> companies = attenceDao.listCompanies();
		Map<String,List<UserInfo>> map = new HashMap<>();
		for(UserInfo userInfo : companies){
			map.put(userInfo.getComId().toString(),userInfoService.listUserWithEnNumber(userInfo.getComId()));
		}

		//获取需要检查的考勤日期
//		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - (60 * 60 * 24 * 1000)));

		AttenceState attenceState = null;

		//遍历所有公司
		for(String key : map.keySet()){
			// 获取考勤规则
			AttenceRule attenceRule = this.getAttenceRule(null, Integer.parseInt(key));
			// 获取该日期的节假日类型
			String isWorkDay = festModService.isWorkDay(Integer.parseInt(key), attenceRule.getRuleType(), date);
			// 获取该日期的星期数
			Date dateDate = DateTimeUtil.parseDate(date, DateTimeUtil.yyyy_MM_dd);
			Integer weekNum = DateTimeUtil.getDay(DateTimeUtil.date2Calendar(dateDate));

			List<AttenceTime> listAttenceTime = new ArrayList();// 考勤时间段
			Boolean isFestDay = false;// 是否是节假日

			// 判断当日是否是节假日
			if (attenceRule.getRuleType().equals("1")) {// 标准考勤
				// 休假
				if ((DateTimeUtil.isWeekend(dateDate) && null == isWorkDay)// 周末未设置加班
						|| (null != isWorkDay && isWorkDay.equals("1"))) {// 设定或者国定节假日
					isFestDay = true;
				} else {
					listAttenceTime = attenceDao.listAttenceTimes(Integer.parseInt(key), attenceRule.getId(), null);
				}
			} else {
				listAttenceTime = attenceDao.queryAttenceTypeByWeek(Integer.parseInt(key), weekNum,
						attenceRule.getId());
				if (!(null != isWorkDay && isWorkDay.equals("1"))// 不是国家或者自定节假日
						&& (null != listAttenceTime && !listAttenceTime.isEmpty())) {// 考勤日期

				} else {// 休假
					isFestDay = true;
				}
			}
			String dayTimeST = "";// 规定签到时间
			String dayTimeET = "";// 规定签退时间
			for (int i = 0; i < listAttenceTime.size(); i++) {
				if(listAttenceTime.size() == 1){
					//考勤规定时间只有一个对象的情况会导致无下班打卡时间
					dayTimeST = listAttenceTime.get(i).getDayTimeS();
					dayTimeET = listAttenceTime.get(i).getDayTimeE();
				}else{
					if (i == 0) {
						dayTimeST = listAttenceTime.get(i).getDayTimeS();
					} else if (i == listAttenceTime.size() - 1) {
						dayTimeET = listAttenceTime.get(i).getDayTimeE();
					}
				}
			}

			//获取当前公司的所有员工
			List<UserInfo> userInfos = map.get(key);

			//遍历当前公司的员工
			for(UserInfo userinfoT : userInfos){
				//待插入对象
				attenceState = new AttenceState();

				//设置对象
				attenceState.setUserId(userinfoT.getId());

				//设置企业编号
				attenceState.setComId(userinfoT.getComId());

				//设置考勤日期
				attenceState.setAttenceDate(date.substring(0,10));

				//考勤状态
				int state = 0;

				//取出当前员工该日的考勤记录
				List<AttenceRecord> records = attenceDao.listAttenceByUser(userinfoT,date.substring(0,10));
				String dayTimeS = "";// 签到时间
				String dayTimeE = "";// 签退时间
				for (int i = 0; i < records.size(); i++) {
					if (i == 0) {
						dayTimeS = records.get(i).getTime();
					} else if (i == records.size() - 1) {
						dayTimeE = records.get(i).getTime();
					}
				}

				//设置签到签退时间
				attenceState.setSignInTime(dayTimeS);
				attenceState.setSignOutTime(dayTimeE);

				//取出最近一次打卡更新截止日期，用来判断员工打卡是否受到更新的影响
				AttenceRecordUpload lastUpload = this.queryLastUpload(userinfoT);

				//取出请假记录，检查是否请假
				List<Leave> leaves = attenceDao.listLeaveByDate(userinfoT,date.substring(0,10));

				//取出出差记录，检查是否出差
				List<FeeBudget> loanApplies = financialService.listLoanApplyByDate(userinfoT,date.substring(0,10));

				//取出加班记录，检查是否加班
				List<OverTime> overTimes = attenceDao.listOverTimeByXZHSTime(userinfoT,date.substring(0,10));
				//因为数据里出现了签到签退相同的情况，所以只能做一个异常处理，不知道是运行数据还是测试数据
				if(!dayTimeS.equals(dayTimeE) || (dayTimeS.equals("") && dayTimeE.equals(""))){
					if(!isFestDay){//考勤日
						if(!dayTimeS.equals("") && !dayTimeE.equals("")){//有正常打卡记录
							//检查迟到
							String temS = dayTimeS.substring(11, 16);
							if(temS.compareTo(dayTimeST) > 0) {
								boolean isMorningLeave = false;
								//打卡时间已超过规定时间，检查是否请假
								for (int i = 0; i < leaves.size(); i++) {
									//如果当前签到时间在请假时间内则视为请假
									if (leaves.get(i).getEndTime().compareTo(dayTimeS) >= 0 && leaves.get(i).getStartTime().compareTo(dayTimeS) <= 0) {
										state = state | 4;
										isMorningLeave = true;
										break;
									}
								}
								//打卡记录大于规定时间，并且超过的部分没有请假则视为迟到，换休需要手动修改
								if (!isMorningLeave) {
									state = state | 1;//迟到
								}
							}

							//检查早退
							String temE = dayTimeE.substring(11, 18);
							if(temE.compareTo(dayTimeET) < 0) {
								boolean isAfternoonLeave = false;
								//打卡时间未到规定时间，检查是否请假
								for (int i = 0; i < leaves.size(); i++) {
									//如果当前签到时间在请假时间内则视为请假
									if (leaves.get(i).getEndTime().compareTo(dayTimeE) >= 0 && leaves.get(i).getStartTime().compareTo(dayTimeE) <= 0) {
										state = state | 4;
										isAfternoonLeave = true;
										break;
									}
								}
								//打卡记录大于规定时间，并且超过的部分没有请假则视为早退，换休需要手动修改
								if (!isAfternoonLeave) {
									state = state | 2;//早退
								}
							}
						}else if(dayTimeS.equals("") && dayTimeE.equals("")){//无打卡记录
							//检查当日是否打卡更新
							if(lastUpload != null && lastUpload.getLastTime() != null && lastUpload.getLastTime().compareTo(date+" "+dayTimeST+":00")>=0
									&& lastUpload.getLastTime().compareTo(date+" "+dayTimeET+":00")<0){
								state = state | 32;//异常
							}else{
								//检查请假
								boolean isAllDayLeave = false;
								for (int i = 0; i < leaves.size(); i++) {
									//如果当前签到时间在请假时间内则视为请假
									if (leaves.get(i).getEndTime().compareTo(date.substring(11,16) + " 18:00:00") >= 0
											&& leaves.get(i).getStartTime().compareTo(date.substring(11,16) + " 09:00:00") <= 0) {
										state = state | 4;//请假
										isAllDayLeave = true;
										break;
									}
								}

								if(!isAllDayLeave){
									//检查出差
									if(null != loanApplies && loanApplies.size() > 0){
										state = state | 16;
									}else{
										//无打卡更新，无请假，无出差视为旷工
										state = state | 64;//旷工
									}
								}else{
									state = state | 4;//请假
								}
							}
						}else {
							//未签到或签退，标记为异常
							state = state | 32;//异常
						}
					}
					//检查加班
					if(null != overTimes && overTimes.size() > 0){
					    state = state | 8;//加班
					}
				}else{
					state = state | 32;//异常
				}

				//节假日加班和正常考勤才进行记录
				if((isFestDay && (state & 8) == 1) || !isFestDay){
					attenceState.setState(state + "");
					attenceDao.add(attenceState);
				}
			}
		}
	}
	
	/**
	 * 初始化请假，加班的数据
	 * @param comId
	 */
	public void initAttence(Integer comId) {
		UserInfo curUser = new UserInfo();
		curUser.setComId(comId);
		attenceDao.delByField("attenceDetail", new String[]{"comId"}, new Object[]{comId});
		logger.info("初始化请假数据开始:"+comId);
		//初始化请假信息
		initLeaveData(curUser);
		logger.info("初始化请假数据结束:"+comId);
		
		logger.info("初始化加班数据开始:"+comId);
		//初始化加班信息
		initOverTimeData(curUser);
		logger.info("初始化加班数据结束:"+comId);
		
		
	}
	/**
	 * 初始化加班信息
	 * @param curUser
	 */
	private void initOverTimeData(UserInfo curUser) {
		Integer pageNum = 0;
		Integer pageSize = 100;
		PaginationContext.setPageSize(pageSize);
		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
		OverTime overTime = new OverTime();
		overTime.setSpState(1);
		//初始化请假的数据
		List<OverTime> overTimes = attenceDao.listPagedOverTime(curUser, overTime, true);
		Integer count = PaginationContext.getTotalCount();
		logger.info("初始化加班开始..."+pageNum*PaginationContext.getPageSize());
		this.initAddOverTimeData(overTimes);
		
		count = count - pageSize;
		Integer left = count % pageSize;
		Integer totalPage;
		if(left>0){
			totalPage = (count - left)/100 + 1;
		}else{
			totalPage = count/100;
		}
		
		for(int i=0;i<totalPage;i++){
			pageNum ++ ;
			//一次加载行数
			PaginationContext.setPageSize(pageSize);
			//列表数据起始索引位置
			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
			
			overTimes = attenceDao.listPagedOverTime(curUser, overTime, true);
			if(null!=overTimes && !overTimes.isEmpty()){
				logger.info("初始化加班开始..."+pageNum*PaginationContext.getPageSize());
				this.initAddOverTimeData(overTimes);
			}
		}
		
	}
	/**
	 * 添加加班信息
	 * @param leaves
	 */
	private void initAddOverTimeData(List<OverTime> overTimes) {
		if(null!=overTimes && !overTimes.isEmpty()){
			for (OverTime overTime : overTimes) {
				if(overTime.getStatus().equals(4)){
					this.addAttenceOverTimeDetail(overTime);
				}
				
			}
		}
		
	}
	public void addAttenceOverTimeDetail(OverTime overTime) {
		Integer comId = overTime.getComId();
		String dateTimeSStr = overTime.getXzhsStartTime();
		String dateTimeEStr = overTime.getXzhsEndTime();
		
		List<AttenceDetail> resutList = festModService.constrWorkTimeForOverTime(comId,dateTimeSStr,dateTimeEStr);
		if(null!=resutList && !resutList.isEmpty()){
			for (AttenceDetail attenceDetail : resutList) {
				attenceDetail.setComId(overTime.getComId());
				attenceDetail.setUserId(overTime.getCreator());
				attenceDetail.setBusId(overTime.getId());
				attenceDetail.setBusType(ConstantInterface.TYPE_OVERTIME);
				
				attenceDao.add(attenceDetail);
			}
		}
		
	}
	/**
	 * 初始化处理加班信息
	 * @param curUser
	 */
	private void initLeaveData(UserInfo curUser) {
		Integer pageNum = 0;
		Integer pageSize = 100;
		PaginationContext.setPageSize(pageSize);
		PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
		Leave leave = new Leave();
		leave.setSpState(1);
		//初始化请假的数据
		List<Leave> leaves = attenceDao.listPagedLeave(curUser, leave, true);
		Integer count = PaginationContext.getTotalCount();
		logger.info("初始化请假开始..."+pageNum*PaginationContext.getPageSize());
		this.initAddLeaveData(leaves);
		count = count - pageSize;
		Integer left = count % pageSize;
		Integer totalPage;
		if(left>0){
			totalPage = (count - left)/100 + 1;
		}else{
			totalPage = count/100;
		}
		
		for(int i=0;i<totalPage;i++){
			pageNum ++ ;
			//一次加载行数
			PaginationContext.setPageSize(pageSize);
			//列表数据起始索引位置
			PaginationContext.setOffset(pageNum*PaginationContext.getPageSize());
			
			leaves = attenceDao.listPagedLeave(curUser, leave, true);
			if(null!=leaves && !leaves.isEmpty()){
				logger.info("初始化请假开始..."+pageNum*PaginationContext.getPageSize());
				this.initAddLeaveData(leaves);
			}
		}
		
	}
	/**
	 * 添加请假信息
	 * @param leaves
	 */
	private void initAddLeaveData(List<Leave> leaves) {
		if(null!=leaves && !leaves.isEmpty()){
			for (Leave leave : leaves) {
				if(leave.getStatus().equals(4)){
					this.addAttenceLeaveDetail(leave);
				}
				
			}
		}
	}
	
	/**
	 * 细化请假到每一天
	 * @param leave
	 */
	public void addAttenceLeaveDetail(Leave leave) {
		Integer comId = leave.getComId();
		
		String dateTimeSStr = leave.getStartTime();
		String dateTimeEStr = leave.getEndTime();
		List<AttenceDetail> resutList = festModService.constrWorkTimeForLeave(comId,dateTimeSStr,dateTimeEStr);
		if(null!=resutList && !resutList.isEmpty()){
			for (AttenceDetail attenceDetail : resutList) {
				attenceDetail.setComId(leave.getComId());
				attenceDetail.setUserId(leave.getCreator());
				attenceDetail.setBusId(leave.getId());
				attenceDetail.setBusType(ConstantInterface.TYPE_LEAVE);
				
				attenceDao.add(attenceDetail);
			}
		}
		
		
	}
	/**
	 * 同步团队的考勤数据信息
	 * @param comId
	 */
	public void initAttenceDetail(Integer comId) {
		//取得该团队的考勤同步的开始数据
		JSONObject jsonObject = attenceDao.queryFirstAttenceDate(comId);
		if(null == jsonObject){
			return;
		}
		String time = jsonObject.getString("TIME");
		
//		String time = "2018-09-13";
		//当前时间
		String nowDate = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd);
		
		
		UserInfo userInfo  = new UserInfo();
		userInfo.setComId(comId);
		//最后一次同步的时间
		AttenceRecordUpload lastUpload =  this.queryLastUpload(userInfo);
		if(null==lastUpload || null == lastUpload.getLastTime()){
			return;
		}
		
		do{
			
			//获取考勤规则
			AttenceRule attenceRule = this.getAttenceRule(null, comId);
			//获取该日期的节假日类型
			String isWorkDay = festModService.isWorkDay(comId, attenceRule.getRuleType(), nowDate);
			//获取该日期的星期数
			Date dateDate = DateTimeUtil.parseDate(nowDate, DateTimeUtil.yyyy_MM_dd);
			Integer weekNum = DateTimeUtil.getDay(DateTimeUtil.date2Calendar(dateDate));
			
			List<AttenceTime> listAttenceTime = new ArrayList();//考勤时间段
			Boolean blnFestDay = false;//是否是节假日
			
			//判断当日是否是节假日
			if(attenceRule.getRuleType().equals("1")){//标准考勤
				// 休假
				if((DateTimeUtil.isWeekend(dateDate) && null ==isWorkDay)//周末未设置加班
						||(null !=isWorkDay && isWorkDay.equals("1"))){//设定或者国定节假日
					blnFestDay = true;
				}else{
					listAttenceTime = attenceDao.listAttenceTimes(comId,attenceRule.getId(), null);
				}
			}else{
				listAttenceTime = attenceDao.queryAttenceTypeByWeek(comId, weekNum, attenceRule.getId());
				if(!(null!=isWorkDay && isWorkDay.equals("1"))//不是国家或者自定节假日
						&&(null !=listAttenceTime && !listAttenceTime.isEmpty())){//考勤日期
					
				}else{//休假
					blnFestDay = true;
				}
			}
			/**************************节假日与否**************************/
			String dayTimeST = "";//规定签到时间
			String dayTimeET = "";//规定签退时间
			for(int i = 0;i<listAttenceTime.size();i++){
				if(i==0){
					dayTimeST = listAttenceTime.get(i).getDayTimeS();
				}else if(i == listAttenceTime.size()-1){
					dayTimeET = listAttenceTime.get(i).getDayTimeE();
				}
			}
			
			this.addAttenceDetail(nowDate,comId,dayTimeST,dayTimeET,blnFestDay,lastUpload);
			nowDate = DateTimeUtil.addDate(nowDate, DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, -1);
		}while(!nowDate.equals(time));
		
	}
	/**
	 * 添加考勤数据
	 * @param attencDate
	 * @param comId
	 * @param dayTimeET 规定签到时间
	 * @param dayTimeST 规定签退时间
	 * @param blnFestDay 
	 * @param lastUpload 
	 * @param listEnabledUser 
	 */
	@SuppressWarnings("unchecked")
	private void addAttenceDetail(String attencDate, Integer comId, String dayTimeST, String dayTimeET, 
			Boolean blnFestDay, AttenceRecordUpload lastUpload) {
		
		//查询当天没有记录上的数据
		List<AttenceRecordDetail> listAttenceRecord = attenceDao.listAttenceRecordForInit(attencDate, comId);
		
		if(null!=listAttenceRecord && !listAttenceRecord.isEmpty()){
			//缓存同一个人的打卡记录
			Map<Integer,List<AttenceRecordDetail>> userRecordMap  = new HashMap<Integer,List<AttenceRecordDetail>>();
			for (AttenceRecordDetail attenceRecordDetail : listAttenceRecord) {
				Integer userId = attenceRecordDetail.getUserId();
				List<AttenceRecordDetail> userRecordList = userRecordMap.get(userId);
				if(null == userRecordList){
					userRecordList = new java.util.ArrayList<AttenceRecordDetail>();
				}
				userRecordList.add(attenceRecordDetail);
				userRecordMap.put(userId, userRecordList);
				
			}
			//遍历同一个人的打卡记录
			Iterator iter = userRecordMap.entrySet().iterator();
			while (iter.hasNext()) {
			   Map.Entry<Integer,List<AttenceRecordDetail>> entry = (Map.Entry<Integer, List<AttenceRecordDetail>>) iter.next();
			   Integer userId = entry.getKey();
			   List<AttenceRecordDetail> userRecordList = entry.getValue();
			   //处理该人员的打卡记录信息
			   this.updateHandleData(userId,userRecordList,attencDate,dayTimeST,dayTimeET,blnFestDay,lastUpload);
			}
		}
		
	}
	/**
	 * 插入或修改数据
	 * @param userId
	 * @param userRecordList
	 * @param dayTimeET 规定签到时间
	 * @param dayTimeST 规定签退时间
	 * @param blnFestDay 
	 * @param lastUpload 
	 * @param date 
	 */
	private void updateHandleData(Integer userId,
			List<AttenceRecordDetail> userRecordList, String attencDate, String dayTimeST, String dayTimeET, 
			Boolean blnFestDay, AttenceRecordUpload lastUpload) {
		//判断是否请假，外出
		AttenceRecordDetail attenceRecordDetail = userRecordList.get(0);
		if(null==lastUpload || null == lastUpload.getLastTime()){
			return;
		}
		
		//是否出差
		attenceRecordDetail.setAttencDate(attencDate);
		
		UserInfo userInfo  = new UserInfo();
		userInfo.setComId(attenceRecordDetail.getComId());
		userInfo.setId(attenceRecordDetail.getUserId());
		
		//是否出差,默认不是
		List<FeeBudget> listLoanApply = financialService.listLoanApplyByDate(userInfo, attencDate);
		if(null != listLoanApply && !listLoanApply.isEmpty()){
			attenceRecordDetail.setRecordType("3");//出差
			
			if(null != attenceRecordDetail.getId()){
				attenceDao.update(attenceRecordDetail);
			}else{
				attenceDao.add(attenceRecordDetail);
			}
			return;
		}
		
		//是否请假或加班
		String dayTimeS = "";//签到时间
		String dayTimeE = "";//签退时间
		
		//有打卡时间
		if(!StringUtils.isEmpty(userRecordList.get(0).getTime())){
			for(int i = 0;i<userRecordList.size();i++){
				if(i==0){
					dayTimeS = userRecordList.get(i).getTime().substring(11, 16);
				}else if(i == userRecordList.size()-1){
					dayTimeE = userRecordList.get(i).getTime().substring(11, 16);
				}
			}
		}
		
		AttenceRecord attenceRecord = new AttenceRecord();
		attenceRecord.setStartDate(attencDate);
		attenceRecord.setEndDate(attencDate);
		//在下班前同步的数据
		if(lastUpload.getLastTime().compareTo(attencDate+" "+dayTimeST+":00")>=0 
				&& lastUpload.getLastTime().compareTo(attencDate+" "+dayTimeET+":00")<0){//今天未同步完成
			//同步至当前查询日期签到与签退当中
			if(StringUtils.isNotEmpty(dayTimeS) && dayTimeS.compareTo(dayTimeST)>0){//考勤时间比规定时间迟  （请假或迟到）
				//获取该时间段内的请假集合
				JSONObject leaveObj = attenceDao.attenceDetailStatistics(userInfo, attenceRecord,ConstantInterface.TYPE_LEAVE);
				Integer leaveTimeT = leaveObj.getInteger("TOTALNUM");
				
				if(leaveTimeT > 0){
					attenceRecordDetail.setRecordType("2");//请假
				}else{
					attenceRecordDetail.setRecordType("4");//迟到
				}
			}else if(StringUtils.isEmpty(dayTimeS)){
				attenceRecordDetail.setRecordType("-1");//未签到
			}else{
				attenceRecordDetail.setRecordType("0");//正常
			}
			
			if(null != attenceRecordDetail.getId()){
				attenceDao.update(attenceRecordDetail);
			}else{
				attenceDao.add(attenceRecordDetail);
			}
			return;
		}else{//指定时间已同步完成
			
			//查询人员是否休假
			//获取该时间段内的请假集合
			JSONObject leaveObj = attenceDao.attenceDetailStatistics(userInfo, attenceRecord,ConstantInterface.TYPE_LEAVE);
			Integer leaveTimeT = leaveObj.getInteger("TOTALNUM");
			
			JSONObject overTimeObj = attenceDao.attenceDetailStatistics(userInfo, attenceRecord,ConstantInterface.TYPE_OVERTIME);
			Integer overTimeT = overTimeObj.getInteger("TOTALNUM");
			
			if(leaveTimeT > 0 && overTimeT > 0){
				//请假和加班
				attenceRecordDetail.setRecordType("1,2");
			}else if(leaveTimeT > 0){
				//加班
				attenceRecordDetail.setRecordType("2");
			}else if(overTimeT > 0){
				//请假
				attenceRecordDetail.setRecordType("1");
			}else if(StringUtils.isEmpty(dayTimeS) || StringUtils.isEmpty(dayTimeE)){
				//未签到
				attenceRecordDetail.setRecordType("-1");
			}else{
				//判断是否迟到或是早退
				//同步至当前查询日期签到与签退当中
				if(dayTimeS.compareTo(dayTimeST)>0){//考勤时间比规定时间迟  （请假，外出，出差，迟到）
					attenceRecordDetail.setRecordType("4");//迟到
				}else if(dayTimeE.compareTo(dayTimeET)<0 ){
					attenceRecordDetail.setRecordType("5");//早退
				}else{
					attenceRecordDetail.setRecordType("0");//正常
				}
			}
			
			if(null != attenceRecordDetail.getId()){
				attenceDao.update(attenceRecordDetail);
			}else{
				attenceDao.add(attenceRecordDetail);
			}
			return;
		}
	}
	
}
