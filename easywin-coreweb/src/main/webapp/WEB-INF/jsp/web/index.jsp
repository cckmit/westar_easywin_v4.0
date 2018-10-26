<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>捷成协同办公平台</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="协同  OA 云平台">
<link rel="stylesheet" type="text/css" href="/static/css/web/style.css">
<script type="text/javascript" src="/static/js/jquery-1.8.0.min.js"></script>
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
</head>
<body>
	<!----头部开始---->
	<div class="top">
		<jsp:include page="head.jsp"></jsp:include>
	</div>
	<!----头部结束---->
	<!----banner开始---->
	<div class="banner"></div>
	<!----banner结束---->
	<!----菜单开始---->
	<div class="menu">
		<div class="menu" id="navbarExample">
			<div class="menu-con">
				<table>
					<tr>
						<td><a href="javascript:;"><img src="static/images/web/tb01.png"
								imgnum="1" />
							<p>客户</p></a></td>
						<td><a href="javascript:;"><img src="static/images/web/tb02.png"
								imgnum="2" />
							<p>项目</p></a></td>
						<td><a href="javascript:;"><img src="static/images/web/tb03.png"
								imgnum="3" />
							<p>任务</p></a></td>
						<td><a href="javascript:;"><img src="static/images/web/tb04.png"
								imgnum="4" />
							<p>周报</p></a></td>
						<td><a href="javascript:;"><img src="static/images/web/tb05.png"
								imgnum="5" />
							<p>会议</p></a></td>
						<td><a href="javascript:;"><img src="static/images/web/tb06.png"
								imgnum="6" />
							<p>日程</p></a></td>
						<td><a href="javascript:;"><img src="static/images/web/tb07.png"
								imgnum="7" />
							<p>知识中心</p></a></td>
						<td><a href="javascript:;"><img src="static/images/web/tb08.png"
								imgnum="8" />
							<p>团队云盘</p></a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<!----菜单结束---->
	<!----客户 S---->
	<div id="sxfy" class="sxfy">
		<div class="sxfy-con">
			<div class="text-wrapper pull-left">
				<div class="big-title kf-title">客户</div>
				<div class="text-box">
					打通线索、商机、客户、交易、服务等各前端业务环节，全流程完成对市场信息的采集、传递、评估、分析、继承、分享、消化</div>
			</div>
			<div class="pic-box pull-right">
				<img src="static/images/web/kh-pic.png" />
			</div>
		</div>
	</div>
	<!----客户 E---->
	<!----项目 S---->
	<div id="msdb" class="msdb" labelnum="2">
		<div class="msdb-con">
			<div class="text-wrapper pull-right">
				<div class="big-title xm-title">项目</div>
				<div class="text-box">
					一旦项目中你参与的事情有新的进展，例如有新的成员回复或是任务状态有变化，你都会在几百毫秒内在「待办事项」中收到通知</div>
			</div>
			<div class="pic-box pull-left">
				<img src="static/images/web/xm-pic.png" />
			</div>
		</div>
	</div>
	<!----项目 E---->
	<!----任务 S---->
	<div id="cjgk" class="cjgk" labelnum="3">
		<div class="cjgk-con">
			<div class="text-wrapper pull-left">
				<div class="big-title rw-title">任务</div>
				<div class="text-box">
					<div class="one-line">
						<h6 class="small-tit">多层次任务分解</h6>
						<p>支持项目、阶段、任务、子任务多个层级，再复杂的项目也可以被拆解为具体任务，做到责任到人、分工明确</p>
					</div>
					<div class="one-line">
						<h6 class="small-tit">自动督办，提升执行力</h6>
						<p>我负责的任务”让每个员工对自己的工作内容一目了然，任务到期系统自动提醒督办，再粗心的员工都不会遗忘工作</p>
					</div>
				</div>
			</div>
			<div class="pic-box pull-right">
				<img src="static/images/web/rw-pic.png" />
			</div>
		</div>
	</div>
	<!----任务 E---->
	<!----周报 S---->
	<div id="hmzc" class="hmzc" labelnum="4">
		<div class="msdb-con">
			<div class="text-wrapper pull-right">
				<div class="big-title zb-title">周报</div>
				<div class="text-box">
					采用扁平化传递，汇报一经发布；所有上级领导同时在第一时间看到你的汇报；而上级的督导评论也能在第一时间才传递给你</div>
			</div>
			<div class="pic-box pull-left">
				<img src="static/images/web/zb-pic.png" />
			</div>
		</div>
	</div>
	<!----周报 E---->
	<!----会议 S---->
	<div id="jytj" class="jytj" labelnum="5">
		<div class="jytj-con">
			<div class="text-wrapper pull-left">
				<div class="big-title hy-title">会议</div>
				<div class="text-box">
					会议从发起、进行、结束以及会议纪要的阅读等操作，被一一记录在案；让我们的会议精神能及时有效的在团队内部传达</div>
			</div>
			<div class="pic-box pull-right">
				<img src="static/images/web/hy-pic.png" />
			</div>
		</div>
	</div>
	<!----会议 E---->
	<!----日程 S---->
	<div class="qzwy" id="qzwy" labelnum="6">
		<div class="qzwy-con">
			<div class="text-wrapper pull-right">
				<div class="big-title rc-title">日程</div>
				<div class="text-box">
					<div class="one-line">
						<h6 class="small-tit">支持查看同事日程</h6>
						<p>您可直接查看同事的公开日程，何时有空一看便知，安排会议不再头痛</p>
					</div>
					<div class="one-line">
						<h6 class="small-tit">打通任务中心</h6>
						<p>任务在到期日自动建立日程，您可以在日程表中直接查看即将到期的任务，更可以在日程中直接进入任务页面</p>
					</div>
				</div>
			</div>
			<div class="pic-box pull-left">
				<img src="static/images/web/rc-pic.png" />
			</div>
		</div>
	</div>
	<!----日程 E---->
	<!----知识中心 S---->
	<div id="rhzd" class="rhzd" labelnum="7">
		<div class="rhzd-con">
			<div class="text-wrapper pull-left">
				<div class="big-title zs-title">知识中心</div>
				<div class="text-box">
					无论身在何处，分享墙是你随时可以与团队沟通想法和总结经验的地方。与来回发送内部邮件相比，在分享墙里发布话题更便于团队讨论和追溯</div>
			</div>
			<div class="pic-box pull-right">
				<img src="static/images/web/zs-pic.png" />
			</div>
		</div>
	</div>
	<!----知识中心 E---->
	<!----团队云盘 S---->
	<div id="sfss" class="sfss" labelnum="8">
		<div class="sfss-con">
			<div class="text-wrapper pull-right">
				<div class="big-title td-title">团队云盘</div>
				<div class="text-box">
					可以协作的网盘，所有文件都可以随时访问，并发表你的看法。它支持图片、doc、PDF、xlsx、MP4等多种常用文件的在线预览。同时，你可以随时上传更新新版本，所有历史版本都会保存。有关文件的一切存储与更新，再也不用邮件抄送了
				</div>
			</div>
			<div class="pic-box pull-left">
				<img src="static/images/web/yp-pic.png" />
			</div>
		</div>
	</div>
	<!----团队云盘 E---->

	<div class="bottom">
		<jsp:include page="bottom.jsp"></jsp:include>
	</div>
