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
                    <li class="${(empty param.activityMenu || fn:contains(param.activityMenu,'m_1'))?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">团队配置 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_1.1')?'active bg-themeprimary':'' }">
                                <a href="">
                                    <span class="menu-text">菜单一</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_2')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">审批配置 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_.1')?'active bg-themeprimary':'' }">
                                <a href="">
                                    <span class="menu-text">菜单一</span>
                                </a>
                            </li>
                            <c:if test="${userInfo.admin>0}">
			                    <li class="${fn:contains(param.activityMenu,'m_3')?'open':''}">
			                        <a href="javascript:void(0)" class="menu-dropdown">
			                            <i class="menu-icon fa fa-cloud"></i>
			                            <span class="menu-text">云表单库</span>
			                            <i class="menu-expand"></i>
			                        </a>
			                        <ul class="submenu">
			                            <li class="${(param.activityMenu=='m_3.1')?'active bg-themeprimary':'' }">
			                                <a href="/form/listPagedCloudFormMod?sid=${param.sid}&activityMenu=m_3.1">
			                                    <span class="menu-text">表单列表</span>
			                                </a>
			                            </li>
			                        </ul>
			                     </li>
			                    <li class="${fn:contains(param.activityMenu,'m_2')?'open':''}">
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
			                            <li class="${(param.activityMenu=='m_2.1' || param.activityMenu=='m_2.1_edit')?'active bg-themeprimary':'' }">
			                                <a href="/form/formModList?pager.pageSize=10&sid=${param.sid}&activityMenu=m_2.1&searchTab=21">
			                                    <span class="menu-text">审批表单</span>
			                                </a>
			                            </li>
			                            <li>
			                                <a href="javascript:void(0)" onclick="listSpFlowType()">
			                                    <span class="menu-text">流程分类</span>
			                                </a>
			                            </li>
			                            <li class="${(param.activityMenu=='m_2.2' || param.activityMenu=='m_2.2_edit')?'active bg-themeprimary':'' }">
			                                <a href="/flowDesign/listFlowModel?pager.pageSize=10&sid=${param.sid}&activityMenu=m_2.2">
			                                    <span class="menu-text">审批配置</span>
			                                </a>
			                            </li>
			                            <li>
			                               <a href="javascript:void(0)" id="forceInBtn">
			                                   <span class="menu-text">监督人员设置</span>
			                               </a>
			                           </li>
			                        </ul>
			                    </li>
		                    </c:if>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_3')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">费用管理配置 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(param.activityMenu=='m_3.1')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.1&busType=029">
                                    <span class="menu-text">出差计划配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_3.2')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.2&busType=030">
                                    <span class="menu-text">出差借款配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_3.4')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.4&busType=03401">
                                    <span class="menu-text">出差总结配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_3.7')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.7&busType=03101">
                                    <span class="menu-text">出差报销配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_3.6')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.6&busType=035">
                                    <span class="menu-text">一般借款申请配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_3.8')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.8&busType=031">
                                    <span class="menu-text">一般借款配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_3.3')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.3&busType=03102">
                                    <span class="menu-text">一般费用报销配置</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_11')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">需求管理 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(param.activityMenu=='m_11.1')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_11.1&busType=070">
                                    <span class="menu-text">需求管理配置</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_4')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">考勤配置 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_4.1')?'active bg-themeprimary':'' }">
                                <a href="/attence/viewAttenceSet?sid=${param.sid}&activityMenu=m_4.1">
                                    <span class="menu-text">考勤管理</span>
                                </a>
                            </li>
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_4.2')?'active bg-themeprimary':'' }">
                                <a href="/festMod/listFestMod?sid=${param.sid}&activityMenu=m_4.2">
                                    <span class="menu-text">节假维护</span>
                                </a>
                            </li>
                              <li class="${(param.activityMenu=='m_4.3')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_4.3&busType=036">
                                    <span class="menu-text">请假数据映射</span>
                                </a>
                            </li>
                             <li class="${(param.activityMenu=='m_4.4')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_4.4&busType=037">
                                    <span class="menu-text">加班数据映射</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_4.5')?'active bg-themeprimary':'' }">
                                <a href="/attence/attenceConnSet?pager.pageSize=10&sid=${param.sid}&activityMenu=m_4.5">
                                    <span class="menu-text">考勤机连接配置</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_5')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">客户配置 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_5.1')?'active bg-themeprimary':'' }">
                                <a href="">
                                    <span class="menu-text">菜单一</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_6')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">模块监督人员 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_6.1')?'active bg-themeprimary':'' }">
                                <a href="/forceIn/editForceInPersionPage?sid=${param.sid }&activityMenu=m_6.1">
                                    <span class="menu-text">监督人员设定</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_7')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">综合办公配置 </span>

                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(empty param.activityMenu || param.activityMenu=='m_7.1')?'active bg-themeprimary':'' }">
                                <a href="/modAdmin/editModAdminPage?sid=${param.sid}&activityMenu=m_7.1">
                                    <span class="menu-text">管理员设定</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <%-- <li class="${fn:contains(param.activityMenu,'m_8')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">权限控制 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(param.activityMenu=='m_8.1')?'active bg-themeprimary':'' }">
                                <a href="">
                                    <span class="menu-text">监督权限</span>
                                    <span class="menu-text">编辑权限</span>
                                </a>
                            </li>
                        </ul>
                    </li> --%>
                    <li class="${fn:contains(param.activityMenu,'m_9')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">运维过程管理配置 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(param.activityMenu=='m_9.1')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_9.1&busType=04201">
                                    <span class="menu-text">事件过程映射关系配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_9.2')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_9.2&busType=04202">
                                    <span class="menu-text">问题过程映射关系配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_9.3')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_9.3&busType=04203">
                                    <span class="menu-text">变更过程映射关系配置</span>
                                </a>
                            </li>
                            <li class="${(param.activityMenu=='m_9.4')?'active bg-themeprimary':'' }">
                                <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_9.4&busType=04204">
                                    <span class="menu-text">发布过程映射关系配置</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="${fn:contains(param.activityMenu,'m_10')?'open':''}">
                        <a href="javascript:void(0)" class="menu-dropdown">
                            <i class="menu-icon fa fa-cog"></i>
                            <span class="menu-text">序列编号配置 </span>
                            <i class="menu-expand"></i>
                        </a>
                        <ul class="submenu">
                            <li class="${(param.activityMenu=='m_10.1')?'active bg-themeprimary':'' }">
                                <a href="/serialNum/listPagedSerialNum?pager.pageSize=10&sid=${param.sid}&activityMenu=m_10.1">
                                    <span class="menu-text">序列编号管理</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <!-- /Sidebar Menu -->
            </div>
	</body>
</html>

