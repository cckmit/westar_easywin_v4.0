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
<link href='/static/plugins/fullCalendar/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link href='/static/plugins/fullCalendar/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />
<script src='/static/plugins/fullCalendar/lib/jquery-ui.custom.min.js'></script>
<script src='/static/plugins/fullCalendar/fullcalendar/fullcalendar.js'></script>
<script type="text/javascript" src="/static/js/schedule/shedule.js"></script>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var pageTag = 'schedule';
var viewStart;
var viewEnd;
var dayDate = new Date();
/** 当天信息初始化 **/
$(function(){
	
	var d = $.fullCalendar.formatDate(dayDate,"dddd");
	var m = $.fullCalendar.formatDate(dayDate,"yyyy-MM-dd");
	var lunarDate = lunar(dayDate);
	var fes = lunarDate.festival();
	if(fes.length>0){
		$(".alm_lunar_date").html($.trim(lunarDate.festival()[0].desc));
		$(".alm_lunar_date").show();
	}else{
		$(".alm_lunar_date").hide();
	}
});

function addSchedulePage(sDate,edate){
	var m = $.fullCalendar.formatDate(dayDate,"yyyy-MM-dd");
	if(!sDate){
		sDate = m;
	}
	if(!edate){
		edate = m;
	}
	var url = '/schedule/addSchedulePage?sid=${param.sid}&scheStartDate='+sDate+'&scheEndDate='+edate+'&formatEl=0';
	var height = $(window).height()-45;
	window.top.layer.open({
		  type: 2,
		  title: false,
		  closeBtn:0,
		  area: ['610px','450px'],
		  fix: true, //不固定
		  maxmin: false,
		  move: false,
		  scrollbar:false,
		  content: [url,'no'],
		  btn: ['添加日程','取消'],
		  yes: function(index, layero){
			  var iframeWin = window.top.window[layero.find('iframe')[0]['name']];
			  var result = iframeWin.addschedle('${param.sid}',viewStart,viewEnd);
			  if(result){
				  var list=result;
	             	for(var i=0;i<list.length;i++){
	             		var schedule =list[i]; 
	             		var f =new Object();
	             		f.id=schedule.id+"_016";
	                    f.title="["+schedule.userName+"]"+schedule.title;
	                    f.start = $.fullCalendar.parseDate(schedule.scheStartDate);
	                    f.end = $.fullCalendar.parseDate(schedule.scheEndDate);
	                    f.className = "agenda_event_class";
	                    f.userId=schedule.userId;
	                    f.busType='016';
	                    f.busId=schedule.id;
	                    if(schedule.isAllDay==1){
	                     f.allDay=true
	                    }else{
		                    f.allDay=false
	                    }
	 	               $("#calendar").fullCalendar('renderEvent', f);  //核心的更新代码  
	             	}
					window.top.layer.close(index);
					showNotification(1,"添加成功");
			  }
		  }
		});
	
}

