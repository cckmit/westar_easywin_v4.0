<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<title><%=SystemStrConstant.TITLE_NAME %></title>
<link rel="stylesheet" type="text/css" href="/static/css/common.css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
</head>
<style>
.mes_box{width:315px;height:165px;font-size:12px;margin:0;padding:0;}
.mes_box_w{width:288px;margin:8px auto;}
.fl{float:left;}
.fr{float:right;}
.mes_tit{height:28px;line-height:28px;}
.mes_tit .fl{color:#287bba;font-weight:bold;}
.mes_tit .fr{background:url(/static/skin/blue/img/mes_icon.gif) no-repeat 0px 9px;padding-left:18px;}
.mes_con{font-size:12px;line-height:20px;text-indent:2em;height:160px;width:350px;overflow:auto; }
.mes_btn{float:right;}
.mes_btn a{display:inline-block;background:url(/static/skin/blue/img/mes_btn_bg.gif) repeat-x;height:21px;line-height:21px;border:1px solid #b6defa;padding:0px 5px;margin:0px 8px;color:#000; text-decoration:none;}
</style>
<body style="overflow: hidden;">
	<div  class="mes_box">
  <div class="mes_box_w">
   <div class="mes_tit"><span  class="fl">当前消息：</span><span class="fr"><font id="msgnum">${num }</font>条未读</span></div>
   <div class="mes_con">
   <p id="msgcontent">${message.content }</p>
   <br/>
   <div id="answerClick" style="width:90%;text-align: right;"><a href="javascript:void(0);" onclick="showAnswer();">[回复]</a></div>
   <textarea id="answerContent" name="answerContent" style="overflow-y:hidden;width: 90%;height: 40px;display: none;" onpropertychange="setAnswerHeight();" oninput="setAnswerHeight();" rows="" cols=""></textarea>
   <div id="answerSub" style="width:90%;text-align: right;display: none;"><a href="javascript:void(0);" onclick="answerMsg();">[提交]</a></div>
   </div>
   <div class="mes_btn" style="position: absolute;bottom: 10px;left: 35px"> 
     <span><a onclick="doReadOne();" href="#" id="nextmsg">${num>1?"下一条":"我知道了" }</a></span>
     <span><a onclick="otherMsg();" href="#">其他消息</a></span>
     <span><a onclick="doAfterShow();" href="#">暂时关闭</a></span>
     <span><a onclick="noShow();" href="#">关闭提醒</a></span>
   </div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("#answerContent").autoTextarea({maxHeight:500});  
});
var msgid=${message.id};
var sender=${message.sender};
setInterval(function(){
	$.post('/message/showMsgNoReadNum?sid=${param.sid}',{random : Math.random()},function(rs){
		if(rs.ifLogin==true){
			$('#msgnum').text(rs.num);
		}
	},'json');
},30000);


function answerMsg(){
	$.post('/message/answerMsg',{content:$('#answerContent').val(),receiver:sender,sid:'${param.sid}'},function(){
		doReadOne();
		$('#answerContent').val("");
		$('#answerClick').show();
		$('#answerContent').hide();
		$('#answerSub').hide();
	},'json');
}

//显示回复框
function showAnswer(){
	$('#answerClick').hide();
	$('#answerContent').show();
	$('#answerSub').show();
	$('#answerContent').focus();
}
//当前设置已读，并查询下一条
function doReadOne(){
	$.post('/message/doReadOne?sid=${param.sid}',{id:msgid},function(rs){
		art.dialog.opener.setMsgNum(rs.num);
		if(rs.ifLogin==true&&rs.num>0){
			$('#msgnum').text(rs.num);
			$('#msgcontent').text(rs.message.content);
			msgid = rs.message.id;
			sender = rs.message.sender;
			if(rs.num==1){
				$('#nextmsg').text("我知道了");
			}
		}
		if(rs.ifLogin==true&&rs.num==0){
			//直接关闭会报错，故延迟200毫秒
			setTimeout(function(){
				art.dialog.close();
			},200);
		}
	},'json');
}

function noShow(){
	artDialog.opener.noShow();
	art.dialog.close();
}

function otherMsg(){
	artDialog.opener.otherMsg();
}

/*半小时内不提示*/
function doAfterShow(){
	artDialog.opener.doAfterShow();
	art.dialog.close();
}
</script>
</body>
</html>
