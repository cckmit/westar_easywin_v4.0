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
                    <li class="open">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text">表单 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="active bg-themeprimary">
                                <a href="/form/formFlowList?sid=${param.sid}">
                                    <span class="menu-text">填写表单</span>
                                </a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" >
                                    <span class="menu-text">未提交的表单</span>
                                </a>
                            </li>
                            <li>
                                <a href="/workFlow/listPagedWorkFlow?sid=${param.sid}&searchTab=13" >
                                    <span class="menu-text">已提交的表单</span>
                                </a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" >
                                    <span class="menu-text">全部表单</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text">表单管理 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="active bg-themeprimary">
                                <a href="/form/formModList?sid=${param.sid}&searchTab=21">
                                    <span class="menu-text">表单维护</span>
                                </a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="listFormModSort()">
                                    <span class="menu-text">分类维护</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                   
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

