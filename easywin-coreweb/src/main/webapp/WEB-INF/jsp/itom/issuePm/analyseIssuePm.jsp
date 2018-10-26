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
										<input type="hidden" name="year" id="year" value="${nowYear}">
									
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
									</div>
									
									<div style="float: left;width: 250px;display: none" id="formTempData">
										<input type="hidden" name="busId" value="${param.busId}">
										<input type="hidden" name="busType" value="${param.busType}">
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
							
						</div>
								<div class="widget-body" style="min-height: 550px">
							
								<table class="table table-bordered " id="editabledatatable">
										<thead>
											<tr role="row">
												<th style="text-align: center;font-weight:bold;width: 60px;padding: 0 0">
													月份
												</th>
												<th style="text-align: center;font-weight:bold;">
													问题总数
												</th>
												<th style="text-align: center;font-weight:bold;">
													成功解决问题
												</th>
												<th style="text-align: center;font-weight:bold">
													关闭问题数量
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0">
													问题成功解决率
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0">
													累加解决完成问题时间
												</th>
												<th style="text-align: center;font-weight:bold;">
													解决完成问题的数量
												</th>
												<th style="text-align: center;font-weight:bold;padding: 0 0">
													平均解决时间
												</th>
											</tr>
										</thead>
										<tbody id="allTodoBody">
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

