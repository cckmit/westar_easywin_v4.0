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
<head>
<script type="text/javascript">
	//按激活状态查询
	function orgderStatusSearch(status){
		$("#searhOrganic").find("input[name='status']").val(status);
		$("#searhOrganic").submit();
	}
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		var height = $(window).height() - 140;
		//$("#contentBody").css("height", height + "px");
	});
	
	function  submitForm(){
		$("#searhOrganic").submit();
	}
	
	function orderDetail(orderId){
		var url = "/web/order/webOrderDetail?orderId="+orderId;
		openWinByRight(url);
	}
</script>
</head>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">

							<form action="/web/orders/listPagedWebOrder" id="searhOrganic">
								<input type="hidden" name="pager.pageSize" value="10" />
								<input type="hidden" name="status" value="${orders.status}"/>
								<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									
								<div class="btn-group pull-left">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown">
													<c:choose>
														<c:when test="${not empty orders.status}">
															<font style="font-weight:bold;">
																${orders.status eq -1?"已失效":orders.status eq 0 ? "待付款":orders.status eq 1?"已支付":"已作废"}
															</font>
														</c:when>
														<c:otherwise>
															状态分类 
														</c:otherwise>
													</c:choose>
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)"
													onclick="orgderStatusSearch('')">清除条件</a></li>
												</li>
												<li><a href="javascript:void(0)"
													onclick="orgderStatusSearch('-1')">已失效</a></li>
												</li>
												<li><a href="javascript:void(0)"
													onclick="orgderStatusSearch('0')">待付款</a></li>
												</li>
												<li><a href="javascript:void(0)"
													onclick="orgderStatusSearch('1')">已支付</a></li>
												</li>
												<li><a href="javascript:void(0)"
													onclick="orgderStatusSearch('2')">已作废</a></li>
												</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="ps-margin ps-search padding-right-10" id="moreCondition_Div">
									<span class="btn-xs">起止时间：</span> <input
										class="form-control dpd2 no-padding condDate" type="text"
										readonly="readonly" value="${orders.startDate}" id="startDate"
										name="startDate" placeholder="开始时间"
										onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared:function(){submitForm()},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
									<span>~</span> <input
										class="form-control dpd2 no-padding condDate" type="text"
										readonly="readonly" value="${orders.endDate}" id="endDate" name="endDate"
										placeholder="结束时间"
										onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared:function(){submitForm()},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
								</div>
								<div class="ps-margin ps-search">
									<span class="input-icon"> 
									<input id="searchOrgName" name="orgName" value="${orders.orgName}" 
									class="form-control ps-input" type="text" placeholder="请输入团队名称" > 
										<a href="#" class="ps-searchBtn">
										<i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
							</form>
						</div>

						<div class="widget-body" id="contentBody">
							<table class="table table-striped table-hover"
								id="editabledatatable">
								<thead>
									<tr role="row">
										<th style="width: 35px">序号</th>
										<th>订单号</th>
										<th>订单创建日期</th>
										<th>团队名称</th>
										<th>联系人</th>
										<th>移动电话</th>
										<th>操作时间</th>
										<th style="text-align: center;">金额（元）</th>
										<th style="text-align: center;">支付方式</th>
										<th style="text-align: center;">状态</th>
										<th style="text-align: center;">操作</th>
									</tr>
								</thead>
								<tbody id="allTodoBody">
									<c:choose>
										<c:when test="${not empty listOrders}">
											<c:forEach items="${listOrders}" var="ordersVo" varStatus="vs">
												<tr>
													<td class="rowNum">${vs.count}</td>
													<td>${ordersVo.orderTradeNo}</td>
													<td>${ordersVo.recordCreateTime}</td>
													<td>${ordersVo.orgName}</td>
													
													
													<td>${ordersVo.linkerName}</td>
													<td>${ordersVo.linkerPhone}</td>
													<td>${ordersVo.paidTime}</td>
													<td style="text-align: center;">${ordersVo.orderCost}</td>
													
													<td style="text-align: center;">
														${ordersVo.paidWay eq 1?'微信支付':ordersVo.paidWay eq 2?'支付宝支付':'线下支付'}
													</td>
													<td style="text-align: center;">
														<c:choose>
															<c:when test="${ordersVo.status == 0 and ordersVo.overDates > 7}">
																<span style="color:red;">已失效</span>
															</c:when>
															<c:otherwise>
																<c:if test="${ordersVo.status==0}"><span style="color:green;font-weight:bold;">待付款</span></c:if>
																<c:if test="${ordersVo.status==1}"><span style="color:blue;font-weight:bold;">已支付</span></c:if>
																<c:if test="${ordersVo.status==2}"><span style="color:red;font-weight:bold;">订单失败</span></c:if>
															</c:otherwise>
														</c:choose>
													</td>
													<td style="text-align: center;">
														<a href="javascript:void(0);" onclick="orderDetail(${ordersVo.id});">详情</a>
													</td>
												</tr>
											</c:forEach>

										</c:when>
									</c:choose>
								</tbody>
							</table>
							<tags:pageBar url="/web/orders/listPagedWebOrder"></tags:pageBar>
						</div>
					</div>
				</div>
			</div>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->
	</div>
	<style type="text/css">
		#contentBody table{
			table-layout: fixed;
		}
		#contentBody td,#infoList th{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
	</style>
</body>
</html>

