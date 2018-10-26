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
<script type="text/javascript">
	//按激活状态查询
	function ogranicEnabledSearch(enabled){
		$("#searhOrganic").find("input[name='enabled']").val(enabled);
		$("#searhOrganic").submit();
	}
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		var height = $(window).height() - 150;
		$("#contentBody").css("height", height + "px");
	});
</script>
</head>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">

							<form action="/web/count/listPagedOrgCount" id="searhOrganic">
								<input type="hidden" name="pager.pageSize" value="10" />
								<input type="hidden" name="enabled" value="${organic.enabled}"/>
								<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									
								<div class="btn-group pull-left">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown">状态分类 <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)"
													onclick="ogranicEnabledSearch('')">清除条件</a></li>
												</li>
												<li><a href="javascript:void(0)"
													onclick="ogranicEnabledSearch('1')">启用</a></li>
												</li>
												<li><a href="javascript:void(0)"
													onclick="ogranicEnabledSearch('-1')">未激活</a></li>
												</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="ps-margin ps-search padding-right-10" id="moreCondition_Div">
									<span class="btn-xs">起止时间：</span> <input
										class="form-control dpd2 no-padding condDate" type="text"
										readonly="readonly" value="${organic.startDate}" id="startDate"
										name="startDate" placeholder="开始时间"
										onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared:function(){submitForm()},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
									<span>~</span> <input
										class="form-control dpd2 no-padding condDate" type="text"
										readonly="readonly" value="${organic.endDate}" id="endDate" name="endDate"
										placeholder="结束时间"
										onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared:function(){submitForm()},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
								</div>
								<div class="ps-margin ps-search">
									<span class="input-icon"> 
									<input id="searchOrgName" name="orgName" value="${organic.orgName}" 
									class="form-control ps-input" type="text" placeholder="请输入团队名称" > 
										<a href="#" class="ps-searchBtn">
										<i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
							</form>
						</div>

						<div class="widget-body" id="contentBody"
							style="overflow: hidden;overflow-y:scroll;">
							<table class="table table-striped table-hover"
								id="editabledatatable">
								<thead>
									<tr role="row">
										<th>序号</th>
										<th>团队名称</th>
										<th>注册人</th>
										<th style="text-align: center;">移动电话</th>
										<th style="text-align: center;">注册时间</th>
										<th style="text-align: center;">团队人数</th>
										<th style="text-align: center;">状态</th>
										<th style="text-align: center;">操作</th>
									</tr>
								</thead>
								<tbody id="allTodoBody">
									<c:choose>
										<c:when test="${not empty listOrganic}">
											<c:forEach items="${listOrganic}" var="organic"
												varStatus="org">
												<tr>
													<td class="rowNum">${org.count}</td>
													<td><a href="javascript:void(0)" onclick="showOrganicInfo(${organic.orgNum},${organic.userId})">${organic.orgName}</a></td>
													<td>${organic.linkerName}</td>
													<td style="text-align: center;">${organic.linkerMovePhone}</td>
													<td style="text-align: center;">${organic.recordCreateTime}</td>
													<td style="text-align: center;">${organic.members}</td>
													<td style="text-align: center;" class="enabled ${organic.enabled==1?'green':'red'}">${organic.enabled==1?'启用':'未激活'}</td>
													<td style="text-align: center;"><a href="javascript:void(0)" onclick="listPagedOrgSysLog(${organic.orgNum})">操作日志</a></td>
												</tr>
											</c:forEach>

										</c:when>
									</c:choose>
								</tbody>
							</table>
							<tags:pageBar url="/web/count/listPagedOrgCount"></tags:pageBar>
						</div>
					</div>
				</div>
			</div>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->
	</div>
</body>
</html>

