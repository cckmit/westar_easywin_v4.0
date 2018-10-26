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
<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
<script type="text/javascript">
//开始一对一聊天
function toChat(busId,busType,userId,chatType){
	var win = art.dialog.open.origin;
	art.dialog.close();
	win.one2OneChat(busId,busType,'${userInfo.id}',userId,chatType,'${param.sid}');
}
</script>
<style type="text/css">
	.view-input{width:180px}
</style>
</head>
<body style="background-color:#fff;">
<section id="container">
<!--任务按钮-->
		<!--nav-tabs style 1-->
		<div class="tc-tabs">
		<header class="panel-heading">
			<span>${userInfoT.userName}的个人信息</span>
			<!--人员信息不是自己，没有模块主键的聊天不存在 -->
			<c:choose>
				<c:when test="${userInfoT.id!=userInfo.id && ( (busId==0 && busType=='0') ||busId!='0')}">
					<c:if test="${busId=='0' && busType=='0'}">
						<button type="button" onclick="toChat('${busId}','${busType}','${userInfoT.id}',0)" style="float:right">立即沟通</button>
					</c:if>
					<c:if test="${busId!='0' && (busType=='003'||busType=='004'||busType=='005'||busType=='006'||busType=='050'||busType=='011'||busType=='012'||busType=='1')}">
						<button type="button" onclick="toChat('${busId}','${busType}','${userInfoT.id}',0)" style="float:right">立即沟通</button>
					</c:if>
				</c:when>
			</c:choose>
		</header>
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="panel-body">
					<div class="ws-introduce2">
						<span>姓名：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >${userInfoT.userName==userInfoT.email?'':userInfoT.userName}</div>
						</div>
						<span>所在部门：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >
								${userInfoT.depName}
							</div>
						</div>
						<div class="ws-clear"></div>
					</div>
					<div class="ws-introduce2">
						<span>手机：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >
								${empty userInfoT.movePhone?'未填':userInfoT.movePhone}
							</div>
						</div>
						<span>座机：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >
								${empty userInfoT.linePhone?'未填':userInfoT.linePhone}
							</div>
						</div>
						<div class="ws-clear"></div>
					</div>
					<div class="ws-introduce2">
						<span>生日：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >${userInfoT.birthday}</div>
						</div>
						<span>性别：</span>
						<div class="ws-form-group">
						<c:set var="genderName">
							<c:choose>
								<c:when test="${empty userInfoT.gender}">保密</c:when>
								<c:when test="${userInfoT.gender==1}">男</c:when>
								<c:when test="${userInfoT.gender==0}">女</c:when>
							</c:choose>
						</c:set>
							<div class="form-control view-input" readonly="readonly" >${genderName}</div>
						</div>
						<div class="ws-clear"></div>
					</div>
					<div class="ws-introduce2">
						<span>email：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >${userInfoT.email }</div>
						</div>
						<span>微信：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >
								${empty userInfoT.wechat?'未填':userInfoT.wechat}
							</div>
						</div>
						<div class="ws-clear"></div>
					</div>
					<div class="ws-introduce2">
						<span>职务：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >
								${empty userInfoT.job?'未填':userInfoT.job}
							</div>
						</div>
						<span>积分：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >${userInfoT.jifenScore}分</div>
						</div>
						<div class="ws-clear"></div>
					</div>
					<div class="ws-introduce2">
						<span>启用状态：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >
								<tags:viewDataDic type="enabled" code="${userInfoT.enabled}"></tags:viewDataDic>
							</div>
						</div>
						<span>系统身份：</span>
						<div class="ws-form-group">
							<div class="form-control view-input" readonly="readonly" >
								<c:choose>
									<c:when test="${userInfoT.admin==1}">超级管理员</c:when>
									<c:when test="${userInfoT.admin==2}">普通管理员</c:when>
									<c:when test="${userInfoT.admin==0}">普通成员</c:when>
								</c:choose>
							</div>
						</div>
						<div class="ws-clear"></div>
					</div>
					<div class="ws-introduce2">
						<div style="clear: both;margin-left: 250px">
			 				<img src="/downLoad/userImg/${userInfoT.comId}/${userInfoT.id}?sid=${param.sid}&size=1"  onload="AutoResizeImage(100,0,this,'')"></img>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--nav-tabs style 2-->
<!--main content end-->
</section>
</body>
</html>
