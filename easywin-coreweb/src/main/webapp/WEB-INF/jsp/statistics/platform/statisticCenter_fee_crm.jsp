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
								<div>
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin margin-right-10">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
													<font style="font-weight:bold;">${nowYear}年</font>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default" id="weekYearUl">
													<!--数据异步获得  #weekYearUl-->
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
									</div>
								</div>
								<div class="ps-clear" id="formTempData">
									<input type="hidden" name="year" id="year" value="${nowYear}">
									<select list="listDeps" listkey="id" listvalue="depName" id="sysDep_select" 
										multiple="multiple" moreselect="true" style="display: none">
									</select>
								</div>
								<!-- 筛选条件显示 -->
								<div class="padding-top-5 padding-bottom-5 text-left moreDepListShow" style="display:none" id="sysDep_selectDiv">
									<strong>部门选择:</strong>
								</div>
							</div>
							
							<div class="widget-body">
								<div style="width: 100%">
									<table class="table table-bordered ">
											<thead>
												<tr role="row" >
													<th style="text-align: center;font-weight:bold;" >
														部门
													</th>
													<th style="text-align: center;font-weight:bold;width: 90px" >
														姓名
													</th>
													<c:forEach var="monVar" begin="1" end="12" step="1">
														<th style="text-align: center;font-weight:bold;width: 65px" >
															${monVar}月
														</th>
													</c:forEach>
													<th style="text-align: center;font-weight:bold;width: 70px">
														合计
													</th>
												</tr>
											</thead>
										</table>
								</div>
								<div style="width: 100%">
									<table class="table table-bordered " id="editabledatatable">
										<tbody id="allTodoBody">
										</tbody>
									</table>
								</div>
								<div style="width: 100%">
									<table class="table table-bordered " id="sumtable">
									</table>
								</div>
								
						</div>
					</div>
					</div>
			</div>
			
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<style type="text/css">
		#infoList table{
			table-layout: fixed;
		}
		#infoList td,#infoList th{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
	</style>
</body>
</html>
