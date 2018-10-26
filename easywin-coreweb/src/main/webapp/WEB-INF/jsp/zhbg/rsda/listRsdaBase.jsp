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
							<form action="/rsdaBase/listPagedRsdaBase" id="searchForm" class="subform">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" name="pager.pageSize" value="10">
								<input type="hidden" name="activityMenu" value="${param.activityMenu}">
								<input type="hidden" name="searchTab" value="${param.searchTab}">
								
								<input type="hidden" name="userName" value="${rsdaBase.userName}">
								<input type="hidden" name="userId" value="${rsdaBase.userId}">
								
								<input type="hidden" name="depName" value="${rsdaBase.depName}">
								<input type="hidden" name="depId" value="${rsdaBase.depId}">
								
								<input type="hidden" name="politStatusName" value="${rsdaBase.politStatusName}">
								<input type="hidden" name="politStatus" value="${rsdaBase.politStatus}">
								
								<div class="btn-group pull-left searchCond">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
												<c:choose>
													<c:when test="${not empty rsdaBase.userName}">
														<font style="font-weight:bold;">${rsdaBase.userName}</font>
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
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="流程状态筛选">
												<c:choose>
													<c:when test="${not empty rsdaBase.depName}">
														<font style="font-weight:bold;">${rsdaBase.depName}</font>
													</c:when>
													<c:otherwise>部门筛选</c:otherwise>
												</c:choose>
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)" class="clearThisElement" relateElement="depId" relateElementName="depName">不限条件</a>
												</li>
												<li><a href="javascript:void(0)" class="depOneElementSelect" relateElement="depId" relateElementName="depName">部门选择</a>
												</li>
											</ul>
										</div>
									</div>
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="政治面貌">
												<c:choose>
													<c:when test="${not empty rsdaBase.politStatusName}">
														<font style="font-weight:bold;">${rsdaBase.politStatusName}</font>
													</c:when>
													<c:otherwise>政治面貌</c:otherwise>
												</c:choose>
												<i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default dataDicClz" 
												dataDic="politStatus"
												relateElement="politStatus" 
												relateElementName="politStatusName">
											</ul>
										</div>
									</div>
									<div class="table-toolbar ps-margin">
										<div class="btn-group cond" id="moreCondition_Div">
											<a class="btn btn-default dropdown-toggle btn-xs"
												onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
													<c:when
														test="${not empty rsdaBase.startDate || not empty rsdaBase.endDate}">
														<font style="font-weight:bold;">筛选中</font>
													</c:when>
													<c:otherwise>
				                                            	更多
	                                            			</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
												<!-- 下属执行则没有状态选择 -->
												<div class="ps-margin ps-search padding-left-10">
													<span class="btn-xs">起止时间：</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${rsdaBase.startDate}"
														id="startDate" name="startDate" placeholder="开始时间"
														onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
													<span>~</span> <input
														class="form-control dpd2 no-padding condDate" type="text"
														readonly="readonly" value="${rsdaBase.endDate}" id="endDate"
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
													<a class="btn btn-default dropdown-toggle btn-xs batchDelData"> 批量删除 </a>
												</div>
											</div>
										</c:if>
									</div>
								</div>
								
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon"> 
									<input name="searchContent" value="${rsdaBase.searchContent}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键字">
										<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
								
							</form>
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty listRsdaBases}">
						<div class="widget-body">
								<table class="table table-striped table-hover">
									<thead>
										<tr role="row">
											<th>序号</th>
											<th>头像</th>
											<th>姓名</th>
											<th>性别</th>
											<th>部门</th>
											<th>入职时间</th>
											<th>手机</th>
											<th>政治面貌</th>
											<th>籍贯</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listRsdaBases}" var="rsdaBaseVo" varStatus="vs">
											<tr class="optTr">
												<td class="optTd" style="height: 47px">
													<label class="optRowNum"
														style="display: block;width: 20px">${vs.count}</label>
													</td>
												<td>
													<div class="ticket-user pull-left other-user-box" 
													data-container="body" data-placement="left">
														<img class="user-avatar userImg"  title="${rsdaBaseVo.userName}" 
															src="/downLoad/userImg/${rsdaBaseVo.comId}/${rsdaBaseVo.userId}"/>
														<i class="user-name"></i>
													</div>
												</td>
												<td>
													${rsdaBaseVo.userName }
												</td>
												<td>
													<c:choose>
														<c:when test="${rsdaBaseVo.gender eq 1}">男</c:when>
														<c:when test="${rsdaBaseVo.gender eq 0}">女</c:when>
														<c:otherwise>--</c:otherwise>
													</c:choose>
												</td>
												<td>
													${rsdaBaseVo.depName}
												</td>
												<td>
													${fn:substring(rsdaBaseVo.hireDate,0,10) }
												</td>
												<td>
													${rsdaBaseVo.userTel }
												</td>
												<td>
													${rsdaBaseVo.politStatusName}
												</td>
												<td>
													${rsdaBaseVo.nativeProName }
												</td>
												<td>
													<a href="javascript:void(0)" userId="${rsdaBaseVo.userId}" busId="${empty rsdaBaseVo.id?0:rsdaBaseVo.id }" class="updateRsdaBase">编辑</a>
													|
													<a href="javascript:void(0)" userId="${rsdaBaseVo.userId}" busId="${empty rsdaBaseVo.id?0:rsdaBaseVo.id }" class="viewRsdaBase">查看</a>
												</td>
												
											</tr>
										</c:forEach>
									</tbody>
								</table>
							<tags:pageBar url="/rsdaBase/listPagedRsdaBase"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>没有查询到人事档案记录！</h2>
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
