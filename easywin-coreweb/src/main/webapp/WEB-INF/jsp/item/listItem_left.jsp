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
                    <li class="${(empty recycleTab && (empty param.searchTab || (param.searchTab - param.searchTab mod 10)/10 eq 1))?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text">项目 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty recycleTab && (empty param.searchTab || param.searchTab==11))?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchItem(11)">
                                    <span class="menu-text">我的项目</span>
                                </a>
                            </li>
                            <li class="${param.searchTab==12?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchItem(12)">
                                    <span class="menu-text">我的关注</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.countSub>0}">
	                            <li class="${param.searchTab==13?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="searchItem(13)">
	                                    <span class="menu-text">下属项目</span>
	                                </a>
	                            </li>
                            </c:if>
                            <li class="${param.searchTab==14?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchItem(14)">
                                    <span class="menu-text">所有项目</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <c:if test="${userInfo.admin>0}">
	                    <li class="${(param.searchTab==33)?'open':'' }">
	                        <a href="javascript:void(0)" class="menu-dropdown">
	                            <i class="menu-icon fa fa-gear"></i>
	                            <span class="menu-text">基础配置</span>
	                            <i class="menu-expand"></i>
	                        </a>
	                        <ul class="submenu">
	                            <li>
	                                <a href="javascript:void(0)" id="forceInBtn">
	                                    <span class="menu-text">监督人员设置</span>
	                                </a>
	                            </li>
	                            <li>
	                                <a href="javascript:void(0)" onclick="settingItem(32,this)">
	                                    <span class="menu-text">操作权限设置</span>
	                                </a>
	                            </li>
	                            <li class="${param.searchTab==33?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="searchItem(33)">
	                                    <span class="menu-text">进度模板设置</span>
	                                </a>
	                            </li>
	                        </ul>
	                    </li>
                    </c:if>
                   
                    <!--Profile-->
                    <li class="${not empty recycleTab?'open active':'' }">
                        <a href="javascript:void(0)" onclick="listRecyBin('005','${param.sid}')">
                            <i class="menu-icon fa fa-trash-o"></i>
                            <span class="menu-text">回收站</span>
                        </a>
                    </li>
                    
                    
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

