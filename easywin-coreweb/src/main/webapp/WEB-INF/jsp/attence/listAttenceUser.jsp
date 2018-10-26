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
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	var sid = "${param.sid}";
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height() - 45;
		$("#contentBody").css("height", height + "px");

		//操作删除和复选框
		$('tr').click(function() {
			var radio = $(this).find("input[type=radio]");
			$(radio).attr("checked", "checked")
		});

		$("#searchName").bind("keydown", function(event) {
			if (event.keyCode == "13") {
				if ($("#searchName").val()) {
					//启动加载页面效果
					layer.load(0, {
						shade : [ 0.6, '#fff' ]
					});
					$("#name").val($("#searchName").val());
					$("#searchForm").submit();
				} else {
					showNotification(1, "请输入检索内容！");
					$("#searchName").focus();
				}
			}
		});
	});
	//返回选择的编号
	function returnEnrollNumber() {
		var itemId = '';
		var radio = $("input[type=radio]:checked");
		if (radio.length == 0) {
			window.top.layer.alert("请选择编号信息", {
				icon : 7
			});
		} else {
			itemId = $(radio).attr("enrollNumber");
		}
		return itemId;
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
<body style="background-color: #fff">
	<div class="widget no-margin ">
		<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
			<form action="/attence/listAttenceUser" id="searchForm">
				<input type="hidden" id="redirectPage" name="redirectPage" />
				<input type="hidden" name="sid" value="${param.sid}" />
				<input type="hidden" name="pager.pageSize" value="8" />
				<input type="hidden" id="name" name="name" value="${attenceUser.name}" />
				<div class="btn-group pull-left">
					<div class="table-toolbar ps-margin">
						<div class="pull-left padding-right-10" style="font-size: 18px">
							<span class="widget-caption themeprimary ps-layerTitle">考勤人员列表</span>
						</div>

					</div>
				</div>
			</form>
			<div class="ps-margin ps-search searchCond">
				<span class="input-icon">
					<input id="searchName" name="searchName" value="${attenceUser.name}" class="form-control ps-input" type="text" placeholder="人员名称">
					<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
				</span>
			</div>
			<div class="widget-buttons">
				<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times themeprimary"></i>
				</a>
			</div>
		</div>
		<c:choose>
			<c:when test="${not empty listAttenceUser}">
				<div class="widget-body" id="contentBody" style="overflow-y:auto;position: relative;">
					<table class="table table-striped table-hover fixTable" style="background-color: #fff;">
						<thead>
							<tr role="row">
								<th  style="width: 20%;" class="text-center">选项</th>
								<th  style="width: 20%;" class="text-center">编号</th>
								<th  style="width: 30%;" class="text-center">名称</th>
								<th  style="width: 30%;" class="text-center">管理权限</th>
							</tr>
							</thead>
						<tbody>
							<c:forEach items="${listAttenceUser}" var="userVo" varStatus="vs">
								<tr class="optTr">
									<td style="padding-left:5px !important;" class="text-center padding-top-5 padding-bottom-5 no-margin">
										<label class="no-margin-bottom">
											<input class="colored-blue" name="enrollNumber" enrollNumber="${userVo.enrollNumber}" type="radio">
											<span class="text" style="color: inherit;"></span>
										</label>
									</td>
									<td class="text-center">${userVo.enrollNumber}</td>
									<td class="text-center">${userVo.name}</td>
									<td class="text-center">
										<c:if test="${userVo.privilege eq 0}">普通用户</c:if>
										<c:if test="${userVo.privilege eq 3}">管理人员</c:if>
									</td>
								</tr>
							</c:forEach>
						
						</tbody>
						</table>
					<tags:pageBar url="/attence/listAttenceUser"></tags:pageBar>
				</div>
						
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>没有相关考勤人员数据,请同步！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
										</div>
									</section>
								</div>
							</c:otherwise>
							</c:choose>
						
					
	</div>
	<script src="/static/assets/js/bootstrap.min.js"></script>
	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
