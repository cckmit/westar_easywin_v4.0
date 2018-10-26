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
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
	function formSub(){
		if($("#subState").val()==1){
			return false;
		}
		$("#fileDivAjax").html('');
		 $("select[multiple=multiple]").each(function(){
			      var index = 0;
			      var pp = $(this);
			      $(this).children().each(function(i){
			        var input = $('<input>');  
                 input.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listkey"));  
                 input.attr("type","hidden");  
                 input.attr("value",$(this).val());  
                 $("#fileDivAjax").append(input); 
                 
                 var inputname = $('<input>');  
                 inputname.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listvalue"));  
                 inputname.attr("type","hidden");  
                 inputname.attr("value",$(this).text());  
                $("#fileDivAjax").append(inputname); 
                 index ++;  
			      });
			    });
		var flag = false;
		$(".subform").ajaxSubmit({
		        type:"post",
		        url:"/item/addCrmItem?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	var itemName = $("#itemName").val();
		        	if(strIsNull(itemName)){
		        		scrollToLocation($("#contentBody"),$("#itemName"));
		        		layer.tips('请填写项目名称', "#itemName", {tips: 1});
		        		return false;
		        	}
		        	var count = itemName.replace(/[^\x00-\xff]/g,"**").length;
					if(count>52){
						scrollToLocation($("#contentBody"),$("#itemName"));
						layer.tips('项目名称太长', "#itemName", {tips: 1});
		        		return false;
					}
					var owner = $("#owner").val();
					if(strIsNull(owner)){
						scrollToLocation($("#contentBody"),$("#owner"));
						layer.tips('请选择项目责任人', "#owner", {tips: 1});
		        		return false;
					}
		        	$("#subState").val(1)
				}, 
		        success:function(data){
			        if('y'==data.status){
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
	$(document).ready(function(){
			$("#itemName").focus();
			
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

		                $("#usedUserDiv").append($(headImgDiv));
		                $(headImgDiv).on("click",function(){
		                	setUser('owner','onwerName',usedUser.id,$(this));
		                })
		            })
		        }
		    });
		});
	
	//人员范围的显示隐藏
	function showMans(){
		
		if($("#pubState").val() == 0){
			$("#mans").show();
		}else{
			$("#mans").hide();
		}
		
	}
</script>
<style type="text/css">
	.btns {
		padding-left: 0px;	
	}
	.btns,.btn {
		margin-left: 0px !important;	
	}
	
</style>
</head>
<body style="background-color: #fff">
<input type="hidden" id="subState" value="0">
<form class="subform" method="post">
	<div id="fileDivAjax">
	</div>
	<input type="hidden" name="parentId" value="${item.parentId}"/>
	<input type="hidden" name="attentionState" id="attentionState" value="0"/>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">创建项目</span>
                        <div class="widget-buttons ps-toolsBtn">
							<a href="javascript:void(0)" class="blue" onclick="setAtten(this)">
								<i class="fa fa-star-o"></i>关注
							</a>
						</div>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                                
                     <div class="widget-body margin-top-40" id="contentBody" style="height: 360px;overflow-y:auto;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue">项目信息</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white" >
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right">
												&nbsp;关联客户：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											 <strong>${crm.customerName}</strong>
											 <input type="hidden" id="partnerId" name="partnerId" value="${item.partnerId}"/>
											</div>
										</li>
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text" style="text-align: right">
										    	<span style="color: red">*</span>
										    	&nbsp;项目名称：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div>
													<input class="colorpicker-default form-control" type="text"  id="itemName" name="itemName" 
													 onpropertychange="handleName()" onkeyup="handleName()" style="width: 350px;float: left"/>
													<span id="msgTitle" style="float:left;width: auto;">(0/26)</span>
												</div>
											</div>
											<script> 
												//当状态改变的时候执行的函数 
												function handleName(){
													var value = $('#itemName').val();
													var len = charLength(value.replace(/\s+/g,""));
													if(len%2==1){
														len = (len+1)/2;
													}else{
														len = len/2;
													}
													if(len>26){
														$('#msgTitle').html("(<font color='red'>"+len+"</font>/26)");
													}else{
														$('#msgTitle').html("("+len+"/26)");
													}
												} 
												//firefox下检测状态改变只能用oninput,且需要用addEventListener来注册事件。 
												if(/msie/i.test(navigator.userAgent)){    //ie浏览器 
													document.getElementById('itemName').onpropertychange=handleName 
												}else {//非ie浏览器，比如Firefox 
													document.getElementById('itemName').addEventListener("input",handleName,false); 
												} 
											</script>   
                                         </li>
                                         <li class="ticket-item no-shadow ps-listline" >
										    <div class="pull-left gray ps-left-text" style="text-align: right">
										    	&nbsp;责任人：
										    </div>
											<div class="ticket-user pull-left ps-right-box" style="min-width: 135px">
												<tags:userOne datatype="s" name="owner" defaultInit="true"
													showValue="${userInfo.userName}" value="${userInfo.id}" uuid="${userInfo.smImgUuid}"
													filename="${userInfo.smImgName}" gender="${userInfo.gender}" onclick="true"
													showName="onwerName"></tags:userOne>
												<div id="usedUserDiv" style="width: 330px;display: inline-block;">
													<span class="pull-left" style="padding-top:8px;display: inline-block;padding-left: 10px">常用人员:</span>
												</div>
											</div>
                                        </li>
										<li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right">
									    		&nbsp;项目简介：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
										  		<textarea class="colorpicker-default form-control margin-top-10 margin-bottom-10" style="height:150px;width: 350px" id="itemRemark" name="itemRemark" rows="" cols=""></textarea>
											</div> 
											<div class="ps-clear"></div>              
                                         </li>
                                         
                                          <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right">
											    	&nbsp;阅读权限：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
												  		<select class="populate" id="pubState" name="pubState" onchange="showMans()">
						 								<option value="0">私有</option>
						 								<option value="1">公开</option>
															</select>
													</div>
												</div>
	                                         </li>
                                         
                                         <li class="ticket-item no-shadow autoHeight no-padding" style="display: block;" id="mans">
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;line-height: 40px">
									    		&nbsp;分享人：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left gray ps-left-text padding-top-10">
											  		<tags:userMore name="listItemSharer.userId" showName="userName" disDivKey="itemSharor_div"></tags:userMore>
												</div>
											</div>
											<div class="ps-clear"></div>
                                         </li>
                                         
										<li class="ticket-item no-shadow autoHeight no-padding" >
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right">
									    		&nbsp;相关附件：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="width: 400px;height: auto;">
												<div class="margin-top-10">
											  		<tags:uploadMore name="listUpfiles.id" showName="fileName" ifream="" comId="${userInfo.comId}"></tags:uploadMore>
												</div>
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
