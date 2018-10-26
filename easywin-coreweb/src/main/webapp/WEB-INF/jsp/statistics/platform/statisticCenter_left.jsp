<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
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
	 <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
                <!-- Sidebar Menu -->
                <ul class="nav sidebar-menu">
                    <!--Tables-->
                    <li class="${(empty param.activityMenu or fn:contains(param.activityMenu,'platform_'))?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text">平台统计 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='platform_1.0')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/operationSummarize?sid=${param.sid}&activityMenu=platform_1.0');">
                                    <span class="menu-text">运营总览</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.2')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticTask?sid=${param.sid}&activityMenu=platform_1.2');">
                                    <span class="menu-text">任务执行分析</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.7')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/task/statisticSupTask?sid=${param.sid}&activityMenu=platform_1.7');">
                                    <span class="menu-text">任务执行督办</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.3')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticCrm?sid=${param.sid}&activityMenu=platform_1.3');">
                                    <span class="menu-text">客户维护督办</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.9')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/dailyRepStatistics?sid=${param.sid}&activityMenu=platform_1.9');">
                                    <span class="menu-text">日报汇报统计</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_2.0')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticWeekReportPage?sid=${param.sid}&activityMenu=platform_2.0');">
                                    <span class="menu-text">周报汇报统计</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.4')?'active bg-themeprimary':'' }">
                               <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticFeeCrm?sid=${param.sid}&activityMenu=platform_1.4');">
                                    <span class="menu-text">差旅费统计</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.5')?'active bg-themeprimary':'' }">
                               <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticFeeItem?sid=${param.sid}&activityMenu=platform_1.5');">
                                    <span class="menu-text">实施费统计</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.6')?'active bg-themeprimary':'' }">
                             	<a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticAttence?sid=${param.sid}&activityMenu=platform_1.6');">
                                    <span class="menu-text">团队考勤</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='platform_1.8')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/platform/statisticBusRemindPage?sid=${param.sid}&activityMenu=platform_1.8');">
                                    <span class="menu-text">催办记录</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                     <!--Tables-->
                     <li class="${(fn:contains(param.activityMenu,'suverViewPlatform_'))?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text">数据监督 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(param.activityMenu=='suverViewPlatform_1.1')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/suverViewPlatform/statisticDemandProcess?sid=${param.sid}&activityMenu=suverViewPlatform_1.1');">
                                    <span class="menu-text">需求落实监督</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='suverViewPlatform_1.2')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/suverViewPlatform/statisticItem?sid=${param.sid}&activityMenu=suverViewPlatform_1.2');">
                                    <span class="menu-text">在建项目监督</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='suverViewPlatform_1.3')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/suverViewPlatform/statisticTask?sid=${param.sid}&activityMenu=suverViewPlatform_1.3');">
                                    <span class="menu-text">任务执行监督</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='suverViewPlatform_1.4')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/statistics/suverViewPlatform/statisticTaskByExecutor?sid=${param.sid}&activityMenu=suverViewPlatform_1.4');">
                                    <span class="menu-text">个人工作统计</span>
                                </a>
                            </li>
                        </ul>
               		</li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

