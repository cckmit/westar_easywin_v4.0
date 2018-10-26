<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<link href="/static/css/timeline.css" rel="stylesheet">
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script type="text/javascript" src="/static/js/weekreport.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<!-- 自动补全js -->
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
var val;
$(function() {
	
	initCard('${param.sid}')
	val = $(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		callback:function (form){
			return sumitPreCheck(null);
		},
		showAllError : true
	});
	$("#weekTalk").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//留言
		$("#otherIframe").attr("src", "/weekReport/weekRepTalkPage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}")
	});
	$("#weekLog").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//周报日志
		$("#otherIframe").attr("src", "/common/listLog?sid=${param.sid}&pager.pageSize=10&busId=${weekReport.id}&busType=006&ifreamName=otherIframe");
	});
	$("#weekFile").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$(this).attr("class","active")
		//周报附件
		$("#otherIframe").attr("src", "/weekReport/weekRepFilePage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}")
	});
	//周报聊天室
	$("#weekChatLi").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$("#weekChatLi").attr("class","active");
		$("#otherIframe").attr("src","/chat/listBusChat?sid=${param.sid}&busId=${weekReport.id}&busType=006&ifreamTag=otherIframe");
	});
	//加载所选年的时间轴，并展开所选的月份
	LoadTimeLine('${pageWeekYear}','${pageWeekMonth}');
})

function LoadTimeLine(yearLine,monthLine){
	//设置月份
	for(var i=1;i<=12;i++){
		var html = '<div class="month">';
		html+='\n <li style="cursor: pointer;" month="'+i+'"><span class="monthspan">'+i+'月</span><p class="monthp"></p></li>'
		html+='\n <ul class="weekul" style="display:none">';
		html+='\n </ul>';
		html+='\n </div>';
		$(".event_list").prepend(html);
		var yearH = $(".event_list .year");
		$(yearH).remove();
		$(".event_list").prepend($(yearH));
		//展开所选月份
		if(i==${nowWeekMonth}){
			$.ajax({
				 type : "post",
				 url : "/weekReport/ajaxGetTimeLime?sid=${param.sid}&rnd="+Math.random(),
				 dataType:"json",
				 data:{year:yearLine,month:monthLine},
				 success:function(data){
				 	if(data.status=='y'){
				 		var weeks = data.weeks;
				 		var htmlWeek = '';
				 		if(weeks.length>0){
				 			//时间轴所选的年份
				 			var yearSelect = $("#pageWeekYear").val();
				 			for(var j=0;j<weeks.length;j++){
				 				//时间轴上的年份与页面年份相同，则点亮相同的周数
				 				if(weeks[j].weekNum==${weekReport.weekNum} && ${weekReport.year}==yearSelect){
				 					htmlWeek = '\n<li><span class="weekspan" weekS='+weeks[j].weekS+' weekNum='+weeks[j].weekNum+'></span><p class="currentWeek"><span>第'+weeks[j].weekNum+'周</span></p></li>'+htmlWeek;
				 				}else{
				 					htmlWeek = '\n<li><span class="weekspan" weekS='+weeks[j].weekS+' weekNum='+weeks[j].weekNum+'></span><p class="weekp"><span>第'+weeks[j].weekNum+'周</span></p></li>'+htmlWeek;
				 				}
				 				//截止今年本周
				 				if(${nowWeekYear}==yearSelect && ${nowWeekNum}==weeks[j].weekNum){
				 					break;
				 				}
				 			}
				 		}
				 		$(".event_list .month li[month='"+${pageWeekMonth}+"']").next().append(htmlWeek);
				 		$(".event_list .month li[month='"+${pageWeekMonth}+"']").next().show();
				 		setStyle()
				 	}
				 }
			});
			//若是本年，则在本月停止加载
			if(${nowWeekYear}==yearLine){
				break;
			}
		}
	}
	//加载月份的周数
	$(".event_list .month").find("li").click(function(){
		var yearSelect = $("#pageWeekYear").val();
		
		var obj = $(this);
		var month = $(obj).attr("month");
		var weeks = $(obj).next().children();
		
		if(weeks.length==0){
			$.ajax({
				 type : "post",
				 url : "/weekReport/ajaxGetTimeLime?sid=${param.sid}&rnd="+Math.random(),
				 dataType:"json",
				 data:{year:yearSelect,month:month},
				 success:function(data){
				 	if(data.status=='y'){
				 		var weeks = data.weeks;
				 		var html = '';
				 		if(weeks.length>0){
				 			for(var j=0;j<weeks.length;j++){
				 				//所选年份与页面年份相同，点亮页面周数
				 				if(weeks[j].weekNum==${weekReport.weekNum} && yearSelect=='${weekReport.year}'){
							 		html = '\n<li><span class="weekspan" weekS='+weeks[j].weekS+' weekNum='+weeks[j].weekNum+'></span><p class="current"><span>第'+weeks[j].weekNum+'周</span></p></li>'+html;
				 				}else{
							 		html = '\n<li><span class="weekspan" weekS='+weeks[j].weekS+' weekNum='+weeks[j].weekNum+'></span><p class="weekp"><span>第'+weeks[j].weekNum+'周</span></p></li>'+html;
				 				}
				 				//截止今年本周
				 				if(${nowWeekYear}==yearSelect && ${nowWeekNum}==weeks[j].weekNum){
				 					break;
				 				}
				 			}
				 		}
				 		$(".event_list .month").find("ul").slideUp(500);
				 		$(obj).next().append(html);
				 		$(obj).next().slideDown(500);
				 		setStyle();
				 	}
				 }
			});
		}else{
			if($(this).next().css("display")=='none'){
				$(".event_list .month").find("ul").slideUp(500);
				$(this).next().slideDown(500);
			}
		}
	});
}
//设置事件
function setStyle(){
	$(".event_list .month ul li").click(function(){
		if($(this).find("p").attr("class")=='weekp'){
			var weekS = $(this).find(".weekspan").attr("weekS");
			$("#weekForm").attr("action","/weekReport/addOrEditWeekRep");
			$("#weekForm").find("#chooseDate").val(weekS);
			$("#weekForm").submit();
		}
	});
}
//重新设置年份
function ChangeYear(sid){
	var obj =  $(".event_list .month ul li").find(".currentWeek").parent();
	var year = $("#pageWeekYear").val();
	var month =$(obj).parent().prev().attr("month");
	if(!month){
		month = '${pageWeekMonth}'
	}
	$(".event_list .month").remove();
	LoadTimeLine(year,month)
		 
}

