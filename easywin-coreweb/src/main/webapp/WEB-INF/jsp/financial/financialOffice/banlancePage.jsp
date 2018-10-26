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
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			datatype:{
				
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
	});
	//提交表单
	function formSub(){
		$(".subform").submit();
		
	}

	
	
</script>
</head>
<body>
<form class="subform" method="post">
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">
	                        <c:choose>
		                        <c:when test="${param.type == '030' }">
		                        	借款确认单
		                        </c:when>
		                        <c:otherwise>		                        	
									报销结算确认单 
		                        </c:otherwise>
	                        </c:choose>
                        </span>
                        <div class="widget-buttons ps-toolsBtn">
                        	<a href="javascript:void(0)" class="blue" id="">
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	<c:choose>
							                        <c:when test="${param.type == '030' }">
							                        	&nbsp;借款人：
							                        </c:when>
							                        <c:otherwise>		                        	
														&nbsp;报销人：
							                        </c:otherwise>
						                        </c:choose>
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
											   <c:choose>
							                        <c:when test="${param.type == '030' }">
							                        	${loan.creatorName}
							                        </c:when>
							                        <c:otherwise>		                        	
														${loanOff.creatorName}
							                        </c:otherwise>
						                        </c:choose>
											   
											</div>               
                                           </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	<c:choose>
							                        <c:when test="${param.type == '030' }">
							                        	&nbsp;借款金额：
							                        </c:when>
							                        <c:otherwise>		                        	
														&nbsp;报销金额： 
							                        </c:otherwise>
						                        </c:choose>
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
												<c:choose>
							                        <c:when test="${param.type == '030' }">
							                        	<fmt:formatNumber value="${loan.borrowingBalance}" pattern="#,###.##"/>&nbsp;元
							                        </c:when>
							                        <c:otherwise>		                        	
														<fmt:formatNumber value="${loanOff.loanOffItemFee}" pattern="#,###.##"/>&nbsp;元
							                        </c:otherwise>
						                        </c:choose>
											  	
											</div>
                                         </li>
                                         <c:if test="${param.type == '031' }">
                                         	<li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
													&nbsp;销账金额： 
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
													<fmt:formatNumber value="${loanOff.loanOffBalance}" pattern="#,###.##"/>&nbsp;元
											</div>
                                         </li>
                                         </c:if>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;支付方式：
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
											    <label class="padding-left-0">
												 	<input type="radio" class="colored-blue" name="payType" value="0" checked/>
												 	<span class="text" style="color:inherit;">现金</span>
											    </label>
											    <label class="padding-left-5">
												 	<input type="radio" class="colored-blue" name="payType"  value="1"/>
												 	<span class="text" style="color:inherit;">银联转账</span>
											    </label>
											</div>               
                                           </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;出款人：
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
											  	${userInfo.userName }
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;出款时间：
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="margin-left: 20px">
											  	${nowTime }
											</div>
                                         </li>
                                        <%--  <li class="ticket-item no-shadow ps-listline" style="clear:both;border-bottom:0">
										    <div class="pull-left gray ps-left-text" style="height: 80px;text-align: right;" >
										    	&nbsp;<span style="color: red">*</span>告知内容：
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="height: auto;margin-left: 20px">
											    <textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:80px;width: 355px;" 
											    		  id="content" name="content" rows="" cols="" datatype="*"  nullmsg="请填写告知内容！">${content}</textarea>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;告知方式：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="margin-left: 20px">
												<label class="padding-left-0">
												 	<input type="checkbox" class="colored-blue" name="" value="1" checked disabled/>
												 	<span class="text" style="color:grey;">待办事项</span>
											    </label>
											    <label class="padding-left-10">
												 	<input type="checkbox" class="colored-blue" name="usePhone" value="1" checked/>
												 	<span class="text" style="color:inherit;">手机短信</span>
											    </label>
											</div>               
                                           </li> --%>
                                   	</ul>
                                </div>
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
