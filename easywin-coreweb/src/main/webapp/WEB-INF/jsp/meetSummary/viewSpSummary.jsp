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
            "flowId":"${meetSummary.flowId}",
            "pageOpt":"update",
            "busId":"${meetSummary.id}",
            "busType":"047",
            "actInstaceId":"${meetSummary.actInstaceId}",
            "executor":"${meetSummary.modSpConf.executor}",
            "stepType":"${meetSummary.modSpConf.stepType}",
            "content":"${meetSummary.modSpConf.content}"
        }
	};
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
//会议属性菜单切换
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		callback:function (form){
			return sumitPreCheck(null)&&checkForm();
		},
		showAllError : true
	});
	//会议纪要信息
	$("#meetSummaryLi").click(function(){
		$("#meetSummaryBase").show();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").hide();
		$(this).parent().find("li").removeAttr("class");
		$("#meetSummaryLi").attr("class","active");
	});
	//会议主要信息
	$("#meetInfoLi").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").show();
		$("#otherAttrIframe").hide();
		$(this).parent().find("li").removeAttr("class");
		$("#meetInfoLi").attr("class","active");
	});
	//会议留言
	$("#meetTalkLi").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#meetTalkLi").attr("class","active");
		$("#otherAttrIframe").attr("src","/meeting/listPagedMeetTalk?sid=${param.sid}&pager.pageSize=10&meetingId=${meeting.id}");
	});
	//会议日志
	$("#meetLogLi").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#meetLogLi").attr("class","active");
		$("#otherAttrIframe").attr("src","/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${meeting.id}&busType=046&ifreamName=otherAttrIframe");
	});
	//会议审批记录
	$("#spFlowRecordLi").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#spFlowRecordLi").attr("class","active");
		$("#otherAttrIframe").attr("src","/modFlow/listSpHistory?sid=${param.sid}&busId=${meeting.id}&busType=046&doneState=1&spState=${meeting.spState}&ifreamName=otherAttrIframe");
	});
	//会议审批记录
	$("#summarySpFlowRecordLi").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#summarySpFlowRecordLi").attr("class","active");
		$("#otherAttrIframe").attr("src","/modFlow/listSpHistory?sid=${param.sid}&busId=${meetSummary.id}&busType=047&doneState=${(meetSummary.spState ==4 || meetSummary.spState ==-1)?1:0}&spState=${meetSummary.spState}&ifreamName=otherAttrIframe");
	});
	//会议附件
	$("#meetUpfileLi").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#meetUpfileLi").attr("class","active");
		$("#otherAttrIframe").attr("src","/meeting/listPagedMeetUpfiles?sid=${param.sid}&pager.pageSize=10&meetingId=${meeting.id}");
	});
	/* //会议查看记录信息
	$("#meetViewRecord").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#meetViewRecord").attr("class","active");
		$("#otherAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${meeting.id}&busType=046&ifreamName=otherAttrIframe");
	}); */
	//编辑会议纪要
	$("#edit").click(function(){
		$("#editSummary").css("display","block");
		$("#viewSummary").css("display","none");
		
		$("#edit").css("display","none");
		$("#edit").attr("class","fa fa-edit pull-left");
		
		$("#save").css("display","block");
		
		$("#cancle").css("display","block");
		$("#cancle").attr("class","fa pull-left");
	});
	
	
	//保存会议纪要
	$("#save").click(function(){
		$("#save").css("display","block");
		$("#save").attr("class","fa fa-save pull-left");
		
		$("#redirectPage").val(window.location.href);
		$("#addSummaryForm").submit();
	});
	//取消编辑会议纪要
	$("#cancle").click(function(){
		$("#editSummary").css("display","none");
		$("#viewSummary").css("display","block");
		
		$("#edit").css("display","block");
		$("#edit").attr("class","fa fa-edit pull-left");
		
		$("#save").css("display","none");
		
		$("#cancle").css("display","none");
		$("#cancle").attr("class","fa pull-left");
	});
	//会议查看记录信息
	$("#meetViewRecord").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#meetViewRecord").attr("class","active");
		$("#otherAttrIframe").attr("src","/common/listViewRecord?sid=${param.sid}&busId=${meeting.id}&busType=017&ifreamName=otherAttrIframe");
	});
	
	//会议学习
	$("#meetLearn").click(function(){
		$("#meetSummaryBase").hide();
		$("#meetInfoBase").hide();
		$("#otherAttrIframe").show();
		$(this).parent().find("li").removeAttr("class");
		$("#meetLearn").attr("class","active");
		$("#otherAttrIframe").attr("src","/meetSummary/meetLearnPage?sid=${param.sid}&id=${meeting.id}&ifreamName=otherAttrIframe");
	});
});

