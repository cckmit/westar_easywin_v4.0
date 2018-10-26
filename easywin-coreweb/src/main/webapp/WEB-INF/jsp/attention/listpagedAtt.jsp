<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
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
									<%--<div class="checkbox ps-margin pull-left">
									<label>
										<input type="checkbox" class="colored-blue" id="checkAllBox"
											onclick="checkAll(this,'ids')">
										<span class="text" style="color: inherit;">全选</span>
									</label>
								</div>
								--%>
									<form id="searchForm" action="/attention/attCenter">
										<input type="hidden" name="redirectPage" /> <input
											type="hidden" name="pager.pageSize" value="10"> <input
											type="hidden" id="activityMenu" name="activityMenu"
											value="${param.activityMenu}"> <input type="hidden"
											id="busType" name="busType" value="${atten.busType}" /> <input
											type="hidden" name="sid" value="${param.sid}" /> <input
											type="hidden" id="modTypes" name="modTypes"
											value="${atten.busType}" /> <input type="hidden" id="owner"
											name="owner" value="${atten.owner}" /> <input type="hidden"
											id="userName" name="userName" value="${atten.userName}" />
											
											<input type="hidden" name="isRead" id="readState"/> 

										<div class="searchCond" style="display: block">
											<div class="btn-group pull-left">
												<%--<div class="table-toolbar ps-margin">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs"
														data-toggle="dropdown">排序<i class="fa fa-angle-down"></i>
													</a>
													<ul class="dropdown-menu dropdown-default">
														<li>
															<a href="javascript:void(0)">按紧急程度</a>
														</li>
														<li>
															<a href="javascript:void(0)">按责任人</a>
														</li>
														<li>
															<a href="javascript:void(0)">按任务更新时间</a>
														</li>
														<li>
															<a href="javascript:void(0)">按任务创建时间(最新)</a>
														</li>
														<li>
															<a href="javascript:void(0)">按任务创建时间(最早)</a>
														</li>
														<li>
															<a href="javascript:void(0)">按任务到期时间(最新)</a>
														</li>
														<li>
															<a href="javascript:void(0)">按任务到期时间(最早)</a>
														</li>
													</ul>
												</div>
											</div>
										--%>
												<div class="table-toolbar ps-margin">
													<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs"
															data-toggle="dropdown"> <c:choose>
																<c:when test="${not empty atten.userName}">
																	<font style="font-weight:bold;">${atten.userName}</font>
																</c:when>
																<c:otherwise>责任人筛选</c:otherwise>
															</c:choose> <i class="fa fa-angle-down"></i>
														</a>
														<ul class="dropdown-menu dropdown-default">
															<li><a href="javascript:void(0);"
																onclick="userOneForUserIdCallBack('',
														'owner','','userName')">不限条件</a>
															</li>
															<li><a href="javascript:void(0);"
																onclick="userOneForUserId('${userInfo.id}',
														'${userInfo.userName}','','${param.sid}','owner','userName');">人员选择</a>
															</li>
															<li class="divider"></li>
															<c:choose>
																<c:when test="${not empty listOwners}">
																	<hr style="margin: 8px 0px" />
																	<c:forEach items="${listOwners}" var="owner"
																		varStatus="vs">
																		<li><a href="javascript:void(0)"
																			onclick="userOneForUserIdCallBack(
																'${owner.id}','owner','${owner.userName}','userName');">${owner.userName}</a>
																		</li>
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
																<c:when test="${empty atten.isRead}">
																	全部关注	
																</c:when>
																<c:when test="${atten.isRead eq 0}">
																	<font style="font-weight:bold;">更新未读</font>	
																</c:when>
																<c:when test="${atten.isRead eq 1}">
																	<font style="font-weight:bold;">已读关注</font>	
																</c:when>
																<c:otherwise>全部消息</c:otherwise>
															</c:choose>
															<i class="fa fa-angle-down"></i>
														</a>
														 <ul class="dropdown-menu dropdown-default">
															 <li><a href="javascript:void(0);"  onclick="setReadState('')">清空条件</a></li>
															<li><a href="javascript:void(0);"  onclick="setReadState('0')">更新未读</a></li>
				                                            </ul>
				                                        </div>
		                                    		</div>

												<div class="table-toolbar ps-margin">
													<div class="btn-group cond" id="moreCondition_Div">
														<a class="btn btn-default dropdown-toggle btn-xs"
															onclick="displayMoreCond('moreCondition_Div')"> <c:choose>
																<c:when
																	test="${not empty atten.startDate || not empty atten.endDate}">
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
																	value="${atten.startDate}" id="startDate"
																	name="startDate" placeholder="开始时间"
																	onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})" />
																<span>~</span> <input
																	class="form-control dpd2 no-padding condDate"
																	type="text" readonly="readonly"
																	value="${atten.endDate}" id="endDate" name="endDate"
																	placeholder="结束时间"
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
												<span class="input-icon"> <input id="modTitle"
													name="modTitle" value="${atten.modTitle}"
													class="form-control ps-input" type="text"
													placeholder="请输入关键字"> <a href="javascript:void(0)"
													class="ps-searchBtn"><i
														class="glyphicon glyphicon-search circular danger"></i> </a>
												</span>
											</div>
										</div>
										<%--<div class="batchOpt" style="display: none">
										<div class="btn-group pull-left">
											放点傻？
										</div>
									</div>
								--%>
									</form>
								</div>
									<c:choose>
				<c:when test="${not empty list}">
								<div class="widget-body">
									<ul class="messages-list">
										<c:forEach items="${list}" var="obj" varStatus="vs">
											<li class="item first-item">
												<div class="message-content">
													<img src="/downLoad/userImg/${obj.comId}/${obj.userId}?sid=${param.sid}"
														class="message-head" width="50" height="50"
														title="${obj.userName}" />
													<div class="content-headline">
														<div class="checkbox pull-left ps-chckbox">
															<label> <i class="fa fa-star ws-star"
																style="font-size:18px;margin-right:5px;"
																onclick="changeAtten('${obj.busType}',${obj.busId},1,'${param.sid}',this)"
																title="取消关注"></i>
															</label>
														</div>
														<c:choose>
															<c:when test="${obj.busType=='1'}">
																<span ${obj.isRead==0?'noread':'' }> [分享] </span>
																<a href="javascript:void(0)"
																	class="item-box ${obj.isRead==0?'noread':'' }"
																	onclick="readMod(this,'atten',${obj.busId},'${obj.busType}');viewDetailMod(${obj.busId},'${obj.busType}','${param.sid}')">
																	<tags:viewTextArea>
																		<tags:cutString num="82">
																				${obj.modTitle}
																			</tags:cutString>
																	</tags:viewTextArea>
																</a>
															</c:when>
															<c:otherwise>
																<span ${obj.isRead==0?'noread':'' }>
																	[${fn:substring(obj.busTypeName,0,2)}] </span>
																<a href="javascript:void(0)"
																	class="item-box ${obj.isRead==0?'noread':'' }"
																	onclick="readMod(this,'atten',${obj.busId},'${obj.busType}');viewDetailMod(${obj.busId},'${obj.busType}','${param.sid}')">
																	<tags:viewTextArea>
																		<tags:cutString num="82">
																				${obj.modTitle}
																			</tags:cutString>
																	</tags:viewTextArea>
																</a>
															</c:otherwise>
														</c:choose>
													</div>
													<a href="javascript:void(0)"
														class="item-box ${obj.isRead==0?'noread':'' } "
														onclick="readMod(this,'atten',${obj.busId},'${obj.busType}');viewDetailMod(${obj.busId},'${obj.busType}','${param.sid}')">
														<div class="content-text">
															<tags:viewTextArea>
																<tags:cutString num="302">
																	${obj.content}
																</tags:cutString>
															</tags:viewTextArea>
														</div>
													</a>
													<div class="item-more">
														<c:choose>
															<c:when test="${not empty obj.modiferName}">
																<span class="time"><i class="fa fa-user"></i>&nbsp;更新人：${obj.modiferName}</span>
															</c:when>
															<c:otherwise>
																<span class="time"><i class="fa fa-user"></i>&nbsp;责任人：${obj.userName}</span>
															</c:otherwise>
														</c:choose>
														<c:choose>
															<c:when test="${not empty obj.modifTime}">
																<span class="time"><i class="fa fa-clock-o"></i>&nbsp;更新时间：${fn:substring(obj.modifTime,0,16)}</span>
															</c:when>
															<c:otherwise>
																<span class="time"><i class="fa fa-clock-o"></i>&nbsp;更新时间：${fn:substring(obj.modTime,0,16)}</span>
															</c:otherwise>
														</c:choose>
													</div>
												</div>
											</li>
										</c:forEach>
									</ul>
									<tags:pageBar url="/attention/attCenter"></tags:pageBar>
								</div>
								</c:when>
				<c:otherwise>
					<!--与上一个searchForm不共存 -->
					<form id="searchForm" action="/attention/attCenter">
						<input type="hidden" name="redirectPage" /> <input type="hidden"
							name="pager.pageSize" value="10"> <input type="hidden"
							id="activityMenu" name="activityMenu"
							value="${param.activityMenu}"> <input type="hidden"
							id="busType" name="busType" value="${atten.busType}" /> <input
							type="hidden" name="sid" value="${param.sid}" /> <input
							type="hidden" id="modTypes" name="modTypes"
							value="${atten.busType}" /> <input type="hidden" id="owner"
							name="owner" value="${atten.owner}" /> <input type="hidden"
							id="userName" name="userName" value="${atten.userName}" />
						<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
							<section class="error-container text-center">
								<h1>
									<i class="fa fa-exclamation-triangle"></i>
								</h1>
								<div class="error-divider">
									<h2>您还没添加过相关关注！</h2>
									<p class="description">协同提高效率，分享拉近距离。</p>
								</div>
							</section>
						</div>
					</form>
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

