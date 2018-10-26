<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<style>
	.mes_box{width:315px;height:165px;font-size:12px;margin:0;padding:0;}
	.mes_box_w{width:288px;margin:8px auto;}
	.mes_tit{height:28px;line-height:28px;}
	.fl{float:left;}
	.fr{float:right;}
	.mes_tit .fl{color:#287bba;font-weight:bold;}
	.mes_tit .fr{padding-left:18px;}
	.mes_con{font-size:12px;line-height:20px;text-indent:2em;height:160px;width:350px;overflow:auto; }
	#answerSub{width:90%;text-align: right;display: none;margin-left: 20px}
	.btn{
		padding-top:2px;
		padding-bottom: 2px;
	}
</style>

</head>
<body style="overflow: hidden;">

<div class="widget no-margin">
   <div class="widget-header bg-themeprimary">
       <span class="widget-caption" style="font-size: 18px">[消息提醒]</span>
       <div class="widget-buttons padding-top-10">
           <a href="javascript:void(0)" data-toggle="dispose" onclick="closeWindow()">
               <i class="fa fa-times darkorange"></i>
           </a>
       </div><!--Widget Buttons-->
   </div><!--Widget Header-->
   <div class="widget-body">
		<div class="mes_tit">
			<span  class="fl">当前消息：</span>
	   		<span class="fr"><font id="msgnum">${num}</font>条未读</span>
		</div>
		<div class="mes_con">
		   <p id="msgcontent">
		   		<c:choose>
					<c:when test="${todayWorks.isClock=='1' && todayWorks.busSpec=='0'}">
						[普通闹铃]
					</c:when>
					<c:when test="${todayWorks.isClock=='1' && todayWorks.busSpec=='1'}">
						[待办闹铃]
					</c:when>
					<c:when test="${todayWorks.busType=='1'}">
						[分享]
					</c:when>
					<c:when test="${todayWorks.busType=='015'}">
						[加入申请]
					</c:when>
					<c:when test="${todayWorks.busType=='017' && todayWorks.busSpec=='1'}">
						[参会确认]
					</c:when>
					<c:when test="${todayWorks.busType=='017'}">
						[会议]
					</c:when>
					<c:when test="${todayWorks.busType=='018'}">
						[会议申请]
					</c:when>
					<c:when test="${todayWorks.busType=='019'}">
						[普通消息]
					</c:when>
					<c:when test="${todayWorks.busType=='02201'}">
						[审批]
					</c:when>
					
					<c:when test="${todayWorks.busType=='016'}">
						[日程]
					</c:when>
					
					<c:when test="${todayWorks.busType=='027010'}">
						[用品采购审核]
					</c:when>
					<c:when test="${todayWorks.busType=='027011'}">
						[采购审核通知]
					</c:when>
					<c:when test="${todayWorks.busType=='027020'}">
						[用品申领审核]
					</c:when>
					<c:when test="${todayWorks.busType=='027021'}">
						[用品申领通知批]
					</c:when>
					<c:when test="${todayWorks.busType=='039'}">
						[公告]
					</c:when>
					<c:when test="${todayWorks.busType=='040'}">
						[制度]
					</c:when>
					<c:when test="${todayWorks.busType=='046'}">
						[会议审批]
					</c:when>
					<c:when test="${todayWorks.busType=='047'}">
						[会议纪要审批]
					</c:when>
					<c:when test="${todayWorks.busType=='099'}">
						[催办]
					</c:when>
					<c:when test="${todayWorks.busType=='0103'}">
						[领款通知]
					</c:when>
					<c:when test="${todayWorks.busType=='068'}">
						[联系人]
					</c:when>
					<c:when test="${todayWorks.busType=='06602'}">
						[完成结算]
					</c:when>
					<c:when test="${todayWorks.busType=='06601'}">
						[财务结算]
					</c:when>
					<c:when test="${todayWorks.busType=='067'}">
						[变更审批]
					</c:when>
					
					<c:otherwise>
						[${fn:substring(todayWorks.moduleTypeName,0,2)}]
					</c:otherwise>
				</c:choose>
				<a href="javascript:void(0)" onclick="viewDetailMod(${(todayWorks.busType==0 || todayWorks.busType=='019')?todayWorks.id:todayWorks.busId},'${todayWorks.busType}',${todayWorks.clockId},'${todayWorks.busSpec}')">
					<tags:cutString num="82">${todayWorks.busTypeName}</tags:cutString> 	
				</a>
				<br/>
			  	<c:if test="${not empty todayWorks.modifyerName}">
					<i>${todayWorks.modifyerName}:</i>
				</c:if>
				
				<c:choose>
					<c:when test="${todayWorks.roomId>0}">
						<a href="javascript:void(0)" onclick="toChat('${todayWorks.busId}','${todayWorks.busType}','${todayWorks.roomId}')">
						 ${todayWorks.content}
						</a>
					</c:when>
					<c:otherwise>
						<a href="javascript:void(0)" onclick="viewDetailMod(${(todayWorks.busType=='0' || todayWorks.busType=='019')?todayWorks.id:todayWorks.busId},'${todayWorks.busType}',${todayWorks.clockId},'${todayWorks.busSpec}')">
						 ${todayWorks.content}
						</a>
					</c:otherwise>
				</c:choose>
		   </p>
		 <div id="answerClick" style="width:90%;text-align: right;display:${needAns=='y'?'block':'none'}">
		 	<a href="javascript:void(0);" onclick="showAnswer();">[回复]</a>
		 </div>
		   <textarea id="answerContent" name="answerContent" class="form-control" style="width:90%;display: none;margin-left: 20px"></textarea>
		   <div id="answerSub" style="margin-top: 5px">
		   		<div id="feedbackTypeDiv">
					<c:choose>
						<c:when test="${not empty listFeedBackType}">
						   <select id="feedBackTypeId" class="no-padding-top no-padding-bottom no-padding-right" name="feedBackTypeId" style="width:150px;float: left;">
								<option value="0">反馈类型</option>
								<c:forEach items="${listFeedBackType}" var="feedBackType" varStatus="status">
								<option value="${feedBackType.id}">${feedBackType.typeName}</option>
								</c:forEach>
							</select>
						</c:when>
					</c:choose>
		   		</div>
			   	<a href="javascript:void(0);" id="answerMsgId" onclick="answerMsg(${todayWorks.busId},'${todayWorks.busType}');">[提交]</a>
			   	<a href="javascript:void(0);" onclick="canleAnsMsg();">[取消]</a>
		   </div>
		</div>
		
		<div class="mes_btn" style="position: absolute;bottom: 10px;left:20px">
		   	<button onclick="doReadOne();" id="nextmsg" class="btn btn-info btn-primary" style="background-color: #5599FF;padding-left: 5px;padding-right: 5px">${todayWorks.nextObjId>0?"下一条":"我知道了" }</button> 
		    <button onclick="otherMsg();" class="btn btn-info btn-primary" style="background-color: #5599FF;padding-left: 5px;padding-right: 5px">其他消息</button>
		    <button onclick="doAfterShow();" class="btn btn-info btn-primary" style="background-color: #5599FF;padding-left: 5px;padding-right: 5px" title="30分钟后显示">暂时关闭</button>
		    <button onclick="noShow();" class="btn btn-info btn-primary" style="background-color: #5599FF;padding-left: 5px;padding-right: 5px" title="关闭弹窗">关闭提醒</button>
		    <button onclick="readAll();" class="btn btn-info btn-primary" style="background-color: #5599FF;padding-left: 5px;padding-right: 5px">全部已读</button>
	   </div>

   </div><!--Widget Body-->
</div>






<script type="text/javascript">
//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
}

