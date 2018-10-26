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
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			
					<div class="row">
						<div class="col-md-12 col-xs-12 ">
							<div class="widget">
								<div
									class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
									<!-- <div class="checkbox ps-margin pull-left">
								<label> <input type="checkbox" class="colored-blue">
									<span class="text">全选</span>
								</label>
							</div> -->
									<form action="/flowDesign/listFlowModel" id="searchForm">
										<input type="hidden" name="activityMenu"
											value="${param.activityMenu}"> <input type="hidden"
											name="sid" value="${param.sid}"> <input type="hidden"
											name="searchTab" value="${param.searchTab}"> <input
											type="hidden" name="flowName" value="${spFlowModel.flowName}">
										<input type="hidden" name="spFlowTypeId"
											value="${spFlowModel.spFlowTypeId}"> <input
											type="hidden" name="spFlowTypeName"
											value="${spFlowModel.spFlowTypeName}"> <input
											type="hidden" name="orderBy" value="${spFlowModel.orderBy}">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs"
														data-toggle="dropdown" title="排序"> <c:choose>
															<c:when test="${not empty spFlowModel.orderBy}">
																<font style="font-weight:bold;"> <c:if
																		test="${spFlowModel.orderBy=='flowNameDesc'}">按流程名称(降序)</c:if>
																	<c:if test="${spFlowModel.orderBy=='flowNameAsc'}">按流程名称(升序)</c:if>
																	<c:if test="${spFlowModel.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if>
																	<c:if test="${spFlowModel.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
																</font>
															</c:when>
															<c:otherwise>排序</c:otherwise>
														</c:choose> <i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li><a href="javascript:void(0)"
															onclick="orderByClean()">不限条件</a></li>
														<li><a href="javascript:void(0)"
															onclick="orderBy('flowNameDesc');">按流程名称(降序)</a></li>
														<li><a href="javascript:void(0)"
															onclick="orderBy('flowNameAsc');">按流程名称(升序)</a></li>
														<li><a href="javascript:void(0)"
															onclick="orderBy('crTimeNewest');">按创建时间(降序)</a></li>
														<li><a href="javascript:void(0)"
															onclick="orderBy('crTimeOldest');">按创建时间(升序)</a></li>
													</ul>
												</div>
											</div>
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs"
														data-toggle="dropdown"> <c:choose>
															<c:when test="${not empty spFlowModel.spFlowTypeId}">
																<font style="font-weight:bold;">${spFlowModel.spFlowTypeName}
																</font>
															</c:when>
															<c:otherwise>流程分类筛选</c:otherwise>
														</c:choose> <i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<c:choose>
															<c:when test="${not empty listSpFlowType}">
																<li><a href="javascript:void(0)"
																	onclick="spFlowTypeClean();">不限条件</a></li>
																<c:forEach items="${listSpFlowType}" var="spFlowType"
																	varStatus="status">
																	<li><a href="javascript:void(0)"
																		typeId="${spFlowType.id}"
																		typeName="${spFlowType.typeName}"
																		onclick="selectBySpFlowType(this);">${spFlowType.typeName}</a></li>
																</c:forEach>
																<li><a href="javascript:void(0)" typeId="0"
																	onclick="selectBySpFlowType(this);">未分类</a></li>
															</c:when>
															<c:otherwise>
																<li><a href="javascript:void(0)">无流程分类</a></li>
															</c:otherwise>
														</c:choose>
													</ul>
												</div>
											</div>
										</div>
									</form>
									<div class="ps-margin ps-search">
										<span class="input-icon"> <input
											class="form-control ps-input" id="searchFlowName"
											value="${spFlowModel.flowName}" type="text"
											placeholder="请输入关键字"> <a href="#"
											class="ps-searchBtn"><i
												class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button"
											onclick="addFlowPage();">
											<i class="fa fa-plus"></i> 新建流程
										</button>
									</div>
								</div>
								<c:choose>
				<c:when test="${not empty listFlowModel}">
								<div class="widget-body">
									<form action="/flowDesign/delFlow" id="delForm">
										<input type="hidden" name="sid" value="${param.sid}">
										<input type="hidden" name="pager.pageSize" value="10">
										<input type="hidden" name="flowId" /> <input type="hidden"
											name="activityMenu" value="${activityMenu}" />
										<table class="table table-striped table-hover"
											id="editabledatatable">
											<thead>
												<tr role="row">
													<th>序号</th>
													<th>流程名称</th>
													<th>流程类型</th>
													<th>状态</th>
													<th>创建人</th>
													<th>创建日期</th>
													<th>操作</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listFlowModel}" var="flow"
													varStatus="vs">
													<tr>
														<td>${vs.count}</td>
														<td><a
															href="/flowDesign/editFlowPage?flowId=${flow.id}&sid=${param.sid}&activityMenu=sp_m_2.2_edit"><tags:cutString
																	num="31">${flow.flowName}</tags:cutString></a></td>
														<td><c:choose>
																<c:when test="${flow.spFlowTypeId==0}">无类别</c:when>
																<c:otherwise>${flow.spFlowTypeName}</c:otherwise>
															</c:choose></td>
														<td><c:if test="${flow.status==1}">
																<span style="color:green;">启用</span>
															</c:if> <c:if test="${flow.status==0}">
																<span style="color:red;">禁用</span>
															</c:if></td>
														<td><span>${flow.creatorName}</span></td>
														<td>${flow.recordCreateTime}</td>
														<td><a
															href="/flowDesign/editFlowPage?flowId=${flow.id}&sid=${param.sid}&activityMenu=sp_m_2.2_edit">编辑</a>
															<c:if test="${flow.deployed==0}"> 
													|&nbsp;
													<a
																	href="/flowDesign/spFlowDeploy?flowId=${flow.id}&sid=${param.sid}">部署流程</a>
															</c:if> <c:if test="${flow.status==1}">
													|&nbsp;
													<a
																	href="/flowDesign/updateFlowAttr?id=${flow.id}&sid=${param.sid}&activityMenu=sp_m_2.2&attrType=status&status=0"
																	class="enabled red">禁用</a>
															</c:if> <c:if test="${flow.status==0}">
													|&nbsp;
													<a
																	href="/flowDesign/updateFlowAttr?id=${flow.id}&sid=${param.sid}&activityMenu=sp_m_2.2&attrType=status&status=1"
																	class="enabled green">启用</a>
															</c:if> |&nbsp; <a
															href="/flowDesign/cloneFlowModel?flowId=${flow.id}&sid=${param.sid}">克隆</a>
															<c:if test="${empty flow.act_deployment_id }">
													|&nbsp;
													<a href="javascript:void(0);"
																	onclick="delFlowModel(${flow.id});">删除</a>
															</c:if></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/flowDesign/listFlowModel"></tags:pageBar>
								</div>
								</c:when>
				<c:otherwise>
					<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>还没有配置部署相关的团队固化流程哦！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
								<a href="javascript:void(0);" onclick="addFlowPage();"
									class="return-btn"><i class="fa fa-plus"></i>新建流程</a>
							</div>
						</section>
					</div>
				</c:otherwise>
			</c:choose>
							</div>
						</div>
					</div>
				
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<!-- /Page Container -->
</body>
</html>

