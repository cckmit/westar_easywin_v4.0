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
								<div
									class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
									<div class="checkbox ps-margin pull-left">
										<label> <input type="checkbox" class="colored-blue"
											id="checkAllBox" onclick="checkAll(this,'ids')"> <span
											class="text" style="color: inherit;">全选</span>
										</label>
									</div>
									<form id="searchForm" action="/meeting/listPagedMeeting">
										<input type="hidden" name="searchTab" id="searchTab"
											value="${empty param.searchTab?'11':param.searchTab}">
										<input type="hidden" name="sid" value="${param.sid}">
										<input type="hidden" name="meetingState" id="meetingState"
											value="${meeting.meetingState}">

										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group cond" id="moreCondition_Div">
														<a class="btn btn-default dropdown-toggle btn-xs"
															onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
																<c:when
																	test="${not empty meeting.startDate || not empty meeting.endDate}">
																	<font style="font-weight:bold;">筛选中</font>
																</c:when>
																<c:otherwise>
					                                            	更多
		                                            			</c:otherwise>
															</c:choose> <i class="fa fa-angle-down"></i>
														</a>
														<div
															class="dropdown-menu dropdown-default padding-bottom-10"
															style="min-width: 330px;">
															<div class="ps-margin ps-search padding-left-10">
																<span class="btn-xs">会议时间：</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${meeting.startDate}" id="startDate"
																	name="startDate" placeholder="开始时间"
																	onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																<span>~</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${meeting.endDate}" id="endDate" name="endDate"
																	placeholder="结束时间"
																	onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
															</div>
															<div class="ps-clear padding-top-10"
																style="text-align: center;">
																<button type="submit" class="btn btn-primary btn-xs">查询</button>
																<button type="button"
																	class="btn btn-default btn-xs margin-left-10"
																	onclick="resetMoreCon('moreCondition_Div')">重置</button>
															</div>
														</div>
													</div>
												</div>
											</div>

											<div class="ps-margin ps-search">
												<span class="input-icon"> <input id="title"
													name="title" class="form-control ps-input" type="text"
													placeholder="请输入关键字" value="${meeting.title}"> <a
													href="javascript:void(0)" class="ps-searchBtn"><i
														class="glyphicon glyphicon-search circular danger"></i></a>
												</span>
											</div>

										</div>
										<div class="batchOpt" style="display: none">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<c:choose>
															<c:when test="${meeting.meetingState==1}">
																<a class="btn btn-default dropdown-toggle btn-xs"
																	id="revokeMeeting"> 批量撤销 </a>
															</c:when>
															<c:otherwise>
																<a class="btn btn-default dropdown-toggle btn-xs"
																	id="delMeeting"> 批量删除 </a>
															</c:otherwise>
														</c:choose>
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
								<c:choose>
				<c:when test="${not empty listMeetings}">
								<div class="widget-body">
									<form id="delForm">
										<input type="hidden" name="redirectPage"> <input
											type="hidden" name="sid" value="${param.sid }">
										<table class="table table-striped table-hover">
											<thead>
												<tr role="row">
													<c:choose>
														<c:when test="${meeting.meetingState==1}">
															<th width="8%" valign="middle">序号</th>
															<th valign="middle" class="hidden-phone">会议名称</th>
															<th width="12%" valign="middle">开始时间</th>
															<th width="12%" valign="middle">结束时间</th>
															<th width="15%" valign="middle">会议地址</th>
															<th width="8%" valign="middle">发布情况</th>
															<th width="8%" valign="middle">会议状态</th>
															<th width="8%" valign="middle">会议类型</th>
														</c:when>
														<c:otherwise>
															<th width="8%" valign="middle">序号</th>
															<th valign="middle" class="hidden-phone">会议名称</th>
															<th width="12%" valign="middle">开始时间</th>
															<th width="12%" valign="middle">结束时间</th>
															<th width="15%" valign="middle">会议地址</th>
														</c:otherwise>
													</c:choose>
													<th width="6%" valign="middle">操作</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listMeetings}" var="obj" varStatus="vs">
													<tr class="optTr">
														<td class="optTd" style="height: 47px"><label
															class="optCheckBox" style="display: none;width: 20px">
																<input class="colored-blue" type="checkbox" name="ids"
																value="${obj.id}" /> <span class="text"></span>
														</label> <label class="optRowNum"
															style="display: block;width: 20px">${vs.count}</label></td>
														<td valign="middle"><c:choose>
																<c:when
																	test="${meeting.meetingState==1 && obj.timeOut==2}">
																	<!-- 会议已发布，未开始-->
																	<a href="javascript:void(0)"
																		onclick="editMeeting('${obj.id}','${obj.meetingType}',1)">
																		${obj.title } </a>
																</c:when>
																<c:when
																	test="${meeting.meetingState==1 && obj.timeOut==1}">
																	<!-- 会议已发布，已开始-->
																	<a href="javascript:void(0)"
																		onclick="invMeetUser('${obj.id}','${obj.meetingType}',1)">
																		${obj.title } </a>
																</c:when>
																<c:when
																	test="${meeting.meetingState==1 && obj.timeOut==0}">
																	<!-- 会议已发布，已结束-->
																	<c:choose>
																		<c:when test="${userInfo.id==obj.recorder}">
																			<!-- 是会议记录人员 -->
																			<a href="javascript:void(0)"
																				onclick="addSummary('${obj.id}')"> ${obj.title }
																			</a>
																		</c:when>
																		<c:otherwise>
																			<!-- 不是会议记录人员 -->
																			<a href="javascript:void(0)"
																				onclick="viewSummary('${obj.id}')"> ${obj.title }
																			</a>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:when test="${meeting.meetingState==0}">
																	<!-- 会议未发布 -->
																	<a href="javascript:void(0)"
																		onclick="editMeeting('${obj.id}','${obj.meetingType}',0)">
																		${obj.title } </a>
																</c:when>
																<c:otherwise>
																${obj.title }
															</c:otherwise>
															</c:choose></td>
														<td valign="middle">${obj.startDate }</td>
														<td valign="middle">${obj.endDate }</td>
														<td valign="middle">${obj.meetingAddrName }<c:choose>
																<c:when
																	test="${obj.applyState==-1 && obj.roomType==0 && obj.timeOut>0 && obj.meetingAddrId>0}">(未申请)</c:when>
																<c:when
																	test="${obj.applyState==0 && obj.roomType==0 && obj.timeOut>0 && obj.meetingAddrId>0}">(待审核)</c:when>
																<c:when
																	test="${obj.applyState==1 && obj.timeOut>0 && obj.roomType==0 && obj.meetingAddrId>0}">(已审核)</c:when>
																<c:when
																	test="${obj.applyState==2 && obj.timeOut>0 && obj.roomType==0 && obj.meetingAddrId>0}">(未通过)</c:when>
																<c:otherwise>
																&nbsp;&nbsp;
															</c:otherwise>
															</c:choose>
														</td>
														<c:if test="${meeting.meetingState==1}">
															<td valign="middle">
																${obj.meetingState==0?'待发布':'已发布'}</td>
															<c:set var="stateColor">
																<c:choose>
																	<c:when test="${obj.timeOut==0}">#FF0000</c:when>
																	<c:when test="${obj.timeOut==1}">#008000</c:when>
																	<c:when test="${obj.timeOut==2}">blue</c:when>
																</c:choose>
															</c:set>
															<td valign="middle" style="color: ${stateColor}"><c:choose>
																	<c:when test="${obj.timeOut==0}">已结束</c:when>
																	<c:when test="${obj.timeOut==1}">正在进行</c:when>
																	<c:when test="${obj.timeOut==2}">未开始</c:when>
																</c:choose></td>
															<td><c:choose>
																	<c:when test="${obj.meetingType==0 }">单次会议</c:when>
																	<c:otherwise>周期会议</c:otherwise>
																</c:choose></td>
														</c:if>
														<td valign="middle"><c:choose>
																<c:when
																	test="${meeting.meetingState==1 && obj.timeOut==2}">
																	<!-- 会议已发布，未开始-->
																	<a href="javascript:void(0)"
																		onclick="editMeeting('${obj.id}','${obj.meetingType}',1)"
																		style="color:blue">编辑</a>
																</c:when>
																<c:when
																	test="${meeting.meetingState==1 && obj.timeOut==1}">
																	<!-- 会议已发布，已开始-->
																	<a href="javascript:void(0)"
																		onclick="invMeetUser('${obj.id}')" style="color:blue">编辑</a>
																</c:when>
																<c:when
																	test="${meeting.meetingState==1 && obj.timeOut==0}">
																	<!-- 会议已发布，已结束-->
																	<c:choose>
																		<c:when test="${userInfo.id==obj.recorder}">
																			<!-- 是会议记录人员 -->
																			<a href="javascript:void(0)"
																				onclick="addSummary('${obj.id}')"
																				style="color: #008000"> 纪要 </a>
																		</c:when>
																		<c:otherwise>
																			<!-- 不是会议记录人员 -->
																			<a href="javascript:void(0)"
																				onclick="viewSummary('${obj.id}')"
																				style="color: #008000"> 纪要 </a>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:when test="${meeting.meetingState==0}">
																	<a href="javascript:void(0)"
																		onclick="editMeeting('${obj.id}','${obj.meetingType}',0)"
																		style="color:blue">编辑</a>
																</c:when>
																<c:otherwise>
																	<a style="color: gray">编辑</a>
																</c:otherwise>
															</c:choose></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/meeting/listPagedMeeting"></tags:pageBar>
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
								<a href="javascript:void(0);" onclick="newMeeting('normal')" class="return-btn"><i
									class="fa fa-plus"></i>新建会议</a>
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

