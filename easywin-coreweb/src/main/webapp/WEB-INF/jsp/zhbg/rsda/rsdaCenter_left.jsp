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
            <div class="page-sidebar" id="sidebar">
                <ul class="nav sidebar-menu">
                    <li class="${(empty recycleTab && param.activityMenu == '028')?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-building-o"></i>
                            <span class="menu-text">人事档案 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                        	<li class="${( param.searchTab == '281' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0);" onclick="menuClick('/rsdaBase/listPagedRsdaBase?pager.pageSize=10&activityMenu=028&searchTab=281&sid=${param.sid}');">
                                    <span class="menu-text">档案查询</span>
                                </a>
                            </li>
                        	<li class="${( param.searchTab == '282' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0);" onclick="menuClick('/jobHistory/listPagedJobHistory?pager.pageSize=10&activityMenu=028&searchTab=282&sid=${param.sid}');">
                                    <span class="menu-text">工作经历</span>
                                </a>
                            </li>
                        	<li class="${( param.searchTab == '283' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0);" onclick="menuClick('/rsdaInc/listPagedRsdaInc?pager.pageSize=10&activityMenu=028&searchTab=283&sid=${param.sid}');">
                                    <span class="menu-text">奖惩管理</span>
                                </a>
                            </li>
                        	<li class="${( param.searchTab == '284' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0);" onclick="menuClick('/rsdaTrance/listPagedRsdaTrance?pager.pageSize=10&activityMenu=028&searchTab=284&sid=${param.sid}');">
                                    <span class="menu-text">人事调动</span>
                                </a>
                            </li>
                        	<li class="${( param.searchTab == '285' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0);" onclick="menuClick('/rsdaLeave/listPagedRsdaLeave?pager.pageSize=10&activityMenu=028&searchTab=285&sid=${param.sid}');">
                                    <span class="menu-text">离职管理</span>
                                </a>
                            </li>
                        	<li class="${( param.searchTab == '286' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0);" onclick="menuClick('/rsdaResume/listPagedRsdaResume?pager.pageSize=10&activityMenu=028&searchTab=286&sid=${param.sid}');">
                                    <span class="menu-text">复职管理</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <!-- 
                    <li class="${(param.activityMenu == '024')?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-gear"></i>
                            <span class="menu-text"> 基础配置 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                        	<li class="">
                                <a href="javascript:void(0);" id="zhbgModAdmin" busType="028">
                                    <span class="menu-text">模块管理人员</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                     -->
                </ul>
            </div>
	</body>
</html>

