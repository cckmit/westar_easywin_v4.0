<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<script type="text/javascript">
	</script>
	</head>
	<body>
	<!-- Page Content -->
            <div class="page-content">
                <!-- Page Body -->
                <div class="page-body">
                    <div class="row">
                        <div class="col-md-9 col-xs-12 ">
                            <div class="widget">
                                <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                                	<div class="checkbox ps-margin pull-left">
										<label>
											<input type="checkbox" class="colored-blue">
											<span class="text">全选</span>
										</label>
									</div>
									<form action="/crm/customerListPage" id="searchForm">
										<input type="hidden" name="sid" value="${param.sid}">
										<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab}">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin">
	                                        <div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
												分类<i class="fa fa-angle-down"></i></a>
											 <ul class="dropdown-menu dropdown-default">
	                                                <li>
	                                                    <a href="javascript:void(0)">按时间</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)">按紧急</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)">按时间</a>
	                                                </li>
	                                                <li class="divider"></li>
	                                                <li>
	                                                    <a href="javascript:void(0)">按时间</a>
	                                                </li>
	                                            </ul>
	                                        </div>
	                                    </div>
	                                    <div class="table-toolbar ps-margin">
	                                        <div class="btn-group">
	                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">排序<i class="fa fa-angle-down"></i></a>
	                                            <ul class="dropdown-menu dropdown-default">
	                                                <li>
	                                                    <a href="javascript:void(0)">按时间</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)">按紧急</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)">按时间</a>
	                                                </li>
	                                                <li class="divider"></li>
	                                                <li>
	                                                    <a href="javascript:void(0)">按时间</a>
	                                                </li>
	                                            </ul>
	                                        </div>
	                                    </div>
	                                    <div class="table-toolbar ps-margin">
	                                        <div class="btn-group">
	                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">筛选<i class="fa fa-angle-down"></i></a>
	                                            <ul class="dropdown-menu dropdown-default">
	                                                <li>
	                                                    <a href="javascript:void(0)">我负责的</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)">我负责的</a>
	                                                </li>
	                                                <li>
	                                                    <a href="javascript:void(0)">我负责的</a>
	                                                </li>
	                                                <li class="divider"></li>
	                                                <li>
	                                                    <a href="javascript:void(0)">我负责的</a>
	                                                </li>
	                                            </ul>
	                                        </div>
	                                        
	                                    </div>
										</div>
									</form>
                                    <div class="ps-margin ps-search">
									<span class="input-icon">
									<input id="glyphicon-search" class="form-control ps-input" type="text" placeholder="请输入关键字">
									<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
									</span>
									</div>
                                    <div class="widget-buttons ps-widget-buttons">
                                    	<button class="btn btn-info btn-primary btn-xs" type="button">
                                    		<i class="fa fa-plus"></i>
                                    		普通任务
                                    	</button>
                                    </div>
                                </div>
                                <div class="widget-body">
                                    <table class="table table-striped table-hover" id="editabledatatable">
                                        <thead>
                                            <tr role="row">
                                                <th> 序号</th>
                                                <th>任务名称</th>
                                                <th>紧急度 </th>
                                                <th>进度</th>
                                                <th>发起人</th>
                                                <th>创建日期</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:choose>
                                        		<c:when test="${not empty list}">
                                        			<c:forEach items="${list}" var="obj" varStatus="vs">
			                                            <tr>
			                                                <td>${vs.count}</td>
			                                                <td><a href="javascript:void(0);" onclick="viewTask('${param.sid}','${obj.id}');"><tags:cutString num="31">${obj.taskName}</tags:cutString></a></td>
			                                             	<c:set var="gradeColor">
													 			<c:choose>
													 				<c:when test="${obj.grade==4}">label-danger</c:when>
													 				<c:when test="${obj.grade==3}">label-danger</c:when>
													 				<c:when test="${obj.grade==2}">label-orange</c:when>
													 				<c:when test="${obj.grade==1}">label-success</c:when>
													 			</c:choose>
													 		</c:set>
			                                                <td>
			                                                	<span class="label ${gradeColor}">
		                                                		<c:choose>
													 				<c:when test="${obj.grade==4}">紧急且重要</c:when>
													 				<c:when test="${obj.grade==3}">紧急</c:when>
													 				<c:when test="${obj.grade==2}">重要</c:when>
													 				<c:when test="${obj.grade==1}">普通</c:when>
													 			</c:choose>
		                                                		</span>
			                                                </td>
			                                                <td class="center ">
			                                                	<c:choose>
											 						<c:when test="${obj.state==3}">已挂起</c:when>
											 						<c:when test="${obj.state==4}"><font style="color:#FF0000;">已完成</font></c:when>
											 						<c:otherwise>${obj.taskprogressDescribe}</c:otherwise>
											 					</c:choose>
			                                                </td>
			                                                <td>
				                                                <div class="ticket-user pull-left other-user-box">
																	<img class="user-avatar"
																		src="/downLoad/userImg/${obj.comId}/${obj.owner}?sid=${param.sid}"
																		title="${obj.ownerName}" />
																	<span class="user-name">${obj.ownerName}</span>
																</div>
			                                                </td>
			                                                <td>${fn:substring(obj.recordCreateTime,0,10) }</td>
			                                            </tr>
                                        			</c:forEach>
                                        		</c:when>
                                        	</c:choose>
                                        </tbody>
                                    </table>
                                    <tags:pageBar url="/task/taskToDoListPage"></tags:pageBar>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-xs-12">
                        	<div class="widget">
							<div class="widget-header bordered-bottom bordered-themeprimary">
							<i class="widget-icon fa fa-tasks themeprimary"></i>
							<span class="widget-caption themeprimary">任务统计</span>
							</div>
							<div class="widget-body">
								<img src="/static/assets/img/chart.png" style="width: 100%; height: 100%;"/>
							</div>
                        </div>
                    </div>
                      
                                     
                </div>
                <!-- /Page Body -->
            </div>
            <!-- /Page Content -->
            
        </div>
        <!-- /Page Container -->
	</body>
</html>

