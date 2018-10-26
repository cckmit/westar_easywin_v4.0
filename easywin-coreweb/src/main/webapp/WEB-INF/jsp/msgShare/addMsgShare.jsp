<%@page import="com.westar.base.cons.CommonConstant"%>
<%@page import="com.westar.base.pojo.Notification"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@page import="com.westar.base.util.DateTimeUtil"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/selfGroupTree.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//打开页面的标签
var openPageTag;
//打开页面的标签
var openTabIndex;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
	openPageTag = openWindow.pageTag;
	openTabIndex = openWindow.tabIndex;
}

//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
});
//初始化分享内容
function setContent(content,scopeTypeSel){
	$("#content").val(content);
	$("#scopeTypeSel").val(scopeTypeSel);
}
/**
 * 初始化分享范围
 */
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
	
	//信息推送
	$("body").on("click","a[data-todoUser]",function(){
		var relateDiv = $(this).attr("data-relateDiv");
		var params = {};
		//查询所有
		params.onlySubState = 0;
		userMore('null',params , '${param.sid}',"null","null",function(options){
			if(options && options[0]){
				$.each(options,function(index,option){
					var span = $('<span></span>');
					var userId = option.value;
					//去重
					var preUserSpan = $("#"+relateDiv).find("span[data-userId='"+userId+"']");
					if(preUserSpan && preUserSpan.get(0)){
						return true;
					}
					$(span).attr("data-userId",option.value);
					$(span).attr("title","双击移除");
					$(span).addClass("blue");
					$(span).css("cursor","pointer");
					$(span).addClass("padding-right-5");
					$(span).html('@'+option.text);
					
					var userObj = {}
					userObj.userId = option.value;
					userObj.userName = option.text;
					$(span).data("userObj",userObj);
					
					$("#"+relateDiv).append($(span));
				})
			}else{
				$("#"+relateDiv).html("");
			}
		})
	})
	
	//信息推送移除
	$("body").on("dblclick","span[data-userId]",function(){
		$(this).remove();
	})
});
/**
 * 添加附件
 */
function addMsgShares(){
	if($("#subState").val()==1){
		return;
	}
	if(!$("#content").val()){
		showNotification(2,"请编辑分享内容！");
		$("#content").focus();
		return false;
	}
	//防止重复提交
	$("#shareForm").ajaxSubmit({
        type:"post",
        url:"/msgShare/ajaxAddMsgShare?sid=${param.sid}&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
        	$("#subState").val(1);
		},
        traditional :true,
        success:function(data){
        	$("#subState").val(0);
            if(data.status=='y'){
           		showNotification(1,"分享成功");
           		if(null!=data.msgShare && openPageTag == 'index'){
           			openWindow.appendMsgShare(data.msgShare);
           		}
            	closeWin();
            }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
        	$("#subState").val(0);
            //可以再次提交
			showNotification(2,"系统错误，请联系管理人员！");
        }
    });
}

//窗体点击事件检测
document.onclick = function(e){
	   var evt = e?e:window.event;
	   var ele = evt.srcElement || evt.target;
	   //表情包失焦关闭
		for(var i=0;i<biaoQingObjs.length;i++){
			if(ele.id != biaoQingObjs[i].switchID){
				$("#"+biaoQingObjs[i].divID).hide();
			}
		}
}
//创建一个表情对象数组
var biaoQingObjs = new Array();
//初始化最新初始化表情对象
var activingBiaoQing;
//表情对象添加；switchID触发器开关，objID返回对象主键,表情显示div层主键
function addBiaoQingObj(switchID,divID,objID){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	if(isBiaoQingEvent(switchID)){
		haven = true;
	}
	//对象构建
	var biaoQing ={"switchID":switchID,"objID":objID,"divID":divID}
	
	if(!haven){
		//主键存入数组
		biaoQingObjs[biaoQingObjs.length]=biaoQing;
	}
	//初始化最新初始化表情对象
	activingBiaoQing = biaoQing;
	//打开表情
	biaoQingOpen(biaoQing);
}
//判断页面点击事件事都是表情触发事件
function isBiaoQingEvent(eventId){
	//数组是否已经包含此元素标识符
	var haven = false;
	//判断数组是否已经包含此主键元素
	for(var i=0;i<biaoQingObjs.length;i++){
		if(biaoQingObjs[i].switchID==eventId){
			haven = true;
			break;
		}
	}
	return haven;
}
//表情打开
function biaoQingOpen(obj){
	$("#"+obj.divID).show();
}
//表情包关闭
function biaoQingClose(){
	$("#"+activingBiaoQing.divID).hide();
}
//关闭表情div，并赋值
function divClose(title,path){
	biaoQingClose();
	insertAtCursor(activingBiaoQing.objID,title)
	$("#"+activingBiaoQing.objID).focus();
}



</script>
</head>
<body style="background-color: #fff">
<input type="hidden" id="subState" value="0">
<form id="shareForm" class="subform"  method="post">
	<div id="fileDiv">
	</div>
	
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary padding-top-5" style="font-size: 20px">信息分享</span>
                        <div class="widget-buttons">
	                        <div class="pull-left padding-top-5 padding-right-20">
								<div class="fx" style="position: relative;">
									 <div style="float: left;font-size: 15px" class="blue padding-left-10">
										范围:<input id="scopeTypeSel" type="text" readonly="readonly" value="${scopeTypeSel}" style="width:150px;height: 30px" onclick="showMenu();" />
									</div>
									<div id="menuContent" style="display:none; position: absolute;top:30px;left: 45px;z-index: 999">
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
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<div>
	                             	<!--直接反馈-->
						    			<span class="input-icon icon-right">
						                	<textarea class="form-control" id="content" name="content"
						                	placeholder="请输入内容……" style="height:115px;"></textarea>
						                </span>
						                <div class="panel-body no-padding-bottom">
						                	<div class="buttons-preview pull-left" style="position: relative;">
						                    	<a class="btn-icon fa fa-meh-o fa-lg" href="javascript:void(0)" id="biaoQingSwitch" 
						                    		onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','content');">
												</a>
												<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:15px;z-index:99;left: 15px">
													<!--表情DIV层-->
											        <div class="main">
											            <ul style="padding: 0px">
											            <jsp:include page="/biaoqing.jsp"></jsp:include>
											            </ul>
											        </div>
											    </div>
											    <!-- 
												    <a class="btn-icon fa-lg" href="javascript:void(0)" data-todoUser="yes" data-relateDiv="todoUserDiv_-1">
												    	@
													</a>
											     -->
												<div id="todoUserDiv_-1" class="padding-top-10">
						                			
						                		</div>
						                    </div>
						                </div>
	                             	</div>
                                </div>
                            </div>
                          </div>
                        </div>
					
					</div>
				</div>
			</div>
		</div>								
									
</form>
</body>
</html>
