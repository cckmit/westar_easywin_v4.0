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
		if('${listConsumeType[0].id}'){
			showAttr('${listConsumeType[0].id}')
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
				"float":function(gets,obj,curform,regxp){
					if(isFloat($("#amount").val())){
						return true;
					}else{
						return "请填入正确的数字";
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
						  $("#consumePersonNum").val("");
					  }
					  if(data.showStartDate == 1){
						  $("#showsd").show();
					  }else{
						  $("#showsd").hide();
						  $("#startDate").val("");
					  }
					  if(data.showEndDate == 1){
						  $("#showed").show();
					  }else{
						  $("#showed").hide();
						  $("#endDate").val("");
					  }
					  if(data.showLeavePlace == 1){
						  $("#showlp").show();
					  }else{
						  $("#showlp").hide();
						  $("#leavePlace").val("");
					  }
					  if(data.showArrivePlace == 1){
						  $("#showap").show();
					  }else{
						  $("#showap").hide();
						  $("#arrivePlace").val("");
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
</script>
</head>
<body>
<form class="subform" method="post" action="/consume/addConsume">
<tags:token></tags:token>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">消费记录添加</span>
                        <div class="widget-buttons ps-toolsBtn">
                        	<a href="javascript:void(0)" class="blue" id="addConsume">
								<i class="fa fa-save"></i>添加
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                        	
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	<span style="color: red">*</span>费用类型：
										    </div>
											<div class="ticket-user pull-left ps-right-box pa" style="margin-left: 20px">
											   <select class="populate"  datatype="consumeType" id="type" name="type" style="cursor:auto;width: 200px" onchange="showAttr(this.value)">
													<optgroup label="选择费用类型"></optgroup>
													<c:choose>
														<c:when test="${not empty listConsumeType}">
							 								<c:forEach items="${listConsumeType}" var="consumeType" varStatus="status">
							 								<option value="${consumeType.id}">${consumeType.typeName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
											</div>               
                                           </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	<span style="color: red">*</span>消费金额：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left">
													<input id="amount" datatype="float" name="amount" nullmsg="请输入消费金额" 
													class="colorpicker-default form-control" type="text" style="width: 200px;float: left">&nbsp;元
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" id="showcn">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	消费人数：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left">
													<input id="consumePersonNum"  name="consumePersonNum" class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">&nbsp;人
												</div>
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow ps-listline" id="showrq">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	<span style="color: red">*</span>消费日期：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left" id="showsd">
													<input type="text" class="form-control" placeholder="消费日期" readonly="readonly" id="startDate" datatype="*" nullmsg="请选择消费日期" 
																	name="startDate" onClick="WdatePicker({maxDate: '#F{$dp.$D(\'endDate\',{d:-0})||\'%y-%M-{%d}\'}'})" style="width: 200px;float: left"/>
												</div>
												<div class="pull-left" id="showrqz">
													&nbsp;至&nbsp;
												</div>
												<div class="pull-left" id="showed">
													<input type="text" class="form-control" placeholder="消费日期" readonly="readonly" id="endDate"
																	name="endDate" onClick="WdatePicker({minDate: '#F{$dp.$D(\'startDate\',{d:+0});}',maxDate:'%y-%M-{%d}'})" style="width: 200px;float: left"/>
												</div>
											</div>
                                         </li>
                                            <li class="ticket-item no-shadow ps-listline" id="showdd">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	地点：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left" id="showlp">
													<input id="leavePlace"  name="leavePlace" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
												<div class="pull-left" id="showddz">
													&nbsp;至&nbsp;
												</div>
												<div class="pull-left" id="showap">
													<input id="arrivePlace"  name="arrivePlace" 	class="colorpicker-default form-control" type="text" value="" style="width: 200px;float: left">
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
									    		<span style="color: red">*</span>消费描述：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;margin-left: 20px;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;" id="describe" name="describe" rows="" cols="" datatype="describe"  nullmsg="请输入消费描述"></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         <!-- <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	消费归属：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
											  	<div class="pull-left">
													<input id="belong"  name="belong" 	class="colorpicker-default form-control" type="text" value="" style="width: 400px;float: left">
												</div>
											</div>
                                         </li> -->
                                         
                                         <li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
									    		发票：
									    	</div>
											<div class="pull-left ps-right-box" style="width: 400px;height: auto;margin-left: 20px;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="listUpfiles.upfileId" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                   	</ul>
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
