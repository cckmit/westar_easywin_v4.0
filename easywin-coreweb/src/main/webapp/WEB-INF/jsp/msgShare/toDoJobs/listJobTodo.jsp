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
				<div class="col-md-12 col-xs-12 ">
							<div class="widget">
								<div
									class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
									<form id="searchForm">
										<input type="hidden" name="redirectPage" /> <input
											type="hidden" id="pageSize" name="pager.pageSize" value="10">
										<input type="hidden" id="activityMenu" name="activityMenu"
											value="${param.activityMenu}"> <input type="hidden"
											id="busType" name="busType" value="${todayWorks.busType}" />
										<input type="hidden" id="modTypes" name="modTypes"
											value="${todayWorks.busType}" /> <input type="hidden"
											id="sid" name="sid" value="${param.sid}" />
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group cond" id="moreCondition_Div">
														<a class="btn btn-default dropdown-toggle btn-xs"
															onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
																<c:when
																	test="${not empty todayWorks.startDate || not empty todayWorks.endDate}">
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
																<span class="btn-xs">起止时间：</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${todayWorks.startDate}" id="startDate"
																	name="startDate" placeholder="开始时间"
																	style="width: 100px"
																	onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																<span>~</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${todayWorks.endDate}" id="endDate"
																	name="endDate" placeholder="结束时间" style="width: 100px"
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
												<span class="input-icon"> <input id="content"
													name="content" value="${todayWorks.content}"
													class="form-control ps-input" type="text"
													placeholder="请输入关键字"> <a href="javascript:void(0)"
													class="ps-searchBtn"><i
														class="glyphicon glyphicon-search circular danger"></i> </a>
												</span>
											</div>
										</div>
									</form>
								</div>
								<c:choose>
						<c:when test="${not empty list}">
								<div class="widget-body">
									<form action="/clock/delClock" id="delForm">
										<input type="hidden" id="redirectPage" name="redirectPage" />
										<input type="hidden" name="sid" value="${param.sid}" />
										<table class="table table-striped table-hover">
											<thead>
												<tr role="row">
													<th>序号</th>
													<th>事项名称</th>
													<th>紧急度</th>
													<th>办理时限</th>
													<th>推送人</th>
													<th>推送时间</th>
												</tr>
											</thead>
											<tbody id="allTodoBody">
												<c:forEach items="${list}" var="obj" varStatus="vs">
													<tr>
														<td class="rowNum">${vs.count}</td>
														<td><span class="${obj.readState==0?'noread':'' }">
																<c:choose>
																	<c:when test="${obj.isClock=='1'}">
																	[闹铃]
																</c:when>
																	<c:when test="${obj.busType=='015'}">
																	[加入申请]
																</c:when>
																	<c:when test="${obj.busType=='017'}">
																	[会议确认]
																</c:when>
																	<c:when test="${obj.busType=='018'}">
																	[会议申请]
																</c:when>
																<c:when test="${obj.busType=='040'}">
																	[制度]
																</c:when>
																<c:when test="${obj.busType=='046'}">
																	[会议审批]
																</c:when>
																<c:when test="${obj.busType=='047'}">
																	[会议纪要审批]
																</c:when>
																<c:when test="${obj.busType=='099'}">
																	[催办]
																</c:when>
																<c:when test="${obj.busType=='0103'}">
																	[领款通知]
																</c:when>
																<c:when test="${obj.busType=='068'}">
																	[联系人]
																</c:when>
																<c:when test="${obj.busType=='06602'}">
																	[完成结算]
																</c:when>
																<c:when test="${obj.busType=='06601'}">
																	[财务结算]
																</c:when>
																<c:when test="${obj.busType=='067'}">
																	[变更审批]
																</c:when>
																<c:when test="${obj.busType=='02201'}">
																	[审批]
																</c:when>
																<c:when test="${obj.busType=='027010'}">
																	[用品采购审核]
																</c:when>
																<c:when test="${obj.busType=='027020'}">
																	[用品申领审核]
																</c:when>
																<c:when test="${obj.busType=='00306'}">
																	[任务报延]
																</c:when>
																<c:when test="${obj.busType=='1' or obj.busType=='100'}">
																	[分享]
																</c:when>
																<c:otherwise>
																[${fn:substring(obj.moduleTypeName,0,2)}]
																</c:otherwise>
																</c:choose>
														</span> 
															<c:choose>
																<c:when test="${obj.isClock=='1'}">
																	<a href="javascript:void(0)"
																		onclick="readMod(this,'allTodo',${obj.busId}, '${obj.busType}');viewTodoDetail(${obj.id},${obj.busId}, '${obj.busType}', '${obj.isClock}','${obj.clockId}','${param.sid}')"
																		class="${obj.readState==0?'noread':'' }"
																		todoId="${obj.id}" isClock="${obj.isClock}"
																		modId="${obj.busId}" modType="${obj.busType}"> 
																		<tags:cutString
																			num="57">
																			${fn:substring(obj.content,5,fn:length(obj.content))}
																		</tags:cutString>
																	</a>
																</c:when>
																<c:otherwise>
																	<a href="javascript:void(0)"
																		onclick="readMod(this,'allTodo',${obj.busId}, '${obj.busType}');viewTodoDetai(${obj.busId},'${obj.busType}',0,0,'${userInfo.id}','${param.sid}','${obj.id}')"
																		class="${obj.readState==0?'noread':'' }"
																		todoId="${obj.id}" isClock="${obj.isClock}"
																		modId="${obj.busId}" modType="${obj.busType}"> <tags:cutString
																			num="57">${obj.busTypeName}</tags:cutString>
																	</a>
																</c:otherwise>
															</c:choose></td>
														<td>
															<c:set var="gradeColor">
																<c:choose>
																	<c:when test="${obj.grade==4}">label-red</c:when>
																	<c:when test="${obj.grade==3}">label-orange</c:when>
																	<c:when test="${obj.grade==2}">label-darkpink</c:when>
																	<c:when test="${obj.grade==1}">label-green</c:when>
																	<c:otherwise>label-default</c:otherwise>
																</c:choose>
															</c:set>
															<span class="label ${gradeColor}">
																<c:choose>
																	<c:when test="${obj.grade>0 && obj.isClock==0}">
																		<tags:viewDataDic type="grade" code="${obj.grade}"></tags:viewDataDic>
																	</c:when>
																	<c:otherwise>/</c:otherwise>
																</c:choose>
															</span>
														</td>
														<td>${empty obj.dealTimeLimit?'/':obj.dealTimeLimit}</td>
														<td>
															<div class="ticket-user pull-left other-user-box">
																<img class="user-avatar userImg" userGender="${obj.gender}" 
																userImgUuid="${obj.modifyerUuid}" 
																userImgName="${obj.modifyerName}" title="${obj.modifyerName}" 
																src="/downLoad/userImg/${obj.comId}/${obj.modifyer}"/>
																<span class="user-name">${obj.modifyerName}</span>
															</div>
														</td>
														<td>${fn:substring(obj.recordCreateTime,0,20)}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/msgShare/toDoJobs/jobsCenter"></tags:pageBar>
								</div>
									</c:when>
						<c:otherwise>
							<!--与上一个searchForm不共存 -->
							<form id="searchForm">
								<input type="hidden" name="redirectPage" /> <input
									type="hidden" id="pageSize" name="pager.pageSize" value="10">
								<input type="hidden" id="activityMenu" name="activityMenu"
									value="${param.activityMenu}"> <input type="hidden"
									id="busType" name="busType" value="${todayWorks.busType}" /> <input
									type="hidden" id="modTypes" name="modTypes"
									value="${todayWorks.busType}" /> <input type="hidden" id="sid"
									name="sid" value="${param.sid}" />
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>当前没有需要您处理的事项！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
										</div>
									</section>
								</div>
							</form>
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
