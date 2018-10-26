<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
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
	//工作日模板html
	var attenceWeekDivHtml = $('<div class="pull-left padding-right-5 padding-top-5"><span class="tag"><span class="weekDayName">星期一</span><a href="javascript:void(0)" title="删除" class="delWeekDayBtn">x</a></span></div>');
	//工作时段html
	var attenceTimeDivHtml = $('<div class="attenceTimeDiv padding-bottom-5"><span class="dayTimeSSpan"><span>签到</span><input class="dpd2 no-padding dayTimeS padding-left-10" type="text" id="dayTimeS0_0" colNum="0" rowNum="0" readonly="readonly"></span><span class="dayTimeESpan"><span>签退</span><input class="dpd2 no-padding dayTimeE padding-left-10" type="text" id="dayTimeE0_0" colNum="0" rowNum="0" readonly="readonly"></span><span class="optSpan"><a href="javascript:void(0)" class="fa fa-times fa-lg padding-left-10 delBtn"></a><a href="javascript:void(0)" class="fa fa-plus fa-lg padding-left-10 addBtn"></a></span></div>');
	//行数标识
	var rowNum = 0;
	//列数标识
	var colNum = 0;
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		//选中下拉框后，显示选中项，请复位
		$(document).on("click","tr select",function(){
			var value = $(this).val();
			if(value>0){//有选中项
				//复位
				$(this).val(0);
				//判断是否有选中星期数
				var chooseLen = $(this).parents("table").find("a[data='"+value+"']");
				if(chooseLen.length == 0){//没有选过
					var attenceWeekDiv = $(attenceWeekDivHtml).clone();
					//缓存数据
					$(attenceWeekDiv).find(".delWeekDayBtn").attr("data",value);
					$(attenceWeekDiv).find(".weekDayName").html($(this).find("option[value='"+value+"']").text());
					
					$(this).parents("td").find(".attenceWeekDiv").append($(attenceWeekDiv));
				}else{
					layer.tips("工作日已选择",$(this).parents("table").find("a[data='"+value+"']").parents(".tag"),{"tips":1});
				}
			}
		})
		//删除单个工作日
		$(document).on("click","tr .delWeekDayBtn",function(){
			$(this).parents(".tag").parent().remove();
		});
		
		
		//添加工作时段按钮
		$("body").on("click",".attenceTimeDiv .optSpan .addBtn",function(){
			var pDiv = $(this).parents(".attenceTimeDiv").parent();
			var childLen = $(pDiv).children().length;
			if(childLen==3){
				layer.tips("工作时段不超过三个",$(pDiv),{tips:1})
			}else{
				var $html = $(this).parents(".attenceTimeDiv").clone();
				$html.find("input").val('');
				
				rowNum++;
				var thisColNum = $html.find(".dayTimeS").attr("colNum");
				$html.find(".dayTimeS").attr("id","dayTimeS"+thisColNum+"_"+rowNum);
				$html.find(".dayTimeS").attr("rowNum",rowNum);
				
				$html.find(".dayTimeE").attr("id","dayTimeE"+thisColNum+"_"+rowNum);
				$html.find(".dayTimeE").attr("rowNum",rowNum);
				
				pDiv.append($html);
			}
			
		})
		//删除工作时段按钮
		$("body").on("click",".attenceTimeDiv .optSpan .delBtn",function(){
			var pDiv = $(this).parents(".attenceTimeDiv").parent();
			var childLen = $(pDiv).children().length;
			if(childLen==1){
				layer.tips("至少保留一个",$(pDiv),{tips:1})
			}else{
				$(this).parents(".attenceTimeDiv").remove();
			}
		})
		//删除一行
		$(document).on("click",".delAttenceWeekBtn",function(){
			var len = $("#singleTable").find("tr").length;
			if(len==1){
				layer.tips("至少保留一个",$(this).parents("table"),{tips:1})
			}else{
				$(this).parents("tr").remove();
			}
		})
		//添加一行
		$(document).on("click",".addAttenceWeekBtn",function(){
			var len = $("#singleTable").find("tr").length;
			if(len==7){
				layer.tips("工作时段不超过七个",$("#singleTable"),{tips:1})
			}else{
				colNum ++;
				rowNum ++;
				var temp = $("#singleTable").find(".singleTr")[0];
				var tr = $(temp).clone();
				//工作日清空
				$(tr).find(".attenceWeekDiv").html('');
				
				var attenceTimeDiv = $(attenceTimeDivHtml).clone();
				$(attenceTimeDiv).find(".dayTimeS").attr("colNum",colNum);
				$(attenceTimeDiv).find(".dayTimeE").attr("colNum",colNum);
				
				$(attenceTimeDiv).find(".dayTimeS").attr("id","dayTimeS"+colNum+"_"+rowNum);
				$(attenceTimeDiv).find(".dayTimeS").attr("rowNum",rowNum);
				
				$(attenceTimeDiv).find(".dayTimeE").attr("id","dayTimeE"+colNum+"_"+rowNum);
				$(attenceTimeDiv).find(".dayTimeE").attr("rowNum",rowNum);
				
				$(tr).find(".attenceTimeDiv").parent().html($(attenceTimeDiv));
				$("#singleTable").append($(tr));
			}
		})
		
		initDatePicker();
		
		
	})
	//初始化页面信息
	function initData(listAttenceType,ruleType){
		if(listAttenceType && listAttenceType.length>0){
			if(ruleType==2){//单双周
				
			}else if(ruleType==3){//灵活考勤
				$.each(listAttenceType,function(typeIndex,attenceType){
					colNum ++;
					var temp = $("#singleTable").find(".singleTr")[0];
					var tr = $(temp).clone();
					//工作日清空
					$(tr).find(".attenceWeekDiv").html('');
					
					$.each(attenceType.listAttenceWeeks,function(weekIndex,attenceWeek){
						var attenceWeekDiv = $(attenceWeekDivHtml).clone();
						//缓存数据
						$(attenceWeekDiv).find(".delWeekDayBtn").attr("data",attenceWeek.weekDay);
						var weekDayNameTd = "星期"+(attenceWeek.weekDay==7?"日":attenceWeek.weekDay==6?"六":attenceWeek.weekDay==5?"五":attenceWeek.weekDay==4?"四":attenceWeek.weekDay==3?"三":attenceWeek.weekDay==2?"二":"一");
						$(attenceWeekDiv).find(".weekDayName").html(weekDayNameTd);
						$(tr).find(".attenceWeekDiv").append($(attenceWeekDiv));
					});
					
					$.each(attenceType.listAttenceTimes,function(timeIndex,attenceTime){
						rowNum ++;
						
						var attenceTimeDiv = $(attenceTimeDivHtml).clone();
						$(attenceTimeDiv).find(".dayTimeS").attr("colNum",colNum);
						$(attenceTimeDiv).find(".dayTimeE").attr("colNum",colNum);
						
						$(attenceTimeDiv).find(".dayTimeS").attr("id","dayTimeS"+colNum+"_"+rowNum);
						$(attenceTimeDiv).find(".dayTimeS").attr("rowNum",rowNum);
						
						$(attenceTimeDiv).find(".dayTimeE").attr("id","dayTimeE"+colNum+"_"+rowNum);
						$(attenceTimeDiv).find(".dayTimeE").attr("rowNum",rowNum);
						
						$(attenceTimeDiv).find(".dayTimeS").val(attenceTime.dayTimeS);
						$(attenceTimeDiv).find(".dayTimeE").val(attenceTime.dayTimeE);
						
						$(tr).find(".attenceTimeDiv").parent().append($(attenceTimeDiv));
						if(timeIndex==0){
							$(tr).find(".attenceTimeDiv").eq(0).remove();
						}
					});
					
					$("#singleTable").append($(tr));
					
					if(typeIndex==0){
						$("#singleTable").find("tr").eq(0).remove();
					}
				})
			}
		}
	}
	//保存规则
	function saveRule(){
		return $("#singleTable").data("attenceTypes");
	}
	//验证规则
	function checkRule(ruleType){
		//默认验证通过
		var a = 1;
		if(ruleType==2){
			
		}else if(ruleType==3){
			//返回结果类型集合
			var attenceTypeArray = new Array();
			//遍历规则，按行处理
			$.each($("#singleTable").find("tr"),function(weekIndex,typeVo){
				
				var attenceType = {"weekType":-1};
				//工作时段集合
				var attenceTimeArray = new Array();
				//工作日集合
				var attenceWeekArray = new Array();
				
				//验证工作日设定
				var weeksLen = $(typeVo).find(".attenceWeekDiv").children().length;
				if(weeksLen==0){
					layer.tips("请选择工作日",$(typeVo).find(".attenceWeekDiv"),{"tips":1});
					a = !1;
					return a;
				}else{
					$.each($(typeVo).find(".attenceWeekDiv").children(),function(weekIndex,weekVo){
						var data = $(weekVo).find(".delWeekDayBtn").attr("data");
						var attenceWeek = {"weekDay":data};
						attenceWeekArray.push(attenceWeek);
					})
				}
				attenceType.listAttenceWeeks = attenceWeekArray;
				
				$.each($(typeVo).find(".attenceTimeDiv"),function(timeIndex,vo){
					var dayTimeS = $(vo).find(".dayTimeS").val();
					if(!dayTimeS){
						layer.tips("请填写完整时间",$(vo).find(".dayTimeS"),{"tips":1});
						a = !1;
						return a;
					}
					var dayTimeE = $(vo).find(".dayTimeE").val();
					if(!dayTimeE){
						layer.tips("请填写完整时间",$(vo).find(".dayTimeE"),{"tips":1});
						a = !1;
						return a;
					}
					//考勤时间
					var attenceTime = {"dayTimeS":dayTimeS,"dayTimeE":dayTimeE};
					attenceTimeArray.push(attenceTime);
				})
				attenceType.listAttenceTimes = attenceTimeArray;
				attenceTypeArray.push(attenceType);
			});
			
			$("#singleTable").data("attenceTypes",attenceTypeArray);
		}
		return a;
	}
	
	//初始化日期选择控件
	function initDatePicker(){
		$(document).on("focus",".attenceTimeDiv .dayTimeSSpan input",function(){
			//本行数据
			var rowNum = $(this).attr("rowNum");
			var colNum = $(this).attr("colNum");
			
			var prev = $(this).parents(".attenceTimeDiv").prev();
			if(prev.length==0){
				//日期小于本次签退时间，大于上次签退时间
				WdatePicker({dateFmt:'HH:mm',maxDate: '#F{$dp.$D(\'dayTimeE'+colNum+'_'+rowNum+'\',{m:-1});}'})
			}else{
				var preRowNum = $(prev).find(".dayTimeSSpan input").attr("rowNum");
				//日期小于本次签退时间，大于上次签退时间
				WdatePicker({dateFmt:'HH:mm',maxDate: '#F{$dp.$D(\'dayTimeE'+colNum+'_'+rowNum+'\',{m:-1});}',minDate: '#F{$dp.$D(\'dayTimeE'+colNum+'_'+preRowNum+'\',{m:+1});}'})
			}
		})
		$(document).on("focus",".attenceTimeDiv .dayTimeESpan input",function(){
			//本行数据
			var rowNum = $(this).attr("rowNum");
			var colNum = $(this).attr("colNum");
			var next = $(this).parents(".attenceTimeDiv").next();
			if(next.length==0){
				//日期大于本次签到时间，小于下次签退时间
				WdatePicker({dateFmt:'HH:mm',minDate: '#F{$dp.$D(\'dayTimeS'+colNum+'_'+rowNum+'\',{m:+1});}'});
			}else{
				var nextRowNum = $(next).find(".dayTimeESpan input").attr("rowNum");
				//日期大于本次签到时间，小于下次签退时间
				WdatePicker({dateFmt:'HH:mm',maxDate: '#F{$dp.$D(\'dayTimeS'+colNum+'_'+nextRowNum+'\',{m:-1});}',minDate: '#F{$dp.$D(\'dayTimeS'+colNum+'_'+rowNum+'\',{m:+1});}'});
				
			}
		})
	}
