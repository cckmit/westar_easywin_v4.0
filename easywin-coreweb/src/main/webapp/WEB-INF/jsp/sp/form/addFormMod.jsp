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
<html>
  <head>
   	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<title><%=SystemStrConstant.TITLE_NAME%></title>
	<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
	<script type="text/javascript">
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	
	function addFormMod(isCloude){
		var modName = $("#modName").val();
		if(strIsNull(modName)){
			layer.tips("请填写表单名称","#modName",{tips:1})
			return false;
		}
		
		if($("#subState").val()==1){
			return false;
		}
		if(isCloude && isCloude=='true'){
    		$("#comId").val(0);
    	}else{
    		$("#comId").val('');
    	}
		var formType = $("#formType").find("option:selected").val();
		var result;
		 //异步提交表单
	    $("#modForm").ajaxSubmit({
	        type:"post",
	        url:"/form/addFormMod?sid=${param.sid}&t="+Math.random(),
	        dataType: "json",
	        async:false,
	        beforeSubmit:function (a,f,o){
	        	$("#subState").val(1);
			},
	        success:function(data){
		         var state = data.status;
		         if(state=='y'){
		        	 result={"id":data.id,"formType":formType};
			     }
	        },
	        error:function(XmlHttpRequest,textStatus,errorThrown){
	        	window.top.layer.alert("系统错误，请联系管理人员",{icon:7,title:false,closeBtn:0,btn:["关闭"]})
	        	return false;
	        }
	        });
		$("#subState").val(0)
		return result;
	}
	</script>
  </head>
  
  <body>
   	<input type="hidden" id="subState" value="0">
	<form class="subform" id="modForm">
	<input type="hidden" name="sid" value="${param.sid}"/>
	<input type="hidden" name="comId" id="comId"/>
	<div class="container no-padding" style="width: 100%">	
		<div class="row" >
			<div class="col-lg-12 col-sm-12 col-xs-12" >
            	<div class="widget" style="margin-top: 0px;">
	            	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	                	<span class="widget-caption themeprimary" style="font-size: 20px">新增表单</span>
	                       <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
	                   </div><!--Widget Header-->
                   
            		<div class="widget-body margin-top-40" style="height: 360px;overflow-y:auto;position: relative;">
                     	<div class="widget radius-bordered">
                            <div class="widget-body no-shadow">
                             	<div class="tickets-container bg-white" >
									<ul class="tickets-list">
                                         <li class="ticket-item no-shadow ps-listline">
										    <div class="pull-left gray ps-left-text">
												表单名称：
										    </div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control ps-pointer" 
												type="text"  style="width: 300px;float: left" id="modName"
												name="modName" />
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline ps-clear" >
									    	<div class="pull-left gray ps-left-text">
									    		表单说明
									    	</div>
											<div class="ticket-user pull-left ps-right-box">
												<input class="colorpicker-default form-control ps-pointer" 
												type="text"  style="width: 300px;float: left" 
												name="modDescrib" />
											</div>
                                         </li>
                                         
                                         <li class="ticket-item no-shadow ps-listline ps-clear">
										    <div class="pull-left gray ps-left-text">
										    	表单分类
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <select class="populate"  id="formSortId" name="formSortId" style="cursor:auto;width: 200px">
													<option value="0">其他</option>
													<c:choose>
														<c:when test="${not empty formSortList}">
							 								<c:forEach items="${formSortList}" var="obj" varStatus="status">
							 								<option value="${obj.id}">${obj.sortName}</option>
							 								</c:forEach>
														</c:when>
													</c:choose>
												</select>
											</div>               
                                           </li>
                                           
                                           <li class="ticket-item no-shadow ps-listline ps-clear">
										    <div class="pull-left gray ps-left-text">
										    	表单类型
										    </div>
											<div class="ticket-user pull-left ps-right-box">
											   <select class="populate"  id="formType" style="cursor:auto;width: 200px">
													<option value="1">simple</option>
													<option value="0">uedior</option>
												</select>
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
     </div>
</form>
  </body>
</html>
