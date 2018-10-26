<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
		<!-- 框架样式 -->
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<script type="text/javascript">
		var EasyWin = {
				"sid" : "${param.sid}",
				"userInfo" : {
					"userId" : "${userInfo.id}",
					"userName" : "${userInfo.userName}",
					"comId" : "${userInfo.comId}",
					"orgName" : "${userInfo.orgName}",
					"isAdmin" : "${userInfo.admin}",
				}
			};
		
		//关闭窗口
		function closeWin(){
			var winIndex = window.top.layer.getFrameIndex(window.name);
			closeWindow(winIndex);
		}
		
		$(function(){
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
			//公开类型变换后
			$("body").on("change","#pubState",function(){
				var val = $(this).val();
				if(val == 0){
					$("#shareUserList").show();
				}else{
					$("#shareUserList").hide();
				}
			})
			//移除数据
			$("body").on("dblclick","[data-userId]",function(){
				$(this).remove();
			})
			
			$("body").on("click",".userSetBtn",function(){
				//人员多选
		    	userMore('null','',EasyWin.sid,'','null',function(result){
		    		//本次选择的管理人员
		    		if(result && result[0]){
						 for ( var i = 0; i < result.length; i++) {
							var userId = result[i].value;
							var userName = result[i].text;
							var user = {
								id:userId,
								userName:userName
							}
								
							var preVal = $("body").find("#shareUserList").find('[data-userId='+userId+']');
							if(!preVal || !preVal.get(0)){
								//头像名称父div
								var divP = $('<div class="ticket-user pull-left other-user-box" title="双击移除"></div>');
								$(divP).attr("data-userId",userId);
								
								$(divP).data("user",user);
								//头像
								var img = $('<img src="/downLoad/userImg/'+EasyWin.userInfo.comId+'/'+userId+'" class="user-avatar">');
								//名称
								var name = $('<span class="user-name">'+userName+'</span>')
							
								$(divP).append(img);
								$(divP).append(name);
								
								$(".userSetBtn").prev().append($(divP))
							  }
						  }
		    		 }
		    	});
			})
		})
		var busId;
		var busType
		function initData(busIdParam,busTypeParam){
			busId = busIdParam;
			busType =busTypeParam;
			var url = "/common/ajaxListShareUsers?sid=${param.sid}&t="+Math.random();
			var param = {
					busId:busId,
					busType:busType,
			}
			if(busIdParam){
				$("#allShareDiv").remove();
				//异步查询数据
				postUrl(url,param,function(data){
					
					if(data.modObj.userId=='${userInfo.id}'){
						$(".ps-layerTitle").html('资料属性')
						$("#shareShowDiv").remove();
					}else{
						$("#ownerShowDiv").remove();
						$(".ps-layerTitle").html('资料属性')
					}
					
					constrHtml(data);
					constrModInfo(data.modObj,busType);
				})
			}else{
				$("#ownerShowDiv").remove();
				$("#shareShowDiv").remove();
			}
		}
		//构建展示信息
		function constrModInfo(modObj,busType){
			$("body").find("[data-justInfoLi='1']:eq(0)").find("[data-info]").html(modObj.fileExt);
			$("body").find("[data-justInfoLi='1']:eq(1)").find("[data-info]").html(modObj.sizem);
			$("body").find("[data-justInfoLi='1']:eq(2)").find("[data-info]").html(modObj.fileDescribe);
			$("body").find("[data-justInfoLi='1']:eq(3)").find("[data-info]").html(modObj.recordCreateTime);
		}
		//构建公开范围的html
		function constrHtml(data){
			var pubState = data.pubState;
			
			//头像
			var img = $('<img src="/downLoad/userImg/${userInfo.comId}/'+data.modObj.userId+'" class="user-avatar">');
			//名称
			var name = $('<span class="user-name">'+data.modObj.userName+'</span>');
			$("[data-userImg=1]").append($(img));
			$("[data-userImg=1]").append($(name));
			
			$('[data-fileName=1]').html(data.modObj.fileName)
			
			$("#pubState").val(pubState);
			if(pubState == 1){
				$("#shareUserList").hide();
				return;
			}
			$("#shareUserList").show();
			var listShare = data.listShare;
			if(listShare && listShare[0]){
				$.each(listShare,function(index,user){
					//头像名称父div
					var divP = $('<div class="ticket-user pull-left other-user-box" title="双击移除"></div>');
					$(divP).attr("data-userId",user.id);
					$(divP).data("user",user);
					//头像
					var img = $('<img src="/downLoad/userImg/'+EasyWin.userInfo.comId+'/'+user.id+'" class="user-avatar">');
					//名称
					var name = $('<span class="user-name">'+user.userName+'</span>')
				
					$(divP).append(img);
					$(divP).append(name);
					
					$(".userSetBtn").prev().append($(divP))
				})
			}
		}
		//返回公开范围
		function returnUsers(){
			var pubState = $("#pubState").val();
			
			if(pubState==-1){
				scrollToLocation($('#contentBody'),$("#pubState"))
				layer.tips("请选择公开类型！",$("#pubState"),{tips:1})
				return;
			}
			var result = {}
			result.pubState = pubState;
			
			var listShare = new Array();
			var shareUsers = $("body").find("#shareUserList").find('[data-userId]');
			
			if(shareUsers && shareUsers[0]){
				$.each(shareUsers,function(index,shareUser){
					var usr = $(shareUser).data("user");
					listShare.push(usr);
				})
			}
			result.listShare = listShare;
			return result;
		}
		
		</script>
		<style type="text/css">
			.online-list{cursor: pointer;}
		</style>
	</head>
	<body >
		<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
           	<span class="widget-caption themeprimary ps-layerTitle">公开范围</span>
                 <div class="widget-buttons">
			<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
				<i class="fa fa-times themeprimary"></i>
			</a>
		</div>
      </div>
      <div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
			<div class="widget-body no-shadow no-padding padding-top-5">
               	<div class="widget-main ">
                   	<div class="tabbable">
                           <div class="tabs-flat">
                           <!--批量设置 -->
                           	<div class="tab-pane" id="allShareDiv">
								<div class="widget-body no-shadow">
									<ul>
										<li class="ticket-item no-shadow ps-listline" >
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	<font color="red">*</font>阅读权限：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
											  		<select class="populate" id="pubState" name="pubState">
						 								<option value="-1">请选择</option>
						 								<option value="1">公开</option>
						 								<option value="0">私有</option>
													</select>
												</div>
											</div>
                                         </li>
                                          <li class="ticket-item no-shadow autoHeight no-padding" id="shareUserList" style="display: none">
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
									    		共享人：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left" style="width: 300px">
												</div>
												<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
												 style="float: left;"><i class="fa fa-plus"></i>选择</a>
											</div>
											<div class="ps-clear"></div>
                                         </li>
									</ul>
								</div>
							</div>
							 <!--负责人置 -->
                           	<div class="tab-pane" id="ownerShowDiv">
                           		<div class="widget-header bg-bluegray no-shadow" style="clear:both;">
									<span class="widget-caption blue">基本信息</span>
									<span class="widget-caption red">
							           <small class="ws-active ws-color"></small>
							        </span>
									<div class="widget-buttons btn-div-full">
									</div>
								</div>
								
								<div class="widget-body no-shadow">
									<ul>
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;资料 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-fileName="1" style="width: 350px">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;文件类型 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;文件大小 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both;" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">描述 ：</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
											</div>
	                                    </li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;创建人 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-userImg="1">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both;" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">创建日期 ：</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
											</div>
	                                    </li>
									</ul>
								</div>
								
								<div class="widget-header bg-bluegray no-shadow" style="clear:both;" >
									<span class="widget-caption blue">公开范围</span>
									<span class="widget-caption red">
							           <small class="ws-active ws-color"></small>
							        </span>
									<div class="widget-buttons btn-div-full">
									</div>
								</div>
								<div class="widget-body no-shadow">
									<ul>
										<li class="ticket-item no-shadow ps-listline" >
										    <div class="pull-left gray ps-left-text" style="text-align: right;">
										    	<font color="red">*</font>阅读权限：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											  	<div class="pull-left">
											  		<select class="populate" id="pubState" name="pubState">
						 								<option value="-1">请选择</option>
						 								<option value="1">公开</option>
						 								<option value="0">私有</option>
													</select>
												</div>
											</div>
                                         </li>
                                         <li class="ticket-item no-shadow autoHeight no-padding" id="shareUserList">
									    	<div class="pull-left gray ps-left-text padding-top-10" style="text-align: right;">
									    		共享人：
									    	</div>
											<div class="ticket-user pull-left ps-right-box" style="height: auto;">
												<div class="pull-left" style="width: 300px">
												</div>
												<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
												 style="float: left;"><i class="fa fa-plus"></i>选择</a>
											</div>
											<div class="ps-clear"></div>
                                         </li>
									</ul>
								</div>
								
							</div>
							<!-- 共享人查看 -->
                           	<div class="tab-pane" id="shareShowDiv">
								<div class="widget-body no-shadow">
									<ul>
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;资料 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-fileName="1" style="width: 350px">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;文件类型 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;文件大小 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both;" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">描述 ：</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
											</div>
	                                    </li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both">
											<div class="pull-left gray ps-left-text" style="text-align: right;">
												&nbsp;创建人 ：
											</div>
											<div class="ticket-user pull-left ps-right-box" data-userImg="1">
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline" style="clear:both;" data-justInfoLi="1">
											<div class="pull-left gray ps-left-text" style="text-align: right;">创建日期 ：</div>
											<div class="ticket-user pull-left ps-right-box" data-info="1">
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
		
	</body>
</html>

