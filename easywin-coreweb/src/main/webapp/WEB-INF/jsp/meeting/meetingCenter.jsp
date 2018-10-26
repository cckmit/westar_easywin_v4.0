<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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
<script type="text/javascript" src="/static/js/meetJs/meetingCenter.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/commonListSearch.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/modFlowJs/modflow.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
var pageTag = '${(param.searchTab>20 && not empty param.searchTab)?"meetRoom":"meeting"}';
var EasyWin = {
		"sid":"${param.sid}",
		"userInfo" : {
			"userId" : "${userInfo.id}",
			"userName" : "${userInfo.userName}",
			"comId" : "${userInfo.comId}",
			"orgName" : "${userInfo.orgName}",
			"isAdmin" : "${userInfo.admin}",
		},
		"homeFlag" : "${homeFlag}",
		"ifreamName" : "${param.ifreamName}",
        "modFlowConfig":{
            "busType":"046"
        }
	};
//页面加载初始化
$(function(){
	//任务名筛选
	$("#title").blur(function(){
		//启动加载页面效果
		layer.load(0, {shade: [0.6,'#fff']});
		$("#searchForm").submit();
	});
	//文本框绑定回车提交事件
	$("#title").bind("keydown",function(event){
        if(event.keyCode == "13"){
        	if(!strIsNull($("#title").val())){
        		//启动加载页面效果
        		layer.load(0, {shade: [0.6,'#fff']});
				$("#searchForm").submit();
        	}else{
        		showNotification(1,"请输入检索内容！");
    			$("#title").focus();
        	}
        }
    });
	//会议撤销
	$("#revokeMeeting").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要撤销会议吗？',{icon:3,title:'询问框'}, function(){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(reConstrUrl);
				$('#delForm').attr("action","/meeting/revokeMeeting");
				$('#delForm').submit();
			}, function(){
				
			});	
		}else{
			window.top.layer.alert('请先选择要撤销会议。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
		}
	});
	
	//会议删除
	$("#delMeeting").click(function(){
		if(checkCkBoxStatus('ids')){
			window.top.layer.confirm('确定要删除会议吗？',{icon:3,title:'询问框'}, function(index){
				var url = reConstrUrl('ids');
				$("#delForm input[name='redirectPage']").val(url);
				$('#delForm').attr("action","/meeting/deleteMeeting");
				$('#delForm').submit();
			}, function(){
			});	
		}else{
			window.top.layer.alert('请先选择要删除会议。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
		}
	});
});
function newMeeting(flowType){
	 ModFlowBase.constrAddUrl(flowType,function(conf){
	        var url="/meeting/addMeetingPage?sid=${param.sid}";
	        var spFlag = conf.spFlag;
	        url = url+"&spFlag="+spFlag;
	        url = url+"&flowType="+conf.flowType;

	        if(spFlag == '1'){//固定流程
	            url = url+"&flowId="+conf.flowId;
	        }
	        openWinByRight(url);
	    });
	 
}

//编辑会议
function editMeeting(meetingId,meetingType,meetingState){
	if(meetingState==0){//会议未发布
		var url="/meeting/updateMeetingPage?sid=${param.sid}&batchUpdate=0&id="+meetingId;
		openWinByRight(url);
	}else{//会议发布了
		if(meetingType=='0'){//不是周期会议
			var url="/meeting/updateMeetingPage?sid=${param.sid}&batchUpdate=0&id="+meetingId;
			openWinByRight(url);
		}else{
			window.top.layer.open({
				 type: 0,
				  title: false,
				  closeBtn:0,
				  icon:3,
				  maxmin: false,
				  content: '仅修改最近一条会议,或是<br>批量修改相关周期性会议?',
				  btn: ['单条修改','批量修改','关闭'],
				  yes:function(index){
					  window.top.layer.close(index);
					  var url="/meeting/updateMeetingPage?sid=${param.sid}&batchUpdate=0&id="+meetingId;
					  openWinByRight(url);
				  },btn2:function(index){
					  window.top.layer.close(index);
					  var url="/meeting/updateMeetingPage?sid=${param.sid}&batchUpdate=1&id="+meetingId;
					  openWinByRight(url);
				  }
			});
		}
	}
}

