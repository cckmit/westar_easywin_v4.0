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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8"
	src="/static/js/sp_center/sp_center.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
$(function(){
	//操作删除和复选框
	$('tr').click(function(){
		var radio = $(this).find("input[type=radio]");
		$(radio).attr("checked","checked")
	});
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
//返回值
function flowSelected(){
	var result;
	var radio = $("input[type=radio]:checked");
	if(radio.length==0){
		window.top.layer.alert("请选择客户信息",{icon:7});
	}else{
		var flowId = $(radio).attr("flowId"); 
		var flowName = $(radio).attr("flowName"); 
		result={"flowId":flowId,"flowName":flowName};
	}
	return result;
}

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
</script>
<style>
.table {
	margin-bottom: 0px;
}

.table tbody>tr>td {
	padding: 5px 0px;
}

tr {
	cursor: auto;
}
</style>
</head>
<body style="background-color: #fff">
	<div class="widget no-margin">
		<div
			class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<form action="/flowDesign/listFlowModelForSelect" id="searchForm">
				<input type="hidden" name="sid" value="${param.sid}"> <input
					type="hidden" name="searchTab" value="${param.searchTab}">
				<input type="hidden" name="flowName" value="${spFlowModel.flowName}">
				<input type="hidden" name="spFlowTypeId"
					value="${spFlowModel.spFlowTypeId}"> <input type="hidden"
					name="spFlowTypeName" value="${spFlowModel.spFlowTypeName}">
				<input type="hidden" name="orderBy" value="${spFlowModel.orderBy}">
				<div class="btn-group pull-left searchCond">
					<div class="table-toolbar ps-margin">
						<div class="btn-group">
							<a class="btn btn-default dropdown-toggle btn-xs"
								data-toggle="dropdown" title="排序"> <c:choose>
									<c:when test="${not empty spFlowModel.orderBy}">
										<font style="font-weight:bold;"> <c:if
												test="${spFlowModel.orderBy=='flowNameDesc'}">按流程名称(降序)</c:if>
											<c:if test="${spFlowModel.orderBy=='flowNameAsc'}">按流程名称(升序)</c:if>
											<c:if test="${spFlowModel.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if>
											<c:if test="${spFlowModel.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
										</font>
									</c:when>
									<c:otherwise>排序</c:otherwise>
								</c:choose> <i class="fa fa-angle-down"></i> </a>
							<ul class="dropdown-menu dropdown-default">
								<li><a href="javascript:void(0)" onclick="orderByClean()">不限条件</a>
								</li>
								<li><a href="javascript:void(0)"
									onclick="orderBy('flowNameDesc');">按流程名称(降序)</a></li>
								<li><a href="javascript:void(0)"
									onclick="orderBy('flowNameAsc');">按流程名称(升序)</a></li>
								<li><a href="javascript:void(0)"
									onclick="orderBy('crTimeNewest');">按创建时间(降序)</a></li>
								<li><a href="javascript:void(0)"
									onclick="orderBy('crTimeOldest');">按创建时间(升序)</a></li>
							</ul>
						</div>
					</div>
					<div class="table-toolbar ps-margin">
						<div class="btn-group">
							<a class="btn btn-default dropdown-toggle btn-xs"
								data-toggle="dropdown"> <c:choose>
									<c:when test="${not empty spFlowModel.spFlowTypeId}">
										<font style="font-weight:bold;">${spFlowModel.spFlowTypeName}
										</font>
									</c:when>
									<c:otherwise>流程分类筛选</c:otherwise>
								</c:choose> <i class="fa fa-angle-down"></i> </a>
							<ul class="dropdown-menu dropdown-default">
								<c:choose>
									<c:when test="${not empty listSpFlowType}">
										<li><a href="javascript:void(0)"
											onclick="spFlowTypeClean();">不限条件</a></li>
										<c:forEach items="${listSpFlowType}" var="spFlowType"
											varStatus="status">
											<li><a href="javascript:void(0)"
												typeId="${spFlowType.id}" typeName="${spFlowType.typeName}"
												onclick="selectBySpFlowType(this);">${spFlowType.typeName}</a>
											</li>
										</c:forEach>
										<li><a href="javascript:void(0)" typeId="0"
											onclick="selectBySpFlowType(this);">未分类</a></li>
									</c:when>
									<c:otherwise>
										<li><a href="javascript:void(0)">无流程分类</a></li>
									</c:otherwise>
								</c:choose>
							</ul>
						</div>
					</div>
				</div>
				<div class="ps-margin ps-search">
					<span class="input-icon"> <input
						class="form-control ps-input" id="searchFlowName"
						value="${spFlowModel.flowName}" type="text" placeholder="请输入关键字">
						<a href="#" class="ps-searchBtn"><i
							class="glyphicon glyphicon-search circular danger"></i>
					</a> </span>
				</div>
			</form>
		</div>
		<c:choose>
			<c:when test="${not empty listFlowModel}">
				<div class="widget-body" id="contentBody"
					style="overflow-y:auto;position: relative;">
					<table class="table table-striped table-hover general-table">
						<thead>
							<tr>
								<th>序号</th>
								<th>流程名称</th>
								<th>流程类型</th>
								<th>状态</th>
								<th>创建人</th>
								<th>创建日期</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listFlowModel}" var="flow" varStatus="status">
								<tr>
									<td style="text-align: center;">
										<label> <input
											class="colored-blue" type="radio" name="flowId"
											${param.flowId==flow.id? 'checked="checked"
											':'' }
					           			value="${flow.id}"
											flowId="${flow.id}" flowName="${flow.flowName}" /> <span
											class="text">&nbsp;</span> 
										</label>
									</td>
									<td><tags:cutString num="31">${flow.flowName}</tags:cutString> </td>
									<td><c:choose>
											<c:when test="${flow.spFlowTypeId==0}">无类别</c:when>
											<c:otherwise>${flow.spFlowTypeName}</c:otherwise>
										</c:choose></td>
									<td><c:if test="${flow.status==1}">
											<span style="color:green;">启用</span>
										</c:if> <c:if test="${flow.status==0}">
											<span style="color:red;">禁用</span>
										</c:if></td>
									<td>${flow.creatorName}</td>
									<td>${flow.recordCreateTime}</td>
								</tr>
							</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="widget-body"
									style="height:550px; text-align:center;padding-top:155px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>还没有配置部署相关的团队固化流程哦！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
										</div>
									</section>
								</div>
							</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<tags:pageBar url="/flowDesign/listFlowModelForSelect"></tags:pageBar>
				</div>
	</div>
	<script src="/static/assets/js/bootstrap.min.js"></script>
	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
