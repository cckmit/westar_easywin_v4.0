<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function orderByDate(){
			if(!$("#orderBy").val() || $("#orderBy").val() =='desc'){
				$("#orderBy").val('asc');
			}else{
				$("#orderBy").val('desc');
			}
			$("#searchForm").submit();
		}
	</script>	
<body>
	<div class="page-content">
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
							<div>
								<form action="/attence/listAttenceRecordOfSelf" id="searchForm" class="subform">
									<input type="hidden" name="sid" value="${param.sid}">
									<input type="hidden" name="activityMenu" value="${param.activityMenu}">
									<input type="hidden" name="searchTab" value="${param.searchTab}">
									<input type="hidden" id = "orderBy" name="orderBy" value="${attenceRecord.orderBy}">
									<div class="btn-group pull-left searchCond">
									<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" onclick ="orderByDate()">
												<c:choose>
													<c:when test="${attenceRecord.orderBy eq 'asc' }">时间升序</c:when>
													<c:otherwise>时间降序</c:otherwise>
												</c:choose>
												<i class="fa fa-angle-down"></i>
												</a>
											</div>
										</div>
										
										<div class="ps-margin ps-search searchCond padding-right-10">
											<span class="pull-left padding-top-5">查询日期：</span>
											<input class="form-control dpd2  Wdate" type="text" style="width:120px;height:30px" readonly="readonly" value="${attenceRecord.startDate}" id="startDate" name="startDate" placeholder="查询日期"
												onFocus="WdatePicker({onpicked:function(){submit();},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
										</div>
										<div class="ps-margin ps-search searchCond padding-right-20">
											<span class="pull-left padding-top-5 padding-right-5">至</span>
											<input class="form-control dpd2  Wdate" type="text" style="width:120px;height:30px" readonly="readonly" value="${attenceRecord.endDate}" id="endDate" name="endDate" placeholder="查询日期"
												onFocus="WdatePicker({onpicked:function(){submit();},dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startDate\',{d:+0});}',maxDate:'${nowDate}'})" />
										</div>
									</div>
								</form>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty listAttenceRecord}">
								<div class="widget-body">
									<table class="table table-bordered" id="editabledatatable">
										<thead>
											<tr role="row">
												<th class="text-center">序号</th>
												<th class="text-center">编号</th>
												<th class="text-center">考勤日期</th>
												<th class="text-center">签到时间</th>
												<th class="text-center">签退时间</th>
												<th class="text-center">状态</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listAttenceRecord}" var="obj" varStatus="vs">
												<tr class="optTr">
													<td class="text-center">${vs.count}</td>
													<td class="text-center">${obj.enrollNumber}</td>
													<td class="text-center">${obj.searchDate}</td>
													<c:choose>
														<c:when test="${not empty obj}">
															<c:choose>
																<c:when test="${obj.attenceType eq 0 }">
																	<c:choose>
																		<c:when test="${obj.recordType eq 0 }">
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<td class="text-center" style="color:red">旷工</td>
																		</c:when>
																		<c:when test="${obj.recordType eq 2 }">
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<!-- 请假 -->
																			<td class="text-center" style="color:#A0522D">请假</td>
																		</c:when>
																		<c:when test="${obj.recordType eq 3 }">
																			<td class="text-center" style="color:red">未签到</td>
																			<td class="text-center" style="color:red">未签退</td>
																			<!-- 出差-->
																			<td class="text-center" style="color:#68228B">出差</td>
																		</c:when>
																		<c:when test="${obj.recordType eq 4 }">
																			<!-- 请假-->
																			<td class="text-center"></td>
																			<td class="text-center"></td>
																			<td class="text-center" style="color:#00C5CD">休假</td>
																		</c:when>
																			<c:when test="${obj.recordType eq 5 }">
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
																		<c:when test="${not empty obj.dayTimeS}">
																			<td class="text-center">${obj.dayTimeS}</td>
																		</c:when>
																		<c:otherwise>
																			<td class="text-center" style="color:red">未签到</td>
																		</c:otherwise>
																	</c:choose>
																	<c:choose>
																		<c:when test="${not empty obj.dayTimeE}">
																			<td class="text-center">${obj.dayTimeE}</td>
																		</c:when>
																		<c:otherwise>
																			<td class="text-center" style="color:red">未签退</td>
																		</c:otherwise>
																	</c:choose>
																	<c:choose>
																		<c:when test="${obj.recordType eq 1}">
																			<td class="text-center" style="color:#9A32CD">加班</td>
																		</c:when>
																		<c:when test="${obj.unusualType eq 1}">
																			<td style="color:red" class="text-center">异常</td>
																		</c:when>
																		<c:when test="${obj.unusualType eq 2}">
																			<td style="color:red" class="text-center">迟到</td>
																		</c:when>
																		<c:when test="${obj.unusualType eq 3}">
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
									<tags:pageBar url="/attence/listAttenceRecordOfSelf"></tags:pageBar>
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
