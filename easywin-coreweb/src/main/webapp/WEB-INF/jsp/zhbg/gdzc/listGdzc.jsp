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
								<form action="/gdzc/listGdzcPage" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="ssType" id="ssType" value="${gdzc.ssType}">
									<input type="hidden" name="addType" id="addType" value="${gdzc.addType}">
									<input type="hidden" name="gdzcName" id="gdzcName" value="${gdzc.gdzcName}">
									<input type="hidden" name="state" id="state" value="${gdzc.state}">
									
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
													<c:when test="${not empty gdzc.state}">
														<c:if test="${gdzc.state ==1 }">使用中</c:if>
														<c:if test="${gdzc.state ==2 }">闲置中</c:if>
														<c:if test="${gdzc.state ==3 }">维修中</c:if>
														<c:if test="${gdzc.state ==4 }">已减少</c:if>
													</c:when>
													<c:otherwise>状态</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0);" onclick="stateFilter(this,'')">不限状态</a>
												</li>
												<li>
													<a href="javascript:void(0);" onclick="stateFilter(this,'1')">使用中</a>
												</li>
												<li>
													<a href="javascript:void(0);" onclick="stateFilter(this,'2')">闲置中</a>
												</li>
												<li>
													<a href="javascript:void(0);" onclick="stateFilter(this,'3')">维修中</a>
												</li>
												<li>
													<a href="javascript:void(0);" onclick="stateFilter(this,'4')">已减少</a>
												</li>
											</ul>
										</div>
									</div>

									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
													<c:when test="${not empty gdzc.ssType}">
														<c:forEach items="${listGdzcType}" var="gdzcType" varStatus="status">
															<c:if test="${gdzcType.id==gdzc.ssType}">
																<font style="font-weight:bold;">${gdzcType.typeName}</font>
															</c:if>
														</c:forEach>
													</c:when>
													<c:otherwise>固定资产类型</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0);" onclick="gdzcTypeFilter(this,'')">不限类型</a>
												</li>
												<c:choose>
													<c:when test="${not empty listGdzcType}">
														<c:forEach items="${listGdzcType}" var="gdzcType" varStatus="status">
															<li>
																<a href="javascript:void(0);" onclick="gdzcTypeFilter(this,'${gdzcType.id}')">${gdzcType.typeName}</a>
															</li>
														</c:forEach>
													</c:when>
												</c:choose>
											</ul>
										</div>


									</div>

									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
													<c:when test="${not empty gdzc.addType}">
														<c:forEach items="${listAddType}" var="addType" varStatus="status">
															<c:if test="${addType.id==gdzc.addType}">
																<font style="font-weight:bold;">${addType.typeName}</font>
															</c:if>
														</c:forEach>
													</c:when>
													<c:otherwise>固定资产添加类型</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li>
													<a href="javascript:void(0);" onclick="addTypeFilter(this,'')">不限类型</a>
												</li>
												<c:choose>
													<c:when test="${not empty listAddType}">
														<c:forEach items="${listAddType}" var="addType" varStatus="status">
															<li>
																<a href="javascript:void(0);" onclick="addTypeFilter(this,'${addType.id}')">${addType.typeName}</a>
															</li>
														</c:forEach>
													</c:when>
												</c:choose>
											</ul>
										</div>
									</div>

									<div class="table-toolbar ps-margin">
										<div class="btn-group cond" id="moreCondition_Div">
											<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
													<c:when test="${not empty gdzc.startDate || not empty gdzc.endDate || not empty gdzc.addStartDate || not empty gdzc.addEndDate}">
														<font style="font-weight:bold;">筛选中</font>
													</c:when>
													<c:otherwise>
							                                            	更多
				                                            			</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
												<div class="ps-margin ps-search padding-left-10">
													<div>
														<span>创建时间筛选：</span>
													</div>
													<div class="margin-left-20">
														<span class="btn-xs">起</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${gdzc.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span class="btn-xs">止</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${gdzc.endDate}" id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
												</div>
												<div class="ps-margin ps-search padding-left-10">
													<div>
														<span>添加时间筛选：</span>
													</div>
													<div class="margin-left-20">
														<span class="btn-xs">起</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${gdzc.addStartDate}" id="addStartDate" name="addStartDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'addEndDate\',{d:-0});}'})" />
														<span class="btn-xs">止</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${gdzc.addEndDate}" id="addEndDate" name="addEndDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'addStartDate\',{d:+0});}'})" />
													</div>
												</div>
												<div class="ps-clear padding-top-10" style="text-align: center;">
													<button type="submit" class="btn btn-primary btn-xs">查询</button>
													<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
												</div>
											</div>
										</div>
									</div>
								</form>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="searchGdzcName" name="searchGdzcName" value="${gdzc.gdzcName }" class="form-control ps-input" type="text" placeholder="请输入关键字">
										<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
								<c:if test="${isModAdmin }">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addGdzc('${param.sid}');">
											<i class="fa fa-plus"></i> 添加固定资产
										</button>
									</div>
								</c:if>
							</div>
						</div>

						<c:choose>
							<c:when test="${not empty listGdzc}">
								<div class="widget-body">
									<table class="table table-striped table-hover" id="editabledatatable">
										<thead>
											<tr role="row">
												<th>序号</th>
												<th>资产编号</th>
												<th>资产名称</th>
												<th>资产类型</th>
												<th>添加日期</th>
												<th>添加类型</th>
												<th>状态</th>
												<c:if test="${isModAdmin }">
													<th>操作</th>
												</c:if>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listGdzc}" var="gdzc" varStatus="vs">
												<tr class="optTr">
													<td>${vs.count}</td>
													<td>${gdzc.gdzcNum }</td>
													<td>
														<a href="javascript:void(0);" onclick="viewGdzc('${param.sid}','${gdzc.id}');">${gdzc.gdzcName }</a>
													</td>
													<td>${gdzc.ssTypeName}</td>
													<td>${gdzc.addDate}</td>
													<td>${gdzc.addTypeName}</td>
													<td>
														<c:if test="${gdzc.state ==1 }">使用中</c:if>
														<c:if test="${gdzc.state ==2 }">闲置中</c:if>
														<c:if test="${gdzc.state ==3 }">维修中</c:if>
														<c:if test="${gdzc.state ==4 }">已减少</c:if>
													</td>
													<c:if test="${isModAdmin }">
														<td>
															<c:if test="${gdzc.state ==1 || gdzc.state ==2}">
																<a href="javascript:void(0)" onclick="gdzcMaintain('${param.sid}','${gdzc.id }')">维修</a>| <a href="javascript:void(0)" onclick="gdzcReduce('${param.sid}','${gdzc.id }')">减少</a>| 
														</c:if>
															<c:if test="${gdzc.state ==3 }">
																<a href="javascript:void(0)" onclick="gdzcMaintainFinish('${param.sid}','${gdzc.id }')">维修完成</a>| <a href="javascript:void(0)" onclick="gdzcReduce('${param.sid}','${gdzc.id }')">减少</a>|
															</c:if>
															<a href="javascript:void(0)" onclick="delGdzc('${param.sid}','${gdzc.id }')">删除</a>
														</td>
													</c:if>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<tags:pageBar url="/gdzc/listGdzcPage"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>您还没有相关固定资产数据！</h2>
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