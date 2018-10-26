<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<%@taglib prefix="t" uri="/WEB-INF/tld/t.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script>
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
var sid = '${param.sid}';
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	$(".container").Validform({
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
	
	//查看审批
	$("body").on("click",".spFlowIns",function(){
		var insId = $(this).attr("insId");
		if(insId){
			url="/workFlow/viewSpFlow?sid="+'${param.sid}'+"&instanceId="+insId;
			openWinWithPams(url, "800px", ($(window).height() - 90) + "px");
		}
	})
});

//选择完成借款页面
function addBanlance(type,busId,instanceId){
	if($("#content").val()){
		var msg = "";
		if(type == "030"){
			msg = "确定完成借款吗？"
		}else{
			msg = "确定完成结算吗？"
		}
		window.top.layer.confirm(msg,{icon:3,title:'询问框'}, function(index){
			 var payType = $("input[name='payType']:checked").val();
			 var usePhone = 1;
			 if(!$("input[name='usePhone']:checked").val()){
				 usePhone = 0;
			 }
			 var content = $("#content").val();
			 $.post("/balance/addBanlance?sid=${param.sid}&random="+Math.random(),{Action:"post",type:type,busId:busId,instanceId:instanceId,payType:payType,usePhone:usePhone,content:content},     
				   function (msgObjs){
					if(msgObjs.status == "y"){
						showNotification(1,msgObjs.msg);
						 window.top.layer.close(index);
						 parent.window.self.location.reload();
					}else{
						showNotification(2,msgObjs.msg);
					}
				},"json");
		}, function(){
		});	
	}else{
		scrollToLocation($("body"),$('#content'));
		layer.tips('请填写告知内容！', $('#content'), {tips: 1});
	}
}


</script>
</head>
<body>
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">完成结算通知</span>
                        
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div>
                     <!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->       
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">基础信息</span>
                                <div class="widget-buttons btn-div-full">
                                	
                                 </div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	&nbsp;申请单
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<a href="javascript:void(0)" class="spFlowIns" insId="${todayWorks.busId }" style="color: #2a6496" >${todayWorks.busTypeName }</a>
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both;border-bottom:0">
										    <div class="pull-left gray ps-left-text" style="height: 80px;">
										    	&nbsp;通知内容
										    </div>
											<div class="ticket-user pull-left ps-right-box"  style="height: auto;">
											    <textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:80px;width: 600px;" 
											    		  id="content" name="content" rows="" cols="" readonly>${todayWorks.content}</textarea>
											</div>
                                         </li>
                                          <li style="clear:both;">
                                       	  </li>
                                   	</ul>
                                </div>
                            </div>
                          </div>
                           
                           <div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">结算信息</span>
                                <div class="widget-buttons btn-div-full">
                                	
                                 </div>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" >
										    	<c:choose>
							                        <c:when test="${busType == '030' }">
							                        	&nbsp;借款金额
							                        </c:when>
							                        <c:otherwise>		                        	
														&nbsp;报销金额
							                        </c:otherwise>
						                        </c:choose>
										    </div>
											<div class="ticket-user pull-left ps-right-box"  >
												<c:choose>
							                        <c:when test="${busType == '030' }">
							                        	<fmt:formatNumber value="${loan.borrowingBalance}" pattern="#,###.##"/>&nbsp;元
							                        </c:when>
							                        <c:otherwise>		                        	
														<fmt:formatNumber value="${loan.loanOffItemFee}" pattern="#,###.##"/>&nbsp;元
							                        </c:otherwise>
						                        </c:choose>
											  	
											</div>
                                         </li>
                                         <c:if test="${busType == '031' }">
                                         	<li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
													&nbsp;销账金额 
										    </div>
											<div class="ticket-user pull-left ps-right-box"  >
													<fmt:formatNumber value="${loan.loanOffBalance}" pattern="#,###.##"/>&nbsp;元
											</div>
                                         </li>
                                         </c:if>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text" >
										    	&nbsp;支付方式
										    </div>
											<div class="ticket-user pull-left ps-right-box"  >
											   <c:choose>
											   	<c:when test="${loan.payType == 0 }">
											   		现金
											   	</c:when>
											   	<c:otherwise>
											   		银联转账
											   	</c:otherwise>
											   </c:choose>
											</div>               
                                           </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" >
										    	&nbsp;出款人
										    </div>
											<div class="ticket-user pull-left ps-right-box"  >
											  	${balanceUserName }
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" >
										    	&nbsp;出款时间
										    </div>
											<div class="ticket-user pull-left ps-right-box"  >
											  	${loan.balanceTime }
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
		
		<script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
