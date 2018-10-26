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
	<body>
	<!-- Page Content -->
            <div class="page-content">
                <!-- Page Body -->
                <div class="page-body">
                    <div class="row">
                        <div class="col-md-12 col-xs-12 ">
                            <div class="widget">
                                <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                                	<div class="checkbox ps-margin pull-left">
										<label>
											<input type="checkbox" class="colored-blue">
											<span class="text">全选</span>
										</label>
									</div>
									<form action="/flowDesign/flowCfg" id="searchForm">
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
                                    	<button class="btn btn-info btn-primary btn-xs" type="button" onclick="openWinOfAddFlow();">
                                    		<i class="fa fa-plus"></i>
                                    		新建流程
                                    	</button>
                                    </div>
                                </div>
                                <div class="widget-body">
                                    <table class="table table-striped table-hover" id="editabledatatable">
                                        <thead>
                                            <tr role="row">
                                                <th> 序号</th>
                                                <th>Category</th>
                                                <th>DeploymentId</th>
                                                <th>Description</th>
                                                <th>DiagramResourceName</th>
                                                <th>Key</th>
                                                <th>Name</th>
                                                <th>ResourceName</th>
                                                <th>TenantId</th>
                                                <th>Version</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:choose>
                                        		<c:when test="${not empty listProcess}">
                                        			<c:forEach items="${listProcess}" var="obj" varStatus="vs">
			                                            <tr>
			                                                <td>${vs.count}&nbsp;</td>
			                                                <td>${obj.category}&nbsp;</td>
			                                                <td>${obj.deploymentId}&nbsp;</td>
			                                                <td>${obj.description}&nbsp;</td>
			                                                <td>${obj.diagramResourceName}&nbsp;</td>
			                                                <td>${obj.key}&nbsp;</td>
			                                                <td>${obj.name}&nbsp;</td>
			                                                <td>${obj.resourceName}&nbsp;</td>
			                                                <td>${obj.tenantId}&nbsp;</td>
			                                                <td>${obj.version}&nbsp;</td>
			                                            </tr>
                                        			</c:forEach>
                                        		</c:when>
                                        	</c:choose>
                                        </tbody>
                                    </table>
                                    <tags:pageBar url="/flowDesign/listProcess"></tags:pageBar>
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

