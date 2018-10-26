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
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="借款申请流程状态">
														借款申请流程状态
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li>
															<a href="javascript:void(0)" class="clearValue" relateElement="status">不限条件</a>
														</li>
														<li>
															<a href="javascript:void(0)" class="setValue" relateElement="status" dataValue="4">审核通过</a>
														</li>
														<li>
															<a href="javascript:void(0)" class="setValue" relateElement="status" dataValue="-1">审核失败</a>
														</li>
														<li>
															<a href="javascript:void(0)" class="setValue" relateElement="status" dataValue="1">审批中</a>
														</li>
													</ul>
												</div>
											</div>
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="销账状态">
														销账状态
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li>
															<a href="javascript:void(0)" class="clearValue" relateElement="loanOffState">不限条件</a>
														</li>
														<li>
															<a href="javascript:void(0)" class="setValue" relateElement="loanOffState" dataValue="1">已销账</a>
														</li>
														<li>
															<a href="javascript:void(0)" class="setValue" relateElement="loanOffState" dataValue="0">未销账</a>
														</li>
													</ul>
												</div>
											</div>
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="申请人筛选">
														申请人筛选
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li>
															<a href="javascript:void(0)" class="clearMoreValue" relateList="applyUser_select">不限条件</a>
														</li>
														<li>
															<a href="javascript:void(0)" class="userMoreSelect" relateList="applyUser_select">人员选择</a>
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
															<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${loanApply.startDate}" id="startDate" name="startDate" placeholder="开始时间"
																onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
															<span>~</span>
															<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${loanApply.endDate}" id="endDate" name="endDate" placeholder="结束时间"
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
											<input name="flowName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
											<a href="javascript:void(0)" class="ps-searchBtn">
												<i class="glyphicon glyphicon-search circular danger"></i>
											</a>
										</span>
									</div>
									</form>
									<c:if test="${empty add}">
										<div class="widget-buttons ps-widget-buttons">
											<button class="btn btn-info btn-primary btn-xs margin-left-5" type="button" 
											 name="addLoanApply" onclick="javascript:void(0);" busType="035"><i class="fa fa-plus"></i>一般费用说明</button>
										</div>
									</c:if>
								</div>
								<div class="ps-clear" id="formTempData">
									<input type="hidden" name="isBusinessTrip" value="0">
									<input type="hidden" name="loanOffState">
									<input type="hidden" name="status">
									<select list="listCreator" listkey="id" listvalue="userName" id="applyUser_select" 
										multiple="multiple" moreselect="true" style="display: none">
									</select>
								</div>
								<!-- 筛选条件显示 -->
								<div class="padding-top-5 padding-bottom-5 text-left moreUserListShow" style="display:none" id="applyUser_selectDiv">
									<strong>报销人员:</strong>
								</div>
							</div>
							
							<div class="widget-body" style="min-height: 550px">
							
								<table class="table table-bordered" id="editabledatatable">
										<thead>
											<tr role="row">
												<th style="text-align: center;font-weight:bold;width: 20px;padding: 0 0">
													序
												</th>
												<th style="text-align: center;font-weight:bold;width: 60px">
													申请人
												</th>
												<th style="text-align: center;font-weight:bold;min-width: 60px">
													一般费用说明记录
												</th>
												<th style="text-align: center;font-weight:bold;width: 120px">
													起止日期
												</th>
												<th style="text-align: center;font-weight:bold;width: 70px;padding: 0 0">
													借款限额
												</th>
												<th style="text-align: center;font-weight:bold;width: 70px;padding: 0 0">
													申请中
												</th>
												<th style="text-align: center;font-weight:bold;width: 70px;padding: 0 0">
													累计借款
												</th>
												<!-- <th style="text-align: center;font-weight:bold;width: 70px;padding: 0 0 !important;">
													汇报次数<br>
													<i class="fa fa-check green" title="申请成功"></i>
													|
													<i class="fa fa-gavel blue" title="审批中"></i>
												</th>
												<th style="text-align: center;font-weight:bold;width: 70px;padding: 0 0">
													报销中
												</th> -->
												<th style="text-align: center;font-weight:bold;width: 80px;padding: 0 0 !important;">
													报销进度
												</th>
												<th style="text-align: center;font-weight:bold;width: 70px;padding: 0 0">
													累计报销
												</th>
												<th style="text-align: center;font-weight:bold;width: 80px">
													操作
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
				
				<div id="subDataDiv" class="pull-left" style="width: 100%;display:none;">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							 <span class="widget-caption themeprimary subTitle" style="font-size: 20px;cursor: pointer;">&lt;&lt;任务发布</span>
							 <span class="widget-caption themeprimary subType" style="font-size: 15px;margin-top: 2px">
                        	</span>
							 <div class="widget-buttons ps-widget-buttons subBtn">
							 </div>
						</div>
						
						<div class="widget-body">
							<table class="table table-bordered">
								<thead>
								</thead>
								<tbody >
								</tbody>
							</table>
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
	</style>
</body>
</html>
