<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
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

$(function(){
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
                	var usedUser = $(this).data("userObj");
                	setUser('toUser','onwerName',usedUser.id,$(this));
                })
            })
        }
    });
})
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
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
		    		if(null==toUser || "" ==toUser ){
		    			 layer.tips('请选择项目接收人员！', "#showToUser", {tips: 1});
		    			return false;
		    		}else if($("#fromUser").val()==toUser){
		    			 layer.tips('不能移交给自己！', "#showToUser", {tips: 1});
		    			return false;
		    		}
		    		$("#subState").val(1)
				}, 
		        success:function(data){
			        if('y'==data.status){
			        	showNotification(1,"移交成功");
			        	flag = true;
			        }else{
			        	showNotification(2,data.info);
			        }
		        },error:function(XmlHttpRequest,textStatus,errorThrown){
		        	showNotification(2,"系统错误，请联系管理人员");
		        }
		 });
		 $("#subState").val(0)
		 return flag;
	}
//删除不需要移交的项目
function del_li(obj){
	if($("[name='itemIds']").length>1){
		$(obj).parent().remove();
	}else{
		showNotification(2,"总的移交一个吧！");
	}
}
</script>

</head>
<body style="background: #ffffff">
<input type="hidden" id="subState" value="0">
<form class="subform" method="post">
	<input type="hidden" id="fromUser" name="fromUser" value="${itemHandOver.fromUser }"/>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">批量移交项目</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <div class="widget-body margin-top-40" style="height: 310px;overflow-y:auto;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white" >
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline" style="border: 0">
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
												待移交项目：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<c:choose>
												 	<c:when test="${not empty listItem}">
												 		<c:forEach items="${listItem}" var="itemVo" varStatus="status">
												 		 <div class="alert-success margin-bottom-5" style="width: 350px">
															 <input type="hidden" name="itemIds" value="${itemVo.id}"/>
				                                            <button class="close margin-right-5" type="button" style="margin-top: 5px"  onclick="del_li(this);">
				                                                ×
				                                            </button>
				                                            <span class="margin-left-10">
				                                            	${itemVo.itemName}&nbsp;
				                                            </span>
				                                       	 </div>
												 		</c:forEach>
												 	</c:when>
												</c:choose>
											</div>
			
										</li>
										<div class="ps-clear"></div>
                                         <li class="ticket-item no-shadow ps-listline" >
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	&nbsp;项目经理：
										    </div>
											<div class="ticket-user pull-left ps-right-box" id="showToUser" style="min-width: 135px">
												<tags:userOne name="toUser" showValue="" value=""  
												showName="onwerName"></tags:userOne>
												<div id="usedUserDiv" style="width: 280px;display: inline-block;">
													<span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
												</div> 
											</div>
                                        </li>
										<li class="ticket-item no-shadow autoHeight no-padding" style="border: 0">
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
									    		&nbsp;移交说明：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 350px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" id="replayContent"  name="replayContent" 
										  		rows="" cols="" style="height:150px;width:350px;" placeholder="移交说明……"></textarea>
											</div> 
											<div class="ps-clear"></div>              
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
</body>
</html>
