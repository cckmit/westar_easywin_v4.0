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
<body>
<!-- Page Sidebar -->
<div class="page-sidebar" id="sidebar">
    <!-- Sidebar Menu -->
    <ul class="nav sidebar-menu">
        <li class="${not empty param.activityMenu?'open':'' }">
            <a href="javascript:void(0)" class="menu-dropdown">
                <i class="menu-icon fa fa-legal "></i>
                <span class="menu-text">任务评分</span>
                <i class="menu-expand"></i>
            </a>
            <ul class="submenu">
                <li class="${param.activityMenu=='task_score_2.0'?'active bg-themeprimary':'' }">
                    <a href="javascript:void(0)" onclick="menuClick('/jfMod/task/listPagedMineTaskToJf?pager.pageSize=10&sid=${param.sid}&activityMenu=task_score_2.0');">
                        <span class="menu-text">我的任务评分</span>
                    </a>
                </li>
                <c:if test="${userInfo.countSub>0}">
                    <li class="${param.activityMenu=='task_score_1.0'?'active bg-themeprimary':'' }">
                        <a href="javascript:void(0)" onclick="menuClick('/jfMod/task/listPagedTaskToJf?pager.pageSize=10&sid=${param.sid}&activityMenu=task_score_1.0');">
                            <span class="menu-text">待评分任务</span>
                        </a>
                    </li>
                </c:if>
                <c:if test="${userInfo.countSub>0 ||isForceIn}">
                    <li  class="${param.activityMenu=='task_score_3.0'?'active bg-themeprimary':'' }">
                        <a href="javascript:void(0)" onclick="menuClick('/jfMod/task/statisticsTaskJfPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_score_3.0');">
                            <span class="menu-text">评分统计</span>
                        </a>
                    </li>
                </c:if>
                    <li>
                    <c:if test="${userInfo.countSub>0}">
                        <a href="javascript:void(0)" onclick="pfSubSet('${param.sid}',this)">
                            <span class="menu-text">评分下属范围设置</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </li>
    </ul>
</div>
</body>
</html>

