package com.westar.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import flex.messaging.io.ArrayList;

/**
 * 日期时间工具类
 *
 */
public class DateTimeUtil {

	private static final Logger logger = Logger.getLogger(DateTimeUtil.class);

	public static final String[] DAY_NAMES = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	
	/**
	 * yyyy-MM-dd
	 */
	public static int yyyy_MM_dd = 0;
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static int yyyy_MM_dd_HH_mm_ss = 1;
	/**
	 * HH:mm:ss
	 */
	public static int HH_mm_ss = 2;
	/**
	 * yyyyMMddHHmmss
	 */
	public static int yyyyMMddHHmmss = 3;
	/**
	 * yyyyMMddHHmmssSS
	 */
	public static int yyyyMMddHHmmssSSS = 4;
	/**
	 * yyyy年MM月dd日
	 */
	public static int c_yyyy_MM_dd_ = 5;
	/**
	 * yyyy年MM月dd日HH时mm分ss秒
	 */
	public static int c_yyyy_MM_dd_HH_mm_ss_ = 6;
	/**
	 * yyyy
	 */
	public static int yyyy = 7;
	/**
	 * MM
	 */
	public static int MM = 8;
	/**
	 * dd
	 */
	public static int dd = 9;
	/**
	 * yyyyMMdd
	 */
	public static int yyyyMMdd = 10;
	/**
	 * yyyy-MM
	 */
	public static int yyyy_MM = 11;
	/**
	 * yyyy年MM月dd日 EE
	 */
	public static int c_yyyy_MM_dd_EE = 12;
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static int x_yyyy_MM_dd_HH_mm_ss = 13;

	/**
	 * HHmmss
	 */
	public static int HHmmss = 14;

	/**
	 * HHmmssSS
	 */
	public static int HHmmssSSS = 15;
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static int yyyy_MM_dd_HH_mm = 16;
	/**
	 * yyyy年MM月dd日HH时mm分
	 */
	public static int c_yyyy_MM_dd_HH_mm = 17;

	/**
	 * 格式化日期样式
	 * @param caseNum
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(int caseNum) {
		SimpleDateFormat dateFormat = null;
		switch (caseNum) {
		case 0:
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 1:
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 2:
			dateFormat = new SimpleDateFormat("HH:mm:ss");
			break;
		case 3:
			dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			break;
		case 4:
			dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			break;
		case 10:
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			break;
		case 5:
			dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			break;
		case 6:
			dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
			break;
		case 7:
			dateFormat = new SimpleDateFormat("yyyy");
			break;
		case 8:
			dateFormat = new SimpleDateFormat("MM");
			break;
		case 9:
			dateFormat = new SimpleDateFormat("dd");
			break;
		case 11:
			dateFormat = new SimpleDateFormat("yyyy-MM");
			break;
		case 12:
			dateFormat = new SimpleDateFormat("yyyy年MM月dd日 EE");
			break;
		case 13:
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			break;
		case 14:
			dateFormat = new SimpleDateFormat("HHmmss");
			break;
		case 15:
			dateFormat = new SimpleDateFormat("HHmmssSSS");
			break;
		case 16:
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break;
		case 17:
			dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			break;
		default:
			break;
		}
		return dateFormat;
	}

	/**
	 * 把日期转换为字符串
	 * @param date
	 * @param caseNum
	 * @return
	 */
	public static String formatDate(Date date, int caseNum) {
		String dateStr = null;
		SimpleDateFormat dateFormat = getDateFormat(caseNum);
		dateStr = dateFormat.format(date);
		return dateStr;
	}

	/**
	 * 日期字符串转Date
	 * @param caseNum 日期格式标识
	 * @param dateTime 需转换的日期字符串
	 * @return
	 */
	public static Date parseDate(String dateStr, int caseNum) {
		Date date = null;
		try {
			SimpleDateFormat dateFormat = getDateFormat(caseNum);
			date = dateFormat.parse(dateStr);
		} catch (Exception e) {
			logger.error("parseDate error:", e);
		}
		return date;
	}

