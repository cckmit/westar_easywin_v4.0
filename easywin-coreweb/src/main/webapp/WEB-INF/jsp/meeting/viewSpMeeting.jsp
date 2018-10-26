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
<script type="text/javascript" src="/static/js/meetJs/meeting.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/modFlowJs/modflow.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var EasyWin = {
		"sid" : "${param.sid}",
		"userInfo" : {
			"userId" : "${userInfo.id}",
			"userName" : "${userInfo.userName}",
			"comId" : "${userInfo.comId}",
			"orgName" : "${userInfo.orgName}",
			"isAdmin" : "${userInfo.admin}",
		},
		"pageOpt":"update",
		"homeFlag" : "${homeFlag}",
		"ifreamName" : "${param.ifreamName}",
        "modFlowConfig":{//流程配置信息
            "spFlag":"1",
            "flowId":"${meeting.flowId}",
            "pageOpt":"update",
            "busId":"${meeting.id}",
            "busType":"046",
            "actInstaceId":"${meeting.actInstaceId}",
            "executor":"${meeting.modSpConf.executor}",
            "stepType":"${meeting.modSpConf.stepType}",
            "content":"${meeting.modSpConf.content}"
        }
	};
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		//会议审批记录
		$("#spFlowRecordLi").click(function(){
			$(this).parent().find("li").removeAttr("class");
			$("#spFlowRecordLi").attr("class","active");
			$("#otherSpAttrIframe").attr("src","/modFlow/listSpHistory?sid=${param.sid}&busId=${meeting.id}&busType=046&doneState=${(meeting.spState==4 || meeting.spState==-1)?1:0}&spState=${meeting.spState}&ifreamName=otherSpAttrIframe");
		});
		//会议日志
		$("#spUpfileMenuLi").click(function(){
			$(this).parent().find("li").removeAttr("class");
			$("#spUpfileMenuLi").attr("class","active");
			$("#otherSpAttrIframe").attr("src","/modFlow/listSpFiles?sid=${param.sid}&busId=${meeting.id}&busType=046&doneState=${(meeting.spState==4 || meeting.spState==-1)?1:0}&ifreamName=otherSpAttrIframe");
		});

	})
	//确认会议
	function checkMeeting(state){
		var meetingId = '${meeting.id}';
		if(state==1){
			$.ajax({
				   type: "POST",
				   dataType: "json",
				   url: "/meeting/checkMeeting?sid=${param.sid}",
				   data:{meetingId:meetingId},
				   success: function(data){
					   
					 //若是有回调
						  if(data.status=='y'){
							  $.ajax({
									 type : "post",
									  url : "/meeting/ajaxAddMeetCheckUser?sid=${param.sid}&rnd="+Math.random(),
									  dataType:"json",
									  data:{state:state,meetingId:meetingId},
									  success : function(data){
										  if(data.status=='y'){
											  showNotification(1,"已确认参加会议");
											  $("#checkState").html("(确认参加)");
											  $("#checkStateOpt").remove();
											  $("#checkState").parent().css("color","#008000")
										  }else{
											  showNotification(2,data.info)
										  }
									  }
								});
						  }else{
							  meetings = data.meetings;
							  var content = '<table class="table table-striped table-hover general-table">';
							  content += '\n <thead>';
							  content += '\n 	<tr>';
							  content += '\n 		<th valign="middle" class="hidden-phone">会议名称</th>';
							  content += '\n 		<th style="width: 28%" valign="middle">开始时间</th>';
							  content += '\n 		<th style="width: 28%" valign="middle">结束时间</th>';
							  content += '\n 	</tr>';
							  content += '\n </thead>';
							  content += '\n <tbody>';
							  $.each(meetings,function(index,vo){
								  if('1'==vo.checkState){
									  content += '\n 	<tr style="color:green">';
								  }else if('2'==vo.checkState){
									  content += '\n 	<tr style="color:red">';
								  }else{
									  content += '\n 	<tr>';
								  }
								  content += '\n 		<td>';
								  content += '\n 		'+vo.title + (vo.id==meetingId ? "(待确认)":"");
								  content += '\n 		</td>';
								  content += '\n 		<td>';
								  content += '\n 		'+vo.startDate;
								  content += '\n 		</td>';
								  content += '\n 		<td>';
								  content += '\n 		'+vo.endDate;
								  content += '\n 		</td>';
								  content += '\n 	</tr>';
							  })
							  content += '\n </tbody>';
							  content += '\n</table>';
							  
							//定义对话框
								var html = constrDialog();
								var $html = $(html);
								$html.find(".ps-layerTitle").html("同时段有重叠会议");
								$html.find("#contentLayerBody").html(content);
								var contentT = $html.prop("outerHTML");
								
								layui.use('layer', function(){
									var layer = layui.layer;
									window.top.layer.open({
										//title: '修改表单信息',
										title:false,
										closeBtn:0,
										type:0,
										area:'550px',
										content:contentT,
										btn:['参会','拒绝'],
										success:function(layero,index){
											var p = $(window.top.document).find("#pContentLayerBody").parent();
											//设置样式
											$(p).css("padding","0px")
											$(window.top.document).find("#dislogCloseBtn").on("click",function(){
												window.top.layer.close(index)
											})
										},yes:function(index,layero){
											$.ajax({
												 type : "post",
												  url : "/meeting/ajaxAddMeetCheckUser?sid=${param.sid}&rnd="+Math.random(),
												  dataType:"json",
												  data:{state:state,meetingId:meetingId},
												  success : function(data){
													  if(data.status=='y'){
														  showNotification(1,"已确认参加会议");
														  $("#checkState").html("(确认参加)");
														  $("#checkStateOpt").remove();
														  $("#checkState").parent().css("color","#008000")
													  }else{
														  showNotification(2,data.info)
													  }
												  }
											});
										},btn2:function(index,layero){
											window.top.layer.close(index);
											window.top.layer.prompt({
												  formType: 2,
												  area:'400px',
												  closeBtn:0,
												  move: false,
												  title: '拒绝参会的事由描述'
												}, function(reason, index, elem){
													if(reason){
														$.ajax({
															 type : "post",
															  url : "/meeting/ajaxAddMeetCheckUser?sid=${param.sid}&rnd="+Math.random(),
															  dataType:"json",
															  data:{state:2,meetingId:meetingId},
															  success : function(data){
																  if(data.status=='y'){
																	  showNotification(1,"已拒绝参加会议");
																	  $("#checkState").html("(拒绝参加)");
																	  $("#checkUser_${userInfo.id}").attr("title",reason);
																	  $("#checkUser_${userInfo.id}").attr("style","cursor: pointer;");
																	  $("#checkStateOpt").remove();
																	  $("#checkState").parent().css("color","#FF0000")
																  }else{
																	  showNotification(2,data.info)
																  }
															  }
														});
													}else{
														showNotification(2,"请填写拒绝理由");
													}
												},function(reason, index, elem){
													return true;
												})
										}
									});
								});
						  }
					   }
				})
		}else if(state==2){
			window.top.layer.prompt({
				  formType: 2,
				  area:'400px',
				  closeBtn:0,
				  move: false,
				  title: '拒绝参会的事由描述'
				}, function(reason, index, elem){
					if(reason){
						$.ajax({
							 type : "post",
							  url : "/meeting/ajaxAddMeetCheckUser?sid=${param.sid}&rnd="+Math.random(),
							  dataType:"json",
							  data:{state:state,meetingId:meetingId,reason:reason},
							  success : function(data){
								  if(data.status=='y'){
									  showNotification(1,"已拒绝参加会议");
									  $("#checkState").html("(拒绝参加)");
									  $("#checkUser_${userInfo.id}").attr("title",reason);
									  $("#checkUser_${userInfo.id}").attr("style","cursor: pointer;");
									  $("#checkStateOpt").remove();
									  $("#checkState").parent().css("color","#FF0000")
								  }else{
									  showNotification(2,data.info)
								  }
							  }
						});
					}
				})
			return;
		}
	}

