package com.westar.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.cons.CommonConstant;
import com.westar.base.model.AttenceDetail;
import com.westar.base.model.AttenceRule;
import com.westar.base.model.AttenceType;
import com.westar.base.model.FestMod;
import com.westar.base.model.FestModDate;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.FestModDao;

/**
 * 要素
	1、填写时间起 A1
	2、填写时间止 A2
	3、填写时间日期集合 B
	
	4、法定节假日时间 C1 优先级2
	5、自己维护的节假日 C2 优先级1
	
	6、正常上班时间 D1 优先级2
	7、自己维护的上班日 D2 优先级1
	
	8、工作时段 E
 * @author H87
 *
 */
@Service
public class FestModService {

	@Autowired
	FestModDao festModDao;
	
	@Autowired
	AttenceService attenceService;
	
	private final static Integer[] WEEKS = {0,7, 1, 2, 3, 4, 5, 6 };
	
	
	/**
	 * 计算请假时间
	 * 1、标准考勤制度(工作日整天数*工作小时数+工作小时数)
		(B-C1-C2)*E + (D1+D2)*8
	   2、灵活考勤制度(工作日整天数*工作小时数+工作小时数)
		(B-C2)*E + (D2)*8
	 * @param comId 团队号
	 * @param dateTimeSStr 填写时间起
	 * @param dateTimeEStr 填写时间止
	 * @param formatStr 格式化  默认 yyyy-MM-dd HH:mm
	 * @return 计算小时数
	 */
	public Double calWorkTime(Integer comId,String dateTimeSStr,String dateTimeEStr) {
		
		//毫秒级时间
		Long timeMiles = 0L;
		
		AttenceRule attenceRule = attenceService.getAttenceRule(null, comId);
		//填写时间起YMD
		String dateTimeSStrYMD = dateTimeSStr.substring(0,10);
		//填写时间止YMD
		String dateTimeEStrYMD = dateTimeEStr.substring(0,10);
		
		//经历的时间区间
		List<String> timeZones = DateTimeUtil.getTimeZones(dateTimeSStrYMD,dateTimeEStrYMD);
		
		//设定的工作时段
		List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(comId, attenceRule.getId());
		
		//有效工作时间
		Integer effectWorkHours = CommonUtil.getEffectWorkHour(listAttenceTypes);
		//取得考勤时间段
		Map<Integer, Map<String,String>> attenceTimeMap = CommonUtil.getAttentcTime(listAttenceTypes,attenceRule.getRuleType());
		
		if("1".equals(attenceRule.getRuleType())){//是标准考勤制度
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(weekNum>5){//非工作日
					timeZones.remove(dateStr);
				}
			}
			
			//第二步：移除节假日
			//取得团队国家假日
			List<FestModDate> listSysFestModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			//移除国家法定节假日
			if(null!=listSysFestModDates && !listSysFestModDates.isEmpty()){
				for (FestModDate festSysModDate : listSysFestModDates) {
					timeZones.remove(festSysModDate.getFestDate());
				}
			}
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			//移除团队定义的节假日
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festModDate : listTeamFestModDates) {
					timeZones.remove(festModDate.getFestDate());
				}
			}
			//第三步：添加加班时间
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//取得团队国家工作日
			List<FestModDate> listSysWorkModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加家国家指定的上班时间
			if(null != listSysWorkModDates && !listSysWorkModDates.isEmpty()){
				for (FestModDate festModDate : listSysWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//重新排序
			Collections.sort(timeZones);
			//第四步：计算时间
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//请假时间为一天内
						timeMiles += CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateTimeEStr);
					}else if(dateStr.equals(dateTimeSStrYMD)){//请假的开始时间
						timeMiles += CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateStr+" 23:59");
						timeMiles += 60L * 1000;
					}else if(dateStr.equals(dateTimeEStrYMD)){//请假的结束时间
						timeMiles += CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateStr+" 00:00",dateTimeEStr);
					}else{//请假为整天
						timeMiles += effectWorkHours * 60L * 60 *1000;
					}
				}
			}
			
		}else if("3".equals(attenceRule.getRuleType())){//是灵活考勤制度
			
			Set<Integer> weekNumSet = attenceTimeMap.keySet();
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(!weekNumSet.contains(weekNum)){//非工作日
					timeZones.remove(dateStr);
				}
			}
			//第二步：移除节假日
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			//移除团队定义的节假日
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festModDate : listTeamFestModDates) {
					timeZones.remove(festModDate.getFestDate());
				}
			}
			//第三步：添加加班时间
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//重新排序
			Collections.sort(timeZones);
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					//取得星期数
					Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
					
					Map<String,String> attenceMap = attenceTimeMap.get(weekNum);
					if(null == attenceMap){
						Set<Integer> set = attenceTimeMap.keySet();
						Integer[] sets = set.toArray(new Integer[set.size()]);
						List<Integer> list = Arrays.asList(sets);
						Collections.sort(list);
						attenceMap = attenceTimeMap.get(list.get(0));
					}
					
					if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//请假时间为一天内
						timeMiles += CommonUtil.getWorkHour(attenceMap,dateStr,dateTimeSStr,dateTimeEStr);
					}else if(dateStr.equals(dateTimeSStrYMD)){//请假的开始时间
						timeMiles += CommonUtil.getWorkHour(attenceMap,dateStr,dateTimeSStr,dateStr+" 23:59");
						timeMiles += 60L * 1000;
					}else if(dateStr.equals(dateTimeEStrYMD)){//请假的结束时间
						timeMiles += CommonUtil.getWorkHour(attenceMap,dateStr,dateStr+" 00:00",dateTimeEStr);
					}else{//请假为整天
						timeMiles += effectWorkHours * 60L * 60 *1000;
					}
				}
			}
		}
		Long second = timeMiles/(1000);
		int min = (int) (second/(60));
		BigDecimal bd = new BigDecimal(min / 60.0D);
		bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP); 
		
		Double result = new Double(bd.toString());
		//计算小时数
		return result;
	}
	/**
	 * 计算请假时间
	 * 1、标准考勤制度(工作日整天数*工作小时数+工作小时数)
		(B-C1-C2)*E + (D1+D2)*8
	   2、灵活考勤制度(工作日整天数*工作小时数+工作小时数)
		(B-C2)*E + (D2)*8
	 * @param comId 团队号
	 * @param dateTimeSStr 填写时间起
	 * @param dateTimeEStr 填写时间止
	 * @param formatStr 格式化  默认 yyyy-MM-dd HH:mm
	 * @return 计算小时数
	 */
	public List<AttenceDetail> constrWorkTimeForLeave(Integer comId,String dateTimeSStr,String dateTimeEStr) {
		List<AttenceDetail> resutList = new ArrayList<AttenceDetail>();
		AttenceRule attenceRule = attenceService.getAttenceRule(null, comId);
		//填写时间起YMD
		String dateTimeSStrYMD = dateTimeSStr.substring(0,10);
		//填写时间止YMD
		String dateTimeEStrYMD = dateTimeEStr.substring(0,10);
		
		//经历的时间区间
		List<String> timeZones = DateTimeUtil.getTimeZones(dateTimeSStrYMD,dateTimeEStrYMD);
		
		//设定的工作时段
		List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(comId, attenceRule.getId());
		
		//有效工作时间
		Integer effectWorkHours = CommonUtil.getEffectWorkHour(listAttenceTypes);
		//取得考勤时间段
		Map<Integer, Map<String,String>> attenceTimeMap = CommonUtil.getAttentcTime(listAttenceTypes,attenceRule.getRuleType());
		
		if("1".equals(attenceRule.getRuleType())){//是标准考勤制度
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(weekNum>5){//非工作日
					timeZones.remove(dateStr);
				}
			}
			
			//第二步：移除节假日
			//取得团队国家假日
			List<FestModDate> listSysFestModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			//移除国家法定节假日
			if(null!=listSysFestModDates && !listSysFestModDates.isEmpty()){
				for (FestModDate festSysModDate : listSysFestModDates) {
					timeZones.remove(festSysModDate.getFestDate());
				}
			}
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			//移除团队定义的节假日
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festModDate : listTeamFestModDates) {
					timeZones.remove(festModDate.getFestDate());
				}
			}
			//第三步：添加加班时间
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//取得团队国家工作日
			List<FestModDate> listSysWorkModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加家国家指定的上班时间
			if(null != listSysWorkModDates && !listSysWorkModDates.isEmpty()){
				for (FestModDate festModDate : listSysWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//重新排序
			Collections.sort(timeZones);
			//第四步：计算时间
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					Long timeMiles = 0L;
					
					String attenceStart = dateTimeSStr;
					String attenceEnd = dateTimeEStr;
					
					if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//请假时间为一天内
						timeMiles = CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateTimeEStr);
					}else if(dateStr.equals(dateTimeSStrYMD)){//请假的开始时间
						timeMiles = CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateStr+" 23:59");
						timeMiles += 60L * 1000;
						
						attenceEnd = dateStr+" 23:59";
						
					}else if(dateStr.equals(dateTimeEStrYMD)){//请假的结束时间
						timeMiles = CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateStr+" 00:00",dateTimeEStr);
						attenceStart = dateStr+" 00:00";
					}else{//请假为整天
						timeMiles = effectWorkHours * 60L * 60 *1000;
						attenceStart = dateStr+" 00:00";
						attenceEnd = dateStr+" 23:59";
					}
					
					Long second = timeMiles/(1000);
					int min = (int) (second/(60));
					BigDecimal bd = new BigDecimal(min / 60.0D);
					bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP);
					
					AttenceDetail attenceDetail = new AttenceDetail();
					attenceDetail.setAttenceStart(attenceStart);
					attenceDetail.setAttenceEnd(attenceEnd);
					attenceDetail.setAttencDate(dateStr);
					attenceDetail.setAttencHour(bd.toString());
					
					resutList.add(attenceDetail);
				}
			}
			
		}else if("3".equals(attenceRule.getRuleType())){//是灵活考勤制度
			
			Set<Integer> weekNumSet = attenceTimeMap.keySet();
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(!weekNumSet.contains(weekNum)){//非工作日
					timeZones.remove(dateStr);
				}
			}
			//第二步：移除节假日
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			//移除团队定义的节假日
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festModDate : listTeamFestModDates) {
					timeZones.remove(festModDate.getFestDate());
				}
			}
			//第三步：添加加班时间
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//重新排序
			Collections.sort(timeZones);
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					//取得星期数
					Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
					
					Map<String,String> attenceMap = attenceTimeMap.get(weekNum);
					if(null == attenceMap){
						Set<Integer> set = attenceTimeMap.keySet();
						Integer[] sets = set.toArray(new Integer[set.size()]);
						List<Integer> list = Arrays.asList(sets);
						Collections.sort(list);
						attenceMap = attenceTimeMap.get(list.get(0));
					}
					
					Long timeMiles = 0L;
					
					String attenceStart = dateTimeSStr;
					String attenceEnd = dateTimeEStr;
					
					if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//请假时间为一天内
						timeMiles = CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateTimeEStr);
					}else if(dateStr.equals(dateTimeSStrYMD)){//请假的开始时间
						timeMiles = CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateStr+" 23:59");
						timeMiles += 60L * 1000;
						
						attenceEnd = dateStr+" 23:59";
						
					}else if(dateStr.equals(dateTimeEStrYMD)){//请假的结束时间
						timeMiles = CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateStr+" 00:00",dateTimeEStr);
						attenceStart = dateStr+" 00:00";
					}else{//请假为整天
						timeMiles = effectWorkHours * 60L * 60 *1000;
						attenceStart = dateStr+" 00:00";
						attenceEnd = dateStr+" 23:59";
					}
					
					Long second = timeMiles/(1000);
					int min = (int) (second/(60));
					BigDecimal bd = new BigDecimal(min / 60.0D);
					bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP);
					
					AttenceDetail attenceDetail = new AttenceDetail();
					attenceDetail.setAttenceStart(attenceStart);
					attenceDetail.setAttenceEnd(attenceEnd);
					attenceDetail.setAttencDate(dateStr);
					attenceDetail.setAttencHour(bd.toString());
					
					resutList.add(attenceDetail);
				}
			}
		}
		return resutList;
	}
	
	/**
	 * 计算请假时间
	 * 1、标准考勤制度(工作日整天数*工作小时数+工作小时数)
		(B-C1-C2)*E + (D1+D2)*8
	   2、灵活考勤制度(工作日整天数*工作小时数+工作小时数)
		(B-C2)*E + (D2)*8
	 * @param comId 团队号
	 * @param dateTimeSStr 填写时间起
	 * @param dateTimeEStr 填写时间止
	 * @param formatStr 格式化  默认 yyyy-MM-dd HH:mm
	 * @param attenceRule 考勤规则
	 * @param listAttenceTypes 团队的工作时段
	 * @param listFestModDateOfTeam 当前团队下所有的节假日维护信息
	 * @return 计算小时数
	 */
	public Double calWorkTime(Integer comId,String dateTimeSStr,String dateTimeEStr,AttenceRule attenceRule,
			List<AttenceType> listAttenceTypes,List<FestModDate> listFestModDateOfTeam) {
		
		//毫秒级时间
		Long timeMiles = 0L;
		
		//填写时间起YMD
		String dateTimeSStrYMD = dateTimeSStr.substring(0,10);
		//填写时间止YMD
		String dateTimeEStrYMD = dateTimeEStr.substring(0,10);
		
		//经历的时间区间
		List<String> timeZones = DateTimeUtil.getTimeZones(dateTimeSStrYMD,dateTimeEStrYMD);
		
		//有效工作时间
		Integer effectWorkHours = CommonUtil.getEffectWorkHour(listAttenceTypes);
		//取得考勤时间段
		Map<Integer, Map<String,String>> attenceTimeMap = CommonUtil.getAttentcTime(listAttenceTypes,attenceRule.getRuleType());
		
		if("1".equals(attenceRule.getRuleType())){//是标准考勤制度
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(weekNum>5){//非工作日
					timeZones.remove(dateStr);
				}
			}
			
			//第二步：移除节假日
			//取得团队国家假日
			List<FestModDate> listSysFestModDates = this.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY,listFestModDateOfTeam);
			//移除国家法定节假日
			if(null!=listSysFestModDates && !listSysFestModDates.isEmpty()){
				for (FestModDate festSysModDate : listSysFestModDates) {
					timeZones.remove(festSysModDate.getFestDate());
				}
			}
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = this.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY,listFestModDateOfTeam);
			//移除团队定义的节假日
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festModDate : listTeamFestModDates) {
					timeZones.remove(festModDate.getFestDate());
				}
			}
			//第三步：添加加班时间
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = this.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY,listFestModDateOfTeam);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//取得团队国家工作日
			List<FestModDate> listSysWorkModDates = this.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY,listFestModDateOfTeam);
			//添加家国家指定的上班时间
			if(null != listSysWorkModDates && !listSysWorkModDates.isEmpty()){
				for (FestModDate festModDate : listSysWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//重新排序
			Collections.sort(timeZones);
			//第四步：计算时间
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//请假时间为一天内
						timeMiles += CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateTimeEStr);
					}else if(dateStr.equals(dateTimeSStrYMD)){//请假的开始时间
						timeMiles += CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateStr+" 23:59");
						timeMiles += 60L * 1000;
					}else if(dateStr.equals(dateTimeEStrYMD)){//请假的结束时间
						timeMiles += CommonUtil.getWorkHour(attenceTimeMap.get(0),dateStr,dateStr+" 00:00",dateTimeEStr);
					}else{//请假为整天
						timeMiles += effectWorkHours * 60L * 60 *1000;
					}
				}
			}
			
		}else if("3".equals(attenceRule.getRuleType())){//是灵活考勤制度
			
			Set<Integer> weekNumSet = attenceTimeMap.keySet();
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(!weekNumSet.contains(weekNum)){//非工作日
					timeZones.remove(dateStr);
				}
			}
			//第二步：移除节假日
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = this.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY,listFestModDateOfTeam);
			//移除团队定义的节假日
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festModDate : listTeamFestModDates) {
					timeZones.remove(festModDate.getFestDate());
				}
			}
			//第三步：添加加班时间
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = this.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY,listFestModDateOfTeam);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!timeZones.contains(festDateStr)){
						timeZones.add(festDateStr);
					}
				}
			}
			//重新排序
			Collections.sort(timeZones);
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					//取得星期数
					Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
					
					Map<String,String> attenceMap = attenceTimeMap.get(weekNum);
					if(null == attenceMap){
						Set<Integer> set = attenceTimeMap.keySet();
						Integer[] sets = set.toArray(new Integer[set.size()]);
						List<Integer> list = Arrays.asList(sets);
						Collections.sort(list);
						attenceMap = attenceTimeMap.get(list.get(0));
					}
					
					if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//请假时间为一天内
						timeMiles += CommonUtil.getWorkHour(attenceMap,dateStr,dateTimeSStr,dateTimeEStr);
					}else if(dateStr.equals(dateTimeSStrYMD)){//请假的开始时间
						timeMiles += CommonUtil.getWorkHour(attenceMap,dateStr,dateTimeSStr,dateStr+" 23:59");
						timeMiles += 60L * 1000;
					}else if(dateStr.equals(dateTimeEStrYMD)){//请假的结束时间
						timeMiles += CommonUtil.getWorkHour(attenceMap,dateStr,dateStr+" 00:00",dateTimeEStr);
					}else{//请假为整天
						timeMiles += effectWorkHours * 60L * 60 *1000;
					}
				}
			}
		}
		Long second = timeMiles/(1000);
		int min = (int) (second/(60));
		BigDecimal bd = new BigDecimal(min / 60.0D);
		bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP); 
		
		Double result = new Double(bd.toString());
		//计算小时数
		return result;
	}
	/**
	 * 计算加班时间（至多连接两天）
	 * @param comId 团队号
	 * @param dateTimeSStr 填写时间起 
	 * @param dateTimeEStr 填写时间止
	 * @param formatStr 格式化  默认 yyyy-MM-dd HH:mm
	 * @return
	 */
	public Double calOverTime(Integer comId,String dateTimeSStr,String dateTimeEStr) {
		
		//毫秒级时间
		Long timeMiles = 0L;
				
		AttenceRule attenceRule = attenceService.getAttenceRule(null, comId);
		//填写时间起YMD
		String dateTimeSStrYMD = dateTimeSStr.substring(0,10);
		//填写时间止YMD
		String dateTimeEStrYMD = dateTimeEStr.substring(0,10);
		
		//经历的时间区间
		List<String> timeZones = DateTimeUtil.getTimeZones(dateTimeSStrYMD,dateTimeEStrYMD);
		
		//设定的工作时段
		List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(comId, attenceRule.getId());
		
		//取得考勤时间段
		Map<Integer, Map<String,String>> attenceTimeMap = CommonUtil.getAttentcTime(listAttenceTypes,attenceRule.getRuleType());
				
		//有效工作时间
		Integer effectWorkHours = CommonUtil.getEffectWorkHour(listAttenceTypes);
		//工作日集合
		List<String> workSet = new ArrayList<String>();
		//假日集合
		List<String> holiSet = new ArrayList<String>();
		
		if("1".equals(attenceRule.getRuleType())){//是标准考勤制度
			
			//第一步：区分填写时段工作日和节假日
			//填写时段的工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(weekNum>5){//非工作日
					holiSet.add(dateStr);
				}else{
					workSet.add(dateStr);
				}
			}
			//第二步：区分节假日
			List<FestModDate> listSysFestModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			if(null!=listSysFestModDates && !listSysFestModDates.isEmpty()){
				for (FestModDate festSysModDate : listSysFestModDates) {
					String festDate = festSysModDate.getFestDate();
					if(!holiSet.contains(festDate)){
						holiSet.add(festDate);
					}
					workSet.remove(festDate);
					
				}
			}
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festTeamModDate : listTeamFestModDates) {
					String festDate = festTeamModDate.getFestDate();
					if(!holiSet.contains(festDate)){
						holiSet.add(festDate);
					}
					workSet.remove(festDate);
					
				}
			}
			//第三步：区分工作日
			
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!workSet.contains(festDateStr)){
						workSet.add(festDateStr);
					}
					holiSet.remove(festDateStr);
				}
			}
			//取得团队国家工作日
			List<FestModDate> listSysWorkModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			if(null != listSysWorkModDates && !listSysWorkModDates.isEmpty()){
				for (FestModDate festModDate : listSysWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!workSet.contains(festDateStr)){
						workSet.add(festDateStr);
					}
					holiSet.remove(festDateStr);
				}
			}
			//第四步：计算加班时间
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					if(workSet.contains(dateStr)){//是工作日
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为工作日一天内
							timeMiles += CommonUtil.getOverTime(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateTimeEStr);
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							timeMiles += CommonUtil.getOverTime(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateStr+" 23:59");
							timeMiles += 60L * 1000;
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							timeMiles += CommonUtil.getOverTime(attenceTimeMap.get(0),dateStr,dateStr+" 00:00",dateTimeEStr);
						}else{
							timeMiles += (24- effectWorkHours) * 60L * 60 * 1000;
						}
					}else{
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为假日一天内
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							Date endDate = DateTimeUtil.parseDate(dateStr+" 23:59", DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							timeMiles += 60L * 1000;
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							//默认开始时间为传入时间
							Date startDate = DateTimeUtil.parseDate(dateStr+" 00:00", DateTimeUtil.yyyy_MM_dd_HH_mm);
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							
						}else{
							timeMiles += 24 * 60L * 60 * 1000;
						}
						
					}
				}
			}
			
		}else if("3".equals(attenceRule.getRuleType())){//是灵活考勤制度
			
			Set<Integer> weekNumSet = attenceTimeMap.keySet();
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(!weekNumSet.contains(weekNum)){//非工作日
					holiSet.add(dateStr);
				}else{
					workSet.add(dateStr);
				}
			}
			//第二步：区分节假日
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festTeamModDate : listTeamFestModDates) {
					String festDate = festTeamModDate.getFestDate();
					if(!holiSet.contains(festDate)){
						holiSet.add(festDate);
					}
					workSet.remove(festDate);
					
				}
			}
			//第三步：区分工作日
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!workSet.contains(festDateStr)){
						workSet.add(festDateStr);
					}
					holiSet.remove(festDateStr);
				}
			}
			
			//第四步：计算加班时间
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					
					Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					//取得星期数
					Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
					
					Map<String,String> attenceMap = attenceTimeMap.get(weekNum);
					if(null == attenceMap){
						Set<Integer> set = attenceTimeMap.keySet();
						Integer[] sets = set.toArray(new Integer[set.size()]);
						List<Integer> list = Arrays.asList(sets);
						Collections.sort(list);
						attenceMap = attenceTimeMap.get(list.get(0));
					}
					
					if(workSet.contains(dateStr)){//是工作日
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为工作日一天内
							timeMiles += CommonUtil.getOverTime(attenceMap,dateStr,dateTimeSStr,dateTimeEStr);
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							timeMiles += CommonUtil.getOverTime(attenceMap,dateStr,dateTimeSStr,dateStr+" 23:59");
							timeMiles += 60L * 1000;
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							timeMiles += CommonUtil.getOverTime(attenceMap,dateStr,dateStr+" 00:00",dateTimeEStr);
						}else{
							timeMiles += (24- effectWorkHours) * 60L * 60 * 1000;
						}
					}else{
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为假日一天内
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							Date endDate = DateTimeUtil.parseDate(dateStr+" 23:59", DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							timeMiles += 60L * 1000;
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							//默认开始时间为传入时间
							Date startDate = DateTimeUtil.parseDate(dateStr+" 00:00", DateTimeUtil.yyyy_MM_dd_HH_mm);
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							
						}else{
							timeMiles += 24 * 60L * 60 * 1000;
						}
						
					}
				}
			}
			
		}
		Long second = timeMiles/(1000);
		int min = (int) (second/(60));
		BigDecimal bd = new BigDecimal(min / 60.0D);
		bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP); 
		
		Double result = new Double(bd.toString());
		//计算小时数
		return result;
	}
	/**
	 * 计算加班时间（至多连接两天）
	 * @param comId 团队号
	 * @param dateTimeSStr 填写时间起 
	 * @param dateTimeEStr 填写时间止
	 * @param formatStr 格式化  默认 yyyy-MM-dd HH:mm
	 * @return
	 */
	public List<AttenceDetail> constrWorkTimeForOverTime (Integer comId,String dateTimeSStr,String dateTimeEStr) {
		List<AttenceDetail> resutList = new ArrayList<AttenceDetail>();
		
		AttenceRule attenceRule = attenceService.getAttenceRule(null, comId);
		//填写时间起YMD
		String dateTimeSStrYMD = dateTimeSStr.substring(0,10);
		//填写时间止YMD
		String dateTimeEStrYMD = dateTimeEStr.substring(0,10);
		
		//经历的时间区间
		List<String> timeZones = DateTimeUtil.getTimeZones(dateTimeSStrYMD,dateTimeEStrYMD);
		
		//设定的工作时段
		List<AttenceType> listAttenceTypes = attenceService.listAttenceTypes(comId, attenceRule.getId());
		
		//取得考勤时间段
		Map<Integer, Map<String,String>> attenceTimeMap = CommonUtil.getAttentcTime(listAttenceTypes,attenceRule.getRuleType());
		
		//有效工作时间
		Integer effectWorkHours = CommonUtil.getEffectWorkHour(listAttenceTypes);
		//工作日集合
		List<String> workSet = new ArrayList<String>();
		//假日集合
		List<String> holiSet = new ArrayList<String>();
		
		if("1".equals(attenceRule.getRuleType())){//是标准考勤制度
			
			//第一步：区分填写时段工作日和节假日
			//填写时段的工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(weekNum>5){//非工作日
					holiSet.add(dateStr);
				}else{
					workSet.add(dateStr);
				}
			}
			//第二步：区分节假日
			List<FestModDate> listSysFestModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			if(null!=listSysFestModDates && !listSysFestModDates.isEmpty()){
				for (FestModDate festSysModDate : listSysFestModDates) {
					String festDate = festSysModDate.getFestDate();
					if(!holiSet.contains(festDate)){
						holiSet.add(festDate);
					}
					workSet.remove(festDate);
					
				}
			}
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festTeamModDate : listTeamFestModDates) {
					String festDate = festTeamModDate.getFestDate();
					if(!holiSet.contains(festDate)){
						holiSet.add(festDate);
					}
					workSet.remove(festDate);
					
				}
			}
			//第三步：区分工作日
			
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!workSet.contains(festDateStr)){
						workSet.add(festDateStr);
					}
					holiSet.remove(festDateStr);
				}
			}
			//取得团队国家工作日
			List<FestModDate> listSysWorkModDates = festModDao.listFestModDates(CommonConstant.SYSCOMID,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			if(null != listSysWorkModDates && !listSysWorkModDates.isEmpty()){
				for (FestModDate festModDate : listSysWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!workSet.contains(festDateStr)){
						workSet.add(festDateStr);
					}
					holiSet.remove(festDateStr);
				}
			}
			//第四步：计算加班时间
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					//毫秒级时间
					Long timeMiles = 0L;
					
					String attenceStart = dateTimeSStr;
					String attenceEnd = dateTimeEStr;
					
					if(workSet.contains(dateStr)){//是工作日
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为工作日一天内
							timeMiles += CommonUtil.getOverTime(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateTimeEStr);
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							timeMiles += CommonUtil.getOverTime(attenceTimeMap.get(0),dateStr,dateTimeSStr,dateStr+" 23:59");
							timeMiles += 60L * 1000;
							attenceEnd = dateStr+" 23:59";
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							timeMiles += CommonUtil.getOverTime(attenceTimeMap.get(0),dateStr,dateStr+" 00:00",dateTimeEStr);
							
							attenceStart = dateStr+" 00:00";
						}else{
							timeMiles += (24- effectWorkHours) * 60L * 60 * 1000;
							
							attenceStart = dateStr+" 00:00";
							attenceEnd = dateStr+" 23:59";
						}
					}else{
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为假日一天内
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							Date endDate = DateTimeUtil.parseDate(dateStr+" 23:59", DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							timeMiles += 60L * 1000;
							
							attenceEnd = dateStr+" 23:59";
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							//默认开始时间为传入时间
							Date startDate = DateTimeUtil.parseDate(dateStr+" 00:00", DateTimeUtil.yyyy_MM_dd_HH_mm);
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							
							attenceStart = dateStr+" 00:00";
							
						}else{
							timeMiles += 24 * 60L * 60 * 1000;
							
							attenceStart = dateStr+" 00:00";
							attenceEnd = dateStr+" 23:59";
						}
						
					}
					
					
					Long second = timeMiles/(1000);
					int min = (int) (second/(60));
					BigDecimal bd = new BigDecimal(min / 60.0D);
					bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP); 
					
					AttenceDetail attenceDetail = new AttenceDetail();
					attenceDetail.setAttenceStart(attenceStart);
					attenceDetail.setAttenceEnd(attenceEnd);
					attenceDetail.setAttencDate(dateStr);
					attenceDetail.setAttencHour(bd.toString());
					
					resutList.add(attenceDetail);
					
				}
			}
			
		}else if("3".equals(attenceRule.getRuleType())){//是灵活考勤制度
			
			Set<Integer> weekNumSet = attenceTimeMap.keySet();
			//第一步：清理非工作日
			List<String> timeZonesTemmp = new ArrayList<String>();
			timeZonesTemmp.addAll(timeZones);
			for (String dateStr : timeZonesTemmp) {
				
				Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				//取得星期数
				Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
				if(!weekNumSet.contains(weekNum)){//非工作日
					holiSet.add(dateStr);
				}else{
					workSet.add(dateStr);
				}
			}
			//第二步：区分节假日
			//取得团队自定义假日
			List<FestModDate> listTeamFestModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.HOLIDAY);
			if(null!=listTeamFestModDates && !listTeamFestModDates.isEmpty()){
				for (FestModDate festTeamModDate : listTeamFestModDates) {
					String festDate = festTeamModDate.getFestDate();
					if(!holiSet.contains(festDate)){
						holiSet.add(festDate);
					}
					workSet.remove(festDate);
					
				}
			}
			//第三步：区分工作日
			//取得团队自定义工作日
			List<FestModDate> listTeamWorkModDates = festModDao.listFestModDates(comId,dateTimeSStrYMD,
					dateTimeEStrYMD,CommonConstant.WORKDAY);
			//添加自定义工作时间
			if(null != listTeamWorkModDates && !listTeamWorkModDates.isEmpty()){
				for (FestModDate festModDate : listTeamWorkModDates) {
					String festDateStr = festModDate.getFestDate();
					if(!workSet.contains(festDateStr)){
						workSet.add(festDateStr);
					}
					holiSet.remove(festDateStr);
				}
			}
			
			//第四步：计算加班时间
			//还有剩下的时间区间
			if(null!=timeZones && !timeZones.isEmpty()){
				for (String dateStr : timeZones) {
					
					Date date = DateTimeUtil.parseDate(dateStr, DateTimeUtil.yyyy_MM_dd);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					//取得星期数
					Integer weekNum = WEEKS[DateTimeUtil.getDay(cal)];
					
					Map<String,String> attenceMap = attenceTimeMap.get(weekNum);
					if(null == attenceMap){
						Set<Integer> set = attenceTimeMap.keySet();
						Integer[] sets = set.toArray(new Integer[set.size()]);
						List<Integer> list = Arrays.asList(sets);
						Collections.sort(list);
						attenceMap = attenceTimeMap.get(list.get(0));
					}
					//毫秒级时间
					Long timeMiles = 0L;
					
					String attenceStart = dateTimeSStr;
					String attenceEnd = dateTimeEStr;
					
					
					if(workSet.contains(dateStr)){//是工作日
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为工作日一天内
							timeMiles += CommonUtil.getOverTime(attenceMap,dateStr,dateTimeSStr,dateTimeEStr);
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							timeMiles += CommonUtil.getOverTime(attenceMap,dateStr,dateTimeSStr,dateStr+" 23:59");
							timeMiles += 60L * 1000;
							
							attenceEnd = dateStr+" 23:59";
							
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							timeMiles += CommonUtil.getOverTime(attenceMap,dateStr,dateStr+" 00:00",dateTimeEStr);
							
							attenceStart = dateStr+" 00:00";
						}else{
							timeMiles += (24- effectWorkHours) * 60L * 60 * 1000;
							
							attenceStart = dateStr+" 00:00";
							attenceEnd = dateStr+" 23:59";
						}
					}else{
						if(dateStr.equals(dateTimeSStrYMD) && dateStr.equals(dateTimeEStrYMD)){//加班时间为假日一天内
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
						}else if(dateStr.equals(dateTimeSStrYMD)){//加班的开始时间
							
							//默认开始时间为传入时间
							String startDateStr = dateTimeSStr;
							Date startDate = DateTimeUtil.parseDate(startDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							
							//默认结束时间为传入时间
							Date endDate = DateTimeUtil.parseDate(dateStr+" 23:59", DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							timeMiles += 60L * 1000;
							
							attenceEnd = dateStr+" 23:59";
						}else if(dateStr.equals(dateTimeEStrYMD)){//加班的结束时间
							//默认开始时间为传入时间
							Date startDate = DateTimeUtil.parseDate(dateStr+" 00:00", DateTimeUtil.yyyy_MM_dd_HH_mm);
							//默认结束时间为传入时间
							String endDateStr = dateTimeEStr;
							Date endDate = DateTimeUtil.parseDate(endDateStr, DateTimeUtil.yyyy_MM_dd_HH_mm);
							timeMiles += endDate.getTime() - startDate.getTime();
							
							attenceStart = dateStr+" 00:00";
							
						}else{
							timeMiles += 24 * 60L * 60 * 1000;
							
							attenceStart = dateStr+" 00:00";
							attenceEnd = dateStr+" 23:59";
						}
						
					}
					
					
					Long second = timeMiles/(1000);
					int min = (int) (second/(60));
					BigDecimal bd = new BigDecimal(min / 60.0D);
					bd  =   bd.setScale(1,BigDecimal.ROUND_HALF_UP); 
					
					AttenceDetail attenceDetail = new AttenceDetail();
					attenceDetail.setAttenceStart(attenceStart);
					attenceDetail.setAttenceEnd(attenceEnd);
					attenceDetail.setAttencDate(dateStr);
					attenceDetail.setAttencHour(bd.toString());
					
					resutList.add(attenceDetail);
				}
			}
			
		}
		
		//计算小时数
		return resutList;
	}
	/**
	 * 添加节假日
	 * @param festMod
	 */
	public void addFestMod(FestMod festMod,Boolean isSystem) {
		
		if(isSystem){//是系统自动
			Integer festModId = festModDao.add(festMod);
			List<FestModDate> listFestModDates = festMod.getListFestModDates();
			if(null!=listFestModDates && !listFestModDates.isEmpty() ){
				for (FestModDate festModDate : listFestModDates) {
					festModDate.setFestModId(festModId);
					festModDao.add(festModDate);
				}
			}
		}
	}
	/**
	 * 验证本年是否已同步
	 * @param comId 团队号
	 * @param year 年度
	 * @return
	 */
	public Boolean checkSysFestDate(Integer comId,Integer year) {
		List<FestMod> listFestMod = festModDao.listYearFestMod(comId,year);
		return !(null==listFestMod || listFestMod.isEmpty());
	}
	/**
	 * 列表查询节假日
	 * @param sessionUser 当前操作人员
	 * @param festMod 带条件的节假日
	 * @param ruleType 采用的考情制度 1标准考勤 3灵活考勤
	 * @return
	 */
	public List<FestMod> listFestMod(UserInfo sessionUser, FestMod festMod,
			String ruleType) {
		List<FestMod> result = new ArrayList<FestMod>();
		
		List<FestMod> list = festModDao.listFestMod(sessionUser,festMod,ruleType);
		if(null!=list && !list.isEmpty()){
			String states = festMod.getStatus();
			for (FestMod festModObj : list) {
				List<FestModDate> listFestModDates = festModDao.listFestModDates(festModObj.getId(), states);
				if(null!=listFestModDates && !listFestModDates.isEmpty()){
					festModObj.setDayTimeS(listFestModDates.get(0).getFestDate());
					festModObj.setDayTimeE(listFestModDates.get(listFestModDates.size()-1).getFestDate());
					result.add(festModObj);
				}
			}
		}
		
		return result;
	}
	/**
	 * 判断日期是否是节假日日期
	 * @param comId
	 * @param ruleType
	 * @param date
	 * @return
	 */
	public String isWorkDay(Integer comId,String ruleType,String date) {
		FestModDate festDateStatus = festModDao.festDateStatus(comId, date,ruleType);
		if(null!= festDateStatus){
			return festDateStatus.getStatus()+"";
		}else{
			return null;
		}
	}
	/**
	 * 添加节日维护
	 * @param festMod
	 * @param sessionUser
	 */
	public void addFestMod(FestMod festMod, UserInfo sessionUser) {
		
		Integer status = Integer.parseInt(festMod.getStatus());
		
		//时间起
		String dayTimeS = festMod.getDayTimeS();
		//时间止
		String dayTimeE = festMod.getDayTimeE();
		
		String yearS = dayTimeS.substring(0,4);
		//设置时间
		festMod.setFestival(dayTimeS);
		//设置团队号
		festMod.setComId(sessionUser.getComId());
		//设置年份
		festMod.setYear(Integer.parseInt(yearS));
		Integer festModId = 0;
		if(1 == status){//休假
			festMod.setFestName("休假");
			festModId = festModDao.add(festMod);
		}else if(2 == status){//上班
			festMod.setFestName("上班");
			
			festModId = festModDao.add(festMod);
		}
		
		List<String> timeZones = DateTimeUtil.getTimeZones(dayTimeS, dayTimeE);
		if(null != timeZones){
			for (String festDate : timeZones) {
				//节假日日期
				FestModDate festModDate = new FestModDate();
				
				//设置团队号
				festModDate.setComId(sessionUser.getComId());
				//设置节日时间
				festModDate.setFestDate(festDate);
				//设置节假日维护主键
				festModDate.setFestModId(festModId);
				//设置1休息日 2工作日
				festModDate.setStatus(status);
				
				//添加数据
				festModDao.add(festModDate);
			}
		}
	}
	/**
	 * 取得节假日维护信息
	 * @param festModId
	 * @return
	 */
	public FestMod getFestModById(Integer festModId){
		
		FestMod festMod = (FestMod) festModDao.objectQuery(FestMod.class, festModId);
		
		List<FestModDate> listFestModDates = festModDao.listFestModDates(festModId, null);
		if(null!=listFestModDates && !listFestModDates.isEmpty()){
			festMod.setDayTimeS(listFestModDates.get(0).getFestDate());
			festMod.setDayTimeE(listFestModDates.get(listFestModDates.size()-1).getFestDate());
			festMod.setStatus(listFestModDates.get(0).getStatus().toString());
		}
		return festMod;
		
	}
	
	/**
	 * 修改节假日维护
	 * @param festMod
	 * @param sessionUser
	 */
	public void updateFestMod(FestMod festMod, UserInfo sessionUser){
		Integer festModId = festMod.getId();
		Integer comId = sessionUser.getComId();
		
		festModDao.delByField("festModDate", new String[]{"comId","festModId"}, 
				new Object[]{comId,festModId});
		
		Integer status = Integer.parseInt(festMod.getStatus());
		
		//时间起
		String dayTimeS = festMod.getDayTimeS();
		//时间止
		String dayTimeE = festMod.getDayTimeE();
		
		String yearS = dayTimeS.substring(0,4);
		//设置时间
		festMod.setFestival(dayTimeS);
		//设置团队号
		festMod.setComId(sessionUser.getComId());
		//设置年份
		festMod.setYear(Integer.parseInt(yearS));
		
		if(1 == status){//休假
			festMod.setFestName("休假");
			festModDao.update(festMod);
		}else if(2 == status){//上班
			festMod.setFestName("上班");
			festModDao.update(festMod);
		}
		
		List<String> timeZones = DateTimeUtil.getTimeZones(dayTimeS, dayTimeE);
		if(null != timeZones){
			for (String festDate : timeZones) {
				//节假日日期
				FestModDate festModDate = new FestModDate();
				
				//设置团队号
				festModDate.setComId(sessionUser.getComId());
				//设置节日时间
				festModDate.setFestDate(festDate);
				//设置节假日维护主键
				festModDate.setFestModId(festModId);
				//设置1休息日 2工作日
				festModDate.setStatus(status);
				
				//添加数据
				festModDao.add(festModDate);
			}
		}
	}
	
	/**
	 * 删除节假日维护
	 * @param festModId
	 * @param sessionUser
	 */
	public void delFestMod(Integer festModId,UserInfo sessionUser){
		Integer comId = sessionUser.getComId();
		
		//删除节假日日期
		festModDao.delByField("festModDate", new String[]{"comId","festModId"}, 
				new Object[]{comId,festModId});
		//删除节假日维护
		festModDao.delById(FestMod.class, festModId);
		
	}
	
	/**
	 * 获取当前团队下所有的节假日维护信息
	 * @param comId 当前团队主键
	 * @return
	 */
	public  List<FestModDate> listFestModDateOfTeam(Integer comId){
		return festModDao.listFestModDates(comId);
	}
	
	/**
	 * 取得指定时间区间的节假日
	 * @param comId 团队号
	 * @param dateTimeSStrYMD 时间起
	 * @param dateTimeEStrYMD 时间止
	 * @param status 1休息日 2工作日
	 * @param listFestModDate 当前团队下所有的节假日维护信息
	 * @return
	 */
	private List<FestModDate> listFestModDates(Integer comId,String dateTimeSStrYMD, String dateTimeEStrYMD, Integer status,
			List<FestModDate> listFestModDate){
		List<FestModDate> cloneList = new ArrayList<FestModDate>();  
		cloneList.addAll(listFestModDate);//集合拷贝
		Date sdate = DateTimeUtil.parseDate(dateTimeSStrYMD,DateTimeUtil.yyyy_MM_dd);//比对开始日期
		Date edate = DateTimeUtil.parseDate(dateTimeEStrYMD,DateTimeUtil.yyyy_MM_dd);//比对结束日期
		//获取符合条件的集合
		if(!CommonUtil.isNull(cloneList)){
			Iterator<FestModDate> it = cloneList.iterator();
			while(it.hasNext()) {  
				FestModDate festModDate = it.next();  
				Date cdate = DateTimeUtil.parseDate(festModDate.getFestDate(),DateTimeUtil.yyyy_MM_dd);//比对日期
				if(!(cdate.getTime() >= sdate.getTime() && cdate.getTime() <= edate.getTime() 
						&& status.equals(festModDate.getStatus()) && comId.equals(festModDate.getComId()))){//取反满足条件的
					//cloneList.remove(festModDate);//移除不满足条件的
					it.remove();
				}
			}
		}
		return cloneList;
	} 
	
}
