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
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="发起人筛选">
													创建人筛选
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearMoreValue" relateList="creator_select">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="userMoreSelect" relateList="creator_select">人员选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="审批状态筛选">
													审批状态筛选
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearValue" relateElement="status">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="status" dataValue="4">已通过</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="status" dataValue="1">审批中</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="status" dataValue="-1">未通过</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="重复标记筛选">
													重复标记筛选
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearValue" relateElement="issueRepetitionMark">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="issueRepetitionMark" dataValue="0">非重复事件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setValue" relateElement="issueRepetitionMark" dataValue="1">重复事件</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin margin-right-5">
	                                        <div class="btn-group">
	                                            <a class="btn btn-default dropdown-toggle btn-xs"
	                                               data-toggle="dropdown" title="关联项目筛选">
	                                               		 关联项目筛选
	                                                <i class="fa fa-angle-down"></i>
	                                            </a>
	                                            <ul class="dropdown-menu dropdown-default">
	                                                <li>
	                                                    <a href="javascript:void(0)" class="clearValue" relateElement="busId">不限条件</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)" class="relateModSelect" busType="005" isMore="0" typeValue="relateModType" relateList="relateModes_select">项目选择</a>
	                                                </li>
	                                            </ul>
	                                        </div>
	                                    </div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty param.startDate 
														|| not empty param.endDate
														|| not empty param.startResolveDate
														|| not empty param.endResolveDate}">
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
														<span class="btn-xs">问题创建日期：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${param.startDate}" id="startDate" name="startDate" placeholder="开始日期"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${param.endDate}" id="endDate" name="endDate" placeholder="结束日期"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">问题关闭日期：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${param.startResolveDate}" id="startResolveDate" name="startResolveDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endResolveDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${param.endResolveDate}" id="endResolveDate" name="endResolveDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startResolveDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10" style="text-align: center;">
														<button type="button" class="btn btn-primary btn-xs moreSearchBtn">查询</button>
														<button type="button" class="btn btn-default btn-xs margin-left-10 moreClearBtn">重置</button>
													</div>
												</div>
											</div>
										</div>
									</div>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="flowName" name="flowName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
										<a href="javascript:void(0)" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
								<div style="float: left;width: 250px;display: none" id="formTempData">
									<input type="hidden" name="status" value="${param.status}">
									<input type="hidden" name="issueRepetitionMark" value="${param.issueRepetitionMark}">
									<input type="hidden" name="issueStatus" value="${param.issueStatus}">
									<input type="hidden" name="issueEndCode" value="${param.issueEndCode}">
									<input type="hidden" name="busId" value="${param.busId}">
									<input type="hidden" name="busType" value="${param.busType}">
									<select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
									</select>
								</div>
								<div class="widget-buttons ps-widget-buttons">
									<a href="javascript:void(0);" addIssuePms>
										<button class="btn btn-info btn-primary btn-xs" type="button">
											<i class="fa fa-plus"></i>
											问题过程
										</button>
									</a>
								</div>
							</div>
							<div class=" padding-top-10 text-left moreUserListShow" style="display:none" id="creator_selectDiv">
								<strong>创建人筛选:</strong>
							</div>
						</div>
								<div class="widget-body" style="min-height: 550px" id="infoList">
							
								<table class="table table-bordered " id="editabledatatable">
										<thead>
											<tr role="row">
												<th style="text-align: center;font-weight:bold;width: 40px;padding: 0 0">
													序
												</th>
												<th style="text-align: center;font-weight:bold;width: 90px;">
													创建日期
												</th>
												<th style="text-align: center;font-weight:bold;width: 60px">
													创建人
												</th>
												<th style="text-align: center;font-weight:bold;width: 100px;">
													问题单流水号
												</th>
												<th style="text-align: center;font-weight:bold">
													问题过程流程
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 60px;">
													审批状态
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 80px;">
													问题来源
												</th>
												<th style="text-align: center;font-weight:bold;width: 150px;">
													所属系统类型
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 85px;">
													问题结束代码
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 90px;">
													问题状态
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
	<!-- /Page Container -->
	
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

