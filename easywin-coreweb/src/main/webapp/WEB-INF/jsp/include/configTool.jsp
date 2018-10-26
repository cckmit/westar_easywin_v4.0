<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<body>
<!--主题颜色设置按钮-->
		<div id="config-tool" class="closed">
		<a id="config-tool-cog" class="themeprimary">
		<i class="fa fa-cog"></i>
		</a>
		<div id="config-tool-options">
		    <div class="setting-container">
		        <label>
		            <input type="checkbox" id="checkbox_fixednavbar">
		            <span class="text">导航固定</span>
		        </label>
		        <label>
		            <input type="checkbox" id="checkbox_fixedsidebar">
		            <span class="text">侧边菜单导航固定</span>
		        </label>
		    </div>
		    <div class="panel-body no-padding padding-top-10">
		    	<h6>主题颜色设置</h6>
		    	<ul class="colorpicker" id="skin-changer">
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#5DB2FF;" rel="/static/assets/css/skins/blue.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#2dc3e8;" rel="/static/assets/css/skins/azure.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#03B3B2;" rel="/static/assets/css/skins/teal.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#53a93f;" rel="/static/assets/css/skins/green.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#FF8F32;" rel="/static/assets/css/skins/orange.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#cc324b;" rel="/static/assets/css/skins/pink.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#AC193D;" rel="/static/assets/css/skins/darkred.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#8C0095;" rel="/static/assets/css/skins/purple.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#0072C6;" rel="/static/assets/css/skins/darkblue.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#585858;" rel="/static/assets/css/skins/gray.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#474544;" rel="/static/assets/css/skins/black.min.css"></a></li>
                      <li><a class="colorpick-btn" href="javascript:void(0)" style="background-color:#001940;" rel="/static/assets/css/skins/deepblue.min.css"></a></li>
                  </ul>
		    </div>
		</div>
		</div>
     <!--主题颜色设置按钮 end-->
	 <script src="/static/assets/js/bootstrap.min.js"></script>
	 <script src="/static/assets/js/demo.js" type="text/javascript" charset="utf-8"></script>
	 <!--Beyond Scripts-->
	 <script src="/static/assets/js/beyond.min.js"></script>
</body>
