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
                 	<li class="open">
                        <a href="javascript:void(0)">
                        	<i class="fa fa-clock-o"></i>
                            <span class="menu-text">所属模块</span>
                        </a>

                        <ul class="submenu">
                            <li class="${empty clock.busType?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchClock()">
                                    <span class="menu-text">全部模块</span>
                                </a>
                            </li>
                            <li class="${clock.busType=='003'?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchClock('003')">
                                    <span class="menu-text">任务模块</span>
                                </a>
                            </li>
                            <li class="${clock.busType=='005'?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchClock('005')">
                                    <span class="menu-text">项目模块</span>
                                </a>
                            </li>
                            <li class="${clock.busType=='012'?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchClock('012')">
                                    <span class="menu-text">客户模块</span>
                                </a>
                            </li>
                            <li class="${clock.busType=='101'?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchClock('101')">
                                    <span class="menu-text">普通闹铃</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

