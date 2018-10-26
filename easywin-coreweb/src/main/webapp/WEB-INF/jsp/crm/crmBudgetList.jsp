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
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<script type="text/javascript">
			// 基于准备好的dom，初始化echarts实例

			$(function() {
				$(".subform").Validform({
					tiptype: function(msg, o, cssctl) {
						validMsg(msg, o, cssctl);
					},
					callback: function(form) {
						//提交前验证是否在上传附件
						return sumitPreCheck(null);
					},
					datatype: {
						"minFloat": function(gets, obj, curform, regxp) {
							return isFloat($("#minBudget").val()) || /^\s*$/.test($("#minBudget").val());
						},
						"maxFloat": function(gets, obj, curform, regxp) {
							return isFloat($("#maxBudget").val()) || /^\s*$/.test($("#minBudget").val());
						},
					},
					showAllError: true
				});

			})
			//导出列表
			function excelExportCrmList(){
			 	var options = $("#owner_select").find("option");
	  			var listOwner = returnListOwner(options);
	  			var listOwnerStr ='';
	  			if(listOwner.length>0){
	  				listOwnerStr = '{"listOwner":'+JSON.stringify(listOwner)+'}'
	  			}
	  			var crmTypeOptions = $("#crmType_select").find("option");
				var listCrmType = returnListCrmType(crmTypeOptions);
				var listCrmTypeStr = '';
				if(listCrmType.length > 0) {
					listCrmTypeStr = '{"listCrmType":' + JSON.stringify(listCrmType) + '}'
				}
				
		   		var areaIdAndType = $("#areaIdAndType").val();
 				var startDate = $("#startDate").val();
 				var endDate = $("#endDate").val();
 				var customerName = $("#customerName").val();
 				var frequenStartDate = $("#frequenStartDate").val();
 				var frequenEndDate = $("#frequenEndDate").val();
 				var minBudget = $("#minBudget").val();
 				var maxBudget = $("#maxBudget").val();
 				var stage = $("#stage").val();
 				
 				var otherUrl = "/crm/excelExportCrmList?sid=${param.sid}&fileName=资金预算表 ";
 	    	    otherUrl +="&areaIdAndType="+areaIdAndType
 	    	    otherUrl +="&startDate="+startDate
 	    	    otherUrl +="&endDate="+endDate
 	    	    otherUrl +="&customerName="+customerName
 	    	  	 otherUrl +="&listOwnerStr="+listOwnerStr
 	    		otherUrl +="&listCrmTypeStr="+listCrmTypeStr
 	    		otherUrl +="&frequenStartDate="+frequenStartDate
 	    		otherUrl +="&frequenEndDate="+frequenEndDate
 	    		otherUrl +="&minBudget="+minBudget
 	    		otherUrl +="&maxBudget="+maxBudget
 	    		otherUrl +="&stage="+stage
 	    		window.open ( otherUrl, "_blank" ) ;
			}
		</script>
	</head>

	<body>
		<!-- Page Content -->
		<div class="page-content">
			<!-- Page Body -->
			<div class="page-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="widget">
							<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
								<div>
									<form action="/crm/customerListPage" id="searchForm" class="subform">
										<input type="hidden" name="sid" value="${param.sid}">
										<input type="hidden" id="pageSize" name="pager.pageSize" value="15">
										<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab}">
										<input type="hidden" name="statisticsType" id="statisticsType" value="${param.statisticsType}">
										<input type="hidden" name="ownerType" id="ownerType" value="${customer.ownerType}">
										<input type="hidden" name="customerTypeId" id="customerTypeId" value="${customer.customerTypeId}">
										<input type="hidden" name="owner" id="owner" value="${customer.owner}">
										<input type="hidden" name="areaIdAndType" id="areaIdAndType" value="${customer.areaIdAndType}">
										<input type="hidden" name="orderBy" id="orderBy" value="${customer.orderBy}">
										<input type="hidden" name="stage" id="stage" value="${customer.stage}">
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
															客户类型筛选
															<i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li>
																<a href="javascript:void(0);" onclick="customerTypeFilter(this,'')">不限条件</a>
															</li>
															<li>
																<a href="javascript:void(0);" onclick="crmTypeMoreTree('${param.sid}','crmType');">类型选择</a>
															</li>
														</ul>
													</div>
												</div>
												<div style="float: left;width: 250px;display: none">
													<select list="listCrmType" listkey="id" listvalue="typeName" id="crmType_select" name="listCrmType.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
														<c:forEach items="${customer.listCrmType }" var="obj" varStatus="vs">
															<option selected="selected" value="${obj.id }">${obj.typeName }</option>
														</c:forEach>
													</select>
												</div>

												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
															<c:choose>
																<c:when test="${not empty customer.stage}">
																	<c:forEach items="${listCrmStage}" var="crmStage" varStatus="status">
																		<c:if test="${crmStage.id==customer.stage}">
																			<font style="font-weight:bold;">${crmStage.stageName}</font>
																		</c:if>
																	</c:forEach>
																</c:when>
																<c:otherwise>所属阶段</c:otherwise>
															</c:choose>
															<i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li>
																<a href="javascript:void(0);" onclick="customerStageFilter(this,'')">不限阶段</a>
															</li>
															<c:choose>
																<c:when test="${not empty listCrmStage}">
																	<c:forEach items="${listCrmStage}" var="crmStage" varStatus="status">
																		<li>
																			<a href="javascript:void(0);" onclick="customerStageFilter(this,'${crmStage.id}')">${crmStage.stageName}</a>
																		</li>
																	</c:forEach>
																</c:when>
															</c:choose>
														</ul>
													</div>
												</div>

												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
															<c:choose>
																<c:when test="${not empty customer.areaName}">
																	<font style="font-weight:bold;">${customer.areaName}</font>
																</c:when>
																<c:otherwise>区域筛选</c:otherwise>
															</c:choose>
															<i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li>
																<a href="javascript:void(0);" onclick="artOpenerCallBack('')">不限区域</a>
															</li>
															<li>
																<a href="javascript:void(0);" onclick="areaOne('areaIdAndType','areaName','${param.sid}','1')">区域选择</a>
															</li>
														</ul>
													</div>
												</div>
												<c:if test="${param.searchTab!=11 && not empty param.searchTab}">
													<div class="table-toolbar ps-margin">
														<div class="btn-group">
															<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
																<c:choose>
																	<c:when test="${not empty customer.ownerName}">
																		<font style="font-weight:bold;">${customer.ownerName}</font>
																	</c:when>
																	<c:otherwise>责任人筛选</c:otherwise>
																</c:choose>
																<i class="fa fa-angle-down"></i>
															</a>
															<ul class="dropdown-menu dropdown-default">
																<li>
																	<a href="javascript:void(0);" onclick="userOneForUserIdCallBack('','owner','','')">不限条件</a>
																</li>
																<li>
																	<a href="javascript:void(0);" onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a>
																</li>
															</ul>
														</div>
													</div>
													<div style="float: left;width: 250px;display: none">
														<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
															<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
																<option selected="selected" value="${obj.id }">${obj.userName }</option>
															</c:forEach>
														</select>
													</div>
												</c:if>
												<div class="table-toolbar ps-margin">
													<div class="btn-group cond" id="moreCondition_Div">
														<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
															<c:choose>
																<c:when test="${not empty customer.startDate || not empty customer.endDate || not empty customer.frequenStartDate || not empty customer.frequenEndDate ||  customer.minBudget >0 || customer.maxBudget >0}">
																	<font style="font-weight:bold;">筛选中</font>
																</c:when>
																<c:otherwise>
																	更多
																</c:otherwise>
															</c:choose>
															<i class="fa fa-angle-down"></i>
														</a>
														<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
															<div class="ps-margin ps-search padding-left-10">
																<div>
																	<span>创建时间筛选：</span>
																</div>
																<div class="margin-left-20">
																	<span class="btn-xs">起</span>
																	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.startDate}" id="startDate" name="startDate" placeholder="开始时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																	<span class="btn-xs">止</span>
																	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.endDate}" id="endDate" name="endDate" placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
																</div>
															</div>
															<div class="ps-margin ps-search padding-left-10">
																<div>
																	<span>未更新时间筛选：</span>
																</div>
																<div class="margin-left-20">
																	<span class="btn-xs">起</span>
																	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.frequenStartDate}" id="frequenStartDate" name="frequenStartDate" placeholder="开始时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'frequenEndDate\',{d:-0});}'})" />
																	<span class="btn-xs">止</span>
																	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.frequenEndDate}" id="frequenEndDate" name="frequenEndDate" placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'frequenStartDate\',{d:+0});}'})" />
																</div>
															</div>
															<div class="ps-margin ps-search padding-left-10">
																<div>
																	<span>资金预算筛选：</span>
																</div>
																<div class="margin-left-20">
																	<span class="btn-xs">起</span>
																	<input class="form-control dpd2 no-padding condDate" type="text" datatype="minFloat" value="${customer.minBudget}" placeholder="最小资金" id="minBudget" name="minBudget" />
																	<span class="btn-xs">止</span>
																	<input class="form-control dpd2 no-padding condDate" type="text" datatype="maxFloat" value="${customer.maxBudget}" placeholder="最大资金" id="maxBudget" name="maxBudget" />
																</div>
															</div>
															<div class="ps-clear padding-top-10" style="text-align: center;">
																<button type="submit" class="btn btn-primary btn-xs">查询</button>
																<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="ps-margin ps-search">
												<span class="input-icon">
														<input id="customerName" name="customerName" class="form-control ps-input" 
														type="text" placeholder="请输入关键字" value="${customer.customerName}">
														<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
													</span>
											</div>
										</div>
									</form>
									<div class="widget-buttons ps-widget-buttons">
										<a class="btn btn-info  btn-xs"  onclick="excelExportCrmList()" >导出列表</a>
									</div>
								</div>
								<div id="listOwner_show"  class=" padding-top-10 text-left " style="display:${empty customer.listOwner ? 'none':'block'}">
									<strong>责任人筛选:</strong>
									<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
										<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
									</c:forEach>
								</div>
								<div id="listCrmType_show" class="padding-top-10 text-left " style="display:${empty customer.listCrmType ? 'none':'block'}">
									<strong>客户类型筛选:</strong>
									<c:forEach items="${customer.listCrmType }" var="obj" varStatus="vs">
										<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('crmType','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.typeName }</span>
									</c:forEach>
								</div>
							</div>
							<c:choose>
								<c:when test="${not empty listCustomer}">
									<div class="widget-body">
										<form action="/crm/delCustomer" id="delForm">
											<input type="hidden" name="redirectPage" id="redirectPage" />
											<input type="hidden" name="sid" value="${param.sid}" />
											<table class="table table-striped table-hover" id="editabledatatable">
												<thead>
													<tr role="row">
														<th>序号</th>
														<th>资金预算</th>
														<th>客户名称</th>
														<th>客户类型</th>
														<th>所属阶段</th>
														<th>责任人</th>
														<th>创建日期</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${listCustomer}" var="obj" varStatus="vs">
														<tr class="optTr">
															<td class="optTd" style="height: 47px">
																<label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
															</td>
															<td class="center">${obj.budget }</td>
															<td>
																<a href="javascript:void(0)" class="item-box ${obj.isRead==0?" noread ":" " }" onclick="readMod(this,'crm',${obj.id},'012');viewCustomer(${obj.id})">
																	<tags:cutString num="31">${obj.customerName}</tags:cutString>
																</a>
															</td>
															<td>${obj.typeName }</td>
															<td>${obj.stageName }</td>
															<!--	<td>${obj.areaName }</td>-->
															<td>
																<div class="ticket-user pull-left other-user-box">
																	<div class="ticket-user pull-left other-user-box" data-container="body" data-placement="left" data-toggle="popover" data-user='${obj.owner}' data-busId='${obj.id}' data-busType='012'>
																		<img class="user-avatar" src="/downLoad/userImg/${obj.comId}/${obj.owner}?sid=${param.sid}" title="${obj.ownerName}" />
																		<i class="user-name">${obj.ownerName}</i>
																	</div>
																</div>
															</td>
															<td>${fn:substring(obj.recordCreateTime,0,10) }</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</form>
										<tags:pageBar url="/crm/customerListPage"></tags:pageBar>
									</div>
								</c:when>
								<c:otherwise>
									<div class="widget-body" style="height:515px; text-align:center;padding-top:155px">
										<section class="error-container text-center">
											<h1>
													<i class="fa fa-exclamation-triangle"></i>
												</h1>
											<div class="error-divider">
												<h2>您还没有相关的客户数据！</h2>
												<p class="description">协同提高效率，分享拉近距离。</p>
												<a href="javascript:void(0);" onclick="addCustomer();" class="return-btn"><i class="fa fa-plus"></i>新建客户</a>
											</div>
										</section>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<!-- 	<div class="col-sm-3">
						<div class="widget">
							<div class="widget-header bordered-bottom bordered-themeprimary ps-titleHeader" style="padding-top:5px">
								<i class="widget-icon fa fa-tasks themeprimary"></i>
								<span class="widget-caption themeprimary">客户预算资金统计</span>
							</div>
							<div class="widget-body">
								<div id="main" style="width:100%;height:500px"></div>
							</div>
						</div>
						<script type="text/javascript">
							var myChart = echarts.init(document.getElementById('main'));
							var url = '/crm/listCrmStatisByBudget?sid=${param.sid}';

							var options = $("#owner_select").find("option");
							var listOwner = returnListOwner(options);
							var listOwnerStr = '';
							if(listOwner.length > 0) {
								listOwnerStr = '{"listOwner":' + JSON.stringify(listOwner) + '}'
							}
							var params = {
								"areaIdAndType": $("#areaIdAndType").val(),
								"startDate": $("#startDate").val(),
								"endDate": $("#endDate").val(),
								"stage": $("#stage").val(),
								"listOwnerStr": listOwnerStr
							}
							getSelfJSON(url, params, function(data) {
								initChart(data);
							});

							function initChart(data) {
								var dataObjs = data.listStatis
								var serieData = new Array();
								var legendData = new Array();
								if(dataObjs.length > 0) {
									for(var i = 0; i < dataObjs.length; i++) {
										var obj = dataObjs[i];
										legendData.push(obj.name);
										serieData.push({ 'value': obj.value, 'name': obj.name, 'type': obj.type });
									}
								} else {
									legendData.push("没有相关数据");
									serieData.push({ 'value': 0, 'name': "没有相关数据", 'type': "all" });
								}

								option = {
									tooltip: {
										trigger: 'item',
										formatter: "{a} <br/>{b} : {c} ({d}%)"
									},
									legend: {
										orient: 'horizontal',
										x: 'left',
										backgroundColor: 'rgba(0,0,0,0)',
										borderColor: '#ccc', // 图例边框颜色
										borderWidth: 0, // 图例边框线宽，单位px，默认为0（无边框）
										padding: 0, // 图例内边距，单位px，默认各方向内边距为5，
										// 接受数组分别设定上右下左边距，同css
										itemGap: 10, // 各个item之间的间隔，单位px，默认为10，
										// 横向布局时为水平间隔，纵向布局时为纵向间隔
										itemWidth: 10, // 图例图形宽度
										itemHeight: 10, // 图例图形高度
										data: legendData
									},

									calculable: false,
									series: [{
										name: '客户类型',
										type: 'pie',
										radius: ['40%', '70%'],
										center: ['50%', '60%'],
										itemStyle: {
											normal: {
												label: {
													position: 'inner',
													formatter: function(params) {
														var percent = (params.percent - 0).toFixed(0);
														var flag = false;
														if(percent == 0 && flag == 'false') {
															return '';
														}
														return params.data.value;
													}
												},
												labelLine: {
													show: false
												}
											},
											emphasis: {
												label: {
													show: true,
													formatter: function(params) {
														var percent = (params.percent - 0).toFixed(2);
														var flag = false;
														if(percent == 0 && flag == 'false') {
															return '';
														}
														return params.data.value;
													}
												}
											}

										},
										data: serieData
									}]
								};

								// 使用刚指定的配置项和数据显示图表。
								myChart.setOption(option);
							}
							//查询客户类型
							myChart.on('click', function(params) {
								customerTypeFilter(null, params.data.type);
							});
							window.onresize = myChart.resize;
						</script>
					</div> -->
				</div>
			</div>
		</div>
	</body>

</html>