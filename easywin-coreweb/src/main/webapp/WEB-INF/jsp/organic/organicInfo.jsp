<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
        <div class="page-content">
         	<!-- Page Body -->
            <div class="page-body">
            	<div class="row">
                	<div class="col-md-12 col-xs-12 ">
                    	<div class="widget">
                         	<div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                            	 <span class="widget-caption themeprimary" style="font-size: 18px">团队信息</span>
                            </div>
                            <div class="widget-body">
                            	<div class="row">
                            		<div class="col-sm-6">
									<div class="form-title themeprimary" style="padding:5px 0;">基础信息</div>
                            		<form class="subform" action="/organic/updateOrganic" method="post" id="orgForm">
										<input type="hidden" value="${organic.id}" name="id"/>
										<input type="hidden" value="${param.sid}" name="sid"/>
										<input type="hidden" name="redirectPage"/>
                            			<ul class="tickets-list">
                            				<li class="ticket-item no-shadow ps-listline" style="border: 0; ">
											    <div class="pull-left gray ps-left-text ">
											    	&nbsp;团队名称：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<c:choose>
														<c:when test="${userInfo.admin==0}">
															<div readonly="readonly" style="width:390px;border: 1px solid #d5d5d5;">
																<span class="padding-left-10">${organic.orgName}</span>
															</div>
														</c:when>
														<c:otherwise>
															<input class="colorpicker-default form-control bordered-themeprimary" style="width:390px;" type="text" value="${organic.orgName}" name="orgName">
														</c:otherwise>
													</c:choose>
												</div>
	                                         </li>
                            			
	                                         <li class="ticket-item no-shadow ps-listline" style="border: 0; ">
											    <div class="pull-left gray ps-left-text">
											    	&nbsp;团队编号：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div readonly="readonly" style="width:390px;border: 1px solid #d5d5d5;">
														<span class="padding-left-10">${organic.orgNum}</span>
													</div>
												</div>
	                                         </li>
	                                         
	                                         <li class="ticket-item no-shadow ps-listline" style="border: 0; ">
											    <div class="pull-left gray ps-left-text ">
											    	&nbsp;联系人：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div readonly="readonly" style="width:390px;border: 1px solid #d5d5d5;">
														<span class="padding-left-10">${organic.linkerName}</span>
													</div>
												</div>
	                                         </li>
	                                         
	                                         <li class="ticket-item no-shadow ps-listline" style="border: 0 ">
											    <div class="pull-left gray ps-left-text ">
											    	&nbsp;联系电话：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div readonly="readonly" style="width:390px;border: 1px solid #d5d5d5;">
														<span class="padding-left-10">${organic.linkerMovePhone}</span>
													</div>
												</div>
	                                         </li>
	                                         <li class="ticket-item no-shadow ps-listline" style="border:0; ">
											    <div class="pull-left gray ps-left-text">
											    	&nbsp;联系邮箱：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div readonly="readonly" style="width:390px;border: 1px solid #d5d5d5;">
														<span class="padding-left-10">${organic.linkerEmail}</span>
													</div>
												</div>
	                                         </li>
	                                         
	                                         <li class="ticket-item no-shadow autoHeight no-padding" style="border: 0 ">
											    <div class="pull-left gray ps-left-text padding-top-10">
											    	&nbsp;联系地址：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="height: auto;">
													<c:choose>
														<c:when test="${userInfo.admin==0}">
															<div readonly="readonly" class="margin-top-10 padding-top-5 padding-bottom-10" style="width:390px;border: 1px solid #d5d5d5;">
																<span class="padding-left-10">${organic.address}</span>
															</div>
														</c:when>
														<c:otherwise>
															<input class="colorpicker-default form-control bordered-themeprimary" style="width:390px;" type="text" value="${organic.address}" name="address">
														</c:otherwise>
													</c:choose>
												</div>
												<div class="ps-clear"></div>
	                                         </li>
	                                          <c:if test="${userInfo.admin!=0}">
		                                         <li class="ticket-item no-shadow ps-listline" style="border: 0 ">
													<button class="btn btn-primary margin-left-20" type="submit">更新</button>
		                                         </li>
	                                          </c:if>
                                         </ul>
                                        </form>
										<div class="form-title themeprimary" style="padding:5px 0;">团队状态</div>
										<li class="ticket-item no-shadow ps-listline" style="border:0; ">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;服务状态：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly">
													<c:if test="${organic.inService==1 }">
														<span class="padding-left-10" style="color:green;font-weight:bold;">付费用户&nbsp;</span>
													</c:if>
													<c:if test="${organic.inService==0 }">
														<span class="padding-left-10" style="color:red;font-weight:bold;">免费&nbsp;</span>
													</c:if>
												</div>
											</div>
                                         </li>
										<li class="ticket-item no-shadow ps-listline" style="border:0; ">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;注册日期：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly">
													<span class="padding-left-10">${fn:substring(organic.recordCreateTime,0,10) }</span>
												</div>
											</div>
                                         </li>
										<li class="ticket-item no-shadow ps-listline" style="border:0; ">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;到期时限：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly">
													<span class="padding-left-10">
														<c:if test="${not empty organic.endDate }">
															${organic.endDate}&nbsp;
														</c:if>
														<c:if test="${userInfo.admin>0}">
															<a href="/order/order_center?sid=${param.sid}">升级</a>
														</c:if>	
													</span>
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="border:0; ">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;团队成员：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly">
													<span class="padding-left-10">${organic.members}/${organic.usersUpperLimit}</span>
													<span style="font-size:6px;color:gray;">（已使用名额 / 使用上限名额）</span>
												</div>
											</div>
                                         </li>
										<%--<li class="ticket-item no-shadow ps-listline" style="border:0; ">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;存储空间：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly">
													<span class="padding-left-10">200M/1000M</span>
													<span style="font-size:6px;color:gray;">（已使用空间 / 使用上限空间）</span>
												</div>
											</div>
                                         </li>
                            		--%>
                            		</div>
                            		<div class="col-sm-6">
                            			<img src="/static/images/pic.jpg"/>
                            		</div>
                            	
                            	</div>
                            </div>
                        </div>             
                </div>
                <!-- /Page Body -->
            </div>
            
        </div>
	<style>
	.ps-left-text{
		min-width: 80px !important;
		text-align: right !important;
	}
	</style>

</body>
</html>
