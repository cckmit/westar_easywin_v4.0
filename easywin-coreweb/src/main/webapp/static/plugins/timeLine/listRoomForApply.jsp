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
<script type="text/javascript" src="/static/plugins/timeLine/js/dhtmlxscheduler.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/js/dhtmlxscheduler_timeline.js"></script>
<script type="text/javascript" src="/static/plugins/timeLine/js/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="/static/plugins/timeLine/css/dhtmlxscheduler.css">

<style type="text/css" media="screen">
		html, body{
			margin:0;
			padding:0;
			height:100%;
			overflow:hidden;
		}
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
		height:44px !important;
		top: 0px !important;
		filter: Alpha(Opacity=70);/*设置拖动DIV的背景透明 */
	}
	.dhx_scale_bar {
	background-image: url(/static/plugins/timeLine/dhx_cal_event_line_bg.gif) repeat-x;
	background-position: 0 0;
	background-repeat: no-repeat;
	background-color: transparent;
	padding-top: 3px;
	border-left: 0;
}
	</style>
<script type="text/javascript" charset="utf-8">
function init(){
	scheduler.locale = {  
		    date:{  
		        month_full:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],  
		        month_short:["1","2","3","4","5","6","7","8","9","10","11","12"],  
		        day_full:["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],  
		        day_short:["日","一","二","三","四","五","六"]  
		    },  
		    labels:{  
		        dhx_cal_today_button:"今天",  
		        day_tab:"日",  
		        week_tab:"周",  
		        month_tab:"月",  
		        new_event:"新日程安排",  
		        icon_save:"保存",  
		        icon_cancel:"取消",  
		        icon_details:"详细",  
		        icon_edit:"编辑",  
		        icon_delete:"删除",  
		        confirm_closing:"",  
		        confirm_deleting:"确定要删除该工作计划?",  
		        section_description:"工作计划"  
		    }  
		};  
		  
	/** 
	 * 日期显示格式配置 
	 */  
	scheduler.config.default_date = '%Y年%m月%d日';  
	scheduler.config.month_date = '%Y年%m月';  
	scheduler.config.day_date = '%Y年%m月%d日 %l'; 
	scheduler.locale.labels.timeline_tab = "Timeline";
	scheduler.locale.labels.section_custom="Section";
	scheduler.config.details_on_create=false;
	scheduler.config.details_on_dblclick=false;
	scheduler.config.xml_date="%Y-%m-%d %H:%i";
	
	 scheduler.config.drag_resize=false;   
     scheduler.config.edit_on_create=false;  
     scheduler.config.details_on_create=false;  
	 scheduler.config.dblclick_create=false;  
	 /*
     scheduler.config.drag_create=false;   
	 scheduler.config.readonly=true;  
	*/ 

	//===============
	//Configuration
	//===============
	var sections=[
		{key:1, label:"<div class='dhx_matrix_scell_title' style='height:44px;'><div style=' display: inline; margin-top: 2px; '><input id='room_1' name='autoSelectRoom' type='checkbox' onclick='selectMtRoom(this);'/></div><a href='#' style='text-align: left;' title='一号会议室' onclick=showMTInfo('1')>一号会议室</a></div>"},
		{key:2, label:"<div class='dhx_matrix_scell_title' style='height:44px;'><div style=' display: inline; margin-top: 2px; '><input id='room_2' name='autoSelectRoom' type='checkbox' onclick='selectMtRoom(this);'/></div><a href='#' style='text-align: left;' title='二号会议室' onclick=showMTInfo('2')>一号会议室</a></div>"},
	];

	scheduler.createTimelineView({
		name:	"timeline",
		x_unit:	"minute",
		x_date:	"%H:%i",
		x_step:	15,
		x_size: 63.9,
		x_start: 32,
		x_length:96,
		y_unit:	sections,
		y_property:	"section_id",
		render:"bar",
		dy:10
	});

	//===============
	//Data loading
	//===============
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
	scheduler.init('scheduler_here',new Date(2014,5,30),"timeline");
	scheduler.parse([
	{ start_date: "2014-06-30 09:00", end_date: "2014-06-30 12:00", text:"Task A-12458", section_id:1,color:"red","state":1,"status":2,readonly:true}
	],"json");
	scheduler.getEvent(1).readonly=true;
	
}
</script>
</head>
<body onload="init();">
		<div id="scheduler_here" class="dhx_cal_container dhx_scheduler_timeline" style="width:100%; height:100%;">
		<div class="dhx_cal_navline" style="width: 1366px; height: 59px; left: 0px; top: 0px;">
			<div class="dhx_cal_prev_button">&nbsp;</div>
			<div class="dhx_cal_next_button">&nbsp;</div>
			<div class="dhx_cal_today_button">Today</div>
			<div class="dhx_cal_date">30 Jun 2014</div>
		</div>
		<div class="dhx_cal_header" style="width: 1348px; height: 20px; left: -1px; top: 60px;"></div>
		<div class="dhx_cal_data" style="height: 370px; left: 0px; top: 56px; cursor: default;"></div>
	</div>



</body></html>