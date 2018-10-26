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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/webJs/sysLog.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function(){
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	})
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row">
			<div class="col-md-12 col-xs-12 ">
				<div class="widget" style="margin-top: 0px;">
					<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						<form action="/web/count/listPagedOrgSysLog" id="sysLogForm">
							<input type="hidden" name="pager.pageSize" value="10" /> 
							<input type="hidden" name="orgNum" value="${orgNum}">

							<div class="btn-group pull-left searchCond">
								<div class="table-toolbar ps-margin">
									<div class="btn-group cond" id="moreCondition_Div">
										<a class="btn btn-default dropdown-toggle btn-xs"
											onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
												<c:when
													test="${not empty systemLog.startDate || not empty systemLog.endDate}">
													<font style="font-weight:bold;">筛选中</font>
												</c:when>
												<c:otherwise>更多</c:otherwise>
											</c:choose> <i class="fa fa-angle-down"></i>
										</a>
										<div class="dropdown-menu dropdown-default padding-bottom-10"
											style="min-width: 330px;">
											<div class="ps-margin ps-search padding-left-10">
												<span class="btn-xs">起止时间：</span> <input
													class="form-control dpd2 no-padding condDate" type="text"
													readonly="readonly" value="${systemLog.startDate}"
													id="startDate" name="startDate" placeholder="开始时间"
													onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
												<span>~</span> <input
													class="form-control dpd2 no-padding condDate" type="text"
													readonly="readonly" value="${systemLog.endDate}"
													id="endDate" name="endDate" placeholder="结束时间"
													onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
											</div>
											<div class="ps-clear padding-top-10"
												style="text-align: center;">
												<button type="submit" class="btn btn-primary btn-xs">查询</button>
												<button type="button"
													class="btn btn-default btn-xs margin-left-10"
													onclick="resetMoreCon('moreCondition_Div')">重置</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="ps-margin ps-search">
								<span class="input-icon"> <input id="content"
									name="content" value="${systemLog.content}"
									class="form-control ps-input" type="text" placeholder="请输入关键字">
									<a href="javascript:void(0)" class="ps-searchBtn"><i
										class="glyphicon glyphicon-search circular danger"></i></a>
								</span>
							</div>
						</form>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
					</div>
					<div class="widget-body" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
						<table class="table table-striped table-hover">
							<thead>
								<tr role="row">
									<th style="width:55px" valign="middle">序号</th>
									<th style="width:150px" valign="middle" class="hidden-phone">部门</th>
									<th valign="middle">日志内容</th>
									<th style="width:180px" valign="middle">操作人员</th>
									<th style="width:140px" valign="middle">日志时间</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty list}">
										<c:forEach items="${list}" var="sysLog" varStatus="vs">
											<tr class="optTr">
												<td valign="middle">${vs.count}</td>
												<td valign="middle">${sysLog.depName }</td>
												<td valign="middle"><c:choose>
														<c:when test="${fn:length(sysLog.content)>40}">
																		${fn:substring(sysLog.content,0,40)}...
																	</c:when>
														<c:otherwise>
																		${sysLog.content }
																	</c:otherwise>
													</c:choose></td>
												<td valign="middle">
													<div class="ticket-user pull-left other-user-box">
														<img src="/downLoad/userImg/${sysLog.comId}/${sysLog.userId}?sid=${param.sid}"
															class="user-avatar" />
														<span class="user-name">${sysLog.userName}</span>
													</div>
												</td>
												<td valign="middle">${fn:substring(sysLog.recordDateTime,0,16)}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<tags:pageBar url="/web/count/listPagedOrgSysLog"></tags:pageBar>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /Page Container -->
	<script type="text/javascript">
		//页面加载初始化
		$(function() {
			//任务名筛选
			$("#content").blur(function() {
				//启动加载页面效果
				layer.load(0, {
					shade : [ 0.6, '#fff' ]
				});
				$("#sysLogForm").submit();
			});
			//文本框绑定回车提交事件
			$("#content").bind("keydown", function(event) {
				if (event.keyCode == "13") {
					if (!strIsNull($("#content").val())) {
						//启动加载页面效果
						layer.load(0, {
							shade : [ 0.6, '#fff' ]
						});
						$("#sysLogForm").submit();
					} else {
						showNotification(2, "请输入检索内容！");
						$("#searchTaskName").focus();
					}
				}
			});
		});
		//窗体点击事件检测
		document.onclick = function(e) {
			var evt = e ? e : window.event;
			var ele = $(evt.srcElement || evt.target);
			if ($(ele).parents("#moreCondition_Div").length == 1) {
				if ($(ele).prop("tagName").toLowerCase() == 'a'
						|| $(ele).parent("a").length == 1) {
					return false;
				} else {
					if (!$("#moreCondition_Div").hasClass("open")) {
						$(".searchCond").find(".open").removeClass("open");
						$("#moreCondition_Div").addClass("open");
					}
				}
			} else {
				$("#moreCondition_Div").removeClass("open");
			}
		}
		/**
		 * 展示查询条件中更多
		 */
		function displayMoreCond(divId) {
			if ($("#" + divId).hasClass("open")) {
				$("#" + divId).removeClass("open");
			} else {
				$("#" + divId).addClass("open")
			}
		}
		/**
		 * 清空更多查询条件
		 */
		function resetMoreCon(divId) {
			$("#" + divId).find("input").val('');

		}
	</script>
</body>
</html>
