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
	
</script>
</head>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-sm-12">
					<div class="widget">
						<div
							class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div class="checkbox ps-margin pull-left">
								<label> <input type="checkbox" class="colored-blue"
									onclick="checkAll(this,'ids')" id="checkAllBox"> <span
									class="text">全选</span>
								</label>
							</div>
							<form action="/web/help/listQus" id="searchHelpForm">
								<input type="hidden" id="pId" name="pId"
									value="${empty pId?0:pId}"> <input type="hidden"
									name="searchTab" id="searchTab" value="${param.searchTab}">
								<input type="hidden" id="qusId" name="qusId" value="">
								<div class="searchCond" style="display: block">
									<div class="ps-margin ps-search">
										<span class="input-icon"> <input id="nameCheck"
											name="nameCheck" class="form-control ps-input" type="text"
											placeholder="请输入关键字" value="${nameCheck}"> <a
											href="javascript:void(0)" class="ps-searchBtn"><i
												class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
								</div>
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<!-- 是否不能删除，为空，可以操作 -->
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													id="batchDel"> 批量删除 </a>
											</div>
										</div>
									</div>
								</div>

							</form>
							<div class="widget-buttons ps-widget-buttons">
								<button class="btn btn-info btn-primary btn-xs" type="button"
									onclick="addQus(${empty pId?0:pId});">
									<i class="fa fa-question-circle"></i>新增疑问
								</button>
								<%-- <button class="btn btn-info btn-primary btn-xs" type="button"
									onclick="addTask('${param.sid}');">
									<i class="fa fa-plus"></i>解答疑问
								</button> --%>
							</div>
						</div>
						<div class="widget-body">
							<form action="/web/help/delHelp" id="delForm">
								<input type="hidden" name="redirectPage" id="redirectPage" /> <input
									type="hidden" name="sid" value="${param.sid}" />
								<input type="hidden" id="pId" name="pId"
								value="${empty pId?0:pId}"> 
								<ul class="messages-list" id="crmListUl">
									<c:choose>
										<c:when test="${not empty list}">
											<c:forEach items="${list}" var="obj" varStatus="vs">
												<li class="item first-item">
													<div class="message-content" style="margin-left:5px;">
														<div class="content-headline">
															<div class="checkbox pull-left ps-chckbox">
																<label> <input type="checkbox"
																	class="colored-blue" value="${obj.id}" name="ids"}>
																	<span class="text"></span>
																</label>
															</div>
															<a href="javascript:void(0)" class="item-box"
																onclick="viewHelp(${obj.id},${obj.sunNum})">
																${obj.name} </a>&nbsp;
															<c:if test="${obj.pId==0}">
																<a href="javascript:void(0)"
																	onclick="editHelp(${obj.id})"> <i
																	class="fa fa-cog gray"></i>
																</a>
															</c:if>
														</div>
														<a href="javascript:void(0)" class="item-box"
															onclick="viewHelp(${obj.id},${obj.sunNum})">
															<div class="content-text" style="margin-left:20px;">
																<tags:viewTextArea>
																	<tags:cutString num="302">${obj.describe}</tags:cutString>
																</tags:viewTextArea>
															</div>
														</a>
														<div class="item-more" style="margin-left:10px;">
															<c:if test="${not empty obj.modifyTime}">
																<span class="time"><i class="fa fa-clock-o"></i>&nbsp;更新时间：${obj.modifyTime }</span>
															</c:if>
															<span class="time"><i class="fa fa-clock-o"></i>&nbsp;创建于：${obj.recordCreateTime }</span>
														</div>
													</div>
												</li>
											</c:forEach>
										</c:when>
									</c:choose>
								</ul>
							</form>
							<tags:pageBar url="/web/help/listQus"></tags:pageBar>
						</div>
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

