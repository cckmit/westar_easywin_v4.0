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
			<c:choose>
				<c:when test="${not empty listIndexVo}">
					<div class="row">
						<div class="col-md-12 col-xs-12 ">
							<div class="widget">
								<div class="widget-body">
									<table class="table table-striped table-hover"
										id="editabledatatable">
										<thead>
											<tr role="row">
												<th width="5%">序号</th>
												<th align="left">事项名称</th>
												<th align="left" style="width:120px;">创建日期</th>
											</tr>
										</thead>
										<tbody id="allTodoBody">
											<c:forEach items="${listIndexVo}" var="obj" varStatus="vs">
												<tr>
													<td class="rowNum">${vs.count}</td>
													<td>
														<span style="font-weight:bold;color:#FF0000;">
															<c:if test="${obj.busType=='1'}">
																[分享]
															</c:if>
															<c:if test="${obj.busType=='015'}">
																[加入申请]
															</c:if>
															<c:if test="${obj.busType=='017'}">
																[会议确认]
															</c:if>
															<c:if test="${obj.busType=='018'}">
																[会议申请]
															</c:if>
															<c:if test="${obj.busType=='003'}">
																[任务]
															</c:if>
															<c:if test="${obj.busType=='004'}">
																[投票]
															</c:if>
															<c:if test="${obj.busType=='005'}">
																[项目]
															</c:if>
															<c:if test="${obj.busType=='006'}">
																[周报]
															</c:if>
															<c:if test="${obj.busType=='050'}">
																[分享]
															</c:if>
															<c:if test="${obj.busType=='011'}">
																[问答]
															</c:if>
															<c:if test="${obj.busType=='012'}">
																[客户]
															</c:if>
															<c:if test="${obj.busType=='080'}">
																[产品]
															</c:if>
															<c:if test="${obj.busType=='013'}">
																[文档]
															</c:if>
															<c:if test="${obj.busType=='022'}">
																[审批]
															</c:if>
														</span>
															<c:choose>
																<c:when test="${obj.busType=='013'}">
																	<a href="javascript:void(0)" onclick="showFile('${obj.busId}','${obj.relatedBusType}','${obj.relatedBusId}');">
																		<tags:viewTextArea>
																			<tags:cutString num="57">${obj.busName}</tags:cutString> 	
																		</tags:viewTextArea>
																	</a>
																</c:when>
																<c:otherwise>
																	<a href="javascript:void(0)" onclick="viewDetailMod(${obj.busId},${obj.busId},'${obj.busType}',0,0)">
																		<tags:viewTextArea>
																			<tags:cutString num="249">${obj.busName}</tags:cutString> 	
																		</tags:viewTextArea>
																	</a>
																</c:otherwise>
															</c:choose>
													</td>
													<td>${fn:substring(obj.recordCreateTime,0,10) }</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<tags:pageBar url="${action}"></tags:pageBar>
								</div>
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="container" style="left:50%;top:50%;position: absolute;
					margin:-90px 0 0 -180px;padding-top:200px;text-align:center;width:488px;">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>没有符合你查询的结果！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
							</div>
						</section>
					</div>
				</c:otherwise>
			</c:choose>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->
	</div>
	<!-- /Page Container -->
</body>
</html>

