package com.westar.core.job;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westar.base.cons.CommonConstant;
import com.westar.base.model.FestMod;
import com.westar.base.model.FestModDate;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.HttpUtil;
import com.westar.base.util.PublicConfig;
import com.westar.core.service.FestModService;
@Component
public class FestivalJob {
	
	@Autowired
	FestModService festModService;
	
	private static final Logger logger = Logger.getRootLogger();
	
	private static final String APPKEY = PublicConfig.HOLIDAY_URL.get("holiday.appKey");  
	private static final String MONTHURL = PublicConfig.HOLIDAY_URL.get("holiday.month.url");  
	
	
	/**
	 * 启动5秒后执行，每月1日午0:10触发
	 */
	//@Scheduled(initialDelay = 5000, fixedDelay = 28*24*60*60*1000)
	@Scheduled(cron="0 10 0 1 * ?")
	public void autoDownHoliday() {
		try {
			logger.info("同步国家节假日.....................");
			Integer year = DateTimeUtil.getYear();
			
			Boolean falgThisYear = festModService.checkSysFestDate(CommonConstant.SYSCOMID,year);
			if(falgThisYear){//本年已同步
				Integer month = DateTimeUtil.getMonth();
				if(month>=6){//每年7月份后
					Boolean falgNextYear = festModService.checkSysFestDate(CommonConstant.SYSCOMID,year+1);
					if(!falgNextYear){
						this.initSySFestival(year+1);
					}
				}
			}else{
				//同步今年的
				this.initSySFestival(year);
				
				Integer month = DateTimeUtil.getMonth();
				if(month>=6){//每年7月份后
					Boolean falgNextYear = festModService.checkSysFestDate(CommonConstant.SYSCOMID,year+1);
					if(!falgNextYear){
						this.initSySFestival(year+1);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化年度节假日
	 * @param year
	 */
	private void initSySFestival(Integer year) {
		Set<String> festivalSet = new HashSet<String>();
		for(int month = 1;month <= 12;month++){
			Map<String,Object> params = new HashMap<String,Object>();//请求参数
			params.put("key", APPKEY);
			params.put("year-month", year+"-"+month);
			try {
				String resultStr = HttpUtil.net(MONTHURL, params, "GET");
				JSONObject infoJson = JSONObject.parseObject(resultStr);
				Integer error_code = (Integer) infoJson.get("error_code");
				if(error_code==0){
					JSONObject result = (JSONObject) infoJson.get("result");
					JSONObject data =  (JSONObject) result.get("data");
					JSONArray holiday = data.getJSONArray("holiday");
					for (Object festObj : holiday) {
						JSONObject festJsonObj = (JSONObject) festObj;
						String festival = (String) festJsonObj.get("festival");
						if(!festivalSet.contains(festival)){//不包含该节日
							festivalSet.add(festival);
							
							String name = (String) festJsonObj.get("name");
							String desc = (String) festJsonObj.get("desc");
							
							FestMod festMod = new FestMod();
							festMod.setComId(CommonConstant.SYSCOMID);
							festMod.setDescribe(desc);
							
							SimpleDateFormat dateFormatB = new SimpleDateFormat("yyyy-M-d");
							Date dateB= dateFormatB.parse(festival);
							
							festMod.setFestival(DateTimeUtil.formatDate(dateB, DateTimeUtil.yyyy_MM_dd));
							festMod.setFestName(name);
							festMod.setYear(year);
							
							List<FestModDate> listFestModDates = new ArrayList<FestModDate>();
							
							JSONArray list = festJsonObj.getJSONArray("list");
							for (Object dateObj : list) {
								JSONObject dateJsonObj = (JSONObject) dateObj;
								
								String date =  (String) dateJsonObj.get("date");
								String status =  (String) dateJsonObj.get("status");
								
								FestModDate festModDate = new FestModDate();
								festModDate.setComId(CommonConstant.SYSCOMID);
								
								SimpleDateFormat dateFormatA = new SimpleDateFormat("yyyy-M-d");
								Date dateA= dateFormatA.parse(date);
								
								festModDate.setFestDate(DateTimeUtil.formatDate(dateA, DateTimeUtil.yyyy_MM_dd));
								festModDate.setStatus(Integer.parseInt(status));
								
								festivalSet.add(date);
								
								listFestModDates.add(festModDate);
							}
							
							festMod.setListFestModDates(listFestModDates);
							
							festModService.addFestMod(festMod,true);
						}else{
							if(month==1){//1月份没有节日，则没有出结果数据，不用在进行了
								break;
							}else{
								continue;
							}
						}
					}
				}else{
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
}
	
