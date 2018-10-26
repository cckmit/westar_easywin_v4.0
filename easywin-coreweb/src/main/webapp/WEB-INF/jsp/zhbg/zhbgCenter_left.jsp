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
<head></head>
<body>
	<div class="page-sidebar" id="sidebar">
		<ul class="nav sidebar-menu">
			<li class="${(empty recycleTab && param.activityMenu == '025')?'open':''}">
				<a href="javascript:void(0);" class="menu-dropdown">
					<i class="menu-icon fa fa-car"></i> <span class="menu-text"> 车辆管理 </span> <i class="menu-expand"></i>
				</a>

				<ul class="submenu">
					<li class="${( param.searchTab == '251' && param.activityMenu == '025')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/car/listCarApplyOfMinPage?activityMenu=025&searchTab=251&sid=${param.sid}&pager.pageSize=10');">
							<span class="menu-text">我的申请</span>
						</a>
					</li>
					<li class="${( param.searchTab == '252' && param.activityMenu == '025')?'active bg-themeprimary':'' }" style="display: none;" searchTab="252">
						<a href="javascript:void(0);" onclick="menuClick('/car/listCarApplyToDoPage?activityMenu=025&searchTab=252&sid=${param.sid}&pager.pageSize=10');">
							<span class="menu-text">审核申请</span>
						</a>
					</li>
					<li class="${( param.searchTab == '254' && param.activityMenu == '025')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/car/listCarOfAllPage?activityMenu=025&searchTab=254&sid=${param.sid}&pager.pageSize=10');">
							<span class="menu-text">所有车辆</span>
						</a>
					</li>
				</ul>
			</li>
			<li class="${(empty recycleTab && param.activityMenu == '027')?'open':''}">
				<a href="javascript:void(0);" class="menu-dropdown">
					<i class="menu-icon fa fa-building-o"></i> <span class="menu-text">办公用品 </span> <i class="menu-expand"></i>
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
			<li class="${(empty recycleTab && param.activityMenu == '028')?'open':''}"  style="display: none;" busType="028">
				<a href="javascript:void(0);" class="menu-dropdown">
					<i class="menu-icon fa fa-building-o"></i> <span class="menu-text">人事档案 </span> <i class="menu-expand"></i>
				</a>

				<ul class="submenu">
					<li class="${( param.searchTab == '281' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/rsdaBase/listPagedRsdaBase?pager.pageSize=10&activityMenu=028&searchTab=281&sid=${param.sid}');">
							<span class="menu-text">档案查询</span>
						</a>
					</li>
					<li class="${( param.searchTab == '282' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/jobHistory/listPagedJobHistory?pager.pageSize=10&activityMenu=028&searchTab=282&sid=${param.sid}');">
							<span class="menu-text">工作经历</span>
						</a>
					</li>
					<li class="${( param.searchTab == '283' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/rsdaInc/listPagedRsdaInc?pager.pageSize=10&activityMenu=028&searchTab=283&sid=${param.sid}');">
							<span class="menu-text">奖惩管理</span>
						</a>
					</li>
					<li class="${( param.searchTab == '284' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/rsdaTrance/listPagedRsdaTrance?pager.pageSize=10&activityMenu=028&searchTab=284&sid=${param.sid}');">
							<span class="menu-text">人事调动</span>
						</a>
					</li>
					<li class="${( param.searchTab == '285' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/rsdaLeave/listPagedRsdaLeave?pager.pageSize=10&activityMenu=028&searchTab=285&sid=${param.sid}');">
							<span class="menu-text">离职管理</span>
						</a>
					</li>
					<li class="${( param.searchTab == '286' && param.activityMenu == '028')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/rsdaResume/listPagedRsdaResume?pager.pageSize=10&activityMenu=028&searchTab=286&sid=${param.sid}');">
							<span class="menu-text">复职管理</span>
						</a>
					</li>
				</ul>
			</li>
			<li class="${(empty recycleTab && param.activityMenu == '026')?'open':''}"  style="display: none;" busType="026">
				<a href="javascript:void(0);" class="menu-dropdown">
					<i class="menu-icon fa fa-briefcase"></i> <span class="menu-text"> 固定资产管理 </span> <i class="menu-expand"></i>
				</a>

				<ul class="submenu">
					<li class="${param.activityMenu == '026' ?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/gdzc/listGdzcPage?activityMenu=026&sid=${param.sid}&pager.pageSize=10');">
							<span class="menu-text">固定资产列表</span>
						</a>
					</li>
				</ul>
			</li>
			<li class="${(empty recycleTab && param.activityMenu == '040')?'open':''}">
				<a href="javascript:void(0);" class="menu-dropdown">
					<i class="menu-icon fa fa-university "></i> <span class="menu-text"> 制度管理 </span> <i class="menu-expand"></i>
				</a>

				<ul class="submenu">
					<li class="${( param.searchTab == '41' && param.activityMenu == '040')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/institution/listPagedInstitu?activityMenu=040&searchTab=41&sid=${param.sid}&pager.pageSize=10');">
							<span class="menu-text">所有制度</span>
						</a>
					</li>
					<li class="${( param.searchTab == '42' && param.activityMenu == '040')?'active bg-themeprimary':'' }">
						<a href="javascript:void(0);" onclick="menuClick('/jfzb/listPagedJfzb?activityMenu=040&searchTab=42&sid=${param.sid}&pager.pageSize=10');">
							<span class="menu-text">评分指标</span>
						</a>
					</li>
				</ul>
			</li>
			<li class="setting" style="display: none;">
				<a href="javascript:void(0);" class="menu-dropdown">
					<i class="menu-icon fa fa-gear"></i> <span class="menu-text"> 基础配置 </span> <i class="menu-expand"></i>
				</a>
				<ul class="submenu">
					<li class="" style="display: none;" busType="040">
						<a href="javascript:void(0);" onclick="setting('401',this);">
							<span class="menu-text">制度类型配置</span>
						</a>
					</li>
					<li class="" style="display: none;" busType="02501">
						<a href="javascript:void(0);" onclick="setting('251','this');">
							<span class="menu-text">车辆类型配置</span>
						</a>
					</li>
					<li class="" style="display: none;" busType="02601">
						<a href="javascript:void(0);" onclick="settingGdzcType('02601',this);" >
							<span class="menu-text">资产所属类型配置</span>
						</a>
					</li>
					<li class="" style="display: none;" busType="02602">
						<a href="javascript:void(0);" onclick="settingGdzcType('02602',this);">
							<span class="menu-text">资产增加类型配置</span>
						</a>
					</li>
					<li class="" style="display: none;" busType="02603">
						<a href="javascript:void(0);" onclick="settingGdzcType('02603',this);">
							<span class="menu-text">资产减少类型配置</span>
						</a>
					</li>
					<li class="" style="display: none;" busType="02604">
						<a href="javascript:void(0);" onclick="settingGdzcType('02604',this);" >
							<span class="menu-text">资产维护类型配置</span>
						</a>
					</li>
				</ul>
			</li>
		</ul>
	</div>
</body>
</html>

