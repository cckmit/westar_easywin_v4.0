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
	if('${todayWorks.busSpec}' != 1){
		showNotification(7,"该记录他人已审批，无需操作！");
	}
	
	//查看关联
	$("body").on("click",".relateClz",function(){
		var actObj = $(this);
		var busId = $(actObj).attr("busId");
		var busType = $(actObj).attr("busType");
		
		var param = {
				busId:busId,
				busType:busType,
				clockId:-1
		}
		authBaseCheck(param,function(authState){
			var url = "";
			if(busType=='005'){
				url = "/item/viewItemPage?sid="+sid+"&id="+busId;
			}else if(busType=='012'){
				url = "/crm/viewCustomer?sid="+sid+"&id="+busId;
			}else if(busType=='003'){
				url = "/task/viewTask?sid="+sid+"&id="+busId;
			}else if(busType=='022'){
				url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+busId;
			}
			openWinWithPams(url,"800px",($(window).height()-90)+"px");
		})
	});
	
	$("body").on("click","#agree",function(){//同意
		$(".subform [name='status']").val(1);
    	var spOpinion = $("#spOpinion").val();
    	if(strIsNull(spOpinion)){
    		layer.tips('请填写审核意见！', "#spOpinion", {tips: 1});
    		return false;
    	}
    	var count = spOpinion.replace(/[^\x00-\xff]/g,"**").length;
		if(count>250){
			layer.tips('审核意见太长！', "#spOpinion", {tips: 1});
    		return false;
		}
		$(".subform").submit();
	});
	$("body").on("click","#refuse",function(){//不同意
		$(".subform [name='status']").val(0);
    	var spOpinion = $("#spOpinion").val();
    	if(strIsNull(spOpinion)){
    		layer.tips('请填写审核意见！', "#spOpinion", {tips: 1});
    		return false;
    	}
    	var count = spOpinion.replace(/[^\x00-\xff]/g,"**").length;
		if(count>250){
			layer.tips('审核意见太长！', "#spOpinion", {tips: 1});
    		return false;
		}
		$(".subform").submit();
	});
	
	
	
});



</script>
</head>
<body>
<form class="subform" method="post" action="/moduleChangeExamine/updateApply">
<input type="hidden" name="id" value="${todayWorks.busId}"/>
<input type="hidden" name="sid" value="${param.sid}"/>
<input type="hidden" name="status" value=""/>
<input type="hidden" name="busId" value="${moduleChangeApply.busId}"/>
<input type="hidden" name="field" value="${moduleChangeApply.field}"/>
<input type="hidden" name="oldValue" value="${moduleChangeApply.oldValue}"/>
<input type="hidden" name="newValue" value="${moduleChangeApply.newValue}"/>
<input type="hidden" name="moduleType" value="${moduleChangeApply.moduleType}"/>
<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget no-margin-bottom" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">属性变更审批</span>
                        <div class="widget-buttons ps-toolsBtn">
							<c:if test="${todayWorks.busSpec == 1}">
								<a href="javascript:void(0)" class="green" id="agree">
									<i class="fa fa-check-square-o"></i>同意
								</a>
								<a href="javascript:void(0)" class="red" id="refuse">
									<i class="fa fa-times-circle-o"></i>不同意
								</a>
							</c:if>
							&nbsp;
						</div>
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
                        	
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	申请人员
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<span>${moduleChangeApply.creatorName}</span>
											</div>               
                                        </li>
                                         <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	申请时间
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												${moduleChangeApply.recordCreateTime}
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both">
										    <div class="pull-left gray ps-left-text">
										    	申请内容
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												${todayWorks.content}
											</div>               
                                        </li>
                                        <li class="ticket-item no-shadow ps-listline" style="clear:both;overflow: auto;">
										    <div class="pull-left gray ps-left-text" >
										    	变更说明
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="word-break: break-all;white-space: normal;width: 600px;height: auto;">
											    ${moduleChangeApply.content}
											</div>
                                          </li>
                                          <li class="ticket-item no-shadow ps-listline" style="clear:both">
											    <div class="pull-left gray ps-left-text">
												    <c:choose>
													    <c:when test="${moduleChangeApply.moduleType == 012 }">
													    	变更客户
													    </c:when>
												    </c:choose>
											    	
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<a href="javascript:void(0);" class="pull-left relateClz" bustype="${moduleChangeApply.moduleType }" busid="${moduleChangeApply.busId }" style="font-size:10px;">${moduleChangeApply.busName }</a>
												</div>               
	                                        </li>
                                        	<li class="ticket-item no-shadow ps-listline" style="clear:both;border-bottom:0;height: 160px">
			                                    <div class="pull-left gray ps-left-text" style="height: 160px;">
			                                                                                                   审核意见<span style="color: red">*</span>
			                                    </div>
			                                    <div class="ticket-user pull-left ps-right-box" style="height: auto;">
			                                        <textarea class="colorpicker-default form-control margin-top-10 " id="spOpinion" name="spOpinion" rows="" cols="" style="width:600px;height: 120px;"></textarea>
			                                        <a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('spOpinion','${param.sid}');" title="常用意见"></a>
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
		
		<script src="/static/assets/js/bootstrap.min.js"></script>
    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
