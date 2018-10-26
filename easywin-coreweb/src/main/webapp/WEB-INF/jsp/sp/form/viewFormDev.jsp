<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html>
  <head>
    <meta charset="utf-8" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    
	    <jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		
		<link rel="stylesheet" type="text/css" href="/static/plugins/ueditor/designCss/form.css" >
		<link rel="stylesheet" type="text/css" href="/static/plugins/ueditor/designCss/form-new.css" >
		
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/formview.layout.js"></script>
		<style type="text/css">
			#formLayoutContent .subTableHead .tdItem,.subTableBody .tdItem{
				display:table-cell;
				min-height:30px;
				border-left: 1px solid #000;
				border-bottom:1px solid #000;
				text-align:center;
				vertical-align: middle;
			}
			#formLayoutContent .subTableHead .tdItem:FIRST-CHILD,.subTableBody .tdItem:FIRST-CHILD{
				border-left:0;
			}
			#formLayoutContent input,textarea{
				margin:3px 0 !important;
			}
			#formLayoutContent input[type="text"] {
				border: 1px solid #DDD !important;
				height:28px !important;
				min-width:30px;
				padding:0;
			}
			.tempCrm,.tempItem{
				display:block;
				margin-top:3px;
				border-radius: 2px!important;
			    background-clip: padding-box!important;
			}
	    </style>
		<script type="text/javascript">
			var EasyWinViewForm;
			$(function(){
				EasyWinViewForm.loadFormLayout();
				
				$(document).on('input propertychange', 'textarea', function() {
		    		$(this).off("paste cut keydown keyup focus blur")
		    		.bind("paste cut keydown keyup focus blur", function() {
		    			var height, style = this.style;
		    			this.style.height = '98%';
		    			if (this.scrollHeight > 50) {
		    				height = this.scrollHeight;
		    				style.overflowY = 'hidden';
		    				style.height = height + 'px';
		    			}else{
		    				style.overflowY = 'hidden';
		    			}
		    		});
		    	});
				
				$("#viewForm").css("overflow","hidden");
				$("#viewForm").css("overflow-y","scroll");
				$("#viewForm").css("padding-top","20px");
				$("#viewForm").css("padding-bottom","20px");
				var height = $(window).height() -45;
				$("#viewForm").css("height",height + "px");
			})
		</script>
		  </head>
		  <style type="text/css">
		input[type="radio"],input[type="checkbox"] {
		    opacity: 0 !important;
		    width: 15px !important;
		    height: 15px !important;
		}
		input[type="text"]{
			border: 1px solid #000 !important;
			border-color:#000 !important;
		}
		.webuploader-pick{
			height: 30px
		}
		</style>
  <body>
		<!-- Navbar -->
	<div class="navbar" id="headDiv">
		<div class="navbar-inner">
			<div class="navbar-container">
				<!-- Navbar Barnd -->
				<div style="vertical-align: middle;line-height: 45px;text-align: center;">
					<span class="white pull-left" style="font-size:25px;">表单预览</span>
					<span class="white" style="font-size:18px;">表单名称:</span>
					<span class="white" id="modName" style="font-size:15px;border-bottom: 1px  solid #fff;width: 400px !important;"> 
					</span>
					
					<span class="btn pull-right margin-right-20 margin-top-5" style="font-size:15px;" onclick="window.close()">关闭</span>
				</div>
			</div>
		</div>
	</div>
	<div id="viewForm" style="width: 100%;" class="bg-white">
		
		<div style="clear:both;padding:0px 6% 0px 23%;">
			<div style="width:780px;vertical-align: middle;" id="formLayoutContent">
				
			</div>
		</div>
	</div>
	</div>
	<script type="text/javascript">
	var sid='${param.sid}'
	var EasyWin={
			"userInfo":{"userId":"${userInfo.id}",
				"id":"${userInfo.id}",
				"username":"${userInfo.userName}",
				"comId":"${userInfo.comId}",
				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
			},
			"comId":"${userInfo.comId}",
			"formModId":"${formMod.id}"
	}
	</script>
  </body>
</html>
