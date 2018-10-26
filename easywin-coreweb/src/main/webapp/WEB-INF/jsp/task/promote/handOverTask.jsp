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
<script src="/static/js/taskJs/taskOpt.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	var sid="${param.sid}";
	var pageParam= {
			"sid":"${param.sid}"
	}
	$(function() {
		//任务办理人员信息设置
		$("body").on("click",".exectorSelectBtn",function(){
			taskOptForm.chooseExectors($(this));
		});
		//列出常用人员信息
		listUsedUser(5,function(data){
			if(data.status=='y'){
				var usedUser = data.usedUser;
				$.each(usedUser,function(index,userObj){
					//添加头像
					var headImgDiv = $('<div class="ticket-user pull-left other-user-box"></div>');
					$(headImgDiv).data("userObj",userObj);
					
					var imgObj = $('<img src="/downLoad/userImg/'+userObj.comId+'/'+userObj.id+'" class="margin-left-5 usedUserImg"/>')
					
					var headImgName = $('<span class="user-name2" style="font-size:6px;display:inline-block"></span>');
					$(headImgName).html(userObj.userName);
					
					$(headImgDiv).append($(imgObj));
					$(headImgDiv).append($(headImgName));
					
					$("#usedUserDiv").append($(headImgDiv));
					
					$(headImgDiv).on("click",function(){
						var relateSelect = $(".exectorSelectBtn").attr("relateSelect");
						var relateImgDiv = $(".exectorSelectBtn").attr("relateImgDiv");
						taskOptForm.appendUsedUser(userObj,relateSelect,relateImgDiv)
					})
					
				})
			}
		});
		
		$("body").on("click","#owner_img",function(){
			if("${nextStepUserState eq 0}" == "true"){
				return false;
			}
			
			//选项
			var option = $("<option></option>");
			$(option).attr("value","${task.owner}");
			$(option).html("${task.ownerName}");
			$("#listTaskExecutor_executor").html($(option));
			//头像
			var headImgDiv = $('<div class="online-list " style="float:left;padding-top:5px" title="双击移除"></div>');
			var imgSrc = $(this).attr("src");
			var img = $('<img class="user-avatar"/>');
			$(img).attr("src",imgSrc);
			$(img).attr("id","userImg_${task.owner}");
			//名字
			var spanName = $("<span class='user-name'></span>")
			$(spanName).html("${task.ownerName}");
			$(headImgDiv).append(img);
			$(headImgDiv).append(spanName);
			
			$("#listTaskExecutor_executorDiv").html(headImgDiv);
			
			$(headImgDiv).on("dblclick",function(){
				$(this).remove();
				$("#listTaskExecutor_executor").find("option[value='${task.owner}']").remove();
			})
			
		})
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsgV2(msg, o, cssctl);
			},
			showAllError : true
		});
	})
	//表单提交
	function formSub(){
		$("#fileDiv").html('');
		
		$("select[multiple=multiple]").each(function(){
		      var index = 0;
		      var pp = $(this);
		      $(this).children().each(function(i){
		        var input = $('<input>');  
                input.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listkey"));  
                input.attr("type","hidden");  
                input.attr("value",$(this).val());  
                $("#fileDiv").append(input); 
                
                var inputname = $('<input>');  
                inputname.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listvalue"));  
                inputname.attr("type","hidden");  
                inputname.attr("value",$(this).text());  
                $("#fileDiv").append(inputname); 
                 
                index ++;  
		      });
		    });
		
		var obj;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/task/handOverTask?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	if('${nextStepUserState eq 1}'=='true'){
	                    //检查完成时限
	                    var expectTime = $("#expectTime").val();
	                    if(!expectTime){
	                        layer.tips("请填写完成时限",$("#expectTime"),{"tips":1});
	                        return false;
	                    }
	                    if(!$("#stepTag").val()){
			    			layer.tips('请选择节点标识', "#stepTag", {tips: 1});
			    			return false;
			    		}
		    		}
		    		
		        	var executors = $("#listTaskExecutor_executor").find("option");
		    		if(!executors || !executors.get(0)){
		    			layer.tips('请选择任务下一步协同人', "#showToUser", {tips: 1});
		    			return false;
		    		}
		    		
				},
		        success:function(data){
			        obj=data;
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"系统错误，请联系管理人员");
		        }
		 });
		 return obj;
	}
	

	$(document).ready(function(){
		$("#taskName").focus();
		$("#cooperateExplain").autoTextarea({minHeight:70,maxHeight:80}); 
	});
