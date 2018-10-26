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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	//关闭窗口
	function closeWin() {
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function() {
		//设置滚动条高度
		var height = $(window).height() - 40;
		$("#contentBody").css("height", height + "px");
		
	
		
		getCarStatus(getNowStr(),'${param.sid}')
	});
	//获取当前日期
	function getNowStr(){
		var date = new Date();
		var year = date.getFullYear(); //年
		var month = date.getMonth() + 1; //月
		var day = date.getDate(); //日
		var clock = year + "-";
		if (month < 10) {
			clock += "0";
		}
		clock += month + "-";
		if (day < 10) {
			clock += "0";
		}
		clock +=day;
		return clock;
	}
	
	//获取未来几天预约情况
	function getCarStatus(dateSting, sid) {
		$.ajax({
			url : "/car/carApplyStatus?sid=" + sid + "&beginDate=" + dateSting,
			async : false,
			type : "POST",
			dataType : "json",
			success : function(result) {
				if(result.status =='y'){
					$("#carTable").html('');
					var tbleHtml = '	<thead><tr role="row"><th style="width:100px"></th>';
						for(var i=0;i<result.nextWeekDay.length;i++){
							tbleHtml += '	<th >'+result.nextWeekDay[i].substring(5)+'</th>';
						}
						tbleHtml += '</tr></thead>';
						tbleHtml += '<tbody>';
						var listCar = result.listCar;
						for(var i=0;i<listCar.length;i++){
							tbleHtml += '<tr >';
							tbleHtml += '<td>'+listCar[i].carNum+'</td>';	
							
							var listCarApply = listCar[i].listCarApply;
							for(var j=0;j<7;j++){
								var listUse = listCarApply[j].listCarUseRecord;
								if(listUse.length == 0){
									tbleHtml += '<td bgcolor="#00ff00;"></td>';
								}else{
									var tdHtml = '';
									for(var k =0;k<listUse.length;k++){
										//处理日期 时间
										var startDateStr = listUse[k].startDate.replace(/\-/g, "\/");
										var endDateStr = listUse[k].endDate.replace(/\-/g, "\/");
										var nowDateMinStr =  listCarApply[j].dateStr.replace(/\-/g, "\/")+" 00:01";
										var nowDateMaxStr = listCarApply[j].dateStr.replace(/\-/g, "\/")+" 23:59";
										
										var startDate = new Date(startDateStr);
										var endDate = new Date(endDateStr);
										var nowDateMin = new Date(nowDateMinStr);
										var nowDateMax = new Date(nowDateMaxStr);
										
										if(startDate < nowDateMin){
											startDateStr = "00:00";
										}else{
											startDateStr = listUse[k].startDate.substring(11,16);
										}
										
										if(endDate > nowDateMax){
											endDateStr = "23:59";
										}else{
											endDateStr = listUse[k].endDate.substring(11,16);
										}
										var time = startDateStr +"-"+endDateStr;
										
										if(listUse[k].state == '0'){
											 tdHtml += '<div style="background-color:#FFD700;border:2px solid #fbfbfb" >'+time+'</div>';
										}else if(listUse[k].state == '1'){
											 tdHtml += '<div style="background-color:#FF0000;border:2px solid #fbfbfb"  >'+time+'</div>';
										}
									}
									
									tbleHtml += '<td class="no-padding">'+tdHtml+'</td>';
								}
							}
							tbleHtml += '</tr>';
						}
						tbleHtml += '</tbody>';
						$("#carTable").append(tbleHtml);
				}
				
			}
		})
	}
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">车辆近期预约情况</span>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times"></i>
							</a>
						</div>
					</div>
					<!--Widget Header-->
					<div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
					
					
					<div class="ticket-user pull-left ps-right-box ">
					<input type="hidden" id="nowDate" value="${nowDate }">
							开始日期：
							<input class="colorpicker-default form-control" type="text" value="${fn:substring(nowDate,0,10)}" readonly="readonly"
								onfocus="WdatePicker({onpicked:function(dp){getCarStatus(dp.cal.getDateStr(),'${param.sid}') },dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'nowDate\',{d:-0});}',readOnly:true})" style="width:125px;padding:0 0;display: inline;height:25px;">
						</div>    
					
					
					<table style="float:right">
					<tbody><tr>
						<td>图例说明：</td>
						<td width="20" bgcolor="#00ff00;"></td>
						<td width="40">空闲</td>
						<td width="20" bgcolor="#FFD700"></td>
						<td width="40">待批</td>
						<td width="20" bgcolor="#FF0000"></td>
						<td width="40">已准</td>
					</tr>
				</tbody>
				</table>
					<table id="carTable"  class="table"  border="0" cellspacing="1" cellpadding="3">
					</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="/static/assets/js/bootstrap.min.js"></script>

	<!--Beyond Scripts-->
	<script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
