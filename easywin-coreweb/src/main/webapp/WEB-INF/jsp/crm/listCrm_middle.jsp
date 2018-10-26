<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script type="text/javascript">
		$(function(){
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
                        <div class="col-sm-12">
                            <div class="widget">
                                <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                                	<div class="checkbox ps-margin pull-left">
										<label>
											<input type="checkbox" class="colored-blue" onclick="checkAllCrm(this,'ids')" id="checkAllBox">
											<span class="text">全选</span>
										</label>
									</div>
									<div>
									<form action="/crm/customerListPage" id="searchForm" class="subform">
										<input type="hidden" name="sid" value="${param.sid}">
										<input type="hidden" id="pageSize" name="pager.pageSize" value="15">
										<input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab}">
										<!--是否为自己的 -->
										<input type="hidden" name="ownerType" id="ownerType" value="${customer.ownerType}">
										<!-- 是否关注 -->
										<input type="hidden" name="attentionState" id="attentionState" value="${customer.attentionState}">
										<!--客户类型选择 -->
										<input type="hidden" name="customerTypeId" id="customerTypeId" value="">
										<!--客户责任人-->
										<input type="hidden" name="owner" id="owner" value="${customer.owner}">
										<!--客户区域-->
										<input type="hidden" name="areaIdAndType" id="areaIdAndType" value="${customer.areaIdAndType}">
										
										<input type="hidden" name="stage" id="stage" value="${customer.stage}">
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
															客户类型筛选
															<i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li>
																<a href="javascript:void(0);" onclick="customerTypeFilter(this,'')">不限条件</a>
															</li>
															<li>
																<a href="javascript:void(0);" onclick="crmTypeMoreTree('${param.sid}','crmType');">类型选择</a>
															</li>
														</ul>
													</div>
												</div>
												<div style="float: left;width: 250px;display: none">
													<select list="listCrmType" listkey="id" listvalue="typeName" id="crmType_select" name="listCrmType.id" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
														<c:forEach items="${customer.listCrmType }" var="obj" varStatus="vs">
															<option selected="selected" value="${obj.id }">${obj.typeName }</option>
														</c:forEach>
													</select>
												</div>
		                                    		
		                                    		
		                                    			<div class="table-toolbar ps-margin">
		                                        	<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
															<c:choose>
															<c:when test="${not empty customer.stage}">
																<c:forEach items="${listCrmStage}" var="crmStage" varStatus="status">
																<c:if test="${crmStage.id==customer.stage}"><font style="font-weight:bold;">${crmStage.stageName}</font></c:if>
																</c:forEach>
															</c:when>
															<c:otherwise>所属阶段</c:otherwise>
														</c:choose>
															<i class="fa fa-angle-down"></i>
														</a>
														 <ul class="dropdown-menu dropdown-default">
															 <li><a href="javascript:void(0);" onclick="customerStageFilter(this,'')">不限阶段</a></li>
															 <c:choose>
																<c:when test="${not empty listCrmStage}">
																	<c:forEach items="${listCrmStage}" var="crmStage" varStatus="status">
																	<li><a href="javascript:void(0);" onclick="customerStageFilter(this,'${crmStage.id}')">${crmStage.stageName}</a></li>
																	</c:forEach>
																</c:when>
															</c:choose>
				                                            </ul>
				                                        </div>
		                                    		</div>
		                                    		
		                                    		
		                                    		
				                                    <div class="table-toolbar ps-margin">
				                                        <div class="btn-group">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
				                                            	<c:choose>
																	<c:when test="${not empty customer.areaName}">
																		<font style="font-weight:bold;">${customer.areaName}</font>	
																	</c:when>
																	<c:otherwise>区域筛选</c:otherwise>
																</c:choose>
				                                            	<i class="fa fa-angle-down"></i>
				                                            </a>
				                                            <ul class="dropdown-menu dropdown-default">
				                                                <li><a href="javascript:void(0);"  onclick="artOpenerCallBack('')">不限区域</a></li>
																<li><a href="javascript:void(0);"  onclick="areaOne('areaIdAndType','areaName','${param.sid}','1')">区域选择</a></li>
				                                            </ul>
				                                        </div>
				                                    </div>
				                                    <c:if test="${param.searchTab!=11 && not empty param.searchTab}">
					                                    <div class="table-toolbar ps-margin">
					                                        <div class="btn-group">
					                                            <a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
					                                            	<c:choose>
																		<c:when test="${not empty customer.ownerName}">
																			<font style="font-weight:bold;">${customer.ownerName}</font>	
																		</c:when>
																		<c:otherwise>责任人筛选</c:otherwise>
																	</c:choose>
					                                            	<i class="fa fa-angle-down"></i>
					                                            </a>
					                                            <ul class="dropdown-menu dropdown-default">
					                                                <li><a href="javascript:void(0);"  onclick="userOneForUserIdCallBack('','owner','','')">不限条件</a></li>
																		<li><a href="javascript:void(0);"  onclick="userMoreForUserId('${param.sid}','owner');">人员选择</a></li>
					                                            </ul>
					                                        </div>
					                                    </div>
					                                    <div style="float: left;width: 250px;display: none">
														<select list="listOwner" listkey="id" listvalue="userName" id="owner_select" name="listOwner.id" multiple="multiple"
															moreselect="true" style="width: 100%; height: 100px;">
														<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
														<option selected="selected" value="${obj.id }">${obj.userName }</option>
														</c:forEach>
														</select>
														</div>
				                                    </c:if>
				                                    <div class="table-toolbar ps-margin">
				                                        <div class="btn-group cond" id="moreCondition_Div">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
				                                            	<c:choose>
				                                            			<c:when test="${not empty customer.startDate || not empty customer.endDate || not empty customer.frequenStartDate || not empty customer.frequenEndDate}">
				                                            				<font style="font-weight:bold;">筛选中</font>
				                                            			</c:when>
				                                            			<c:otherwise>
							                                            	更多
				                                            			</c:otherwise>
				                                            	</c:choose>
				                                            	<i class="fa fa-angle-down"></i>
				                                            </a>
				                                            <div class="dropdown-menu dropdown-default padding-bottom-10" 
				                                            style="min-width: 330px;">
																<div class="ps-margin ps-search padding-left-10">
																	<div>
																		<span>创建时间筛选：</span>
																	</div>
																	<div class="margin-left-20">
							                                        	<span class="btn-xs">起</span>
							                                        	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.startDate}" id="startDate" name="startDate" 
																			placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
																			<span class="btn-xs">止</span>
																			<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.endDate}" id="endDate"  name="endDate"
																			placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
																	</div>
						                                    	</div>
																<div class="ps-margin ps-search padding-left-10">
																	<div>
																		<span>未更新时间筛选：</span>
																	</div>
																	<div class="margin-left-20">
						                                        		<span class="btn-xs">起</span>
						                                        		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.frequenStartDate}" id="frequenStartDate" name="frequenStartDate" 
																		placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'frequenEndDate\',{d:-0});}'})"/>
																		<span class="btn-xs">止</span>
																		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${customer.frequenEndDate}" id="frequenEndDate"  name="frequenEndDate"
																		placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'frequenStartDate\',{d:+0});}'})"/>
																	</div>
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
														<input id="customerName" name="customerName" class="form-control ps-input" 
														type="text" placeholder="请输入关键字" value="${customer.customerName}">
														<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
													</span>
												</div>
											</div>
											<div class="batchOpt" style="display: none">
											<div class="btn-group pull-left">
												<!-- 是否不能删除，为空，可以操作 -->
												<c:if test="${empty delete}">
													<div class="table-toolbar ps-margin">
			                                        	<div class="btn-group">
															<a class="btn btn-default dropdown-toggle btn-xs" id="batchDelCrm">
																批量删除
															</a>
			                                        	</div>
			                                    	</div>
												</c:if>
												<div class="table-toolbar ps-margin">
		                                        	<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" id="batchCrmHandOver">
															批量移交
														</a>
		                                        	</div>
		                                    	</div>
		                                    	<!--是否不能编辑，为空，可以操作 -->
		                                    	<c:if test="${empty update}">
													<div class="table-toolbar ps-margin">
			                                        	<div class="btn-group">
															<a class="btn btn-default dropdown-toggle btn-xs" onclick="crmCompress('${param.sid}')">
																合并客户
															</a>
			                                        	</div>
			                                    	</div>
		                                    	</c:if>
											</div>
										</div>
										
										</form>
										<!--是否不能添加，为空，可以操作 -->
										<c:if test="${empty add}">
		                                    <div class="widget-buttons ps-widget-buttons">
		                                    	<button class="btn btn-primary btn-xs" type="button" onclick="addCustomer()">
		                                    		<i class="fa fa-plus"></i>
		                                    		新建客户
		                                    	</button>
		                                    </div>
										</c:if>
										</div>
										<div id="listOwner_show" class=" padding-top-10 text-left " style="display:${empty customer.listOwner ? 'none':'block'}">
											<strong >责任人筛选:</strong>
											<c:forEach items="${customer.listOwner }" var="obj" varStatus="vs">
												 <span  style="cursor:pointer;"  title="双击移除" ondblclick="removeChoose('owner','${param.sid}','${obj.id }')"
												 class="label label-default margin-right-5 margin-bottom-5">${obj.userName }</span>	
											</c:forEach>
										</div>
										<div id="listCrmType_show" class="padding-top-10 text-left " style="display:${empty customer.listCrmType ? 'none':'block'}">
											<strong>客户类型筛选:</strong>
											<c:forEach items="${customer.listCrmType }" var="obj" varStatus="vs">
												<span style="cursor:pointer;" title="双击移除" ondblclick="removeChoose('crmType','${param.sid}','${obj.id }')" class="label label-default margin-right-5 margin-bottom-5">${obj.typeName }</span>
											</c:forEach>
										</div>
                               		 </div>
                              <c:choose>
			      					 <c:when test="${not empty listCustomer}">
	                                <div class="widget-body">
		                                <form action="/crm/delCustomer" id="delForm">
											<input type="hidden" name="redirectPage" id="redirectPage"/>
											<input type="hidden" name="sid" value="${param.sid}"/>
		                                	<ul class="messages-list" id="crmListUl">
	                                			<c:forEach items="${listCustomer}" var="obj" varStatus="vs">
	                                				<li class="item first-item">
														<div class="message-content">
															<img class="message-head userImg" width="50" height="50"
																title="${obj.ownerName}" 
																src="/downLoad/userImg/${userInfo.comId}/${obj.owner}"/>
															<div class="content-headline">
																<div class="checkbox pull-left ps-chckbox">
																	<label>
																		<input type="checkbox" class="colored-blue" value="${obj.id}" name="ids" ${obj.owner==userInfo.id?'':'disabled="disabled"'} >
																		<span class="text"></span>
																	</label>
																</div>
																<a href="javascript:void(0)" class="item-box ${obj.isRead==0?"noread":"" }" onclick="readMod(this,'crm',${obj.id},'012');viewCustomer(${obj.id})">
																	${obj.customerName}
																</a>
															</div>
															<a href="javascript:void(0)" class="item-box ${obj.isRead==0?"noread":"" }" onclick="readMod(this,'crm',${obj.id},'012');viewCustomer(${obj.id})">
																<div class="content-text">
																	<tags:viewTextArea>
																		<tags:cutString num="302">${obj.modifyContent}</tags:cutString>
																	</tags:viewTextArea>
																</div>
															</a>
															<div class="item-more">
																<span class="label label-default">${obj.typeName }</span>
																<span class="label label-default">${obj.areaName }</span>
																<a class="btn btn-default btn-xs" style="cursor: pointer;" 
																	attentionState="${obj.attentionState}" busType="012" busId="${obj.id}" describe="1" iconSize="sizeMd"
																	onclick="changeAtten('${param.sid}',this)">
																	<i class="fa ${obj.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>${obj.attentionState==0?'关注':'取消'}
																</a>
																<span class="time"><i class="fa fa-clock-o"></i>更新时间：${obj.modifyTime }</span>
																<span class="time"><i class="fa fa-user"></i>更新人：${obj.modifierName }</span>
																<span class="time"><i class="fa fa-clock-o"></i>创建于：${obj.recordCreateTime }</span>
															</div>
						                                </div>
													</li>
	                                			</c:forEach>
											</ul>
										</form>
										<tags:pageBar url="/crm/customerListPage"></tags:pageBar>
	                                </div>
	                                </c:when>
					                <c:otherwise>
					                	<div class="widget-body" style="height:515px; text-align:center;padding-top:155px">
											<section class="error-container text-center">
												<h1>
													<i class="fa fa-exclamation-triangle"></i>
												</h1>
												<div class="error-divider">
													<h2>您还没有相关的客户数据！</h2>
													<p class="description">协同提高效率，分享拉近距离。</p>
													<a href="javascript:void(0);" onclick="addCustomer();"
														class="return-btn"><i class="fa fa-plus"></i>新建客户</a>
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

