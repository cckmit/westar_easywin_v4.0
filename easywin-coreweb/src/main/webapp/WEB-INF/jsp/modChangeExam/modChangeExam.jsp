<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
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
	var valid;
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	var type = '${param.moduleType}';
	$(function() {
		valid = $(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			callback:function (form){
				//提交前验证是否在上传附件
				return sumitPreCheck(null);
			},
			showAllError : true
		});
		
		var title = "";
		if(type=='012'){
			title = "客户属性变更审批配置";
		}
		$(".ps-layerTitle").html(title);
		
		//设置滚动条高度
		var height = $(window).height()-40;
		$("#contentBody").css("height",height+"px");
		
		
		//查询相关配置
		$.ajax({
      		type : "post",
      		url : "/moduleChangeExamine/ajaxGetModChangeExam?sid="+'${param.sid}' + "&moduleType=" + type,
      		dataType:"json",
      		traditional :true, 
      		data:null,
      		  beforeSend: function(XMLHttpRequest){
               },
      		  success : function(data){
      			  if(data.status == "y"){
      				  if(data.lists && data.lists[0]){
      					 for (var i = 0; i < data.lists.length; i++) {
      						 if(type=='012'){//客户
      							 if(data.lists[i].field == "customerTypeId"){
      								 $("#customerTypeId").attr("checked",true);
      							 }else if(data.lists[i].field == "owner"){
      								 $("#owner").attr("checked",true);
      							 }else if(data.lists[i].field == "stage"){
      								 $("#stage").attr("checked",true);
      							 }
      						 }
						}				  
      				  }
      			  }
      		  },
      		  error:  function(XMLHttpRequest, textStatus, errorThrown){
      			  window.top.layer.msg('系统错误，请联系管理人员', {icon:2});
      	      }
      	}); 
		
	})
	function formSub(){
		$(".subform").submit();
	}
</script>
<style type="text/css">
	body:before {
		background-color:#fff;
		z-index: 0
	}
</style>
</head>
<body>
	<div class="container no-padding" style="width: 100%">	
		<div class="widget" style="border-bottom: 0px">
		        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
		             	<span class="widget-caption themeprimary ps-layerTitle"></span>
	                    <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
		             </div>
					<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
						<form class="subform" method="post" action="/moduleChangeExamine/updateExamConfig">
								<tags:token></tags:token>
								<input type="hidden" name="moduleType" value="${moduleType}">
								<div class="checkbox no-margin bg-white">
		                        	<c:choose>
										<c:when test="${moduleType == 012}">
											<label class="no-padding" style="margin-top:10px;">
												<input id="owner" name="fields" value="owner" type="checkbox" />
											    <span class="text" style="margin-left:10px;">客户移交审批</span>
										    </label><br/>
											<label class="no-padding" style="margin-top:10px;">
												<input id="customerTypeId" name="fields" value="customerTypeId" type="checkbox" />
											    <span class="text" style="margin-left:10px;">客户类型变更审批</span>
										    </label><br/>
										    <label class="no-padding" style="margin-top:10px;">
												<input id="stage" name="fields" value="stage" type="checkbox" />
											    <span class="text" style="margin-left:10px;">所属阶段变更审批</span>
										    </label><br/>
										    
										</c:when>
									</c:choose>
								</div>
							</form>
					
					</div>
				</div>
	</div>

</body>
</html>
