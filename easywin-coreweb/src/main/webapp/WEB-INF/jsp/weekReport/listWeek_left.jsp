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
                 	<li class="${(empty param.searchTab || (param.searchTab - param.searchTab mod 10)/10 eq 1)?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                        	<i class="fa fa-tags"></i>
                            <span class="menu-text">周报模块</span>
                             <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty param.searchTab || param.searchTab==11)?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="menuClick('/weekReport/listPagedWeekRep?sid=${param.sid}&searchTab=11');">
                                    <span class="menu-text">全部汇报</span>
                                </a>
                            </li>
                            <li class="${(param.searchTab==12)?'active bg-themeprimary':''}">
                                 <a href="javascript:void(0)" onclick="menuClick('/weekReport/listPagedWeekRep?sid=${param.sid}&weekerType=0&searchTab=12');">
                                    <span class="menu-text">我的汇报</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.countSub>0}">
	                            <li class="${(param.searchTab==13)?'active bg-themeprimary':''}">
	                                 <a href="javascript:void(0)" onclick="menuClick('/weekReport/listPagedWeekRep?sid=${param.sid}&weekerType=1&searchTab=13');">
	                                    <span class="menu-text">下属汇报</span>
	                                </a>
	                            </li>
                            </c:if>
                        </ul>
                    </li>
                 	<li class="${((param.searchTab - param.searchTab mod 10)/10 eq 2)?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                        	<i class="menu-icon fa fa-gear"></i>
                            <span class="menu-text">基础配置</span>
                             <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                       	 <c:if test="${userInfo.admin>0}">
                            <li class="${(param.searchTab==21)?'active bg-themeprimary':''}">
                                <a href="javascript:void(0)" onclick="menuClick('/weekReport/addWeekRepModPage?sid=${param.sid}&searchTab=21')">
                                    <span class="menu-text">模板设置</span>
                                </a>
                            </li>
                            
                             <li >
                                <a href="javascript:void(0)" onclick="subTimeSet('${param.sid}',this)">
                                    <span class="menu-text">汇报时间设置</span>
                                </a>
                            </li>
                       	 </c:if>
                            <li>
                                 <a href="javascript:void(0)" id="weekRepSetBtn" onclick="setWeekRepViewScope('${param.sid}',this)">
                                    <span class="menu-text">汇报范围</span>
                                    <font style="font-size:12px;color:red;"></font>
                                </a>
                            </li>
                             <c:if test="${userInfo.admin>0}">
	                            <li>
	                                <a href="javascript:void(0)" id="forceInBtn">
	                                    <span class="menu-text">监督人员设置</span>
	                                </a>
	                            </li>
	                            <li>
	                                <a href="javascript:void(0)" onclick="modOperateConfig('${param.sid}','006',this)">
	                                    <span class="menu-text">操作权限设置</span>
	                                </a>
	                            </li>
                            </c:if>
                        </ul>
                    </li>
                    
                    
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

