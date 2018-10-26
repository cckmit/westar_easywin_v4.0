<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	</head>
	<body>
		 <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
                <!-- Sidebar Menu -->
                <ul class="nav sidebar-menu">
                 	
                 	<li class="${(not empty param.tab && param.tab <30)  ?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                        	<i class="fa fa-users"></i>
                            <span class="menu-text">团队配置</span>
                        </a>

                        <ul class="submenu">
                            <li class="${param.tab==11?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/organic/organicInfo?sid=${param.sid}&tab=11');">
                                    <span class="menu-text">团队信息</span>
                                </a>
                            </li>
                            <li class="${param.tab==12?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/department/framesetDepPage?sid=${param.sid}&tab=12');">
                                    <span class="menu-text">团队部门</span>
                                </a>
                            </li>
                            <li class="${param.tab==13?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/userInfo/listPagedUserInfo?sid=${param.sid}&pager.pageSize=10&tab=13');">
                                    <span class="menu-text">团队成员</span>
                                </a>
                            </li>
                            <li class="${param.tab==18?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/role/listPagedRole?sid=${param.sid}&pager.pageSize=10&tab=18');">
                                    <span class="menu-text">团队角色</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.admin>0}">
	                            <li class="${param.tab==14?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0);" onclick="menuClick('/userInfo/listPagedForCheck?sid=${param.sid}&pager.pageSize=10&tab=14');">
	                                    <span class="menu-text">申请审核</span>
	                                </a>
	                            </li>
                            </c:if>
                            <li class="${param.tab==15?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/userInfo/inviteUserPage?sid=${param.sid}&tab=15');">
                                    <span class="menu-text">邀请用户</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.admin == 1}">
	                            <li class="${param.tab==16?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0);" onclick="menuClick('/organic/organicManagePage?sid=${param.sid}&tab=16');">
	                                    <span class="menu-text">团队管治</span>
	                                </a>
	                            </li>
                            </c:if>
                            <c:if test="${userInfo.admin>0}">
	                            <li class="${param.tab==17?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0);" onclick="menuClick('/systemLog/listPagedOrgSysLog?sid=${param.sid}&pager.pageSize=10&tab=17');">
	                                    <span class="menu-text">系统日志</span>
	                                </a>
	                            </li>
                            </c:if>
                        </ul>
                    </li>
                    
                    <li class="${(not empty param.tab && param.tab >30) ?'open':''}">
                  	    <a href="/sysUpLog/toListSysUpLog?sid=${param.sid}&activityMenu=sul_m_1.1&tab=31" class="menu-dropdown">
                        	<i class="fa fa-clock-o"></i>
                            <span class="menu-text">平台升级日志</span>
                        </a>
                    </li>
                </ul>
                
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

