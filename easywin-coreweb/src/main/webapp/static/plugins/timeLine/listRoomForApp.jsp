<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
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
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/dhtmlxscheduler.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/locale_cn.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/locale_recurring_cn.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/dhtmlxscheduler_tooltip.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/dhtmlxscheduler_timeline.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/dhtmlxscheduler_minical.js"></script>
<script type="text/javascript" src="/static/plugins/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="/static/plugins/timeLine/dhtmlxscheduler_glossy.css">
<link rel="stylesheet" type="text/css" href="/static/plugins/timeLine/skin(2).css">
<style type="text/css" media="screen">
		body{
		margin:0px;
		padding:0px;
		height:100%;
		overflow:hidden;
		position:absolute;
		left:0;
		width:100%;
	}
	.one_line{
		white-space:nowrap;
		overflow:hidden;
		padding-top:5px;
		padding-left:5px;
		text-align:left!important;
	}
	.dhx_cal_event_line,.dhx_cal_event_line1,.dhx_cal_event_line2 {
		height:85%;
		/**filter: Alpha(Opacity=70);设置拖动DIV的背景透明 */
	}
	</style>
<script type="text/javascript" charset="utf-8">
function loadUE() {
	if("" != "portalRoomApp"){
		$('body').height("100%");
	}else {//二级栏目
		$('#scheduler_here').height("83%");
	}
	
		var _body = $('body').width(); //这里的判断主要是时间安排portal的周视图窄栏处理
		if (_body < 690) {
			$('body').width(1052);
		}
	
}
var action="create";
var old_id = "";//记录上一次选择的时间段自动生成的Id
var oldRoomAppId = null;//记录回显时的Id
var currentdate = "";//当前的时间,不一定是当前,可以是随意选择的时间
var deleteStatusId = "";//删除状态,在移动已经申请过的会议室时,要给它赋上值
function init(){
	var sections = [{key:"1",label:"<div class='dhx_matrix_scell_title' style='height:44px;'><div style=' display: inline; margin-top: 2px; '><input id='room_1' name='autoSelectRoom' type='checkbox' onclick='selectMtRoom(this);'/></div><a href='javascript:void(0)' style='text-align: left;' title='一号会议室' onclick=showMTInfo('1')>一号会议室</a></div>"},{key:"2",label:"<div class='dhx_matrix_scell_title' style='height:44px;'><div style=' display: inline; margin-top: 2px; '><input id='room_2' name='autoSelectRoom' type='checkbox' onclick='selectMtRoom(this);'/></div><a href='javascript:void(0)' style='text-align: left;' title='二号会议室' onclick=showMTInfo('2')>二号会议室</a></div>"}];
	scheduler.locale.labels.timeline_tab = "时间";
	scheduler.locale.labels.section_custom="会议室申请";
	scheduler.config.details_on_create=true;
	scheduler.config.details_on_dblclick=true;
	scheduler.config.xml_date="%Y-%m-%d %H:%i";
	scheduler.createTimelineView({
		name:"timeline",
		x_unit:"minute",
		x_date:"%H:%i",
		x_step:15,
		x_size:63.9,//判断时间长度
		x_start:32,
		x_length:96,
		y_unit:sections,
		y_property:"section_id",
		render:"bar",
		dy:10
	});
	scheduler._render_x_header = function (a, b, c, d) {
		var e = document.createElement("DIV");
		e.className = "dhx_scale_bar";
		this.set_xy(e, this._cols[a] - 1, this.xy.scale_height - 2, b, 0);
		var timeVar=this.templates[this._mode + "_scale_date"](c, this._mode);

		if(timeVar.substring(3)=="00") {
			e.innerHTML ="&nbsp;&nbsp;&nbsp;"+timeVar;
			d.appendChild(e);
		}
	};
	scheduler.init('scheduler_here',null,"timeline");
	var jsonDate = '{"color":"","createUserName":"周智强","end_date":"2016-05-06 12:00","id":"123","meetingid":"123","mtappid":"123","section_id":"1","start_date":"2016-05-06 09:00","state":0,"status":3,"text":"","textColor":"","text_hid":"","timeout":1,"upmtid":"123"}';
	var jsonMeeting = "["+jsonDate + "]";
	scheduler.parse(jsonMeeting, "json");
	//-----------------------------回显手动选择的会议室(考虑了周期性的)------------------------------------start
	var returnMrs=null;//在没入库前,点击再次进入时要显示回显的数据
	if(returnMrs!=null&&""!=returnMrs&&"null"!=returnMrs){
		$("#start_time").val(returnMrs[0].start_date);
		$("#end_time").val(returnMrs[0].end_date);


		//从父页面获得周期性的所有日期字符串
		var periodicityDates = getPeriodicityDatesInParentPage();
		if(periodicityDates != ""){
			try{
				var start_date = returnMrs[0].start_date.substr(0,10);
				//当设置了周期性后，再选择了会议室，确定后，再打开会议室时，就要判断当前日期是否在所设置的周期性时间内，在的话当前日期时间区域才会显示出来
				if(periodicityDates.indexOf(start_date)>-1){
					scheduler.parse(returnMrs, "json");//加载选择的记录
					var roomM = document.getElementById("room_"+returnMrs[0].section_id);
					if(roomM){
						roomM.checked = true;
						room_obj = document.getElementById("room_"+returnMrs[0].section_id);
					}
				}
			}catch(e){
			}
		}else{
			scheduler.parse(returnMrs, "json");//加载选择的记录
			var roomM = document.getElementById("room_"+returnMrs[0].section_id);
			if(roomM){
				roomM.checked = true;
				room_obj = document.getElementById("room_"+returnMrs[0].section_id);
			}
		}

	}
	//-----------------------------回显手动选择的会议室(考虑了周期性的)------------------------------------end
	else if(window.opener || window.dialogArguments){
		var _parentWindow = window.opener || window.dialogArguments;
		if(_parentWindow.document.getElementById("beginDate") && _parentWindow.document.getElementById("endDate")){
			$("#start_time").val(_parentWindow.document.getElementById("beginDate").value);
			$("#end_time").val(_parentWindow.document.getElementById("endDate").value);
		}
	}
	
	scheduler.showLightbox = function(id){
		var ev = scheduler.getEvent(id);
	    var convert = scheduler.date.date_to_str("%Y-%m-%d %H:%i");
	    var start_date = convert(ev.start_date);
	    var end_date = convert(ev.end_date);
	    //判断用户选反时间是否是>=30分钟,如果超过三十分钟则进行其他操作,不到三十分钟则提示并删除当前选择项
	    //if(((getDateTimeStamp(end_date))-(getDateTimeStamp(start_date)))>=1800000){
	    	var section_id=ev.section_id;
			//在绘制新的时间段时,要先恢复以前拖动的时间段---------------------------------------------Start
			if(currentdate==null||""==currentdate||"undefined"==currentdate){
				currentdate=new Date();
			}
		  	//如果在绘制新的时间段时,要先恢复以前拖动的时间段---------------------------------------------End
		  	//在绘制新的时间段时,删除原来"回显"时绘制的时间段---------------------------------------------Statr
			if(oldRoomAppId!=null&&""!=oldRoomAppId&&"null"!=oldRoomAppId){
				scheduler.deleteEvent(oldRoomAppId);
			}
			//在绘制新的时间段时,删除原来"回显"时绘制的时间段---------------------------------------------End
			//如果用户选择第二个时间段时清除上一个新建的时间段--------------------------------------------Start
			if((""!=old_id)||(null!=old_id)){
				scheduler.deleteEvent(old_id);
			}
			//如果用户选择第二个时间段时清除上一个选择的时间段--------------------------------------------End
			old_id=id;
		    document.getElementById("meetingRoom").value=section_id;
			document.getElementById("startDate").value=start_date;
			document.getElementById("endDate").value=end_date;

			//原来的方法保留并做修改(注销)
			if (id && this.callEvent("onBeforeLightbox", [id])) {
				var b = this._get_lightbox();
				//this.showCover(b);//去掉弹出的框
				this._fill_lightbox(id, b);
				this.callEvent("onLightbox", [id]);
		    }
		    //保存
			scheduler.save_lightbox();

			//同步信息到上面的时间框里面
			$("#start_time").val(start_date);
			$("#end_time").val(end_date);
			disableCheckBox();
			room_obj = document.getElementById("room_"+section_id);
			if(!room_obj.checked){
				room_obj.checked = true;
			}else {
				room_obj.checked = false;
			}
		//}else{
			//alert("会议时间必须超过三十分钟");
			//scheduler.deleteEvent(id);
		//}
	}
	
	//----------------------------------------------------------------------移动后触发
	scheduler.attachEvent("onEventChanged", function(event_id, event_object){
	    var id = event_object.id;
	    var text = event_object.text;
	    var convert = scheduler.date.date_to_str("%Y-%m-%d %H:%i");
	    var start_date = convert(event_object.start_date);
	    var end_date = convert(event_object.end_date);
        var section_id = event_object.section_id;
        var status=event_object.status;
        if(status==2){//如果拖动从数据库读出的时间段,则把新拖动的给删除掉
		    scheduler.deleteEvent(old_id);//删除上一个新建的时间段
		    scheduler.deleteEvent(oldRoomAppId);//删除回显的时间段
		    deleteStatusId=id;
        	//old_id=id;
		}

        document.getElementById("meetingRoom").value=section_id;
		document.getElementById("startDate").value=start_date;
		document.getElementById("endDate").value=end_date;
	    //这里使用true刷心主窗口
	    return true;
	});
	//--------------------------------------------------------------------拖动时触发
	scheduler.attachEvent("onBeforeEventChanged", function (a) {
		//用来记录是添加还是修改的标记(如果是添加则不能拖动任何的时间段,如果是修改,再去判断拖动的是否是当前的会议,如果是则可拖动,否则不难拖动)
		var action="create";
		//修改的时候取得会议的ID用来判断只许修改当前ID的会议
		var meetId="";
		//添加,时不能修改其他的会议时间段,如果meetingid!=null的话就表示当前是拖动的其他时间段,如果是新增的这个是不会有值的
		if(action=="create" && a.meetingid!=null){
			alert("不能修改已申请的时间段。");
			return false;
		}
		//修改时meetId会有值,判断当前拖动的会议时间段是不是属于当前会议室
		if(a.meetingid!=null){
			if(action=="edit" && meetId!=a.meetingid){
				alert("对不起,此时间段不属于当前会议。");
				return false;
			}
		}
		//检查当选择的时间是否小于系统时间，如查小于系统时间则不允许选择会议室
		var convert1 = scheduler.date.date_to_str("%Y-%m-%d");
		if(currentdate!=null&&""!=currentdate){
			var curdate=convert1(currentdate);
			var nowdate=convert1(new Date());
			if(curdate<nowdate){
				//alert("选择的时间不能小于系统当天时间。");
				alert("选择的时间不能小于系统当前时间!");
				return false;
			}
		}
		var id = a.id;
		var status=a.status;//判断是否有权限修改 1不是本人添加的时间,2是本人添加,3其他情况
		var timeout=a.timeout;
	    var convert = scheduler.date.date_to_str("%Y-%m-%d %H:%i");
	    var start_date = convert(a.start_date);
	    var end_date = convert(a.end_date);
	    if(convert(new Date())>start_date){
			alert("开始时间不能小于系统当前时间!");//开始时间不能小于系统当前时间
		    return false;
		}
        var section_id = a.section_id;
		var flag=false;
        if(status==1){//没有权限
			alert("对不起,只能修改自己添加的会议室！!");
			return false;
		}
		if(timeout==2){//会议已经开始
			alert("会议已经开始不能修改!");
			return false;
		}
		if(timeout==3){//会议已经结束
			alert("会议已经结束不能修改!");
			return false;
		}
		if(status==2||status==null||""==status||"undefined"==status){//有权限或者是新建都,要去后台查询是否被占用
			flag = checkRoomDisabled(start_date, end_date, section_id);
		}else{//其他情况
			alert("Error");
		}
		//同步到上方的时间框和右侧的勾选框
		$("#start_time").val(start_date);
		$("#end_time").val(end_date);
		disableCheckBox();
		room_obj = document.getElementById("room_"+section_id);
		if(!room_obj.checked){
			room_obj.checked = true;
		}else {
			room_obj.checked = false;
		}
		return flag;
	});
	
	//鼠标放在上边提示的信息
	scheduler.templates.tooltip_text = function (b, d, c) {
		var status=c.status;//鼠标放在上边就为隐藏表单赋值
		if(status==2||status==null||""==status||"undefined"==status){
			//这里赋值是为了区分在删除的时候,是删除数据库还是删除刚新建的会议室
			document.getElementById("mtid").value=c.mtappid;
			document.getElementById("boxid").value=c.id;
		}
		var startdate=scheduler.templates.tooltip_date_format(b);
		var enddate=scheduler.templates.tooltip_date_format(d);
		var startdates=startdate.split(" ");
		var enddates=enddate.split(" ");
		var time=startdates[1]+"—"+enddates[1];
		var text=c.text_hid;
		var tooltipMsg="";
		if(null!=c.createUserName && ""!=c.createUserName && "undefined" != c.createUserName){
		tooltipMsg+="<div style='height:18px;'>&nbsp;&nbsp;&nbsp;<b>申请人:</b> "+c.createUserName+"</div>";
		}
		tooltipMsg+="<b>会议时间:</b> " + time;
		return tooltipMsg;
	};

}

