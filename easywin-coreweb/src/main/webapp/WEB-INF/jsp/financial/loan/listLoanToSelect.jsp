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
	<!-- 审批相关JS -->
	<script type="text/javascript" charset="utf-8" src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<!-- 财务模块JS -->
	<script type="text/javascript" charset="utf-8" src="/static/js/financial/financial.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
	<script type="text/javascript">
		var sid="${param.sid}";//sid全局变量
		$(function(){
			$(".optTr").click(function(){
				$(this).find("[type='radio']").attr("checked","checked");
			});
			//设置滚动条高度
			var height = $(window).height();
			$("#contentBody").css("height",height+"px");
		});
		//选择关联的出差记录
		function selectedTrip(){
			var result;
			var radio = $("input[type=radio]:checked");
			if(radio.length==0){
				window.top.layer.alert("请选择需借款的“出差记录”",{icon:7});
			}else{
				var loanId = $(radio).attr("loanId"); 
				var loanName = $(radio).attr("loanName"); 
				result={"loanId":loanId,"loanName":loanName};
			}
			return result;
		}
	</script>
</head>
<body>
	<!-- Page Content -->
	<div>
		<!-- Page Body -->
		<div class="page-body" style="padding:1px;">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form action="/financial/loan/listLoanToSelect" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="flowState" value="${loan.flowState}">
									<input type="hidden" name="flowName" value="${loan.flowName}">
									<input type="hidden" name="executor" value="${loan.executor}">
									<input type="hidden" name="executorName" value="${loan.executorName}">
									<input type="hidden" name="orderBy" value="${loan.orderBy}">
									<input type="hidden" name="creator" value="${loan.creator}">
									<input type="hidden" name="creatorName" value="${loan.creatorName}">
									<input type="hidden" name="spState" value="${loan.spState}">
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													<c:choose>
														<c:when test="${not empty loan.orderBy}">
															<font style="font-weight:bold;"> 
																<c:if test="${loan.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if> 
																<c:if test="${loan.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
															</font>
														</c:when>
														<c:otherwise>排序</c:otherwise>
													</c:choose>
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
										<a href="#" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty listLoan}">
						<div class="widget-body" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<form action="/task/delTask?sid=${param.sid}" method="post"
								id="delForm">
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover">
									<thead>
										<tr role="row">
											<th>序号</th>
											<th>事项名称</th>
											<th>申请人</th>
											<th>申请时间</th>
											<th>借款记录</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listLoan}" var="loanVo" varStatus="vs">
											<tr class="optTr" style="cursor:pointer;">
												<td class="optTd" style="height: 47px"><label
													class="optCheckBox" style="display: none;width: 20px">
														<input class="colored-blue" type="radio" name="ids" value="${loanVo.id}"
														${(loanVo.creator==userInfo.id)?'':'disabled="disabled"'} loanId="${loanVo.id}" loanName="${loanVo.flowName}" /> <span class="text"></span>
												</label> <label class="optRowNum"
													style="display: block;width: 20px">${vs.count}</label></td>
												<td>
													<tags:cutString num="31">${loanVo.flowName}</tags:cutString>
												</td>
												<td>
													<div class="ticket-user pull-left other-user-box">
														<img class="user-avatar"
															src="/downLoad/userImg/${loanVo.comId}/${loanVo.creator}?sid=${param.sid}"
															title="${loanVo.creatorName}" />
														<span class="user-name">${loanVo.creatorName}</span>
													</div>
												</td>
												<td>${loanVo.recordCreateTime}</td>
												<td>
													<c:if test="${loanVo.status eq '1'}">
														<c:choose>
															<c:when test="${not empty loanVo.writeOffFlowInstanceId and loanVo.writeOffFlowState ne 0}">
																<c:if test="${loanVo.writeOffStatus eq 0 
																				and loanVo.writeOffFlowSpState eq 1 
																				and loanVo.writeOffFlowState eq 1}">
																	<span class="blue" style="font-weight:bold;">销账中</span>
																</c:if>
																<c:if test="${loanVo.writeOffStatus eq 1 
																				and loanVo.writeOffFlowSpState eq 1 
																				and loanVo.writeOffFlowState eq 4}">
																	<span class="green" style="font-weight:bold;">已销账</span>
																</c:if>
															</c:when>
															<c:otherwise>
																<span class="red" style="font-weight:bold;">未销账</span>
															</c:otherwise>
														</c:choose>
													</c:if>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/financial/loan/listLoanToSelect"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:350px; text-align:center;padding-top:50px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>没有相关借款数据！</h2>
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
<!--主题颜色设置按钮 end-->
<script src="/static/assets/js/bootstrap.min.js"></script>
</html>
