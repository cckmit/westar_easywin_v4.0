<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
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
<script type="text/javascript" src="/static/js/autoTextarea.js"></script>
<script type="text/javascript">
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
		
		var url = "/modFlow/ajaxListJoinUsers";
		var params={"busId":"${param.busId}",
				"busType":"${param.busType}",
				"actInstaceId":"${param.actInstaceId}",
				"sid":"${param.sid}"};
		postUrl(url, params, function(data){
			if(data.status=='y'){
				var list = data.list;
				var doneFirst = true;
				var doingFirst = true;
				$.each(list,function(index,modRunStepInfo){
					var endTime = modRunStepInfo.endTime;
					var doneFirstLi = $(".subform").find("ul").find("#doneFirstLi");
					var doingFirstLi = $(".subform").find("ul").find("#doingFirstLi");
					if(endTime){//首个会签完成的
						if(doneFirst){
							doneFisrt = false;
							doneFirst = false;
							doneFirstLi = $('<li class="clearfix ticket-item no-shadow autoHeight no-padding"></li>');
							$(doneFirstLi).attr("id","doneFirstLi");
							$(doneFirstLi).attr("userId",modRunStepInfo.assignee);
							$(doneFirstLi).attr("state","1");
							var text = $('<div class="pull-left gray ps-left-text padding-top-10"></div>');
							$(text).html('已会签人员：');
							$(doneFirstLi).append(text);
							
							var imgDiv = $('<div class="ticket-user pull-left ps-right-box" style="height: auto;max-width: 380px" ></div>');
							var tempDivA = $('<div class="pull-left gray ps-left-text padding-top-10"></div>')
							var tempDivB = $('<div class="pull-left" style="width: 450px"></div>')
							var tempDivC = $('<div class="pull-left" style="max-width:460px"></div>')
							$(tempDivC).attr("id","doneIngDiv");
							$(tempDivB).append($(tempDivC));
							$(tempDivA).append($(tempDivB));
							$(imgDiv).append($(tempDivA));
							$(doneFirstLi).append($(imgDiv));
							
							if(doingFirstLi && doingFirstLi.get(0)){
								$(doingFirstLi).before($(doneFirstLi))
							}else{
								$(".subform").find("ul").append($(doneFirstLi));
							}
						}
						var doneIngDiv = $(doneFirstLi).find("#doneIngDiv");
						
						var divInclude = $('<div class="online-list margin-left-5 margin-bottom-5 pull-left"></div>');
						var userImgUrl = "/downLoan/userImg/${userInfo.comId}/"+modRunStepInfo.assignee;
						var headImg = $('<img src="'+userImgUrl+'" class="user-avatar"/>');
						var headName= $('<span class="user-name"></span>');
						$(headName).html(modRunStepInfo.assigneeName);
						$(divInclude).append($(headImg));
						$(divInclude).append($(headName));
						$(doneIngDiv).append($(divInclude));
						
						$(doneFirstLi).append($(imgDiv))
						
					}else{
						if(doingFirst){
							doingFirst = false;
							doingFirstLi = $('<li class="clearfix ticket-item no-shadow autoHeight no-padding"></li>');
							$(doingFirstLi).attr("id","doingFirstLi");
							$(doingFirstLi).attr("userId",modRunStepInfo.assignee);
							$(doingFirstLi).attr("state","0");
							var text = $('<div class="pull-left gray ps-left-text padding-top-10"></div>');
							$(text).html('会签中人员：');
							$(doingFirstLi).append(text);
							
							var imgDiv = $('<div class="ticket-user pull-left ps-right-box" style="height: auto;max-width: 380px" ></div>');
							var tempDivA = $('<div class="pull-left gray ps-left-text padding-top-10"></div>')
							var tempDivB = $('<div class="pull-left" style="width: 450px"></div>')
							var tempDivC = $('<div class="pull-left" style="max-width:460px"></div>')
							$(tempDivC).attr("id","doingIngDiv");
							$(tempDivB).append($(tempDivC));
							$(tempDivA).append($(tempDivB));
							$(imgDiv).append($(tempDivA));
							$(doingFirstLi).append($(imgDiv));
							$(".subform").find("ul").append($(doingFirstLi));
						}
						
						var doingIngDiv = $(doingFirstLi).find("#doingIngDiv");
						
						var divInclude = $('<div class="online-list margin-left-5 margin-bottom-5 pull-left"></div>');
						var userImgUrl = "/downLoad/userImg/${userInfo.comId}/"+modRunStepInfo.assignee;
						var headImg = $('<img src="'+userImgUrl+'" class="user-avatar"/>');
						var headName= $('<span class="user-name"></span>');
						$(headName).html(modRunStepInfo.assigneeName);
						$(divInclude).append($(headImg));
						$(divInclude).append($(headName));
						$(doingIngDiv).append($(divInclude));
						
						$(doingFirstLi).append($(imgDiv))
					}
				})
			}
		});
		//页面加载完成后初始化
		$("#content").autoTextarea({minHeight:80,maxHeight:169}); 
		$("#content").css("width",$(window).width()*0.7);
	})
	
	function returnJoinConf(){
		var flag = checkFormData();
		if(flag){
			var joinConf = {};
			//会签人员
			var jointProcessUsers = new Array();
			var options = $("#joinProgressUsers").find("option");
			$.each(options,function(index,option){
				 var joinUser = {"id":option.value,"userName":option.text};
				 jointProcessUsers.push(joinUser);
			 });
			joinConf.jointProcessUsers = jointProcessUsers;
			//会签说明
			var content = $("#content").val();
			joinConf.content = content;
			
			//通知方式
			var msgSendFlag = $("#msgSendFlag").is(":checked");
			joinConf.msgSendFlag = msgSendFlag?1:0;
			
			/* //是否覆盖
			var cover = $("#cover").is(":checked");
			joinConf.cover = cover?1:0; */
			return joinConf;
			
		}
	}
	
	function checkFormData(){
		//会签人员检查
		var joinProgressUsers = $("#joinProgressUsers").find("option");
		if(!joinProgressUsers || !joinProgressUsers.get(0)){
			layer.tips("请选择会签人员！",$("#joinProgressUsers").parent().parent().find("a"),{"tips":1});
			return false;	
		}
		//会签说明严重
		var content = $("#content").val();
		if(!content){
			layer.tips("请填写会签说明！",$("#content"),{"tips":1});
			return false;	
		}
		return true;
	}
