<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	})
	
	function userMoreCallBack(){
		var array = $("#listMeetPerson_userId").val();
		var sid = '${param.sid}';
		 $(".subform").ajaxSubmit({
		        type:"post",
		        url:"/meeting/invMeetUser?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        beforeSubmit:function (a,f,o){
		        	//参会人员
		    		var personlen = $("#listMeetPerson_userId").find("option").length;
		    		if(personlen==0 ){
		    			window.top.layer.alert("请选择邀请人员");
		    			return false;
		    		}
				}, 
		        traditional :true,
		        data:{"meetIngId":${meeting.id},"userIds":array},
		        success:function(data){
		        	if(data.status=='y'){
		        		var users = data.users;
		        		if(users && users.length>0){
	        				var img="";
	        				 for (var i=0, l=users.length; i<l; i++) {
     							img = img + "<div class=\"online-list margin-top-5 margin-left-5 margin-bottom-5\" style=\"float:left\" id=\"user_img_listMeetPerson_userId_"+users[i].id+"\" ondblclick=\"removeInvUser('listMeetPerson_userId',"+users[i].id+")\" title=\"双击移除\">";
     							img = img + "<img src=\"/downLoad/userImg/${userInfo.comId}/"+users[i].id+"?sid=${param.sid}\" class=\"user-avatar\"/>"
     							img = img + "<span class=\"user-name\">"+users[i].userName+"</span>"
     							img = img + "</div>"
	     					 }
		        			$("#MeetPerson_div").append(img);
		        			showNotification(1,"成功邀请");
		        		}
		        	}else{
		        		showNotification(2,data.info);
		        	}
		        }
		 });
	}
	
	function removeInvUser(divTag,userId){
		$.ajax({
			 type : "post",
			  url : "/meeting/delInvMeetUser?sid=${param.sid}&rnd="+Math.random(),
			  dataType:"json",
			  data:{meetIngId:${meeting.id},userId:userId},
			  success:function(data){
				  if(data.status=='y'){
					  $("#listMeetPerson_userId").find("option[value='"+userId+"']").remove();
					  $("#MeetPerson_div").find("#user_img_listMeetPerson_userId_"+userId).remove();
					  
					  showNotification(1,"成功移除");
				  }else{
					  showNotification(2,data.info);
				  }
			  }
		});
	}
</script>
</head>
<body>
<form class="subform" method="post">
<input type="hidden" name="id" id="id" value="${meeting.id}"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">邀请参会人员</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a class="btn btn-primary btn-xs margin-top-5 margin-bottom-5" title="人员多选" 
								onclick="userMore('listMeetPerson_userId','','${param.sid}','yes','');" 
								href="javascript:void(0);">邀请人员</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">本次受邀人员</span>
                               </div>
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               			<div style="width: 250px; float: left; display: none;">
											<select style="width: 100%; height: 100px;" id="listMeetPerson_userId" ondblclick="removeClick(this.id)" multiple="multiple" name="listMeetPerson.userId" moreselect="true" listvalue="userName" listkey="userId" list="listMeetPerson">
											</select>
										</div>
										<div id="MeetPerson_div" class="pull-left"></div>
                               		</div>
                               		<div class="ps-clear"></div>    
                               </div>
                           </div>
                           
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
												<img src="/downLoad/userImg/${meeting.comId}/${meeting.presenter}?sid=${param.sid}"
													title="${meeting.presenterName}" class="user-avatar"/>
												 <span class="user-name">${meeting.presenterName}</span>
											</div> 
										    <div class="pull-left gray ps-left-text margin-left-50">
										    	&nbsp;会议记录员
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<img src="/downLoad/userImg/${meeting.comId}/${meeting.recorder}?sid=${param.sid}"
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
										    	&nbsp;开始时间
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												${meeting.startDate}
											</div>               
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;结束时间
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												${meeting.endDate}
											</div>               
                                        </li>
                                        <c:if test="${not empty  meeting.meetingAddrName}">
	                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
											    	&nbsp;会议地点
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													${meeting.meetingAddrName}
												</div>               
	                                        </li>
                                        </c:if>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;提前提醒
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:viewDataDic type="aheadTime" code="${meeting.aheadTime}"></tags:viewDataDic>
											</div>               
                                        </li>
                                        <c:if test="${not empty meeting.listMeetPerson }">
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
																			class="online-list margin-left-5 margin-bottom-5">
																			<img class="user-avatar" src="/downLoad/userImg/${person.comId}/${person.userId}?sid=${param.sid}">
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
                                        <c:if test="${not empty meeting.listMeetDep }">
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
																			class="online-list margin-left-5 margin-bottom-5" >
																			<img class="user-avatar" src="/downLoad/userImg/${person.comId}/${person.userId}?sid=${param.sid}">
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
                                        <c:if test="${not empty meeting.content }">
	                                        <li class="ticket-item no-shadow autoHeight no-padding" >
										    	<div class="pull-left gray ps-left-text padding-top-10">
										    		&nbsp;会议内容
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
											  		<c:choose>
														<c:when test="${empty meeting.content}">
														</c:when>
														<c:otherwise>
															${meeting.content}
														</c:otherwise>
													</c:choose>
												</div> 
												<div class="ps-clear"></div>              
	                                         </li>
                                        </c:if>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                          
                         <div class="widget radius-bordered collapsed" style="clear:both">
			                               <div class="widget-header bg-bluegray no-shadow">
			                                   <span class="widget-caption blue">与会人员确认情况</span>
			                                   <div class="widget-buttons btn-div-full">
			                                      <a class="ps-point btn-a-full" data-toggle="collapse">
			                                    	<i class="fa fa-plus blue"></i>
			                                       </a>
			                                   </div>
			                               </div>
			                               <div class="widget-body no-shadow">
			                               		<div class="tickets-container bg-white">
			                               			<c:choose>
														<c:when test="${not empty listMeetCheckUser }">
															<c:forEach items="${listMeetCheckUser}" var="obj" varStatus="vs">
																<div id="checkUser_${obj.userId}" title="${obj.reason }" style="${empty obj.reason?'':'cursor: pointer'}"
																class="online-list margin-top-5 margin-left-5 margin-bottom-5 pull-left" >
													<div class="ticket-user pull-left other-user-box" >
														<img class="user-avatar" 
															src="/downLoad/userImg/${obj.comId}/${obj.userId}?sid=${param.sid}">
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
											 					<c:otherwise>
											 						(未确认)
											 					</c:otherwise>
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
                           
                           <div class="widget-body no-shadow">
                            </div> 
                        </div>
					
					</div>
				</div>
			</div>
		</div>
	</form>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
