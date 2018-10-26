<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME%></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" src="/static/js/clockJs/clock.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
		
	//添加闹铃	
	function formSub(){
		var validFormState = validClockForm();
		if(!validFormState){
			return false;
		}
		if($("#subState").val()==1){
			return false;
		}
		var flag = false;
		//提醒周期
		var clockRepType = $("#clockRepType").val();
		//提醒的周期时间
		var clockRepDate=''; 
		if(clockRepType=='2'){//每周
			var obj = $(":checkbox[name='weekRepDate']:checked");
			for(var i=0; i<obj.length; i++){ 
				clockRepDate+=obj[i].value+','; //如果选中，将value添加到变量s中 
			} 
		}else if(clockRepType=='3'){//每月
			clockRepDate = $("#monthRepDate").val();
		}
		$("#clockRepDate").val(clockRepDate);
		
		//发送方式
		var  obj = $(":checkbox[name='listClockWay.sendWay']:checked");
		var sendWay=new Array();;
		for(var i=0; i<obj.length; i++){ 
			sendWay.push(obj[i].value); //如果选中，将value添加到变量s中 
		} 
		var clockPersons = $("#clockPersons").find("option");
		var users=new Array();
		for(var i=0; i<clockPersons.length; i++){ 
			users.push($(clockPersons[i]).val()); //如果选中，将value添加到变量s中 
		}
		var result;
		 //异步提交表单
	    $("#clockForm").ajaxSubmit({
	        type:"post",
	        url:"/clock/ajaxAddClock?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        async:false,
	        beforeSubmit:function (a,f,o){
	        	$("#subState").val(1)
			}, 
			traditional :true,
		    data:{"sendWays":sendWay,"users":users},
	        success:function(data){
		         var state = data.state;
		         if(state=='y'){
			         var clockTime = $("#clockTime").val();
			         var clockDate = $("#clockDate").val();
			         
		        	 result={"clockTime":clockTime,
		        			 "clockDate":clockDate,
		        			 "id":data.id,
		        			 "clockRepType":clockRepType,
		        			 "clockRepDate":clockRepDate};
			     }
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
	        	return false;
	        	
	        }
	    });
	    $("#subState").val(0)
	    return result;
	}
</script>
</head>
<body>
<input type="hidden" id="subState" value="0">
<form class="subform" id="clockForm">
	<input type="hidden" name="busId" value="${clock.busId}"/>
	<input type="hidden" name="busType" value="${clock.busType}"/>
	<input type="hidden" name="sid" value="${param.sid}"/>
	<input type="hidden" name="clockRepDate" id="clockRepDate"/>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px">添加提醒</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" style="height: 360px;overflow-y:auto;position: relative;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">闹铃信息</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white" >
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												开始日期：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control ps-pointer" type="text" id="clockDate" readonly="readonly" style="width: 300px;float: left" 
												name="clockDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" nullmsg="请选择日期" value="${nowDate}"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												提醒时间：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control" type="text" readonly="readonly" id="clockTime" style="width: 300px;float: left"
												 name="clockTime" onFocus="WdatePicker({dateFmt:'HH:mm'})" nullmsg="请选择时间" value="${nowTime }"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												提醒周期：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<tags:dataDic type="clockType" id="clockRepType" name="clockRepType" onchange="getClockRepDate()" style="width: 300px;float: left"></tags:dataDic>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline" id="clockRepDateDiv" style="display: none">
										    <div class="pull-left gray ps-left-text">
												周期时间：
										    </div>
											<div class="ticket-user pull-left ps-right-box" id="weekDate" style="display: none">
												<label class="no-padding">
												 	<input type="checkbox"  name="weekRepDate" value="1" ${dayWeek==1?'checked':'' }/>
												 	<span class="text">日</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="checkbox"  name="weekRepDate" value="2" ${dayWeek==2?'checked':'' }/>
												 	<span class="text">一</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="checkbox"  name="weekRepDate" value="3" ${dayWeek==3?'checked':'' }/>
												 	<span class="text">二</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="checkbox"  name="weekRepDate" value="4" ${dayWeek==4?'checked':'' }/>
												 	<span class="text">三</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="checkbox"  name="weekRepDate" value="5" ${dayWeek==5?'checked':'' }/>
												 	<span class="text">四</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="checkbox"  name="weekRepDate" value="6" ${dayWeek==6?'checked':'' }/>
												 	<span class="text">五</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="checkbox"  name="weekRepDate" value="7" ${dayWeek==7?'checked':'' }/>
												 	<span class="text">六</span>
											    </label>
											</div>
											<div class="ticket-user pull-left ps-right-box" id="monthDate" style="display: none">
												<select name="monthRepDate" id="monthRepDate" style="width: 100px;float: left;">
													<c:forEach begin="1" end="31" step="1" var="monthDay">
														<option value="${monthDay}" ${dayMonth==monthDay?'selected':'' }>${monthDay }</option>
													</c:forEach>
												</select>
												<div style="margin-top: 5px;float: left">
													日
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		定时说明
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 300px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10"
										  		placeholder="定时说明……"  style="height:80px;width: 300px" id="content" name="content" rows="" cols=""></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
										<li class="ticket-item no-shadow autoHeight no-padding" style="border: 0" >
									    	<div class="pull-left gray ps-left-text padding-top-10">
									    		提醒人员
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 300px;height: auto;">
												<div class="margin-top-10 margin-bottom-10 pull-left">
													<select 
														id="clockPersons"
														ondblclick="removeClick(this.id)"
														multiple="multiple" moreselect="true"
														style="width: 230px; height: 100px;">
														<option value="${userInfo.id}">${userInfo.userName}</option>
													</select>
												</div>
												<a href="javascript:void(0)" onclick="userMore('clockPersons','','${param.sid}','','');" 
												class="btn btn-blue btn-xs margin-top-50 margin-left-10" title="人员选择"><i class="fa fa-plus"></i>添加</a>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
										<hr style="margin-bottom: 0px;" class="ps-border-bottom"/>
                                         <%-- <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												提醒方式：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											<c:forEach items="${listClockWay}" var="way" varStatus="vs">
												<c:if test="${way.parentId>0}">
													<label class="padding-left-5">
													 	<input type="checkbox" class="colored-blue" id="listClockWay_sendWay" name="listClockWay.sendWay" value="${way.code }"${way.code==0?'checked="checked"':'' }"/>
													 	<span class="text" style="color:inherit;">${way.zvalue}</span>
												    </label>
												</c:if>
											</c:forEach>	
											</div>
										</li> --%>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												消息类型：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<label class="padding-left-5">
												 	<input type="radio" class="colored-blue" name="clockMsgType" value="0" checked="checked"></input>
												 	<span class="text" style="color:inherit;">发送普通消息</span>
											    </label>
												<label class="padding-left-5">
												 	<input type="radio" class="colored-blue" name="clockMsgType" value="1"/>
												 	<span class="text" style="color:inherit;">发送待办提醒</span>
											    </label>
											</div>
										</li>
									</ul>
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
