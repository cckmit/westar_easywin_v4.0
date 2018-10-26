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
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'demand_1'))?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text">需求管理</span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='demand_1.1')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/demand/listPagedMineDemand?pager.pageSize=10&sid=${param.sid}&activityMenu=demand_1.1');">
                                    <span class="menu-text">发布需求</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='demand_1.2')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/demand/listPagedDemandForAccept?pager.pageSize=10&sid=${param.sid}&activityMenu=demand_1.2');">
                                    <span class="menu-text">需求签收</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='demand_1.3')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/demand/listPagedDemandForHandle?pager.pageSize=10&sid=${param.sid}&activityMenu=demand_1.3');">
                                    <span class="menu-text">需求处理</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='demand_1.4')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/demand/listPagedDemandForConfirm?pager.pageSize=10&sid=${param.sid}&activityMenu=demand_1.4');">
                                    <span class="menu-text">成果确认</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='demand_1.5')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/demand/listPagedDemandForAll?pager.pageSize=10&sid=${param.sid}&activityMenu=demand_1.5');">
                                    <span class="menu-text">所有需求</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    
                   <c:if test="${userInfo.admin>0}">
                   <li class="${(fn:contains(param.activityMenu,'demandMod_1'))?'open':''}">
                       <a href="javascript:void(0)" class="menu-dropdown">
                           <i class="menu-icon fa fa-gear"></i>
                           <span class="menu-text">基础配置</span>
                           <i class="menu-expand"></i>
                       </a>
                       <ul class="submenu">
                           <li class="${(param.activityMenu=='demandMod_1.1')?'active bg-themeprimary':'' }">
                               <a href="javascript:void(0)" onclick="menuClick('/demandModuleCfg/listPagedDemandModuleCfg?pager.pageSize=10&sid=${param.sid}&activityMenu=demandMod_1.1');">
                                   <span class="menu-text">需求处理模板</span>
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

