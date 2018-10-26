package com.westar.core.web;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.westar.base.model.BandMAC;
import com.westar.base.model.Clock;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.File2swfUtil;
import com.westar.base.util.FileUtil;
import com.westar.base.util.JpushUtils;
import com.westar.base.util.MACUtil;
import com.westar.core.job.FestivalJob;
import com.westar.core.job.QuartzJobOne;
import com.westar.core.service.BandMACService;
import com.westar.core.service.BiaoQingService;
import com.westar.core.service.ClockService;
import com.westar.core.service.DataDicService;
import com.westar.core.service.JpushRegisteService;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getRootLogger();
	
	public static final String SYSTEM_STARUP_TIME = DateTimeUtil.formatDate(new Date(), DateTimeUtil.yyyyMMddHHmmss);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			// 从Spring容器中获得ServiceBean
			WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
			DataDicService dataDicService = webAppContext.getBean(DataDicService.class);
			
			ClockService clockService = webAppContext.getBean(ClockService.class);
			QuartzJobOne.setClockService(clockService);
			
			JpushRegisteService jpushRegisteService = webAppContext.getBean(JpushRegisteService.class);
			JpushUtils.setJpushRegisteService(jpushRegisteService);
			
			String nowTime = DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
			//取得在服务器关闭的时候未来得及执行的定时提醒（不考虑只执行一次的）
			List<Clock> listClocks = clockService.listClockUndone(nowTime);
			if(null!=listClocks && !listClocks.isEmpty()){//在服务器关闭期间有未执行的
				for (Clock clock : listClocks) {
					
					if(ConstantInterface.CLOCK_EXESTATE_NO.equals(clock.getExecuteState())//闹铃未执行
							||ConstantInterface.CLOCK_EXESTATE_TODO.equals(clock.getExecuteState()) ){//一直没有执行的或是待执行的
						//最近一次执行时间
						String clockNextDate = CommonUtil.getRecentClockDate(clock.getClockDate(),clock.getClockTime(),clock.getClockRepType(),clock.getClockRepDate());
						clock.setClockNextDate(clockNextDate);
						clock.setExecuteState(ConstantInterface.CLOCK_EXESTATE_NO);
						//修改闹铃并发起提醒
						clockService.updateClock(clock, "done");
					}
				}
			}
			//重置在服务器关闭期间有未执行的
			clockService.updateResetClock();
			// 项目路径
			FileUtil.setRootPath(this.getServletConfig().getServletContext().getRealPath("/"));
			
			logger.info("系统检测MAC开始.....................");
			BandMACService bandMACService = webAppContext.getBean(BandMACService.class);
			BandMAC bandMAC = bandMACService.queryBandMAC();
			MACUtil.setBandMAC(bandMAC);
			logger.info("系统检测MAC结束.....................");

			logger.info("系统初始化开始.....................");
			logger.info("初始化字典表.....................");
			dataDicService.initDataDic();
			// dataDicService.initDataDicContext();
			logger.info("初始化系统表情包.....................");
			//初始化系统表情包
			BiaoQingService biaoQingService = webAppContext.getBean(BiaoQingService.class);
			biaoQingService.initBiaoQing();
			
			
			//初始化节假日
			FestivalJob festivalJob = webAppContext.getBean(FestivalJob.class);
			festivalJob.autoDownHoliday();
			
			//检测团队服务配置
//			OrganicServiceCheckJob organicServiceCheckJob = webAppContext.getBean(OrganicServiceCheckJob.class);
//			organicServiceCheckJob.organicServiceCheck();
			
			//初始化文件转换工具
//			File2swfUtil.init();
			
			Runtime.getRuntime().gc();
			logger.info("系统初始化结束.....................");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
