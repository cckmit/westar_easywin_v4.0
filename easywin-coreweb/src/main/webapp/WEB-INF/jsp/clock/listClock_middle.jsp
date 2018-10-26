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
								<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
									<div class="checkbox ps-margin pull-left">
										<label> <input type="checkbox" class="colored-blue"
											id="checkAllBox" onclick="checkAll(this,'ids')"> <span
											class="text" style="color: inherit;">全选</span>
										</label>
									</div>
									<form id="searchForm" action="/clock/listPagedClock">
										<input type="hidden" name="redirectPage" /> <input
											type="hidden" name="pager.pageSize" value="15" /> <input
											type="hidden" name="sid" value="${param.sid}" /> <input
											type="hidden" name="busType" id="busType"
											value="${clock.busType}" /> <input type="hidden"
											name="modTypes" id="modTypes" value="${clock.busType}" /> <input
											type="hidden" name="clockRepType" id="clockRepType"
											value="${clock.clockRepType}" />
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs"
															data-toggle="dropdown"> <c:choose>
																<c:when test="${clock.clockRepType=='0'}">
																	<font style="font-weight:bold;">单次</font>
																</c:when>
																<c:when test="${clock.clockRepType=='1'}">
																	<font style="font-weight:bold;">每天</font>
																</c:when>
																<c:when test="${clock.clockRepType=='2'}">
																	<font style="font-weight:bold;">每周</font>
																</c:when>
																<c:when test="${clock.clockRepType=='3'}">
																	<font style="font-weight:bold;">每月</font>
																</c:when>
																<c:when test="${clock.clockRepType=='4'}">
																	<font style="font-weight:bold;">每年</font>
																</c:when>
																<c:otherwise>全部类型</c:otherwise>
															</c:choose> <i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li><a href="javascript:void(0);" id="allRepType">不限条件</a></li>
															<li><a href="javascript:void(0);" id="once">单次</a></li>
															<li><a href="javascript:void(0);" id="day">每天</a></li>
															<li><a href="javascript:void(0);" id="week">每周</a></li>
															<li><a href="javascript:void(0);" id="month">每月</a></li>
															<li><a href="javascript:void(0);" id="year">每年</a></li>
														</ul>
													</div>
												</div>

												<div class="table-toolbar ps-margin">
													<div class="btn-group cond" id="moreCondition_Div">
														<a class="btn btn-default dropdown-toggle btn-xs"
															onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
																<c:when
																	test="${not empty clock.startDate || not empty clock.endDate}">
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
																<span class="btn-xs">执行时间：</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${clock.startDate}" id="startDate"
																	name="startDate" placeholder="开始时间"
																	onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																<span>~</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${clock.endDate}" id="endDate" name="endDate"
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
												<span class="input-icon"> <input id="content"
													name="content" value="${clock.content}"
													class="form-control ps-input" type="text"
													placeholder="请输入关键字"> <a href="javascript:void(0)"
													class="ps-searchBtn"><i
														class="glyphicon glyphicon-search circular danger"></i></a>
												</span>
											</div>
										</div>
										<div class="batchOpt" style="display: none">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs"
															onclick="delClock()"> 批量删除 </a>
													</div>
												</div>
											</div>
										</div>
									</form>
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-primary btn-xs" type="button"
											onclick="addClock(0,0,'${param.sid}')">
											<i class="fa fa-plus"></i> 添加闹铃
										</button>
									</div>
								</div>
								<c:choose>
								<c:when test="${not empty listClocks}">
								<div class="widget-body">
									<form action="/clock/delClock" id="delForm">
										<input type="hidden" id="redirectPage" name="redirectPage" />
										<input type="hidden" name="sid" value="${param.sid}" />
										<table class="table table-striped table-hover">
											<thead>
												<tr role="row">
													<th style="width: 20px">&nbsp;</th>
													<th>开始日期</th>
													<th>开始时间</th>
													<th>提醒类型</th>
													<th>提醒说明</th>
													<th>模块名称</th>
													<th>下次执行时间</th>
													<th>执行状态</th>
													<th>操作</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listClocks}" var="obj" varStatus="vs">
													<tr class="optTr">
														<td class="optTd" style="height: 47px"><label
															class="optCheckBox" style="display: none;width: 20px">
																<input class="colored-blue" type="checkbox" name="ids"
																value="${obj.id}" /> <span class="text"></span>
														</label> <label class="optRowNum"
															style="display: block;width: 20px">${vs.count}</label></td>
														<td>${obj.clockDate }</td>
														<td>${obj.clockTime }</td>
														<td><tags:viewDataDic type="clockType"
																code="${obj.clockRepType}"></tags:viewDataDic> <c:choose>
																<c:when test="${obj.clockRepType=='3'}">(${obj.clockRepDate}日)</c:when>
																<c:when test="${obj.clockRepType=='2'}">
																	<c:set var="tepDateLen">${fn:length(obj.clockRepDate)-1}</c:set>
																	<c:set var="repDate0">${fn:substring(obj.clockRepDate,0,tepDateLen)}</c:set>
																	<c:set var="repDate1"
																		value="${fn:replace(repDate0,'1', '日')}" />
																	<c:set var="repDate2"
																		value="${fn:replace(repDate1,'2', '一')}" />
																	<c:set var="repDate3"
																		value="${fn:replace(repDate2,'3', '二')}" />
																	<c:set var="repDate4"
																		value="${fn:replace(repDate3,'4', '三')}" />
																	<c:set var="repDate5"
																		value="${fn:replace(repDate4,'5', '四')}" />
																	<c:set var="repDate6"
																		value="${fn:replace(repDate5,'6', '五')}" />
																	<c:set var="repDate7"
																		value="${fn:replace(repDate6,'7', '六')}" />
									 						(${repDate7})
									 						</c:when>
															</c:choose></td>
														<td><tags:viewTextArea>${obj.content}</tags:viewTextArea></td>
														<td><c:choose>
																<c:when test="${obj.busType==0 && obj.clockMsgType==1}">
									 							[普通待办]--
									 						</c:when>
																<c:when test="${obj.busType==0 && obj.clockMsgType==0}">
									 							[普通提醒]--
									 						</c:when>
																<c:otherwise>
																	<c:set var="clockMsgType">
																		<c:choose>
																			<c:when test="${obj.clockMsgType==1}">待办</c:when>
																			<c:otherwise>提醒 </c:otherwise>
																		</c:choose>
																	</c:set>
									 						[<c:choose>
																		<c:when test="${obj.busId==0 && obj.busType==0}">
										 							普通
										 						</c:when>
																		<c:otherwise>
																			<c:choose>
																				<c:when test="${obj.busType=='012'}">客户${clockMsgType}</c:when>
																				<c:when test="${obj.busType=='005'}">项目${clockMsgType}</c:when>
																				<c:when test="${obj.busType=='003'}">任务${clockMsgType}</c:when>
																			</c:choose>
																		</c:otherwise>
																	</c:choose>]
									 							<a href="javascript:void(0);"
																		onclick="viewDetailHead(${obj.busId},'${obj.busType}',${obj.id},0,0)">
																		<tags:viewTextArea>${obj.entyName}</tags:viewTextArea>
																	</a>
																</c:otherwise>
															</c:choose></td>
														<td><c:choose>
																<c:when test="${obj.clockState==1}">
							 									--
							 								</c:when>
																<c:otherwise>
										 						${fn:substring(obj.clockNextDate,0,10) }
							 								</c:otherwise>
															</c:choose></td>
														<c:set var="color">
															<c:choose>
																<c:when test="${obj.executeState==0}">
																	<c:choose>
																		<c:when test="${obj.clockState==1}">
									 									#FF0000
									 								</c:when>
																		<c:otherwise>
												 						#008000
									 								</c:otherwise>
																	</c:choose>
																</c:when>
																<c:when test="${obj.executeState==1}">#00bfff</c:when>
															</c:choose>
														</c:set>
														<td style="color: ${color}"><c:choose>
																<c:when test="${obj.executeState==0}">
																	<c:choose>
																		<c:when test="${obj.clockState==1}">
									 									已过期
									 								</c:when>
																		<c:otherwise>
												 						未执行
									 								</c:otherwise>
																	</c:choose>
																</c:when>
																<c:when test="${obj.executeState==1}">待执行</c:when>
																<c:when test="${obj.executeState==2}">已执行</c:when>
															</c:choose></td>
														<td><a href="javascript:void(0)"
															onclick="editClock(${obj.busId},'${obj.busType}',${obj.id},'${param.sid}')">修改</a>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/clock/listPagedClock"></tags:pageBar>
								</div>
								</c:when>
							<c:otherwise>
								<form id="searchForm" action="/clock/listPagedClock">
									<input type="hidden" name="redirectPage" /> <input type="hidden"
										name="pager.pageSize" value="15" /> <input type="hidden"
										name="sid" value="${param.sid}" /> <input type="hidden"
										name="busType" id="busType" value="${clock.busType}" /> <input
										type="hidden" name="modTypes" id="modTypes"
										value="${clock.busType}" /> <input type="hidden"
										name="clockRepType" id="clockRepType"
										value="${clock.clockRepType}" />
									<div class="widget-body" style="height:515px; text-align:center;padding-top:155px">
										<section class="error-container text-center">
											<h1>
												<i class="fa fa-exclamation-triangle"></i>
											</h1>
											<div class="error-divider">
												<h2>您还没有设置相关闹铃！</h2>
												<p class="description">协同提高效率，分享拉近距离。</p>
												<a href="javascript:void(0);"
													onclick="addClock(0,0,'${param.sid}');" class="return-btn"><i
													class="fa fa-plus"></i>添加闹铃</a>
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

