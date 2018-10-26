<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
<div class="widget-body">
        	<div class="widget radius-bordered">
           		<div class="widget-header bg-bluegray no-shadow">
	               	<span class="widget-caption blue">基础信息</span>
	               	<div class="widget-buttons btn-div-full">
	                   	<a class="ps-point btn-a-full" data-toggle="collapse">
	                       	<i class="fa fa-minus blue"></i>
	                      </a>
	                    </div>
	                </div>
                     <div class="widget-body no-shadow">
                      	<div class="tickets-container bg-white">
						<ul class="tickets-list">
							<li class="ticket-item no-shadow ps-listline" >
								<div class="pull-left gray ps-left-text">
							    	&nbsp;会议主持人
							    </div>
							<div class="ticket-user pull-left ps-right-box">
								<img src="/downLoad/userImg/${userInfo.comId}/${meeting.presenter}"
									title="${meeting.presenterName}" class="user-avatar"/>
								 <span class="user-name">${meeting.presenterName}</span>
							</div> 
						    <div class="pull-left gray ps-left-text margin-left-50">
						    	&nbsp;会议记录员
						    </div>
							<div class="ticket-user pull-left ps-right-box">
								<img src="/downLoad/userImg/${userInfo.comId}/${meeting.recorder}"
									title="${meeting.recorderName}" class="user-avatar"/>
								 <span class="user-name">${meeting.recorderName}</span> 
							</div>               
      					</li>
						<li class="ticket-item no-shadow ps-listline">
						    <div class="pull-left gray ps-left-text">
						    	&nbsp;会议名称
						    </div>
							<div class="ticket-user pull-left ps-right-box">
							  	<strong>${meeting.title }</strong>
							</div>
						</li>
						<li class="ticket-item no-shadow ps-listline" style="clear:both">
						    <div class="pull-left gray ps-left-text">
						    	&nbsp;会议时间
						    </div>
							<div class="ticket-user pull-left ps-right-box">
								${meeting.startDate} —— ${meeting.endDate}
							</div>               
						</li>
                         <c:if test="${not empty meeting.meetingAddrName}">
                           <li class="ticket-item no-shadow ps-listline">
							    <div class="pull-left gray ps-left-text">
							    	&nbsp;会议地点
							    </div>
								<div class="ticket-user pull-left ps-right-box">
									${meeting.meetingAddrName}
								</div>               
		        			</li>
                   		</c:if>
						 <c:if test="${not empty meeting.content}">
							<li class="ticket-item no-shadow autoHeight no-padding" >
						    	<div class="pull-left gray ps-left-text padding-top-10">
						    		&nbsp;会议内容
						    	</div>
								<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
							  		<div class=" margin-top-10 margin-bottom-10">${meeting.content}</div>
								</div> 
								<div class="ps-clear"></div>              
							</li>
						</c:if>
                                        <c:if test="${not empty meeting.listMeetPerson}">
	                                         <li class="ticket-item no-shadow autoHeight no-padding">
											    <div class="pull-left gray ps-left-text padding-top-10">
											    	&nbsp;参会人员
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="height: auto;">
													<div class="pull-left gray ps-left-text padding-top-10" id="MeetPersonDiv">
														<div style="max-width: 460px;" class="pull-left">
															<c:choose>
																<c:when test="${not empty meeting.listMeetPerson }">
																	<c:forEach items="${meeting.listMeetPerson}" var="person" varStatus="vs">
																		<div style="float: left; cursor: pointer;" 
																			class="online-list margin-top-5 margin-left-5 margin-bottom-5">
																			<img class="user-avatar" src="/downLoad/userImg/${userInfo.comId}/${person.userId}?sid=${param.sid}">
																			<span class="user-name">${person.personName}</span>
																		</div>
																	</c:forEach>
																</c:when>
															</c:choose>
														</div>
													</div>
												</div>
												<div class="ps-clear"></div>                 
	                                        </li>
                                        </c:if>
                                        <c:if test="${not empty meeting.listMeetDep}">
	                                         <li class="ticket-item no-shadow autoHeight no-padding">
											    <div class="pull-left gray ps-left-text padding-top-10">
											    	&nbsp;参会部门
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="height: auto;">
													<div class="pull-left gray ps-left-text padding-top-10">
														<div style="max-width: 460px;" id="MeetDepDiv" class="pull-left">
															<c:choose>
																<c:when test="${not empty meeting.listMeetDep}">
																	<c:forEach items="${meeting.listMeetDep}" var="dep" varStatus="vs">
																		<span style="cursor: pointer;"class="label label-default margin-top-5 margin-right-5 margin-bottom-5">
																		 	${dep.depName}
																		</span>
																	</c:forEach>
																</c:when>
															</c:choose>
														</div>
													</div>
												</div>
												<div class="ps-clear"></div>                 
	                                        </li>
                                        </c:if>
                                        <c:if test="${not empty meeting.listNoticePerson }">
	                                         <li class="ticket-item no-shadow autoHeight no-padding">
											    <div class="pull-left gray ps-left-text padding-top-10">
											    	&nbsp;告知人员
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="height: auto;">
													<div class="pull-left gray ps-left-text padding-top-10">
														<div style="max-width: 460px;" class="pull-left">
															<c:choose>
																<c:when test="${not empty meeting.listNoticePerson }">
																	<c:forEach items="${meeting.listNoticePerson}" var="person" varStatus="vs">
																		<div style="float: left; cursor: pointer;"  
																			class="online-list margin-top-5 margin-left-5 margin-bottom-5" >
																			<img class="user-avatar" src="/downLoad/userImg/${userInfo.comId}/${person.userId}">
																			<span class="user-name">${person.userName }</span>
																		</div>
																	</c:forEach>
																</c:when>
															</c:choose>
														</div>
													</div>
												</div>
												<div class="ps-clear"></div>                
	                                        </li>
                                        </c:if>
                                       
                                   	</ul>
                                </div>
                            </div>
                          </div>
                          
                          <div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">与会人员确认情况</span>
                                   <div class="widget-buttons btn-div-full">
                                      <a class="ps-point btn-a-full" data-toggle="collapse">
                                    	<i class="fa fa-minus blue"></i>
                                       </a>
                                   </div>
                               </div>
                               <div class="clearfix widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               			<c:choose>
											<c:when test="${not empty listMeetCheckUser }">
												<c:forEach items="${listMeetCheckUser}" var="obj" varStatus="vs">
													<div  title="${obj.reason }" style="${empty obj.reason?'':'cursor: pointer'}"  
													class="online-list margin-top-5 margin-left-5 margin-bottom-5 pull-left" >
													<div class="ticket-user pull-left other-user-box" >
														<img class="user-avatar" src="/downLoad/userImg/${userInfo.comId}/${obj.userId}?sid=${param.sid}">
														<span class="user-name">${obj.userName}</span>
														<c:set var="color">
											 				<c:choose>
											 					<c:when test="${obj.state==1}">#008000</c:when>
											 					<c:when test="${obj.state==2}">#FF0000</c:when>
											 					<c:otherwise>#00bfff</c:otherwise>
											 				</c:choose>
														</c:set>
											 			<span style="color:${color}" title="${obj.reason}">
											 				<c:choose>
											 					<c:when test="${obj.state==1}">(已确认)</c:when>
											 					<c:when test="${obj.state==2}">(已拒绝)</c:when>
											 					<c:otherwise>(未确认)</c:otherwise>
											 				</c:choose>
											 			</span>
													</div>
													</div>
												</c:forEach>
											</c:when>
										</c:choose>
                               		</div>
                               </div>
                           </div>
                        </div>
</body>
</html>
