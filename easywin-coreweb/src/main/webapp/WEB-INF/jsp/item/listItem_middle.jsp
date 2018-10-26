<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
	$(function() {

		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
	})
</script>
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
								<label>
									<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAllItem(this,'ids')">
									<span class="text">全选</span>
								</label>
							</div>
							<div>
								<form id="searchForm" action="/item/listItemPage" class="subform">
									<input type="hidden" name="redirectPage" />
									<input type="hidden" id="pageSize" name="pager.pageSize" value="12" />
									<input type="hidden" name="sid" value="${param.sid}" />
									<input type="hidden" name="state" id="state" value="${item.state}" />
									<input type="hidden" name="ownerType" id="ownerType" value="${item.ownerType}" />
									<input type="hidden" id="owner" name="owner" value="${item.owner}" />
									<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab}">
									<input type="hidden" name="attentionState" id="attentionState" value="${param.attentionState}">

									<div class="searchCond" style="display: block">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														<c:choose>
															<c:when test="${item.state==1}">
																<font style="font-weight:bold;">进行中</font>
															</c:when>
															<c:when test="${item.state==3}">
																<font style="font-weight:bold;">挂起中</font>
															</c:when>
															<c:when test="${item.state==4}">
																<font style="font-weight:bold;">已完结</font>
															</c:when>
															<c:otherwise>项目状态</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li>
															<a href="javascript:void(0)" onclick="itemStateFilter('')">全部状态</a>
														</li>
														<li>
															<a href="javascript:void(0)" onclick="itemStateFilter('1')">进行中</a>
														</li>
														<!-- 
															<li><a href="javascript:void(0)" onclick="itemStateFilter('3')">挂起中</a></li>
															 -->
														<li>
															<a href="javascript:void(0)" onclick="itemStateFilter('4')">已完结</a>
														</li>
													</ul>
												</div>
											</div>
											<c:if test="${param.searchTab!=11 && not empty param.searchTab}">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
															<c:choose>
																<c:when test="${not empty item.ownerName}">
																	<font style="font-weight:bold;">${item.ownerName}</font>
																</c:when>
																<c:otherwise>责任人</c:otherwise>
															</c:choose>
															<i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li>
																<a href="javascript:void(0)" onclick="userOneForUserIdCallBack('','owner','','')">不限条件</a>
															</li>
															<li>
																<a href="javascript:void(0)" onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a>
															</li>
															<%-- 	<c:choose>
																	<c:when test="${not empty listOwners}">
																		<hr style="margin: 8px 0px" />
																		<c:forEach items="${listOwners}" var="owner"
																			varStatus="vs">
																			<li><a href="javascript:void(0);"
																				onclick="userOneForUserIdCallBack('${owner.id}','owner','${owner.userName}','');">${owner.userName}</a></li>
																		</c:forEach>
																	</c:when>
																</c:choose> --%>
														</ul>
													</div>
												</div>
												<div style="float: left;width: 250px;display: none">
													<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
														<c:forEach items="${item.listOwner }" var="obj" varStatus="vs">
															<option selected="selected" value="${obj.id }">${obj.userName }</option>
														</c:forEach>
													</select>
												</div>
											</c:if>
											<div class="table-toolbar ps-margin margin-right-5">
		                                        <div class="btn-group">
		                                            <a class="btn btn-default dropdown-toggle btn-xs"  data-toggle="dropdown">
		                                            	产品筛选<i class="fa fa-angle-down"></i>
		                                            </a>
		                                            <ul class="dropdown-menu dropdown-default">
		                                                <li>
		                                                    <a href="javascript:void(0)" class="clearMoreElement" relateList="product_select">不限条件</a>
		                                                </li>
		                                                <li>
		                                                    <a href="javascript:void(0)" class="relateModSelect" busType="070" isMore="1" typeValue="relateModType" relateList="product_select">产品选择</a>
		                                                </li>
		                                            </ul>
		                                        </div>
		                                    </div>
		                                    <div style="float: left;width: 250px;display: none">
		                                        <select list="listProduct" listkey="id" listvalue="name" id="product_select" name="listProduct.id" multiple="multiple"
		                                                moreselect="true" style="width: 100%; height: 100px;">
			                                            <c:forEach items="${item.listProduct }" var="obj" varStatus="vs">
			                                                <option selected="selected" value="${obj.id}">${obj.name}</option>
			                                            </c:forEach>
		                                        </select>
		                                    </div>
											<div class="table-toolbar ps-margin">
												<div class="btn-group cond" id="moreCondition_Div">
													<a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
														<c:choose>
															<c:when test="${not empty item.startDate || not empty item.endDate}">
																<font style="font-weight:bold;">筛选中</font>
															</c:when>
															<c:otherwise>
						                                            	更多
			                                            			</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
													<div class="dropdown-menu dropdown-default padding-bottom-10" style="min-width: 330px;">
														<div class="ps-margin ps-search padding-left-10">
															<span class="btn-xs">起止时间：</span>
															<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.startDate}" id="startDate" name="startDate" placeholder="开始时间"
																onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
															<span>~</span>
															<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${item.endDate}" id="endDate" name="endDate" placeholder="结束时间"
																onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})" />
														</div>
														<div class="ps-clear padding-top-10" style="text-align: center;">
															<button type="submit" class="btn btn-primary btn-xs">查询</button>
															<button type="button" class="btn btn-default btn-xs margin-left-10" onclick="resetMoreCon('moreCondition_Div')">重置</button>
														</div>
													</div>
												</div>
											</div>

										</div>
										<div class="ps-margin ps-search">
											<span class="input-icon">
												<input id="itemName" class="form-control ps-input" name="itemName" type="text" placeholder="请输入关键字" value="${item.itemName}">
												<a href="javascript:void(0)" class="ps-searchBtn" onclick="$('#searchForm').submit()">
													<i class="glyphicon glyphicon-search circular danger"></i>
												</a>
											</span>
										</div>
									</div>

									<div class="batchOpt" style="display: none">
										<div class="btn-group pull-left">
											<!-- 是否不能删除，为空，可以操作 -->
											<c:if test="${empty delete}">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" id="batchDelItem"> 批量删除 </a>
													</div>
												</div>
											</c:if>
											<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" id="batchItemHandOver"> 批量移交 </a>
												</div>
											</div>
											<!--是否不能编辑，为空，可以操作 -->
											<c:if test="${empty update}">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" onclick="itemCompress('${param.sid }')"> 合并项目 </a>
													</div>
												</div>
											</c:if>
										</div>
									</div>
								</form>
								<c:if test="${empty add}">
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-info btn-primary btn-xs" type="button" onclick="addItem()">
											<i class="fa fa-plus"></i>
											新建项目
										</button>
									</div>
								</c:if>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty item.listOwner ? 'none':'block'}">
								<strong>责任人筛选:</strong>
								<c:forEach items="${item.listOwner }" var="obj" varStatus="vs">
									<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>
								</c:forEach>
							</div>
							<div class=" padding-top-10 text-left " style="display:${empty item.listProduct ? 'none':'block'}">
								<strong>
									 产品筛选: 
								</strong>
								<c:forEach items="${item.listProduct }" var="obj" varStatus="vs">
									 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('product','${param.sid}','${obj.id }')"
									 class="label label-default margin-right-5 margin-bottom-5">${obj.name }</span>	
								</c:forEach>
							</div>
						</div>
						<c:choose>
							<c:when test="${not empty list}">
								<div class="widget-body">
									<form action="/item/delItem" method="post" id="delForm">
										<input type="hidden" name="sid" value="${param.sid}" />
										<input type="hidden" id="redirectPage" name="redirectPage" />
										<div class="panel-body" id="listItemDiv">
											<c:forEach items="${list}" var="itemVo" varStatus="status">
												<c:if test="${(status.count>1 && status.count mod 4 == 1) || status.first}">
													<div class="row margin-bottom-20">
												</c:if>
												<div class="col-xs-3 optDiv" itemId="${itemVo.id}">
													<div class="widget-body no-shadow ps-project-border no-padding">
														<div class="panel-body">
															<a href="javascript:void(0)" class="project-box ${itemVo.readState==0?" noread":"" }" onclick="readMod(this,'item',${itemVo.id},'005');viewItem(${itemVo.id});">
																<span class="fa fa-folder-o ps-project-icon"></span>
																<p class="no-margin">${itemVo.itemName}</p>
																<small>${fn:substring(itemVo.modifyDate,0,10)}</small>
															</a>
														</div>
														<div class="project-box-ultrafooter clearfix">
															<span class="pull-left ps-name">
															<img class="project-img-owner" title="项目经理" 
																src="/downLoad/userImg/${itemVo.comId}/${itemVo.owner}"/>
																${itemVo.ownerName}
															</span>
															<c:if test="${not empty itemVo.amount}">
																<span class="pull-left ps-name" style="margin-left: 10px;font-weight: 500" title="项目金额">
																	<fmt:formatNumber value="${itemVo.amount}" pattern="#,###.##"/>万
																</span>
															</c:if>
															<a href="javascript:void(0)" class="link pull-right margin-left-10" attentionState="${itemVo.attentionState}" busType="005" busId="${itemVo.id}" describe="0" iconSize="sizeMd"
																onclick="changeAtten('${param.sid}',this)">
																<i class="fa fa-lg sizeLg ${itemVo.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>
															</a>
															<c:if test="${itemVo.owner==userInfo.id && itemVo.state!=4}">
																<a href="javascript:void(0)" class="link pull-right margin-left-10 optA" style="display:none">
																	<label>
																		<input type="checkbox" class="colored-blue" name="ids" value="${itemVo.id}">
																		<span class="text"></span>
																	</label>
																</a>
															</c:if>
														</div>
													</div>
												</div>
												<c:if test="${status.count mod 4 == 0 || status.last}">
										</div>
										</c:if>
										</c:forEach>
									</form>
								</div>
								<div style="clear:both"></div>
								<tags:pageBar url="/item/listItemPage"></tags:pageBar>
							</c:when>
							<c:otherwise>
								<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
									<section class="error-container text-center">
										<h1>
											<i class="fa fa-exclamation-triangle"></i>
										</h1>
										<div class="error-divider">
											<h2>您还没有自己的项目哦！</h2>
											<p class="description">协同提高效率，分享拉近距离。</p>
											<a href="javascript:void(0);" onclick="addItem();" class="return-btn">
												<i class="fa fa-plus"></i>
												新建项目
											</a>
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

