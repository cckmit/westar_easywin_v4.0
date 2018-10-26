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
		//选择关联的借款记录
		function selectedLoan(){
			var result;
			var radio = $("input[type=radio]:checked");
			if(radio.length==0){
				window.top.layer.alert("请选择需借款的“出差记录”",{icon:7});
			}else{
				var loanId = $(radio).attr("loanId"); 
				var loanName = $(radio).attr("loanName"); 
				var instanceId = $(radio).attr("instanceId");
				result={"loanId":loanId,"loanName":loanName,"instanceId":instanceId};
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
						<c:choose>
						<c:when test="${not empty listLoanOfTrip}">
						<div class="widget-body" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
							<form action="/task/delTask?sid=${param.sid}" method="post"
								id="delForm">
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover">
									<thead>
										<tr role="row">
											<th>序号</th>
											<th>事项名称</th>
											<th>审核状态</th>
											<th>申请人</th>
											<th>申请时间</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listLoanOfTrip}" var="loanVo" varStatus="vs">
											<tr class="optTr" style="cursor:pointer;">
												<td class="optTd" style="height: 47px"><label
													class="optCheckBox" style="display: none;width: 20px">
														<input class="colored-blue" type="radio" name="ids" value="${loanVo.id}"
														${(loanVo.creator==userInfo.id)?'':'disabled="disabled"'} instanceId="${loanVo.instanceId}" loanId="${loanVo.id}" loanName="${loanVo.flowName}" /> <span class="text"></span>
												</label> <label class="optRowNum"
													style="display: block;width: 20px">${vs.count}</label></td>
												<td>
													<tags:cutString num="31">${loanVo.flowName}</tags:cutString>
												</td>
												<td>
													<c:if test="${loanVo.flowState==0}">
														<span class="gray" style="font-weight:bold;">无效的</span>
													</c:if> <c:if test="${loanVo.flowState==1}">
														<span class="blue" style="font-weight:bold;">审核中</span>
													</c:if> <c:if test="${loanVo.flowState==2}">
														<span style="color:fuchsia;font-weight:bold;">草稿</span>
													</c:if> 
													<c:if test="${loanVo.flowState==4 and loanVo.spState==1}">
														<span class="green" style="font-weight:bold;">申请通过</span>
													</c:if>
													<c:if test="${loanVo.flowState==4 and loanVo.spState==0}">
														<span class="red" style="font-weight:bold;">申请驳回</span>
													</c:if>
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
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/financial/loan/listLoanOfTrip"></tags:pageBar>
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