//填写纪要
function addSummary(meetingId){
	var url="/meetSummary/addSummaryPage?sid=${param.sid}&meetingId="+meetingId;
	openWinByRight(url);
}
//邀请参会人员
function invMeetUser(meetingId){
	var url="/meeting/invMeetUserPage?sid=${param.sid}&meetingId="+meetingId;
	openWinByRight(url);
}
//邀请参会人员
function viewMeeting(meetingId){
	var url="/meeting/viewMeetingPage?sid=${param.sid}&meetingId="+meetingId;
	openWinByRight(url);
}
//查看纪要
function viewSummary(meetingId){
	var url="/meetSummary/viewSummaryPage?sid=${param.sid}&meetingId="+meetingId;
	openWinByRight(url);
}

function userOneForUserIdCallBack(userId,userIdtag,userName,userNametag){
	$("#creator_select").find("option").remove();
	$("#"+userIdtag).val(userId)
	$("#"+userNametag).val(userName)
	$("#searchForm").submit();
}
//参会确认
function checkMeeting(meetingId,state){
	if(state==1){//审核通过
		
		$.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "/meeting/checkMeeting?sid=${param.sid}",
			   data:{meetingId:meetingId},
			   success: function(data){
				  //若是有回调
				  if(data.status=='y'){
						$("#delForm").attr("action","/meeting/addMeetCheckUser");
						$("#delForm input[name='redirectPage']").val(window.location.href);
						$("#delForm #state").val(state);
						$("#delForm #meetingId").val(meetingId);
						$('#delForm').submit();
				  }else{
					  meetings = data.meetings;
					  var content = '<table class="table table-striped table-hover general-table">';
					  content += '\n <thead>';
					  content += '\n 	<tr>';
					  content += '\n 		<th valign="middle" class="hidden-phone">会议名称</th>';
					  content += '\n 		<th style="width: 28%" valign="middle">开始时间</th>';
					  content += '\n 		<th style="width: 28%" valign="middle">结束时间</th>';
					  content += '\n 	</tr>';
					  content += '\n </thead>';
					  content += '\n <tbody>';
					  $.each(meetings,function(index,vo){
						  if('1'==vo.checkState){
							  content += '\n 	<tr style="color:green">';
						  }else if('2'==vo.checkState){
							  content += '\n 	<tr style="color:red">';
						  }else{
							  content += '\n 	<tr>';
						  }
						  content += '\n 		<td>';
						  content += '\n 		'+vo.title + (vo.id==meetingId ? "(待确认)":"");
						  content += '\n 		</td>';
						  content += '\n 		<td>';
						  content += '\n 		'+vo.startDate;
						  content += '\n 		</td>';
						  content += '\n 		<td>';
						  content += '\n 		'+vo.endDate;
						  content += '\n 		</td>';
						  content += '\n 	</tr>';
					  })
					  content += '\n </tbody>';
					  content += '\n</table>';
					  
					//定义对话框
						var html = constrDialog();
						var $html = $(html);
						$html.find(".ps-layerTitle").html("同时段有重叠会议");
						$html.find("#contentLayerBody").html(content);
						var contentT = $html.prop("outerHTML");
						
						layui.use('layer', function(){
							var layer = layui.layer;
							window.top.layer.open({
								//title: '修改表单信息',
								title:false,
								closeBtn:0,
								type:0,
								area:'550px',
								content:contentT,
								btn:['参会','拒绝'],
								success:function(layero,index){
									//设置样式
									$("#pContentLayerBody").parent().css("padding","0px");
									$("#dislogCloseBtn").on("click",function(){
										window.top.layer.close(index)
									})
								},yes:function(index,layero){
									$("#delForm").attr("action","/meeting/addMeetCheckUser");
									$("#delForm input[name='redirectPage']").val(window.location.href);
									$("#delForm #state").val(1);
									$("#delForm #meetingId").val(meetingId);
									$('#delForm').submit();
								},btn2:function(index,layero){
									window.top.layer.close(index);
									window.top.layer.prompt({
										  formType: 2,
										  area:'400px',
										  closeBtn:0,
										  move: false,
										  title: '拒绝参会的事由描述'
										}, function(reason, index, elem){
											if(reason){
												$("#delForm").attr("action","/meeting/addMeetCheckUser");
												$("#delForm input[name='redirectPage']").val(window.location.href);
												$("#delForm #state").val(2);
												$("#delForm #reason").val(reason);
												$("#delForm #meetingId").val(meetingId);
												$('#delForm').submit();
											}else{
												showNotification(2,"请填写拒绝理由");
											}
										},function(reason, index, elem){
											return true;
										})
								}
							});
						});
				  }
			   }
		})
		
	}else if(state==2){//审核不通过
		window.top.layer.prompt({
			  formType: 2,
			  area:'400px',
			  closeBtn:0,
			  move: false,
			  title: '拒绝参会的事由描述'
			}, function(reason, index, elem){
				if(reason){
					$("#delForm").attr("action","/meeting/addMeetCheckUser");
					$("#delForm input[name='redirectPage']").val(window.location.href);
					$("#delForm #state").val(state);
					$("#delForm #reason").val(reason);
					$("#delForm #meetingId").val(meetingId);
					$('#delForm').submit();
				}
			})
	}
}
</script>
<script type="text/javascript">
$(function(){
	//操作删除和复选框
	$('.optTr').mouseover(function(){
		var display = $(this).find(".optTd .optCheckBox").css("display");
		if(display=='none'){
			$(this).find(".optTd .optCheckBox").css("display","block");
			$(this).find(".optTd .optRowNum").css("display","none");
		}
	});
	$('.optTr ').mouseout(function(){
		var optCheckBox = $(this).find(".optTd .optCheckBox");
			var check = $(optCheckBox).find("input").attr('checked');
			if(check){
				$(this).find(".optTd .optCheckBox").css("display","block");
				$(this).find(".optTd .optRowNum").css("display","none");
			}else{
				$(this).find(".optTd .optCheckBox").css("display","none");
				$(this).find(".optTd .optRowNum").css("display","block");
			}
	});
	
	$(":checkbox[name='ids'][disabled!='disabled']").click(function(){
		var checkLen = $(":checkbox[name='ids'][disabled!='disabled']:checked").length;
		var len = $(":checkbox[name='ids'][disabled!='disabled']").length;
		if(checkLen>0){
			//隐藏查询条件
			$(".searchCond").css("display","none");
			//显示批量操作
			$(".batchOpt").css("display","block");
			if(checkLen==len){
				$("#checkAllBox").attr('checked', true);
			}else{
				$("#checkAllBox").attr('checked', false);
			}
		}else{
			//显示查询条件
			$(".searchCond").css("display","block");
			//隐藏批量操作
			$(".batchOpt").css("display","none");
			
			$("#checkAllBox").attr('checked', false);
		}
	});
});

