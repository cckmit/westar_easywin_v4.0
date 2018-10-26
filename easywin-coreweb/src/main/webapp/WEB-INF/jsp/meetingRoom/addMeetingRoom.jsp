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
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
	var valid;
	var loadImgIndex;
	$(function() {
		valid = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		//form表单提交
		$("#addRoom").click(function(){
			//添加完成后查看
			$("#way").val("view");
			$(".subform").submit();
		});
		//form表单提交
		$("#addAndviewRoom").click(function(){
			//添加完成后继续添加
			$("#way").val("add");
			$(".subform").submit();
		});
	})
	
	//重写回调函数为空
	function artOpenerCallBack(args){}

	var arr = [ "gif", "jpg", "jpeg", "png", "bmp"];
	var picPath;

	var fileTypes="*.gif;*.jpg;*.jpeg;*.png;*.bmp;";
	var fileSize="200MB";
	var	numberOfFiles = 200;
	
	$(document).ready(function() {
	    $(".subform").keydown(function(e) {
	        if (e.keyCode == 13) {
	        	return false;
	        }
	    });
	 });
</script>
</head>
<body>
	<form class="subform" method="post" action="/meetingRoom/addMeetingRoom">
	<tags:token></tags:token>
	<input type="hidden" name="way" id="way">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">新增会议室</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="addRoom">
								<i class="fa fa-save"></i>添加
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
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;会议室名称
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input id="roomName" datatype="*" defaultLen="52" name="roomName" nullmsg="请填写会议室名称" 
												class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
											</div>
											<div class="ps-clear"></div>   
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;会议室地点
										    	<span style="color: red">*</span>
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input id="roomAddress" datatype="*" name="roomAddress"	 
												class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
											</div>
											<div class="ps-clear"></div>   
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;默认会议室
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<label class="padding-left-5">
												 	<input type="radio" class="colored-blue" name="isDefault" value="1"/>
												 	<span class="text" style="color:inherit;">是</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="radio" class="colored-blue" name="isDefault" value="0" checked="checked"></input>
												 	<span class="text" style="color:inherit;">否</span>
											    </label>
											</div>
											<div class="ps-clear"></div>   
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;会议室管理员
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:userOne datatype="s" name="mamager" defaultInit="true"
													showValue="${userInfo.userName}" value="${userInfo.id}" comId="${userInfo.comId}"
													onclick="true" showName="mamagerName"></tags:userOne> 
											</div>  
											<div class="ps-clear"></div>                
                                        </li>
                                        
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;容纳人数
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<div>
													<input class="colorpicker-default form-control " datatype="n" type="text" value="" ignore="ignore" 
														id="containMax" name="containMax" onpaste="return false;"  onkeypress="keyPress()" style="width: 200px;float: left">
												</div>
											</div>
											<div class="ps-clear"></div>   
                                         </li>
                                        <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;会议室描述
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
										  		style="height:150px;" id="content" name="content" rows="" cols=""></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
										<li class="ticket-item no-shadow autoHeight" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		&nbsp;会议室图片
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
													<div>
									      				<div>
									      					<div id="imgPicker" class="pull-left">选择图片</div>
									      					<span class="pull-left padding-left-10">
															  <input type="button" id="clearPic" value="清除" class="btn btn-info ws-btnBlue" style="display: none">
									      					</span>
														</div>
														<script type="text/javascript">
															loadUpfilesForMeetingRoom('${param.sid}','imgPicker','showpic0','fileMeetRoom','meetingRoomImg',0);
														</script>
													</div>
													<div id="showpic0" style="clear: both">
					 								</div>
												</div>
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