	/**
	 * 获取当前时间的字符串形式
	 * @param caseNum
	 * @return
	 * @throws Exception
	 */
	public static String getNowDateStr(int caseNum) {
		Date date = new Date();
		SimpleDateFormat dateFormat = getDateFormat(caseNum);
		String dateStr = dateFormat.format(date);
		return dateStr;
	}
	/**
	 * 获取明天时间的字符串形式
	 * @param caseNum
	 * @return
	 * @throws Exception
	 */
	public static String getTomDateStr(int caseNum) {
		Date date = calendar2Date(tomorrow());
		SimpleDateFormat dateFormat = getDateFormat(caseNum);
		String dateStr = dateFormat.format(date);
		return dateStr;
	}

	/**
	 * 得到年份
	 * @param c
	 * @return
	 */
	public static int getYear(Calendar c) {
		return c.get(Calendar.YEAR);
	}

	/**
	 * 得到当前年份
	 * @return
	 */
	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 得到月份
	 * @param c
	 * @return
	 */
	public static int getMonth(Calendar c) {
		return c.get(Calendar.MONTH);
	}

	/**
	 * 得到当前月份
	 * @return
	 */
	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	/**
	 * 得到日
	 * @param c
	 * @return
	 */
	public static int getDate(Calendar c) {
		return c.get(Calendar.DATE);
	}

	/**
	 * 得到当前日
	 * @return
	 */
	public static int getDate() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * 得到星期
	 * @param c
	 * @return
	 */
	public static int getDay(Calendar c) {
		c.setFirstDayOfWeek(Calendar.MONDAY); 
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 得到当前星期
	 * @return
	 */
	public static int getDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}
	public static int getMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 得到星期
	 * @param c
	 * @return
	 */
	public static String getChineseDay(Calendar c) {
		return DAY_NAMES[getDay(c) - 1];
	}

	/**
	 * 得到当前星期（中文）
	 * @return
	 */
	public static String getChineseDay() {
		return DAY_NAMES[getDay() - 1];
	}