var loadIndex;
/** calendar配置 **/
$(document).ready(
function() {
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	
 var calendar = $("#calendar").fullCalendar(
	{
		defaults: {
             lang: "zh"
        },
        header: {
        	center: "prev,next",
            left: "title",
            right: "today,month,agendaWeek,agendaDay"
        },
        selectable: !0,
        selectHelper: !0,
        weekNumbers: 0,
        weekNumberTitle: "\u5468\u6b21",
		editable : true,
		events:function(start,end,callback){
			viewStart = $.fullCalendar.formatDate(start,"yyyy-MM-dd HH:mm:ss");  
            viewEnd = $.fullCalendar.formatDate(end,"yyyy-MM-dd HH:mm:ss");
            $.getJSON('/schedule/listEvents?sid=${param.sid}',
            	{startDate:viewStart,
            	endDate:viewEnd,
            	subType:$("#subType").val(),
            	userId:$("#userId").val(),
            	busType:$("#busTypes").val()
            	},
            	function(data) { 
            		var events =[];
            		if(null!=data.list){
	            		for(var i=0;i<data.list.length;i++){
	             			 var schedule =data.list[i]; 
		             		 var f =new Object();
		             		 f.id=schedule.busId+"_"+schedule.busType;
		                     f.title="["+schedule.userName+"]"+schedule.title;
		                     f.start = $.fullCalendar.parseDate(schedule.scheStartDate);
		                     f.end = $.fullCalendar.parseDate(schedule.scheEndDate);
		                     f.userId=schedule.userId;
		                     if(schedule.busType==003){
		                    	 f.className = "agenda_task_class";
		                     }else if(schedule.busType==016){
			                     f.className = "agenda_event_class";
		                     }
		                     f.busType=schedule.busType;
		                     f.busId=schedule.busId;
		                     if(schedule.userId!=${userInfo.id} || schedule.busType!='016'){
		                    	 f.editable=false;
		                     }
		                     if(schedule.isAllDay==1){
		                     	f.allDay=true
		                     }else{
			                     f.allDay=false
		                     }
		                     events.push(f);
	             		}
            		}
            	 callback(events);
            });
		},
		select: function(a, c, b, e, g) {
			doSelect(a, c, b, e, g);
        },
        eventDrop: function(a, c, b, e, g, l, p, q) {
            doEventDrop(a, c, b, e, g, l, p, q)
        },
        eventResize: function(a, c, b, e, g, l, p) {
            doEventResize(a, c, b, e, g, l, p)
        },
		dayClick : function(dayDate, allDay, jsEvent, view) { //点击单元格事件			
		},
	  eventClick:function(calEvent, jsEvent, view){
		  if(calEvent.busType=='003'){
			  $.post("/task/authorCheck?sid=${param.sid}",{Action:"post",taskId:calEvent.busId},     
						function (msgObjs){
						if(!msgObjs.succ){
							showNotification(2,msgObjs.promptMsg);
						}else{
							var url = "/task/viewTask?sid=${param.sid}&id="+calEvent.busId;
							var height = $(window).height()-45
							window.top.layer.open({
								  type: 2,
								  title: false,
								  closeBtn: 0,
								  shadeClose: true,
								  scrollbar:false,
								  shade: 0.1,
								  shift:0,
								  fix: true, //固定
								  maxmin: false,
								  move: false,
								  area: ['800px', height+'px'],
								  content: [url,'no'] //iframe的url
								});
						}
				},"json");
		  }else{
				if(${userInfo.id}!=calEvent.userId){
					viewSchedule('${param.sid}',calEvent.busId);
				}else{
					editSchedulePage('${param.sid}',calEvent.busId,calEvent.id);
				}
				
		  }
	  },
	  eventRender: function(a, c, b) {
           c.find(".fc-event-inner").addClass("ellipsis");
           c.find(".fc-event-inner").attr("title", a.title)
       },
       eventAfterRender: function(a, c, b) {
           a.allDay && "003" != a.busType && c.find(".fc-event-inner").prepend("\x3cspan style\x3d'font-weight:bold'\x3e\u5168\u5929\x3c/span\x3e")
       },
       viewRender: function(a, c) {
     },
     viewDisplay: function(view) {
     },
	loading : function(bool) {
		//在此处添加加载动态
			if (bool){
				loadIndex = layer.load(0, {
					  shade: [0.1,'#000'] //0.1透明度的白色背景
					});
			}else{
				window.top.layer.close(loadIndex);
			}
		}
	});
 
	fillCalender('${userInfo.id}','${userInfo.userName}','${param.sid}',${userInfo.countSub});
	var html = "${usedUser}";
	$("#usedUl").append(html);
 	var offset = $(".fc-today").offset();
	if(undefined==offset){
		return;
	}else{
		var offsettop = offset.top;
		window.scrollTo(0,offsettop);
	}
});
/** 绑定事件到日期下拉框 **/
$(function(){
	$("#fc-dateSelect").delegate("select","change",function(){
		var fcsYear = $("#fcs_date_year").val();
		var fcsMonth = $("#fcs_date_month").val();
		$("#calendar").fullCalendar('gotoDate', fcsYear, fcsMonth);
	});
});

