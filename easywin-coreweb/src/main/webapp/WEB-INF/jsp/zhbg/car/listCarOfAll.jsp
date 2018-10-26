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
								<form action="/car/listCarOfAllPage" id="searchForm" class="subform">
								<tags:token></tags:token>
								<input type="hidden" name="pager.pageSize" value="10">
								<input type="hidden" name="activityMenu" value="${param.activityMenu }">
								<input type="hidden" name="searchTab" value="${param.searchTab}">
								<input type="hidden" name="stateType" value="${car.stateType}">
								
								<div class="btn-group pull-left searchCond">
								<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="状态">
													<c:choose>
														<c:when test="${not empty car.stateType}">
															<font style="font-weight:bold;">
																<c:choose>
																	<c:when test="${car.stateType eq 1}">可用</c:when>
																	<c:when test="${car.stateType eq 2}">损坏</c:when>
																	<c:when test="${car.stateType eq 3}">维修</c:when>
																	<c:when test="${car.stateType eq 4}">报废</c:when>
																</c:choose>
															</font>
														</c:when>
														<c:otherwise>状态</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)" class="clearThisElement" relateElement="stateType">不限条件</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="stateType" dataValue="1">可用</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="stateType" dataValue="2">损坏</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="stateType" dataValue="3">维修</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="stateType" dataValue="4">报废</a>
													</li>
												</ul>
											</div>
										</div>
								
								</div>
								<div class="table-toolbar ps-margin">
										<div class="btn-group cond" id="moreCondition_Div">
											<a class="btn btn-default dropdown-toggle btn-xs"
												onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
													<c:when
														test="${not empty car.startDate || not empty car.endDate}">
														<font style="font-weight:bold;">筛选中</font>
													</c:when>
													<c:otherwise>
				                                            	更多
	                                            			</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
												<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">创建时间：</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${car.startDate}"
														id="startDate" name="startDate" placeholder="开始时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
													<span>~</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${car.endDate}" id="endDate"
														name="endDate" placeholder="结束时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
												</div>
												<div class="ps-clear padding-top-10"
													style="text-align: center;">
													<button type="submit" class="btn btn-primary btn-xs">查询</button>
													<button type="button"
														class="btn btn-default btn-xs margin-left-10"
														onclick="resetMoreCon('moreCondition_Div')">重置</button>
												</div>
											</div>
										</div>
									</div>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input name="carModel" value="${car.carModel}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键字">
										<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
								
								</form>
								<c:if test="${isModAdmin }">
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addCar('${param.sid}');">
										<i class="fa fa-plus"></i> 添加车辆
									</button>
								</div>
								</c:if>
							</div>
						</div>

						<c:choose>
							<c:when test="${not empty listCar}">
								<div class="widget-body">
									<table class="table table-striped table-hover" id="editabledatatable">
										<thead>
											<tr role="row">
												<th class="text-center">序号</th>
												<th class="text-center">车牌号</th>
												<th>车辆型号</th>
												<th class="text-center">排量(L)</th>
												<th class="text-center">座位数</th>
												<th class="text-center">强险截止日期</th>
												<th class="text-center">商业险截止日期</th>
												<th class="text-center">状态</th>
												<th class="text-center">操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listCar}" var="car" varStatus="vs">
												<tr class="optTr">
													<td class="text-center">${vs.count}</td>
													<td class="text-center">
														<a href="javascript:void(0);" onclick="viewCar('${param.sid}','${car.id}');">${car.carNum }</a>
													</td>
													<td>
														<c:choose>
															<c:when test="${not empty car.carModel }">${car.carModel }</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td class="text-center">
														<c:choose>
															<c:when test="${not empty car.displacement }">${car.displacement }</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td class="text-center">${car.seatNum }</td>
													<td class="text-center">
														<c:choose>
															<c:when test="${not empty car.qxEndDate }">${car.qxEndDate }</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td class="text-center">
														<c:choose>
															<c:when test="${not empty car.syxEndDate }">${car.syxEndDate }</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td class="text-center">
														<c:if test="${car.stateType ==1 }">可用</c:if>
														<c:if test="${car.stateType ==2 }">损坏</c:if>
														<c:if test="${car.stateType ==3 }">维修</c:if>
														<c:if test="${car.stateType ==4 }">报废</c:if>
													</td>
													<td class="text-center">
														<c:if test="${isModAdmin}">
															<a href="javascript:void(0)" onclick="addInsuranceRecord('${param.sid}','${car.id }','02501')">强险</a>|
															<a href="javascript:void(0)" onclick="addInsuranceRecord('${param.sid}','${car.id }','02502')">商业险</a>|
															<c:if test="${car.stateType != 3}">
															<a href="javascript:void(0)" onclick="carMaintain('${param.sid}','${car.id }')">维修</a>|
															</c:if>
															<a href="javascript:void(0)" onclick="delCar('${param.sid}','${car.id }')">删除</a>
														</c:if>
														<c:if test="${car.stateType ==1 && !isModAdmin }">
															<a href="javascript:void(0)" onclick="applyCar('${param.sid}','${car.id }')">申请</a>
														</c:if>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<tags:pageBar url="/car/listCarOfAllPage"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>您还没有相关车辆数据！</h2>
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