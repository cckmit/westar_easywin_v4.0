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
						<div
							class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<form action="/order/listOrders" id="searchForm">
								<input type="hidden" name="sid" value="${param.sid}">
								<input type="hidden" name="activityMenu" value="order_m_1.2">
								<input type="hidden" id="pageSize" name="pager.pageSize" value="10">
								<div class="btn-group pull-left">
									<div class="ps-margin ps-search">
										<input
											class="form-control dpd2 no-padding condDate"
											type="text" readonly="readonly"
											value="${order.startDate}" id="startDate" name="startDate" placeholder="开始时间"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
										<span>~</span> 
										<input
											class="form-control dpd2 no-padding condDate"
											type="text" readonly="readonly" value="${order.endDate}" id="endDate" name="endDate" placeholder="结束时间"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
										<input type="submit" class="btn btn-primary" value="查询" style="padding:4px 11px;">
										<input type="button" class="btn margin-left-5" value="清空日期" onclick="dateReset();" style="padding:4px 11px;">
									</div>
								</div>
							</form>
						</div>
						<div class="widget-body">
							<table class="table table-striped table-hover"
								id="editabledatatable">
								<thead>
									<tr role="row">
										<th>序号</th>
										<th>订单号</th>
										<th>分类</th>
										<th>创建时间</th>
										<th>金额（元）</th>
										<th>状态</th>
										<th>联系人</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="allTodoBody">
									<c:choose>
										<c:when test="${not empty listOrders}">
										<c:forEach items="${listOrders}" var="orderVo" varStatus="vs">
											<tr>
												<td class="rowNum">${vs.count}</td>
												<td><a href="javascript:void(0);" onclick="orderDetail(${orderVo.id});">${orderVo.orderTradeNo}</a></td>
												<td>${orderVo.orderTypeName}</td>
												<td>${orderVo.recordCreateTime}</td>
												<td>${orderVo.orderCost}</td>
												<td>
													<c:choose>
														<c:when test="${orderVo.status==0 and orderVo.overDates > 7}">
															<span style="color:red;">已失效</span>
														</c:when>
														<c:otherwise>
															<c:if test="${orderVo.status==0}"><span style="color:green;font-weight:bold;">待付款</span></c:if>
															<c:if test="${orderVo.status==1}"><span style="color:blue;font-weight:bold;">已支付</span></c:if>
															<c:if test="${orderVo.status==2}"><span style="color:red;font-weight:bold;">订单失败</span></c:if>
														</c:otherwise>
													</c:choose>
												</td>
												<td>${orderVo.linkerName}</td>
												<td>
													<c:if test="${orderVo.status==0 and orderVo.overDates<=7 and (orgBalanceMoney<orderVo.orderCost)}">
														<a href="javascript:void(0);" onclick="orderToPay(${orderVo.id});">付款</a>&nbsp;|
													</c:if>
													<a href="javascript:void(0);" onclick="orderDetail(${orderVo.id});">详情</a>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="8">
												<section class="error-container text-center">
													<h1>
														<i class="fa fa-exclamation-triangle"></i>
													</h1>
													<div class="error-divider">
														<h2>没有交易记录。</h2>
														<p class="description">协同提高效率，分享拉近距离。</p>
													</div>
												</section>
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
								</tbody>
							</table>
							<tags:pageBar url="/order/listOrders"></tags:pageBar>
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

