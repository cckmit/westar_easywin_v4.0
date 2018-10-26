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
										<div class="table-toolbar ps-margin margin-right-5">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="发布人筛选">
													 发布人筛选
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
										
										<div class="table-toolbar ps-margin margin-right-5">
	                                        <div class="btn-group">
	                                            <a class="btn btn-default dropdown-toggle btn-xs"  data-toggle="dropdown" title="产品筛选">
	                                            	产品筛选<i class="fa fa-angle-down"></i>
	                                            </a>
	                                            <ul class="dropdown-menu dropdown-default">
	                                                <li>
	                                                    <a href="javascript:void(0)" class="clearMoreValue" relateList="product_select">不限条件</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)" class="productMoreSelect" relateList="product_select">产品选择</a>
	                                                </li>
	                                            </ul>
	                                        </div>
	                                    </div>
	                                    <div class="table-toolbar ps-margin margin-right-5">
								         <div class="btn-group">
								             <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="类型筛选">
								             	类型筛选<i class="fa fa-angle-down"></i>
								            </a>
								             <ul class="dropdown-menu dropdown-default">
								                <li><a href="javascript:void(0)" class="clearValue" relateElement="type" relateElementName="typeName">不限条件</a></li>
												<li><a href="javascript:void(0)" class="setValue" relateElement="type" dataValue="1" relateElementName="typeName">新增</a></li>
												<li><a href="javascript:void(0)" class="setValue" relateElement="type" dataValue="2" relateElementName="typeName">变更</a></li>
												<li><a href="javascript:void(0)" class="setValue" relateElement="type" dataValue="3" relateElementName="typeName">BUG</a></li>
								              </ul>
					                     </div>
					                </div>
									</div>
									<div class="ps-margin ps-search searchCond">
										<span class="input-icon">
											<input name="serialNum" class="form-control ps-input moreSearch" type="text" placeholder="请输入关键字">
											<a href="javascript:void(0)" class="ps-searchBtn">
												<i class="glyphicon glyphicon-search circular danger"></i>
											</a>
										</span>
									</div>
								</div>
								<div class="ps-clear" id="formTempData">
									<input type="hidden" name="type" id="type">
									<input type="hidden" name="crmTypeId" id="crmTypeId" value="${param.crmTypeId}">
									<select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple"
                                               moreselect="true" style="width: 100%; height: 100px;display: none">
                                      </select>
                                       <select list="listProduct" listkey="id" listvalue="name" id="product_select" name="listProduct.id" multiple="multiple"
                                               moreselect="true" style="width: 100%; height: 100px;display: none;">
                                       </select>
								</div>
								<!-- 筛选条件显示 -->
								<div class=" padding-top-10 text-left moreUserListShow" style="display:none" id="creator_selectDiv">
									<strong>发布人筛选:</strong>
								</div>
								<div class=" padding-top-10 text-left moreProductListShow" style="display:none" id="product_selectDiv">
									<strong>
										 产品筛选: 
									</strong>
								</div>
								<div class=" padding-top-10 text-left " style="display:none" id="applyUser_selectDiv">
									<strong>
										 项目筛选: 
									</strong>
								</div>
							
							</div>
							
							<div class="widget-body" style="min-height: 550px">
								<table class="table table-bordered " id="editabledatatable">
										<thead>
											<tr role="row">
												<th style="text-align: center;font-weight:bold;width: 50px;padding: 0 0" >
													序
												</th>
												<th style="text-align: center;font-weight:bold;width:150px;" >
													需求编号
												</th>
												<th style="text-align: center;font-weight:bold;width: 150px" >
													星级
												</th>
												<th style="text-align: center;font-weight:bold;width:80px;padding: 0 0" >
													类型
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;">
													关联项目
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width:120px;">
													发布时间
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 100px"  >
													所属阶段
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 120px"  >
													阶段持续时间
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0;width: 70px"  >
													阶段责任人
												</th>
												<th style="text-align: center;font-weight:bold;width: 80px;padding: 0 0" >
													操作
												</th>
											</tr>
										</thead>
										<tbody id="allTodoBody">
										</tbody>
									</table>
									<div class="panel-body ps-page bg-white" style="font-size: 12px;display: none">
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
