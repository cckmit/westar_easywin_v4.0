<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<div class="page-content">
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form action="/attence/listAttenceStateByDay" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="pager.pageSize" value="15">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="searchTab" value="${param.searchTab}">
									<div class="btn-group pull-left searchCond">

										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">人员筛选<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearMoreElement" relateList="creator_select">不限条件</a>
													</li>
													<li>
														<a href="javascript:void(0)" class="userMoreElementSelect" relateList="creator_select">人员选择</a>
													</li>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listCreator" listkey="id" listvalue="userName" id="creator_select" name="listCreator.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${attenceRecord.listCreator }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.userName }</option>
												</c:forEach>
											</select>
										</div>

										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">部门筛选<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li>
														<a href="javascript:void(0)" class="clearMoreElement" relateList="dep_select">不限条件</a>
													<li>
														<a href="javascript:void(0)" class="depMoreElementSelect" relateList="dep_select">部门选择</a>
												</ul>
											</div>
										</div>
										<div style="float: left;width: 250px;display: none">
											<select list="listDep" listkey="id" listvalue="depName" id="dep_select" name="listDep.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
												<c:forEach items="${attenceRecord.listDep }" var="obj" varStatus="vs">
													<option selected="selected" value="${obj.id }">${obj.depName }</option>
												</c:forEach>
											</select>
										</div>
											<div class="ps-margin ps-search searchCond padding-right-20">
											<span class="pull-left padding-top-5">考勤日期：</span><input class="form-control dpd2  Wdate" type="text" style="width:120px;height:30px" readonly="readonly" value="${attenceRecord.searchDate}" id="searchDate" name="searchDate"
												placeholder="查询日期" onFocus="WdatePicker({onpicked:function(){getSubmit();},dateFmt:'yyyy-MM-dd',maxDate: '${nowDate}'})" />
										</div>
									</div>
								</form>

								<div class="widget-buttons ps-widget-buttons">
									<%-- <c:if test="${isForceIn }">
										<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addAttence('${param.sid}');">同步考勤记录</button>
										<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addAttenceUser('${param.sid}');">同步考勤人员</button>
									</c:if> --%>
								</div>
								<div class="padding-top-10 text-left " style="display:${empty attenceRecord.listCreator ? 'none':'block'}">
									<strong>考勤人筛选:</strong>
									<c:forEach items="${attenceRecord.listCreator }" var="obj" varStatus="vs">
										<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('creator','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
									</c:forEach>
								</div>

								<div class=" padding-top-10 text-left " style="display:${empty attenceRecord.listDep ? 'none':'block'}">
									<strong>考勤部门筛选:</strong>
									<c:forEach items="${attenceRecord.listDep }" var="obj" varStatus="vs">
										<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('dep','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.depName }</span>
									</c:forEach>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty listAttence}">
								<div class="widget-body">
									<table class="table table-bordered" id="editabledatatable">
										<thead>
											<tr role="row">
												<th class="text-center">序号</th>
												<th class="text-center">所属部门</th>
												<th class="text-center">人员</th>
												<th class="text-center">编号</th>
												<th class="text-center">考勤日期</th>
												<th class="text-center">签到时间</th>
												<th class="text-center">签退时间</th>
												<th class="text-center">状态</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listAttence}" var="obj" varStatus="vs">
												<tr class="optTr">
													<td class="text-center">${vs.count}</td>
													<td class="text-center">${obj.depName}</td>
													<td class="text-center">${obj.userName}</td>
													<c:choose>
														<c:when test="${not empty obj.enrollNumber && obj.enrollNumber ne 0}">
															<td class="text-center">${obj.enrollNumber}</td>
															<td class="text-center">${attenceRecord.searchDate}</td>
														</c:when>
														<c:otherwise>
															<td colspan="5" class="text-center">
																<span style="color:red;">无编号！</span>
															</td>
														</c:otherwise>
													</c:choose>

													<c:choose>
														<c:when test="${not empty obj.attenceRecord}">
															<c:choose>
																<c:when test="${obj.attenceRecord.attenceType eq 0 }">

																	<c:choose>
																		<c:when test="${obj.attenceRecord.recordType eq 0 }">
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<td class="text-center" style="color:red">旷工</td>
																		</c:when>
																		<c:when test="${obj.attenceRecord.recordType eq 2 }">
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<!-- 请假 -->
																			<td class="text-center" style="color:#A0522D">请假</td>
																		</c:when>
																		<c:when test="${obj.attenceRecord.recordType eq 3 }">
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<!-- 出差-->
																			<td class="text-center" style="color:#68228B">出差</td>
																		</c:when>
																		<c:when test="${obj.attenceRecord.recordType eq 4 }">
																			<!-- 请假-->
																			<td class="text-center"></td>
																			<td class="text-center"></td>
																			<td class="text-center" style="color:green">休假</td>
																		</c:when>
																		<c:when test="${obj.attenceRecord.recordType eq 5 }">
																			<!-- 没打卡-->
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<td class="text-center" style="color:red">异常</td>
																		</c:when>
																		<c:otherwise>
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<td class="text-center" style="color:red">未同步</td>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${not empty obj.attenceRecord.dayTimeS}">
																			<td class="text-center">${obj.attenceRecord.dayTimeS}</td>
																		</c:when>
																		<c:otherwise>
																			<td class="text-center" style="color:red">未签到</td>
																		</c:otherwise>
																	</c:choose>
																	<c:choose>
																		<c:when test="${not empty obj.attenceRecord.dayTimeE}">
																			<td class="text-center">${obj.attenceRecord.dayTimeE}</td>
																		</c:when>
																		<c:otherwise>
																			<td class="text-center" style="color:red">未签退</td>
																		</c:otherwise>
																	</c:choose>
																	<c:choose>
																		<c:when test="${obj.attenceRecord.recordType eq 1}">
																			<td class="text-center" style="color:#9A32CD">加班</td>
																		</c:when>
																		<c:when test="${obj.attenceRecord.unusualType eq 1}">
																			<td style="color:red" class="text-center">异常</td>
																		</c:when>
																		<c:when test="${obj.attenceRecord.unusualType eq 2}">
																			<td style="color:red" class="text-center">迟到</td>
																		</c:when>
																		<c:when test="${obj.attenceRecord.unusualType eq 3}">
																			<td style="color:red" class="text-center">早退</td>
																		</c:when>
																		<c:otherwise>
																			<td class="text-center" style="color:green">正常</td>
																		</c:otherwise>
																	</c:choose>
																</c:otherwise>
															</c:choose>

														</c:when>
														<c:otherwise>

														</c:otherwise>
													</c:choose>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<tags:pageBar url="/attence/listAttenceStateByDay"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>没有相关数据！</h2>
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
</body>
</html>
