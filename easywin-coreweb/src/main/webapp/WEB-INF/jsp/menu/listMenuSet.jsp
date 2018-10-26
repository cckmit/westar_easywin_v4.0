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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
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
	$(function() {
		//设置滚动条高度
		var height = $(window).height()-50;
		$("#contentBody").css("height",height+"px");
	})
	
	function updateMenuSet(ts,menuHead,sid){
		var ckBoxs = $("#menuSetList").find(":checkbox[name='openState']:checked");
		if(ckBoxs.length==0){
			$(ts).attr("checked",true);
			return;
		}
		var openState = 0;
		if($(ts).attr("checked")){
			openState =1;
		}
		$.post("/menu/updateMenuSet",{Action:"post",sid:sid,menuHead:menuHead,openState:openState},    
				function (data){
				if(data.status=='y'){
					
				}
			},"json");
		
		
		
	}
</script>
</head>
<body>
	<div class="container" style="padding: 0px 0px;width: 100%">	
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
            	<div class="widget" style="margin-top: 0px;" >
                	<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
                        <span class="widget-caption themeprimary" style="font-size: 20px">快捷入口设置</span>
                        <div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭">
								<i class="fa fa-times themeprimary"></i>
							</a>
						</div>
                     </div><!--Widget Header-->
                     <!-- id="contentBody" 是必须的，用于调整滚动条高度 --> 
                     <div class="widget-body margin-top-40" id="contentBody" style="overflow: hidden;overflow-y:scroll;">
                     	<ul class="list-group" id="menuSetList">
                     		<c:choose>
                     			<c:when test="${not empty listMenuSet}">
                     				<c:forEach items="${listMenuSet}" var="menuSet" varStatus="vs">
                     					<li class="list-group-item">
			                           		<span class="pull-right">
			                           			<label>
													<input class="checkbox-slider slider-icon colored-blue" name="openState"
													type="checkbox" ${menuSet.opened eq 1?'checked':''} onclick="updateMenuSet(this,${menuSet.menuHead},'${param.sid }')">
													<span class="text"></span>
												</label>
			                           		</span>${menuSet.menuName}
			                           	</li>
                     				</c:forEach>
                     			</c:when>
                     		</c:choose>
                          </ul> 
                     </div>
					
				</div>
			</div>
		</div>
	</div>
	<script src="/static/assets/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">var jq11 = $.noConflict(true);</script>
    <script src="/static/assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="/static/assets/js/beyond.min.js"></script>
</body>
</html>
