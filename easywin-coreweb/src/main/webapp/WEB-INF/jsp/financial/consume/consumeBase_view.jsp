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
		
		//查看审批
		$("body").on("click",".spFlowIns",function(){
			var insId = $(this).attr("insId");
			if(insId){
				viewSpFlow(insId);
			}
		})
	});
	
	//流程查看页面
	function viewSpFlow(instanceId){
		var url = "/workFlow/viewSpFlow?sid="+'${param.sid}'+"&instanceId="+instanceId;
		openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
	}
	
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
</script>
</head>
<body>
<form class="subform" method="post" action="/consume/addConsume">
<tags:token></tags:token>
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
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
											   ${consume.typeName}
											</div>               
                                           </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;消费金额：
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
											  	<fmt:formatNumber value="${consume.amount }" pattern="#,###.##"/>&nbsp;元
											</div>
                                         </li>
                                          <c:if test="${not empty consume.consumePersonNum}">
	                                         <li class="ticket-item no-shadow ps-listline" id="showcn">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;消费人数：
											    </div>
												<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
												  	<div class="pull-left">
														${consume.consumePersonNum}<c:if test="${not empty consume.consumePersonNum}">&nbsp;人</c:if>
													</div>
												</div>
	                                         </li>
                                         </c:if>
                                         <li class="ticket-item no-shadow ps-listline" id="showrq">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;消费日期：
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
											  	<div class="pull-left" id="showsd">
													${consume.startDate}
												</div>
												<c:if test="${not empty consume.startDate && not empty consume.endDate}">
													<div class="pull-left" id="showrqz">
														&nbsp;至&nbsp;
													</div>
												</c:if>
												<div class="pull-left" id="showed">
													${consume.endDate}
												</div>
											</div>
                                         </li>
                                          <c:if test="${not empty consume.leavePlace || not empty consume.arrivePlace}">
                                            <li class="ticket-item no-shadow ps-listline" id="showdd">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;地点：
											    </div>
												<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
												  	<div class="pull-left" id="showlp">
														${consume.leavePlace}
													</div>
													<c:if test="${not empty consume.leavePlace && not empty consume.arrivePlace}">
														<div class="pull-left" id="showddz">
															&nbsp;至&nbsp;
														</div>
													</c:if>
													<div class="pull-left" id="showap">
														${consume.arrivePlace}
													</div>
												</div>
	                                         </li>
                                         </c:if>
                                         <li class="icket-item no-shadow ps-listline" >
									    	<div class="pull-left gray ps-left-text" style="text-align: right;">
									    		&nbsp;消费描述：
									    	</div>
									    	<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												<div class="pull-left">
														${consume.describe}
												</div>
											</div>              
                                         </li>
                                        <c:if test="${not empty consume.belong}">
                                        	<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;消费归属：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												  	<div class="pull-left">
														${consume.belong}
													</div>
												</div>
                                         	</li>
                                        </c:if>
                                        
                                        <c:if test="${not empty consume.spFlowName}">
                                        	<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;关联报销记录：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												  	<div class="pull-left">
														<a href="javascript:void(0)" class="spFlowIns" insId="${consume.instanceId }" style="color: #2a6496" title="${consume.spFlowName }">${consume.spFlowName }</a>
													</div>
												</div>
                                         	</li>
                                        </c:if>
                                        <c:if test="${not empty consume.balanceUserName}">
                                        	<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;结算人：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												  	<div class="pull-left">
														${consume.balanceUserName}
													</div>
												</div>
                                         	</li>
                                        </c:if>
                                        <c:if test="${not empty consume.payType}">
                                        	<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;结算支付方式：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												  	<div class="pull-left">
												  		<c:choose>
												  			<c:when test="${consume.payType == 0}">
												  				现金
												  			</c:when>
												  			<c:otherwise>
												  				银联转账
												  			</c:otherwise>
												  		</c:choose>
														
													</div>
												</div>
                                         	</li>
                                        </c:if>
                                        <c:if test="${not empty consume.balanceTime}">
                                        	<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	&nbsp;结算时间：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												  	<div class="pull-left">
														${fn:substring(consume.balanceTime,0,16) }
													</div>
												</div>
                                         	</li>
                                        </c:if>
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
	</form>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
