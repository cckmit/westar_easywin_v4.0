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
		"pageOpt":"add",
		"homeFlag" : "${homeFlag}",
		"ifreamName" : "${param.ifreamName}",
        "modFlowConfig":{//流程配置信息
            "spFlag":"${param.spFlag}",
            "flowId":"${param.flowId}",
            "pageOpt":"add",
            "busType":"046"
        }
	};
	
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	var valid;
	$(function() {
		valid = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return addMeeting();
			},
			showAllError : true
		});
		$("#week").find("select").val("${dayWeek}")
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
	})
	//添加会议
	function addMeet(state){
		//发布
		$("#meetingState").val(state);
		$(".subform").submit();
	}
	$(document).ready(function() {
	    $(document).keydown(function(e) {
	        if (e.keyCode == 13) {
	        	return false;
	        }
	    });
	 });
	//申请会议室
	function applyRoom(){
		var startDate = $('#startDate').val();
		
		var nowDate = new Date().getTime();
		var beginTime = startDate+":00"
		var beginTimes = beginTime.substring(0, 10).split('-');
		beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
		var a = (nowDate - Date.parse(beginTime)) / 3600 / 1000;
		if(a>0){//当前时间大，不能添加
			layer.tips("会议开始时间不能小于当前时间",$('#startDate'),{tips:1})
			return false;
		}
		var endDate =  $('#endDate').val();
		
		var url = '/meetingRoom/listRoomForApply?sid=${param.sid}&startDate='+startDate+'&endDate='+endDate
		+'&roomId='+$("#meetingAddrId").val()+"&meetId=0";
		
		window.top.layer.open({
			  type: 2,
			  //title: "会议室申请",
			  title:false,
			  closeBtn: 0,
			  shadeClose: true,
			  shade: 0.5,
			  shift:0,
			  btn:['选择','关闭'],
			  fix: true, //固定
			  maxmin: false,
			  move: false,
			  area: ['1055px', '500px'],
			  content: [url,'no'], //iframe的url
			  yes: function(index, layero){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  var result = iframeWin.returnRoom();
				  if(result){
					var roomId = result.roomId;
					var roomName = result.roomName;
					var startDate = result.startDate;
					var endDate = result.endDate;
					if(roomId && startDate && endDate){
						$("#meetingAddrId").val(roomId);
						$("#meetingAddrName").val(roomName);
						$("#startDate").val(startDate);
						$("#endDate").val(endDate);
						$("#roomType").find("option[value='0']").attr("preRoomId",roomId);
						$("#roomType").find("option[value='0']").selected();
						$("#roomType").find("option[value='0']").text(roomName);
						
					}
					window.top.layer.close(index);
				  }
			  },btn2:function(index){
			  },success: function(layero,index){
				  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
				  $(iframeWin.document).find("#titleCloseBtn").on("click",function(){
					 window.top.layer.close(index);
				 })
			  }
			});
	}
	//设置会议地点
	function setRoomName(ts){
		//会议室类型（0公司会议室 1外部会议室）
		var roomType = $(ts).val();
		if(roomType==0){//公司会议室
			var roomName = $("#roomType").find("option[value='0']").text();
			$("#meetingAddrName").val(roomName);
		}else if(roomType==1){//外部会议室
			var address = $("#roomType").find("option[value='1']").text();
			//例子2
			window.top.layer.prompt({
			  formType: 0,
			  area:'400px',
			  style:{width:'350px'},
			  value: address,
			  closeBtn:0,
			  move: false,
			  title: '请输入会议室地点'
			}, function(meetingAddrName, index, elem){
				if(meetingAddrName && "<外部会议室>"!=meetingAddrName){
					$("#roomType").find("option[value='1']").text(meetingAddrName)
					$("#meetingAddrName").val(meetingAddrName);
				  	window.top.layer.close(index);
				}else{
					window.top.layer.alert("*请输入会议室地点",{title:false,closeBtn:0,icon:7,btn:['关闭']});
					return false;
				}
			})
			
			
		}
		
	}
	//添加会议
	function addMeeting(){
		//会议开始时间
		var startDate = $("#startDate").val();
		if(!startDate){
			layer.tips("请填写会议开始时间",$("#startDate"),{tips:1});
			return false;
		}
		var nowDate = new Date().getTime();
		var beginTime = startDate+":00"
		var beginTimes = beginTime.substring(0, 10).split('-');
		beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
		var a = (nowDate - Date.parse(beginTime)) / 3600 / 1000;
		if(a>0){//当前时间大，不能添加
			layer.tips("会议开始时间不能小于当前时间",$("#startDate"),{tips:1});
			return false;
		}
		
		//会议截止时间
		var endDate = $("#endDate").val();
		if(!endDate){
			layer.tips("请填写会议截止时间",$("#endDate"),{tips:1});
			return false;
		}
		//与会人员
		var personlen = $("#listMeetPerson_userId").find("option").length;
		var deplen = $("#listMeetDep_depId").find("option").length;
		if(personlen==0 && deplen==0){
			layer.tips("请选择参会人员",$("#MeetPersonDiv"),{tips:1});
			return false;
		}
		
		var roomType = $("#roomType").val();
		if(roomType=='0'){
			//会议室主键
			var roomId = $("#meetingAddrId").val();
			var result = checkRoomDisabled(startDate,endDate,roomId);
			if(!result){//不可以添加
				return false;
			}
		}
		//验证会议周期性
		var meetingType = $("#meetingType").val();
		if("0"==meetingType){
			$("#regularDate").val('')
		}else{
			//周期开始时间
			var startDateStr = $("#meetRegular_startDate").val();
			//周期结束时间
			var endDateStr = $("#meetRegular_endDate").val();
			if(!startDateStr){//周期开始时间为空
				layer.tips("周期开始时间不能为空",$("#meetRegular_startDate"),{tips:1});
				return false;
			}
			if(!endDateStr){//周期结束时间为空
				layer.tips("周期结束时间不能为空",$("#meetRegular_endDate"),{tips:1});
				return false;
			}
			if("1"==meetingType){//每天
				var regularDate = $("#day").find("input").val();
				if(parseInt(regularDate)<=0){
					layer.tips("周期时间非零",$("#day").find("input"),{tips:1});
					return false;
				}
				$("#regularDate").val(regularDate)
			}else if("2"==meetingType){//每周
				var regularDate = $("#week").find("select").val();
				$("#regularDate").val(regularDate)
				selectDate(null);
			}else if("3"==meetingType){//每月
				var regularDate = $("#month").find("input").val();
				if(parseInt(regularDate)<=0){
					layer.tips("周期时间非零",$("#day").find("input"),{tips:1});
					return false;
				}
				$("#regularDate").val(regularDate)
				selectDate(null);
			}
		}
		

        var flag = ModFlowBase.checkModFlowConfigForStart();
        if(flag){

            ModFlowBase.constrAddConfData($(".subform"))
            //提交前验证是否在上传附件
            return sumitPreCheck(null);
        }else{
            return false;
        }
	}
	//验证是否能够申请该会议室
	function checkRoomDisabled(start_date, end_date, section_id){
		//默认不能添加
		var _result = false;
		$.ajax({ 
		       type: "post", 
		       url: "/meetingRoom/checkRoomDisabled?sid=${param.sid}", 
		       data:{startDate:start_date,endDate:end_date,roomId:section_id,meetingId:0},
		       cache:false, 
		       async:false, 
		       dataType: "json", 
		       success: function(data){
		    	   if(data.status=='y'){
		    		   if(data.disAble=='0'){
		    			   _result = true;
		    		   }else{
		    			   window.top.layer.alert("该时间段被占用",{title:false,closeBtn:0,icon:7});
		    		   }
		    	   }else{
		    		   window.top.layer.alert(data.info);
		    	   }
		    	   
		       }
		});
		return _result;
	}
	
	/* 清除下拉框中选择的option */
	function removeMeetClickUser(id,selectedUserId) {
		var newId = id.replace("listMembers_memberId",'');
		if(newId=="meet"){
			id="listMeetPerson_userId";
		}else{
			id="listNoticePerson_userId";
		}
		var selectobj = document.getElementById(id);
		for ( var i = 0; i < selectobj.options.length; i++) {
			if (selectobj.options[i].value==selectedUserId) {
				selectobj.options[i] = null;
				break;
			}
		}
		$("#user_img_"+selectedUserId+"_"+newId).parent().remove();
		
		selected(id);
	}
	
	function setRegularDate(ts){
		var meetingType = $("#meetingType").val();
			$(".regularDate").css("display","none")
		if("0"==meetingType){
			$("#dateDiv").css("display","none");
		}else{
			$("#dateDiv").css("display","block");
			if("1"==meetingType){//每天
				$("#day").css("display","block")
			}else if("2"==meetingType){//每周
				$("#week").css("display","block")
				selectDate(null);
			}else if("3"==meetingType){//每月
				$("#month").css("display","block")
				selectDate(null);
			}
		}
	}
	//判断是否在周期时间范围内
	function selectDate(ts){
		var meetingType = $("#meetingType").val();
		var startDateStr = $("#meetRegular_startDate").val();
		var endDateStr = $("#meetRegular_endDate").val();
		if(startDateStr && endDateStr){
			if("1"==meetingType){//每天
			}else if("2"==meetingType){//每周
				var startDate = new Date(startDateStr);
				var endDate = new Date(endDateStr);
				var day = (endDate-startDate)/(1000*60*60*24);
				if(day<7){
					layer.alert("周期时间至少一周",{title:false,closeBtn:0,icon:7,btn:['关闭']});
				}
			}else if("3"==meetingType){//每月
				var regularDate = $("#month").find("input").val();
				var startMonth = parseInt(startDateStr.substring(5,7)); 
				var endMonth = parseInt(endDateStr.substring(5,7)); 
				if(startMonth<=endMonth && endMonth-startMonth<=1){
					var dateStartHead = startDateStr.substring(0,8);
					if(regularDate<10){
						dateStartHead = dateStartHead+"0"+regularDate;
					}else{
						dateStartHead = dateStartHead+regularDate;
					}
					var startDate = new Date(startDateStr);
					
					var dateEndHead = endDateStr.substring(0,8);
					if(regularDate<10){
						dateEndHead = dateEndHead+"0"+regularDate;
					}else{
						dateEndHead = dateEndHead+regularDate;
					}
					var endDate = new Date(endDateStr);
					if((startDate<=new Date(dateStartHead) 
						&& endDate>=new Date(dateStartHead))
						||(startDate<=new Date(dateEndHead) 
							&& 	endDate>=new Date(dateEndHead))){
					}else{
						layer.alert("周期时间不在所选时间范围内",{title:false,closeBtn:0,icon:7,btn:['关闭']});
					}
				}
			}
		}
	}
