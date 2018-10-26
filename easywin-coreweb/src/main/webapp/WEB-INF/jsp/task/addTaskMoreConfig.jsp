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
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	$(function() {
		$("#taskRemark").autoTextarea({maxHeight:80});
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		//初始化母任务选择数据
		$("#itemName").keyup(function(){
			if(!strIsNull($("#itemName").val())){
				$.post("/task/itemJson?sid=${param.sid}",{Action:"post",id:0,itemName:$("#itemName").val()},     
				 function (msgObjs){
					var autoComplete=new AutoComplete("itemName","itemId","itemNameDiv",msgObjs);
					autoComplete.start(event);
				});
			}
		});
	})
	function formSub(){
		$(".subform").submit();
	}
	$(document).ready(function(){
			$("#taskName").focus();
			//信息分享操作
			$("#shareMsgCheckBox").click(function(){
				if($("#shareMsgCheckBox").attr("checked")){
					$("#content_wrap_id").css("display","block");
				}else{
					$("#content_wrap_id").css("display","none");
				}
			});
		});
	var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;
//关联项目返回函数
function itemSelectedReturn(id,name){
	var oldId = $("#itemId").val();
	if(oldId!=id){//不是同一个项目则阶段主键就不一样
		$("#stagedItemName").val('');
		$("#stagedItemId").val('');
	}
	
	$("#itemName").val(name);
	$("#itemId").val(id);
}
//项目阶段关联
function stagedItemSelected(){
	if(strIsNull($("#itemId").val())){
		showNotification(1,"请先选择需要关联的项目！");
	}else{
		stagedItemSelectedPage("${param.sid}",$("#itemId").val());
	}
}
//选择关联的项目阶段
function stegedSelectedReturn(id,name){
	$("#stagedItemName").val(name);
	$("#stagedItemId").val(id);
}
//双击移除项目
function removeItem(){
	$("#itemName").val('');
	$("#itemId").val('');
	$("#stagedItemName").val('');
	$("#stagedItemId").val('');
}
//双击移除项目阶段
function removeItemStage(){
	$("#stagedItemName").val('');
	$("#stagedItemId").val('');
}
//删除附件
function delTaskFile(obj){
	$(obj).parent().remove();
}
</script>
</head>
<body style="background-color: #fff">
<section class="panel">
<header class="panel-heading">任务详情</header>
<form class="subform" id="subformOfTask" method="post" action="/task/updateTaskForMoreConfig?sid=${param.sid}&<%=TokenManager.TOKEN_PARAM %>=<%=TokenManager.newToken(request) %>">
<tags:token></tags:token>
<input type="hidden" name="parentId" value="${task.parentId}"/>
<input type="hidden" name="id" value="${task.id}"/>
<div class="panel-body">
	<div class="ws-introduce2">
		<span><b class="ws-color">*</b>任务名称：</span>
		<div class="ws-form-group">
			<input class="colorpicker-default form-control" style="width:320px;" type="text" id="taskName" datatype="*" name="taskName" nullmsg="请填写任务名称" value="${task.taskName}">
		</div>
		<div class="ws-clear"></div>
	</div>
	<div class="ws-introduce2">
		<span><b class="ws-color">&nbsp;</b>发起人：</span>
		<div class="ws-head-Img">
			<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}"
				title="${userInfo.userName}" />
			<i class="ws-Name">${userInfo.userName}</i>
			<input type="hidden" id="owner" name="owner" value="${userInfo.id}"/>
		</div>
		<!-- 
		<div class="ws-form-group">
			<tags:userOne datatype="s" name="owner" defaultInit="true"
					showValue="${task.ownerName}" value="${task.owner}" uuid="${task.uuid}"
					filename="${task.filename}" gender="${task.gender}"
					showName="onwerName"></tags:userOne> 
		</div>
		 -->
		<span><b class="ws-color">*</b>办理人：</span>
		<div class="ws-form-group">
			<tags:userOne datatype="s" name="executor" defaultInit="true"
					showValue="${task.executorName}" value="${task.executor}" uuid="${task.executorUuid}"
					filename="${task.executorFileName}" gender="${task.executorGender}"
					showName="executorName"></tags:userOne> 
					<input type="hidden" id="executor" name="executor" />
		</div>
		<div class="ws-clear"></div>
	</div>
	<div class="ws-introduce2">
		<span>项目关联：</span>
		<div class="ws-form-group">
			<input type="hidden" name="itemId" id="itemId"/>
			<input class="colorpicker-default form-control pull-left" style="width:300px;" type="text" id="itemName" name="itemName" readonly="readonly" value="" ondblclick="removeItem()" title="双击移除">
			<a href="javascript:void(0);" class="fa fa-chain pull-left" title="项目关联" onclick="listItem('${param.sid}');"></a>
		</div>
		<div class="ws-clear"></div>
	</div>
	<div class="ws-introduce2">
		<span>项目阶段：</span>
		<div class="ws-form-group">
		    <input type="hidden" name="stagedItemId" id="stagedItemId"/>
			<input class="colorpicker-default form-control pull-left" style="width:300px;" type="text" id="stagedItemName" name="stagedItemName" readonly="readonly" value="" ondblclick="removeItemStage()" title="双击移除">
			<a href="javascript:void(0);" class="fa fa-folder-open pull-left" title="阶段文件夹选择" onclick="stagedItemSelected();"></a>
		</div>
		<div class="ws-clear"></div>
	</div>
	<div class="ws-introduce2">
		<span>办理时限：</span>
		<div class="ws-form-group">
			<input class="colorpicker-default form-control" style="width:150px;" readonly="readonly"
			id="dealTimeLimit" name="dealTimeLimit" onClick="WdatePicker({minDate:'%y-%M-{%d}'})" type="text" value="${task.dealTimeLimit}">
		</div>
		<div class="ws-clear"></div>
	</div>
	<div class="ws-introduce2">
		<span>委托说明：</span>
		<div class="ws-form-group">
			<textarea class="form-control" id="taskRemark" name="taskRemark" rows="" cols="" style="height:70px;width:320px;" placeholder="委托说明……">${task.taskRemark}</textarea>
		</div>
		<div class="ws-clear"></div>
	</div>
	<div class="panel-body">
		<div class="ws-textareaBox" style="margin-left:85px;">
			<div class="ws-notice">
				<c:choose>
				 	<c:when test="${not empty listTaskUpfile}">
				 		<c:forEach items="${listTaskUpfile}" var="taskFile" varStatus="status">
				 			<div>
				 			 <span style="float:left;">${taskFile.filename}</span>
				 			 <a href="javascript:void(0);" onclick="delTaskFile(this)" class="fa fa-times-circle-o" style="margin-left:10px;" title="删除"></a>
				 			 <input type="hidden" name="listUpfilesOfFirstStep[${status.count-1}].id" value="${taskFile.upfileId}"/>
				 			 <div style="clear: both;"></div>
				 			</div>
				 		</c:forEach>
				 	</c:when>
				 </c:choose>
				<tags:uploadMore name="listUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
			</div>
		</div>
	</div>
</div>
</form>
</section>
</body>
</html>
