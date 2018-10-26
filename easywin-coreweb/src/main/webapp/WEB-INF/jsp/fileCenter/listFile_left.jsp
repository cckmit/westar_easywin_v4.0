<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
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
                        	<i class="fa fa-book"></i>
                            <span class="menu-text">文档管理</span>
                        </a>

                        <ul class="submenu">
                            <li class="${ empty modList ?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchFileTab('')">
                                    <span class="menu-text">全部模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'003')?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchFileTab('003')">
                                    <span class="menu-text">任务模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'004')?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchFileTab('004')">
                                    <span class="menu-text">投票模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'005')?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchFileTab('005')">
                                    <span class="menu-text">项目模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'006')?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchFileTab('006')">
                                    <span class="menu-text">周报模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'050')?'active bg-themeprimary':''}">
                                <a href="javascript:void(0)" onclick="searchFileTab('050')">
                                    <span class="menu-text">分享模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'011')?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchFileTab('011')">
                                    <span class="menu-text">问答模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'012')?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchFileTab('012')">
                                    <span class="menu-text">客户模块</span>
                                </a>
                            </li>
                            <li class="${fn:contains(modList,'013')?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="searchFileTab('013')">
                                    <span class="menu-text">文档模块</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

