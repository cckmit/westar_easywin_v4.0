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
<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
var sid="${param.sid}";
var pageParam= {
    "sid":"${param.sid}"
}

	function formSub(){
		var flag = false;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/moduleChangeExamine/addMoreApply?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        		if($("#field").val() == "owner"){
		        		var toUser = $("#newValue").val()
			    		if(!toUser){
			    			scrollToLocation($("#contentBody"),$("#newValueDiv"));
			    			layer.tips("请选择客户接收人员",$("#newValueDiv"),{tips:1});
			    			return false;
			    		}else if($("#oldValue").val()==toUser){
			    			scrollToLocation($("#contentBody"),$("#handoverUserDiv"));
			    			layer.tips("不能移交给自己",$("#handoverUserDiv"),{tips:1});
			    			return false;
			    		}else if(!'${listExamUser[0]}'){
			    			scrollToLocation($("#contentBody"),$("#examUserDiv"));
			    			layer.tips("暂无相关审批人，请联系管理员",$("#examUserDiv"),{tips:1});
			    			return false;
			    		}else if(!$("#content").val()){
			    			scrollToLocation($("#contentBody"),$("#content"));
			    			layer.tips("请填写移交说明",$("#content"),{tips:1});
			    			return false;
			    		}
			    		var feedBackTypeId = $("#feedBackTypeId").val();
			    		if(!strIsNull($("#content").val())){
			    			if(feedBackTypeId==0){
			    				scrollToLocation($("#contentBody"),$("#feedBackTypeId"));
			    				layer.tips('请选择反馈类型！', "#feedBackTypeId", {tips: 1});
			    				return false;
			    			}
			    		}
		        	}
		        	
				}, 
		        success:function(data){
			        if('y'==data.status){
			        	flag = true;
			        }else{
		        		layer.msg(data.info);
			        }
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"系统错误，请联系管理人员");
		        }
		 });
		 return flag;
	}
$(function(){
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	
	//列出常用人员信息
    listUsedUser(3,function(data){
        if(data.status=='y'){
            var usedUser = data.usedUser;
            $.each(usedUser,function(index,userObj){
                //添加头像
                var headImgDiv = $('<div class="ticket-user pull-left other-user-box usedUser"></div>');
                $(headImgDiv).data("userObj",userObj);
                var imgObj = $('<img src="/downLoad/userImg/${userInfo.comId}/'+userObj.id+'" class="margin-left-5 usedUserImg"/>');
                $(imgObj).attr("title",userObj.userName)
                var headImgName = $('<span class="user-name2" style="font-size:6px;display:inline-block"></span>');
                $(headImgName).html(userObj.userName);

                $(headImgDiv).append($(imgObj));
                $(headImgDiv).append($(headImgName));

                $("#usedUserDiv1").append($(headImgDiv));
                $(headImgDiv).on("click",function(){
                	var usedUser = $(this).data("userObj");
                	setUser('newValue','newName',usedUser.id,$(this));
                })
            })
        }
    });
})

//删除不需要移交的客户	
function del_li(obj){
	if($("[name='busIds']").length>1){
		$(obj).parent().remove();
	}else{
		showNotification(1,"总的移交一个吧！");
	}
}
</script>
</head>
<body>

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">批量变更申请</span>
    <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body margin-top-40" style="overflow:hidden;overflow-y:auto;">

    	<div class="widget radius-bordered margin-bottom-0">
             <div class="widget-body no-shadow">
              	<div class="tickets-container bg-white">
              		<form class="subform" method="post" action="/moduleChangeExamine/addMoreApply">
						<input type="hidden" name="redirectPage" value="${param.redirectPage}"/>
						<input type="hidden" name="moduleType" value="${moduleChangeApply.moduleType}"/>
						<input type="hidden" id="field" name="field" value="${moduleChangeApply.field}"/>
						<ul class="tickets-list">
	                        <c:choose>
	                        	<c:when test="${moduleChangeApply.moduleType == '012'}">
	                        		<c:choose>
	                        			<c:when test="${moduleChangeApply.field == 'owner'}">
		                        			<li class="ticket-item no-shadow ps-listline" style="border: 0">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
													待移交客户：
											    </div>
												<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												
													<c:choose>
													 	<c:when test="${not empty listCustomer}">
													 		<c:forEach items="${listCustomer}" var="crmVo" varStatus="status">
													 		 <div class="alert-success margin-bottom-5" style="width: 350px">
																 <input type="hidden" name="busIds" value="${crmVo.id}"/>
					                                            <button class="close margin-right-5" type="button" style="margin-top: 5px"  onclick="del_li(this);">
					                                                ×
					                                            </button>
					                                            <span class="margin-left-10">
					                                            	${crmVo.customerName}&nbsp;
					                                            </span>
					                                       	 </div>
													 		</c:forEach>
													 	</c:when>
													</c:choose>
												</div>
												<div style="clear:both"></div>
				
											</li>
											<li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		<font color="red">*</font> 接收人：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px" id="newValueDiv">
													<tags:userOne name="newValue" showValue="" value=""
													 showName="newName" nullmsg="请选择接收人"></tags:userOne>
													  <div id="usedUserDiv1" style="width: 280px;display: inline-block;">
														<span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
													</div>
												</div> 
					                        </li>
					                        <li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		<font color="red">*</font> 审批人：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px" id="examUserDiv">
													<c:if test="${not empty listExamUser }">
														<c:forEach items="${listExamUser}" var="examUser" varStatus="status">
															<img id="userOneImgexamUser" style="display: block; float: left;" class="user-avatar" src="/downLoad/userImg/${examUser.comId }/${examUser.userId }" title="${examUser.adminName }">
															<span class="user-name" style="font-size:10px;">${examUser.adminName }</span>
														</c:forEach>
													</c:if>
												</div> 
					                        </li>
					                        <li class="ticket-item no-shadow autoHeight no-padding">
										    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
										    		<font color="red">*</font>移交说明：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:70px;width:350px;" id="content" 
											  		name="content" rows="" cols="" placeholder="移交说明……"></textarea>
												</div> 
												<div class="ps-clear"></div>              
				                            </li>
					                        <li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		<font color="red">*</font>反馈类型：
										    	</div>
												<div class="ticket-user pull-left ps-right-box">
													<select class="populate" id="feedBackTypeId" name="feedBackTypeId" style="width:350px;">
														<option value="0">反馈类型</option>
														<c:choose>
															<c:when test="${not empty listFeedBackType}">
																<c:forEach items="${listFeedBackType}" var="feedBackType" varStatus="status">
																<option value="${feedBackType.id}">${feedBackType.typeName}</option>
																</c:forEach>
															</c:when>
														</c:choose>
													</select>
												</div>               
					                        </li> 
					                        <li class="ticket-item no-shadow no-padding">
										    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
										    		<font color="red">*</font>通知方式：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="padding-top: 10px !important;">
												    <label class="padding-left-0">
													 	<input type="checkbox" class="colored-blue" name="" value="1" checked disabled/>
													 	<span class="text" style="color:grey;">待办事项</span>
												    </label>
												    <label class="padding-left-10">
													 	<input type="checkbox" class="colored-blue" name="usePhone" value="1" checked/>
													 	<span class="text" style="color:inherit;">手机短信</span>
												    </label>
												</div>   
												<div class="ps-clear"></div>              
				                            </li>
	                        			</c:when>
	                        		</c:choose>
	                        	</c:when>
	                        
	                        </c:choose>
	                        
	                        
	                    </ul>
	                   </form>
                </div>
           </div>
         </div>
     
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>	
</body>
</html>
