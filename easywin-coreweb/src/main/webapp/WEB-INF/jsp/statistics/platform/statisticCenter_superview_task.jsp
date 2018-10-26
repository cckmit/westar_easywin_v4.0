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
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12" id="infoList">
				
					<div id="mainDataDiv" class="pull-left" style="width: 100%">
						<div class="widget" >
							<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
								<div class="searchCond">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin margin-right-10">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													<font style="font-weight:bold;">按紧急程度降序</font>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="orderBy" dataValue="1">按紧急程度降序</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="orderBy" dataValue="2">按紧急程度升序</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="orderBy" dataValue="3">按预警状态降序</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="orderBy" dataValue="4">按预警状态升序</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="orderBy" dataValue="5">按任务时限升序</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="orderBy" dataValue="6">按任务时限降序</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="紧急程度">
													紧急程度
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default dataDicClz" 
													dataDic="grade"
													relateElement="grade">
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="预警状态">
													预警状态
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearValue" relateElement="overDueLevel">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="overDueLevel" dataValue="1">已逾期</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="overDueLevel" dataValue="2">三天内到期</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="overDueLevel" dataValue="3">三天后到期</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="部门筛选">
													部门筛选
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearMoreValue" relateList="sysDep_select">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="depMoreSelect" relateList="sysDep_select">部门选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="办理人筛选">
														办理人筛选
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li>
															<a href="javascript:void(0)" class="clearMoreValue" relateList="executor_select">不限条件</a>
														</li>
														<li>
															<a href="javascript:void(0)" class="userMoreSelect" relateList="executor_select">人员选择</a>
														</li>
													</ul>
												</div>
											</div>
									</div>
										
									<div class="ps-margin ps-search">
										<span class="input-icon">
											<input id="taskName" name="taskName"  
												class="form-control ps-input moreSearch" type="text" placeholder="任务名称关键字">
											<a href="javascript:void(0)" class="ps-searchBtn">
												<i class="glyphicon glyphicon-search circular danger"></i>
											</a>
										</span>
									</div>
									
									<div class="widget-buttons ps-widget-buttons">
										<label>
											<div class="pull-left"><img src="/static/images/light_red.gif"></div>
											<span class="pull-left margin-left-5" style="line-height: 20px">已逾期</span> 
										</label>
										<label>
											<div class="pull-left"><img src="/static/images/light_yellow.gif"></div>
											<span class="pull-left margin-left-5" style="line-height: 20px">三天内到期</span> 
										</label>
										<label>
											<div class="pull-left"><img src="/static/images/light_green.gif"></div>
											<span class="pull-left margin-left-5" style="line-height: 20px">三天后到期</span> 
										</label>
									</div>
								</div>
								<div class="ps-clear" id="formTempData">
									<input type="hidden" name="grade" id="grade">
									<input type="hidden" name="orderBy" id="orderBy">
									<input type="hidden" name="overDueLevel" id="overDueLevel">
									<select list="listExecuteDep" listkey="id" listvalue="depName" id="sysDep_select" 
										multiple="multiple" moreselect="true" style="display: none">
										<c:choose>
											<c:when test="${not empty task.listExecuteDep }">
												<c:forEach items="${task.listExecuteDep}" var="dep" varStatus="vs">
													<option value="${dep.id}">${dep.depName}</option>
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
									<select list="listTaskExecutor" listkey="executor" listvalue="executorName" id="executor_select" 
										multiple="multiple" moreselect="true" style="display: none">
										<c:choose>
										<c:when test="${not empty task.listTaskExecutor }">
											<c:forEach items="${task.listTaskExecutor}" var="taskExecutor" varStatus="vs">
												<option value="${taskExecutor.executor}">${taskExecutor.executorName}</option>
											</c:forEach>
										</c:when>
									</c:choose>
									</select>
								</div>
								<!-- 筛选条件显示 -->
								<div class="padding-top-5 padding-bottom-5 text-left moreUserListShow" 
									style="display:${not empty task.listTaskExecutor?'block':'none'}" id="executor_selectDiv">
									<strong>办理人员:</strong>
									<c:choose>
										<c:when test="${not empty task.listTaskExecutor }">
											<c:forEach items="${task.listTaskExecutor}" var="taskExecutor" varStatus="vs">
												<span class="label label-default margin-right-5 margin-bottom-5" title="双击移除" 
												style="cursor: pointer;" relateList="executor_select" userId="${taskExecutor.executor}">${taskExecutor.executorName}</span>
											</c:forEach>
										</c:when>
									</c:choose>
								</div>
								<!-- 筛选条件显示 -->
								<div class="padding-top-5 padding-bottom-5 text-left moreDepListShow" 
									style="display:${not empty task.listExecuteDep?'block':'none'}" id="sysDep_selectDiv">
									<strong>部门选择:</strong>
									<c:choose>
										<c:when test="${not empty task.listExecuteDep }">
											<c:forEach items="${task.listExecuteDep}" var="dep" varStatus="vs">
												<span class="label label-default margin-right-5 margin-bottom-5" title="双击移除" 
												style="cursor: pointer;" relateList="sysDep_select" depId="${dep.id}">${dep.depName}</span>
											</c:forEach>
										</c:when>
									</c:choose>
								</div>
							</div>
							
							<div class="widget-body" style="min-height: 450px" id="infoList">
								<table class="table table-bordered ">
									<thead>
										<tr role="row">
											<th style="text-align: center;font-weight:bold;width:50px" >
												序号
											</th>
											<th style="text-align: center;font-weight:bold;" >
												任务名称
											</th>
											<th style="text-align: center;font-weight:bold;width:100px;padding: 0 0" >
												紧急度
											</th>
											<th style="text-align: center;font-weight:bold;width:100px;padding: 0 0;">
												任务时限
											</th>
											<th style="text-align: center;font-weight:bold;width: 150px"  >
												任务进度
											</th>
											<th style="text-align: center;font-weight:bold;width: 100px;padding: 0 0" >
												办理人
											</th>
											<th style="text-align: center;font-weight:bold;width: 100px;padding: 0 0" >
												预警状态
											</th>
											<th style="text-align: center;font-weight:bold;width: 100px;padding: 0 0" >
												操作
											</th>
										</tr>
									<tbody id="allTodoBody">
									</tbody>
									
								</table>
								<div class="panel-body ps-page bg-white" style="font-size: 12px">
									 <p class="pull-left ps-pageText">共<b class="badge" id="totalNum">11</b>条记录</p>
									 <ul class="pagination pull-right" id="pageDiv">
									 </ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
	<style type="text/css">
		#infoList table{
			table-layout: fixed;
		}
		#infoList td,#infoList th{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
		
		.progress {
		  overflow: hidden;
		  height:15px;
		  float: left;
		  margin-bottom: 0px;
		  margin-top: 0px;
		  margin-left: 1px;
		  background-color: #f7f7f7;
		  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
		  -webkit-border-radius: 2px;
		  -moz-border-radius: 2px;
		  width:100px;
		}
		
		.progress .bar {
		  width: 0%;
		  height: 100%;
		  color: #ffffff;
		  float: left;
		  font-size: 12px;
		  text-align: center;
		  align-items: center;
		  display: flex;
		  justify-content: center;
		  background-color: #2dc3e8;
		  transition: width 0.6s ease;
		}
	
	</style>
</body>
</html>
