<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<link href="/static/2015/style/comm.css" rel="stylesheet" type="text/css" />
<link href="/static/2015/style/infoCss.css" rel="stylesheet" type="text/css" />
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/2015/js/popup_layer.js"></script>
<script type="text/javascript" src="/static/2015/js/jquery.qtip-1.0.0-rc3.js"></script>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript" src="/static/js/shareCode.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
$(function() {
		$('.subform').Validform({
			tiptype : function(msg, o, cssctl) {
			},
			showAllError : true
		});
	})
<%--
function openNewEdtior(){
	art.dialog.open("/msgShare/fullScreenEditor?style=fullScreenEditor2015&sid=${param.sid}", {
					title : '富文本编辑',
					lock : true,
					max : false,
					min : false,
					width : 690,
					height : 600
				});
}
--%>

function msgShareView(id,type){
	if('012'==type){//客户
		$.post("/crm/authorCheck?sid=${param.sid}",{Action:"post",customerId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/crm/viewCustomer?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('005'==type){//项目
		$.post("/item/authorCheck?sid=${param.sid}",{Action:"post",itemId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/item/viewItemPage?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('003'==type){//任务
		$.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/task/viewTask?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('006'==type){//周报
		$.post("/weekReport/authorCheck?sid=${param.sid}",{Action:"post",weekReportId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/weekReport/viewWeekReport?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('050'==type){//分享
        $.post("/daily/authorCheck?sid=${param.sid}",{Action:"post",dailyId:id},
            function (msgObjs){
                if(!msgObjs.succ){
                    showNotification(1,msgObjs.promptMsg);
                }else{
                    window.location.href="/daily/viewDaily?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
                }
            },"json");
    }else if('004'==type){//投票
		$.post("/vote/authorCheck?sid=${param.sid}",{Action:"post",voteId:id},     
				function (msgObjs){
				if(!msgObjs.succ){
					showNotification(1,msgObjs.promptMsg);
				}else{
					window.location.href="/vote/voteDetail?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
				}
		},"json");
	}else if('011'==type){//问答
		window.location.href="/qas/viewQuesPage?sid=${param.sid}&id="+id+"&redirectPage="+encodeURIComponent(window.location.href);
	}else{//普通分享
		parent.frames['iframe_body_right'].location.href="/msgShare/msgShareViewPage?sid=${param.sid}&id="+id+"&type="+type;
	}
	
}
//初始化个人分组Ztree
function initSelfGroupTree(selfGroupStr){
	var setting = {
		check: {
			enable: true,
			chkboxType: {"Y":"", "N":""}
		},
		view: {
			dblClickExpand: false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeClick: beforeClick,
			onCheck: onCheck
		}
	};
	initZTree(setting,"treeDemo","scopeTypeSel","idType","menuContent",selfGroupStr);
}
$(document).ready(function(){
	initSelfGroupTree(${selfGroupStr});
});
//-->
//打开表情div
document.onclick = function(e){
	   var evt = e?e:window.event;
	   var ele = evt.srcElement || evt.target;
	   if(ele.id == 'ele2'){
           if($("#blk2").css('display')=="none"){
           	$("#blk2").show();
           }else{
           	$("#blk2").hide();
           }
	   }else{
        $("#blk2").hide();
	  }
}
//关闭表情div，并赋值
function divClose(title,path){
	$("#blk2").css('display','none');
	insertAtCursor("content",title)
	$("#content").focus();
}
//滚动加载数据
var pageNum = 0;
var pageOffSet=2;
pageNum = pageNum + 1;
	function loadMeinv() {
	$.post("/msgShare/nextPageSizeMsgs?sid=${param.sid}&dataType=josn&pageSize=5&pageNum="+pageNum,function(msgObjs){
		var scrolltopPre = $(window.parent.document).height();
		//没有更多内容的时候隐藏
		if(msgObjs.length==0){
			$("#moreMsg").hide();
			return;
		}else{
			pageOffSet++;
			$("#moreMsg").show();
		}
		for (var i=0, l=msgObjs.length; i<l; i++) {
			if(undefined != $("#msg"+msgObjs[i].id).attr("class")){
				return;
			}else{
				var html = "";
				html ="<div class=\"new-list\" id=\"msg"+msgObjs[i].id+"\">";
				html+='<dl>'; 
				html+='<dt>'; 
				html+='<a class="nameInfo fl" onclick="javascript:void(0)"  rel="/userInfo/userInfoPage?sid=${param.sid}&id='+msgObjs[i].creator+'">'; 
				html+='<img src="/downLoad/userImg/'+msgObjs[i].comId+'/'+msgObjs[i].creator+'?sid=${param.sid}" width="50" height="50" title="' + msgObjs[i].creatorName + '" />';
				html+='</a>'; 
				html+='</dt>'; 
				html+='<dd>'; 
				html+='<h1 class="name">'+msgObjs[i].creatorName+'</h1>'; 
				html+='<p>'+msgObjs[i].content+'</p>'; 
				if(null!=msgObjs[i].vote){//是投票
					vote = msgObjs[i].vote;
					html+="<div id=\"msgVote"+vote.id+"\">";
					html+=getHtml(vote,'${param.sid}');
					html+="</div>";
					html+='<div class="b-info" style="clear: both;"><span class="fl"><a href="#" onclick="msgShareView('+vote.id+',\''+msgObjs[i].type+'\');">全文</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="#">加入</a></span><span class="fr">时间:'+msgObjs[i].createDate+'</span></div>'; 
				}else{//若是其他
					if(msgObjs[i].type==1){
						html+='<div class="b-info" style="clear: both;"><span class="fl"><a href="#" onclick="msgShareView('+msgObjs[i].id+',\''+msgObjs[i].type+'\');">全文</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="#">加入</a></span><span class="fr">时间:'+msgObjs[i].createDate+'</span></div>'; 
					}else{
						html+='<div class="b-info" style="clear: both;"><span class="fl"><a href="#" onclick="msgShareView('+msgObjs[i].modId+',\''+msgObjs[i].type+'\');">全文</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="#">加入</a></span><span class="fr">时间:'+msgObjs[i].createDate+'</span></div>'; 
					}
				}
				html+='</dd>'; 
				html+='</dl>'; 
				html+='</div>'; 
				//$minUl = getMinUl();
				$("#container").append(html);
				scrollPage();
			}
		var scrolltopAfter = $(window.parent.document).height();
		parent.window.scrollTo(0,$(window.parent.document).scrollTop()+(scrolltopAfter-scrolltopPre));
		}
	},'json');
	}
    var scrollFunc = function (e) { 
    	scrollPage();
        //滚动条所在的高度
       var topH = $(window.parent.document).scrollTop();
      	//已经滚动的部分
       var scrolH = $(window.parent.document).height()-$(window.parent.window).height();
        if((scrolH-topH)/100<=0){
        e = e || window.event;  
        if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件               
            if (e.wheelDelta > 0) { //当滑轮向上滚动时  
            }  
            if (e.wheelDelta < 0) { //当滑轮向下滚动时  
                scrollText() 
            }  
        } else if (e.detail) {  //Firefox滑轮事件  
            if (e.detail> 0) { //当滑轮向下滚动时  
                scrollText() 
            }  
            if (e.detail< 0) { //当滑轮向上滚动时  
            }  
        }  
       }
        
    }  
	//给页面绑定滑轮滚动事件  
    if (document.addEventListener) {//firefox  
        document.addEventListener('DOMMouseScroll', scrollFunc, false);  
    } 
    //滚动滑轮触发scrollFunc方法  //ie 谷歌  
    window.onmousewheel =scrollFunc; 
    preScro=0;
    window.parent.document.onkeydown=function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if(e && e.keyCode==34){ // 按 pagedown
        	 //滚动条所在的高度
            var topH = $(window.parent.document).scrollTop();
           	//已经滚动的部分
            var scrolH = $(window.parent.document).height()-$(window.parent.window).height();
             if((scrolH-topH)/100<=0){
	    		scrollText();
             }
         }
        if(e && e.keyCode==40){ // 按方向下
        	 //滚动条所在的高度
            var topH = $(window.parent.document).scrollTop();
           	//已经滚动的部分
            var scrolH = $(window.parent.document).height()-$(window.parent.window).height();
             if((scrolH-topH)/100<=0){
	    		scrollText();
             }
         }
    };
	function scrollText() {
		//$minUl = getMinUl();
		var a2 = $("#bottomDiv").offset();
		var obj2 =parent.document.getElementById("iframe_body_right");  //取得父页面IFrame对象
		if(preScro == a2.top){
			$("#moreMsg").hide();
			return;
		}
		preScro = a2.top
		if (obj2.height <= preScro) {
			//当最短的ul的高度比窗口滚出去的高度+浏览器高度大时加载新图片
			loadMeinv();
		}
	}
	function getMinUl() {//每次获取最短的ul,将图片放到其后
		var $arrUl = $("#container");
		var $minUl = $arrUl.eq(0);
		$arrUl.each(function(index, elem) {
			if ($(elem).height() < $minUl.height()) {
				$minUl = $(elem);
			}
		});
		return $minUl;
	}

$(function(){
	$("#ajaxSubmit").click(function(){
		if(strIsNull($("#content").val())){
			showNotification(1,"请编辑分享内容！");
			$("#content").focus();
			return false;
		}
		if(strIsNull($("#scopeTypeSel").val())){
			showNotification(1,"请编辑分享范围！");
			$("#scopeTypeSel").focus();
			return false;
		}
		$.post("/msgShare/ajaxAddMsgShare?sid=${param.sid}", { Action: "post", content:$("#content").val(),idType:$("#idType").val()},
			   function (data){

			  		var msgObjs = data.msgShare;
					var html = "";
					html ='<div class="new-list">';
					html+='<dl>';  
					html+='<dt>'; 
					html+='<a class="nameInfo fl" onclick="javascript:void(0)"  rel="/userInfo/userInfoPage?sid=${param.sid}&id='+msgObjs.creator+'">'; 
					html+='<img src="/downLoad/userImg/'+msgObjs.comId+'/'+msgObjs.creator+'?sid=${param.sid}" width="50" height="50"/>';
					html+='</a>'; 
					html+='</dt>';
					html+='<dd>'; 
					html+='<h1 class="name">'+msgObjs.creatorName+'</h1>'; 
					html+='<p>'+ msgObjs.content+'</p>'; 
					html+='<div class="b-info"><span class="fl"><a href="#" onclick="msgShareView('+msgObjs.id+',\''+msgObjs.type+'\');">全文</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="#">加入</a></span><span class="fr">时间:'+msgObjs.createDate+'</span></div>'; 
					html+='</dd>'; 
					html+='</dl>'; 
					html+='</div>'; 
					$("#container").prepend(html);
					$("#content").attr("value","");
			   }, "json");
	});
});


<%-- 此功能在管理员可以使用，非管理人员有问题

$(document).ready(function(){

	// 使用each（）方法来获得每个元素的属性
	$('.nameInfo').each(function(){
		$(this).qtip({
			content: {
				// 设置您要使用的文字图像的HTML字符串，正确的src URL加载图像
				text: '<img class="throbber" src="images/throbber.gif" alt="Loading..." />',
				url: $(this).attr('rel'), // 使用的URL加载的每个元素的rel属性
				title:{
					text: $(this).attr("title"), // 给工具提示使用每个元素的文本标题
					button: '关闭' // 在标题中显示关闭文字按钮
				}
			},
			position: {
				corner: {
					target: 'bottomMiddle', // 定位上面的链接工具提示
					tooltip: 'topMiddle'
				},
				adjust: {
					screen: true // 在任何时候都保持提示屏幕上的
				}
			},
			show: { 
				when: 'mouseover', //或click 
				solo: true // 一次只显示一个工具提示
			},
			hide: 'unfocus',
			style: {
				tip: true, // 设置一个语音气泡提示在指定工具提示角落的工具提示
				border: {
					width: 0,
					radius: 4
				},
				name: 'light', // 使用默认的淡样式
				width: 390 // 设置提示的宽度
			}
		})
	});
	
});
--%>
</script>
</head>
<body onload="scrollPage();">
       <div class="wrap_m fl">
       <div class="d-share rad">
        <form id="msgShareForm" class="subform" method="post">
        <input type="hidden" id="idType" name="idType" value="${idType }"/>
        <textarea id="content" name="content" cols="" rows="" class="txt-dialog" datatype="*"></textarea>
        <div class="fx">
            <div class="content_wrap">
			<div class="zTreeDemoBackground left">
				<ul class="list">
					<li class="title">&nbsp;&nbsp;
					 <div id="emample2" class="example">
				        <div><input type="button" id="ele2" class="tigger" value="表情"/></div>
				        <div id="blk2" class="blk" style="display:none;">
				            <div class="main">
				                <ul>
				              	  <jsp:include page="/biaoqing.jsp"></jsp:include>
				                </ul>
				            </div>
				        </div>
				    </div>
					范围: <input id="scopeTypeSel" type="text" readonly value="${scopeTypeSel }" style="width:120px;" onclick="showMenu();"  datatype="*"/>
					<input name="" type="button" value="" class="share_btn" id="ajaxSubmit"/>
				    </li>
				</ul>
			</div>
		</div>
		<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
			<ul id="treeDemo" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
		</div>
       </div>
       </form>
       </div>
       <div class="d-new mtop">
       <div class="t rad"><span class="fl">动态更新</span><span class="fr"><input name="" type="text" /></span></div>
       <div class="b cl mtop" id="container">
       	  <c:choose>
			<c:when test="${not empty listMsgShare}">
				<c:forEach items="${listMsgShare}" var="msg" varStatus="st">
				<%--分享类型 --%>
					<c:choose>
						<c:when test="${not empty msg.vote}">
						  <%--投票分享模块 --%>
						          <div class="new-list" id="msg${msg.id }">
						          <dl>
						           <dt>
						           	<a class="nameInfo fl" onclick="javascript:void(0)"  rel="/userInfo/userInfoPage?sid=${param.sid}&id=${msg.creator}">
						 				<img src="/downLoad/userImg/${msg.comId}/${msg.creator}?sid=${param.sid}" width="50" height="50" onload="scrollPage();"/>
						           </a>
						           </dt>
						          <dd>
						          <h1 class="name">${msg.creatorName }</h1>
						          <c:set var="vote" value="${msg.vote}"></c:set>
						          <p><tags:viewTextArea><tags:cutString num="140">${vote.voteContent}</tags:cutString> 
						          </tags:viewTextArea> </p>
						  <div id="msgVote${vote.id}">
					          <form id="voteForm${vote.id}">
					          	<input name="finishTime" type="hidden" value="${vote.finishTime}:00:00"/>
						          <%--还没有投票的  还没有过期的--%>
									<div style="${(vote.voterChooseNum eq 0 && vote.enabled eq 1)?"display:block":"display:none" };padding-top:15px" id="voting${vote.id}">
										<c:forEach items="${vote.voteChooses}" var="voteChoose" varStatus="vs">
										<div style="clear: both">
											<div class="voteChoose">
											<%--文本类型选择 --%>
											<c:choose>
												<c:when test="${vote.maxChoose==1}">
													<input type="radio" name="voteChoose" value="${voteChoose.id}" ${voteChoose.chooseState==1?"checked":"" }/>&nbsp;&nbsp;${voteChoose.content }
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="voteChoose" value="${voteChoose.id}" ${voteChoose.chooseState==1?"checked":"" }/>&nbsp;&nbsp;${voteChoose.content }
												</c:otherwise>
											</c:choose>
											</div>
											<%--选项内容 --%>
											<div  style="clear:both;padding-left: 10px" >
											<c:if test="${not empty voteChoose.middle && not empty voteChoose.large}">
												<div style="clear:both;float:left" id="mid_${vote.id}_${vs.count }">
														<a href="javascript:void(0)" onclick="$('#large_${vote.id}_${vs.count }').show();$('#mid_${vote.id}_${vs.count }').hide();" >
															<img src="/downLoad/down/${voteChoose.midImgUuid}/${voteChoose.midImgName}?sid=${param.sid}" onload="scrollPage();"/><br/>	
														</a>
												</div>
												<div style="display:none;clear:both;float:left" id="large_${vote.id}_${vs.count }">
														<a href="javascript:void(0)" onclick="$('#large_${vote.id}_${vs.count }').hide();$('#mid_${vote.id}_${vs.count }').show();" >
															<img src="/downLoad/down/${voteChoose.largeImgUuid}/${voteChoose.largeImgName}?sid=${param.sid}" onload="scrollPage();"/><br/>	
														</a>
												</div>
											</c:if>
											</div>
										</div>
										</c:forEach>
										<div style="clear: both;padding-top: 15px">
											<%--投票 --%>
											<span>
												<input type="button" class="blue_btn" value="投票" onclick="vote123(${vote.maxChoose},${vote.id },${vote.voterChooseNum},'${param.sid }');"/>
											</span>
											<%--已投过票的在点击更改投票后显示取消更改--%>
											<c:if test="${vote.voterChooseNum >0}">
												<span>
													<input type="button" class="blue_btn" value="取消更改" onclick="$('#voting${vote.id}').hide();$('#voted${vote.id}').show();"/>
												</span>
											</c:if>
											<%--未投票的查看投票结果 --%>
											<c:if test="${vote.voterChooseNum eq 0}">
												<span>
													<input type="button" class="blue_btn" value="查看结果" onclick="$('#voting${vote.id}').hide();$('#voted${vote.id}').show();"/>
												</span>
											</c:if>
										</div>
										<div style="clear: both;padding-top: 15px">
											总计${vote.voteTotal}票    ${vote.voteType eq 1?"匿名投票":"" }  最多可以选择${vote.maxChoose }项  <span id="otherTime">${vote.finishTime}</span> :00:00 到期  投票后可以查看结果
										</div>
									</div>
									
									
									<%--已经投票的 已经过期的 已经过期但未投票的--%>
									<div style="${((vote.voterChooseNum eq 0 && vote.enabled eq 0)||vote.enabled eq 0||vote.voterChooseNum >0)?"display:block":"display:none" };padding-top:15px" id="voted${vote.id}">
										<c:forEach items="${vote.voteChooses}" var="voteChoose" varStatus="vs">
										<div style="clear: both;padding-bottom: 3px">
											<div class="voteChoose">
											<%--投票内容--%>
												${voteChoose.content}
											</div>
											<%--选项内容 --%>
											<div  style="clear:both;" >
												<c:if test="${not empty voteChoose.middle && not empty voteChoose.large}">
													<div style="clear:both;float:left" id="midV_${vote.id}_${vs.count }">
															<a href="javascript:void(0)" onclick="$('#largeV_${vote.id}_${vs.count }').show();$('#midV_${vote.id}_${vs.count }').hide();" >
																<img src="/downLoad/down/${voteChoose.midImgUuid}/${voteChoose.midImgName}?sid=${param.sid}" onload="scrollPage();"/><br/>	
															</a>
													</div>
													<div style="display:none;clear:both;float:left;" id="largeV_${vote.id}_${vs.count }">
															<a href="javascript:void(0)" onclick="$('#largeV_${vote.id}_${vs.count }').hide();$('#midV_${vote.id}_${vs.count }').show();" >
																<img src="/downLoad/down/${voteChoose.largeImgUuid}/${voteChoose.largeImgName}?sid=${param.sid}" onload="scrollPage();"/><br/>	
															</a>
													</div>
												</c:if>
											</div>
											<div style="clear:both;width: 100%;">
												<div class="votebar" style="float: left">
													<div class="votePer" style="width:${vote.voteTotal eq 0?0:(voteChoose.total/vote.voteTotal)*100}%;">
														<span><fmt:formatNumber value="${vote.voteTotal eq 0?0:(voteChoose.total/vote.voteTotal)*100}"  pattern="0"/>%</span>
													</div>
												</div>
												${voteChoose.total}票
												<%--有人投票并且不是匿名的 --%>
												<c:if test="${voteChoose.total>0 && vote.voteType==0}">
													<div style="float: right;padding-right: 20px">
													 <a href="javascript:void(0)" onclick="viewVoter('voterPic_${vote.id}_${vs.count }')">查看详情</a>	
													</div>
												</c:if>
											</div>
											<c:if test="${voteChoose.total>0 && vote.voteType==0}">
												<div style="clear:both;display: none" id="voterPic_${vote.id}_${vs.count }">
													<c:forEach items="${voteChoose.voters}" var="voter" varStatus="vsVoter">
														<img src="/downLoad/userImg/${voter.comId}/${voter.voter}?sid=${param.sid}" title="${voter.voterName}"></img>
													</c:forEach>
												</div>
											</c:if>
										</div>
										</c:forEach>
										<div style="clear: both;padding-top: 15px">
											<span style="display: block;float: left;">
												总计${vote.voteTotal}票   
											</span>
											 <c:if test="${vote.enabled==0}">
											 	<span id="voteIsEnd" style="display: block;float: left;padding-left: 15px;">
													投票已截止
												</span>
											</c:if>
											<c:choose>
												<c:when test="${vote.voterChooseNum eq 0 && vote.enabled==1}">
												    <%--未投票的返回投票 --%>
													<span style="display: block;float: left;padding-left: 15px;">
														<a href="javascript:void(0)" onclick="$('#voting${vote.id}').show();$('#voted${vote.id}').hide();scrollPage();">返回投票 </a>
													</span>
												</c:when>
												<c:otherwise>
													<%--投过票的修改投票 --%>
													<span style=" ${vote.enabled==0?"display:none":""};float: left;padding-left: 15px;" id="updateVote">
														<a href="javascript:void(0)" onclick="$('#voting${vote.id}').show();$('#voted${vote.id}').hide();scrollPage();">修改投票</a>
													</span>
													<span style="display: block;float: left;padding-left: 15px;"><a href="javascript:void(0)" onclick="reloadDiv(${vote.id},'${param.sid }');scrollPage();"> 刷新结果</a> </span>
												</c:otherwise>
											</c:choose>
										</div>
						        	  </div>
						          </form>
								</div>
			  				 <div class="b-info" style="clear: both;">
					          <span class="fl">
						          <a href="#" onclick="msgShareView(${msg.modId},'${msg.type}');">全文</a>
					          &nbsp;&nbsp;|&nbsp;&nbsp;<a href="#">加入</a></span><span class="fr">时间:${msg.createDate}</span></div>
					          </dd>
					          </dl>
						  </div>
						</c:when>
						<c:otherwise>
						<%--纯文本分享模块 --%>
			          <div class="new-list" id="msg${msg.id }">
			          <dl>
			           <dt>
			           	<a class="nameInfo fl" onclick="javascript:void(0)"  rel="/userInfo/userInfoPage?sid=${param.sid}&id=${msg.creator}">
			 				<img src="/downLoad/userImg/${msg.comId}/${msg.creator}?sid=${param.sid}" width="50" height="50"/>
			           </a>
			           </dt>
			          <dd>
			          <h1 class="name">${msg.creatorName }</h1>
			          <p><tags:viewTextArea><tags:cutString num="140">${msg.content}</tags:cutString> 
			          </tags:viewTextArea> </p>
	  				  <%--<form name="msgShareForm" action="/msgShare/addMsgShareLeaveMsg?sid=${param.sid}" method="post">
	  				  <table>
	  				  	<tr>
	  				  	<td>
	  				  		
	  				  	</td>
	  				  	</tr>
	  				  </table>
	  				  </form>
			          --%><div class="b-info">
			          <span class="fl">
			          <c:choose>
			          	<c:when test="${msg.type==1}">
				       <a href="#" onclick="msgShareView(${msg.id},'${msg.type}');">全文</a>
			          	</c:when>
			          	<c:otherwise>
				       <a href="#" onclick="msgShareView(${msg.modId},'${msg.type}');">全文</a>
			          	</c:otherwise>
			          </c:choose>
			          &nbsp;&nbsp;|&nbsp;&nbsp;<a href="#">加入</a></span><span class="fr">时间:${msg.createDate}</span></div>
			          </dd>
			          </dl>
			          </div>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		  </c:choose>
       </div>
       <div id="moreMsg" class="new-list" style="${msgShares>5?'dispaly:block':'display:none' };background">
       		<a href="javascript:void(0)" onclick="loadMeinv();">查看更多</a>
       </div>
       </div>
</div>
       
       
       <div class="wrap_r fl">
         <div class="admin-info rad">
         <span class="fl"><a href="javascript:void(0);" onclick="parent.frames['iframe_body_right'].location.href='/userInfo/aboutUserInfoPage?sid=${param.sid}'">
			<img src="/downLoad/userImg/${sessionUser.comId}/${sessionUser.id}?sid=${param.sid}" width="50" height="50"></img>
         </a></span>
         <span class="fr">${empty sessionUser.userName?'匿名':sessionUser.userName}<br/>${sessionUser.admin==1?'管理员':'非管理员' }<br/>${sessionUser.levName}</span>
         </div>
         <div class="addr-list mtop cl rad">
           <div class="t"><a href="#">未看更新</a></div>
           <ul>
       	  <c:choose>
			<c:when test="${not empty listTodayWorks}">
				<c:forEach items="${listTodayWorks}" var="vo" varStatus="st">
				<c:if test="${vo.sum>0}">
				<li><a href="/crm/customerListPage?pager.pageSize=10&sid=${param.sid}&isRead=0"><span class="fl">${vo.busTypeName}</span><span class="fr"><font color="#ff0000">(${vo.sum})</font></span></a></li>
				</c:if>
				</c:forEach>
			</c:when>
		  </c:choose>
           </ul>
           
         </div>
         <div class="d-attenttion cl rad mtop">
          <div class="t"><a href="#">关注动态</a></div>
           <ul>
           <li><a href="#" class="fl">XX客户</a><span class="fr">1-6 15:30</span></li>
           <li><a href="#" class="fl">aaa</a><span class="fr">1月6日 15:30</span></li>
           <li><a href="#" class="fl">aaa</a><span class="fr">1月6日 15:30</span></li>
           <li><a href="#" class="fl">aaa</a><span class="fr">1月6日 15:30</span></li>
           <li><a href="#" class="fl">aaa</a><span class="fr">1月6日 15:30</span></li>
           <li><a href="#" class="fl">aaa</a><span class="fr">1月6日 15:30</span></li>
           <li><a href="#" class="fl">aaa</a><span class="fr">1月6日 15:30</span></li>
           <li><a href="#" class="fl">aaa</a><span class="fr">1月6日 15:30</span></li>
           </ul>
         </div> 
       </div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>

