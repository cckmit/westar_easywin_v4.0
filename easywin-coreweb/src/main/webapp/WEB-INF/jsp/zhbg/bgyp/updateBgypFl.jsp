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
		var showTabIndex = 0;
		var sid='${param.sid}';
		var busType = '${busType}';
		
		//关闭窗口
		function closeWin(){
			var winIndex = window.top.layer.getFrameIndex(window.name);
			closeWindow(winIndex);
		}
		$(function(){
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
		})
		//添加办公分类
		function optBgypFl(){
			if($("#subState").val()==1){
				return false;
			}
			//分类号验证
			var flCode = $("#flCode").val();
			if(!flCode){
				layer.tips("请填写分类编号","#flCode",{tips:1});
				return false;
			}
			//分类号验证
			var flName = $("#flName").val();
			if(!flName){
				layer.tips("请填写分类名称","#flName",{tips:1});
				return false;
			}
			
			var result;
			 //异步提交表单
		    $("#bgypFlForm").ajaxSubmit({
		        type:"post",
		        url:"/bgypFl/ajaxUpdateBgypFl?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async:false,
		        beforeSubmit:function (a,f,o){
		        	$("#subState").val(1)
				}, 
				traditional :true,
		        success:function(data){
			         var status = data.status;
			         if(status=='y'){
			        	 result = true;
				     }else{
				    	 layer.tips(data.info,"#flCode",{tips:1});
				    	 result = false;
				     }
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
		        	result = false;
		        	
		        }
		    });
		    $("#subState").val(0)
		    return result;
			
			
		}
		
		
		</script>
		<style type="text/css">
			.online-list{cursor: pointer;}
		</style>
	</head>
	<body >
		<input type="hidden" id="subState" value="0">
		<div class="container no-padding" style="width: 100%">	
			<div class="row" >
				<div class="col-lg-12 col-sm-12 col-xs-12" >
					<div class="widget" style="border-bottom: 0px">
			        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
			             	<span class="widget-caption themeprimary ps-layerTitle">修改办公用品分类</span>
		                    <div class="widget-buttons">
								<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
									<i class="fa fa-times themeprimary"></i>
								</a>
							</div>
			             </div>
						<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
							<div class="widget radius-bordered no-padding-bottom no-margin-bottom">
	                        	<div class="widget-header bg-bluegray no-shadow">
	                            	<span class="widget-caption blue">办公用品分类</span>
	                             </div>
	                            <div class="widget-body no-shadow">
	                             	<div class="tickets-container bg-white" >
										<ul class="tickets-list">
											<form class="subform" id="bgypFlForm">
											<input type="hidden"  name="id" value="${bgypFl.id}">
	                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
													所属分类：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
													<input type="hidden" name="parentId" id="parentId" value="${bgypFl.parentId }">
													<span id="pFlName">${bgypFl.parentId eq -1?'办公用品分类' :bgypFl.pFlName}</span>
												</div>
											</li>
	                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
											    <span style="color:red">*</span>
													分类编码：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												 <input class="colorpicker-default form-control" type="text" id="flCode" 
												 style="width: 300px;" name="flCode"  value="${bgypFl.flCode}"/>
												</div>
											</li>
	                                         <li class="ticket-item no-shadow ps-listline">
											    <div class="pull-left gray ps-left-text">
											    <span style="color:red">*</span>
													分类名称：
											    </div>
												<div class="ticket-user pull-left ps-right-box">
												 <input class="colorpicker-default form-control" type="text" id="flName" 
												 style="width: 300px;" name="flName"  value="${bgypFl.flName}"/>
												</div>
											</li>
											</form>
										</ul>
									</div>
								</div>	
							</div>				
        
						</div>
						
					</div>
				</div>
			</div>
		</div>
		
	</body>
</html>