$(function(){
	$("#answerContent").autoTextarea({minHeight:50,maxHeight:50});  
});

var msgid='${todayWorks.id}';
var isClock ='${todayWorks.isClock}';
var busSpec = '${todayWorks.busSpec}';
var busType = '${todayWorks.busType}';

setInterval(function(){
	$.post('/msgShare/showMsgNoReadNum?sid=${param.sid}',{random : Math.random()},function(rs){
		if(rs.ifLogin==true){
			$('#msgnum').text(rs.num);
		}
	},'json');
},30000);
//显示回复框
function showAnswer(){
	$('#answerClick').hide();
	$('#answerContent').show();
	$('#answerSub').show();
	$('#answerContent').focus();
}
//取消回复
function canleAnsMsg(){
	$('#answerContent').val("");
	$('#answerClick').show();
	$('#answerContent').hide();
	$('#answerSub').hide();
}
function answerMsg(busId,busType){
	var answerContent = $('#answerContent').val();
	
	var feedBackTypeId = $("#feedBackTypeId").val();
	if(busType=='012'){
		if(feedBackTypeId==0){
			layer.tips("请填写反馈类型!","#feedBackTypeId",{tips:1})
			return;
		}
	}
	if(answerContent){
		$.post('/msgShare/answerMsg',{content:answerContent,busId:busId,
			busType:busType,feedBackTypeId:feedBackTypeId,sid:'${param.sid}'},function(){
			doReadOne();
			openWindow.changeNoReadNum(busType);
			canleAnsMsg();
		},'json');
	}else{
		layer.tips("请填写请填写回复内容","#answerContent",{tips:1})
	}
}
//当前设置已读，并查询下一条
function doReadOne(){
	var type = busType;
	if(isClock=='1'){
		type = '101';
	}else if(type=='1'){
		type = '100';
	}
	openWindow.changeNoReadNum(type);
	$.post('/msgShare/doReadOne?sid=${param.sid}',{id:msgid,random : Math.random()},function(rs){
		if(rs.ifLogin==true&&rs.num>0){
			$('#msgnum').text(rs.num);
			var work = rs.work;
			var html = getMsgHtml(work);
			$('#msgcontent').html(html);

			var needAns = rs.needAns;
			if('y'==needAns){//需要回复
				$("#answerClick").css("display","block");
				
				//反馈类型集合
				var listFeedBackType = rs.listFeedBackType;
				if(work.busType=='012'){//是客户模块，并且有反馈类型
					var feedbackTypeDivHtml = getFeedBackHtml(listFeedBackType);
					$("#feedbackTypeDiv").html(feedbackTypeDivHtml);
				}else{
					$("#feedbackTypeDiv").html('');
				}
				$("#answerMsgId").attr("onclick","answerMsg("+work.busId+",'"+work.busType+"')");
			}else{//不需要回复
				$("#answerClick").css("display","none")
			}
			msgid = work.id;
			isClock =work.isClock;
			busSpec = work.busSpec;
			busType = work.busType;
			
			if(rs.num==1){
				$('#nextmsg').text("我知道了");
			}
		}
		if(rs.ifLogin==true&&rs.num==0){
			//直接关闭会报错，故延迟200毫秒
			setTimeout(function(){
				window.top.layer.close(index);
			},200);
		}
	},'json');
}
//取得客户反馈类型
function getFeedBackHtml(listFeedBackType){
	var html = '<select id="feedBackTypeId" name="feedBackTypeId" style="width:150px;float: left;">';
	html+='\n<option value="0">反馈类型</option>';
	for(var i=0;i<listFeedBackType.length;i++){
		var feedBackType = listFeedBackType[i];
		html+='\n <option value="'+feedBackType.id+'">'+feedBackType.typeName+'</option>';
	}
	html+='\n</select>';
	return html;
}

