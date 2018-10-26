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
        <li class="${(empty param.searchTab || (param.searchTab - param.searchTab mod 10)/10 eq 5)?'open':'' }">
            <a href="javascript:void(0)" class="menu-dropdown">
                <i class="fa fa-tags"></i>
                <span class="menu-text">分享模块</span>
                <i class="menu-expand"></i>
            </a>

            <ul class="submenu">
                <li class="${(empty param.searchTab || param.searchTab==51)?'active bg-themeprimary':'' }">
                    <a href="javascript:void(0)" onclick="menuClick('/daily/listPagedDaily?sid=${param.sid}&searchTab=51');">
                        <span class="menu-text">全部分享</span>
                    </a>
                </li>
                <li class="${(param.searchTab==52)?'active bg-themeprimary':''}">
                    <a href="javascript:void(0)" onclick="menuClick('/daily/listPagedDaily?sid=${param.sid}&dailierType=0&searchTab=52');">
                        <span class="menu-text">我的分享</span>
                    </a>
                </li>
                <c:if test="${userInfo.countSub>0}">
                    <li class="${(param.searchTab==53)?'active bg-themeprimary':''}">
                        <a href="javascript:void(0)" onclick="menuClick('/daily/listPagedDaily?sid=${param.sid}&dailierType=1&searchTab=53');">
                            <span class="menu-text">下属分享</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </li>
        <%--有下属，监督人--%>
        <c:if test="${power > 0 || isForce > 0}">
            <li class="${(((param.searchTab - param.searchTab mod 10)/10 eq 6)) ?'open':'' }">
                <a href="javascript:void(0)" class="menu-dropdown">
                    <i class="menu-icon fa fa-gear"></i>
                    <span class="menu-text">基础配置</span>
                    <i class="menu-expand"></i>
                </a>

                <ul class="submenu">
                    <li class="${(param.searchTab==61)?'active bg-themeprimary':''}">
                        <a href="javascript:void(0)" onclick="menuClick('/daily/dailyModConfPage?sid=${param.sid}&searchTab=61')">
                            <span class="menu-text">模板设置</span>
                        </a>
                    </li>
                </ul>
            </li>
        </c:if>


    </ul>
    <!-- /Sidebar Menu -->
</div>
</body>
</html>

