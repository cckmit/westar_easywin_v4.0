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
<head>
	<meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<title><%=SystemStrConstant.TITLE_NAME%></title>
	<meta name="description" content="Dashboard" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- 框架样式 -->
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<script type="text/javascript">
	
		var pageParam = {
				"sid":"${param.sid}",
				"isBusinessTrip":"${isBusinessTrip}"
		}
		var sid="${param.sid}";//sid全局变量
		$(function(){
			$(".optTr").click(function(){
				$(this).find("[type='radio']").attr("checked","checked");
			});
			//设置滚动条高度
			var height = $(window).height() -30;
			$("#contentBody").css("height",height+"px");
		});
		//选择关联的出差记录
		function selectObj(ts){
			var loan = $(ts).data("loan");
			if(loan){
				return loan;
			}else{
				layer.tips("审核中不能报销！",$(ts),{tips:1})
				return null;
			}
		}
	</script>
	<style type="text/css">
		#infoList table{
			table-layout: fixed;
		}
		#infoList td,#infoList th{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
		.panel-heading{
			padding: 5px 20px
		}
		.panel-body{
			padding: 0px;
		}
		.table{
		margin-bottom:0px;
		}
		.pagination-lg >li>a{
		font-size: small;
		}
		.pagination{
		margin: 10px 0px
		}
	</style>
</head>
<body>
	<!-- Page Content -->
	<div>
		<!-- Page Body -->
		<div class="page-body" style="padding:1px;">
			<div class="row">
				<div class="col-md-12 col-xs-12 " id="infoList">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form id="searchForm" class="subform">
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													排序
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="orderByClean()">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeNewest');">按创建时间(降序)</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeOldest');">按创建时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty loan.startDate || not empty loan.endDate}">
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
														<span class="btn-xs">起止时间：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${loan.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${loan.endDate}" id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10" style="text-align: center;">
														<button type="submit" class="btn btn-primary btn-xs">查询</button>
														<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
													</div>
												</div>
											</div>
										</div>
									</div>
								</form>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="searchFlowName" value="${loan.flowName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
										<a href="javascript:void(0)" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
								
							</div>
							<div class="widget-buttons ps-widget-buttons">
							</div>
						</div>
						<div class="widget-body" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
								<table class="table table-bordered " id="editabledatatable">
									<thead>
										<tr role="row">
											<th style="width: 5%;padding: 0 0;text-align: center;">序</th>
											<th style="width: 12%">申请时间</th>
											<th>额度申请记录</th>
											<th style="width: 19%">起止时间</th>
											<th style="width: 12%">借款限额</th>
											<th style="width: 11%;text-align: center;">状态</th>
											<th style="width: 7%;padding: 0 0;text-align: center;">操作</th>
										</tr>
									</thead>
									<tbody id="allTodoBody">
										
									</tbody>
								</table>
								<div class="panel-body ps-page bg-white" style="font-size: 12px">
										 <p class="pull-left ps-pageText">共<b class="badge" id="totalNum">11</b>条记录</p>
										 <ul class="pagination pull-right" id="pageDiv">
										 </ul>
									</div>
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
<!--主题颜色设置按钮 end-->
<script src="/static/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" charset="utf-8" src="/static/js/financial/loanForOffSelect.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
</html>