/*
*d 开始时间
*b 结束时间
*/
function doSelect(d, b, a, c, f){
	
	var scheStartDate;
	var scheEndDate;
	var formatEl = 0;
	if(f.name=="month"){
		scheStartDate = $.fullCalendar.formatDate(d,"yyyy-MM-dd");
		scheEndDate = $.fullCalendar.formatDate(b,"yyyy-MM-dd")
		
		var m = $.fullCalendar.formatDate(new Date(),"yyyy-MM-dd");
		var date = $.fullCalendar.parseDate(m);
		if(date>b){//截止时间已过当前时间，则不能添加
			$("#calendar").fullCalendar('unselect');
			return;
		}
		
	}else{
		if( "00:00" == $.fullCalendar.formatDate(d,"HH:mm") && "00:00" == $.fullCalendar.formatDate(b,"HH:mm")){
			scheStartDate = $.fullCalendar.formatDate(d,"yyyy-MM-dd");
			scheEndDate = $.fullCalendar.formatDate(b,"yyyy-MM-dd")
			
			var m = $.fullCalendar.formatDate(new Date(),"yyyy-MM-dd");
			var date = $.fullCalendar.parseDate(m);
			if(date>b){//截止时间已过当前时间，则不能添加
				$("#calendar").fullCalendar('unselect');
				return;
			}
		}else{
			scheStartDate = $.fullCalendar.formatDate(d,"yyyy-MM-dd HH:mm");
			scheEndDate = $.fullCalendar.formatDate(b,"yyyy-MM-dd HH:mm")
			formatEl = 1;
			
			var m = $.fullCalendar.formatDate(new Date(),"yyyy-MM-dd HH:mm");
			var date = $.fullCalendar.parseDate(m);
			if(date>b){//截止时间已过当前时间，则不能添加
				$("#calendar").fullCalendar('unselect');
				return;
			}
		}
	}
	var obj =new Object(); 
	//添加日程
	addSchedulePage(scheStartDate,scheEndDate);
	
	$("#calendar").fullCalendar('unselect');
	
	
	
}
/*
*d 对象
*b 移动的天数
*a 移动的分钟
*/
function doEventDrop(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
	var eventId = event.id;
	eventId=eventId.substring(0,eventId.indexOf("_"));
	$.ajax({
		type:"post",
		url:"/schedule/updateScheduleByDrag?sid=${param.sid}&t="+Math.random(),
		dataType: "json",
		data:{
			"id":eventId,
			"day":dayDelta,
			"minu":minuteDelta
		},
		success:function(data){
			var status = data.status;
			if(status=='y'){
				showNotification(1,"操作成功");
			}else{
				showNotification(2,"操作失败");
			}
		},
		error:function(XmlHttpRequest,textStatus,errorThrown){
			showNotification(2,"系统错误，请联系管理人员");
		}
	})
}
function  doEventResize(event, dayDelta, minuteDelta, c, f, e, g){
	var eventId = event.id;
	eventId=eventId.substring(0,eventId.indexOf("_"));
	$.ajax({
		type:"post",
		url:"/schedule/updateScheduleByResize?sid=${param.sid}&t="+Math.random(),
		dataType: "json",
		data:{
			"id":eventId,
			"day":dayDelta,
			"minu":minuteDelta
		},
		success:function(data){
			var status = data.status;
			if(status=='y'){
				showNotification(1,"操作成功");
			}else{
				showNotification(2,"操作失败");
			}
		},
		error:function(XmlHttpRequest,textStatus,errorThrown){
			showNotification(2,"系统错误，请联系管理人员");
		}
	})
}
</script>
<style>
.calendarWrapper {
width: 100%;
margin: 0 auto 15px;
}

#calendar {
	width: 100%;
	background: #fff;
	padding: 15px 10px;
}

.calendarWrapper .rightSidePanel {
	width: 240px;
	padding: 0px 15px;
}
.dib{display: inline-block;}
.fr{float: right;}
.fc-button {
	font-size:15px !important;
}
#modTypeId input{
margin-left: 8px
}
</style>
</head>
<body>
<!-- 系统头部装载 -->
		<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
		<!-- 数据展示区域 -->
	    <div class="main-container container-fluid">
	        <!-- Page Container -->
	        <div class="page-container">
	        	<%-- 大条件
				<jsp:include page="/WEB-INF/jsp/schedule/listSchedule_left.jsp"></jsp:include>
	        	 --%>
				<!-- 列表-->
				<jsp:include page="/WEB-INF/jsp/schedule/listSchedule_middle.jsp"></jsp:include>
	        </div>
	   		 <!--主题颜色设置按钮-->
			<jsp:include page="/WEB-INF/jsp/include/configTool.jsp"></jsp:include>
	    </div>
</body>
</html>
