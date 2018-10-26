<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
var index = window.top.layer.getFrameIndex(window.name); //获取窗口索引

//打开页面body
var openWindowDoc;
//打开页面,可调用父页面script
var openWindow;
//注入父页面信息
function setWindow(winDoc,win){
	openWindowDoc = winDoc;
	openWindow = win;
}

function addTypeSelect(type){
	// 如果父页面重载或者关闭其子对话框全部会关闭
	openWindow.addType(type);
	window.top.layer.close(index);
}
</script>
<style type="text/css">
li{
cursor: pointer;
}
</style>
</head>
<body style="background-color: #fff">
<div class="container no-padding" style="width: 100%">	
	<div class="row" >
		<div class="col-lg-12 col-sm-12 col-xs-12" >
			<div class="widget" style="border-bottom: 0px">
	        	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
	             	<span class="widget-caption themeprimary ps-layerTitle">数据类型选择</span>
                    <div class="widget-buttons">
						<a href="javascript:void(0)" id="titleCloseBtn" title="关闭">
							<i class="fa fa-times themeprimary"></i>
						</a>
					</div>
	             </div>
				<div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
					<div class="well no-padding-buttom no-margin-buttom">
			         <div class="dd dd-draghandle bordered">
			             <ol class="dd-list">
			                 <li class="dd-item dd2-item" onclick="addTypeSelect('newFolder');">
			                     <div class="dd-handle dd2-handle">
			                         <i class="normal-icon glyphicon glyphicon-folder-open"></i>
			                     </div>
			                     <div class="dd2-content">添加文件夹</div>
			                 </li>
			
			                 <li class="dd-item dd2-item" onclick="addTypeSelect('newTask');">
			                     <div class="dd-handle dd2-handle">
			                         <i class="normal-icon fa fa-flag "></i>
			
			                     </div>
			                     <div class="dd2-content">发布任务</div>
			                 </li>
			                 <li class="dd-item dd2-item" onclick="addTypeSelect('newFile');">
			                     <div class="dd-handle dd2-handle">
			                         <i class="normal-icon fa fa-file "></i>
			
			                     </div>
			                     <div class="dd2-content">上传文件</div>
			                 </li>
			             </ol>
			         </div>
			     </div>
     
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
