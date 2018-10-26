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
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="客户类型筛选">
													客户类型筛选
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default" id="crmTypeUl">
													<li>
														<a href="javascript:void(0)" class="clearValue" relateElement="crmTypeId">不限条件</a>
													</li>
													<!--数据异步获得  #crmTypeUl-->
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="负责人筛选">
														负责人筛选
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
									</div>
									<div class="ps-margin ps-search searchCond">
										<span class="input-icon">
											<input name="crmName" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
											<a href="javascript:void(0)" class="ps-searchBtn">
												<i class="glyphicon glyphicon-search circular danger"></i>
											</a>
										</span>
									</div>
									
									
									<div class="widget-buttons ps-widget-buttons" id="legendDiv">
										<label>
											<div legend style="background-color: #e51c23" class="pull-left"></div>
											<span class="pull-left margin-left-5" style="line-height: 20px">逾期</span> 
										</label>
										<label>
											<div legend style="background-color: #259b24" class="pull-left"></div>
											<span class="pull-left margin-left-5" style="line-height: 20px">正常</span> 
										</label>
									</div>
								</div>
								<div class="ps-clear" id="formTempData">
									<input type="hidden" name="year" id="year" value="${nowYear}">
									<input type="hidden" name="crmTypeId" id="crmTypeId" value="${param.crmTypeId}">
									<select list="listOwner" listkey="id" listvalue="userName" id="applyUser_select" 
										multiple="multiple" moreselect="true" style="display: none">
									</select>
								</div>
								<!-- 筛选条件显示 -->
								<div class="padding-top-5 padding-bottom-5 text-left moreUserListShow" style="display:none" id="applyUser_selectDiv">
									<strong>负责人员:</strong>
								</div>
							</div>
							
							<div class="widget-body" style="min-height: 550px">
								<table class="table table-bordered " id="editabledatatable">
										<thead>
											<tr role="row">
												<th style="text-align: center;font-weight:bold;width: 50px;padding: 0 0" >
													序
												</th>
												<th style="text-align: center;font-weight:bold;" >
													客户名称
												</th>
												<th style="text-align: center;font-weight:bold;width: 90px" >
													类型
												</th>
												<th style="text-align: center;font-weight:bold;width:120px;padding: 0 0" >
													责任人
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width:80px;">
													上次跟进
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width:70px;">
													跟进周期
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 70px"  >
													逾期等级
												</th>
												<th style="text-align: center;font-weight:bold;width: 80px;padding: 0 0" >
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
</body>
</html>
