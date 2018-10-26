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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
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
								<label> <input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')"> <span class="text" style="color: inherit;">全选</span>
								</label>
							</div>
							<div>
							<form action="/meeting/listPagedMeetWithSpSummary" class="subform" id="searchForm">
								<input type="hidden" name="searchTab" id="searchTab" value="${empty param.searchTab?'11':param.searchTab}">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" name="summaryNeedSpState" value="1"> 
								<input type="hidden" value="${meeting.summarySpState}" name="summarySpState" id="summarySpState">
								<input type="hidden" value="${meeting.organiser}" name="organiser" id="organiser">
								<input type="hidden" value="${meeting.organiserName}" name="organiserName" id="organiserName">

								<div class="searchCond" style="display: block">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown"> <c:choose>
														<c:when test="${not empty meeting.organiserName}">
															<font style="font-weight:bold;">${meeting.organiserName}</font>
														</c:when>
														<c:otherwise>发布人员</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)" onclick="userOneForUserIdCallBack('','organiser','','organiserName')">不限条件</a></li>
													<li><a href="javascript:void(0)" onclick="userMoreForUserId('${param.sid}','creator');">人员选择</a></li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${meeting.listCreator }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>
										
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
													<c:choose>
														<c:when test="${not empty meeting.summarySpState}">
															<font style="font-weight:bold;"> 
																<c:if test="${meeting.summarySpState==1}">审批中的</c:if> 
																<c:if test="${meeting.summarySpState==4}">审批通过</c:if>
																<c:if test="${meeting.summarySpState==-1}">审批终止</c:if>
															</font>
														</c:when>
														<c:otherwise>流程状态筛选</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearThisElement" relateElement="summarySpState">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="summarySpState" dataValue="1">审批中的</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="summarySpState" dataValue="4">已通过的</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="setElementValue" relateElement="summarySpState" dataValue="-1">未通过的</a>
													</li>
												</ul>
											</div>
										</div>
										
										<div class="table-toolbar ps-margin">
											<div class="btn-group cond" id="moreCondition_Div">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
														<c:when test="${not empty meeting.startDate || not empty meeting.endDate}">
															<font style="font-weight:bold;">筛选中</font>
														</c:when>
														<c:otherwise>
					                                            	更多
		                                            			</c:otherwise>
													</c:choose> <i class="fa fa-angle-down"></i>
												</a>
												<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
													<div class="ps-margin ps-search padding-left-10">
														<span class="btn-xs">会议时间：</span> <input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${meeting.startDate}" id="startDate" name="startDate"
															placeholder="开始时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" /> <span>~</span> <input class="form-control dpd2 no-padding condDate"
															type="text" readonly="readonly" value="${meeting.endDate}" id="endDate" name="endDate" placeholder="结束时间"
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

									<div class="ps-margin ps-search">
										<span class="input-icon"> <input id="title" name="title" class="form-control ps-input" type="text" placeholder="请输入关键字" value="${meeting.title}"> <a href="javascript:void(0)"
											class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
										</span>
									</div>
								</div>
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" id="delMeeting"> 批量删除 </a>
											</div>
										</div>
									</div>
								</div>
							</form>
								<div class="widget-buttons ps-widget-buttons">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-primary btn-xs" type="button" id="newMeeting" onclick="newMeeting('normal')">
											<i class="fa fa-plus"></i> 普通会议
										</button>
									</div>
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-primary btn-xs" type="button" id="newMeeting" onclick="newMeeting('modflow')">
											<i class="fa fa-plus"></i> 审批会议
										</button>
									</div>
								</div>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty meeting.listCreator ? 'none':'block'}">
								<strong>发布人筛选:</strong>
								<c:forEach items="${meeting.listCreator }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty listMeetings}">
								<div class="widget-body">
									<form id="delForm">
										<input type="hidden" name="redirectPage"> <input type="hidden" name="sid" value="${param.sid }">
										<table class="table table-striped table-hover">
											<thead>
												<tr role="row">
													<th width="15px" valign="middle">&nbsp;</th>
													<th valign="middle" class="hidden-phone">会议名称</th>
													<th width="8%" valign="middle">发布人</th>
													<th width="125px" valign="middle">开始时间</th>
													<th width="125px" valign="middle">结束时间</th>
													<th width="15%" valign="middle">会议地址</th>
													<th width="8%" valign="middle">审批状态</th>
													<th width="8%" valign="middle">审批人员</th>
													<th width="8%" valign="middle">纪要</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listMeetings}" var="obj" varStatus="vs">
													<tr class="optTr">
														<c:choose>
															<c:when test="${userInfo.id==obj.organiser}">
																<td class="optTd" style="height: 47px"><label class="optCheckBox" style="display: none;width: 20px"> <input class="colored-blue" type="checkbox" name="ids"
																		value="${obj.id}" /> <span class="text"></span>
																</label> <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label></td>
															</c:when>
															<c:otherwise>
																<td style="height: 47px"><label style="display: block;width: 20px">${vs.count}</label></td>
															</c:otherwise>
														</c:choose>
														<td valign="middle"><c:choose>
																<c:when test="${obj.summaryState==0 && userInfo.id==obj.recorder}">
																	<a href="javascript:void(0)" onclick="addSummary('${obj.id}')">${obj.title }</a>
																</c:when>
																<c:otherwise>
																	<a href="javascript:void(0)" onclick="viewSummary('${obj.id}')">${obj.title }</a>
																</c:otherwise>
															</c:choose></td>
														<td valign="middle">${obj.organiserName }</td>
														<td valign="middle">${obj.startDate }</td>
														<td valign="middle">${obj.endDate }</td>
														<td valign="middle">${obj.meetingAddrName }</td>
														<c:set var="stateColor">
															<c:choose>
																<c:when test="${obj.summarySpState==1}">blue</c:when>
																<c:when test="${obj.summarySpState==-1}">#FF0000</c:when>
																<c:when test="${obj.summarySpState==4}">#008000</c:when>
															</c:choose>
														</c:set>
														<td valign="middle" style="color: ${stateColor}">
															<c:choose>
																<c:when test="${obj.summarySpState==1}">审批中</c:when>
																<c:when test="${obj.summarySpState==-1}">驳回</c:when>
																<c:when test="${obj.summarySpState==4}">通过</c:when>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${obj.summarySpState==1}">
																	${obj.spExecutorName}
																</c:when>
																<c:otherwise>
																	--
																</c:otherwise>
															</c:choose>
														</td>
														<td valign="middle">
															<c:choose>
																<c:when test="${ obj.summarySpState==1 && obj.spExecutorId eq  userInfo.id}">
																	<a href="javascript:void(0)" onclick="addSummary('${obj.id}')">审批</a>
																</c:when>
																<c:otherwise>
																	<a href="javascript:void(0)" onclick="viewSummary('${obj.id}')">查看</a>
																</c:otherwise>
															</c:choose>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/meeting/listPagedMeetWithSpSummary"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>暂无相关会议信息！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
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
	<!-- Main Container -->
</body>
</html>

