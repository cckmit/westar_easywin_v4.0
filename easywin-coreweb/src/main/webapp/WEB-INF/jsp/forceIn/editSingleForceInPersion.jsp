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
		<script type="text/javascript" src="/static/js/forceInJs/forceIn.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<script type="text/javascript">
		var showTabIndex = 0;
		var sid='${param.sid}';
		
		//关闭窗口
		function closeWin(){
			var winIndex = window.top.layer.getFrameIndex(window.name);
			closeWindow(winIndex);
		}
		
		$(function(){
			var busType = '${busType}';
			
			var title = busType=='003'?"任务中心":busType=='005'?"项目中心":busType=='006'?"周报中心"
				:busType=='012'?"客户中心":busType=='022'?"审批中心":busType=='033'?"差旅管理":busType=='050'?"分享中心":"";
			title = "\""+title+"\"监督人员配置";
			$(".ps-layerTitle").html(title);
			//设置滚动条高度
			var height = $(window).height()-40;
			$("#contentBody").css("height",height+"px");
		})

		
		</script>
		<style type="text/css">
			.online-list{cursor: pointer;}
		</style>
	</head>
	<body >
		<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
           	<span class="widget-caption themeprimary ps-layerTitle"></span>
                 <div class="widget-buttons">
			<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
				<i class="fa fa-times themeprimary"></i>
			</a>
		</div>
      </div>
      <div id="contentBody" class="widget-body margin-top-40" style="overflow-y:auto;position: relative;">
			<div class="widget-body no-shadow no-padding padding-top-5">
               	<div class="widget-main ">
                   	<div class="tabbable">
                           <div class="tabs-flat">
                           
                           	<div class="tab-pane" id="userDepDiv">
								<div class="widget-header bg-bluegray no-shadow" style="clear:both">
									<span class="widget-caption blue">监督人员设定</span>
									<span class="widget-caption red">
							           <small class="ws-active ws-color">(双击移除)</small>
							        </span>
									<div class="widget-buttons btn-div-full">
									</div>
								</div>
								<div class="widget-body no-shadow">
									<div class="online-list" style="cursor: auto;">
										<div style="float: left; width: 250px;display:none;">
											<select id="forceInSelect_${busType}" busType="${busType}" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
											</select>
										</div>
										<div id="forceInDiv_${busType}" class="pull-left forceIn" busType="${busType}" >
											<a href="javascript:void(0);" class="btn btn-primary btn-xs margin-top-10 margin-bottom-5 margin-left-5 userSetBtn" title="人员多选" 
											 style="float: left;"><i class="fa fa-plus"></i>选择</a>
										</div>
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

