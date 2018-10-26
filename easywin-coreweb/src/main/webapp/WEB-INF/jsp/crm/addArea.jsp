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
	$(document).ready(function(){
		$("#stagedName").focus();
	});
	//关闭窗口
	function closeWin(){
		var winIndex = window.top.layer.getFrameIndex(window.name);
		closeWindow(winIndex);
	}
	
	var regE = /^\d+$/
	function formSub(){
		if($("#subState").val()==1){
			return false;
		}
		var flag = false;
		$("#areaForm").ajaxSubmit({
		        type:"post",
		        url:"/crm/addArea?sid=${param.sid}&t="+Math.random(),
		        dataType: "json",
		        async: false,
		        beforeSubmit:function (a,f,o){
		        	var areaName = $("#areaName").val();
		        	if(strIsNull(areaName)){
		        		 layer.tips('请填写区域名称', "#areaName", {tips: 1});
		        		return false;
		        	}
		        	var areaOrder = $("#areaOrder").val();
		        	if(strIsNull(areaOrder) || !regE.test(areaOrder)){
		        		 layer.tips('请填写区域排序', "#areaOrder", {tips: 1});
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
</script>
</head>
<body>

<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">添加区域</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
					<input type="hidden" id="subState" value="0">
					<div class="widget radius-bordered" style="margin-bottom: 0px">
				         	<div class="widget-header bg-bluegray no-shadow">
				             	<span class="widget-caption blue">区域信息</span>
				             </div>
				             <div class="widget-body no-shadow">
				             	<div class="tickets-container bg-white" style="padding: 0">
									<ul class="tickets-list">
										<form class="subform" id="areaForm">
											<input type="hidden" name="parentId" value="${area.parentId}"/>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;区域名称
												</div>
												<div class="ticket-user pull-left ps-boxinput">
													<input class="colorpicker-default form-control" style="width: 300px" type="text" id="areaName" name="areaName" />
												</div>               
					                         </li>
											<li class="ticket-item no-shadow ps-listline">
												<div class="pull-left gray ps-left-text">
													&nbsp;区域排序
												</div>
												<div class="ticket-user pull-left ps-boxinput">
													<input class="colorpicker-default form-control" style="width: 300px" type="text" id="areaOrder" name="areaOrder" value="${area.areaOrder}"/>
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
