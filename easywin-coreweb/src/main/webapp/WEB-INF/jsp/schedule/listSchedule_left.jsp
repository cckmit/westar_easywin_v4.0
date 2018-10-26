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
                <ul class="nav sidebar-menu" id="allUl">
                 	<li class="open" id="oneLi">
                        <a href="javascript:void(0)" class="menu-dropdown">
                        	<i class="fa fa-calendar"></i>
                            <span class="menu-text">日程应用</span>
                        </a>

                        <ul class="submenu">
                            <li class="active bg-themeprimary">
                                <a href="javascript:void(0)" onclick="searchSche(this,'016')">
                                    <span class="menu-text">日程模块</span>
                                </a>
                            </li>
                            <%--<li>--%>
                                 <%--<a href="javascript:void(0)" onclick="searchSche(this,'003')">--%>
                                    <%--<span class="menu-text">任务模块</span>--%>
                                <%--</a>--%>
                            <%--</li>--%>
                          <!--   <li>

                                 <a href="javascript:void(0)" onclick="searchSche(this,'016')">
                                    <span class="menu-text">日程模块</span>
                                </a>
                            </li> -->
                        </ul>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

