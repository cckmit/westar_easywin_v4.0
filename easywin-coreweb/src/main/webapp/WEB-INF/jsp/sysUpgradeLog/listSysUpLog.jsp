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
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<script type="text/javascript">
$(function(){
	//PC端升级日志
	$("#PCMenuLi").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$("#PCMenuLi").attr("class","active");
		$("#otherLogAttrIframe").attr("src","/sysUpLog/listSysUpLog?activityMenu=sul_m_1.1&type=0&sid=${param.sid}&tab=31");
	});
	//IOS端升级日志
	$("#IOSMenuLi").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$("#IOSMenuLi").attr("class","active");
		$("#otherLogAttrIframe").attr("src","/sysUpLog/listSysUpLog?activityMenu=sul_m_1.1&type=1&sid=${param.sid}&tab=31");
	});
	//Android端升级日志
	$("#AndroidMenuLi").click(function(){
		$(this).parent().find("li").removeAttr("class");
		$("#AndroidMenuLi").attr("class","active");
		$("#otherLogAttrIframe").attr("src","/sysUpLog/listSysUpLog?activityMenu=sul_m_1.1&type=2&sid=${param.sid}&tab=31");
	});
});
</script>
</head>
<body>
<!-- Page Content -->
	<div class="page-content">
		<!-- Page Body -->
		<div class="page-body">
			<div class="row">
				<div class="col-md-12 col-xs-12 ">
					<div class="widget">
						<div class="widget-body" id="contentBody"
							style="overflow: hidden;overflow-y:scroll;">
								<div class="widget-main ">
                                	 <div class="">
                                      	<ul class="nav nav-tabs tabs-flat" id="myTab11">
                                             <li class="active" id="PCMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">电脑版</a>
                                             </li>
                                             <li id="IOSMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">苹果版</a>
                                             </li>
                                             <li id="AndroidMenuLi">
                                                 <a data-toggle="tab" href="javascript:void(0)">安卓版</a>
                                             </li>
                                    	</ul>
                                    	<div class="tab-content tabs-flat">
                                    	 	<iframe id="otherLogAttrIframe"  class="layui-layer-load"
												src="/sysUpLog/listSysUpLog?type=0&activityMenu=sul_m_1.1&sid=${param.sid}" 
												border="0" frameborder="0" allowTransparency="true"
												noResize  scrolling="no" width=100% height=100% vspale="0" ></iframe>
                                    	 </div>
                                	</div>
                            	</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /Page Body -->
		</div>
		<!-- /Page Content -->
	</div>
</body>
</html>
