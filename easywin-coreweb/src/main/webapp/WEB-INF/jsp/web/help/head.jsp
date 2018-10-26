<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.base.util.CommonUtil"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<script type="text/javascript">
<!--
	$(function() {
		//定时器
		var addMoreBusLiClock;

		//鼠标移动到快捷添加
		$("#addMoreBusLi").mouseover(function(e) {
			if (!$("#addMoreBusLi").hasClass("open")) {
				$("#addMoreBusLi").addClass("open");
			}
			//清除定时器
			clearTimeout(addMoreBusLiClock);
			//关闭其他的
			$("#headMenuLi").removeClass("open");
			$("#busMsgLi").removeClass("open");
			$("#menuUserLi").removeClass("open");
			$("#menuOrgDiv").removeClass("open");

		})
		//鼠标移除快捷添加
		$("#addMoreBusLi").mouseout(function(e) {
			evt = window.event || e;
			var obj = evt.toElement || evt.relatedTarget;
			var pa = this;
			if (pa.contains(obj)) {
				return false;
			}
			addMoreBusLiClock = setTimeout(function() {
				$("#addMoreBusLi").removeClass("open");
			}, 300);
		})
	})
//-->
</script>
<body>
	<!-- Navbar -->
	<div class="navbar" id="headDiv">
		<div class="navbar-inner">
			<div class="navbar-container">
				<!-- Navbar Barnd -->
				<div class="navbar-header pull-left" style="max-width:250px;"
					id="menuOrgDiv">
					<a class="navbar-brand dropdown-toggle" href="javascript:void(0)"
						data-toggle="dropdown"> <small> <img
							src="/static/assets/img/logo.png" />
					</small> <i class="fa fa-angle-down logo-arrow"></i>
					</a>
				</div>

				<!-- /Navbar Barnd -->
				<div class="navbar-header pull-right">
					<div class="navbar-account">
						<ul class="account-area">
							<li><a class=" dropdown-toggle" data-toggle="dropdown"
								title="主页" href="/web/index"> <i class="icon fa fa-home"></i>
							</a></li>
							<!--添加按钮-->
							<!-- <li id="addMoreBusLi"><a class=" dropdown-toggle"
								data-toggle="dropdown" title="快捷添加" href="JavaScript:void(0)">
									<i class="icon fa fa-plus-circle"></i>
							</a> Notification Dropdown
								<ul
									class="pull-right dropdown-menu dropdown-arrow dropdown-notifications">
									<li><a href="javascript:void(0)"
										onclick="addQus();"
										style="padding: 8px 2px">
											<div class="clearfix" style="vertical-align: middle;">
												<i class="fa fa-question-circle fa-lg blue icon-small"
													style="background: url('');margin-top: 3px;margin-left: 1px;font-size: 21px"></i>
												<span class="title ps-topmargin">新增疑问</span>
											</div>
									</a></li>
									<li><a href="javascript:void(0)"
										onclick="addHeadBus('012')"
										style="padding: 8px 2px">
											<div class="clearfix">
												<i class="fa icon-small add-customer"></i> <span
													class="title ps-topmargin">解答疑问</span>
											</div>
									</a></li>
								</ul></li> -->
							<!--添加按钮 end-->
							<li class="margin-left-10 margin-right-10"></li>
						</ul>
					</div>
				</div>
				<!-- /Account Area and Settings -->
			</div>
		</div>
	</div>
	<!-- /Navbar -->
</body>
