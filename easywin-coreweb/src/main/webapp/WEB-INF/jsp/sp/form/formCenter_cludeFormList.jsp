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
							<div
								class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
								
									<form action="/form/listPagedCloudFormMod" id="searhForm">
										<input type="hidden" name="sid" value="${param.sid }"/>
										<input type="hidden" name="searchTab" value="${param.searchTab}"/>
										<input type="hidden" name="activityMenu" value="${param.activityMenu}">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin" >
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs"
														data-toggle="dropdown">
														<c:choose>
															<c:when test="${not empty formMod.formSortId}">
																<font style="font-weight:bold;">${formMod.modSortName}
																</font>	
															</c:when>
															<c:otherwise>分类</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<input type="hidden" name="formSortId" value="${formMod.formSortId}"/>
														<input type="hidden" name="modSortName" value="${formMod.modSortName}"/>
														<li>
															<a href="javascript:void(0)" onclick="formModSortSearch(-1,this)">不限条件</a>
														</li>
														<c:choose>
															<c:when test="${not empty listFormSort}">
																<c:forEach items="${listFormSort}" var="sortObj" varStatus="vs">
																	<li>
																		<a href="javascript:void(0)" onclick="formModSortSearch(${sortObj.id},this)">${sortObj.sortName }</a>
																	</li>
																</c:forEach>
															</c:when>
														</c:choose>
														<li>
															<a href="javascript:void(0)" onclick="formModSortSearch(0,this)">其他</a>
														</li>
													</ul>
												</div>
											</div>
										</div>
										<div class="ps-margin ps-search">
											<span class="input-icon"> <input id="modName" name="modName" value="${formMod.modName}"
													class="form-control ps-input" type="text"
													placeholder="请输入关键字"> <a href="javascript:void(0)"
												class="ps-searchBtn"><i
													class="glyphicon glyphicon-search circular danger"></i> </a> </span>
										</div>
									</form>
									<!-- 
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" id="addFormMod" isCloude="true">
										<i class="fa fa-plus"></i>新建表单
									</button>
									<button class="btn btn-info btn-xs" type="button" id="addFormSort">
										<i class="fa fa-plus"></i>添加分类
									</button>
								</div>
								 -->
							</div>
							<c:choose>
											<c:when test="${not empty formModlist}">
							<div class="widget-body">
								<table class="table table-striped table-hover"
									id="editabledatatable">
									<thead>
										<tr role="row">
											<th>
												序号
											</th>
											<th>
												名称
											</th>
											<th style="text-align: center;">
												分类
											</th>
											<th style="text-align: center;">
												创建时间
											</th>
											<th style="text-align: center;">
												操作
											</th>
										</tr>
									</thead>
									<tbody id="allTodoBody">
										
												<c:forEach items="${formModlist}" var="formModVo" varStatus="vs">
													<tr>
														<td class="rowNum">
															${vs.count}
														</td>
														<td>
															${formModVo.modName }
														</td>
														<td style="text-align: center;" class="optUpdataSort" title="${formModVo.enable==1?'修改分类':''}">
															<a href="javascript:void(0)" dataId="${formModVo.id}" enableState="${formModVo.enable}" formSortId="${formModVo.formSortId}">${formModVo.modSortName}</a>
														</td>
														<td style="text-align: center;">
															${fn:substring(formModVo.recordCreateTime,0,16) }
														</td>
														<td class="optTd" style="text-align: center;">
															<a href="javascript:void(0)" onclick="viewFormMod(${formModVo.id})">预览</a>|
															<!-- 
															<a href="javascript:void(0)" onclick="editFormMod(${formModVo.id})">编辑</a>|
															 -->
															<a href="javascript:void(0)" onclick="downLoadFormMod(${formModVo.id},this)">下载使用</a>
														</td>
													</tr>
												</c:forEach>
										
									</tbody>
								</table>
								 <tags:pageBar url="/form/listPagedCloudFormMod"></tags:pageBar>
							</div>
								</c:when>
								<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
							<section class="error-container text-center">
								<h1>
									<i class="fa fa-exclamation-triangle"></i>
								</h1>
								<div class="error-divider">
									<h2>还没相关云表单！</h2>
									<p class="description">协同提高效率，分享拉近距离。</p>
								</div>
							</section>
						</div></c:otherwise>
										</c:choose>
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

