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
							<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							</div>
							<div class="widget-body">
								<table class="table table-striped table-hover"
									id="editabledatatable">
									<thead>
										<tr role="row">
											<th>
												序号(提交)
											</th>
											<th>
												名称
											</th>
											<th>
												时间
											</th>
											<th>
												操作
											</th>
										</tr>
									</thead>
									<tbody id="allTodoBody">
										<c:choose>
											<c:when test="${not empty listSpFlows}">
												<c:forEach items="${listSpFlows}" var="spFlowVo" varStatus="vs">
													<tr>
														<td class="rowNum">
															${vs.count}
														</td>
														<td>
															${spFlowVo.flowName}
														</td>
														<td>
															${fn:substring(spFlowVo.recordCreateTime,0,16) }
														</td>
														<td>
															<a href="javascript:void(0)" onclick="viewSpFormWork(${spFlowVo.id},1)">编辑</a>
														</td>
													</tr>
												</c:forEach>
											</c:when>
										</c:choose>
									</tbody>
								</table>
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

