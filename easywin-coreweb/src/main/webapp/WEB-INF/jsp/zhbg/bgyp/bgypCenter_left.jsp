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
            <div class="page-sidebar" id="sidebar">
                <ul class="nav sidebar-menu">
                    <li class="${(empty recycleTab && param.activityMenu == '027')?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-building-o"></i>
                            <span class="menu-text">办公用品 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu" id="bgypMenuOne">
                        	<li class="${( param.searchTab == '271' && param.activityMenu == '027')?'active bg-themeprimary':'' }" style="display: none;" busType="271">
                                 <a href="javascript:void(0);" onclick="menuClick('/bgypFl/frameBgypflPage?activityMenu=027&searchTab=271&sid=${param.sid}');">
                                    <span class="menu-text">基础维护</span>
                                </a>
                            </li>
                        	<li class="${( param.searchTab == '272' && param.activityMenu == '027')?'active bg-themeprimary':'' }" style="display: none;" busType="272">
                                 <a href="javascript:void(0);" onclick="menuClick('/bgypPurOrder/listPagedBgypPurOrder?activityMenu=027&searchTab=272&sid=${param.sid}');">
                                    <span class="menu-text">查询采购</span>
                                </a>
                            </li>
                        	<li class="${( param.searchTab == '273' && param.activityMenu == '027')?'active bg-themeprimary':'' }" style="display: none;" busType="273">
                                 <a href="javascript:void(0);" onclick="menuClick('/bgypPurOrder/listPagedSpBgypPurOrder?activityMenu=027&searchTab=273&sid=${param.sid}');">
                                    <span class="menu-text">审核采购</span>
                                </a>
                            </li>
                            
                        	<li class="${(param.searchTab == '274' && param.activityMenu == '027')?'active bg-themeprimary':'' }" busType="274">
                                <a href="javascript:void(0);" onclick="menuClick('/bgypApply/listPagedBgypApply?activityMenu=027&searchTab=274&sid=${param.sid}');">
                                    <span class="menu-text">查询申领</span>
                                </a>
                            </li>
                        	<li class="${(param.searchTab == '275' && param.activityMenu == '027')?'active bg-themeprimary':'' }" style="display: none;" busType="275">
                                 <a href="javascript:void(0);" onclick="menuClick('/bgypApply/listPagedSpBgypApply?activityMenu=027&searchTab=275&sid=${param.sid}');">
                                    <span class="menu-text">审核申领</span>
                                </a>
                            </li>
                            <li class="${(param.searchTab == '276' && param.activityMenu == '027')?'active bg-themeprimary':'' }" busType="276">
                                 <a href="javascript:void(0);" onclick="menuClick('/bgypItem/listPagedBgypStore?activityMenu=027&searchTab=276&sid=${param.sid}');">
                                    <span class="menu-text">用品库存</span>
                                </a>
                            </li>
                             
                        </ul>
                    </li>
                    <!-- 
                    <c:choose>
                    	<c:when test="${userInfo.admin>0 }">
		                    <li class="${(param.activityMenu == '024')?'open':''}">
		                        <a href="javascript:void(0);" class="menu-dropdown">
		                            <i class="menu-icon fa fa-gear"></i>
		                            <span class="menu-text"> 基础配置 </span>
		                            <i class="menu-expand"></i>
		                        </a>
		
		                        <ul class="submenu">
		                        	<li class="">
		                                <a href="javascript:void(0);" id="zhbgModAdmin" busType="027">
		                                    <span class="menu-text">模块管理人员</span>
		                                </a>
		                            </li>
		                        </ul>
		                    </li>
                    	</c:when>
                    </c:choose>
                     -->
                </ul>
            </div>
	</body>
</html>

