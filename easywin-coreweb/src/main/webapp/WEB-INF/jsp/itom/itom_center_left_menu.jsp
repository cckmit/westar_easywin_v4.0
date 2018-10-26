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
	<body>
	 <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
                <!-- Sidebar Menu -->
                <ul class="nav sidebar-menu">
                    <!--Tables-->
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'042'))?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-gavel"></i>
                            <span class="menu-text">运维过程管理 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='04201')?'active bg-themeprimary':'' }">
                                <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04201&sid=${param.sid}">
                                    <span class="menu-text">事件管理</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='04202'?'active bg-themeprimary':'' }">
                                <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04202&sid=${param.sid}">
                                    <span class="menu-text">问题管理</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='04203'?'active bg-themeprimary':'' }">
                                <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04203&sid=${param.sid}">
                                    <span class="menu-text">变更管理</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='04204'?'active bg-themeprimary':'' }">
                                <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04204&sid=${param.sid}">
                                    <span class="menu-text">发布管理</span>
                                </a>
                            </li>
                        </ul>
                        
                    </li>
                    <li class="${fn:contains(param.activityMenu,'043')?'open':''}">
	                        <a href="javascript:void(0)" class="menu-dropdown">
	                            <i class="menu-icon fa fa-cloud"></i>
	                            <span class="menu-text">运维过程统计</span>
	                            <i class="menu-expand"></i>
	                        </a>
	                        <ul class="submenu">
	                            <li class="${(param.activityMenu=='04301')?'active bg-themeprimary':'' }">
	                               <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04301&sid=${param.sid}">
	                                    <span class="menu-text">事件管理统计</span>
	                                </a>
	                            </li>
	                            <li class="${(param.activityMenu=='04302')?'active bg-themeprimary':'' }">
	                               <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04302&sid=${param.sid}">
	                                    <span class="menu-text">问题管理统计</span>
	                                </a>
	                            </li>
	                            <li class="${(param.activityMenu=='04303')?'active bg-themeprimary':'' }">
	                               <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04303&sid=${param.sid}">
	                                    <span class="menu-text">变更管理统计</span>
	                                </a>
	                            </li>
	                            <li class="${(param.activityMenu=='04304')?'active bg-themeprimary':'' }">
	                                <a href="/iTOm/itom_center?pager.pageSize=15&activityMenu=04304&sid=${param.sid}">
	                                    <span class="menu-text">发布管理统计</span>
	                                </a>
	                            </li>
	                        </ul>
	                     </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

