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
		<jsp:include page="/WEB-INF/jsp/include/static_bootstrap.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/static.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/jsp/include/showNotification.jsp"></jsp:include>
		<script type="text/javascript" src="/static/js/shareCode.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<!-- 自动补全js -->
		<script type="text/javascript" src="/static/js/autoTextarea.js?version=<%=InitServlet.SYSTEM_STARUP_TIME%>"></script>
		<title><%=SystemStrConstant.TITLE_NAME%></title>
		<script type="text/javascript">
		//设置等级
		function setLev(){
			var viewType = '${viewType}';
			if('341'==viewType){//总积分排名
				$("#userCenter").attr("src", "/jiFen/listPagedJifenOrder?sid=${param.sid }&pager.pageSize=20")
			}else if('342'==viewType){//周积分排名
				$("#userCenter").attr("src", "/jiFen/listPagedJifenOrder?sid=${param.sid }&pager.pageSize=20&type=1")
			}else if('343'==viewType){//月积分排名
				$("#userCenter").attr("src", "/jiFen/listPagedJifenOrder?sid=${param.sid }&pager.pageSize=20&type=2")
			}
			
		}
		</script>
</head>
<body onload="setLev();">
<section id="container">
<!-- 系统头部装载 -->
<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>
<!-- 左边菜单 -->
<jsp:include page="/WEB-INF/jsp/include/left_menu.jsp"></jsp:include>
<!--main content start-->
<section id="main-content">
<!--任务按钮-->
<section class="wrapper wrapper2">
	<div class="ws-wrapIn">
		<div class="ws-tabTit">
			<ul class="ws-tabBar">
				<li>个人中心</li>
			</ul>
		</div>
							
		<div class="ws-projectBox">
			<div class="row">
				<div class="col-sm-12">
					<!--任务概述-->
					<section class="panel">
                        <header class="panel-heading tab-bg-dark-navy-blue ">
                            <ul class="nav nav-tabs">
                                <li>
                                   <a data-toggle="tab" href="javascript:void(0)" onclick="window.self.location='/userInfo/aboutUserInfoPage?sid=${param.sid }'">工作动态</a>
                                </li>
                                <li>
                                    <a data-toggle="tab" href="javascript:void(0)" onclick="window.self.location='/attention/listpagedAtten?sid=${param.sid}&pager.pageSize=15'">关注动态</a>
                                </li>
                                <li class="active">
									<a data-toggle="tab" href="javascript:void(0);" onclick="window.self.location='/jiFen/jiFenTabPage?sid=${param.sid}&viewType=341'">积分与等级</a>
								</li>
								<li>
									<a data-toggle="tab" href="javascript:void(0);" id="useStaticTab">使用统计</a>
								</li>
                                <li>
                                    <a data-toggle="tab" href="javascript:void(0)" onclick="window.self.location='/systemLog/listPagedSelfSysLog?sid=${param.sid}&pager.pageSize=15'">个人系统日志</a>
                                </li>
                            </ul>
                        </header>
			
                        <div class="panel-body">
                           <div class="tab-content">
								<iframe id="userCenter" style="width: 100%;" frameborder="0" scrolling="no"></iframe>
							</div>
                        </div>
                    </section>
				</div>
			</div>
		</div>
	</div>
</section>
</section>
<!--main content end-->
<!--right sidebar start-->
<div class="right-sidebar">
	<div class="ws-transactor">
		<div class="ws-bigheadImg">
   			<img src="/downLoad/userImg/${userInfo.comId}/${userInfo.id}?sid=${param.sid}&size=1"></img>
		</div>
		<div class="ws-personalNews">
			<p>姓名：${userInfo.userName}</p>
			<p>部门：${userInfo.depName}</p>
			<p>移动电话：${userInfo.movePhone}</p>
			<a href="/userInfo/editUserInfoPage?sid=${param.sid}" class="ws-personalSet">个人设置</a>
		</div>
		
	</div>
	<section class="panel">
		<header class="panel-heading">
			其他信息						
			<span class="tools pull-right">
               <a href="javascript:;" class="fa fa-chevron-down"></a>
            </span>
		</header>
		<div class="panel-body">
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>
			<div class="online-list"><i class="fa fa-volume-down"></i>待办任务<span class="ws-color">+3</span></div>						
		</div>
	</section>
</div>
<!--right sidebar end-->
</section>
</body>
</html>

