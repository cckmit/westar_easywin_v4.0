<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
						<form action="/systemLog/listPagedSelfSysLog" id="searchForm">
							<input type="hidden" id="activityMenu" name="activityMenu" value="${param.activityMenu}">
						 	<input type="hidden" name="sid" value="${param.sid}"/>
							<div class="btn-group pull-left">
								<div class="table-toolbar ps-margin">
									<span class="pull-left">起</span>
									<input class="form-control ps-input pull-left margin-left-10" style="width:160px;margin-top:-3px;" type="text" readonly="readonly" 
									value="${systemLog.startDate}" id="startDate" name="startDate" placeholder="开始时间"  
									onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared: 
									function(){submitForm()},dateFmt:'yyyy-MM-dd',maxDate: 
									'#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
									<span class="pull-left margin-left-10">止</span>
									<input class="form-control ps-input pull-left margin-left-10" style="width:160px;margin-top:-3px;" class="colorpicker-default form-control" type="text" readonly="readonly" 
									value="${systemLog.endDate}" id="endDate"  name="endDate" placeholder="结束时间" 
									onFocus="WdatePicker({onpicked:function(){submitForm()},oncleared: 
									function(){submitForm()},dateFmt:'yyyy-MM-dd',minDate: 
									'#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
								</div>
							</div>
							<div class="ps-margin ps-search">
								<span class="input-icon"> <input id="content"
										name="content" value="${systemLog.content}"
										class="form-control ps-input" type="text"
										placeholder="请输入关键字"> <a href="javascript:void(0)"
									class="ps-searchBtn"><i
										class="glyphicon glyphicon-search circular danger"></i>
								</a> </span>
							</div>
						</form>
					</div>
					
					<c:choose>
					<c:when test="${not empty list}">
					<div class="widget-body">
						<table class="table table-striped table-hover"
							id="editabledatatable">
							<thead>
								<tr>
									<th width="8%">序号</th>
									<th width="10%">操作IP</th>
									<th>日志内容</th>
									<th >记录时间</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list}" var="obj" varStatus="vs">
									<tr>
										<td>${vs.count}</td>
										<td >
											${empty obj.optIP?'--':obj.optIP}
										</td>
										<td>
											<c:choose>
												<c:when test="${fn:length(obj.content)>50}">
													${fn:substring(obj.content,0,50)}...
												</c:when>
												<c:otherwise>
													${obj.content }
												</c:otherwise>
											</c:choose>
										</td>
										<td >
											${obj.recordDateTime}
										</td>
									</tr>
								</c:forEach>
									
							</tbody>
						</table>
						<tags:pageBar url="/systemLog/listPagedSelfSysLog"></tags:pageBar>
					</div>
					</c:when>
					<c:otherwise>
					<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
							<section class="error-container text-center">
								<h1>
									<i class="fa fa-exclamation-triangle"></i>
								</h1>
								<div class="error-divider">
									<h2>您还没相关操作日志！</h2>
									<p class="description">协同提高效率，分享拉近距离。</p>
								</div>
							</section>
						</div>
					</c:otherwise>
					</c:choose>
				</div>
			</div>
			<!-- 
				<div class="col-md-3 col-xs-12">
					<div class="widget">
						<div class="widget-header bordered-bottom bordered-themeprimary">
							<i class="widget-icon fa fa-tasks themeprimary"></i>
							<span class="widget-caption themeprimary">活跃度统计</span>
						</div>
						<div class="widget-body">
							<img src="/static/assets/img/chart.png"
								style="width: 100%; height: 100%;" />
						</div>
					</div>
				</div>
			 -->


		</div>
		<!-- /Page Body -->
	</div>
	<!-- /Page Content -->

</div>
<!-- /Page Container -->
</body>
</html>