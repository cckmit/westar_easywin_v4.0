<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.BiaoQing"%>
<%@page import="com.westar.core.web.BiaoQingContext"%>
<%@page import="com.westar.core.web.TokenManager"%>
<%@page import="com.westar.base.util.DateTimeUtil"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script> 
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
$(function() {
	//设置滚动条高度
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
	//公开类型变换后
	$("#pubState").on("change",function(){
		var val = $(this).val();
		if(val == 0){
			$("#shareUserList").show();
		}else{
			$("#shareUserList").hide();
		}
	});
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
	
	//存档位置
	$("body").on("click","[data-dirSelectBtn='1']",function(){
		var params = {};
		params.preDirs = '0';
		params.oldPId = $("#classifyId").val();
		chooseDir('${param.sid}',params,function(dir){
			
			var parentId = dir.id;
			var dirName = dir.dirName;
			$(".dirChoose").find("span").remove();
			$("#classifyId").val(parentId); 
			var html = '<span id="dir_span_'+parentId+'" style="cursor:pointer;"'
			html +=  'class="label label-default margin-right-5 margin-bottom-5" >'+dirName+'</span>'
			$(".dirChoose").append(html)
			
		})
	})
	
});
//关闭窗口
function closeWin(){
	var winIndex = window.top.layer.getFrameIndex(window.name);
	closeWindow(winIndex);
}
/**
 * 添加附件
 */
function addFiles(callback){
	if($("#subState").val()==1){
		return;
	}
	var len = $("#listFiles_id").find("option").length;
	var classifyId = $("#classifyId").val();
	var fileDescribe = $("#fileDescribe").val();
	if(!fileDescribe){
		scrollToLocation($("#contentBody"),$('#fileDescribe'));
		layer.tips('请填写文档描述！', $('#fileDescribe'), {tips: 1});
    	return false;
	}else if(!classifyId){
		scrollToLocation($("#contentBody"),$('[data-dirSelectBtn]'));
		layer.tips('请选择存档位置！', $('[data-dirSelectBtn]'), {tips: 1});
    	return false;
	}else if(len==0){
		scrollToLocation($("#contentBody"),$('#fileListDiv'));
		layer.tips('请上传文档！', '#fileListDiv', {tips: 1});
    	$("#fileForm").attr("class","subform");
    	return false;
	}else{
    	var html = "";
    	$("#listFiles_id").find("option").each(function(i){
        	html+='\n<input type="hidden" name="listUpfiles['+i+'].id" value="'+$(this).val()+'">';
        	html+='\n<input type="hidden" name="listUpfiles['+i+'].filename" value="'+$(this).text()+'">';
        });
    	$("#shareUserList").find("option").each(function(i){
        	html+='\n<input type="hidden" name="listFileShare['+i+'].shareId" value="'+$(this).val()+'">';
        	html+='\n<input type="hidden" name="listFileShare['+i+'].useName" value="'+$(this).text()+'">';
        });
        $("#fileDiv").html(html);
	}

	//防止重复提交
	$("#fileForm").ajaxSubmit({
        type:"post",
        url:"/fileCenter/addFile?sid=${param.sid}&t="+Math.random(),
        dataType: "json",
        beforeSubmit:function (a,f,o){
        	$("#subState").val(1);
		},
        traditional :true,
        success:function(data){
        	$("#subState").val(0);
            if(data.status=='y'){
            	callback();
            }else{
	            //可以再次提交
				$("#fileDiv").html('');
            }
        },
        error:function(XmlHttpRequest,textStatus,errorThrown){
        	$("#subState").val(0);
            //可以再次提交
			showNotification(2,"系统个错误，请联系管理人员！");
        }
    });
}

</script>
</head>
<body style="background-color: #fff">
<input type="hidden" id="subState" value="0">
<form id="fileForm" class="subform"  method="post">
	<input  type="hidden" name="classifyId" id="classifyId" value=""/>
	<input type="hidden" name="moduleType" id="moduleType" value="013"/>
	<div id="fileDiv">
	</div>
	
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary padding-top-5" style="font-size: 20px">资料上传</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                      <!-- id="contentBody" 是必须的，用于调整滚动条高度 -->           
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<div class="widget radius-bordered">
                        	<div class="widget-header bg-bluegray no-shadow">
                            	<span class="widget-caption blue"><font color="red">*</font>文档描述</span>
                             </div>
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white">
									<textarea  id="fileDescribe" name="fileDescribe" 
									style="height: 60px;color: #000;" class="form-control"></textarea>
                                </div>
                                <div class="pull-left blue padding-top-20 padding-right-10 dirChoose">
										<font color="red">*</font>存档位置:
									</div>
									<div class="ticket-user  ps-right-box" style="height: auto;">
										<div class="pull-left gray ps-left-text padding-top-10">
										<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-5" data-dirSelectBtn="1" >
										<i class="fa fa-plus"></i>存档位置</a>
										</div>
									</div>
                            </div>
                          </div>
                     	
                          <div class="widget radius-bordered" style="clear:both">
                               <div class="widget-header bg-bluegray no-shadow">
                                   <span class="widget-caption blue"><font color="red">*</font>附件</span>
                               </div>
                               <div class="widget-body no-shadow" id="fileListDiv">
                               		<tags:uploadMore name="listFiles.id" ifream="" showName="filename" width="600px"></tags:uploadMore>
                               </div>
                           </div>
	                        <div class="widget radius-bordered">
	                        	<div class="widget-header bg-bluegray no-shadow">
	                            	<span class="widget-caption blue">分享范围</span>
	                             </div>
	                            <div class="widget-body no-shadow">
	                             	<div class="tickets-container bg-white">
										<li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text" style="text-align: right;">
											    	<font color="red">*</font>阅读权限：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												  	<div class="pull-left">
												  		<select class="populate" id="pubState" name="pubState">
							 								<option value="1" selected>公开</option>
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
	                                </div>
	                            </div>
	                          </div>
                           <div class="widget-body no-shadow">
                            </div> 
                        </div>
					
					</div>
				</div>
			</div>
		</div>								
									
</form>
</body>
</html>
