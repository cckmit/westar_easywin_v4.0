<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script>
$(function(){
	$(".subform").Validform({
		tiptype : function(msg, o, cssctl) {
			validMsg(msg, o, cssctl);
		},
		showAllError : true
	});
	
});

//设置访问成功后的跳转地址
function setLocation(){
	$("#redirectPage").val(window.self.location);
	
	var autoEject = '${userInfo.autoEject}';
	if(autoEject==0){//不弹窗
		sessionStorage.setItem("ifshow",'n');
		parent.dialog.close();
	}else{//弹窗
		sessionStorage.setItem("ifshow",'y');
	}
}
</script>

</head>
<body style="background-color: #fff" onload="resizeVoteH('userCenter');setLocation();">
<div id="home" class="tab-pane active">
	<div id="tabs-1" class="panel-collapse collapse in">
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="row">
						<div class="col-sm-12">
							<form action="/userInfo/setMsgShow" method="post" class="subform">
					   		<input type="hidden" id="redirectPage" name="redirectPage"/>
					   		<input type="hidden" value="${param.sid}" name="sid" />
					   		<input type="hidden" value="${userOrganic.id}" name="id"/>
							<div class="panel-body">
								<div class="ws-introduce2">
									<span style="font-size:18px !important;width: 150px;margin-top: 3px">消息弹窗设置：</span>
									<div class="radio">
										<label class="pull-left ws-sex">
									    <input class="ws-radio" name="autoEject" value="1" type="radio"  ${userOrganic.autoEject=='1'?'checked':''}/>
									    弹窗提示</label>
										<label class="pull-left ws-sex">
									    <input class="ws-radio" name="autoEject" value="0" type="radio" ${userOrganic.autoEject=='0'?'checked':''}/>
									    不弹窗</label>
									</div>
									<div>
										<button class="btn btn-info ws-btnBlue2" style="margin-left:50px;" type="submit">保存</button>
									</div>
								</div>
							</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--nav-tabs style 2-->
	</div>
</div>
<%--用与测量当前页面的高度 --%>
<jsp:include page="/bottomdiv.jsp"></jsp:include>
</body>
</html>
