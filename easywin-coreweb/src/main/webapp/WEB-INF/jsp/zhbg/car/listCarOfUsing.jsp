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
<script type="text/javascript">
$(function(){
	
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
})
function addCar(sid){
	var url = '/car/addCarPage?sid='+sid;
	openWinByRight(url);
}
</script>
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
							<form action="/task/listTaskOfAllPage" id="searchForm" class="subform">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" name="pager.pageSize" value="10"> 
								<input type="hidden" name="activityMenu" value="${param.activityMenu}">
								<div class="btn-group pull-left searchCond">
								</div>
								
								
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
									</div>
								</div>
								
							</form>
							<div class="ps-margin ps-search searchCond">
								<span class="input-icon"> 
								<input id="searchTaskName" name="searchTaskName" value="" class="form-control ps-input" type="text" placeholder="请输入关键字">
									<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
								</span>
							</div>
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addCar('${param.sid}');"> <i class="fa fa-plus"></i> 添加车辆
									</button>
								</div>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty list}">
						<div class="widget-body">
							<form action="/task/delTask?sid=${param.sid}" method="post"
								id="delForm">
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover" id="editabledatatable">
									<thead>
										<tr role="row">
											<th><label> <input type="checkbox"
													class="colored-blue" id="checkAllBox"
													onclick="checkAll(this,'ids')"> <span class="text"
													style="color: inherit;"></span>
											</label></th>
											<th>任务名称</th>
											<th>紧急度</th>
											<th>进度</th>
											<th>办理时限</th>
											<th>执行人</th>
											<th>责任人</th>
											<th>发布日期</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${list}" var="taskVo" varStatus="vs">
											<tr class="optTr">
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/task/listTaskOfAllPage"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>您还没有相关车辆使用数据！</h2>
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
</body>
</html>