function checkAll(ckBoxElement, ckBoxName){
	var checkStatus = $(ckBoxElement).attr('checked');
	var obj = $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']");
	if (checkStatus) {
		$(obj).attr('checked', true);
		//显示复选框
		$(obj).parent().parent().find(".optCheckBox").css("display","block");
		//隐藏序号
		$(obj).parent().parent().find(".optRowNum").css("display","none");
		
		//隐藏查询条件
		$(".searchCond").css("display","none");
		//显示批量操作
		$(".batchOpt").css("display","block");
	} else {
		$(obj).attr('checked', false);
		//隐藏复选框
		$(obj).parent().parent().find(".optCheckBox").css("display","none");
		//显示序号
		$(obj).parent().parent().find(".optRowNum").css("display","block");
		
		//显示查询条件
		$(".searchCond").css("display","block");
		//隐藏批量操作
		$(".batchOpt").css("display","none");
	}
}

//窗体点击事件检测
document.onclick = function(e){
	var evt = e?e:window.event;
	var ele = $(evt.srcElement || evt.target);
	if ($(ele).parents("#moreCondition_Div").length == 1) { 
		if($(ele).prop("tagName").toLowerCase()=='a' || $(ele).parent("a").length == 1 ){
			return false;
		}else{
			if(!$("#moreCondition_Div").hasClass("open")){
				$(".searchCond").find(".open").removeClass("open");
				$("#moreCondition_Div").addClass("open");
			}
		}
	} else{
		$("#moreCondition_Div").removeClass("open");
	}
}
/**
 * 展示查询条件中更多
 */
