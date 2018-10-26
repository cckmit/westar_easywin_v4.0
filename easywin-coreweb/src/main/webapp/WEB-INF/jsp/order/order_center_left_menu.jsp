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
                    <!--Tables-->
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'order_m_1'))?'open':''}">
                        <a href="javascript:void(0)">
                            <i class="menu-icon fa fa-list-ul"></i>
                            <span class="menu-text">升级服务 </span>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='order_m_1.1')?'active bg-themeprimary':'' }">
                                <a href="/order/order_center?sid=${param.sid}&activityMenu=order_m_1.1">
                                    <span class="menu-text">收费介绍</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='order_m_1.2'?'active bg-themeprimary':'' }">
                                <a href="/order/listOrders?pager.pageSize=10&sid=${param.sid}&activityMenu=order_m_1.2">
                                    <span class="menu-text">交易记录</span>
                                </a>
                            </li>
                            <%--<li class="${param.activityMenu=='order_m_1.4'?'active bg-themeprimary':'' }">
                                <a href="/order/listSpFlowOfAtten?pager.pageSize=10&sid=${param.sid}&activityMenu=order_m_1.4">
                                    <span class="menu-text">团队钱包</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='order_m_1.3'?'active bg-themeprimary':'' }">
                                <a href="/order/listSpFlowOfAll?pager.pageSize=10&sid=${param.sid}&activityMenu=order_m_1.3">
                                    <span class="menu-text">存储空间</span>
                                </a>
                            </li>
                        --%>
                        </ul>
                    </li>
                    <!--Forms-->
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

