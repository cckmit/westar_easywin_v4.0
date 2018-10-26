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
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	
	
	$(function() {
		//页面属性显示隐藏
		if('${consume.type}'){
			showAttr('${consume.type}')
		}
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				"consumeType":function(gets,obj,curform,regxp){
					if(!strIsNull($("#type").val())){
						return true;
					}else{
						return false;
					}
				},
				"describe":function(gets,obj,curform,regxp){
					if($("#describe").val()){
						return true;
					}else{
						return "请填入消费描述";
					}
				}
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		$("#addConsume").click(function(){
			formSub();
		});
		
		$("#amount").change(function(){
			if(!strIsNull($("#amount").val()) && isFloat($("#amount").val())){
				$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',amount:$("#amount").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		$("#consumePersonNum").change(function(){
			if(!strIsNull($("#consumePersonNum").val()) && isFloat($("#consumePersonNum").val())){
				$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',consumePersonNum:$("#consumePersonNum").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		$("#leavePlace").change(function(){
			if(!strIsNull($("#leavePlace").val())){
				$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',leavePlace:$("#leavePlace").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		$("#arrivePlace").change(function(){
			if(!strIsNull($("#arrivePlace").val())){
				$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',arrivePlace:$("#arrivePlace").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		$("#describe").change(function(){
			if(!strIsNull($("#describe").val())){
				$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',describe:$("#describe").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		$("#belong").change(function(){
			if(!strIsNull($("#belong").val())){
				$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',belong:$("#belong").val()},     
	 				function (msgObjs){
					showNotification(1,msgObjs);
				});
			}
		});
		
	});
	//提交表单
	function formSub(){
		$(".subform").submit();
	}
	
	//根据类型显示属性
	function showAttr(id) {
		$.ajax({
			type : "post",
			url : "/consume/getConsumeTypeById?sid="+'${param.sid}'+"&id="+id,
			dataType:"json",
			traditional :true, 
			data:null,
			  beforeSend: function(XMLHttpRequest){
	         },
			  success : function(data){
				  if(data){
					  if(data.showConsumePersonNum == 1){
						  $("#showcn").show();
					  }else{
						  $("#showcn").hide();
					  }
					  if(data.showStartDate == 1){
						  $("#showsd").show();
					  }else{
						  $("#showsd").hide();
					  }
					  if(data.showEndDate == 1){
						  $("#showed").show();
					  }else{
						  $("#showed").hide();
					  }
					  if(data.showLeavePlace == 1){
						  $("#showlp").show();
					  }else{
						  $("#showlp").hide();
					  }
					  if(data.showArrivePlace == 1){
						  $("#showap").show();
					  }else{
						  $("#showap").hide();
					  }
					  if(data.showEndDate == 1 && data.showStartDate == 1){
						  $("#showrqz").show();
					  }else{
						  $("#showrqz").hide();
					  }
					  if(data.showEndDate != 1 && data.showStartDate != 1){
						  $("#showrq").hide();
					  }else{
						  $("#showrq").show();
					  }
					  if(data.showArrivePlace == 1 && data.showLeavePlace == 1){
						  $("#showddz").show();
					  }else{
						  $("#showddz").hide();
					  }
					  if(data.showArrivePlace != 1 && data.showLeavePlace != 1){
						  $("#showdd").hide();
					  }else{
						  $("#showdd").show();
					  }
				  }
			  },
			  error:  function(XMLHttpRequest, textStatus, errorThrown){
				  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
		      }
		}); 
	}
	
	//选择开始日期
	function selectDate(val){
		var startDate = $("#startDate").attr("preval");
		if(startDate!=val){
			$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',startDate:val,attrName:"startDate"},     
				function (msgObjs){
				showNotification(1,msgObjs);
				$("#startDate").attr("preval",val)
			});
		}
	}
	
	//选择结束日期
	function selectDateEnd(val){
		var endDate = $("#endDate").attr("preval");
		if(endDate!=val){
			$.post("/consume/updateConsumes?sid=${param.sid}",{Action:"post",id:'${consume.id}',endDate:val,attrName:"endDate"},     
				function (msgObjs){
				showNotification(1,msgObjs);
				$("#endDate").attr("preval",val)
			});
		}
	}
	
	function saveUpfiles() {
		var files = $("#listUpfiles_upfileId").val();
		
		if(files && files[0]){
			var upfileIds = files.join(",");
			$.post("/consume/addFiles?sid=${param.sid}",{Action:"post",id:'${consume.id}',upfileIds:upfileIds},     
					function (msgObjs){
					showNotification(1,msgObjs);
					 location.reload();
				});
		}else{
			scrollToLocation($("#contentBody"),$('#fileListDiv'));
			layer.tips('请选择文件！', $('#fileListDiv'), {tips: 1});
		}
		
	}
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                        	
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;费用类型：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											   <select class="populate"  datatype="consumeType" id="type" name="type" style="cursor:auto;width: 200px" onchange="showAttr(this.value)" disabled="true">
													<optgroup label="选择费用类型"></optgroup>
													<c:choose>
														<c:when test="${not empty listConsumeType}">
							 								<c:forEach items="${listConsumeType}" var="consumeType" varStatus="status">
							 								<option value="${consumeType.id}" ${(consume.type==consumeType.id)?"selected='selected'":''}>${consumeType.typeName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
											</div>               
                                           </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;消费金额：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left">
													<input id="amount" datatype="*" name="amount" nullmsg="请输入消费金额" 
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left" value="${consume.amount}">&nbsp;元
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" id="showcn">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;消费人数：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left">
													<input id="consumePersonNum"  name="consumePersonNum" 	class="colorpicker-default form-control" type="text" style="width: 200px;float: left" value="${consume.consumePersonNum}">&nbsp;人
												</div>
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow ps-listline" id="showrq">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;消费日期：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left" id="showsd">
													<input type="text" class="form-control" placeholder="消费日期" readonly="readonly" id="startDate"
																	name="startDate" 
																	onFocus="WdatePicker({onpicked:function(dp){selectDate(dp.cal.getNewDateStr())},dateFmt:'yyyy-MM-dd',maxDate: '#F{$dp.$D(\'endDate\',{d:-0}) || \'%y-%M-{%d}\'}'})"
																	style="width: 200px;float: left" value="${consume.startDate}" preval="${consume.startDate}"/>
												</div>
												<div class="pull-left" id="showrqz">
													&nbsp;至&nbsp;
												</div>
												<div class="pull-left" id="showed">
													<input type="text" class="form-control" placeholder="消费日期" readonly="readonly" id="endDate"
																	name="endDate"
																	onFocus="WdatePicker({onpicked:function(dp){selectDateEnd(dp.cal.getNewDateStr())},dateFmt:'yyyy-MM-dd',minDate: '#F{$dp.$D(\'startDate\',{d:+0});}',maxDate:'%y-%M-{%d}'})"
																	style="width: 200px;float: left" value="${consume.endDate}" preval="${consume.endDate}"/>
												</div>
											</div>
                                         </li>
                                            <li class="ticket-item no-shadow ps-listline" id="showdd">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;地点：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left" id="showlp">
													<input id="leavePlace"  name="leavePlace" 	class="colorpicker-default form-control" type="text"  style="width: 200px;float: left" value="${consume.leavePlace}">
												</div>
												<div class="pull-left" id="showddz">
													&nbsp;至&nbsp;
												</div>
												<div class="pull-left" id="showap">
													<input id="arrivePlace"  name="arrivePlace" 	class="colorpicker-default form-control" type="text"  style="width: 200px;float: left" value="${consume.arrivePlace}">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding" style="border-bottom: 1px solid #EDEDED;">
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
									    		&nbsp;消费描述：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;margin-left: 20px;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="describe" name="describe" rows="" cols="" datatype="describe"  nullmsg="请输入消费描述">${consume.describe}</textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         <%-- <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;消费归属：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left">
													<input id="belong"  name="belong" 	class="colorpicker-default form-control" type="text"  style="width: 400px;float: left" value="${consume.belong}">
												</div>
											</div>
                                         </li> --%>
                                         
                                         <li class="ticket-item no-shadow autoHeight no-padding"  style="border-bottom: 1px solid #EDEDED;">
										    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
										    		&nbsp;发票：
										    	</div>
												<div class="pull-left ps-right-box" style="width: 400px;height: auto;margin-left: 20px;">
													<div class="margin-top-10" id="fileListDiv">
												  		<tags:uploadMore name="listUpfiles.upfileId" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
													</div>
													<div class="margin-top-10">
												  		<button type="button" onclick="saveUpfiles()"
														class="btn btn-info ws-btnBlue pull-right" style="float: left !important;margin-left: 100px;margin-top: -45px;line-height: 1">发票保存</button>
													</div>
													
												</div> 
												<div class="ps-clear"></div> 
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;状态：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												  	<div class="pull-left">
														<c:choose>
															<c:when test="${consume.status == 0 }">
																<span style="color: red;">待报销</span>
															</c:when>
															<c:when test="${consume.status == 1 }">
																<span style="color: blue;">报销中</span>
															</c:when>
															<c:when test="${consume.status == 2 }">
																<span style="color: green;">已报销</span>
															</c:when>
														</c:choose>
													</div>
												</div>
                                          </li>
                                         
                                   	</ul>
                                </div>
                                
                             
                              
                           </div>
                           
                            
                           <div class="widget-body no-shadow">
                            </div> 
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
