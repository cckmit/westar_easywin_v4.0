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
			<li class="open"><a href="javascript:void(0);"
				class="menu-dropdown"> <i class="menu-icon fa fa-flag"></i> <span
					class="menu-text">维护分类 </span>
			</a>

				<ul class="submenu">
					<li
						class="${( (empty param.activityMenu || param.activityMenu=='task_m_1.1'))?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);"
						onclick="menuClick('/web/help/listQus?activityMenu=task_m_1.1');">
							<span class="menu-text">帮助维护</span>
					</a>
					</li>
					<%-- <li class="${param.activityMenu=='task_m_1.2'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAllPage?attentionState=1&sid=${param.sid}&activityMenu=task_m_1.2');">
                                    <span class="menu-text">我关注的任务</span>
                                </a>
                            </li> --%>
					<li
						class="${(param.activityMenu=='sp_m_2.1')?'active bg-themeprimary':'' }">
						<a href="/web/form/listPagedCloudFormMod?activityMenu=sp_m_2.1">
							<span class="menu-text">云表单</span>
					</a>
					</li>
					<li
						class="${(param.activityMenu=='sp_m_2.2')?'active bg-themeprimary':'' }">
						<a href="/web/count/listPagedOrgCount?activityMenu=sp_m_2.2&pager.pageSize=10">
							<span class="menu-text">团队统计</span>
					</a>
					</li>
					<li class="${(param.activityMenu=='sp_m_2.3')?'active bg-themeprimary':'' }">
						<a href="/web/orders/listPagedWebOrder?activityMenu=sp_m_2.3&pager.pageSize=10">
							<span class="menu-text">支付信息</span>
						</a>
					</li>
					<li
						class="${(param.activityMenu=='sul_m_1.1')?'active bg-themeprimary':'' }">
						<a href="/web/upLog/toListSysUpLog?activityMenu=sul_m_1.1">
							<span class="menu-text">升级日志</span>
					</a>
					
					</li>
				</ul></li>
		</ul>
	</div>
</body>
</html>

