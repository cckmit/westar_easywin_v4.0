<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
$(function(){
	
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
})
function addCar(sid){
	var url = '/car/addCarPage?sid='+sid;
	openWinByRight(url);
}
</script>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">

			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">

							<div>
								<form action="/car/listCarApplyToDoPage" id="searchForm" class="subform">
									<tags:token></tags:token>
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu }">
									<input type="hidden" name="searchTab" value="${param.searchTab}">
									<input type="hidden" name="applyer" value="${applyRecord.applyer }">
									<input type="hidden" name="applyerName" value="${applyRecord.applyerName}">


									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown">
												<c:choose>
													<c:when test="${not empty applyRecord.applyerName}">
														<font style="font-weight:bold;">${applyRecord.applyerName}</font>
													</c:when>
													<c:otherwise>申请人筛选</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)" class="clearThisElement" relateElement="applyer" relateElementName="applyerName">不限条件</a>
												</li>
												<li><a href="javascript:void(0)" class="userOneElementSelect" relateElement="applyer" relateElementName="applyerName">人员选择</a>
												</li>
											</ul>
										</div>
									</div>
									<div class="table-toolbar ps-margin">
										<div class="btn-group cond" id="moreCondition_Div">
											<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
													<c:when test="${not empty applyRecord.startDate || not empty applyRecord.endDate}">
														<font style="font-weight:bold;">筛选中</font>
													</c:when>
													<c:otherwise>更多</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
												<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">申请时间：</span>
													<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${car.startDate}" id="startDate" name="startDate" placeholder="开始时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
													<span>~</span>
													<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${car.endDate}" id="endDate" name="endDate" placeholder="结束时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
												</div>
												<div class="ps-clear padding-top-10" style="text-align: center;">
													<button type="submit" class="btn btn-primary btn-xs">查询</button>
													<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
												</div>
											</div>
										</div>
									</div>
									<div class="ps-margin ps-search searchCond">
										<span class="input-icon">
											<input name="searchLike" value="${applyRecord.searchLike}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键字">
											<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
										</span>
									</div>

								</form>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty listCarApply}">
								<form action="/car/agreeCarApply" id="carApplyForm" class="subform">
									<input type="hidden" id="redirectPage" name="redirectPage" />
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="id" id="carApplyId" value="">
									<input type="hidden" name="voteReason" id="voteReason" value="">
								</form>
								<div class="widget-body">
									<table class="table table-striped table-hover" id="editabledatatable">
										<thead>
											<tr role="row">
												<th></th>
												<th>申请车辆</th>
												<th>目的地</th>
												<th>申请原因</th>
												<th class="text-center">使用日期</th>
												<th class="text-center">预算里程(km)</th>
												<th>申请人</th>
												<th class="text-center">状态</th>
												<th class="text-center">操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listCarApply}" var="obj" varStatus="vs">
												<tr class="optTr">
													<td>${vs.count}</td>
													<td><a href="javascript:void(0);" onclick="viewCar('${param.sid}','${obj.carId}');">${obj.carName}(${obj.carNum})</a></td>
													<td>${obj.destination}</td>
													<td>
														<a href="javascript:void(0);" onclick="viewApplyRecord('${param.sid}','${obj.id}');"><tags:cutString num="34">${obj.reason}</tags:cutString></a>
													</td>
													<td class="text-center">${fn:substring(obj.startDate,6,16) } --- ${fn:substring(obj.endDate,6,16) }</td>
													<td class="text-center">${obj.predictJourney}</td>
													<td class="text-center">
														<div class="ticket-user pull-left other-user-box" data-container="body" data-placement="left">
															<img class="user-avatar userImg"  title="${obj.applyerName}" 
																src="/downLoad/userImg/${obj.comId}/${obj.applyer}" /> 
																<i class="user-name">${obj.applyerName}</i>
														</div>
													</td>
														<td class="text-center">
														<c:choose>
															<c:when test="${obj.state == 0}"><span style="color:red;font-weight:bold;">待审核</span></c:when>
															<c:otherwise><span style="color:green;font-weight:bold;">已审核</span></c:otherwise>
														</c:choose>
													</td>
													<td align="center">
													<c:choose>
													<c:when test="${obj.state eq '0' }">
													<a href="javascript:void(0)" onclick="agreeApply('${param.sid}','${obj.id}','${obj.carId}','${obj.startDate }','${obj.endDate }')"><span style="color:green;font-weight:bold;">同意</span></a>| 
														<a href="javascript:void(0)" onclick="voteApply('${param.sid}','${obj.id}')"><span style="color:red;font-weight:bold;">不同意</span></a>
													</c:when>
													<c:otherwise>
													<a href="javascript:void(0)"  onclick="viewApplyRecord('${param.sid}','${obj.id}');">查看</a>
													</c:otherwise>
													</c:choose>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<tags:pageBar url="/car/listCarApplyToDoPage"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>您还没有相关审核数据！</h2>
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
</body>
</html>
