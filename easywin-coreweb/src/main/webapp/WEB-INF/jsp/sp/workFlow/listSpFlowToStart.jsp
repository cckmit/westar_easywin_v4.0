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
									<form action="/flowDesign/listSpFlow" id="searchForm">
										<input type="hidden" name="activityMenu" value="${param.activityMenu}">
										<input type="hidden" name="sid" value="${param.sid}"> 
										<input type="hidden" name="searchTab" value="${param.searchTab}">
										<input type="hidden" name="flowName" value="${spFlowModel.flowName}">
										<input type="hidden" name="spFlowTypeId" value="${spFlowModel.spFlowTypeId}">
										<input type="hidden" name="spFlowTypeName" value="${spFlowModel.spFlowTypeName}">
										<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs"
													data-toggle="dropdown">
													<c:choose>
														<c:when test="${not empty spFlowModel.spFlowTypeId}">
															<font style="font-weight:bold;">${spFlowModel.spFlowTypeName}
															</font>	
														</c:when>
														<c:otherwise>流程分类筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<c:choose>
														<c:when test="${not empty listSpFlowType}">
															<li><a href="javascript:void(0)" onclick="spFlowTypeClean();">不限条件</a></li>
							 								<c:forEach items="${listSpFlowType}" var="spFlowType" varStatus="status">
																<li><a href="javascript:void(0)" typeId="${spFlowType.id}" typeName="${spFlowType.typeName}" onclick="selectBySpFlowType(this);">${spFlowType.typeName}</a></li>
							 								</c:forEach>
															<li><a href="javascript:void(0)" typeId="0" onclick="selectBySpFlowType(this);">无类别</a></li>
														</c:when>
														<c:otherwise>
															<li><a href="javascript:void(0)">无流程分类</a></li>
														</c:otherwise>
													</c:choose>
												</ul>
											</div>
										</div>
									</div>
									</form>
									<div class="ps-margin ps-search">
										<span class="input-icon"> 
										<input class="form-control ps-input" id="searchFlowName" value="${spFlowModel.flowName}" type="text" placeholder="请输入关键字">
											<a href="#" class="ps-searchBtn"><i
												class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
                                    <div class="widget-buttons ps-widget-buttons">
                                    </div>
                                </div>
                                <div class="widget-body">
                                    <table class="table table-striped table-hover" id="editabledatatable">
                                        <thead>
                                            <tr role="row">
                                                <th> 序号</th>
                                                <th>流程名称</th>
												<th>流程类型</th>
                                                <th>创建于</th>
                                                <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:choose>
                                        		<c:when test="${not empty listSpFlow}">
                                        			<c:forEach items="${listSpFlow}" var="flow" varStatus="vs">
			                                            <tr>
			                                                <td>${vs.count}</td>
			                                                <td>${flow.flowName}</td>
			                                                <td>
																<c:choose>
																	<c:when test="${flow.spFlowTypeId==0}">无类别</c:when>
																	<c:otherwise>${flow.spFlowTypeName}</c:otherwise>
																</c:choose>
															</td>
			                                                <td>${flow.recordCreateTime}</td>
			                                                <td><a href="javascript:void(0);" onclick="startSpFlow(${flow.id});">发起审批</a></td>
			                                            </tr>
                                        			</c:forEach>
                                        		</c:when>
                                        	</c:choose>
                                        </tbody>
                                    </table>
                                    <tags:pageBar url="/flowDesign/listSpFlow"></tags:pageBar>
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

