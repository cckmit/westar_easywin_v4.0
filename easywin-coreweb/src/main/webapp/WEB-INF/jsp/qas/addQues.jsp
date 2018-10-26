<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@page import="com.westar.base.util.DateTimeUtil"%>
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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/selfGroupTree.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 

//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
	
var vali;
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	vali=$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		callback:function (form){
			return addQuestion();
		},
		showAllError:true
	});
});

//初始化树
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
	$("#content").autoTextarea({minHeight:80,maxHeight:150});  
	$("#title").autoTextarea({minHeight:50,maxHeight:100});  
	initSelfGroupTree(${selfGroupStr});
});

function addAndViewQues(){
	$("#way").val("view")
	$("#quesForm").submit();
}
function addQues(){
	$("#way").val("add")
	$("#quesForm").submit();
}

//添加提问
function addQuestion(){
	//问题长度
	var titleLen = $('#title').val().replace(/\s+/g,"").length;
	if(titleLen==0){
		layer.tips('请简述问题！', "#title", {tips: 1});
		$('#title').focus();
		return false;
	}
	if(titleLen>100){
		layer.tips('问题太长！', "#title", {tips: 1});
		$('#title').focus();
		return false;
	}
	//补充的问题的长度
	var contLen = $('#content').val().replace(/\s+/g,"").length;
	if(contLen>1500){
		layer.tips('补充的问题太长！', "#content", {tips: 1});
		$('#content').focus();
		return false;
	}
	
	return true;
}

</script>
</head>
<body>
<form id="quesForm" class="subform" method="post" action="/qas/addQues">
<input type="hidden" name="attentionState" id="attentionState" value="0"/>
<input type="hidden" id="way" name="way" value="${param.way}">
<tags:token></tags:token>

<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">我要提问</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="setAtten(this)">
								<i class="fa fa-star-o"></i>关注
							</a>
							<a href="javascript:void(0)" class="blue" onclick="addAndViewQues()">
								<i class="fa fa-save"></i>提问
							</a>
							<!-- 
							<a href="javascript:void(0)" class="blue" onclick="addQues()">
								<i class="fa fa-save"></i>提问并继续
							</a>
							 -->
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
                            	<span class="widget-caption blue">简要概述您的问题</span>
                            	<div class="pull-right margin-top-5" style="width: 330px">
                            		<div style="font-size: 15px" class="pull-left blue">
										分享到动态更新
										<label>
											<input type="checkbox" id="shareMsgCheckBox" name="shareMsg"  value="yes"/>
											<span class="text" style="color: inherit;"></span>
										</label>
									 </div>
									 
									 <div class="fx" style="position: relative;">
										 <div style="float: left;font-size: 15px" class="blue padding-left-10">
											范围:<input id="scopeTypeSel" type="text" readonly="readonly" value="${scopeTypeSel}" style="width:150px;height: 25px" onclick="showMenu();" />
										</div>
										<div id="menuContent" style="display:none; position: absolute;top:30px;left: 170px;z-index: 999">
												<input type="hidden" id="idType" name="idType" value="${idType}"/>
											<ul id="treeDemo" class="ztree" style="clear:both;margin-top:0;z-index:1;background-color:#f0f0f0;width: 150px;padding-bottom: 0px"></ul>
											<ul id="addGrpUl" class="ztree" style="z-index:1;background-color:#f0f0f0;width: 150px;padding-bottom: 10px;">
												<li style="text-align: center;margin-top: 5px;color: #1c98dc;cursor: pointer;" onclick="addGrpForTree('scopeTypeSel','menuContent','idType','treeDemo','${param.sid}')">
													+添加分组
												</li>
											</ul>
										</div>
									 </div>
                            	</div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<textarea  id="title" name="title" style="height: 50px;color: #000;" class="form-control" onpropertychange="handleTitle()" onkeyup="handleTitle()"></textarea>
								 	 <div id="msgTitle" style="float:right;padding-right: 20px">0/100</div> 
									<script> 
											//当状态改变的时候执行的函数 
											function handleTitle(){
												var value = $('#title').val();
												var len = charLength(value.replace(/\s+/g,""));
												if(len%2==1){
													len = (len+1)/2;
												}else{
													len = len/2;
												}
												if(len>100){
													$('#msgTitle').html("<font color='red'>"+len+"</font>/100");
												}else{
													$('#msgTitle').html(len+"/100");
												}
											} 
											//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
											if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
												document.getElementById('title').onpropertychange=handleTitle 
											}else {//非ie浏览器，比如Firefox 
												document.getElementById('title').addEventListener("input",handleTitle,false); 
											} 
										</script> 
                                </div>
                            </div>
                          </div>
                            <div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">问题补充</span>
                               </div>
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               			<textarea id="content"  name="content" class="form-control" style="height: 80px;color: #000;"
									 	 onpropertychange="handleContent()" onkeyup="handleContent()"></textarea>
									 	<div id="msgCont" style="float:right"></div> 
									 	<script> 
											//当状态改变的时候执行的函数 
											function handleContent(){
												var value = $('#content').val();
												var len = charLength(value.replace(/\s+/g,""));
												if(len%2==1){
													len = (len+1)/2;
												}else{
													len = len/2;
												}
												if(len>1500){
													$('#msgCont').html("<font color='red'>文字太长，请删减</font>");
												}else{
													$('#msgCont').html("");
												}
											} 
											//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
											if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
												document.getElementById('content').onpropertychange=handleContent 
											}else {//非ie浏览器，比如Firefox 
												document.getElementById('content').addEventListener("input",handleContent,false); 
											} 
										</script> 
                               		</div>
                               </div>
                           </div>
                            <div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue">附件补充</span>
                               </div>
                               <div class="widget-body no-shadow">
                               		<div class="tickets-container bg-white">
                               			<tags:uploadMore name="listQuesFiles.original" showName="fileName" ifream="" comId="${userInfo.comId}">
						  				</tags:uploadMore>
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
	<script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
