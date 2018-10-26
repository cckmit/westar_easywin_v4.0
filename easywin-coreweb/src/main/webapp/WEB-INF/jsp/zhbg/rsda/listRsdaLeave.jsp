<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						
						<div>
							<form action="/rsdaLeave/listPagedRsdaLeave" id="searchForm" class="subform">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" name="pager.pageSize" value="10">
								<input type="hidden" name="activityMenu" value="${param.activityMenu}">
								<input type="hidden" name="searchTab" value="${param.searchTab}">
								
								<input type="hidden" name="userId" value="${rsdaLeave.userId}">
								<input type="hidden" name="userName" value="${rsdaLeave.userName }">
								<input type="hidden" name="leaveType" value="${rsdaLeave.leaveType }">
								
								<div class="btn-group pull-left searchCond">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
												<c:choose>
													<c:when test="${not empty rsdaLeave.userName}">
														<font style="font-weight:bold;">${rsdaLeave.userName}</font>
													</c:when>
													<c:otherwise>人员筛选</c:otherwise>
												</c:choose>
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)" class="clearThisElement" relateElement="userId" relateElementName="userName">不限条件</a>
												</li>
												<li><a href="javascript:void(0)" class="userOneElementSelect" relateElement="userId" relateElementName="userName">人员选择</a>
												</li>
											</ul>
										</div>
									</div>
									<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="离职类型筛选">
													<c:choose>
														<c:when test="${not empty rsdaLeave.leaveTypeName}">
															<font style="font-weight:bold;">${rsdaLeave.leaveTypeName}</font>
														</c:when>
														<c:otherwise>离职类型</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default dataDicClz" 
													dataDic="leaveType"
													relateElement="leaveType">
												</ul>
											</div>
										</div>
									<div class="table-toolbar ps-margin">
										<div class="btn-group cond" id="moreCondition_Div">
											<a class="btn btn-default dropdown-toggle btn-xs"
												onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
													<c:when
														test="${not empty rsdaLeave.startDate || not empty rsdaLeave.endDate}">
														<font style="font-weight:bold;">筛选中</font>
													</c:when>
													<c:otherwise>更多 </c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
												<!-- 下属执行则没有状态选择 -->
												<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">起止时间：</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${rsdaLeave.startDate}"
														id="startDate" name="startDate" placeholder="开始时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
													<span>~</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${rsdaLeave.endDate}" id="endDate"
														name="endDate" placeholder="结束时间"
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
								
								
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<c:if test="${empty delete}">
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs batchDelRsdaTranceData">批量删除 </a>
												</div>
											</div>
										</c:if>
									</div>
								</div>
								
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon"> 
									<input name="leaveReason" value="${rsdaTrance.leaveReason}" class="form-control ps-input formElementSearch" type="text" placeholder="模糊查询离职原因">
										<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
								
							</form>
							<c:if test="${empty add}">
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" id="addRsdaLeaveBtn">
									 <i class="fa fa-plus"></i>添加离职信息
									</button>
								</div>
							</c:if>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty listRsdaLeaves}">
						<div class="widget-body">
							<form  method="post" id="delForm" action="/rsdaLeave/deleteRsdaLeave">
								<input type="hidden" name="sid" value="${param.sid }" />
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover">
									<thead>
										<tr role="row">
											<th><label> <input type="checkbox"
													class="colored-blue" id="checkAllBox"
													onclick="checkAll(this,'ids')"> <span class="text"
													style="color: inherit;"></span>
											</label></th>
											<th>人员</th>
											<th>职务</th>
											<th>离职类型</th>
											<th>离职时间</th>
											<th>离职原因</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listRsdaLeaves}" var="rsdaLeaveVo" varStatus="vs">
											<tr class="optTr">
												<td class="optTd" style="height: 47px">
													<label class="optCheckBox" style="display: none;width: 20px">
															<input class="colored-blue" type="checkbox" name="ids"
															 value="${rsdaLeaveVo.id}"/> 
															<span class="text"></span>
													</label> 
													<label class="optRowNum"
														style="display: block;width: 20px">${vs.count}</label>
													</td>
												<td>
													<div class="ticket-user pull-left other-user-box" 
													data-container="body" data-placement="left">
														<img class="user-avatar userImg" title="${rsdaLeaveVo.userName}" 
															src="/downLoad/userImg/${rsdaLeaveVo.comId }/${rsdaLeaveVo.userId}"/>
															<i class="user-name">${rsdaLeaveVo.userName}</i>
													</div>
												</td>
												<td>
													${rsdaLeaveVo.userDuty}
												</td>
												<td>
													${rsdaLeaveVo.leaveTypeName}
												</td>
												<td>
													${rsdaLeaveVo.leaveDate}
												</td>
												<td>
													${rsdaLeaveVo.leaveReason}
												</td>
												<td>
														<a href="javascript:void(0)" busId="${rsdaLeaveVo.id}" class="updateRsdaLeave">编辑</a>
														|
														<a href="javascript:void(0)" busId="${rsdaLeaveVo.id}" class="viewRsdaLeave">查看</a>
												</td>
												
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/rsdaLeave/listPagedRsdaLeave"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>没有查询到离职记录数据！</h2>
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
</body>
</html>
