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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">

<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	var sid="${param.sid}";
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	$(function(){
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		//报销进度显示
		if("${loanOff.stepth}"=="1"){
			var step1 = $(".step1");
			$(step1).removeClass("step1");
			$(step1).addClass("actStep1");
			$(step1).css("color","#fff");
			$(".firstStep").removeClass("gray");
		}else if("${loanOff.stepth}"=="2"){
			var step1 = $(".step1");
			$(step1).removeClass("step1");
			$(step1).addClass("actStep1");
			$(step1).css("color","#fff");
			var step2 = $(".step2");
			$(step2).removeClass("step2");
			$(step2).addClass("actStep2");
			$(step2).css("color","#fff");
			$(".firstStep").removeClass("gray");
			$(".secondStep").removeClass("gray");
		}
		//审批事项查看
		$("body").on("click",".flowView",function(){
			var actObj = $(this);
			var instanceId = $(actObj).attr("instanceId");
			var url = "/workFlow/viewSpFlow?sid=${param.sid}&instanceId="+instanceId;
		    //this.href= url; 
	        //this.target = "_blank"; 
			openWinWithPams(url,"850px",($(window.parent).height()-90)+"px");
		});
	});
</script>
<style>
	.pop-content dl{
		line-height: 30px
	}
	.stepLi{
		width: 290px;
		height: 52px;
		text-align: center;
		line-height: 52px;
		color: #000;
	}
	.step1{
		background: url(/static/images/web/regist-step-2-0.png) no-repeat;
	}
	.actStep1{
		background: url(/static/images/web/regist-step-2-1.png) no-repeat;
	}
	.step2{
		background: url(/static/images/web/regist-step-3-0.png) no-repeat;
	}
	.actStep2{
		background: url(/static/images/web/regist-step-3-1.png) no-repeat;
	}
</style>
</head>
<body >
<form class="subform" method="post">
	<input name="stepth" value="${loanOff.stepth}">
	<input name="loanRepStatus" value="${loanOff.loanRepStatus}">
	<input name="loanOffStatus" value="${loanOff.status}">
	<input name="loanReportId" value="${loanOff.loanReportId}">
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget" style="margin-top: 0px;">
					<div
						class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">费用报销</span>
						<div class="widget-buttons ps-toolsBtn">&nbsp;</div>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i> 
							</a>
						</div>
					</div>
					<!--Widget Header-->
					<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
					<div class="widget margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
			            <div class="widget-body">
			            	<div class="widget no-header ">
			                    <div class="widget-body bordered-radius">
			                        <div class="padding-bottom-10">
							    		<ul class="flow-menu">
							    			<li class="stepLi step1 pull-left">1、报销说明 </li>
							    			<li class="stepLi step2 pull-left">2、费用报销</li>
							    		</ul>
							    	</div>
							    	<div style="margin-top:60px;" >
							    		<p style="color:red;font-weight:bold;font-size:16px;">按公司规定，费用报销分两步骤：</p>
								    	<p class="margin-top-5 firstStep gray" style="line-height:35px;font-weight:bold;">第一步（报销说明）：
								    	<span>
								    		<c:if test="${not empty loanOff.loanRepStatus and loanOff.loanRepStatus ne -1}">
								    			<a href="javascript:void(0);" class="flowView" instanceId="${loanOff.loanRepFlowId}" title="点击查看">
								    				${loanOff.loanReportName}（${loanOff.loanRepStatus eq 4 ?'完结':loanOff.loanRepStatus eq 1?'审批中':'驳回'}）
								    			</a>
								    		</c:if>
								    	</span>
								    	</p>
								    	<p class="margin-top-5 secondStep gray" style="line-height:35px;font-weight:bold;">第二步（费用报销）：
								    	<span>
								    		<a href="javascript:void(0);" class="flowView" instanceId="${loanOff.instanceId}" title="点击查看">
								    			<c:if test="${not empty loanOff.status and loanOff.status ne -1}">
									    			${loanOff.loanOffName}（${ not empty loanOff.status?(loanOff.status eq 4 ?'完结':loanOff.status eq 1?'审批中':'驳回'):''}）
								    			</c:if>
							    			</a>
								    	</span>
								    	</p>
							    	</div>
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