//日期转换，将date日期转换为yyyy-mm-dd类型
function formatDate(v){
   if(typeof v == 'string') v = parseDate(v);
   if(v instanceof Date){
     var y = v.getFullYear();
     var m = v.getMonth() + 1;
     var d = v.getDate();
     var h = v.getHours();
     var i = v.getMinutes();
     var s = v.getSeconds();
     var ms = v.getMilliseconds();
     if(ms>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s + '.' + ms;
     if(h>0 || i>0 || s>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;
     if(m<10)m = "0"+m;
     if(d<10)d = "0"+d;
     return y + '-' + m + '-' + d;
   }
   return '';
}

//将时间字符串转换成毫秒
function getDateTimeStamp(dateStr){
	return Date.parse(dateStr.replace(/-/gi,"/"));
}
function disableCheckBox(){
	var _allCheckBox = document.getElementsByName("autoSelectRoom");
	if(_allCheckBox){
		for(var i=0; i<_allCheckBox.length; i++){
			document.getElementById(_allCheckBox[i].getAttribute("id")).checked = false;
		}
	}
}

//会议时候被占用
function checkRoomDisabled(start_date, end_date, section_id){
	var _result = true;
	return _result;
}
/**
 * 时间校验
 */
function _vilidateDate(){
    
    var autoSelectRooms = document.getElementsByName("autoSelectRoom");
    var temp_start_time = $("#start_time").val();
    var temp_end_time = $("#end_time").val();
    
    var start_time = $("#start_time").val();
    var end_time = $("#end_time").val();
    

    var convert = scheduler.date.date_to_str("%Y-%m-%d %H:%i");
    //获得服务器端的当前日期时间
    var newDate = "2016/05/06 13:39";
    
    if(start_time != "" && convert(new Date(newDate))>start_time){
        alert("开始时间不能小于系统当前时间!");//开始时间不能小于系统当前时间
        $("#start_time").val(temp_start_time);
        $("#end_time").val(temp_end_time);
        return false;
    }
    if(start_time != "" && end_time != "" && start_time>end_time){
        alert("开始时间不能大于结束时间");
        $("#start_time").val(temp_start_time);
        $("#end_time").val(temp_end_time);
        return false;
    }
    
    if(start_time != "" && end_time != ""){
        for(var i=0; i<autoSelectRooms.length; i++){
            if(autoSelectRooms[i].checked){
                if(!checkRoomDisabled($("#start_time").val(), $("#end_time").val(), 
                    autoSelectRooms[i].getAttribute("id").split("_")[1])){
                    $("#start_time").val(temp_start_time);
                    $("#end_time").val(temp_end_time);
                    return ;
                }
                selectMtRoom(autoSelectRooms[i], "timeWidget")
                break;
            }
        }
    }
}
var save_old_section_id = "";//当拖动的蓝色区域直接删除时，要记录删除的对应的section_id
function selectMtRoom(obj, from){
	//如果是勾选
	if(document.getElementById(obj.getAttribute("id")).checked){
		//当时间为空时，勾选一个会议室，需要将其他的取消掉
		disableCheckBox();
		document.getElementById(obj.getAttribute("id")).checked = true;
	}

	var id_str = obj.getAttribute("id");
	id_str = id_str.split("_")[1];
	var start_time = $("#start_time").val();
	var end_time = $("#end_time").val();

	var convert = scheduler.date.date_to_str("%Y-%m-%d %H:%i");
	if(start_time == "" || end_time == ""){
		return false;
	}else if(convert(new Date())>start_time){
		/*
		if(periodicityDates==""){
			alert("开始时间不能小于系统当前时间!");//开始时间不能小于系统当前时间
			obj.checked = false;
		    return false;
		}*/
	}
	//校验会议室是否已经被占用了
	if(from != "timeWidget" && !checkRoomDisabled(start_time, end_time, id_str)){
		obj.checked = false;
		return false;
	}

	if(start_time != '' && end_time != ''){
		var old_section_id = "";
		var jsonDate = [{id:id_str,start_date: start_time, end_date: end_time,section_id:id_str}
		                ];
		for(var i in scheduler._events){
			if(!scheduler._events[i].createUserName && !scheduler._events[i].mtappid ){
				old_id = i;
				break;
			}
		}
		if(old_id != null && old_id != ""){
			try{
				old_section_id = scheduler._events[old_id].section_id;
			}catch(e){
				old_section_id = save_old_section_id;
			}
			//document.getElementById("room_"+old_section_id).checked = false;
			scheduler.deleteEvent(old_id);
			old_id = "";
		}

		if(from != "timeWidget" && obj.getAttribute("id") == ("room_"+old_section_id) &&
		 !document.getElementById(obj.getAttribute("id")).checked){
			document.getElementById("room_"+old_section_id).checked = false;
			document.getElementById("meetingRoom").value="";
			document.getElementById("startDate").value="";
			document.getElementById("endDate").value="";
			/*  下面代码会将会议通知页面的 时间和地点清空，不清楚老代码为什么要这样做，这样有问题啊
			if(window.opener || window.dialogArguments){
				var _p = window.opener || window.dialogArguments;
				if(_p.cleanMt)
					_p.cleanMt();
			}*/
			return ;
		}


    	var time=$("#start_time").val().substr(0,10);
    	var flag = false;
    	if(periodicityDates!=""){
        	//当是周期性会议时，需要判断当前日期 是否在周期性里面，在才显示出来
    		if(periodicityDates.indexOf(time)>-1){
        		flag = true;
    		}
    	}else{
        	flag = true;
    	}

		if(flag){
			scheduler.parse(jsonDate, "json");
			for(var i in scheduler._events){
				if(!scheduler._events[i].createUserName && !scheduler._events[i].mtappid ){
					old_id = i;
					save_old_section_id = old_id;
					break;
				}
			}

			document.getElementById("meetingRoom").value=id_str;
			document.getElementById("startDate").value=start_time;
			document.getElementById("endDate").value=end_time;

			//原来的方法保留并做修改(注销)
			if (old_id && scheduler.callEvent && scheduler.callEvent("onBeforeLightbox", [old_id])) {
				var b = scheduler._get_lightbox();
				//this.showCover(b);//去掉弹出的框
				scheduler._fill_lightbox(old_id, b);
				scheduler.callEvent("onLightbox", [old_id]);
			}

			//保存
			scheduler.save_lightbox();
		}
	}

	disableCheckBox();
	if(!document.getElementById(obj.getAttribute("id")).checked){
		document.getElementById(obj.getAttribute("id")).checked = true;
		room_obj = obj;
	}
	resetDate(start_time);

}

function resetDate(start_time){
	var convert = scheduler.date.date_to_str("%Y-%m-%d");
	var nowDate = convert(new Date());
	var selected_start_Date = start_time.split(" ")[0];
	//if(nowDate != selected_start_Date){
		//var interval_date = (new Date(selected_start_Date) - new Date(nowDate))/1000/60/60/24;
		timebutton(new Date(selected_start_Date.replace(/-/g,"/")));
		scheduler.setCurrentView(new Date(selected_start_Date.replace(/-/g,"/")));
	//}
}
function selectRoomTime(thisDom) {
	_vilidateDate();
}
//点击删除时弹出提示框,点击确定则删除,否则取消删除
var deleteFlag = false;/** xiangfan 修复GOV-3901，默认为false */
function showDelete(id){
	var flag=confirm("您确定要删除吗?");
	if(flag){
		scheduler.deleteEvent(id);//提示删除成功
        alert("删除成功");
        deleteFlag = true;/** xiangfan 修复GOV-3901，如果是真删，设置为 true */
	}
}
//点击选择时间调用方法
function timebutton(date){
	var action="create";
	var meetId="";
	//格試化日期
	var time=formatDate(date);
	currentdate=date;
	timebuttonFun();
	$("#selectDate").val(time)
}
var room_obj;
function timebuttonFun(){
	var jsonDate = "";
	var jsonMeeting=[];
	var time=formatDate(currentdate);
	var start_time = $("#start_time").val();
	var end_time = $("#end_time").val();
	scheduler.parse(jsonMeeting, "json");
 	disableCheckBox();
 	if(room_obj){
		document.getElementById(room_obj.getAttribute("id")).checked = true;
	}
}
</script>
</head>
<body onload="loadUE();init();" width="100%" style="overflow-x: auto; overflow-y: hidden; height: 100%;">
<input type="hidden" id="meetingRoom" name="meetingRoom" value="">
<input type="hidden" id="startDate" name="startDate" value="">
<input type="hidden" id="endDate" name="endDate" value="">
<input type="hidden" id="mtid" name="mtid">
<input type="hidden" id="boxid" name="boxid">

	<div id="scheduler_here" class="dhx_cal_container" style="width:100%;height:91%">
		<div class="dhx_cal_navline" style="height: 34px; left: 0px; top: 0px; background-color: white;">
			<div class="dhx_cal_prev_button" style=" margin-top: 3px;cursor: pointer;">&nbsp;&lt;</div>
			<input type="text" id="selectDate" class="dhx_cal_date Wdate" readonly="readonly" 
			onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="margin-left: 40px" value="2016-05-06">
			<div class="dhx_cal_next_button" style=" margin-top: 3px;cursor: pointer; ">&nbsp;&gt;</div>
			<div class="dhx_cal_today_button" style="display:none;">今天</div>
			<div class="dhx_cal_tab" name="timeline_tab" style="right:20px; display:none;">时间</div>
			<div style=" margin-left: 230px;">
					开始时间:<input type="text" style=" width: 125px; " id="start_time" name="textfield" class="Wdate"
					 onfocus="WdatePicker({onpicked:function(dp){selectRoomTime(dp.cal.getNewDateStr())},dateFmt:'yyyy-MM-dd HH:mm',maxDate: '#F{$dp.$D(\'end_time\',{d:-0});}'})" readonly="">
					结束时间:<input type="text" style=" width: 125px; " id="end_time" name="textfield" class="Wdate" 
					 onfocus="WdatePicker({onpicked:function(dp){selectRoomTime(dp.cal.getNewDateStr())},dateFmt:'yyyy-MM-dd HH:mm',minDate: '#F{$dp.$D(\'start_time\',{d:-0});}'})" readonly="">
			</div>
			<div style="right:25px;">
				<table>
					<tbody><tr>
						<td>图例说明：</td>
						<td width="20"><div style="width:15px;height:15px;background-color:#ffffff; border:1px solid #000;"></div></td>
						<td>空闲</td>
						<td width="20"><div style="width:15px;height:15px;background-color:#7f7f7f; border:1px solid #000;"></div></td>
						<td>已预订</td>
						<td width="20"><div style="width:15px;height:15px;background-color:#64be0f; border:1px solid #000;"></div></td>
						<td>申请中</td>
					</tr>
				</tbody></table>
			</div>
		</div>
		<div class="dhx_cal_header" style="height: 20px; left: -1px; top: 35px;"></div>
		<div class="dhx_cal_data" style="height: 370px; left: 0px; top: 56px; cursor: default;"></div>
	</div>

</body>
</html>