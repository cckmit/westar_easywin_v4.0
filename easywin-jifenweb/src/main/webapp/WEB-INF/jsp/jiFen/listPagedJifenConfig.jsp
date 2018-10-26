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
									<th width="10%" valign="middle">序号</th>
									<th width="20%" valign="middle">积分项类别</th>
									<th width="10%" valign="middle">分值</th>
									<th width="10%" valign="middle">每日可积分</th>
									<th width="10%" valign="middle">总共可积分</th>
									<th valign="middle">描述</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty list}">
										<c:forEach items="${list}" var="obj" varStatus="vs">
											<tr>
								 				<td valign="middle">${vs.count }</td>
								 				<td valign="middle">${obj.title}</td>
								 				<td valign="middle"> ${obj.typeScore} </td>
								 				<td valign="middle">${obj.dayMaxScore==0?'/':obj.dayMaxScore}</td>
								 				<td valign="middle">${obj.sysMaxScore==0?'/':obj.sysMaxScore}</td>
								 				<td valign="middle">${fn:replace(obj.content,"R","") }</td>
								 			</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</form>
					<tags:pageBar url="/jiFen/listPagedJifenConfig"></tags:pageBar>
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