</body>
</html>
<script type="text/javascript">
	var sxfy = $("#sxfy").height() + 60;
	var msdb = $("#msdb").height();
	var cjgk = $("#cjgk").height();
	var hmzc = $("#hmzc").height() + 30;
	var jytj = $("#jytj").height();
	var qzwy = $("#qzwy").height();
	var rhzd = $("#rhzd").height() + 30;
	var sfss = $("#sfss").height();
	var navbarExampleHei = $("#navbarExample").height();
	var topSessionHei = $(".top").height() + $(".banner").height()
			+ navbarExampleHei + 64;
	$(window)
			.scroll(
					function() {
						var scrollTop = $(window).scrollTop();
						scrollTop = scrollTop + navbarExampleHei;
						if (scrollTop < topSessionHei) {
							$("#navbarExample").css("position", "static");
						}
						if (scrollTop > topSessionHei) {
							$("#navbarExample").css("position", "fixed").css(
									"top", "0px");
							changeColor(1);
						} else {
							$(".menu-con a").each(function(k, v) {
								if ($(v).hasClass("a-visited"))
									$(v).addClass("a-visited");
								else
									$(v).removeClass("a-visited");
							})
						}
						if (scrollTop > (topSessionHei + sxfy / 2)) {
							changeColor(2);
						}
						if (scrollTop > (topSessionHei + sxfy + msdb / 2)) {
							changeColor(3);
						}
						if (scrollTop > (topSessionHei + sxfy + msdb + cjgk / 2)) {
							changeColor(4);
						}
						if (scrollTop > (topSessionHei + sxfy + msdb + cjgk + hmzc / 2)) {
							changeColor(5);
						}
						if (scrollTop > (topSessionHei + sxfy + msdb + cjgk
								+ hmzc + jytj / 2)) {
							changeColor(6);
						}
						if (scrollTop > (topSessionHei + sxfy + msdb + cjgk
								+ hmzc + jytj + qzwy / 2)) {
							changeColor(7);
						}
						if (scrollTop > (topSessionHei + sxfy + msdb + cjgk
								+ hmzc + jytj + qzwy + rhzd / 2)) {
							changeColor(8);
						}
						if (scrollTop > (topSessionHei + sxfy + msdb + cjgk
								+ hmzc + jytj + qzwy + rhzd + sfss)) {
							changeColor(9);
						}
					});
	$("#navbarExample a").click(function() {
		var num = $(this).children("img").attr("imgNum");
		num = parseInt(num) + 1;
		scrollTopToOther(num);
	});
	function scrollTopToOther(num) {
		var scrollNum = 0;
		var temp = true;
		switch (num) {
		case 2:
			scrollNum = topSessionHei;
			break;
		case 3:
			scrollNum = topSessionHei + sxfy;
			break;
		case 4:
			scrollNum = topSessionHei + sxfy + msdb;
			break;
		case 5:
			scrollNum = topSessionHei + sxfy + msdb + cjgk;
			break;
		case 6:
			scrollNum = topSessionHei + sxfy + msdb + cjgk + hmzc;
			break;
		case 7:
			scrollNum = topSessionHei + sxfy + msdb + cjgk + hmzc + jytj;
			break;
		case 8:
			scrollNum = topSessionHei + sxfy + msdb + cjgk + hmzc + jytj + qzwy;
			break;
		case 9:
			scrollNum = topSessionHei + sxfy + msdb + cjgk + hmzc + jytj + qzwy
					+ rhzd;
			break;
		default:
			temp = false;
		}
		if (temp)
			$("body, html").animate({
				scrollTop : scrollNum - navbarExampleHei + 5
			}, 500);
	}
	function changeColor(num) {
		$(".menu-con a").each(function(k, v) {
			if ($(v).children().first().attr("imgNum") == num) {
				$(v).addClass("a-visited");
			} else {
				$(v).removeClass("a-visited");
			}
		})
	}
	//
	var CanSbRzm = false;
	function SerarchThings() {
		if (CanSbRzm)
			return;
		var phone = $("#txtPhone").val();
		if (!/1[345678%][\d%]{9}$/.test(phone)) {
			$("#txtPhone").focus();
			alert("你输入的手机号格式不正确");
		} else {
			$(".bt1").addClass("btnNouse");
			CanSbRzm = true;
			$.ajax({
				url : 'js/index.ashx?method=getValidStmsg',
				type : 'POST',
				data : {
					"phone" : phone
				},
				success : function(reponseText) {
					var jsonPm = eval("(" + reponseText + ")");
					if (jsonPm.error) {
						alert(jsonPm.error);
						CanSbRzm = false;
						$(".bt1").removeClass("btnNouse");
					} else {
						$(".bt2").removeClass("btnNouse");
						CanSbRzm = true;
						alert("验证码已发送至你的手机，请注意查收！");
					}
				}
			});
		}
	}
	function ValidRzm() {
		if (!CanSbRzm)
			return;
		var rzm = $("#txtYzm").val();
		if (!/[\d%]{6}$/.test(rzm)) {
			$("#txtYzm").focus();
			alert("请输入6位数字认证码！");
		} else {
			$(".bt2").addClass("btnNouse");
			$.ajax({
				url : 'js/index.ashx?method=ValidRzm',
				type : 'POST',
				data : {
					"rzm" : rzm
				},
				success : function(reponseText) {
					var jsonPm = eval("(" + reponseText + ")");
					if (jsonPm.error) {
						$(".bt2").removeClass("btnNouse");
					} else {
						//                        if (jsonPm.success)//gly
						//                            window.location = "Statistics.aspx";
						//                        else
						window.location = "SxResultList.aspx";
					}
				}
			});
		}
	}
	$(function(){
		//判断手否从别的地方类的
		var sorcePage = '${param.sorcePage}';
		if(sorcePage=='other'){//是，则滚动
			scrollTopToOther(2);
		}
	});
</script>