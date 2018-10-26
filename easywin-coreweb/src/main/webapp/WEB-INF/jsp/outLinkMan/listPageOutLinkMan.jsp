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
								<label> <input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')"> <span class="text" style="color: inherit;">全选</span>
								</label>
							</div>
							<div>
								<form action="/outLinkMan/listOutLinkMan" id="searchForm" class="subform">
								
								<input type="hidden" name="pager.pageSize" value="10">
								<input type="hidden" name="activityMenu" value="${param.activityMenu }">
								<input type="hidden" name="searchTab" value="${param.searchTab}">
								<input type="hidden" name="sid" value="${param.sid}"> 
								<input type="hidden" id="pubState" name="pubState" value="${outLinkMan.pubState}">
								<div class="btn-group pull-left searchCond">
								
								
								</div>
								<div class="btn-group pull-left searchCond">
									<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown" title="公有或私有">
													<c:choose>
														<c:when test="${not empty outLinkMan.pubState}">
															<font style="font-weight:bold;">
																<c:choose>
																	<c:when test="${outLinkMan.pubState eq 0}">私有</c:when>
																	<c:when test="${outLinkMan.pubState eq 1}">公开</c:when>
																</c:choose>
															</font>
														</c:when>
														<c:otherwise>阅读权限</c:otherwise>
													</c:choose>
													<i class="fa fa-angle-down"></i>
												</a>
												<ul class="dropdown-menu dropdown-default">
													<li><a href="javascript:void(0)" class="clearThisElement" relateElement="pubState" onclick="choosePubState(this,'')">不限条件</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="pubState" dataValue="0" onclick="choosePubState(this,0)">私有</a>
													</li>
													<li><a href="javascript:void(0)" class="setElementValue" relateElement="pubState" dataValue="1" onclick="choosePubState(this,1)">公开</a>
													</li>
												</ul>
											</div>
										</div>
								
								</div>
								<div class="ps-margin ps-search searchCond">
									<span class="input-icon">
										<input name="linkManName" id="linkManName" value="${outLinkMan.linkManName}" class="form-control ps-input formElementSearch" type="text" placeholder="请输入关键字" >
										<a href="#" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i> </a>
									</span>
								</div>
								<div class="batchOpt" style="display: none">
									<div class="btn-group pull-left">
										<div class="table-toolbar ps-margin">
											<div class="btn-group">
												<a class="btn btn-default dropdown-toggle btn-xs" onclick="delOlms()"> 批量删除 </a>
											</div>
										</div>
									</div>
								</div>
								</form>
								
								<div class="widget-buttons ps-widget-buttons">
									<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addOutLinMan('${param.sid}');">
										<i class="fa fa-plus"></i> 外部联系人
									</button>
								</div>
					
							</div>
						</div>

						<c:choose>
							<c:when test="${not empty list}">
								<div class="widget-body">
								<form method="post" id="delForm">
										<input type="hidden" name="sid" value="${param.sid }" />
										<input type="hidden" name="redirectPage" />
									<table class="table table-striped table-hover" id="editabledatatable">
										<thead>
											<tr role="row">
												<th class="">序号</th>
												<th class="text-left">联系人姓名</th>
												<th class="text-left">职务</th>
												<th class="text-left">联系方式</th>
												<th class="text-left">阅读权限</th>
												<th class="text-left">使用状态</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${list}" var="ol" varStatus="vs">
												<c:choose>
													<c:when test="${not empty ol.listContactWay}">
														<c:forEach var="linkWay" items="${ol.listContactWay}" varStatus="subVs">
																<tr class="optTr" olmId="${ol.id}" style="clear: both">
																	<c:choose>
																		<c:when test="${ol.used == 0 && ol.creator == userInfo.id}">
																			<td class="optTd text-left" style="height: 47px;"><label class="optCheckBox" style="display: none;width: 20px"> <input class="colored-blue" type="checkbox" name="ids"
																						value="${ol.id}" /> <span class="text"></span>
																				</label> <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
																			</td>
																		</c:when>
																		<c:otherwise>
																			<td class="text-left"><label class="optRowNum" style="display: block;width: 20px">${vs.count}</label></td>
																		</c:otherwise>
																	</c:choose>
																	<td class="text-left">
																		<a href="javascript:void(0);" onclick="viewOlm('${param.sid}','${ol.id}');">${ol.linkManName }</a>
																	</td>
																	<td class="text-left">
																		<c:choose>
																			<c:when test="${not empty ol.post }">${ol.post }</c:when>
																			<c:otherwise>--</c:otherwise>
																		</c:choose>
																	</td>
																	<td class="text-left">
																		<c:choose>
																			<c:when test="${not empty linkWay.contactWayValue }">${linkWay.contactWayValue }</c:when>
																			<c:otherwise>--</c:otherwise>
																		</c:choose>
																		<c:choose>
																			<c:when test="${not empty linkWay.contactWay}">：${linkWay.contactWay }</c:when>
																		</c:choose>
																	</td>
																	<td class="text-left">
																		<c:choose>
																			<c:when test="${not empty ol.pubState && ol.pubState == 0 }">私有</c:when>
																			<c:when test="${not empty ol.pubState && ol.pubState == 1 }">公开</c:when>
																			<c:otherwise>--</c:otherwise>
																		</c:choose>
																	</td>
																	<td class="text-left">
																		<c:choose>
																			<c:when test="${not empty ol.used && ol.used == 0 }">未使用</c:when>
																			<c:when test="${not empty ol.used && ol.used == 1 }">使用中</c:when>
																			<c:otherwise>--</c:otherwise>
																		</c:choose>
																	</td>
																</tr>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<tr class="optTr" olmId="${ol.id}" style="clear: both">
															<c:choose>
																<c:when test="${ol.used == 0 && ol.creator == userInfo.id}">
																	<td class="optTd text-left" style="height: 47px;"><label class="optCheckBox" style="display: none;width: 20px"> <input class="colored-blue" type="checkbox" name="ids"
																				value="${ol.id}" /> <span class="text"></span>
																		</label> <label class="optRowNum" style="display: block;width: 20px">${vs.count}</label>
																	</td>
																</c:when>
																<c:otherwise>
																	<td class="text-left"><label class="optRowNum" style="display: block;width: 20px">${vs.count}</label></td>
																</c:otherwise>
															</c:choose>
															<td class="text-left">
																<a href="javascript:void(0);" onclick="viewOlm('${param.sid}','${ol.id}');">${ol.linkManName }</a>
															</td>
															<td class="text-left">
																<c:choose>
																	<c:when test="${not empty ol.post }">${ol.post }</c:when>
																	<c:otherwise>--</c:otherwise>
																</c:choose>
															</td>
															<td class="text-left">
																<c:choose>
																	<c:when test="${not empty ol.contactWayValue }">${ol.contactWayValue }</c:when>
																	<c:otherwise>--</c:otherwise>
																</c:choose>
																<c:choose>
																	<c:when test="${not empty ol.contactWay}">：${ol.contactWay }</c:when>
																</c:choose>
															</td>
															<td class="text-left">
																<c:choose>
																	<c:when test="${not empty ol.pubState && ol.pubState == 0 }">私有</c:when>
																	<c:when test="${not empty ol.pubState && ol.pubState == 1 }">公开</c:when>
																	<c:otherwise>--</c:otherwise>
																</c:choose>
															</td>
															<td class="text-left">
																<c:choose>
																	<c:when test="${not empty ol.used && ol.used == 0 }">未使用</c:when>
																	<c:when test="${not empty ol.used && ol.used == 1 }">使用中</c:when>
																	<c:otherwise>--</c:otherwise>
																</c:choose>
															</td>
															
														</tr>
												
													</c:otherwise>
												</c:choose>
											
											
											
												
											</c:forEach>
										</tbody>
									</table>
									</form>
									<tags:pageBar url="/outLinkMan/listOutLinkMan"></tags:pageBar>
								</div>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:160px">
									<section class="error-container text-left">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>您还没有相关外部联系人数据！</h2>
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