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
                            	 <span class="widget-caption themeprimary" style="font-size: 18px">团队配置</span>
                            </div>
                            <div class="widget-body">
                            	<div class="row">
                            		<div class="col-sm-6">
                            			<div class="tickets-list">
                            				<div class="widget-header bg-bluegray no-shadow margin-top-5 margin-bottom-5">
												<span class="widget-caption blue">考勤配置</span>
												<div class="widget-buttons btn-div-full">
												</div>
											</div>
                            				<!-- 
	                                         <div class="clearfix" style="border: 0; ">
											    <div class="pull-left gray ps-left-text" style="font-size: 15px">
											    	&nbsp;考勤规则:
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<div readonly="readonly" style="width: 350px;border: 1px solid #d5d5d5;">
														<span class="padding-left-10">${attenceRule.ruleName}</span>
													</div>
												</div>
	                                         </div>
                            				 -->
	                                         <div class="clearfix" style="border:0; ">
											    <div class="pull-left gray ps-left-text" style="font-size: 15px">
											    	&nbsp;考勤制度:
											    </div>
												<div class="ticket-user pull-left ps-right-box margin-left-20" id="ruleTypeDiv">
													<tags:radioDataDic type="ruleType" name="ruleType" value="${attenceRule.ruleType}"></tags:radioDataDic>
												</div>
	                                         </div>
	                                         <div class="clearfix" id="rule1" style="display: none">
		                                         <div class="clearfix" style="border: 0; ">
												    <div class="pull-left gray ps-left-text " style="font-size: 15px">
												    	&nbsp;考勤规则:
												    </div>
													<div class="ticket-user pull-left ps-right-box margin-left-10">
														<div readonly="readonly" style="width: 350px;border: 0px solid #d5d5d5;">
															<span class="padding-left-10">国家法定节假日</span>
														</div>
													</div>
		                                         </div>
		                                         <div class="clearfix" style="border: 0;">
												    <div class="pull-left gray ps-left-text " style="font-size: 15px">
												    	&nbsp;考勤时段:
												    </div>
													<div class="ticket-user pull-left ps-right-box padding-left-10 margin-left-20">
														<div class="attenceTimeDiv padding-bottom-10" style="height:40px;">
															<div class="dayTimeSSpan pull-left">
																<span class="pull-left padding-top-5 padding-right-10">签到</span>
																<input class="dpd2 dayTimeS padding-left-20 form-control pull-left" type="text" id="dayTimeS0" rowNum="0" readonly="readonly" style="width: 100px">
															</div>
															<div class="dayTimeESpan pull-left padding-left-20">
																<span class="pull-left padding-top-5 padding-right-10">签退</span>
																<input class="dpd2 dayTimeE padding-left-20 form-control pull-left" type="text" id="dayTimeE0" rowNum="0" readonly="readonly" style="width: 100px">
															</div>
															<div class="optSpan padding-top-5 pull-left padding-left-20">
																<a href="javascript:void(0)" class="fa fa-times fa-lg padding-left-10 delBtn"></a>
																<a href="javascript:void(0)" class="fa fa-plus fa-lg padding-left-10 addBtn"></a>
															</div>
														</div>
													</div>
		                                         </div>
	                                         </div>
	                                         <div id="rule3" style="display: none">
		                                         <div class="clearfix" style="border: 0;">
												    <div class="pull-left gray ps-left-text " style="font-size: 15px">
												    	&nbsp;考勤规则:
												    </div>
													<div class="ticket-user pull-left ps-right-box margin-left-20">
														<button type="button" class="btn btn-blue pull-left">设定规则</button>
														<div class="pull-left margin-left-20 margin-top-5">
															<font color="red">(需维护节假日)</font>
														</div>
													</div>
		                                         </div>
		                                         <div id="rule3Details" class="clearfix padding-top-10" style="border: 0;display: none">
												    <div class="pull-left gray ps-left-text " style="font-size: 15px">
												    	&nbsp;具体规则:
												    </div>
													<div class="ticket-user pull-left ps-right-box margin-left-20">
														<table class="display table table-bordered table-striped">
															<thead>
																<tr>
																	<th>工作日</th>
																	<th>考勤时段</th>
																</tr>
															</thead>
															<tbody>
															</tbody>
														</table>
													</div>
		                                         </div>
	                                         </div>
	                                          <c:if test="${userInfo.admin!=0}">
		                                         <div class="margin-top-50">
			                                         <div class="ticket-item no-shadow padding-top-40 ps-listline" style="border: 0;text-align: center;">
														<button class="btn btn-primary" type="button" id="saveAtttenceRule">保存</button>
			                                         </div>
		                                         </div>
	                                          </c:if>
                                         </div>
                                         </form>
                            		</div>
                            		<div class="col-sm-6" id="orgCfgDiv">
                            			<div class="tickets-list">
                            				<div class="widget-header bg-bluegray no-shadow margin-top-5 margin-bottom-5">
												<span class="widget-caption blue">开关配置</span>
												<div class="widget-buttons btn-div-full">
												</div>
											</div>
											<div class="clearfix" style="border:0; ">
												    <div class="pull-left gray ps-left-text" style="font-size: 15px">
												    	&nbsp;直属上级设定验证:
												    </div>
													<div class="ticket-user pull-left margin-left-20" id="ruleTypeDiv">
														<ul class="list-group no-margin" style="border: 0;">
															<li class="list-group-item no-padding" style="border: 0;">
																<span class="pull-right"> 
																	<label class="no-margin" style="border: 0;">
																	<input class="checkbox-slider slider-icon colored-blue" name="leaderCfg"
																		type="checkbox"  checked="checked" style="height: 15px;"
																		value="1" onclick="switchSet(this,'${param.sid}');">
																		<span class="text"></span>
																	</label> 
																</span>
															</li>
														</ul>
													</div>
		                                         </div>
										</div>
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
