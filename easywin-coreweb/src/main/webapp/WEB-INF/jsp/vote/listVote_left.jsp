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
                 	<li class="${empty recycleTab?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                           	<i class="menu-icon fa fa-bar-chart-o blue"></i>
                            <span class="menu-text">投票中心</span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty recycleTab && (empty param.searchTab || param.searchTab==11))?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchVoteTab(11)">
                                    <span class="menu-text">所有投票</span>
                                </a>
                            </li>
                            <li class="${param.searchTab==12?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0)" onclick="searchVoteTab(12)">
                                    <span class="menu-text">我发起的</span>
                                </a>
                            </li>
                            <li class="${param.searchTab==13?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0)" onclick="searchVoteTab(13)">
                                    <span class="menu-text">我参与的</span>
                                </a>
                            </li>
                            <li class="${param.searchTab==14?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchVoteTab(14)">
                                    <span class="menu-text">关注投票</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <!--Profile-->
                    <li class="${not empty recycleTab?'open':'' }">
                        <a href="javascript:void(0)" onclick="listRecyBin('004','${param.sid}')">
                            <i class="menu-icon fa fa-trash-o"></i>
                            <span class="menu-text">回收站</span>
                        </a>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