$(document).ready(function() {
	$(".taskarea").autoTextarea({minHeight:55,maxHeight:80});  
	$(".taskareaAdd").autoTextarea({minHeight:55,maxHeight:150});
});

/**
 * 添加下周计划
 */
function addPlan(ts,index){
	//当前行的文本域数据
	var textarea = $("#planBody"+index).find("textarea").val();
	//当前行的文本框数据
	var input = $("#planBody"+index).find("input").val();
	if(strIsNull(textarea)){//文本域数据数据为空
		layer.tips("请填写计划内容",$("#planBody" + index),{tips: 1});
		$("#planBody"+index).find("textarea").focus();
		return;
	}
	var num = index+1;
	var buttons = $(ts).parent().find("button").length;
	if(1==buttons){
		var html = '<button class="btn btn-default" type="button" onclick="delPlan(this,'+index+')">删除</button>';
		$(ts).parent().html(html);
	}else{
		 $(ts).parent().find(".ws-btnBlue").remove();
	}
	
	var html ='';
	html += '\n	<div class="row ws-plan-line" id="planBody'+num+'" style="padding-bottom: 10px">';
	html += '\n		<div class="control-label col-xs-7">';
	html += '\n			<textarea name="weekReportPlans['+num+'].planContent" class="form-control taskarea" style="color: #000"></textarea>';
	html += '\n		</div>';
	html += '\n		<div class="col-xs-2">';
	html += '\n			<input type="text" name="weekReportPlans['+num+'].planTime" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\'});" style="cursor: pointer;width: 125px;padding: 0px 5px" class="form-control" readonly="readonly" />';
	html += '\n		</div>';
	html += '\n		<div class="col-xs-3 ws-plan-btn" style="text-align:center"> ';
	html += '\n			<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,'+num+')">添加</button>';
	html += '\n			<button class="btn btn-default" type="button" onclick="delPlan(this,'+num+')">删除</button>';
	html += '\n		</div>';
	html += '\n	</div>';
	$("#weekPlan").append(html);
	$(".taskarea").autoTextarea({minHeight:55,maxHeight:80});  
	
}
//删除计划
function delPlan(ts,index){
	//当前按钮是否为最后一行
	var buttons = $(ts).parent().find("button").length;
	var plans = $("#planBody"+index).parent().find(".ws-plan-line").length;
	if(plans==2){//还有两个计划,只有一个计划的时候就没有删除按钮了
		if(buttons==2){//删除的是最后一行
			//前一个节点的id
			var preId = $("#planBody"+index).prev().attr("id");
			var indexPre = preId.replace('planBody','');
			var html = '<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,'+indexPre+')">添加</button>';
			//前一个节点设置为添加按钮
			$("#planBody"+indexPre).find(".ws-plan-btn").html(html);
		}else{//删除的是第一个计划
			//最后一个节点保留添加按钮
			$("#planBody"+index).next().find(".ws-plan-btn").find(".btn-default").remove();
		}
		$("#planBody"+index).remove()
	}else{
		if(buttons==2){//删除的是最后一个
			//前一个节点的id
			var preId = $("#planBody"+index).prev().attr("id");
			var indexPre = preId.replace('planBody','');
			var html = '<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,'+indexPre+')">添加</button>';
			html += '\n<button class="btn btn-default" type="button" onclick="delPlan(this,'+indexPre+')">删除</button>';
			//前一个节点设置添加和删除按钮
			$("#planBody"+indexPre).find(".ws-plan-btn").html(html);
		}
		$("#planBody"+index).remove()
	}
}

