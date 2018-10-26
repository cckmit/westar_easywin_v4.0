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
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'self_m_1'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-bar-chart-o"></i>
                            <span class="menu-text">个人中心 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='self_m_1.1')?'active':'' }">
                                <a href="/userInfo/selfCenter?pager.pageSize=10&sid=${param.sid}&activityMenu=self_m_1.1">
                                    <span class="menu-text">工作分享</span>
                                </a>
                            </li>
                            <li>
                                <a href="/attention/attCenter?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.2">
                                    <span class="menu-text">我的关注</span>
                                </a>
                            </li>
                            <li>
                                <a href="/systemLog/listPagedSelfSysLog?sid=${param.sid}&pager.pageSize=15&activityMenu=self_m_1.3">
                                    <span class="menu-text">操作日志</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${(fn:contains(param.activityMenu,'self_m_2'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-trophy"></i>
                            <span class="menu-text">积分中心</span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li>
                                <a href="#">
                                    <span class="menu-text">总积分</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">我的积分</span>
                                </a>
                            </li>
                            <li class="active">
                                <a href="#">
                                    <span class="menu-text">月积分排名</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">周积分排名</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">积分等级</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">如何积分</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">申请加入团队</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${(fn:contains(param.activityMenu,'self_m_3'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-gear"></i>
                            <span class="menu-text">个人设置 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="active">
                                <a href="/task/listTaskToDoPage?pager.pageSize=10&sid=${param.sid}&activityMenu=self_m_2.1">
                                    <span class="menu-text">个人资料</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">头像设置</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">修改密码</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">消息设置</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">应用设置</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">首页设置</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="menu-text">申请加入团队</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <!--Charts-->
                    <li>
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-bar-chart-o"></i>
                            <span class="menu-text">任务完成情况 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li>
                                <a href="#">
                                    <span class="menu-text">任务完成情况统计</span>
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
                               <a href="javascript:void(0)" onclick="forceIn('${param.sid}');">
                                   <span class="menu-text">监督人员设置</span>
                               </a>
                           </li>
                           <li>
                               <a href="javascript:void(0)" onclick="modOperateConfig('${param.sid}','003')"">
                                   <span class="menu-text">模块权限设置</span>
                               </a>
                           </li>
                       </ul>
                    </li>
                    </c:if>
                    <!--Profile-->
                    <li>
                        <a href="#">
                            <i class="menu-icon fa fa-trash-o"></i>
                            <span class="menu-text">回收站</span>
                        </a>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

