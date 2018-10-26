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
	<div class="page-content" id="infoList">
		<!-- Page Body -->
		<div class="page-body">
			
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="报销类型">
													报销类型
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)"  class="clearValue" relateElement="busTripState" >不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="busTripState" dataValue="1">出差报销</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="busTripState" dataValue="0">一般报销</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="汇报流程状态">
													汇报流程状态
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearValue" relateElement="loanRepStatus">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="loanRepStatus" dataValue="1">汇报通过</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="loanRepStatus" dataValue="-1">汇报失败</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="loanRepStatus" dataValue="0">汇报审批中</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													更多
													<i class="fa fa-angle-down"></i>
												</a>
												<div class="dropdown-menu dropdown-default padding-bottom-10 subMore" style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">起止时间：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10" style="text-align: center;">
														<button type="button" class="btn btn-primary btn-xs moreSearchBtn noMoreShow">查询</button>
														<button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn noMoreShow">重置</button>
													</div>
												</div>
											</div>
										</div>
									</div>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input name="loanReportName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
										<a href="javascript:void(0)" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
							</div>
							<div class="ps-clear" id="formTempData">
								<input type="hidden" name="busTripState">
								<input type="hidden" name="loanRepStatus">
								<input type="hidden" name="creator" value="${userInfo.id}">
								<select list="listCreator" listkey="id" listvalue="userName" id="reportUser_select" 
									multiple="multiple" moreselect="true" style="display: none">
								</select>
							</div>
							<div class="padding-top-5 padding-bottom-5 text-left moreUserListShow" style="display:none" id="reportUser_selectDiv">
								<strong>报销人员:</strong>
							</div>
						</div>
						<div class="widget-body" id="infoList">
							<table class="table table-bordered">
								<thead>
									<tr role="row">
										<th style="text-align: center;font-weight:bold;width: 80px;padding: 0">
											序
										</th>
										<th style="text-align: center;font-weight:bold;width: 70px;padding: 0">
											报销类型
										</th>
										<th style="text-align: center;font-weight:bold;width: 70px;padding: 0">
											报销人员
										</th>
										<th style="text-align: center;font-weight:bold;">
											汇报流程
										</th>
										<th style="text-align: center;font-weight:bold;width:120px">
											起止时间
										</th>
										<th style="text-align: center;font-weight:bold;width:130px">
											汇报时间
										</th>
										<th style="text-align: center;font-weight:bold;">
											报销流程
										</th>
										<th style="text-align: center;font-weight:bold;width:130px">
											报销时间
										</th>
										<th style="text-align: center;font-weight:bold;width: 70px;padding: 0">
											报销状态
										</th>
										<th style="text-align: center;font-weight:bold;width: 90px">
											实报销(元)
										</th>
									</tr>
								</thead>
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
			
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<script src="/static/ajaxPageJs/jquery.paginatio.js" type="text/javascript" charset="utf-8"></script>
	<style type="text/css">
		#infoList table{
			table-layout: fixed;
		}
		#infoList td{
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden; 
		}
	</style>
</body>
</html>
