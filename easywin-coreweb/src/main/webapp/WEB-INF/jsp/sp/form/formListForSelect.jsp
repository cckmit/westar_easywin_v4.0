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
		<meta charset="utf-8" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<!-- 框架样式 -->
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	    <script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<script type="text/javascript" charset="utf-8" src="/static/js/formJs/formCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	    <script type="text/javascript">
	     var sid="${param.sid}";//申明一个sid全局变量
		 var searchTab = '${param.searchTab}';
		//关闭窗口
		 function closeWin(){
		 	var winIndex = window.top.layer.getFrameIndex(window.name);
		 	closeWindow(winIndex);
		 }
		
		$(function(){
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
		})
	    </script>
	</head>
	<body>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Content -->
				<div class="page-content">
					<!-- Page Body -->
					<div class="page-body no-padding">
								<div class="row">
									<div class="col-md-12 col-xs-12 " >
										<div class="widget">
											<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
												<form action="/form/formListForSelect" id="searhForm">
													<input type="hidden" name="sid" value="${param.sid }" /> <input
														type="hidden" name="searchTab" value="${param.searchTab}" />
													<input type="hidden" name="activityMenu"
														value="${param.activityMenu}">
													<div class="btn-group pull-left">
														<div class="table-toolbar ps-margin">
															<div class="btn-group">
																<a class="btn btn-default dropdown-toggle btn-xs"
																	data-toggle="dropdown"> <c:choose>
																		<c:when test="${not empty formMod.formSortId}">
																			<font style="font-weight:bold;">${formMod.modSortName}
																			</font>
																		</c:when>
																		<c:otherwise>分类</c:otherwise>
																	</c:choose> <i class="fa fa-angle-down"></i>
																</a>
																<ul class="dropdown-menu dropdown-default">
																	<input type="hidden" name="formSortId"
																		value="${formMod.formSortId}" />
																	<input type="hidden" name="modSortName"
																		value="${formMod.modSortName}" />
																	<li><a href="javascript:void(0)"
																		onclick="formModSortSearch(-1,this)">不限条件</a></li>
																	<c:choose>
																		<c:when test="${not empty listFormSort}">
																			<c:forEach items="${listFormSort}" var="sortObj"
																				varStatus="vs">
																				<li><a href="javascript:void(0)"
																					onclick="formModSortSearch(${sortObj.id},this)">${sortObj.sortName }</a>
																				</li>
																			</c:forEach>
																		</c:when>
																	</c:choose>
																	<li><a href="javascript:void(0)"
																		onclick="formModSortSearch(0,this)">其他</a></li>
																</ul>
															</div>
														</div>
													</div>
													<div class="ps-margin ps-search">
														<span class="input-icon no-padding no-margin"> <input id="modName"
															name="modName" value="${formMod.modName}"
															class="form-control ps-input" type="text"
															placeholder="请输入关键字"> <a href="javascript:void(0)"
															class="ps-searchBtn"><i
																class="glyphicon glyphicon-search circular danger"></i> </a>
														</span>
													</div>
												</form>
												<div class="widget-buttons ps-widget-buttons">
													<%-- <a href="/form/listPagedCloudFormMod?sid=${param.sid}&activityMenu=sp_m_3.1">
														<button class="btn btn-info btn-primary btn-xs" type="button" isCloude="true">
															<i class="fa fa-plus"></i>表单下载
														</button>
													</a> --%>
													<a href="javascript:void(0)" onclick="closeWin();" title="关闭">
															<i class="fa fa-times themeprimary"></i>
														</a>
												</div>
											</div>
										
								<c:choose>
								<c:when test="${not empty formModlist}">
										<div class="widget-body" id="contentBody" style="overflow:hidden;overflow-y:scroll; ">
											<table class="table table-striped table-hover"
												id="editabledatatable">
												<thead>
													<tr role="row">
														<th>
															序号
														</th>
														<th>
															名称
														</th>
														<th style="text-align: center;">
															分类
														</th>
														<th style="text-align: center;">
															创建时间
														</th>
														<th style="text-align: center;">
															操作
														</th>
													</tr>
												</thead>
												<tbody id="allTodoBody">
													<c:choose>
														<c:when test="${not empty formModlist}">
															<c:forEach items="${formModlist}" var="formModVo" varStatus="vs">
																<tr>
																	<td class="rowNum">
																		${vs.count}
																	</td>
																	<td>
																		${formModVo.modName }
																	</td>
																	<td style="text-align: center;" class="optUpdataSort" title="${formModVo.enable==1?'修改分类':''}">
																		${formModVo.modSortName}
																	</td>
																	<td style="text-align: center;">
																		${fn:substring(formModVo.recordCreateTime,0,16) }
																	</td>
																	<td class="optTd" style="text-align: center;">
																		<a href="javascript:void(0)" onclick="viewFormMod(${formModVo.id})">预览</a>|
																		<a href="/workFlow/startSpFlowByUserDefined?sid=${param.sid}&formId=${formModVo.id}">采用</a>
																	</td>
																</tr>
															</c:forEach>
														</c:when>
													</c:choose>
												</tbody>
											</table>
											<tags:pageBar url="/form/formListForSelect"></tags:pageBar>
										</div>
										</c:when>
										<c:otherwise>
											<div class="widget-body" id="contentBody" style="overflow:hidden;overflow-y:scroll; ">
												<section class="error-container text-center" style="padding-top: 20%">
													<h1>
														<i class="fa fa-exclamation-triangle"></i>
													</h1>
													<div class="error-divider">
														<h2>当前没有可用的表单！</h2>
														<p class="description">协同提高效率，分享拉近距离。</p>
														<%-- <a href="/form/listPagedCloudFormMod?sid=${param.sid}&activityMenu=sp_m_3.1"
															class="return-btn"><i class="fa fa-plus"></i>表单下载</a> --%>
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
	        </div>
	    </div>
	     <!--Beyond Scripts-->
    	<script src="/static/assets/js/beyond.min.js"></script>
    	
    	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
    	<script src="/static/assets/js/bootstrap.min.js"></script>
	</body>
</html>

