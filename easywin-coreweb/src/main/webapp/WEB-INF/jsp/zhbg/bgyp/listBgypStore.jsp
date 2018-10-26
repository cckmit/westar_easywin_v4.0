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
							<form action="/bgypItem/listPagedBgypStore" id="searchForm" class="subform">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" name="pager.pageSize" value="10">
								<input type="hidden" name="activityMenu" value="${param.activityMenu }">
								<input type="hidden" name="searchTab" value="${param.searchTab }">
								
								<input type="hidden" name="flId" value="${bgypItem.flId}">
								<input type="hidden" name="flName" value="${bgypItem.flName}">
								
								<div class="btn-group pull-left searchCond">
									<div class="table-toolbar ps-margin">
										<div class="btn-group">
											<a class="btn btn-default dropdown-toggle btn-xs"
												data-toggle="dropdown">
												<c:choose>
													<c:when test="${not empty bgypItem.flName}">
														<font style="font-weight:bold;">${bgypItem.flName}</font>
													</c:when>
													<c:otherwise>类别筛选</c:otherwise>
												</c:choose> <i class="fa fa-angle-down"></i>
											</a>
											<ul class="dropdown-menu dropdown-default">
												<li><a href="javascript:void(0)" class="clearThisElement" relateElement="flId" relateElementName="flName">不限条件</a>
												</li>
												<li><a href="javascript:void(0)" class="bgflOneElementSelect" relateElement="flId" relateElementName="flName">类别选择</a>
												</li>
											</ul>
										</div>
									</div>
									
									
								</div>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon"> 
									<input name="bgypName" value="${bgypItem.bgypName}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键字">
										<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
								
							</form>
							
							</div>
						</div>
						
						<c:choose>
						<c:when test="${not empty listBgypItem}">
						<div class="widget-body">
							<form  method="post"
								id="delForm">
								<input type="hidden" id="redirectPage" name="redirectPage" />
								<table class="table table-striped table-hover" id="editabledatatable">
									<thead>
										<tr role="row">
											<th>序号</th>
											<th>办公用品类别</th>
											<th>用品名称</th>
											<th>库存量</th>
											<th>单位</th>
											<th>规格</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listBgypItem}" var="storeVo" varStatus="vs">
											<tr class="optTr">
												<td class="optTd" style="height: 47px">
													<label 
														style="display: block;width: 20px">${vs.count}</label>
													</td>
												<td>
													${storeVo.flName }
												</td>
												<td>
													[${storeVo.bgypCode}]${storeVo.bgypName}
												</td>
												<td>
													${storeVo.storeNum}
												</td>
												<td>
													${storeVo.bgypUnitName }
												</td>
												<td>
													${empty storeVo.bgypSpec?'/':storeVo.bgypSpec}
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
							<tags:pageBar url="/bgypItem/listPagedBgypStore"></tags:pageBar>
						</div>
						</c:when>
						<c:otherwise>
							<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
								<section class="error-container text-center">
									<h1>
										<i class="fa fa-exclamation-triangle"></i>
									</h1>
									<div class="error-divider">
										<h2>未查询到用品库存信息！</h2>
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
