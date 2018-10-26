<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
	<div class="page-sidebar" id="sidebar">
		<ul class="nav sidebar-menu">
			<li class="open">
				<a href="javascript:void(0);" class="menu-dropdown"> <i class="menu-icon fa fa-calendar-check-o"></i> <span class="menu-text">考勤管理 </span> <i class="menu-expand"></i>
				</a>
				<ul class="submenu">
					<li class="${( param.searchTab == '03801' && param.activityMenu == '038')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/attence/listAttenceStateByDay?activityMenu=038&searchTab=03801&sid=${param.sid}&pager.pageSize=15');">
							<span class="menu-text">每日考勤</span>
						</a>
					</li>
					<li class="${( param.searchTab == '03802' && param.activityMenu == '038')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/attence/listAttenceRecord?activityMenu=038&searchTab=03802&sid=${param.sid}');">
							<span class="menu-text">考勤记录</span>
						</a>
					</li>
					<li class="${( param.searchTab == '03803' && param.activityMenu == '038')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/attence/attenceStatistics?activityMenu=038&searchTab=03803&sid=${param.sid}&pager.pageSize=15');">
							<span class="menu-text">月度统计</span>
						</a>
					</li>
					<li class="${( param.searchTab == '03804')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);"
						   onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/attence/listLeave?sid=${param.sid}&pager.pageSize=10&searchTab=03804&activityMenu=038");'>
							<span class="menu-text">我的请假记录</span>
						</a>
					</li>
					<li class="${( param.searchTab == '03805')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);"
						   onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/attence/listOverTime?sid=${param.sid}&pager.pageSize=10&searchTab=03805&activityMenu=038");'>
							<span class="menu-text">我的加班记录</span>
						</a>
					</li>
					<li class="${( param.searchTab == '03806')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/attence/listAttenceRecordOfSelf?sid=${param.sid}&pager.pageSize=15&searchTab=03806&userId=${userInfo.id }&activityMenu=038');">
							<span class="menu-text">我的考勤记录</span>
						</a>
					</li>
				</ul>
			</li>
		</ul>
	</div>
</body>
</html>

