<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
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
			<li class="${(empty recycleTab && param.activityMenu == '025')?'open':''}">
				<a href="javascript:void(0);" class="menu-dropdown"> <i class="menu-icon fa fa-car"></i> <span class="menu-text"> 车辆管理 </span> <i class="menu-expand"></i>
				</a>

				<ul class="submenu">
					<li class="${( param.searchTab == '251' && param.activityMenu == '025')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/car/listCarApplyOfMinPage?activityMenu=025&searchTab=251&sid=${param.sid}&pager.pageSize=10');"> <span class="menu-text">我的申请</span>
						</a>
					</li>
					<c:if test="${isModAdmin }">
						<li class="${( param.searchTab == '252' && param.activityMenu == '025')?'active bg-themeprimary':'' }">
							<a href="javascript:void(0);" onclick="menuClick('/car/listCarApplyToDoPage?activityMenu=025&searchTab=252&sid=${param.sid}&pager.pageSize=10');"> <span class="menu-text">审核申请</span>
							</a>
						</li>
					</c:if>
					<li class="${( param.searchTab == '254' && param.activityMenu == '025')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/car/listCarOfAllPage?activityMenu=025&searchTab=254&sid=${param.sid}&pager.pageSize=10');"> <span class="menu-text">所有车辆</span>
						</a>
					</li>

				</ul>
			</li>
			<li class="${(empty recycleTab && param.activityMenu == '026')?'open':''}">
                  <a href="javascript:void(0);" class="menu-dropdown">
                      <i class="menu-icon fa fa-briefcase"></i>
                      <span class="menu-text"> 固定资产管理 </span>
                      <i class="menu-expand"></i>
                  </a>

                  <ul class="submenu">
                      <li class="${param.activityMenu == '026' ?'active bg-themeprimary':'' }">
                          <a href="javascript:void(0);" onclick="menuClick('/gdzc/listGdzcPage?activityMenu=026&sid=${param.sid}&pager.pageSize=10');">
                              <span class="menu-text">固定资产列表</span>
                          </a>
                      </li>
                  </ul>
              </li>
                    
			<c:if test="${userInfo.admin>0 }">
				<li class="${(param.activityMenu == '024')?'open':''}">
					<a href="javascript:void(0);" class="menu-dropdown"> <i class="menu-icon fa fa-gear"></i> <span class="menu-text"> 基础配置 </span> <i class="menu-expand"></i>
					</a>

					<ul class="submenu">
					<!-- 
						<li class="">
							<a href="javascript:void(0);" id="zhbgModAdmin" bustype="025"> <span class="menu-text">模块管理人员</span>
							</a>
						</li>
					 -->
						<li class="">
							<a href="javascript:void(0);" onclick="setting('251','this');"> <span class="menu-text">车辆类型配置</span>
							</a>
						</li>
					</ul>
				</li>
			</c:if>
		</ul>
	</div>
</body>
</html>

