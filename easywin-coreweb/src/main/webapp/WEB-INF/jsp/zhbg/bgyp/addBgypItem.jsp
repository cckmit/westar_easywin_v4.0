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
		
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<script type="text/javascript">
		var sid='${param.sid}';
		
		$(function(){
			//添加办公用品条目
			$("body").on("click","#addBgypItem",function(){
				optBgypItem();
			})
			//返回上一页
			$("body").on("click","#returnBack",function(){
				window.location.replace('${param.redirectPage}');
			})
			
			//控制输入
			$("body").on("keypress",".tabYpApply input",function(event){
				 var eventObj = event || e;
		         var keyCode = eventObj.keyCode || eventObj.which;
		         
				if($(this).parents("td").hasClass("tabYpApply")){
			         if ((keyCode >= 48 && keyCode <= 57)){
			        	 return true;
			         }
			         else
			             return false;
				}
			})
			//控制输入
			$("body").on("keydown",".storeNumDiv input",function(event){
				 var eventObj = event || e;
		         var keyCode = eventObj.keyCode || eventObj.which;
		         if (((event.keyCode>=48 && event.keyCode<=57)
		        		 ||(event.keyCode>=96 && event.keyCode<=105))){
		        	 return true;
		         }else if(event.keyCode == 8){
		        	 return true;
		         }else
		             return false;
			})
			
			//禁止粘贴
			$("body").bind("paste",".storeNumDiv input",function(event){
				return false;
			})
			
			//减少
			$("body").on("click",".minusNum",function(){
				var applyNumStr = $(this).parents(".storeNumDiv").find("input").val();
				if(applyNumStr){
					var applyNum = parseInt(applyNumStr);
					if(applyNum > 0){
						applyNum = applyNum-1;
						$(this).parents(".storeNumDiv").find("input").val(applyNum)
					}
				}else{
					$(this).parents(".storeNumDiv").find("input").val(0)
				}
			});
			//添加
			$("body").on("click",".addNum",function(){
				var applyNumStr = $(this).parents(".storeNumDiv").find("input").val();
				if(applyNumStr){
					var applyNum = parseInt(applyNumStr);
					applyNum = applyNum+1;
					$(this).parents(".storeNumDiv").find("input").val(applyNum)
				}else{
					$(this).parents(".storeNumDiv").find("input").val(1)
				}
			})
		})
		
		//添加办公分类
		function optBgypItem(callback){
			if($("#subState").val()==1){
				return false;
			}
			//分类号验证
			var bgypCode = $("#bgypCode").val();
			if(!bgypCode){
				layer.tips("请填写代码","#bgypCode",{tips:1});
				return false;
			}
			//分类号验证
			var bgypName = $("#bgypName").val();
			if(!bgypName){
				layer.tips("请填写用品名称","#bgypName",{tips:1});
				return false;
			}
			//分类号验证
			var bgypUnit = $("#bgypUnit").val();
			if(!bgypUnit){
				layer.tips("请选择用品单位","#bgypUnit",{tips:1});
				return false;
			}
			var result = false;
			 //异步提交表单
		    $("#bgypItemForm").ajaxSubmit({
		        type:"post",
		        url:"/bgypItem/ajaxAddBgypItem?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async:false,
		        beforeSubmit:function (a,f,o){
		        	$("#subState").val(1)
				}, 
				traditional :true,
		        success:function(data){
			         var status = data.status;
			         if(status=='y'){
			        	 window.location.replace("${param.redirectPage}");
				     }else{
				    	 layer.tips(data.info,"#flCode",{tips:1});
				     }
		        },
		        error:function(XmlHttpRequest,textStatus,errorThrown){
		        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
		        	result = false;
		        	
		        }
		    });
		    $("#subState").val(0)
		    
		    
		}
		
		
		</script>
		<style type="text/css">
			.online-list{cursor: pointer;}
		</style>
	</head>
	<body onload="resizeVoteH('rightFrame')">
		<input type="hidden" id="subState" value="0">
		<div  style="width: 100%">	
			<div id="contentBody" class="widget-body " style="overflow-y:auto;position: relative;">
				<div class="widget radius-bordered no-padding-bottom no-margin-bottom">
                      	<div class="widget-header bg-bluegray no-shadow">
                          	<span class="widget-caption blue">新增办公用品条目</span>
                           </div>
                          <div class="widget-body no-shadow">
                           	<div class="tickets-container bg-white" >
							<ul class="tickets-list">
								<form class="subform" id="bgypItemForm">
									<input type="hidden" name="redirectpage">
                                	<li class="ticket-item no-shadow ps-listline">
									    <div class="pull-left gray ps-left-text">
											所属分类：
									    </div>
										<div class="ticket-user pull-left ps-right-box">
											<input type="hidden" name="flId" id="flId" value="${bgypItem.flId }">
											<span id="pFlName">${bgypItem.flName}</span>
										</div>
									</li>
                                 	<li class="ticket-item no-shadow ps-listline">
									    <div class="pull-left gray ps-left-text">
									    <span style="color:red">*</span>
											用品代码：
									    </div>
										<div class="ticket-user pull-left ps-right-box">
										 <input class="colorpicker-default form-control" type="text" id="bgypCode" 
										 style="width: 300px;" name="bgypCode"  value=""/>
										</div>
									</li>
                                  	<li class="ticket-item no-shadow ps-listline">
									    <div class="pull-left gray ps-left-text">
									    <span style="color:red">*</span>
											用品名称：
									    </div>
										<div class="ticket-user pull-left ps-right-box">
										 <input class="colorpicker-default form-control" type="text" id="bgypName" 
										 style="width: 300px;" name="bgypName"  value=""/>
										</div>
									</li>
                                  	<li class="ticket-item no-shadow ps-listline">
									    <div class="pull-left gray ps-left-text">
									    <span style="color:red">*</span>
											用品库存：
									    </div>
										<div class="ticket-user pull-left ps-right-box storeNumDiv">
										
											<div class="spinner spinner-horizontal spinner-two-sided pull-left">
												<div class="spinner-buttons	btn-group spinner-buttons-left">
													<button type="button" class="btn spinner-down danger minusNum">
														<i class="fa fa-minus"></i>
													</button>
												</div>
												<input name="storeNum" id="storeNum" class="spinner-input form-control number" maxlength="10" type="text" value="0">
												<div class="spinner-buttons	btn-group spinner-buttons-right">
													<button type="button" class="btn spinner-up blue addNum">
														<i class="fa fa-plus"></i>
													</button>
												</div>
											</div>
										</div>
									</li>
                                	<li class="ticket-item no-shadow ps-listline">
									    <div class="pull-left gray ps-left-text">
									    <span style="color:red">*</span>
											用品单位：
									    </div>
										<div class="ticket-user pull-left ps-right-box">
										 <tags:dataDic type="bgypUnit" name="bgypUnit" id="bgypUnit" please="t"></tags:dataDic>
										</div>
									</li>
                                  	<li class="ticket-item no-shadow ps-listline">
									    <div class="pull-left gray ps-left-text">
											用品规格：
									    </div>
										<div class="ticket-user pull-left ps-right-box">
										 <input class="colorpicker-default form-control" type="text" id="bgypSpec" 
										 style="width: 300px;" name="bgypSpec"  value=""/>
										</div>
									</li>
                                  	<li class="ticket-item no-shadow ps-listline">
	                                  	<div style="text-align: center;width: 60%">
										    <a href="javascript:void(0);" class="btn btn-primary margin-top-5 margin-right-20" id="addBgypItem">新增</a>
										    <a href="javascript:void(0);" class="btn margin-top-5" id="returnBack">返回</a>
	                                  	</div>
									</li>
								</form>
							</ul>
							
						</div>
					</div>	
				</div>				
     
			</div>
		</div>
		<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
	</body>
</html>

