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
		//页面加载完成后初始化
		$("#huiQianContent").autoTextarea({minHeight:80,maxHeight:169}); 
		$("#huiQianContent").css("width",$(window).width()*0.7);
	})
	
	function returnJoinConf(){
		var flag = checkFormData();
		if(flag){
			var joinConf = {};
			//会签反馈
			var huiQianContent = $("#huiQianContent").val();
			joinConf.huiQianContent = huiQianContent;
			
			//通知方式
			var msgSendFlag = $("#msgSendFlag").is(":checked");
			joinConf.msgSendFlag = msgSendFlag?1:0;
			
			return joinConf;
			
		}
	}
	//必填验证
	function checkFormData(){
		//会签人员检查
		var huiQianContent = $("#huiQianContent").val();
		if(!huiQianContent){
			layer.tips("请填写反馈说明！",$("#huiQianContent"),{"tips":1});
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
				<span class="themeprimary ps-layerTitle">会签反馈</span>
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
                        <li class="ticket-item no-shadow autoHeight no-padding">
					    	<div class="pull-left gray ps-left-text">
					    		会签说明
					    	</div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;">
						  		${spFlowHuiQianInfo.content}
							</div> 
							<div class="ps-clear"></div>              
                       	</li>
                        <li class="ticket-item no-shadow autoHeight no-padding">
					    	<div class="pull-left gray ps-left-text padding-top-10">
					    		反馈说明
					    		<font color="red">*</font>
					    	</div>
							<div class="ticket-user pull-left ps-right-box" style="height: auto;">
						  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
						  		style="height:80px;width:80%;" id="huiQianContent" 
						  		name="huiQianContent" rows="" cols=""></textarea>
								<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('huiQianContent','${param.sid}');" title="常用意见"></a>
							</div> 
							<div class="ps-clear"></div>              
                       	</li>
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
