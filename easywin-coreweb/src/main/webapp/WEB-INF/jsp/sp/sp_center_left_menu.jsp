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
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'sp_m_1'))?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-gavel"></i>
                            <span class="menu-text"> 审批 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='sp_m_1.1')?'active bg-themeprimary':'' }">
                                <a href="/workFlow/listSpToDo?pager.pageSize=10&sid=${param.sid}&activityMenu=sp_m_1.1">
                                    <span class="menu-text">待批审批</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='sp_m_1.2'?'active bg-themeprimary':'' }">
                                <a href="/workFlow/listSpFlowOfMine?pager.pageSize=10&sid=${param.sid}&activityMenu=sp_m_1.2">
                                    <span class="menu-text">我的审批</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='sp_m_1.4'?'active bg-themeprimary':'' }">
                                <a href="/workFlow/listSpFlowOfAtten?pager.pageSize=10&sid=${param.sid}&activityMenu=sp_m_1.4">
                                    <span class="menu-text">我的关注</span>
                                </a>
                            </li>
                            <li class="${param.activityMenu=='sp_m_1.3'?'active bg-themeprimary':'' }">
                                <a href="/workFlow/listSpFlowOfAll?pager.pageSize=10&sid=${param.sid}&activityMenu=sp_m_1.3">
                                    <span class="menu-text">所有审批</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <!--Forms-->
                    <c:if test="${userInfo.admin>0}">
	                    <%-- <li class="${fn:contains(param.activityMenu,'sp_m_3')?'open':''}">
	                        <a href="javascript:void(0)" class="menu-dropdown">
	                            <i class="menu-icon fa fa-cloud"></i>
	                            <span class="menu-text">云表单库</span>
	                            <i class="menu-expand"></i>
	                        </a>
	                        <ul class="submenu">
	                            <li class="${(param.activityMenu=='sp_m_3.1')?'active bg-themeprimary':'' }">
	                                <a href="/form/listPagedCloudFormMod?sid=${param.sid}&activityMenu=sp_m_3.1">
	                                    <span class="menu-text">表单列表</span>
	                                </a>
	                            </li>
	                        </ul>
	                     </li> --%>
                     
                    <li class="${fn:contains(param.activityMenu,'sp_m_2')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-clipboard"></i>
                            <span class="menu-text">配置管理 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li>
                                <a href="javascript:void(0)" onclick="listFormModSort()">
                                    <span class="menu-text">表单分类</span>
                                </a>
                            </li>
                            <li class="${(activityMenu=='sp_m_2.1' || activityMenu=='sp_m_2.1_edit')?'active bg-themeprimary':'' }">
                                <a href="/form/formModList?pager.pageSize=10&sid=${param.sid}&activityMenu=sp_m_2.1&searchTab=21">
                                    <span class="menu-text">审批表单</span>
                                </a>
                            </li>
                            <li>
                                <a href="javascript:void(0)" onclick="listSpFlowType()">
                                    <span class="menu-text">流程分类</span>
                                </a>
                            </li>
                            <li class="${(activityMenu=='sp_m_2.2' || activityMenu=='sp_m_2.2_edit')?'active bg-themeprimary':'' }">
                                <a href="/flowDesign/listFlowModel?pager.pageSize=10&sid=${param.sid}&activityMenu=sp_m_2.2">
                                    <span class="menu-text">审批配置</span>
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

