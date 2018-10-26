<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/voteJs/addVote.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<link rel="stylesheet" type="text/css" href="/static/plugins/zTree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="/static/plugins/zTree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="/static/js/selfGroupTree.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script> 

//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
}
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
$(function() {
	$("#voteContent").bind("paste cut keydown keyup focus blur",function(event){
		resizeVoteH('voteInfo');
    });
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});

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
	$("#voteContent").autoTextarea({minHeight:60,maxHeight:80});
	initSelfGroupTree(${selfGroupStr});
});

var arr = [ "gif", "jpg", "jpeg", "png", "bmp"];
var picPath;

var fileTypes="*.gif;*.jpg;*.jpeg;*.png;*.bmp;";
var fileSize="200MB";
var	numberOfFiles = 200;

//移除图片
function removeImg(divid){
	$("#"+divid).html('');
	resizeVoteH('voteInfo')
}
//移除一个选项
function removeopt(ts,index){
	var optadd = $(ts).parent().parent().find(".optadd");
	if(optadd.length==1){
		var pre = $(ts).parent().parent().parent().prev();
		var html="\n	<div class=\"optadd\" style=\"float: left; margin-top: 10px;margin-left: 15px\">";
		html+="\n		<a href=\"javascript:void(0)\" onclick=\"addline('${param.sid}','${sessionUser.id }','${sessionUser.comId }')\" title=\"添加\" class=\"fa fa-plus-square-o fa-lg\">";
		html+="\n	</a>";
		if($(pre).find(".optdel").length==0){
			$("#opt1").html(html)
		}else{
			$(pre).find(".optdel").parent().append(html);
		}
	}
	$(ts).parent().parent().parent().remove();
	var opts = $("#countopt").html();
	$("#countopt").html(parseInt(opts)-1);

	var maxChoose = $("#maxChoose").find("option").length;
	$("#maxChoose  option:last").remove();
	resizeVoteH('voteInfo')
}


function checkForm(way){
	var content = $("#voteContent").val();
	if(strIsNull(content)){
		$("#voteContent").focus();
		layer.tips("请填写投票描述",$("#voteContent"),{"tips":1});
		return false;
	}
	var finishTime = $("#finishTime").val();
	if(strIsNull(finishTime)){
		$("#finishTime").focus();
		layer.tips("请填写投票 截止时间",$("#finishTime"),{"tips":1});
		return false;
	}
	var contents =$("input[name$='].content'");
	for(var i=0;i<contents.length;i++){
		var val = $(contents[i]).val();
		if(strIsNull(val)){
			$(contents[i]).focus();
			layer.tips("请填写投票投票选项",$(contents[i]),{"tips":1});
			return false;
		}
	}

	$("#chooseForm").ajaxSubmit({
		type : "post",
		url : "/vote/addVote?sid=${param.sid}&rnd="+Math.random(),
		dataType:"json",
		data:{idType:$("#idType").val()},
		beforeSubmit: function(a,o,f){
			 $("#addAndViewBtn").attr("disabled","disabled");
			 $("#addBtn").attr("disabled","disabled");
        },
        success : function(data){
            if(data.status=='y'){
            	openWindow.location.reload()
            }else{
            	showNotification(2,"发起投票失败！");
            }
            $("#addAndViewBtn").removeAttr("disabled");
            $("#addBtn").removeAttr("disabled");
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
            	showNotification(2,"发起投票失败！");
            	$("#addAndViewBtn").removeAttr("disabled");
                $("#addBtn").removeAttr("disabled");
	    }
	});
}

