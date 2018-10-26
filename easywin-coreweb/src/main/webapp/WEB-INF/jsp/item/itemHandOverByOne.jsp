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
<script type="text/javascript">
var sid="${param.sid}";
var pageParam= {
    "sid":"${param.sid}"
}
	function formSub(){
		if($("#subState").val()==1){
			return false;
		}
		var flag = false;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/item/batchItemHandOver?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	var toUser = $("#toUser").val()
		    		if(null==toUser || ""==toUser ){
		    			scrollToLocation($("#contentBody"),$("#showToUser"));
		    			layer.tips('请选择项目接收人员！', "#showToUser", {tips: 1});
		    			return false;
		    		}else if($("#fromUser").val()==toUser){
		    			scrollToLocation($("#contentBody"),$("#showToUser"));
		    			layer.tips('不能移交给自己！', "#showToUser", {tips: 1});
		    			return false;
		    		}
		    		$("#subState").val(1)
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
		 $("#subState").val(0)
		 return flag;
	}

	
	$(function(){
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		//列出常用人员信息
	    listUsedUser(2,function(data){
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

	                $("#usedUserDiv").append($(headImgDiv));
	                $(headImgDiv).on("click",function(){
	                	var userObj = $(this).data("userObj");
	                	setUser('toUser','onwerName',userObj.id,$(this));
	                })
	            })
	        }
	    });
	})
</script>
</head>
<body>

<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
          	<span class="widget-caption themeprimary ps-layerTitle">项目移交</span>
                <div class="widget-buttons">
		<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
			<i class="fa fa-times themeprimary"></i>
		</a>
	</div>
          </div>
<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">

	
		<input type="hidden" id="subState" value="0">
    	<div class="widget radius-bordered margin-bottom-0">
        	<div class="widget-header bg-bluegray no-shadow">
            	<span class="widget-caption blue">${item.itemName}</span>
             </div>
             <div class="widget-body no-shadow">
              	<div class="tickets-container bg-white">
              		<form class="subform" method="post" action="/crm/customerHandOver">
						<input type="hidden" name="itemIds" value="${item.id}"/>
						<input type="hidden" id="fromUser" name="fromUser" value="${itemHandOver.fromUser }"/>
						<ul class="tickets-list">
							<li class="ticket-item no-shadow ps-listline">
						    	<div class="pull-left gray ps-left-text" style="text-align: right;">
						    		&nbsp;项目经理：
						    	</div>
								<div class="ticket-user pull-left ps-right-box">
									<img src="/downLoad/userImg/${userInfo.comId}/${item.owner}?sid=${param.sid}"
										title="${item.ownerName}" class="user-avatar"/>
									<span class="user-name">${item.ownerName}</span>
								</div>               
	                        </li>
							<li class="ticket-item no-shadow ps-listline">
						    	<div class="pull-left gray ps-left-text" style="text-align: right;">
						    		<span style="color: red">*</span>&nbsp;接收人：
						    	</div>
								<div class="ticket-user pull-left ps-right-box" id="showToUser" style="min-width: 135px">
									<tags:userOne name="toUser" showValue="" value=""
									 showName="onwerName" nullmsg="请选择接收人"></tags:userOne>
									 <div id="usedUserDiv" style="width: 280px;display: inline-block;">
										<span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
									</div>
								</div>
	                        </li>
	                        <li class="ticket-item no-shadow autoHeight no-padding">
						    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
						    		&nbsp;移交说明：
						    	</div>
								<div class="ticket-user pull-left ps-right-box" style="width: 200px;height: auto;">
							  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" 
							  		style="height:130px;width:320px;" id="replayContent" 
							  		name="replayContent" rows="" cols="" placeholder="移交说明……"></textarea>
								</div> 
								<div class="ps-clear"></div>              
                            </li>
	                    </ul>
	                   </form>
                </div>
           </div>
         </div>
     
</div>
				


</body>
</html>