//凭借显示消息的html
function getMsgHtml(work){
	var html = "";
	//业务类型
	var busType =work.busType;
	//是否为闹铃
	var isClock =work.isClock;
	//业务类别
	var busSpec =work.busSpec;
	//聊天室房间号
	var roomId = work.roomId;
	if(busType=='1'){
		html+='[分享]';
	}else if(isClock=='1' && busSpec=='0'){
		html+='[普通闹铃]';
	}else if(isClock=='1' && busSpec=='1'){
		html+='[待办闹铃]';
	}else if(busType=='015'){
		html+='[加入申请]';
	}else if(busType=='012'){
		html+='[客户]';
	}else if(busType=='006'){
		html+='[周报]';
	}else if(busType=='050'){
        html+='[分享]';
    }else if(busType=='003'){
		html+='[任务]';
	}else if(busType=='022' ||busType=='02201'){
		html+='[审批]';
	}else if(busType=='005'){
		html+='[项目]';
	}else if(busType=='011'){
		html+='[问答]';
	}else if(busType=='004'){
		html+='[投票]';
	}else if(busType=='013'){
		html+='[文档]';
	}else if(busType=='017' && busSpec=='1'){
		html+='[参会确认]';
	}else if(busType=='017'){
		html+='[会议]';
	}else if(busType=='018'){
		html+='[会议申请]';
	}else if(busType=='019'){
		html+='[普通消息]';
	}else if(busType=='027010'){
		html+='[用品采购审核]';
	}else if(busType=='027011'){
		html+='[采购审核通知]';
	}else if(busType=='027020'){
		html+='[用品申领审核]';
	}else if(busType=='027021'){
		html+='[用品申领通知]';
	}else if(busType=='047'){
		html+='[会移纪要审批]';
	}else if(busType=='099'){
		html+='[催办]';
	}else if(busType=='0103'){
		html+='[领款通知]';
	}else if(busType=='068'){
		html+='[联系人]';
	}else if(busType=='06602'){
		html+='[完成结算]';
	}else if(busType=='06601'){
		html+='[财务结算]';
	}else if(busType=='067'){
		html+='[变更审批]';
	}else if(busType=='016'){
		html+='[日程]';
	}
	if(roomId>0){//是聊天室
			html+='<a href="javascript:void(0)" onclick="toChat('+work.busId+',\''+busType+'\','+roomId+')">';
			html+=''+work.busTypeName;
			html+='</a>';
	}else{
		//闹铃或附件直接删除消息
		if(busType=='0' || busType=='013' || busType=='019'){
			html+='<a href="javascript:void(0)" onclick="viewDetailMod('+work.id+',\''+busType+'\','+work.clockId+',\''+busSpec+'\')">';
			html+=''+work.busTypeName;
			html+='</a>';
		}else{
			html+='<a href="javascript:void(0)" onclick="viewDetailMod('+work.busId+',\''+busType+'\','+work.clockId+',\''+busSpec+'\')">';
			html+=''+work.busTypeName;
			html+='</a>';
		}
		
	}
	html+='<br/>';
	if(work.modifyerName){
		html+='<i>'+work.modifyerName+':</i>';
	}
	if(busType=='0' || busType=='013' || busType=='019'){
		html+='<a href="javascript:void(0)" onclick="viewDetailMod('+work.id+',\''+busType+'\','+work.clockId+',\''+busSpec+'\')">';
		html+=''+work.content;
		html+='</a>';
	}else{
		html+='<a href="javascript:void(0)" onclick="viewDetailMod('+work.busId+',\''+busType+'\','+work.clockId+',\''+busSpec+'\')">';
		html+=''+work.content;
		html+='</a>';
	}
	return html;
}
//不在显示
function noShow(){
	artDialog.opener.noShow();
	art.dialog.close();
}
//全部已读
function readAll(){
	artDialog.opener.readAll();
	art.dialog.close();
}
var index = window.top.layer.getFrameIndex(window.name); //获取窗口索引
//查看详情
function viewDetailMod(id,type,clockId,busSpec){
	if(type=='0' || type=='013' || type=='019'){//闹铃和附件之间查看删除
		msgid =id;
		doReadOne()
		openWindow.changeNoReadNum(type=='0'?'101':type);
	}else{
		//openWindow.changeNoReadNum(type=='1'?'100':type);
		openWindow.viewModDetail(id,type,clockId,msgid,busSpec);
		doReadOne()
	}
}
//聊天
function toChat(busId,busType,roomId){
	//artDialog.opener.toChat(busId,busType,roomId);
}
//其他消息
function otherMsg(){
	openWindow.otherMsg();
	doReadOne();
}

/*半小时内不提示*/
function doAfterShow(){
	openWindow.doAfterShow();
	window.top.layer.close(index);
}
//不在显示
function noShow(){
	openWindow.noShow();
	window.top.layer.close(index);
}
//全部已读
function readAll(){
	openWindow.readAll();
	window.top.layer.close(index);
}

//关闭弹窗
function closeWindow(){
	openWindow.doAfterShow();
	window.top.layer.close(index);
}
/**
 * 查看详情
 *id 业务主键
 *type 业务类型
 *clockId 闹铃主键
 *msgid 提醒主键
 *busSpec 1待办事项 0普通事项
 */
</script>
</body>
</html>
