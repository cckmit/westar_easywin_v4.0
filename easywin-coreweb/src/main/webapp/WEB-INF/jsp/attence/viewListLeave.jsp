<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	var sid = '${param.sid}';
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-45;
		$("#contentBody").css("height",height+"px");
		
		
		//操作删除和复选框
		$('tr').click(function() {
			var  instanceId = $(this).attr("instanceId");
			var url = "/workFlow/viewSpFlow?sid=${param.sid}&instanceId="+instanceId;
			parent.openWinWithPams(url, "800px", ($(window.parent).height() - 90) + "px");
		});

	});
</script>
<style type="text/css">
.outer,.inner {
	height: 40px;
}

.bg-line {
	height: 22px;
	border-bottom: 2px solid #f0f0f0;
}

.outer {
	position: absolute;
	width: 100%;
	top: 0;
	left: 0;
	z-index: 10;
}

.inner {
	width: 95px;
	line-height: 40px;
	background: #fff;
	font-weight: bold;
	text-align: center;
	margin: 0 auto;
	z-index: 20;
	font-size: 18px;
}
.table-hover tr:hover td{color:#0186e1;}
</style>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">${leave.creatorName }:${leave.startTime }至${leave.endTime }请假记录</span>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times themeprimary"></i>
							</a>
						</div>
					</div>
					<!--Widget Header-->
					<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->

					<c:choose>
						<c:when test="${not empty listLeave}">
							<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
								<table class="table table-striped table-hover" id="editabledatatable">
									<thead>
										<tr role="row">
											<th class="text-center">序号</th>
											<th class="text-center">请假类型</th>
											<th class="text-center">开始时间</th>
											<th class="text-center">结束时间</th>
											<th class="text-center">请假时长（小时）</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listLeave}" var="leaveVo" varStatus="vs">
											<tr class="optTr" instanceId = "${leaveVo.instanceId}"  title="点击查看详情">
												<td class="text-center">
													<label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
												</td>
												<td class="text-center">${leaveVo.busType}</td>
												<td class="text-center">${leaveVo.startTime}</td>
												<td class="text-center">${leaveVo.endTime}</td>
												<td class="text-center">${leaveVo.leaveTime}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<tags:pageBar url="/attence/viewListLeave"></tags:pageBar>
							</div>
						</c:when>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
