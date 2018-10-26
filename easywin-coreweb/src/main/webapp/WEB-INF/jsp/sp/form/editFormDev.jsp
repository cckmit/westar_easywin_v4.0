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
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	    
		<jsp:include page="/WEB-INF/jsp/include/static_assets.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		
		<link rel="stylesheet" type="text/css" href="/static/plugins/ueditor/designCss/form.css" >
		<link rel="stylesheet" type="text/css" href="/static/plugins/ueditor/designCss/form-new.css" >
		
		<script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/ueditor.config.js"></script>
	    <script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/ueditor.all.js"> </script>
	    <script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/formdesign.js"> </script>
	    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
	    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
	    <script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/lang/zh-cn/zh-cn.js"></script>
	    <script type="text/javascript" charset="utf-8" src="/static/js/formJs/editFormMod.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"> </script>
	    <script type="text/javascript" charset="utf-8" src="/static/plugins/ueditor/formview.layout.js"></script>
		<link rel="stylesheet" type="text/css" href="/static/css/formControl.css" >
	    <style type="text/css">
		    #formLayoutContent input[type="text"] {
				border: 1px solid #DDD !important;
			}
			#formLayoutContent .subTableHead .tdItem,.subTableBody .tdItem{
				display:table-cell;
				min-height:30px;
				border-left: 1px solid #000;
				border-bottom:1px solid #000;
				text-align:center;
				vertical-align: middle;
			}
			
			#formLayoutContent .subTableBody .tdItem{
				padding: 5px 5px
			}
			#formLayoutContent .subTableHead .tdItem:FIRST-CHILD,.subTableBody .tdItem:FIRST-CHILD{
				border-left:0;
			}
			
			p {
			    margin: 5px 0;
			}
			.tempCrm,.tempItem{
				display:block;
				margin-top:3px;
				border-radius: 2px!important;
			    background-clip: padding-box!important;
			}
	    </style>
	    <script type="text/javascript">
	    	var easyWinEditor;
	    	var easyWinFormDesign;
	    	var EasyWinViewForm;
	    	var pageFlag = "${flag}";
	    	var sid = pageFlag && pageFlag == 'web'?null:"${param.sid}";
	    	var EasyWin={
	    			"userInfo":{
	    				"id":"${userInfo.id}",
	    				"username":"${userInfo.userName}",
	    				"department":{"name":"${userInfo.depName}","id":"${userInfo.depId}"}
	    			},
	    			"comId":"${userInfo.comId}",
	    			"formModId":"${param.formModId}",
	    			"sid":sid
	    	}
	    	
	    	$(function(){
	    		
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
		    	$("#viewForm").css("height",height + "px")
	    	})
	    	
	    	
	    </script>
  </head>
  
  <body>
  
  <!-- Navbar -->
	<div class="navbar" id="headDiv">
		<div class="navbar-inner">
			<div class="navbar-container">
				<!-- Navbar Barnd -->
				<div style="vertical-align: middle;line-height: 45px;text-align: center;">
					<span class="white pull-left" style="font-size:25px;">表单编辑</span>
					<span class="white" style="font-size:18px;">表单名称:</span>
					<span class="white" style="font-size:15px;border-bottom: 1px  solid #fff"> 
						<input type="text" class="white" id="modName" style="width: 400px !important;">
					</span>
					
					<span class="btn pull-right margin-right-20 margin-top-5" style="font-size:15px;" id="viewBtn">预览</span>
					<span class="btn pull-right margin-right-10 margin-top-5" style="font-size:15px;" id="saveBtn">保存</span>
					<span class="btn pull-right margin-right-10 margin-top-5" style="font-size:15px;" id="backBtn">返回</span>
					<span class="btn pull-right margin-right-20 margin-top-5" style="font-size:15px;" id="rollBtn">恢复</span>
				</div>
			</div>
		</div>
	</div>
	<div class="form-wrap" id="editForm">
	    <!-- todo 左侧自适应-->
	    <div class="form-self">
	        <div class="form-left">
	            <div class="form-left-pd">
	                <div class="mode-box no-margin-bottom">
		                <div id="preFormComponent" style="display: none">
							<div class="mode-top">
							
							</div>
							<div class="mode-body">
								<div class="mode-title">
									原有控件  <a href="javascript:void(0)" id="refreshList">刷新</a>
							    </div>
								<ul class = "form-style form-style-new componPreList bg-white">
								
								</ul>
							</div>
						</div>
	                    <div class="mode-top"></div>
	                    <div class="mode-body">
	                        <div class="mode-title">字段控件</div>
	                        <ul class="form-style componList">
	                            <li componType="Text">
									<p class="fa fa-pencil form-icon"></p>
									<p class="form-text">文本框</p>
								</li>
								<li componType="TextArea">
									<p class="fa fa-align-center form-icon"></p>
									<p class="form-text">多行文本</p>
								</li>
								<li componType="RadioBox">
									<p class="fa fa-dot-circle-o form-icon"></p>
									<p class="form-text">单选框</p>
								</li>
								<li componType="CheckBox">
									<p class="fa fa-check-square-o form-icon"></p>
									<p class="form-text">复选框</p>
								</li>
								<li componType="Select">
									<p class="fa fa-caret-square-o-down form-icon"></p>
									<p class="form-text">下拉菜单</p>
								</li>
								<li componType="DateComponent">
									<p class="fa fa-calendar form-icon"></p>
									<p class="form-text">日期</p>
								</li>
								<li componType="DateInterval">
									<p class="fa fa-calendar-o form-icon"></p>
									<p class="form-text">日期区间</p>
								</li>
								<li componType="NumberComponent">
									<p class="fa fa-sort-numeric-desc form-icon"></p>
									<p class="form-text">数字输入框</p>
								</li>
								<li componType="MoneyComponent">
									<p class="fa fa-cny form-icon"></p>
									<p class="form-text">大写金额</p>
								</li>
								<li componType="Monitor">
									<p class="fa fa-gears form-icon"></p>
									<p class="form-text">运算控件</p>
								</li>
								<li componType="Employee">
									<p class="fa fa-group form-icon"></p>
									<p class="form-text">用户选择</p>
								</li>
								<li componType="Department">
									<p class="fa fa-sitemap form-icon"></p>
									<p class="form-text">部门选择</p>
								</li>
								<li componType="RelateItem">
									<p class="fa fa-link form-icon"></p>
									<p class="form-text">项目选择</p>
								</li>
								<li componType="RelateCrm">
									<p class="fa fa-link form-icon"></p>
									<p class="form-text">客户选择</p>
								</li>
								<li componType="RelateMod">
									<p class="fa fa-link form-icon"></p>
									<p class="form-text">关联模块</p>
								</li>
								<li componType="SerialNum">
									<p class="fa fa-gears form-icon"></p>
									<p class="form-text">序列编号</p>
								</li>
								<li componType="RelateFile">
									<p class="fa fa-file form-icon"></p>
									<p class="form-text">资料上传</p>
								</li>
								<li componType="DataTable">
									<p class="fa fa-clipboard form-icon"></p>
									<p class="form-text">明细子表</p>
								</li>
	                        </ul>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	    <!--todo 右侧固定-->
	    <div class="form-fixed">
	        <div class="form-fixed-pd clearfix">
	            <div class="form-plug pull-left">
		            <div id="middlePlug" style="border: 1px solid #ddd;">
		            	<script id="editor" type="text/plain" style="width:787px;"></script>
		            </div>
	            </div>
	            <div class="form-redact pull-left">
	                <div class="right-mode-box">
	                    <div class="right-mode-top">
	                        <div>表单设置</div>
	                    </div>
	                    <div class="right-mode-body" id="formConfDiv">
	                      
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	
	<div id="viewForm" style="width: 100%;" class="bg-white">
		
		<div style="clear:both;padding: 0px 6% 0px 23%;">
			<div style="width:780px;vertical-align: middle;" id="formLayoutContent">
				
			</div>
		</div>
	</div>
  </body>
</html>
