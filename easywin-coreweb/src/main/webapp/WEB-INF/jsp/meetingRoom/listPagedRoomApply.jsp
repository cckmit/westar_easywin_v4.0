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
<head>
</head>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<c:choose>
				<c:when test="${not empty listRoomApply}">
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
									<form id="searchForm" action="/meetingRoom/listPagedRoomApply">
										<input type="hidden" name="searchTab" id="searchTab"
											value="${param.searchTab}"> <input type="hidden"
											name="sid" value="${param.sid}" />

										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left"></div>
										</div>
										<div class="batchOpt" style="display: none">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs"
															id="delRoomApply"> 撤销申请 </a>
													</div>
												</div>
											</div>
										</div>
									</form>
								</div>
								<div class="widget-body">
									<form id="delForm" action="/meetingRoom/delMeetingRoom">
										<input type="hidden" name="redirectPage"> <input
											type="hidden" name="sid" value="${param.sid }">
										<table class="table table-striped table-hover">
											<thead>
												<tr role="row">
													<th width="10%" valign="middle">序号</th>
													<th width="15%" valign="middle" class="hidden-phone">会议室名称</th>
													<th width="8%" valign="middle">管理员</th>
													<th width="12%" valign="middle">申请时间</th>
													<th width="15%" valign="middle">会议名称</th>
													<th width="12%" valign="middle">开始时间</th>
													<th width="12%" valign="middle">结束时间</th>
													<th width="10%" valign="middle">审核结果</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${listRoomApply}" var="obj" varStatus="vs">
													<tr class="optTr">
														<td class="optTd" style="height: 47px"><label
															class="optCheckBox" style="display: none;width: 20px">
																<input class="colored-blue" type="checkbox" name="ids"
																value="${obj.id}" state='${obj.state }' /> <span
																class="text"></span>
														</label> <label class="optRowNum"
															style="display: block;width: 20px">${vs.count}</label></td>
														<td valign="middle">${obj.roomName}</td>
														<td valign="middle">${obj.managerName}</td>
														<td valign="middle">
															${fn:substring(obj.recordCreateTime,0,16)}</td>
														<td valign="middle">${obj.meetTitle}</td>
														<td valign="middle">${obj.startDate}</td>
														<td valign="middle">${obj.endDate}</td>
														<td><c:choose>
																<c:when test="${obj.state==0}">待审核</c:when>
																<c:when test="${obj.state==1}">审核通过</c:when>
																<c:when test="${obj.state==2}">未通过</c:when>
															</c:choose></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</form>
									<tags:pageBar url="/meetingRoom/listPagedRoomApply"></tags:pageBar>
								</div>
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="container"
						style="left:50%;top:50%;position: absolute;
				margin:-90px 0 0 -180px;padding-top:200px;text-align:center;width:488px;">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>暂无相关会议室使用申请！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
							</div>
						</section>
					</div>
				</c:otherwise>
			</c:choose>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->

	</div>
	<!-- /Page Container -->
	<!-- Main Container -->
</body>
</html>
