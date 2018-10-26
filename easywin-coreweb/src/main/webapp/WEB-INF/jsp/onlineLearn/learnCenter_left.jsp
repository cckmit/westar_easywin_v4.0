<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page
	import="com.westar.core.web.InitServlet"%>
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
			<li class="open"><a href="javascript:void(0);"
				class="menu-dropdown"><i class="menu-icon fa fa-graduation-cap "></i>
					<span class="menu-text"> 在线学习</span> <i class="menu-expand"></i> </a>

				<ul class="submenu">
					<li class="active bg-themeprimary">
						<a href="javascript:void(0);"
						onclick="menuClick('/onlineLearn/listPagedVideo?sid=${param.sid}&pager.pageSize=10');">
							<span class="menu-text">文件资料</span>
					</a>
					</li>
				</ul></li>
			<c:if test="${userInfo.admin>0}">
				<li><a href="javascript:void(0)" class="menu-dropdown"> <i
						class="menu-icon fa fa-gear"></i> <span class="menu-text">基础配置</span>
						<i class="menu-expand"></i>
				</a>
					<ul class="submenu">
						<li><a href="javascript:void(0)"
							onclick="modOperateConfig('${param.sid}','023',this,'')"> <span
								class="menu-text">模块权限设置</span>
						</a></li>
					</ul></li>
			</c:if>
		</ul>
	</div>
</body>
</html>

