<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
	errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.base.util.CommonUtil"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.westar.core.web.InitServlet"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!-- 财务模块JS -->
<script type="text/javascript" charset="utf-8" src="/static/js/financial/financial.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript" charset="utf-8" src="/static/js/head.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
<script type="text/javascript">
	var lastHistory = "ｌａｓｔＨｉｓｔｏｒｙ＿${userInfo.comId}＿${userInfo.id}";
	lastHistory = escape(lastHistory);
	var localValue = escape(window.self.location);
	localStorage.setItem(lastHistory, localValue);
	var sid="${param.sid}";
	var showModNoReadP="${showModNoRead}";
	//是否点击的界面
	var homeFlag = '${homeFlag}';
	var t = setTimeout(function() {
		//etiger.socket.init('${param.sid}', '${userInfo.comId}',
				//'${userInfo.id}', '${userInfo.userName}');
	}, 1000);
	$(function() {
		initHeadMenu('${userInfo.id}','${userInfo.comId}');
		//取得积分信息
		//userLeveName
		getSelfJSON("/jiFen/getJiFenOfUser",{sid:"${param.sid}"},function(result){
			if(result.code == 0){
				var user = result.data;
				$("body").find("#userLeveName").html(user.levName);
			}
		})
	});
	//判断是否需要弹窗
	var ifshow = sessionStorage.getItem("ifshow");
	if (!ifshow) {
		sessionStorage.setItem("ifshow", 'y');
	}
	var showMsgIndex;
	/*有新消息就弹出*/
	function ifMsg() {
		if ('${empty noRead}'=='false') {
			return;
		}
		if ("${userInfo.autoEject}" == '0') {
			return;
		}
		var flag = sessionStorage.getItem("ifshow");
		if (flag == 'y' && !showMsgIndex) {
			$.post('/common/getNowTime?sid=${param.sid}', {
				caseNum : 1
			}, function(rs) {
				var showtime = sessionStorage.getItem("showtime");
				if (rs.time >= showtime) {
					$.post('/msgShare/showMsgNoReadNum?sid=${param.sid}', {
						random : Math.random()
					}, function(rs) {
						if (rs.ifLogin == true) {
							//setMsgNum(rs.num);
							if (rs.num > 0) {
								showMsg(rs.num);
							}
						}
					}, 'json');
				}
			}, 'json');
		}
	}
    var msgInt = setInterval(function() {
        ifMsg();
    }, 1000 * 60);
	//未读消息计数器
	var msgNumInt = sessionStorage.getItem("msgNumInt");
	if (!msgNumInt) {//没有计数器，则建立一个并执行
		msgNumInt = 10;
		sessionStorage.setItem("msgNumInt", msgNumInt);
		clockForNoReadNum();
	} else {//有计数器，则执行
		clockForNoReadNum();
	}

	var clockForNoRead;
	//执行计数
	function clockForNoReadNum() {
		clockForNoRead = setInterval(function() {//每秒计数减1,为0后重新计数
			msgNumInt = sessionStorage.getItem("msgNumInt");
			msgNumInt = msgNumInt - 1;
			if (msgNumInt == 0) {
				getFlagNum();
				msgNumInt = 10;
			}
			sessionStorage.setItem("msgNumInt", msgNumInt);
		}, 10000);
	}

	//取得未读消息
	function getFlagNum() {
		$.ajax({
			type : "post",
			url : "/msgShare/countTodo?sid=${param.sid}&rnd=" + Math.random(),
			dataType : "json",
			async : false,
			success : function(data) {
				if (data.status == 'y') {
					setFlagNum(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showNotification(2, "系统错误，请联系管理员！");
				clearInterval(clockForNoRead);
			}
		});
	}
	function viewModDetail(id, type, clockId, msgid, busSpec) {
		if (clockId > 0 && msgid > 0 && busSpec == '1') {//是待办事项
			var url = "/msgShare/viewTodoDetailPage?sid=${param.sid}&id="+ msgid;
			url += "&busId=" + id + "&busType=" + type + "&isClock=1&clockId="+ clockId;
			openWinByRight(url);
		} else if ('012' == type
				|| '005' == type
				||'003' == type
				||'006' == type
            	||'050' == type
				||'004' == type) {
			viewModInfo(id,type,clockId);
		} else if ('011' == type) {//问答
			var url = "/qas/viewQuesPage?sid=${param.sid}&id=" + id;
			openWinByRight(url);
		} else if ('015' == type) {//申请
			checkApplyInUser(id);
		} else if ('017' == type) {//会议
			$.post("/meeting/checkMeetTimeOut?sid=${param.sid}",{
				Action : "post",
				meetId : id
			},
			function(data) {
				if (data.status == 'y' && data.organiser > 0) {
					if (data.timeOut == 0) {//会议已结束
						if (${userInfo.id} == data.recorder) {//会议记录人员添加会议纪要
							var url = "/meetSummary/addSummaryPage?sid=${param.sid}&meetingId="+ id;
							openWinByRight(url);
						} else {//非会议记录人员，查看会议+纪要
							var url = "/meetSummary/viewSummaryPage?sid=${param.sid}&meetingId="+ id;
							openWinByRight(url);
						}
					}else if (data.timeOut == 1) {//会正在进行
						if ("${userInfo.id} "== data.organiser) {//会议发布人员邀请参会人员
							var url = "/meeting/invMeetUserPage?sid=${param.sid}&meetingId="+ id;
							openWinByRight(url);
						} else {//非会议发布人员查看会议
							var url = "/meeting/viewMeetingPage?sid=${param.sid}&meetingId="+ id;
							openWinByRight(url);
						}
					} else {
						if (${userInfo.id} == data.organiser) {//会议发布人员修改会议信息
							var url = "/meeting/updateMeetingPage?sid=${param.sid}&batchUpdate=1&id="+ id;
							openWinByRight(url);
						} else {//非会议发布人员，查看会议信息
							var url = "/meeting/viewMeetingPage?sid=${param.sid}&meetingId="+ id;
							openWinByRight(url);
						}
					}
				} else if (data.status == 'y'&& data.organiser == 0) {
					showNotification(2, "会议已删除");
				} else {
					showNotification(2, data.info);
				}
			}, "json");
		} else if ('018' == type) {//会议
			if (busSpec == 1) {
				$.post("/meeting/authCheckMeetRoom?sid=${param.sid}",{
						Action : "post",
						meetingId : id
					},function(data) {
						if (data.status == 'y') {
							window.top.location.href = "/meetingRoom/listPagedApplyForCheck?sid=${param.sid}&meetingId="+ id + "&searchTab=22";
						} else {
							showNotification(1, data.info);
						}
					}, "json");
			} else {
				viewModDetail(id, '017', clockId, msgid, busSpec);
			}
		} else if ('022' == type || '02201' == type) {//审批
			var url = "/workFlow/viewSpFlow?sid="+sid+"&instanceId="+id;
			openWinByRight(url);
		}else if('027010'==type||type == '027011'){//采购审核
			var url = "/bgypPurOrder/viewBgypPurOrderPage?sid="+sid+"&purOrderId="+id;
			openWinByRight(url);
		}else if('027020'==type||type == '027021'){//申领审核
			var url = "/bgypApply/viewBgypApplyPage?sid="+sid+"&applyId="+id;
			openWinByRight(url);
		} else if ('1' == type) {//分享

			var url = "/msgShare/msgShareViewPage?sid=${param.sid}&id=" + id
					+ "&type=" + type;
			openWinByRight(url);
		}else if (type == 39) {//公告

			var url = "/announcement/viewAnnoun?sid=" + sid + "&id=" + id;
			openWinByRight(url);
		}else if (type == 40) {//公告
			var url = "/institution/viewInstitu?sid=" + sid + "&id=" + id;
			openWinByRight(url);
		}else if('046'== type){//会议审批
			var url = "/meeting/viewMeetingPage?sid="+sid+"&meetingId="+id;
			openWinByRight(url);
		}else if('047'==type){//会议纪要审批
			var url = "/meetSummary/viewSummaryById?sid="+sid+"&summaryId="+id;
			openWinByRight(url);
		}else if('099'==type){//催办
			var url = "/busRemind/viewBusRemindPage?sid="+sid+"&id="+id;
			openWinByRight(url);
		}else if('0103'==type){//领款通知
			var url = "/balance/viewNotices?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
			openWinByRight(url);
		}else if('068'==type){//外部联系人
			var url = "/outLinkMan/viewOlmPage?sid="+sid+"&id="+id;
			openWinByRight(url);
		}else if('06602'==type){//完成结算通知
			var url = "/balance/viewBalancedNotices?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
			openWinByRight(url);
		}else if('06601'==type){//财务结算
			var url = "/balance/viewBalanceNotices?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
			openWinByRight(url);
		}else if('067'==type){//属性变更
			var url = "/moduleChangeExamine/viewModChangeApply?sid="+sid+"&id="+msgid + "&busId="+id+"&busType="+type;
			openWinByRight(url);
		}else if('016'==type){//日程
			var url = "/schedule/updateSchedulePage?sid="+sid+"&id="+id;
			openWinByRight(url);
		}
		
	}
</script>
<body>
	<!-- Navbar -->
	<div class="navbar" id="headDiv">
		<div class="navbar-inner">
			<div class="navbar-container">
				<!-- Navbar Barnd -->
				<div class="navbar-header pull-left" style="max-width:250px;" id="menuOrgDiv">
					<a class="navbar-brand dropdown-toggle" href="javascript:void(0)" id ="menuOrgA"
						data-toggle="dropdown"> <small> <img
							src="/static/assets/img/logo.png" />
					</small> <i class="fa fa-angle-down logo-arrow"></i>
					</a>
					<ul class="dropdown-menu logo-dropdown">
						<!-- 
	                        <li>
	                            <a href="javascript:void(0)">微信号</a>
	                        </li>
                    	 -->
                    	 <li id="helpIntro"><a href="javaScript:void(0)" onclick="pageIntro()" >帮助</a>
						</li>
						<li><a
							href="/userInfo/inviteUserPage?sid=${param.sid}&tab=15">邀请同事</a>
						</li>
						<!-- 
	                        <li>
	                            <a href="javascript:void(0)">推介给好友</a>
	                        </li>
	                        <li>
	                            <a href="javascript:void(0)">创建或加入团队</a>
	                        </li>
                         -->
						<li class="divider"></li>
						<li><a href="/organic/organicInfo?sid=${param.sid}&tab=11">团队信息</a></li>
						<li><a href="javascript:void(0)" onclick="orgChange()">团队切换</a></li>
						<c:if test="${userInfo.admin>0}">
							<li><a href="/order/order_center?sid=${param.sid}">团队升级</a></li>
						</c:if>
					<%-- 	<li class="divider"></li>
						<li><a
							href="/userInfo/selfCenter?pager.pageSize=10&sid=${param.sid}&activityMenu=self_m_1.1">个人中心</a>
						</li>
						<li><a href="/userInfo/editUserInfoPage?sid=${param.sid }&activityMenu=self_m_3.1">个人设置</a>
						</li> --%>
						<li class="divider"></li>
						<c:if test="${userInfo.admin>0}">
                        <li>
                            <a href="/adminCfg/conf/listBusMapFlow?pager.pageSize=10&sid=${param.sid}&activityMenu=m_3.1&busType=029">系统配置</a>
                        </li>
						</c:if>
                        <li>
                            <a href="javascript:void(0)" id="androidApp">android手机移动端下载</a>
                        </li>
					    <li>
						 <a href="/sysUpLog/toListSysUpLog?sid=${param.sid}&activityMenu=sul_m_1.1&tab=31">平台升级日志</a></li>
                        </li>
						<li class="divider"></li>
						<!-- 
	                        <li>
	                            <a href="javascript:void(0)">帮助</a>
	                        </li>
	                        <li>
	                            <a href="javascript:void(0)">意见反馈</a>
	                        </li>
                         -->
						<li><a href="javascript:void(0)"
							onclick="exitSys('${param.sid}')">退出系统</a></li>
					</ul>
				</div>
				<c:if test="${page!='index'}">
					<!-- Sidebar Collapse -->
					<div class="sidebar-collapse" id="sidebar-collapse">
						<i class="collapse-icon fa fa-bars"></i>
					</div>
				</c:if>
				<div class="navbar-header pull-left ps-homeGroup">
					<div class="navbar-account">
						<ul class="account-area ${page=='index'?'navbar-homeBtn':'navbar-otherBtn'}">
							<li class="ps-white" id="headMenuFirst" style="padding-left: 25px">
								<a href="javascript:void(0);" class="${homeFlag=='1'?'home-active':''}">
									<i class="fa fa-home" style="font-size: 20px" title="主页"></i>
								</a>
							</li>
							<li class="ps-white" id="headMenuLi"><a
								class=" dropdown-toggle ps-apply" data-toggle="dropdown"
								title="更多应用" href="javascript:void(0)"> <i
									class="icon fa fa-th-large pull-left ps-applyIcon"></i><span
									class="pull-left">更多应用</span>
							</a> <!--Notification Dropdown-->
								<ul class="pull-left dropdown-menu dropdown-arrow dropdown-notifications ps-apply-in">
									<li>
										<div class="menu-group">
											<dl class="menu-box">
												<dt>工作</dt>
												<dd id="menuHead_1"></dd>
											</dl>
											<dl class="menu-box">
												<dt>审批</dt>
												<dd id="menuHead_3"></dd>
											</dl>
											<dl class="menu-box">
												<dt>CRM</dt>
												<dd id="menuHead_2"></dd>
											</dl>
											<dl class="menu-box">
												<dt>订单</dt>
												<dd id="menuHead_10"></dd>
											</dl>
										</div>
										<div class="menu-group">
											<dl class="menu-box">
												<dt>综合办公</dt>
												<dd id="menuHead_7">
												</dd>
											</dl>
                                        	<dl class="menu-box">
                                        		<dt>考勤管理</dt>
                                        		<dd id="menuHead_6"></dd>
                                        	</dl>
                                        	<dl class="menu-box">
                                        		<dt id="menuHead_8">费用管理</dt>
                                        		<dd></dd>
                                        	</dl>
										</div>
										<div class="menu-group">
											<dl class="menu-box">
												<dt>知识管理</dt>
												<dd id="menuHead_4"></dd>
											</dl>
											<dl class="menu-box">
												<dt>小工具</dt>
												<dd id="menuHead_5"></dd>
											</dl>
                                        	<!-- <dl class="menu-box">
                                        		<dt>第三方</dt>
                                        		<dd>
                                        			<a href="javascript:void(0)" style="cursor: default;"><i class="fa fa-enterprise fa-lg fa-pinterest" style="background: url('');cursor: default;"></i>微信企业号</a>
                                        			<a href="javascript:void(0)" style="cursor: default;"><i class="fa fa-comments fa-lg fa-wechat2" style="background: url('');cursor: default;"></i>微信服务号</a>
                                        		</dd>
                                        	</dl> -->
										</div>
									</li>
                                    <li class="no-padding"><a href="javascript:void(0)" style="color:#428bca !important" class="ps-set" onclick="menuNumSet('${param.sid}')"><i class="fa fa-cogs"></i>&nbsp;显示设置</a></li>
								</ul>
								</li>
						</ul>
					</div>

				</div>
				<!-- /Navbar Barnd -->
				<div class="navbar-header pull-right">
					<div class="navbar-account">
						<ul class="account-area">
							<!--平台统计分析-->
							<c:if test="${userInfo.admin>0 || userInfo.countSub>0 }">
								<li>
									<a class=" dropdown-toggle" id="platFormStatistic"
										data-toggle="dropdown" title="平台运营分析" href="JavaScript:void(0)">
											<i class="icon fa fa-bar-chart-o"></i>
									</a>
								</li>
							</c:if>
							<!--添加按钮-->
							<li id="addMoreBusLi">
							<a class=" dropdown-toggle" id="addMoreBusA"
								data-toggle="dropdown" title="快捷添加" href="JavaScript:void(0)">
									<i class="icon fa fa-plus-circle"></i>
							</a> <!--Notification Dropdown-->
								<ul class="pull-right dropdown-menu dropdown-arrow dropdown-notifications" 
									id="soonAddModUl">
									
								</ul> <!--/Notification Dropdown-->
							</li>
							<!--添加按钮 end-->
							<li id="busMsgLi">
							<a class="wave in dropdown-toggle" id = "busMsgA" data-toggle="dropdown" title="消息" href="javascript:void(0)">
									<i class="icon fa fa-volume-up"></i> 
									<span class="badge" id="allNoReadNum">0</span>
							</a> <!--Messages Dropdown-->
								<ul class="pull-right dropdown-menu dropdown-arrow dropdown-messages">
									<li><a
										href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&activityMenu=job_m_1.1"
										style="padding: 8px 2px">
											<div class="clearfix">
												<i class="icon icon-small mes-daily"></i> <span
													class="message-sender pull-left">全部未读</span> <span
													class="pull-right" id="modAllNoReadNum"
													style="background-color: red"></span>
											</div>
									</a></li>
									
									<li><a
										href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&busType=003&modTypes=003&activityMenu=job_m_1.2"
										style="padding: 8px 2px">
											<div class="clearfix">
												<i class="icon icon-small mes-daily"></i> <span
													class="message-sender pull-left">任务未读</span> <span
													class="pull-right" id="noReadNum_003"
													style="background-color: red"></span>
											</div>
									</a></li>
									<li><a
										href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&busType=022&modTypes=022&activityMenu=job_m_1.3"
										style="padding: 8px 2px">
											<div class="clearfix">
												<i class="icon icon-small mes-daily"></i> <span
													class="message-sender pull-left">审批未读</span> <span
													class="pull-right" id="noReadNum_022"
													style="background-color: red"></span>
											</div>
									</a></li>
									<li><a
										href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&busType=017&modTypes=017&activityMenu=job_m_1.9"
										style="padding: 8px 2px">
											<div class="clearfix">
												<i class="icon icon-small mes-follow"></i> <span
													class="message-sender pull-left">会议通知</span> <span
													class="pull-right" id="noReadNum_017"
													style="background-color: red"></span>
											</div>
									</a></li>
									<li><a
											href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&busType=050&modTypes=050&activityMenu=job_m_1.6"
											style="padding: 8px 2px">
										<div class="clearfix">
											<i class="icon icon-small mes-finish"></i> <span
												class="message-sender pull-left">分享未读</span> <span
												class="pull-right" id="noReadNum_050"
												style="background-color: red"></span>
										</div>
									</a></li>
									<li><a
										href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&busType=006&modTypes=006&activityMenu=job_m_1.7"
										style="padding: 8px 2px">
											<div class="clearfix">
												<i class="icon icon-small mes-finish"></i> <span
													class="message-sender pull-left">周报未读</span> <span
													class="pull-right" id="noReadNum_006"
													style="background-color: red"></span>
											</div>
									</a></li>
									<c:if test="${userInfo.admin>0}">
										<li><a
											href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&busType=015&modTypes=015&activityMenu=job_m_1.12"
											style="padding: 8px 2px">
												<div class="clearfix">
													<i class="icon icon-small mes-me"></i> <span
														class="message-sender pull-left">加入申请</span> <span
														class="pull-right" id="noReadNum_015"
														style="background-color: red"></span>
												</div>
										</a></li>
									</c:if>
									<li><a href="/msgShare/toDoJobs/jobsCenter?sid=${param.sid}&pager.pageSize=10&busType=9999&modTypes=9999&activityMenu=job_m_1.18"
										style="padding: 8px 2px">
											<div class="clearfix">
												<i class="icon icon-small mes-follow"></i> <span
													class="message-sender pull-left">其它消息</span> <span
													class="pull-right" id="noReadNum_9999"
													style="background-color: red"></span>
											</div>
									</a></li>
								</ul> <!--/Messages Dropdown--></li>
							<li class="margin-left-10 margin-right-10">
								<form id="indexSearchForm" action="/index/searchIndexInCom">
									<input type="hidden" name="sid" value="${param.sid}" /> <input
										type="hidden" name="busType" id="indexBusType" />
									<!-- Page Sidebar Header-->
									<div class="sidebar-header-wrapper" id="headSearch">
										<input type="text" class="searchinput" name="searchStr"
											id="searchStr" value="${searchStr}" style="padding-left: 10px;padding-right: 30px"/> 
										<i class="searchicon fa fa-search" style="right: 0!important;left:auto;margin-right: 10px!important;"></i>
										<div class="searchhelper">请输入关键字</div>
									</div>
									<!-- /Page Sidebar Header -->
								</form>
							</li>
							<li class="margin-right-20" id="menuUserLi">
							<a class="login-area dropdown-toggle" data-toggle="dropdown">
									<div class="avatar" title="View your public profile">
											<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}"></img>
									</div>
									<section>
										<h2 class="ps-user-name">
											<span class="profile"> <span>${userInfo.userName}
													<small class="white padding-left-10" id="userLeveName"></small>
											</span>
											</span>
										</h2>
									</section>
							</a> <!--Login Area Dropdown-->
								<ul class="pull-right dropdown-menu dropdown-arrow dropdown-login-area" id="headSelf">
									<li class="username"><a>${userInfo.userName }</a></li>
									<li class="email"><a>${userInfo.email}</a></li>
									<!--Avatar Area-->
									<li>
										<div class="avatar-area" id="menuHeadImg">
											<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?size=1"
														class="avatar" style="cursor:pointer;" name="selfImg"></img>
											<a href="/userInfo/editUserHeadImg?sid=${param.sid}&activityMenu=self_m_3.2"
												class="caption" style="display: none">换头像</a>
										</div>
									</li>
									<!--Avatar Area-->
									<li class="edit"><a
										href="/userInfo/selfCenter?pager.pageSize=10&sid=${param.sid}&activityMenu=self_m_1.1"
										class="pull-left">个人中心</a> <a
										href="/userInfo/editUserInfoPage?sid=${param.sid}&activityMenu=self_m_3.1"
										class="pull-right">个人设置</a></li>
									<!--/Theme Selector Area-->
									<li class="dropdown-footer"><a href="javascript:void(0)"
										onclick="exitSys('${param.sid}')">退出</a></li>
								</ul> <!--/Login Area Dropdown--></li>
							<!-- /Account Area -->
							<!--Note: notice that setting div must start right after account area list.
                            no space must be between these elements-->
							<!-- Settings -->
						</ul>

					</div>
				</div>
				<!-- /Account Area and Settings -->
			</div>
		</div>
	</div>
	<!-- /Navbar -->
</body>
