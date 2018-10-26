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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			showAllError : true
		});
	})
	//表单提交
	function formSub(){
		var userLength = $("#reminderUsers_userId").find("option").length;
		if(userLength == 0){
			layer.tips("请选择催办对象","#busRemindUser_div",{tips:1});
			return false;
		}
		
		var content = $("#content").val();
		if(!content){
			layer.tips("请填写催办内容","#content",{tips:1});
			return false;
		}
		$(".subform").submit();
	}
	

	$(document).ready(function(){
		$("#content").autoTextarea({minHeight:70,maxHeight:80}); 

		$(".checkbox").on("click",function () {
			if($(this).is(":checked")){
			    $("#reminderUsers_userId").append("<option selected=\"selected\" value='"+$(this).val()+"' id='user_"+$(this).val()+"'>"+$(this).attr("userName")+"</option>")
			}else{
                $("#user_"+$(this).val()).remove();
			}
        });
	});
</script>
</head >
<body>
<div class="container" style="padding: 0px 0px;width: 100%">
	<div class="row" style="margin: 0 0">
		<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
		<input type="hidden" id="subState" value="0">
   		<div class="widget" style="margin-top: 0px;min-height:600px">
   			<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
            <div class="widget-caption">
				<span class="themeprimary ps-layerTitle">催办</span>
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
             		<form class="subform" method="post" action="/busRemind/addBusRemind">
					<tags:token></tags:token>
					<input type="hidden" name="redirectPage" value="${param.redirectPage}"/>
					<input type="hidden" name="busId" value="${busRemind.busId}"/>
					<input type="hidden" name="busType" value="${busRemind.busType}"/>
					<input type="hidden" name="busModName" value="${busRemind.busModName}"/>
					<ul class="tickets-list">
						<li class="clearfix ticket-item no-shadow autoHeight no-padding">
					    	<div class="pull-left gray ps-left-text padding-top-10">
					    		被催办人
					    	</div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;max-width: 380px" >
								<div class="pull-left gray ps-left-text padding-top-10">
									<div class="pull-left" style="width: 310px">
										<select id="reminderUsers_userId" multiple="multiple" style="display: none" list="listBusRemindUser" listkey="userId" name="listBusRemindUser.userId">
											<c:forEach items="${listRemindUser}" var="busRemindUser" varStatus="status">
												<option selected="selected" value="${busRemindUser.userId }" id="user_${busRemindUser.userId}">${busRemindUser.userName}</option>
											</c:forEach>
										</select>
										<div id="busRemindUser_div" class="clearfix" style="width:310px;">
											<c:forEach items="${listRemindUser}" var="busRemindUser" varStatus="status">
												<div class="ticket-item no-shadow clearfix ticket-normal">
														<%--<div class="ticket-user pull-left no-padding">--%>
														<label class="padding-left-5 pull-left padding-right-5" style="display: none;">
															<input type="checkbox" class="colored-blue checkbox" userName="${busRemindUser.userName}" value="${busRemindUser.userId}" checked/>
															<span class="text" style="color:inherit;"></span>
														</label>
															<span>${busRemindUser.userName}</span>
													<%--</div>--%>

												</div>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							<div class="ps-clear"></div>
                        </li>
                        <li class="ticket-item no-shadow autoHeight no-padding">
					    	<div class="pull-left gray ps-left-text padding-top-10">
					    		催办内容<font color="red">*</font>
					    	</div>
							<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
						  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10"
						  		style="height:55px;width:400px;" id="content"
						  		name="content" rows="" cols="" datatype="*">${defMsg}</textarea>
							</div>
							<div class="ps-clear"></div>
                       	</li>
                       	<li class="ticket-item no-shadow ps-listline" id="xxtzDiv">
						    <div class="pull-left gray ps-left-text">
								通知方式
						    </div>
							<div class="ticket-user pull-left ps-right-box">
								<label class="padding-left-5">
								 	<input type="checkbox" class="colored-blue" name="" value="1" checked disabled/>
								 	<span class="text" style="color:grey;">待办事项</span>
							    </label>
								<label class="padding-left-10">
								 	<input type="checkbox" class="colored-blue" name="msgSendFlag" value="1" checked></input>
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