</script>
<style type="text/css">
.uploadify{
position: inherit;
}
</style>
</head>
<body>
<form id="chooseForm" class="subform" method="post">
<input type="hidden" name="attentionState" id="attentionState" value="0"/>
	
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">发起投票</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="setAtten(this)">
								<i class="fa fa-star-o"></i>关注
							</a>
							<a href="javascript:void(0)" class="blue" id="addAndViewBtn" onclick="checkForm(0)">
								<i class="fa fa-save"></i>发起
							</a>
							<!-- 
								<a href="javascript:void(0)" class="blue" id="addBtn" onclick="checkForm(1)">
									<i class="fa fa-save"></i>发起并继续
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
                            	<span class="widget-caption blue">投票描述</span>
                            	<div class="pull-right margin-top-5" style="width: 350px">
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
                             		<div>
	                             	<!--直接反馈-->
						    			<span class="input-icon icon-right">
						                	<textarea class="form-control" id="voteContent" name="voteContent"
						                	placeholder="请输入内容……" style="height:60px;"></textarea>
						                </span>
						                <div class="panel-body no-padding-bottom">
						                	<div class="buttons-preview pull-left" style="position: relative;">
						                    	<a class="btn-icon fa fa-meh-o fa-lg" href="javascript:void(0)" id="biaoQingSwitch" 
						                    		onclick="addBiaoQingObj('biaoQingSwitch','biaoQingDiv','voteContent');">
												</a>
												<div id="biaoQingDiv" class="blk" style="display:none;position:absolute;width:200px;top:15px;z-index:99;left: 15px">
													<!--表情DIV层-->
											        <div class="main">
											            <ul style="padding: 0px">
											            <jsp:include page="/biaoqing.jsp"></jsp:include>
											            </ul>
											        </div>
											    </div>
						                    </div>
						                </div>
	                             	</div>
									
									
									<div class="ps-clear">
										<div id="options" style="margin-left: 10px">
			
											<div id="option0" class="singleOption" style="clear: both">
												<div class="optionHead" style="float: left;margin-top: 8px">
													<span>投票选项：</span>
												</div>
												<div class="optionBody" style="float: left;border: 1px solid #f0f0f0;">
													<div style="width: 350px" >
														<input class="form-control-new" type="text" name="voteChooses[0].content" id="voteChoose0_content"/>
													</div>
													<div class="optionPicShow">
									  					<input type="hidden" name="voteChooses[0].orgImgPath" id="voteChooses0_orgImgPath"/>
											 			<input type="hidden" name="voteChooses[0].largeImgPath" id="voteChooses0_largeImgPath"/>
											 			<input type="hidden" name="voteChooses[0].midImgPath" id="voteChooses0_midImgPath"/>
											 			<div id="showpic0">
											 			</div>
								  					</div>
												</div>
												<div class="optionFile" style="float:left;margin: 5px;font-size: 12px">
													<div id="imgPicker0" class="pull-left">选择图片</div>
													<script type="text/javascript">
														loadUpfilesForVote('${param.sid}','imgPicker0','showpic0','pic0','voteImg',0);
													</script>
								  				</div>
											</div>
											
											<div id="option1" class="singleOption" style="clear: both">
												<div class="optionHead" style="float: left;margin-top: 8px">
													<span>投票选项：</span>
												</div>
												<div class="optionBody" style="float: left;border: 1px solid #f0f0f0;">
													<div style="width: 350px" >
														<input class="form-control-new" type="text" name="voteChooses[1].content" id="voteChoose1_content"/>
													</div>
													<div class="optionPicShow">
									  					<input type="hidden" name="voteChooses[1].orgImgPath" id="voteChooses1_orgImgPath"/>
											 			<input type="hidden" name="voteChooses[1].largeImgPath" id="voteChooses1_largeImgPath"/>
											 			<input type="hidden" name="voteChooses[1].midImgPath" id="voteChooses1_midImgPath"/>
											 			<div id="showpic1">
											 			</div>
								  					</div>
												</div>
												<div class="optionFile" style="float:left;margin: 5px;font-size: 12px">
									  				<div id="imgPicker1" class="pull-left">选择图片</div>
													<script type="text/javascript">
														loadUpfilesForVote('${param.sid}','imgPicker1','showpic1','pic1','voteImg',1);
													</script>
								  				</div>
								  				<div style="float: left;width: 50px" id="opt1">
									  				<div class="optadd" style="float: left; margin-top: 10px;margin-left: 15px">
											  			<a href="javascript:void(0)" onclick="addline('${param.sid}','${sessionUser.id }','${sessionUser.comId }')" title="添加" class="fa fa-plus-square-o fa-lg"></a>
								  					</div>
								  				</div>
											</div>
										</div>
										
										<div style="clear: both;font-size: 13px;padding-top: 15px;margin-left: 20px">
								  			共<span id="countopt">2</span>个选项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  			选择设置<select class="form-control-new" style="width: 100px;height: 25px;" id="maxChoose" name="maxChoose">
								  				<option selected="selected" value="1">1</option>
								  				<option value="2">2</option>
								  			</select>项
								  		</div>
									
                                	</div>
                                	
                                	<%--条件部分 --%>
									<div style="clear:both; font-size: 15px;line-height: 30px;padding-top: 15px;margin-left: 20px">
								  		<div style="font-size: 13px;" class="pull-left">
								  			<span style="float: left">
								  				<label>
									  				<input type="checkbox" value="1" name="voteType"/>
									  				<span class="text" style="color: inherit;">匿名投票</span>
								  				</label>
								  				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;截止时间
								  			</span>
								  			<span style="float: left">
								  				<input id="finishTime" name="finishTime" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH',minDate:'%y-%M-%d {%H}'})" style="cursor: pointer;width: 110px;color: #000;padding: 0px 5px;height: 30px" class="form-control" readonly="readonly" value="<%=DateTimeUtil.getTomDateStr(DateTimeUtil.yyyy_MM_dd)+" "+(DateTimeUtil.getHourStr()) %>"/>
								  			</span>
								  			时
								  		</div>
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