function displayMoreCond(divId){
	if($("#"+divId).hasClass("open")){
		$("#"+divId).removeClass("open");
	}else{
		$("#"+divId).addClass("open")
	}
}
/**
 * 清空更多查询条件
 */
function resetMoreCon(divId){
	$("#"+divId).find("input").val('');
	
}
//人员筛选
function userMoreForUserIdCallBack(options,userIdtag){
	//启动加载页面效果
	layer.load(0, {shade: [0.6,'#fff']});
	$("#"+userIdtag+"_select").find("option").remove();
	for(var i =0;i<options.length;i++){
		$("#"+userIdtag+"_select").append("<option selected='selected' value='"+options[i].value+"' >"+options[i].text+"</option>");
	}
	$("#searchForm").submit();
}
//移除筛选
function removeChoose(userIdtag,sid,userId){
	$("#"+userIdtag+"_select").find("option[value='"+userId+"']").remove();
	$("#searchForm").submit();
}
</script>

<script type="text/javascript">
	$(function(){
		$("#newMeetingRoom").click(function(){
			var url="/meetingRoom/addMeetingRoomPage?sid=${param.sid}"
			openWinByRight(url);
		})
		//会议室删除
		$("#delMeetingRoom").click(function(){
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('确定要删除会议室吗？',{icon:3,title:'询问框'}, function(){
					var url = reConstrUrl('ids');
					$("#delForm input[name='redirectPage']").val(url);
					$('#delForm').submit();
				}, function(){
				});	
			}else{
				window.top.layer.alert('请先选择需要删除的会议室。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
			}
		});
	})
	
	$(function(){
		//会议室申请删除
		$("#delRoomApply").click(function(){
			//不选择审核通过的
			$(":checkbox[name='ids'][state='1']").attr("checked",false)
			//不选择审核没通过的
			$(":checkbox[name='ids'][state='2']").attr("checked",false)
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('撤消后会议将没有会议室!<br>确定撤销会议室申请吗？',{icon:3,title:'询问框'}, function(){
					var url = reConstrUrl('ids');
					$("#delForm input[name='redirectPage']").val(url);
					$('#delForm').submit();
				});	
			}else{
				window.top.layer.alert('请先选择未审核的会议室申请。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
			}
		});
	})
	//编辑会议室
	function editMeetingRoom(id){
		var url="/meetingRoom/updateMeetingRoomPage?sid=${param.sid}&id="+id;
		openWinByRight(url);
	}
	
	$(function(){
		//审核通过
		$("#checkYes").click(function(){
			//不选择审核通过的
			$(":checkbox[name='ids'][state='1']").attr("checked",false)
			if(checkCkBoxStatus('ids')){
				window.top.layer.confirm('确定审核通过吗？', function(){
					var url = reConstrUrl('ids');
					$("#delForm input[name='redirectPage']").val(url);
					$("#delForm #state").val(1);
					$('#delForm').submit();
				});	
			}else{
				window.top.layer.alert('请选择需要审核的申请。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
			}
		});
		//审核不通过
		$("#checkNo").click(function(){
			//选择未审核通过的
			$(":checkbox[name='ids'][state='1']").attr("checked",false)
			$(":checkbox[name='ids'][state='2']").attr("checked",false)
			if(checkCkBoxStatus('ids')){
				window.top.layer.prompt({
					  formType: 2,
					  area:'400px',
					  closeBtn:0,
					  move: false,
					  title: '审核意见描述'
					}, function(reason, index, elem){
						if(reason){
							var url = reConstrUrl('ids');
							$("#delForm input[name='redirectPage']").val(url);
							$("#delForm #state").val(2);
							$("#delForm #reason").val(reason);
							$('#delForm').submit();
						}
					})
			}else{
				window.top.layer.alert('请选择需要审核的申请。',{title:false,closeBtn:0,icon:7,btn:['关闭']});
			}
		});
	})
	//编辑会议室
	function editMeetingRoom(id){
		var url="/meetingRoom/updateMeetingRoomPage?sid=${param.sid}&id="+id;
		openWinByRight(url);
	}
	//会议室审核
	function checkApply(appId,state){
		if(state==1){//审核通过
			$("#delForm").attr("action","/meetingRoom/checkRoomApplyOne?appId="+appId);
			$("#delForm input[name='redirectPage']").val(window.location.href);
			$("#delForm #state").val(state);
			$('#delForm').submit();
		}else if(state==2){//审核不通过
			window.top.layer.prompt({
				  formType: 2,
				  area:'400px',
				  closeBtn:0,
				  move: false,
				  title: '审核意见描述'
				}, function(reason, index, elem){
					if(reason){
						$("#delForm").attr("action","/meetingRoom/checkRoomApplyOne?appId="+appId);
						$("#delForm input[name='redirectPage']").val(window.location.href);
						$("#delForm #state").val(2);
						$("#delForm #reason").val(reason);
						$('#delForm').submit();
					}
				})
		}
	}
</script>
<script type="text/javascript">
$(function(){
	
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
})
</script>
</head>
<body>
	<!-- 系统头部装载 -->
	<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
	<!-- 数据展示区域 -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
        	<!-- 大条件 -->
			<jsp:include page="/WEB-INF/jsp/meeting/listMeet_left.jsp"></jsp:include>
			<!-- 列表-->
			<!-- 待开会议 -->
			<c:if test="${param.searchTab==11 || empty param.searchTab}">
				<jsp:include page="/WEB-INF/jsp/meeting/listTodoMeet_middle.jsp"></jsp:include>
			</c:if>
			<c:if test="${param.searchTab==15}">
				<jsp:include page="/WEB-INF/jsp/meeting/listSpFlowMeet.jsp"></jsp:include>
			</c:if>
			<!-- 待发和已发会议 -->
			<c:if test="${param.searchTab==12 || param.searchTab==13}">
				<jsp:include page="/WEB-INF/jsp/meeting/listMeeting.jsp"></jsp:include>
			</c:if>
			<!--已开会议 -->
			<c:if test="${param.searchTab==14}">
				<jsp:include page="/WEB-INF/jsp/meeting/listDoneMeeting.jsp"></jsp:include>
			</c:if>
			<c:if test="${param.searchTab==16}">
				<jsp:include page="/WEB-INF/jsp/meeting/listPagedMeetWithSpSummary.jsp"></jsp:include>
			</c:if>
			<!-- 会议室申请 -->
			<c:if test="${param.searchTab==21}">
				<jsp:include page="/WEB-INF/jsp/meetingRoom/listPagedRoomApply.jsp"></jsp:include>
			</c:if>
			
			<!-- 会议室审核 -->
			<c:if test="${param.searchTab==22}">
				<jsp:include page="/WEB-INF/jsp/meetingRoom/listPagedApplyForCheck.jsp"></jsp:include>
			</c:if>
			<!-- 会议室添加 -->
			<c:if test="${param.searchTab==23}">
				<jsp:include page="/WEB-INF/jsp/meetingRoom/listMeetingRoom.jsp"></jsp:include>
			</c:if>
        </div>
   		 <!--主题颜色设置按钮-->
		<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
    </div>
</body>
</html>
