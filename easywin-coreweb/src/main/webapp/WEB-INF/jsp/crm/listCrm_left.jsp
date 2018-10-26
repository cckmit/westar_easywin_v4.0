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
                 	<li class="${(empty recycleTab && (empty param.searchTab || (param.searchTab - param.searchTab mod 10)/10 eq 1))?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                           	<i class="menu-icon fa fa-users"></i>
                            <span class="menu-text">客户</span>
                            <i class="menu-expand"></i>
                        </a>

                        <ul class="submenu">
                            <li class="${(empty recycleTab && (empty param.searchTab || param.searchTab==11))?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchCrm(11)">
                                    <span class="menu-text">我的客户</span>
                                </a>
                            </li>
                            <li class="${param.searchTab==12?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0)" onclick="searchCrm(12)">
                                    <span class="menu-text">我的关注</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.countSub>0}">
	                            <li class="${param.searchTab==13?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="searchCrm(13)">
	                                    <span class="menu-text">下属客户</span>
	                                </a>
	                            </li>
                            </c:if>
                            <li class="${param.searchTab==15?'active bg-themeprimary':'' }">
                                 <a href="javascript:void(0)" onclick="searchCrm(15)">
                                    <span class="menu-text">所有客户</span>
                                </a>
                            </li>
                              <li class="${param.searchTab==16?'active bg-themeprimary':'' }">
                                <a href="javascript:void(0)" onclick="searchCrm(16)">
                                    <span class="menu-text">资金预算</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <%--<li>
                        <a href="javascript:void(0)">
                            <i class="menu-icon fa fa-dedent "></i>
                            <span class="menu-text">客户列表</span>
                        </a>
                    </li>
                   --%>
                   <%--
	                   <li class="${(((param.searchTab - param.searchTab mod 10)/10 eq 2))?'open':'' }">
	                        <a href="javascript:void(0)" class="menu-dropdown">
	                            <i class="menu-icon fa fa-bar-chart-o "></i>
	                            <span class="menu-text">统计分析</span>
	                            <i class="menu-expand"></i>
	                        </a>
	                        <ul class="submenu">
	                            <li class="${param.searchTab==21?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="statisticsCrm(21,1)">
	                                    <span class="menu-text">分类统计</span>
	                                </a>
	                            </li>
	                            <li class="${param.searchTab==22?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="statisticsCrm(22,2)">
	                                    <span class="menu-text">更新统计</span>
	                                </a>
	                            </li>
	                            <li class="${param.searchTab==23?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="statisticsCrm(23,3)">
	                                    <span class="menu-text">分布统计</span>
	                                </a>
	                            </li>
	                            <li class="${param.searchTab==24?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="statisticsCrm(24,4)">
	                                    <span class="menu-text">归属统计</span>
	                                </a>
	                            </li>
	                            <li class="${param.searchTab==25?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="statisticsCrm(25,5)">
	                                    <span class="menu-text">增长趋势分析</span>
	                                </a>
	                            </li>
	                             <li class="${param.searchTab==26?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="statisticsCrm(26,6)">
	                                    <span class="menu-text">所属阶段统计</span>
	                                </a>
	                            </li>
	                             <li class="${param.searchTab==27?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="statisticsCrm(27,7)">
	                                    <span class="menu-text">资金预算统计</span>
	                                </a>
	                            </li>
	                        </ul>
	                   </li>
                    --%>
                   <li class="${(((param.searchTab - param.searchTab mod 10)/10 eq 3))?'open':'' }">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-gear"></i>
                            <span class="menu-text">基础配置</span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li>
                                <a href="javascript:void(0)" onclick="settingCrm(31,this)">
                                    <span class="menu-text">客户类型维护</span>
                                </a>
                            </li>
                            <li>
                                 <a href="javascript:void(0)" onclick="settingCrm(32,this)">
                                    <span class="menu-text">反馈类型维护</span>
                                </a>
                            </li>
                            <li>
                                 <a href="javascript:void(0)" onclick="settingCrm(33,this)">
                                    <span class="menu-text">客户区域维护</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.admin>0}">
                              <li>
                                <a href="javascript:void(0)" onclick="settingCrm(36,this)">
                                    <span class="menu-text">客户阶段维护</span>
                                </a>
                           	 </li>
	                            <!-- <li>
	                                <a href="javascript:void(0)" id="forceInBtn">
	                                    <span class="menu-text">监督人员设置</span>
	                                </a>
	                            </li>
	                            <li>
	                                <a href="javascript:void(0)" onclick="settingCrm(35,this)">
	                                    <span class="menu-text">操作权限设置</span>
	                                </a>
	                            </li>
	                            <li>
	                                <a href="javascript:void(0)" onclick="settingCrm(38,this)">
	                                    <span class="menu-text">变更审批设置</span>
	                                </a>
	                            </li> -->
	                            <li class="${param.searchTab==39?'active bg-themeprimary':'' }">
	                                <a href="javascript:void(0)" onclick="settingCrm(39,this)">
	                                    <span class="menu-text">客户操作配置</span>
	                                </a>
	                            </li>
                            </c:if>
                        </ul>
                    </li>
                   
                    <li class="${not empty recycleTab?'open active ':''}">
                        <a href="javascript:void(0)" onclick="listRecyBin('012','${param.sid}')">
                            <i class="menu-icon fa fa-trash-o"></i>
                            <span class="menu-text">回收站</span>
                        </a>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