</script>
</head>
<body>
<div class="container" style="padding: 0px 0px;width: 100%">	
	<div class="row" style="margin: 0 0">
		<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
		<input type="hidden" id="subState" value="0">
   		<div class="widget" style="margin-top: 0px;">
   			<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
            <div class="widget-caption">
				<span class="themeprimary ps-layerTitle">任务协同</span>
			</div>
		 		<div class="widget-buttons">
				<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
					<i class="fa fa-times themeprimary"></i>
				</a>
				</div> 
             </div>
             
            <div class="widget-body no-shadow  margin-top-40"  id="contentBody" style="overflow: hidden;overflow-y:scroll;">
             	<div class="tickets-container bg-white">
             		<form class="subform" method="post">
             		<div style="display: none" id="fileDiv">
             		</div>
					<input type="hidden" name="sid" value="${param.sid}"/>
					<input type="hidden" name="id" value="${task.id}"/>
					<input type="hidden" name="redirectPage" value="${redirectPage}"/>
					<input type="hidden" name="taskName" value="${task.taskName}"/>
					<input type="hidden" name="taskProgress" value="${task.taskProgress}"/>
					<input type="hidden" name="version" value="${task.version}"/>
					<ul class="tickets-list">
						<li class="ticket-item no-shadow ps-listline">
					    	<div class="pull-left gray ps-left-text">
					    		&nbsp;负责人
					    	</div>
							<div class="ticket-user pull-left ps-right-box">
								<img style="float: left" src="/downLoad/userImg/${userInfo.comId}/${task.owner}"
									class="user-avatar userImg" title="${nextStepUserState eq 0 ?task.ownerName:'点击，反馈给我。'} " id="owner_img" />
								<span class="user-name">${task.ownerName}</span>
							</div>               
                        </li>
                        
                        <c:choose>
                        	<c:when test="${nextStepUserState eq 0 }">
								<li class="ticket-item no-shadow ps-listline">
							    	<div class="pull-left gray ps-left-text">
							    		<span style="color: red">*</span>办理人
							    	</div>
									<div class="ticket-user pull-left ps-right-box" id="showToUser" style="min-width: 135px">
										<img style="float: left" src="/downLoad/userImg/${userInfo.comId}/${pushUserInfo.id}" 
											class="user-avatar userImg"/>
										<span class="user-name">${pushUserInfo.userName}</span>
										
										<div style="width: 250px;display:none;">
											<select datatype="*" list="listTaskExecutor" listkey="executor" listvalue="executorName" 
												id="listTaskExecutor_executor" name="listTaskExecutor.executor" ondblclick="removeClick(this.id)" multiple="multiple" 
												moreselect="true" style="width: 100%; height: 100px;">
												<option value="${pushUserInfo.id}">${pushUserInfo.userName}</option>
											</select>
										</div>
													
									</div>
		                        </li>
                        	</c:when>
                        	<c:otherwise>
                        		<li class=" ticket-item no-shadow ps-listline">
									<div class="pull-left col-lg-6 col-sm-6 col-xs-6 no-padding-left" >
								    	<div class="pull-left gray ps-left-text">
								    		任务类型<span style="color: red">*</span>：
								    	</div>
								    	<div class="ticket-user pull-left ps-right-box">
								    		<div class="row">
	                                            <tags:dataDic type="taskType" name="taskType" id="taskType" value="2"></tags:dataDic>
                                            </div>
								    	</div>
									</div>
									<div class="pull-left col-lg-6 col-sm-6 col-xs-6 no-padding-left" >
								    	<div class="pull-left gray ps-left-text">
								    		办理时限<span style="color: red">*</span>：
								    	</div>
										<div class="ticket-user pull-left ps-right-box">
											 <input type="text" class="form-control" placeholder="完成时限"  style="width:150px;"
				                                            	datatype="ff,pff" id="expectTime" name="expectTime" />
										</div> 
										<span style="line-height: 30px">小时</span>              
									</div>
		                        </li>
		                        <li class=" ticket-item no-shadow ps-listline">
									<div class="pull-left col-lg-6 col-sm-6 col-xs-6 no-padding-left" >
								    	<div class="pull-left gray ps-left-text">
								    		节点标识<span style="color: red">*</span>：
								    	</div>
                                        <div class="ticket-user pull-left ps-right-box">
                                        	<div class="row pull-left">
                                        		<tags:dataDic type="stepTag" name="stepTag" id="stepTag" datatype="*" please="t"></tags:dataDic>
											</div>
                                        </div>
                                    </li>
								 <li class="ticket-item no-shadow autoHeight no-padding">
							    	<div class="pull-left gray ps-left-text padding-top-10">
							    		办理人<span style="color: red">*</span>：
							    	</div>
									<div class="ticket-user pull-left ps-right-box" id="showToUser" style="min-width: 135px;height: auto;">
										<div style="width: 250px;display:none;">
										<select datatype="*" list="listTaskExecutor" listkey="executor" listvalue="executorName" 
											id="listTaskExecutor_executor" name="listTaskExecutor.executor" ondblclick="removeClick(this.id)" multiple="multiple" 
											moreselect="true" style="width: 100%; height: 100px;">
										</select>
									</div>
									<div class="pull-left" id="listTaskExecutor_executorDiv" style="max-width: 450px"></div>
									<div class="ps-clear"></div>
										<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 exectorSelectBtn" 
											relateSelect="listTaskExecutor_executor" relateImgDiv="listTaskExecutor_executorDiv"
											title="人员选择"  style="float: left;"><i class="fa fa-plus"></i>选择</a>
										<div id="usedUserDiv" style="width: 400px;display: inline-block;">
											<span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
										</div>
									</div>
									<div class="ps-clear"></div>
		                        </li>
                        	</c:otherwise>
                        </c:choose>
                        
                        <li class="ticket-item no-shadow autoHeight no-padding">
					    	<div class="pull-left gray ps-left-text padding-top-10">
					    		&nbsp;移交说明
					    	</div>
							<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
						  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
						  		style="height:55px;width:400px;" id="cooperateExplain" 
						  		name="cooperateExplain" rows="" cols="" placeholder="移交说明……"></textarea>
						  		<a href="javascript:void(0);" class="fa fa-comments-o" onclick="addIdea('cooperateExplain','${param.sid}');" title="常用意见"></a>
							</div> 
							<div class="ps-clear"></div>              
                       	</li>
						<li class="ticket-item no-shadow autoHeight" >
					    	<div class="pull-left gray ps-left-text padding-top-10">&nbsp;</div>
							<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
								<div class="margin-top-10">
									<tags:uploadMore name="listTaskUpfile.upfileId" showName="filename" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
								</div>
							</div> 
							<div class="ps-clear"></div>                
	                       </li>
                    </ul>
                   </form>
               </div>
          </div>
        </div>
        </div>
	</div>
</div>
</body>
</html>
