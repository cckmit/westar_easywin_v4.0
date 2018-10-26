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
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form action="/jfMod/task/listPagedMineTaskToJf" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="scoreState" value="${jfScore.scoreState}">
									<input type="hidden" id="searchYear" name="searchYear" value="${jfScore.searchYear}">
									
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin margin-right-10">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
													<c:choose>
														<c:when test="${not empty jfScore.searchYear}">
															<font style="font-weight:bold;"> ${jfScore.searchYear}年</font>
														</c:when>
														<c:otherwise>年份筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default" id="searchYearUl">
													<!--数据异步获得  #searchYearUl-->
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													<c:choose>
														<c:when test="${not empty jfScore.scoreState}">
															<font style="font-weight:bold;">
																<c:if test="${jfScore.scoreState==0}">未评分</c:if>
																<c:if test="${jfScore.scoreState==1}">不评分</c:if>
																<c:if test="${jfScore.scoreState==2}">已评分</c:if>
															</font>
														</c:when>
														<c:otherwise>评分状态</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)" class="clearThisElement" relateElement="scoreState">不限条件</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="scoreState" dataValue="0">未评分</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="scoreState" dataValue="1">不评分</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="scoreState" dataValue="2">已评分</a>
													</li>
													
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty jfScore.startDate || not empty jfScore.endDate}">
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
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${jfScore.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0})||\'${jfScore.searchYear}-12-31\' }',minDate:'${jfScore.searchYear}-01-01'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${jfScore.endDate}" id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0})||\'${jfScore.searchYear}-01-01\'}',maxDate: '${jfScore.searchYear}-12-31'})" />
													</div>
													<div class="ps-clear padding-top-10" style="text-align: center;">
														<button type="submit" class="btn btn-primary btn-xs">查询</button>
														<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="ps-margin ps-search">
										<span class="input-icon">
											<input id="modName" name="modName" value="${jfScore.modName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
											<a href="javascript:void" class="ps-searchBtn">
												<i class="glyphicon glyphicon-search circular danger"></i>
											</a>
										</span>
									</div>
								</form>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty list}">
								<div class="widget-body">
									<table class="table table-striped table-hover" id="editabledatatable">
										<thead>
											<tr role="row">
												<th style="text-align:center;width:5%">序号</th>
												<th>办结时间</th>
												<th>任务名称</th>
												<th>状态</th>
												<th>评分时间</th>
												<th>得分</th>
												<th>评分员</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${list}" var="jfModVo" varStatus="vs">
												<tr>
													<td style="text-align:center;">${vs.count}</td>
													<td style="width: 120px">${fn:substring(jfModVo.modReaseDate,0,10)}</td>
													<td><a href="javascript:void(0);" busId="${jfModVo.busId}" busType="${jfModVo.busType}" class="addJfScore">${jfModVo.modName}</a></td>
													<c:set var="scoreStateColor">
														<c:choose>
															<c:when test="${jfModVo.scoreState eq 0}">red</c:when>
															<c:when test="${jfModVo.scoreState eq 1}">blue</c:when>
															<c:when test="${jfModVo.scoreState eq 2}">green</c:when>
														</c:choose>
													</c:set>
													<td style="color: ${scoreStateColor}">
														<c:choose>
															<c:when test="${jfModVo.scoreState eq 0}">未评分</c:when>
															<c:when test="${jfModVo.scoreState eq 1}">不评分</c:when>
															<c:when test="${jfModVo.scoreState eq 2}">已评分</c:when>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${not empty jfModVo.recordCreateTime}">
																${fn:substring(jfModVo.recordCreateTime,0,10) }
															</c:when>
															<c:otherwise>
																--
															</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${not empty jfModVo.score}">
																${jfModVo.score }分
															</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td class="no-padding">
														<c:choose>
															<c:when test="${not empty jfModVo.score}">
																<div class="ticket-user pull-left other-user-box">
																	<img class="user-avatar userImg" title="${jfModVo.pfUserName}" 
																		src="/downLoad/userImg/${userInfo.comId}/${jfModVo.pfUserId}"/>
																	<span class="user-name">${jfModVo.pfUserName}</span>
																</div>
															</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td>
														<a href="javascript:void(0)" busId="${jfModVo.busId}" busType="${jfModVo.busType}" class="addJfScore">查看</a>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<tags:pageBar url="/jfMod/task/listPagedMineTaskToJf"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>没有查询到相关数据！</h2>
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
<script type="text/javascript">
	$(function(){
		$("body").on("click",".addJfScore",function(){
			var busId = $(this).attr("busId");
			var busType = $(this).attr("busType");
			//设置URL
			var url = '/jfMod/task/viewTaskJfPage?sid='+sid;
			url = url+"&busId="+busId;
			url = url+"&busType="+busType;
			openWinByRight(url);
		})
		
		//任务名筛选
		$("#modName").blur(function(){
			//启动加载页面效果
			layer.load(0, {shade: [0.6,'#fff']});
			$("#searchForm").submit();
		});
		//文本框绑定回车提交事件
		$("#modName").bind("keydown",function(event){
	        if(event.keyCode == "13")    
	        {
	        	if($("#modName").val()){
	        		//启动加载页面效果
	        		layer.load(0, {shade: [0.6,'#fff']});
					$("#searchForm").submit();
	        	}else{
	        		showNotification(1,"请输入检索内容！");
	    			$("#modName").focus();
	        	}
	        }
	    });
		//设置年份选择和周数选择
		getSelfJSON('/weekReport/findOrgInfo',{"sid":sid},function(data){
			//团队注册时间年份
			var registYear = data.registYear;
			//当前时间年份
			var nowYear = data.nowYear;
			//添加年份选择
			for(var index = nowYear ; index >= registYear ; index--){
				var _li = $('<li></li>');
				var _a = $('<a href="javascript:void(0)" class="setElementValue" relateElement="searchYear"></a>')
				$(_a).attr("dataValue",index);
				$(_a).html(index+'年');
				$(_li).append($(_a));
				$("#searchYearUl").append($(_li));
				
				$(_a).on("click",function(){
					$("#searchYear").val($(this).attr("dataValue"));
					$("#searchForm").submit();
				})
			}
		})
	})
</script>
</html>

