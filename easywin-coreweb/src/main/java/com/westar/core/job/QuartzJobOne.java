/**
 * 
 */
package com.westar.core.job;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.StatefulMethodInvokingJob;
import org.springframework.stereotype.Component;

import com.westar.base.model.Clock;
import com.westar.base.util.CommonUtil;
import com.westar.core.service.ClockService;

/**
 * @author jl_love
 *
 * 2011-10-14
 * 豁达，坚强，勤奋
 */
@Component
public class QuartzJobOne extends StatefulMethodInvokingJob  {
	
	private static ClockService clockService;
	
	public static void setClockService(ClockService clockService) {
		QuartzJobOne.clockService = clockService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		
		String name = jobDetail.getName();
		
		String group = jobDetail.getGroup();
		
		Clock clock = (Clock) jobDetail.getJobDataMap().get(name);
		
		//周期类型
		String clockRepType = clock.getClockRepType();
		//取得闹铃下次执行时间
		String nextStartDate = CommonUtil.getClockNextStartDate(clock.getClockNextDate(),clockRepType,
				clock.getClockRepDate(),clock.getClockTime());
	
		clock.setClockNextDate(nextStartDate);
		if("0".equals(clockRepType)){//已执行
			clock.setExecuteState("2");
		}else{//下次未执行
			clock.setExecuteState("0");
		}
		//闹铃
		clockService.updateClock(clock,"done");
		
		QuartzManager.disableSchedule(name,group);
	}
}
