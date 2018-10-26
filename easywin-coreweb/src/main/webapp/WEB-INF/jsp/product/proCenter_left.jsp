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
                <span class="menu-text">产品 </span>
                <i class="menu-expand"></i>
            </a>

            <ul class="submenu">
                <li class="${(empty recycleTab && (empty param.searchTab || param.searchTab==11))?'active bg-themeprimary':'' }">
                    <a href="javascript:void(0)" onclick="searchItem(11,${userInfo.id})">
                        <span class="menu-text">我创建的产品</span>
                    </a>
                </li>
                <li class="${param.searchTab==12?'active bg-themeprimary':'' }">
                    <a href="javascript:void(0)" onclick="searchItem(12,${userInfo.id})">
                        <span class="menu-text">我关注的产品</span>
                    </a>
                </li>
                <li class="${param.searchTab==13?'active bg-themeprimary':'' }">
                    <a href="javascript:void(0)" onclick="searchItem(13,${userInfo.id})">
                        <span class="menu-text">我负责的产品</span>
                    </a>
                </li>
                <li class="${param.searchTab==14?'active bg-themeprimary':'' }">
                    <a href="javascript:void(0)" onclick="searchItem(14,${userInfo.id})">
                        <span class="menu-text">我管理的产品</span>
                    </a>
                </li>
                <li class="${param.searchTab==15?'active bg-themeprimary':'' }">
                    <a href="javascript:void(0)" onclick="searchItem(15,${userInfo.id})">
                        <span class="menu-text">所有产品</span>
                    </a>
                </li>
            </ul>
        </li>
    </ul>
    <!-- /Sidebar Menu -->
</div>
</body>
</html>