</script>
</head>
<body>
<form class="subform" method="post" action="/meeting/addMeeting">
<tags:token></tags:token>
<input type="hidden" name="meetingState" id="meetingState"/>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">添加审批会议</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="addMeet(1)">
								<i class="fa fa-save" ></i>提交
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
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline" >
                                         	<div class="pull-left gray ps-left-text">
										    	&nbsp;会议主持人
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:userOne datatype="s" name="presenter" defaultInit="true"
													showValue="${userInfo.userName}" value="${userInfo.id}" uuid="${userInfo.smImgUuid}"
													filename="${userInfo.smImgName}" gender="${userInfo.gender}" onclick="true"
													showName="presenterName"></tags:userOne>  
											</div> 
										    <div class="pull-left gray ps-left-text margin-left-50">
										    	&nbsp;会议记录员
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:userOne datatype="s" name="recorder" defaultInit="true"
													showValue="${userInfo.userName}" value="${userInfo.id}" uuid="${userInfo.smImgUuid}"
													filename="${userInfo.smImgName}" gender="${userInfo.gender}" onclick="true"
													showName="recorderName"></tags:userOne> 
											</div>               
										                  
                                        </li>
									
									
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;会议名称
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div>
													<input id="title" datatype="*" defaultLen="52" name="title" nullmsg="请填写会议名称" 
													class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
												</div>
											</div>
											 
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;会议地点
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input type="hidden" name="meetingAddrId" id="meetingAddrId" value="0">
												<input type="hidden" name="meetingAddrName" id="meetingAddrName" value="">
												<select class="populate"  id="roomType" name="roomType" onchange="setRoomName(this)" style="width:310px">
													<option value="0" preRoomId="0">&lt;公司会议室&gt;</option>
													<option value="1">&lt;外部会议室&gt;</option>
												</select>
											<button type="button" onclick="applyRoom()" class="btn btn-info ws-btnBlue">申请会议室</button>
											</div>               
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;开始时间
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input type="hidden" id="nowDate" value="${nowDate }">
												<input class="colorpicker-default form-control" type="text" value="${meeting.startDate}" id="startDate" name="startDate" readonly="readonly"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endDate\',{d:-0});}',minDate: '#F{$dp.$D(\'nowDate\',{d:-0});}',readOnly:true})">
											</div>               
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;结束时间
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control" type="text" value="${meeting.endDate }" id="endDate" name="endDate" readonly="readonly"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate: '#F{$dp.$D(\'startDate\',{d:-0})||$dp.$D(\'nowDate\',{d:-0});}',readOnly:true})">
											</div>               
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;提前提醒
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:dataDic type="aheadTime" name="aheadTime"></tags:dataDic>
											</div>
											<div class="pull-left gray ps-left-text padding-left-50">
										    	通知方式：
										    </div>
											<div class="ticket-user pull-left ps-right-box" >
												<label class="padding-left-5">
												 	<input type="checkbox" class="colored-blue" name="sendPhoneMsg" value="1"></input>
												 	<span class="text" style="color:inherit;">手机短信通知</span>
											    </label>
											</div> 
											<div class="ps-clear"></div>              
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;会议类型
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<select id="meetingType" name="meetingType" style="width: 255px;float: left" onchange="setRegularDate(this)">
													<option value="0">单次会议</option>
												</select>
												<input type="hidden" id="regularDate" name="meetRegular.regularDate" value="">
												<div style="float: left;margin-left: 50px" id="regularDateDiv">
													<div id="day" class="regularDate" style="display: none;margin-top: 5px">
														每<input type="text" class="colorpicker-default form-control" style="width: 35px;padding:0 0;display: inline;height:25px;" 
														maxlength="3" value="1" onpaste="return false;"  onkeypress="keyPress()">
															天
													</div>
													<div id="week" class="regularDate" style="display: none;margin-top: 5px">
														每周<select style="width: 55px;height:25px;padding:0 0;">
															<option value="2">周一</option>
															<option value="3">周二</option>
															<option value="4">周三</option>
															<option value="5">周四</option>
															<option value="6">周五</option>
															<option value="7">周六</option>
															<option value="1">周日</option>
														</select>
													</div>
													<div id="month" class="regularDate" style="display: none;margin-top: 5px">
														每月<input type="text" class="colorpicker-default form-control" style="width: 35px;padding:0 0;display: inline;height:25px;" 
														maxlength="3" value="1" onpaste="return false;"  onkeypress="keyPress()">
															号
													</div>
												</div>
											</div>               
                                        </li>
                                        <div style="display: none" id="dateDiv">
	                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
											    	&nbsp;
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													开始时间：
													<input class="colorpicker-default form-control" type="text" value="${fn:substring(nowDate,0,10)}" id="meetRegular_startDate" name="meetRegular.startDate" readonly="readonly"
														onfocus="WdatePicker({onpicked:function(dp){selectDate(dp.cal.getNewDateStr())},dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'meetRegular_endDate\',{d:-0});}',readOnly:true})" style="width:125px;padding:0 0;display: inline;height:25px;">
													结束时间：
													<input class="colorpicker-default form-control" type="text" value="" id="meetRegular_endDate" name="meetRegular.endDate" readonly="readonly"
														onfocus="WdatePicker({onpicked:function(dp){selectDate(dp.cal.getNewDateStr())},dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'meetRegular_startDate\',{d:-0});}',readOnly:true})" style="width:125px;padding:0 0;display: inline;height:25px;">
												</div>               
	                                        </li>
                                        </div>
                                         <li class="ticket-item no-shadow autoHeight no-padding">
										    <div class="pull-left gray ps-left-text padding-top-10">
										    	&nbsp;参会人员
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10" id="MeetPersonDiv">
													<tags:userMore name="listMeetPerson.userId" showName="userName" callBackStart="yes" disDivKey="MeetPersonImgDiv"></tags:userMore>
												</div>
											</div>
											<div class="ps-clear"></div>                 
                                        </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding">
										    <div class="pull-left gray ps-left-text padding-top-10">
										    	&nbsp;参会部门
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10">
													<tags:depMore name="listMeetDep.depId" showName="depName" disDivKey="MeetDepDiv"></tags:depMore>
												</div>
											</div>
											<div class="ps-clear"></div>                 
                                        </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding">
										    <div class="pull-left gray ps-left-text padding-top-10">
										    	&nbsp;告知人员
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10">
													<tags:userMore name="listNoticePerson.userId" showName="userName" callBackStart="yes" disDivKey="NoticePersonImgDiv"></tags:userMore>
												</div>
											</div>
											<div class="ps-clear"></div>                
                                        </li>
                                        <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;会议内容
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="content" name="content"></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                   	</ul>
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
</body>
</html>