//0前一周，1后一周
function chooseWeekNum(flag,id){
	$.post("/weekReport/authorCheck?sid=${param.sid}",{Action:"post",weekReportId:id},     
			function (msgObjs){
			if(!msgObjs.succ){
				showNotification(1,msgObjs.promptMsg);
			}else{
				$("#weekForm").find("input[name='id']").val(id);
				$("#weekForm").submit();
			}
	},"json");
}
//编辑周报
function editWeekReport(state){
	if(state==1){//修改界面
		$(".addWeekReport").show();
		$(".viewWeekReport").hide();
		$("#workTrace").css("display","block")
	}else if(state==0){//不修改了
		$(".addWeekReport").hide();
		$(".viewWeekReport").show();
		$("#workTrace").css("display","none")
	}
}
//保存周报 1是存草稿 0是发布
function save(state,ts){

	//若是一个都没有填写，不能发布
	if(state==0){
		//是否能发布，默认不能
		var flag=false;
		var areas = $(".addWeekReport").find("textarea");
		for(var i=0;i<areas.length;i++){
			if($(areas[i]).attr("name").indexOf("DATA")==0
				&& $(areas[i]).val().replace(/\s+/g,"").length>0){
				flag=true;
			}
		}
		if($("#state").val()==0 && !flag){//已发布的周报不能重新成为
			window.top.layer.alert("已发布的周报，修改后内容不能为空！");
			return;
		}
		if(!flag){
			window.top.layer.confirm("周报未填写！<br>不能发布！<br>确定保存？", {
				title : "询问框",
				btn : [ "确定", "取消" ],
				icon : 3
			}, function(index) {
				window.top.layer.close(index);
				$("#state").attr("value", 1);
				$(ts).attr("disabled", "disabled")
				$("#weekRepForm").submit();
			});
		}else{
				$("#state").attr("value",0);
				$(ts).attr("disabled","disabled")
				$("#weekRepForm").submit();
		}
	}else{//存草稿无所谓填没填
		$("#state").attr("value",1);
		$(ts).attr("disabled","disabled")
		$("#weekRepForm").submit();
	}
	
}
//选择日期
function selectDate(val,dateS,dateE){
	 var d = val.replace('年','/').replace('月','/').replace('日','/');   
	 var d1 = dateS.replace('年','/').replace('月','/').replace('日','/');   
	 var d2 = dateE.replace('年','/').replace('月','/').replace('日','/'); 
	 if(judgeDate(d,d1,d2)){//只有周报发布人才可以进入
		 $("#weekForm").attr("action","/weekReport/addOrEditWeekRep");
		 $("#weekForm").find("#chooseDate").val(val);
		 $("#weekForm").submit();
	 }
}
//判断时间
function judgeDate(val,dateS,dateE){   
	var d = new Date(val).getTime()/1000/60/60/24;   
	var d1 = new Date(dateS).getTime()/1000/60/60/24;   
	var d2 = new Date(dateE).getTime()/1000/60/60/24; 
	if(d>=d1 && d<=d2){//选中的时间在两个之间，不进行任何操作
		return false;
	}else{
		return true;
	}
}
//周报列表
function listWeekReps(){
	$("#weekForm").attr("action","/weekReport/listPagedWeekRep")
	$("#weekForm").submit();
}
//周报模板设置
function addWeekRepMod(){
	window.self.location = '/weekReport/addWeekRepModPage?sid=${param.sid}';
}

//查看工作轨迹
function viewWorkTrace(dateS,dateE){
	var d1 = dateS.replace('年', '-').replace('月', '-').replace('日', '');
	var d2 = dateE.replace('年', '-').replace('月', '-').replace('日', '');
	var url = '/systemLog/exportWorkTrace?sid=${param.sid}&type=006&startDate='
			+ d1 + '&endDate=' + d2;
	window.top.layer.open({
		type : 2,
		title : false,
		closeBtn : 0,
		shadeClose : true,
		shade : 0.3,
		btn : [ '关闭' ],
		shift : 0,
		fix : true, //固定
		maxmin : false,
		move : false,
		area : [ '750px', '450px' ],
		content : [ url, 'no' ]
	//iframe的url
	});
		
}
var fileTypes="*.rar;*.zip;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.pdf;*.mp3;*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.avi;*.wma;*.rmb;*.rm;*.flash;*.flv;*.mp4;*.mid;*.3gp";
var fileSize="200MB";
var	numberOfFiles = 200;

</script>
</head>
<body>
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 左边菜单 -->
<jsp:include page="/WEB-INF/jsp/include/left_menu.jsp"></jsp:include>