</script>
<style type="text/css">
 span.tag {
    background: none repeat scroll 0 0 #CDE69C !important;
    border: 0 none !important;
    color: #638421 !important;
    margin-bottom: 2px !important;
    padding: 1px 1px !important;
    border: 1px solid #a5d24a;
    -moz-border-radius: 1px;
    -webkit-border-radius: 1px;
}
 span.tag a{
 	padding:0px 5px 
 }
 
 .attenceTimeDiv{
 	padding-top:7px
 }
 .attenceTimeDiv input{
	 width: 65px !important;
	 height: 30px !important;
 }
</style>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">考勤规则设定</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" id="saveRuleBtn">
								<i class="fa fa-save"></i>确定
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">考勤规则</span>
                                <div class="widget-buttons btn-div-full">
                                	<a class="ps-point btn-a-full addAttenceWeekBtn">
                                    	<i class="fa fa-plus blue"></i>
                                   </a>
                                 </div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<table class="display table table-bordered table-striped" id="singleTable">
										<tr class="singleTr">
											<td style="width: 30%">
												<div class="pull-left gray ps-left-text " style="font-size: 15px">
													<div class="pull-left">
												    	&nbsp;工作日:
													</div>
													<div class="pull-left padding-left-20">
												    	<select class="no-padding tempSelect" style="cursor:auto;width: 80px">
															<option value="0">请选择</option>
							 								<option value="1">星期一</option>
							 								<option value="2">星期二</option>
							 								<option value="3">星期三</option>
							 								<option value="4">星期四</option>
							 								<option value="5">星期五</option>
							 								<option value="6">星期六</option>
							 								<option value="7">星期日</option>
							 							</select>
													</div>
											    </div>
												<div class="ticket-user pull-left attenceWeekDiv">
												</div>
											</td>
											<td style="width: 50%">
												<div class="pull-left gray ps-left-text padding-top-10 clearfix" style="font-size: 15px">
											    	&nbsp;考勤时段:
											    </div>
												<div class="ticket-user pull-left ps-right-box padding-left-10 ">
													<div class="attenceTimeDiv">
														<span class="dayTimeSSpan">
															<span>签到</span>
															<input class="dpd2 no-padding dayTimeS padding-left-10" type="text" id="dayTimeS0_0" colNum="0" rowNum="0" readonly="readonly" >
														</span>
														<span class="dayTimeESpan">
															<span>签退</span>
															<input class="dpd2 no-padding dayTimeE padding-left-10" type="text" id="dayTimeE0_0" colNum="0" rowNum="0" readonly="readonly">
														</span>
														<span class="optSpan">
															<a href="javascript:void(0)" class="fa fa-times fa-lg padding-left-10 delBtn"></a>
															<a href="javascript:void(0)" class="fa fa-plus fa-lg padding-left-10 addBtn"></a>
														</span>
													</div>
												</div>
											</td>
											<td style="width: 10%" align="center">
												<a href="javascript:void(0)" class="delAttenceWeekBtn">删除</a>
											</td>
										</tr>
										
									</table>
                                </div>
                            </div>
                          </div>
                           
                           
                        </div>
					
					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
