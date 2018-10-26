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
		resizeVoteH('otherTaskAttrIframe');
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
			style="padding:15px;margin-top: 0px;margin-bottom: 20px;display:inline-block;width:100%">
			<!-- page start-->
			<div class="time-border">
			 <c:choose >
			 <c:when test="${task.state==4 }">
				<div style="margin-top:30px;">
						<div class="bottom-start"><div class="bottom-start-text">开始</div></div>
						<div class="time-content">
							<span class="time-arrow"><img src="/static/images/titme-arrow.png" /></span>
							<div class="row time-content-text">
								<div class="col-xs-12"><p>发布人：<span class="blue">${task.ownerName} </span></p></div>
								<div class="col-xs-6"><p>发布时间：<span class="blue">${fn:substring(task.recordCreateTime,0,16)}</span></p></div>
							</div>
						</div>
						</div>
				</c:when>
				</c:choose>
				
				<c:choose >
					<c:when test="${not empty listFlowRecord}">
						<c:forEach items="${listFlowRecord}" var="obj" varStatus="vs">

							<c:set var="useTimeLong" value="${obj.useTime}"></c:set>
							<fmt:formatNumber
								value="${useTimeLong>=(1000*60*60*24)?(useTimeLong - useTimeLong mod(1000*60*60*24)) /1000/60/60/24:0}"
								pattern="#0" var="dd" />
							<c:set var="useTimeD" value="${useTimeLong - dd*1000*60*60*24}"></c:set>
							<fmt:formatNumber
								value="${useTimeD>=(1000*60*60)?(useTimeD - useTimeD mod(1000*60*60)) /1000/60/60:0}"
								pattern="#0" var="hh" />
							<c:set var="useTimeH" value="${useTimeD - hh*1000*60*60}"></c:set>
							<fmt:formatNumber
								value="${useTimeH>=(1000*60)?(useTimeH - useTimeH mod(1000*60)) /1000/60:0}"
								pattern="#0" var="mm" />
							<c:set var="useTimeM" value="${useTimeH - mm*1000*60}"></c:set>
							<fmt:formatNumber
								value="${useTimeM>=(1000)?(useTimeM - useTimeM mod(1000)) /1000:0}"
								pattern="#0" var="ss" />
							<c:set var="useTime">
								<c:if test="${dd>0}">${dd}天</c:if>
								<c:if test="${hh>0}">${hh}时</c:if>
								<c:if test="${mm>0}">${mm}分</c:if>
								<c:if test="${dd>=0 && hh>=0 && ss>0}">${ss}秒</c:if>
							</c:set>

							<c:set var="overTimeLong" value="${obj.overTime}"></c:set>
							<fmt:formatNumber
								value="${overTimeLong>=(1000*60*60*24)?(overTimeLong - overTimeLong mod(1000*60*60*24)) /1000/60/60/24:0}"
								pattern="#0" var="dd" />
							<c:set var="overTimeD" value="${overTimeLong - dd*1000*60*60*24}"></c:set>
							<fmt:formatNumber
								value="${overTimeD>=(1000*60*60)?(overTimeD - overTimeD mod(1000*60*60)) /1000/60/60:0}"
								pattern="#0" var="hh" />
							<c:set var="overTimeH" value="${overTimeD - hh*1000*60*60}"></c:set>
							<fmt:formatNumber
								value="${overTimeH>=(1000*60)?(overTimeH - overTimeH mod(1000*60)) /1000/60:0}"
								pattern="#0" var="mm" />
							<c:set var="overTimeM" value="${overTimeH - mm*1000*60}"></c:set>
							<fmt:formatNumber
								value="${overTimeM>=(1000)?(overTimeM - overTimeM mod(1000)) /1000:0}"
								pattern="#0" var="ss" />
							<c:set var="overTime">
								<c:if test="${dd>0}">${dd}天</c:if>
								<c:if test="${hh>0}">${hh}时</c:if>
								<c:if test="${dd<=0 && hh>0 && mm>0}">${mm}分</c:if>
							</c:set>

							<div class="time-content-box">
							<c:choose>
							<c:when test="${task.state==4 && vs.last}">
								<div class="time-head">
										<div class="finish-text">办结</div>
									</div>
									<div class="time-content">
										<span class="time-arrow"><img src="/static/images/titme-arrow.png" /></span>
										<div class="row time-content-text"> 
										<div class="col-xs-12"> <p>办结人：<span class="blue">${obj.userName } </span></p></div>
										<div class="col-xs-6"><p>开始时间：<span class="blue">${fn:substring(obj.acceptDate,0,16)}</span></p></div>
										<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,16)}</span></p></div>
										<c:choose>
											<c:when test="${overTimeLong >= 1000*60*60}">
												<div class="col-xs-6"><p>办理时限：<span class="blue"> ${obj.handTimeLimit}</span></p></div>
												<div class="col-xs-6"><p>超时：<span class="red">${overTime}</span></p></div>
											</c:when>
											<c:otherwise>
												<c:if test="${not empty useTime }">
												<div class="col-xs-12"><p>耗时：<span class="blue">${useTime}</span></p></div></c:if>
											</c:otherwise>
										</c:choose>
										</div>
									</div>
							</c:when>
							<c:otherwise>
								<div class="time-head">
									<img src="/downLoad/userImg/${obj.comId}/${obj.userId}?sid=${param.sid}" title="${obj.userName}" onload="AutoResizeImage(0,0,this,'otherTaskAttrIframe')"></img>
								</div>
								<div class="time-content">
									<span class="time-arrow"><img
										src="/static/images/titme-arrow.png" /></span>
									<div class="row time-content-text"> 
									<div class="col-xs-12"> <p>协办人：<span class="blue">${obj.userName } </span></p></div>
									<div class="col-xs-6"><p>开始时间：<span class="blue">${fn:substring(obj.acceptDate,0,16)}</span></p></div>
									<c:choose>
										<c:when test="${vs.first or empty obj.endTime}">
											<c:choose>
												<c:when test="${obj.state ==1}">
													<c:choose>
														<c:when test="${overTimeLong >= 1000*60*60}">
															<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,16)}</span></p></div>
															<div class="col-xs-6"><p>办理时限：<span class="blue"> ${obj.handTimeLimit}</span></p></div>
															<div class="col-xs-6"><p>超时：<span class="red">${overTime}</span></p></div>
														</c:when>
														<c:otherwise>
															<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,16)}</span></p>
															</div>
															<c:if test="${not empty useTime }">
															<div class="col-xs-12"><p>耗时：<span class="blue">${useTime}</span></p></div></c:if>
														</c:otherwise>
													</c:choose>
												</c:when>
												
												<c:otherwise>
													<c:if test="${not empty obj.handTimeLimit}">
														<div class="col-xs-6"><p>办理时限：<span class="blue"> ${obj.handTimeLimit}</span></p></div>
													</c:if>
													<div class="col-xs-12"><p>正在办理...</p></div>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${overTimeLong >= 1000*60*60}">
														<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,16)}</span></p></div>
														<div class="col-xs-6"><p>办理时限：<span class="blue"> ${obj.handTimeLimit}</span></p></div>
														<div class="col-xs-6"><p>超时：<span class="red">${overTime}</span></p></div>
												</c:when>
												<c:otherwise>
													<div class="col-xs-6"><p>结束时间：<span class="blue">${fn:substring(obj.endTime,0,16)}</span></p></div>
													<c:if test="${not empty useTime }">
													<div class="col-xs-12"><p>耗时：<span class="blue">${useTime}</span></p></div></c:if>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
									</div>
								</div>								
							</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:when>
				</c:choose>

				<c:choose>
					<c:when test="${task.state!=4 }">
						<div style="margin-top:30px;">
						<div class="bottom-start"><div class="bottom-start-text">开始</div></div>
						<div class="time-content">
							<span class="time-arrow"><img src="/static/images/titme-arrow.png" /></span>
							<div class="row time-content-text">
								<div class="col-xs-12"><p>发布人：<span class="blue">${task.ownerName} </span></p></div>
								<div class="col-xs-6"><p>发布时间：<span class="blue">${fn:substring(task.recordCreateTime,0,16)}</span></p></div>
							</div>
						</div>
						</div>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</div>
			<!-- page end-->
		</section>
	</section>
	<%--用与测量当前页面的高度 --%>
	<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
