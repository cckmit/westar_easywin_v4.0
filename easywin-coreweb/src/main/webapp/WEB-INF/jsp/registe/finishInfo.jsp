<%@page import="com.westar.core.web.InitServlet"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%pageContext.setAttribute("requestURI",request.getRequestURI().replace("/","_").replace(".","_"));%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
<!-- 框架样式 -->
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="/static/plugins/Validform/css/validform.css" >
<script type="text/javascript" src="/static/plugins/Validform/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/static/js/common.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>

<script type="text/javascript" src="/static/js/jquery.form.js"></script>
<script src="/static/plugins/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$(".subform").Validform({
			tiptype : function(msg, o, cssctl) {
				validMsg(msg, o, cssctl);
			},
			showAllError : true
		});
	})
	
</script>
<style>
	dt{
		font-size: 12px
	}
	.ws-common-dl dd{
		padding-bottom: 0px
	}
	dd span{
		font-size: 12px
	}
	dd input{
		font-size: 12px;border-radius: 5px;
	}
	dd input:focus{
		border-color: #1ABC9C;box-shadow:0 0 8px rgba(38,194,155,0.6);
	}
	.pop-content{
		padding: 8px;
	}
	.pop-content dl{
		line-height: 38px
	}
	.pop-content dl dd{
		width: 75%
	}
</style>
</head>
<body>
	<!----头部开始---->
	<div class="top">
		<jsp:include page="/WEB-INF/jsp/web/head.jsp"></jsp:include>
	</div>
	 <!----头部结束---->
	
	<!--注册 S-->
    <div class="register-box">
    	<div class="flow-box">
    		<ul class="flow-menu">
    			<li><a href="javascript:void(0)"><img src="/static/images/web/flow-1.png"/></a></li>
    			<li><a href="javascript:void(0)"><img src="/static/images/web/flow-2-2.png"/></a></li>
    			<li><a href="javascript:void(0)"><img src="/static/images/web/flow-3.png"/></a></li>
    		</ul>
    	</div>
    	<div class="register-form" style="margin:50px auto;">
    		<form class="subform" method="post" action="/registe/finishInfo/${requestURI}">
				<input type="hidden" name="recordId" value="${joinRecord.id}"/>
				<input type="hidden" name="confirmId" value="${joinRecord.confirmId}"/>
				<input type="hidden" name="joinType" value="${joinRecord.joinType}"/>
				<input type="hidden" name="userId" value="${empty joinRecord.userId?0:joinRecord.userId}"/>
				<input type="hidden" name="account" value="${joinRecord.account }"/>
				<input type="hidden" name="comId" value="${joinRecord.comId }"/>
				
	    		<div class="form-group">
	    			<label>邮箱账号：</label>
	    			<div class="input-form pull-left">${joinRecord.account}</div>
	    		</div>
	    		
	    		<c:choose>
	    			<%--是注册的用户 --%>
	    			<c:when test="${0==joinRecord.joinType}">
	    				<c:if test="${status!='y'}">
	    					<div class="form-group">
		    					<label>用户姓名：</label>
		    					<div class="input-form pull-left">${userInfo.userName}</div>
		    				</div>
	    				</c:if>
	    				<div class="form-group" style="height: 42px">
			    			<label>团队名称：</label>
			    			<div class="pull-left">
				    			<input class="input-form" datatype="*" type="text" name="orgName" />
			    			</div>
			    		</div>
	    			</c:when>
	    			<c:otherwise>
	    				<div class="form-group">
	    					<label>团队名称：</label>
	    					<div class="input-form pull-left">${joinRecord.comName}</div>
	    				</div>
	    			</c:otherwise>
	    		</c:choose>
	    		
	    		
	    		<%--需要重新设置密码 --%>
	    		<c:if test="${status=='y'}">
	    			<div class="form-group" style="height: 42px">
		    			<label>用户姓名：</label>
		    			<div class="pull-left">
			    			<input class="input-form" datatype="*" name="userName" 
			    			value="${empty userName?userInfo.userName:userName}" />
		    			</div>
		    		</div>
	    			<div class="form-group" style="height: 42px">
		    			<label>设置密码：</label>
		    			<div class="pull-left">
			    			<input class="input-form" datatype="s6-18" id="password" type="password" name="password" 
			    			value="" ajaxurl="/registe/checkPassword?account=${joinRecord.account}" />
		    			</div>
		    		</div>
	    			<div class="form-group" style="height: 42px">
		    			<label>再次输入：</label>
		    			<div class="pull-left">
			    			<input class="input-form" datatype="*" type="password" recheck="password" name="password2" value="" />
		    			</div>
		    		</div>
		    		
	    		</c:if>
	    		
	    		<div class="form-group">
	    			<label>验证码：</label>
	    			<div class="pull-left" style="height: 42px">
		    			<input class="input-form" type="text" id="yzm" name="yzm" datatype="*" datatype="*" 
		    			ajaxurl="/registe/checkYzm/${requestURI}" style="width:250px;" />
		    			<img class="yzm" src="/registe/AuthImage/${requestURI}" id="yz" style="margin-left: 15px"
		    			onclick="this.src='/registe/AuthImage/${requestURI}?rnd=' + Math.random();$('#yzm').val('')"/>
	    			</div>
	    		</div>
	    		<a href="javascript:void(0)" class="next-btn" onclick="$('.subform').submit()">完成</a>
    		</form>
    	</div>
    </div>
    <!--注册 E-->
	<!--登录框 E-->
	<div class="bottom">
		<jsp:include page="/WEB-INF/jsp/web/bottom.jsp"></jsp:include>
	</div>
</body>

</html>
