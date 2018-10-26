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
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                            	<div class="checkbox ps-margin pull-left">
									<label>
										<input type="checkbox" class="colored-blue" id="checkAllBox" onclick="checkAll(this,'ids')">
										<span class="text" style="color: inherit;">全选</span>
									</label>
								</div>
								<form id="qasSearchForm" action="/qas/listPagedQas">
									 <input type="hidden" name="searchTab" id="searchTab" value="${param.searchTab }" />
									 <input type="hidden" name="sid" value="${param.sid }" />
									 <input type="hidden" id="state" name="state" value="${question.state }"/>
									  <input type="hidden" name="pager.pageSize" value="8">
									 <input type="hidden" id="attentionState" name="attentionState" value="${question.attentionState}"/>
									 <input name="searchAll"  type="hidden" value="${question.searchAll}"  readonly="readonly" />
									  <input name="orderBy"  type="hidden" value="${question.orderBy}"  readonly="readonly" />
									<div class="searchCond" style="display: block">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin">
	                                        	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														<c:choose>
															<c:when test="${question.state=='1'}"><font style="font-weight:bold;">正开放</font></c:when>
															<c:when test="${question.state=='0'}"><font style="font-weight:bold;">已关闭</font></c:when>
															<c:otherwise>所有状态</c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
											 		<ul class="dropdown-menu dropdown-default">
		                                                <li><a href="javascript:void(0)" onclick="enabledQues('2')">所有状态</a></li>
														<li><a href="javascript:void(0)" onclick="enabledQues('1')">正开放</a></li>
														<li><a href="javascript:void(0)" onclick="enabledQues('0')">已关闭</a></li>
		                                            </ul>
	                                        	</div>
	                                    	</div>
	                                    	
											<div class="table-toolbar ps-margin">
	                                        	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
														<c:choose>
															<c:when test="${question.orderBy=='31'}"><font style="font-weight:bold;">回答最多</font></c:when>
															<c:otherwise><font style="font-weight:bold;">最新提问</font></c:otherwise>
														</c:choose>
														<i class="fa fa-angle-down"></i>
													</a>
											 		<ul class="dropdown-menu dropdown-default">
		                                                <li><a href="javascript:void(0)" onclick="gets_value('',this,'orderByC','orderBy')">最新提问</a></li>
														<li><a href="javascript:void(0)" onclick="gets_value('31',this,'orderByC','orderBy')">回答最多</a></li>
		                                            </ul>
	                                        	</div>
	                                    	</div>
	                                    	
	                                    	<div class="table-toolbar ps-margin">
				                                        <div class="btn-group cond" id="moreCondition_Div">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
				                                            	<c:choose>
				                                            			<c:when test="${not empty question.startDate || not empty question.endDate}">
				                                            				<font style="font-weight:bold;">筛选中/font>
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
																	<span class="btn-xs">起止时间：</span>
						                                        	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${question.startDate}" id="startDate" name="startDate" 
																		placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
																		<span>~</span>
																		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${question.endDate}" id="endDate"  name="endDate"
																		placeholder="结束时间" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}'})"/>
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
												<input id="title" name="title" value="${question.title}" class="form-control ps-input" type="text" placeholder="请输入关键字">
												<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
											</span>
										</div>
									</div>
									<div class="batchOpt" style="display: none">
										<div class="btn-group pull-left">
											<div class="table-toolbar ps-margin">
	                                        	<div class="btn-group">
													<a class="btn btn-default dropdown-toggle btn-xs" onclick="delQues()">
														批量删除
													</a>
	                                        	</div>
	                                    	</div>
										</div>
									</div>
								</form>
								<div class="widget-buttons ps-widget-buttons">
                                   	<button class="btn btn-primary btn-xs" type="button" title="创建提问" onclick="addQues()">
                                   		<i class="fa fa-plus"></i>
                                   		创建提问
                                   	</button>
                                   </div>
                            </div>
                             <c:choose>
                 <c:when test="${not empty list}">
                            <div class="widget-body">
                               <form action="/qas/delBatchQues" id="delForm" method="post">
									<input type="hidden" name="sid" value="${param.sid}"/>
									<input type="hidden" name="redirectPage" id="redirectPage"/>
                                	<ul class="messages-list">
                              			<c:forEach items="${list}" var="question" varStatus="vs">
                              				<li class="item first-item">
											<div class="message-content">
												<img class="message-head" width="50" height="50"
													src="/downLoad/userImg/${question.comId}/${question.userId}?sid=${param.sid}"
													title="${question.userName}" />
												<div class="checkbox pull-left ps-chckbox">
													<label>
														<input type="checkbox" class="colored-blue" value="${question.id}" name="ids" ${question.userId == userInfo.id?'':'disabled="disabled"'} >
														<span class="text"></span>
													</label>
												</div>
												<a href="javascript:void(0)"  class="item-box ${question.isRead==0?'noread':'' }" onclick="readMod(this,'qas',${question.id},'011');viewQues(${question.id})">
													<div class="content-text ws-texts" style="width: 90%">
														<tags:viewTextArea><tags:cutString num="300">${question.title}</tags:cutString></tags:viewTextArea>
													</div>
												</a>
												<div class="item-more">
													<a class="btn btn-default btn-xs" style="cursor: pointer;" 
														attentionState="${question.attentionState}" busType="011" busId="${question.id}" describe="1" iconSize="sizeMd"
														onclick="changeAtten('${param.sid}',this)">
														<i class="fa ${question.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>${question.attentionState==0?'关注':'取消'}
													</a>
													<span class="time"><i class="fa fa-clock-o"></i>${fn:substring(question.recordCreateTime,0,16)}</span>
													<span class="time"><i class="fa fa-user"></i>回答数：${question.ansTotal }</span>
												</div>
			                                </div>
										</li>
                              			</c:forEach>
								</ul>
							</form>
							<tags:pageBar url="/qas/listPagedQas"></tags:pageBar>
                          </div>
                          </c:when>
                <c:otherwise>
                	<div class="widget-body" style="height:550px; text-align:center;padding-top:170px">
						<section class="error-container text-center">
							<h1>
								<i class="fa fa-exclamation-triangle"></i>
							</h1>
							<div class="error-divider">
								<h2>还没有人提过问哦！</h2>
								<p class="description">协同提高效率，分享拉近距离。</p>
								<a href="javascript:void(0);" onclick="addQues();"
									class="return-btn"><i class="fa fa-plus"></i>创建提问</a>
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

