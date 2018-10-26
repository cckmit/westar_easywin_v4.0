<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<title><%=SystemStrConstant.TITLE_NAME %></title>
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
	var height = $(window).height()-40;
	$("#contentBody").css("height",height+"px");
});
</script>
</head>
<body style="background-color:#fff;">
	<div class="container" style="padding: 0px 0px;width: 100%">
		<div class="row" style="margin: 0 0">
			<div class="col-lg-12 col-sm-12 col-xs-12" style="padding: 0px 0px">
				<div class="widget no-margin-bottom" style="margin-top: 0px;">
					<div class="widget-header bordered-bottom bordered-themeprimary detailHead">
						<span class="widget-caption themeprimary" style="font-size: 20px">"${userInfoT.userName}"的个人信息</span>
						<div class="widget-buttons">
							<a href="javascript:void(0)" onclick="closeWin()" title="关闭"> <i class="fa fa-times themeprimary"></i>
							</a>
						</div>
					</div>
					<!--Widget Header-->
					<!-- id="contentBody" 是必须的，用于调整滚动条高度 -->
					<div class="widget-body margin-top-40 padding-left-30" id="contentBody" style="overflow: hidden;">
						<div class="widget radius-bordered">
							<div class="row no-margin">
								<div class="col-xs-6 no-padding">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;姓名:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfoT.userName==userInfoT.email?'':userInfoT.userName }</span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;手机:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10"> ${userInfoT.movePhone  } </span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;性别:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">
														<c:choose>
															<c:when test="${empty userInfoT.gender}">
															保密
														</c:when>
															<c:when test="${userInfoT.gender==1}">
															男
														</c:when>
															<c:when test="${userInfoT.gender==0}">
															女
														</c:when>
														</c:choose>
													</span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;姓名全拼:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfoT.allSpelling}</span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;email:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10"> ${userInfoT.email } </span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;职务:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10"> ${userInfoT.job } </span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;启用状态:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">
														已
														<tags:viewDataDic type="enabled" code="${userInfoT.enabled}"></tags:viewDataDic>
													</span>
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="col-xs-6 no-padding">
									<ul class="tickets-list">
										<li class="ticket-item no-shadow margin-top-20" style="border: 0; ">
											<img src="/downLoad/userImg/${userInfoT.comId}/${userInfoT.id}?sid=${param.sid}&size=1" onload="AutoResizeImage(150,0,this,'')"></img>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;编号:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfoT.enrollNumber}</span>
												</div>

											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;生日:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfoT.birthday}</span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;座机:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfoT.linePhone}</span>
												</div>
											</div>
										</li>
										<li class="ticket-item no-shadow ps-listline margin-top-5" style="border: 0; ">
											<div class="pull-left ps-left-text" style="font-size: 15px">&nbsp;微信:</div>
											<div class="ticket-user pull-left ps-right-box">
												<div readonly="readonly" style="width: 150px;border: 1px solid #d5d5d5;">
													<span class="padding-left-10">${userInfoT.wechat}</span>
												</div>
											</div>
										</li>
									</ul>
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
