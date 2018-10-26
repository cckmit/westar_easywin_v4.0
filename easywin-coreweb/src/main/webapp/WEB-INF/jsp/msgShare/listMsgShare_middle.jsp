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
                        <div class="col-sm-12">
                            <div class="widget">
                                <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                                	<div class="checkbox ps-margin pull-left">
										<label>
											<input type="checkbox" class="colored-blue" onclick="checkAll(this,'ids')" id="checkAllBox">
											<span class="text">全选</span>
										</label>
									</div>
									<form id="searchForm" action="/msgShare/listPagedMsgNoRead">
										<input type="hidden" name="redirectPage"/>
										 <input type="hidden" id="pageSize" name="pager.pageSize" value="15"/>
										 <input type="hidden" id="readState" name="readState" value="${todayWorks.readState }"/>
										 <input type="hidden" name="sid" value="${param.sid}"/>
										 <input type="hidden" name="busType" id="busType" value="${todayWorks.busType}"/>
											<div class="btn-group pull-left">
												<%-- <div class="table-toolbar ps-margin">
		                                        	<div class="btn-group">
														<a class="btn btn-default dropdown-toggle btn-xs" data-toggle="dropdown">
															<c:choose>
																<c:when test="${todayWorks.readState eq 0}">
																	<font style="font-weight:bold;">未读消息</font>	
																</c:when>
																<c:when test="${todayWorks.readState eq 1}">
																	<font style="font-weight:bold;">已读消息</font>	
																</c:when>
																<c:otherwise>全部消息</c:otherwise>
															</c:choose>
															<i class="fa fa-angle-down"></i>
														</a>
														 <ul class="dropdown-menu dropdown-default">
															 <li><a href="javascript:void(0);"  onclick="setReadState('')">清空条件</a></li>
															<li><a href="javascript:void(0);"  onclick="setReadState('0')">未读消息</a></li>
															<!-- <li><a href="javascript:void(0);"  onclick="setReadState('1')">已读消息</a></li> -->
				                                            </ul>
				                                        </div>
		                                    		</div> --%>
		                                    		
		                                    		<div class="table-toolbar ps-margin">
				                                        <div class="btn-group cond" id="moreCondition_Div">
				                                            <a class="btn btn-default dropdown-toggle btn-xs" onclick="displayMoreCond('moreCondition_Div')">
				                                            	<c:choose>
				                                            			<c:when test="${not empty todayWorks.startDate || not empty todayWorks.endDate}">
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
																	<span class="btn-xs">起止时间：</span>
						                                        	<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${todayWorks.startDate}" id="startDate" name="startDate" 
																		placeholder="开始时间"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0});}'})"/>
																		<span>~</span>
																		<input class="form-control dpd2 no-padding condDate" type="text" readonly="readonly" value="${todayWorks.endDate}" id="endDate"  name="endDate"
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
														<input id="content" name="content" class="form-control ps-input" 
														type="text" placeholder="请输入关键字" value="${todayWorks.content}">
														<a href="javascript:void(0)" class="ps-searchBtn"><i class="glyphicon glyphicon-search circular danger"></i></a>
													</span>
												</div>
										</form>
										<div class="widget-buttons ps-widget-buttons">
		                                    	<button class="btn btn-xs" type="button" id="readWork">
		                                    		标识已读
		                                    	</button>
		                                    	<button class="btn btn-xs" type="button" id="readAll">
		                                    		全部已读
		                                    	</button>
		                                    </div>
                               		 </div>
	                                <div class="widget-body" id="contentBody">
	                                <c:choose>
                    					<c:when test="${not empty list}">
			                               <form  method="post" id="delForm">
											 <input type="hidden" name="redirectPage"/>
											 <input type="hidden" name="sid" value="${param.sid}"/>
			                                	<ul class="messages-list" id="msgListUl">
				                                	<c:choose>
				                                		<c:when test="${not empty list}">
				                                			<c:forEach items="${list}" var="obj" varStatus="vs">
				                                				<li class="item first-item">
																	<div class="message-content">
																		<img class="message-head" width="50" height="50"
																			src="/downLoad/userImg/${obj.comId}/${obj.modifyer}?sid=${param.sid}"
																			title="${obj.modifyerName}" />
																		<div class="content-headline">
																			<div class="checkbox pull-left ps-chckbox">
																				<label>
																					<input type="checkbox" class="colored-blue" value="${obj.id}" name="ids" ${obj.readState eq 0?'':'disabled="disabled"'} >
																					<span class="text"></span>
																				</label>
																			</div>
																			<span class="${obj.readState==0?'noread':'' }">
																				<c:choose>
																					<c:when test="${obj.isClock=='1'}">
																						[闹铃]
																					</c:when>
																					<c:when test="${obj.busType=='1'}">
																						[分享]
																					</c:when>
																					<c:when test="${obj.busType=='016'}">
																						[日程]
																					</c:when>
																					<c:when test="${obj.busType=='015'}">
																						[加入申请]
																					</c:when>
																					<c:when test="${obj.busType=='017' && obj.busSpec==1}">
																						[参会确认]
																					</c:when>
																					<c:when test="${obj.busType=='017'}">
																						[会议]
																					</c:when>
																					<c:when test="${obj.busType=='018'}">
																						[会议申请]
																					</c:when>
																					<c:when test="${obj.busType=='019'}">
																						
																					</c:when>
																					<c:when test="${obj.busType=='02201'}">
																						[审批]
																					</c:when>
																					<c:when test="${obj.busType=='027010'}">
																						[用品采购审核]
																					</c:when>
																					<c:when test="${obj.busType=='027011'}">
																						[采购审核通知]
																					</c:when>
																					<c:when test="${obj.busType=='027020'}">
																						[用品申领审核]
																					</c:when>
																					<c:when test="${obj.busType=='027021'}">
																						[用品申领通知]
																					</c:when>
																					<c:when test="${obj.busType=='039'}">
																						[公告]
																					</c:when>
																					<c:when test="${obj.busType=='040'}">
																						[制度]
																					</c:when>
																					<c:when test="${obj.busType=='046'}">
																						[会议审批]
																					</c:when>
																					<c:when test="${obj.busType=='047'}">
																						[会议纪要审批]
																					</c:when>
																					<c:when test="${obj.busType=='099'}">
																						[催办]
																					</c:when>
																					<c:when test="${obj.busType=='0103'}">
																						[领款通知]
																					</c:when>
																					<c:when test="${obj.busType=='068'}">
																						[联系人]
																					</c:when>
																					<c:when test="${obj.busType=='06602'}">
																						[完成结算]
																					</c:when>
																					<c:when test="${obj.busType=='06601'}">
																						[财务结算]
																					</c:when>
																					<c:when test="${obj.busType=='067'}">
																						[变更审批]
																					</c:when>
																					<c:otherwise>
																						[${fn:substring(obj.moduleTypeName,0,2)}]
																					</c:otherwise>
																				</c:choose>
																			</span>
																			<a href="javascript:void(0)" class="item-box ${obj.readState==0?'noread':'' }" isClock="${obj.isClock}" modId="${obj.busId}" modType="${obj.busType}"
																			onclick="readMod(this,'msgShare',${obj.busId},'${obj.busType}');viewDetailMod(${obj.id},${obj.busId},'${obj.busType}',${obj.clockId },${obj.busSpec})">
																				<tags:viewTextArea>
																					<tags:cutString num="82">${obj.busTypeName}</tags:cutString> 	
																				</tags:viewTextArea>
																			</a>
																		</div>
																		<c:if test="${not empty obj.content}">
																			<c:choose>
																				<c:when test="${obj.roomId>0}">
																					<a href="javascript:void(0);" class="item-box ${obj.readState==0?'noread':'' }" onclick="toChat('${obj.busType==0?obj.id:obj.busId}','${obj.busType}','${obj.roomId}')">
																						<div class="content-text">
																							<tags:viewTextArea>
																								<tags:cutString num="302">${obj.content}</tags:cutString>	
																							</tags:viewTextArea>
																						</div>
																					</a>
																				</c:when>
																				<c:otherwise>
																					<a href="javascript:void(0);" class="item-box ${obj.readState==0?'noread':'' }" isClock="${obj.isClock}" modId="${obj.busId}" modType="${obj.busType}"
																					onclick="readMod(this,'msgShare',${obj.busId},'${obj.busType}');viewDetailMod(${obj.id},${obj.busId},'${obj.busType}',${obj.clockId},${obj.busSpec })">
																						<div class="content-text">
																							<tags:viewTextArea>
																								<tags:cutString num="302">${obj.content}</tags:cutString>	
																							</tags:viewTextArea>
																						</div>
																					</a>
																				</c:otherwise>
																			</c:choose>
																		</c:if>
																		<div class="item-more">
																			<span class="time"><i class="fa fa-clock-o"></i>更新时间：${fn:substring(obj.recordCreateTime,0,20)}</span>
																			<c:if test="${not empty obj.modifyerName}">
																				<span class="time"><i class="fa fa-user"></i>更新人：${obj.modifyerName }</span>
																			</c:if>
																			<c:choose>
																				<c:when test="${obj.isClock==1}">
																				</c:when>
																				<c:when test="${obj.busType=='1' || obj.busType=='003' || obj.busType=='004' || obj.busType=='005'  || obj.busType=='011' || obj.busType=='012'}">
																					<a class="btn btn-default btn-xs" style="cursor: pointer;" 
																						attentionState="${obj.attentionState }" busType="${obj.busType}"  busId="${obj.busId}"  describe="1" iconSize="sizeMd"
																						onclick="changeAtten('${param.sid}',this)">
																						<i class="fa ${obj.attentionState==0?'fa-star-o':'fa-star ws-star'}"></i>${obj.attentionState==0?'关注':'取消'}
																					</a>
																				</c:when>
																				<c:otherwise>
																				</c:otherwise>
																			</c:choose>
																		</div>
									                                </div>
																</li>
				                                			</c:forEach>
				                                		</c:when>
				                                	</c:choose>
												</ul>
											</form>
											<tags:pageBar url="/msgShare/listPagedMsgNoRead"></tags:pageBar>
										</c:when>
					                	<c:otherwise>
					                		<div class="container" id="noMessage">
												<section class="error-container text-center" 
												style="position: absolute;left:40%;top:30%;">
													<h1>
														<i class="fa fa-exclamation-triangle"></i>
													</h1>
													<div class="error-divider">
														<h2>暂无系统消息需要您查看！</h2>
														<p class="description">协同提高效率，分享拉近距离。</p>
													</div>
												</section>
											</div>
					                	</c:otherwise>
					                	</c:choose>
	                                </div>
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

