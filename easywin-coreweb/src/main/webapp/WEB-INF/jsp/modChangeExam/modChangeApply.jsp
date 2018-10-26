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
		        url:"/moduleChangeExamine/addApply?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	if($("#field").val() == "customerTypeId"){
		        		 if($("#oldValue").val() == $("#newValue").val()){
				    			scrollToLocation($("#contentBody"),$("#newValue"));
				    			layer.tips("变更后类型和原来相同",$("#newValue"),{tips:1});
				    			return false;
				    	}else if(!'${listExamUser[0]}'){
			    			scrollToLocation($("#contentBody"),$("#examUserDiv"));
			    			layer.tips("暂无相关审批人，请联系管理员",$("#examUserDiv"),{tips:1});
			    			return false;
			    		}else if(!$("#content").val()){
			    			scrollToLocation($("#contentBody"),$("#content"));
			    			layer.tips("请填写变更说明",$("#content"),{tips:1});
			    			return false;
			    		}
		        	}else if($("#field").val() == "stage"){
		        		 if($("#oldValue").val() == $("#newValue").val()){
				    			scrollToLocation($("#contentBody"),$("#newValue"));
				    			layer.tips("变更后阶段和原来相同",$("#newValue"),{tips:1});
				    			return false;
				    	}else if(!'${listExamUser[0]}'){
			    			scrollToLocation($("#contentBody"),$("#examUserDiv"));
			    			layer.tips("暂无相关审批人，请联系管理员",$("#examUserDiv"),{tips:1});
			    			return false;
			    		}else if(!$("#content").val()){
			    			scrollToLocation($("#contentBody"),$("#content"));
			    			layer.tips("请填写变更说明",$("#content"),{tips:1});
			    			return false;
			    		}
		        	}else if($("#field").val() == "owner"){
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
</script>
</head>
<body>

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	<span class="widget-caption themeprimary ps-layerTitle">属性变更申请</span>
    <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
</div>
<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">

    	<div class="widget radius-bordered margin-bottom-0">
        	<div class="widget-header bg-bluegray no-shadow">
            	<span class="widget-caption blue">${moduleChangeApply.busName}</span>
             </div>
             <div class="widget-body no-shadow">
              	<div class="tickets-container bg-white">
              		<form class="subform" method="post" action="/moduleChangeExamine/addApply">
						<input type="hidden" name="busId" value="${moduleChangeApply.busId}"/>
						<input type="hidden" name="redirectPage" value="${param.redirectPage}"/>
						<input type="hidden" name="moduleType" value="${moduleChangeApply.moduleType}"/>
						<input type="hidden" id="field" name="field" value="${moduleChangeApply.field}"/>
						<input type="hidden" name="oldValue" value="${moduleChangeApply.oldValue}"/>
						<input type="hidden" name="busName" value="${moduleChangeApply.busName}"/>
						<ul class="tickets-list">
	                        <c:choose>
	                        	<c:when test="${moduleChangeApply.moduleType == '012'}">
	                        		<c:choose>
	                        			<c:when test="${moduleChangeApply.field == 'customerTypeId'}">
	                        				<li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		当前类型：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px" >
													${moduleChangeApply.oldName}
												</div> 
					                        </li>
					                        <li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		<font color="red">*</font> 预变更类型：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
													<select class="populate" datatype="customerType" id="newValue" name="newValue" style="cursor:auto;width: 200px">
														<optgroup label="选择客户类型"></optgroup>
														<c:choose>
															<c:when test="${not empty listCustomerType}">
																<c:forEach items="${listCustomerType}" var="customerType" varStatus="status">
																	<option value="${customerType.id}" ${(moduleChangeApply.newValue==customerType.id)?
																		"selected='selected'":''}>${customerType.typeName}</option>
																</c:forEach>
															</c:when>
														</c:choose>
													</select>
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
										    		<font color="red">*</font>变更说明：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:70px;width:320px;" id="content" 
											  		name="content" rows="" cols="" placeholder="变更说明……"></textarea>
												</div> 
												<div class="ps-clear"></div>              
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
	                        			<c:when test="${moduleChangeApply.field == 'owner'}">
	                        				<li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		&nbsp;责任人：
										    	</div>
												<div class="ticket-user pull-left ps-right-box">
													<img src="/downLoad/userImg/${userInfo.comId}/${moduleChangeApply.oldValue}?sid=${param.sid}"
														title="${moduleChangeApply.oldName}" class="user-avatar"/>
													<span class="user-name">${moduleChangeApply.oldName}</span>
												</div>               
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
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:70px;width:320px;" id="content" 
											  		name="content" rows="" cols="" placeholder="移交说明……"></textarea>
												</div> 
												<div class="ps-clear"></div>              
				                            </li>
					                        <li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		<font color="red">*</font>反馈类型：
										    	</div>
												<div class="ticket-user pull-left ps-right-box">
													<select class="populate" id="feedBackTypeId" name="feedBackTypeId" style="width:320px;">
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
	                        			<c:when test="${moduleChangeApply.field == 'stage'}">
	                        				<li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		当前阶段：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px" >
														${moduleChangeApply.oldName}
												</div> 
					                        </li>
					                        <li class="ticket-item no-shadow ps-listline">
										    	<div class="pull-left gray ps-left-text" style="text-align: right;">
										    		<font color="red">*</font> 预变更阶段：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
													<select class="populate" datatype="customerType" id="newValue" name="newValue" style="cursor:auto;width: 200px">
														<optgroup label="选择所属阶段"></optgroup>
														<c:choose>
															<c:when test="${not empty listCustomerStages}">
																<c:forEach items="${listCustomerStages}" var="stage" varStatus="status">
																	<option value="${stage.id}" ${(moduleChangeApply.newValue==stage.id)?
																		"selected='selected'":''}>${stage.stageName}</option>
																</c:forEach>
															</c:when>
														</c:choose>
													</select>
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
										    		<font color="red">*</font>变更说明：
										    	</div>
												<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
											  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:70px;width:320px;" id="content" 
											  		name="content" rows="" cols="" placeholder="变更说明……"></textarea>
												</div> 
												<div class="ps-clear"></div>              
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
</body>
</html>
