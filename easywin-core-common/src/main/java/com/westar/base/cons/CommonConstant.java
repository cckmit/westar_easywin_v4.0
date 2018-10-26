package com.westar.base.cons;

/**
 * 整个应用通用的常量 
 * @author Administrator
 */
public interface CommonConstant {

	/**
	 * 静态资源文件路径
	 */
	final static String STATIC_PATH = "/static/";

	/**
	 * 网站静态页面路径
	 */
	final static String WEB_PAGE = "/webPage/";

	/**
	 * 网站处理controller
	 */
	final static String WEB_CONTROLLER = "/web/";

	/**
	 * 附件处理controller
	 */
	final static String UPLOAD_PATH = "/upload/";

	/**
	 * flex资源路径
	 */
	final static String FLEX_PATH = "/flex-debug/";

	/**
	 * Web通知对象放到session中的键名称
	 */
	final static String NOTIFICATION_CONTEXT = "notification_context";

	/**
	 * 分页索引值参数名
	 */
	final static String PAGINATION_OFFSET = "pager.offset";
	/**
	 * 每页显示多少条记录
	 */
	final static String PAGINATION_PAGESIZE = "pager.pageSize";
	
	/**
	 * 掉线提示
	 */
	final static String OFF_LINE_INFO = "服务已断开，请重新登陆";
	
	/**
	 * 手机客户端authkey参数名
	 */
	public final static String APP_AUTH_KEY = "auth_key";
	
	/**
	 * 手机端用户唯一标识存放
	 */
	public final static String APP_USER_CONTEXT_KEY="app_user_context";
	
	/**
	 * 休息日
	 */
	public final static Integer HOLIDAY=1;
	
	/**
	 * 工作日
	 */
	public final static Integer WORKDAY=2;
	
	/**
	 * 系统模板的团队号
	 */
	public final static Integer SYSCOMID=0;
}
