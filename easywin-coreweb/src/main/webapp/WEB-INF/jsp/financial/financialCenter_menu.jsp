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
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'m_1'))?'open':''}">
                        <a href="#" class="menu-dropdown">
                            <i class="menu-icon fa fa-bar-chart-o"></i>
                            <span class="menu-text">费用管理</span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                             <li class="${param.activityMenu=='m_1.9'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/personalApply?sid=${param.sid}&activityMenu=m_1.9&tab=1");'>
                                    <span class="menu-text">个人费用</span>
                                </a>
                            </li>
                        	<!-- 
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_1.1')?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);" onclick="menuClick('/financial/financialStatistics?sid=${param.sid}&activityMenu=m_1.1');">
                                    <span class="menu-text">差旅一览</span>
                                </a>
                            </li>
                        	 -->
                            <li class="${param.activityMenu=='m_1.2'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanApply/listLoanApplyOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.2");'>
                                    <span class="menu-text">出差记录</span>
                                </a>
                            </li>
                            <!-- 
	                            <li class="${param.activityMenu=='m_1.5'?'active bg-themeprimary':''}">
	                                <a href="javascript:void(0);"
	                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanApply/listLoanApplyOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.5");'>
	                                    <span class="menu-text">一般费用管理</span>
	                                </a>
	                            </li>
                             -->
                            <li class="${param.activityMenu=='m_1.3'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loan/listLoanOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.3");'>
                                    <span class="menu-text">借款记录</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='m_1.4'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanOff/listLoanOffOfAuth?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.4");'>
                                    <span class="menu-text">报销记录</span>
                                </a>
                            </li>
                             <li class="${param.activityMenu=='m_1.7'?'active bg-themeprimary':''}">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/consume/listConsume?sid=${param.sid}&pager.pageSize=10&activityMenu=m_1.7");'>
                                    <span class="menu-text">消费记录</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='m_1.8'?'active bg-themeprimary':''}" id="financialOffice" style="display: none">
                                <a href="javascript:void(0);"
                                onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/viewFinancialOffices?sid=${param.sid}&activityMenu=m_1.8");'>
                                    <span class="menu-text">财务办公</span>
                                </a>
                            </li>
	                            <%--<li class="${param.activityMenu=='self_m_1.9'?'active bg-themeprimary':''}">--%>
	                                <%--<a href="javascript:void(0);"--%>
	                                <%--onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanApply/listLoanApply?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.9");'>--%>
	                                    <%--<span class="menu-text">一般借款申请</span>--%>
	                                <%--</a>--%>
	                            <%--</li>--%>
	                            <%--<li class="${param.activityMenu=='self_m_1.5'?'active bg-themeprimary':''}">--%>
	                                <%--<a href="javascript:void(0);"--%>
	                                <%--onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loan/listLoan?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.5");'>--%>
	                                    <%--<span class="menu-text">我的借款记录</span>--%>
	                                <%--</a>--%>
	                            <%--</li>--%>
	                            <%--<li class="${param.activityMenu=='self_m_1.6'?'active bg-themeprimary':''}">--%>
	                                <%--<a href="javascript:void(0);"--%>
	                                <%--onclick='$(".submenu").find(".active").removeClass();$(this).parent().addClass("active bg-themeprimary");menuClick("/financial/loanOff/listLoanOff?sid=${param.sid}&pager.pageSize=10&activityMenu=self_m_1.6");'>--%>
	                                    <%--<span class="menu-text">我的报销记录</span>--%>
	                                <%--</a>--%>
	                            <%--</li>--%>
                        </ul>
                        <!-- 
	                        <%--<c:if test="${userInfo.admin>0}">--%>
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
			                      </ul>
			                   </li>
		                  <%--</c:if>--%>
                         -->
                    </li>
                    <!--Profile-->
                    <%--<li>
                        <a href="#">
                            <i class="menu-icon fa fa-trash-o"></i>
                            <span class="menu-text">回收站</span>
                        </a>
                    </li>
                --%>
                	<c:if test="${userInfo.admin>0}">
                		<li>
	                        <a href="javascript:void(0)" class="menu-dropdown">
	                            <i class="menu-icon fa fa-gear"></i>
	                            <span class="menu-text">基础配置</span>
	                            <i class="menu-expand"></i>
	                        </a>
	                        <ul class="submenu">
	                            <li>
	                                <a href="javascript:void(0)" onclick="settingConsume(1,this)">
	                                    <span class="menu-text">费用类型维护</span>
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

