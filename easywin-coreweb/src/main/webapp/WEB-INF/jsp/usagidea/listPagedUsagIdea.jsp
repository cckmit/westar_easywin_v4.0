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
<!-- Page Content -->
<div class="page-content">
	<!-- Page Body -->
	<div class="page-body">
		
		<div class="row">
			<div class="col-md-12 col-xs-12 ">
				<div class="widget">
					<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
						<div class="checkbox ps-margin pull-left">
							<label>
								<input type="checkbox" class="colored-blue" id="checkAllBox"
									onclick="checkAll(this,'ids')">
								<span class="text" style="color: inherit;">全选</span>
							</label>
						</div>
                             	<div class="table-toolbar ps-margin">
							<button class="btn btn-info btn-primary btn-xs" type="button" onclick="add('${param.sid}');">
								<i class="fa fa-plus"></i>新增
							</button>
						</div>
						<div class="table-toolbar ps-margin">
                                 	<div class="btn-group">
								<a class="btn btn-default dropdown-toggle btn-xs" onclick="del('${param.sid}')">
									删除
								</a>
                                 	</div>
                             	</div>
					</div>
					<c:choose>
			<c:when test="${not empty list}">
					<div class="widget-body">
						<form action="/usagIdea/delUsagIdea" id="delForm">
						    <tags:token></tags:token>
							<input type="hidden" name="redirectPage" id="redirectPage"/>
							<input type="hidden" name="sid" value="${param.sid}"/>
							<input type="hidden" name="pager.pageSize" value="12">
							<input type="hidden" id="activityMenu" name="activityMenu" value="${param.activityMenu}">
							<table class="table table-striped table-hover">
								<thead>
									<tr role="row">
										<th width="8%">序号</th>
										<th>常用意见</th>
										<th width="10%">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="obj" varStatus="vs">
										<tr class="optTr">
											<td class="optTd" style="height: 47px">
												<label class="optCheckBox" style="display: none;width: 20px">
								 					<input class="colored-blue" type="checkbox" name="ids" value="${obj.id}"/>
								 					<span class="text"></span>
									 			</label>
                                               	<label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
											</td>
											<td>
												${ obj.idea}
											</td>
											<td>
												<a href="javascript:void(0)" onclick="update(${obj.id},'${param.sid}')">修改</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</form>
						<tags:pageBar url="/usagIdea/listPagedUsagIdea"></tags:pageBar>
					</div>
					</c:when>
		<c:otherwise>
			<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
				<section class="error-container text-center">
					<h1>
						<i class="fa fa-exclamation-triangle"></i>
					</h1>
					<div class="error-divider">
						<h2>您还没有添加过常用意见！</h2>
						<p class="description">协同提高效率，分享拉近距离。</p>
						<a href="javascript:void(0);" onclick="add('${param.sid}');"
							class="return-btn"><i class="fa fa-plus"></i>新增常用意见</a>
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

