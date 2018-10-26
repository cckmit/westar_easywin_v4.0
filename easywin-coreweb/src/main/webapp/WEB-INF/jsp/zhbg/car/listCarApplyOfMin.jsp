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
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">

							<div>
								<form action="/car/listCarApplyOfMinPage" id="searchForm" class="subform">
									<tags:token></tags:token>
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu }">
									<input type="hidden" name="searchTab" value="${param.searchTab}">
									<input type="hidden" name="state" value="${applyRecord.state}">

									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="状态"> <c:choose>
														<c:when test="${not empty applyRecord.state}">
															<font style="font-weight:bold;"> <c:choose>
																	<c:when test="${applyRecord.state eq 0}">审核中</c:when>
																	<c:when test="${applyRecord.state eq 1}">通过</c:when>
																	<c:when test="${applyRecord.state eq 2}">已归还</c:when>
																	<c:when test="${applyRecord.state eq 3}">拒绝</c:when>
																</c:choose>
															</font>
														</c:when>
														<c:otherwise>状态</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearThisElement" relateElement="state">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="state" dataValue="0">审核中</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="state" dataValue="1">通过</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="state" dataValue="2">已归还</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="state" dataValue="3">拒绝</a>
													</li>
												</ul>
											</div>
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
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" onclick="applyCar('${param.sid}');">
										<i class="fa fa-plus"></i>车辆申请
									</button>
								</div>
							</div>
						</div>

						<c:choose>
							<c:when test="${not empty listCarApply}">
								<div class="widget-body">
									<table class="table table-striped table-hover" id="editabledatatable">
										<thead>
											<tr role="row">
												<th></th>
												<th>申请车辆</th>
												<th>目的地</th>
												<th>申请原因</th>
												<th class="text-center">使用日期</th>
												<th class="text-center">里程(km)</th>
												<th class="text-center">实际油耗</th>
												<th class="text-center">状态</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listCarApply}" var="obj" varStatus="vs">
												<tr class="optTr">
													<td>${vs.count}</td>
													<td>
														<a href="javascript:void(0);" onclick="viewCar('${param.sid}','${obj.carId}');">${obj.carName}(${obj.carNum})</a>
													</td>
													<td>${obj.destination}</td>
													<td>
														<a href="javascript:void(0);" onclick="viewApplyRecord('${param.sid}','${obj.id}');"><tags:cutString num="34">${obj.reason}</tags:cutString></a>
													</td>
													<td class="text-center">${fn:substring(obj.startDate,6,16) } --- ${fn:substring(obj.endDate,6,16) }</td>
													<td class="text-center">
														<c:choose>
															<c:when test="${not empty obj.realJourney}">${obj.realJourney}</c:when>
															<c:otherwise>${obj.predictJourney}</c:otherwise>
														</c:choose>
													</td>
													<td class="text-center">
														<c:choose>
															<c:when test="${not empty obj.oilConsumption}">${obj.oilConsumption}</c:when>
															<c:otherwise>----</c:otherwise>
														</c:choose>
													</td>
													<td class="text-center">
														<c:choose>
															<c:when test="${obj.state == 0}"><span style="color:#C6E2FF;font-weight:bold;">审核中</span></c:when>
															<c:when test="${obj.state == 1}"><span style="color:green;font-weight:bold;">通过</span></c:when>
															<c:when test="${obj.state == 2}"><span style="color:#00EE00;font-weight:bold;">已归还</span></c:when>
															<c:when test="${obj.state == 3}"><span style="color:red;font-weight:bold;">拒绝</span></c:when>
															<c:when test="${obj.state == 4}"><span style="color:#A3A3A3 ;font-weight:bold;">已撤回</span></c:when>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${obj.state eq 1}">
																<a href="javascript:void(0)" onclick="returnCar('${param.sid}','${obj.id}')">归还</a>
															</c:when>
															<c:when test="${obj.state eq 0}">
																<a href="javascript:void(0)" onclick="backoutApply('${param.sid}','${obj.id}')">撤回</a>
															</c:when>
															<c:otherwise>
																<a href="javascript:void(0)" onclick="viewApplyRecord('${param.sid}','${obj.id}')">查看</a>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<tags:pageBar url="/car/listCarApplyOfMinPage"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>您还没有相关申请数据！</h2>
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
