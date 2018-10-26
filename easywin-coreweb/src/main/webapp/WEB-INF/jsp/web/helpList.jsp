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
	<!----搜索 S---->
	<div class="search-box">
		<div class="search-in">
			<input type="text" name="" id="" value="" placeholder="请输入关键字，如【任务】" />
			<a href="#" class="search-btn">搜索</a>
		</div>
	</div>
	<!----搜索 E---->
	<!--快捷菜单 S-->
	<div class="icon-menu">
		<ul>
			<li><a href="/web/helpContent"><img src="static/images/web/xsbz-icon.png" /><br />新手帮助</a>
			</li>
			<li><a href="#"><img src="static/images/web/smfw-icon.png" /><br />上门服务</a>
			</li>
			<li><a href="#"><img src="static/images/web/gly-icon.png" /><br />管理员手册</a>
			</li>
			<li><a href="#"><img src="static/images/web/dnd-icon.png" /><br />电脑端指南</a>
			</li>
		</ul>
	</div>
	<!--快捷菜单 E-->
	<!--常见问题 S-->
	<div class="FAQ-box">
		<div class="FAQ-header">
			<div class="pull-left FAQ-title">常见问题</div>
		</div>
		<div class="QA">
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">钉钉商务电话权益变更的说明（2016-07-01生效）
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>
						根据有关部门相关规定和要求，从2016年7月1日起商务电话将做以下调整；<br>
						1、取消赠送商务电话单人通话（一对一的免费商务电话）权益；<br>
						2、对所有用户开放基于网络的语音、视频通话功能以及0流量、免话费的多人电话会议； <br>
						3、个人用户每月可用30分钟电话会议，最多支持5人同时通话； <br>
						4、团队用户不再分级，每月最高可用10000分钟电话会议，最多支持9人同时通话；<br>
						5、创建团队并升级认证，每月最高可用10万分钟电话会议，支持高达16人同时通话。
					</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">怎么使用钉钉打电话？（实用技巧）
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>
						通过钉钉仍然可以给企业通讯录的同事、钉钉好友及手机通讯录的联系人打电话，具体实用技巧如下：<br>
						1、搜索、从企业通讯录选择联系人或从上次通话记录发起，可根据钉钉智能推荐（基于网络和权限等）来发起相应的通话；<br>
						2、针对消息未读或DING未确认的同事，单人可直接发起用自己手机拨打的普通电话；多人可以直接发起电话会议；<br>
						3、针对正在聊天的同事/好友，点击“+”号，可发起快速接通、高清稳定的语音/视频通话；<br>
						4、从群聊或通话记录发起，选择多人后，可快速发起电话会议；<br>
						5、针对钉钉好友或企业中隐藏号码的同事，建议再添加一个人发起电话会议会更加高效（钉钉电话会议有强大的管理能力，过程中可以随时增减人员）。
					</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">什么是电话会议？怎么使用？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>
						钉钉电话会议是指2人以上的多人电话通话，当需要开会时发现有同事在出差或其他人办公地点不在同一地方，可使用钉钉电话会议，有3种便捷发起方式：<br>
						1、在群聊中，如果有事情打字无法快速讲清楚，可以点击右上角【电话按钮】，选择群中成员，点击【电话会议】按钮发起；<br>
						2、直接搜索或从企业通讯录中选择某人，点击【商务电话】，然后点击面板上的【+】按钮，添加其他成员后，点击【电话会议】；<br>
						3、从通话记录中选择之前的电话会议记录，进入后点击【电话会议】发起；<br>温馨提示：<br>电话会议最大支持人数：团队最高9人，认证企业最高16人。
					</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">如何获得电话会议分钟数？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>
						钉钉每月会根据用户的等级来赠送最高10万分钟的电话会议权益，等级越高分钟数越多，可同时呼叫的人数也越多：<br>
						1、个人用户每月赠送30分钟，可同时呼叫5人；<br>
						2、创建团队后将最高获赠1万分钟数，团队内所有成员共享使用，可同时呼叫9人；<br>
						3、团队用户升级认证企业后将获得最高10万分钟，可同时呼叫16人。
					</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">免费电话权益取消，钉钉还有哪些亮点？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>钉
						钉，是一个工作方式，阿里巴巴出品，专为中国企业打造的免费沟通协同多端平台，含PC版，Web版、手机版以及iPad版。企业通讯录，随时随地找人不
						难，消息已读未读，DING消息使命必达，语音通话、视频通话、电话会议、视频会议让沟通更高效；移动办公考勤、签到，审批，邮箱云盘，让工作更简单；澡
						堂模式，第三方加密让信息更安全。使用钉钉将全方位提升企业内部沟通与协同。如果您对这些功能有更好的建议或者觉得有不足的地方，我们一定会积极记录反
						馈，希望能够给您带来更好的使用体验。</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">预约专家上门服务？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>
						钉钉为您免费提供一次专家上门服务，通过讲解和部署钉钉，助力您的企业高效沟通和协同。 <br>目前，已经开通56个服务城市：<br>华东地区：上海、杭州、南京、合肥、苏州、济南、厦门、临沂、宁波、常州、淄博、烟台、漳州、青岛、威海、济宁、福州、温州、芜湖、蚌埠、铜陵、马鞍山、宣城、淮南、安庆、黄山、滁州、池州
						<br>华南地区：深圳、东莞、广州、佛山、珠海、中山 <br>华中地区：武汉、郑州、宜昌、长沙、湘潭 <br>华北地区：北京、石家庄、太原、呼和浩特
						<br>西北地区：西安、西宁、兰州、榆林、咸阳 <br>西南地区：成都、重庆、贵阳 <br>东北地区：哈尔滨、长春、沈阳、大连<br>申请条件：仅限企业或组织主管理员可发起预约
						<br>
						<span style="color:#38adff">立刻预约上门服务：请钉钉扫一扫二维码<span>
								<br>
							<img
								src="%E5%B8%AE%E5%8A%A9_files/TB1J3hWMXXXXXcnXXXXXXXXXXXX-280-280.png"
								alt="" width="150" height="150">
						</span></span>
					</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">针对小部分安卓系统（X86芯片）无法正常使用钉钉解决方案
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>
						亲，由于钉钉2.8版本对安卓性能进行了整体优化，提升性能，“瘦身”4MB，但是会影响安卓x86(芯片)系统下部分机型（部分功能失效），为了更好的使用钉钉，体验完整功能，需要亲扫一扫二维码，重新安装下钉钉解决问题，有疑问可以联系我们的钉小秘。<br>
						<span style="color:#38adff">请钉钉扫一扫二维码，重新下载安装<span> <br>
							<img
								src="%E5%B8%AE%E5%8A%A9_files/TB1jxvCMXXXXXcnaXXXXXXXXXXX-213-211.png"
								alt="" width="150" height="150"></span></span>
					</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">蓝色“企”字标志的群与黄色“企”字标识的群有什么区别？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>
						共性：<br>1、企业通讯录的成员才可以加入，非企业成员无法加入；<br>2、离职员工自动退出企业群；<br>3、群成员上限1000人；<br>不同点：<br>1、黄色标识企业群：企业管理后台创建部门时同时生成的部门群；部门添加人员后会自动加入该群；管理员可以解散此群、设置群主；<br>2、蓝色标识企业群：在手机/PC客户端发起的企业群聊天；群成员增加时需要在群里面进行添加；群主可以解散群；
					</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">如何自定义审批模板？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>企业管理员登录企业管理后台，进入“微应用—审批—进入后台—审批管理—创建新审批”进行；</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">如何设置微应用审批中的固定审批人？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>企业管理员登录企业管理后台，进入“微应用—审批—进入后台—审批管理—相应的审批流程—设置审批人—设置”；</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">能否可以自定义日志模板？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>需要企业管理员，在手机客户端或者电脑客户端，点击“微应用—日志—管理模板—新建模板“，根据本企业的需求创建日志模板即可；</p>
				</dd>
			</dl>
			<dl data-maidian="">
				<dt>
					<img src="static/images/web/Q.png" alt="" width="20px" height="21px">为什么电脑客户端左下角无钉邮图标？
				</dt>
				<dd>
					<img src="static/images/web/A.png" alt="" width="20px" height="21px">
					<p>需要登录手机客户端，进入钉邮，登陆自己的邮箱，可以选择一键体验或阿里云企业邮箱登陆，登陆成功之后，退出电脑客户端重新登录即可</p>
				</dd>
			</dl>
		</div>
	</div>

	<div class="bottom">
		<jsp:include page="bottom.jsp"></jsp:include>
	</div>
</body>
</html>
<script type="text/javascript">
	var i = -1;
	$("dl").on("click", function() {
		//var maidian = $(this).attr('data-maidian')
		//window.goldlog ? goldlog.record('/ding.10000', maidian, '', 'H46747591') : setTimeout(arguments.callee, 200);
		if (i == $(this).index()) {
			return;
		} else if (i > -1) {
			$("dl dd").eq(i).slideUp("slow");
		}

		$("dl dd").eq($(this).index()).slideDown("slow");
		i = $(this).index();

	})
</script>