function checkForm(){
	//附件个数
	var filesLen = $("#listSummaryFile_upfileId").find("option").length;
	var summary = document.getElementById("eWebEditor1").contentWindow.getHTML();
	if(filesLen>0 || summary){//有会议纪要
		var stepData = $("#contentBody").data("stepData");
		if(stepData&& stepData.status == 'y'){
			 ModFlowBase.constrAddConfData($(".subform"))
		}
		return true;
	}else{
		window.top.layer.alert("请填写会议纪要");
		return false;
	}
}
//提交或送审会议纪要 spState 0直接发布 1送审
function addMeetSummary(spState){
	$("#redirectPage").val(window.location.href);
	if(spState && spState ==1){//送审
		var checKState = checkForm();
		if(checKState){
			ModFlowBase.spFlowForSummarySelect();
		}
	}else{
		$("#addSummaryForm").submit();
	}
}
//重新上传会议纪要信息
function reUploadSummary(){
	var url = "/meetSummary/reUploadSummary?sid=${param.sid}&meetingId=${meeting.id}&meetSummaryId=${meetSummary.id}";
	window.self.location.replace(url);
}
</script>
<style type="text/css">
	.comment{
		word-break: break-all;
	}
</style>
</head>
<body >
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">会议纪要</span>
                        <div class="widget-buttons ps-toolsBtn" id="spBtnDiv">
							<a href="javascript:void(0)" class="blue">
							</a>
							<c:choose>
								<c:when test="${meetSummary.spState eq -1 && userInfo.id eq meeting.recorder }">
									<a href="javascript:void(0)" class="blue" onclick="reUploadSummary()">
										<i class="fa fa-edit" ></i>重新编辑
									</a>
								</c:when>
							</c:choose>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-10" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                           <div class="widget-body no-shadow">
                          	 <div class="widget-main ">
                                	 <div class="tabbable">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                                      		<li id="meetSummaryLi" class="active">
                                      			<a href="javascript:void(0);" data-toggle="tab">会议纪要</a>
                                      		</li>
                                      		<li id="meetInfoLi">
                                      			<a href="javascript:void(0);" data-toggle="tab">会议详情</a>
                                      		</li>
                                            <li id="meetTalkLi"><a href="javascript:void(0);" data-toggle="tab">会议留言<c:if test="${meeting.talkNum > 0}"><span style="color:red;font-weight:bold;">（${meeting.talkNum}）</span></c:if></a></li>
											<c:if test="${meeting.spState ne 0 }">
												<li id="spFlowRecordLi"><a href="javascript:void(0);" data-toggle="tab">会议审批记录</a></li>
											</c:if>
											<c:if test="${meetSummary.spState ne 0 }">
												<li id="summarySpFlowRecordLi"><a href="javascript:void(0);" data-toggle="tab">纪要审批记录</a></li>
											</c:if>
											<li id="meetLogLi"><a href="javascript:void(0);" data-toggle="tab">操作日志</a></li>
											<li id="meetUpfileLi"><a href="javascript:void(0);" data-toggle="tab">会议文档<c:if test="${meeting.fileNum > 0}"><span style="color:red;font-weight:bold;">（${meeting.fileNum}）</span></c:if></a></li>
											<!-- <li id="meetLearn"><a href="javascript:void(0);" data-toggle="tab">会议学习</a></li> -->
											<li id="meetViewRecord"><a href="javascript:void(0);" data-toggle="tab">阅读记录</a></li>
                                             <%--<li id="meetViewRecord">--%>
                                                 <%--<a data-toggle="tab" href="javascript:void(0)">最近查看</a>--%>
                                             <%--</li>--%>
                                    	</ul>
                                    	 <div class="tab-content tabs-flat">
                                    	 	<div id="meetSummaryBase" style="display:block;">
												<jsp:include page="./meetSpSummary_view.jsp"></jsp:include>
											</div>
                                    	 	<div id="meetInfoBase" style="display:none;">
												<jsp:include page="./meetInfoForSummary.jsp"></jsp:include>
											</div>
                                    	 	<iframe id="otherAttrIframe" style="display:none;" class="layui-layer-load"
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
</body>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</html>
