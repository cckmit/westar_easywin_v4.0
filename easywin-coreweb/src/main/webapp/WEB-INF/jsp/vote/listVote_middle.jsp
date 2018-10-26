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

</head>
<body>
	<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			
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
									<form action="/vote/listPagedVote" id="voteSearchForm">
										<input type="hidden" name="searchTab" id="searchTab"
											value="${param.searchTab }" /> <input type="hidden"
											name="sid" value="${param.sid }" /> <input type="hidden"
											name="pager.pageSize" value="8"> <input type="hidden"
											id="enabled" name="enabled" value="${vote.enabled }" /> <input
											type="hidden" id="attentionState" name="attentionState"
											value="${vote.attentionState}" /> <input name="searchAll"
											type="hidden" value="${vote.searchAll}" readonly="readonly" />
										<input name="orderBy" type="hidden" value="${vote.orderBy}"
											readonly="readonly" />
										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs"
															data-toggle="dropdown"> <c:choose>
																<c:when test="${vote.enabled=='1'}">
																	<font style="font-weight:bold;">正在进行</font>
																</c:when>
																<c:when test="${vote.enabled=='0'}">
																	<font style="font-weight:bold;">已经过期</font>
																</c:when>
																<c:otherwise>所有状态</c:otherwise>
															</c:choose> <i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li><a href="javascript:void(0)"
																onclick="enabledVote('2')">所有状态</a></li>
															<li><a href="javascript:void(0)"
																onclick="enabledVote('1')">正在进行</a></li>
															<li><a href="javascript:void(0)"
																onclick="enabledVote('0')">已经过期</a></li>
														</ul>
													</div>
												</div>

												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs"
															data-toggle="dropdown"> <c:choose>
																<c:when test="${vote.orderBy=='31'}">
																	<font style="font-weight:bold;">回复最多</font>
																</c:when>
																<c:when test="${vote.orderBy=='32'}">
																	<font style="font-weight:bold;">投票最多</font>
																</c:when>
																<c:otherwise>
																	<font style="font-weight:bold;">最近发布</font>
																</c:otherwise>
															</c:choose> <i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li><a href="javascript:void(0)"
																onclick="gets_value('',this,'orderByC','orderBy')">最近发布</a></li>
															<li><a href="javascript:void(0)"
																onclick="gets_value('31',this,'orderByC','orderBy')">回复最多</a></li>
															<li><a href="javascript:void(0)"
																onclick="gets_value('32',this,'orderByC','orderBy')">投票最多</a></li>
														</ul>
													</div>
												</div>

												<div class="table-toolbar ps-margin">
													<div class="btn-group cond" id="moreCondition_Div">
														<a class="btn btn-default dropdown-toggle btn-xs"
															onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
																<c:when
																	test="${not empty vote.startDate || not empty vote.endDate}">
																	<font style="font-weight:bold;">筛选中</font>
																</c:when>
																<c:otherwise>
					                                            	更多
		                                            			</c:otherwise>
															</c:choose> <i class="fa fa-angle-down"></i>
														</a>
														<div
															class="dropdown-menu dropdown-default padding-bottom-10"
															style="min-width: 330px;">
															<div class="ps-margin ps-search padding-left-10">
																<span class="btn-xs">起止时间：</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${vote.startDate}" id="startDate"
																	name="startDate" placeholder="开始时间"
																	onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																<span>~</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly" value="${vote.endDate}"
																	id="endDate" name="endDate" placeholder="结束时间"
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
											<div class="ps-margin ps-search">
												<span class="input-icon"> <input id="voteContent"
													name="voteContent" value="${vote.voteContent}"
													class="form-control ps-input" type="text"
													placeholder="请输入关键字"> <a href="javascript:void(0)"
													class="ps-searchBtn"><i
														class="glyphicon glyphicon-search circular danger"></i></a>
												</span>
											</div>
										</div>
										<div class="batchOpt" style="display: none">
											<div class="btn-group pull-left">
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs"
															onclick="delVote()"> 批量删除 </a>
													</div>
												</div>
											</div>
										</div>
									</form>
									<div class="widget-buttons ps-widget-buttons">
										<button class="btn btn-primary btn-xs" type="button"
											title="提问" onclick="addVote()">
											<i class="fa fa-plus"></i> 新建投票
										</button>
									</div>
								</div>
								<c:choose>
				<c:when test="${not empty list}">
								<div class="widget-body">
									<form action="/vote/delBatchVote" id="delForm" method="post">
										<input type="hidden" name="sid" value="${param.sid}" /> <input
											type="hidden" name="redirectPage" id="redirectPage" />
										<ul class="messages-list">
											<c:forEach items="${list}" var="vote" varStatus="vs">
												<li class="item first-item">
													<div class="message-content">

														<img
															src="/downLoad/userImg/${vote.comId}/${vote.owner}?sid=${param.sid}"
															title="${vote.ownerName}" class="message-head"
															width="50" height="50" />
														<div class="checkbox pull-left ps-chckbox">
															<label> <input type="checkbox"
																class="colored-blue" value="${vote.id}" name="ids"
																${vote.owner == userInfo.id?'':'disabled="disabled"'}>
																<span class="text"></span>
															</label>
														</div>
														<a href="javascript:void(0)"
															class="item-box ${vote.isRead==0?'noread':'' }"
															onclick="readMod(this,'vote',${vote.id},'004');viewVote(${vote.id})">
															<div class="content-text ws-texts " style="width: 90%">
																<tags:viewTextArea>
																	<tags:cutString num="120">
																${vote.voteContent} 
															</tags:cutString>
																</tags:viewTextArea>
															</div>
														</a>
														<c:choose>
															<c:when test="${not empty vote.mostChooses}">
																<p>
																	<c:choose>
																		<c:when test="${fn:length(vote.mostChooses)==1}">
																最多结果：
							  								</c:when>
																		<c:otherwise>
									  							有${fn:length(vote.mostChooses)}个最多结果:
							  								</c:otherwise>
																	</c:choose>
																</p>
																<c:forEach items="${vote.mostChooses}" var="voteChoose"
																	varStatus="vs">
																	<p>
																		<c:if test="${fn:length(vote.mostChooses)>1 }">
																	(${vs.count })
																</c:if>
																		<span class="vote-name" title="${voteChoose.content}"
																			style="cursor: default">${voteChoose.content}</span>
																	</p>
																</c:forEach>
															</c:when>
														</c:choose>

														<div class="item-more">
															<a class="btn btn-default btn-xs"
																style="cursor: pointer;"
																attentionState="${vote.attentionState}" busType="004" busId="${vote.id}" describe="1" iconSize="sizeMd"
																onclick="changeAtten('${param.sid}',this)">
																<i
																class="fa ${vote.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>${vote.attentionState==0?'关注':'取消'}
															</a> <span class="time"><i class="fa fa-clock-o"></i>${fn:substring(vote.recordCreateTime,0,16)}</span>
															<span class="time"><i class="fa fa-user"></i>已投票：<b
																id="vote${vote.id}">${vote.voteTotal}</b></span> <span
																class="time"> <c:choose>
																	<c:when test="${vote.enabled==0}">
										  						投票已结束 
									  						</c:when>
																	<c:when test="${vote.voterChooseNum==0}">
																你还没有投票
									  						</c:when>
																	<c:otherwise>
									  							 投票正在进行 
									  						</c:otherwise>
																</c:choose>
															</span>
														</div>
													</div>
												</li>
											</c:forEach>
										</ul>
									</form>
									<tags:pageBar url="/vote/listPagedVote"></tags:pageBar>
								</div>
								</c:when>
				<c:otherwise>
					<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>还没有谁创建过投票哦！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
								<a href="javascript:void(0);" onclick="addVote();"
									class="return-btn"><i class="fa fa-plus"></i>新建投票</a>
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

