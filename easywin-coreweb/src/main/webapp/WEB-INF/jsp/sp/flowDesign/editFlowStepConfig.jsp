<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8"
	src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
var sid="${param.sid}";//全局变量
$(function(){
	//关联表单
	$("#formName").click(function(){
		formModListForSelect();//弹窗打开表单候选列表页面
	});
});
//表单选择后回调
function formModSelectedReturn(formModId,formModName){
	if(!strIsNull(formModId) && !strIsNull(formModName)){
		$("#formName").val(formModName);
		$("#formKey").val(formModId);
		updateFlowAttr("formKey");
	}else{
		window.top.layer.msg("请选择\"表单关联失败\"！formName="+formName+" & formKey="+formKey,{icon:2});
	}
}
$(document).ready(function() {
	resizeVoteH('otherFlowModelAttrIframe');
});
</script>
<style type="text/css">
.tab-content{padding:10px 12px}
</style>
</head>
<body>
	<div class="row">
		<div class="col-md-12 col-xs-12 ">
			<div class="container" style="padding: 0px 0px;width: 100%">
				<div class="row" style="margin: 0 0">
					<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
						<div class="widget" style="margin-top: 0px;">
							<div class="widget-main ">
								<div class="tabbable">
									<div class="tab-content">
										<div class="widget radius-bordered">
											<div class="no-shadow">
												<div class="tickets-container bg-white">
													<ul class="tickets-list">
														<li class="ticket-item no-shadow ps-listline">流程步骤
															<div class="widget-body">
																<form action="/flowDesign/delFlowStep" id="flowStepForm">
																	<input type="hidden" name="sid" value="${param.sid}">
																	<input type="hidden" name="activityMenu"
																		value="${activityMenu}" /> <input type="hidden"
																		name="flowId" /> <input type="hidden" name="stepId" />
																	<table class="table table-striped table-hover"
																		id="editabledatatable">
																		<thead>
																			<tr role="row">
																				<th>序号</th>
																				<th>步骤名称</th>
																				<th>下一步骤</th>
																				<th>操作</th>
																			</tr>
																		</thead>
																		<tbody>
																			<c:choose>
																				<c:when test="${not empty flowConfig.listFlowSteps}">
																					<c:forEach items="${flowConfig.listFlowSteps}"
																						var="flowStep" varStatus="vs">
																						<tr>
																							<td>${vs.count}</td>
																							<td><a href="javascript:void(0);"
																										onclick="editFlowStep(${flowConfig.id},${flowStep.id},'${flowStep.nextStepWay}');">
																										<tags:cutString num="31">${flowStep.stepName}</tags:cutString>
																								 </a>
																							</td>
																							<td><c:choose>
																									<c:when
																										test="${not empty flowStep.listNextStep}">
																										<table>
																											<c:forEach items="${flowStep.listNextStep}"
																												var="nextStep" varStatus="vs">
																												<tr>
																													<td>
																														${nextStep.stepName}
																														<c:if test="${fn:length(flowStep.listNextStep)>1}">
																														${nextStep.defaultStep==1?'<span style="font-size:11px;color:red;">&nbsp;-&nbsp;默认步骤</span>':''}
																														</c:if>
																													</td>
																												</tr>
																											</c:forEach>
																										</table>
																									</c:when>
																								</c:choose></td>
																							<td><c:if test="${flowStep.stepType!='end'}">
																									<a href="javascript:void(0);"
																										onclick="editFlowStep(${flowConfig.id},${flowStep.id},'${flowStep.nextStepWay}');">编辑</a>
																											|&nbsp; 
																											<a href="javascript:void(0);"
																										onclick="addFlowStep(${flowConfig.id},${flowStep.id});">添加下一步</a>
																								</c:if> 
																								<c:if
																									test="${flowStep.stepType!='start' && flowStep.stepType!='end'}">
																											|&nbsp; 
																											<a href="javascript:void(0);"
																										onclick="addFlowStepConditions(${flowStep.flowId},${flowStep.id});">设置条件</a>
																											|&nbsp; 
																											<a href="javascript:void(0);"
																										onclick="delFlowStep(${flowStep.flowId},${flowStep.id});">删除</a>
																								</c:if></td>
																						</tr>
																					</c:forEach>
																				</c:when>
																			</c:choose>
																		</tbody>
																	</table>
																</form>
															</div>
															<div class="ps-clear"></div>
														</li>
													</ul>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">
		var jq11 = $.noConflict(true);
	</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>