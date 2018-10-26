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
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>

<!-- Custom styles for this template -->
<link href="/static/css/style.css" rel="stylesheet">
<link href="/static/css/style-responsive.css" rel="stylesheet" />

<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function() {
		resizeVoteH('otherSpAttrIframe');
	});

	//页面刷新
	$(function() {
		//初始化名片
		initCard('${param.sid}');
		$("#refreshImg").click(function() {
			var win = art.dialog.open.origin;//来源页面
			// 如果父页面重载或者关闭其子对话框全部会关闭
			win.location.reload();
		});
	});
</script>
<style type="text/css">
.timeline-item.alt h1 {
	text-align: left !important;
}

.timeline-item h1 span {
	display: inline-block;
	width: 55px !important;
	height: 15px;
	text-align: justify !important;
	*text-justify: inter-ideograph;
	vertical-align: top;
	overflow: hidden;
	*zoom: 1;
}

.timeline-item h1 span i {
	display: inline-block;
	width: 100%;
	height: 0;
}

.black {
	color: rgba(0, 0, 0, 0.7);
}
</style>
</head>
<body style="background-color:#FFFFFF;">
	<!--main content start-->
	<section id="main-content" style="width:100%;margin-left:0px">
		<section class="wrapper"
			style="padding:15px;margin-top: 0px;display:inline-block;width:100%">
			<!-- page start-->
			<div class="time-border">

				<c:choose>
					<c:when test="${not empty listSpFlowHiStep}">

						<c:forEach items="${listSpFlowHiStep}" var="obj" varStatus="vs">
							<%-- <c:set var="takeTimeLong" value="${3*1000*60*60*24-100}"></c:set> --%>
							<c:set var="takeTimeLong" value="${obj.usedTime}"></c:set>
							<fmt:formatNumber
								value="${takeTimeLong>=(1000*60*60*24)?(takeTimeLong - takeTimeLong mod(1000*60*60*24)) /1000/60/60/24:0}"
								pattern="#0" var="dd" />

							<c:set var="takeTimeD" value="${takeTimeLong - dd*1000*60*60*24}"></c:set>
							<fmt:formatNumber
								value="${takeTimeD>=(1000*60*60)?(takeTimeD - takeTimeD mod(1000*60*60)) /1000/60/60:0}"
								pattern="#0" var="hh" />

							<c:set var="takeTimeH" value="${takeTimeD - hh*1000*60*60}"></c:set>
							<fmt:formatNumber
								value="${takeTimeH>=(1000*60)?(takeTimeH - takeTimeH mod(1000*60)) /1000/60:0}"
								pattern="#0" var="mm" />

							<c:set var="takeTimeM" value="${takeTimeH - mm*1000*60}"></c:set>
							<fmt:formatNumber
								value="${takeTimeM>=(1000)?(takeTimeM - takeTimeM mod(1000)) /1000:0}"
								pattern="#0" var="ss" />
							<c:set var="takeTime">
								<c:if test="${dd>0}">${dd}天</c:if>
								<c:if test="${hh>0}">${hh}时</c:if>
								<c:if test="${mm>0}">${mm}分</c:if>
                               			${ss}秒
                               		</c:set>

							<div class="time-content-box">
								
								<c:choose>
								<c:when test="${instance.flowState==4 && vs.first }">
								<div class="bottom-start"><div class="bottom-start-text">开始</div></div>
									<div class="time-content">
										<span class="time-arrow"><img src="/static/images/titme-arrow.png" /></span>
										<div class="row time-content-text">
											<div class="col-xs-12"><p>发起人：<span class="blue">${obj.executorName} </span></p></div>
											<div class="col-xs-6"><p>开始时间：<span class="blue">${fn:substring(obj.startTime,0,19)}</span></p></div>
											<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,19)}</span></p></div>
											<div class="col-xs-12"><p>耗时：<span class="blue">${takeTime}</span></p></div>
										</div>
									</div>
							</c:when>
							<c:when test="${instance.flowState!=4 && vs.last }">
								<div class="bottom-start"><div class="bottom-start-text">开始</div></div>
									<div class="time-content">
										<span class="time-arrow"><img src="/static/images/titme-arrow.png" /></span>
										<div class="row time-content-text">
											<div class="col-xs-12"><p>发起人：<span class="blue">${obj.executorName} </span></p></div>
											<div class="col-xs-6"><p>开始时间：<span class="blue">${fn:substring(obj.startTime,0,19)}</span></p></div>
											<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,19)}</span></p></div>
											<div class="col-xs-12"><p>耗时：<span class="blue">${takeTime}</span></p></div>
										</div>
									</div>
							</c:when>
								<c:otherwise>
								<div class="time-head">
									<img src="/downLoad/userImg/${userInfo.comId}/${obj.executor}?sid=${param.sid}" title="${obj.executorName}" onload="AutoResizeImage(0,0,this,'otherSpAttrIframe')"></img>
								</div>
								<div class="time-content">
									<span class="time-arrow"><img
										src="/static/images/titme-arrow.png" /></span>
									<div class="row time-content-text">
										<div class="col-xs-6"><p>节点：<span class="blue">${obj.stepName }</span></p></div>
										<div class="col-xs-6"><p>办理人：<span class="blue">${obj.executorName} </span></p></div>
										<div class="col-xs-6"><p>开始时间：<span class="blue">${fn:substring(obj.startTime,0,19)}</span></p></div>
										<c:choose>
											<c:when test="${!((empty obj.endTime) && instance.flowState!=4)}">
												<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,19)}</span></p></div>
												<div class="col-xs-12"><p>耗时：<span class="blue">${takeTime}</span></p></div>
												<div class="col-xs-12"><p>审批意见：<span class="blue">${obj.spMsg}</span></p></div>
											</c:when>
											<c:otherwise>
												<div class="col-xs-12"><p><span class="blue"> 正在办理...</span></p></div>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								</c:otherwise>
							</c:choose>
							</div>
						</c:forEach>
						<c:choose>
							<c:when test="${instance.flowState==4}">
								<div class="time-content-box">
									<div class="time-head">
										<div class="finish-text">已办结</div>
									</div>
									<div class="time-content">
										<span class="time-arrow"><img src="/static/images/titme-arrow.png" /></span>
										<div class="row time-content-text"> 
										<div class="col-xs-12"> <p><c:choose>
														<c:when test="${instance.spState==1}">
															<span style="color:green;">通过！</span>
														</c:when>
														<c:when test="${instance.spState==0}">
															<span class="red">驳回！</span>
														</c:when>
													</c:choose></p></div>
										</div>
									</div>
								</div>
							</c:when>
						</c:choose>
						
					</c:when>
				</c:choose>

			</div>
			<!-- page end-->
		</section>
	</section>
	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