</script>
</head>
<body>
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">查看会议</span>
                        <div class="widget-buttons ps-toolsBtn" id="spBtnDiv">
                        	<a href="javascript:void(0)" class="blue">
                        	</a>
                        </div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
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
												<img
													src="/downLoad/userImg/${userInfo.comId}/${meeting.presenter}"
													title="${meeting.presenterName}" class="user-avatar"/>
												 <span class="user-name">${meeting.presenterName}</span>
											</div> 
										    <div class="pull-left gray ps-left-text margin-left-50">
										    	&nbsp;会议记录员
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<img src="/downLoad/userImg/${userInfo.comId}/${meeting.recorder}?sid=${param.sid}"
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
                                        <c:if test="${not empty meeting.meetingAddrName }">
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
																			<img class="user-avatar" src="/downLoad/userImg/${userInfo.comId}/${person.userId}">
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
                                        <c:if test="${not empty meeting.listNoticePerson}">
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
                                        <c:if test="${not empty meeting.content}">
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
                          
                          <div class="widget radius-bordered" style="clear:both;display:${meeting.spState==4?'block':'none'}">
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
											<c:when test="${not empty listMeetCheckUser}">
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
											 						<span id="checkState">
																		<c:choose>
																			<c:when test="${obj.userId==userInfo.id }">
																				<script>
																					$("#checkStateOpt").show()
																				</script>
																			</c:when>
																		</c:choose>
																				(未确认)
																	</span>
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
                          	 <div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                                            <li id="spFlowRecordLi" class="active"><a href="javascript:void(0);" data-toggle="tab">审批记录</a></li>
											<li id="spUpfileMenuLi"><a href="javascript:void(0);" data-toggle="tab">审批文档</a></li>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<iframe id="otherSpAttrIframe"
                                    	 		src="/modFlow/listSpHistory?sid=${param.sid}&busId=${meeting.id}&busType=046&doneState=${(meeting.spState==4 || meeting.spState==-1)?1:0}&spState=${meeting.spState}&ifreamName=otherSpAttrIframe"
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
                                    	 </div>
                                	</div>
                            	</div>
                            </div> 
                        </div>
					
					</div>
				</div>
			</div>
		</div>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
