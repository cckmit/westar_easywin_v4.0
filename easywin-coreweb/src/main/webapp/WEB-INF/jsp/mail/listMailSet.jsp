<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<div class="page-content">
<!-- Page Body -->
<div class="page-body">
	
	<div class="row">
		<div class="col-md-12 col-xs-12 ">
			<div class="widget">
				<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                    <div class="table-toolbar ps-margin">
						<button class="btn btn-info btn-primary btn-xs" type="button" onclick="add('${param.sid}');">
							<i class="fa fa-plus"></i>添加
						</button>
					</div>
				</div>
				<c:choose>
		<c:when test="${not empty list}">
				<div class="widget-body">
					<form action="/mailSet/delMailSet" id="delForm">
						<input type="hidden" name="redirectPage" id="redirectPage"/>
						<input type="hidden" name="sid" value="${param.sid}"/>
						<input type="hidden" name="pager.pageSize" value="12">
						<input type="hidden" id="activityMenu" name="activityMenu" value="${param.activityMenu}">
						<table class="table table-striped table-hover">
							<thead>
								<tr role="row">
									<th width="5%" valign="middle">序号</th>
									<th width="17.5%" valign="middle">电子邮件地址</th>
									<th width="15%" valign="middle">发送服务器(SMTP)</th>
									<th width="10%" valign="middle">SMTP端口</th>
									<th width="15%" valign="middle">发送服务器(IMAP)</th>
									<th width="10%" valign="middle">IMAP端口</th>
									<th width="17.5%" valign="middle">登录帐户</th>
									<th width="10%" valign="middle">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list}" var="mailSet" varStatus="vs">
									<tr>
										<td>
                                              	<label style="display: block;width: 20px">${vs.count}</label>
										</td>
						 				<td valign="middle">${mailSet.email}</td>
						 				<td valign="middle">${mailSet.serverHost }</td>
						 				<td valign="middle">${mailSet.serverPort}</td>
						 				<td valign="middle">${mailSet.serverImapHost }</td>
						 				<td valign="middle">${mailSet.serverImapPort}</td>
						 				<td valign="middle">${mailSet.account}</td>
						 				<td valign="middle">
						 					<span>
						 						<a href="javascript:void(0)" onclick="update(${mailSet.id},'${param.sid}')">修改</a>
						 					</span>
						 					|
						 					<span>
						 						<a href="javascript:void(0)" onclick="del(${mailSet.id},'${param.sid}')">删除</a>
						 					</span>
						 				</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
					<tags:pageBar url="/mailSet/listPagedMailSet"></tags:pageBar>
				</div>
				</c:when>
	<c:otherwise>
	
		<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
			<section class="error-container text-center">
				<h1>
					<i class="fa fa-exclamation-triangle"></i>
				</h1>
				<div class="error-divider">
					<h2>您还没有你的邮件通讯配置！</h2>
					<p class="description">协同提高效率，分享拉近距离。</p>
					<a href="javascript:void(0);" onclick="add('${param.sid}');"
						class="return-btn"><i class="fa fa-plus"></i>添加配置</a>
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