</script>
</head >
<body style="background-color:#FFFFFF;">
<div class="container" style="padding: 0px 0px;width: 100%">	
	<div class="row" style="margin: 0 0">
		<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
   		<div class="widget" style="margin-top: 0px;min-height:600px">
   			<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
            <div class="widget-caption">
				<span class="themeprimary ps-layerTitle">审批会签</span>
			</div>
		 		<div class="widget-buttons">
				<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
					<i class="fa fa-times themeprimary"></i>
				</a>
				</div> 
             </div>
             
            <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
				<div class="widget radius-bordered">
                    <div class="widget-body no-shadow">
             		<form class="subform">
					<ul class="tickets-list">
						<li class="clearfix ticket-item no-shadow autoHeight no-padding">
					    	<div class="pull-left gray ps-left-text">
					    		&nbsp;会签人
					    		<font color="red">*</font>
					    	</div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;max-width: 380px" >
								<div class="pull-left gray ps-left-text">
									<div class="pull-left" style="width: 450px">
										<tags:userMore name="joinProgressUsers" showName="userName" datatype="*" disDivKey="joinProgressUsersDiv"></tags:userMore>
									</div>
								</div>
							</div>
							<div class="ps-clear"></div>
                        </li>
                        <li class="ticket-item no-shadow autoHeight no-padding">
					    	<div class="pull-left gray ps-left-text padding-top-10">
					    		会签说明<font color="red">*</font>
					    	</div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;">
						  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
						  		style="height:80px;width:80%;" id="content" 
						  		name="content" rows="" cols=""></textarea>
						  		<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('content','${param.sid}');" title="常用意见"></a>
							</div> 
							<div class="ps-clear"></div>              
                       	</li>
                       	<!-- <li class="ticket-item no-shadow ps-listline" id="xxtzDiv">
						    <div class="pull-left gray ps-left-text">
								覆盖会签：
						    </div>
							<div class="ticket-user pull-left ps-right-box">
								<label class="padding-left-5">
								 	<input type="checkbox" class="colored-blue" id="cover" name="cover" value="1"></input>
								 	<span class="text" style="color:inherit;">覆盖</span>
							    </label>
							</div>
						    <div class="ticket-user pull-left ps-right-box margin-left-10">
								<span class="fa fa-lg fa-info-circle blue sysTips" style="cursor: pointer;" 
									tipInfo="删除以前的会签记录；&lt;br&gt;只保留当前的会签记录。"></span>
							</div>
						</li> -->
                       	<li class="ticket-item no-shadow ps-listline" id="xxtzDiv">
						    <div class="pull-left gray ps-left-text">
								通知方式：
						    </div>
							<div class="ticket-user pull-left ps-right-box">
								<label class="padding-left-5">
								 	<input type="checkbox" class="colored-blue" id="msgSendFlag" name="msgSendFlag" value="1" checked="checked"></input>
								 	<span class="text" style="color:inherit;">手机短信通知</span>
							    </label>
							</div>
						</li>
                    </ul>
                   </form>
               </div>
          </div>
        </div>
        </div>
	</div>
</div>
</div>
</body>
</html>
