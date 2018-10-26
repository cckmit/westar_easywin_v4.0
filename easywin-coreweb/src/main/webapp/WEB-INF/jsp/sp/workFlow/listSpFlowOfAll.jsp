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
								<form action="/workFlow/listSpFlowOfAll" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="10">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="flowState" value="${instance.flowState}">
									<input type="hidden" name="flowName" value="${instance.flowName}">
									<input type="hidden" name="executor" value="${instance.executor}">
									<input type="hidden" name="executorName" value="${instance.executorName}">
									<input type="hidden" name="orderBy" value="${instance.orderBy}">
									<input type="hidden" name="creator" value="${instance.creator}">
									<input type="hidden" name="creatorName" value="${instance.creatorName}">
									<input type="hidden" name="spState" value="${instance.spState}">
									<div class="btn-group pull-left searchCond">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="排序">
													<c:choose>
														<c:when test="${not empty instance.orderBy}">
															<font style="font-weight:bold;"> <c:if test="${instance.orderBy=='executor'}">按审批人</c:if> <c:if test="${instance.orderBy=='crTimeNewest'}">按创建时间(降序)</c:if> <c:if
																	test="${instance.orderBy=='crTimeOldest'}">按创建时间(升序)</c:if>
															</font>
														</c:when>
														<c:otherwise>排序</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="orderByClean()">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('executor');">按审批人</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeNewest');">按创建时间(降序)</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="orderBy('crTimeOldest');">按创建时间(升序)</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
													<c:choose>
														<c:when test="${not empty instance.flowState}">
															<font style="font-weight:bold;"> <c:if test="${instance.flowState==1}">审批中的</c:if> <c:if test="${instance.flowState==4}">审批完的</c:if> <c:if test="${instance.flowState==2}">待发起的</c:if>
															</font>
														</c:when>
														<c:otherwise>流程状态筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="flowStateClean()">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectByFlowState(1);">审批中的</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectByFlowState(4);">已完成的</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectByFlowState(2);">待发起的</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="审批结果筛选">
													<c:choose>
														<c:when test="${not empty instance.spState}">
															<font style="font-weight:bold;"> <c:if test="${instance.spState==0}">驳回</c:if> <c:if test="${instance.spState==1}">通过</c:if>
															</font>
														</c:when>
														<c:otherwise>审批结果筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="spStateClean()">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectBySpState(0);">驳回</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="selectBySpState(1);">通过</a>
													</li>
												</ul>
											</div>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="发起人筛选">
													<c:choose>
														<c:when test="${not empty instance.creator}">
															<font style="font-weight:bold;">${instance.creatorName}</font>
														</c:when>
														<c:otherwise>发起人筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="userOneForUserIdCallBack('',
												'creator','','')">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="userMoreForUserId('${param.sid}','creator');">人员选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${instance.listCreator }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="当前审批人筛选">
													<c:choose>
														<c:when test="${not empty instance.executor}">
															<font style="font-weight:bold;">${instance.executorName}</font>
														</c:when>
														<c:otherwise>当前审批人筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" onclick="userOneForUserIdCallBack('',
												'executor','','')">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" onclick="userMoreForUserId('${param.sid}','executor');">人员选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listExecutor" listkey="id" listvalue="userName" id="executor_select" name="listExecutor.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${instance.listExecutor }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
													<c:choose>
														<c:when test="${not empty instance.startDate || not empty instance.endDate}">
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
														<span class="btn-xs">起止时间：</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${instance.startDate}" id="startDate" name="startDate" placeholder="开始时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
														<span>~</span>
														<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${instance.endDate}" id="endDate" name="endDate" placeholder="结束时间"
															onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
													</div>
													<div class="ps-clear padding-top-10" style="text-align: center;">
														<button type="submit" class="btn btn-primary btn-xs">查询</button>
														<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="batchOpt" style="display: none">
										<div class="btn-group pull-left">
											<c:if test="${empty delete}">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" onclick="delSpFlow()"> 批量删除 </a>
													</div>
												</div>
											</c:if>
										</div>
									</div>
								</form>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input id="searchFlowName" value="${instance.flowName}" class="form-control ps-input" type="text" placeholder="请输入关键字">
										<a href="#" class="ps-searchBtn">
											<i class="glyphicon glyphicon-search circular danger"></i>
										</a>
									</span>
								</div>
								<c:if test="${empty add}">
									<div class="widget-buttons ps-widget-buttons">
										<a href="javascript:void(0);" onclick="newSpFlow();">
											<button class="btn btn-info btn-primary btn-xs" type="button">
												<i class="fa fa-plus"></i>
												新建审批
											</button>
										</a>
									</div>
								</c:if>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty instance.listCreator ? 'none':'block'}">
								<strong>发起人筛选:</strong>
								<c:forEach items="${instance.listCreator }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty instance.listExecutor ? 'none':'block'}">
								<strong>当前审批人筛选:</strong>
								<c:forEach items="${instance.listExecutor }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('executor','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty listSp}">
								<div class="widget-body">
									<form action="/workFlow/delSpFlow?sid=${param.sid}" method="post" id="delForm">
										<input type="hidden" id="redirectPage" name="redirectPage" />
										<table class="table table-striped table-hover" id="editabledatatable">
											<thead>
												<tr role="row">
													<th style="text-align:center;width:5%"><label>
															<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')">
															<span class="text" style="color: inherit;"></span>
														</label></th>
													<th>名称</th>
													<th>创建于</th>
													<th>状态</th>
													<th>当前审批人</th>
													<th>审批结果</th>
													<th>创建人</th>
													<th>操作</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listSp}" var="sp" varStatus="vs">
													<tr class="optTr">
														<td style="text-align:center;" class="optTd"><label class="optCheckBox" style="display: none;width: 20px">
																<input class="colored-blue" type="checkbox" name="ids" value="${sp.id}" ${(sp.creator==userInfo.id and sp.flowState==2)?'':'disabled="disabled"' } state="${sp.flowState }" />
																<span class="text"></span>
															</label> <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label></td>
														<td><a href="javascript:void(0);" onclick="viewSpFlow('${sp.id}')">${sp.flowName}</a></td>
														<td>${sp.recordCreateTime}</td>
														<td><c:if test="${sp.flowState==0}">
																<span style="color:gray;font-weight:bold;">无效的</span>
															</c:if> <c:if test="${sp.flowState==1}">
																<span style="color:green;font-weight:bold;">审批中</span>
															</c:if> <c:if test="${sp.flowState==2}">
																<span style="color:fuchsia;font-weight:bold;">草稿</span>
															</c:if> <c:if test="${sp.flowState==4}">
																<span style="color:red;font-weight:bold;">完结</span>
															</c:if>
														</td>
														<td>
															<div class="ticket-user pull-left other-user-box">
																<c:choose>
																	<c:when test="${sp.flowState==2 || sp.flowState==4}">
							 							--
							 						</c:when>
																	<c:otherwise>
																		<div class="ticket-user pull-left other-user-box" data-container="body" data-placement="left" data-toggle="popover">
																			<img class="user-avatar userImg" title="${sp.executorName}"
																				src="/downLoad/userImg/${sp.comId}/${sp.executor}"/>
																			<i class="user-name">${sp.executorName}</i>
																		</div>
																	</c:otherwise>
																</c:choose>
															</div>
														</td>
														<td><c:choose>
																<c:when test="${sp.flowState==4 }">
																	<c:if test="${sp.spState==0}">
																		<span style="color:red;font-weight:bold;">驳回</span>
																	</c:if>
																	<c:if test="${sp.spState==1}">
																		<span style="color:green;font-weight:bold;">通过</span>
																	</c:if>
																</c:when>
																<c:otherwise>
														--
													</c:otherwise>
															</c:choose></td>
														<td>
															<div class="ticket-user pull-left other-user-box">
																<div class="ticket-user pull-left other-user-box" data-container="body" data-placement="left" data-toggle="popover">
																	<img class="user-avatar userImg" title="${sp.creatorName}"
																		src="/downLoad/userImg/${sp.comId}/${sp.creator}"/>
																	<i class="user-name">${sp.creatorName}</i>
																</div>
															</div>
														</td>
														<td><c:if test="${sp.flowState==1}">
																<c:if test="${sp.executor==userInfo.id && sp.executor==userInfo.id}">
																	<a href="javascript:void(0);" onclick="viewSpFlow('${sp.id}')">
																		<c:if test="${sp.executeType eq 'assignee'}">
																			审批
																		</c:if>
																		<c:if test="${sp.executeType eq 'huiqian'}">
																			会签
																		</c:if>
																	</a>
																</c:if>
																<c:if test="${sp.executor!=userInfo.id}">
																	<a href="javascript:void(0);" onclick="viewSpFlow('${sp.id}')">查看</a>
																</c:if>
															</c:if> 
															<c:if test="${sp.flowState==2}">
																<c:if test="${sp.creator==userInfo.id}">
																	<a href="javascript:void(0);" onclick="viewSpFlow('${sp.id}')">继续</a>
																</c:if>
																<c:if test="${sp.creator!=userInfo.id}">
																	<a href="javascript:void(0);" onclick="viewSpFlow('${sp.id}')">查看</a>
																</c:if>
															</c:if> <c:if test="${sp.flowState==4}">
																<a href="javascript:void(0);" onclick="viewSpFlow('${sp.id}')">查看</a>
															</c:if></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/workFlow/listSpFlowOfAll"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>暂无相关审批数据！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
											<a href="javascript:void(0);" onclick="newSpFlow();" class="return-btn">
												<i class="fa fa-plus"></i>
												新建审批
											</a>
										</div>
									</section>
								</div>
							</c:otherwise>
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