	/**
	 * 得到小时
	 * @param c
	 * @return
	 */
	public static int getHour(Calendar c) {
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到当前小时
	 * @return
	 */
	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	/**
	 * 得到当前小时
	 * @return
	 */
	public static String getHourStr() {
		int hour = getHour()+1;
		if(hour<10){
			return "0"+hour;
		}
		return ""+hour;
	}

	/**
	 * 昨天
	 * @param c
	 * @return
	 */
	public static Calendar yesterday(Calendar c) {
		long offset = 1 * 24 * 60 * 60 * 1000;
		c.setTimeInMillis(c.getTimeInMillis() - offset);
		return c;
	}

	/**
	 * 当前时间的昨天
	 * @return
	 */
	public static Calendar yesterday() {
		long offset = 1 * 24 * 60 * 60 * 1000;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(c.getTimeInMillis() - offset);
		return c;
	}

	/**
	 * 明天
	 * @param c
	 * @return
	 */
	public static Calendar tomorrow(Calendar c) {
		long offset = 1 * 24 * 60 * 60 * 1000;
		c.setTimeInMillis(c.getTimeInMillis() + offset);
		return c;
	}

	/**
	 * 当前时间的明天
	 * @return
	 */
	public static Calendar tomorrow() {
		long offset = 1 * 24 * 60 * 60 * 1000;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(c.getTimeInMillis() + offset);
		return c;
	}

	/**
	 * Date类型转换到Calendar类型
	 * @param d
	 * @return
	 */
	public static Calendar date2Calendar(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	/**
	 * Calendar类型转换到Date类型
	 * @param c
	 * @return
	 */
	public static Date calendar2Date(Calendar c) {
		return c.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int daysBetweenDate(Date startDate, Date endDate) {
		Long interval = endDate.getTime() - startDate.getTime();
		interval = interval / (24 * 60 * 60 * 1000);
		return interval.intValue();
	}

	/**
	 * 日期加减
	 * @param date 当前时间
	 * @param formatCaseNum 格式
	 * @param calendarType 标识符
	 * gc.add(1,-1)表示年份减一.
	 * gc.add(2,-1)表示月份减一.
	 * gc.add(3.-1)表示周减一.
	 * gc.add(5,-1)表示天减一.
	 * @param num 变量
	 * @return
	 * @throws Exception
	 */
	static public String addDate(String date, int formatCaseNum, int calendarType, int num) {
		SimpleDateFormat format = null;
		Calendar calendar = Calendar.getInstance();
		try {
			format = getDateFormat(formatCaseNum);
			calendar.setTime(format.parse(date));
			calendar.add(calendarType, num);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format.format(calendar.getTime());
	}

	/**
	 * 判断是否是周末
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		if (day == 1 || day == 7) {
			return true;
		}
		return false;
	}

	/**
	 * 给定日期是第几季度
	 * @param date
	 * @return
	 */
	public static int getQuarter(Date date) {
		int quarter = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = getMonth(c);
		if (month >= 0 && month <= 2) {
			quarter = 1;
		} else if (month >= 3 && month <= 5) {
			quarter = 2;
		} else if (month >= 6 && month <= 8) {
			quarter = 3;
		} else if (month >= 9 && month <= 11) {
			quarter = 4;
		}
		return quarter;
	}

	/**
	 * 判断某个日期是否在某个日期范围 
	 * @param beginDate
	 * @param endDate
	 * @param src
	 * @return
	 */
	public static boolean isBetween(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}
	
	/**
	 * 取得一年中的第几周
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Integer getWeekOfYear(String dateStr,int formatCaseNum) throws ParseException{
		SimpleDateFormat format = getDateFormat(formatCaseNum);  
		Calendar cal=Calendar.getInstance(); 
		cal.setFirstDayOfWeek(Calendar.MONDAY); 
		cal.setTime(format.parse(dateStr));
		Integer weekNum = cal.get(Calendar.WEEK_OF_YEAR);
		return weekNum;
	}
	/**
	 * 取得一周的第一天
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getFirstDayOfWeek(String dateStr,int formatCaseNum) throws ParseException{ 
		SimpleDateFormat format = getDateFormat(formatCaseNum);
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(format.parse(dateStr));
		Integer weekNum = cal.get(Calendar.DAY_OF_WEEK)-1;
		if (0 == weekNum) {  
			weekNum = 7;  
        } 
		cal.add(Calendar.DATE, 1-weekNum);
        return format.format(cal.getTime());
	} 
	/**
	 * 取得一周的最后一天
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDayOfWeek(String dateStr,int formatCaseNum) throws ParseException{ 
		SimpleDateFormat format = getDateFormat(formatCaseNum); 
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(format.parse(dateStr));
		Integer weekNum = cal.get(Calendar.DAY_OF_WEEK)-1;
		if (0 == weekNum) {  
			weekNum = 7;  
		} 
		cal.add(Calendar.DATE, 7-weekNum);
		return format.format(cal.getTime());
	}
	//获取当月的第一天
	public static String getMonthFristDay(int caseNum){
		SimpleDateFormat format = getDateFormat(caseNum);
		Calendar frist = Calendar.getInstance();
		frist.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(frist.getTime());
	}
	/**
	 * 将时间转换成定时表达式
	 * @param clockNextDate
	 * @return
	 * @throws ParseException
	 */
	public static String transCorn(String clockNextDate) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(format.parse(clockNextDate));
		
		String cornTime = "0 "+cal.get(Calendar.MINUTE)+" "+cal.get(Calendar.HOUR_OF_DAY)+" "
			+cal.get(Calendar.DAY_OF_MONTH)+" "+(cal.get(Calendar.MONTH)+1)+" ? "+cal.get(Calendar.YEAR);
		
		return cornTime;
	}

	/**
	 * 取得时间区间信息
	 * @param dateTimeSStrYMD 时间起
	 * @param dateTimeEStrYMD 时间止
	 * @return
	 */
	public static List<String> getTimeZones(String dateTimeSStrYMD,
			String dateTimeEStrYMD) {
		@SuppressWarnings("unchecked")
		List<String> timeZones = new ArrayList();
		Date dateTimeS = DateTimeUtil.parseDate(dateTimeSStrYMD, DateTimeUtil.yyyy_MM_dd);
		Date dateTimeE = DateTimeUtil.parseDate(dateTimeEStrYMD, DateTimeUtil.yyyy_MM_dd);
		
		
		Long timeDic = dateTimeE.getTime()-dateTimeS.getTime();
		Long oneDay = 24L*60*60*1000;
		
		//经历的天数
		int days = (int) (timeDic/oneDay +1);
		
		timeZones.add(dateTimeSStrYMD);
		
		for(int i=1;i<days;i++){
			dateTimeSStrYMD = DateTimeUtil.addDate(dateTimeSStrYMD,
					DateTimeUtil.yyyy_MM_dd, Calendar.DAY_OF_YEAR, 1);
			timeZones.add(dateTimeSStrYMD);
		}
		
		return timeZones;
	} 
	/**
	 * 将long型毫秒数转换成指定的日期格式
	 * @param time long型毫秒数
	 * @param caseNum 指定的日期格式
	 * @return
	 */
	public static String changeStrDate(long time,int caseNum){
		SimpleDateFormat df = DateTimeUtil.getDateFormat(caseNum);
		String ctime = df.format(new Date(time));
		return ctime;
	}

	/**
	 * 取得一年的第一天
	 * @param formatCaseNum
	 * @return
	 */
	public static String getFirstDayOfYear(int formatCaseNum) {
		SimpleDateFormat format = getDateFormat(formatCaseNum); 
		
		Calendar calendar = Calendar.getInstance();
		Integer year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
		return format.format(calendar.getTime());
	}
	/**
	 * 获取两日期类直接的所有日期
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Date> findDates(Date dBegin, Date dEnd){  
	  List<Date> lDate = new ArrayList();  
	  lDate.add(dBegin);  
	  Calendar calBegin = Calendar.getInstance();  
	  // 使用给定的 Date 设置此 Calendar 的时间  
	  calBegin.setTime(dBegin);  
	  Calendar calEnd = Calendar.getInstance();  
	  // 使用给定的 Date 设置此 Calendar 的时间  
	  calEnd.setTime(dEnd);  
	  // 测试此日期是否在指定日期之后  
	  while (dEnd.after(calBegin.getTime()))  
	  {  
	   // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
	   calBegin.add(Calendar.DAY_OF_MONTH, 1);  
	   lDate.add(calBegin.getTime());  
	  }  
	  return lDate;  
	 }  
	
	 /** 
     * 取得给定周数的第一天 
     * @param year 
     * @param weekNo 
     * @return 
     */  
    public static String getStartDayOfWeekNo(int year,int weekNo,int formatCaseNum){ 
    	SimpleDateFormat format = getDateFormat(formatCaseNum); 
        Calendar cal = DateTimeUtil.getCalendarFormYear(year);  
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);  
        return format.format(cal.getTime());      
          
    }  
    /** 
     * get Calendar of given year 
     * @param year 
     * @return 
     */  
    private static Calendar getCalendarFormYear(int year){  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);        
        cal.set(Calendar.YEAR, year);  
        return cal;  
    }  
    /** 
     * 取得给定周数的最后一天
     * @param year 
     * @param weekNo 
     * @return 
     */  
    public static String getEndDayOfWeekNo(int year,int weekNo,int formatCaseNum){ 
    	SimpleDateFormat format = getDateFormat(formatCaseNum);
        Calendar cal = DateTimeUtil.getCalendarFormYear(year);  
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);  
        cal.add(Calendar.DAY_OF_WEEK, 6);  
        return format.format(cal.getTime());         
    }  
	
}
