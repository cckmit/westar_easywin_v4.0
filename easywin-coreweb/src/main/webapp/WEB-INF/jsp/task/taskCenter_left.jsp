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
                    <li class="${(empty recycleTab && ((empty param.activityMenu  && empty param.statisticsType) || fn:contains(param.activityMenu,'task_m_1')))?'open':''}">
                        <a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text"> 任务 </span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty recycleTab && ((empty param.activityMenu && empty param.statisticsType) || param.activityMenu=='task_m_1.1'))?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskToDoPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.1');">
                                    <span class="menu-text">待办任务</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='task_m_1.3'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfMinePage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.3');">
                                    <span class="menu-text">我负责的任务</span>
                                </a>
                            </li>
                             <c:if test="${userInfo.countSub>0}">
	                            <li class="${param.activityMenu=='task_m_1.4'?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAllPage?execuType=1&pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.4');">
	                                    <span class="menu-text">下属执行任务</span>
	                                </a>
	                            </li>
                             </c:if>
                            <li class="${param.activityMenu=='task_m_1.2'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAttenPage?attentionState=1&sid=${param.sid}&activityMenu=task_m_1.2');">
                                    <span class="menu-text">我的关注</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='task_m_1.5'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAllPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.5');">
                                    <span class="menu-text">所有任务</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='task_m_1.6'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listPagedTaskForSupView?pager.pageSize=10&sid=${param.sid}&activityMenu=task_m_1.6');">
                                    <span class="menu-text">任务督查</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    
                   <c:if test="${userInfo.admin>0}">
                   <li>
                       <a href="javascript:void(0)" class="menu-dropdown">
                           <i class="menu-icon fa fa-gear"></i>
                           <span class="menu-text">基础配置</span>
                           <i class="menu-expand"></i>
                       </a>
                       <ul class="submenu">
                           <li>
                               <a href="javascript:void(0)" id="forceInBtn">
                                   <span class="menu-text">监督人员设置</span>
                               </a>
                           </li>
                           <li>
                               <a href="javascript:void(0)" onclick="modOperateConfig('${param.sid}','003',this,'')">
                                   <span class="menu-text">模块权限设置</span>
                               </a>
                           </li>
                       </ul>
                    </li>
                    </c:if>
                    <!--Profile-->
                     <li class="${( fn:contains(param.activityMenu,'task_promote_1'))?'open':''}">
                      	<a href="javascript:void(0);" class="menu-dropdown">
                            <i class="menu-icon fa fa-flag"></i>
                            <span class="menu-text">技术任务</span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(param.activityMenu=='task_promote_1.1')?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskToDoPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_promote_1.1&version=2');">
                                    <span class="menu-text">待办任务</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='task_promote_1.3'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfMinePage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_promote_1.3&version=2');">
                                    <span class="menu-text">我负责的任务</span>
                                </a>
                            </li>
                             <c:if test="${userInfo.countSub>0}">
	                            <li class="${param.activityMenu=='task_promote_1.4'?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAllPage?execuType=1&pager.pageSize=10&sid=${param.sid}&activityMenu=task_promote_1.4&version=2');">
	                                    <span class="menu-text">下属执行任务</span>
	                                </a>
	                            </li>
                             </c:if>
                            <li class="${param.activityMenu=='task_promote_1.2'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAttenPage?attentionState=1&sid=${param.sid}&activityMenu=task_promote_1.2&version=2');">
                                    <span class="menu-text">我的关注</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='task_promote_1.5'?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0);" onclick="menuClick('/task/listTaskOfAllPage?pager.pageSize=10&sid=${param.sid}&activityMenu=task_promote_1.5&version=2');">
                                    <span class="menu-text">所有任务</span>
                                </a>
                            </li>
                        </ul>
                       
                    </li>
                    <!--Profile-->
                    <li class="${not empty recycleTab?'open active':'' }">
                        <a href="javascript:void(0)" onclick="listRecyBin('003','${param.sid}')">
                            <i class="menu-icon fa fa-trash-o"></i>
                            <span class="menu-text">回收站</span>
                        </a>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

