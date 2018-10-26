<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
	<div class="page-content">
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form method="get" action="/statistics/platform/statisticWeekReportPage" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid }"/>
									<input type="hidden" id="activityMenu" name="activityMenu" value="${param.activityMenu}"/>
									<input type="hidden" id="depId" name="depId" value="${weekReport.depId}"/>
									<input type="hidden" id="depName" name="depName" value="${weekReport.depName}"/>
									<input type="hidden" id="submitState" name="submitState" value="${weekReport.submitState}"/>
									<input type="hidden" id="weekNum" name="weekNum" value="${weekReport.weekNum}"/>
									<input type="hidden" id="weekYear" name="weekYear" value="${weekReport.weekYear}"/>

									<div class="searchCond" style="display: block">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin margin-right-10">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														<c:choose>
															<c:when test="${not empty weekReport.weekYear}">
																<font style="font-weight:bold;"> ${weekReport.weekYear}年</font>
															</c:when>
															<c:otherwise>年份筛选</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default" id="weekYearUl">
														<!--数据异步获得  #weekYearUl-->
													</ul>
												</div>
											</div>
											<div class="table-toolbar ps-margin margin-right-10">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														<c:choose>
															<c:when test="${not empty weekReport.weekNum && weekReport.weekNum ne 0}">
																<font style="font-weight:bold;"> 第${weekReport.weekNum}周 </font>
															</c:when>
															<c:otherwise>周数筛选</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default" id="weekNumUl">
														<li><a href="javascript:void(0)" onclick="weekNumFilte('')">不限周数</a></li>
														<li><a href="javascript:void(0)" onclick="weekNumThisFilte(${nowWeekNum})">本周</a></li>
													</ul>
												</div>
											</div>
											<div class="table-toolbar ps-margin margin-right-10">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														<c:choose>
															<c:when test="${not empty weekReport.submitState}">
																<font style="font-weight:bold;"> <c:choose>
																		<c:when test="${weekReport.submitState eq 0}">未提交</c:when>
																		<c:when test="${weekReport.submitState eq 1}">已提交</c:when>
																		<c:when test="${weekReport.submitState eq 2}">延迟提交</c:when>
																	</c:choose>
																</font>
															</c:when>
															<c:otherwise>提交状态</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li><a href="javascript:void(0)" class="clearThisElement" relateElement="submitState">不限条件</a></li>
														<li><a href="javascript:void(0)" class="setElementValue" relateElement="submitState" dataValue="0">未提交</a></li>
														<li><a href="javascript:void(0)" class="setElementValue" relateElement="submitState" dataValue="1">已提交</a></li>
														<li><a href="javascript:void(0)" class="setElementValue" relateElement="submitState" dataValue="2">延迟提交</a></li>
													</ul>
												</div>
											</div>
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														部门筛选<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li><a href="javascript:void(0)" class="clearMoreElement" relateList="dep_select">不限条件</a></li>
														<li><a href="javascript:void(0)" class="depMoreElementSelect" relateList="dep_select">部门选择</a></li>
													</ul>
												</div>
											</div>
											<div style="float: left;width: 250px;display: none">
												<select list="listDep" listkey="id" listvalue="depName" id="dep_select" name="listDep.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													<c:forEach items="${weekReport.listDep }" var="obj" varStatus="vs">
														<option selected="selected" value="${obj.id }">${obj.depName }</option>
													</c:forEach>
												</select>
											</div>
											<div class="table-toolbar ps-margin" style="display:${param.searchTab eq '12'?'none':'block'}">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														汇报人员筛选<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li><a href="javascript:void(0)" class="clearMoreElement" relateList="owner_select">不限条件</a></li>
														<li><a href="javascript:void(0)" class="userMoreElementSelect" relateList="owner_select">人员选择</a></li>
													</ul>
												</div>
											</div>
											<div style="float: left;width: 250px;display: none">
												<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
													<c:forEach items="${weekReport.listOwner }" var="obj" varStatus="vs">
														<option selected="selected" value="${obj.id }">${obj.userName }</option>
													</c:forEach>
												</select>
											</div>
											<div class="table-toolbar ps-margin">
												<div class="btn-group cond" id="moreCondition_Div">
													<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
														<c:choose>
															<c:when test="${not empty weekReport.startDate || not empty weekReport.endDate}">
																<font style="font-weight:bold;">筛选中</font>
															</c:when>
															<c:otherwise>更多</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
													<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
														<div class="ps-margin ps-search padding-left-10">
															<span class="btn-xs">起止时间：</span>
															<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${weekReport.startDate}" id="startDate" name="startDate" placeholder="开始时间"
																onFocus="WdatePicker({dateFmt:'yyyy年MM月dd日',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"  />
															<span>~</span>
															<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${weekReport.endDate}" id="endDate" name="endDate" placeholder="结束时间"
																onFocus="WdatePicker({dateFmt:'yyyy年MM月dd日',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
														</div>
														<div class="ps-clear padding-top-10" style="text-align: center;">
															<button type="button" onclick="weekNumDate()" class="btn btn-primary btn-xs">查询</button>
															<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
														</div>
													</div>
												</div>
											</div>

										</div>
									</div>
									<div class="batchOpt" style="display: none"></div>
								</form>
							</div>
							<div class="widget-buttons ps-widget-buttons"></div>
							<div class="padding-top-10 text-left " style="display:${empty weekReport.listOwner ? 'none':'block'}">
								<strong>汇报人筛选:</strong>
								<c:forEach items="${weekReport.listOwner }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty weekReport.listDep ? 'none':'block'}">
								<strong>汇报部门筛选:</strong>
								<c:forEach items="${weekReport.listDep }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('dep','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.depName }</span>
								</c:forEach>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty weekReports}">
								<div class="widget-body">
									<form action="/clock/delClock" id="delForm">
										<input type="hidden" id="redirectPage" name="redirectPage" />
										<input type="hidden" name="sid" value="${param.sid}" />
										<table class="table table-bordered">
											<thead>
												<tr role="row">
													<th class="text-center">序号</th>
													<th class="text-center">汇报年份</th>
													<th class="text-center">汇报周数</th>
													<th class="text-center">汇报范围</th>
													<th class="text-center">部门</th>
													<th class="text-center">汇报人</th>
													<th class="text-center">汇报时间</th>
													<th class="text-center">汇报状态</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${weekReports}" var="weekReportVo" varStatus="status">
													<tr style="color:${weekReportVo.state==0?'':'gray'}">
														<td class="text-center">${status.count}</td>
														<td class="text-center">${weekReportVo.year}年</td>
														<td class="text-center">
															<a href="javascript:void(0)" title="点击查看" data-busId="${weekReportVo.id}">第${weekReportVo.weekNum }周</a>
														</td>
														<td class="text-center">${fn:substring(weekReportVo.weekS,5,11)}~${fn:substring(weekReportVo.weekE,5,11)}</td>
														<td class="text-center">${weekReportVo.depName }</td>
														<td class="text-center">${weekReportVo.userName}</td>
														<td class="text-center"><c:choose>
																<c:when test="${not empty weekReportVo.createDate}">
																		${weekReportVo.createDate}
																</c:when>
																<c:otherwise>
																	未发布
																</c:otherwise>
															</c:choose></td>
														<td class="text-center"><c:choose>
																<c:when test="${weekReportVo.submitState ==0 }">
																	<span style="color: red;">未提交</span>
																</c:when>
																<c:when test="${weekReportVo.submitState ==1 }">
																	<span style="color: green;cursor: pointer;" title="点击查看" data-busId="${weekReportVo.id}">已提交</span>
																</c:when>
																<c:when test="${weekReportVo.submitState ==2 }">
																	<span style="color:#CD00CD;cursor: pointer;" title="点击查看" data-busId="${weekReportVo.id}">延迟提交</span>
																</c:when>
															</c:choose></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/statistics/platform/statisticWeekReportPage"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>还没有汇报周报数据！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
										</div>
									</section>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		var params={
			"sid":"${param.sid}"
		}
		if($("#weekNum").val()){
			params["weekNum"]= $("#weekNum").val()
		}
		if($("#weekYear").val()){
			params["weekYear"]= $("#weekYear").val()
		}
		//设置年份选择和周数选择
		getSelfJSON('/weekReport/findOrgInfo',params,function(data){
			//团队注册时间年份
			var registYear = data.registYear;
			//当前时间年份
			var nowYear = data.nowYear;
			//添加年份选择
			for(var index = nowYear ; index >= registYear ; index--){
				var _li = $('<li></li>');
				var _a = $('<a href="javascript:void(0)" class="setElementValue" relateElement="weekYear"></a>')
				$(_a).attr("dataValue",index);
				$(_a).html(index+'年');
				$(_li).append($(_a));
				$("#weekYearUl").append($(_li));
				
				$(_a).on("click",function(){
					$("#weekNum").val('');
				})
			}
			//添加年份不限
			var _liAll = $('<li></li>');
			var _aALL = $('<a href="javascript:void(0)" class="clearThisElement" relateElement="weekYear"></a>')
			$(_aALL).html('不限条件');
			$(_liAll).append($(_aALL));
			$("#weekYearUl").prepend($(_liAll));
			
			$(_aALL).on("click",function(){
				$("#weekNum").val('');
			})
			
			if(params.weekYear){
				var listWeeks = data.listWeeks;
				if(listWeeks && listWeeks[0]){
					$.each(listWeeks,function(index,obj){
						var _li = $('<li></li>');
						var _a = $('<a href="javascript:void(0)"></a>')
						$(_a).attr("onclick","weekNumFilte("+obj+")");
						$(_a).html('第'+obj+'周');
						$(_li).append($(_a));
						$("#weekNumUl").append($(_li));
					})
				}
			}
			
			
		})
		
		
		
		$("body").on("click","[data-busId]",function(){
			var busId = $(this).attr("data-busId");
			if(busId && busId>0){
				viewModInfo(busId,'006');
			}else{
				showNotification(2, "周报未发布！")
			}
		})
	})
	//本周
	function weekNumThisFilte(weekNum){
		$("#weekNum").val(weekNum)
		$("#weekYear").val(new Date().getFullYear())
		$("#searchForm").submit();
	}
	//周数筛选
	function weekNumFilte(weekNum) {
		if (weekNum && weekNum != 0) {
			$("#startDate").val('')
			$("#endDate").val('')
		}
		$("#weekNum").val(weekNum)
		$("#searchForm").submit();
	}
	
	function weekNumDate(){
		if($("#startDate").val() ||$("#endDate").val()){
			$("#weekNum").val('')
			$("#weekYear").val('')
		}
		$("#searchForm").submit();
	} 
</script>
</html>