<form method="get" action="/weekReport/viewWeekReport" id="weekForm">
	<input type="hidden" name="id">
	<input type="hidden" name="sid" value="${param.sid }">
	<input type="hidden" name="pager.pageSize" value="10">
	<input type="hidden" name="weekerId" value="${weekReportParam.weekerId}">
	<input type="hidden" name="weekerType" value="${weekReportParam.weekerType}">
	<input type="hidden" name="weekName" value="${weekReportParam.weekName}"/>
	<input type="hidden" name="depId" value="${weekReportParam.depId}">
	<input type="hidden" name="depName" value="${weekReportParam.depName}">
	<input type="hidden" name="weekDoneState" value="${weekReportParam.weekDoneState}">
	<input type="hidden" value="${weekReportParam.startDate}" name="startDate" />
	<input type="hidden" value="${weekReportParam.endDate}" name="endDate" />
	<input type="hidden" id="preId" name="preId" value="${weekReport.preId}"/>
	<input type="hidden" id="nextId" name="nextId" value="${weekReport.nextId}"/>
	<input type="hidden" id="chooseDate" name="chooseDate"/>
	<input type="hidden" name="viewType" value="1"/>
	</form>
<!--main content start-->
<section id="main-content">
	<section class="wrapper wrapper2" style="float: left;width:150px;position:absolute;padding-top: 25px;">
	<div class="page" style="background-color:inherit;">
		<div class="box">
			<div class="event_list">
				<h3 class="year"> 
				<select id="pageWeekYear" class="form-control" style="font-size: inherit;color: inherit;padding: 0 0 " onchange="ChangeYear('${param.sid }')">
					<c:choose>
						<c:when test="${not empty yearList }">
							<c:forEach var="year" items="${yearList}">
								<option value="${year}" ${year eq pageWeekYear?'selected':'' }>${year }</option>
							</c:forEach>
						</c:when>
					</c:choose>
				</select>
				</h3>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>
	</section>
	<!--任务按钮-->
	<section class="wrapper wrapper2" style="width: 85%;margin-left: 170px">
		<div class="ws-wrapIn">
			<div class="ws-tabTit">
				<ul class="ws-tabBar">
					<li><a href="javascript:void(0)" onclick="listWeekReps()">周报列表</a></li>
					<c:if test="${userInfo.admin>0}">
					<li><a href="javascript:void(0)" onclick="addWeekRepMod()">模板设置</a></li>
					</c:if>
				</ul>
			</div>
			<div class="ws-projectBox">
				<div class="ws-projectIn">
					<div class="ws-titleBox">
						<h6 class="ws-title">${weekReport.year}年第${weekReport.weekNum}周周报</h6>
						<c:set var="display">
							<c:choose>
								<c:when test="${not empty weekReport && weekReport.state==0 }">
								block
								</c:when>
								<c:otherwise>
								none
								</c:otherwise>
							</c:choose>
						</c:set>
						<h6 style="float: right;display:${display}" id="workTrace"><a href="javascript:void(0)" onclick="viewWorkTrace('${firstDayOfWeek}','${lastDayOfWeek}')">本周工作</a></h6>
					
					</div>
					<div class="row">
						<c:choose>
							<c:when test="${empty weekReport}">
								<div class="col-sm-12">
									<div class="panel-body">
										<%--周报头部 --%>
											<div class="ws-write-title">
												<c:if test="${weekReport.preId>0}">
													<a href="javascript:void(0)" onclick="chooseWeekNum(0,${weekReport.preId})" class="ws-write-left"><img src="/static/images/w-left.png" title="上一个"/></a>
												</c:if>
												<c:if test="${weekReport.nextId>0}">
													<a href="javascript:void(0)" onclick="chooseWeekNum(1,${weekReport.nextId})" class="ws-write-right"><img src="/static/images/w-right.png" title="下一个"/></a>
												</c:if>
												<div class="ws-write-choice" style="cursor: default;line-height: 35px">
													<div class="ws-week-name" style="cursor: default"> ${weekReport.year}年第${weekReport.weekNum}周</div>
													<div class="ws-week-name" style="cursor: default">${weekReport.userName}第${weekReport.weekNum}周周报</div>
													<div class="ws-week-time" style="cursor: default">
														${fn:substring(firstDayOfWeek,5,fn:length(firstDayOfWeek)) } ~ 
														${fn:substring(lastDayOfWeek,5,fn:length(lastDayOfWeek))}
													</div>
													<div style="padding-left: 45px">
														<input id="chooseDate"  name="chooseDate" type="text" onFocus="WdatePicker({onpicked:function(dp){selectDate(dp.cal.getNewDateStr(),'${firstDayOfWeek}','${lastDayOfWeek}')},dateFmt:'yyyy年MM月dd日',maxDate:'%y年%M月%d日'})"   
															style="cursor: pointer;width: 125px;padding: 0px 5px;color: #000" class="form-control" readonly="readonly" value="${firstDayOfWeek}" title="周数时间选择"/>
													</div>
												</div>
											</div>
											<%--周报头部结束 --%>
											<div style="text-align: center;margin:50px 0px;font-size: large">
												请联系团队管理人员,设置周报模板
											</div>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="col-sm-12">
									<form method="post" id="weekRepForm" class="subform" action="/weekReport/addWeekReport">
										<div class="panel-body">
											<tags:token></tags:token>
											<input type="hidden" name="weekerId" value="${weekReportParam.weekerId}">
											<input type="hidden" name="depId" value="${weekReportParam.depId}">
											<input type="hidden" name="depName" value="${weekReportParam.depName}">
											<input type="hidden" value="${weekReportParam.startDate}" name="startDate" />
											<input type="hidden" value="${weekReportParam.endDate}" name="endDate" />
											<input type="hidden" name="weekName" value="${weekReportParam.weekName}"/>
											<input type="hidden" name="viewType" value="1"/>
											
											<input type="hidden" name="chooseDate" value="${lastDayOfWeek}"/>
	
										    <input type="hidden" id="num" value="${fn:length(weekReport.weekReportPlans)}"/>
										 	<input type="hidden" id="id" name="id" value="${weekReport.id}"/>
										 	<input type="hidden" id="state" name="state" value="${weekReport.state}"/>
											<%--周报头部 --%>
											<div class="ws-write-title">
												<c:if test="${weekReport.preId>0}">
													<a href="javascript:void(0)" onclick="chooseWeekNum(0,${weekReport.preId})" class="ws-write-left"><img src="/static/images/w-left.png" title="上一个"/></a>
												</c:if>
												<c:if test="${weekReport.nextId>0}">
													<a href="javascript:void(0)" onclick="chooseWeekNum(1,${weekReport.nextId})" class="ws-write-right"><img src="/static/images/w-right.png" title="下一个"/></a>
												</c:if>
												<div class="ws-write-choice" style="cursor: default;line-height: 35px">
													<div class="ws-week-name" style="cursor: default"> ${weekReport.year}年第${weekReport.weekNum}周</div>
													<div class="ws-week-name" style="cursor: default">${weekReport.userName}第${weekReport.weekNum}周周报</div>
													<div class="ws-week-time" style="cursor: default">
														${fn:substring(firstDayOfWeek,5,fn:length(firstDayOfWeek)) } ~ 
														${fn:substring(lastDayOfWeek,5,fn:length(lastDayOfWeek))}
													</div>
													<div style="padding-left: 45px">
														<input id="chooseDate"  name="chooseDate" type="text" onFocus="WdatePicker({onpicked:function(dp){selectDate(dp.cal.getNewDateStr(),'${firstDayOfWeek}','${lastDayOfWeek}')},dateFmt:'yyyy年MM月dd日',maxDate:'%y年%M月%d日'})"   
															style="cursor: pointer;width: 125px;padding: 0px 5px;color: #000" class="form-control" readonly="readonly" value="${firstDayOfWeek}" title="周数时间选择"/>
													</div>
												</div>
											</div>
											<%--周报头部结束 --%>
										</div>
										<%--周报添加 --%>
										<div class="addWeekReport" style="${(weekReport.state!=0 || (weekReport.countVal==0 && weekReport.state==0))?'display:block':'display:none' }">
										<!--工作汇报-->
										<section class="panel">
											<header class="panel-heading" style="height: 40px;" >
												工作汇报
												<span class="tools pull-right">
						                            <a href="javascript:void(0);" class="fa fa-chevron-down"></a>
						                         </span>
											</header>
											<div class="panel-body">
												<c:choose>
							 						<c:when test="${not empty weekReport}">
														<%--条目 --%>
											 			<c:forEach var="weekReportQ" items="${weekReport.weekReportQs}" varStatus="vs">
															<div class="panel-body">
																<h4>${weekReportQ.modReportName}</h4>
																<textarea name="${weekReportQ.reportName}" class="form-control taskareaAdd" style="color: #000">${weekReportQ.reportVal} </textarea>
															</div>
											 			</c:forEach>
														<%--周报填写结束 --%>
							 						</c:when>
							 						<c:otherwise>
							 						</c:otherwise>
							 					</c:choose>
												
											</div>
										</section>
										<c:if test="${weekReport.hasPlan==1}">
								 				<section class="panel">
													<header class="panel-heading">
														下周工作计划
														<span class="tools pull-right">
									                       <a href="javascript:javascript：void(0);" class="fa fa-chevron-down"></a>
									                    </span>
													</header>
													<div class="panel-body" id="weekPlan">
														<div class="row">
															<label class="control-label col-xs-7">
																<h4>下周计划任务名称</h4></label>
															<label class="control-label col-xs-5">
																<h4>计划完成时间</h4></label>
														</div>
														
														<c:choose>
										 					<c:when test="${not empty weekReport.weekReportPlans}">
											 					<c:forEach items="${weekReport.weekReportPlans}" var="weekRepPlan" varStatus="vs">
																	<div class="row ws-plan-line" id="planBody${vs.count-1}" style="padding-bottom: 10px">
																		<div class="control-label col-xs-7">
																			<textarea name="weekReportPlans[${vs.count-1}].planContent" class="form-control taskarea" style="color: #000">${weekRepPlan.planContent }</textarea>
																		</div>
																		<div class="col-xs-2">
																			<input type="text" value="${weekRepPlan.planTime}" name="weekReportPlans[${vs.count-1}].planTime" onclick="WdatePicker({dateFmt:'yyyy年MM月dd日'});" style="cursor: pointer;width: 125px;padding: 0px 5px" class="form-control" readonly="readonly" />
																		</div>
																		<div class="col-xs-3 ws-plan-btn" style="text-align:center"> 
																			<c:if test="${vs.last}">
																				<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,${vs.count-1})">添加</button>
																			</c:if>
																			<c:if test="${fn:length(weekReport.weekReportPlans)>1}">
																				<button class="btn btn-default" type="button" onclick="delPlan(this,${vs.count-1})">删除</button>
																			</c:if>
																		</div>
																	</div>
											 					</c:forEach>
										 					</c:when>
										 					<c:otherwise>
																<div class="row ws-plan-line" id="planBody0" style="padding-bottom: 10px">
																	<div class="control-label col-xs-7" >
																		<textarea name="weekReportPlans[0].planContent" class="form-control taskarea" style="color: #000;"></textarea>
																	</div>
																	<div class="col-xs-2">
																		<input type="text" value="" name="weekReportPlans[0].planTime" onclick="WdatePicker({dateFmt:'yyyy年MM月dd日'});" style="cursor: pointer;width: 125px;padding: 0px 5px" class="form-control" readonly="readonly" />
																	</div>
																	<div class="col-xs-3 ws-plan-btn" style="text-align:center"> 
																		<button class="btn btn-info ws-btnBlue" type="button" onclick="addPlan(this,0)">添加</button>
																	</div>
																</div>
										 					
										 					</c:otherwise>
										 				</c:choose>
														
														
														
													</div>
												</section>
								 			</c:if>
								 			<%--上传附件部分 --%>
								 			<section class="panel">
												<header class="panel-heading">
													周报附件
													<span class="tools pull-right">
								                       <a href="javascript:javascript：void(0);" class="fa fa-chevron-down"></a>
								                    </span>
												</header>
												<div class="panel-body">
						                                  <div style="clear:both" class="ws-process">
																<div id="fileQueueweekReportFiles_upfileId" style="width: 300px">
																<c:choose>
																	<c:when test="${not empty weekReport.weekReportFiles}">
																		<c:forEach items="${weekReport.weekReportFiles}" var="upfile" varStatus="vs">
																		 <div id="SWFUpload_0_-${upfile.upfileId}" class="uploadify-queue-item">	
																			<div class="cancel">
																				<a href="javascript:delFile('undefined','SWFUpload_0_-${upfile.upfileId }','weekReportFiles_upfileId','no','${upfile.upfileId}','');">X</a>
																			</div>	
																				<span class="fileName">${upfile.fileName}(已有文件)</span><span class="data"> - 完成</span>
																			<div class="uploadify-progress">
																				<div class="uploadify-progress-bar" style="width: 100%;"></div>
																			</div>	
																		</div>	
																		</c:forEach>
																	</c:when>
																</c:choose>
															</div>
															<div class="ws-choice-btn">
																    <input type="file" name="fileweekReportFiles_upfileId" id="fileweekReportFiles_upfileId"/>
															</div>
															<script type="text/javascript">
																loadUpfiles('weekReportFiles_upfileId','${param.sid}','','${userInfo.comId}');
															</script>
															<div style="position: relative; width: 350px; height: 90px;display: none">
																<div style="float: left;">
																	<select list="weekReportFiles" listkey="upfileId" listvalue="fileName" id="weekReportFiles_upfileId" name="weekReportFiles.upfileId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 90px;">
																		<c:choose>
																			<c:when test="${not empty weekReport.weekReportFiles}">
																				<c:forEach items="${weekReport.weekReportFiles}" var="upfile" varStatus="vs">
																					<option selected="selected" value="${upfile.upfileId}">${upfile.fileName}</option>
																				</c:forEach>
																			</c:when>
																		</c:choose>
																	</select>
																</div>
															</div>
														</div>
												</div>
											</section>
								 			<%--上传附件部分 结束--%>
								 			
								 			
								 			
								 			<div class="optAddWeekReport" style="clear:both;text-align: center;">
									 			<c:if test="${weekReport.state==1}">
									 				<input type="button" class="btn btn-primary date-reset" onclick="save(1,this);" value="存草稿"/>
									 			</c:if>
									 			<input type="button" class="btn btn-info ws-btnBlue" onclick="save(0,this);" value="发布周报"/>
									 			<c:if test="${weekReport.state==0}">
									 				<input type="reset" class="btn btn-default" onclick="editWeekReport(0);" value="取消"/>
									 			</c:if>
								 			</div>
										</div>
										<%--周报添加结束 --%>
										<%--周报查看 --%>
										<div class="viewWeekReport" style="${(weekReport.countVal>0 && weekReport.state==0)?'display:block':'display:none' }">
											<!--工作汇报-->
											<section class="panel">
												<header class="panel-heading" style="height: 40px;" >
													工作汇报
													<span class="tools pull-right">
							                            <a href="javascript:void(0);" class="fa fa-chevron-down"></a>
							                         </span>
												</header>
												<div class="panel-body">
													<c:choose>
								 						<c:when test="${not empty weekReport}">
																<%--条目 --%>
													 			<c:forEach var="weekReportQ" items="${weekReport.weekReportQs}" varStatus="vs">
																	<div class="panel-body">
																		<h4>${weekReportQ.modReportName}</h4>
																		<div class="ws-zbContent">
																		<c:choose>
																			<c:when test="${not empty weekReportQ.reportVal}">
																				<tags:viewTextArea>${weekReportQ.reportVal}</tags:viewTextArea>
																			</c:when>
																			<c:otherwise>
																				<font color="red">未填写</font>
																			</c:otherwise>
																		</c:choose>
																		</div>
																	</div>
													 			</c:forEach>
								 						</c:when>
								 						<c:otherwise>
								 						</c:otherwise>
								 					</c:choose>
													
												</div>
											</section>
											<c:if test="${weekReport.hasPlan==1}">
												<!--任务简介-->
												<section class="panel">
													<header class="panel-heading">
													<c:set var="planNums" value="${fn:length(weekReport.weekReportPlans)}"></c:set>
														下周工作计划(${planNums })
														<span class="tools pull-right">
								                            <a href="javascript:void(0);" class="fa fa-chevron-down"></a>
								                         </span>
													</header>
													<div class="panel-body" style="display: block">
														<table class="display table table-bordered table-striped">
					                                        <thead>
					                                            <tr>
					                                                <th width="10%">序号</th>
					                                                <th>计划任务名称</th>
					                                                <th width="15%">计划完成时间</th>
					                                            </tr>
					                                        </thead>
					                                        <tbody>
					                                        <c:choose>
											 					<c:when test="${not empty weekReport.weekReportPlans}">
											 						<c:forEach items="${weekReport.weekReportPlans}" var="weekRepPlan" varStatus="vs">
							                                            <tr class="gradeX">
							                                                <td>${vs.count}</td>
							                                                <td><tags:viewTextArea>${weekRepPlan.planContent }</tags:viewTextArea></td>
							                                                <td>${weekRepPlan.planTime}</td>
							                                            </tr>
											 						</c:forEach>
											 					</c:when>
											 					<c:otherwise>
						                                            <tr class="gradeX" align="center">
						                                                <td colspan="3">未填写下周计划</td>
						                                            </tr>
											 					</c:otherwise>
											 				</c:choose>
						                                    </tbody>
						                                 </table>
													</div>
												</section>
											</c:if>
											<%--上传附件部分 --%>
								 			<section class="panel">
												<header class="panel-heading">
												<c:set var="fileNums" value="${fn:length(weekReport.weekReportFiles)}"></c:set>
													周报附件(${fileNums})
													<span class="tools pull-right">
								                       <a href="javascript:javascript：void(0);" class="fa fa-chevron-down"></a>
								                    </span>
												</header>
												<div class="panel-body" style="display: block">
													<table class="display table table-bordered table-striped">
					                                        <thead>
					                                            <tr>
					                                                <th width="10%">序号</th>
					                                                <th>附件名称</th>
					                                                <th width="20%">上传时间</th>
					                                                <th width="10%" style="text-align:center;" >操作</th>
					                                            </tr>
					                                        </thead>
					                                        <tbody>
					                                        <c:choose>
											 					<c:when test="${not empty weekReport.weekReportFiles}">
											 						<c:forEach items="${weekReport.weekReportFiles}" var="upfile" varStatus="vs">
							                                            <tr class="gradeX">
							                                                <td>${vs.count}</td>
							                                                <td>${upfile.fileName }</td>
							                                                <td>${upfile.upTime}</td>
							                                                <td align="center">
							                                                <c:choose>
															 					<c:when test="${upfile.fileExt=='doc' || upfile.fileExt=='docx' || upfile.fileExt=='xls' || upfile.fileExt=='xlsx' || upfile.fileExt=='ppt' || upfile.fileExt=='pptx'}">
																	 				<a class="fa fa-download" style="padding-right: 10px" href="javascript:void(0)" onclick="downLoad('${upfile.fileUuid}','${upfile.fileName}','${param.sid }')" title="下载"></a>
																	 				<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${upfile.upfileId}','${upfile.fileUuid}','${upfile.fileName}','${upfile.fileExt}','${param.sid}','006','${weekReport.id}')" title="预览">
																	 				</a>
															 					</c:when>
															 					<c:when test="${upfile.fileExt=='txt'|| upfile.fileExt=='pdf'}">
															 						<a class="fa fa-download" style="padding-right: 10px" href="/downLoad/down/${upfile.fileUuid}/${upfile.fileName}?sid=${param.sid}" title="下载"></a>
																	 				<a class="fa fa-eye" href="javascript:void(0)" onclick="viewOfficePage('${upfile.upfileId}','${upfile.fileUuid}','${upfile.fileName}','${upfile.fileExt}','${param.sid}','006','${weekReport.id}')" title="预览">
																	 				</a>
															 					</c:when>
															 					<c:when test="${upfile.fileExt=='jpg'||upfile.fileExt=='bmp'||upfile.fileExt=='gif'||upfile.fileExt=='jpeg'||upfile.fileExt=='png'}">
															 						<a class="fa fa-download" style="padding-right: 10px" href="/downLoad/down/${upfile.fileUuid}/${upfile.fileName}?sid=${param.sid}" title="下载"></a>
															 						<a class="fa fa-eye" href="javascript:void(0)" onclick="showPic('/downLoad/down/${upfile.fileUuid}/${upfile.fileName}','${param.sid}','${upfile.upfileId}','006','${weekReport.id}')" title="预览"></a>
															 					</c:when>
															 					<c:otherwise>
																	 				<a class="fa fa-download" style="padding-right: 10px" onclick="downLoad('${upfile.fileUuid}','${upfile.fileName}','${param.sid }')" title="下载"></a>
															 					</c:otherwise>
															 				</c:choose>
							                                                </td>
							                                            </tr>
											 						</c:forEach>
											 					</c:when>
											 					<c:otherwise>
						                                            <tr class="gradeX" align="center">
						                                                <td colspan="4">未上传附件</td>
						                                            </tr>
											 					</c:otherwise>
											 				</c:choose>
						                                    </tbody>
						                                 </table>
												</div>
											</section>
								 			<%--上传附件部分 结束--%>
											
											
											<c:if test="${empty editWeek}">
												<div class="optViewWeekReport" style="clear:both;text-align: center;">
									 				<input type="button" class="btn btn-warning date-set" onclick="editWeekReport(1)" value="修改周报"/>
									 			</div>
											</c:if>
										</div>
										<%--周报查看结束 --%>
										<section class="panel">
											<header class="panel-heading">
												更多信息
												<span class="tools pull-right">
						                            <a href="javascript:void(0);"  class="fa fa-chevron-down"></a>
						                         </span>
											</header>
											<div class="panel-body" style="display: block">
												<div id="tabs-1" class="panel-collapse collapse in">
			
													<!--nav-tabs style 1-->
			
													<div class="tc-tabs">
														<!-- Nav tabs style 2-->
														<ul class="nav nav-tabs tab-color-dark background-dark">
															<li class="active"><a href="javascript:void(0)" id="weekTalk" data-toggle="tab">周报留言</a>
															</li>
															<li><a href="javascript:void(0)" data-toggle="tab" id="weekLog">周报日志<c:if test="${weekReport.docNum > 0}"><span style="color:red;font-weight:bold;">（${weekReport.docNum}）</span></c:if></a>
															</li>
															<li><a href="javascript:void(0)" data-toggle="tab" id="weekFile">周报文档</a>
															</li>
															<li id="weekChatLi"><a href="javascript:void(0);" data-toggle="tab">聊天室</a></li>
														</ul>
			
														<!-- Tab panes -->
														<div class="tab-content">
															<iframe style="width:100%;" id="otherIframe" class="layui-layer-load"
																src="/weekReport/weekRepTalkPage?sid=${param.sid}&pager.pageSize=10&weekReportId=${weekReport.id}"
																border="0" frameborder="0" allowTransparency="true"
																noResize  scrolling="no" width=100% height=100% vspale="0"></iframe>
														</div>
			
													</div>
													<!--nav-tabs style 2-->
												</div>
											</div>
										</section>
									 </form>
									
								</div>
							</c:otherwise>
						</c:choose>
						
						
					</div>
					
					
					
				</div>
			</div>
		</div>
	</section>

</section>
<!--main content end-->
<!--right sidebar start-->
<div class="right-sidebar" style="width: 260px">
<%--浏览记录开始 --%>
<section class="panel" >
	<header class="panel-heading">
		浏览记录
		<span class="tools pull-right">
                <a href="javascript:void(0);" class="fa fa-chevron-down"></a>
            </span>
	</header>
	<div class="panel-body" style="padding-right:0px">
		<c:choose>
			<c:when test="${not empty listViewRecord}">
				<c:forEach items="${listViewRecord}" var="obj" varStatus="vs">
					<div class="online-list">
						<div class="online-head">
							<img src="/downLoad/userImg/${obj.comId}/${obj.userId}?sid=${param.sid}" />
						</div>
						<div class="online-name">
							<span class="ws-smallSize">${obj.userName }</span>
						</div>
						<div class="online-name" style="float:right;">
							${fn:substring(obj.recordCreateTime,0,16) }
						</div>
						<div class="ws-clear"></div>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>
	</div>
</section>
<%--浏览记录结束 --%>
</div>
</body>
</html>

