<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/zhbgJs/carJs/car.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<style type="text/css">
body:before {
	background-color: #FFFFFF;
}
</style>
</head>
<body onload="resizeVoteH('otherCarAttrIframe');" style="background-color:#FFFFFF;">
	<div class="widget-body no-shadow" style="padding:0 12px;">
		<div class="widget radius-bordered">
			<div class="widget-header bg-bluegray no-shadow">
				<span class="widget-caption blue">强险记录</span>
				<div class="widget-buttons btn-div-full">
					<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-minus blue"></i>
					</a>
				</div>
			</div>
			<div class="widget-body no-shadow">
				<div class="tickets-container bg-white">
					<table class="table table-hover general-table">
						<thead>
							<tr>
								<th valign="middle">序号</th>
								<th valign="middle" class="hidden-phone">缴纳日期</th>
								<th valign="middle">到期日期</th>
								<th valign="middle">缴纳金额(元)</th>
								<c:if test="${isModAdmin }">
									<th valign="middle" style="text-align: center;">操作</th>
								</c:if>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty listQxRecord}">
									<c:forEach items="${listQxRecord}" var="obj" varStatus="status">
										<tr>
											<td class="qxOrderNum">${ status.count}</td>
											<td>
												<c:choose>
													<c:when test="${isModAdmin}">
														<input class="colorpicker-default form-control" readonly="readonly" id="qx_${obj.id}_stratDate"
															onClick="WdatePicker({onpicked:function(){updateStartDate('${obj.id}','${param.sid }',this)},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'qx_${obj.id}_endDate\',{d:-0});}'})" type="text"
															value="${ obj.startDate}" style="width: 150px;float: left">
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${not empty obj.startDate}">${ obj.startDate}</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<c:choose>
													<c:when test="${isModAdmin}">
														<input class="colorpicker-default form-control" readonly="readonly" id="qx_${obj.id}_endDate"
															onClick="WdatePicker({onpicked:function(){updateEndDate('${obj.id}','${param.sid }',this)},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'qx_${obj.id}_stratDate\',{d:-0});}'})" type="text"
															value="${ obj.endDate}" style="width: 150px;float: left">
													</c:when>
													<c:otherwise>
													${ obj.endDate}
												</c:otherwise>
												</c:choose>

											</td>
											<td>
												<c:choose>
													<c:when test="${isModAdmin}">
														<input class="colorpicker-default form-control" onblur="updateInsurancePrice('${obj.id}','${param.sid }',this,'${obj.insurancePrice}')" type="text" value="${obj.insurancePrice}"
															style="width: 100px;float: left">
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${not empty obj.insurancePrice}">${ obj.insurancePrice}</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</td>
											<c:if test="${isModAdmin }">
												<td>
													<a href="javascript:void(0)" onclick="delInsurance('${obj.id }','${param.sid}',this,'02501')">删除</a>
												</td>
											</c:if>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td height="25" colspan="9" align="center">
											<h3>没有相关保险记录！</h3>
										</td>
									</tr>
								</c:otherwise>
							</c:choose>

							</div>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="widget radius-bordered">
			<div class="widget-header bg-bluegray no-shadow">
				<span class="widget-caption blue">商业险记录</span>
				<div class="widget-buttons btn-div-full">
					<a class="ps-point btn-a-full" data-toggle="collapse"> <i class="fa fa-minus blue"></i>
					</a>
				</div>
			</div>
			<div class="widget-body no-shadow">
				<div class="tickets-container bg-white">
					<table class="table table-hover general-table">
						<thead>
							<tr>
								<th valign="middle">序号</th>
								<th valign="middle" class="hidden-phone">缴纳日期</th>
								<th valign="middle">到期日期</th>
								<th valign="middle">缴纳金额</th>
								<c:if test="${isModAdmin }">
									<th valign="middle" style="text-align: center;">操作</th>
								</c:if>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty listSyxRecord}">
									<c:forEach items="${listSyxRecord}" var="obj" varStatus="status">
										<tr>
											<td class="syxOrderNum">${ status.count}</td>
											<td>
												<c:choose>
													<c:when test="${isModAdmin}">
														<input class="colorpicker-default form-control" readonly="readonly" id="qx_${obj.id}_stratDate"
															onClick="WdatePicker({onpicked:function(){updateStartDate('${obj.id}','${param.sid }',this)},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'qx_${obj.id}_endDate\',{d:-0});}'})" type="text"
															value="${ obj.startDate}" style="width: 150px;float: left">
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${not empty obj.startDate}">${ obj.startDate}</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<c:choose>
													<c:when test="${isModAdmin}">
														<input class="colorpicker-default form-control" readonly="readonly" id="qx_${obj.id}_endDate"
															onClick="WdatePicker({onpicked:function(){updateEndDate('${obj.id}','${param.sid }',this)},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'qx_${obj.id}_stratDate\',{d:-0});}'})" type="text"
															value="${ obj.endDate}" style="width: 150px;float: left">
													</c:when>
													<c:otherwise>
													${ obj.endDate}
												</c:otherwise>
												</c:choose>

											</td>
											<td>
												<c:choose>
													<c:when test="${isModAdmin}">
														<input class="colorpicker-default form-control" onblur="updateInsurancePrice('${obj.id}','${param.sid }',this,'${obj.insurancePrice}')" type="text" value="${obj.insurancePrice}"
															style="width: 100px;float: left">
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${not empty obj.insurancePrice}">${ obj.insurancePrice}</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</td>
											<c:if test="${isModAdmin }">
												<td>
													<a href="javascript:void(0)" onclick="delInsurance('${obj.id }','${param.sid}',this,'02502')">删除</a>
												</td>
											</c:if>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td height="25" colspan="9" align="center">
											<h3>没有相关保险记录！</h3>
										</td>
									</tr>
								</c:otherwise>
							</c:choose>

							</div>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">
		var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>

	<!--Beyond Scripts-->
</body>
</html>
