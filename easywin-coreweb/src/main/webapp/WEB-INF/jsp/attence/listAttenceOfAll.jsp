<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						</div>
						<c:choose>
						<c:when test="${not empty listAttenceRecord}">
						<div class="widget-body">
								<table class="table table-striped table-hover" id="editabledatatable">
									<thead>
										<tr role="row">
											<th class="text-center">
											序号
											</th>
											<th>编号</th>
											<th>名称</th>
											<th class="text-center">考勤时间</th>
											<th class="text-center">考勤模式</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listAttenceRecord}" var="recordVo" varStatus="vs">
											<tr class="optTr">
												<td class="text-center">
												${vs.count}</td>
												<td>
												${recordVo.enrollNumber}
												</td>
												<td>
												${recordVo.userName}
												</td>
												<td class="text-center">${recordVo.time}</td>
												<td class="text-center">
												<c:if test="${recordVo.verifyMode eq 0}">密码</c:if> 
												<c:if test="${recordVo.verifyMode eq 1}">指纹</c:if> 
												<c:if test="${recordVo.verifyMode eq 2}">卡</c:if> 
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<tags:pageBar url="/attence/listAttenceRecord"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>没有相关数据！</h2>
										<p class="description">协同提高效率，分享拉近距离。</p>
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
