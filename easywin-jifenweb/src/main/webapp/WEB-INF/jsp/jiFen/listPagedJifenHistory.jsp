<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<!-- Page Content -->
<div class="page-content">
<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<div class="col-md-12 col-xs-12 ">
			<div class="widget">
				<div class="widget-body">
					<form action="/clock/delClock" id="delForm">
					<input type="hidden" name="sid" value="${param.sid}"/>
					<input type="hidden" name="pager.pageSize" value="20"/>
					<input type="hidden" id="type" name="type" value="${param.type}"/>
					<input type="hidden" id="activityMenu" name="activityMenu" value="${param.activityMenu}">
						<table class="table table-striped table-hover">
							<thead>
								<tr role="row">
									<th width="6%" valign="middle">序号</th>
									<th width="19%" valign="middle">时间</th>
									<th width="11%" valign="middle">积分变化</th>
									<th valign="middle">描述</th>
									<th width="10%" valign="middle">当前积分</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty list}">
										<c:forEach items="${list}" var="obj" varStatus="vs">
											<tr>
								 				<td valign="middle">${vs.count}</td>
								 				<td valign="middle">${fn:substring(obj.recordCreateTime,0,10)}</td>
								 				<td valign="middle">
								 					<c:choose>
								 						<c:when test="${obj.jifenChange>0}">
								 							+${obj.jifenChange }
								 						</c:when>
								 						<c:when test="${obj.jifenChange<=0}">
								 							${obj.jifenChange }
								 						</c:when>
								 					</c:choose>
								 				</td>
								 				<td title="${obj.content}" valign="middle"><tags:cutString num="68">${obj.content}</tags:cutString> </td>
								 				<td>${obj.allScore}分</td>
								 			</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</form>
					<tags:pageBar url="/jiFen/listPagedJifenHistory"></tags:pageBar>
				</div>
			</div>
		</div>
	</div>
	<!-- /Page Body -->
</div>
<!-- /Page Content -->
</div>
<!-- /Page Container -->
<!-- Main Container -->
</body>
</html>

